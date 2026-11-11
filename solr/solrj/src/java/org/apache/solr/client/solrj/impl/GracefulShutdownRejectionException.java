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

import org.apache.solr.common.SolrException;

/** Signals a shutdown rejection received over a path the caller has chosen to trust. */
public class GracefulShutdownRejectionException extends SolrException {

  /** Response header a gracefully shutting-down server sets on pre-handler rejections. */
  public static final String MARKER_HEADER = "X-Solr-Graceful-Shutdown";

  public static final String MARKER_VALUE = "rejected";

  public GracefulShutdownRejectionException(String remoteHost, String detail) {
    super(ErrorCode.SERVICE_UNAVAILABLE, remoteHost + ": " + detail);
  }
}
