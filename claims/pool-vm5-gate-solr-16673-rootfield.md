# Claim: pool-vm5-gate-solr-16673-rootfield

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE: 2026-10-19T05:39:35Z. Deliverables: receipts/SOLR-16673.md (round 2 sections).
- Job or assignment: `gates/SOLR-16673-rootfield.md`.
- Slice: the whole gate job. Gates run one at a time on this host: uniquekey first, then rootfield.
- Capability tags used: gate.
- Started: 2026-10-19T00:29:39Z (UTC).

Heartbeat: 2026-10-19T00:29:39Z


Heartbeat: 2026-10-19T00:55:43Z

Result: 2026-10-19T00:55:43Z: GATE INVALID at step 1. Tidy removes `oshai-logging` from `gradle/libs.versions.toml`; the same removal happens on the merge-base, so it is base drift. Gate file: `gates/SOLR-16673-rootfield.md`. Logs: `/workspace/gates/logs/vm5/16673-rootfield/`. Steps 2 to 5 not run.

Result r2: 2026-10-19T04:14:30Z: see gates/SOLR-16673-rootfield.md Result r2. Logs under /workspace/gates/logs/vm5/16673-rootfield-r2/.
