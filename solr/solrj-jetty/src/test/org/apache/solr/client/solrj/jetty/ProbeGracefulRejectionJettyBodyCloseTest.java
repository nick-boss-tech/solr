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
package org.apache.solr.client.solrj.jetty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.impl.GracefulShutdownRejectionException;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.eclipse.jetty.client.Response;
import org.eclipse.jetty.http.HttpFields;

public class ProbeGracefulRejectionJettyBodyCloseTest extends SolrTestCase {
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

  public void testMarkedJettyResponseClosesStreamParserBody() throws Exception {
    CloseTrackingInputStream body = new CloseTrackingInputStream();
    HttpFields.Mutable headers = HttpFields.build();
    headers.put(
        GracefulShutdownRejectionException.MARKER_HEADER,
        GracefulShutdownRejectionException.MARKER_VALUE);
    Response response =
        (Response)
            Proxy.newProxyInstance(
                Response.class.getClassLoader(),
                new Class<?>[] {Response.class},
                (proxy, method, args) -> {
                  if (method.getName().equals("getStatus")) {
                    return 503;
                  }
                  if (method.getName().equals("getHeaders")) {
                    return headers;
                  }
                  return null;
                });

    try (HttpJettySolrClient client =
        new HttpJettySolrClient.Builder("http://127.0.0.1:1/solr")
            .withTrustedGracefulShutdownRejections(true)
            .useHttp1_1(true)
            .build()) {
      expectThrows(
          GracefulShutdownRejectionException.class,
          () ->
              client.processErrorsAndResponse(
                  new UpdateRequest(), response, body, "http://127.0.0.1/solr/core1"));
    }
    assertTrue("Jetty marker processing must close the response stream", body.closed);
  }
}
