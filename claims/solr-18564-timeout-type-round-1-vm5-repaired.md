# Claim: SOLR-18564 / PR 5192 typed executor timeout follow-up, Linux gate, round 1

- Claimant host: vm5, matching the Host id line in `hosts/vm5.md`.
- Status: ACTIVE; a receipt at the exact head is the completion record.
- Job or assignment: `gates/SOLR-18564-pr5192-timeout-type-round-1.md`.
- Slice: the whole gate at head `6ca1cba8061b3c6298040634972afc4361f55add` (changelog parse, tidy, Error Prone compile, focused suites with seeds 18564ABC0001 and 18564ABC0002, matched mutation, `check -x test` with NODE_ENV unset).
- Capability tags used: gate, premise-run.
- Started: 2026-10-27T17:58:16Z (UTC). Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18564-timeout-type-round-1@6ca1cba8061b.
- Links: Ticket or parent: SOLR-18564 / apache/solr PR 5192, fork branch `nick-boss-tech/solr:corecontainer-shutdown-closes`, original head O `dd65b21b41dc1d008c51031b71f61964ee2ca169`. Implementation claim: `claims/solr-18564-timeout-type-round-1-windows.md`. Category: review. Siblings: `claims/solr-18564-exact-head-gate-round-1.md` (vm6, head O, DONE), `claims/solr-18564-exact-head-gate-vm2.md` (vm2, head O). Supersedes: none; the exact-head gate covers O only, as the job states.

Decision rung: rung 1 for scope. Tidy at F changed one test-file line (wrapping an assertion); per the job, the repair is a formatting commit `6ca1cba8061b` on the same fork branch (fast-forward from F, author Nick Shanin, no trailers, no PR number) and this claim gates that repaired head. Head F is not gated. The earlier F tidy run is kept under `/workspace/gates/logs/vm5/solr-18564-timeout-type/`. The job names any capable Linux gate host; vm5 holds gate and premise-run. Checked before the claim: the fork head equals F, and no Linux claim exists for this round (only the Windows implementation claim and the head-O gates). The host lock `/workspace/tools/.gradle-vm5.lock` serializes this gate behind other builds on this host. Preflight tidy at F ran detached through `pool/bin/gate-preflight.sh` before this claim, as the job requires, and returned one file changed.

Method note: the gate's full checkouts are under `/workspace/gates/solr-18564/` (head R at `/workspace/gates/solr-18564/repair`, and a separate copy for the mutation), not the pool worktree. Logs are under `/workspace/gates/logs/vm5/solr-18564-timeout-type/`. Test work directories are under `/tmp/solr18564-timeout-type-*`.

Heartbeat: 2026-10-27T17:58:16Z

Result: none yet.
