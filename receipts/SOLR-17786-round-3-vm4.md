# SOLR-17786 gate receipt: PR 5108 reverse cursor merge, round 3, vm4 (GREEN at F2)

Host: vm4 (Linux aarch64, JDK Temurin 21.0.12.1+1, Gradle 9.7.0 via the repo wrapper).
Exact tested head F2: `495d3fbdbe07bfb474b43d77b89f8c033bbeb8fd` on `nick-boss-tech/solr:solr-17786-reverse-cursor-submit`, commit "SOLR-17786: tidy the local query call in DistribCursorPagingTest" (Nick Shanin, no trailers).
Original round-3 head: `c5a16393d2b0d2ad8b05f66697a906733e551b69`. Tidy at that head changed one line, so the repair commit above was pushed fast-forward and verified by ls-remote. Round-3 proof is for F2 only.
Parent: `fde070281383f113a331a9ec5ccde173b66fac3c` (round-2 gated head). Delta to F2: one file, `solr/core/src/test/org/apache/solr/cloud/DistribCursorPagingTest.java`, test code only.
Gate: `gates/pr-5108-reverse-merge-round-3.md`. Superseding job: `gates/pr-5108-reverse-merge-round-3-f2-vm4.md`. Claim: `claims/pr-5108-reverse-merge-round-3-vm4.md`.
Earlier round-2 receipt `receipts/SOLR-17786.md` (vm2, GREEN at `fde07028`) is unchanged; this file is the round-3 record.

## Results

