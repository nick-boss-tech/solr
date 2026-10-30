# Gate: PR 2452 UBI recording context fix, Linux round 2 (superseding)

Status: OPEN (exact-head gate; proof pending)

- Supersedes: `gates/pr-2452-ubi-context-round-1.md`, whose tidy clean-tree check failed on one rewrapped line of the test at `37be6eedfe`.
- Assignment: `assignments/pr-2452-ubi-context-rework-and-gate-round-1.md`. Claim: `claims/pr-2452-ubi-context-rework-vm4-round-1.md`. Executing host: vm4.
- Contribution base B: apache/solr PR 2452 head `6402b4b528d3ec8daa134772abc84532380e3f7c`.
- Candidate F: `bbc00109d07b9d53e4c30659c28e626c48dcfca4` on `nick-boss-tech/solr:ubi-pr2452-context-submit`. Two commits above B: `37be6eedfe` (UBI: isolate recording context per request, the ported fix plus test) and `bbc00109d0` (UBI: format recording context test, one rewrapped line, formatting only).
- Final regression test blob: `solr/core/src/test/org/apache/solr/handler/component/UBIRecordingContextTest.java` at `886244b0025ac22b747dfcaa3bf1b5a963bfc618`. The same blob is overlaid on B for fail-before and runs unchanged on F.
- Ported `UBIComponent.java` blob: `b08770ef5f41c352a4098618924b03e3f7a3e5ce`, identical to the source fix.
- Environment: standalone full clone `/workspace/worktrees/pr2452-clone`. JDK `/workspace/tools/jdk21` (Temurin 21.0.12.1). Gradle 9.7.0 through the repo wrapper. Every Gradle command `--no-daemon --no-build-cache` under host lock `/workspace/tools/.gradle-vm4.lock`. Root check runs with `env -u NODE_ENV`. The driver reaps orphan Gradle daemons from this gate before each step.
- Driver: `/workspace/gates/logs/vm4/pr2452-ubi-context-round-2/run.sh`.

## Legs

1. Fail-before: B with only the final test blob overlaid, `:solr:core:test --tests org.apache.solr.handler.component.UBIRecordingContextTest -Ptests.seed=11994C01`. Expected shape as in round 1: 5 executed; both overlap cases fail at the identity comparison, expected `[query-B, query-A]`, actual `[query-B, query-B]`.
2. Restore: tree returns clean to B, then checkout F.
3. Pass-after at F: `UBIRecordingContextTest`, seeds `11994C01` and `11994C02`, zero skips, failures, or errors.
4. Neighbors at F, seed `11994C01`: `UBIComponentTest`, `UBIComponentStreamingQueriesTest`, `LoggingStreamTest`, `TestSolrConfigHandler`, `TestSubQueryTransformerDistrib`.
5. Formatting and checks at F: `tidy`, clean-tree check, `git diff --check B F`, root `check -x test` with NODE_ENV unset, final clean tree at F.

Any failing or skipped result, or any tidy change, stops the run and is recorded; no retry of an executed failing test.

## Interpretation

Same boundary as round 1: recording-context isolation on the PR 2452 UBI feature at B and F only. Not readiness of the full draft, a listener migration, background ingestion, multi-shard merging, redaction, or production overhead.
