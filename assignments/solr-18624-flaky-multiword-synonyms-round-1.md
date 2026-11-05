# Assignment: SOLR-18624 publish the prepared multiword-synonym test branch, round 1
Status: OPEN
Posted 2026-11-04 by the phone agent at Nick Shanin's direction. Claim before work.
Tags: review, records
Intended host: any available pool Linux host holding those tags and GitHub push access.
Authority: Nick authorizes publishing the exact prepared commit below as a normal, non-forced push to `nick-boss-tech/solr:solr-18624-flaky-multiword-synonyms`. This authorizes branch publication only. It does not authorize code edits, a rebase or cherry-pick, opening a PR, or any Jira/GitHub comment or review submission.
Target: [SOLR-18624](https://issues.apache.org/jira/browse/SOLR-18624).

## Prepared local source
- Worktree: `/workspace/worktrees/testpf-flaky-rewrite/solr`.
- Local branch: `solr-18624-flaky-multiword-synonyms`.
- Base: `apache/main` at `07ccc73cd24e26e59da169ca35b3fd5f64c35986`.
- Head: `c610e6d05db5700835a123de54da61137478e48f`; its parent is exactly the base above.
- Commit: `SOLR-18624: Stabilize TestMultiWordSynonyms.testPf assertions`.
- The prepared branch is one commit ahead; its only changed path is `solr/core/src/test/org/apache/solr/search/TestMultiWordSynonyms.java` (23 insertions, 7 deletions). The source worktree was clean when checked for this assignment.
- The fork's target branch was absent when this assignment was prepared. Recheck before publishing. No automated test result is asserted or requested by this publication-only assignment.

## Work
1. Claim this assignment before work. From the source worktree, verify that the named local branch is clean, has the exact base and head above, and changes only the listed test file. If that worktree/branch is unavailable or any value differs, stop without reconstructing or changing the commit; record the blocker and hand back.
2. Recheck `nick-boss-tech/solr`'s target branch. If absent, publish the exact head to `refs/heads/solr-18624-flaky-multiword-synonyms` with a normal push. If already at the exact head, report it as already published. If it points anywhere else, stop; do not overwrite or force-push.
3. Read back the remote ref and confirm it is exactly `c610e6d05db5700835a123de54da61137478e48f`. The expected outcome is a reviewable fork branch at that exact head, based on the stated upstream `main` commit—not a pull request.
4. Add `reports/solr-18624-publish-branch-round-1.md` with the source/base/head, remote ref read-back, branch URL, changed-path check, and whether the branch was newly published or already present.

## Boundaries and deliverables
- No source edits, commit amendments, rebases, cherry-picks, tests, gate, receipt, PR, Jira write, GitHub comment, or review submission.
- Deliver the exact commit on the named fork branch and the verification report above. Do not edit this assignment or generated indexes.
