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
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import java.util.Objects;
import org.apache.lucene.codecs.DocValuesProducer;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.util.Bits;
import org.apache.solr.common.MapWriter;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.Hash;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.LongPointField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.RTimer;
import org.apache.solr.util.RefCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @lucene.internal
 */
public class IndexFingerprint implements MapWriter {
  /** SegmentInfo attribute containing the gross fingerprint written with a new segment. */
  public static final String SEGMENT_METADATA_ATTRIBUTE = "solr.indexFingerprint";

  private static final int SEGMENT_METADATA_FORMAT_VERSION = 1;
  private static final int MAX_CACHED_DELETED_DOC_IDS = 4096;

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private long maxVersionSpecified;
  private long maxVersionEncountered;
  // this actually means max versions used in computing the hash.
  // we cannot change this now because it changes back-compat
  private long maxInHash;
  private long versionsHash;
  private long numVersions;
  private long numDocs;
  private long maxDoc;

  public IndexFingerprint() {
    // default constructor
  }

  public IndexFingerprint(long maxVersionSpecified) {
    this.maxVersionSpecified = maxVersionSpecified;
  }

  public long getMaxVersionSpecified() {
    return maxVersionSpecified;
  }

  public long getMaxVersionEncountered() {
    return maxVersionEncountered;
  }

  public long getMaxInHash() {
    return maxInHash;
  }

  public long getVersionsHash() {
    return versionsHash;
  }

  public long getNumVersions() {
    return numVersions;
  }

  public long getNumDocs() {
    return numDocs;
  }

  public long getMaxDoc() {
    return maxDoc;
  }

  /** Opens a new realtime searcher and returns it's (possibly cached) fingerprint */
  public static IndexFingerprint getFingerprint(SolrCore core, long maxVersion) throws IOException {
    RTimer timer = new RTimer();
    core.getUpdateHandler().getUpdateLog().openRealtimeSearcher();
    RefCounted<SolrIndexSearcher> newestSearcher =
        core.getUpdateHandler().getUpdateLog().uhandler.core.getRealtimeSearcher();
    try {
      IndexFingerprint f = newestSearcher.get().getIndexFingerprint(maxVersion);
      final double duration = timer.stop();
      log.info("IndexFingerprint millis:{} result:{}", duration, f);
      return f;
    } finally {
      if (newestSearcher != null) {
        newestSearcher.decref();
      }
    }
  }

  public static IndexFingerprint getFingerprint(
      SolrIndexSearcher searcher, LeafReaderContext ctx, Long maxVersion) throws IOException {
    IndexFingerprint f = new IndexFingerprint();
    f.maxVersionSpecified = maxVersion;
    f.maxDoc = ctx.reader().maxDoc();
    f.numDocs = ctx.reader().numDocs();

    int maxDoc = ctx.reader().maxDoc();
    Bits liveDocs = ctx.reader().getLiveDocs();
    FunctionValues fv = getVersionValues(searcher, ctx);
    for (int doc = 0; doc < maxDoc; doc++) {
      if (liveDocs != null && !liveDocs.get(doc)) continue;
      long v = fv.longVal(doc);
      addVersion(f, v, maxVersion);
    }

    return f;
  }

  /**
   * Computes the live fingerprint and a gross fingerprint that includes deleted documents. The
   * gross value can be retained for the segment core and adjusted for later deletions without
   * decoding the versions of live documents again.
   */
  public static FingerprintAndGross getFingerprintAndGross(
      SolrIndexSearcher searcher, LeafReaderContext ctx, long maxVersion) throws IOException {
    if (ctx.reader().numDocs() == ctx.reader().maxDoc()) {
      IndexFingerprint fingerprint = getFingerprint(searcher, ctx, maxVersion);
      return new FingerprintAndGross(fingerprint, fingerprint.copy());
    }

    IndexFingerprint live = new IndexFingerprint(maxVersion);
    IndexFingerprint gross = new IndexFingerprint(maxVersion);
    int maxDoc = ctx.reader().maxDoc();
    Bits liveDocs = ctx.reader().getLiveDocs();
    live.maxDoc = maxDoc;
    live.numDocs = ctx.reader().numDocs();
    gross.maxDoc = maxDoc;
    gross.numDocs = maxDoc;

    FunctionValues fv = getVersionValues(searcher, ctx);
    for (int doc = 0; doc < maxDoc; doc++) {
      long version = fv.longVal(doc);
      addVersion(gross, version, maxVersion);
      if (liveDocs == null || liveDocs.get(doc)) {
        addVersion(live, version, maxVersion);
      }
    }
    return new FingerprintAndGross(live, gross);
  }

