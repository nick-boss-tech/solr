# Claim: SOLR-18178 rebased-head gate, round 1 (phone agent)

- Claimant host: phone agent (Linux, this VM); tags gate.
- Job: `gates/solr-18178-rebased-head-round-1.md`.
- Target: `nick-boss-tech/solr:solr-18178-verify` at exact head `d6abc36c94687dc5744a7573bafb1ce473acc75f` (fork tip re-read at claim time); proof base `35e2be2a7de9855a0f909613065ba22243097102`.
- Setup: fresh full clone (`~/workspace/solr-clones/18178-gate`) for the head legs and root check; fail-before in a scratch checkout at the proof base with the candidate test file overlaid and only the two compile-compatibility edits the job prescribes, with the scratch diff saved as evidence. JDK 21.0.12.1+1, Gradle 9.7.0 via solr-gradle.sh, host test-queue lock. Root check leg uses the host's combined truststore for the sandbox egress CA, recorded in the receipt.
- Logs and XML: `~/workspace/tools/18178-gate-logs/`.
- Deliverable: receipt at `receipts/SOLR-18178-rebased-20261028.md`.
- Claimed: 2026-11-02 (America/Denver). Idempotency key: `solr-18178-rebased-head-round-1-exec@d6abc36c94687dc5744a7573bafb1ce473acc75f`.

## Closeout (2026-11-02)

Status: DONE GREEN. Gate passed at `d6abc36c94687dc5744a7573bafb1ce473acc75f`; receipt `receipts/SOLR-18178-rebased-20261028.md`.
