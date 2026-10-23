# Pool assignment: SOLR-18506 TestThinCache cleanup after assertion failure
Status: CLAIMED by `claims/pool-vm5-solr-18506-test-cleanup.md`. Was OPEN (one-off vm5 exception approved by Nick, 2026-10-23).
- Capability tags: implementation, gate.
- Category: Test quality, with Search and query components as the production category.
- Intended host: vm5 (one-off named-host exception approved by Nick on 2026-10-23). Claim under `claims/` before starting.
- Posted by: phone-agent-2, 2026-10-23, at the direction of Nick Shanin.
- Starting point: PR #5029, branch `solr-18506-submit`, head `77c019e1ff0e922e3c2350379dece841651b1d44` as checked on 2026-10-23.

## Context
The read-only teardown investigation in `reports/teardown-objecttracker-leak.md` found that `TestThinCache.testSimple` closes its parent `SolrMetricsContext` after an eviction assertion. If that assertion throws, cleanup is skipped and ObjectTracker reports the parent and two cache contexts. This is a test-owned cleanup gap; the CI association does not show that PR #5029 caused it.

The goal is only to make this test's owned resources close after failure without changing cache behavior or expected values. Keep the existing PR branch untouched. The related history is in `receipts/SOLR-18506.md`.

## Items
1. **Make TestThinCache cleanup unconditional and validate the failure path.** Start from `solr-18506-submit` at the pinned head above. Change only `solr/core/src/test/org/apache/solr/search/TestThinCache.java`. Preserve the existing assertions, cache size, warming logic and expected results. Use a temporary, reverted failure injection after the parent context and caches are registered; run the focused test and record whether the intentional assertion failure leaves any ObjectTracker leak. Restore the injection, then run the final focused test class at the candidate head. Output: `reports/solr-18506-test-cleanup-vm5.md`, `gates/SOLR-18506-test-cleanup-vm5.md`, and the updated `receipts/SOLR-18506.md`.

## Decisions
| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scope | decided: test-only cleanup in `TestThinCache.testSimple` | Do not edit production code, change cache behavior, or alter assertions. | Nick Shanin |
| Candidate branch | decided: `solr-18506-test-cleanup` based on the pinned `solr-18506-submit` head | Push this separate candidate branch only after the focused gate is green. Never push to or rewrite `solr-18506-submit`. | Nick Shanin |
| Validation | decided: tidy, Error Prone test compilation, one controlled failure-path proof, then `:solr:core:cleanTest` and the focused `TestThinCache` class at seed `1668E8BD438E6830`; finish with `:solr:core:check -x test` | Count fresh JUnit XML and retain it before another test run. Do not run the full `:solr:core:test` suite. If the forced-failure run cannot be made safely or produces unexplained failures, record the result and stop without pushing the candidate. | Nick Shanin |
| PR actions | decided: none | No change to PR #5029, no new PR, no comments, and no Jira post. | Nick Shanin |

## Deliverable
`reports/solr-18506-test-cleanup-vm5.md`: exact candidate SHA, patch summary, controlled failure result and ObjectTracker output, final focused-test XML counts, tidy/compiler/module-check results, and any limitations. Mark this assignment and its claim DONE in the same coordination push as the report, gate and receipt update.

## Limits
- Do not change production code or any file outside `TestThinCache.java` on the candidate branch.
- Do not modify or push to the existing PR branch `solr-18506-submit`.
- No full `:solr:core:test` or `:solr:solrj:test` suite.
- Do not rerun a test run that executed and failed. Record the failure and stop for review.
- No PR opening or state change, no comment, no Jira or email, no force push. Commit as Nick Shanin with no trailers. No em dashes or en dashes.
