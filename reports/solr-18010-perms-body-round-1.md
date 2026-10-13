# SOLR-18010 description update for the permission fix, round 1

Done. The live PR description and `pr-drafts/core-admin/SOLR-18010.md` now match, and both describe head `2b5ad801c9e`. The PR is still a draft and has not been marked ready.

## What changed

- The head and its verification date: `2b5ad801c9e`, verified 2026-10-13, from fresh JUnit XML.
- The permission step is described in "What this change does": the existing permissions are copied to the new file before the move, and the copy is skipped or fails where POSIX permissions are not available.
- The Proof: SecurityConfHandlerTest is 4 of 4, with the new permissions test linked. The fail-before result is stated: with the permission step reverted, the new test fails with 0644 observed where 0640 was expected.
- Limits: the owner-only temporary-file bullet is removed, because the permissions are now kept. The Windows bullet stays.
- Every code link moved to `2b5ad801c9e`. The line ranges were checked at that SHA. The merge-base links are unchanged.

## Checks

- The gate record is `receipts/SOLR-18010.md`, the permission re-gate section: GREEN at `2b5ad801c9e`, with the counts and the fail-before run in that section.
- The live body was read back and matches the draft.
- No dashes, and no process words in the PR text. Visible length is about 3,500 characters, counted with link URLs stripped.

## Owner items

1. The Choice section is absent. The permission change is a behavior change a maintainer could reject, so you may want a choice section for it.
2. The Limits do not say the permissions test is skipped on Windows. Adding that would take the text over the guide.

## Note on a subagent

One subagent attempted a write to a path outside the repository while checking the formula. The write was denied and no file was created. It has no effect on the record.

## Not done

- No PR opened, closed, marked ready, or commented on.
- No build, test, or gate run on this host.
