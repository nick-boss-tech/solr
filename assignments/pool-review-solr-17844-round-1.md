# Review assignment: SOLR-17844 restored sort assertions (round 1)

Status: DONE (2026-10-23T04:02:32Z, vm3, verdict FIX FIRST, report `reports/pool-review-solr-17844-round-1.md`)

Posted by the phone agent at the owner's direction, 2026-10-22. One reviewer, one branch. Claim before starting by adding `claims/pool-<host>-review-SOLR-17844-round-1.md` per WORKFLOW.md, and work under that claim's lease.

## Why this review exists

Branch `solr-17844-restore-sort-assertions-phone-agent-2` was built and gated by phone-agent-2. No one else has read the change. The standing rule is that a host never audits its own closeout, and the owner wants an independent read before any PR opening. The reviewer must not be phone-agent-2. This branch is test-only, and its first version failed its own gate; the review has to establish that the rework is sound, not merely green.

## The branch

- Branch: `solr-17844-restore-sort-assertions-phone-agent-2`, single squashed commit at `6e9eefd163122d1dccfa6080d64278e95ad4c394`. Verify the tip by ls-remote at claim time; if it has moved, stop and record the new tip here.
- Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Diff, 1 file: `solr/core/src/test/org/apache/solr/uninverting/TestFieldCacheSortRandom.java` (82 insertions, 47 deletions).
- Records: `receipts/SOLR-17844.md` carries both gate attempts (the first failed and is preserved) and the PR draft as its final section. Ticket: https://issues.apache.org/jira/browse/SOLR-17844

## What to review

Read the whole diff against the base, then answer each question in the report with file and line citations at the reviewed head.

1. Independent expectations: are the expected count, order, and sort values computed without reading actual search results? Trace where every expected value comes from. Any expectation derived from the results under test fails this review.
2. Deduplication soundness: the repair deduplicates expected observations by global docID because Lucene partition dispatch can invoke the full-leaf supplier more than once. Establish that repeated observations are asserted to agree, not silently dropped, and that a genuinely missing or extra document would still fail the count or order assertions.
3. Nothing weakened: compare the restored assertions against the test as it stood before the two workaround commits named in the ticket (`3a58c85b9a12` and `403069e7b4`, both ancestors of the base). Report any assertion the original had that the repair does not restore, or state that the comparison was made and none is missing.
4. Mutation evidence: the gate record claims that inverting the actual SortField direction fails both tests with "Unexpected document at position" at seed `1BB02D3762B56611`. Verify from the preserved gate XML, or rerun the mutation once if the evidence is not convincing.
5. First-failure accounting: the receipt records the first candidate failing 42 expected against 14 returned. Confirm the deduplication explains that exact failure shape, or say what else could.
6. Draft accuracy: read the PR draft in `receipts/SOLR-17844.md` against the diff. Every factual claim in it must hold. If one does not, correct the draft in the receipt and record the correction in the report.

## Patch loop

A defect in repair scope (small and mechanical: formatting, an import, a wrong constant) is fixed by the reviewer as one fast-forward commit on the branch, the focused class rerun at the new head, and the fix recorded in the report with its commit. Anything that touches what the assertions check, or how expectations are computed, is a FIX FIRST verdict with the fix specified, not a repair. No history rewrite; if a repair seems to need one, stop and report instead.

## Deliverable

`reports/pool-review-solr-17844-round-1.md` with:

- The verdict, one of: OPEN-READY; FIX FIRST (each fix specified); WON'T DO (evidence attached).
- The six answers, each with citations.
- Any repair commits made, with their files and the rerun result.
- The reviewed head confirmed by ls-remote.

## Limits

No PR opening or state change, no comments, no Jira edits. No production code is in scope for this branch; a finding that production sorting is at fault is reported as a separate observation, not fixed here. No em dashes in any file written for this assignment. Commit as Nick Shanin with no trailers.
