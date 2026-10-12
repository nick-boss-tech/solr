# Claim: SOLR-16013 re-gate at the live tip (vm2, gate backlog coordinator)

- Claimed: 2026-10-12T09:39Z by vm2 (coordinator). Job: gates/SOLR-16013.md.
- Branch tip read at claim time: ba26b7028917ef29d5b5e1cfe75ee83862cccf63 (solr-16013-submit, ls-remote). No branch change under this job.
- Capability tags: gate (re-gate only, no push).
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Gate run at ba26b70289 ended 2026-10-12T09:55:48Z: FAILED. Focused test testZkClientClosesOnlyAfterOverseerCloseCompletes failed with "no node is running the overseer" (shared-cluster ordering in OverseerCloseOrderingTest; details in gates/SOLR-16013.md Investigation).
- Next step: assignments/solr-16013-overseer-fixture.md (review coordinator, step b). No receipt written; the claim is closed as FAILED, not DONE.
- Status: FAILED (vm2 coordinator, 2026-10-12T10:08Z).
