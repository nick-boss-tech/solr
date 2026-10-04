# SOLR-15615 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-15615 - "SolrCloud MLT does not work with Time Routed Alias (TRA)" (2021). Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-15615-submit` off `apache/solr` main `e2cdb2d7e8ae`

## The bug, as understood
`CloudMLTQParser.getDocument` fetches the source document with the `/get` handler of **the core handling the request**. Real-time get only distributes across that core's own collection, so for an alias spanning several collections (a TRA is the common case) a source document living in another collection is not found, and the parser throws 400 "Could not fetch document with id".

## What the branch changes
When the local RTG finds nothing, and the request has a `collection` param (HttpSolrCall sets it to the alias-resolved list), `getDocumentFromOtherCollections` asks each of the other listed collections with `ZkController.getSolrClient().getById(collection, id)` and uses the first hit. Errors are logged and skipped.

Test: `CloudMLTQParserTest.testMLTQParserOnAliasOverMultipleCollections` (second collection with the source doc, alias over both, MLT query repeated 6 times so each collection gets to handle the request).

## Guesses to verify first
1. `HttpSolrCall.addCollectionParamIfNeeded` really leaves a `collection` param on `req.getParams()` at the point the parser runs, and **not** when the alias resolves to the single collection of the handling core (no extra work then).
2. `CloudSolrClient.getById(collection, id)` works from inside a request thread (uses the `/get` handler with `ids`); security/auth for the internal request (inter-node PKI) may need `ZkController` credentials handling that a plain `getSolrClient()` call doesn't have.
3. Shard sub-requests: with a plain multi-shard collection the parser also runs per shard; the new fallback only triggers when a `collection` param is present, which shard requests should not carry. Verify no extra RPCs in the single-collection path.
4. The MLT "results are indeterministic (depends on which local shard is hit)" part of the ticket (non-parser MLT handler) is not addressed.
5. Whether the final query should also be restricted to exclude the source doc in the other collection (it is excluded by an id query on the unique key; with different collections that id could also exist in the first collection).

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.mlt.CloudMLTQParserTest"
```
Fail-before: revert `CloudMLTQParser.java` only; the alias test should fail with "Could not fetch document with id [100]" on at least one of the attempts.

## Not done
No JIRA comment, no PR.
