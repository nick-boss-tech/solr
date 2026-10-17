# Claim: pool-vm1-solr-9124-test-fix-round-1

- Claimant host: vm1, matching a file in `hosts/`.
- Status: DONE.
- Assignment: `assignments/pool-vm1-solr-9124-test-fix-round-1.md`.
- Slice: the whole assignment (test fix plus the re-gate under `gates/SOLR-9124-test-fix-gate.md`).
- Capability tags used: implementation, gate.
- Started: 2026-10-16T05:58:46Z (UTC).

Heartbeat: 2026-10-16T05:58:46Z

Fix the SOLR-9124 head test defect recorded by gates/SOLR-9124-live-head-gate.md (GATE FAILED at 07b5f39e35, step 4): checkWithinGroupOrder groups on grp_s, multivalued in the harness schema; the fix groups on grp_s1 (the *_s1 dynamic field, multiValued="false" in schema.xml, schema12.xml and schema15.xml) with the same values and assertions, test code only. Then a full gate at the new head under gates/SOLR-9124-test-fix-gate.md.

Result: 2026-10-16T07:07Z: fix commit 3dd985b93ac0e26a3850c338765ba1235bd811d4 pushed plainly to solr-9124-submit (fast-forward from 07b5f39e35). Gate GATE GREEN (log g9124-fix-gate.log, seed 9124C0FFEE9124): steps 0, 1, 2 and 5 rc 0; head run 5 tests, 0 failures from fresh XML (archived at ~/workspace/tools/g9124-fix-head-xml/); proof leg on base production exactly 1 failure, the TestExactStatsCache grouped-scores divergence at checkGroupedScores, and no grp_s FieldCache error in any XML (proof XML archived at ~/workspace/tools/g9124-fix-proof-xml/).

DONE: 2026-10-16T07:07Z. Deliverables: gates/SOLR-9124-test-fix-gate.md (Result section), receipts/SOLR-9124.md (new top section), assignments/pool-vm1-solr-9124-test-fix-round-1.md (Outcome section).
