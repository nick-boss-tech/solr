# Review: mergeIds fix branch, thorough review

Verdict: **STILL OPEN.** One blocking item (the ticket as drafted does not describe the fallback commit). Ten should-fix items: nine graded in the areas below, plus the premise record's test-count wording. No item was found to be wrong in the arithmetic of the skip fix itself.

Subject: branch `mergeids-skipped-shard-totals`, head `d211794596bc3fc05757bc9548d999e0bd4322ea`, base `d76fc14b9857e77b830d7a82514b28c523a239c2`. Commits `0d900a3b679` (skip before accumulation) and `d211794596b` (fallback uses the shard's own numFound). Nothing was run, built, tested or gated on this host. Nothing was edited on the branch, the draft or the gate records.

## Evidence this review did not have

- The verification report `side-finding-mergeids-verification.md` and the gate log `gmergeids-gate.log` were not found under `C:\Users\shaninna\dev\Solr-issues` (research, env, and the other workspace folders were searched; the worktree search timed out). The gate's state (running, green, or failed) is therefore **not stated**; this review relies on no gate state. The premise numbers (5 vs 3, 2 vs 5, 99.0 vs 3.0) are reproduced by reading the code only.
- The premise record as written says "after the first fix the class ran 8 tests with 0 failures and the fallback case failed expected:<5> but was:<2>". That cannot both be true. It should read "8 tests, 1 failure" once the log is checked.

## Area 1: reorder correctness (`0d900a3b679`)

- **[should-fix] `QueryComponent.java:1195-1196`.** The `numFoundExact` check now runs after the skip (`continue` at 1178). On main it ran before the skip. A skipped shard that reports `numFoundExact=false` no longer makes the merged response inexact. This is a behaviour change the commit message does not record. The side note (`side-finding-mergeids-numfound-partial-shard.md`, lines 31-37) lists this as an open choice between dropping the shard's count and clearing the flag. The branch takes the first option without saying so. Record the decision in the commit and the ticket, or switch to the flag option.
- **[should-fix] `QueryComponent.java:1025, 1296-1297`.** An all-shards-skipped response writes `numFound=0` and `numFoundExact=true` with `partialResults=true`. `hitCountIsExact` starts true at 1025 and nothing downgrades it when every shard is skipped. The response claims an exact zero for a result that had matches. `maxScore` is null-guarded (no NPE), so the maxScore part is safe. No test covers this case.
- **[note] `QueryComponent.java:1142, 1148, 1157`.** `thereArePartialResults`, `segmentTerminatedEarly` and `maxHitsTerminatedEarly` still fold in from a skipped shard, because they sit above the skip. Keeping `partialResults` is right. A skipped shard can still set the max-hits flag (around 1270) while adding no approximate count. The asymmetry is not stated in the commit.
- **[note] `QueryComponent.java:1227`.** The `numFound--` compensation is for duplicate document ids, not queue overflow (`ShardDocQueue.push` returns false only on a duplicate id). Skipped documents are never pushed, so this is unaffected.
- **[note] `QueryComponent.java:1163`.** Moving the accumulation below the sort-values read changes no output for shards that are not skipped; `getSubsectionFromShardResponse(..., true)` has no side effect beyond returning null.
- **[note] `QueryComponent.java:1074-1086`.** `shards.info` still reports each skipped shard's own numFound and maxScore, so summing `shards.info` will not match the top-level numFound. Expected; worth one sentence in the ticket.

Check results: non-skipped output unchanged, CONFIRMED by reading (test results NEEDS RUN). Duplicate compensation, CONFIRMED. All-skipped maxScore null handling, CONFIRMED (no NPE). All-skipped numFoundExact, FINDING (above). First-shard-skipped clean start, CONFIRMED for the totals; the flags finding above applies.

## Area 2: fallback commit (`d211794596b`)

- The fix is correct. The if/else is exclusive: a shard with no header adds its own `docs.getNumFound()` once; a shard with the header adds the header value once, and its numFound never enters the approximate total. Skipped and errored shards add nothing. On main, the running `numFound` was added instead, so shards with 2 and 3 documents gave 2 where 5 is correct.
- **[note] Mixed headers are the normal case.** A shard writes its header only when partial (`ResponseBuilder.java:490-508`). A partial shard with a header next to a complete shard without one is common under `timeAllowed`. The test at `QueryComponentPartialResultsTest.java:115` uses headerless shards only. Add one case with one headered and one headerless shard merged. Expected: header value plus the headerless shard's numFound.
- **Independence.** The fallback is independent of the skip. The test at 115 uses no partial results and no skipped shard; it needs `maxHitsTerminatedEarly` on one shard so the total is written.

**Recommendation: split the fallback into its own change.** Cherry-pick `d211794596b` onto main with its test. Reasons: it is a distinct defect with its own proof (main fails the test at 2 vs 5, with no partial results and no skip); the skip change still has the open exactness decision in Area 1, and keeping them together makes the one-line fallback wait on it; a one-concern title does not describe a combined change. Caveat: if the ticket is scoped to "merged totals as a whole", one PR with both commits is acceptable, but then the description must name both defects and the mixed-header test must be added.

## Area 3: test adequacy

Accumulator map (from code, no run):

| Test (test file lines) | Pins | Fails on main by reading |
|---|---|---|
| `skippedPartialShardDocsAreAbsent` (82-84) | document list | No: passes on main, since the `continue` predates the totals move. Guard only. |
| `mergedNumFoundCountsOnlyMergedShards` (88-94) | numFound, numFoundExact (94) | Yes: 5 vs 3 |
| `mergedMaxScoreComesFromMergedShards` (98-100) | maxScore | Yes: 99.0 vs 3.0 |
| `mergedApproximateTotalHitsExcludesSkippedShard` (104-111) | approximateTotalHits (header branch) | Yes: 10 vs 3 |
| `approximateTotalHitsFallsBackToEachShardsNumFound` (115-149) | approximateTotalHits (fallback), numFound | Yes on the header path: 2 vs 5 |

- **[should-fix] `QueryComponentPartialResultsTest.java:170, 94`. `hitCountIsExact` is pinned by no test.** Every input is exact, so the flag is only ever true. Moving the exactness update above the skip, or deleting it, leaves all five tests green. Test-only fix: set shard1's `numFoundExact` to false in `mergeWithSkippedPartialShard`. The assertion at 94 then pins the skipped-shard behaviour from Area 1 explicitly.
- **[should-fix] `QueryComponentPartialResultsTest.java:174, 176`. The header branch is not discriminated.** Shard2 sends `approximateTotalHits=3L` and its `numFound` is also 3. A regression that ignores the header and sums `docs.getNumFound()` passes all five. Use a header value distinct from numFound (for example 4L) and adjust the expectation at 111.
- **[should-fix] Maximum aggregation is pinned with one merged shard only** (98-100). A min or last-shard regression passes. No test checks totals contributed by a merged partial shard with sort values; the `includesPartial*` tests assert document counts only.
- Premise record: four of the five tests fail on main, not "8 tests, 0 failures" as the record says. Fix the wording before citing it.

Fidelity limits, stated plainly: the tests call the production `mergeIds` with fabricated shard responses. The skip precondition (partial, no sort values, sorted request) is reproduced exactly at the merge input (lines 164-168). Not reproduced: whether a real timed-out shard emits that shape, or reports `numFoundExact=false`. The sort-value emission path (`doFieldSortValues`, around `QueryComponent.java:455, 1888`) was not traced. The existing SOLR-14758 test (`DistributedQueryComponentCustomSortTest.java:240-252`) asserts only no NPE and conditional documents; it pins no totals.

**End-to-end call:** not required before the PR opens for the arithmetic. Merge-level tests plus the SOLR-14758 scenario description are enough. A deterministic distributed test (`DelayingSearchComponent` is in the test tree) would strengthen the coverage but does not settle the exactness question, which is the real open decision. NEEDS RUN on a Linux host: whether a timed-out partial shard actually sends the skip shape and `numFoundExact=false`. The ticket should not assert that as fact until it is run.

## Area 4: unexamined merge behaviour

- **[should-fix] `CombinedQueryComponent.java:342-343, 384-386, 399`. Same defect, outside the branch.** `CombinedQueryComponent.mergeIds` (the override) folds `queryNumFound`, `hitCountIsExact` and `queryApproximateTotalHits` before the partial `continue` at 399. Affected: numFound, approximateTotalHits, hitCountIsExact. The branch does not touch this class. Decide whether the PR covers it or the ticket says it is a follow-up. Do not leave it unmentioned.
- **[note] `CombinedQueryComponent.java:342, 384` (pre-existing, not skip-related).** The no-header fallback adds `queryNumFound`, which already includes the current shard (342 runs first). Totals accumulate cumulatively: shards with 2 and 3 documents give 2 then 7, where 5 is correct. Same accumulator family as the `d211794596b` fix, opposite direction. Separate defect; file or note it separately.
- **[note] `QueryComponent.java:1234`.** `addToLog("hits", numFound)` follows numFound, so the hits log drops for partial sorted responses. Affected by the fix; untested.
- **Relation handling, `QueryComponent.java:1195-1197`.** `hitCountIsExact` is an AND over the shards' `numFoundExact`; a null count counts as exact. Affected by the skip (the dropped shards' flags). Only the TRUE path is pinned by a test.
- **shards.info, `QueryComponent.java:1084-1086, 1098`.** Unaffected. The block runs before the skip by design and reports each shard's own values.
- **Grouped responses, `QueryComponent.java:660-667, 684-686`.** Unaffected. Grouped requests route to `handleGroupedResponses`; `mergeIds` is called only from `handleRegularResponses`. `TopGroupsShardResponseProcessor.java:133-156` has its own numFound sum and no partial skip.
- **Dedupe helpers, `QueryComponent.java:928-935, 1227, 762, 1318`.** Unaffected; skipped documents are never pushed.
- **maxHitsTerminatedEarly, `QueryComponent.java:1157`.** A skipped shard's flag still folds in, as noted in Area 1. Flags, not totals; the wording in the ticket should say so.

