# Skip record: SOLR-15823

- Ticket: SOLR-15823, "Split v2 /node/logging API into separate GET and PUT APIs and deal with single node/all nodes logic". JIRA status Open, resolution none, created 2021-11-28, last updated 2026-10-05, checked 2026-10-19 UTC. Child issue: SOLR-15781 (Open).
- Verdict: pr-needed.
- Draft branch: solr-15823-draft-pr.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

Two asks. (a) Split the v2 `/node/logging` API by HTTP verb, so GET reads and PUT changes levels, which also lets each verb get its own permission. (b) Make the v2 API able to change a level on every live node; today the v2 endpoint is single-node, and the Admin UI falls back to the v1 `/admin/info/logging?nodes=all` handler for that. A comment on 2026-10-05, posted for Nick Shanin, says the work is planned as two pull requests.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

Part (a), done:

- `solr/api/src/java/org/apache/solr/client/api/endpoint/NodeLoggingApis.java:34-60`: `listAllLoggersAndLevels` is `@GET`; `modifyLocalLogLevel` is `@PUT`; `fetchLocalLogMessages` is `@GET`; `setMessageThreshold` is `@PUT`.
- `solr/core/src/java/org/apache/solr/handler/admin/api/NodeLogging.java`: each method carries its own `@PermissionName` (`CONFIG_READ_PERM` for GET, `CONFIG_EDIT_PERM` for PUT).

Part (b), not done:

- `solr/core/src/java/org/apache/solr/handler/admin/api/NodeLogging.java:47`: `// TODO Add support for 'nodes' param once SOLR-16738 is completed.` The TODO is still there. SOLR-16738 (`730398a940`, 2026-04-27) added the proxy package (`RemoteRequestProxy`, `V2SolrRequestBasedProxy`), but no code in `NodeLogging` uses it or takes a `nodes` parameter.
- `solr/webapp/web/js/angular/controllers/logging.js:164-167` and `solr/webapp/web/js/angular/services.js:192`: the Admin UI keeps the v1 "nodes=all" call for the level change, with a comment that names the missing v2 wiring.

So the verb split is in place, and the all-nodes v2 path is a code change that is still needed.

## History

- `730398a940` (2026-04-27), SOLR-16738: the proxy package the TODO refers to. Ancestor of the pinned head: yes.
- The verb split commits were not traced; they are on main.
- No commit on the fork names SOLR-15823 (empty `git log --grep` at the pinned head).

## Reproduction attempts

Not needed for pr-needed here: the change is a missing code path (the TODO and the absence of a `nodes` parameter in `NodeLogging`), not a failure to reproduce. The PR's check would be a test that calls the v2 level-change endpoint with a `nodes` parameter on a two-node cluster and reads the level on both nodes. No such test was searched for on the fork.

## Gap analysis

The v2 broadcast needs the node-to-node proxy to carry a PUT with a body to every live node, and to aggregate the responses the way the v1 handler does. The proxy exists; the wiring and the response shape are the design work. The ticket's comment says the team plans two PRs, so the work is planned; this record does not open or duplicate a PR.

## Verdict justification

pr-needed: "the code path shows it, and a change is needed." Minimum evidence: the TODO at `NodeLogging.java:47`, the missing `nodes` handling, and the UI's v1 fallback with its comment. The change the PR would make: add a `nodes` parameter to the v2 level-change endpoint that uses the proxy to broadcast, and point the Admin UI at it. The PR's check: a two-node test that changes the level with `nodes=all` through v2 and reads it back on both nodes.

Scope note: the ticket comment (2026-10-05) says the two-PR plan is in progress. The skip closeout does not open a PR and does not replace that plan.

## Limits

This record does not check whether the planned PRs are open or merged. It does not run a two-node test. It does not check the v1 aggregation code in detail.
