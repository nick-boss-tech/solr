# Report: SOLR-18325 Solr 10 client feasibility, vm2 round 2

Verdict: INCOMPLETE for the client-first approach. Full server migration NOT ESTABLISHED. Release readiness NOT ESTABLISHED. Receipt: `r2/receipt.md`.

## What passed at 8bfca8edd30

- Tidy and the Error Prone compile.
- Focused SolrJ: six classes green.
- Published candidate artifact: `solr-solrj` 10.2.0-SNAPSHOT, runtime graph with Jackson 3.2.1 and no Jackson 2 core or databind.
- Harness on Java 21 and 17: every consumer lane passes; the internal-field control reports the expected `NoSuchFieldError`.
- HTTP `candidateHttp` against Solr 10.0.0 standalone, both Java 17 consumer and real server responses.
- `check -x test` rc 0 in a clean full clone.

## What blocks the verdict

- `binaryHttp`: released 10.0.0 consumer bytecode fails with `NoSuchMethodError` on `HttpJdkSolrClient$Builder.withConnectionTimeout`. Released 10.0.0 has a top-level `HttpSolrClientBuilderBase`; the base nests it as `HttpSolrClient.BuilderBase`. The prototype doesn't touch that file. This is Solr source evolution, so the client-first verdict cannot be a complete pass.

## Inherited, not caused by the prototype

- `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` fails on base `f45b38d5f6c0` at the same seed. It is one failure in the 33-test core focused set.

## Decision for the owner

Restore a top-level `HttpSolrClientBuilderBase` (or add a shim) on the Solr branch, then rerun `binaryHttp`. This touches Solr source outside the prototype, so it's your call. Nothing has been posted to GitHub or Jira.
