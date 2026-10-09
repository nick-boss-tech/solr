# Plain-language pass: read-only check, batch B (13 drafts)

Scope: `pr-drafts/update-processing/SOLR-<ticket>.md` at `bc61a3ce3d3` (previous) against `HEAD` (004575c037b, current), with receipts from `receipts/SOLR-<ticket>.md` at HEAD. Read-only. No drafts, branches, PRs, builds, or tests were touched.

Method: commit-like hex heads (7 to 40 characters) compared as sets; every numeric token compared as a multiset; `git cat-file -e` run on all 41 distinct heads in the current drafts (all resolve); vocabulary grep for the owner's list; receipt comparison per Proof sentence.

Overall: checks 1 to 4 are clean for all 13 drafts. Check 5 has three findings, listed below. Vocabulary removals are expected under the owner's rule and are not findings.

Note: SOLR-13265 is byte-identical to its previous version (`cmp`).

## SOLR-12245
1. Heads dropped: none (3 kept).
2. Counts and dates: none changed (`6 of 6`, 2026-10-09). `rc=0` replaced by "The module checks pass".
3. Blob targets: resolve.
4. Vocabulary: none.
5. Receipt: none. The claim "The fix commit `f325d5d0576e` added it" is not in the receipt. `git show --stat` confirms the test file changed in that commit.
Finding: none.

## SOLR-12703
1. Heads dropped: none (5 kept).
2. Counts and dates: none changed (`27 tests`, 2026-10-09).
3. Blob targets: resolve.
4. Vocabulary: none.
5. Receipt: none.
Finding: none.

## SOLR-12705
1. Heads dropped: none (4 kept).
2. Counts and dates: none changed (`25 tests`, `43 tests`, `30 of 30`, `43 of 43`, `0 failures`, 2026-10-09). Only "green" became "pass".
3. Blob targets: resolve.
4. Vocabulary: none.
5. Receipt: none. "does not cover the fixed SOLR-7504 head, `2fe06bfd917f`" is supported by `receipts/SOLR-7504.md`, not by this ticket's receipt.
Finding: none.

## SOLR-12864
1. Heads dropped: none (2 kept).
2. Counts and dates: none changed (`32 of 32`, 2026-10-08).
3. Blob targets: resolve.
4. Vocabulary: none.
5. Receipt: none.
Finding: none.

## SOLR-13265
1. Heads dropped: none (1 kept). Byte-identical to previous.
2. Counts and dates: none changed (`1 of 3`, `3 of 3`, 2026-10-07).
3. Blob targets: resolve.
4. Vocabulary: none.
5. Minor: "On the base code, with only this test file applied, 1 of 3 fails". The receipt says only "On base production ... exactly 1 failure". "with only this test file applied" is a method detail the receipt does not state. Pre-existing text.
Finding: minor (5).

## SOLR-13696
1. Heads dropped: none (5 kept).
2. Counts and dates: none changed (`13 of 13`, `2 of 2`, `6 of 6`, 2020-10-23). Removed: `rc=0`, the `fresh JUnit XML` sentence, and the gate round labels `(gate r3)`, `(gate r5)`, `(gate r6)`, `(gate r7)`. Labels only, not counts.
3. Blob targets: resolve.
4. Vocabulary: none.
5. Finding: four pre-fix heads in the Proof are not in `receipts/SOLR-13696.md`: `98ad9d3fcc33`, `a4e0da422327`, `08f9384e47c0`, `c3cdf7b46e8`. The receipt says only "pre-fix trees". The heads come from `material/update-processing-13696-r8-addendum.md`, which the owner's rulings do not accept as a source for this ticket (ruling 5 covers SOLR-16673 only).
Finding: yes (5).

## SOLR-13943
1. Heads dropped: none (3 kept).
2. Counts and dates: none changed (`1 of 1`, `6 of 6`, `2 of 2`, `13 of 13`, "six tests", "Five pass"). Removed: `rc=0` and the `fresh JUnit XML` sentence, replaced by "These counts are from runs at this head."
3. Blob targets: resolve.
4. Vocabulary: none.
5. Finding: "`CategoryRoutedAliasUpdateProcessorTest` 6 of 6, `DimensionalRoutedAliasUpdateProcessorTest` 2 of 2, and `CreateAliasAPITest` 13 of 13, all in normal mode." The receipt states normal mode only for `TimeRoutedAliasDateMathInStartTest` 1/1. The receipt gives no mode for the other three.
Finding: yes (5).

## SOLR-14262
1. Heads dropped: none (2 kept).
2. Counts and dates: none changed (`1 of 1`, `1/1`, 2026-10-07). `rc=0` replaced by "The module checks pass".
3. Blob targets: resolve.
4. Vocabulary: none.
5. Receipt: none.
Finding: none.

## SOLR-14718
1. Heads dropped: none (5 kept).
2. Counts and dates: none changed (`1/1`, "exactly 1 test", 2026-10-07).
3. Blob targets: resolve.
4. Vocabulary: none.
5. Receipt: none.
Finding: none.

## SOLR-16356
1. Heads dropped: none (2 kept).
2. Counts and dates: none changed (`1/1`, `5/5`, 2026-10-04).
3. Blob targets: resolve.
4. Vocabulary: none.
5. Source note, not a finding: "Without the fix, this test fails." The receipt says only "Pre-fix proof: PASS (recorded in the gate entry)". The head-level PASS is in `material/update-processing-receipts-addendum.md`.
Finding: none.

## SOLR-16655
1. Heads dropped: none (4 kept).
2. Counts and dates: none changed (`36 of 36`, `44 of 44`, 2026-10-09). Removed: "From fresh JUnit XML" and "`:solr:core:check -x test` exit code 0" (now "The module checks pass").
3. Blob targets: resolve.
4. Vocabulary: none.
5. Source note, not a finding: "That head is the parent of ... `5e2317443f41`" is not in the receipt. `git rev-parse 5e2317443f41^` gives `b201a57fb3e`, and the commit subject matches the description.
Finding: none.

## SOLR-16673
1. Heads dropped: none (3 kept).
2. Counts and dates: none changed (`43 of 43`, 2026-10-09). `tidy returns 0`, `Error Prone compile returns 0`, and `:solr:core:check -x test returns 0` became "passes".
3. Blob targets: resolve.
4. Vocabulary: none. "tidy" and "Error Prone" are build-tool names, not on the list.
5. Source note, not a finding: the earlier-head facts (tidy, Error Prone, module checks, 43 of 43, pre-fix PASS at `d5c19e64ba1b`) are not in `receipts/SOLR-16673.md`, which records only the top-up. They are in `material/update-processing-receipts-addendum.md`, which owner ruling 5 accepts.
Finding: none.

## SOLR-16910
1. Heads dropped: none (2 kept).
2. Counts and dates: none changed (`3/3`, "exactly 1 failure", "3 tests", 2026-10-07). `rc=0` replaced by "The module checks pass".
3. Blob targets: resolve.
4. Vocabulary: none.
5. Source note, not a finding: "Three new tests" is not in the receipt. The test file is absent at the merge-base `e2cdb2d7e8ae`, so the three tests are new.
Finding: none.

## Result

Drafts with any finding: SOLR-13265 (minor, check 5), SOLR-13696 (check 5, four heads not in the receipt), SOLR-13943 (check 5, "all in normal mode" not in the receipt).
