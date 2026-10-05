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
package org.apache.solr.handler;

import org.apache.lucene.codecs.CodecUtil;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexOutput;
import org.apache.solr.SolrTestCase;
import org.apache.solr.util.LogListener;

/** SOLR-12246: a checksum mismatch just means the file is fetched again, it is not a warning. */
public class IndexFetcherCompareFileTest extends SolrTestCase {

  public void testChecksumMismatchIsNotLoggedAsWarning() throws Exception {
    try (Directory dir = newDirectory();
        LogListener warnings = LogListener.warn(IndexFetcher.class);
        LogListener infos = LogListener.info(IndexFetcher.class).substring("did not match")) {
      try (IndexOutput out = dir.createOutput("_0_1.liv", IOContext.DEFAULT)) {
        CodecUtil.writeHeader(out, "test", 1);
        out.writeInt(42);
        CodecUtil.writeFooter(out);
      }
      final long length = dir.fileLength("_0_1.liv");

      IndexFetcher.CompareResult result =
          IndexFetcher.compareFile(dir, "_0_1.liv", length, /* wrong checksum */ 12345L);

      assertFalse("different checksum must not compare equal", result.equal);
      assertTrue(result.checkSummed);
      assertNull("a checksum mismatch should not be logged as a warning", warnings.pollMessage());
      assertNotNull("a checksum mismatch should still be logged (at info)", infos.pollMessage());
    }
  }
}
