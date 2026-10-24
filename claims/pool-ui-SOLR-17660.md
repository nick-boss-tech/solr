# Claim: pool-ui-SOLR-17660

- Claimant host: vm4 (AGENTS.md host id).
- Status: DONE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md` (row SOLR-17660 is not listed there; this claim is for the Figma category, reports `reports/new-ui-index-query-components-round-1.md` and `reports/figma-node-index-round-1.md`). One ticket per claim.
- Ticket type: component (reusable list with filter). Previous vm6 claim was a dialog (SOLR-17914), so the type rule is met.
- Slice: SOLR-17660, the full ticket acceptance list for the stateless component. The logging-section consumer is out of scope because the section has no data source yet (ticket wording).
- Branch: `solr-17660-basic-list-main` on the fork. Commits `1e8aca0ffc` (component, tests, preview; cherry-picked from Nick's earlier `b43dc58ce3` onto current `main` 498a09aea9) and `3ab8f52d01` (draft PR description `pr-draft/SOLR-17660.md`). The older fork branch `solr-17660-basic-list` (90ff62171a) is based on an old main and was not used.
- Gate at head `1e8aca0ffc`: focused `*SolrListTest*` 5 of 5 pass; full `:shared:jvmTest` 16 suites, 52 tests, 2 skipped, 0 failures; `:shared:spotlessCheck` passes. Logs: `/workspace/gates/logs/vm4/solr-17660/` (focused-test-3.log, full-test-spotless.log, junit-focused/, junit-full/).
- Visual check: preview rendered to PNG (`list-empty-filter.png`, `list-filled.png` in the same log dir). The scratch capture test was not committed.
- Environment note: this host has no GL driver. Skiko needs `libEGL.so.1`; the run used Mesa from conda-forge (`/workspace/tools/mesa-egl/lib`, via LD_LIBRARY_PATH). The pool's `material/ui-test-setup/run-ui-jvm-tests.sh` was not used.
- Capability tags used: implementation, ui, jvm-tests.
- Started: 2026-10-24T17:58:17Z (UTC), the claim write time. Lease: stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17660@498a09aea9.
- Links: Ticket SOLR-17660 (parent SOLR-17886). Category: ui. Guide: `/workspace/guides/new-ui-figma-ticket-guide.md` on vm4.
- Constraints: no JIRA or GitHub post, no PR opened. Commit as Nick Shanin with no trailers. No force push.

Heartbeat: 2026-10-24T17:58:04Z

DONE: 2026-10-24T17:58:04Z. Deliverables: branch `solr-17660-basic-list-main` at `3ab8f52d01`, `pr-draft/SOLR-17660.md` on that branch.
