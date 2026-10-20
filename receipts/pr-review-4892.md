# Receipt: PR review apache/solr #4892 (vm4)

- PR: https://github.com/apache/solr/pull/4892, open, not draft, head branch `SOLR-17864_part_dieciocho` on `epugh/solr`, 18 files, +46/-18 (GitHub files API), updated 2026-09-26, `mergeable_state` unknown at the snapshot.
- Production files: `SyncStrategy.java` (+3/-1), `ZkController.java` (+4/-2), `DefaultSolrCoreState.java` (+5/-2), `SimplePropagator.java` (+3/-2), plus `DeprecatedSystemPropertyMappings.properties` (+3, new=old pairs).
- Base checks at `c8ab71430f`: `Boolean.getBoolean("solrcloud.skip.autorecovery")` at `SyncStrategy.java` line 52, `ZkController.java` line 159 and `DefaultSolrCoreState.java` line 50; the flag is read at run time, not only in tests.
- Docs: `solr-properties.adoc` (+6), auth and securing pages (1 line each), `configuring-solr-xml.adoc` (1 line).
- Gate: none run.
- Outcome: READY-WITH-NOTES. Draft: `drafts/pr-review/4892.md`.
