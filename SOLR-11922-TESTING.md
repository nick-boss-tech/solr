# SOLR-11922 - hypothetical-reproduction handoff (TEST-ONLY branch)

**Read this first: nothing on this branch was compiled or run, and there is no fix.** The research/implement pipeline has no Gradle access. This branch only adds a guessed test.

- JIRA: https://issues.apache.org/jira/browse/SOLR-11922 - "parallel - cartesianProduct" (Robson Koji, 6.6.2). The expression is truncated in the ticket; the stack trace is
  `ParallelStream.constructStreams` -> `IOException: NullPointerException` (the NPE is swallowed into the IOException, so its true origin is unknown).
- Branch: `solr-11922-submit` off `apache/solr` main `14c7aac0d15`
- Commits: test, this file. No changelog fragment (no fix).

## What the branch adds
`StreamDecoratorTest.testParallelCartesianProductStream`: `parallel(collection, cartesianProduct(search(..., partitionKeys="id", path="/export"), a_ss), workers=2, sort="id asc")` over two docs with five `a_ss` values each; expects 10 tuples ordered by id.

## What was guessed (verify these first)
1. **The test may already pass on main.** The ticket is from 2018 and `CartesianProductStream.toExpression` looks sound on main; I could not find the NPE by reading. If it passes, this is coverage only and the ticket may be obsolete - report on the ticket instead of opening a PR.
2. If it fails, read the *cause* of the `IOException` (`constructStreams` wraps everything in `new IOException(e)`): suspects are `getShards`, `tupleStream.toExpression(streamFactory)` for the evaluator, or an unregistered function name.
3. The local `StreamFactory` must register `cartesianProduct` (not `cartesian`, which `testCartesianProductStream` uses) because the expression is re-sent to the server `StreamHandler`.
4. Parameter names (`solrConnection`, `path="/export"`) were copied from `testParallelUniqueStream`; `partitionKeys=id` on an `/export` search needs `id` as a docValues sort field.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:solrj-streaming:spotlessApply
.\gradlew :solr:solrj-streaming:test --tests "org.apache.solr.client.solrj.io.stream.StreamDecoratorTest.testParallelCartesianProductStream"
```
