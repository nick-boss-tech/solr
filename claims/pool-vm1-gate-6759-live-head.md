# Claim: pool-vm1-gate-6759-live-head

- Claimant host: vm1, matching `hosts/vm1.md`.
- Status: DONE.
- Job or assignment: `gates/SOLR-6759-live-head-gate.md`.
- Slice: the whole job.
- Capability tags used: gate.
- Started: 2026-10-16T03:43:11Z (UTC).

Competition check (2026-10-16T03:41Z to 03:43Z, at pr-prepare tip da2dbfa8cb): claims/, assignments/ and gates/ were searched by filename and by content for 6759 and for the head 680044f1d2. No gate job file exists for this branch, no file names this head, no claim covers gating it, and no assignment names another host for it; the mentions are old audit, review and draft-fidelity records, all DONE or audit-only. This claim is the first for this gate.

Heartbeat: 2026-10-16T03:43:11Z
Heartbeat: 2026-10-16T04:28:00Z (main gate GREEN; supplementary parent-head proof leg running)

Result: 2026-10-16T04:31Z: GATE GREEN at 680044f1d2a6a36b4543f4d52bb552e32b142919 (log ~/workspace/tools/g6759-gate.log on vm1). All steps rc 0; focused TestExpandComponent 10 of 10 from fresh JUnit XML at seed 6759C0FFEE6759. Proof leg on merge-base production: exactly 1 failure, the premise test with COMPLETED one less than CREATED. The new query-limit test passes on the merge-base and on the parent head too (supplementary leg, ~/workspace/tools/g6759-proof-parent.log), so it is a regression pin, not a discriminator for the delta commit; recorded in the gate job and the receipt. Receipt: receipts/SOLR-6759.md.

DONE: 2026-10-16T04:31Z. Deliverables: receipts/SOLR-6759.md (new top section), gates/SOLR-6759-live-head-gate.md (DONE with outcome section).
