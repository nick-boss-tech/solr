# Gate job: SOLR-17297 review pass 2 (unit 18, vm3)

Status: DONE (GATE GREEN at head `c0ab38fc0a8b57953f8b8df03f54085fb37e138d`, 2026-10-21T04:37:00Z; receipts/SOLR-17297.md)

- Branch: `solr-17297-submit`. Head: `c0ab38fc0a8b57953f8b8df03f54085fb37e138d`. Base: `14c7aac0d1`.
- Intended host: vm3. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.core.TestCoreContainer` (the receipt's count: 26 tests, 3 skipped).
- Diff scope against `14c7aac0d1`: 3 files, 165 insertions, 1 deletion: changelog/unreleased/SOLR-17297-modules-before-shared-lib.yml, solr/core NodeConfig.java (two lines reordered), solr/core TestCoreContainer.java (+157).
- Why: the pass 2 regate. The receipt's gate is from 2026-10-04 and predates the last commit (c0ab38fc0a, forbiddenApis fix in the test fixture).
- Runner: `/workspace/tools/gate-solr-17297-p2.sh`. Worktree `/workspace/worktrees/solr-17297-p1`. Log: `gates/logs/vm3/solr-17297-review-pass-2/`.

Links: Ticket or parent: SOLR-17297. Category: 10, Core admin and cores (NodeConfig and the core container). Siblings: none known. Supersedes: none.

## What to run

- Step 0: changelog YAML parse. Step 1: tidy. Step 2: Error Prone compile with `--rerun`.
- Step 4: focused run of `TestCoreContainer`, counted from fresh XML.
- Step 5: `:solr:core:check -x test`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded, not fixed | vm3, rung 4 |
| Inverse-direction SPI finding (receipt, round 35) | default: recorded as an owner item, not repaired | a behaviour change in module and shared-lib classloading; not in repair scope | owner |