| Leg | Command (after the runner prefix) | Result | Fresh XML / log |
| --- | --- | --- | --- |
| Preflight tidy at F2 | `tidy` through `sweep-run.sh` | rc 0, tree clean after | `preflight-tidy-f2.log`, `.headroom.log` |
| Preflight tidy at original head | `tidy` | rc 0, changed one line (repaired in F2) | `preflight-tidy` logs were replaced by the F2 rerun; the failed first attempt is recorded under Limits |
| Diff check | `git diff --check` at F2 | rc 0 | `f2-diffcheck.txt` |
| 1 Error Prone compile | `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | BUILD SUCCESSFUL 6m51s, rc 0, no errorProneSkipped line | `f2-errorprone.log` |
| 2 Focused, seed 5108C0DE | `:solr:core:cleanTest :solr:core:test --tests ...testReverseCursorMergeAcrossShards --tests ...testReverseCursorMergeWithTiesAcrossShards -Ptests.seed=5108C0DE` | BUILD SUCCESSFUL 6m34s, rc 0, 2 tests, 0 failures, 0 errors, 0 skipped | `f2-focused-5108.log`, `f2-focused-5108.xml/` |
| 3 Focused, seed 17786C0DE | same, `-Ptests.seed=17786C0DE` | BUILD SUCCESSFUL 2m57s, rc 0, 2 tests, 0/0/0 | `f2-focused-17786.log`, `f2-focused-17786.xml/` |
| 4 Suites at F2, seed 5108C0DE | `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.SortSpecReverseTest --tests org.apache.solr.search.CursorMarkTest --tests org.apache.solr.CursorPagingTest --tests org.apache.solr.cloud.DistribCursorPagingTest -Ptests.seed=5108C0DE` | BUILD SUCCESSFUL 4m27s, rc 0. CursorPagingTest 7, DistribCursorPagingTest 4, CursorMarkTest 6, SortSpecReverseTest 1: 18 tests, 0 failures, 0 errors, 0 skipped | `f2-suites-core.log`, `f2-suites-core.xml/` |
| 5 solrj QueryResponseTest | `:solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.response.QueryResponseTest -Ptests.seed=5108C0DE` | BUILD SUCCESSFUL 1m29s, rc 0, 7 tests, 0/0/0 | `f2-solrj-qrt.log`, `f2-solrj-qrt.xml/` |
| 6 Pristine check | `:solr:core:check :solr:solrj:check -x test` on the clean F2 worktree | BUILD SUCCESSFUL 3m35s, rc 0; tree clean afterwards | `f2-check.log`, `f2-check.headroom.log` |
| M1 coordinator comparator | `:solr:core:cleanTest :solr:core:test` for both new tests, seed 5108C0DE, on a disposable F2 checkout with M1 applied | BUILD FAILED 7m47s, rc 1 (intended). Both tests fail at the document-selection assertion, see below | `m1-focused-5108.log`, `m1-focused-5108.xml/`, `m1-patch.diff`, `m1-patch-stat.txt` |
| M2 tiebreaker inversion | `:solr:core:cleanTest :solr:core:test --tests ...testReverseCursorMergeWithTiesAcrossShards -Ptests.seed=5108C0DE` on a separate disposable F2 checkout with M2 applied | BUILD FAILED 10m25s, rc 1 (intended). The tied test fails at the document-selection assertion, see below | `m2-tie-5108.log`, `m2-tie-5108.xml/`, `m2-patch.diff`, `m2-patch-stat.txt` |
| 9 Final focused at F2 | same as leg 2, seed 5108C0DE, on the clean F2 worktree after the mutations | BUILD SUCCESSFUL 4m14s, rc 0, 2 tests, 0/0/0 | `f2-final-focused-5108.log`, `f2-final-focused-5108.xml/` |

Driver status and sequence: `f2-driver.status` (end line at 21:10:29Z). Build lock: `/workspace/tools/.gradle-vm4.lock` through `sweep-run.sh`, one build at a time. Logs: `/workspace/gates/logs/vm4/pr-5108-round-3/`.

## Mutation evidence

Patches are coordinator-only, in `QueryComponent.mergeIds`, and no test or shard-side code changed.
- M1: three added lines after `SortSpec ss = rb.getSortSpec();` (`ss = rb.getCursorMark().getSortSpec()` for reverse cursors). Patch stat: 3 insertions, 0 deletions.
- M2: five added lines before the priority queue comment, cloning `sortFields` and restoring the last (unique-key) comparator from the cursor sort. Patch stat: 5 insertions, 0 deletions.

Expected and actual selected IDs, from fresh JUnit XML. The failure frames are at `DistribCursorPagingTest.java:182` (the page assertion helper) and `:257` (the reverse-page call), with the tied test's own call line at `:199` and the non-tied test's at `:194`.

| Mutation | Test | Expected (selected) | Actual (selected) | Comparison |
| --- | --- | --- | --- | --- |
| M1 | testReverseCursorMergeWithTiesAcrossShards | doc107_3, doc108_0, doc109_2 | doc103_0, doc106_0, doc109_2 | Different identities. Not an order-only difference. |
| M1 | testReverseCursorMergeAcrossShards | doc107_3, doc108_0, doc109_2 | doc107_3, doc108_0, doc103_0 | Different identity in the last slot. |
| M2 | testReverseCursorMergeWithTiesAcrossShards | doc107_3, doc108_0, doc109_2 | doc108_0, doc106_0, doc109_2 | Different identities (107_3 missing, 106_0 included). |

Each mutation test failed on the document-ID assertion at the reverse page, with a different selected set, so the negative evidence discriminates selection rather than presentation.

## Totals (positive, fresh XML)

- Focused, seed 5108C0DE: 2 tests, 0 failures, 0 errors, 0 skipped.
- Focused, seed 17786C0DE: 2 tests, 0 failures, 0 errors, 0 skipped.
- Suites: 18 tests, 0 failures, 0 errors, 0 skipped.
- solrj QueryResponseTest: 7 tests, 0 failures, 0 errors, 0 skipped.
- Final focused at F2: 2 tests, 0 failures, 0 errors, 0 skipped.

## Limits

- The check leg ran `:solr:core:check :solr:solrj:check -x test` as the gate asks. It is not a full repository test run and it does not enable Error Prone (`errorProneSkipped` appears there). Error Prone itself ran in leg 1, which compiled main and test sources with `-Pvalidation.errorprone=true`.
- Each test leg prefixed `:solr:core:cleanTest` (or `:solr:solrj:cleanTest`) before `test`, so that the runs were fresh execution and not UP-TO-DATE output. The gate command is otherwise unchanged.
- Preflight: the first tidy attempt used the wrong runner arguments (`./gradlew tidy`, which the runner already prefixes) and exited with rc 1 before tidy ran. Its logs were removed and replaced by the rerun. The tidy at the original head then changed one line (the repair commit), and tidy at F2 was clean.
- The driver unset `NODE_ENV` for every leg. The host shell has `NODE_ENV=production`; none of these legs runs the ref-guide Antora build, so the setting does not affect them. The host setting itself is unchanged and still awaits Nick's decision.
- The round-2 receipt `receipts/SOLR-17786.md` is vm2's single-writer file and was not edited. This receipt is a new file and the gate's "write receipts/SOLR-17786.md" instruction was deliberately not followed for that reason, recorded in the claim.
- No Windows build or test was run. No benchmark was requested. No PR, comment, or JIRA action was taken; the public apache/solr PR 5108 is still at `32f80171030ca1108cd20292f50232d2729ed930`.
- The first gate worktree was built from the fork at `c5a16393d2`; the repaired head `495d3fbdbe` is the only head this receipt proves.
