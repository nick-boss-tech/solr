# Claim: pool-vm1-gate-9124-live-head

- Claimant host: vm1, matching a file in `hosts/`.
- Status: DONE.
- Job or assignment: `gates/SOLR-9124-live-head-gate.md`.
- Slice: the whole job.
- Capability tags used: gate.
- Started: 2026-10-16T05:29:16Z (UTC).

Heartbeat: 2026-10-16T05:29:16Z

Gate the live tip 07b5f39e35f32fad95017140ec40840c7aed30a4 of solr-9124-submit on vm1 (full hardening gate; job file carries the steps and the proof expectation). Competition check done at pr-prepare tip f3e2a6920f: no file names this head and no claim or gate job covers gating it.

Result: 2026-10-16T05:46:14Z: GATE FAILED at step 4. Steps 0 to 2 green; proof leg premise confirmed by content for TestExactStatsCache; head run 5 tests with 3 failures, all the same server error ("can not use FieldCache on multivalued field: grp_s") in the head commit's new checkWithinGroupOrder: a test defect in the head commit, recorded in gates/SOLR-9124-live-head-gate.md and receipts/SOLR-9124.md. Gate not relaunched.

DONE: 2026-10-16T05:46:14Z. Deliverables: gates/SOLR-9124-live-head-gate.md (Result section), receipts/SOLR-9124.md (new top section).
