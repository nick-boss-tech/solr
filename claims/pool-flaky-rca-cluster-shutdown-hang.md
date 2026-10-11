# Claim: flaky root-cause reading, the MiniSolrCloudCluster shutdown hang

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-flaky-rca-cluster-shutdown-hang.md`
- Date: 2026-10-11
- Slice: one, one subagent. Reading only: the shutdown path on current main, the mechanisms ranked with code evidence, and a reproduction spec for the Linux hosts. No fix implemented.
- Deliverable: `reports/flaky-rca-cluster-shutdown-hang.md`.
- Scope: read-only. No builds, no test runs, no gate runs.
- Heartbeat: 2026-10-11T04:12Z (claim taken); reading reported.
- Status: DONE, 2026-10-11. Deliverable: `reports/flaky-rca-cluster-shutdown-hang.md`. Top-ranked mechanism: the close task stops the Jetty client before draining the communication executor, both under one 60-plus-60-second budget, so the drain burns the second 60 seconds and the pool throws "Timeout waiting for pool to shutdown". Not yet separated from the alternative (an untimed wait on outstanding requests); a thread dump about 30 seconds into the close separates them. No fix implemented.
