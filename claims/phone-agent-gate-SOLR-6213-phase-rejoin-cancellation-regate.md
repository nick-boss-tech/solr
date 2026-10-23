# Claim: SOLR-6213 phase rejoin-cancellation re-gate

- Holder: phone-agent (vm1).
- Job: gates/SOLR-6213-phase-rejoin-cancellation-regate.md.
- Branch: `solr-6213-rejoin-fix` at `2193e0104a3fa5c6235d4f843f1d6930afeb65d1`, confirmed against the fork ref and the PR #5210 head at claim time.
- Plan: run the job's steps 0 to 5 on vm1 with the host's serialized gate runner: changelog parse, tidy, Error Prone compile, the two scaffolded fail-before legs (step 3a at the old PR head, step 3b at base), the focused class at three seeds, the six neighboring classes, and `:solr:core:check -x test`. On green, prepend the receipt section and mark the job and this claim DONE in one push. No public writes.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims.
- Idempotency key: `gate-solr-6213-phase-rejoin-cancellation-regate@2193e0104a3fa5c6235d4f843f1d6930afeb65d1`.
Heartbeat: 2026-10-23T20:33:00Z
