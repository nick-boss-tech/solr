# Claim: PR 5206 reconnection cleanup gate, round 1 (phone agent)

- Claimant host: phone agent (Linux, this VM); tags gate, review, records.
- Job: `gates/pr-5206-reconnection-cleanup-round-1.md`.
- Target: `nick-boss-tech/solr:solr-18576-submit-apache-main` at candidate F `6c6ff8d9a0e173c02b5a11f8ad3b13a59ebd36fa` (fork ref re-read at F at claim time; parent O `4c8f12f526f54e4e90a8ea49d46c1d28d899cb65` to be confirmed in preflight). PR #5206 head to be re-read at execution.
- Setup: fresh full clone (`~/workspace/solr-clones/5206-gate`) on a detached checkout; all Gradle legs run with the job's `--no-daemon` and `--no-build-cache` flags through the host's solr-gradle.sh; root check leg under `env -u NODE_ENV` plus the host's combined truststore for the sandbox egress CA, both recorded as environment conditions. JDK 21.0.12.1+1, host test-queue lock.
- Logs and XML: `~/workspace/tools/5206-gate-logs/`.
- Deliverables: report at `reports/pr-5206-reconnection-cleanup-round-1-gate.md` and phase receipt at `receipts/SOLR-18576-pr5206-review-fixes-round-1.md`.
- Claimed: 2026-11-02 (America/Denver). Idempotency key: `pr-5206-reconnection-cleanup-round-1-exec@6c6ff8d9a0e173c02b5a11f8ad3b13a59ebd36fa`.
