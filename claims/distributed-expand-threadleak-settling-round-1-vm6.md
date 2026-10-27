# Claim: distributed-expand-threadleak-settling-round-1 (premise-run)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: LIVE.
- Job or assignment: `assignments/distributed-expand-threadleak-settling-round-1.md`.
- Slice: the whole assignment. Pinned trees: branch_9x `dae5ac66f9c821d0edb26ca62493a1896c001d3b` (PR 5198 backport tip, fetched from apache/solr 2026-10-27) and apache/main `ee2f1bf2e0109956145d7fd995da63c4091a589f`. Seed `85F9F77FD74C1D41`. Class `org.apache.solr.handler.component.DistributedExpandComponentTest`.
- Capability tags used: premise-run, records.
- Started: 2026-10-27T07:27:48Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: distributed-expand-threadleak-settling-round-1@dae5ac66f9c+ee2f1bf2e01.
- Links: Ticket or parent: PR 5198 (SOLR-17953 9x backport), CI job 121178733450. Category: premise-run (pool/work-map.md). Siblings: none known. Supersedes: none.
- Method: two detached worktrees on the host, `/workspace/worktrees/dx-9x-vm6` and `/workspace/worktrees/dx-main-vm6`, one Gradle run at a time under the host lock via `pool/bin/sweep-run.sh`. The assignment's `-Ptests.file.encoding=ISO-8859-1` and security manager settings apply; the local gate does not give the CI runner's environment exactly, which the report will state.

Heartbeat: 2026-10-27T07:27:48Z
