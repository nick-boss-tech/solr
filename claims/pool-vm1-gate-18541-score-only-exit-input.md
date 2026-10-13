# Claim: SOLR-18541 score-only timeAllowed exit input (vm1)

- Claimed: 2026-10-13T17:10Z by vm1. Job: gates/SOLR-18541-score-only-exit-input.md.
- Capability tags: gate, premise-run.
- Slice: the whole job. Premise run on the merge-base `d76fc14b9857e77b830d7a82514b28c523a239c2`, seed `C0FFEE42C0FFEE42`: a score-only sort with the timeAllowed exit forced inside the first sub-query, recording (1) whether `response_per_query` stays absent for the partial shard (absent, null, or an empty list), (2) whether the merge throws NullPointerException at the document loop, with the stack frame and line, and (3) numFound and numFoundExact of every per-query entry and of the merged result. No code change, no fork push, nothing added to any branch.
- Spec note recorded in the job file: the class the job names, `CombinedQueryComponentRealExitPremiseTest`, is not on the merge-base; it is the scratch probe from the combined component real-exit premise job, held uncommitted in the vm1 worktree ~/workspace/solr-worktrees/cqc-real-exit. The run uses a score-only variant of that fixture, `CombinedQueryComponentScoreOnlyExitPremiseTest`, uncommitted in the job worktree, source kept at ~/workspace/tools/probes/CombinedQueryComponentScoreOnlyExitPremiseTest.java.
- Host paths on vm1: worktree ~/workspace/solr-worktrees/cqc-score-only-exit; runner ~/workspace/tools/gate-strong-review-vm1-chain.sh (chained, serial on the test-queue flock); log ~/workspace/tools/g18541-score-only.log.
- Status: CLAIMED, run queued in the chained runner.
- Heartbeat: 2026-10-13T17:10Z claim pushed, runner being launched.
