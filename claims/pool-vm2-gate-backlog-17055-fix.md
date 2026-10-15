# Claim: SOLR-17055 sort-path fix and gate (vm2, gate backlog coordinator)

- Claimed: 2026-10-14T10:34:37Z by vm2. Job: gates/SOLR-17055-sort-path.md, steps 2 onward (the fix). The premise run is recorded in the job file.
- Head at claim: c2396f425fa2a67a40fea7afee341840c3896358 (solr-17055-submit, verified by ls-remote by the job).
- Capability tags: gate.
- Runner: gates/g17055-gate.sh, worktree /workspace/gates/solr-17055-fix (detached at the head). No fork push until the gate is green.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED, fix in progress.

- Result 2026-10-14T11:55:19Z: gate ended with the head green (fix commit 2fe0986158, DistributedKnnTopKTest 1 of 1, compile, module check all rc 0). Fail-before at base 97d973814336 returned rc 1 at the score-only assertion, not the sort assertion, so it does not establish the proof. Not GREEN by the coordinator rule; no fork push. Left for the main agent. Claim ended, not DONE.
- Status: ENDED, not green, left for the main agent.

- 2026-10-15T00:03Z coordinator: proof-c run (log queue-g17055-proof-c-gate-20261014T233003Z.out) at head 2fe0986158: head legs rc 0; fail-before rc 1 at the sort assertion `.response.size():9!=3`. Not GREEN under the every-rc-0 rule; no fork push; left for the main agent. Status unchanged: ENDED, not green.
