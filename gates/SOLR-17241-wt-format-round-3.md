# Gate: SOLR-17241 wt response format on non-hosting collection nodes, round 3

Status: OPEN, claimed by vm5 (see `claims/SOLR-17241-wt-format-gate-round-3-vm5.md`). Public comment HOLD.

- Supersedes: `gates/SOLR-17241-wt-format-round-2.md`. Round 2 pinned `7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e` and FAILED at `V2ApiIntegrationTest.testReloadHonorsWtOnNonHostingNode` (javabin writer NPE, `req` null, non-hosting node). Receipt: `receipts/SOLR-17241.md` at round 2.
- Fork: nick-boss-tech/solr, branch `solr-17241-submit`.
- Candidate (gated head): `86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`, verified by `git ls-remote` before the claim. Head chain: `bded8997e5` (implementation) → `7a5a998ee9` (tidy-only line wrap) → `86d86e3d3e` (source repair).
- Source repair scope: `solr/core/src/java/org/apache/solr/jersey/MessageBodyWriters.java` only. When the request context has no `SOLR_QUERY_REQUEST` (non-hosting collection path), `BaseMessageBodyWriter.writeTo` builds `new SolrQueryRequestBase(null, <SOLR_PARAMS>)` instead of passing null to the javabin/XML/CSV writers. No routing, servlet, request-body or SOLR-16398 change. Author Nick Shanin, no trailers.
- Parent / original production control: `9859d8bb2e8d63061116b8281639a4beb1be480c`.
- Seed: `17241C0FFEE17241`.
- Host: vm5. JDK `/workspace/jdk/jdk-21.0.12.1+1`. Gradle under `flock /workspace/tools/.gradle-vm5.lock`, `env -u NODE_ENV`.
- Logs: `/workspace/gates/logs/vm5/solr17241-wt-format-round-3/`. Idempotency key: `SOLR-17241-wt-format-round-3@86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`.
- Deliverables: `receipts/SOLR-17241.md` (round 3 section) and `reports/SOLR-17241-wt-format-linux-round-3.md`. Draft PR description in the receipt, NOT POSTED.

## Required proof

Same steps as round 2 (`gates/SOLR-17241-wt-format-round-2.md`), pinned to the head above:

1. Tidy leaves a clean tree; changelog parses.
2. Compile with `-Pvalidation.errorprone=true`: `:solr:api:compileJava`, `:solr:solrj:compileJava`, `:solr:core:compileJava`, `:solr:core:compileTestJava`.
3. Candidate: `MediaTypeOverridingFilterTest` (8 methods) and `V2ApiIntegrationTest` (must execute `testReloadHonorsWtOnNonHostingNode`) pass, fresh XML per leg.
4. `:solr:core:check -x test` and root `check -x test`, recorded separately.
5. Controls: disposable tree from the candidate with only `MediaTypeOverridingFilter.java` replaced by the parent blob. Expected: the filter test fails by assertion (`text/plain` survives), and the integration test fails at the non-hosting request with the `application/json` MIME mismatch (not an NPE, not a setup failure).

PASS requires all candidate steps and both controls at these identities, plus clean final status and remote-head verification at `86d86e3d3e`.
