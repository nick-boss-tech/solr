# SOLR-8576 - hypothetical reproduction (not run)

Nothing here was compiled or run. The test was guessed from reading `upstream/main`.

## JIRA context
Mark Miller: add tests for Collection API errors, specifically "collection already exists" and related, after chasing a
suspected regression that turned out to be a local timeout change.

## What the code shows on main
`CreateCollectionCmd` throws BAD_REQUEST `collection already exists: <name>` and
`collection alias already exists: <name>`. No test asserts either message (grep over `solr/core/src/test`).

## Change
Test-only: `CollectionsAPISolrJTest.testCreateCollectionNameAlreadyTaken` creates a collection and an alias, then
expects both CREATE failures with the exact messages and checks the original collection is intact and no collection
named like the alias appeared.

## Guessed / verify first
- Message text is asserted via `e.toString()`; the distributed command runner may wrap it differently.
- Uses the existing `conf` configset and `createAlias`; the class may run both distributed and Overseer modes.
- A changelog fragment of type `other` for a test-only change may be unwanted; delete it if so.
