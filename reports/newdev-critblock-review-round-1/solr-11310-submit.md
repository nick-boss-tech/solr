# Review: SOLR-11310, branch solr-11310-submit (unit 6)

Head reviewed: e38ddec5279c465f65a2fa2de22ee9c92e0f5e31 (ls-remote 2026-10-20T14:21Z). Base: 14c7aac0d1. Commits: d1b6180a8d (apply elevation after full rerank slice), 00ca6272e2 (test that fails without the fix; changelog), e38ddec527 (tidy formatting of the test). Diff: `ReRankCollector.java` (-2), `TestLTRReRankingPipeline.java` (+53), changelog (+7).

## Pass 1

Review (2026-10-20, vm5):

- Change. In `ReRankCollector` the boosted-docs pass no longer stops at the first non-boosted document. Before, the loop broke at the first doc not in `boostedDocs`, so only a leading run of elevated docs (by doc id order) was boosted. After, every reranked doc is checked, and elevated docs sort to the top of the rerank window, as the ticket asks.
- Owner call from the receipt, settled here at rung 3 (ticket intent): the receipt asked the owner to confirm removing the early exit so elevated docs always boost with `forceElevation=false`. The ticket asks for elevated documents to be kept at the top; the stated intent supports always boosting. Recorded choice: keep the removal. Not escalated.
- Cost. The loop is one hash lookup per reranked doc. Negligible for the window sizes the rerank uses. Not measured.
- Test. `testBoostedDocsSurviveLTRRescore` puts the elevated doc (id 1) after a higher-scored non-elevated doc (id 0) and checks that the elevated doc is kept at the top of a window of 2. The receipt records the A/B: the new test fails at base 14c7aac0d1 with `expected:<[1]>` and passes at the head (round 35, 2026-10-07, g11310r35-ab.log).
- Changelog. `type: fixed`, author Nick Shanin, link SOLR-11310. The title describes the user-visible change. Matches the changelog format.
- Repairs: none.

Gate (vm5, this pass):
- `:solr:core:compileJava :solr:modules:ltr:compileTestJava` (offline): rc=0 (`/workspace/gates/logs/vm5/g11310/compile2.log`). The first attempt (compile.log) failed on an incomplete scratch worktree, not on code.
- `:solr:modules:ltr:test --tests org.apache.solr.ltr.TestLTRReRankingPipeline` (offline): rc=0; JUnit XML tests=4, failures=0, errors=0, skipped=0 (`/workspace/gates/logs/vm5/g11310/test2.log`).

Not run this pass: tidy, Error Prone, module check for ltr (the receipt's hardening gate covered those on the head before; this pass reran the focused leg).

Regate: gates/SOLR-11310-review-pass-1.md.
