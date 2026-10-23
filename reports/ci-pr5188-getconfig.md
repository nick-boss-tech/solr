# Report: PR 5188 GetConfigTest CI failure (phone agent, vm1)

Assignment: `assignments/pool-ci-pr5188-getconfig.md`, claimed by the phone agent at the owner's direction (`claims/pool-phone-agent-ci-pr5188-getconfig.md`) and run on vm1 tooling (solr-gradle.sh under the vm1 queue lock; scratch worktrees under ~/workspace/solr-worktrees/). All runs use the exact CI seed `47D7DB78A5A00E95` and the full `GetConfigTest` class; counts are from fresh JUnit XML.

## Result: not reproduced on any of the three trees

| Leg | Tree | Tests | Skipped | Failures | Errors |
| --- | --- | --- | --- | --- | --- |
| PR head | `54c8ae098eaddfcb22f03e0294947965a8db9b02` | 14 | 0 | 0 | 0 |
| Assignment base | `c8ab71430f6e7547c773da596101d680862310ff` | 9 | 0 | 0 | 0 |
| Merge of the head with current `origin/main` | `77b71382eef` (merge made for this check) | 16 | 0 | 0 | 0 |

Logs: `~/workspace/tools/ci5188-head.log`, `~/workspace/tools/ci5188-base.log`, `~/workspace/tools/ci5188-merge.log` on vm1. The assignment asked for the failing assertion and the map keys seen; there was no failure on this host, so there are no keys to record.

## What the failure was

The CI tree is the PR merged with main at run time, and its line numbers point at main's newer test, not the PR head's. In the merged tree, the assertions at lines 65 to 66 are the `updateProcessor` pair: absent as `updateProcessor`, present as `updateProcessors`. The archaeology:

- `8576dc8974a` (2026-10-20 17:16 -0400, PR #5174) pluralized the remaining config sections and made the test assert `updateProcessors`.
- The full-config rename map in `GetConfig.java` did not yet cover that key, so the merged test failed on the `updateProcessor` assertions. `b7518ba9182` (2026-10-20 21:04 -0400, PR #5187, "Fixed failing tests") added the missing entry (`GetConfig.java` line 106 in the merged tree).
- PR #5188's head commit is dated 2026-10-21 00:40Z, inside the window between those two main commits (21:16Z to 01:04Z), and the fix is not in the PR's ancestry. A CI run triggered by that push merged against the broken main and inherited its failure.

Two corrections to the assignment's context follow from the code. The rename (`renameToExternalFullConfigNames`) is not Smiley's change; it landed on main in the SOLR-16396 series (#5175, then #5174/#5187). And Smiley's diff does not touch `GetConfig` or `GetConfigTest` at all; it changes `SolrReturnFields` and its tests.

## Likely cause, in one sentence

The CI failure was main's own roughly four-hour red window between #5174 asserting the plural `updateProcessors` key and #5187 teaching the full-config rename map that key, and PR #5188's CI merge inherited it; nothing in the PR causes it, and current main already carries the fix.

## Process notes

- Both assigned runs were delayed by the queue lock being held by a leaked proxy-relay descriptor (26 minutes for the head leg's chain); cleared by hand. The new queue-lock-relay-guard hook missed that episode for a state-keying reason that has since been fixed and re-verified.
- No push to the PR branch, no comment on the PR, no production code changed. The scratch worktrees were removed after this report was posted; the logs stay on vm1.
- Whether anything is said to the PR author stays with the owner, per the assignment. On this evidence there is nothing to say: the failure is explained, already fixed upstream, and a fresh CI run on the PR would merge against the fixed main.
