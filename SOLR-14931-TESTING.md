# SOLR-14931 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-14931 - "Macros in appends/invariants parameters not getting expanded in Solrcloud" (2020). The earlier skip note was "has-pr: apache/solr#159, closed unmerged"; the bug is still present on main. Reopened in audit round audit-1 (Tier 2 batch 1).
- Branch: `solr-14931-submit` off `apache/solr` main `9b3a84b1c46`

## The bug, as understood
`RequestUtil.processParams` merges the handler's defaults, appends and invariants into the request parameters and then expands macros, but only when the request is not a shard request (`!isShard`, because the coordinator already expanded the client's parameters and expanding again would let client text be interpreted twice). The handler config is applied again on the shard, though (appends and invariants are security-relevant, so it must be). On a shard those config values are therefore merged in *unexpanded*: the reporter's `fl` from `appends` shows up as `term_appends:'${my_term}'` in the documents while `responseHeader.params.fl` shows the coordinator's expanded copy. Invariants have the same problem and additionally overwrite the coordinator's expanded value with the raw macro.

## Design choice
- Rejected: skipping appends/invariants on shard requests. `isShard=true` is a client-settable parameter, so that would let anyone drop an `appends` ACL filter (`fq`) by adding `isShard=true`.
- Chosen: on shard requests, expand macros only in the config-provided appends and invariants, looking macro values up in the request parameters. Request-sourced values (what the client sent, or the coordinator forwarded) are still never expanded on shards. `expandMacros=false` is respected. Non-shard behavior is unchanged.
- Not fixed: the appended value is still present twice on a shard (the coordinator's copy plus the shard's); that duplication is the separate SOLR-10059 symptom and is harmless for `fl`, but does matter for things like `fq` count or `facet.field`.

## What the branch changes
- `MacroExpander`: new static `expand(Map toExpand, Map paramSource)` and a private `paramSource` field (macro values are looked up there; it equals `orig` for the existing entry points).
- `RequestUtil`: private `expandConfigMacros` used for the appends and invariants maps.
- Test: `TestMacros.testHandlerAppendsAndInvariantsMacrosAreExpandedOnShardRequests` calls `RequestUtil.processParams(null, req, null, appends, invariants)` directly with `isShard=true`, and checks (1) expanded `fl`/`rows`, (2) client-sent `${my_term}` stays literal.

## Guesses to verify first
1. `req(...)` returns params that `MultiMapSolrParams.asMultiMap` can copy (the test passes MapSolrParams-based request params); `getParams("fl")` order is request values first, then appended values.
2. `processParams` accepts a `null` handler (the Javadoc says so) and does not touch content streams in that case.
3. The single-node reporter scenario (no `isShard`) was never broken by this; the end-to-end SolrCloud case was not written (would need a cloud test with `${...}` in solrconfig).

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.request.macro.TestMacros"
```
Fail-before: change `expandConfigMacros` to return `configValues` unconditionally; the new test should fail on the first assertion.

## Not done
No JIRA comment, no PR.
