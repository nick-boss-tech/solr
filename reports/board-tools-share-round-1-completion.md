# Board tools share, round 1: completion record

- Assignment: `assignments/board-tools-share-round-1.md`. Completed by the phone agent, 2026-10-26.

1. `WORKFLOW.md`, section "Posting a file to this branch" (commit `d2110660ef`): multi-file records now name `pool/bin/board-push.sh` as the route, described as building one commit on the fetched tip with a temporary index and pushing fast-forward. The "normal git push with fetch and rebase first" wording is replaced with "build on the fetched tip, no rebase".
2. `pool/bin/post-file.sh` (commit `86b5d30ea0`): the `--branch` default is now `coordination-ui` (code and usage comment), and the credential read falls back from line 2 of `~/.git-credentials` to the `GITHUB_TOKEN` environment variable, then to `GITHUB_TOKEN` in `/workspace/.env`. Header comment updated to match; the edited script passes `bash -n`. The phone agent's own posts pass `--branch coordination-ui` explicitly and are unaffected either way.
3. Host-default check: a search of the board's `pool/`, `hosts/`, and top-level docs for defaults or instructions pointing at the retired `coordination` branch found none outside `coordination-ui` usage itself. The only default was the one in `post-file.sh`, fixed in item 2. (The phone agent's local `~/workspace/tools/post-record.sh` targets the frozen archive branch by design; it is not a board tool and no host uses it.)
