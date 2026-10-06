# SOLR-10305 - hypothetical reproduction (nothing was compiled or run)

JIRA (2017): a non-stored uniqueKey gave an NPE in `QueryComponent.mergeIds`. The audit note said "obsolete: fixed on
master per reporter, docValues uniqueKey supported". That half is right (David Smiley: `uniqueKey` with `docValues=true
stored=false` works). The thread's last comment (Jan Hacker, 2020) raised a second case that nobody picked up: the schema
has **no** uniqueKey (allowed; the ref guide says it is not required) and the request carries an explicit `shards=`.

## What main does (read on `upstream/main`)
- `IndexSchema` logs "no uniqueKey specified in schema" and leaves `getUniqueKeyField()` null.
- `SearchHandler.isDistrib` is true for `shards=host/core`, so the query goes through `QueryComponent.regularDistributedProcess`,
  where `mergeIds` (`rb.req.getSchema().getUniqueKeyField().getName()`, ~L793), `createMainQuery` (~L870), `createRetrieveDocs`
  and `returnFields` (~L1417, ~L1460) dereference it unconditionally: NullPointerException, HTTP 500.
- `ResponseLogComponent` already guards the null; the query component does not.

## Change
`QueryComponent.prepare` throws `BAD_REQUEST` "Distributed search requires a uniqueKey field in the schema" at the start of
the `rb.isDistrib` block, before any shard request is built. New `MinimalSchemaTest.testDistributedQueryWithoutUniqueKeyIsBadRequest`
(that class already asserts its schema has no uniqueKey).

## Guesses to verify first
- `prepare` runs after `isDistrib` is set and before the first distributed stage (read in `SearchHandler`, not run); the shard
  host `127.0.0.1:1/solr/collection1` is never contacted because of the early throw.
- `rb.isDistrib` can still be reset to false by `ShardHandler.prepDistributed` for a single-shard SolrCloud collection; those
  always have a uniqueKey, so the new check is not reached.
- Other components in the chain (`MoreLikeThisComponent`, `TermVectorComponent`) have the same dereference but only matter
  in distributed mode if `QueryComponent` accepted the request; not changed.

## Fail-before
Expected: the new test fails on `upstream/main` with a NullPointerException (HTTP 500) instead of a 400 mentioning uniqueKey.