  /**
   * Computes a gross fingerprint while Lucene writes the numeric _version_ DocValues for a segment.
   * Missing values have the same zero value used by the version ValueSource.
   */
  public static IndexFingerprint getGrossFingerprint(
      DocValuesProducer valuesProducer, FieldInfo fieldInfo, int maxDoc) throws IOException {
    IndexFingerprint fingerprint = new IndexFingerprint(Long.MAX_VALUE);
    fingerprint.maxDoc = maxDoc;
    fingerprint.numDocs = maxDoc;
    NumericDocValues values = valuesProducer.getNumeric(fieldInfo);
    int nextDocWithValue = values.nextDoc();
    for (int doc = 0; doc < maxDoc; doc++) {
      long version = 0L;
      if (doc == nextDocWithValue) {
        version = values.longValue();
        nextDocWithValue = values.nextDoc();
      }
      addVersion(fingerprint, version, Long.MAX_VALUE);
    }
    return fingerprint;
  }

  /**
   * Identifies the _version_ interpretation used to create a persisted summary, or returns {@code
   * null} when the field type does not have the direct numeric DocValues semantics required by the
   * write-time summary.
   */
  public static String getSegmentMetadataSchemaSignature(IndexSchema schema) {
    SchemaField versionField = VersionInfo.getAndCheckVersionField(schema);
    if (!versionField.hasDocValues() || !supportsSegmentMetadata(versionField.getType())) {
      return null;
    }
    return versionField.getName()
        + "|"
        + versionField.getType().getNamedPropertyValues(true)
        + "|"
        + versionField.getDocValuesFormat()
        + "|"
        + versionField.getProperties();
  }

  static boolean supportsSegmentMetadata(FieldType fieldType) {
    // The writer summary reads raw numeric DocValues. Only the built-in LongPointField is known to
    // expose those same values through its ValueSource; custom types keep the scan fallback.
    return fieldType.getClass() == LongPointField.class;
  }

  /**
   * Serializes the gross summary as a versioned, self-validating SegmentInfo attribute. The segment
   * id, schema signature, and DocValues generation prevent reuse across incompatible data.
   */
  public String toSegmentMetadata(SegmentInfo segmentInfo, String schemaSignature) {
    String encodedSchema =
        Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(schemaSignature.getBytes(StandardCharsets.UTF_8));
    return SEGMENT_METADATA_FORMAT_VERSION
        + "|"
        + HexFormat.of().formatHex(segmentInfo.getId())
        + "|"
        + encodedSchema
        + "|-1|"
        + maxDoc
        + "|"
        + maxVersionEncountered
        + "|"
        + maxInHash
        + "|"
        + versionsHash
        + "|"
        + numVersions;
  }

  /** Returns a persisted gross summary, or {@code null} when it is absent or stale. */
  public static IndexFingerprint getPersistedGrossFingerprint(
      SegmentReader reader, IndexSchema schema) {
    if (reader.getSegmentInfo().getDocValuesGen() != -1) {
      return null;
    }
    SegmentInfo segmentInfo = reader.getSegmentInfo().info;
    String metadata = segmentInfo.getAttribute(SEGMENT_METADATA_ATTRIBUTE);
    if (metadata == null) {
      return null;
    }
    String schemaSignature = getSegmentMetadataSchemaSignature(schema);
    if (schemaSignature == null) {
      return null;
    }
    return fromSegmentMetadata(metadata, segmentInfo, schemaSignature);
  }

