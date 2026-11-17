<!--
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements. See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

# Field discovery prototype

`/discovery/fields` reports field names and counts for live documents matching
`q` and `fq` in one core. It does not aggregate shards. In SolrCloud, callers
must supply `distrib=false` to acknowledge that the result covers only the
selected core. A client requiring collection-wide discovery must not use this
response as a collection-wide result.

The handler is configured in the shipped `_default` configset. Existing
collections and other configsets must register it explicitly in `solrconfig.xml`:

```xml
<requestHandler name="/discovery/fields"
    class="org.apache.solr.handler.admin.DiscoveryFieldsRequestHandler" />
```

Check whether the handler is enabled rather than assuming it is available from
the Solr version. Requests must include a `timeAllowed` value from 1 to 5000
milliseconds. The prototype rejects requests when another discovery request is
running in the same JVM or when the core has more than 256 discoverable fields.
Limits can result in a partial response; callers must check the response header.
These limits reduce workload but do not guarantee a particular cluster load.

The response omits fields with an exact count of zero. A field whose count is
unavailable remains in the response with `docCount: null` and
`countAccuracy: unavailable`; its presence among the matching documents is
unknown. Counts are based on supported index structures rather than a scan of
stored fields. Deleted documents are excluded from counts.

Names come from Lucene index fields, with rank fields reconstructed from their
feature names. Other field types that map logical names to different internal
fields can be incomplete or expose internal names. The prototype does not yet
provide a complete logical schema view, detailed field statistics, or a UI.
