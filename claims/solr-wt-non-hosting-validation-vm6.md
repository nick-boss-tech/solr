# Claim: wt routing bug validation on upstream main, with receipts (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (questions 1 to 3). Questions 4 and 5 remain with the reviewer.
- Job or assignment: `assignments/solr-wt-non-hosting-node-round-1.md`. The reviewer has no Gradle, so vm6 runs the probe and writes the receipts.
- Slice: questions 1 to 3 of the assignment (probe on upstream main, mechanism with temporary prints, other endpoints). JIRA search and draft are for the reviewer, unless vm6 finishes first.
- Capability tags used: gate.
- Started: 2026-10-29T22:30:00Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-wt-non-hosting-validation-round-1@852b427c11.
- Links: Ticket or parent: SOLR-16398 (source of the observation). Category: see `pool/work-map.md`. Siblings: none. Supersedes: none.
- Constraints: scratch clone under `/workspace/tmp/wt-validate` only; temporary prints reverted; no fork push, no PR, no JIRA or GitHub post.

Heartbeat: 2026-10-29T22:40:00Z

Result: 2026-10-29T22:40:00Z: reproduced on upstream main (reload; split has no JAX-RS route there). Mechanism confirmed with temporary prints, reverted. Receipt: `receipts/solr-wt-non-hosting-validation-round-1.md`.

DONE: 2026-10-29T22:40:00Z. Deliverables: `receipts/solr-wt-non-hosting-validation-round-1.md`; assignment status updated.
