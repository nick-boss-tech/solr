# Claim: PR 5301 reconnect liveness gate, round 3 (execution)

Status: CLAIMED
Host: phone-agent (Linux, this VM)
Job: gates/pr-5301-reconnect-liveness-round-3.md
Repair: claims/pr-5301-reconnect-repair-windows-round-3.md / reports/pr-5301-reconnect-repair-round-3.md

Candidate H3: dfde9bde0a348ad44e6f6ceec197d478ef11bfd0 on fork branch solr-18623-reconnect-liveness, verified against the fork refs read-back before claiming. Parent H2: 27a9cf105b25f1bf3d604534cf8bdd8674c470fc (failed gate, receipt preserved). Contributor H0: 01e820f0539cddc4697d26176d216614a776a99a, PR 5301, base branch_9x. Fixed seed: 5301C0DE. Builds via solr-gradle8.sh (Gradle 8.10, vendored JDK 21), serialized under the host build lock.

Scope per the job: tidy preflight, changelog generation, validateSourcePatterns, Error Prone compile with the documented warning-tolerance option (baseline FloatRangeFieldTest warning only), ordering and client suites, the repaired stress test twice at the failed seed, root check, the controlled flap comparison, and both original negative legs at H3. Receipt: receipts/PR-5301-reconnect-liveness-round-3.md (this host is its only writer). No public comment, body edit, or contributor-branch write.
