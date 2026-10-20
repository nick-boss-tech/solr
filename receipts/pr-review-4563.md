# Receipt: PR review apache/solr #4563 (vm4)

- PR: https://github.com/apache/solr/pull/4563, open, not draft, head branch `SOLR-18295-replicationfactortest-flaky` on `janhoy/solr`, base `c851aae5c8`, 1 file, +17/-6 (GitHub files API).
- File: `solr/core/src/test/org/apache/solr/cloud/ReplicationFactorTest.java`. Removes `@AwaitsFix(bugUrl = ".../SOLR-18295")`. Wraps `runAndGetAchievedRf` in a retry loop (max 5) on `SolrServerException | IOException`.
- Base checks at `c8ab71430f`: `ReplicationFactorTest.java` still has the `@AwaitsFix` (line 52) and `runAndGetAchievedRf` (line 492). The `sendDocsWithRetry` helper used in the test comment exists (used by `ReplicationFactorTest` line 245 and others).
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4563.md`.
