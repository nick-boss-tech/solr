# SOLR-12657 - hypothetical-reproduction handoff

**Read this first: the regression test on this branch was written without being compiled or run.** The research/implement pipeline has
no Gradle access, so the test is a best-guess reproduction. Treat it as a hypothesis to confirm, not as proof.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12657 - "Facet streaming expression doesn't support min / max correctly for date fields" (7.4; the JIRA fix-version 7.4.1 is stale, the ticket is open and the code was unchanged)
- Branch: `solr-12657-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)
- Research note: `research/pipeline/research-notes/SOLR-12657.md` in the Solr-issues workspace

## The bug, as understood
`FacetStream.fillTuples` did `Number d = (Number) bucket.get("facet_" + m)` for every non-count metric. For `min(date_field)` / `max(date_field)` the JSON facet
response carries a `Date` (javabin) or ISO string, so the cast threw `ClassCastException: java.util.Date cannot be cast to java.lang.Number`
(reporter's stack trace in the ticket; surfaces as an IOException from `FacetStream.open`). The JSON Facet API itself returns the dates correctly.

## What the branch changes
- `FacetStream.java`: if the bucket value is not a `Number`, put it in the tuple as-is (a `Date` is converted to `Instant.toString()`, i.e. ISO-8601 UTC). Numbers behave exactly as before.
- `StreamExpressionTest.testFacetStreamMinMaxOnDateField`: 3 docs with `d_dt`, `facet(... buckets="a_s", min(d_dt), max(d_dt), count(*))`, asserts per-bucket min/max strings and count.

## What was guessed (verify these first)
1. `MinMetric`/`MaxMetric` constructors or `getFacetMetrics`/`getJsonFacetString` may themselves assume numeric fields (e.g. `outputLong`, rollup). If the test now fails earlier
   than `fillTuples`, the real fix is wider than the one-line cast and this branch is a partial fix.
2. `*_dt` in the `streaming` configset is assumed to have docValues (needed by JSON facet min/max). Schema type `date` uses `${solr.tests.numeric.dv}`-style properties; check it.
3. Expected string format: `Instant.toString()` renders `2018-01-01T00:00:00Z` (no fractional part when zero). If javabin already returns a String the pass-through is used and the format is Solr's own.
4. Parallel/rolled-up facet (`ParallelMetricsRollup`, `getRollupSelectFields`) for date min/max is NOT addressed; only the plain `facet()` path.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:solrj-streaming:spotlessApply
.\gradlew :solr:solrj-streaming:test --tests "org.apache.solr.client.solrj.io.stream.StreamExpressionTest.testFacetStreamMinMaxOnDateField"
```
Fail-before: revert only the `FacetStream.java` hunk; expect `ClassCastException` wrapped in IOException.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
