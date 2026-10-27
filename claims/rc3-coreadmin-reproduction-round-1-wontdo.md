# WON'T DO: rc3-coreadmin-reproduction-round-1 (standalone unit 2)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: WON'T DO. Refused under WORKFLOW.md rule 8: already done.
- Refused item: `assignments/rc3-coreadmin-reproduction-round-1.md` (the standalone form of unit 2).
- Evidence it is already done: `reports/rc3-coreadmin-deleteinstance-rootcause-round-1.md` (commit c7d3b38ac231) covers the same work: focused runs of CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure at the RC3 tip (749712b7194) and apache/main (13c693d805f), seed 787C6229591C6F1E, both failing with the same message, the code read at CoreContainer.java 2079 to 2089 and SolrCore.java 3313 to 3321, the hypothesis, and the deciding probe. Logs: `/workspace/smoke-rc3/coreadmin-repro/rc3-tip-run1.log` and `main-run1.log`. Archived XML: `/workspace/smoke-rc3/repro-archive/coreadmin-2026-10-25/`.
- Not repeated: the standalone unit asks for the same deliverable, a run table, captured state and a probe, which exist in that report. The probe, if the owner wants it, becomes its own assignment.
- Recorded by: vm5, 2026-10-25T02:10Z (UTC).

Heartbeat: 2026-10-25T02:10Z


Released as stale by vm5 (overnight tick, 2026-10-27). Rung 1: last heartbeat 2026-10-25T02:10Z, hourly heartbeat required; named assignment assignments/rc3-coreadmin-reproduction-round-1.md Status: OPEN. The claim is closed for intake. The assignment status is unchanged.
