# Claim: solr-10928-live-head-gate-round-1 (gate)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: LIVE.
- Job: `gates/SOLR-10928-regate-live-head.md`. Tags: gate.
- Slice: the job's legs at head `f2001e640a464e8e861ee3c202821b6dcc4bd497` on `nick-boss-tech/solr:solr-10928-submit` (verified by ls-remote at claim time): tidy with the GATE INVALID guard, Error Prone compile, `:solr:core:check -x test`, and the focused classes `QueryElevationComponentTest`, `QueryElevationComponentParamTest`, and the distributed elevation test named on the branch.
- Head diff checked before the claim: since the earlier gated commit `1ff57c1b21`, two commits (`d5621a2928`, `f2001e640a`), 3 files, 183 insertions, 3 deletions. Author Nick Shanin.
- Receipt: `receipts/SOLR-10928.md`, not on the board yet; this claim creates it with one exact-head section.
- Capability tags used: gate.
- Started: 2026-10-25T03:40Z (UTC). Attempts: 1. Idempotency key: solr-10928-live-head-gate-round-1@f2001e640a.
- Runner: `pool/bin/gate-preflight.sh` then `pool/bin/bg-steps.sh` under `/workspace/tools/.gradle-vm6.lock`. Logs: `/workspace/gates/logs/vm6/solr-10928/`. Worktree: `/workspace/worktrees/pool-vm6-10928`.
- Constraints: no PR, no JIRA or GitHub post, no fork push, no change to the branch. Commit as Nick Shanin with no trailers.
- Decision ladder: the job's acceptance rule governs. A tidy change at the head would be a GATE INVALID repair, recorded and not hidden.

Heartbeat: 2026-10-25T03:40Z
