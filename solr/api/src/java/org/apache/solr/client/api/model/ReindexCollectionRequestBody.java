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
package org.apache.solr.client.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/** Request body for the v2 reindex-collection API. */
public class ReindexCollectionRequestBody {
  /** The reindexing command to run (the v1 "cmd" parameter), for example "start". */
  @JsonProperty public String cmd;

  @JsonProperty public Boolean removeSource;

  @JsonProperty public String target;

  @JsonProperty public String configName;

  @JsonProperty public Integer numShards;

  @JsonProperty public Integer replicationFactor;

  @JsonProperty public String shards;

  @JsonProperty public Integer rows;

  @JsonProperty public String query;

  @JsonProperty public String fields;

  @JsonProperty public Boolean followAliases;

  @JsonProperty public Map<String, Object> routerProperties;

  @JsonProperty public String async;
}
