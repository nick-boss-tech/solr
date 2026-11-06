# Claim status: SOLR-18602 alias opt-out control test, round 1 (phone-agent-2)

Status: SUPERSEDED BEFORE TESTS; no proof was run at the old head.

- Original immutable claim: `claims/pool-phone-agent-2-gate-pr-5259-solr-18602-alias-optout-control-test-round-1.md`.
- Original gate: `gates/pr-5259-solr-18602-alias-optout-control-test-round-1.md`.
- The only round-1 execution was the detached `tidy` preflight at F1 `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7`. Gradle completed successfully, but the preflight returned 3 because tidy reformatted one `expectThrows` call in `DeleteReplicaTest.java`. No compile, focused test, or root check ran at F1; no round-1 proof receipt is being claimed.
- The exact formatter output was self-reviewed as formatting-only and committed by Nick Shanin as `2efbd9884eac22cc8ddd0bdad2f8715f800eebee`, a direct child of F1. The commit changes no expression or behavior and was plainly fast-forward pushed to the same fork branch; the remote ref read back at F2.
- Superseding exact-head gate and claim: `gates/pr-5259-solr-18602-alias-optout-control-test-round-2.md` and `claims/pool-phone-agent-2-gate-pr-5259-solr-18602-alias-optout-control-test-round-2.md`. A new detached preflight at F2 completed with exit 0 and a clean tree before any proof test.
- F1 preflight raw logs and its tidy patch remain preserved under `/workspace/gates/logs/phone-agent-2/pr-5259-solr-18602-alias-optout-control-test-round-1/preflight/`; F2 preflight evidence is under the round-2 evidence root.

This status record does not mark the gate GREEN and does not alter the original claim or gate file.