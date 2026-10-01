# SOLR-15479 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

When the `rq` param re-ranks the top documents (e.g. `{!rerank
reRankQuery=$x}`), per-doc `score` values were correctly boosted but the
response's `maxScore` still reflected the ORIGINAL pre-rescore main-query
scores — the ticket's repro shows a doc scored 3.958 with `"maxScore":1.405`,
i.e. maxScore *below* an actual doc score. Root cause: `MaxScoreCollector`
(single-threaded) / `MaxScoreCM` (multi-threaded) record scores during leaf
collection, which happens *before* `ReRankCollector.topDocs()` rescores the
docs, and nothing ever corrected maxScore afterward.

Fix (`solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java`,
only file changed): in both `getDocListNC` and `getDocListAndSetNC`, and in
both their single-threaded and multi-threaded branches, the final maxScore is
now `max(pre-rescore collected max, max over the rescored docs' scores)`
whenever re-ranking was involved (detected via `topCollector instanceof
ReRankCollector` in the ST path, `cmd.getQuery() instanceof RankQuery` in
the MT path where the merged TopDocs carry per-thread rescored scores).
Taking the max of both preserves the "max over all hits" semantic: docs
beyond the rescore window keep original scores bounded by the collected max,
while boosted docs are covered by the rescored max. NaN still propagates when
scores weren't requested. A small private `maxScoreOf(ScoreDoc[])` helper was
added; no changes to `ReRankCollector` itself were needed.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

- Integration (the ticket's exact repro): techproducts-style query with
  `rq={!rerank ...}` boosting a doc above the main-query max and
  `fl=id,score` → assert `response.maxScore >=` every returned doc's score
  (fails before the patch, passes after).
- Exercise both single-threaded and multi-threaded (`multiThreaded=true`)
  search paths.
- Existing rerank / `ReRankCollector` / `SolrIndexSearcher` test suites for
  regressions — especially any test asserting exact maxScore values on
  reranked queries (those expectations may legitimately change).

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
