# Claim: SOLR-16398-command-jaxrs fix round 3 and regate (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `gates/SOLR-16398-command-jaxrs.md`; owner direction 2026-10-29 ("Need to try to use the decision ladder for these questions, then try to take 16398 next").
- Slice: decide the three open questions on the round 2 head with the decision ladder, change the rebalance response shape if the ladder says so, regate the command branch.
- Capability tags used: gate.
- Started: 2026-10-29T14:26:00Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-16398-command-jaxrs-fix-r3@1a8ef0a.
- Links: Ticket or parent: SOLR-16398. Category: see `pool/work-map.md`. Siblings: `gates/SOLR-16398-core-commands.md` (next, after this round); `gates/SOLR-16396-config-jaxrs.md` (DONE, error shape decision below). Supersedes: `receipts/SOLR-16398.md` round 2 response shape (`success` field).
- Constraints: fork push fast-forward only; no JIRA or GitHub post, no PR; commit identity Nick Shanin, no trailers.

## Decisions (ladder, recorded before the run)

1. Rebalance response shape: rung 3 (documented intent). `solr/solr-ref-guide/modules/deployment-guide/pages/collection-management.adoc` (v2 REBALANCELEADERS example, around lines 2400-2530) documents top-level `responseHeader`, `Summary` and `successes`. `dev-docs/apis-v2-conventions.adoc` lines 151-156 say a response whose whole top-level shape is open-ended uses `FlexibleSolrJerseyResponse`, and `SubResponseAccumulatingJerseyResponse` is for Overseer fan-out. Options: (a) keep the v1 map under `success` (rejected: contradicts the documented shape and the Overseer-only base); (b) `FlexibleSolrJerseyResponse` with the v1 keys at top level (chosen); (c) a typed response (rejected: the top-level keys are shard names, not fixed). Discriminating check: on head `1a8ef0a` the new assertion `Summary` at top level fails (`testRebalanceLeaders`, probe run `solr16398-r5-probe`).
2. Javabin on split-shard and move-replica: rung 2 probe. On `1a8ef0a`, with the explicit javabin response parser: GET `/collections` passes (`solr16398-r5-probe`), POST `/aliases` (existing JAX-RS POST on this branch's base, `CreateAlias`) passes, POST split passes (`solr16398-r5-probe3`). The earlier failure used SolrJ's default parser, which expects javabin but does not send `wt=javabin`; without `wt` or `Accept` the server answers JSON (existing test `V2ApiIntegrationTest.testRespondsWithJsonWhenWtAndAcceptAreMissing`). Chosen: no server change; the tests keep JSON explicitly. Not a question for the owner. Note for SolrJ maintainers: the default V2 parser does not match the server default; pre-existing and generic.
3. V2 config error shape (SOLR-16396): rung 3 (dev-docs). `dev-docs/apis-v2-conventions.adoc` lines 73-77 define V2 errors as `responseHeader.status`, `error.code` and `error.msg`. No ref-guide page mentions `errorMessages`. No SolrJ or Admin UI code reads a top-level `errorMessages` from a V2 config call (the remaining hits are v1 schema, security and command-parser code). Options: (a) keep a top-level `errorMessages` in V2 responses (rejected: non-convention duplicate); (b) messages only under `error.details[].errorMessages` (chosen, already on `a645bff`). Recorded as a stated behavior in the 16396 PR draft, not as a question.

## Steps

Same as round 2: tidy, Error Prone compile, SolrJ generation, focused (`org.apache.solr.handler.admin.api.*`, `TestCollectionAPIs`, `V2ApiIntegrationTest`), `:solr:core:check -x test`.

Heartbeat: 2026-10-29T14:26:00Z

Result: pending.
