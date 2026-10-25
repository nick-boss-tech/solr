# Claim: solr-18564-exact-head-gate-round-1 (gate)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: LIVE.
- Job: `gates/SOLR-18564-pr5192-exact-head.md`. Tags: gate.
- Slice: the job's legs at head `dd65b21b41dc1d008c51031b71f61964ee2ca169` on `nick-boss-tech/solr:corecontainer-shutdown-closes` (PR apache/solr#5192): changelog YAML parse, tidy with the GATE INVALID guard, Error Prone compile, focused tests `CoreContainerShutdownTest` and `TestCoreContainer`, `:solr:core:check -x test`.
- Head diff checked before the claim: one file (`changelog/unreleased/SOLR-18564.yml`, 7 insertions) on top of `d7caa8cb78`, the fix commit gated on 2026-10-22. Author Nick Shanin.
- Receipt: `receipts/SOLR-18564.md` does not exist on the board yet; this claim creates it with one exact-head section.
- Capability tags used: gate.
- Started: 2026-10-25T02:45Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18564-exact-head-gate-round-1@dd65b21b41.
- Runner: `pool/bin/gate-preflight.sh` then `pool/bin/bg-steps.sh` under `/workspace/tools/.gradle-vm6.lock`. `NODE_ENV` is unset for the runs. Logs: `/workspace/gates/logs/vm6/solr-18564/`. Worktree: `/workspace/worktrees/pool-vm6-18564`.
- Constraints: no PR, no JIRA or GitHub post, no fork push, no change to the branch. Commit as Nick Shanin with no trailers.
- Decision ladder: the job's acceptance rule governs. A tidy change at the head would be a GATE INVALID repair (WORKFLOW.md Gate repairs), recorded and not hidden.

Heartbeat: 2026-10-25T02:45Z
