# Claim: SOLR-17268 full gate (vm1)

- Claimed: 2026-10-16 by vm1, on dispatch from the main agent. Job: `gates/SOLR-17268.md`. Capability tags: gate.
- Dispatched head `06d6b6b4cf1ebd4b75b712132621e7b701a0f1fc`; the tidy rule in the dispatch brief moved the gated head to `e97dacd2232369912365ace3141080187b4391c3` (tidy commit pushed by this lane).
- Status: DONE 2026-10-16. Gate executed in full at `e97dacd223`: GATE FAILED at step 4 (focused head runs), with steps 0, 1, 2 and 5 green and both proof legs recorded. The failures are two defects in the new test code, not in the production change; see `receipts/SOLR-17268.md` for the per-test record and the job file for the outcome section. Handed back to the main agent under the job's failure route; no relaunch attempted.
