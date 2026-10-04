# SOLR-17731 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-17731 - "Some v2 APIs are overshadowed at runtime so can't be used" (2025). Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-17731-submit` off `apache/solr` main `e2cdb2d7e8ae`

## The bug, as understood
Jersey picks one resource class by matching the class-level `@Path` (most literal characters first) and does not fall back to another class when that one has no matching HTTP method, so the answer is 405.
- `CollectionSnapshotApis.Create` was `@Path(".../snapshots")` + `@POST @Path("/{snapshotName}")`; `.Delete` is `@Path(".../snapshots/{snapshotName}")` (DELETE only) and wins for `POST .../snapshots/x`.
- `ListAliasesApi` (`/aliases`) had `GET /{aliasName}`; `DeleteAliasApi` (`/aliases/{aliasName}`, DELETE only) wins for `GET /aliases/x`.

## What the branch changes
- `Create` now has class-level `@Path("/collections/{collName}/snapshots/{snapshotName}")` with a plain `@POST`, sharing its path with `Delete`.
- `getAliasByName` moved to a new `GetAliasByNameApi` (`@Path("/aliases/{aliasName}")`, `@GET`) with impl `GetAliasByName`, registered in `CollectionsHandler`; `ListAliasesAPITest` updated.
- New `V2ResourcePathOverlapTest` (1-node SolrCloudTestCase) hits both endpoints over HTTP.

## Guesses to verify first
1. Jersey merges two resource classes with an identical class-level path in Solr's setup. If not, use one interface/impl holding both methods.
2. `V2Request` with payload `{}` is accepted for snapshot create; list response contains the snapshot name.
3. OpenAPI generation / API-listing tests may need updating for the new interface.
4. Other overlapping resource classes probably exist; only the two in the ticket were fixed.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.admin.api.V2ResourcePathOverlapTest" --tests "org.apache.solr.handler.admin.api.ListAliasesAPITest"
```
Fail-before: revert the two API-interface changes; both new tests should fail with a 405.

## Not done
No JIRA comment, no PR.
