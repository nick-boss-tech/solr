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
package org.apache.solr.spelling;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.HighFrequencyDictionary;
import org.apache.lucene.store.FSDirectory;
import org.apache.solr.common.util.IOUtils;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.search.SolrIndexSearcher;

/**
 * A spell checker implementation that loads words from Solr as well as arbitrary Lucene indices.
 *
 * <p>Refer to <a
 * href="https://solr.apache.org/guide/solr/latest/query-guide/spell-checking.html">https://solr.apache.org/guide/solr/latest/query-guide/spell-checking.html</a>
 * for more details.
 *
 * @since solr 1.3
 */
public class IndexBasedSpellChecker extends AbstractLuceneSpellChecker {

  public static final String THRESHOLD_TOKEN_FREQUENCY = "thresholdTokenFrequency";

  protected float threshold;
  protected IndexReader reader;

  // the directory behind the reader opened from the configured sourceLocation, if any
  private FSDirectory sourceDir;

  private final ReentrantReadWriteLock readerLock = new ReentrantReadWriteLock();

  @Override
  public String init(NamedList<?> config, SolrCore core) {
    super.init(config, core);
    threshold =
        config.get(THRESHOLD_TOKEN_FREQUENCY) == null
            ? 0.0f
            : (Float) config.get(THRESHOLD_TOKEN_FREQUENCY);
    initSourceReader();
    return name;
  }

  private void initSourceReader() {
    if (sourceLocation != null) {
      FSDirectory luceneIndexDir = null;
      IndexReader newReader;
      try {
        luceneIndexDir = FSDirectory.open(Path.of(sourceLocation));
        newReader = DirectoryReader.open(luceneIndexDir);
      } catch (IOException e) {
        IOUtils.closeQuietly(luceneIndexDir);
        throw new RuntimeException(e);
      }
      IndexReader previousReader;
      FSDirectory previousDir;
      readerLock.writeLock().lock();
      try {
        previousReader = this.reader;
        previousDir = this.sourceDir;
        this.reader = newReader;
        this.sourceDir = luceneIndexDir;
      } finally {
        readerLock.writeLock().unlock();
      }
      IOUtils.closeQuietly(previousReader);
      IOUtils.closeQuietly(previousDir);
    }
  }

  @Override
  public SpellingResult getSuggestions(SpellingOptions options) throws IOException {
    readerLock.readLock().lock();
    try {
      return super.getSuggestions(options);
    } finally {
      readerLock.readLock().unlock();
    }
  }

  @Override
  public void close() throws IOException {
    try {
      super.close();
    } finally {
      IOUtils.closeQuietly(reader);
      IOUtils.closeQuietly(sourceDir);
    }
  }

  @Override
  public void build(SolrCore core, SolrIndexSearcher searcher) throws IOException {
    readerLock.readLock().lock();
    try {
      IndexReader reader = null;
      if (sourceLocation == null) {
        // Load from Solr's index
        reader = searcher.getIndexReader();
      } else {
        // Load from Lucene index at given sourceLocation
        reader = this.reader;
      }

      // Create the dictionary
      dictionary = new HighFrequencyDictionary(reader, field, threshold);
      // TODO: maybe whether or not to clear the index should be configurable?
      // an incremental update is faster (just adds new terms), but if you 'expunged'
      // old terms I think they might hang around.
      spellChecker.clearIndex();
      // TODO: you should be able to specify the IWC params?
      // TODO: if we enable this, codec gets angry since field won't exist in the schema
      // config.setCodec(core.getCodec());
      spellChecker.indexDictionary(dictionary, new IndexWriterConfig(null), false);
    } finally {
      readerLock.readLock().unlock();
    }
  }

  @Override
  protected IndexReader determineReader(IndexReader reader) {
    IndexReader result = null;
    if (sourceLocation != null) {
      result = this.reader;
    } else {
      result = reader;
    }
    return result;
  }

  @Override
  public void reload(SolrCore core, SolrIndexSearcher searcher) throws IOException {
    super.reload(core, searcher);
    // reload the source
    initSourceReader();
  }

  public float getThreshold() {
    return threshold;
  }
}
