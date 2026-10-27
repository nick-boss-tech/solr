# Report: SOLR-18325 binary attribution, Linux round 1 (vm2)

Verdict: INCOMPLETE. Released binary compatibility is NOT established. The binary requirement is kept, not waived. Receipt: `receipts/SOLR-18325-binary-attribution-round-1.md`.

## Result

The missing baseline control ran. The same old consumer bytes (`StableConsumer`, compiled against released 10.0.0, SHA-256 `027d21fd…`, unchanged across all legs) were run on Java 17 against three runtimes on a Solr 10.0.0 standalone server.

| Runtime | Result |
| --- | --- |
| Released 10.0.0 (positive control) | PASS: core-status conversion, add/commit/query/delete |
| Unmodified pre-Jackson base `f45b38d5f6c0` | FAIL: `NoSuchMethodError` on `HttpJdkSolrClient$Builder.withConnectionTimeout`, before HTTP |
| Jackson candidate `8bfca8edd30` | FAIL: identical signature, identical stack |

So the builder break is present in the pre-Jackson base. It is inherited, not caused by the Jackson change. The released binary requirement still is not met on either side.

## Why it breaks

- Released 10.0.0: `HttpJdkSolrClient$Builder` extends the top-level `HttpSolrClientBuilderBase`. `HttpJdkSolrClient` extends the top-level `HttpSolrClientBase`. The timeout methods return `LHttpSolrClientBuilderBase;`.
- Base and candidate: the builder extends the nested `HttpSolrClient$BuilderBase`, and `HttpJdkSolrClient` extends `HttpSolrClient`. The timeout methods return `LHttpSolrClient$BuilderBase;`. Both top-level classes are absent.
- The static inventory of the old consumer's 40 org/apache/solr references resolves 40 of 40 on released, 38 of 40 on base, and 38 of 40 on candidate. The only misses on base and candidate are the two timeout methods.
- This matches SOLR-17968 (`e5340a954b6700627b8826431a243c2e2759b0f3`). The Jackson candidate changes no file under `solrj/impl`.

## Decision that follows

Keep the binary requirement. The choice between restoring the top-level classes and adding a shim is still open, and it is a separate prerequisite, not a Jackson repair. Any repair has to cover both `HttpSolrClientBuilderBase` and `HttpSolrClientBase`, because released binaries also link the `HttpJdkSolrClient` superclass. Pick it from descriptor and supertype evidence, not from recreating one class name. No waiver and no shim have been applied.

## Preserved and linked evidence

- The four core tests keep `new ObjectMapper()`. They are Jackson 2 oracles for the server's retained Jackson 2 behavior. Not rerun.
- The unload failure `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` is SOLR-18587. Not fixed or suppressed in this job.

## Not run, labelled

- Maven consumer resolution: `mvn` is not installed on this host. Gradle consumers were used for the resolution graphs. Maven execution is unrun.
- Mutation, default and error-boundary controls: not rerun. Round 2 `baselineBehavior` and `candidateBehavior` lanes passed, logs in `/workspace/gates/logs/vm2/solr18325/harness-21-reports/` and `harness-17-reports/`.
- Public and protected API comparison: not run in this job. Pending.

## Limits

- The inventory checks name and descriptor through the superclass graph. It does not check access rules, so it is linkage-shape evidence, not a complete proof.
- Full server migration, the 3.1 LTS target and release readiness remain unestablished.
- No GitHub, PR, Jira, upstream, or fork write. No commit.

## Artifacts

Raw outputs, hashes, runner, javap-based inventory and the external consumers are in `/workspace/gates/logs/vm2/solr18325-binattr/`.
