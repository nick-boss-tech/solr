# Report: SOLR-16065 phase 1, run checkIfIamStillLeader outside a thread

- Ticket: https://issues.apache.org/jira/browse/SOLR-16065 (Open,
  unassigned, reporter Houston Putman, 2022). The ticket asks
  whether the leader check at the end of Overseer.run() still
  needs its dedicated thread, and for it to run without the
  extra thread if it can.
- Assignment:
  assignments/solr-16065-overseer-leader-check-thread-round-1.md
  (phase 1, this report; phase 2 gate under a separate claim).
- Branch: nick-boss-tech/solr:solr-16065-submit, head
  49f0f1de22e74942d4368ea0e85d0591558ac999, base
  83d560030fde. The head is content-identical to the vm2 branch
  solr-16065-overseer-exit-main at 94d9e6a80657; the only change
  from that commit is the author/committer address, corrected to
  Nick Shanin <nick.boss.us@gmail.com> to match the standing
  authorship rule. The vm2 branch is left untouched.
- No PR, no Jira post. Both wait for Nick's explicit go.

## Census

~/workspace/tools/solr-work.sh SOLR-16065 at this run found: no
prior board receipt, no board draft, one fork branch
(solr-16065-overseer-exit-main), no GitHub PR, no local
worktree. The vm2 gate handoff
(gates/SOLR-16065-overseer-exit-gate-round-1.md) is the only
prior implementation record, and it states phase 1 was not
finished. This report closes that gap.

## Verdict: the thread is NOT needed; the premise holds

At base 83d560030fde, Overseer.java line 396 carried the comment
"do this in a separate thread because any wait is interrupted
in this main thread", and run()'s finally spawned a daemon
"OverseerExitThread" running checkIfIamStillLeader. The "main
thread" is the ClusterStateUpdater thread (updaterThread,
started in Overseer.start()). What interrupts it, at base and
at head, is exactly one caller: Overseer.doClose(), which
closes the updater thread (setting ClusterStateUpdater.isClosed)
and then calls updaterThread.interrupt(), then joins it with no
timeout. No other interrupter of that thread was found in
Overseer.java.

So the old comment was true but incomplete: the thread's waits
are interrupted only by its own shutdown path, and the finally
block runs after that interruption. The fix clears the flag
(Thread.interrupted()) and then runs the check inline, which
removes the only reason for the separate thread:

- If run() exits via an InterruptedException catch, that catch
  re-sets the flag and returns, so the finally block starts
  with the flag set. If it exits via the isClosed flag, the
  flag may still be set from doClose()'s interrupt(). In both
  cases Thread.interrupted() clears it before any ZK call, so
  the check's waits are not aborted. Clearing the flag has no
  downstream effect: the thread exits right after the finally
  block.
- checkIfIamStillLeader() itself is exception-guarded: a failed
  getData returns after a warning, a failed delete is logged,
  and the rejoin-election step in its finally block is wrapped
  in try/catch. A ZK failure therefore cannot break shutdown.
- During a full container shutdown the check is a no-op: it
  returns at once when the CoreContainer is shut down or the
  ZkController is closed, and it skips rejoining the election
  when shutting down. The inline check only does real work when
  the overseer stops while the container stays up, which is the
  case the old thread served.
- The old separate thread was daemon and leaked by design: the
  test-framework thread filter carried a dead
  "OverseerExitThread" ignore entry for it (CURATOR-720), which
  this change removes along with the thread.

## The one behavior change, stated plainly

doClose() joins the updater thread with no timeout, so
Overseer.close() now waits for the inline check (getData, a
possible leader-node delete, and a rejoin-election attempt)
instead of returning while a daemon thread did that work
asynchronously. That makes close() synchronous with leader-node
removal, which is an improvement for callers that close and
then act, but it means a ZK call that blocks during shutdown
now blocks close() rather than running in the background. The
check's exception guards and its shutdown no-op make a hang
unlikely, but the bound is ZK's own behavior, not a timeout in
this code. This tradeoff is acceptable for the ticket's ask
and is the point a reviewer should confirm; it is not a reason
to keep the thread, because the thread did not bound the wait
either, it only hid it from close().

## What the change is (4 files, base to head)

- solr/core/src/java/org/apache/solr/cloud/Overseer.java:
  the finally block in ClusterStateUpdater.run() now clears
  the interrupt flag and calls checkIfIamStillLeader() inline
  instead of starting OverseerExitThread.
- solr/core/src/test/org/apache/solr/cloud/OverseerTest.java:
  new testOverseerExitRemovesLeaderNodeBeforeClose, which
  elects an overseer, asserts the leader node exists, closes
  the overseer, and asserts the leader node is gone.
- solr/test-framework/src/java/org/apache/solr/SolrIgnoredThreadsFilter.java:
  the dead OverseerExitThread ignore entry is removed.
- changelog/unreleased/SOLR-16065-overseer-exit-check-inline.yml:
  new entry, parses as plain YAML (checked this run).

## Test evidence, and what it does NOT prove

- Prior vm2 verification at 94d9e6a80657 (same content):
  changelog YAML parses, Error Prone compile succeeded, and
  focused OverseerTest (14 run, 1 skipped, 0 failures) plus
  OverseerRolesTest (1 run, 0 failures) passed at seed
  16065C0FFEE16065. Those run logs were overwritten by a later
  vm2 run, so phase 2 must rerun them with kept logs and fresh
  JUnit XML; this report does not present the vm2 counts as
  phase 2 gate evidence.
- Fresh this run: root `./gradlew tidy` rc=0 on the final
  tree, git status clean afterwards (log
  ~/workspace/tools/16065-phase1-tidy.log). No tidy commit was
  needed.
- Fail-before: NOT reproduced, and phase 2 must not claim it.
  The new test passed on the base Overseer.java in 3 of 3 vm2
  runs, because the old daemon thread still deleted the leader
  node fast enough after close() returned. The test is a
  behavioral pin for the inline ordering (the node is gone
  when close() returns), not a discriminating proof pair. A
  deterministic version would need to hold the exit check on
  a latch; that was not built, because the pin plus the
  existing suite covers the change honestly and a latch would
  be test-only machinery around production code.
- Test base class decision, recorded explicitly: the new
  method lives in OverseerTest, which extends SolrTestCaseJ4.
  It is a method in an existing J4 class, not a new J4 class,
  and moving it would duplicate that class's whole cluster
  harness (MockZKController, ZkTestServer, overseer list) for
  a pin test. It stays where it is; a reviewer may still
  flag the J4 base, and that is noted here rather than hidden.

## History limitation

The local clone's history is truncated at the June 2026 tree
import commit 454ef4b24f, so the commit that originally wrote
the "separate thread" comment cannot be traced locally (blame
stops at that import). The verdict above therefore rests on
the current code's mechanics, which are fully cited, not on
the comment's origin.

## Phase 2 gate instructions

Gate the exact head named above (49f0f1de22e), under a
separate claim, with the full module gate: tidy clean
(formatting-only tidy diffs get the standard tidy commit and
re-gate), Error Prone compile (-Pvalidation.errorprone=true),
the focused tests named here (OverseerTest,
OverseerRolesTest), and :solr:core:check -x test, all with
kept logs and fresh JUnit XML. The receipt goes to
receipts/SOLR-16065.md and must state that fail-before was
not reproduced and that the new test is a pin, not a proof
pair.
