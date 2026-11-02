# Assignment: PR 5265 CI failure repair, round 1

Status: CLAIMED (Windows source patch; Linux gate pending)

- Owner direction: Nick asked in chat on 2026-11-02 to patch the branch for the reported CI failures and hand the result to a gate host.
- Target: PR 5265, SOLR-16398, `nick-boss-tech/solr:solr-16398-split-shard`.
- Starting head: `009f2c60513636b4aba2c93f4a4137aa39ccea64`; live PR and fork ref were re-read at that head before work.
- Host split: Windows may make the scoped source and test edits; no Gradle, tidy, or local test queue. A Linux host with JDK 21, the pinned Gradle distribution, and browser/Selenium support must run the exact-head gate.
- Scope:
  1. Guard the Admin UI collection selector against its initial null model in `solr/webapp/web/js/angular/app.js`; keep the existing analysis screen console assertion as regression coverage.
  2. Update the generic legacy `TestApiFramework` lookup to use an endpoint still served by the legacy API bag. The split-shard JAX-RS route remains covered by `SplitShardAPITest`; do not change its public path to satisfy the obsolete legacy lookup.
- Exact outbound files: `solr/webapp/web/js/angular/app.js` and `solr/core/src/test/org/apache/solr/handler/admin/TestApiFramework.java`.
- Deliverables: scoped commit and normal fast-forward push, an implementation report, and an immutable gate job pinned to the new full SHA. No PR comment, review, body edit, or JIRA write.
- Gate focus: reproduce the Admin UI analysis test at seed `83E3932EA6612D4A`; run `TestApiFramework.testFramework` at seed `4E30896B42ECC98D`; rerun the split-shard focused and neighboring suites from the previous gate; tidy, the required compile and module check, and `git diff --check`. Record host, commands, UTC times, exit codes, counts, XML/log paths, and final SHA.
