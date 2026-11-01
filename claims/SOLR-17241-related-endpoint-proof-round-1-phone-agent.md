# Claim: SOLR-17241 related endpoint proof, round 1 (phone agent)

Status: CLAIMED by the phone agent, 2026-11-01T05:20Z (approx., at claim post time). Taking over after vm2's partial run and handoff (gates/SOLR-17241-related-endpoint-proof-round-1-handoff-vm2.md); vm2's claim is released and its candidate XML stays on vm2, so the candidate probe is re-run here for local fresh XML.

- Job: gates/SOLR-17241-related-endpoint-proof-round-1.md. Idempotency key: SOLR-17241-related-endpoint-proof-round-1@86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b.
- Production head: 86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b. Verified at claim time: PR apache/solr#5266 open at that head; fork refs/heads/solr-17241-submit at that head.
- Parent control: 9859d8bb2e8d63061116b8281639a4beb1be480c, applied as the board's control-diff.patch (parent blobs of MediaTypeOverridingFilter.java and MessageBodyWriters.java; hashes in gates/artifacts/SOLR-17241-related-endpoint-proof-round-1/control-hashes.txt).
- Probe: gates/artifacts/SOLR-17241-related-endpoint-proof-round-1/RelatedEndpointProofTest.java.txt, expected SHA-256 e1fb605b8e5aca85f4a6acaf50d18412ca5c168894c65c21c7f79125c9c9b9cd, run as a test-only overlay in a disposable checkout; production files stay at the pinned head.
- Host: phone agent VM, Linux x86_64. JDK 21.0.12.1+1 (vendored). Runner: ~/workspace/tools/solr-gradle.sh under flock on ~/workspace/tools/.solr-test-queue.lock, env -u NODE_ENV.
- Worktree: ~/workspace/solr-worktrees/17241-proof (disposable). Logs: ~/workspace/tools/gate-17241-proof/.
- Seed: 17241C0FFEE17241 for the two existing suites.

Remaining work per the handoff: control probe run to a valid outcome, MediaTypeOverridingFilterTest (8) and V2ApiIntegrationTest (14) on the candidate, then the report and receipt. Candidate probe re-run first for local XML.
