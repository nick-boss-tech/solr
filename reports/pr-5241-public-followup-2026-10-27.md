# PR 5241 tested fix offered upstream, October 27, 2026

Status: POSTED AND READ BACK; awaiting contributor review.

- Public comment: https://github.com/apache/solr/pull/5241#issuecomment-6435404327
- Posted: 2026-10-27 06:31:24 UTC (02:31:24 America/New_York), author `nick-boss-tech`.
- Authorization: Nick's standing clean-gate PR follow-up authorization, after the explicit driver-property evidence gap was closed and all required proof, remote heads, ancestry, and public discussion were rechecked. No separate approval was requested.
- Live PR: OPEN, `epugh/solr:SOLR-16398-admin-ui-replication-details-v2`, head `aa8272ef089c39df449c519551ef439557b876f8`. No pre-existing comments, inline comments, or reviews at the final pre-post refresh.
- Offered fork: `nick-boss-tech/solr:solr-16398-submit`, exact remotely verified head `3b36f32e93d513c58ff14c48db303a296218f84a`.
- Git confirms the live PR head is an ancestor of the offered head. The comment includes the pinned original-to-offered diff and contributor branch switch/fetch/merge --ff-only commands.
- Evidence snapshot: `5736254fa050881d1e2d70638ccc54fd6a21042c` on coordination-ui. Receipt: `receipts/PR-5241-overview-errors.md`; gate: `gates/pr-5241-overview-errors-round-2-f3-vm4.md`; evidence closeout: `reports/pr-5241-round-2-evidence-closeout-vm4.md`.

## Proof accepted

At offered head `3b36f32e93`: 3 focused browser tests and 10 tests in five neighboring classes pass, with zero failures, errors, or skips. Tidy, forbiddenApisTest, JavaScript syntax, and whitespace checks pass. Root `check -x test` succeeds in a clean standalone clone at that exact SHA with NODE_ENV unset. The comment discloses the standalone-clone execution condition and the reproduced Antora worktree-layout failure; it does not present the failed attempt as a pass. Error Prone was not separately enabled and is not claimed.

Fail-before uses original PR production at `aa8272ef089c` plus only the final test file: all 3 methods fail at their intended 403 permission-warning, 503 server-message, and 401 login-form assertions, after successful healthy-start and injected-status waits. Existing screenshots, page source, console logs and fresh XML are retained by vm4.

The records-only closeout quotes actual test-worker Java launcher output for the chromedriver property: focused log line 170 after the test task at line 169; neighbor lines 90/91 after task line 89; fail-before line 191 after task line 190. This satisfies the explicit XML-or-log requirement, without a rerun. The earlier inference alone was not treated as sufficient. The closeout also provides test totals, fail-before messages, and the clean-clone check result.

Incomplete scratch-directory cleanup is not a proof defect: the log and artifact directories remain retained. No cleanup or extra tests were started here.

## Separate read-back

`verify-pr5241-comment.ps1` made a separate GET of `repos/apache/solr/issues/comments/6435404327` after posting. Stored body matches `PR-5241-comment-draft.md` after CRLF normalization only; comment author and URL match. Saved read-back: `PR-5241-posted-comment.json` and `PR-5241-posted-comment.md`.

A separate live PR read confirms the PR remains OPEN at `aa8272ef089c39df449c519551ef439557b876f8`, with exactly this one conversation comment and no reviews. No PR body edit, JIRA write, review approval, contributor-branch change, merge, or new PR was performed. The contribution is now awaiting a human response to the offered fix.
