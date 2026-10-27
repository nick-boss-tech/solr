# PR 5206 review fixes and Linux gate handoff, round 1

Status: IMPLEMENTED, PROOF PENDING
Host: windows, Codex desktop. Completed implementation: 2026-10-27T05:31:29Z.
Claim: `claims/pr-5206-review-fixes-round-1-windows.md` (board commit `47b35a4ad3d57392839aee3cc3eb948b6ec02153`).
Assignment: Nick's direct request to patch the reviewed test and send a gate assignment.
Board snapshot reviewed before the claim: `597d69da398f54eb0a796882a187670313f09e55`.

## Pushed change

- PR: https://github.com/apache/solr/pull/5206.
- Fork branch: `nick-boss-tech/solr:solr-18576-submit-apache-main`.
- Previous PR head O: `4c8f12f526f54e4e90a8ea49d46c1d28d899cb65`.
- Pushed head F: `6c6ff8d9a0e173c02b5a11f8ad3b13a59ebd36fa`.
- Commit: `SOLR-18576: Assert reconnection and always remove expiry listener`.
- Author and committer: Nick Shanin, `nick.boss.us@gmail.com`; no attribution trailers.
- Diff: https://github.com/nick-boss-tech/solr/compare/4c8f12f526f54e4e90a8ea49d46c1d28d899cb65...6c6ff8d9a0e173c02b5a11f8ad3b13a59ebd36fa.

Only `solr/core/src/test/org/apache/solr/cloud/BasicDistributedZk2Test.java` changed, with 12 additions and 7 deletions. The existing timed reconnection call is now asserted, with the message `client did not reconnect after session expiry`. Expiry and both checked waits are inside try/finally, ensuring listener removal on exceptions, assertion failures, or interruption. Both 30-second timeouts are preserved. Registration stays before expiry and indexing stays after cleanup.

F was committed in an isolated sparse ticket worktree and pushed as a normal fast-forward from O. The push helper checked the expected old ref and ancestry, refused force behavior, and verified F remotely. A separate GitHub ref read and live PR read both confirmed the full SHA F. The live PR had no comments or reviews at that verification. The PR head moved; its old receipt and description's proof lines still refer to O.

## Verification and limitations

The scoped diff and outbound list were reviewed, `git diff --check` passed, the commit contains only the named test file, and the worktree is clean. No Gradle, formatting tool, compilation, test, or Windows queue was run. The original race remains a timing hypothesis without fail-before/pass-after reproduction; this change improves checking and cleanup without a production change.

The Linux gate request is `gates/pr-5206-reconnection-cleanup-round-1.md`, pinned to F. It requires tidy, Error Prone compile, the original failure seed `7E5494314774A841`, four random iterations, the root `check -x test`, final tidy and clean-tree checks, and final live fork/PR head verification. It explicitly distinguishes normal-path test runs from source inspection of timeout/interruption cleanup. A formatting repair requires a new pushed head and superseding gate before complete proof at that head.

The gate host writes `reports/pr-5206-reconnection-cleanup-round-1-gate.md` and a new phase receipt `receipts/SOLR-18576-pr5206-review-fixes-round-1.md`, preserving `receipts/SOLR-18576.md`. Older passing runs must not be used to mark F green. The follow-up stays Implemented, Proof Pending until the required Linux proof succeeds.

No GitHub/JIRA comment, live PR body edit, approval, merge, force-push, or production change was made. A fresh description draft is a gate deliverable; updating the live body remains a separately authorized action under the workspace instructions.

This report and the new gate job are the implementation claim's deliverables. The Linux gate remains separate open work for a capable host, with one gate per host under its build lock.
