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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PointValues;
import org.apache.lucene.index.StoredFieldVisitor;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.search.DocIdSetIterator;
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

/** Reports populated fields and their document counts for one core's live index. */
public class DiscoveryFieldsRequestHandler extends RequestHandlerBase {

  private record FieldCount(int count, String source) {}

  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    SolrIndexSearcher searcher = req.getSearcher();
    IndexSchema schema = req.getSchema();

    List<Query> domainQueries = new ArrayList<>();
    String queryString = req.getParams().get(CommonParams.Q, "*:*").trim();
    domainQueries.add(QParser.getParser(queryString, req).getQuery());
    domainQueries.addAll(QueryUtils.parseFilterQueries(req));
    DocSet domain = searcher.getDocSet(domainQueries);

    TreeSet<String> fieldNames = new TreeSet<>();
    for (FieldInfo fieldInfo : searcher.getFieldInfos()) {
      fieldNames.add(fieldInfo.name);
    }

    boolean needsStoredFieldScan = false;
    for (String fieldName : fieldNames) {
      FieldInfo fieldInfo = searcher.getFieldInfos().fieldInfo(fieldName);
      SchemaField schemaField = schema.getFieldOrNull(fieldName);
      if (!hasQueryableRepresentation(fieldInfo, schemaField, searcher)) {
        needsStoredFieldScan = true;
        break;
      }
    }
    Map<String, Integer> storedFieldCounts =
        needsStoredFieldScan ? countStoredFields(domain, searcher) : Map.of();

    SimpleOrderedMap<Object> fields = new SimpleOrderedMap<>();
    QParser existenceParser = QParser.getParser("*:*", req);
    for (String fieldName : fieldNames) {
      SchemaField schemaField = schema.getFieldOrNull(fieldName);
      FieldCount fieldCount =
          countField(fieldName, schemaField, domain, storedFieldCounts, searcher, existenceParser);

      SimpleOrderedMap<Object> field = new SimpleOrderedMap<>();
      field.add("type", schemaField == null ? null : schemaField.getType().getTypeName());
      if (schemaField != null
          && schema.isDynamicField(fieldName)
          && schema.getDynamicPattern(fieldName) != null) {
        field.add("dynamicBase", schema.getDynamicPattern(fieldName));
      }
      field.add("docCount", fieldCount.count());
      field.add("countSource", fieldCount.source());
      fields.add(fieldName, field);
    }

    rsp.add("scope", "local-core");
    rsp.add("numFound", domain.size());
    rsp.add("fields", fields);
    rsp.setHttpCaching(false);
  }

  private static boolean hasQueryableRepresentation(
      FieldInfo fieldInfo, SchemaField schemaField, SolrIndexSearcher searcher) throws IOException {
    if (schemaField != null && (schemaField.indexed() || schemaField.hasDocValues())) {
      return true;
    }
    if (fieldInfo.getDocValuesType() != DocValuesType.NONE
        || fieldInfo.hasNorms()
        || fieldInfo.getVectorDimension() > 0
        || fieldInfo.getIndexOptions() != IndexOptions.NONE
        || fieldInfo.getPointDimensionCount() > 0) {
      return true;
    }
    return searcher.getSlowAtomicReader().terms(fieldInfo.name) != null;
  }

  private static FieldCount countField(
      String fieldName,
      SchemaField schemaField,
      DocSet domain,
      Map<String, Integer> storedFieldCounts,
      SolrIndexSearcher searcher,
      QParser existenceParser)
      throws IOException {
    if (schemaField != null && (schemaField.indexed() || schemaField.hasDocValues())) {
      Query existsQuery = schemaField.getType().getExistenceQuery(existenceParser, schemaField);
      return new FieldCount(
          domain.intersectionSize(searcher.getDocSet(existsQuery)), "field-type-query");
    }

    FieldInfo fieldInfo = searcher.getFieldInfos().fieldInfo(fieldName);
    if (fieldInfo.getDocValuesType() != DocValuesType.NONE
        || fieldInfo.hasNorms()
        || fieldInfo.getVectorDimension() > 0) {
      Query existsQuery = new FieldExistsQuery(fieldName);
      return new FieldCount(
          domain.intersectionSize(searcher.getDocSet(existsQuery)), "field-exists-query");
    }

    Terms terms = searcher.getSlowAtomicReader().terms(fieldName);
    if (terms != null) {
      Query existsQuery = TermRangeQuery.newStringRange(fieldName, null, null, true, true);
      return new FieldCount(
          domain.intersectionSize(searcher.getDocSet(existsQuery)), "term-range-query");
    }

    if (fieldInfo.getPointDimensionCount() > 0) {
      return new FieldCount(
          countPointFieldDocuments(fieldName, domain, searcher), "point-values-scan");
    }

    return new FieldCount(storedFieldCounts.getOrDefault(fieldName, 0), "stored-field-scan");
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

  private static Map<String, Integer> countStoredFields(DocSet domain, SolrIndexSearcher searcher)
      throws IOException {
    Map<String, Integer> counts = new HashMap<>();
    for (LeafReaderContext context : searcher.getIndexReader().leaves()) {
      DocIdSetIterator matchingDocs = domain.iterator(context);
      if (matchingDocs == null) {
        continue;
      }
      StoredFields storedFields = context.reader().storedFields();
      for (int docID = matchingDocs.nextDoc();
          docID != DocIdSetIterator.NO_MORE_DOCS;
          docID = matchingDocs.nextDoc()) {
        Set<String> namesInDocument = new HashSet<>();
        storedFields.document(
            docID,
            new StoredFieldVisitor() {
              @Override
              public Status needsField(FieldInfo fieldInfo) {
                namesInDocument.add(fieldInfo.name);
                return Status.YES;
              }
            });
        for (String fieldName : namesInDocument) {
          counts.merge(fieldName, 1, Integer::sum);
        }
      }
    }
    return counts;
  }

  @Override
  public String getDescription() {
    return "Populated field names and counts for the local core.";
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
