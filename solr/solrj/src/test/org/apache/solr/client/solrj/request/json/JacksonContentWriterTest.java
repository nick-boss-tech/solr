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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.solr.SolrTestCase;
import org.junit.Test;

/** Compatibility of JSON request bodies with the previous client mapper. */
public class JacksonContentWriterTest extends SolrTestCase {

  @Test
  public void testJackson2RequestRepresentationAndStreamOwnership() throws Exception {
    final Object value = Map.of("payload", new Payload(), "array", List.of(1, "two", true));
    final var expected = new TrackingOutputStream();
    new ObjectMapper().writeValue(expected, value);
    final var actual = new TrackingOutputStream();
    new JacksonContentWriter(value).write(actual);

    assertArrayEquals(expected.toByteArray(), actual.toByteArray());
    assertEquals(expected.closed, actual.closed);
    assertEquals(expected.flushed, actual.flushed);
  }

  @Test
  public void testOutputFailureRemainsIOException() {
    final var failure = new IOException("output failed");
    final var output =
        new OutputStream() {
          @Override
          public void write(int value) throws IOException {
            throw failure;
          }
        };
    final var exception =
        expectThrows(
            IOException.class, () -> new JacksonContentWriter(Map.of("a", 1)).write(output));
    assertTrue(exception.getMessage().contains("output failed"));
  }

  @Test
  public void testSerializationFailureRemainsIOException() {
    expectThrows(
        IOException.class,
        () -> new JacksonContentWriter(new Object()).write(new ByteArrayOutputStream()));
  }

  @Test
  public void testNestedRequestRetainsJackson2DepthLimit() throws Exception {
    Object value = 0;
    for (int depth = 0; depth < 600; depth++) {
      value = List.of(value);
    }
    final var output = new ByteArrayOutputStream();
    new JacksonContentWriter(value).write(output);
    assertEquals(
        new ObjectMapper().writeValueAsString(value), output.toString(StandardCharsets.UTF_8));
  }

  @Test
  public void testReadConstraintsMatchJackson2() {
    final var expected = new ObjectMapper().getFactory().streamReadConstraints();
    final var actual =
        JacksonContentWriter.DEFAULT_MAPPER.tokenStreamFactory().streamReadConstraints();
    assertEquals(expected.getMaxNestingDepth(), actual.getMaxNestingDepth());
    assertEquals(expected.getMaxStringLength(), actual.getMaxStringLength());
    assertEquals(expected.getMaxNumberLength(), actual.getMaxNumberLength());
    assertEquals(expected.getMaxNameLength(), actual.getMaxNameLength());
    assertEquals(expected.getMaxDocumentLength(), actual.getMaxDocumentLength());
    assertEquals(expected.getMaxTokenCount(), actual.getMaxTokenCount());
  }

  public static class Payload {
    public String z = "last alphabetically";
    public Date when = new Date(1234);
    public Choice choice = Choice.VALUE;
    public String absent = null;

    @JsonProperty("alias")
    public String renamed = "shared annotation";
  }

  public enum Choice {
    VALUE;

    @Override
    public String toString() {
      return "custom enum representation";
    }
  }

  private static class TrackingOutputStream extends ByteArrayOutputStream {
    boolean closed;
    boolean flushed;

    @Override
    public void close() throws IOException {
      closed = true;
      super.close();
    }

    @Override
    public void flush() throws IOException {
      flushed = true;
      super.flush();
    }
  }
}
