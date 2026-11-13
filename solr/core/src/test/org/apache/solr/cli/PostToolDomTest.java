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
package org.apache.solr.cli;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.apache.solr.SolrTestCase;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Tests direct XML-byte input to {@link PostTool#makeDom(byte[])} only, not the web-page to Tika to
 * serialized-extraction-response path.
 */
public class PostToolDomTest extends SolrTestCase {

  @Test
  public void testMakeDomDoesNotResolveExternalFileEntity() throws Exception {
    Path entityFile = createTempDir().resolve("external-entity.txt");
    String marker = "posttool-test-entity-" + UUID.randomUUID();
    Files.writeString(entityFile, marker, StandardCharsets.UTF_8);

    String xml =
        "<!DOCTYPE root [\n"
            + "  <!ENTITY external SYSTEM \""
            + entityFile.toUri().toASCIIString()
            + "\">\n"
            + "]>\n"
            + "<root>&external;</root>";

    try {
      Document document = PostTool.makeDom(xml.getBytes(StandardCharsets.UTF_8));
      assertFalse(
          "PostTool.makeDom expanded the external entity from the test-created file: " + marker,
          document.getDocumentElement().getTextContent().contains(marker));
    } catch (SAXException e) {
      // A parser that rejects the document type instead of resolving it is safe for this input.
    }
  }

  @Test
  public void testMakeDomDoesNotResolveExternalHttpEntity() throws Exception {
    Path entityFile = createTempDir().resolve("external-entity.txt");
    String marker = "posttool-test-entity-" + UUID.randomUUID();
    Files.writeString(entityFile, marker, StandardCharsets.UTF_8);

    try (ServerSocket entityServer = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      entityServer.setSoTimeout(1000);
      FutureTask<Boolean> requestReceived =
          new FutureTask<>(
              () -> {
                try (Socket socket = entityServer.accept()) {
                  byte[] body = Files.readAllBytes(entityFile);
                  String response =
                      "HTTP/1.1 200 OK\r\n"
                          + "Content-Type: text/plain\r\n"
                          + "Content-Length: "
                          + body.length
                          + "\r\nConnection: close\r\n\r\n";
                  socket.getOutputStream().write(response.getBytes(StandardCharsets.US_ASCII));
                  socket.getOutputStream().write(body);
                  socket.getOutputStream().flush();
                  return true;
                } catch (SocketTimeoutException e) {
                  return false;
                }
              });
      Thread serverThread = new Thread(requestReceived);
      serverThread.setDaemon(true);
      serverThread.start();

      String xml =
          "<!DOCTYPE root [\n"
              + "  <!ENTITY external SYSTEM \"http://127.0.0.1:"
              + entityServer.getLocalPort()
              + "/external-entity.txt\">\n"
              + "]>\n"
              + "<root>&external;</root>";

      try {
        Document document = PostTool.makeDom(xml.getBytes(StandardCharsets.UTF_8));
        assertFalse(
            "PostTool.makeDom expanded the external entity served from loopback: " + marker,
            document.getDocumentElement().getTextContent().contains(marker));
      } catch (SAXException e) {
        // A parser that rejects the document type instead of resolving it is safe for this input.
      }
      assertFalse(
          "PostTool.makeDom made an HTTP request for the external entity",
          requestReceived.get(2, TimeUnit.SECONDS));
    }
  }
}
