# SOLR-11391 - hypothetical reproduction (nothing was compiled or run)

JIRA (2017, Tier 4, audit note "performance enhancement, not a bug"): the ticket proposes using the graph terms collector for joins on
non-point fields and a `method` choice for `{!join}`. Most of it landed under SOLR-13892: `JoinQParserPlugin` has `Method`
(`index`, `dvWithScore`, `topLevelDV`, `crossCollection`) and the `method` local param. The perf work itself is not in scope here.

## What was left
Hoss's review asked that the `method` value be validated. `JoinQParserPlugin.parse` still carried the line
`// TODO Make sure 'method' is valid value here and give users a nice error` and called `Method.valueOf(...)` directly, so
`{!join ... method=bogus}` threw a raw `IllegalArgumentException` (HTTP 500). The static helper `createJoinQuery` already maps the same
failure to a 400 through `parseMethodString`.

## Change
`parse()` uses `parseMethodString`, so both entry points give "Provided join method 'x' not supported" with BAD_REQUEST. The TODO is gone.
Test: `TestScoreJoinQPNoScore.testUnknownJoinMethodIsBadRequest` (schema-docValuesJoin, `method=nosuchmethod`, expects 400 and the message).

## Guesses to verify first
- Nothing earlier in query parsing rejects the unknown local param with a different message or code.
- `assertQEx(String, String, SolrQueryRequest, ErrorCode)` is the 4-arg form in this tree (read from `SolrTestCaseJ4`, not run).
- Valid values stay case-sensitive (`topLevelDV`), as before.

## Fail-before
On main the request fails with `IllegalArgumentException: No enum constant ...Method.nosuchmethod`, wrapped as a 500, so the code assertion fails.
