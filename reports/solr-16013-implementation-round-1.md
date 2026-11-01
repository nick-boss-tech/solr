# SOLR-16013 implementation, round 1 (phase 1)

Assignment: `assignments/solr-16013-overseer-shutdown-order-round-1.md`. Claim: `claims/solr-16013-overseer-shutdown-order-round-1-implementation-vm6.md` (vm6). Date: 2026-11-01.

## Verdict

- The two closes are still unordered at current upstream main (`apache/solr` main `83d560030f`, and the shared checkout `555e1bf239`, which has the same `ZkController.java` as the branch base `14c7aac0d1`). The defect is live at main.
- The fix is already implemented on the fork branch `solr-16013-submit`, head `37b01337eb4c6d5af6d2a6fdf14af3a2ff2d75a7` (Nick Shanin, 2026-10-14). It makes the session-ending close wait for the overseer close. This meets the assignment's intent, so phase 1 adds no new commit and makes no push to the fork. Phase 2 gates that head.
- The branch delta applies to apache main `83d560030f` with no conflict (`git merge-tree` on base `14c7aac0d1`; ZkController.java differs on main elsewhere, and the close() block is at the same offsets).

## Census

- The `solr-work.sh` census tool named in the assignment is not on this host (`/workspace/tools` has no such script). The census was done by hand from the records.
- Prior work on SOLR-16013 on the archived board `coordination` (closed 2026-10-23): `receipts/SOLR-16013.md`, which records the sequence of fixes and gates. Its latest section is GREEN at `37b01337eb` (fixture gate on vm1, 2026-10-13/14). The route was approved by the owner on 2026-10-13. The earlier heads `ecda7b862b`, `4215c8cee5` (interrupt-flag handling) and the fixture commit `37b01337eb` are on the fork branch.
- Claims `claims/pool-vm2-gate-backlog-16013*.md` and `claims/pool-review-16013-head-failure-fixture-patch.md` are closed on the archived board. Nothing on the live board `coordination-ui` before this claim.
- Fork branch head checked by `git ls-remote` on 2026-11-01: `37b01337eb`, unchanged.

## Shutdown sequence as found (main)

`solr/core/src/java/org/apache/solr/cloud/ZkController.java`, `close()` (main `83d560030f`; same lines at `555e1bf239`):

1. `close()` calls `preClose()` if not already closed.
2. A cached thread pool `closeThreadPool` is created.
3. Line 881: `customThreadPool.execute(() -> IOUtils.closeQuietly(overseerElector.getContext()))`.
4. Line 883: `customThreadPool.execute(() -> IOUtils.closeQuietly(overseer))`.
5. The state-reader, cloud-manager and client closes are submitted to the same pool.
6. On the calling thread, `zkStateReader.close()` and then `zkClient.close()` (line 910) run without waiting for tasks 3 and 4. The pool is shut down and awaited only in the innermost `finally`, after `zkClient.close()`.

So the overseer close can still be running when the ZooKeeper session ends.

## Why the session end is the release point

- `ElectionContext.close()` is empty (`ElectionContext.java:52`). `ElectionContext.cancelElection()` (`:54` to `:58`) is the one that deletes the election node, and `close()` in ZkController does not call it.
- `OverseerElectionContext.close()` (`OverseerElectionContext.java:80` to `:83`) sets `isClosed` and calls `overseer.close()` synchronously. So the elector-context task closes the overseer before it returns, but it does not remove the node.
- The election node is an ephemeral node. It is removed when the ZooKeeper session ends, which is `zkClient.close()`. That is the point where another node can be elected while this overseer may still run.

## The change on the fork branch

Delta `14c7aac0d1..37b01337eb` in ZkController.java (three hunks, 36 changed lines):

- Lines 881 to 892: the elector-context close and the overseer close run inside one task submitted with `customThreadPool.submit`, as `overseerClosed` (a `Future`, line 883). Within that task the elector context closes first and then the overseer, as before. The elector context's close is what calls the overseer close synchronously (see above), so the order inside the task is the ticket's order, and the new part is the wait.
- Lines 912 to 931: before `zkStateReader.close()` (line 927), the thread waits on `overseerClosed.get()` (line 916) through interrupts (`InterruptedException` is remembered, and the loop continues), and an `ExecutionException` is logged. `Thread.interrupted()` is folded in after the wait (line 925).
- Lines 934 to 947: before `zkClient.close()` (line 936) the interrupt is folded in again (line 934), and the remembered interrupt is restored only in the innermost `finally` (lines 946 to 947), after the client close and the pool shutdown.
- `changelog/unreleased/SOLR-16013.yml` (new, type fixed).
- `OverseerCloseOrderingTest.java` (new, 276 lines, three methods, extends `SolrCloudTestCase`).

The assignment's requirement "the Overseer closes before its election context is released" holds in the sense that the session end (`zkClient.close()`) now runs only after the overseer close task has finished. The elector-context close still runs first inside the task, as the ticket describes. This is the point to check at the gate; no code change was made in phase 1.

## What the proof can and cannot observe

Can observe (from the records, to be re-run in phase 2):
- The two premise checks in `OverseerCloseOrderingTest` (`testZkClientClosesOnlyAfterOverseerCloseCompletes` and `testZkClientStaysOpenWhenShutdownThreadIsInterrupted`). With ZkController reverted to the base (`14c7aac0d151`) and the branch test file kept, both fail fast with their premise messages ("ZooKeeper client closed before the overseer finished closing" and the interrupted variant). This is the mutation proof the ladder requires; the earlier receipt reports it.
- The third method (`testZkClientClosesUninterruptedWhenInterruptArrivesAfterOverseerCloseFinishes`) exercises the interrupt fold-in. Its base behavior is recorded as observed, not as a proof.

Cannot observe, or observed only weakly:
- The race is timing-dependent. The premise tests catch the ordering through the ZooKeeper client's state at the moment of close, not through the election node itself. A pass does not prove that no second overseer can ever run.
- The third method depends on the shared two-node cluster surviving two node stops. The branch fixes that with a restart of the stopped node after each method (`37b01337eb`). The owner approved that fixture route on 2026-10-13.
- The interrupt handling rests on JDK `FutureTask.get()` returning without checking the interrupt once the future is done (JDK 21 `lib/src.zip`, `java/util/concurrent/FutureTask.java`, lines 187 to 192). That is the justification recorded on the archived board.
- The wait has no timeout. If the overseer close hangs, node shutdown now hangs with it. This is a behavior change that no test covers.

## Phase 2

Gate the exact head `37b01337eb` on vm6, in a separate claim: changelog YAML parse, tidy, Error Prone compile, `OverseerCloseOrderingTest` at two seeds, the base-revert proof, and the module check. Receipt to `receipts/SOLR-16013.md` (new on this board).

Nothing was posted to JIRA or GitHub. No PR was opened. No commit was pushed to the fork in phase 1.
