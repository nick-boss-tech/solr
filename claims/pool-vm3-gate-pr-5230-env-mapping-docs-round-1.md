# Claim: PR 5230 environment-mapping docs gate, round 1

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: ACTIVE. Mark DONE with the receipt and report paths when the gate lands.
- Job or assignment: `gates/pr-5230-env-mapping-docs-round-1.md`.
- Slice: the whole gate job (exact-head tidy preflight, three Ref Guide tasks, EnvUtils behavior probes at the Solr 10.0.0 baseline, and startup-script module checks).
- Capability tags used: gate, premise-run.
- Started: 2026-10-26T01:44:02Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: pr-5230-env-mapping-docs-round-1@bf285189f74b775165fc53ba305d35dccf880a47.
- Pinned documentation head: `bf285189f74b775165fc53ba305d35dccf880a47` on fork branch `solr-17988-docs` (checked by `git ls-remote` at claim time).
- Behavior baseline: Solr 10.0.0 release `6c6c48a6f78486130682ea9c1f7a2723af5a87be`.
- Runner and lock: sequential commands run in the foreground inside this session, serialized with `flock /workspace/tools/.vm3-gradle.lock`.
- Standalone clone: `/workspace/pr5230-docs-standalone` (own `.git` directory, not a linked worktree).
- Log directory: `/workspace/gates/logs/vm3/pr-5230-env-mapping-docs-round-1/`.
- Receipt: `receipts/SOLR-17988.md` (none existed on the board at claim time).
- Links: Ticket or parent: SOLR-17988 / https://github.com/apache/solr/pull/5230. Category: documentation (`pool/work-map.md` is not on `coordination-ui`; no category file to cite). Siblings: `claims/solr-17988-docs-refguide-build-round-3-done.md` (round 3 at `16d3005293d3`, now superseded for this head), `claims/pr-5230-env-mapping-windows-round-1.md` (the Windows correction that produced this head). Supersedes: none.

Heartbeat: 2026-10-26T01:44:02Z

Preflight: detached tidy preflight is the first step of the run and is recorded in the next heartbeat.

Result: pending.
