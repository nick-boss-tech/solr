# Claim: Admin UI premise runs (SOLR-9831, SOLR-9818; SOLR-9759 optional)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job assignments/pool-admin-ui-premise-runs.md: Status: UNCONFIRMED (see the census note)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: selenium
- Assignment: `assignments/pool-admin-ui-premise-runs.md`
- Date: 2026-10-11
- Staffing: 1, with no subagent. The runs are executed by Nick in this session with the `!` prefix, because this host's job is blocked from Gradle. The agent prepares the handoff commands and records the results.
- Standing rule: the Windows host's build rule (WORKFLOW.md) says premise-run work does not run on this machine. Nick has directed these Selenium runs to run here, typed by him. This claim records that exception and applies to these three tickets only.
- Heads checked at claim time against the ticket worktrees: `solr-9831-submit` at `f269a70e84f0`, `solr-9818-submit` at `63f2d7ce9267`, `solr-9759-submit` at `31e702622dae`. All match the assignment's tips.
- Base for both runs: merge-base `cabedd1d968059215188f4e7563fb303241899ed`, with the head's `solr/webapp/src/test` overlaid (the fail-before shape).
- Deliverables: `reports/admin-ui-premise-runs.md` (command shape, seed, outcome on base and at head, corrected premises) and updates to `receipts/SOLR-9831.md` and `receipts/SOLR-9818.md`. SOLR-9759 runs only if Nick asks.
- Scope: no branch edits. No PR, comment or Jira write. No builds run by this agent.
- Heartbeat: 2026-10-11T03:37Z (claim taken; handoff commands prepared); the head run was retried at 06:30 UTC.
- Status refresh: 2026-10-11T18:53Z. PAUSED, not running. No run and no subagent is in progress on this host. The SOLR-9831 head run (run 1) did not execute the test: Gradle reported no tests found and the class wrote no JUnit result (the gradlew wrapper bug was worked around with the Windows wrapper). The cause is posted as `assignments/pool-admin-ui-test-selection.md`, which has no claim yet. Runs 2 to 4 wait on that investigation or on Nick's direction. The claim is re-taken with a new heartbeat when the runs resume; until then it is not a live claim.
