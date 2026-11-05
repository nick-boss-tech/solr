# Report: PR 5287 diff count clarification

Status: POSTED and read back.

- PR comment: https://github.com/apache/solr/pull/5287#issuecomment-6653195270.
- Comment edit: 2026-11-05T08:37:09Z. The read-back body matches `research/scratchpad/PR-5287-comment-draft.md`; GitHub marks the comment as edited.
- PR head at read-back: `19f7c83812a4c81227601ff1abadca84beb05cb9` (unchanged).
- Offered fork head: `d0cf2471f37ae973ca06390ec195cf51f8fccbb3`.
- The GitHub compare API returns 8 changed files: the seven files from the original patch plus `gradle/testing/randomization/policies/solr-tests.policy`. Tidy formatting and LocalFS symlink-test setup fixes are modifications within files already counted.
- `gates/pr-5287-configset-upload-hardening-round-2.md` and `receipts/PR-5287-configset-upload-hardening-round-2.md` say 9 changed files. This is a count error in those records; they enumerate the original seven plus one additional policy file.
- The existing PR comment was edited in place to explain the eight-file scope. No duplicate comment was added.
