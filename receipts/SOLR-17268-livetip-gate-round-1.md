# SOLR-17268 exact-head gate at the live tip (phone agent, 2026-10-26): GATE GREEN

- Branch: `solr-17268-submit`. Head: `77a7e748e9ddf30fe7b0957796d6de151d13d2fa`, verified as the fork tip before the run and the head the reconciliation report settled on (`reports/solr-17268-head-reconciliation-round-1.md`).
- Merge-base with origin/main: `63f2459f3b58861de8575ecbfbefc38281482f3a`. Seed: `17268C0FFEE17268`. Host: the phone agent's VM, run directly by the phone agent. Runner: `~/workspace/tools/g17268-livetip-gate.sh`; evidence (per-step logs, steps.tsv, manifest, fresh XML for all three runs, checker output) under `~/workspace/tools/g17268-livetip-gate/`.
- Gate shape: the archive board's `gates/SOLR-17268.md` steps 0 to 5, including both proof legs, judged from fresh JUnit XML by an acceptance checker (exact counts, exact failing-test sets, and the expected failure text).

## Step results

| Step | Result |
| --- | --- |
| 0 changelog YAML parse | rc 0 |
| 1 tidy (`:solr:core:tidy`) | rc 0, tree clean afterward; no tidy commit was needed or made |
| 2 Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`) | rc 0 |
| 3 leg A (base production `63f2459f3b5`, head's two test files transplanted) | `JSONWriterTest` 20 tests, 8 failures; `JacksonJsonWriterArrntvCloudTest` 4 tests, 4 failures. The failing set matches the plan by name: seven failures carry `arrntv namedListStyle must only be used with ArrayOfNameTypeValueJSONWriter` (the cloud four inside HTTP 500 bodies) and `testArrntvNestedNamedListIsValidJson` fails on comparison. Controls pass. |
| 3 leg B (head with the single line `ifNeededWriteTypeAndValueKey("lst");` removed from `JSONResponseWriter.java`) | Exactly the five expected tests fail: `JSONWriterTest` 20 tests, 3 failures; cloud class 4 tests, 2 failures. Every other test passes. |
| 4 focused head run (after cleanTest) | `JSONWriterTest` 20 tests, 0 failures, 0 errors, 0 skipped; `JacksonJsonWriterArrntvCloudTest` 4 tests, 0 failures, 0 errors, 0 skipped |
| 5 module check (`:solr:core:check -x test`) | rc 0, BUILD SUCCESSFUL |

## Run history

Two earlier attempts the same evening stopped on defects in the runner, not on the branch: attempt 1 stopped before any test ran (its transplant check counted only tracked diffs, but the cloud test file is new on the branch and untracked at the base); attempt 2 stopped after leg A when its XML copy step failed on ownership preservation, and leg A's XML from that attempt was read directly from the worktree and met the same acceptance criteria. The GREEN above is the third attempt, one continuous run at the pinned head; attempts 1 and 2 are preserved under `~/workspace/tools/g17268-livetip-gate-attempt1/` and `-attempt2/`.

## State after this receipt

`solr-17268-submit` at `77a7e748e9dd` is fully gated at its exact live head, with the matched proof pair complete at that head. Nothing was pushed, opened, or posted by this gate. The route decision for SOLR-17268 stays with the owner.
