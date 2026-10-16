# Claim: pool-vm1-solr-8240-head-failure-round-1

- Claimant host: vm1, matching `hosts/vm1.md`.
- Status: BLOCKED (2026-10-16T02:16Z). Investigation complete: the head failure is a defect in the new TestInitParams test, in two independent parts, and the production code at 97cd22159b is correct. Per the assignment's Scope item 3 the test is not changed by this lane; the item is escalated to the main agent. Record: assignments/pool-vm1-solr-8240-head-failure-round-1.md, section "Investigation outcome (vm1, 2026-10-16)", and gates/SOLR-8240.md, section "Head-failure investigation, vm1, 2026-10-16".
- Job or assignment: `assignments/pool-vm1-solr-8240-head-failure-round-1.md`.
- Slice: the whole assignment.
- Capability tags used: implementation, gate.
- Started: 2026-10-16T01:52:00Z (UTC).

Heartbeat: 2026-10-16T01:52:00Z
Heartbeat: 2026-10-16T01:58:22Z (investigation runs launched on vm1: head reproduction, prior-head baseline, scratch corrected-assertion probe)
Heartbeat: 2026-10-16T02:16:05Z (investigation complete: test defect on both failures, production cleared; records written, claim marked BLOCKED, escalated to the main agent)

Scope note: find why the head at 97cd22159b does not reject the initParams field-mapping case; fix production and re-gate if the head is wrong, escalate to the main agent if the test is wrong. The vm2 claim claims/pool-vm2-gate-backlog-8240.md stays open until this assignment is DONE, per the assignment.
