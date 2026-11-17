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
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PointValues;
import org.apache.lucene.index.Terms;
import org.apache.lucene.search.FieldExistsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.DocSet;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QueryUtils;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.security.AuthorizationContext;
import org.apache.solr.security.PermissionNameProvider.Name;

/** Reports field names and available document counts for one core's live index. */
public class DiscoveryFieldsRequestHandler extends RequestHandlerBase {

  private record FieldCount(Integer count, String accuracy, String source) {}

  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    SolrIndexSearcher searcher = req.getSearcher();
    IndexSchema schema = searcher.getSchema();

    List<Query> domainQueries = new ArrayList<>();
    String queryString = req.getParams().get(CommonParams.Q, "*:*").trim();
    domainQueries.add(QParser.getParser(queryString, req).getQuery());
    domainQueries.addAll(QueryUtils.parseFilterQueries(req));
    DocSet domain = searcher.getDocSet(domainQueries);

    TreeSet<String> fieldNames = new TreeSet<>();
    for (FieldInfo fieldInfo : searcher.getFieldInfos()) {
      fieldNames.add(fieldInfo.name);
    }

    SimpleOrderedMap<Object> fields = new SimpleOrderedMap<>();
    QParser existenceParser = QParser.getParser("*:*", req);
    for (String fieldName : fieldNames) {
      SchemaField schemaField = schema.getFieldOrNull(fieldName);
      FieldCount fieldCount = countField(fieldName, schemaField, domain, searcher, existenceParser);

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
    rsp.add("numFound", domain.size());
    rsp.add("numFoundAccuracy", "exact");
    rsp.add("fields", fields);
    rsp.setHttpCaching(false);
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
          domain.intersectionSize(searcher.getDocSet(existsQuery)), "exact", "field-type-query");
    }

    FieldInfo fieldInfo = searcher.getFieldInfos().fieldInfo(fieldName);
    if (fieldInfo.getDocValuesType() != DocValuesType.NONE
        || fieldInfo.hasNorms()
        || fieldInfo.getVectorDimension() > 0) {
      Query existsQuery = new FieldExistsQuery(fieldName);
      return new FieldCount(
          domain.intersectionSize(searcher.getDocSet(existsQuery)), "exact", "field-exists-query");
    }

    Terms terms = searcher.getSlowAtomicReader().terms(fieldName);
    if (terms != null) {
      Query existsQuery = TermRangeQuery.newStringRange(fieldName, null, null, true, true);
      return new FieldCount(
          domain.intersectionSize(searcher.getDocSet(existsQuery)), "exact", "term-range-query");
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
            private void countIfInDomain(int docID) {
              if (domain.exists(context.docBase + docID) && !countedDocs.get(docID)) {
                countedDocs.set(docID);
                leafCount[0]++;
              }
            }

            @Override
            public void visit(int docID) {
              countIfInDomain(docID);
            }

            @Override
            public void visit(int docID, byte[] packedValue) {
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
