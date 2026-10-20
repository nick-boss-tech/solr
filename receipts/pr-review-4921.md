# Receipt: PR review apache/solr #4921 (vm4)

- PR: https://github.com/apache/solr/pull/4921, open, not draft, head branch `streaming-search-route/SOLR-18466` on `tsornin14/solr`, 4 files, +255/-3 (GitHub files API), updated 2026-09-22, `mergeable_state` unknown at the snapshot.
- Files: changelog entry (added, 8, `type: added`), `CloudSolrStream.java` (+22/-2, new `getSlices` overload with `ShardParams._ROUTE_`), `TupleStream.java` (+2/-1, call moved after params), `StreamExpressionTest.java` (+223, `testCloudSolrStreamWithRouteParam`).
- Base checks at `c8ab71430f`: not yet done for this PR (the `solrj-streaming` module is at base; the new overload is additive).
- Gate: none run.
- Outcome: READY-WITH-NOTES. Draft: `drafts/pr-review/4921.md`.
