# Claim: rc3-repeating-failures-rootcause-round-1 (unit 3, TestTermsQParserPlugin metrics leak)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job or assignment: `assignments/rc3-repeating-failures-rootcause-round-1.md`, unit 3, with `assignments/rc3-repeating-failures-unit3-amendment-round-1.md` (test the SOLR-18564 hypothesis first).
- Slice: step 1 from the existing RC3 loop logs (no Gradle); step 2 (focused runs at the RC3 tip with the SOLR-18564 fix) only after its own decision. Deliverable: `reports/rc3-termsqparser-metrics-leak-round-1.md`.
- Capability tags used: premise-run, sweep. Read-only on the logs for step 1.
- Started: 2026-10-25T00:10Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: rc3-repeating-failures-rootcause-round-1@unit3@vm5.
- Links: SOLR-18588 (closed as duplicate of SOLR-18564 by Nick Shanin, 2026-10-24), SOLR-18564 (open). Unit 1 is claimed separately (claims/rc3-tlogreplica-rootcause-round-1.md). Unit 2 is not claimed. Supersedes: none.
- Decision ladder: rung 1 for the logs; rung 2 for the controlled check if step 1 leaves two explanations open.

Heartbeat: 2026-10-25T00:10Z
