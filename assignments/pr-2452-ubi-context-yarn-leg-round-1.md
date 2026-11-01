# Assignment: PR 2452 UBI context, yarn-dependent root check leg, round 1

Status: OPEN. One claim. Records and gate only; no PR comment, no Jira write.

Why: the round 3 gate for candidate F `b48c4509a0375f93080f8be50520c18dba23aa29` (fork branch `ubi-pr2452-context-submit`; base B `6402b4b528d3ec8daa134772abc84532380e3f7c`, the apache/solr PR 2452 head) is complete except the unmodified root check. On the phone agent VM the UI build's bundled yarn 1 cannot fetch npm packages through the egress, so `:kotlinWasmNpmInstall` and `:kotlinWasmToolingSetup` fail for an environment reason; excluding both, the root `check -x test` is green (595 tasks, rc=0), and the unmodified run failed only on `:kotlinWasmNpmInstall` after 583 executed tasks. Detail: `reports/pr-2452-ubi-context-rework-and-gate-round-1.md` and `receipts/PR-2452-ubi-context.md`, round 3 completion sections.

Task, for a host with working npm egress (the vm6 shape: it ran the PR 5258 wasm browser leg):

1. Check out F in a real clone, not a git worktree (`:rat` misreads worktrees).
2. Run the root `check -x test` unmodified, NODE_ENV unset, no task exclusions.
3. Post a receipt under `receipts/` naming the head, the host, the full command, and the result. If it fails, name the failing task and whether the failure sits in the yarn tasks or elsewhere. A failure outside the yarn tasks reopens the round 3 gate; say so in the receipt.

Done when: the unmodified root check at F has a recorded result from a host where yarn can fetch.
