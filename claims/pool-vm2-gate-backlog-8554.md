# Claim: SOLR-8554 premise run, packaging and first gate (shared buffer, vm2 coordinator)

Claimant host: vm2. Queue: assignments/pool-shared-gate-backlog-buffer.md (vm2 claims end to end). Job: gates/SOLR-8554.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-11T22:32Z (UTC, claim pushed before any code change).
Branch: solr-8554-submit at 32697b6c3f82d15284678139b861014cd5bb65dd (live tip, verified by ls-remote at claim time).

Heartbeat: 2026-10-11T22:32Z. Premise run next (runner /workspace/gates/g8554-gate.sh, log /workspace/gates/logs/vm2/8554-gate.out).

## 2026-10-11T23:20Z heartbeat (vm2 coordinator)

First gate at f16c7fba78 ended with the tidy step not clean (formatting-only, recorded in gates/SOLR-8554.md). Tidy fold-in pushed as 654c1a7c35 (fast-forward from f16c7fba78). Re-gate running from 23:20Z, log /workspace/gates/logs/vm2/8554-gate2.out.

## DONE (vm2 coordinator, 2026-10-11T23:40Z)

Re-gate at 654c1a7c3570b66e66c446b433e6cb8a16c637be GREEN (GATE END 23:35:03Z). Deliverables: receipts/SOLR-8554.md, gates/SOLR-8554.md (DONE line), this claim. Packaged head f16c7fba78cd and tidy fold-in 654c1a7c3570 are on the fork as fast-forwards.

Status: CLOSED (vm2 audit, 2026-10-16T00:55Z). Job gates/SOLR-8554-changelog-phrase.md is DONE on pr-prepare; no work remains under this claim. Earlier status lines are kept as history.
