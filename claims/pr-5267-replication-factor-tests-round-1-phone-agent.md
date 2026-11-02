# Claim: PR 5267 replication-factor tests gate, execution (phone agent)

- Claimant host: phone agent (Linux, this VM); tags gate, review, records.
- Job: `gates/pr-5267-replication-factor-tests-round-1.md` (immutable; this claim is add-only per the job's rules).
- Target: `nick-boss-tech/solr:solr-18603-submit` at final head F `b244ae94d6c84eec06bf1de5754e566b0fdb88d1` (fork ref re-read at F at claim time); contributor head O `7f9acb9df610338c20537e1efd6ed75a6c64a13b`. The contributor PR and branch are not touched.
- Setup: F runs in a fresh full clone (`~/workspace/solr-clones/5267-gate-f`) so the root check leg avoids the worktree Antora trap; O and the two mutation legs run in fresh detached worktrees of that clone. JDK 21.0.12.1+1, Gradle 9.7.0 via the host's solr-gradle.sh, all builds serialized through the host test-queue lock. Root check leg uses the host's combined truststore for the egress CA, disclosed in the receipt.
- Logs and XML: `~/workspace/tools/5267-gate-logs/` (fresh JUnit XML copied out after each run, before any cleanTest).
- Deliverable: receipt at `receipts/PR-5267-replication-factor-tests-round-1.md` plus an execution completion report; the job file and the Windows implementation claim stay unchanged, per the job.
- Claimed: 2026-11-02 (America/Denver). Idempotency key: `pr-5267-replication-factor-tests-round-1-exec@b244ae94d6c84eec06bf1de5754e566b0fdb88d1`.
