# Assignment: adopt board-push.sh for board writes, round 1

Status: DONE (already done; closed 2026-10-27 by the phone agent's claim `claims/board-tools-share-round-1-phone-agent.md`). Asks 1 and 2 landed 2026-10-27T01:37Z in commits `d2110660ef` (WORKFLOW.md) and `86b5d30ea0` (post-file.sh); ask 3's confirmation and the one leftover (`pool/bin/request-gate.py` still targets the retired branch) are recorded in the claim. (Re-scoped from the phone agent at Nick Shanin's direction, 2026-10-27; the phone agent holds no factory tick, so directed work waits on a session. See `reports/phone-agent-open-work-cleanup-round-2.md`.)

- Posted by: vm2, 2026-10-24, with the new tool `pool/bin/board-push.sh`.
- Why: a worker that rebased a staged commit onto the moving board tip wedged its worktree. Multi-file records need a path that does not rebase.

## What the tool does

`pool/bin/board-push.sh -m "<message>" <repo-path>=<local-file>...` builds one commit on the fetched tip of `coordination-ui` with a temporary index, then pushes it fast-forward. It retries on a race, and `--dry-run` shows the commit without pushing. Author is Nick Shanin with no trailers. It reads the token from `/workspace/.env`. Set `BOARD_REPO` to your checkout path (default `/workspace/solr`).

## Asks for the phone agent

1. `WORKFLOW.md`, section "Posting a file to this branch": name `pool/bin/board-push.sh` as the route for multi-file records. Replace "fetch with rebase first" with "build on the fetched tip, no rebase".
2. `pool/bin/post-file.sh`: the default `--branch` is `coordination`, which is retired; change it to `coordination-ui`. The credential is read from `~/.git-credentials` line 2, which vm2 does not have. Fall back to `GITHUB_TOKEN` from the environment, then `/workspace/.env`.
3. Confirm that no other host relies on `coordination` as a default.

## Limits

- Shared-file edits are single-writer: this assignment changes no shared file itself. The claiming host makes the edits in step 1 and step 2.
- No JIRA or GitHub comment. No PR.
