# Addendum: SOLR-18600 premise review, 10.1 consumer check (vm2, round 1)

Adds to `assignments/vm2-solr-18600-premise-review-round-1.md` (board `a6af3056725`). Add-only.

## Test

Published `solr-solrj-10.1.0.jar` from Maven Central, consumers compiled against it, run against three builds of `branch_10x` source: base `a336bf871b2c`, the four core files from `c40665af8d6b`, and all ten changed source files.

| Consumer (compiled against 10.1.0) | Base | Four core files | All ten files |
|---|---|---|---|
| `HttpJdkSolrClient.Builder.withConnectionTimeout` (the ticket's consumer) | CONSUMER OK | NoSuchMethodError: `HttpJdkSolrClient$Builder.withConnectionTimeout` returns `HttpSolrClient$BuilderBase` | same NoSuchMethodError |
| `CloudHttp2SolrClient.Builder.withHttpClientBuilder` | CLOUD BUILDER OK | CLOUD BUILDER OK | CLOUD BUILDER OK |

## Reading

- The branch breaks 10.1-compiled callers of the HttpJdk builder. The review agent's finding holds for this path.
- The 10.0-compiled consumer passes on the branch (from the earlier bisect). The branch trades 10.0 callers for 10.1 callers on this method.
- The Cloud builder call tested here does not break on the branch. The review's "Cloud getters" and LB claims are not covered by this test.

## Not tested

- Cloud getters (`CloudHttp2SolrClient.getHttpClient`) and `withHttpClient` (not in 10.1).
- LB builders and ConcurrentUpdate APIs.
- A consumer compiled against 10.1.0 with the full Linux test suite.

Artifacts (vm2): `gates/logs/vm2/solr18600-probe/out/c101-*`.

No GitHub, JIRA, or mailing list action taken.
