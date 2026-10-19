# Claim: pool-vm1-gate-SOLR-18507-phase-2.md

- Claimant host: vm1, matching a file in `hosts/`.
- Status: CLAIMED round 2, 2026-10-19T05:20Z, at head a36ca787d5b (tidy commit pushed under the owner's proactive tidy direction); gate relaunching on vm1. Job: `gates/SOLR-18507-phase-2.md`.
- Slice: the whole gate job at branch head 3f273e29ee000c44647304e583bd3f564231ea60 (tip confirmed by ls-remote at claim time).
- Capability tags used: gate.
- Started: 2026-10-19T03:40Z (UTC). Queued on vm1 behind the SOLR-18458 round 2 gate; runners serialize on the vm1 build lock.

Heartbeat: 2026-10-19T03:40Z (claim taken; queued)

Heartbeat: 2026-10-19T05:05Z (gate stopped at step 1, GATE INVALID; see gates/SOLR-18507-phase-2.md)

Heartbeat: 2026-10-19T05:20Z (tidy commit a36ca787d5b pushed; round 2 launching)

Heartbeat: 2026-10-19T06:00Z (round 2 GATE FAILED at compile: missing LeafReader import; fix d55d4422de4 pushed; round 3 launching)
