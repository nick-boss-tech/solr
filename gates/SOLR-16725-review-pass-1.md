# Gate job: SOLR-16725 review pass 1 (unit 14, vm3)

Status: CLAIMED (claims/pool-newdev-critblock-review-round-1-solr-16725-submit.md, 2026-10-21T01:59:01Z)

- Branch: `solr-16725-submit`. Head: `550af28313` (ls-remote at claim time). If the tip moved, stop and record it here.
- Prior gated head: `be1838ef8c` (receipt). Base: `14c7aac0d1`.
- Intended host: vm3. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.admin.api.LocalFSCloudIncrementalBackupTest` (the test the receipt names). Seed: `16725C0FFEE16725`.
- Diff scope against `14c7aac0d1`: 3 files, 92 insertions, 1 deletion: changelog/unreleased/SOLR-16725-clusterstatus-restored-count-types.yml, solr/core ClusterStatus.java, LocalFSCloudIncrementalBackupTest.java.
- Why: the review round's pass 1 regate, the sign-off for unit 14.
- Runner: `/workspace/tools/gate-solr-16725-p1.sh`. Worktree `/workspace/worktrees/solr-16725-p1`. Log: `gates/logs/vm3/solr-16725-review-pass-1/`.

Links: Ticket or parent: SOLR-16725. Category: 10, Core admin and cores (ClusterStatus is in the core admin handler). Siblings: none known. Supersedes: none.

## What to run

- Step 0: changelog YAML parse of `changelog/unreleased`.
- Step 1: tidy. If tidy changes a file, apply it as a gate repair and retry once from step 0.
- Step 2: Error Prone compile of `:solr:core` main and test with a forced re-run (`--rerun`), so the Error Prone compile really executes.
- Step 4: focused run of `LocalFSCloudIncrementalBackupTest` with seed `16725C0FFEE16725`, counted from fresh JUnit XML.
- Step 5: `:solr:core:check -x test`.

Failures follow WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded, not fixed | vm3, rung 4 |
| Proof leg | default: not repeated in pass 1 | the receipt's base-versus-head failure (testCustomProperties, Long vs String) is cited; pass 2 may rerun it | vm3, rung 1 |
