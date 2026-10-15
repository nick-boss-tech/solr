# Pool assignment: mergeIds fix review, round 2 confirmation

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-mergeids-fix-review-round2.md.

## Scope: confirmation of the adjudicated items only

Round 1 (reports/mergeids-fix-review.md) returned STILL OPEN on branch `mergeids-skipped-shard-totals`: 1 blocking item and 10 should-fix items. The main side adjudicated every item against the branch code and the verification evidence; the record is reports/mergeids-fix-review-response.md. Applied code and test items landed as commit `73d20a0f939f0ddbaea89a9bd590ce9527f7dd29`, the new branch head (base unchanged, `d76fc14b9857e77b830d7a82514b28c523a239c2`). The gate state is no longer something to take on faith: receipts/mergeids-fix.md records the GREEN gate at the reviewed head `d211794596` (vm1 log gmergeids-gate.log, GATE RUNNER DONE 2026-10-11T19:22:52Z, proof and head counts from the JUnit XML) and the re-gate state at the new head. Read the receipt for the gate state at the time of this round and say which state the confirmation relied on.

Confirm each item below against the response file, the updated ticket draft, and the branch diff from `d211794596` to `73d20a0f93`. Nothing else is in scope; do not re-open areas the round 1 review confirmed, and do not edit the branch or the draft; flag instead.

## Items to confirm

1. **Blocking item (ticket describes the fallback).** material/mergeids-ticket-draft.md now describes the fallback defect and its fix in Symptom, Mechanism, Reproduction and Fix options, in the ticket's own markup and voice, reading as one report. Confirm the described mechanism matches the code (fallback at line 1162 on cabedd1d968 adds the running merged numFound; the current shard's count folds in at line 1171) and the reproduction values match the run evidence (2 instead of 5).
2. **Split recommendation declined.** The fallback stays in this ticket and branch per Nick's direction, on the review's own caveat (description names both defects, mixed-header test added). Confirm both halves of the caveat hold, or state why they do not.
3. **numFoundExact decision (Area 1).** The decision is recorded (ticket fix option 1 names hitCountIsExact among the accumulations below the skip; commit 73d20a0f93 message; the response file) and pinned by skippedShardNumFoundExactDoesNotFoldIntoMergedFlag. Confirm the record matches the branch behavior.
4. **All-shards-skipped exact zero (Area 1).** The guard in QueryComponent.mergeIds at 73d20a0f93 and the test allShardsSkippedDoesNotClaimExactZero. Confirm the guard fires only when no shard was merged and a skipped shard reported hits, and that the receipt's local proof contents match (13 tests, 7 failures on merge-base production, contents as listed).
5. **Exactness pin (Area 3).** The dedicated test in item 3, chosen over changing the shared helper's shard1 flag so the shared scenario stays the ticket's reproduction shape. Confirm the pin is equivalent, or state the case it misses.
6. **Header discrimination (Area 3).** The merged shard's header is 4L against numFound 3; the expectation is 4; the merge-base outcome is 11. Confirm against the branch test file.
7. **maxScore pin, partial decline (Area 3).** mergedMaxScoreIsTheMaximumAcrossMergedShards (5.0 on the first merged shard against 2.0). The merged-partial-with-sort-values totals case was declined on harness grounds (MockShardRequest fabricates no sort_values subsection; unmarshalSortValues runs against a mocked schema). Confirm the new pin, and confirm or re-open the decline with the harness evidence.
8. **Mixed headers (Area 2 note).** approximateTotalHitsMixesHeaderAndFallbackShards expects 14 (header 11 plus fallback 3) against 13 on merge-base production. Confirm against the test file and the receipt.
9. **CombinedQueryComponent (Area 4).** Verified at lines 342, 343, 384 to 386 with the partial continue at 399 on the branch base; held out of this branch; the ticket names it as a follow-up in Related. Confirm the citation and the ticket line.
10. **Draft wording items (Area 5).** The approximateTotalHits symptom bullet (11 instead of 4); the reproduction values (shard1 header 7 and maxHitsTerminatedEarly, shard2 header 4, merged 11, fallback 2 instead of 5); the reworded title and numFoundExact symptom (the flag is not itself called a defect; the total it qualifies is). Confirm each passage against the evidence.
11. **Premise record wording (Evidence).** assignments/pool-mergeids-fix-review.md now carries the corrected run sequence (5 tests 1 failure; 8 and 0; 9 and 1; 9 and 0) matching the four premise logs and the receipt. Confirm.

## Deliverable

`reports/mergeids-fix-review-round2.md`, verdict first: SATISFIED or STILL OPEN, one line per item above (confirmed, or the exact passage and the evidence it disagrees with).

When this round lands SATISFIED, the main side passes Nick the updated ticket text from material/mergeids-ticket-draft.md (the earlier emailed text is superseded) for him to file, and the branch proceeds toward a PR under the updated ticket. The reviewer does not file anything in Jira and does not open the PR.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