## Area 5: consistency across branch, ticket draft and evidence

- **(a) Branch commit messages and diff:** CONFIRMED. Both messages match the diff, and `d211794596b` is the only fallback change.
- **[blocking] Ticket draft does not describe the fallback commit.** `material/mergeids-ticket-draft.md`: no symptom, reproduction, mechanism or fix option names the change to `approximateTotalHits += docs.getNumFound()` (commit `d211794596b`). Draft line 30 only says the total "is accumulated around line 1160". A PR containing both commits would carry a change the ticket does not describe. Either add a bullet after line 30 (a shard with no approximate header adds the running merged numFound, so shards with 2 and 3 documents give 2 instead of 5) and a third fix item after line 50, or file it as a second ticket. This is the one item that blocks filing the ticket as the draft stands.
- **[should-fix] Draft lines 20-22 (symptoms).** The list omits `approximateTotalHits`. On main the reproduction writes the header as 10 (7 from shard1 plus 3 from shard2), when `maxHitsTerminatedEarly` is set and the total is positive (around `QueryComponent.java:1264-1272`). Add a bullet.
- **[should-fix] Draft line 41 (reproduction).** It omits the approximate headers (7 on shard1, 3 on shard2) and shard1's `maxHitsTerminatedEarly` flag, which the branch helper sets. Without them the approximate symptom cannot be reproduced from the ticket text.
- **[should-fix] Draft lines 12 and 21 (title and symptom).** They call `numFoundExact=true` a defect, but the fix leaves it true (`QueryComponentPartialResultsTest.java:94` asserts TRUE after the fix, and draft line 37 says the flag survives the merge). Reword: the flag qualifies a count that includes a skipped shard, and the branch does not change it.
- **Draft line numbers** (lines 30, 49 and 52 against base 1169, 1171, 1174, 1193, 1225): CONFIRMED against `cabedd1d968`. Draft line 24 (`partialResults` header at 1239-1242) and line 35 (`numFound--` at 1224-1225): CONFIRMED.
- **[note] Draft lines 49 and 52.** "A skipped shard never enters the merged totals" holds for the counts. The max-hits flag still folds in from a skipped shard; wording only.
- **[note] Draft line 37.** The timeout mechanism that sets a shard's exact flag was not verified by this review.
- **[note] Draft line 54 and line 5.** SOLR-15595 (`812de21e75d`) and SOLR-14758 (`a72c9931238`) are consistent with the draft. SOLR-15144 has no hit in local history; JIRA was not accessed.
- **(c) Premise and gate numbers:** NEEDS RUN for the gate and verification record (files not found, see Evidence). The code reading reproduces 5, 2, 99.0, 3.0 and the test counts.

