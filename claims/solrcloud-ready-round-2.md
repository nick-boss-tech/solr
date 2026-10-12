# Claim: SolrCloud items ready for PR, round 2 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft, and text-only branch edits. The two code edits (SOLR-14919, SOLR-9155) are compile-sensitive and are handed to a Linux gate job before any PR is treated as ready.
- Assignment: none posted. Scope comes from Nick's direction on 2026-10-12: implement the changes recommended in `reports/solrcloud-readiness-round-1.md`, and get every SolrCloud item ready for PR.
- Scope: changelog title edits (SOLR-15863, SOLR-11288, SOLR-12651, SOLR-14919); one commit message body edit (SOLR-17680); a processor-only route with the recovery marker removed (SOLR-14919); an interrupt declared on `getLeader` with its test updated (SOLR-9155); draft edits for SOLR-11479, SOLR-15674, SOLR-14919, SOLR-9155, SOLR-13369, SOLR-12651 and SOLR-17680; gate jobs posted under `gates/` for each branch that changed and for each record still owed (SOLR-13186, SOLR-15106, SOLR-17733).
- Force-push: fork submit branches only, through `research\push-fork-lease.ps1`, leased to the SHA read. Allowed by Nick, 2026-10-12 (AGENTS.md).
- Not in scope: opening PRs, posting comments, editing live PR descriptions, builds, Gradle, tests. SOLR-15035 needs a JIRA summary check that this host cannot make; it stays an owner item.
- Deliverable: `reports/solrcloud-ready-round-2.md`.
- Heartbeat: 2026-10-12T23:27Z (claim taken).
- Status: CLAIMED.
