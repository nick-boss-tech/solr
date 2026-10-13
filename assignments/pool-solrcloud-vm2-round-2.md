# Pool assignment: SolrCloud round 2 builds and tests (vm2)

Capability tags: `gate`, `premise-run`, `settling-run`, `sweep`, and `implementation` for the investigation in job 5 only. Intended host: vm2 (Linux). Staffing: one gate per host at a time, so these run in the order listed unless vm2's coordinator says otherwise. Claim: `claims/pool-vm2-solrcloud-round-2.md`, taken by vm2.

Why this exists: the Windows review host does not run builds. Nick directed on 2026-10-12 that anything needing a build or a test goes here. Every job below has a gate or record file under `gates/`, or is described in full here.

## Jobs

1. **Gate SOLR-9155 at dbd88c84ff2.** Gate file: `gates/SOLR-9155-declared-route-gate.md`. The change moves getLeader to a declared InterruptedException. The gate must confirm the two callers still compile.
2. **Re-gates at new heads** (one gate file each):
   - SOLR-14919 at c1fa37e33c1: `gates/SOLR-14919-processor-only-gate.md`
   - SOLR-17680 at ce59bbe112d (message-only rewrite; tree identical): `gates/SOLR-17680-message-regate.md`
   - SOLR-15863 at 78dff1d52f6 (changelog only): `gates/SOLR-15863-title-regate.md`
   - SOLR-11288 at cbf0c678f277 (changelog only): `gates/SOLR-11288-title-regate.md`
   - SOLR-12651 at 404f13b501c (changelog only): `gates/SOLR-12651-title-regate.md`
3. **Dated records** (no head change):
   - SOLR-13186 focused run at b436d90d2a88: `gates/SOLR-13186-dated-run.md`
   - SOLR-17733 focused run at 636196b7954a: `gates/SOLR-17733-dated-run.md`
   - SOLR-15106 premise run at the merge-base, then a dated run at 40b7e5d0efa7: `gates/SOLR-15106-premise-base.md`
4. **Sweep for SOLR-13369.** Question: does the ticket's reproducing seed still put one route prefix in two shards on current main, with main's test rule? Seed: `A6B6F0104FE6018F`, multiplier 3, as the ticket records it. Run `TriLevelCompositeIdRoutingTest` on upstream/main in a scratch worktree, and make the test run even if main marks it disabled, but do not commit that change anywhere. Then run the same seed on the branch test at dfa0db5bdf96. Record both results with the failing assertion text. No commits, no pushes.
5. **Investigation for SOLR-14919** (the processor route question). The ticket reports that replica recovery fails when the processor returns a status other than 200. The recovery commit carries no marker. The question: does the processor-only head (c1fa37e33c1) let recovery succeed when IgnoreCommitOptimizeUpdateProcessorFactory returns 403? Steps:
   - Find an existing SolrCloud recovery test under solr/core/src/test (confirm by grep). Configure the processor to return 403 in that scenario.
   - Run it at c1fa37e33c1 and at 84e7bcaeb57 (the head that carries the recovery marker). Record whether recovery completes in each.
   - Run one existing distributed-commit test at both heads, to see whether the marker's change to commit fan-out breaks it.
   - Use scratch worktrees. No commits, no pushes. Report only.

## Rules

- No edits to any submit branch in this assignment, except job 1 and the re-gates, which are gate-only runs.
- Counts come from fresh JUnit XML. Record each seed.
- Failures where tests executed are investigated and recorded in the gate file before any relaunch. Do not relaunch on sight.
- No public posts of any kind.
- No em dashes in anything written.

## Deliverable

- Gate and record jobs: each gate file's checklist, marked DONE, with a receipt refresh where the job says so.
- Jobs 4 and 5: `reports/solrcloud-vm2-round-2.md`. For each job, state GREEN, RED or INCONCLUSIVE, the head, the counts, the seed, the log path on vm2, and any failure text verbatim.
- Job 5's answer decides whether the SOLR-14919 PR can be described as fixing the ticket. Lead the report with that answer.
