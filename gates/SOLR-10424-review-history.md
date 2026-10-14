# Gate job: SOLR-10424 review finding, public commit history carries handoff-doc commits

- Branch: solr-10424-submit. Head: `e629ab8bb293eb54249b426b69a559f1da0f08d7` (live head of PR #5152, read 2026-10-14). The job applies to that head only.
- Merge-base with upstream main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Filed by: the slice 6 review. The review did not push and did not commit.
- Status: OPEN. Needs Nick's direction before any push.

## Finding answered

The commit list of PR #5152 shows public subjects that name an internal handoff doc: `06de62e5594` ("SOLR-10424: add hypothetical-reproduction handoff doc") and `b48f4053430` ("SOLR-10424: remove hypothetical-reproduction handoff doc"). The net diff against the merge-base has no handoff file, so the tree is clean. The problem is only the history a maintainer reads on the PR. No earlier round recorded this history for this branch.

## Patch

Rebuild `solr-10424-submit` from the merge-base so that:

- the tree at the new head is byte-identical to the old head, checked with `git diff --exit-code e629ab8bb293eb54249b426b69a559f1da0f08d7 <new-head>` (must print nothing);
- no commit subject names a handoff doc, a hypothetical reproduction, a gate, or a receipt.

Either one squashed commit with a plain subject, or the non-handoff commits re-subjected with the same content. The changelog author stays Nick Shanin, with no co-author trailer.

## Push rule

This is a history rewrite on the head of a public draft PR. Do not push without Nick's explicit direction for PR #5152. When directed, push with the lease wrapper, pinned to the old head:

`.\research\push-fork-lease.ps1 -Worktree 'C:\Users\shaninna\dev\Solr-issues\wt\SOLR-10424' -Branch 'solr-10424-submit' -ExpectedSha 'e629ab8bb293eb54249b426b69a559f1da0f08d7'`

Record the old and new SHA in the gate file. After the push, re-point the PR body's blob links from `e629ab8bb293` to the new head, read the body back, and re-review.

## Gate shape

1. Changelog YAML parse of `changelog/unreleased/SOLR-10424-techproducts-json-docs.yml`.
2. Tidy for `solr/core`, with the versions-file restore. The diff against the old head must be empty.
3. Error Prone compile: `-Pvalidation.errorprone=true :solr:core:compileTestJava`.
4. Pre-fix proof leg: on base `cabedd1d968059215188f4e7563fb303241899ed`, with only `TechproductsJsonDocsParamsTest.java` overlaid, the test reports exactly one failure, the `mapUniqueKeyOnly` absence assertion, from fresh JUnit XML.
5. Focused run at the new head, from fresh JUnit XML: `TechproductsJsonDocsParamsTest` 1 of 1.
6. Module check: `:solr:core:check` for the test's module passes.

Because the tree is identical, the old-head results carry over only if the identity check passes. Record that check in the gate file.
