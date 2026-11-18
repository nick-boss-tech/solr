/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.handler.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PointValues;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FieldExistsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.util.EnvUtils;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestInfo;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.RankField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.DocSet;
import org.apache.solr.search.ExtendedQuery;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QueryLimits;
import org.apache.solr.search.QueryLimitsExceededException;
import org.apache.solr.search.QueryUtils;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.TimeAllowedLimit;
import org.apache.solr.search.WrappedQuery;
import org.apache.solr.security.AuthorizationContext;

/** Reports field presence and optional document counts for one core's live index. */
public class DiscoveryFieldsRequestHandler extends RequestHandlerBase {

  private static final String MAX_FIELDS_PROPERTY = "solr.discovery.fields.maxFields";
  private static final String MAX_TIME_ALLOWED_PROPERTY = "solr.discovery.fields.maxTimeAllowedMs";
  private static final String MAX_CONCURRENT_REQUESTS_PROPERTY =
      "solr.discovery.fields.maxConcurrentRequests";
  private static final String USE_FILTER_CACHE_PROPERTY = "solr.discovery.fields.useFilterCache";
  private static final int MAX_FIELDS = positiveIntProperty(MAX_FIELDS_PROPERTY, 256);
  private static final int MAX_GLOB_LENGTH = 256;
  private static final int MAX_TIME_ALLOWED_MS =
      positiveIntProperty(MAX_TIME_ALLOWED_PROPERTY, 5000);
  private static final Semaphore DISCOVERY_PERMITS =
      new Semaphore(positiveIntProperty(MAX_CONCURRENT_REQUESTS_PROPERTY, 1));
  private static final boolean USE_FILTER_CACHE =
      EnvUtils.getPropertyAsBool(USE_FILTER_CACHE_PROPERTY, false);

  private static final String COUNT_MODE_PRESENCE = "presence";
  private static final String COUNT_MODE_EXACT = "exact";

  private record FieldCount(Integer count, String accuracy, String source) {}

  private record FieldPresence(String status, String source) {}

  private record FieldExistenceQuery(Query query, String source) {}

