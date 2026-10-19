# SOLR-18522 9x live reproduction, round 1

Run date: 2026-10-18 (America/New_York)
Server endpoint: `http://solrdbtest11:1601/solr`
Server node: `solrdbtest11.be-md:1601_solr`

## Server version

The authenticated `SYSTEM` response reported:

- Solr spec version: `9.10.1`
- Solr implementation version: `9.10.1 c135e6335c7158fa26e96b0dc386f825255b47c0 - gerlowskija - 2026-01-15 09:36:55`
- Lucene spec version: `9.12.3`
- Lucene implementation version: `9.12.3 f965e930673c1f5cb478dc6a8907f5fc4ef7b539 - 2025-09-24 07:44:31`
- Java: `21.0.12 21.0.12+8-LTS`
- Mode: `solrcloud`
- Solr home: `/data/solr/pubmedmcp_1`
- ZooKeeper path: `/nosql/solr/pubmedmcp`

The server reports release 9.10.1. The test config files below were fetched from `upstream/branch_9x` at `3511867c3e9e3bc9f5977f3767d6d5276f751b6f`.

## Test config and data

The configset `solr18522-combinedquery` was uploaded with the test's config files:

- `solr/core/src/test-files/solr/collection1/conf/solrconfig-combined-query.xml` as `solrconfig.xml`, SHA-256 `bdb64203349ed90bd5cf5a6674f02dbbd5de3a170605d22be2586305a69e9dbe`
- `solr/core/src/test-files/solr/collection1/conf/schema-vector-catchall.xml` as `schema.xml`, SHA-256 `6491c53d767b7d8e1dbdd7fe21b7f7de03ab455abecfe57ec2e0760b64745339`

The schema already contains the `*_sdv` dynamic field at line 729: type `string`, indexed, stored, docValues enabled, and single-valued. No schema field was added or changed.

The 1000-document JSON payload was prepared with IDs 1 through 1000, `color_sdv=red` for even IDs, and `color_sdv=blue` for odd IDs. Local file: `C:\Users\shaninna\dev\securecrt-docs-bundle\outputs\securecrt-ssh-codex-setup\outputs\solr-18522-live-repro\documents.json`. Remote staging file: `/home/shaninna/solr18522-repro-48cd06a9/documents.json`. No update request was sent to Solr.

Configset upload response:

```text
HTTP 200
{"responseHeader":{"status":0,"QTime":93}}
```

The collection LIST response after the create attempts was:

```json
{
  "responseHeader": {
    "status": 0,
    "QTime": 0
  },
  "collections": [
    "pubmedmcp_reload_test_20260814",
    "pubmedmcp_vectors_only_20260920"
  ]
}
```

## Collection creation responses

### cq1

Create request: one shard, replication factor 1, node `solrdbtest11.be-md:1601_solr`, configset `solr18522-combinedquery`.

First response:

```json
{
  "responseHeader": {
    "status": 400,
    "QTime": 492
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

The core log error was:

```text
Error CREATEing SolrCore 'cq1_shard1_replica_n1': Unable to create core [cq1_shard1_replica_n1] Caused by: No system property or default value specified for solr.tests.numeric.dv value:${solr.tests.numeric.dv}
```

A second create request passed the test properties `solr.tests.numeric.dv=false` and the Trie integer, float, long, double, and date field type names. Response:

```json
{
  "responseHeader": {
    "status": 400,
    "QTime": 255
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

The core log error was:

```text
Error CREATEing SolrCore 'cq1_shard1_replica_n1': Unable to create core [cq1_shard1_replica_n1] Caused by: solr.MockTokenizerFactory
```

### cq2

Create request: two shards, replication factor 1, nodes `solrdbtest11.be-md:1601_solr` and `solrdbtest11.be-md:1602_solr`, configset `solr18522-combinedquery`, with the same test properties listed for the second `cq1` request.

Response:

```json
{
  "responseHeader": {
    "status": 400,
    "QTime": 289
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

The core log error was:

```text
Error CREATEing SolrCore 'cq2_shard1_replica_n2': Unable to create core [cq2_shard1_replica_n2] Caused by: solr.MockTokenizerFactory
```

Authenticated CLUSTERSTATUS responses had `status: 400` and these error messages:

- `Collection: cq1 not found`
- `Collection: cq2 not found`

## Query requests and results

Both request bodies were posted as `application/json` to `/solr/<collection>/search?wt=json`.

Facet-on body:

```json
{"queries":{"lexical1":{"lucene":{"query":"id:(1 OR 2)"}},"lexical2":{"lucene":{"query":"id:(3 OR 4)"}}},"limit":5,"fields":["id","score"],"params":{"combiner":true,"combiner.query":["lexical1","lexical2"],"facet":true,"facet.field":"color_sdv"}}
```

Facet-off body:

```json
{"queries":{"lexical1":{"lucene":{"query":"id:(1 OR 2)"}},"lexical2":{"lucene":{"query":"id:(3 OR 4)"}}},"limit":5,"fields":["id","score"],"params":{"combiner":true,"combiner.query":["lexical1","lexical2"]}}
```

| Collection | Variant | HTTP | Documents returned | `numFound` | `maxScore` | Facets | Document IDs and scores |
| --- | --- | ---: | --- | --- | --- | --- | --- |
| cq1 | Facet on | 404 | No Solr query response | Not present | Not present | Not present | Not present |
| cq1 | Facet off | 404 | No Solr query response | Not present | Not present | Not present | Not present |
| cq2 | Facet on | 404 | No Solr query response | Not present | Not present | Not present | Not present |
| cq2 | Facet off | 404 | No Solr query response | Not present | Not present | Not present | Not present |

Each of the four query requests returned this response body:

```html
<p>
  Searching for Solr?<br/>
  You must type the correct path.<br/>
  Solr will respond.
</p>
```

Expected by the test: 4 documents; facet `color_sdv`: blue 2, red 2.
