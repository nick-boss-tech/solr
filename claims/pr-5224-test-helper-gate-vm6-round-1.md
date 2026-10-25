# Claim: pr-5224-test-helper-round-1 gate (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`. Linux, JDK 21.0.12.1+1.
- Status: CLAIMED.
- Job: `gates/pr-5224-test-helper-round-1.md`, the whole gate at pinned head `1f3ed5658862b8fd682fcb999a1d3dd12863957f` (fork `nick-boss-tech/solr:solr-18585-submit`).
- Slice: clean-clone tidy preflight, four focused test methods with seed 6A68830B3A1BB525 from fresh XML, `check -x test`, `git diff --check`, receipt at `receipts/PR-5224-test-helper.md`.
- Capability tags used: gate.
- Constraints: no write to apache/solr, mkhludnev/solr, the live PR, JIRA or GitHub comments; a tidy repair only if tidy changes files, under the gate's repair rule.
- Started: 2026-10-25T23:45Z (UTC). Attempts: 1. Lease: live while heartbeats continue.
- Decision ladder: rung 1 (helper and test diff), rung 2 (the focused run's XML counts), rung 3 (the gate file's own requirements).

Heartbeat: 2026-10-25T23:45Z
