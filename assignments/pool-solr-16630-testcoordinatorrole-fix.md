# Pool assignment: SOLR-16630 TestCoordinatorRole.testNRTRestart fix

Capability tags: `implementation`, `gate`. Staffing: 1 to 2. Claim path: claims/pool-solr-16630-testcoordinatorrole-fix.md. Gate job file when the gate is posted: gates/SOLR-16630.md.

Background: SOLR-16630 is an existing, reopened Jira ticket for this test failing; no new ticket is needed. The root-cause analysis is on this branch at reports/flaky-tests-root-cause-round-1-t3.md (read it in full first). Finding (about 70 percent confidence): the test stops the PULL node on a fixed timer, which closes the client the add loop is still using; the loop catches only SolrException, so a ClosedChannelException from the closed channel fails the test. It failed again on an apache/solr CI run for an unrelated change on 2026-10-10 (run 38009158573, seed 681E2A715B2CE1D3), and settling runs with that seed on current main and at that change's head did not reproduce it.

Work:
1. On a new branch solr-16630-submit off current upstream main, implement the report's fix in the test only: keep the PULL node up until an add succeeds (no production change), following the report's exact proposal adapted to the code as found.
2. Add changelog/unreleased/SOLR-16630.yml (type: fixed; no unquoted ": " in the title).
3. Post the gate job at gates/SOLR-16630.md and run the full hardening gate. Verification is repetition-based because the failure is intermittent: the fixed test runs at least 6 times (the CI seed plus random seeds), all green required; the gate file and log state this shape plainly.
4. On green: push the branch, write receipts/SOLR-16630.md, mark the claim DONE. PR opening stays with the main agent and needs Nick's approval.

Rules: the standing limits in WORKFLOW.md bind (subagent cap, tidy-clean committed tree before gating, no-wait pattern: a lane that reaches a gate records the gate job and ends; it does not sleep-poll).
