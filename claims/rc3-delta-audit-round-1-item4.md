# Claim: rc3-delta-audit-round-1-item4

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: `assignments/rc3-delta-audit-round-1.md`, item 4 (the changelog state). Item 1 is vm2's and DONE; item 2 is vm2's and ACTIVE (`claims/rc3-delta-audit-round-1-item2.md`, covering all eight cherry-picks); item 3 is vm6's and DONE. The assignment stays OPEN.
- Slice: item 4 only: commits 1a45846c45, 3702672a00, bba4024eed, 749712b719 and the RC3 CHANGELOG.md.
- Capability tags used: review. Research only: no code, no comments, no push except this claim and the report section.
- Started: 2026-10-24T06:42:41Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: rc3-delta-audit-round-1@item4.
- Links: Ticket or parent: RC3 vote for 10.1.0 (closes 2026-10-27 11:00 UTC). Category: release. Siblings: `claims/rc3-delta-audit-round-1-item1.md` (vm2, DONE), `claims/rc3-delta-audit-round-1-item2.md` (vm2, ACTIVE), `claims/rc3-delta-audit-round-1-item3.md` (vm6, DONE). Supersedes: none.
- Decision ladder: rung 1 for the changelog, the scripts and the commit history; a probe only if rung 1 leaves two explanations open.

Heartbeat: 2026-10-24T06:42:41Z

Result: 2026-10-24T06:44:37Z: FINDING on dependency-list duplication across added, upgraded and removed (filename-based summary); SOLR-18450 not announced (CLEAN); cuVS known-issue placement is an owner call. Section in reports/rc3-delta-audit-round-1.md.

DONE: 2026-10-24T06:44:37Z. Deliverable: `reports/rc3-delta-audit-round-1.md` (item 4 section).