  private static final class MatchFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private MatchFoundException() {
      super(null, null, false, false);
    }
  }

  private static final MatchFoundException MATCH_FOUND = new MatchFoundException();

  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    if (req.getCore().getCoreDescriptor().getCloudDescriptor() != null
        && req.getParams().getBool("distrib", true)) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "Discovery is local to one core. Set distrib=false to request local results explicitly.");
    }
    long timeAllowed = req.getParams().getLong(CommonParams.TIME_ALLOWED, -1);
    if (timeAllowed <= 0 || timeAllowed > MAX_TIME_ALLOWED_MS) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "Supply timeAllowed between 1 and " + MAX_TIME_ALLOWED_MS + " milliseconds.");
    }
    SolrRequestInfo requestInfo = SolrRequestInfo.getRequestInfo();
    if (requestInfo == null) {
      throw new SolrException(
          SolrException.ErrorCode.SERVER_ERROR,
          "Discovery requires an active SolrRequestInfo to enforce timeAllowed.");
    }
    QueryLimits queryLimits = requestInfo.getLimits();
    if (queryLimits.currentLimitValueFor(TimeAllowedLimit.class).isEmpty()) {
      queryLimits.addLimitIfAbsent(new TimeAllowedLimit(req));
    }
    if (!DISCOVERY_PERMITS.tryAcquire()) {
      throw new SolrException(
          SolrException.ErrorCode.TOO_MANY_REQUESTS, "Discovery concurrency limit reached.");
    }
    try {
      discover(req, rsp);
    } finally {
      DISCOVERY_PERMITS.release();
    }
  }

  private void discover(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    SolrIndexSearcher searcher = req.getSearcher();
    IndexSchema schema = searcher.getSchema();
    String countMode = req.getParams().get("countMode", COUNT_MODE_EXACT);
    if (!COUNT_MODE_PRESENCE.equals(countMode) && !COUNT_MODE_EXACT.equals(countMode)) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST, "countMode must be 'presence' or 'exact'.");
    }
    String fieldGlob = req.getParams().get("fieldGlob", "*");
    if (fieldGlob.length() > MAX_GLOB_LENGTH) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "fieldGlob is limited to " + MAX_GLOB_LENGTH + " characters.");
    }

    List<Query> domainQueries = new ArrayList<>();
    String queryString = req.getParams().get(CommonParams.Q, "*:*").trim();
    if (!"*:*".equals(queryString)) {
      domainQueries.add(QParser.getParser(queryString, req).getQuery());
    }
    domainQueries.addAll(QueryUtils.parseFilterQueries(req));
    if (!USE_FILTER_CACHE) {
      domainQueries.replaceAll(DiscoveryFieldsRequestHandler::disableCache);
    }
    DocSet domain = searcher.getDocSet(domainQueries);

    TreeSet<String> fieldNames = new TreeSet<>();
    for (FieldInfo fieldInfo : searcher.getFieldInfos()) {
      checkLimits();
      if (!RankField.INTERNAL_RANK_FIELD_NAME.equals(fieldInfo.name)
          && matchesFieldGlob(fieldGlob, fieldInfo.name)) {
        fieldNames.add(fieldInfo.name);
      }
      checkFieldLimit(fieldNames);
    }
    Terms rankTerms = searcher.getSlowAtomicReader().terms(RankField.INTERNAL_RANK_FIELD_NAME);
    if (rankTerms != null) {
      TermsEnum terms = rankTerms.iterator();
      BytesRef term;
      while ((term = terms.next()) != null) {
        checkLimits();
        String fieldName = term.utf8ToString();
        SchemaField field = schema.getFieldOrNull(fieldName);
        if (field != null
            && field.getType() instanceof RankField
            && matchesFieldGlob(fieldGlob, fieldName)) {
          fieldNames.add(fieldName);
          checkFieldLimit(fieldNames);
        }
      }
    }

    SimpleOrderedMap<Object> fields = new SimpleOrderedMap<>();
    QParser existenceParser = QParser.getParser("*:*", req);
    for (String fieldName : fieldNames) {
      checkLimits();
      SchemaField schemaField = schema.getFieldOrNull(fieldName);
      SimpleOrderedMap<Object> field = new SimpleOrderedMap<>();
      field.add("type", schemaField == null ? null : schemaField.getType().getTypeName());
      if (schemaField != null
          && schema.isDynamicField(fieldName)
          && schema.getDynamicPattern(fieldName) != null) {
        field.add("dynamicBase", schema.getDynamicPattern(fieldName));
      }

      if (COUNT_MODE_PRESENCE.equals(countMode)) {
        FieldPresence presence =
            fieldPresence(fieldName, schemaField, domain, searcher, existenceParser);
        field.add("presence", presence.status());
        field.add("presenceSource", presence.source());
      } else {
        FieldCount fieldCount =
            countField(fieldName, schemaField, domain, searcher, existenceParser);
        if (fieldCount.count() != null && fieldCount.count() == 0) {
          continue;
        }
        field.add("presence", fieldCount.count() == null ? "unknown" : "present");
        field.add("docCount", fieldCount.count());
        field.add("countAccuracy", fieldCount.accuracy());
        field.add("countSource", fieldCount.source());
      }
      fields.add(fieldName, field);
    }

    rsp.add("scope", "local-core");
    rsp.add("countDomain", "q+fq");
    rsp.add("countMode", countMode);
    rsp.add("domainFilterCacheEnabled", USE_FILTER_CACHE);
    rsp.add("countsUseLiveDocuments", true);
    rsp.add("fieldNameScope", "index-fields-and-rank-fields");
    rsp.add("numFound", domain.size());
    rsp.add("numFoundAccuracy", "exact");
    rsp.add("fields", fields);
    rsp.setHttpCaching(false);
  }

  private static int positiveIntProperty(String property, int defaultValue) {
    int value = EnvUtils.getPropertyAsInteger(property, defaultValue);
    if (value <= 0) {
      throw new IllegalArgumentException(property + " must be greater than zero.");
    }
    return value;
  }

  private static Query disableCache(Query query) {
    if (query instanceof ExtendedQuery extendedQuery) {
      extendedQuery.setCache(false);
      return query;
    }
    WrappedQuery uncached = new WrappedQuery(query);
    uncached.setCache(false);
    return uncached;
  }

  private static void checkFieldLimit(TreeSet<String> fieldNames) {
    if (fieldNames.size() > MAX_FIELDS) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "Discovery is limited to " + MAX_FIELDS + " fields per core.");
    }
  }

  // Avoid backtracking on request-supplied patterns; match the same * and ? glob syntax.
  private static boolean matchesFieldGlob(String pattern, String name)
      throws QueryLimitsExceededException {
    boolean[] previous = new boolean[pattern.length() + 1];
    previous[0] = true;
    for (int j = 1; j <= pattern.length(); j++) {
      previous[j] = previous[j - 1] && pattern.charAt(j - 1) == '*';
    }
    for (int i = 0; i < name.length(); i++) {
      if ((i & 127) == 0) {
        checkLimits();
      }
      boolean[] current = new boolean[pattern.length() + 1];
      for (int j = 1; j <= pattern.length(); j++) {
        char token = pattern.charAt(j - 1);
        current[j] =
            token == '*'
                ? current[j - 1] || previous[j]
                : previous[j - 1] && (token == '?' || token == name.charAt(i));
      }
      previous = current;
    }
    return previous[pattern.length()];
  }

  private static void checkLimits() throws QueryLimitsExceededException {
    QueryLimits limits = QueryLimits.getCurrentLimits();
    if (limits.shouldExit()) {
      throw new QueryLimitsExceededException(limits.formatExceptionMessage("discovery"));
    }
  }

  private static int countQuery(Query query, DocSet domain, SolrIndexSearcher searcher)
      throws IOException {
    WrappedQuery uncached = new WrappedQuery(query);
    uncached.setCache(false);
    return searcher.getDocSet(uncached, domain).size();
  }

  private static FieldExistenceQuery getFieldExistenceQuery(
      String fieldName,
      SchemaField schemaField,
      SolrIndexSearcher searcher,
      QParser existenceParser)
      throws IOException {
    if (schemaField != null && (schemaField.indexed() || schemaField.hasDocValues())) {
      return new FieldExistenceQuery(
          schemaField.getType().getExistenceQuery(existenceParser, schemaField),
          "field-type-query");
    }

    FieldInfo fieldInfo = searcher.getFieldInfos().fieldInfo(fieldName);
    if (fieldInfo.getDocValuesType() != DocValuesType.NONE
        || fieldInfo.hasNorms()
        || fieldInfo.getVectorDimension() > 0) {
      return new FieldExistenceQuery(new FieldExistsQuery(fieldName), "field-exists-query");
    }

    Terms terms = searcher.getSlowAtomicReader().terms(fieldName);
    if (terms != null) {
      return new FieldExistenceQuery(
          TermRangeQuery.newStringRange(fieldName, null, null, true, true), "term-range-query");
    }

    return null;
  }

  private static FieldCount countField(
      String fieldName,
      SchemaField schemaField,
      DocSet domain,
      SolrIndexSearcher searcher,
      QParser existenceParser)
      throws IOException {
    FieldExistenceQuery existenceQuery =
        getFieldExistenceQuery(fieldName, schemaField, searcher, existenceParser);
    if (existenceQuery != null) {
      return new FieldCount(
          countQuery(existenceQuery.query(), domain, searcher), "exact", existenceQuery.source());
    }

    FieldInfo fieldInfo = searcher.getFieldInfos().fieldInfo(fieldName);
    if (fieldInfo.getPointDimensionCount() > 0) {
      return new FieldCount(
          countPointFieldDocuments(fieldName, domain, searcher), "exact", "point-values-scan");
    }

    return new FieldCount(null, "unavailable", "no-supported-index-presence-data");
  }

  private static FieldPresence fieldPresence(
      String fieldName,
      SchemaField schemaField,
      DocSet domain,
      SolrIndexSearcher searcher,
      QParser existenceParser)
      throws IOException {
    FieldExistenceQuery existenceQuery =
        getFieldExistenceQuery(fieldName, schemaField, searcher, existenceParser);
    if (existenceQuery != null) {
      BooleanQuery query =
          new BooleanQuery.Builder()
              .add(existenceQuery.query(), Occur.MUST)
              .add(domain.makeQuery(), Occur.FILTER)
              .build();
      try {
        searcher.search(
            query,
            new SimpleCollector() {
              @Override
              public void collect(int doc) throws IOException {
                checkLimits();
                throw MATCH_FOUND;
              }

              @Override
              public ScoreMode scoreMode() {
                return ScoreMode.COMPLETE_NO_SCORES;
              }
            });
        return new FieldPresence("absent", existenceQuery.source());
      } catch (MatchFoundException matchFound) {
        return new FieldPresence("present", existenceQuery.source());
      }
    }

    FieldInfo fieldInfo = searcher.getFieldInfos().fieldInfo(fieldName);
    if (fieldInfo.getPointDimensionCount() > 0) {
      return new FieldPresence(
          hasPointFieldDocument(fieldName, domain, searcher) ? "present" : "absent",
          "point-values-first-match");
    }

    return new FieldPresence("unknown", "no-supported-index-presence-data");
  }

  private static int countPointFieldDocuments(
      String fieldName, DocSet domain, SolrIndexSearcher searcher) throws IOException {
    int count = 0;
    for (LeafReaderContext context : searcher.getIndexReader().leaves()) {
      PointValues points = context.reader().getPointValues(fieldName);
      if (points == null) {
        continue;
      }

      BitSet countedDocs = new BitSet(context.reader().maxDoc());
      int[] leafCount = new int[1];
      points.intersect(
          new PointValues.IntersectVisitor() {
            private void countIfInDomain(int docID) throws QueryLimitsExceededException {
              checkLimits();
              if (domain.exists(context.docBase + docID) && !countedDocs.get(docID)) {
                countedDocs.set(docID);
                leafCount[0]++;
              }
            }

            @Override
            public void visit(int docID) throws IOException {
              countIfInDomain(docID);
            }

            @Override
            public void visit(int docID, byte[] packedValue) throws IOException {
              countIfInDomain(docID);
            }

            @Override
            public PointValues.Relation compare(byte[] minPackedValue, byte[] maxPackedValue) {
              return PointValues.Relation.CELL_CROSSES_QUERY;
            }
          });
      count += leafCount[0];
    }
    return count;
  }

  private static boolean hasPointFieldDocument(
      String fieldName, DocSet domain, SolrIndexSearcher searcher) throws IOException {
    try {
      for (LeafReaderContext context : searcher.getIndexReader().leaves()) {
        PointValues points = context.reader().getPointValues(fieldName);
        if (points == null) {
          continue;
        }

        points.intersect(
            new PointValues.IntersectVisitor() {
              private void checkIfInDomain(int docID) throws QueryLimitsExceededException {
                checkLimits();
                if (domain.exists(context.docBase + docID)) {
                  throw MATCH_FOUND;
                }
              }

              @Override
              public void visit(int docID) throws IOException {
                checkIfInDomain(docID);
              }

              @Override
              public void visit(int docID, byte[] packedValue) throws IOException {
                checkIfInDomain(docID);
              }

              @Override
              public PointValues.Relation compare(byte[] minPackedValue, byte[] maxPackedValue) {
                return PointValues.Relation.CELL_CROSSES_QUERY;
              }
            });
      }
      return false;
    } catch (MatchFoundException matchFound) {
      return true;
    }
  }

  @Override
  public String getDescription() {
    return "Field names and presence, with optional document counts, for the local core.";
  }

  @Override
  public Category getCategory() {
    return Category.ADMIN;
  }

  @Override
  public Name getPermissionName(AuthorizationContext request) {
    return Name.READ_PERM;
  }
}
