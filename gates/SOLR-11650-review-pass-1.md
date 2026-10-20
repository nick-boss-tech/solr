# Gate job: SOLR-11650 review pass 1 (leaderUrl credential redaction)

Status: HANDED BACK (implementation: redaction fails open for a raw / or ? in a password, so the password fragment reaches the error and log messages; the redaction regex and its tests need a change outside the review repair scope)

- Branch: solr-11650-submit. Head: `e4f5e941cd8e88524196b03f2022261c2215c3ba` (ls-remote 2026-10-20T14:46Z).
- Prior gated head: `e4f5e941cd` (the receipt's gated head). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:core`, `:solr:solrj`. Focused classes: `org.apache.solr.handler.IndexFetcherLeaderUrlRedactionTest`, `org.apache.solr.common.util.URLUtilTest`. Seed: default.
- Diff scope against `14c7aac0d1`: 7 files (IndexFetcher, ReplicationHandler, URLUtil, URLUtilTest, IndexFetcherLeaderUrlRedactionTest, TestUserManagedReplicationWithAuth, changelog).

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope read
- [x] compile: `:solr:core:compileJava :solr:core:compileTestJava :solr:solrj:compileJava` rc=0
- [x] focused run: `IndexFetcherLeaderUrlRedactionTest` 2 of 2 (fresh XML)
- [x] focused run: `URLUtilTest` 18 of 18 (fresh XML, solrj)
- [ ] step 0 changelog recheck, step 1 tidy, Error Prone, module checks: not run this pass
- [x] finding: raw `/` or `?` in a password not redacted (blocker, HANDED BACK)
- [x] finding: `#` in the javadoc contract but not the regex (minor)

## Deliverable

- Pass 1 result and finding in `reports/newdev-critblock-review-round-1/solr-11650-submit.md`.
- Receipt section in `receipts/SOLR-11650.md`, top section, HANDED BACK.
