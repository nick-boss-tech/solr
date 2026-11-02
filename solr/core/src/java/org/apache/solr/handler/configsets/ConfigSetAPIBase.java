/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.handler.configsets;

import static org.apache.solr.handler.admin.ConfigSetsHandler.CONFIG_SET_TIMEOUT;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.Map;
import org.apache.solr.api.JerseyResource;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ConfigSetParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.EnvUtils;
import org.apache.solr.core.ConfigSetService;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent class for all APIs that manipulate configsets
 *
 * <p>Contains utilities for tasks common in configset manipulation, including running configset
 * "commands".
 */
public class ConfigSetAPIBase extends JerseyResource {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  protected final CoreContainer coreContainer;
  protected final ConfigSetService configSetService;
  protected final SolrQueryRequest solrQueryRequest;
  protected final SolrQueryResponse solrQueryResponse;

  public ConfigSetAPIBase(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    this.coreContainer = coreContainer;
    this.configSetService = coreContainer.getConfigSetService();
    this.solrQueryRequest = solrQueryRequest;
    this.solrQueryResponse = solrQueryResponse;
  }

  protected void runConfigSetCommand(
      SolrQueryResponse rsp,
      ConfigSetParams.ConfigSetAction action,
      Map<String, Object> messageToSend)
      throws Exception {
    if (log.isInfoEnabled()) {
      log.info("Invoked ConfigSet Action :{} with params {} ", action.toLower(), messageToSend);
    }

    coreContainer
        .getZkController()
        .getDistributedCommandRunner()
        .get()
        .runConfigSetCommand(rsp, action, messageToSend, CONFIG_SET_TIMEOUT);
  }

  protected void ensureConfigSetUploadEnabled() {
    if (!EnvUtils.getPropertyAsBool("solr.configset.upload.enabled", true)) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "Configset upload feature is disabled. To enable this, start Solr with '-Dsolr.configset.upload.enabled=true'.");
    }
  }

  public static InputStream ensureNonEmptyInputStream(SolrQueryRequest req) throws IOException {
    Iterator<ContentStream> contentStreamsIterator = req.getContentStreams().iterator();

    if (!contentStreamsIterator.hasNext()) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "No stream found for the config data to be uploaded");
    }

    return contentStreamsIterator.next().getStream();
  }
}
