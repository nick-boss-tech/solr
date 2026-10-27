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

import org.apache.solr.client.solrj.request.json.JacksonContentWriter;

/** Negative control for the internal mapper field's changed JVM descriptor. */
public class InternalFieldControl {
  public static void main(String[] args) throws Exception {
    boolean expectFailure = args.length == 0;
    Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
    try {
      Object mapper = JacksonContentWriter.DEFAULT_MAPPER;
      if (expectFailure) {
        throw new AssertionError("Expected old internal field descriptor to fail, got " + mapper);
      }
      System.out.println("PASS internal field negative control resolves on baseline");
    } catch (NoSuchFieldError expected) {
      if (!expectFailure) throw expected;
      System.out.println("EXPECTED internal DEFAULT_MAPPER binary incompatibility: " + expected);
    }
  }
}
