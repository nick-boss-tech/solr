# Pool assignment: shared gate backlog buffer (either Linux host)

Capability tags: `gate`, `premise-run`, `bats`. Staffing: one job at a time per host. Claim path: claims/pool-vm2-gate-backlog-<ticket>.md (the established per-ticket claim files).

This file makes the buffer explicit: the round 2 gate jobs below are written up in gates/<ticket>.md and are unclaimed. Either Linux host (vm1 or vm2) may claim any of them under the WORKFLOW.md claim rules; the round 2 assignment named vm2 as the intended host, and the owner has since directed both Linux hosts to draw from the same backlog (2026-10-10). Round 1 jobs and claimed jobs are not part of this buffer.

Unclaimed at the time of writing: SOLR-10667 (gates/SOLR-10667.md), SOLR-12347, SOLR-11678, SOLR-12161, SOLR-6430, SOLR-7119, SOLR-11700, SOLR-17356, SOLR-16322, SOLR-17722. Check claims/ before starting: a ticket with a live claim is taken, whatever this list says.

Added 2026-10-10 (buffer top-up, first gates for branches whose premises already held on vm2 premise runs): SOLR-5262 (gates/SOLR-5262.md), SOLR-13705 (gates/SOLR-13705.md), SOLR-10390 (gates/SOLR-10390.md, capability tag `bats`). Each is a first full gate; the premise evidence is recorded in the job file and in the ticket's receipt. Several tickets in the list above have since been claimed or gated; claims/ governs.

Rules: each job follows its gates/ file exactly (premise run first where the file requires one; a premise that does not hold is recorded as NO GATE by finding with run evidence). The gating host writes the ticket's receipt on green, per WORKFLOW.md. One gate per host at a time.
