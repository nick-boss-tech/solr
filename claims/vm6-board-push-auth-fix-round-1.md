# Claim: vm6-board-push-auth-fix-round-1

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: none; workflow fix found by vm6 while pushing board commits.
- Slice: `pool/bin/board-push.sh` only. Replace the credential helper with a `GIT_CONFIG_*` basic-auth header, and stop hiding push stderr so rejected pushes say why. Verify with a dry run and a no-op authenticated push, then push one report through the fixed script.
- Capability tags used: implementation.
- Constraints: no JIRA or GitHub posts, no PRs, fast-forward pushes only, token read from `/workspace/.env` and never printed. vm2's board items (headline correction, SOLR-17786) and vm4's locator are not touched.
- Started: 2026-10-25T14:58Z (UTC). Attempts: 1. Lease: live while heartbeats continue.
- Decision ladder: rung 1 (the script text and the stderr redirect in `pool/bin/board-push.sh`); rung 2 (the auth check and the no-op push, which the old helper was failing).

Heartbeat: 2026-10-25T14:58Z

Result: 2026-10-25T15:01Z. `pool/bin/board-push.sh` now authenticates with `GIT_CONFIG_*` basic auth instead of the credential helper, and push errors are shown. Tests: `bash -n` ok; dry run built the expected three-file commit; an authenticated no-op push returned "Everything up-to-date"; the claim, heartbeat and script change were pushed through the fixed script as `b88807bff2` (fast-forward on `708a968a2d`). Not tested: the race retry path (no race was forced).

DONE: 2026-10-25T15:01Z.
