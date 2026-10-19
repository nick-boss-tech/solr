# Claim: vm3 investigation of SOLR-18507 phase 2 round 4 failure

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED.
- Job: `gates/SOLR-18507-phase-2.md`, owner complaint 6 in `pool/owner-complaints-2026-10-19.md`.
- Scope: read the branch `solr-18507-phase-2-submit` at head `613a1b29fea`, find why `SolrCoreFingerprintCacheTest.testGrossFingerprintCacheHitAfterDelete` reaches a closed IndexReader after a delete, then either land a fix with a gate re-run request or write the diagnosis into the gate file.
- Gate runs stay with vm1 (its round 4 claim is `claims/pool-vm1-gate-SOLR-18507-phase-2.md`). This claim does not relaunch the gate.
- Capability tags used: investigation, implementation.
- Started: 2026-10-19T17:02Z (UTC).

Heartbeat: 2026-10-19T17:02Z

Heartbeat: 2026-10-19T18:05Z (round 4 reproduced; cause in the test; fix 137e77a4c8 pushed to solr-18507-phase-2-submit; diagnosis in gates/SOLR-18507-phase-2.md)

DONE: 2026-10-19 by vm3. Diagnosis in gates/SOLR-18507-phase-2.md. Fix commit 137e77a4c8 on solr-18507-phase-2-submit (fast-forward from 613a1b29fe). Gate re-run on 137e77a4c8 is for vm1; this claim does not relaunch it.
