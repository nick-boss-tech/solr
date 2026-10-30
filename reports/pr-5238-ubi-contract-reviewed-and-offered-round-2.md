# Listener UBI proof review and public follow-up

Status: Linux proof reviewed; public follow-up posted and read back

## Heads and scope

- Reviewed board snapshot: `c3399be9d12684579c6e38f690fcf5714ad56935`.
- Live PR 5238 head: `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`, open.
- Tested and remotely verified fork head: `c2c387064a83def34cece4163eccded28318caeb` on `solr-11994-pr5238-ubi-submit`.
- Untested initial prototype: `de4d222bb12302eb0e10aeb5697bae307eb2cd22`.
- Parent-to-tested-head diff: one added test file, 375 lines. Production Java diff empty; whitespace check clean. PR head is an ancestor of the tested head, verified with git.
- Reviewed `de4d222bb1..c2c387064a`: the only changes rewrap two ConfigRequest calls. There are two commits above the live PR head, not one.

## Proof reviewed

Read the actual receipt `receipts/PR-5238-ubi-contract.md`, superseding round-2 job and original required proof legs, round-2 execution report and round-1 closeout at the recorded board snapshot.

- New UBI contract class: 9 tests on each of seeds 5238AB1 and 5238AB2, all passing without skips/errors/failures.
- Existing listener class: 19 tests, all passing; CPU test executed.
- Neighbors: PingRequestHandlerTest 6, TestSolrConfigHandler 8, TestSolrRequestInfo 3 and TestSubQueryTransformerDistrib 1, all passing without skips/errors/failures.
- Clean tidy and successful root `check -x test` at the tested head, in a standalone full Linux clone, with NODE_ENV unset. Successful root check ended 2026-10-29 16:17:10 UTC and reports 4m45s.
- No Windows execution or fail-before result is claimed. Round 1 halted at tidy and provides no test result.

The proof supports a positive test-only consumer pattern, not a production-hook defect or a complete UBI migration. It does not establish persistent sinks, core lifecycle, asynchronous lifetime, grouped/ID-omitting results, redaction or production overhead.

## Execution-record clarification

Nick's relayed vm4 handoff corrects the round-2 report's interruption narrative: `check-interrupted-attempt-3.log` refers to the 15:30 attempt, and the earlier 15:25 attempt's log was overwritten. The successful `check.log` and `check.rc = 0` are reported intact. The final successful run, the completed XML counts and the required scope are unchanged by that correction. No interrupted attempt is treated as a successful run.

This is a correction to interruption-log attribution, not an unexplained test failure. I verified the board receipt/report and git state; I did not directly inspect vm4's private filesystem logs. The gate host's receipt and claim are not edited here. Unrelated SOLR-18178 and SOLR-16395 items in the handoff are outside this review.

## Live discussion and communication

Eric replied at 2026-10-29 15:17-15:18 UTC: the ingestion boundary makes sense, helper reuse is accepted, and his UBI response raises storage affecting query execution. The drafted comment therefore explicitly states that the callback is synchronous and does not isolate slow sink latency. This is not a claim of asynchronous or zero-overhead recording.

Prepared `PR-5238-ubi-followup-comment.md` after reading live `comment-formula.md`. It has the exact GitHub disclosure header, first-person/neutral wording, no em/en dashes or internal process vocabulary, a pinned diff and ancestry-checked fast-forward commands. The standing clean-proof PR-review follow-up authorization in workspace AGENTS.md applies to the factual offer of this reviewed test patch; it does not authorize other public actions. Before posting, refresh the heads/discussion and check for an equivalent comment. Read the stored comment back separately and record its URL/time.

## Posted follow-up

Posted once at **2026-10-30 00:32:11 UTC**, October 29 at 20:32:11 EDT:
[PR 5238 follow-up](https://github.com/apache/solr/pull/5238#issuecomment-6511901747).
The separate REST read-back matches the draft byte-for-byte. The PR remains
open at `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`, with the same nine review
submissions and exactly one additional conversation comment (the posted offer).
No equivalent offer existed in the refreshed discussion. The fork head was
reverified as `c2c387064a83def34cece4163eccded28318caeb` immediately before posting.

The comment offers the tested example with pinned fast-forward commands and
explicitly distinguishes exception isolation from slow-sink latency. It cites
round 2 only and reports NODE_ENV unset for the successful root check. Local
read-back evidence is saved in the Windows workspace under
`research/branch-reviews/2026-10-29-pr-batch/evidence/PR-5238/ubi-green/`.
The source branch and contributor PR head were not changed by this follow-up.
