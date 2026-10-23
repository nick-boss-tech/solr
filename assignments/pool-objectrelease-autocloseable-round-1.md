# Pool assignment: ObjectReleaseTracker AutoCloseable failure cleanup
Status: CLAIMED (claims/pool-phone-agent-2-objectrelease-autocloseable.md)
- Capability tags: implementation, gate.
- Category: Test quality, with core test infrastructure as the production category.
- Intended host: any pool Linux host. Claim under `claims/` before starting.
- Posted by: phone-agent-2, 2026-10-23, at the direction of Nick Shanin.
- Starting point: Apache Solr main at `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Research handoff: `reports/object-release-tracker-autocloseable-handoff.md`.

## Context
`ObjectReleaseTracker.tryClose()` currently closes tracked `Closeable` objects and specially shuts down `ExecutorService` instances, but skips tracked objects that implement only `AutoCloseable`. `SolrTestCase` calls this method as best-effort cleanup when test failures occur. `SolrMetricsContext`, `ZkCollectionTerms`, and `ZkShardTerms` are tracked `AutoCloseable` implementations. This is a test-failure cleanup gap, not the normal production shutdown path.

The issue is related to the lifecycle work in SOLR-17857 / PR #4226, which made `SolrMetricsContext` AutoCloseable and leak-tracked. The diagnosis does not establish that this method caused every recent CI leak. Read the handoff before editing.

## Items
1. **Add safe AutoCloseable cleanup and regression coverage.** Start from the pinned Apache main SHA. Inspect every tracked AutoCloseable-only implementation and decide whether generic close-on-failure is safe, including nested parent/child tracker entries. Preserve the executor-specific `ExecutorUtil.shutdownAndAwaitTermination` path and best-effort exception handling. Add regression tests for a minimal AutoCloseable-only tracked object, exception continuation, and relevant nested or registered resources. Keep global tracker state isolated with `finally`. Change only `solr/solrj/src/java/org/apache/solr/common/util/ObjectReleaseTracker.java` and `solr/core/src/test/org/apache/solr/common/util/TestObjectReleaseTracker.java` unless a narrowly necessary test-support change is explained in the report. Output: `reports/object-release-tracker-autocloseable-fix.md`, `gates/object-release-tracker-autocloseable.md`, and a local candidate commit SHA.

## Decisions
| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scope | decided: test-failure cleanup fallback in `ObjectReleaseTracker.tryClose()` and focused regression tests | Do not change normal production shutdown, `CoreContainer.shutdown`, `HttpShardHandlerFactory`, `HttpJettySolrClient.close`, ThinCache, or unrelated lifecycle code. | Nick Shanin |
| Base | decided: Apache main at `79b23df58daac14b92cc35f23b8b81f72098b4ba` | If upstream main advances before the claim, rebase the candidate to its current tip and record both SHAs; do not use an old fork main. | Nick Shanin |
| Candidate visibility | decided: keep the candidate local for Nick's review | Do not push a product branch, open or edit a PR, post a comment, or file a Jira issue. | Nick Shanin |
| Validation | decided: tidy, Error Prone, focused tracker tests, and scoped `:solr:core:check -x test` plus `:solr:solrj:check -x test` | Run once under the host Gradle lock and the 15% inode guard. Do not run full core or SolrJ suites. If a focused test fails, record it and stop without retrying. | Nick Shanin |

## Deliverable
`reports/object-release-tracker-autocloseable-fix.md`: exact base and candidate SHA, audited tracked types, patch explanation, precise test counts, tidy/Error Prone/scoped-check results, and remaining limitations. `gates/object-release-tracker-autocloseable.md` records the exact gate commands and result. Mark this assignment and claim DONE in the same coordination push as the report and gate.

## Limits
- Only modify the two listed source/test files unless the report explains a necessary test-support change.
- Do not push a product branch or touch any PR or Jira record. Keep any implementation commit local for Nick's review.
- No full `:solr:core:test` or `:solr:solrj:test` suites.
- No force push. Commit as Nick Shanin with no trailers. No em dashes or en dashes.
