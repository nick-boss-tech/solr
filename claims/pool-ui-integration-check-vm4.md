# Claim: pool-ui-integration-check-vm4

- Claimant host: vm4.
- Status: LIVE.
- Job or assignment: no assignment row. Follows the grouped-gate proposal in `reports/workflow-settlement-ui-gates-round-1.md` (commit 10c93f131a). Not a gate of any ticket: this checks whether the seven gated vm6 branches combine.
- Slice: merge `solr-17850-search-handler-form`, `solr-17887-file-tree`, `solr-17909-breadcrumb-wrap`, `solr-17911-file-content-viewer`, `solr-17913-collection-details-card`, `solr-17914-create-collection-dialog` and `solr-17849-index-query-screen` onto base `c8ab71430f` in one integration branch, run `:shared:jvmTest` and `:shared:spotlessCheck` on the result. Resolve textual conflicts only. Do not change any branch's code.
- Capability tags used: ui, jvm-tests.
- Started: 2026-10-24T18:53:35Z (UTC).
- Lease: stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-integration-check-vm4@c8ab71430f.
- Constraints: no PR, no Jira or GitHub comment. Branch pushed to the fork only as `ui-integration-check-vm4` after the run. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-24T18:53:35Z