  private static IndexFingerprint fromSegmentMetadata(
      String metadata, SegmentInfo segmentInfo, String schemaSignature) {
    try {
      String[] parts = metadata.split("\\|", -1);
      if (parts.length != 9
          || Integer.parseInt(parts[0]) != SEGMENT_METADATA_FORMAT_VERSION
          || !parts[1].equals(HexFormat.of().formatHex(segmentInfo.getId()))
          || !parts[2].equals(
              Base64.getUrlEncoder()
                  .withoutPadding()
                  .encodeToString(schemaSignature.getBytes(StandardCharsets.UTF_8)))
          || Long.parseLong(parts[3]) != -1) {
        return null;
      }

      long maxDoc = Long.parseLong(parts[4]);
      long maxVersionEncountered = Long.parseLong(parts[5]);
      long maxInHash = Long.parseLong(parts[6]);
      long versionsHash = Long.parseLong(parts[7]);
      long numVersions = Long.parseLong(parts[8]);
      if (maxDoc != segmentInfo.maxDoc()
          || numVersions != maxDoc
          || maxVersionEncountered != maxInHash) {
        return null;
      }

      IndexFingerprint fingerprint = new IndexFingerprint(Long.MAX_VALUE);
      fingerprint.maxDoc = maxDoc;
      fingerprint.numDocs = maxDoc;
      fingerprint.maxVersionEncountered = maxVersionEncountered;
      fingerprint.maxInHash = maxInHash;
      fingerprint.versionsHash = versionsHash;
      fingerprint.numVersions = numVersions;
      return fingerprint;
    } catch (IllegalArgumentException e) {
      // Old, malformed, or incompatible attributes must never affect the computed fingerprint.
      return null;
    }
  }

  /** Returns the live fingerprint represented by a gross per-segment fingerprint. */
  public static IndexFingerprint getFingerprintFromGross(
      IndexFingerprint gross, SolrIndexSearcher searcher, LeafReaderContext ctx, long maxVersion)
      throws IOException {
    // A gross fingerprint cannot answer a request that excludes some of its versions.
    if (gross.maxVersionEncountered > maxVersion) {
      return getFingerprint(searcher, ctx, maxVersion);
    }

    IndexFingerprint live = gross.copy();
    live.maxVersionSpecified = maxVersion;
    Bits liveDocs = ctx.reader().getLiveDocs();
    if (liveDocs == null || ctx.reader().numDocs() == gross.numDocs) {
      live.numDocs = ctx.reader().numDocs();
      return live;
    }

    FunctionValues fv = getVersionValues(searcher, ctx);
    boolean recomputeMaxima = false;
    for (int doc = 0; doc < ctx.reader().maxDoc(); doc++) {
      if (liveDocs.get(doc)) continue;
      long version = fv.longVal(doc);
      if (version <= maxVersion) {
        // The fingerprint hash is an additive 64-bit checksum, so subtraction preserves its
        // existing modulo-2^64 behavior.
        live.versionsHash -= Hash.fmix64(version);
        live.numVersions--;
      }
      if (version == gross.maxVersionEncountered || version == gross.maxInHash) {
        recomputeMaxima = true;
      }
    }
    live.numDocs = ctx.reader().numDocs();

    // Maxima are not invertible. Re-scan live versions only when a deletion removed one of the
    // cached maxima; the checksum and counts above still come from the gross summary.
    if (recomputeMaxima) {
      live.maxVersionEncountered = 0;
      live.maxInHash = 0;
      FunctionValues liveValues = getVersionValues(searcher, ctx);
      for (int doc = 0; doc < ctx.reader().maxDoc(); doc++) {
        if (!liveDocs.get(doc)) continue;
        long version = liveValues.longVal(doc);
        live.maxVersionEncountered = Math.max(version, live.maxVersionEncountered);
        if (version <= maxVersion) {
          live.maxInHash = Math.max(version, live.maxInHash);
        }
      }
    }
    return live;
  }

