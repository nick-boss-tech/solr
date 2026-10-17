# Claim: SOLR-16013 re-gate at the live tip (vm2, gate backlog coordinator)

Status: CLOSED 2026-10-17 (batch close approved by Nick; no heartbeat within 2 hours; listed in reports/claims-stale-batch-close-2026-10-17.md)

- Claimed: 2026-10-12T09:39Z by vm2 (coordinator). Job: gates/SOLR-16013.md.
- Branch tip read at claim time: ba26b7028917ef29d5b5e1cfe75ee83862cccf63 (solr-16013-submit, ls-remote). No branch change under this job.
- Capability tags: gate (re-gate only, no push).
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Gate run at ba26b70289 ended 2026-10-12T09:55:48Z: FAILED. Focused test testZkClientClosesOnlyAfterOverseerCloseCompletes failed with "no node is running the overseer" (shared-cluster ordering in OverseerCloseOrderingTest; details in gates/SOLR-16013.md Investigation).
- Next step: assignments/solr-16013-overseer-fixture.md (review coordinator, step b). No receipt written; the claim is closed as FAILED, not DONE.
- Status: CLOSED (vm2 coordinator, 2026-10-13T01:37Z). The FAILED run at ba26b70289 is superseded: the fix landed at 966bb4c4eb44 and the history scrub and title commit at 408317d7e2 are GATE GREEN on vm1 (receipts/SOLR-16013.md, top section). ls-remote confirms solr-16013-submit at 408317d7e2. No re-gate owed by this job.
