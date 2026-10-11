# Response: mergeIds fix review adjudication (main side, 2026-10-11)

Review: reports/mergeids-fix-review.md, verdict STILL OPEN, 1 blocking item and 10 should-fix items. Branch `mergeids-skipped-shard-totals` was at `d211794596bc3fc05757bc9548d999e0bd4322ea` for the review; the applied code and test items landed as commit `73d20a0f939f0ddbaea89a9bd590ce9527f7dd29`, pushed as the new head, with a re-gate recorded as GATE PENDING in the main side's takeover log (runner gmergeids-regate.sh, log gmergeids-regate.log on vm1).

Two record points first. The gate the review could not see finished GREEN on vm1: GATE RUNNER DONE 2026-10-11T19:22:52Z, proof leg 9 tests with the 4 premise failures by content, head 9 of 9, module check rc=0. The full record is now at receipts/mergeids-fix.md, so any host can state the gate state. And the ticket text Nick already received by email is superseded by the updated draft; the main side passes him the updated text before he files.

## The blocking item

**Ticket draft does not describe the fallback commit: APPLIED.** material/mergeids-ticket-draft.md now describes the fallback defect and its fix in the same markup and voice as the rest of the ticket: a second-defect paragraph in Symptom (two shards with 2 and 3 documents and no headers merge to 2 instead of 5), a mechanism bullet (the fallback at line 1162 adds the running merged total; the current shard's own count is only folded in at line 1171), a fallback paragraph in Reproduction, and a third item in Fix options. The ticket reads as one report of two defects in one accumulation block.

**The Area 2 split recommendation: DECLINED.** The fallback stays in this ticket and this branch, per Nick's direction that the ticket describe it. The review's own caveat accepts one change when the description names both defects and the mixed-header test is added; both are now done (the draft names both; the mixed-header test is item 6 below). Splitting would also strand the one-line fix behind the exactness discussion for no correctness gain: the two defects share the accumulation block and the same test class.

## Area 1 should-fix items

**The numFoundExact decision after the skip is undocumented: SETTLED BY RECORD, no code change.** The decision is the one the ticket already states: fix option 1 moves the {{hitCountIsExact}} accumulation below the skip along with the other three, so a skipped shard's flag drops with its count. That is now also recorded in the message of commit 73d20a0f93 and pinned by the new test under the Area 3 exactness item. The alternative (clearing the flag when a shard is skipped) is the ticket's second option, presented and not recommended, for the reason the ticket gives: it corrects only the exactness symptom and leaves the count and the score counting the skipped shard.

**The all-shards-skipped response claims an exact zero: APPLIED IN CODE.** Commit 73d20a0f93 adds the guard in QueryComponent.mergeIds: when no shard was merged and a skipped shard reported hits, the merged response no longer reports numFoundExact true for its zero count. maxScore stays null-guarded as the review confirmed. The new test allShardsSkippedDoesNotClaimExactZero pins it; in the local proof leg it fails on merge-base production (expected:<0> but was:<5> on the count) and the class passes 13 of 13 at the new head.

## Area 3 should-fix items

**hitCountIsExact is pinned by no test: APPLIED, in a dedicated test.** skippedShardNumFoundExactDoesNotFoldIntoMergedFlag gives the skipped shard numFoundExact false and asserts the merged flag stays true. The review suggested setting the flag false on the shared helper's shard1 instead; a dedicated test was chosen so the shared scenario stays the one the ticket's Reproduction describes (the SOLR-14758 shape, where the skipped shard's own count is exact). The pin is equivalent: moving the fold above the skip, or deleting it, fails this test.

**The header branch is not discriminated: APPLIED.** The merged shard's approximateTotalHits header in the test helper is now 4L against its numFound of 3, and mergedApproximateTotalHitsExcludesSkippedShard expects 4. On merge-base production the merged total is now 11 (7 from the skipped shard plus 4), checked by content in the re-gate's proof leg.

**Maximum aggregation is pinned with one merged shard only: APPLIED IN PART.** mergedMaxScoreIsTheMaximumAcrossMergedShards puts the highest score (5.0) on the first of two merged shards against 2.0 on the second, so a last-shard or minimum regression fails. DECLINED: a totals case for a merged partial shard carrying sort_values. MockShardRequest fabricates only the response and responseHeader subsections, so no sort_values subsection can be supplied, and unmarshalSortValues runs against a mocked schema; fabricating one is a harness project of its own. A partial shard that is not skipped takes the same accumulation path as any merged shard, which the merged-shard tests pin. Flagged here so round 2 can confirm the reasoning or re-open it.

**Mixed headers (Area 2 note, in the review's clearing list): APPLIED.** approximateTotalHitsMixesHeaderAndFallbackShards merges a header shard (11) with a headerless shard (numFound 3) and expects 14. On merge-base production it gives 13 (the header value plus the running total 2), confirmed in the local proof leg.

## Area 4 should-fix item

**CombinedQueryComponent has the same defect, outside the branch: VERIFIED, OUT OF SCOPE for this branch, named in the ticket.** On the branch base d76fc14b98: queryNumFound += docs.getNumFound() at line 342 and the hitCountIsExact fold at line 343 run when the per-query document list is read; queryApproximateTotalHits is accumulated at lines 384 to 386; the partial continue is at line 399, after all of it. Its fallback adds queryNumFound, which already includes the current shard, so its approximate totals accumulate cumulatively (the review's note: 2 then 7 where 5 is correct). This is a different component with per-query combined semantics and its own fallback direction; it needs its own analysis, tests and gate rather than a ride-along in this branch. The ticket draft now names it in Related as a follow-up.

## Area 5 should-fix items

**Symptom list omits approximateTotalHits: APPLIED.** A bullet is added: the merged approximateTotalHits includes the skipped shard's approximate count when the total is written (11 instead of 4 in the reproduction).

**Reproduction omits the approximate headers and the maxHits flag: APPLIED.** The reproduction now states shard1's approximateTotalHits header of 7 and maxHitsTerminatedEarly, shard2's header of 4, the merged 11 on main, and adds the fallback reproduction (2 instead of 5).

**Title and symptom wording call numFoundExact=true a defect: APPLIED.** The title now reads "QueryComponent.mergeIds counts a partial shard whose documents are skipped in the merged totals, and adds the running total as the approximateTotalHits fallback". The symptom bullet now says the skipped shard's own count is exact, so nothing about the flag is wrong on the shard, but the total it qualifies counts a shard that contributed no documents. The branch deliberately leaves the flag true for the count of the merged shards; that is the recorded decision above.

## Evidence should-fix item

**Premise record test-count wording: APPLIED.** assignments/pool-mergeids-fix-review.md now carries the corrected sequence from the four run logs: scratch premise on unmodified main, 5 tests, 1 failure (expected:<3> but was:<5>); after the first fix, 8 tests, 0 failures; the fallback case added and run against the not-yet-fixed fallback, 9 tests, 1 failure (expected:<5> but was:<2>); after the second fix, 9 tests, 0 failures. The gate receipt at receipts/mergeids-fix.md carries the same sequence.

## Notes (no grade, disposition for the record)

- Flag asymmetry and shards.info: the ticket Mechanism now states that partialResults and the max-hits flag still fold in from every shard including a skipped one, and that shards.info reports each shard's own totals, so its numbers do not sum to the merged totals.
- The hits log line (addToLog("hits", numFound)) follows the corrected merged count by construction; no separate change.
- The CombinedQueryComponent fallback double count is part of the follow-up named in the ticket, with the pattern verified above.

## Verification of the applied code and test items

Local runs on vm1 at commit 73d20a0f93 (logs gmergeids-adj-head-tests.log and gmergeids-adj-proof.log; XML copies gmergeids-adj-head-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml and gmergeids-adj-proof-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml): head, QueryComponentPartialResultsTest 13 tests, 0 failures; proof leg with QueryComponent.java at the merge-base, 13 tests, 7 failures, each with the predicted content (numFound 5 vs 3 in two cases, maxScore 99.0 vs 3.0, approximateTotalHits 11 vs 4, fallback 2 vs 5, mixed headers 13 vs 14, all-skipped 5 vs 0). Tidy ran clean on the final tree before the commit. The full re-gate at the new head is GATE PENDING in the takeover log.
