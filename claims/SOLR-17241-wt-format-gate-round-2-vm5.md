# Claim: SOLR-17241 wt gate, round 2 (vm5)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: DONE, FAIL at the candidate head. Receipt: `receipts/SOLR-17241.md`. Repair report: `reports/SOLR-17241-wt-format-linux-round-2.md`.
- Job: `gates/SOLR-17241-wt-format-round-2.md`. Tested head: `7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e`.
- Failing leg: `V2ApiIntegrationTest.testReloadHonorsWtOnNonHostingNode` (NPE in `JavaBinResponseWriter.write`, `req` null, on the non-hosting node).
- Attempts: 2 (attempt 1 lost to a VM crash, logs kept). Idempotency key: SOLR-17241-wt-format-round-2@7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e.
- Capability tags used: gate. No PR, public comment, or JIRA action. No candidate change.
- Lock released; no Gradle process left from this claim.

Heartbeat: 2026-10-30T00:58Z
Result: 2026-10-30T00:58Z: FAIL at 7a5a998ee9. Candidate integration test failed on the non-hosting node; controls fail for the regression reason; check legs not completed.
