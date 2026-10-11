# Claim: Admin UI Selenium test selection (the SOLR-9831 premise run selects no tests)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review (the investigation). The `selenium` confirming runs are not claimed here.
- Assignment: `assignments/pool-admin-ui-test-selection.md`
- Date: 2026-10-11
- Staffing: 1, as the assignment says. One read-only subagent.
- Scope: read the SOLR-9831 head (`f269a70e84f`, branch `solr-9831-submit`), the merge-base and the Gradle test configuration. Rank the mechanisms and write the confirming run spec. No edit to any branch. No Gradle, build or test on this host. The confirming runs go to a Linux host with Chrome and Gradle, or to Nick typing the commands himself.
- Deliverable: `reports/admin-ui-test-selection.md`, with the ranked mechanisms, the code evidence, the confirming run spec and any fix sketch as text.
- Heartbeat: 2026-10-11T21:04Z (claim taken).
- Status: ACTIVE.
- Link: this unblocks runs 2 to 4 of `claims/pool-admin-ui-premise-runs.md` (paused).
