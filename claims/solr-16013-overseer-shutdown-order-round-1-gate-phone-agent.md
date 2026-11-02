# Claim: SOLR-16013 Overseer shutdown order, phase 2 follow-up (phone agent)

- Claimant host: phone agent (Linux gate host, JDK 21, Gradle 9.7.0 via ~/workspace/tools/solr-gradle.sh, serialized on ~/workspace/tools/.solr-test-queue.lock).
- Status: CLAIMED.
- Job: assignments/solr-16013-overseer-shutdown-order-round-1.md, phase 2 follow-up after vm6's handoff at NEARLY (2026-11-02T01:47Z). vm6's claim (claims/solr-16013-overseer-shutdown-order-round-1-gate-vm6.md) is Status HANDED with "No further runs by vm6"; this claim takes the handed-off slice only, it does not rerun vm6's green legs.
- Slice: run the ordering method alone, at base and at head, same seed, to separate the premise from the fixture interaction vm6 found:
  - Method: OverseerCloseOrderingTest.testZkClientClosesOnlyAfterOverseerCloseCompletes, alone (not the full 3-method class).
  - Base leg: worktree at fork head 37b01337eb4c6d5af6d2a6fdf14af3a2ff2d75a7 with ONLY solr/core/src/java/org/apache/solr/cloud/ZkController.java reverted to base 14c7aac0d151, branch test file kept, never committed.
  - Head leg: same worktree restored to 37b01337eb, same method, same seed.
  - Seed: 2E201C17D50DCEAA (vm6's proof-leg seed), both legs.
  - Module: :solr:core. Fresh JUnit XML per leg (cleanTest before each).
- Fork branch: nick-boss-tech/solr:solr-16013-submit, verified by ls-remote at claim time at 37b01337eb4c6d5af6d2a6fdf14af3a2ff2d75a7.
- Worktree: ~/workspace/solr-worktrees/16013-proof.
- Logs: ~/workspace/tools/16013-proof-base.log, ~/workspace/tools/16013-proof-head.log, XML copies under ~/workspace/tools/16013-proof-*.
- Prior evidence used, not rerun: vm6's receipt receipts/SOLR-16013.md (NEARLY: tidy rc=0, Error Prone rc=0, focused 3/3 at two seeds, module check BUILD SUCCESSFUL, base-revert proof 2 failures where the ordering method failed with a fixture message "no node is running the overseer" instead of the premise message).
- Deliverables: reports/solr-16013-ordering-method-alone-round-1.md with both legs' fresh XML counts, plus an addendum section on receipts/SOLR-16013.md recording the follow-up result; claim marked DONE when both are posted. No PR, no Jira/GitHub post, no push to the fork (no branch change; the base revert is never committed).
- Started: 2026-11-01 19:53 MST (this wake).
