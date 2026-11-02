# Implementation report: PR 5265 CI failure repair, round 1

- Assignment: `assignments/pr-5265-ci-failure-repair-round-1.md`.
- Target: PR 5265, SOLR-16398, `nick-boss-tech/solr:solr-16398-split-shard`.
- Base: `009f2c60513636b4aba2c93f4a4137aa39ccea64`.
- Implemented head: `16b0d50218bf82dc0d28158a63602c2d3da2ef3a`, pushed with a normal fast-forward update and read back from the fork. Parent is the stated base.
- PR head was independently read back as the same SHA. The PR remains open; no comment, review, PR body edit, or JIRA write was made.

## Findings addressed

1. `AdminUiCollectionScreensTest.testAnalysisScreenAnalyzesText` can hit the collection-selection handler while its model is still null. The handler now returns before reading `collection.type`. The existing Selenium test remains the regression check; its original CI seed is retained in the gate.
2. `TestApiFramework.testFramework` looked up `/collections/hello/shards`, a split-shard route now served by the JAX-RS API and not the legacy API bag. It now uses `/collections/hello` and verifies the legacy `/commands/modify` endpoint. The split-shard JAX-RS route remains covered by `SplitShardAPITest`.

## Files and review

- `solr/webapp/web/js/angular/app.js`
- `solr/core/src/test/org/apache/solr/handler/admin/TestApiFramework.java`
- Diffstat: 2 files changed, 6 insertions(+), 7 deletions(-).
- `git diff --check` passed before commit. The isolated worktree is clean at the pushed commit.

## Verification state

No Gradle task or test ran on Windows. Exact-head Linux proof is pending in `gates/pr-5265-ci-failure-repair-round-1.md`. That gate includes the original seeded Selenium test, the seeded API-framework test, the prior split-shard focused and neighboring suites, Error Prone compilation, module check, tidy, and final remote-head verification.

The registered Linux pool host records do not currently list browser/Selenium capability. The Admin UI leg is required; the gate remains open until a capable Linux host claims it and records browser/driver details. Do not count a skipped or unavailable browser leg as passing.
