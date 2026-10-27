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

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.util.Set;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.params.CollectionAdminParams;
import org.apache.solr.common.params.CommonAdminParams;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.ShardParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple/direct {@link SolrClient} using HTTP.
 *
 * <p>The protected methods can be considered as internal / unstable APIs.
 *
 * @see #builder(String)
 * @see "org.apache.solr.client.solrj.jetty.HttpJettySolrClient"
 * @see org.apache.solr.client.solrj.impl.HttpJdkSolrClient
 */
public abstract class HttpSolrClient extends HttpSolrClientBase {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * See {@link #getUrlParamNames()}. Default set are interesting for routing or fundamental request
   * purpose
   */
  public static final Set<String> DEFAULT_URL_PARAM_NAMES =
      Set.of(
          CoreAdminParams.ACTION,
          CommonAdminParams.ASYNC,
          CollectionAdminParams.COLLECTION,
          "name", // core/collection name
          "command", // e.g. for replication
          ShardParams.IS_SHARD,
          CommonParams.DISTRIB,
          ShardParams._ROUTE_,
          ShardParams.SHARDS_PREFERENCE,
          ShardParams.SHARDS_PURPOSE);

  protected HttpSolrClient(String serverBaseUrl, BuilderBase<?, ?> builder) {
    super(serverBaseUrl, builder);
  }

  @Override
  public BuilderBase<?, ?> builder() {
    return toBuilder(getBaseURL());
  }

  /**
   * @lucene.internal
   */
  protected abstract BuilderBase<?, ?> toBuilder(String baseUrl);

  // If the Jetty-based HttpJettySolrClient builder is on the classpath, this will be its no-arg
  // constructor; otherwise it will be null and we will fall back to the JDK HTTP client.
  private static final Constructor<? extends BuilderBase<?, ?>> HTTP_JETTY_SOLR_CLIENT_BUILDER_CTOR;

  static {
    Constructor<? extends HttpSolrClient.BuilderBase<?, ?>> ctor = null;
    try {
      @SuppressWarnings("unchecked")
      Class<? extends HttpSolrClient.BuilderBase<?, ?>> builderClass =
          (Class<? extends HttpSolrClient.BuilderBase<?, ?>>)
              Class.forName("org.apache.solr.client.solrj.jetty.HttpJettySolrClient$Builder");
      ctor = builderClass.getDeclaredConstructor();
      ctor.newInstance(); // perhaps fails because Jetty libs aren't on the classpath
    } catch (Throwable t) {
      // Class not present or incompatible; leave ctor as null to indicate unavailability
      if (log.isTraceEnabled()) {
        log.trace(
            "HttpJettySolrClient$Builder not available on classpath; will use HttpJdkSolrClient",
            t);
      }
    }
    HTTP_JETTY_SOLR_CLIENT_BUILDER_CTOR = ctor;
  }

  /**
   * Provides a new builder of an {@link HttpSolrClient}. The implementation will try to create a
   * {@code org.apache.solr.client.solrj.jetty.HttpJettySolrClient} if available, otherwise will
   * fall back on {@link HttpJdkSolrClient}.
   *
   * @param baseUrl for {@link BuilderBase#withBaseSolrUrl(String)}.
   */
  public static BuilderBase<?, ?> builder(String baseUrl) {
    HttpSolrClient.BuilderBase<?, ?> builder;
    if (HTTP_JETTY_SOLR_CLIENT_BUILDER_CTOR != null) {
      try {
        log.debug("Using HttpJettySolrClient as the delegate http client");
        builder = HTTP_JETTY_SOLR_CLIENT_BUILDER_CTOR.newInstance();
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      log.debug("Using HttpJdkSolrClient as the delegate http client");
      builder = new HttpJdkSolrClient.Builder();
    }
    return builder.withBaseSolrUrl(baseUrl);
  }

  /**
   * Builder for {@link HttpSolrClient} implementations. The shared builder state and methods live
   * on {@link HttpSolrClientBuilderBase}, restored as a top-level class for binary compatibility;
   * this nested class narrows the client type to {@link HttpSolrClient}.
   */
  public abstract static class BuilderBase<B extends BuilderBase<?, ?>, C extends HttpSolrClient>
      extends HttpSolrClientBuilderBase<B, C> {

    @Override
    public abstract C build();
  }
}
