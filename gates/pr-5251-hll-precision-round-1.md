# Gate: SOLR-18598 HLL precision compatibility, round 1

Status: OPEN. Immutable once posted. Proves only the exact fork head below.

- Ticket / public PR: SOLR-18598 / apache/solr 5251.
- Required tags: `gate`, `premise-run`, `records`.
- Intended host: any capable Linux gate host with the branch's JDK 21 toolchain, wrapper/build dependencies, and Node/npm access for the reference-guide tasks. One build at a time under that host's lock.
- Fork repository / branch: `nick-boss-tech/solr:solr-18598-submit`.
- Exact head F: `e7834ca954505c27778e59a61140bf02490226a6`.
- Original contributor head P / fail-before production base: `ca539cad196f26c78e14a1dc698b84e0f5c1f22e`, `elangelo/solr:json-facet-hll-precision`.
- F is one ordinary commit above P. Both remote heads and ancestry were verified after the fork push. The public PR still points at P.
- Implementation claim: `claims/pr-5251-hll-precision-windows-round-1.md`.
- Implementation report: `reports/pr-5251-hll-precision-round-1.md`.
- Gate claim: the Linux host adds its own claim before execution, following `WORKFLOW.md`. It records actual JDK/Gradle versions, build lock/runner, positive and negative worktrees, log directory and fresh XML paths. No Linux host has been selected or claimed by Windows.
- Windows verification: static diff/code review and `git diff --check` only. No Gradle, compilation, tidy or tests executed here.

## Scope and acceptance

F rejects different register counts or register widths before `HLL.union` dispatches or mutates either sketch. The JSON facet merger wraps that rejection in a 500 SolrException with actual parameters and `solr.facet.hll.log2m` guidance. Different explicit thresholds and compatible FULL/EXPLICIT representations still merge; the coordinator's local property need not match incoming shards. The guide states the full-restart requirement for a changed `log2m`.

The initially considered metadata-only facet check was not used: `IHLLMetadata` is package-private. F checks the existing HLL fields in the existing union entry point without exposing new API. This also protects other union callers, so the shared-library and StatsComponent neighbors below are required.

Outbound paths relative to P, and permitted repair scope:

1. `changelog/unreleased/SOLR-18598-hll-precision.yml`
2. `solr/core/src/java/org/apache/solr/search/facet/HLLAgg.java`
3. `solr/core/src/java/org/apache/solr/util/hll/HLL.java`
4. `solr/core/src/test/org/apache/solr/search/facet/HLLAggTest.java`
5. `solr/core/src/test/org/apache/solr/util/hll/HLLUnionTest.java`
6. `solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc`

## Preflight and repair rule

Fetch the fork branch and verify it is exactly F before creating a detached positive worktree. Inspect repository instructions and use the host's normal serialized runner. Record head, clean status, environment, commands, exit codes and fresh evidence for every leg.

Run `./gradlew tidy`, then `git diff --exit-code` and `git status --porcelain` at F. A clean tree is required. If tidy changes files, this job does not prove F: commit only the scoped formatting repair under Nick Shanin's ICLA identity, push a plain descendant to the same fork branch, verify its remote SHA, and add a superseding gate file pinning that head before running proof. Copy these requirements into the superseding job. Do not edit this job or force-push. The same rule applies to any source/test repair.

## Required positive legs at F

1. Focused factory, facet-merger and union regressions:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.facet.HLLAggTest --tests org.apache.solr.util.hll.HLLUnionTest -Ptests.seed=5251C0DE`

   Expect 11 executed JUnit tests: ten in HLLAggTest (three original property tests and seven new merger tests), one in HLLUnionTest. Require zero failures, errors and skips. The library method covers all 16 pairs of EMPTY/EXPLICIT/SPARSE/FULL, both mismatch types and both orders, asserting byte-for-byte preservation after rejection. Keep the XML and failure output even on a failed leg.

2. Existing shard and local facet behavior across all six requested method settings:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.facet.TestJsonFacets.testStats --tests org.apache.solr.search.facet.TestJsonFacets.testStatsDistrib -Ptests.nightly=true -Ptests.seed=5251C0DE`

   Require both methods for all six parameters, ordinarily 12 executed cases, with zero failures, errors and skips. Report the actual case names and counts. These are requested method settings: STREAM/ENUM fall back when numBuckets is enabled, not distinct streaming algorithms. Do not enable Monster tests or expand this to an unrelated settling sweep.

