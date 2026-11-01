# Assignment: SOLR-16793 Overseer queue length metrics report the same value for all queues, round 1

Status: OPEN. Capability tags: implementation, records (phase 1); gate (phase 2, separate claim). Host: any capable pool Linux host.

Trigger: Nick's Overseer tail census (2026-10-31) placed this in Group D, small fixes whose mechanism is already specified on the ticket. Nick directed two-phase assignments on 2026-10-31: implementation first, gate second, one claim per phase.

Ticket: https://issues.apache.org/jira/browse/SOLR-16793 (Minor, Open, unassigned; reporter pmcarthur, 2023). The report: the queue length metrics for the Overseer's state-update and collection work queues are always identical. The stated cause: the Overseer injects one shared Stats instance into the ZkDistributedQueue for each of its queues, and that Stats tracks only one length.

## Phase 1: implementation (this claim)

1. Census first: run ~/workspace/tools/solr-work.sh SOLR-16793 to see any prior branch or draft for this ticket.
2. Read the current code before changing anything: queue construction in org.apache.solr.cloud.Overseer, the Stats type it injects, and ZkDistributedQueue's use of it. Confirm or refute the shared-instance mechanism at current main. If the mechanism no longer matches the code (already fixed, or different cause), stop: the deliverable becomes a verdict report with file and line citations, and phase 2 does not open.
3. If confirmed, implement the narrow fix: each queue reports its own length. No metrics renames, no unrelated cleanup.
4. Add or adjust a test that would have caught it: two queues of different lengths reporting different values. New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4.
5. Tidy the final tree, commit as Nick Shanin with no trailers, and push branch solr-16793-submit to the fork.

Deliverable: reports/solr-16793-implementation-round-1.md naming the branch, the head, the mechanism as confirmed in the code, and the test added. Do NOT post on Jira and do NOT open a PR; both wait for Nick's explicit go.

## Phase 2: gate (separate claim, after the phase 1 report lands)

1. Claim separately with the gate tag; gate the exact pushed head from the phase 1 report.
2. Full gate for the touched module: tidy clean (a formatting-only tidy diff gets the standard tidy commit and re-gate), Error Prone compile (-Pvalidation.errorprone=true), the focused test class, and the module check.
3. Proof pair: the new or adjusted test fails at the fix commit's parent and passes at the head, same seed both legs.

Deliverable: receipts/SOLR-16793.md with both legs, the seed, and the fresh JUnit XML counts.
