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

package org.apache.solr.client.solrj.request.json;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.solr.client.solrj.request.RequestWriter;
import tools.jackson.core.JacksonException;
import tools.jackson.core.StreamReadConstraints;
import tools.jackson.core.StreamWriteConstraints;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * @lucene.internal
 */
public class JacksonContentWriter implements RequestWriter.ContentWriter {

  public static final ObjectMapper DEFAULT_MAPPER =
      JsonMapper.builder(
              JsonFactory.builderWithJackson2Defaults()
                  .streamReadConstraints(
                      StreamReadConstraints.builder()
                          .maxNestingDepth(1000)
                          .maxStringLength(20_000_000)
                          .build())
                  .streamWriteConstraints(
                      StreamWriteConstraints.builder().maxNestingDepth(1000).build())
                  .build())
          .configureForJackson2()
          .build();

  private final Object toWrite;

  public JacksonContentWriter(Object toWrite) {
    this.toWrite = toWrite;
  }

  @Override
  public void write(OutputStream os) throws IOException {
    try {
      DEFAULT_MAPPER.writeValue(os, toWrite);
    } catch (JacksonException e) {
      throw asIOException(e);
    }
  }

  /** Converts values while retaining the argument-error contract of Jackson 2. */
  public static <T> T convertValue(Object value, Class<T> type) {
    try {
      return DEFAULT_MAPPER.convertValue(value, type);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  /** Converts parameterized values while retaining the argument-error contract of Jackson 2. */
  public static <T> T convertValue(Object value, TypeReference<T> type) {
    try {
      return DEFAULT_MAPPER.convertValue(value, type);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  /** Adapts Jackson failures to SolrJ's stream-reading and stream-writing contracts. */
  public static IOException asIOException(JacksonException exception) {
    return new IOException(exception.getMessage(), exception);
  }

  @Override
  public String getContentType() {
    return "application/json; charset=UTF-8";
  }
}
