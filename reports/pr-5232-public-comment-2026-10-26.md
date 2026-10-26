# PR 5232 follow-up comment posted, October 26, 2026

Status: POSTED; derived branch Linux proof GREEN; original PR awaits contributor response.

- Authorization: Nick's direct request in this chat to post a PR comment including the fix diff and Git commands to pull in the changes.
- Posted by `nick-boss-tech` at `2026-10-26T18:04:57Z` (2:04:57 PM America/New_York).
- Comment: https://github.com/apache/solr/pull/5232#issuecomment-6423638209
- Comment ID: `6423638209`.
- Draft: `research/branch-reviews/2026-10-26-pr-batch/PR-5232-comment-draft.md` in the Windows workspace.
- Separate GitHub comment GET confirmed the stored body matches the draft. The author, creation time, diff link, seed, test counts, disclosure, and three Git commands were checked.
- Original PR head, confirmed before and after posting: `2c9243b111b38367677d7e0c9b30b8e72c554d2c`, open.
- Fork fix head, independently read from the live ref: `23c68215cf0c858b7dd1ceda6b945d2d9daaa174` on `nick-boss-tech/solr:solr-11994-submit`.
- Diff: https://github.com/nick-boss-tech/solr/compare/2c9243b111b38367677d7e0c9b30b8e72c554d2c...23c68215cf0c858b7dd1ceda6b945d2d9daaa174
- GitHub comparison confirms exactly two follow-up commits and the original PR head as merge base: implementation `b3a11484f13feaab55fe4f27504bfce3d1bdd2ef`, then formatting repair `23c68215cf0c858b7dd1ceda6b945d2d9daaa174`.

## Proof used in the comment

Read `receipts/PR-5232-listener-followup.md` both at current board snapshot `1dd98fc94d4c70da467cc6ef2fe88f40de8367dd` and at the receipt commit `554aa52a0c8193e383b1936246dd203aebe512e1`. The receipt names the exact live fork head and reports:

- Focused classes: 17 tests, zero failures/errors/skips, including executed `testCpuTime`.
- Neighbor classes: 4 tests, zero failures/errors/skips.
- Clean tidy and successful root `check -x test` with `NODE_ENV` unset for the successful relaunch. The initial Antora failure caused by the host's `NODE_ENV=production` is disclosed in the posted comment.
- Original PR production code plus new tests: all three discriminators fail on the intended assertions, CPU 14 instead of 7 and subquery event counts 4 and 2 instead of 1.

The comment links the pinned verification details and identifies these as focused and neighboring test results. It does not claim a full repository test run. No new tests were executed on Windows, and the host-wide NODE_ENV configuration was not changed.

## Commands offered to the contributor

```sh
git switch SOLR-11994-search-request-listener
git fetch https://github.com/nick-boss-tech/solr.git solr-11994-submit
git merge --ff-only 23c68215cf0c858b7dd1ceda6b945d2d9daaa174
```

These commands take the two follow-up commits above the current PR head without rewriting history. They were provided as instructions to the contributor and were not executed against the contributor's branch.

No PR was opened, no PR body was edited, and no JIRA write occurred. The original PR still points at its original head; the fixes are proposed through this comment and remain on Nick's fork. Wait for contributor or maintainer response before another public update.
