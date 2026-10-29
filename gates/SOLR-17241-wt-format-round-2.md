# Gate: SOLR-17241 wt response format on non-hosting collection nodes, round 2

Status: OPEN, claimed by vm5 (see `claims/SOLR-17241-wt-format-gate-round-2-vm5.md`). Public comment HOLD.

- Supersedes: `gates/SOLR-17241-wt-format-round-1.md`. Round 1 pinned `bded8997e57e7cc0974aa766251735f93f289f87`; its tidy preflight reflowed one line in `solr/core/src/test/org/apache/solr/jersey/MediaTypeOverridingFilterTest.java`, so round 1 proof is not run and is not claimed.
- Tags: gate, review, records.
- Fork: nick-boss-tech/solr, branch solr-17241-submit.
- Candidate (gated head): `7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e`, verified by `git ls-remote` before the claim. It is `bded8997e57e7cc0974aa766251735f93f289f87` plus one tidy-only commit, `SOLR-17241: Wrap the filter test to the tidy line length`, author Nick Shanin, no trailers.
- Parent / original production control: `9859d8bb2e8d63061116b8281639a4beb1be480c`.
- Seed: `17241C0FFEE17241`. Preserve the same seed across each before/after pair.
- Host: vm5. JDK `/workspace/jdk/jdk-21.0.12.1+1`. Gradle runs under `flock /workspace/tools/.gradle-vm5.lock` (the host-lock name follows AGENTS.md, which uses the host id, not `hostname`).
- Logs: `/workspace/gates/logs/vm5/solr17241-wt-format-round-2/`. Idempotency key: `SOLR-17241-wt-format-round-2@7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e`.
- Deliverables: `receipts/SOLR-17241.md` and `reports/SOLR-17241-wt-format-linux-round-2.md`. Draft PR description in the receipt, NOT POSTED.

## Required proof

Follow the required candidate proof and the required original-production controls in `gates/SOLR-17241-wt-format-round-1.md` exactly, with these identities:

1. Tidy: `./gradlew tidy` leaves a clean tracked tree at `7a5a998ee9`. Changelog `changelog/unreleased/SOLR-17241-wt-non-hosting-node.yml` parses.
2. Compile with `-Pvalidation.errorprone=true`: `:solr:api:compileJava`, `:solr:solrj:compileJava`, `:solr:core:compileJava`, `:solr:core:compileTestJava`.
3. Candidate: `org.apache.solr.jersey.MediaTypeOverridingFilterTest` (all 8 methods) and `org.apache.solr.handler.V2ApiIntegrationTest` (must execute `testReloadHonorsWtOnNonHostingNode`) pass, with fresh XML preserved per leg.
4. `:solr:core:check -x test` and root `check -x test`, recorded separately.
5. Controls, in a disposable tree derived from the candidate with only `solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java` replaced by the parent version (diff and hashes recorded, nothing committed or pushed):
   - `MediaTypeOverridingFilterTest.testWtFromQueryParamsWithoutSolrQueryRequest` fails by assertion.
   - `V2ApiIntegrationTest.testReloadHonorsWtOnNonHostingNode` passes at the hosting node and fails at the non-hosting request with the javabin-versus-application/json mismatch.

PASS requires all candidate steps and both controls at these identities, plus a clean final status and remote-head verification at `7a5a998ee9`.
