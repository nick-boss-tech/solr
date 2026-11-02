# SOLR-16013 ordering method alone, phase 2 follow-up (phone agent)

Assignment: assignments/solr-16013-overseer-shutdown-order-round-1.md, phase 2.
Prior gate: receipts/SOLR-16013.md (vm6, NEARLY). Prior claim: claims/solr-16013-overseer-shutdown-order-round-1-gate-vm6.md (HANDED, no further runs by vm6).
This claim: claims/solr-16013-overseer-shutdown-order-round-1-gate-phone-agent.md.
Fork branch: nick-boss-tech/solr:solr-16013-submit, head 37b01337eb4c6d5af6d2a6fdf14af3a2ff2d75a7 (verified by ls-remote before and after; nothing pushed, no branch change).

## Question handed off

vm6's phase 2 gate was NEARLY: tidy, Error Prone compile, focused 3/3 at two
seeds, and the :solr:core:check -x test module check were all green at
37b01337eb, but the base-revert proof leg did not reproduce the ordering
premise as a clean pair. In vm6's full-class base run, the ordering method
testZkClientClosesOnlyAfterOverseerCloseCompletes failed with the fixture
message "no node is running the overseer" (60.05 s) instead of the premise
message, most likely because an earlier method left the two-node cluster
without a live overseer. The handoff asked the next claimant to run the
ordering method alone at base and at head, same seed, to separate the
premise from that fixture interaction. This report is that run. It does not
rerun vm6's green legs.

## Legs (this run, 2026-11-01 evening MST / 2026-11-02 UTC)

Worktree: ~/workspace/solr-worktrees/16013-proof, detached at the exact head
37b01337eb, clean before and after. Host: phone agent, Linux, vendored JDK
21 (21.0.12.1+1) and Gradle 9.7.0 via ~/workspace/tools/solr-gradle.sh,
serialized on ~/workspace/tools/.solr-test-queue.lock. Seed for both legs:
2E201C17D50DCEAA (vm6's proof-leg seed). Module: :solr:core. Each leg used
: solr:core:cleanTest first, so the XML below is fresh, not reused.

- Leg 1, BASE: only solr/core/src/java/org/apache/solr/cloud/ZkController.java
  reverted to base 14c7aac0d151; the branch test file kept; never committed.
  Command: :solr:core:cleanTest :solr:core:test --tests
  "org.apache.solr.cloud.OverseerCloseOrderingTest.testZkClientClosesOnlyAfterOverseerCloseCompletes"
  -Ptests.seed=2E201C17D50DCEAA.
  Result: BUILD FAILED in 2m 13s (the expected exit for a failing test).
  Fresh XML (2026-11-02T03:04:53Z, copy ~/workspace/tools/16013-proof-base-TEST.xml):
  1 test, 1 failure, 0 errors, 0 skipped.
  Failure: java.lang.AssertionError: "ZooKeeper client closed before the
  overseer finished closing". That is the premise message, in 1.5 s of test
  time (13.6 s XML suite time), not the 60 s fixture timeout vm6 saw.
  Log: ~/workspace/tools/16013-proof-base.log.
- Leg 2, HEAD: ZkController restored to 37b01337eb (worktree verified clean
  at the exact head before the run).
  Same command, same seed.
  Result: BUILD SUCCESSFUL in 1m 21s.
  Fresh XML (2026-11-02T03:07:14Z, copy ~/workspace/tools/16013-proof-head-TEST.xml):
  1 test, 0 failures, 0 errors, 0 skipped.
  Log: ~/workspace/tools/16013-proof-head.log.

Harness note: the first runner draft aborted at the restore step because
`git checkout -- <file>` restores the worktree from the index, which still
held the base version, so the tree stayed staged-modified. The restore was
redone with `git checkout HEAD -- <file>` and verified clean before leg 2.
No test result was affected; leg 1's evidence was already captured.

## Verdict

The ordering premise IS reproduced as a fail-before when the method runs
alone: base fails with the premise message, head passes, same method, same
seed. vm6's diagnosis is confirmed: the full-class base failure was the
method-order fixture interaction, not a missing premise. Combined with
vm6's green legs (tidy rc=0, Error Prone rc=0, focused 3/3 at seeds
2E201C17D50DCEAA and 16013C0FFEE16013, :solr:core:check -x test BUILD
SUCCESSFUL), phase 2's evidence is now complete for head 37b01337eb.

Limits carried from vm6 and phase 1, unchanged: the tests observe ordering
through the ZooKeeper client's state at close, not through the election
node itself, so a pass does not prove no second overseer can ever run; the
wait in close() has no timeout, so a hung overseer close would hang node
shutdown, and no test covers that. No PR, no Jira/GitHub post, no push.
