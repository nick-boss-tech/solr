# SOLR-15106 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-15106 - "Thread in OverseerTaskProcessor should not 'return'" (Mathieu Marie, 2020). The old skip note said "stale, 0 comments, overseer session-expiry recovery is design-sensitive". Reopened in audit round audit-1 (Tier 1 batch 8).
- Branch: `solr-15106-submit` off `apache/solr` main `9b3a84b1c46`

## The bug, as understood
After a long ZooKeeper outage the reporter saw the Overseer's cluster-state queue drain again but the Collection API (OC) queue stay stuck on the same Overseer; bouncing the Overseer node cured it. `OverseerTaskProcessor.run()` ends its loop with `return` when it catches `KeeperException` with code `SESSIONEXPIRED` (and on `InterruptedException`), and its `finally` only closes the processor itself. The sibling thread, `Overseer.ClusterStateUpdater`, handles the same situation differently: when its loop exits, the `finally` starts `checkIfIamStillLeader`, which deletes this node's leader znode if it still holds it and rejoins the election. Nothing equivalent exists for the processor thread, so a node can remain Overseer (the updater is healthy) with no thread dequeuing the OC queue.

## Design choice and why
The ticket suggests the thread should not `return`. Looping on a dead session would just spin on errors, and the existing design (Overseer is replaced when a thread gives up) is sound if the other thread is made to follow. So: reuse the updater's existing exit path rather than build a second recovery mechanism.

- `Overseer.start`: the collection processor `OverseerThread` is created as an anonymous subclass; after its `run()` returns, if neither the thread nor the Overseer was asked to close, it calls `stopStateUpdaterAfterProcessorExit()`, which closes and interrupts the updater thread. The updater's own `finally` then runs `checkIfIamStillLeader`, i.e. leader znode removal and rejoin of the election.
- `OverseerThread.close()` now records `closeRequested` before closing the wrapped runnable, so `doClose()` (restart, shutdown) never triggers the hook.
- The hook is deliberately not synchronized: `Overseer.close()` holds the monitor while joining the processor thread.
- `Overseer.getCollectionProcessorThread()` is added for tests, like `getUpdaterThread()`.
- Rejected: restarting only the processor thread in place (needs a fresh `OverseerTaskProcessor`, prioritizer and metrics context; a full re-election is simpler and already exercised).

## What the branch changes
`Overseer.java` (above) and a new test `OverseerProcessorExitTest`: closes the active Overseer's processor runnable (which makes its `run()` end by itself), waits up to 60s for an Overseer whose collection processor thread is a different, live thread, then creates a collection to prove the Collection API works.

## Guesses to verify first
1. `cluster.getOpenOverseer()` returns the active Overseer and the post-election Overseer (possibly the same node) exposes a new `ccThread`; `OverseerTaskProcessor.run()` loops `while (!this.isClosed)`, so `thread.close()` ends it without an exception.
2. With the distributed Collection API enabled the processor thread still exists and `thread.close()` still ends it (the test should not depend on the mode).
3. `IOUtils.closeQuietly(updater)` followed by `interrupt()` is enough for the updater loop to hit its `finally` (the same pair is used by `doClose()`).
4. Possible double rejoin if both threads end at the same moment (session expiry): `rejoinOverseerElection` is expected to tolerate it, but this was not checked.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.cloud.OverseerProcessorExitTest" --tests "org.apache.solr.cloud.OverseerTest"
```
Fail-before: remove the `if (!isCloseRequested() && !closed)` block in the anonymous `run()`; the new test should time out waiting for a new processor thread.

## Not done
No JIRA comment, no PR.
