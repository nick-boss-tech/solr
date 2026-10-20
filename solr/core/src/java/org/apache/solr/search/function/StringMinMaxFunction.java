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
import java.util.stream.Collectors;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.StrDocValues;
import org.apache.lucene.util.BytesRef;

/**
 * Lexicographic minimum or maximum of string-valued sources. Missing values are ignored, and a
 * document with no value in any source has no value.
 */
public class StringMinMaxFunction extends ValueSource {
  private final ValueSource[] sources;
  private final boolean max;

  public StringMinMaxFunction(ValueSource[] sources, boolean max) {
    this.sources = sources;
    this.max = max;
  }

  @Override
  public FunctionValues getValues(Map<Object, Object> context, LeafReaderContext readerContext)
      throws IOException {
    FunctionValues[] valsArr = new FunctionValues[sources.length];
    for (int i = 0; i < sources.length; i++) {
      valsArr[i] = sources[i].getValues(context, readerContext);
    }
    return new StrDocValues(this) {
      @Override
      public String strVal(int doc) throws IOException {
        BytesRef best = null;
        for (FunctionValues vals : valsArr) {
          String value = vals.strVal(doc);
          if (value == null) {
            continue;
          }
          BytesRef candidate = new BytesRef(value);
          if (best == null
              || (max ? candidate.compareTo(best) > 0 : candidate.compareTo(best) < 0)) {
            best = candidate;
          }
        }
        return best == null ? null : best.utf8ToString();
      }

      @Override
      public boolean exists(int doc) throws IOException {
        return strVal(doc) != null;
      }

      @Override
      public float floatVal(int doc) {
        throw new UnsupportedOperationException(
            "string min/max has no numeric value: " + description());
      }
    };
  }

  @Override
  public String description() {
    return (max ? "max(" : "min(")
        + Arrays.stream(sources).map(ValueSource::description).collect(Collectors.joining(","))
        + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StringMinMaxFunction other)) {
      return false;
    }
    return max == other.max && Arrays.equals(sources, other.sources);
  }

  @Override
  public int hashCode() {
    return 31 * Arrays.hashCode(sources) + Boolean.hashCode(max);
  }
}
