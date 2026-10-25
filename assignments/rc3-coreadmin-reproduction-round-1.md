# Assignment: CoreAdminHandlerTest RC3 failure reproduction (SOLR-18587, standalone unit)

Status: OPEN
Tags: premise-run

Standalone form of unit 2 of `assignments/rc3-repeating-failures-rootcause-round-1.md`, posted separately so it can be claimed without taking the whole parent. `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` failed in all five RC3 smoke runs; SOLR-18587 is the filed ticket. SOLR-9433 (Reopened) covers the adjacent cleanup path; this unit is about the test failure, not that ticket's scope.

## Work

1. Mirror unit 1's shape: focused runs of `org.apache.solr.core.CoreAdminHandlerTest` (the one method first, the class if the method alone does not reproduce) at the RC3 tip (`749712b71940`) and at apache/main (`13c693d805f`), host lock, JDK 21, seeds recorded for every run.
2. Capture the failing state from the logs and XML: what the create-failure setup leaves behind, where the delete-instance-dir assertion or wait fails, and whether the failure is timing, leftover state, or an assertion about the wrong thing.
3. Read the test and the handler path it exercises and state the likeliest mechanism as a hypothesis with the code citations, plus the probe that would decide it, in the same shape as `reports/rc3-tlogreplica-rootcause-round-1.md`.

## Deliverables

- `reports/rc3-coreadmin-rootcause-round-1.md` with the run table (tree, seed, result counted from the XML), the captured state, and the hypothesis and deciding probe.

No fix branch and no gate in this unit; the probe becomes its own assignment if the hypothesis holds up.
