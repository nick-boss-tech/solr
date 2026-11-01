# Assignment: SOLR-16065 run checkIfIamStillLeader outside a thread in the Overseer, round 1

Status: OPEN. Capability tags: implementation, records (phase 1); gate (phase 2, separate claim). Host: any capable pool Linux host.

Trigger: Nick's Overseer tail census (2026-10-31), Group D. Nick directed two-phase assignments on 2026-10-31: implementation first, gate second, one claim per phase.

Ticket: https://issues.apache.org/jira/browse/SOLR-16065 (Open, unassigned; reporter Houston Putman, 2022). At the end of Overseer.run(), a dedicated thread is started to run checkIfIamStillLeader continuously. The comment in the code says the check runs in a separate thread because any wait is interrupted in the main thread. The ticket questions whether that is still true and asks for the check to run without the extra thread if it can.

## Phase 1: implementation (this claim)

1. Census first: run ~/workspace/tools/solr-work.sh SOLR-16065 for prior work on this ticket.
2. Answer the ticket's question from evidence before changing anything: read Overseer.run() and the leader-check loop at current main, and the history of the interruption comment (git blame and log on those lines). Establish what interrupts the main thread's waits, or that nothing does anymore. If the thread is still needed, stop: the deliverable is a verdict report answering the ticket's question with citations, and phase 2 does not open.
3. If the premise holds, implement the narrow change: the leader check runs without spawning the dedicated thread, or the smallest restructuring that removes it safely. No changes to election timing, intervals, or leadership semantics.
4. Cover the behavior with the existing Overseer tests plus any focused test the change honestly supports. New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4.
5. Tidy the final tree, commit as Nick Shanin with no trailers, and push branch solr-16065-submit to the fork.

Deliverable: reports/solr-16065-implementation-round-1.md naming the branch, the head, and the evidence for why the thread is or is not needed. Do NOT post on Jira and do NOT open a PR; both wait for Nick's explicit go.

## Phase 2: gate (separate claim, after the phase 1 report lands)

1. Claim separately with the gate tag; gate the exact pushed head from the phase 1 report.
2. Full gate for the touched module: tidy clean (formatting-only tidy diffs get the standard tidy commit and re-gate), Error Prone compile (-Pvalidation.errorprone=true), the focused tests named in the phase 1 report, and the module check.
3. Proof pair where observable: fail-before at the fix commit's parent, pass at the head, same seed; where the change is structural only, the receipt says so and leans on the existing suite passing at the head.

Deliverable: receipts/SOLR-16065.md with the run results and the proof statement.
