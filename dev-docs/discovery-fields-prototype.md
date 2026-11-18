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

`/discovery/fields` reports field names and their presence or counts for live
documents matching `q` and `fq` in one core. It does not aggregate shards. In
SolrCloud, callers must supply `distrib=false` to acknowledge that the result
covers only the selected core. A client requiring collection-wide discovery
must not use this response as a collection-wide result.

The handler is configured in the shipped `_default` configset. Existing
collections and other configsets must register it explicitly in `solrconfig.xml`.
The shipped config defaults to `countMode=presence` and sets `timeAllowed` to the
configured maximum (5000 milliseconds by default):

```xml
<requestHandler name="/discovery/fields"
    class="org.apache.solr.handler.admin.DiscoveryFieldsRequestHandler">
  <lst name="defaults">
    <str name="countMode">${solr.discovery.fields.countMode:presence}</str>
    <int name="timeAllowed">${solr.discovery.fields.maxTimeAllowedMs:5000}</int>
  </lst>
</requestHandler>
```

Check whether the handler is enabled rather than assuming it is available from
the Solr version. The optional `fieldGlob` parameter selects field names using
case-sensitive Solr glob matching: `*` matches any sequence and `?` matches one
character. It defaults to `*` and is limited to 256 characters. The pattern is
matched against the whole field name, including reconstructed rank-field names,
before checking presence or counts.

`countMode=presence` checks whether each selected field has a live document in
the `q`/`fq` domain and stops after the first match. Each returned field has a
`presence` value of `present`, `absent`, or `unknown`, plus `presenceSource`.
Unsupported index structures have `presence=unknown`. No per-field document
counts are computed in this mode. Use `countMode=exact` to request document
counts; fields with a supported exact count of zero remain omitted, and
unavailable counts have `docCount: null`, `countAccuracy: unavailable`, and
`presence: unknown`. A caller may override the configured default with
`countMode` on a request.

The handler requires `timeAllowed` from 1 through the configured maximum. The
shipped config defaults it to 5000 milliseconds. It also limits matching fields
and concurrent requests per Solr JVM. Operators can tune these node-level limits
with system properties on each Solr JVM:

| Property | Default | Purpose |
| --- | ---: | --- |
| `solr.discovery.fields.maxFields` | `256` | Maximum matching index field names per request |
| `solr.discovery.fields.maxTimeAllowedMs` | `5000` | Maximum accepted `timeAllowed` |
| `solr.discovery.fields.maxConcurrentRequests` | `1` | Concurrent discovery requests per JVM |
| `solr.discovery.fields.useFilterCache` | `false` | Allow `q`/`fq` domain construction to use the filter cache |

For example, add `-Dsolr.discovery.fields.maxFields=512` to the node's JVM
options to raise the field cap. These values are read when the handler class is
loaded, so restart Solr after changing them. `maxConcurrentRequests` is a
per-JVM limit; total cluster concurrency can reach the per-node limit on each
node. The time limit is cooperative, and these controls do not guarantee a fixed
cluster load. A request that reaches a query limit may have a partial response;
callers must check the response header.

Exact counts are based on supported index structures rather than a scan of
stored fields. Deleted documents are excluded from counts. The domain's
`numFound` remains an exact count of documents matching `q` and `fq` in the
selected core.

Names come from Lucene index fields, with rank fields reconstructed from their
feature names. Other field types that map logical names to different internal
fields can be incomplete or expose internal names. The prototype does not yet
provide a complete logical schema view, detailed field statistics, or a UI.
