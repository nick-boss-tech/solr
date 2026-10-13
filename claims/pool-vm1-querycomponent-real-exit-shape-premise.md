# Claim: QueryComponent real-exit shape premise run (vm1)

- Claimed: 2026-10-13T17:10Z by vm1. Job: gates/queryComponent-real-exit-shape-premise.md.
- Capability tags: premise-run.
- Slice: the whole job. On unmodified main `d76fc14b9857e77b830d7a82514b28c523a239c2`, a throwaway probe class `QueryComponentRealExitShapePremiseTest` in :solr:core runs the plain shard-side process path (distrib=false, isShard=true, fsv=true, sort `intDefault asc`) with a timeAllowed limit that actually expires, seed `C0FFEE42C0FFEE42`: Q1 the shape when the exit lands during the main search, Q2 the shape when it lands during field sort value preparation, Q3 each captured shape driven unedited through `QueryComponent.mergeIds` with a complete second shard. Nothing is pushed; the probe lives only in the job worktree. This run settles the SOLR-18539 exactness rule width (canonical Option B against the any-skip rule).
- Host paths on vm1: worktree ~/workspace/solr-worktrees/qc-real-exit-shape; runner ~/workspace/tools/gate-strong-review-vm1-chain.sh (chained, serial on the test-queue flock); log ~/workspace/tools/gqc-real-exit-shape.log; probe source kept at ~/workspace/tools/probes/QueryComponentRealExitShapePremiseTest.java.
- Status: CLAIMED, run queued in the chained runner.
- Heartbeat: 2026-10-13T17:10Z claim pushed, runner being launched.
