# Claim: SOLR-18507 gate, phase 4 (live fingerprint deltas)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: HANDED BACK (retry failed at compile; see gates/SOLR-18507-phase-4.md attempt 2)
- Job or assignment: `gates/SOLR-18507-phase-4.md`.
- Slice: the whole gate job at head d797b9b31060252bf941e28d53aef1ce78e641b3 (fork tip read at claim time; the gate file named f50c2271a3, which is no longer the tip). Steps 0 to 5, then the receipt `receipts/SOLR-18507.md` section.
- Capability tags used: gate.
- Started: 2026-10-20T04:37:19Z (UTC).

Heartbeat: 2026-10-20T04:37:19Z

Result: 2026-10-20T05:30:16Z: HANDED BACK. Attempt 1 test failed (6 of 7, seed 18507A004, searcher lifecycle defect in the tests, repaired in 13f2c6d045). Attempt 2 (the one retry) failed at compile on the repair (duplicate searcherRef declaration). Record and fix: gates/SOLR-18507-phase-4.md.
