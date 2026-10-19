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
package org.apache.solr.update;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.FilterLeafReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.Term;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.core.SolrCore;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;
import org.junit.Test;

public class SolrIndexFingerprintTest extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeTests() throws Exception {
    initCore("solrconfig-nomergepolicyfactory.xml", "schema.xml");
  }

  @Test
  public void testSequentialVsParallelFingerprint() throws Exception {
    long maxVersion = Long.MAX_VALUE;
    SolrCore core = h.getCore();

    int numDocs = RANDOM_MULTIPLIER == 1 ? 3 : 500;
    // Create a set of many segments (to catch race conditions, i.e. SOLR-17863).
    // Write directly to the IndexWriter and flush after each doc to create one segment per doc,
    // avoiding the overhead of opening a new searcher on every Solr commit.
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 0; i < numDocs; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", Integer.toString(i), Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i + 1));
        writer.addDocument(doc);
        writer.flush();
      }
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    try (var searcher = core.getSearcher().get()) {
      // Compute fingerprint sequentially to compare with parallel computation
      IndexFingerprint expectedFingerprint =
          searcher.getTopReaderContext().leaves().stream()
              .map(
                  ctx -> {
                    try {
                      LeafReader noCacheLeafReader =
                          new FilterLeafReader(ctx.reader()) {
                            @Override
                            public CacheHelper getReaderCacheHelper() {
                              return null;
                            }

                            @Override
                            public CacheHelper getCoreCacheHelper() {
                              return null;
                            }
                          };
                      return core.getIndexFingerprint(
                          searcher, noCacheLeafReader.getContext(), maxVersion);
                    } catch (IOException e) {
                      throw new RuntimeException(e);
                    }
                  })
              .reduce(new IndexFingerprint(maxVersion), IndexFingerprint::reduce);

      IndexFingerprint actualFingerprint = searcher.getIndexFingerprint(maxVersion);
      assertEquals(expectedFingerprint, actualFingerprint);
    }
  }

  @Test
  public void testGrossFingerprintCacheAccountsForDeletesAndDocValuesUpdates() throws Exception {
    SolrCore core = h.getCore();
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 1; i <= 8; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", Integer.toString(i), Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i));
        writer.addDocument(doc);
      }
      writer.flush();
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));
    assertCachedFingerprintMatchesUncached(core, Long.MAX_VALUE);

    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().deleteDocuments(new Term("id", "8"), new Term("id", "4"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));
    // Deleting the maximum version exercises the non-invertible maxima fallback. The other
    // deletion exercises checksum subtraction from the cached gross fingerprint.
    assertCachedFingerprintMatchesUncached(core, Long.MAX_VALUE);

    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().updateDocValues(new Term("id", "1"), new NumericDocValuesField("_version_", 100));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));
    // An in-place DocValues update advances the segment DocValues generation. The gross entry
    // must be rebuilt before it is reused.
    assertCachedFingerprintMatchesUncached(core, Long.MAX_VALUE);
    assertCachedFingerprintMatchesUncached(core, 50);
  }

  private void assertCachedFingerprintMatchesUncached(SolrCore core, long maxVersion)
      throws Exception {
    try (var searcher = core.getSearcher().get()) {
      IndexFingerprint expectedFingerprint = new IndexFingerprint(maxVersion);
      for (var ctx : searcher.getTopReaderContext().leaves()) {
        LeafReader noCacheLeafReader =
            new FilterLeafReader(ctx.reader()) {
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
}
