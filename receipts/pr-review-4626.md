# Receipt: PR review apache/solr #4626 (vm4)

- PR: https://github.com/apache/solr/pull/4626, open, not draft, head branch `SOLR-1743-changed-default-request-timeout` on `VishnuPriyaChandraSekar/solr`, 10 files, +158/-37 (GitHub files API), updated 2026-09-04, `mergeable_state` unknown at the snapshot.
- Files: changelog entry (added, 9), `HttpJettySolrClient.java` (+9/-1), `HttpJettySolrClientTest.java` (+24/-4), `HttpJdkSolrClient.java` (+4/-1), `HttpSolrClient.java` (+9/-3), `SolrHttpConstants.java` (+1), `ConcurrentUpdateJdkSolrClientTest.java` (+4/-1), `HttpJdkSolrClientTest.java` (+2/-13), `LB2SolrClientTest.java` (+93/-13), `ServletFixtures.java` (+3/-1).
- Key behavior: `HttpSolrClient.getRequestTimeoutMillis()` returns 0 (infinite) when no request timeout is configured, instead of the idle timeout.
- Comment check: the `HttpJettySolrClient` comment has a missing space ("timeout.The").
- Gate: none run.
- Outcome: NEEDS-OWNER. Draft: `drafts/pr-review/4626.md`.
