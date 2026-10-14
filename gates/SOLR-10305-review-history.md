# Gate job: SOLR-10305 review finding, public commit history carries handoff-doc commits

- Branch: solr-10305-submit. Head: `f559126972153367a846ef2a6a1989906849e403` (live head of PR #5151, read 2026-10-14). The job applies to that head only.
- Merge-base with upstream main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Filed by: the slice 6 review. The review did not push and did not commit.
- Status: OPEN. Needs Nick's direction before any push.

## Finding answered

The commit list of PR #5151 shows public subjects that name an internal handoff doc: `c21ca8c0e75` ("SOLR-10305: add hypothetical-reproduction handoff doc") and `0cf26e5f331` ("SOLR-10305: remove handoff doc"). The net diff against the merge-base has no handoff file, so the tree is clean. The problem is only the history a maintainer reads on the PR. No earlier round recorded this history for this branch.

## Patch

Rebuild `solr-10305-submit` from the merge-base so that:

- the tree at the new head is byte-identical to the old head, checked with `git diff --exit-code f559126972153367a846ef2a6a1989906849e403 <new-head>` (must print nothing);
- no commit subject names a handoff doc, a hypothetical reproduction, a gate, or a receipt.

Either one squashed commit with a plain subject, or the non-handoff commits re-subjected with the same content. The changelog author stays Nick Shanin, with no co-author trailer.

## Push rule

This is a history rewrite on the head of a public draft PR. Do not push without Nick's explicit direction for PR #5151. When directed, push with the lease wrapper, pinned to the old head:

`.\research\push-fork-lease.ps1 -Worktree 'C:\Users\shaninna\dev\Solr-issues\wt\SOLR-10305' -Branch 'solr-10305-submit' -ExpectedSha 'f559126972153367a846ef2a6a1989906849e403'`

Record the old and new SHA in the gate file. After the push, re-point the PR body's blob links from `f559126972` to the new head, read the body back, and re-review.

## Gate shape

1. Changelog YAML parse of `changelog/unreleased/SOLR-10305-distributed-query-requires-unique-key.yml`.
2. Tidy for `solr/core`, with the versions-file restore. The diff against the old head must be empty.
3. Error Prone compile: `-Pvalidation.errorprone=true :solr:core:compileTestJava`.
4. Pre-fix proof leg: on base `cabedd1d968059215188f4e7563fb303241899ed`, with the branch tests overlaid, `MinimalSchemaTest` reports exactly one failure, `testDistributedQueryWithoutUniqueKeyIsBadRequest`, from fresh JUnit XML.
5. Focused run at the new head, from fresh JUnit XML: `MinimalSchemaTest` 4 of 4.
6. Module check: `:solr:core:check` for the test file's module passes, as the receipt's module check does.

Because the tree is identical, the old-head results carry over only if the identity check passes. Record that check in the gate file.

## Outcome, 2026-10-14

The rewrite was executed under Nick's direction: branch rebuilt from the merge-base with the handoff doc pair dropped, tree byte-identical to the old head (see the ticket receipt for both heads and the tree hash), and the branch now stands at the rewritten head. During the run a malformed push deleted the fork branch and GitHub closed the PR; it refuses to reopen a PR whose head branch was recreated, so the PR stays closed and a replacement draft opening from the same branch awaits Nick's approval. This job's push and body steps are therefore complete in substance on a closed PR; no further action here.
