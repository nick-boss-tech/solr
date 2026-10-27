# Claim: SOLR-17887 internal selection on the file-tree branch, round 1

- Claimant host: vm5, matching the Host id line in `hosts/vm5.md`.
- Status: ACTIVE; a receipt at the exact new head is the completion record.
- Job or assignment: owner direction, 2026-10-27, to close the acceptance gap on SOLR-17887 ("Single click selects files and fodlers") and re-gate. Ticket: https://issues.apache.org/jira/browse/SOLR-17887 (acceptance criteria 6 and 7).
- Slice: `FileTree.kt` makes a click update the component's own selection; `selectedPath` sets the selection when the caller changes it. Adds `FileTreeTest` cases for click selection and for the caller's `selectedPath`. Visual check against the look-and-feel criterion is out of this slice unless it fits in the same round.
- Capability tags used: implementation, records.
- Started: 2026-10-27T21:09:59Z (UTC). Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-17887-internal-selection-round-1@a22da1bfe72399204b6fd87b8b084e19e4a9bd90.
- Links: Ticket: SOLR-17887 (parent SOLR-17886). Branch `nick-boss-tech/solr:solr-17887-file-tree`, base tip `a22da1bfe72399204b6fd87b8b084e19e4a9bd90`. Prior gate: `receipts/SOLR-17887.md` (vm6, `9c7f72aa0e`). Prior claim: `claims/christos-reply-17887-pr-prep-round-1-vm5.md`. Category: ui. Supersedes: none.

Decision rung: rung 3 (follow stated intent). The ticket's wording "Single click selects files and folders" is the stated intent; the current component only highlights a caller-provided path. Christos's scope confirmation (comment 18133017) does not cover selection behavior, so this is the ticket's own criterion, not a scope widening.

Method note: the checkout is a detached shallow copy under `/workspace/gates/solr-17887/`; the JVM test runs use `material/ui-test-setup/run-ui-jvm-tests.sh`.

Heartbeat: 2026-10-27T21:09:59Z

Result: none yet.
