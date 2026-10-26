# Claim: PR 5108 reverse merge round 3 Linux gate (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Started: 2026-10-26T19:57Z.
- Job: `gates/pr-5108-reverse-merge-round-3.md` (OPEN, Linux proof only), run as the superseding job `gates/pr-5108-reverse-merge-round-3-f2-vm4.md` pinned to the repaired head.
- Fork: `nick-boss-tech/solr`, branch `solr-17786-reverse-cursor-submit`. Original head `c5a16393d2b0d2ad8b05f66697a906733e551b69` (verified by fetch at claim time). Tidy at that head changed one line, so the repair commit `495d3fbdbe07bfb474b43d77b89f8c033bbeb8fd` (Nick Shanin, no trailers, one line in `DistribCursorPagingTest.java`) was fast-forward pushed and verified by ls-remote. Exact head under test: `495d3fbdbe07bfb474b43d77b89f8c033bbeb8fd`. Parent `fde070281383f113a331a9ec5ccde173b66fac3c`, the round-2 gated head.
- Tags: gate, premise-run, review, records.
- Worktrees: positive `/workspace/worktrees/gate-5108` (detached at the exact head). Mutations in separate disposable checkouts from the same head. Logs under `/workspace/gates/logs/vm4/pr-5108-round-3/`.

## Takeover

Two phone-agent-2 claims on this gate are marked ACTIVE but have no host activity since 2026-10-25: `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard.md` (last heartbeat 2026-10-25T03:54:24Z) and `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-post-tidy.md` (last heartbeat 2026-10-25T03:45:30Z). Both are more than two hours stale under WORKFLOW.md. The owner reports phone-agent-2 is not working on 5108 and directed vm4 to take the reverse-merge gate on 2026-10-26. This claim takes over the round-3 gate. It does not resume those claims' steps: they ran an earlier head, and neither recorded a receipt.

## Decisions recorded

- Tidy repair: a formatting-only commit, applied by the gate host as the gate requires, with a superseding job pinned to its head. The original gate file is not edited.
- Receipt writer: `receipts/SOLR-17786.md` is the round-2 receipt of vm2, and vm2 is its single writer. This gate writes a new receipt file, `receipts/SOLR-17786-round-3-vm4.md`, and leaves vm2's receipt and evidence untouched.

## Deliverables

- `receipts/SOLR-17786-round-3-vm4.md`: result, every leg with command and exit code, JUnit totals from fresh XML, mutation failures with expected and actual IDs, log and XML paths, limits.
- `reports/pr-5108-reverse-merge-round-3-gate-vm4.md`: evidence links and the round-3 proof summary.

Status: CLAIMED until the receipt is pushed. The gate file and the superseding job file are not edited after push.
