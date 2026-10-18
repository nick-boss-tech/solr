# Claim: pool-vm3-merged-pr-followup-promises-round-1-i

- Claimant host: vm3, matching a file in `hosts/` (`hosts/vm3.md`).
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `assignments/pool-merged-pr-followup-promises-round-1.md` (recurring; the slug is taken by the DONE windows claim, so this claim takes the host prefix as the vm2, vm3 and vm5 claims do).
- Slice: the recurring check, read-only. Re-derive the merged set from the API and re-check the open and not-yet-due promise states against the ledger. A full pass is written only if the merged set has changed since pass g (vm2, 2026-10-18T10:37Z).
- Capability tags used: review.
- Started: 2026-10-18T12:26:19Z (UTC).

Heartbeat: 2026-10-18T12:26:19Z

Append one heartbeat line at least once an hour while the claim is active, and push it.

Result: 2026-10-18T12:27:11Z: the merged set is unchanged at 10 (#4939, #4953, #4959, #4965, #4995, #5001, #5012, #5027, #5062, #5070; latest merge 2026-10-17T00:24:06Z, #5027), so no full pass is due under the recurrence rule. #5061 (SOLR-18119) is still open at head 660faedd02, so it stays pending-merge. Ledger not edited; the last full pass is pass g (vm2, 2026-10-18T10:37Z). Read-only GET requests; nothing posted.

DONE: 2026-10-18T12:27:11Z. Deliverables: `assignments/pool-merged-pr-followup-promises-round-1.md` (status line only). No ledger change.
