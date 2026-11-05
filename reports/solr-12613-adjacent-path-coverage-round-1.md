# Encoded-path second-hop coverage implementation, round 1

Status: Implemented, Proof Pending.

- Fork branch `pr-5110-followup-path-coverage`; exact pushed/read-back head `96747864862bf716daf451e02a7c24194d41c267`.
- Integration baseline `6ec2e527ad716bde74404b2037bb1a1e6c52f79c`, joining current main `07ccc73cd24e26e59da169ca35b3fd5f64c35986` and PR 5110 head `73b30956ff2c0f7b16f6af3bceacf95bda8271b9` without conflicts. This branch is independent of the encoded-key fix branch.
- Gate `gates/solr-12613-adjacent-path-coverage-round-1.md`; receipt `receipts/PR-5110-adjacent-path-coverage-round-1.md`.
- Only outbound file: `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java` (37 added lines).

Two tests use the existing collection/echo fixture to exercise header and query directives through a non-replica node and back to its replica. Assertions check exact encoded URI, actual forwarding count, unchanged query data/foreign header and consumed directive. No production bug is asserted. Both baseline and candidate suites are expected to pass; the Linux gate specifies an isolated valid-path mutation to prove the second-hop URI assertion.

Nick Shanin authored the ordinary scoped commit; it was plain-pushed to the fork and separately verified. Diff and diff --check were reviewed, no runtime/Gradle/test execution occurred on Windows. No new test fixture, changelog, public issue, PR or comment was created. Other implementation areas remain active under `claims/solr-12613-adjacent-windows-round-1.md`.
