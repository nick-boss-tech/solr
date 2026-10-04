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

import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.NoDeletionPolicy;
import org.apache.lucene.index.NoMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.solr.SolrTestCaseJ4;

/** SOLR-12085: files of commits retained by the deletion policy are not "unused" files. */
public class IndexFetcherUnusedFilesTest extends SolrTestCaseJ4 {

  public void testRetainedCommitFilesAreNotUnused() throws Exception {
    try (Directory dir = newDirectory()) {
      // keep every commit, as SolrDeletionPolicy does with maxCommitsToKeep > 1
      IndexWriterConfig iwc =
          new IndexWriterConfig()
              .setIndexDeletionPolicy(NoDeletionPolicy.INSTANCE)
              .setMergePolicy(NoMergePolicy.INSTANCE);
      try (IndexWriter writer = new IndexWriter(dir, iwc)) {
        for (int i = 0; i < 2; i++) {
          Document doc = new Document();
          doc.add(new StringField("id", "doc" + i, Field.Store.YES));
          writer.addDocument(doc);
          writer.commit();
        }
      }

      List<IndexCommit> commits = DirectoryReader.listCommits(dir);
      assertEquals(2, commits.size());
      IndexCommit latest = commits.get(commits.size() - 1);

      // the older commit's segment files are still referenced by a retained commit
      assertFalse(IndexFetcher.hasUnusedFiles(dir, latest));

      // a file no commit refers to is still reported
      dir.createOutput("_orphan.cfs", IOContext.DEFAULT).close();
      assertTrue(IndexFetcher.hasUnusedFiles(dir, latest));
    }
  }
}
