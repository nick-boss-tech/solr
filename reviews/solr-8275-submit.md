# solr-8275-submit

- Branch: origin/solr-8275-submit
- Head: e52e10fa50a3 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind, 2 commits ahead)
- Scope: 2 commits, 3 files. `PrepRecoveryOp.java` (an `AtomicReference<String> lastSeen` is set in the cluster-state predicate for the missing-shard, missing-replica, and found-replica branches; the timeout's `NotInClusterStateException` message now names the replica, the requested state, `checkLive`, and `lastSeen`), `TestPrepRecovery.java` (one new test, `testTimeoutMessageNamesLastSeenState`, for the missing-replica case), changelog `SOLR-8275-prep-recovery-timeout-message.yml` (`type: changed`). Wording and diagnostics only; the status stays `SERVER_ERROR` and control flow is unchanged.
- Verdict: Nearly (unchanged from the bulk verdict). The message is built from variables that are in scope and the status is unchanged. The one gap is the one the bulk review named: the test never reaches the found-replica branch that the ticket describes.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head. This branch is wording-level; the review stays at that level and does not look for behavior changes beyond the message.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-8275-review.md` (Nearly) reviewed this head (`e52e10fa50a`). No delta.

- Bulk F1 (MEDIUM, cover the found-replica predicate failure in the regression test): **confirmed.** `testTimeoutMessageNamesLastSeenState` sends `coreNodeName=core_node_that_does_not_exist` and asserts the missing-replica text. The found-replica branch, which records `replica state=…, node live=…, this core's published state=…` (`PrepRecoveryOp.java`, the `lastSeen.set` after `onlyIfActiveCheckResult`), is not executed by any test. The ticket's central case, a found replica observed in a state that contradicts the request, is therefore not pinned.
- Bulk claim that `lastSeen` is updated for the missing shard, missing replica, and normal predicate: **confirmed** by the diff.
- Bulk note on the changelog (accurate about the user-visible timeout improvement): **confirmed.** The changelog title matches the new message.

## Findings (ranked)

1. **MEDIUM, verified (test gap, bulk F1). The found-replica diagnostic is not tested.** The new test (`TestPrepRecovery.java:89-116`, `testTimeoutMessageNamesLastSeenState`) exercises the `replica == null` branch only. The found-replica branch sets `lastSeen` to the replica's state, node liveness, and the core's published state, and appends `(onlyIfLeaderActive requires ACTIVE)` when that check fails. None of those strings is asserted. Add a case where the replica exists and its state is not the requested one (for example, the leader's own core with `waitForState=RECOVERING` and `checkLive=true`), then assert `replica state=ACTIVE` (or the actual state) and `node live=true`.

2. **LOW, verified (wording). The message says "Timed out" for interrupts too.** The catch is `catch (TimeoutException | InterruptedException e)` (`PrepRecoveryOp.java:223`). An interrupt produces "Timed out after …ms waiting for replica …". The base had the same catch with "Timeout waiting for collection state.", so the wording now asserts a timeout on an interrupt. Interrupt status is also not restored. This predates the branch; the wording change makes it more visible. Fix: separate the two cases or say "Wait interrupted" for `InterruptedException`.

3. **LOW, verified (checked, no issue). The `AtomicReference` is needed.** The predicate runs on the ZooKeeper state-watch path, so a plain field would not be safe. The `lastSeen` default ("no collection state") covers the case where the predicate never records anything.

4. **LOW, verified (checked, no issue). Every variable in the message is in scope at the `catch`.** `conflictWaitMs` (`:68`), `coreNodeName` (`:60`), `waitForState` (`:61`), `checkLive` (`:62`), `collectionName`, and `lastSeen` are declared before the `try`. `localState` is declared inside the predicate, so the message does not use it, and the code is correct on that point. The test cluster sets `leaderConflictResolveWait` to 5000 ms (`TestPrepRecovery.java:41`), so the test does not wait the 180 s default (`CloudConfig.java:154`).

5. **LOW, verified (checked, acceptable). The message exposes cluster details to a CoreAdmin caller.** It adds node liveness and the core's published state (`PrepRecoveryOp.java`, the `lastSeen` text). The caller already has CoreAdmin rights, and these details are the ones the status page shows. The bulk review reached the same view. No new permission boundary.

## Owner calls (not decided here)

None needed. The message wording is the author's and the changelog describes it accurately.

## Proposed fixes (not applied; the owner decides)

- Finding 1: add a found-replica case to `testTimeoutMessageNamesLastSeenState` (or a second test). Use an existing replica whose state differs from the requested one, and assert `replica state=` and `node live=true` in the message.
- Finding 2: separate `InterruptedException` from `TimeoutException`, or reword the interrupt case to say the wait was interrupted, and restore the interrupt flag.

## Not checked

- Nothing compiled, formatted, or run. The new test was read, not executed.
- Whether the predicate can be called with a null collection state on the found-replica path (the code's "no collection state" default implies it can be, but that path was not traced).
- The `HttpJettySolrClient` and `CoreAdminRequest.WaitForState` imports exist at this head (`solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java`, `solr/solrj/src/java/org/apache/solr/client/solrj/request/CoreAdminRequest.java:228`). Checked, not an issue.
- The `RemoteSolrException` message format used by the test's `e.getMessage()` assertions was not traced in the client code.
