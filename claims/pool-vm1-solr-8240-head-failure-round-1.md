# Claim: pool-vm1-solr-8240-head-failure-round-1

- Claimant host: vm1, matching `hosts/vm1.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-vm1-solr-8240-head-failure-round-1.md`.
- Slice: the whole assignment.
- Capability tags used: implementation, gate.
- Started: 2026-10-16T01:52:00Z (UTC).

Heartbeat: 2026-10-16T01:52:00Z
Heartbeat: 2026-10-16T01:58:22Z (investigation runs launched on vm1: head reproduction, prior-head baseline, scratch corrected-assertion probe)

Scope note: find why the head at 97cd22159b does not reject the initParams field-mapping case; fix production and re-gate if the head is wrong, escalate to the main agent if the test is wrong. The vm2 claim claims/pool-vm2-gate-backlog-8240.md stays open until this assignment is DONE, per the assignment.
