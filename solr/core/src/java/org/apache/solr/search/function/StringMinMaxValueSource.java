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
package org.apache.solr.search.function;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.IndexSearcher;

/**
 * Per-document lexicographic min or max over string value sources; documents with no value are
 * skipped.
 */
public class StringMinMaxValueSource extends ValueSource {

  private final ValueSource[] sources;
  private final boolean max;

  public StringMinMaxValueSource(boolean max, ValueSource... sources) {
    this.max = max;
    this.sources = sources;
  }

  @Override
  public String description() {
    StringBuilder sb = new StringBuilder(max ? "max(" : "min(");
    for (int i = 0; i < sources.length; i++) {
      if (i > 0) sb.append(',');
      sb.append(sources[i].description());
    }
    return sb.append(')').toString();
  }

  @Override
  public FunctionValues getValues(Map<Object, Object> context, LeafReaderContext readerContext)
      throws IOException {
    FunctionValues[] values = new FunctionValues[sources.length];
    for (int i = 0; i < sources.length; i++) {
      values[i] = sources[i].getValues(context, readerContext);
    }
    return new FunctionValues() {
      @Override
      public String strVal(int doc) throws IOException {
        String best = null;
        for (FunctionValues v : values) {
          if (!v.exists(doc)) continue;
          String s = v.strVal(doc);
          if (best == null || (max ? s.compareTo(best) > 0 : s.compareTo(best) < 0)) {
            best = s;
          }
        }
        return best;
      }

      @Override
      public Object objectVal(int doc) throws IOException {
        return strVal(doc);
      }

      @Override
      public boolean exists(int doc) throws IOException {
        for (FunctionValues v : values) {
          if (v.exists(doc)) return true;
        }
        return false;
      }

      @Override
      public String toString(int doc) throws IOException {
        return description() + "=" + strVal(doc);
      }
    };
  }

  @Override
  public void createWeight(Map<Object, Object> context, IndexSearcher searcher) throws IOException {
    for (ValueSource source : sources) {
      source.createWeight(context, searcher);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof StringMinMaxValueSource other)) return false;
    return max == other.max && Arrays.equals(sources, other.sources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(max, Arrays.hashCode(sources));
  }
}