  /**
   * Applies only the newly deleted documents to a previous live fingerprint. Returns {@code null}
   * when the snapshots are not a monotonic delete-only transition or a deleted document could
   * change a cached maximum; callers should then rebuild from the gross fingerprint.
   */
  public static FingerprintAndDeletedDocs getFingerprintFromPrevious(
      IndexFingerprint previous,
      int[] previousDeletedDocIds,
      SolrIndexSearcher searcher,
      LeafReaderContext ctx,
      long maxVersion)
      throws IOException {
    if (previous == null
        || previousDeletedDocIds == null
        || previous.maxVersionSpecified != Long.MAX_VALUE
        || maxVersion != Long.MAX_VALUE
        || previous.maxDoc != ctx.reader().maxDoc()
        || previous.numDocs != previous.maxDoc - previousDeletedDocIds.length
        || previousDeletedDocIds.length > MAX_CACHED_DELETED_DOC_IDS) {
      return null;
    }

    long newlyDeletedCount = previous.numDocs - ctx.reader().numDocs();
    if (newlyDeletedCount < 0
        || previousDeletedDocIds.length + newlyDeletedCount > MAX_CACHED_DELETED_DOC_IDS) {
      return null;
    }

    int[] deletedDocIds = new int[previousDeletedDocIds.length + (int) newlyDeletedCount];
    IndexFingerprint live = previous.copy();
    live.maxVersionSpecified = maxVersion;
    live.numDocs = ctx.reader().numDocs();
    Bits liveDocs = ctx.reader().getLiveDocs();
    FunctionValues versionValues = null;
    int previousDeletedIndex = 0;
    int deletedDocIndex = 0;

    for (int doc = 0; doc < ctx.reader().maxDoc(); doc++) {
      if (previousDeletedIndex < previousDeletedDocIds.length
          && previousDeletedDocIds[previousDeletedIndex] < doc) {
        return null;
      }
      boolean previouslyDeleted =
          previousDeletedIndex < previousDeletedDocIds.length
              && previousDeletedDocIds[previousDeletedIndex] == doc;
      if (previouslyDeleted) previousDeletedIndex++;

      boolean currentlyLive = liveDocs == null || liveDocs.get(doc);
      if (previouslyDeleted) {
        if (currentlyLive) return null;
        deletedDocIds[deletedDocIndex++] = doc;
      } else if (!currentlyLive) {
        if (versionValues == null) {
          versionValues = getVersionValues(searcher, ctx);
        }
        long version = versionValues.longVal(doc);
        if (version == previous.maxVersionEncountered || version == previous.maxInHash) {
          return null;
        }
        if (version <= maxVersion) {
          if (live.numVersions == 0) return null;
          // Fingerprint hashes are additive 64-bit checksums, so subtraction retains their
          // existing modulo-2^64 behavior.
          live.versionsHash -= Hash.fmix64(version);
          live.numVersions--;
        }
        deletedDocIds[deletedDocIndex++] = doc;
      }
    }

    if (previousDeletedIndex != previousDeletedDocIds.length
        || deletedDocIndex != deletedDocIds.length) {
      return null;
    }
    return new FingerprintAndDeletedDocs(live, deletedDocIds);
  }

  /** Returns a bounded, sorted snapshot of deleted document IDs, or {@code null} if too large. */
  public static int[] getDeletedDocIds(LeafReaderContext ctx) {
    int deletedCount = ctx.reader().maxDoc() - ctx.reader().numDocs();
    if (deletedCount < 0 || deletedCount > MAX_CACHED_DELETED_DOC_IDS) return null;
    if (deletedCount == 0) return new int[0];

    Bits liveDocs = ctx.reader().getLiveDocs();
    if (liveDocs == null) return null;
    int[] deletedDocIds = new int[deletedCount];
    int deletedDocIndex = 0;
    for (int doc = 0; doc < ctx.reader().maxDoc(); doc++) {
      if (!liveDocs.get(doc)) {
        if (deletedDocIndex == deletedDocIds.length) return null;
        deletedDocIds[deletedDocIndex++] = doc;
      }
    }
    return deletedDocIndex == deletedDocIds.length ? deletedDocIds : null;
  }

  private static FunctionValues getVersionValues(SolrIndexSearcher searcher, LeafReaderContext ctx)
      throws IOException {
    SchemaField versionField = VersionInfo.getAndCheckVersionField(searcher.getSchema());
    ValueSource vs = versionField.getType().getValueSource(versionField, null);
    Map<Object, Object> funcContext = ValueSource.newContext(searcher);
    vs.createWeight(funcContext, searcher);
    return vs.getValues(funcContext, ctx);
  }

  private static void addVersion(IndexFingerprint f, long version, long maxVersion) {
    f.maxVersionEncountered = Math.max(version, f.maxVersionEncountered);
    if (version <= maxVersion) {
      f.maxInHash = Math.max(version, f.maxInHash);
      f.versionsHash += Hash.fmix64(version);
      f.numVersions++;
    }
  }

  private IndexFingerprint copy() {
    IndexFingerprint copy = new IndexFingerprint(maxVersionSpecified);
    copy.maxVersionEncountered = maxVersionEncountered;
    copy.maxInHash = maxInHash;
    copy.versionsHash = versionsHash;
    copy.numVersions = numVersions;
    copy.numDocs = numDocs;
    copy.maxDoc = maxDoc;
    return copy;
  }

