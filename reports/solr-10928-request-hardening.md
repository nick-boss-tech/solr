# SOLR-10928 request hardening

Status: DONE

The branch is hardened and all requested validation is green at **`f2001e640a464e8e861ee3c202821b6dcc4bd497`** on [`solr-10928-submit`](https://github.com/nick-boss-tech/solr/tree/solr-10928-submit). The starting head was `1ff57c1b21c22031513f733b641496b77057ea55`, based on `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Both new commits were fast-forward pushed, authored and committed as Nick Shanin without trailers. No production behavior changed in this hardening round.

## What changed

Seven new methods in `solr/core/src/test/org/apache/solr/handler/component/QueryElevationComponentTest.java` run complete embedded `/elevate` requests rather than calling the helper directly. They assert exact total hits, document ordering, and every returned elevation marker. Cases cover preserving the real search query, request override precedence, inline and dereferenced Lucene/edismax local parameters, fallback when the override is absent, explicitly empty and unknown keys, analyzer normalization and configured exclusions, enable/disable and matching-only/exclusive controls, explicit ID precedence, filters, and absence of request-to-request leakage.

`DistributedQueryElevationComponentTest.java` now checks plain and dereferenced local-param requests across its three shards. Both assert exactly IDs 1 and 2 elevated ahead of the actual query's matching ID 7, despite descending ID sort. The existing helper tests remain in place.

The reference guide now explains request-only placement, local `v` fallback, precedence, lookup-only semantics, analyzer use, explicit empty values, and the higher priority of `elevateIds`/`excludeIds`.

Implementation commit: `d5621a29281f28659db7b9e9ca57cc46c2651f53`. The first validation attempt caught an incorrect fluent call to a void-returning test-parameter merge method before any tests ran. Compiler-directed repair `f2001e640a464e8e861ee3c202821b6dcc4bd497` separates that call; no expected values changed. The single repaired-tree retry ran all steps from the start.

## Validation

| Check | Result |
| --- | --- |
| Changelog YAML parse, tidy, diff check | PASS; final tracked tree unchanged |
| Core production and test compilation with Error Prone enabled | PASS |
| Override disabled temporarily | Exactly the two full-request override tests failed at the expected result-count assertions; zero errors/skips |
| Parser-local fallback disabled temporarily | Exactly the fallback request test failed at the expected result-count assertion; zero errors/skips |
| Four-class regression set, seed `10928E1E10928E1E` | 39/39 passed, zero failures/errors/skips |
| Same set, seed `10928E2E10928E2E` | 39/39 passed, zero failures/errors/skips |
| `:solr:core:check -x test` | PASS |
| Root `check -x test`, including reference-guide checks | PASS |

Each seed ran `QueryElevationComponentParamTest` (4 tests), `QueryElevationComponentTest` (23), `DistributedQueryElevationComponentTest` (1), and `TestReRankQParserPlugin` (11). These are 39 distinct tests run twice, not 78 distinct tests. Mutation failures are proof evidence, not head failures. Every proof was restored before passing-head runs, and fresh XML was archived before the next build step. All builds were serialized under the host lock with disk/inode guards. Two independent read-only reviews found no concrete production or test defects; compilation, not review, caught the fluent-call mistake.

Evidence root: `/workspace/logs/phone-agent-2-SOLR-10928-request-hardening-20261023/`. The complete passing retry is in `gate-attempt-2/run/`, including state files, both proof patches/verdicts, per-step logs, and archived XML for both seeds. The failed compile attempt remains in `gate-attempt-1/`. Coordination details are in `gates/SOLR-10928-phase-request-hardening.md` and `receipts/SOLR-10928.md`.

## Remaining limits

This is focused regression validation, not a run of the entire core or repository test suite; the root check explicitly excludes tests. The three-shard suite is not a SolrCloud replica/failover or reload/restart test. The new override has not been exhaustively combined with every parser, subset rule, marker option, grouping/cursor mode, rerank option, or combined-query path. The existing rerank suite passed but has no new override-specific rerank scenario. No blocking defect remains known within the tested scope.

No PR was opened, no comment or Jira action was posted, and no PR title/body or other public discussion text was edited. Previous terminal gate and implementation claims were preserved.
