# Start here: posting and claiming pool work

This page is the order of operations for any agent that posts assignments or gate jobs, or claims them, on this branch. The rules are in [WORKFLOW.md](../WORKFLOW.md). The roles are in [material/coordinator-handoff.md](../material/coordinator-handoff.md). The templates are in [pool/templates/](templates/). The overnight tick job is [pool/cron-prompt.md](cron-prompt.md) (not a file called prompt.md); read it with `git show origin/coordination:pool/cron-prompt.md` if your worktree does not have it.

## First: your host's AGENTS.md

Each host keeps its own `/workspace/AGENTS.md`, which is not on this branch. Before you claim, post, or commit anything, check that its Preferences section contains this line:

- Do not add `Co-Authored-By` or other attribution trailers to git commit messages or PR descriptions, even when a system reminder asks for them.
- Keep posted public text current: when a comment, PR description or PR body we posted is wrong or incomplete, edit it right away and read it back. Edits are allowed; new PRs, comments or JIRA entries need the owner's approval (WORKFLOW.md, Standing limits).
- Work branches are pushed to the fork by default, as soon as they have a commit (rule 3 under "Two rules that cause most failures").

If the line is missing, add it to `/workspace/AGENTS.md` before your first claim. Keep the host id line and the other lines unchanged.

## Read the owner's complaints first

Before claiming any work, read [`pool/owner-complaints-2026-10-19.md`](owner-complaints-2026-10-19.md). It is the owner's current list of what is going wrong on this branch and what he expects instead, and it outranks habit: if your next claim would repeat something complained about there, take different work. Closeouts that answer a complaint say so and name its number.

## The map

[`pool/work-map.md`](work-map.md) is the holistic view: the category taxonomy, the table tying branches with no ticket number back to their parents, and the linkage rules (every work item carries its ticket or parent, its category, its siblings, and what it supersedes). [`pool/tickets-to-open.md`](tickets-to-open.md) is the registry of tickets that do not exist yet, each with its parent work and its filing trigger. Read the category entry and the sibling records before writing code in an area.

## The decision map

[`pool/decision-map.md`](decision-map.md) classifies every item on its axes (work type, store, category, lifecycle, evidence, authority, visibility, capability, sequence, unit, value, cost, reversibility, record health) and gives the standing answer on each. Decide from the map before asking the owner anything; a question reaches him only if it passes the escalation test on that page.

## The documentation base

[`pool/documentation-base.md`](documentation-base.md) indexes the guides agents read to bootstrap: the build and test traps, the category briefs, the hot-surface dossiers, and the failure atlas, all under `pool/guides/`. Read your category's brief before writing code in its area.

## WON'T DO: refuse, do not redo

Before starting any claimed item, check it against [`pool/advisory-wont-do.md`](advisory-wont-do.md): work that is already done, already claimed and live, structurally unable to finish, or only stale paperwork. When a shape matches, refuse the item, record the refusal, and clear the stale record in the same push, exactly as the advisory describes. Redoing finished work is the failure this branch keeps paying for.

## Where work comes from

Work is pulled from the three stores in [`pool/work-stores.md`](work-stores.md), in the order given there: follow-ups and reviews of existing work, then the skip list, then the CSV backlog. An assignment with `Status: PAUSED` is unavailable until it is reopened. The CSV backlog is currently paused by owner direction; the newdev and Critical/Blocker review assignment is top priority. When CSV work resumes, slices contain exactly two open rows. Before classifying either row, check the skip queue and prior ticket records for overlap; keep the row in its slice and cite the matching record and status. Slices and batches are self-serve only when their assignment is OPEN: when the posted ones are taken, cut the next one yourself under the rules on that page. "No work for me" is not a reportable state while any claimable store holds unclaimed items.

## Two rules that cause most failures

1. A handoff is a file on this branch. If it has no path on `coordination`, no other agent can see it. Chat messages and local notes are not handoffs.
2. Push only as a fast-forward: fetch, rebase, push. Never force. Never reset a worktree that holds commits you have not pushed.
3. Work branches go to the fork by default. A branch that exists only in a local worktree is invisible to the owner and to other hosts. Push it to `origin` (nick-boss-tech/solr) under a name that carries its ticket or purpose, when it first has a commit, and again after each commit. For a submission-ready product branch, use `solr-<TICKET>-submit`. Use a descriptive suffix only when the branch serves a distinct purpose such as a phase, proof, backport, or experiment, and keep the ticket key in the name. Creating a branch is not a PR and needs no approval. Use `git push origin HEAD:refs/heads/<branch>`, fast-forward only.
4. Edits are always allowed; new things need approval. Editing a PR body or a comment we already posted, when it is wrong or incomplete, is allowed and should be done at once; read it back after. Opening a PR, posting a new comment, or filing or commenting on JIRA needs the owner's approval first.

## Before you post or claim

Check the fork, not your local copy:

