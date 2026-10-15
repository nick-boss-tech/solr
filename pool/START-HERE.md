# Start here: posting and claiming pool work

This page is the order of operations for any agent that posts assignments or gate jobs, or claims them, on this branch. The rules are in [WORKFLOW.md](../WORKFLOW.md). The roles are in [material/coordinator-handoff.md](../material/coordinator-handoff.md). The templates are in [pool/templates/](templates/).

## Two rules that cause most failures

1. A handoff is a file on this branch. If it has no path on `pr-prepare`, no other agent can see it. Chat messages and local notes are not handoffs.
2. Push only as a fast-forward: fetch, rebase, push. Never force. Never reset a worktree that holds commits you have not pushed.

## Before you post or claim

Check the fork, not your local copy:

- Head: `git ls-remote origin refs/heads/<branch>`. Put that exact sha in the job.
- Existing gate: `git ls-tree --name-only origin/pr-prepare -- gates/`. Look for `<TICKET>.md`.
- Existing claims: `git ls-tree --name-only origin/pr-prepare -- claims/`. Look for names with the ticket number. Read any claim that has no `DONE:` line.

If the job or claim name already exists, stop. Another session got there first, and the first push wins.

## Post a gate job or an assignment

1. Copy the matching template from `pool/templates/` to a scratch file. Fill every field, keep every heading, and delete the sample text.
2. Check that the file has no em dashes and no en dashes. Write any instruction for a later fill-in as words, not as an angle-bracket placeholder: the helper refuses every unfilled placeholder, and it leaves Java generics such as `Comparator<ShardDoc>` alone.
3. Post it:
   - Windows: `research\pool-post.ps1 -Kind gate -Name SOLR-NNNN -File <scratch file>`, or `-Kind assignment -Name <slug>`. The script runs the checks in the next section, then commits and pushes.
   - Any host, by hand:
     1. `git fetch origin pr-prepare`
     2. Make a worktree at the fork tip with `git worktree add --detach <your path> origin/pr-prepare`. Use your own path. Do not share a checkout with another session.
     3. Copy the file to `gates/<TICKET>.md` or `assignments/<slug>.md` in that worktree.
     4. `git add` that file, then commit as Nick Shanin with no trailers: `git -c user.name="Nick Shanin" -c user.email="nick.boss.us@gmail.com" commit -m "<TICKET>: post gate job at <short head>"`.
     5. `git push origin HEAD:refs/heads/pr-prepare`. If the push is rejected, run `git fetch origin pr-prepare`, `git rebase origin/pr-prepare`, and push again.
     6. Read it back: `git show origin/pr-prepare:gates/<TICKET>.md`.

## Claim a gate job

Copy `pool/templates/claim.md` to `claims/pool-<host>-gate-backlog-<ticket number>.md`, fill it, and post it with the same steps (`-Kind claim -Name pool-<host>-gate-backlog-<ticket number>` on Windows). Claim before you run anything. Append a heartbeat line at least once an hour and push it. Mark the claim `DONE:` in the same push as the deliverable.

## Decisions in a job

Every gate job and assignment has a Decisions table. Each row is one of two things:

- **Decided.** The answer is in the row. The claimant applies it and does not ask again.
- **Default with a condition.** The row says what to do if the condition happens, for example "if the payload fetch failure appears, record it and stop."

A row must never leave the choice to the claimant. If a decision is needed and nobody has made it, say "escalate to the main agent" on that item, stop that item, and do not guess.

## What the Windows helper checks

`research\pool-post.ps1` refuses to post when:

- the file is missing, has an em dash or en dash, or lacks a required heading or field;
- the job or claim name already exists on the fork;
- a gate's head is not the current tip of its branch;
- a claim for the same ticket has no `DONE:` line;
- the worktree has uncommitted changes or commits that are not on the fork.

It then commits as Nick Shanin, pushes fast-forward, rebases and retries if the push is rejected, and reads the file back from the fork.

## Hosts

Linux hosts cannot run the PowerShell helper. Use the manual steps above. Windows posts and reviews. It never claims build work (`gate`, `implementation`, `premise-run`, `settling-run`, `sweep`, `bats`).

## Public text

This branch is public on the fork. Write everything here as publishable. Keep tokens, credentials and private security material out. Commit messages name ticket keys only, with no PR numbers.

## When something does not fit

If a rule is unclear or two records disagree, say so in the job or claim file and escalate to the main agent. Do not work around a refusal by force-pushing, and do not edit another session's job to make your post fit.
