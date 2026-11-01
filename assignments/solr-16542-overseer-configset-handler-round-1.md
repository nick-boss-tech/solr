# Assignment: SOLR-16542 remove OverseerConfigSetMessageHandler, always do distributed mode, round 1

Status: OPEN. Capability tags: implementation, records (phase 1); gate (phase 2, separate claim). Host: any capable pool Linux host.

Trigger: Nick's Overseer tail census (2026-10-31) lists SOLR-16542 in Group 0 as the ConfigSet handler removal implementation slice, carried from the SOLR-14927 removal umbrella subtask list (the summary search misses it). Posted by the phone agent overnight 2026-11-01, in the same two-phase shape as the four Group D assignments posted 2026-10-31: implementation first, gate second, one claim per phase. Nick directed two-phase assignments on 2026-10-31.

Ticket: https://issues.apache.org/jira/browse/SOLR-16542 (Major, Open, unassigned; reporter David Smiley; label newdev; last updated 2025-10-17). Summary: Remove OverseerConfigSetMessageHandler; always do distributed mode. No prior fork branch, board receipt, or board draft was found for this ticket before posting (checked 2026-11-01).

Boundary: this ticket sits under the removal umbrella SOLR-14927 and next to SOLR-17293 (decentralized processing as the default, with the overseerEnabled switch). If the code read shows the handler is already gone, or distributed mode is already unconditional, the deliverable is a verdict report saying so with citations, not a removal patch. If the removal overlaps live work on SOLR-14927 or SOLR-17293, say so and stop rather than duplicating it.

## Phase 1: implementation (this claim)

1. Claim this phase first (a claims/ file naming SOLR-16542) before starting it, and take no second ticket from this assignment at the same time.
2. Census first: run ~/workspace/tools/solr-work.sh SOLR-16542 to see any prior branch or draft for this ticket.
3. Read the current code before changing anything: OverseerConfigSetMessageHandler, its callers, and every place that still branches on distributed versus overseer mode for ConfigSet handling. Confirm or refute the ticket's premise at current main: the handler still exists, it still does work that distributed mode would replace, and the removal scope is identifiable. If the premise no longer matches the code (already removed, already unconditional, or a different mechanism), stop: the deliverable becomes a verdict report with file and line citations, and phase 2 does not open.
4. If confirmed, implement the narrow removal slice: remove the handler and its callers, and make the ConfigSet path always do distributed mode. No unrelated cleanup, no renames beyond what the removal forces, no changes to other Overseer handlers.
5. Add or adjust a test that would have caught a regression: ConfigSet handling works with no overseer handler in the path. New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4.
6. Tidy the final tree, commit as Nick Shanin with no trailers, and push branch solr-16542-submit to the fork.

Deliverable: reports/solr-16542-implementation-round-1.md naming the branch, the head, the mechanism as confirmed in the code, and the test added. Do NOT post on Jira and do NOT open a PR; both wait for Nick's explicit go.

## Phase 2: gate (separate claim, after the phase 1 report lands)

1. Claim separately with the gate tag; gate the exact pushed head from the phase 1 report.
2. Full gate for the touched module: tidy clean (a formatting-only tidy diff gets the standard tidy commit and re-gate), Error Prone compile (-Pvalidation.errorprone=true), the focused test class, and the module check.
3. Proof pair: the new or adjusted test fails at the fix commit's parent and passes at the head, same seed both legs. If the change is a pure removal with no new test leg, state that plainly and record the module check result instead of forcing a pair.

Deliverable: receipts/SOLR-16542.md with both legs or the stated substitute, the seed, and the fresh JUnit XML counts.