  public static record FingerprintAndGross(IndexFingerprint live, IndexFingerprint gross) {}

  public static record FingerprintAndDeletedDocs(
      IndexFingerprint fingerprint, int[] deletedDocIds) {}

  public static IndexFingerprint reduce(IndexFingerprint acc, IndexFingerprint f2) {
    // acc should have maxVersionSpecified already set in it using IndexFingerprint(long
    // maxVersionSpecified) constructor
    acc.maxDoc = Math.max(acc.maxDoc, f2.maxDoc);
    acc.numDocs += f2.numDocs;
    acc.maxVersionEncountered = Math.max(acc.maxVersionEncountered, f2.maxVersionEncountered);
    acc.maxInHash = Math.max(acc.maxInHash, f2.maxInHash);
    acc.versionsHash += f2.versionsHash;
    acc.numVersions += f2.numVersions;

    return acc;
  }

  /** returns 0 for equal, negative if f1 is less recent than f2, positive if more recent */
  public static int compare(IndexFingerprint f1, IndexFingerprint f2) {
    int cmp;

    // NOTE: some way want number of docs in index to take precedence over highest version (add-only
    // systems for sure)

    // if we're comparing all of the versions in the index, then go by the highest encountered.
    if (f1.maxVersionSpecified == Long.MAX_VALUE) {
      cmp = Long.compare(f1.maxVersionEncountered, f2.maxVersionEncountered);
      if (cmp != 0) return cmp;
    }

    // Go by the highest version under the requested max.
    cmp = Long.compare(f1.maxInHash, f2.maxInHash);
    if (cmp != 0) return cmp;

    // go by who has the most documents in the index
    cmp = Long.compare(f1.numVersions, f2.numVersions);
    if (cmp != 0) return cmp;

    // both have same number of documents, so go by hash
    cmp = Long.compare(f1.versionsHash, f2.versionsHash);
    return cmp;
  }

  @Override
  public void writeMap(EntryWriter ew) throws IOException {
    ew.put("maxVersionSpecified", maxVersionSpecified)
        .put("maxVersionEncountered", maxVersionEncountered)
        .put("maxInHash", maxInHash)
        .put("versionsHash", versionsHash)
        .put("numVersions", numVersions)
        .put("numDocs", numDocs)
        .put("maxDoc", maxDoc);
  }

  private static long getLong(Map<String, Object> m, String key, long def) {
    Object oval = m.get(key);
    return oval != null ? ((Number) oval).longValue() : def;
  }

  /** Create an IndexFingerprint object from a deserialized generic object (Map or NamedList) */
  @SuppressWarnings("unchecked")
  public static IndexFingerprint fromObject(Object o) {
    if (o instanceof IndexFingerprint) return (IndexFingerprint) o;
    Map<String, Object> map = null;
    if (o instanceof Map) {
      map = (Map<String, Object>) o;
    } else if (o instanceof NamedList) {
      map = new SimpleOrderedMap<>((NamedList<Object>) o);
    } else {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Unknown type " + o);
    }
    IndexFingerprint f = new IndexFingerprint();
    f.maxVersionSpecified = getLong(map, "maxVersionSpecified", Long.MAX_VALUE);
    f.maxVersionEncountered = getLong(map, "maxVersionEncountered", -1);
    f.maxInHash = getLong(map, "maxInHash", -1);
    f.versionsHash = getLong(map, "versionsHash", -1);
    f.numVersions = getLong(map, "numVersions", -1);
    f.numDocs = getLong(map, "numDocs", -1);
    f.maxDoc = getLong(map, "maxDoc", -1);
    return f;
  }

  @Override
  public String toString() {
    return new SimpleOrderedMap<>(this).toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof IndexFingerprint other)) return false;
    return maxVersionSpecified == other.maxVersionSpecified
        && this.maxVersionEncountered == other.maxVersionEncountered
        && this.maxInHash == other.maxInHash
        && this.versionsHash == other.versionsHash
        && this.numVersions == other.numVersions
        && this.numDocs == other.numDocs
        && this.maxDoc == other.maxDoc;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        maxVersionSpecified,
        maxVersionEncountered,
        maxInHash,
        versionsHash,
        numVersions,
        numDocs,
        maxDoc);
  }
}
