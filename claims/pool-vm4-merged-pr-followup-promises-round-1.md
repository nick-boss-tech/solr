# Claim: merged-PR follow-up promises audit, round 1, vm4 pass

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE 2026-10-18T12:06Z. Deliverables: `reports/merged-pr-followup-promises-round-1.md` (pass h, dated at the top).
- Job or assignment: `assignments/pool-merged-pr-followup-promises-round-1.md`.
- Slice: the whole recurring pass: re-derive the merged set from the API, re-check open and not-yet-due promises, append new merges, date the pass at the top of the report.
- Capability tags used: review.
- Started: 2026-10-18T12:03:09Z (UTC).
- Host id: taken as `vm4` from the operator's instruction for this tick, because `~/.pool-host` is missing on this VM (`HOME=/home/claude`). Earlier vm4 ticks stopped at step 0 for this reason (see `claims/pool-vm4-tick-stopped-no-pool-host.md`).
- No Gradle build is run in this pass.

Heartbeat: 2026-10-18T12:03:09Z

Result: 2026-10-18T12:06Z: merged set unchanged at 10 (no merge since #5062 and #5027); open PRs and fork heads unchanged from pass g; no promise changed state. Ledger pass h in reports/merged-pr-followup-promises-round-1.md. Read-only; no build run.

DONE: 2026-10-18T12:06Z. Deliverables: reports/merged-pr-followup-promises-round-1.md (pass h).
