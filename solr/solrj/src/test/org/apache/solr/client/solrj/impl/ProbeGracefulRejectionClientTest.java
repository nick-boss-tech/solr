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

package org.apache.solr.client.solrj.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.SSLSession;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.RemoteSolrException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.client.solrj.jetty.LBJettySolrClient;
import org.apache.solr.client.solrj.request.GenericSolrRequest;
import org.apache.solr.client.solrj.request.RequestWriter;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.request.V2Request;
import org.apache.solr.client.solrj.response.InputStreamResponseParser;
import org.apache.solr.client.solrj.response.XMLResponseParser;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.GracefulHandler;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;

/** Tests graceful shutdown rejection handling and the opt-in trust boundary. */
public class ProbeGracefulRejectionClientTest extends SolrTestCaseJ4 {

  /** Local copy of the marking handler (the canonical probe class lives in test-framework). */
  private static final class MarkingGracefulHandler extends GracefulHandler {
    MarkingGracefulHandler(Handler handler) {
      super(handler);
    }

    @Override
    protected void handleShutdownRejection(Request request, Response response, Callback callback) {
      response
          .getHeaders()
          .put(GracefulShutdownRejectionException.MARKER_HEADER, JettySolrRunnerMarker.VALUE);
      super.handleShutdownRejection(request, response, callback);
    }
  }

  private static final class JettySolrRunnerMarker {
    static final String VALUE = GracefulShutdownRejectionException.MARKER_VALUE;
  }

  private static final class CloseTrackingInputStream extends ByteArrayInputStream {
    boolean closed;

    CloseTrackingInputStream() {
      super(new byte[0]);
    }

    @Override
    public void close() throws IOException {
      closed = true;
      super.close();
    }
  }

  private static final class OneShotRequestWriter extends RequestWriter {
    final AtomicInteger writes = new AtomicInteger();

