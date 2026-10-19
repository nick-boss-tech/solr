# Skip record: SOLR-16548

- Ticket: SOLR-16548, "CaffeineCache seems to be not working in 9.0 and later". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2022-11-14, updated 2023-04-05, no fix version. Linked: SOLR-15859 (Open, issue split). Five comments: elyograg (2022-11-14, four comments: the repro, the queryResultCache finding, a techproducts check, and a plan to test 8.11.2) and janhoy (2023-04-05, pointing to `TestFiltersQueryCaching` as passing).
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-16548-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:11-14:11 UTC.

## Ticket reading

On 9.0.0 with the cloud example, the filterCache hits counter on the Admin UI does not increase for repeated queries with `fq`. The reporter's repro is a browser check on the cache page. A commenter (elyograg) found that the queryResultCache returns the full result for a repeated query, so the filterCache is not consulted. The maintainer's view is that filterCache is independent of queryResultCache and that the test coverage passes.

## Code reading

- `solr/core/src/test/org/apache/solr/search/TestFiltersQueryCaching.java` at the pin exists. Its assertions (for example lines 90, 94, 99, 104, 110, 116) check `lookupFilterCacheInserts`, which is the filterCache insert count, not the hits counter the ticket is about.
- The test's query sequence (lines 89-116) covers a filter query with a repeated term query. Whether that sequence exercises the queryResultCache path that elyograg described is not read in this pass.

What this shows at the pin: a test exists that checks filterCache inserts for `fq`. It does not check the hits counter that the reporter saw. So the audit's claim "TestFiltersQueryCaching passes" supports the insert behavior, not the hits behavior the ticket reports.

## History

- No commit on the pinned head names SOLR-16548 (evidence tool: no hits).
- SOLR-15859 (linked, Open) is not checked for a fix commit in this pass.

## Reproduction attempts

Not attempted. The reporter's repro needs a running cloud example and a browser reading of the cache page, and a test that reads the hits counter for repeated `fq` queries has not been run. This pass is review-only and runs no builds.

## Gap analysis

Two explanations remain open. (1) The queryResultCache answers repeated queries before the filterCache is consulted, so the filterCache hits counter does not move for a repeated query. That matches elyograg's observation and would make the report a stats-expectation issue. (2) The hits counter itself is wrong for some path, which the insert-based test would not catch. The ticket and the existing test do not separate these.

Missing piece for a verdict change: a run of a test (or a small script against the pinned build) that issues the repeated `fq` query and reads the filterCache hits and inserts directly, with `queryResultCache` both on and off. The result would show whether the hits counter moves when it should.

## Verdict justification

keep-open meets the rubric: the code path is read (the test and its assertions), the attempts are listed (none run), and the missing piece is named (the hits-counter run with the queryResultCache on and off). close-cannot-reproduce needs a reproduction on the pinned head, which has not been done, so it is not met. close-fixed and close-obsolete are not met: no fix or removed code is identified. pr-needed is not met: no line causes the hits counter to be wrong.

## Limits

This record does not show that the stats are correct, and does not show that they are wrong. It does not run the test. It does not check SOLR-15859. It does not post anything to JIRA or GitHub.
