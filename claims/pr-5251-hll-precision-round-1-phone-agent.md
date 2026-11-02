# Claim: PR 5251 HLL precision gate, round 1 (phone agent)

- Claimant host: phone agent (Linux, this VM); tags gate, premise-run, records.
- Job: `gates/pr-5251-hll-precision-round-1.md` (immutable; add-only claim).
- Target: `nick-boss-tech/solr:solr-18598-submit` at exact head F `e7834ca954505c27778e59a61140bf02490226a6` (fork ref re-read at F at claim time); contributor head P `ca539cad196f26c78e14a1dc698b84e0f5c1f22e` is F's parent and the fail-before base.
- Setup: fresh full clone for F (`~/workspace/solr-clones/5251-gate`) so the root check and ref-guide legs avoid the worktree Antora trap; fail-before in a detached worktree at P with only the two test files overlaid from F. JDK 21.0.12.1+1, Gradle 9.7.0 via solr-gradle.sh, builds serialized through the host test-queue lock. Root check leg uses the host's combined truststore for the sandbox egress CA, recorded in the receipt. Known risk carried in the open: the ref-guide checkSiteLinks leg needs Node/npm tooling and this VM's npm egress has failed for other modules; if it fails there, the receipt names it and the verdict is PARTIAL at best.
- Logs and XML: `~/workspace/tools/5251-gate-logs/`.
- Deliverable: receipt at `receipts/SOLR-18598.md`.
- Claimed: 2026-11-02 (America/Denver). Idempotency key: `pr-5251-hll-precision-round-1-exec@e7834ca954505c27778e59a61140bf02490226a6`.
