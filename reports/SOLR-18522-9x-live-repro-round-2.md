# SOLR-18522 live reproduction, round 2

Run window: 2026-10-19 04:50Z to 05:04Z
Result: Collection core creation failed before indexing or query execution.
Follow-up to [round 1](SOLR-18522-9x-live-repro-round-1.md).

## Environment and inputs

- Endpoint: `http://solrdbtest11:1601/solr`
- Node: `solrdbtest11.be-md:1601_solr`
- Solr: `9.10.1 c135e6335c7158fa26e96b0dc386f825255b47c0`
- Lucene: `9.12.3 f965e930673c1f5cb478dc6a8907f5fc4ef7b539`
- Configset: `solr18522-combinedquery-r2`
- Configset upload: HTTP 200, `{"responseHeader":{"status":0,"QTime":9}}`
- The supplied production-safe schema was uploaded. SHA-256: `9818e29326a1a14b0185cc340d52ab2175a91cda0f107b35d8607bc38911c2d0`.
- The original solrconfig was left unchanged. SHA-256: `bdb64203349ed90bd5cf5a6674f02dbbd5de3a170605d22be2586305a69e9dbe`.
- Prepared data: 1000 documents, IDs 1 through 1000. SHA-256: `7817d07348d3b7be782bcde2e48750076569a11a16b7355489677e76ce6ba672`. The payload was not posted.

## Collection creation

### cq1

Request: create one shard with configset `solr18522-combinedquery-r2`.

Response:

```text
HTTP 400
{
  "responseHeader": {
    "status": 400,
    "QTime": 269
  },
  "error": {
    "metadata": [
      "error-class",
      "org.apache.solr.common.SolrException",
      "root-error-class",
      "org.apache.solr.common.SolrException"
    ],
    "msg": "Underlying core creation failed while creating collection: cq1",
    "code": 400
  }
}
```

Core log sequence: schema name `repro`; `Loaded schema repro/1.6 with uniqueid field id`; then:

```text
2026-10-19 00:52:10.102 ERROR (qtp939199469-68-solrdbtest11.be-md-4) [ t:solrdbtest11.be-md-4] o.a.s.h.RequestHandlerBase Client exception =>org.apache.solr.common.SolrException: Error CREATEing SolrCore 'cq1_shard1_replica_n1': Unable to create core [cq1_shard1_replica_n1] Caused by: solr.CombinedQueryComponent
```

### cq2

Request: create two shards with configset `solr18522-combinedquery-r2`.

Response:

```text
HTTP 400
{
  "responseHeader": {
    "status": 400,
    "QTime": 263
  },
  "error": {
    "metadata": [
      "error-class",
      "org.apache.solr.common.SolrException",
      "root-error-class",
      "org.apache.solr.common.SolrException"
    ],
    "msg": "Underlying core creation failed while creating collection: cq2",
    "code": 400
  }
}
```

Core log:

```text
2026-10-19 01:03:51.545 ERROR (qtp939199469-339-solrdbtest11.be-md-5) [ t:solrdbtest11.be-md-5] o.a.s.h.RequestHandlerBase Client exception =>org.apache.solr.common.SolrException: Error CREATEing SolrCore 'cq2_shard1_replica_n2': Unable to create core [cq2_shard1_replica_n2] Caused by: solr.CombinedQueryComponent
```

Final collection LIST response:

```json
{
  "responseHeader": {
    "status": 0,
    "QTime": 1
  },
  "collections": [
    "pubmedmcp_reload_test_20260814",
    "pubmedmcp_vectors_only_20260920"
  ]
}
```

Neither `cq1` nor `cq2` exists.

## Indexing and query results

The 1000-document update was not sent. None of the four facet-on/facet-off queries was sent. There are no document counts, `numFound`, `maxScore`, facet counts, or per-document scores for round 2.

## Config observations

The uploaded solrconfig is not defaults-only. Its `/search` handler names `solr.CombinedQuerySearchHandler`; it registers `solr.CombinedQueryComponent`; and `/forcedDistribTest` names `org.apache.solr.handler.component.CombinedQuerySolrCloudTest$ForcedDistribSearchHandler`.

On the server, `jar tf` of the installed `solr-core-9.10.1.jar`, filtered for `CombinedQuery`, returned no entries. The core log shows the supplied schema loading before the `solr.CombinedQueryComponent` core-creation error.
