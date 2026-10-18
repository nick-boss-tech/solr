# Claim: SOLR-17268 full re-gate on the patched head (vm3)

Status: DONE 2026-10-18T21:45Z. Deliverable: receipts/SOLR-17268.md (new top section, GREEN at f888c1cea8), gates/SOLR-17268.md (re-gate run), assignments/pool-solr-17268-regate-round-1.md (DONE).

- Claimant host: vm3. Job: `gates/SOLR-17268.md` (re-gate section) and `assignments/pool-solr-17268-regate-round-1.md`.
- Head: `f888c1cea8` on `solr-17268-submit`, pushed by Nick Shanin at 2026-10-18T01:30Z. Its +/- lines match `gates/SOLR-17268-test-fix-proposal.patch` exactly.
- Decision: the gate's "tip moved at claim time" rule says stop. The operator approved the fix path on 2026-10-18 (option (a): accept the patch and run the full gate on the new head). This claim runs on `f888c1cea8` on that approval, and the gate file records it.
- Steps: 0 changelog parse, 1 tidy, 2 Error Prone, 3 legs A and B, 4 focused head run, 5 module check. Logs under `gates/logs/vm3/17268/`.
