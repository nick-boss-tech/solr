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
package org.apache.solr.core;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.FilterLeafReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.index.Term;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.update.IndexFingerprint;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;
import org.junit.Test;

public class SolrCoreFingerprintMergeTest extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeTests() throws Exception {
    initCore("solrconfig-mergepolicyfactory-nocfs.xml", "schema.xml");
  }

  @Test
  public void testMergedSegmentPersistsGrossFingerprint() throws Exception {
    SolrCore core = h.getCore();
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int segment = 0; segment < 3; segment++) {
        for (int doc = 1; doc <= 4; doc++) {
          int version = segment * 4 + doc;
          Document document = new Document();
          document.add(new StringField("id", "merge-" + version, Field.Store.YES));
          document.add(new NumericDocValuesField("_version_", version));
          writer.addDocument(document);
        }
        writer.flush();
      }

      // The merge must carry summaries computed from the live input documents, not retain a
      // summary that still includes this deleted source document.
      writer.deleteDocuments(new Term("id", "merge-2"));
      writer.forceMerge(1);
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    h.getCoreContainer().reload(core.getName());
    core = h.getCore();
    assertEquals(0, core.getPerSegmentGrossFingerprintCacheSize());
    try (var searcher = core.getSearcher().get()) {
      assertEquals(1, searcher.getTopReaderContext().leaves().size());
      SegmentReader segmentReader =
          getSegmentReader(searcher.getTopReaderContext().leaves().get(0).reader());
      assertEquals(11, segmentReader.maxDoc());
      assertEquals(11, segmentReader.numDocs());
      assertNotNull(
          "The merged segment must contain a persisted gross fingerprint",
          segmentReader.getSegmentInfo().info.getAttribute("solr.indexFingerprint"));

      assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
      assertEquals(1, core.getPerSegmentGrossFingerprintCacheSize());
    }
  }

  private static SegmentReader getSegmentReader(LeafReader reader) {
    LeafReader unwrapped = FilterLeafReader.unwrap(reader);
    assertTrue(
        "Expected a segment reader but got " + unwrapped, unwrapped instanceof SegmentReader);
    return (SegmentReader) unwrapped;
  }

  private static void assertCachedFingerprintMatchesUncached(
      SolrIndexSearcher searcher, long maxVersion) throws IOException {
    IndexFingerprint expectedFingerprint = new IndexFingerprint(maxVersion);
    for (var context : searcher.getTopReaderContext().leaves()) {
      LeafReader noCacheLeafReader =
          new FilterLeafReader(context.reader()) {
            @Override
            public CacheHelper getReaderCacheHelper() {
              return null;
            }

            @Override
            public CacheHelper getCoreCacheHelper() {
              return null;
            }
          };
      expectedFingerprint =
          IndexFingerprint.reduce(
              expectedFingerprint,
              IndexFingerprint.getFingerprint(
                  searcher, noCacheLeafReader.getContext(), maxVersion));
    }
    assertEquals(expectedFingerprint, searcher.getIndexFingerprint(maxVersion));
  }
}
