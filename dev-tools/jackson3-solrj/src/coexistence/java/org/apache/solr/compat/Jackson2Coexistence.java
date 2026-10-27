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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/** An application may deliberately retain its own Jackson 2 alongside migrated SolrJ. */
public class Jackson2Coexistence {
  public static void main(String[] args) throws Exception {
    var mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(Map.of("application", "jackson2"));
    if (!"jackson2".equals(mapper.readValue(json, Map.class).get("application"))) {
      throw new AssertionError("Application Jackson 2 round trip");
    }
    Class.forName("tools.jackson.databind.ObjectMapper");
    StableConsumer.main(new String[0]);
    System.out.println("PASS application Jackson 2 and candidate SolrJ Jackson 3 coexistence");
  }
}
