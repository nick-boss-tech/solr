# Claim: SOLR-6430 drop the changelog fragment, docs gate at the new head (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T09:05Z by vm2. Job: gates/SOLR-6430-fragment-drop.md (not the vm3 premise claim, which is DONE).
- Head at claim: solr-6430-submit c0c6e5dd21c4eee60257db6b85b2fec7f01489b7 (live tip, equal to the packaged head).
- Change: delete changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml, as its job file names. Commit on a detached clone at /workspace/gates/solr-6430-docs, not pushed until the gate is green.
- Clone rather than linked worktree: Antora rejects a linked worktree's .git pointer (see receipts/SOLR-6430.md), so the docs build runs in a shared clone.
- Capability tags: implementation (one deletion), gate (docs shape).
- Runner: gates/g6430-gate.sh, log gates/logs/vm2/6430-*.log.
- Status: DONE (see the line above).

- 2026-10-13T09:31Z DONE by vm2 (gate backlog coordinator): gate GREEN at 78cddfb520d63069353dc95d42853cec779057ce (docs shape, no Java tests). Deliverables: gates/SOLR-6430-fragment-drop.md, receipts/SOLR-6430.md (new section), pr-drafts/build-docs/SOLR-6430.md (Changelog line deleted, lint sentence re-pointed). Fork push c0c6e5dd21c4 to 78cddfb520d6 follows this commit.