    @Override
    public ContentWriter getContentWriter(SolrRequest<?> request) {
      return new ContentWriter() {
        @Override
        public void write(OutputStream output) throws IOException {
          if (writes.incrementAndGet() > 1) {
            throw new IOException("one-shot request writer was invoked more than once");
          }
          output.write("one-shot".getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String getContentType() {
          return "application/octet-stream";
        }
      };
    }

    @Override
    public void write(SolrRequest<?> request, OutputStream output) throws IOException {
      getContentWriter(request).write(output);
    }

    @Override
    public String getUpdateContentType() {
      return "application/octet-stream";
    }
  }

  private static final class MarkerRejectingHttpJdkSolrClient extends HttpJdkSolrClient {
    MarkerRejectingHttpJdkSolrClient(String baseUrl, RequestWriter requestWriter) {
      super(baseUrl, new HttpJdkSolrClient.Builder(baseUrl).withRequestWriter(requestWriter));
    }

    @Override
    public NamedList<Object> requestWithBaseUrl(
        String baseUrl, SolrRequest<?> request, String collection) {
      throw new GracefulShutdownRejectionException(
          baseUrl, "synthetic marked pre-handler rejection");
    }
  }

  private static final class FakeSolrEndpoint implements AutoCloseable {
    final Server server;
    final GracefulHandler graceful;
    final AtomicInteger totalHits = new AtomicInteger();
    final AtomicInteger appHits = new AtomicInteger();

    FakeSolrEndpoint(boolean marking, boolean app503) throws Exception {
      this(marking, app503, false);
    }

    FakeSolrEndpoint(boolean marking, boolean app503, boolean addMarkerToApplication503)
        throws Exception {
      Handler app =
          new Handler.Abstract() {
            @Override
            public boolean handle(Request request, Response response, Callback callback) {
              if (!"HEAD".equals(request.getMethod())) {
                appHits.incrementAndGet();
              }
              String body;
              if (app503) {
                body =
                    "<response><lst name=\"error\"><str name=\"msg\">application unavailable</str>"
                        + "<int name=\"code\">503</int></lst><lst name=\"responseHeader\">"
                        + "<int name=\"status\">503</int><int name=\"QTime\">1</int></lst></response>";
                response.setStatus(503);
                if (addMarkerToApplication503) {
                  // Simulate an intermediary adding the marker after the application handler ran.
                  response
                      .getHeaders()
                      .put(
                          GracefulShutdownRejectionException.MARKER_HEADER,
                          GracefulShutdownRejectionException.MARKER_VALUE);
                }
              } else {
                body =
                    "<response><lst name=\"responseHeader\"><int name=\"status\">0</int>"
                        + "<int name=\"QTime\">1</int></lst></response>";
                response.setStatus(200);
              }
              response.getHeaders().put(HttpHeader.CONTENT_TYPE, "application/xml; charset=utf-8");
              response.write(true, BufferUtil.toBuffer(body), callback);
              return true;
            }
          };
      graceful = marking ? new MarkingGracefulHandler(app) : new GracefulHandler(app);
      Handler.Wrapper counting =
          new Handler.Wrapper(graceful) {
            @Override
            public boolean handle(Request request, Response response, Callback callback)
                throws Exception {
              // HttpJdkSolrClient sends a one-time HEAD probe per base URI (HTTP/2 upgrade
              // check); it is not a submission of the request under test and is not counted.
              if (!"HEAD".equals(request.getMethod())) {
                totalHits.incrementAndGet();
              }
              return super.handle(request, response, callback);
            }
          };
      server = new Server();
      ServerConnector connector = new ServerConnector(server);
      connector.setPort(0);
      server.addConnector(connector);
      server.setHandler(counting);
      server.start();
    }

    String baseUrl() {
      int port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
      return "http://127.0.0.1:" + port + "/solr/core1";
    }

    LBSolrClient.Endpoint endpoint() {
      return new LBSolrClient.Endpoint(baseUrl());
    }

    void beginShutdown() {
      graceful.shutdown();
    }

    @Override
    public void close() {
      try {
        server.stop();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  private final List<SolrClient> createdClients = new ArrayList<>();

  private LBSolrClient newLbClient(boolean jetty) {
    return newLbClient(jetty, true);
  }

  private LBSolrClient newLbClient(boolean jetty, boolean trustMarker) {
    return new LBSolrClient(List.of()) {
      @Override
      protected SolrClient getClient(Endpoint endpoint) {
        SolrClient client =
            jetty
                ? new HttpJettySolrClient.Builder(endpoint.getBaseUrl())
                    .withTrustedGracefulShutdownRejections(trustMarker)
                    .useHttp1_1(true)
                    .build()
                : new HttpJdkSolrClient.Builder(endpoint.getBaseUrl())
                    .withTrustedGracefulShutdownRejections(trustMarker)
                    .build();
        createdClients.add(client);
        return client;
      }
    };
  }

  private LBAsyncSolrClient newJdkAsyncLbClient(FakeSolrEndpoint first, FakeSolrEndpoint second) {
    HttpJdkSolrClient delegate =
        new HttpJdkSolrClient.Builder(first.endpoint().getBaseUrl())
            .withTrustedGracefulShutdownRejections(true)
            .build();
    createdClients.add(delegate);
    return new LBAsyncSolrClient(
        new LBSolrClient.Builder<>(delegate, first.endpoint(), second.endpoint())) {
      @Override
      protected HttpSolrClient getClient(Endpoint endpoint) {
        HttpJdkSolrClient client =
            new HttpJdkSolrClient.Builder(endpoint.getBaseUrl())
                .withTrustedGracefulShutdownRejections(true)
                .withDefaultCollection(endpoint.getCore())
                .build();
        createdClients.add(client);
        return client;
      }

      @Override
      protected CompletableFuture<NamedList<Object>> requestAsyncWithUrl(
          SolrClient client, String baseUrl, SolrRequest<?> request) {
        return ((HttpJdkSolrClient) client).requestAsync(request, null);
      }
    };
  }

  @org.junit.After
  public void closeCreatedClients() throws Exception {
    for (SolrClient client : createdClients) {
      client.close();
    }
    createdClients.clear();
  }

  private static UpdateRequest materializedUpdate() {
    UpdateRequest update = new UpdateRequest();
    update.setResponseParser(new XMLResponseParser());
    SolrInputDocument doc = new SolrInputDocument("id", "1");
    doc.setField("count_i", java.util.Map.of("inc", 1));
    update.add(doc);
    return update;
  }

  private static HttpResponse<InputStream> markedResponse(InputStream body) {
    HttpHeaders headers =
        HttpHeaders.of(
            Map.of(
                GracefulShutdownRejectionException.MARKER_HEADER,
                List.of(GracefulShutdownRejectionException.MARKER_VALUE)),
            (name, value) -> true);
    return new HttpResponse<>() {
      @Override
      public int statusCode() {
        return 503;
      }

      @Override
      public HttpRequest request() {
        return null;
      }

      @Override
      public Optional<HttpResponse<InputStream>> previousResponse() {
        return Optional.empty();
      }

      @Override
      public HttpHeaders headers() {
        return headers;
      }

      @Override
      public InputStream body() {
        return body;
      }

      @Override
      public Optional<SSLSession> sslSession() {
        return Optional.empty();
      }

      @Override
      public URI uri() {
        return URI.create("http://127.0.0.1/solr/core1");
      }

      @Override
      public HttpClient.Version version() {
        return HttpClient.Version.HTTP_1_1;
      }
    };
  }

  public void testMarkedJdkResponseClosesStreamParserBody() throws Exception {
    CloseTrackingInputStream body = new CloseTrackingInputStream();
    try (HttpJdkSolrClient client =
        new HttpJdkSolrClient.Builder("http://127.0.0.1:1/solr")
            .withTrustedGracefulShutdownRejections(true)
            .build()) {
      expectThrows(
          GracefulShutdownRejectionException.class,
          () ->
              client.processErrorsAndResponse(
                  materializedUpdate(),
                  new InputStreamResponseParser("xml"),
                  markedResponse(body),
                  "http://127.0.0.1/solr/core1"));
    }
    assertTrue("JDK marker processing must close the response stream", body.closed);
  }

  public void testAsyncMarkedUpdateIsNotReplayedByEitherTransport() throws Exception {
    for (boolean jetty : new boolean[] {false, true}) {
      try (FakeSolrEndpoint rejecting = new FakeSolrEndpoint(true, false);
          FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false)) {
        rejecting.beginShutdown();
        LBAsyncSolrClient lb;
        if (jetty) {
          HttpJettySolrClient client =
              new HttpJettySolrClient.Builder(rejecting.endpoint().getBaseUrl())
                  .withTrustedGracefulShutdownRejections(true)
                  .useHttp1_1(true)
                  .build();
          createdClients.add(client);
          lb =
              new LBJettySolrClient.Builder(client, rejecting.endpoint(), healthy.endpoint())
                  .build();
        } else {
          lb = newJdkAsyncLbClient(rejecting, healthy);
        }
        try (lb) {
          ExecutionException failure =
              expectThrows(
                  ExecutionException.class,
                  () ->
                      lb.requestAsync(
                              new LBSolrClient.Req(
                                  materializedUpdate(),
                                  List.of(rejecting.endpoint(), healthy.endpoint())))
                          .get(30, TimeUnit.SECONDS));
          StringWriter trace = new StringWriter();
          failure.printStackTrace(new PrintWriter(trace));
          assertTrue(
              "async marker failure must surface, not replay the update; jetty="
                  + jetty
                  + ", rejected requests="
                  + rejecting.totalHits.get()
                  + ", healthy requests="
                  + healthy.totalHits.get()
                  + "\n"
                  + trace,
              SolrException.hasCause(failure, GracefulShutdownRejectionException.class));
          assertEquals("client jetty=" + jetty, 1, rejecting.totalHits.get());
          assertEquals("client jetty=" + jetty, 0, rejecting.appHits.get());
          assertEquals("client jetty=" + jetty, 0, healthy.totalHits.get());
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void testMarkedRejectionRetriesMaterializedUpdateOnce() throws Exception {
    for (boolean jetty : new boolean[] {false, true}) {
      try (FakeSolrEndpoint rejecting = new FakeSolrEndpoint(true, false);
          FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
          LBSolrClient lb = newLbClient(jetty)) {
        rejecting.beginShutdown();
        LBSolrClient.Rsp rsp =
            lb.request(
                new LBSolrClient.Req(
                    materializedUpdate(), List.of(rejecting.endpoint(), healthy.endpoint())));
        NamedList<Object> responseHeader = (NamedList<Object>) rsp.rsp.get("responseHeader");
        assertEquals(0, ((Number) responseHeader.get("status")).intValue());
        assertEquals("client jetty=" + jetty, 1, rejecting.totalHits.get());
        assertEquals("client jetty=" + jetty, 0, rejecting.appHits.get());
        assertEquals("client jetty=" + jetty, 1, healthy.appHits.get());
      }
    }
  }

  public void testTypedCustomContentWriterIsNotResent() throws Exception {
    for (boolean jetty : new boolean[] {false, true}) {
      try (FakeSolrEndpoint rejecting = new FakeSolrEndpoint(true, false);
          FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
          LBSolrClient lb = newLbClient(jetty)) {
        rejecting.beginShutdown();
        AtomicInteger writes = new AtomicInteger();
        GenericSolrRequest request =
            new GenericSolrRequest(
                    SolrRequest.METHOD.POST, "/update", SolrRequest.SolrRequestType.UPDATE)
                .setContentWriter(
                    new RequestWriter.ContentWriter() {
                      @Override
                      public void write(OutputStream output) throws IOException {
                        if (writes.incrementAndGet() > 1) {
                          throw new IOException("custom content writer was invoked more than once");
                        }
                        output.write("one-shot".getBytes(StandardCharsets.UTF_8));
                      }

                      @Override
                      public String getContentType() {
                        return "application/octet-stream";
                      }
                    });
        expectThrows(
            GracefulShutdownRejectionException.class,
            () ->
                lb.request(
                    new LBSolrClient.Req(
                        request, List.of(rejecting.endpoint(), healthy.endpoint()))));
        assertEquals("client jetty=" + jetty, 1, writes.get());
        assertEquals("client jetty=" + jetty, 0, healthy.appHits.get());
        assertEquals("client jetty=" + jetty, 0, rejecting.appHits.get());
      }
    }
  }

  public void testCustomRequestWriterIsNotResent() throws Exception {
    OneShotRequestWriter customWriter = new OneShotRequestWriter();
    LBSolrClient.Endpoint markerEndpoint =
        new LBSolrClient.Endpoint("http://marker.invalid/solr/core1");
    try (FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
        LBSolrClient lb =
            new LBSolrClient(List.of()) {
              @Override
              protected SolrClient getClient(Endpoint endpoint) {
                SolrClient client;
                if (endpoint.getBaseUrl().contains("marker.invalid")) {
                  client =
                      new MarkerRejectingHttpJdkSolrClient(endpoint.getBaseUrl(), customWriter);
                } else {
                  client = new HttpJdkSolrClient.Builder(endpoint.getBaseUrl()).build();
                }
                createdClients.add(client);
                if (endpoint.getBaseUrl().contains("marker.invalid")) {
                  assertSame(customWriter, ((HttpSolrClient) client).getRequestWriter());
                  assertFalse(
                      LBSolrClient.isGracefulRejectionRetryable(materializedUpdate(), client));
                }
                return client;
              }
            }) {
      expectThrows(
          GracefulShutdownRejectionException.class,
          () ->
              lb.request(
                  new LBSolrClient.Req(
                      materializedUpdate(), List.of(markerEndpoint, healthy.endpoint()))));
      assertEquals(0, healthy.appHits.get());
      assertEquals(0, customWriter.writes.get());
    }
  }

  public void testOneShotIteratorIsNotResent() throws Exception {
    try (FakeSolrEndpoint rejecting = new FakeSolrEndpoint(true, false);
        FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
        LBSolrClient lb = newLbClient(false)) {
      rejecting.beginShutdown();
      UpdateRequest update = new UpdateRequest();
      Iterator<SolrInputDocument> oneShot = List.of(new SolrInputDocument("id", "1")).iterator();
      update.setDocIterator(oneShot);
      expectThrows(
          GracefulShutdownRejectionException.class,
          () ->
              lb.request(
                  new LBSolrClient.Req(update, List.of(rejecting.endpoint(), healthy.endpoint()))));
      assertEquals(0, healthy.appHits.get());
      assertEquals(0, rejecting.appHits.get());
    }
  }

  public void testNestedOneShotFieldIsNotRepeatable() throws Exception {
    try (HttpJdkSolrClient client =
        new HttpJdkSolrClient.Builder("http://127.0.0.1:1/solr").build()) {
      UpdateRequest update = new UpdateRequest();
      SolrInputDocument doc = new SolrInputDocument("id", "1");
      doc.setField("values", List.of("a", "b").iterator());
      update.add(doc);
      assertFalse(LBSolrClient.isGracefulRejectionRetryable(update, client));

      SolrInputDocument nested = new SolrInputDocument("id", "2");
      nested.setField("values", Map.of("set", List.of("a", "b").iterator()));
      UpdateRequest nestedUpdate = new UpdateRequest();
      nestedUpdate.add(nested);
      assertFalse(LBSolrClient.isGracefulRejectionRetryable(nestedUpdate, client));
      assertTrue(LBSolrClient.isGracefulRejectionRetryable(materializedUpdate(), client));
    }
  }

  public void testUnspecifiedRequestIsNotResent() throws Exception {
    try (FakeSolrEndpoint rejecting = new FakeSolrEndpoint(true, false);
        FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
        LBSolrClient lb = newLbClient(false)) {
      rejecting.beginShutdown();
      GenericSolrRequest request =
          new GenericSolrRequest(org.apache.solr.client.solrj.SolrRequest.METHOD.POST, "/update");
      expectThrows(
          GracefulShutdownRejectionException.class,
          () ->
              lb.request(
                  new LBSolrClient.Req(
                      request, List.of(rejecting.endpoint(), healthy.endpoint()))));
      assertEquals(0, healthy.appHits.get());
    }
  }

  public void testV2RequestIsNotResent() throws Exception {
    // Jetty client here: under the test security manager the JDK HttpClient is denied
    // the URLPermission for the header set a V2 request carries, before any I/O.
    try (FakeSolrEndpoint rejecting = new FakeSolrEndpoint(true, false);
        FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
        LBSolrClient lb = newLbClient(true)) {
      rejecting.beginShutdown();
      V2Request request = new V2Request.Builder("/cluster").build();
      expectThrows(
          RemoteSolrException.class,
          () ->
              lb.request(
                  new LBSolrClient.Req(
                      request, List.of(rejecting.endpoint(), healthy.endpoint()))));
      assertEquals(0, healthy.appHits.get());
    }
  }

  public void testNoAlternateEndpointSurfacesFailureWithoutReplay() throws Exception {
    try (FakeSolrEndpoint rejecting = new FakeSolrEndpoint(true, false);
        LBSolrClient lb = newLbClient(false)) {
      rejecting.beginShutdown();
      SolrServerException e =
          expectThrows(
              SolrServerException.class,
              () ->
                  lb.request(
                      new LBSolrClient.Req(materializedUpdate(), List.of(rejecting.endpoint()))));
      assertTrue(SolrException.hasCause(e, GracefulShutdownRejectionException.class));
      assertEquals(1, rejecting.totalHits.get());
      assertEquals(0, rejecting.appHits.get());
    }
  }

  public void testUnmarkedGracefulRejectionFromMixedVersionNodeIsNotRetried() throws Exception {
    for (boolean jetty : new boolean[] {false, true}) {
      try (FakeSolrEndpoint olderNode = new FakeSolrEndpoint(false, false);
          FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
          LBSolrClient lb = newLbClient(jetty)) {
        olderNode.beginShutdown();
        expectThrows(
            RemoteSolrException.class,
            () ->
                lb.request(
                    new LBSolrClient.Req(
                        materializedUpdate(), List.of(olderNode.endpoint(), healthy.endpoint()))));
        assertEquals("client jetty=" + jetty, 1, olderNode.totalHits.get());
        assertEquals("client jetty=" + jetty, 0, olderNode.appHits.get());
        assertEquals("client jetty=" + jetty, 0, healthy.appHits.get());
      }
    }
  }

  public void testForgedMarkerOnApplication503AuthorizesRetry() throws Exception {
    for (boolean jetty : new boolean[] {false, true}) {
      try (FakeSolrEndpoint forgedMarker = new FakeSolrEndpoint(false, true, true);
          FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
          LBSolrClient lb = newLbClient(jetty)) {
        LBSolrClient.Rsp rsp =
            lb.request(
                new LBSolrClient.Req(
                    materializedUpdate(), List.of(forgedMarker.endpoint(), healthy.endpoint())));
        assertNotNull(rsp);
        // The first application handler was reached despite the marker, but the client accepts
        // the header and sends the same update again. This is a deliberate unresolved trust gap.
        assertEquals("client jetty=" + jetty, 1, forgedMarker.appHits.get());
        assertEquals("client jetty=" + jetty, 1, healthy.appHits.get());
      }
    }
  }

  public void testMarkerIsIgnoredWithoutExplicitTrust() throws Exception {
    for (boolean jetty : new boolean[] {false, true}) {
      try (FakeSolrEndpoint forgedMarker = new FakeSolrEndpoint(false, true, true);
          FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
          LBSolrClient lb = newLbClient(jetty, false)) {
        expectThrows(
            RemoteSolrException.class,
            () ->
                lb.request(
                    new LBSolrClient.Req(
                        materializedUpdate(),
                        List.of(forgedMarker.endpoint(), healthy.endpoint()))));
        assertEquals("client jetty=" + jetty, 1, forgedMarker.appHits.get());
        assertEquals("client jetty=" + jetty, 0, healthy.appHits.get());
      }
    }
  }

  public void testUnmarkedApplication503IsNotRetried() throws Exception {
    try (FakeSolrEndpoint appError = new FakeSolrEndpoint(false, true);
        FakeSolrEndpoint healthy = new FakeSolrEndpoint(false, false);
        LBSolrClient lb = newLbClient(false)) {
      RemoteSolrException e =
          expectThrows(
              RemoteSolrException.class,
              () ->
                  lb.request(
                      new LBSolrClient.Req(
                          materializedUpdate(), List.of(appError.endpoint(), healthy.endpoint()))));
      assertEquals(RemoteSolrException.class, e.getClass());
      assertEquals(1, appError.appHits.get());
      assertEquals(0, healthy.appHits.get());
    }
  }
}
