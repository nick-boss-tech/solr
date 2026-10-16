# Claim: SOLR-15805 re-gate at the live tip (vm2, gate backlog coordinator)

- Claimed: 2026-10-12T17:28Z by vm2 (coordinator). Job: gates/SOLR-15805.md.
- Branch tip at claim time: 6d78603d82575dfaef78584d5f59f59c107ff0ed (solr-15805-submit, ls-remote). Tree identical to the gated commit 3432f950f0. No branch change under this job.
- Capability tags: gate (re-gate only, no push).
- Runner: gates/g15805-gate.sh, worktree /workspace/gates/solr-15805-gate (detached), log gates/logs/vm2/15805-run.out. Starts after the SOLR-13943 run in the same detached process.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED by vm2 (coordinator).

## 2026-10-12T18:06Z DONE (vm2 coordinator)

DONE: gate GREEN at 6d78603d82 (GATE END 18:05:22Z). CoreContainerProviderTest 1 run, 0 failed. Receipt section at the top of receipts/SOLR-15805.md; job marked DONE.

Status: CLOSED (vm2 audit, 2026-10-16T00:55Z). Job gates/SOLR-15805.md is DONE on pr-prepare; no work remains under this claim. Earlier status lines are kept as history.
