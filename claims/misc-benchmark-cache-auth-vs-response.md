# Claim: misc-benchmark-cache-auth-vs-response (misc box item 1)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: LIVE.
- Job or assignment: `assignments/misc-box-round-1.md`, item 1 (slug `benchmark-cache-auth-vs-response`). Parent: `assignments/misc-box-and-day-scan-parent-round-1.md`.
- Ticket type: measurement study on a local Solr build. The immediately previous claim by vm6 was a gate (SOLR-18295), so owner complaint 5 is met.
- Slice: the whole item. Deliverable `reports/misc-benchmark-cache-auth-vs-response-round-1.md`. No Solr code change; the harness stays uncommitted in scratch.
- Capability tags used: jvm-tests, records.
- Started: 2026-10-24T23:02:44Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: misc-benchmark-cache-auth-vs-response@2026-10-24T23:02:44Z.
- Constraints: only authorized requests are measured; no permission outcomes recorded for unauthorized callers. No other build or test load on the host during the timed runs. No post to GitHub or JIRA. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-24T23:02:44Z

Result: 2026-10-24T23:43:54Z: report `reports/misc-benchmark-cache-auth-vs-response-round-1.md`. Full response median 1.594 ms (off) and 1.816 ms (on); conditional 304 median 0.386 ms (off) and 0.463 ms (on); 1,000 measured requests per path after 200 warmup. Raw logs under `/workspace/gates/logs/vm6/misc-benchmark/raw/`. Observation: the v2 core schema read returned 403 for a reader-only user in every rule variant tried, while v1 returned 200 once for the any-role grant (report, authorization finding).

DONE: 2026-10-24T23:43:54Z (benchmark). Deliverable: the report on the board. The microbenchmark of the authorizer alone was not done.
