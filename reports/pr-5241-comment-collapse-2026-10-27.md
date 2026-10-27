# PR 5241 comment formatting update, October 27, 2026

- User request: make the Linux verification section collapsible with summary/detail tags.
- Comment edited: https://github.com/apache/solr/pull/5241#issuecomment-6435404327
- Updated UTC: 2026-10-27 06:34:58 (02:34:58 America/New_York).
- Change: insert `<details>` with `<summary>Linux verification</summary>` immediately before the verification section, and close it before the contributor's merge commands. It is collapsed by default. All existing text, evidence links, disclosure, and commands are preserved.
- Read the actual live comment and PR discussion before preparing the draft. PR was OPEN at `aa8272ef089c39df449c519551ef439557b876f8`, with only the existing Nick comment and no reviews.
- Reviewed the local diff, which contains only the wrapper and its blank lines. Edited the existing comment through the issue-comment PATCH endpoint; did not post another comment or change the PR body.
- Separate GET read-back matches `PR-5241-comment-collapsed-draft.md` after CRLF normalization only. Saved `PR-5241-comment-collapsed-readback.json` and `PR-5241-comment-collapsed-posted.md`. The original pre-edit comment is preserved in `PR-5241-comment-before-collapse.md` and `.json`.
- No source, proof result, branch head, test execution, or contributor action changed.
