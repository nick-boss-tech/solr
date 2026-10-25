# Claim: solr-10928-live-head-gate-round-1 (gate)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job: `gates/SOLR-10928-regate-live-head.md`. Tags: gate.
- Slice: the job's legs at head `f2001e640a464e8e861ee3c202821b6dcc4bd497` on `nick-boss-tech/solr:solr-10928-submit` (verified by ls-remote at claim time): tidy with the GATE INVALID guard, Error Prone compile, `:solr:core:check -x test`, and the focused classes `QueryElevationComponentTest`, `QueryElevationComponentParamTest`, and `DistributedQueryElevationComponentTest`.
- Head diff checked before the claim: since the earlier gated commit `1ff57c1b21`, two commits (`d5621a2928`, `f2001e640a`), 3 files, 183 insertions, 3 deletions. Author Nick Shanin.
- Receipt: `receipts/SOLR-10928.md` (new file, one exact-head section).
- Started: 2026-10-25T03:40Z (UTC). Attempts: 1. Idempotency key: solr-10928-live-head-gate-round-1@f2001e640a.
- Runner: `pool/bin/gate-preflight.sh` and `pool/bin/bg-steps.sh` under `/workspace/tools/.gradle-vm6.lock`, launched with `NODE_ENV` unset. Logs: `/workspace/gates/logs/vm6/solr-10928/`. Worktree: `/workspace/worktrees/pool-vm6-10928`.
- Constraints: no PR, no JIRA or GitHub post, no fork push, no change to the branch.
- Decision ladder: the job's acceptance rule governs. No tidy repair was needed.

Heartbeat: 2026-10-25T04:01Z

Result: 2026-10-25T04:01Z: gate green at head f2001e640a464e8e861ee3c202821b6dcc4bd497. Preflight tidy clean. Legs rc 0 (tidy, Error Prone compile, `check -x test`). Focused runs with seeds 10928ABC0001 and 10928ABC0002 (cleanTest before each): `QueryElevationComponentTest` 23 tests, `QueryElevationComponentParamTest` 4 tests, `DistributedQueryElevationComponentTest` 1 test; 0 failures, 0 errors, 0 skipped in each run.

DONE: 2026-10-25T04:01Z (gate). Deliverable: `receipts/SOLR-10928.md` and this claim's Result lines.

Note 2026-10-25T04:55Z: `claims/solr-10928-live-head-regate-vm2.md` (vm2, 03:33:51Z) claims the same job at the same head. This job was already DONE at 04:01Z, and `receipts/SOLR-10928.md` already holds the green exact-head section. vm2 should not repeat the run without a reason recorded in its claim; if it does run, it writes its own section and does not edit this one.
