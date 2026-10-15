# Claim: SOLR-16655 gate at the live head (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T19:19:34Z by vm2. Job: gates/SOLR-16655-live-head-gate.md (head 5e2317443f4116a9e97330b0d6ce175cf44b54df, fork tip verified by ls-remote at claim time).
- Capability tags: gate. Runner: /workspace/gates/g16655-gate.sh on vm2, using the shared runner /workspace/gates/lib/gate-run.sh. Seed 16655C0FFEE16655.
- Stops before any push. A tidy change (GATE INVALID), a proof-leg failure of an unexpected shape, or a failed focused run is recorded in the job file and escalated; not relaunched.
- Status: DONE (gate settled GATE GREEN by the main-side proof verdict, 2026-10-15).

Gate result, 2026-10-15T20:35Z: not green. Proof leg rc=1 with 1 failed of 36 at base (testTrimFieldsRecurseIntoNestedDocumentsOnlyWhenParentSelected); the run used base b201a57fb3e5 where the job names 14c7aac0d151. Recorded in gates/SOLR-16655-live-head-gate.md under Gate result. Left for the main agent; not relaunched; no push.

Closed 2026-10-15 by the main side: the proof verdict this run left open is recorded in gates/SOLR-16655-live-head-gate.md (Main-side proof verdict, 2026-10-15). GATE GREEN at 5e2317443f4116a9e97330b0d6ce175cf44b54df; no commit produced, no push owed.
