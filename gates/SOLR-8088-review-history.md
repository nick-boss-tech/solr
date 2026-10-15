# SOLR-8088 review gate: handoff commits in branch history

- Branch: solr-8088-submit (fork). Live PR: apache/solr #5144, draft.
- Applies to head: 567efa9b78c32df3454ed431a40a04c06ed09d46
Status: DONE

## Finding answered

Two commits on the PR's commit list name an internal process document:

- 2398c9bea08528c4ccccf255d3bd1205ff9ee79a "SOLR-8088: add hypothetical-reproduction handoff doc"
- 567efa9b78c32df3454ed431a40a04c06ed09d46 "SOLR-8088: remove handoff doc" (current head)

The net diff against the merge-base (cabedd1d968059215188f4e7563fb303241899ed) has four files, all in ticket scope, and no handoff file. Only the commit messages carry the process term.

## Proposed change

Rebuild the branch without the add and remove pair. Keep, in order: c3c9f36855253eada0c3009098bfa87c68b15fb6 (reject), then 6863082aa7c (changelog). Skip 2398c9bea08 and 567efa9b78c. If the changelog gate (gates/SOLR-8088-review-changelog-scope.md) lands first, rebuild on top of its commit so the changelog change is kept. Before any push, require `git diff 567efa9b78c32df3454ed431a40a04c06ed09d46 <new-head>` to differ only in the changelog title line, or to be empty if the changelog gate has not landed.

The result is a force push to the head of an open PR. That needs Nick's direction for PR #5144 (AGENTS.md, Maintainer Interaction). If he approves, use `research\push-fork-lease.ps1 -Worktree <wt> -Branch solr-8088-submit -ExpectedSha 567efa9b78c32df3454ed431a40a04c06ed09d46`. (Superseded by the Closure below: he did not approve; he cancelled the rewrite.)

## Gate shape

If the tree is identical, the receipt at 567efa9 still describes the content. Otherwise run the full shape: changelog YAML parse; tidy with the versions-file restore; Error Prone compile; pre-fix proof legs; focused SearchGroupsFieldCommandTest counted from fresh JUnit XML; module check :solr:core.

## Closure (owner ruling, 2026-10-14)

CLOSED. Nick ruled on 2026-10-14 (~17:21 MDT) that this rewrite does not happen: "it seems like we should just keep what we have next time instead of rewriting, I guess the commits are not what matters but the final PR is." The branch keeps the add and remove pair in its history exactly as it stands; the changelog-scope commit c36cfc094af4 that landed on top of 567efa9b78c stays too, so the live head is c36cfc094af45ef3209f2eefa0aab7621407fae0 (verified by ls-remote 2026-10-14). No force push, no rebuild, no re-gate for history reasons. Standing form of the ruling: do not rewrite a branch's history for cosmetic commit-message defects; keep the branch and let the PR carry the content.
