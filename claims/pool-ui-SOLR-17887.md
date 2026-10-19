# Claim: pool-ui-SOLR-17887

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`.
- Slice: SOLR-17887, the reusable file tree component.
- Capability tags used: ui, jvm-tests.
- Branch: `solr-17887-file-tree` on the fork, commit 9c7f72aa0e5 on base apache/main c8ab71430f.
- Gate: `material/ui-test-setup/run-ui-jvm-tests.sh org.apache.solr.ui.views.files.FileTreeTest` passes 5 of 5; `:shared:spotlessCheck` passes.
- Draft PR: `pr-draft/SOLR-17887.md` on the branch.
- Blast radius: not flagged. Layout, click handling, and two chevron drawables only.
- Not checked visually: no screen hosts the component yet.
- Behavior note: single clicks report after the double-click window, so selection lags by that window.

Heartbeat: 2026-10-19T17:55:00Z
