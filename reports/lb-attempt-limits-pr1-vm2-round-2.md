# Report: LB attempt limits, PR 1, vm2 gate round 2

Status: gate green at local head 28dbd2edfce; fork publication blocked. Full details are in the receipt, `receipts/lb-attempt-limits-pr1.md`.

## Decision needed

The gated head is not on the fork. The fork's live tip is 7a226ef, and the repairs are two commits that exist only in vm2's local repository:

- bb3876a: formatting-only tidy changes.
- 28dbd2e: javadoc qualification that fixes an ECJ lint failure in `LBAsyncSolrClient.java`.

GitHub rejected the fork push of bb3876a with `Password authentication is not supported for Git operations`, though the same token reads the fork and writes the board. The series applies cleanly to 7a226ef and replays to the same tree as 28dbd2e. The patch is `drafts/lb-attempt-limits-pr1-repair-series.mbox` on the board.

To finish:
1. Apply the series to `solr-lb-attempt-limits-pr1-submit` (author's push, or a fork push once the credential works).
2. Re-run the tidy and focused preflight at the new fork tip, or accept 28dbd2e as the proven head if the fork tip matches exactly.

## Summary

- Discrimination: the four named methods fail on the parent for the stated behavioral reasons.
- Focused: 47 tests at both fixed heads, zero failures, errors, and skips.
- Root check: `check -x test` passes at 28dbd2e (rc 0) in a full clone; the worktree attempts failed at Antora, which needs a `.git` directory.

## Not done

- No GitHub, PR, or Jira write. The changelog is not added, per AGENTS.md (no number yet).
- PRs 2 and 3 untouched.
