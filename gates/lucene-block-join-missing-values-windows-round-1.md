# Lucene block join missing-value repair: Windows proof

Status: CLAIMED (windows, Codex desktop)

- Authority: Nick explicitly authorized necessary local Gradle execution for Lucene and requested keeping this work off the Linux boxes. This is an exception for Lucene only; no retired Solr queue or Solr Gradle run is authorized here.
- Supersedes Linux execution plan `gates/lucene-block-join-missing-values-round-1.md`. Linux hosts should stand down; the Windows host holds `claims/lucene-block-join-missing-values-windows-proof-round-1.md`.
- Repository: nick-boss-tech/lucene, branch `fix-block-join-missing-values`.
- Exact candidate head: `a9ce0a45074ac2708c2c0ade7cc67162870d2e88`, ordinary descendant of the initial implementation `c015935c04a39bda74c898eb80a3e5d6c0a0eb8c`; independently verified after push.
- Base: apache/lucene main `aa366ae209ae8296dd70d2835c6d47f8fc201972`.
- Receipt owned by windows: `receipts/lucene-block-join-missing-values-windows-round-1.md`.
- Runtime: portable checksum-verified Temurin JDK 25.0.5+8, Lucene's Gradle 9.8.1 wrapper, at most two Gradle workers, seed `18622C0DE`, rerun property for fresh test execution. Host build lock: `research/lucene-windows/build.lock`.

## Required proof and exact scope

1. Tidy at the candidate: root tidy has run, then join tidy after adjacent test additions. Require clean source for test runs. A source/format repair gets an ordinary pushed commit, separate fork ref read-back and complete fresh proof at that new head.
2. Positive focused suites: `:lucene:join:test` for `TestBlockJoinSelector` and `TestBlockJoinSorting`.
3. Full join module: `:lucene:join:test` without filters.
4. Root validations: `check -x test`. This Windows plan explicitly scopes runtime tests to the join module because the changed internal iterator's production consumers are all in join. It does not claim every Lucene module's tests ran, and supersedes the earlier Linux plan's full root test run. The distinction must appear in the receipt and handoff.
5. Fail-before: a separate disposable worktree at the same candidate head, with only `ToParentDocValues.java` restored from the pinned base. Candidate test files remain byte-for-byte unchanged. Individually run:
   - `TestBlockJoinSelector.testNumericMissingValuesRespectChildFilter` (excluded sibling sentinel assertion).
   - `TestBlockJoinSelector.testSortedMissingValuesAreAbsent` (invalid missing-selected parent reported as valued).
   - `TestBlockJoinSorting.testFilteredChildMissingValueNestedSorting` (actual sort order, including true multi-valued numeric fields and null/explicit missing values).
   - `TestBlockJoinSorting.testChildMissingValueNestedSorting` (MAX string missing sentinel reaches lookup).

The adjacent review added paging through missing and real sort values using searchAfter and true multi-valued numeric fields covering both explicit sentinels and the null/skip path. These are compatibility checks, not separately mutation-proven assertions unless a specific negative leg demonstrates them.

## Evidence and failure rules

`research/lucene-windows/run-proof.ps1` records the exact arguments, source head, verified JDK metadata, start/end UTC, exit code, full log, source diff and fresh JUnit XML under per-leg result directories. Each build holds the host lock. Keep setup failures, test failures and intentional fail-before results separate. No compilation/startup failure counts as negative runtime proof; diagnose every executed failure before retrying. Do not weaken assertions or introduce unrecorded exclusions.

A first tidy invocation failed before tests because the project directory was omitted; the runner now supplies the exact project directory. The subsequent full tidy succeeded. No test result comes from that setup failure. Default eclint tooling is not configured; record any resulting optional check skips accurately.

No public PR, issue or maintainer comment. Lucene AI_POLICY.md requires human understanding/ownership and human-written public submission text. The receipt supplies technical facts for Nick's review.
