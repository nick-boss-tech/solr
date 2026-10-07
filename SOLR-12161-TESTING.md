# SOLR-12161 - hypothetical reproduction (not run)

Nothing here was compiled or run. The test was guessed from reading `upstream/main`.

## JIRA context
Erick Erickson: with basic auth enabled, `CloudSolrClient` adds documents without credentials, while commits and
queries fail with 401. Noble Paul's diagnosis: updates are split per shard and sent from a client thread pool; SolrJ
mistook that for an inter-node request and attached the PKI header, which the in-JVM Solr nodes of the test accepted.
Noble's fix was a flag so a pool used outside Solr does not set the PKI header. Never confirmed fixed in the ticket.

## What the code shows on main
`PKIAuthenticationPlugin` only adds its header when `isSolrThread()` / a `SolrRequestInfo` is present (around line 389),
so the described path looks closed. But `BasicAuthIntegrationTest` has no test that sends a document update through the
shared `cluster.getSolrClient()` without credentials; its 401 checks use a collection reload, a `deleteByQuery` on a
node-local client, and Jetty clients. The ticket's own scenario (docs split over several shards, no credentials) is
unpinned.

## Change
Test-only, in `BasicAuthIntegrationTest.testBasicAuth`: after the `update` permission is limited to `admin`, a 30
document `UpdateRequest` without credentials goes through `cluster.getSolrClient()` against the 3-shard collection and
must fail with 401.

## Guessed / verify first
- The request may surface as a `RemoteSolrException` with code 401 or as a wrapped `SolrServerException`; adjust the
  exception type if the run shows otherwise.
- Assumes the `update` permission is already in force at that point of the test (it is set a few lines above).
- If it passes on a build without any change this is a pin, not a fix: expect `NOT_PROVEN` from the fail-before stage.
