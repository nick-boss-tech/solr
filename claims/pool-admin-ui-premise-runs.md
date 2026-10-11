# Claim: Admin UI premise runs (SOLR-9831, SOLR-9818; SOLR-9759 optional)

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
- Heartbeat: 2026-10-11T03:37Z (claim taken; handoff commands prepared).
- Status: ACTIVE, waiting on Nick's runs.
