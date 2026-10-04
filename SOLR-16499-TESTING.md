# SOLR-16499 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-16499 - "REPLACENODE API doesn't obey 'parallel', 'timeout' params" (Jason Gerlowski, 2022). Noble Paul in the ticket: the omission "should be a mistake". Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-16499-submit` off `apache/solr` main `e2cdb2d7e8ae`

## The bug, as understood
`ReplaceNodeCmd` reads `parallel` (default false) and `timeout` (default 600s) from the overseer message, and the Ref Guide documents both, but the v1 `REPLACENODE_OP` in `CollectionsHandler` builds a `ReplaceNodeRequestBody` that has no such fields and `ReplaceNode.createRemoteMessage` never put them in the message.

## What the branch changes
- `ReplaceNodeRequestBody` (public v2 model): new optional `Boolean parallel` and `Integer timeout`.
- `ReplaceNode.createRemoteMessage`: forwards both when non-null (keys `parallel`, `timeout`).
- `CollectionsHandler.REPLACENODE_OP`: fills them from the v1 params.
- `ReplaceNodeAPITest.testParallelAndTimeoutAreForwardedToTheOverseerMessage`. Existing tests unchanged (3-arg constructor kept, so message sizes still hold).

## Guesses to verify first
1. Adding fields to a public v2 request model changes the generated OpenAPI spec / SolrJ model; any API-spec check may need regenerating.
2. `params.getBool("parallel")` / `getInt(TIMEOUT)` return null (not a default) when absent, so the message stays unchanged for old clients.
3. SolrJ `CollectionAdminRequest.ReplaceNode` already has `setParallel` (sends `parallel`); it has no timeout setter. Not added.
4. The Ref Guide already documents both parameters; no doc change was made.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.admin.api.ReplaceNodeAPITest"
```
Fail-before: revert `ReplaceNode.java` only; the new test should fail on message size 2 vs 4.

## Not done
No JIRA comment, no PR. No end-to-end test that `parallel=true` really parallelizes (would belong in `ReplaceNodeTest`).
