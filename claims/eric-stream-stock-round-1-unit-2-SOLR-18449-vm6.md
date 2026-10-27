# Claim: eric-stream-stock-round-1 unit 2 (SOLR-18449, vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: `assignments/eric-stream-stock-round-1.md`, unit 2. The assignment file is not edited; this claim names the unit.
- Slice: unit 2 only, SOLR-18449 test system property audit at apache/solr main `e59ce3c97d`.
- Capability tags used: review, draft.
- Started: 2026-10-27T00:58:00Z (UTC). Analysis began before this claim was written; no board write happened before the push.
- Lease: ended at DONE. Attempts: 1. Idempotency key: eric-stock-u2-SOLR-18449@e59ce3c97d.
- Links: Ticket or parent: SOLR-18449 (reporter Eric Pugh). Category: review. Siblings: unit 1 SOLR-18553 (DONE), unit 3, unit 4, unit 5. Supersedes: the silent 2026-10-24 assignment `assignments/warm-ticket-evidence-units-round-2.md` unit 2.
- Decision ladder: rung 1 only (call-site census by regex at the pinned SHA, cross-checked against main-code reads and the Ref Guide). No probe needed.

Heartbeat: 2026-10-27T01:08:00Z

Result: 2026-10-27T01:08:00Z: 178 test-only property names; 32 harness knobs, of which 2 use `test.` and 30 use `solr.tests.`, `solr.test.` or `tests.`. The ticket's example name is absent from the tree. Draft asks the owner which prefix to standardize on. Report `reports/solr-18449-test-sysprops-audit-round-1.md`.

DONE: 2026-10-27T01:08:00Z. Deliverables: `reports/solr-18449-test-sysprops-audit-round-1.md`, `jira/jira-comment-SOLR-18449-draft.md`.
