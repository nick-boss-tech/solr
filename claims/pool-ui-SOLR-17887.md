# Claim: pool-ui-SOLR-17887

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, one ticket per claim.
- Ticket type: component (file tree). The previous claim was a dialog (SOLR-17914), so complaint 5 is met.
- Slice: SOLR-17887, gate only. Two fork branches exist. `solr-17887-file-tree` (`9c7f72aa0e`, from base `c8ab71430f`) is the standalone component: `FileTree.kt`, `FileTreeTest.kt`, two drawables, and a draft PR description. `solr-17887-configset-files` (`80e97a1e8e`) is an integration branch that also carries other tickets' work (39 files), so it is not the one gated here. This claim verifies the standalone branch on a fresh checkout. No code change, no push.
- Capability tags used: ui, jvm-tests.
- Started: 2026-10-24T09:37:38Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17887@9c7f72aa0e.
- Links: Ticket: SOLR-17887 (parent SOLR-17886). Category: ui. Siblings: `claims/pool-ui-SOLR-17913.md`, `claims/pool-ui-SOLR-17914.md` (DONE). Supersedes: none.
- Decision ladder: rung 1 for the branch contents. GL: read in the Result.
- Constraints: no JIRA or GitHub post, no PR. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-24T09:37:38Z

Run: job `pool-ui-17887-9c7f72aa0e-094238`, log dir `/workspace/gates/logs/vm6/pool-ui-17887/`, head `9c7f72aa0e` (fork branch, not pushed by this claim), started 2026-10-24T09:42:47Z (UTC). Steps: spotless, then `:shared:jvmTest --tests org.apache.solr.ui.views.files.FileTreeTest`.

Heartbeat: 2026-10-24T09:42:47Z
