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
import org.apache.lucene.search.FieldExistsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.RankField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.DocSet;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QueryLimits;
import org.apache.solr.search.QueryLimitsExceededException;
import org.apache.solr.search.QueryUtils;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.TimeAllowedLimit;
import org.apache.solr.search.WrappedQuery;
import org.apache.solr.security.AuthorizationContext;
import org.apache.solr.security.PermissionNameProvider.Name;

/** Reports field names and available document counts for one core's live index. */
public class DiscoveryFieldsRequestHandler extends RequestHandlerBase {

  private static final int MAX_FIELDS = 256;
  private static final long MAX_TIME_ALLOWED_MS = 5000;
  private static final Semaphore DISCOVERY_PERMIT = new Semaphore(1);

  private record FieldCount(Integer count, String accuracy, String source) {}

  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    if (req.getCore().getCoreDescriptor().getCloudDescriptor() != null
        && req.getParams().getBool("distrib", true)) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "Discovery is local to one core. Set distrib=false to request local results explicitly.");
    }
    long timeAllowed = req.getParams().getLong(CommonParams.TIME_ALLOWED, -1);
    if (timeAllowed <= 0
        || timeAllowed > MAX_TIME_ALLOWED_MS
        || QueryLimits.getCurrentLimits().currentLimitValueFor(TimeAllowedLimit.class).isEmpty()) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "Supply timeAllowed between 1 and " + MAX_TIME_ALLOWED_MS + " milliseconds.");
    }
    if (!DISCOVERY_PERMIT.tryAcquire()) {
      throw new SolrException(
          SolrException.ErrorCode.TOO_MANY_REQUESTS, "Another discovery request is running.");
    }
    try {
      discover(req, rsp);
    } finally {
      DISCOVERY_PERMIT.release();
    }
  }

  private void discover(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    SolrIndexSearcher searcher = req.getSearcher();
    IndexSchema schema = searcher.getSchema();

    List<Query> domainQueries = new ArrayList<>();
    String queryString = req.getParams().get(CommonParams.Q, "*:*").trim();
    domainQueries.add(QParser.getParser(queryString, req).getQuery());
    domainQueries.addAll(QueryUtils.parseFilterQueries(req));
    DocSet domain = searcher.getDocSet(domainQueries);

    TreeSet<String> fieldNames = new TreeSet<>();
    for (FieldInfo fieldInfo : searcher.getFieldInfos()) {
      if (!RankField.INTERNAL_RANK_FIELD_NAME.equals(fieldInfo.name)) {
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
        if (field != null && field.getType() instanceof RankField) {
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
      FieldCount fieldCount = countField(fieldName, schemaField, domain, searcher, existenceParser);

      if (fieldCount.count() != null && fieldCount.count() == 0) {
        continue;
      }

      SimpleOrderedMap<Object> field = new SimpleOrderedMap<>();
      field.add("type", schemaField == null ? null : schemaField.getType().getTypeName());
      if (schemaField != null
          && schema.isDynamicField(fieldName)
          && schema.getDynamicPattern(fieldName) != null) {
        field.add("dynamicBase", schema.getDynamicPattern(fieldName));
      }
      field.add("docCount", fieldCount.count());
      field.add("countAccuracy", fieldCount.accuracy());
      field.add("countSource", fieldCount.source());
      fields.add(fieldName, field);
    }

    rsp.add("scope", "local-core");
    rsp.add("countDomain", "q+fq");
    rsp.add("countsUseLiveDocuments", true);
    rsp.add("fieldNameScope", "index-fields-and-rank-fields");
    rsp.add("numFound", domain.size());
    rsp.add("numFoundAccuracy", "exact");
    rsp.add("fields", fields);
    rsp.setHttpCaching(false);
  }

  private static void checkFieldLimit(TreeSet<String> fieldNames) {
    if (fieldNames.size() > MAX_FIELDS) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "Discovery is limited to " + MAX_FIELDS + " fields per core.");
    }
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

  private static FieldCount countField(
      String fieldName,
      SchemaField schemaField,
      DocSet domain,
      SolrIndexSearcher searcher,
      QParser existenceParser)
      throws IOException {
    if (schemaField != null && (schemaField.indexed() || schemaField.hasDocValues())) {
      Query existsQuery = schemaField.getType().getExistenceQuery(existenceParser, schemaField);
      return new FieldCount(
          countQuery(existsQuery, domain, searcher), "exact", "field-type-query");
    }

    FieldInfo fieldInfo = searcher.getFieldInfos().fieldInfo(fieldName);
    if (fieldInfo.getDocValuesType() != DocValuesType.NONE
        || fieldInfo.hasNorms()
        || fieldInfo.getVectorDimension() > 0) {
      Query existsQuery = new FieldExistsQuery(fieldName);
      return new FieldCount(
          countQuery(existsQuery, domain, searcher), "exact", "field-exists-query");
    }

    Terms terms = searcher.getSlowAtomicReader().terms(fieldName);
    if (terms != null) {
      Query existsQuery = TermRangeQuery.newStringRange(fieldName, null, null, true, true);
      return new FieldCount(
          countQuery(existsQuery, domain, searcher), "exact", "term-range-query");
    }

    if (fieldInfo.getPointDimensionCount() > 0) {
      return new FieldCount(
          countPointFieldDocuments(fieldName, domain, searcher), "exact", "point-values-scan");
    }

    return new FieldCount(null, "unavailable", "no-supported-index-presence-data");
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

  @Override
  public String getDescription() {
    return "Field names and available document counts for the local core.";
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
