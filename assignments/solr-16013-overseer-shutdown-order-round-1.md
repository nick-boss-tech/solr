# Assignment: SOLR-16013 Overseer gives up its election node before closing, round 1

Status: CLAIMED (phase 1, implementation and records) by vm6, 2026-11-01. Phase 2 (gate) is a separate claim. Carried from the archived board `coordination` (assignment commit 3b53a1c638) because this board is the live one.

Trigger: Nick's Overseer tail census (2026-10-31), Group D. Nick directed two-phase assignments on 2026-10-31: implementation first, gate second, one claim per phase.

Ticket: https://issues.apache.org/jira/browse/SOLR-16013 (Open, unassigned; reporter Hossman, 2022). The defect, with the code quoted on the ticket: ZkController shutdown submits the overseer elector context close and the Overseer close as two independent tasks on a custom thread pool. The elector context can therefore close first, giving up the election node while the Overseer is still running, so a newly elected Overseer elsewhere can process the same in-flight commands a second time.

## Phase 1: implementation (this claim)

1. Census first: run ~/workspace/tools/solr-work.sh SOLR-16013 for prior work on this ticket.
2. Read the current shutdown path in org.apache.solr.cloud.ZkController before changing anything. Confirm the two closes are still unordered at current main. If shutdown has been restructured since 2022 and the ordering is already guaranteed, stop: the deliverable becomes a verdict report with file and line citations, and phase 2 does not open.
3. If confirmed, implement the narrow fix: the Overseer closes before its election context is released, sequenced rather than racing on a pool. Keep the change inside the shutdown path; no lifecycle redesign.
4. Cover it as far as the harness honestly allows: a test that observes close ordering, or a stated reason in the report why ordering cannot be observed in a test, with the strongest check that can be made instead. New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4.
5. Tidy the final tree, commit as Nick Shanin with no trailers, and push branch solr-16013-submit to the fork.

Deliverable: reports/solr-16013-implementation-round-1.md naming the branch, the head, the shutdown sequence as found, and what the proof can and cannot observe. Do NOT post on Jira and do NOT open a PR; both wait for Nick's explicit go.

## Phase 2: gate (separate claim, after the phase 1 report lands)

1. Claim separately with the gate tag; gate the exact pushed head from the phase 1 report.
2. Full gate for the touched module: tidy clean (formatting-only tidy diffs get the standard tidy commit and re-gate), Error Prone compile (-Pvalidation.errorprone=true), the focused tests named in the phase 1 report, and the module check.
3. Proof pair where the fix is observable: fail-before at the fix commit's parent, pass at the head, same seed. Where it is not observable, the receipt states that plainly instead of claiming a pair.

Deliverable: receipts/SOLR-16013.md with the run results and the proof statement.
