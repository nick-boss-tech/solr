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
package org.apache.solr.client.solrj.response.json;

import static org.hamcrest.Matchers.instanceOf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.api.model.SolrJerseyResponse;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

/** Unit tests for {@link JacksonDataBindResponseParser} */
public class JacksonDataBindResponseParserTest extends SolrTestCase {

  @Test
  public void testSuccessfulResponseHasNoErrorKey() throws IOException {
    final var parser = new JacksonDataBindResponseParser<>(SolrJerseyResponse.class);
    final var json = "{\"responseHeader\":{\"status\":0,\"QTime\":5}}";

    final NamedList<Object> result = parser.processResponse(toStream(json), null);

    assertNotNull(result.get("response"));
    assertThat(result.get("response"), instanceOf(SolrJerseyResponse.class));
    assertNull("No 'error' key expected on success", result.get("error"));
  }

  @Test
  public void testErrorResponseSurfacesErrorAtTopLevel() throws IOException {
    final var parser = new JacksonDataBindResponseParser<>(SolrJerseyResponse.class);
    final var json =
        "{"
            + "\"responseHeader\":{\"status\":400,\"QTime\":3},"
            + "\"error\":{"
            + "  \"code\":400,"
            + "  \"errorClass\":\"org.apache.solr.common.SolrException\","
            + "  \"msg\":\"Bad collection name\""
            + "}"
            + "}";

    final NamedList<Object> result = parser.processResponse(toStream(json), null);

    // The deserialized POJO is present under "response"
    assertNotNull(result.get("response"));
    assertThat(result.get("response"), instanceOf(SolrJerseyResponse.class));
    final var responsePojo = (SolrJerseyResponse) result.get("response");

    // Error is present in the deserialized POJO
    assertNotNull("Error should be populated in the POJO", responsePojo.error);

    // Error is also present on NL for clients to detect
    assertNotNull(result.get("error"));
    assertThat(result.get("error"), instanceOf(Map.class));
    @SuppressWarnings("unchecked")
    final var topLevelError = (Map<String, Object>) result.get("error");
    assertEquals(400, topLevelError.get("code"));
    assertEquals("org.apache.solr.common.SolrException", topLevelError.get("errorClass"));
    assertEquals("Bad collection name", topLevelError.get("msg"));
  }

  @Test
  public void testErrorResponseIncludesMetadataAtTopLevel() throws IOException {
    final var parser = new JacksonDataBindResponseParser<>(SolrJerseyResponse.class);
    final var json =
        """
        {
            "responseHeader":{
                "status":500,
                "QTime":1
            },
            "error": {
                "code":500,
                "errorClass": "org.apache.solr.common.SolrException",
                "msg": "Internal error",
                "metadata":{
                    "error-class": "org.apache.solr.common.SolrException",
                    "root-error-class": "java.lang.IllegalStateException"
                }
            }
        }""";

    final NamedList<Object> result = parser.processResponse(toStream(json), null);

    @SuppressWarnings("unchecked")
    final var topLevelError = (Map<String, Object>) result.get("error");
    assertNotNull(topLevelError);

    @SuppressWarnings("unchecked")
    final var metadata = (Map<String, Object>) topLevelError.get("metadata");
    assertNotNull("Metadata must be present for RemoteSolrException to extract it", metadata);
    assertEquals("org.apache.solr.common.SolrException", metadata.get("error-class"));
    assertEquals("java.lang.IllegalStateException", metadata.get("root-error-class"));
  }

  @Test
  public void testNonJerseyResponseTypeIsUnaffected() throws IOException {
    // A parser bound to a plain POJO (i.e. not SolrJerseyResponse) must not add an "error" key
    final var parser = new JacksonDataBindResponseParser<>(PlainPojo.class);
    final var json = "{\"value\":\"hello\"}";

    final NamedList<Object> result = parser.processResponse(toStream(json), null);

    assertNotNull(result.get("response"));
    assertThat(result.get("response"), instanceOf(PlainPojo.class));
    assertNull("Error extraction only occurs for SolrJerseyResponse types", result.get("error"));
  }

  @Test
  public void testMalformedInputRemainsIOException() {
    final var parser = new JacksonDataBindResponseParser<>(PlainPojo.class);
    for (String encoding : new String[] {null, "UTF-8"}) {
      for (String json : new String[] {"{", "{\"value\":", "{\"unknown\":1}"}) {
        expectThrows(IOException.class, () -> parser.processResponse(toStream(json), encoding));
      }
    }
  }

  @Test
  public void testExplicitCharacterEncoding() throws IOException {
    final var parser = new JacksonDataBindResponseParser<>(PlainPojo.class);
    final var stream =
        new ByteArrayInputStream("{\"value\":\"caf\u00e9\"}".getBytes(StandardCharsets.UTF_16LE));
    final var result = parser.processResponse(stream, "UTF-16LE");
    assertEquals("caf\u00e9", ((PlainPojo) result.get("response")).value);
  }

  @Test
  public void testNestedResponseRetainsJackson2DepthLimit() throws IOException {
    final var parser = new JacksonDataBindResponseParser<>(Object.class);
    Object value =
        parser
            .processResponse(toStream("[".repeat(600) + "0" + "]".repeat(600)), null)
            .get("response");
    for (int depth = 0; depth < 600; depth++) {
      value = ((List<?>) value).get(0);
    }
    assertEquals(0, value);
  }

  @Test
  public void testInputFailureRemainsIOException() {
    final var parser = new JacksonDataBindResponseParser<>(PlainPojo.class);
    for (String encoding : new String[] {null, "UTF-8"}) {
      final var stream =
          new InputStream() {
            @Override
            public int read() throws IOException {
              throw new IOException("input failed");
            }
          };
      final var exception =
          expectThrows(IOException.class, () -> parser.processResponse(stream, encoding));
      assertTrue(exception.getMessage().contains("input failed"));
    }
  }

  // Minimal POJO for the non-SolrJerseyResponse test above
  public static class PlainPojo {
    public String value;
  }

  private static ByteArrayInputStream toStream(String json) {
    return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
  }
}
