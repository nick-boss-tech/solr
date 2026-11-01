# Assignment: SOLR-13570 timeout for requests from Overseer to other nodes cannot be controlled, round 1

Status: OPEN. Capability tags: implementation, records (phase 1); gate (phase 2, separate claim). Host: any capable pool Linux host.

Trigger: Nick's Overseer tail census (2026-10-31), Group D. Nick directed two-phase assignments on 2026-10-31: implementation first, gate second, one claim per phase.

Ticket: https://issues.apache.org/jira/browse/SOLR-13570 (Open; reporter and assignee caomanhdat, 2019). The regression, as written on the ticket: Overseer requests to other nodes go through the client held by UpdateShardHandler, whose timeouts should follow the distribUpdateConnTimeout and distribUpdateSocketTimeout settings, but since SOLR-11004 the client builder hardcodes the socket timeout, so the configured values no longer take effect for these requests.

## Phase 1: implementation (this claim)

1. Census first: run ~/workspace/tools/solr-work.sh SOLR-13570 for prior work. Note the ticket has an assignee; the factory prepares work and does not post or open anything (below), and Nick decides how it is offered.
2. Trace the current path before changing anything: which client the Overseer uses for node-to-node requests today, where that client is built, and where its timeouts are set at current main (the client stack has changed since 2019; Http2 and Jetty clients are both in play). Confirm the configured timeouts still fail to reach that client. If they already flow correctly, stop: the deliverable becomes a verdict report with file and line citations, and phase 2 does not open.
3. If confirmed, implement the narrow fix: the configured connection and socket timeouts reach the client the Overseer actually uses. No new settings, no builder redesign.
4. Add or adjust a test that pins it: a client built through the production path carries the configured timeout values. New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4.
5. Tidy the final tree, commit as Nick Shanin with no trailers, and push branch solr-13570-submit to the fork.

Deliverable: reports/solr-13570-implementation-round-1.md naming the branch, the head, the client path as traced, and the test added. Do NOT post on Jira and do NOT open a PR; both wait for Nick's explicit go.

## Phase 2: gate (separate claim, after the phase 1 report lands)

1. Claim separately with the gate tag; gate the exact pushed head from the phase 1 report.
2. Full gate for the touched module: tidy clean (formatting-only tidy diffs get the standard tidy commit and re-gate), Error Prone compile (-Pvalidation.errorprone=true), the focused test class, and the module check.
3. Proof pair: the pinning test fails at the fix commit's parent and passes at the head, same seed both legs.

Deliverable: receipts/SOLR-13570.md with both legs, the seed, and the fresh JUnit XML counts.
