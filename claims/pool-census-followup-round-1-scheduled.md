# Claim: census follow-up, scheduled round 1 (six slices, four tickets each)

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Trigger: the two-hourly scheduled check. Live tip at the check: `029377d2361`; recorded tip `38ba628d1f7`. The new commits are vm2's gate and receipt records, the SOLR-3498 push record, the openings census (`reports/openings-census-round-1.md`) and its follow-up assignments `assignments/pool-census-followup-*-round-1.md`.
- Scope this round (24 tickets, six subagents, four each):
  - A, search components wording (`pool-census-followup-search-components-wording-round-1`): SOLR-15319, SOLR-14381, SOLR-5394, SOLR-7390.
  - B, search components wording: SOLR-7520, SOLR-8009, SOLR-8240, SOLR-8939.
  - C, query parsing (`pool-census-followup-query-parsing-round-1`): SOLR-874, SOLR-12212, SOLR-15615 (wording); SOLR-13838 (draft owed).
  - D, core admin (`pool-census-followup-core-admin-round-1`): SOLR-5262, SOLR-8554, SOLR-8628, SOLR-14098 (drafts owed).
  - E, SolrJ (`pool-census-followup-solrj-round-1`): SOLR-3498 (draft owed); SOLR-2018, SOLR-3722, SOLR-4422 (confirmations).
  - F, SolrCloud (`pool-census-followup-solrcloud-round-1`): SOLR-16013 (refresh to `37b01337eb`); SOLR-3865, SOLR-4754, SOLR-10234 (drafts owed).
- Left for later rounds: the rest of the search-components and query-parsing lists; the search-components drafts owed; the other SolrCloud confirmations and the SOLR-17292 reconcile; the core-admin refreshes (SOLR-12916, SOLR-8576); the other SolrJ confirmations; spellcheck; suggester; streaming-schema-configsets; build-docs-cli-metrics; and the edismax leftover.
- Staffing: six subagents, one slice each. Each may write only the draft files of its own tickets under `pr-drafts/`, and gate job files under `gates/` for branch-side items. None pushes, opens a PR, moves a branch, or edits a receipt or a claim.
- Limits: no PR opened, closed, flipped or commented on; no `solr-*-submit` branch touched; no live PR body edited; no gate job run; no build, test or Gradle on this host; nothing posted.
- Decision rule: scope questions under the decision default and the three-rung ladder; each decision recorded in the round report.
- Deliverable: `reports/census-followup-scheduled-round-1.md`, one line per ticket, with each fix, each draft written and each gate job posted.
- Heartbeat: 2026-10-14T04:38Z (claim taken).
- Status: IN PROGRESS.
