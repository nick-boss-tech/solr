# Gate job: SOLR-9864 review finding, public commit history carries handoff-doc commits

- Branch: solr-9864-submit. Head: `b5826e466b9a8664adf11e0f247cff220f2f5953` (live head of PR #5150, read 2026-10-14). The job applies to that head only.
- Merge-base with upstream main: `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.
- Filed by: the slice 6 review. The review did not push and did not commit.
- Status: CLOSED 2026-10-14 by owner ruling (see Closure at the end of this file). Nick's direction arrived and it is the other way: the rewrite is cancelled and the branch stays as it stands. Not run, and not to be run.

## Finding answered

The commit list of PR #5150 shows two public subjects that name an internal handoff doc: `f7d6028ddde` ("SOLR-9864: add hypothetical-reproduction handoff doc") and `b5826e466b9` ("SOLR-9864: remove the hypothetical-reproduction handoff doc"). The net diff against the merge-base has no handoff file, so the tree is clean. The problem is only the history a maintainer reads on the PR. No earlier round recorded this history for this branch. The round that did (`reports/search-components-1-f2.md`, Finding 3) covers SOLR-10492, 11129 and 6193 only.

## Patch

Rebuild `solr-9864-submit` from the merge-base so that:

- the tree at the new head is byte-identical to the old head, checked with `git diff --exit-code b5826e466b9a8664adf11e0f247cff220f2f5953 <new-head>` (must print nothing);
- no commit subject names a handoff doc, a hypothetical reproduction, a gate, or a receipt.

Either one squashed commit with a plain subject, or the non-handoff commits re-subjected with the same content. The changelog author stays Nick Shanin, with no co-author trailer.

## Push rule

This is a history rewrite on the head of a public draft PR. Do not push without Nick's explicit direction for PR #5150. When directed, push with the lease wrapper, pinned to the old head:

`.\research\push-fork-lease.ps1 -Worktree 'C:\Users\shaninna\dev\Solr-issues\wt\SOLR-9864' -Branch 'solr-9864-submit' -ExpectedSha 'b5826e466b9a8664adf11e0f247cff220f2f5953'`

Record the old and new SHA in the gate file. After the push, re-point the PR body's blob links from `b5826e466b9a` to the new head (the body cites the head SHA in its links), read the body back, and re-review.

## Gate shape

1. Changelog YAML parse of `changelog/unreleased/SOLR-9864-solrquery-getcopy-sorts.yml`.
2. Tidy for `solr/solrj`, with the versions-file restore. The diff against the old head must be empty.
3. Error Prone compile: `-Pvalidation.errorprone=true :solr:solrj:compileTestJava`.
4. Pre-fix proof leg: on base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`, `SolrQueryTest` reports exactly one failure, `testGetCopyKeepsSortClauses`, from fresh JUnit XML.
5. Focused runs at the new head, from fresh JUnit XML: `SolrQueryTest` 17 of 17, `QueryRequestTest` 5 of 5, `TestUpdateRequest` 5 of 5.
6. Module check: `:solr:solrj:check` passes.

Because the tree is identical, the old-head results carry over only if the identity check passes. Record that check in the gate file.

## Note

Combine this rewrite with the javadoc move in `gates/SOLR-9864-review-javadoc-placement.md` if Nick directs both, so the PR moves once. (The javadoc move landed on its own at 176ed67491b5; the rewrite half is cancelled under the Closure below.)

## Closure (owner ruling, 2026-10-14)

CLOSED. Nick ruled on 2026-10-14 (~17:21 MDT) that this rewrite does not happen: "it seems like we should just keep what we have next time instead of rewriting, I guess the commits are not what matters but the final PR is." The branch keeps the add and remove pair (f7d6028ddde and b5826e466b9) in its history exactly as it stands; the javadoc-placement commit 176ed67491b50fef0669d629341ed05db4b2e700 that landed on top stays too, so the live head is 176ed67491b5 (verified by ls-remote 2026-10-14). No force push, no rebuild, no re-gate for history reasons. Standing form of the ruling: do not rewrite a branch's history for cosmetic commit-message defects; keep the branch and let the PR carry the content.
