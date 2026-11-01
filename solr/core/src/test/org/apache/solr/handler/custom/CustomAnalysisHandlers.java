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
package org.apache.solr.handler.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.DocumentAnalysisRequestHandler;
import org.apache.solr.handler.FieldAnalysisRequestHandler;
import org.apache.solr.schema.FieldType;

/** Outside-package handler subclasses using analyzers owned by the calling test. */
public final class CustomAnalysisHandlers {
  private CustomAnalysisHandlers() {}

  /** Records per-value dispatch while delegating analysis to the field handler. */
  public static class FieldHandler extends FieldAnalysisRequestHandler {
    private final Analyzer indexAnalyzer;
    private final Analyzer queryAnalyzer;
    private final List<String> analyzedFieldNames = new ArrayList<>();

    public FieldHandler(Analyzer indexAnalyzer, Analyzer queryAnalyzer) {
      this.indexAnalyzer = indexAnalyzer;
      this.queryAnalyzer = queryAnalyzer;
    }

    @Override
    protected Analyzer getIndexAnalyzer(FieldType fieldType) {
      return indexAnalyzer;
    }

    @Override
    protected Analyzer getQueryAnalyzer(FieldType fieldType) {
      return queryAnalyzer;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected NamedList<NamedList> analyzeValues(
        FieldAnalysisRequest request, FieldType fieldType, String fieldName) {
      analyzedFieldNames.add(fieldName);
      return super.analyzeValues(request, fieldType, fieldName);
    }

    public List<String> getAnalyzedFieldNames() {
      return Collections.unmodifiableList(analyzedFieldNames);
    }
  }

  /** Uses the same analyzer extension points through the document handler. */
  public static class DocumentHandler extends DocumentAnalysisRequestHandler {
    private final Analyzer indexAnalyzer;
    private final Analyzer queryAnalyzer;

    public DocumentHandler(Analyzer indexAnalyzer, Analyzer queryAnalyzer) {
      this.indexAnalyzer = indexAnalyzer;
      this.queryAnalyzer = queryAnalyzer;
    }

    @Override
    protected Analyzer getIndexAnalyzer(FieldType fieldType) {
      return indexAnalyzer;
    }

    @Override
    protected Analyzer getQueryAnalyzer(FieldType fieldType) {
      return queryAnalyzer;
    }
  }
}
