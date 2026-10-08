# code-review: git channel for branch reviews

This orphan branch is the message bus between the review agents and the owner. It holds
review documents only. It never holds code, and nothing here is merged anywhere.

Only one branch is used: `code-review` on the fork (`origin`). Each agent works in its own
clone or worktree, and talks to the others through commits on this branch.

## Layout

| Path | Written by | Purpose |
| --- | --- | --- |
| `claims/<slug>.md` | the reviewer that takes the branch | "I am reviewing `<branch>`". Created once. |
| `reviews/<slug>.md` | the reviewer that claimed the branch | The review. Only its claimant edits it. |
| `replies/<slug>.md` | the owner | Answers and decisions on a review. |
| `inventory/*.tsv` | the owner or triage run | Per-branch triage numbers. Regenerated, not hand-edited. |

`<slug>` is the fork branch name with `/` replaced by `-`, for example
`ci/12543-exportwriter` becomes `ci-12543-exportwriter`.

## Protocol

1. **Claim.** `git fetch origin code-review`, then check that `claims/<slug>.md` does not
   exist. Create it (reviewer id, date, branch, head SHA), commit, and `git push origin
   HEAD:code-review`. A rejected push means another agent got there first: fetch, pick a
   different branch, and do not force.
2. **Review.** Read-only on the source. Use `git diff upstream/main...origin/<branch>`
   (three dots, from the merge-base). Write `reviews/<slug>.md` with the template below.
   Mark every claim as **verified** (checked against the code path) or **hypothesis**.
3. **Publish.** Commit the review and push. One review per commit so history stays readable.
4. **Reply.** The owner answers in `replies/<slug>.md`. Reviewers do not edit replies.
5. **Release.** Remove the claim only if the review is abandoned, in its own commit.

## Hard limits (every agent)

- No Gradle, no `drain`, no test runs. Nothing here is compiled or executed. Say so in each review.
- No pushes to any branch except `code-review`. No force, rebase, or squash.
- No GitHub or JIRA writes (comments, PRs, reviews). Those need the owner's explicit approval.
- No secrets in commits. Tokens live in `gh_token.env` and `jira_token.env`; never copy them.
- Commit author is the ICLA identity used by the workspace; do not add Co-Authored-By trailers.

## Review template

```
# <branch>

- Branch: origin/<branch>
- Head: <short sha>
- Base: upstream/main at <short sha> (merge-base <short sha>, N commits behind)
- Scope: <files and commits>
- Verdict: Not ready | Needs work | Close | Nearly | Ready for review
- Reviewer: <id>, date

## Findings (ranked)
HIGH / MEDIUM / LOW: one line each, with file and symbol. Tag as verified or hypothesis.

## Not checked
What was not run or read, and why.
```
