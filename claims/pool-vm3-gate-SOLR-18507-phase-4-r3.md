# Claim: SOLR-18507 gate, phase 4, new gate job at head 807f9d42db (vm3)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED
- Supersedes: `claims/pool-vm3-gate-SOLR-18507-phase-4.md` (HANDED BACK at attempt 2, compile error in repair 13f2c6d045). That claim is not reopened; this is a new gate job after implementation, as WORKFLOW.md "Gate repairs and hand-back" says.
- Job or assignment: `gates/SOLR-18507-phase-4.md`.
- Slice: the whole gate job at head 807f9d42db, steps 0 to 5, then the receipt section in `receipts/SOLR-18507.md`.
- Capability tags used: gate, implementation (one repair commit).
- Started: 2026-10-20T05:33:20Z (UTC).
- Idempotency key: SOLR-18507-phase-4@807f9d42db.

Heartbeat: 2026-10-20T05:33:20Z

## Commits on solr-18507-phase-4-submit (all fast-forward, from d797b9b310)

- 76d29000fb: tidy output (IndexFingerprint.java, SolrCoreFingerprintCacheTest.java), tidied tree 9eb840aa20. Repair of attempt 1.
- 13f2c6d045: the four test blocks that closed the shared core's searcher now release a RefCounted in finally (attempt 1 test failure, 6 of 7; investigation in the gate file).
- 807f9d42db: repair of 13f2c6d045, which declared a second `searcherRef` in the same method (attempt 2 compile error). Renamed to `reloadedSearcherRef`, test code only, no expected value changed.
- Commit message format: 76d29000fb and 13f2c6d045 and 807f9d42db predate the `TICKET: gate repair:` format. Those commits are not rewritten (no force push). Later repairs use the format.

## Decision (ladder, rung 4)

Attempt 2's handback came from a one-line compile error in my own repair. The repair is in scope (test code, one line, no expected value changed). The job's purpose is the phase 4 gate, so the claim carries on: the fix goes in as a repair commit and the job reruns from step 0 on 807f9d42db under this new claim.
