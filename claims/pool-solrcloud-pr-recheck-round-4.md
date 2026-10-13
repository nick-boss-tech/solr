# Claim: SolrCloud PR drafts, re-check after new gate records, round 4 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Assignment: none posted. Trigger: new records on pr-prepare at `8192d7a2db1` (SOLR-15386 dated run, SOLR-12651 base run) and the SOLR-13369 dated run at `985df658111`. Standing direction from Nick, 2026-10-13: patch and audit the SolrCloud drafts.
- Staffing: two read-only-plus-draft subagents, two drafts each. Each checks its draft's Proof against the receipt or gate file that now carries the run, and edits only its own draft.
- Scope: `pr-drafts/solrcloud/SOLR-15386.md`, `SOLR-12651.md`, `SOLR-13369.md`, `SOLR-15106.md`. The SOLR-18540 draft (another lane, build-docs) is outside this round.
- Limits: no PR, no comment, no JIRA write, no email. No branch change, no gate, no build, no test, no Gradle on this host. Receipts and gate files are not edited here. Commit as Nick Shanin, no trailers, no em dashes.
- Heartbeat: 2026-10-13T05:05Z (claim taken).
- Status: ACTIVE.
