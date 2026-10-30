# Gate: PR 2452 UBI recording context fix, Linux round 1

Status: OPEN (exact-head gate; proof pending)

- Assignment: `assignments/pr-2452-ubi-context-rework-and-gate-round-1.md`. Claim: `claims/pr-2452-ubi-context-rework-vm4-round-1.md`. Executing host: vm4.
- Board snapshot at gate creation: this commit on `coordination-ui`. Assignment snapshot `238336be0a`.
- Contribution base B (preferred path): apache/solr PR 2452 head `6402b4b528a3ec8daa134772abc84532380e3f7c` (live `refs/pull/2452/head` and `epugh/solr:ubi` both at this head at claim time). Parent upstream: `87fbf513b6`.
- Candidate F: `37be6eedfe8696712265424f1e7fb59109d2aac9` on `nick-boss-tech/solr:ubi-pr2452-context-submit`, one commit above B, message `UBI: isolate recording context per request`. Diff against B touches exactly two files.
- Source of the port: `09343928e7d2ba5cb5e7def1d0a550337b67a7d2` on `nick-boss-tech/solr:solr-11994-ubi-context-submit`, the I0..F0 fix. Ported blobs are identical to the source: `UBIComponent.java` `b08770ef5f`, `UBIRecordingContextTest.java` `1fd0311dde`.
- Final regression test blob (overlaid on B for fail-before): `solr/core/src/test/org/apache/solr/handler/component/UBIRecordingContextTest.java` at blob `1fd0311dde4b028b833a3190d31ae2a34f75896d`.
- Not touched: listener hook, `SearchRequestListenerUbiTest`, PR 5238 branches, `solr-11994-ubi-context-submit`.
- Environment: standalone full clone `/workspace/worktrees/pr2452-clone`. JDK `/workspace/tools/jdk21` (Temurin 21.0.12.1). Gradle 9.7.0 through the repo wrapper. Every Gradle command `--no-daemon --no-build-cache` under host lock `/workspace/tools/.gradle-vm4.lock`. Root check runs with `env -u NODE_ENV`.
- Driver: `/workspace/gates/logs/vm4/pr2452-ubi-context-round-1/run.sh`, with per-step `.log`, `.rc`, `state.txt`, and XML copies in `<step>-xml/`.

## Legs

1. **Fail-before** (`baseline-fail-before`): checkout B, overlay only the final test blob, no UBIComponent change. Run `:solr:core:test --tests org.apache.solr.handler.component.UBIRecordingContextTest -Ptests.seed=11994C01`. Expected shape: five tests executed; three controls pass; both overlap cases fail at the identity comparison (expected `[query-B, query-A]`, actual `[query-B, query-B]`). A compile error, skip, timeout, or different failure is not proof. If the baseline passes, the result is reported as non-reproduction.
2. **Restore check** (`baseline-clean`): tree returns to clean B before the candidate checkout.
3. **Pass-after**: checkout F. `UBIRecordingContextTest` at seeds `11994C01` and `11994C02`, zero skips, failures, or errors; all five methods executed.
4. **Neighbors** at F, seed `11994C01`: `UBIComponentTest`, `UBIComponentStreamingQueriesTest`, `LoggingStreamTest`, `TestSolrConfigHandler`, `TestSubQueryTransformerDistrib`. Paths confirmed present on F.
5. **Formatting and checks** at F: `./gradlew tidy`, clean-tree check, `git diff --check B F`, root `check -x test` with NODE_ENV unset, final clean tree at F.

A failure in legs 3 to 5 is recorded and preserved, not retried. A tidy change to any file stops the run for a superseding head.

## Interpretation

This gate tests the isolation of recording context on the PR 2452 UBI feature at B and F only. It does not establish readiness of the full UBI draft, a listener migration, background ingestion, multi-shard merging, redaction, or production overhead. Any public offer must state that it addresses recording-context isolation only.

## Erratum (add-only)

- The full SHA for B in this record was written as `6402b4b528a3ec8daa134772abc84532380e3f7c`. The correct full SHA is `6402b4b528d3ec8daa134772abc84532380e3f7c`, matching `git ls-remote` for `refs/pull/2452/head`. The short form `6402b4b528` is unambiguous.
- A driver launched with the wrong SHA failed the baseline step at once (`fatal: reference is not a tree`), then ran `checkout-candidate` and began `test-pass-c01`, which was stopped before its result. Those artifacts are kept in `misfire-1/` under the gate log directory. No test result from that launch is cited. The driver was relaunched from the top with the correct SHA.

## Round-1 result and supersession (add-only)

- Round-1 legs at `37be6eedfe`: fail-before on B `6402b4b528d3ec8daa134772abc84532380e3f7c` produced the intended shape (5 executed, 2 failed at the identity comparison, `[query-B, query-A]` expected, `[query-B, query-B]` actual). Pass-after seeds `11994C01` and `11994C02` each ran 5 of 5 green. Neighbors at `37be6eedfe` were 23 tests green. Tidy changed one line of the test file (`testDisabledRecordingLeavesNoRecord`). Tidy-clean-tree failed; the driver halted.
- Round 1 is superseded by `gates/pr-2452-ubi-context-round-2.md` at formatting-repaired head `bbc00109d07b9d53e4c30659c28e626c48dcfca4`. Round-1 pass and neighbor results are not cited for the new head. Round-1 logs stay under `/workspace/gates/logs/vm4/pr2452-ubi-context-round-1/`, including the setup-failure attempts.
