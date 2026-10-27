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

package org.apache.solr.compat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.apache.solr.client.solrj.request.json.JacksonContentWriter;
import org.apache.solr.client.solrj.response.json.JacksonDataBindResponseParser;

/** Behavioral probes for the internal writer and experimental typed JSON parser. */
public class JacksonBehavior {
  public static void main(String[] args) throws Exception {
    var payload = new Payload();
    payload.displayName = "caf\u00e9";
    payload.created = new Date(1234);
    payload.secret = "must-not-appear";
    var output = new TrackedOutput();
    var writer = new JacksonContentWriter(payload);
    writer.write(output);
    require(output.closed, "writer closes supplied output as on baseline");
    String json = output.toString(StandardCharsets.UTF_8);
    require(json.contains("\"external_name\":\"caf\u00e9\""), "shared @JsonProperty and Unicode");
    require(json.contains("\"created\":1234"), "Jackson 2 timestamp date default");
    require(!json.contains("secret") && !json.contains("must-not-appear"), "shared @JsonIgnore");
    require(writer.getContentType().startsWith("application/json"), "writer content type");

    var parser = new JacksonDataBindResponseParser<>(Payload.class);
    for (String encoding : new String[] {null, "UTF-8"}) {
      var input = new TrackedInput(output.toByteArray());
      var parsed = (Payload) parser.processResponse(input, encoding).get("response");
      require(input.closed, "parser closes supplied input with encoding " + encoding);
      require(payload.displayName.equals(parsed.displayName), "typed response annotation");
      require(payload.created.equals(parsed.created), "typed response date");
      require(parsed.secret == null, "ignored response field");
    }
    var malformed = new TrackedInput("{bad json".getBytes(StandardCharsets.UTF_8));
    expectIOException(() -> parser.processResponse(malformed, "UTF-8"), "malformed JSON");
    require(malformed.closed, "parser closes supplied input after malformed JSON");
    expectIOException(() -> parser.processResponse(bytes("{\"unknown\":1}"), null), "unknown property");
    expectIOException(() -> new JacksonContentWriter(new Object()).write(new ByteArrayOutputStream()),
        "empty bean");
    expectIOException(() -> writer.write(new OutputStream() {
      @Override
      public void write(int value) throws IOException {
        throw new IOException("deliberate consumer output failure");
      }
    }), "output I/O failure");
    System.out.println("PASS shared annotations, typed JSON, dates, Unicode, and IOException boundaries");
    System.out.println(
        "STREAM_OWNERSHIP outputClosed=true inputClosed=true readerInputClosed=true"
            + " malformedInputClosed=true");
  }

  private static ByteArrayInputStream bytes(String json) {
    return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
  }

  private static void expectIOException(IOAction action, String description) throws Exception {
    try {
      action.run();
    } catch (IOException expected) {
      return;
    }
    throw new AssertionError("Expected IOException: " + description);
  }

  private static void require(boolean condition, String description) {
    if (!condition) throw new AssertionError(description);
  }

  @FunctionalInterface
  private interface IOAction {
    void run() throws Exception;
  }

  private static class TrackedOutput extends ByteArrayOutputStream {
    private boolean closed;

    @Override
    public void close() throws IOException {
      closed = true;
      super.close();
    }
  }

  private static class TrackedInput extends ByteArrayInputStream {
    private boolean closed;

    private TrackedInput(byte[] bytes) {
      super(bytes);
    }

    @Override
    public void close() throws IOException {
      closed = true;
      super.close();
    }
  }

  /** Application-owned model that uses the shared Jackson annotations artifact. */
  public static class Payload {
    @JsonProperty("external_name")
    public String displayName;

    public Date created;

    @JsonIgnore
    public String secret;
  }
}