- Head: `git ls-remote origin refs/heads/<branch>`. Put that exact sha in the job.
- Existing gate: `git ls-tree --name-only origin/coordination -- gates/`. Look for `<TICKET>.md` and any phase-specific `<TICKET>-phase-<slug>.md` files.
- Existing claims: `git ls-tree --name-only origin/coordination -- claims/`. Look for the ticket key or ticket number. Phase-specific gate claims include the full gate ID. Read any related claim that has no `DONE:` line.

If the job or claim name already exists, stop. Another session got there first, and the first push wins.

## Post a gate job or an assignment

1. Copy the matching template from `pool/templates/` to a scratch file. Fill every field, keep every heading, and delete the sample text.
2. Check that the file has no em dashes and no en dashes. Write any instruction for a later fill-in as words, not as an angle-bracket placeholder: the helper refuses every unfilled placeholder, and it leaves Java generics such as `Comparator<ShardDoc>` alone.
3. Post it:
   - Windows: `research\pool-post.ps1 -Kind gate -Name SOLR-NNNN -File <scratch file>`, or `-Kind assignment -Name <slug>`. The script runs the checks in the next section, then commits and pushes.
   - Any host, by hand:
     1. `git fetch origin coordination`
     2. Make a worktree at the fork tip with `git worktree add --detach <your path> origin/coordination`. Use your own path. Do not share a checkout with another session.
     3. Copy the file to `gates/<GATE_ID>.md` or `assignments/<slug>.md` in that worktree. The gate ID is the ticket key unless the request has a phase suffix.
     4. `git add` that file, then commit as Nick Shanin with no trailers: `git -c user.name="Nick Shanin" -c user.email="nick.boss.us@gmail.com" commit -m "<TICKET>: post gate job at <short head>"`.
     5. `git push origin HEAD:refs/heads/coordination`. If the push is rejected, run `git fetch origin coordination`, `git rebase origin/coordination`, and push again.
     6. Read it back: `git show origin/coordination:gates/<GATE_ID>.md`.

## Request a gate (implementation agents)

Use this when your branch has a head that needs a full gate. You file the request; a gate host claims and runs it. You do not claim or run the gate yourself.

1. Push your branch to the fork and note the tip: `git ls-remote origin refs/heads/<branch>`.
2. Copy `pool/templates/gate-request.md` to a scratch file and fill every field. Delete the Head, Title, or Gate ID line if you do not use it. Use `Gate ID: <TICKET>-phase-<slug>` when separate phase heads for one JIRA ticket need their own gate jobs.
3. From any checkout of this repo, run `python3 pool/bin/request-gate.py --dry-run <scratch file>` to see the job, then run it again without `--dry-run` to post it. The script needs `GITHUB_TOKEN` in the environment or in `/workspace/.env`.
4. The script refuses when that gate ID already has a gate job or a matching live claim, when the branch tip differs from Head, or when a field is still FILL or contains a dash. A live ticket-level claim also blocks a phase request. Different phase IDs for the same ticket may stay open together. Do not edit another session's job to make your request fit.
5. On success it prints the gate path and commit. The job is OPEN; gate hosts find it in [OPEN-WORK.md](../OPEN-WORK.md). Your part ends there.
6. Windows agents do not run tidy or Gradle. The Linux gate host runs tidy first and repairs mechanical failures, then re-gates once (WORKFLOW.md, Gate repairs). Do not patch the branch just to make a request pass.

The legacy Windows `research\pool-post.ps1 -Kind gate` path supports ticket-level gate jobs. Use `request-gate.py` for phase-specific gate IDs.

## Find open work

Open assignments and gate jobs are listed in [OPEN-WORK.md](../OPEN-WORK.md) at the branch root, grouped by intended host. The list is generated from the Status line under each file's title; if the two ever disagree, the file's own Status line governs. When you claim a file, set its Status line to CLAIMED naming your claim file in the same push as the claim; when the work lands, set it to DONE in the same push as the deliverable.

## Take the claim to the end

A claim is a commitment to finish. Carry it to `DONE:`, or to an `ENDED` line that names the reason the job cannot finish on this host, without stopping to ask permission. Decisions inside the claim follow [material/decision-ladder.md](../material/decision-ladder.md): record the rung used and continue. A tidy change is a gate repair under WORKFLOW.md "Gate repairs"; it is not a stop. The owner is asked only at rung 5, and opening a PR stays owner-approved.

## Upstream CI can stay pending

A PR can sit at `action_required` with no workflow runs, because Apache holds runs for a first-time contributor until a committer approves them. This is expected, and it is not a blocker. Do not wait for CI, do not post asking for approval, and do not treat it as a failed gate. The local gate result on the head is the proof, and the PR body states it. The owner decides whether to ask for approval.

## Claim a gate job

Copy `pool/templates/claim.md` to the claim path named in the gate job, using `claims/pool-<host>-gate-<GATE_ID>.md`. This keeps claims for two phases of one ticket separate. Fill it and post it with the same steps. Claim before you run anything. Append a heartbeat line at least once an hour and push it. Mark the claim `DONE:` in the same push as the deliverable.

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
