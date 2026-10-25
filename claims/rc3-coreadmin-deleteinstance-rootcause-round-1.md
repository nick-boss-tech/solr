# Claim: rc3-repeating-failures-rootcause-round-1 (unit 2, CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job or assignment: `assignments/rc3-repeating-failures-rootcause-round-1.md`, unit 2. Ticket SOLR-18587.
- Slice: reproduce `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` at the RC3 tip (749712b7194) and at apache/main (13c693d805f), focused, seed 787C6229591C6F1E (the loop's run-001 seed for this method). Deliverable: `reports/rc3-coreadmin-deleteinstance-rootcause-round-1.md`.
- Capability tags used: premise-run, records. Runs under the host lock, one at a time. No fix branch in this round.
- Started: 2026-10-25T01:38Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: rc3-repeating-failures-rootcause-round-1@unit2@vm5.
- Links: SOLR-18587 (Nick Shanin, Open). Related: SOLR-9433 (Reopened). Units 1 and 3 are done by their reports. Supersedes: none.
- Decision ladder: rung 1 for the code and history; rung 2 for the repro runs.

Heartbeat: 2026-10-25T01:38Z
