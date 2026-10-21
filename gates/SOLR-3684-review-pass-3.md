# Gate job: SOLR-3684 review pass 3 (unit 22, vm3)

Status: CLAIMED (claims/pool-newdev-critblock-review-round-1-solr-3684-submit.md, 2026-10-21T08:16:47Z)

- Branch: `solr-3684-submit`. Head: `663b8ce754b69fd81dcd1b5c1abe7e94ca8e6faa`. Base: `14c7aac0d1`.
- Intended host: vm3. Capability tags: gate.
- Module: `:solr:test-framework`. Focused class: `org.apache.solr.embedded.TestJettySolrRunner`. Seed: default.
- Diff scope against `14c7aac0d1`: 4 files, 54 insertions, 1 deletion: changelog/unreleased/SOLR-3684.yml, test-framework JettyConfig.java, JettySolrRunner.java, TestJettySolrRunner.java.
- Why: the pass 3 regate on the same head. Sign-off for unit 22.
- Runner: `/workspace/tools/gate-solr-3684-p1.sh`. Worktree `/workspace/worktrees/solr-3684-p1`. Log: `gates/logs/vm3/solr-3684-review-pass-3/`.

Links: Ticket or parent: SOLR-3684. Category: 10, Core admin and cores (JettySolrRunner is the test-framework runner used by core tests). Siblings: none known. Supersedes: none.

## What to run

- Step 0: changelog YAML parse. Step 1: tidy. Step 2: Error Prone compile of `:solr:test-framework` main and test, with `--rerun`.
- Step 4: focused `TestJettySolrRunner`, counted from fresh XML.
- Step 5: `:solr:test-framework:check -x test`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded, not fixed | vm3, rung 4 |
