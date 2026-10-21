# Gate job: SOLR-18010 review pass 2 (unit 20, vm3)

Status: CLAIMED (claims/pool-newdev-critblock-review-round-1-solr-18010-submit.md, 2026-10-21T06:28:50Z)

- Branch: `solr-18010-submit`. Head: `571307400b6917ce458db989f29febb5cff8a600`. Base: `14c7aac0d1`.
- Intended host: vm3. Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.handler.admin.SecurityConfHandlerTest`, `org.apache.solr.security.BasicAuthStandaloneTest`.
- Diff scope against `14c7aac0d1`: 4 files, 390 insertions, 4 deletions: changelog SOLR-18010-security-json-concurrent-edits.yml, SecurityConfHandlerLocal.java, SecurityConfHandlerTest.java (+294), BasicAuthStandaloneTest.java (+25).
- Why: the pass 2 regate on the same head. Pass 2 reviews the test expectations against the ticket.
- Runner: `/workspace/tools/gate-solr-18010-p1.sh`. Worktree `/workspace/worktrees/solr-18010-p1`. Log: `gates/logs/vm3/solr-18010-review-pass-2/`.

Links: Ticket or parent: SOLR-18010. Category: 7, Permissions and security declarations. Siblings: vm2 review claims for this ticket (DONE). Supersedes: none.

## What to run

- Step 0: changelog YAML parse. Step 1: tidy. Step 2: Error Prone compile of `:solr:core` with `--rerun`.
- Step 4: focused classes above, counted from fresh XML.
- Step 5: `:solr:core:check -x test`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded, not fixed | vm3, rung 4 |