3. Shared HLL and StatsComponent compatibility neighbors:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.util.hll.ExplicitHLLTest --tests org.apache.solr.util.hll.SparseHLLTest --tests org.apache.solr.util.hll.FullHLLTest --tests org.apache.solr.util.hll.BitVectorTest --tests org.apache.solr.handler.component.StatsComponentTest.testCardinality --tests org.apache.solr.handler.component.StatsComponentTest.testHllOptions --tests org.apache.solr.handler.component.StatsComponentTest.testHllOptionsErrors -Ptests.seed=5251C0DE`

   Require zero failures, errors and skips, recording actual counts. These verify compatible existing union callers continue to work.

4. Root checks and guide validation:

   `./gradlew check -x test`

   `./gradlew :solr:solr-ref-guide:checkSiteLinks`

   Require both commands to pass. The latter depends on the local guide build and validates its links. Record any material environment setup such as unsetting NODE_ENV; do not describe an earlier failure as a pass. No silent exclusions or skipping the guide check. Distinguish unrelated baseline/environment problems from patch failures in the report.

5. Final hygiene: `git diff --check`, `git diff --exit-code`, clean status and head F. Validate the changelog YAML, with authors Samuel Verstraete and Nick Shanin, name only, no nick lines. Record the author of F as Nick Shanin, with no attribution trailer in F's commit; preserve the contributor's original P history.

## Required fail-before leg

Create a separate detached negative worktree at P. Overlay only these two test files from F:

- `solr/core/src/test/org/apache/solr/search/facet/HLLAggTest.java`
- `solr/core/src/test/org/apache/solr/util/hll/HLLUnionTest.java`

Keep both production files at P: HLLAgg.java and HLL.java. Record the synthetic tree, test blob hashes and production identity. No new API stubs are needed. Run:

`./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.facet.HLLAggTest.testMergeRejectsLargerLog2m --tests org.apache.solr.search.facet.HLLAggTest.testMergeRejectsSmallerLog2m --tests org.apache.solr.search.facet.HLLAggTest.testMergeRejectsDifferentRegisterWidths --tests org.apache.solr.search.facet.HLLAggTest.testMergeRejectsDifferentLog2mBeforePromotion --tests org.apache.solr.util.hll.HLLUnionTest.testUnionRejectsIncompatiblePrecisionWithoutMutation -Ptests.seed=5251C0DE`

Require exactly these five tests to execute and fail at their rejection assertions. Larger-log2m-first and smaller-log2m-first are separate methods so both original failure paths are demonstrated. The old smaller-source path may be reported as an unexpected ArrayIndexOutOfBoundsException by expectThrows; the other cases ordinarily report that the expected exception was absent. Record actual messages and causes. A compilation failure, initialization error, skip, missing method or unrelated failure is not fail-before proof. Passing methods are not negative proof and must be investigated before claiming GREEN.

## Completion

GREEN requires all positive legs at the exact final head, clean tidy and tree, the five discriminating fail-before failures, and retained logs/fresh XML. Executed failures require investigation and a recorded repair before retry; do not blindly rerun or loosen acceptance. A genuine setup refusal before execution follows WORKFLOW.md's environment rule.

The gate host writes `receipts/SOLR-18598.md`, names this job and its claim, records final tested/pushed SHA and any superseding job, commands, UTC times, seeds, counts, exception messages and evidence paths. Finish with a concise suggested contributor follow-up draft for owner review. Public GitHub/JIRA updates are outside this gate job. Keep the contribution **Implemented, Proof Pending** until this gate completes successfully at the offered head.
