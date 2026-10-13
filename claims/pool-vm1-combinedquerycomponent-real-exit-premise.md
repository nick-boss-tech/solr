# Claim: CombinedQueryComponent real-exit premise run (vm1)

- Claimed: 2026-10-13T10:31Z by vm1. Job: gates/combinedquerycomponent-real-exit-premise.md.
- Capability tags: premise-run.
- Slice: the whole job. Step 1 (real shard shapes from the component's own process path, with a timeAllowed limit that actually expires) and step 2 (the captured responses driven unedited through CombinedQueryComponent.mergeIds) run on vm1 as one probe class, CombinedQueryComponentRealExitPremiseTest, in :solr:core at unmodified main d76fc14b9857e77b830d7a82514b28c523a239c2, seed C0FFEE42C0FFEE42.
- Host paths on vm1: worktree ~/workspace/solr-worktrees/cqc-real-exit; runner ~/workspace/tools/gate-cqc-real-exit.sh; log ~/workspace/tools/gcqc-real-exit.log.
- Status: CLAIMED. The run starts detached under the vm1 test-queue flock; the finisher settles Q1, Q2 and Q3 from the log and the fresh JUnit XML, records the verdicts in the job file, appends the real-exit addendum to reports/combinedquerycomponent-premise.md, and marks this claim and the job DONE in that push.
- Heartbeat: 2026-10-13T10:31Z claim pushed, probe setup under way.
