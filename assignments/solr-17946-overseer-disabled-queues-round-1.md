# Assignment: SOLR-17946 disabled Overseer should not create ZK queues, round 1

Status: OPEN. Capability tags: implementation, records (phase 1); gate (phase 2, separate claim). Host: any capable pool Linux host.

Trigger: Nick's Overseer tail census (2026-10-31) lists SOLR-17946 in Group 0 as the disabled-Overseer queue creation implementation slice under SOLR-17293 (decentralized processing as the default, with the overseerEnabled switch). Posted by the phone agent overnight 2026-11-01, in the same two-phase shape as the four Group D assignments posted 2026-10-31 and the SOLR-16542 assignment posted the same night: implementation first, gate second, one claim per phase. Nick directed two-phase assignments on 2026-10-31.

Ticket: https://issues.apache.org/jira/browse/SOLR-17946 (Major, Open, unassigned; reporter David Smiley; last updated 2026-10-14). Summary: Disabled Overseer shouldn't create ZK queues. No prior fork branch, board receipt, or board draft was found for this ticket before posting (checked 2026-11-01), and no implementation assignment for SOLR-17946 exists on coordination-ui.

Boundary: this ticket sits under SOLR-17293 and next to the removal umbrella SOLR-14927 and the sibling slice SOLR-16542 (ConfigSet handler removal). If the code read shows a disabled Overseer already creates no ZK queues, or the queues are created by a path that is not the Overseer's to gate, the deliverable is a verdict report saying so with citations, not a patch. If live work on SOLR-14927 or SOLR-17293 already covers this slice, say so and stop rather than duplicating it.

## Phase 1: implementation (this claim)

1. Claim this phase first (a claims/ file naming SOLR-17946) before starting it, and take no second ticket from this assignment at the same time.
2. Census first: run ~/workspace/tools/solr-work.sh SOLR-17946 to see any prior branch or draft for this ticket.
3. Read the current code before changing anything: where the Overseer creates its ZK queues on startup, what "disabled" means in the current code (overseerEnabled switch, distributed mode), and every caller that assumes those queues exist. Confirm or refute the ticket's premise at current main: a disabled Overseer still creates ZK queues, and the creation site is identifiable. If the premise no longer matches the code (already gated, already unconditional, or a different mechanism), stop: the deliverable becomes a verdict report with file and line citations, and phase 2 does not open.
4. If confirmed, implement the narrow slice: a disabled Overseer creates no ZK queues. No unrelated cleanup, no renames beyond what the gating forces, no changes to other Overseer startup paths.
5. Add or adjust a test that would have caught a regression: with the Overseer disabled, no ZK queues are created, and enabled behavior is unchanged. New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4.
6. Tidy the final tree, commit as Nick Shanin with no trailers, and push branch solr-17946-submit to the fork.

Deliverable: reports/solr-17946-implementation-round-1.md naming the branch, the head, the mechanism as confirmed in the code, and the test added. Do NOT post on Jira and do NOT open a PR; both wait for Nick's explicit go.

## Phase 2: gate (separate claim, after the phase 1 report lands)

1. Claim separately with the gate tag; gate the exact pushed head from the phase 1 report.
2. Full gate for the touched module: tidy clean (a formatting-only tidy diff gets the standard tidy commit and re-gate), Error Prone compile (-Pvalidation.errorprone=true), the focused test class, and the module check.
3. Proof pair: the new or adjusted test fails at the fix commit's parent and passes at the head, same seed both legs. If the change is a pure gating change with no new test leg, state that plainly and record the module check result instead of forcing a pair.

Deliverable: receipts/SOLR-17946.md with both legs or the stated substitute, the seed, and the fresh JUnit XML counts.
