# Report: PR 5287 follow-up comment, round 1

Status: POSTED and read back.

- PR: https://github.com/apache/solr/pull/5287
- Comment: https://github.com/apache/solr/pull/5287#issuecomment-6653195270
- Posted: 2026-11-05T08:18:53Z.
- Pre-post and read-back PR state: OPEN, head `19f7c83812a4c81227601ff1abadca84beb05cb9`, base `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- Offered fork branch: `nick-boss-tech/solr:pr-5287-configset-source-validation`, head `d0cf2471f37ae973ca06390ec195cf51f8fccbb3`, a fast-forward from the current PR head.
- Proof source: `receipts/PR-5287-configset-upload-hardening-round-2.md`, GATE PASSED at the offered head. LocalFS, core API, and GCS suites passed; all 10 S3 tests skipped because no Docker host was available. The comment states this limit and the checksum-repaired root-check rerun.
- Duplicate check: before posting, the live PR had the two original discussion comments and no equivalent patch offer.
- Read-back: `gh api repos/apache/solr/issues/comments/6653195270 --jq .body` matched `research/scratchpad/PR-5287-comment-draft.md` exactly. Post-read PR view retained the same head and showed the new comment.
- Pinned diff: https://github.com/nick-boss-tech/solr/compare/19f7c83812a4c81227601ff1abadca84beb05cb9...d0cf2471f37ae973ca06390ec195cf51f8fccbb3

No PR body edit, review, approval, merge, or JIRA comment was made.
