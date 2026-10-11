# Claim: SOLR-5011 shared-schema loader analysis

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-solr-5011-shared-schema-analysis.md`
- Date: 2026-10-11
- Staffing: 1. One subagent reads the gated head's code and answers the shared-schema question.
- Scope: read-only. No branch edit, no test, no build, no gate job.
- Deliverable: `reports/solr-5011-shared-schema-analysis.md`, with the verdict (UNSAFE, SAFE or NOT SETTLED BY READING) up front.
- Heartbeat: 2026-10-11T07:20Z (claim taken); the analysis reported.
- Status: DONE, 2026-10-11. Deliverable: `reports/solr-5011-shared-schema-analysis.md`, verdict UNSAFE. With `shareSchema` on, the surviving core's cached schema keeps the origin core's loader, so the branch's `doClose` closes a class loader that the survivor's schema still uses. Recommended narrowing: skip the loader close when a schema cache is in use. The SOLR-5011 draft is on hold until that lands.
