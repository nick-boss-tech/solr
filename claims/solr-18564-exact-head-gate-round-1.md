# Claim: solr-18564-exact-head-gate-round-1 (gate)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job: `gates/SOLR-18564-pr5192-exact-head.md`. Tags: gate.
- Slice: the job's legs at head `dd65b21b41dc1d008c51031b71f61964ee2ca169` on `nick-boss-tech/solr:corecontainer-shutdown-closes` (PR apache/solr#5192): changelog YAML parse, tidy with the GATE INVALID guard, Error Prone compile, focused tests `CoreContainerShutdownTest` and `TestCoreContainer`, `:solr:core:check -x test`.
- Head diff checked before the claim: one file (`changelog/unreleased/SOLR-18564.yml`, 7 insertions) on top of `d7caa8cb78`, the fix commit gated on 2026-10-22. Author Nick Shanin.
- Receipt: `receipts/SOLR-18564.md` (new file, one exact-head section).
- Capability tags used: gate.
- Started: 2026-10-25T02:45Z (UTC). Attempts: 1. Idempotency key: solr-18564-exact-head-gate-round-1@dd65b21b41.
- Runner: `pool/bin/gate-preflight.sh` then `pool/bin/bg-steps.sh` under `/workspace/tools/.gradle-vm6.lock`. Logs: `/workspace/gates/logs/vm6/solr-18564/`. Worktree: `/workspace/worktrees/pool-vm6-18564`.
- Constraints: no PR, no JIRA or GitHub post, no fork push, no change to the branch.
- Decision ladder: the job's acceptance rule governs. No tidy repair was needed.

Heartbeat: 2026-10-25T03:12Z

Result: 2026-10-25T03:12Z: gate green at head dd65b21b41dc1d008c51031b71f61964ee2ca169. Changelog YAML parses. Preflight tidy clean. Legs rc 0 (tidy, Error Prone compile, `check -x test`). Focused runs with seeds 18564ABC0001 and 18564ABC0002 (cleanTest before each): `CoreContainerShutdownTest` 1 test, 0 failures, 0 errors, 0 skipped in each run; `TestCoreContainer` 25 tests, 0 failures, 0 errors, 3 skipped in each run (the 3 are Windows-only assumptions). Deviation recorded in the receipt: the runner inherited `NODE_ENV=production`; no executed step ran npm.

DONE: 2026-10-25T03:12Z (gate). Deliverable: `receipts/SOLR-18564.md` and this claim's Result lines.

Note 2026-10-25T03:41Z: `claims/solr-18564-exact-head-gate-vm2.md` (vm2, 03:33:51Z, commit 67e4e7bd9a) claims the same job at the same head. This job was already DONE at 03:12Z, and `receipts/SOLR-18564.md` already holds the green exact-head section. vm2 should not repeat the run without a reason recorded in its claim; if it does run, it writes its own section and does not edit this one.