**Fallback coverage question:** not covered as filed (see the blocking item above).

## Summary of graded findings

- **Blocking (1):** ticket draft does not describe the fallback commit.
- **Should-fix (10):** nine graded in the areas: the numFoundExact decision after the skip is undocumented (Area 1); the all-shards-skipped response claims an exact zero (Area 1); `hitCountIsExact` is pinned by no test (Area 3); the header branch is not discriminated (Area 3); maxScore is pinned with one merged shard only (Area 3); `CombinedQueryComponent` has the same defect outside the branch (Area 4); the draft's symptom list (Area 5); the draft's reproduction (Area 5); the draft's title and symptom wording on numFoundExact (Area 5). Plus the premise record's test-count wording (Evidence).
- **Notes:** flag asymmetry, `shards.info` sums, hits log, and the pre-existing `CombinedQueryComponent` fallback double count.

## What would clear the verdict

1. Decide the fallback: split it, or add the ticket lines (Area 2 and Area 5).
2. Record the numFoundExact decision, and decide the all-shards-skipped response (Area 1).
3. Add the two test changes (exactness and the discriminating header value), and a mixed-header case (Area 3).
4. Decide the `CombinedQueryComponent` scope (Area 4).
5. Fix the draft wording items and the premise record's test count (Area 5 and Evidence).
6. Point the review at the verification report and the gate log, so the gate state can be stated. NEEDS RUN on a Linux host for the fail-before set and the shard-side emission question.

The reviewer does not edit the branch, the ticket draft or the gate records; each item above is flagged for the main side.
