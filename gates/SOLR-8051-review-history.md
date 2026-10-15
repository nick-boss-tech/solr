# SOLR-8051 review gate: handoff commits in branch history

- Branch: solr-8051-submit (fork). Live PR: apache/solr #5143, draft.
- Applies to head: e50a2aa3437cb14ce4310a5025ad783fc93a97b1
Status: DONE

## Finding answered

Two commits on the PR's commit list name an internal process document:

- 44588ce6719e1cedc126a55ccf22044cbe1f4973 "SOLR-8051: add hypothetical-reproduction handoff doc"
- 55b24f64a8bd9cc29bdebd993530c8774f3695cd "SOLR-8051: remove handoff doc"

The net diff against the merge-base (cabedd1d968059215188f4e7563fb303241899ed) has three files, all in ticket scope, and no handoff file. Only the commit messages carry the process term.

## Proposed change

Rebuild the branch without the add and remove pair. Cherry-pick, in order: 431961dba7559d87346d5fd7ff16af6bdeb95d29 (skip shard), 4fb691e8543 (changelog), e50a2aa3437cb14ce4310a5025ad783fc93a97b1 (null check). The last commit gets a new SHA on the rebuilt branch, which becomes the new head. Skip 44588ce6719 and 55b24f64a8b. Before any push, require `git diff e50a2aa3437cb14ce4310a5025ad783fc93a97b1 <new-head>` to be empty.

The result is a force push to the head of an open PR. That needs Nick's direction for PR #5143 (AGENTS.md, Maintainer Interaction). If he approves, use `research\push-fork-lease.ps1 -Worktree <wt> -Branch solr-8051-submit -ExpectedSha e50a2aa3437cb14ce4310a5025ad783fc93a97b1`.

The commit e50a2aa3437c is the final commit; the rebuild keeps its message and tree. Do not change its message unless the gate says so.

## Gate shape

If the tree is identical, the re-gate record at e50a2aa3 still describes the content. Otherwise run the full shape: changelog YAML parse; tidy with the versions-file restore; Error Prone compile; pre-fix proof legs; focused ExactStatsCacheMergeTest counted from fresh JUnit XML; module check :solr:core.

## Outcome, 2026-10-14

The rewrite was executed under Nick's direction: branch rebuilt from the merge-base with the handoff doc pair dropped, tree byte-identical to the old head (see the ticket receipt for both heads and the tree hash), and the branch now stands at the rewritten head. During the run a malformed push deleted the fork branch and GitHub closed the PR; it refuses to reopen a PR whose head branch was recreated, so the PR stays closed and a replacement draft opening from the same branch awaits Nick's approval. This job's push and body steps are therefore complete in substance on a closed PR; no further action here.
