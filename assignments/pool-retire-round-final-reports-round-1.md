# Pool assignment: retire candidates, final evidence packs, round 1

Status: DONE 2026-10-18T07:02:03Z (vm3; claims/pool-vm3-retire-round-final-reports-round-1.md; index reports/retire-final-index.md)

Capability tags: `review`. No build work. Claim under `claims/` before starting, per WORKFLOW.md.

## Starting record

`reports/retire-candidates-review-round.md`, with its part reports `reports/retire-candidates-review-round-rr1.md` and `reports/retire-candidates-review-round-rr2.md`. The round returned nine items: 7 RETIRE CLEAN (SOLR-17722, SOLR-17882, SOLR-12743, SOLR-17825, SOLR-10198, SOLR-18119-submit, SOLR-9759) and 2 SALVAGE FIRST (SOLR-17372, SOLR-18277). The owner's retire rule: one review round first; retire unless the round finds something gainable, in which case try it even if it is not directly related to the ticket. The round is done. This assignment assembles the final evidence pack per candidate so the owner's deletion and closing calls are one reading each.

## Bounds

No branch deletions, no PR closings, no Jira changes, no pushes to submit branches. Those stay with the owner. The packs, and the one receipt record below, are the whole deliverable.

## Work

1. One pack per candidate, at `reports/retire-final-<ticket>.md` (for SOLR-18119-submit, `reports/retire-final-18119-submit.md`; for SOLR-10198, the branch item, `reports/retire-final-10198.md`). Each pack states:
   - the premise state, from the ticket's receipt in `receipts/` and the round's part report, quoted or cited, not paraphrased from memory;
   - what the branch holds at its live fork tip (verify the tip by ls-remote or the fork inventory record, and state the head checked);
   - what is gainable from the branch, if anything, and what has already been taken from it;
   - the exact step the owner would take: the branch or branches to delete (named in full), any PR to close (with its state), and any Jira note the closing would need, drafted in the pack;
   - any condition the verdict depends on (the round named one for SOLR-18119-submit: retire-clean only while the shipped pair lands or is accepted; state the condition and how the owner checks it).
2. SOLR-18277 (salvage first, a record not code): write the missing receipt record into `receipts/SOLR-18277.md` from the branch and gate records. The round's finding: fork runs show `ShardSplitTest` passing on base production, so the branch's tests do not pin the fix, and the existing receipt records only an inconclusive result. The record written here states the gate and proof state plainly, run by run as the records give it, so the receipt no longer reads as an open question. Its pack then states the retire case on the completed record.
3. SOLR-17372 (salvage first in the round; premise run since run): the round's salvage was one premise run, legs on main without the hunk and on the branch head with it. That run has since been run on the main side and returned NOT SUPPORTED (the ticket's receipt, `receipts/SOLR-17372.md`, carries the current state; read it and the round's part report together, and where they differ in age, the receipt governs). Its pack states the retire case plainly on that verdict: what the branch holds, why the premise failing closes the salvage, and the exact deletion step.
4. A short index at the top of the claim report or as `reports/retire-final-index.md`: one line per candidate, verdict, pack path, and the owner step it waits on.

## Deliverable

The nine packs, the completed `receipts/SOLR-18277.md` record, and the index. The assignment is discharged when every candidate from the retire round has a pack whose owner step can be executed without further reading.
