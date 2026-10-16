# Claim: pool-vm1-solr-9124-test-fix-round-1

- Claimant host: vm1, matching a file in `hosts/`.
- Status: ACTIVE.
- Assignment: `assignments/pool-vm1-solr-9124-test-fix-round-1.md`.
- Slice: the whole assignment (test fix plus the re-gate under `gates/SOLR-9124-test-fix-gate.md`).
- Capability tags used: implementation, gate.
- Started: 2026-10-16T05:58:46Z (UTC).

Heartbeat: 2026-10-16T05:58:46Z

Fix the SOLR-9124 head test defect recorded by gates/SOLR-9124-live-head-gate.md (GATE FAILED at 07b5f39e35, step 4): checkWithinGroupOrder groups on grp_s, multivalued in the harness schema; the fix groups on grp_s1 (the *_s1 dynamic field, multiValued="false" in schema.xml, schema12.xml and schema15.xml) with the same values and assertions, test code only. Then a full gate at the new head under gates/SOLR-9124-test-fix-gate.md.
