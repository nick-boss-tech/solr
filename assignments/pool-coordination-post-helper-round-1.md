# Pool assignment: Windows coordination posting helper, round 1

Status: OPEN

- Capability tags: windows-check, review.
- Intended host: Windows. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent, 2026-10-19, at Nick Shanin's direction.

## Why

The Windows `research/pool-post.ps1` helper still targets the archived `pr-prepare` board. Posting to the live `coordination` board has required repeated fetch, rebase, generated-list refresh, push, and read-back steps while the board tip was moving. Create a coordination-specific helper that makes this sequence safe and gives a clear recovery path when another post overlaps the same work.

## Items

1. **Audit current Windows posting flow.** Read `pool/START-HERE.md`, `WORKFLOW.md`, and `env/agents.md`; inspect `research/pool-post.ps1` and the manual posting steps. Record the stale defaults and any assumptions that do not hold for `coordination`. Start from: those files. Output: `reports/coordination-post-helper-round-1.md`.
2. **Implement a coordination-only poster.** Add `pool/bin/post-coordination.ps1`. It must accept the dedicated worktree, the expected remote head, the intended changed paths, and a commit message. It must verify the target remote branch is `coordination`, the worktree is clean, and the local post is exactly one commit based on the expected head. Load `GH_TOKEN` in process without printing or persisting it, run `gh auth setup-git`, and push from that same process. Output: `pool/bin/post-coordination.ps1`.
3. **Document the operating contract.** Update `pool/START-HERE.md` with the helper's invocation and refusal cases, retaining the manual procedure as recovery guidance. The helper may regenerate `OPEN-WORK.md` with `pool/bin/open-work.py` after a rebase. Output: `pool/START-HERE.md` and the report.

## Proposed helper behavior

Use a bounded fetch/rebase/push loop. On each attempt, read the live tip and fetch it. If it advanced, confirm the expected head is still an ancestor and compare the intervening paths with the intended post. Stop if the same gate or claim changed, if the local commit is not a single clean post, or if rebase conflicts outside generated `OPEN-WORK.md`. After a safe rebase, regenerate `OPEN-WORK.md`, amend the one local post commit, then retry a plain fast-forward push. Never reset, clean, force-push, or overwrite a gate or claim. After pushing, read back each requested path from the remote and report both the pushed commit and current remote head. If the remote moves again or any check fails, leave the checkout recoverable and print the exact next manual step.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Target branch | decided: `coordination` only | Refuse any other branch, including archived `pr-prepare`. | Nick Shanin |
| Remote advances on unrelated paths | decided: bounded rebase and retry | Regenerate `OPEN-WORK.md` after rebase; stop on overlap, unsafe history, conflict, or retry limit. | Nick Shanin |
| Gate or claim path changed during post | decided: stop | Do not amend or replace that file; report the changed path and current remote head. | Nick Shanin |
| Token handling | decided: process memory only | Never print it, write it to disk, or place it in arguments or a URL. | Nick Shanin |
| Live GitHub or JIRA communication | decided: none | The helper only updates the coordination branch. | Nick Shanin |

No row may leave the choice to the claimant.

## Deliverable

`reports/coordination-post-helper-round-1.md`: findings, helper invocation, refusal and recovery behavior, and a concise review of the resulting script. `pool/bin/post-coordination.ps1`: the helper. `pool/START-HERE.md`: updated Windows posting instructions.

## Limits

- No Gradle or ticket builds. Do not alter the existing ticket-gate workflow or run a live post as part of this assignment.
- Use a dedicated coordination worktree. Do not reuse another task's worktree or touch the archived `pr-prepare` branch.
- No PR, comment, JIRA, email, or force push. Commit as Nick Shanin with no trailers. No em dashes.
