# Claim: SolrCloud readiness round 1, application of the verdicts (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft, and text-only branch edits. The two code edits (SOLR-14919, SOLR-9155) are compile-sensitive. Each is committed on its fork branch and handed to a Linux gate job before the PR is treated as ready.
- Assignment: `assignments/pool-solrcloud-readiness-round-1-application.md`. Nick's direction on 2026-10-12 adds: implement the changes recommended in `reports/solrcloud-readiness-round-1.md`, so that every SolrCloud item is ready for PR.
- Scope: Parts 1 to 3 of the assignment; the branch edits Nick directed (changelog titles for SOLR-15863, SOLR-11288 and SOLR-12651; the processor-only route for SOLR-14919; the commit body on SOLR-17680); draft patches; gate jobs under `gates/` for each branch that changed and for each record still owed (SOLR-13186, SOLR-15106, SOLR-17733).
- Force-push: fork submit branches only, through `research\push-fork-lease.ps1`, leased to the SHA read. Allowed by Nick on 2026-10-12 (AGENTS.md).
- Not in scope: opening PRs, posting comments, editing live PR descriptions, builds, Gradle, tests. SOLR-9155 and SOLR-13369 get no edits; their settling information is named. SOLR-15035 needs a JIRA summary check that this host cannot make, so it stays an owner item.
- Deliverable: `reports/solrcloud-readiness-round-1-application.md`.
- Heartbeat: 2026-10-12T23:27Z (claim taken).
- Status: CLAIMED.
