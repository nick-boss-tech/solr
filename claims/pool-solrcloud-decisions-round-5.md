# Claim: SolrCloud decisions from Nick, round 5 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Direction: Nick, 2026-10-13, answers to the audit decisions:
  1. SOLR-11479: do the history rewrite. Done in this round: `solr-11479-submit` moved from `360dca60fcfa` to `6f739cfbbd3` with a lease push. The author and committer are now `nick.boss.us@gmail.com`. The tree is identical. A gate job records the carry-over.
  2. SOLR-16437: the nightly-group run goes to vm2. The gate job is marked assigned.
  3. SOLR-9155: agents write the Limits wording. Keep it simple.
  4. SOLR-17680: find more information. vm2 runs tests; this host reads code.
  5. SOLR-5813: improve the vague sentence. Find the information first.
  6. JIRA: the helper reads the dev-folder token file, `dev/jira_token.env`. Done; a read test passed.
- Staffing: two subagents. One reads the SOLR-17680 v2 path and its tests. One reads what `collection=` returns for SOLR-5813. Both are read-only; the lead makes the draft edits.
- Limits: no PR, no comment, no JIRA write, no email. No build, test or Gradle on this host. Gate runs go to vm2 as job files. Commit as Nick Shanin, no trailers, no em dashes.
- Heartbeat: 2026-10-13T05:08Z (claim taken).
- Status: ACTIVE.
