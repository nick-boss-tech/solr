# Claim: SOLR-6065 gate at the live head (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T19:19:34Z by vm2. Job: gates/SOLR-6065-cloud-test-gate.md (head 3d2cec9e1ab3a19f6b948682b116c2780da4b65b, fork tip verified by ls-remote at claim time).
- Capability tags: gate. Runner: /workspace/gates/g6065-gate.sh on vm2, using the shared runner /workspace/gates/lib/gate-run.sh. Seed 6065C0FFEE6065.
- Stops before any push. A tidy change (GATE INVALID), a proof-leg failure of an unexpected shape, or a failed focused run is recorded in the job file and escalated; not relaunched.
- Status: CLAIMED, runner queued.

Gate result, 2026-10-15T21:00Z: not green. Proof leg rc=1: the cloud test failed on base with `expected:<500> but was:<400>`; the focused head and check legs were rc=0. Recorded in gates/SOLR-6065-cloud-test-gate.md under Gate result. Left for the main agent; not relaunched; no push.
