# Claim: pool-ui-SOLR-17914

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, one ticket per claim.
- Ticket type: dialog. The immediately previous claim was a component (SOLR-17913), so complaint 5 is met. The other open types (file tree, configset files) are components too. This ticket has no fork branch, so it is an implementation claim.
- Slice (smallest first, from `reports/new-ui-collections-configsets-round-1.md`, SOLR-17914 section): the form alone with name, configset picker, shards and replication factor, plus the inline name-exists error and the general error. No advanced options, no tooltips, no host screen (the Collections screen is SOLR-17912, not this claim).
- Rung 1 settled the open item in the report: the server create endpoint is `POST /api/collections` (`solr/api/src/java/org/apache/solr/client/api/endpoint/CreateCollectionApi.java`, lines 26 to 28, `@Path("/collections")`), body `CreateCollectionRequestBody` with `name`, `config`, `numShards`, `replicationFactor`, `nrtReplicas` (lines 25 to 39).
- Branch: `solr-17914-create-collection-dialog` from base `c8ab71430f`. Gate before any push.
- Capability tags used: implementation, ui, jvm-tests.
- Started: 2026-10-24T09:22:51Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17914@c8ab71430f.
- Links: Ticket: SOLR-17914 (parent SOLR-17912). Category: ui. Siblings: `claims/pool-ui-SOLR-17913.md` (card, DONE). Supersedes: none.
- Decision ladder: rung 1 for the endpoint (done above). GL: the dialog uses text fields, buttons and a dialog only; check in the Result.
- Constraints: no JIRA or GitHub post, no PR. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-24T09:22:51Z
