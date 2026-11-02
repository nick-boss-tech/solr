# Claim: PR 5107 at F4, legs that cannot run on the phone agent host (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `gates/pr-5107-f4-unrun-legs-round-1.md`.
- Slice: the whole gate at F4 `7aa3f77bed263728a13f361d9f371bf16937672c`: exact-head basics (tidy, compileJava with Error Prone, root `check -x test`), both Java suites plus `testCredentialsOptionIsAccepted`, packaging BATS in both parser modes as the non-root account, and the matched pairs (original production, shell, F4 Java pair cited from its report).
- Capability tags used: gate, records. The gate states no Tags line. BATS is not on PATH on this host; the gate's seeded-dependency route (downloadBats from the GitHub archives) is the one used, and the receipt records the source and result.
- Started: 2026-11-02T20:01:23Z (UTC). Account uid 1000 (non-root).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr-5107-f4-unrun-legs-round-1@7aa3f77bed263728a13f361d9f371bf16937672c.
- Preflight (before claim): uid 1000; `/` 4.6G free, inodes 91% free; `openssl` 3.0.2; `curl` present; keytool from the vendored JDK at /workspace/jdk/jdk-21.0.12.1+1/bin/keytool (not on PATH); localhost connect to an unused port refused (ConnectionRefusedError, errno 111), so no intercept; F4 verified by ls-remote on the fork and present in clone history.
- Runner: /workspace/tmp/5107-r3/ (standalone clone-F at F4, clone-P for the pairs). Logs: /workspace/gates/logs/vm6/pr-5107-f4-unrun-legs-round-1/.
- Build lock: /workspace/tools/.gradle-vm6.lock, taken by pool/bin/sweep-run.sh.
- Links: Ticket or parent: SOLR-8442, PR 5107, fork branch nick-boss-tech/solr:solr-8442-security-review-r2-submit. Category: gate. Siblings: gates/pr-5107-security-integration-round-3*.md (PARTIAL at F2, handed off), assignments/pr-5107-example-rerun-diagnostic-round-1.md. Supersedes: none.

Heartbeat: 2026-11-02T20:01:23Z
