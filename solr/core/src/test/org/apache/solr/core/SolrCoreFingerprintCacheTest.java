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
import org.apache.lucene.util.Bits;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.update.IndexFingerprint;
import org.apache.solr.util.RefCounted;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SolrCoreFingerprintCacheTest extends SolrTestCaseJ4 {

  private static final String SEGMENT_FINGERPRINT_ATTRIBUTE = "solr.indexFingerprint";
  private static final String FINGERPRINT_SCHEMA =
      hasPersistedFingerprintFormat() ? "schema-fingerprint-docvaluesformat.xml" : "schema.xml";

  @BeforeClass
  public static void beforeTests() throws Exception {
    initCore("solrconfig-nomergepolicyfactory.xml", FINGERPRINT_SCHEMA);
  }

  @Before
  public void resetFingerprintTestIndex() throws Exception {
    // This class shares one core across test methods. Remove earlier segments so cache sizes and
    // update counts describe only the segment under test.
    SolrCore core = h.getCore();
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().deleteAll();
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));
    h.getCoreContainer().reload(core.getName());
  }

  private static boolean hasPersistedFingerprintFormat() {
    try {
      Class.forName(
          "org.apache.solr.update.IndexFingerprintDocValuesFormat",
          false,
          SolrCoreFingerprintCacheTest.class.getClassLoader());
      return true;
    } catch (ClassNotFoundException e) {
      // The fail-before proof overlays src/test but not src/test-files on its Phase 2 base.
      return false;
    }
  }

  @Test
  public void testGrossFingerprintCacheHitAfterDelete() throws Exception {
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

    core.withSearcher(searcher -> searcher.getIndexFingerprint(Long.MAX_VALUE));

    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().deleteDocuments(new Term("id", "4"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    long hitsBeforeDeleteFingerprint = core.getPerSegmentGrossFingerprintCacheHitCount();
    long incrementalUpdatesBefore = core.getIncrementalFingerprintUpdateCount();
    core.withSearcher(
        searcher -> {
          assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
          return null;
        });

    assertTrue(
        "Expected the deleted segment to reuse its gross fingerprint",
        core.getPerSegmentGrossFingerprintCacheHitCount() > hitsBeforeDeleteFingerprint);
    assertEquals(
        "The first non-maximum deletion should update the cached live fingerprint",
        incrementalUpdatesBefore + 1,
        core.getIncrementalFingerprintUpdateCount());

    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().deleteDocuments(new Term("id", "3"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    incrementalUpdatesBefore = core.getIncrementalFingerprintUpdateCount();
    core.withSearcher(
        searcher -> {
          assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
          return null;
        });
    assertEquals(
        "A later non-maximum deletion should subtract only its new version",
        incrementalUpdatesBefore + 1,
        core.getIncrementalFingerprintUpdateCount());

    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().deleteDocuments(new Term("id", "8"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    incrementalUpdatesBefore = core.getIncrementalFingerprintUpdateCount();
    core.withSearcher(
        searcher -> {
          assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
          return null;
        });
    assertEquals(
        "Deleting a cached maximum must use the gross rebuild path",
        incrementalUpdatesBefore,
        core.getIncrementalFingerprintUpdateCount());

    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().deleteDocuments(new Term("id", "5"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    incrementalUpdatesBefore = core.getIncrementalFingerprintUpdateCount();
    core.withSearcher(
        searcher -> {
          assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
          return null;
        });
    assertEquals(
        "A gross rebuild should refresh the deletion snapshot for later incremental updates",
        incrementalUpdatesBefore + 1,
        core.getIncrementalFingerprintUpdateCount());

    long grossCacheSizeBeforeColdSegment = core.getPerSegmentGrossFingerprintCacheSize();
    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 1; i <= 8; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", "cold-" + i, Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i + 100));
        writer.addDocument(doc);
      }
      writer.flush();
      writer.deleteDocuments(new Term("id", "cold-8"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    core.withSearcher(searcher -> searcher.getIndexFingerprint(Long.MAX_VALUE));

    assertTrue(
        "Expected a cold deleted segment to load its persisted gross fingerprint",
        core.getPerSegmentGrossFingerprintCacheSize() > grossCacheSizeBeforeColdSegment);
  }

  @Test
  public void testIncrementalFingerprintRejectsRestoredDocument() throws Exception {
    SolrCore core = h.getCore();
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 1; i <= 3; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", "restored-" + i, Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i + 400));
        writer.addDocument(doc);
      }
      writer.flush();
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    core.withSearcher(
        searcher -> {
          var currentContext = searcher.getTopReaderContext().leaves().get(0);
          LeafReader currentReader = currentContext.reader();
          Bits currentLiveDocs = currentReader.getLiveDocs();
          Bits previousLiveDocs =
              new Bits() {
                @Override
                public boolean get(int index) {
                  return index != 0 && (currentLiveDocs == null || currentLiveDocs.get(index));
                }

                @Override
                public int length() {
                  return currentReader.maxDoc();
                }
              };
          LeafReader previousReader =
              new FilterLeafReader(currentReader) {
                @Override
                public Bits getLiveDocs() {
                  return previousLiveDocs;
                }

                @Override
                public int numDocs() {
                  return currentReader.numDocs() - 1;
                }

                @Override
                public CacheHelper getReaderCacheHelper() {
                  return null;
                }

                @Override
                public CacheHelper getCoreCacheHelper() {
                  return null;
                }
              };
          IndexFingerprint previousFingerprint =
              IndexFingerprint.getFingerprint(
                  searcher, previousReader.getContext(), Long.MAX_VALUE);

          assertNull(
              "The incremental path must reject a snapshot that restores a deleted document",
              IndexFingerprint.getFingerprintFromPrevious(
                  previousFingerprint, new int[] {0}, searcher, currentContext, Long.MAX_VALUE));
          return null;
        });
  }

  @Test
  public void testIncrementalFingerprintFallsBackWhenDeletedSnapshotExceedsLimit()
      throws Exception {
    SolrCore core = h.getCore();
    int deletedDocLimit = 4096;
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 1; i <= deletedDocLimit + 2; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", "limit-" + i, Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i));
        writer.addDocument(doc);
      }
      writer.flush();
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));
    core.withSearcher(searcher -> searcher.getIndexFingerprint(Long.MAX_VALUE));

    Term[] deleteTerms = new Term[deletedDocLimit + 1];
    for (int i = 1; i <= deletedDocLimit + 1; i++) {
      deleteTerms[i - 1] = new Term("id", "limit-" + i);
    }
    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().deleteDocuments(deleteTerms);
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    long cacheHitsBefore = core.getPerSegmentGrossFingerprintCacheHitCount();
    long incrementalUpdatesBefore = core.getIncrementalFingerprintUpdateCount();
    core.withSearcher(
        searcher -> {
          assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
          return null;
        });
    assertTrue(
        "An over-limit deletion snapshot should fall back to the cached gross fingerprint",
        core.getPerSegmentGrossFingerprintCacheHitCount() > cacheHitsBefore);
    assertEquals(
        "An over-limit deletion snapshot must not use the incremental update path",
        incrementalUpdatesBefore,
        core.getIncrementalFingerprintUpdateCount());
  }

  @Test
  public void testPersistedGrossFingerprintSurvivesCoreReloadAndDeletes() throws Exception {
    SolrCore core = h.getCore();
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 1; i <= 8; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", "persisted-" + i, Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i + 100));
        writer.addDocument(doc);
      }
      writer.flush();
      writer.deleteDocuments(new Term("id", "persisted-4"), new Term("id", "persisted-8"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    h.getCoreContainer().reload(core.getName());
    core = h.getCore();
    assertEquals(
        "A reloaded core must start with an empty gross fingerprint cache",
        0,
        core.getPerSegmentGrossFingerprintCacheSize());

    RefCounted<SolrIndexSearcher> searcherRef = core.getSearcher();
    try {
      SolrIndexSearcher searcher = searcherRef.get();
      assertEquals(1, searcher.getTopReaderContext().leaves().size());
      SegmentReader segmentReader =
          getSegmentReader(searcher.getTopReaderContext().leaves().get(0).reader());
      assertEquals(8, segmentReader.maxDoc());
      assertEquals(6, segmentReader.numDocs());
      assertNotNull(
          "A committed segment must retain its persisted gross fingerprint",
          segmentReader.getSegmentInfo().info.getAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE));

      assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
      assertEquals(
          "The cold deleted segment should load its persisted gross summary",
          1,
          core.getPerSegmentGrossFingerprintCacheSize());
    } finally {
      searcherRef.decref();
    }
  }

  @Test
  public void testStalePersistedGrossFingerprintIsRejectedAfterDocValuesUpdate() throws Exception {
    SolrCore core = h.getCore();
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 1; i <= 4; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", "updated-" + i, Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i));
        writer.addDocument(doc);
      }
      writer.flush();
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));
    core.withSearcher(searcher -> searcher.getIndexFingerprint(Long.MAX_VALUE));

    String persistedMetadata;
    RefCounted<SolrIndexSearcher> searcherRef = core.getSearcher();
    try {
      SolrIndexSearcher searcher = searcherRef.get();
      SegmentReader segmentReader =
          getSegmentReader(searcher.getTopReaderContext().leaves().get(0).reader());
      persistedMetadata =
          segmentReader.getSegmentInfo().info.getAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE);
      assertNotNull("The test requires a persisted pre-update summary", persistedMetadata);
    } finally {
      searcherRef.decref();
    }

    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef.get().deleteDocuments(new Term("id", "updated-1"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    long incrementalUpdatesBeforeDelete = core.getIncrementalFingerprintUpdateCount();
    core.withSearcher(
        searcher -> {
          assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
          return null;
        });
    assertEquals(
        "The non-maximum deletion should populate the cached live snapshot",
        incrementalUpdatesBeforeDelete + 1,
        core.getIncrementalFingerprintUpdateCount());

    iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      iwRef
          .get()
          .updateDocValues(
              new Term("id", "updated-2"), new NumericDocValuesField("_version_", 1000));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    long incrementalUpdatesBefore = core.getIncrementalFingerprintUpdateCount();
    core.withSearcher(
        searcher -> {
          assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
          return null;
        });
    assertEquals(
        "A DocValues generation change must rebuild instead of applying the old live summary",
        incrementalUpdatesBefore,
        core.getIncrementalFingerprintUpdateCount());

    h.getCoreContainer().reload(core.getName());
    core = h.getCore();
    assertEquals(0, core.getPerSegmentGrossFingerprintCacheSize());
    RefCounted<SolrIndexSearcher> reloadedSearcherRef = core.getSearcher();
    try {
      SolrIndexSearcher searcher = reloadedSearcherRef.get();
      SegmentReader segmentReader =
          getSegmentReader(searcher.getTopReaderContext().leaves().get(0).reader());
      assertTrue(
          "An in-place DocValues update must advance the segment generation",
          segmentReader.getSegmentInfo().getDocValuesGen() != -1);
      assertEquals(
          "The old persisted summary is retained so generation validation is exercised",
          persistedMetadata,
          segmentReader.getSegmentInfo().info.getAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE));

      // The old summary has the wrong version hash and maximum. Accepting it would disagree with
      // this uncached live-doc scan.
      assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
    } finally {
      reloadedSearcherRef.decref();
    }
  }

  @Test
  public void testMalformedPersistedGrossFingerprintFallsBackToLiveScan() throws Exception {
    SolrCore core = h.getCore();
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 1; i <= 5; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", "malformed-" + i, Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i + 200));
        writer.addDocument(doc);
      }
      writer.flush();
      writer.deleteDocuments(new Term("id", "malformed-2"), new Term("id", "malformed-5"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    h.getCoreContainer().reload(core.getName());
    core = h.getCore();
    assertEquals(0, core.getPerSegmentGrossFingerprintCacheSize());
    RefCounted<SolrIndexSearcher> searcherRef = core.getSearcher();
    try {
      SolrIndexSearcher searcher = searcherRef.get();
      SegmentReader segmentReader =
          getSegmentReader(searcher.getTopReaderContext().leaves().get(0).reader());
      assertNotNull(
          "The test requires an attribute to corrupt",
          segmentReader.getSegmentInfo().info.getAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE));
      String persistedMetadata =
          segmentReader.getSegmentInfo().info.getAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE);
      segmentReader.getSegmentInfo().info.putAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE, "malformed");

      try {
        assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
        assertEquals(
            "A malformed summary must fall back to a live scan instead of warming a gross entry",
            0,
            core.getPerSegmentGrossFingerprintCacheSize());
      } finally {
        segmentReader
            .getSegmentInfo()
            .info
            .putAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE, persistedMetadata);
      }
    } finally {
      searcherRef.decref();
    }
  }

  @Test
  public void testSchemaMismatchedPersistedGrossFingerprintFallsBackToLiveScan() throws Exception {
    SolrCore core = h.getCore();
    RefCounted<IndexWriter> iwRef = core.getSolrCoreState().getIndexWriter(core);
    try {
      IndexWriter writer = iwRef.get();
      for (int i = 1; i <= 5; i++) {
        Document doc = new Document();
        doc.add(new StringField("id", "schema-mismatch-" + i, Field.Store.YES));
        doc.add(new NumericDocValuesField("_version_", i + 300));
        writer.addDocument(doc);
      }
      writer.flush();
      writer.deleteDocuments(
          new Term("id", "schema-mismatch-2"), new Term("id", "schema-mismatch-5"));
    } finally {
      iwRef.decref();
    }
    assertU(commit("openSearcher", "true"));

    h.getCoreContainer().reload(core.getName());
    core = h.getCore();
    assertEquals(0, core.getPerSegmentGrossFingerprintCacheSize());
    RefCounted<SolrIndexSearcher> searcherRef = core.getSearcher();
    try {
      SolrIndexSearcher searcher = searcherRef.get();
      SegmentReader segmentReader =
          getSegmentReader(searcher.getTopReaderContext().leaves().get(0).reader());
      String persistedMetadata =
          segmentReader.getSegmentInfo().info.getAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE);
      assertNotNull("The test requires a persisted summary", persistedMetadata);
      String[] metadataParts = persistedMetadata.split("\\|", -1);
      assertEquals(9, metadataParts.length);
      metadataParts[2] = "incompatible-schema-signature";
      String incompatibleMetadata = String.join("|", metadataParts);
      segmentReader
          .getSegmentInfo()
          .info
          .putAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE, incompatibleMetadata);

      try {
        assertCachedFingerprintMatchesUncached(searcher, Long.MAX_VALUE);
        assertEquals(
            "A schema-incompatible summary must fall back to a live scan",
            0,
            core.getPerSegmentGrossFingerprintCacheSize());
      } finally {
        segmentReader
            .getSegmentInfo()
            .info
            .putAttribute(SEGMENT_FINGERPRINT_ATTRIBUTE, persistedMetadata);
      }
    } finally {
      searcherRef.decref();
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
