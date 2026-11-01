# Verdict: SOLR-13416, Overseer node getting stuck

Verdict: STILL LIVE on main, promote to implementation, not closure.
The ticket's 2019 hypotheses are all retired by later code, and the
ticket's own named warning is gone, but one unguarded window remains
in the dispatch path, and it reproduces the ticket's symptom shape
exactly: a lock leaked for the Overseer process's lifetime, blocking
that lock path until a new Overseer is elected. The fix is narrow:
guard the submit window so a failed submission unlocks. Nothing
closes without a maintainer's or Nick's go.

Ticket: https://issues.apache.org/jira/browse/SOLR-13416 (2019,
reporter and assignee Cao Manh Dat, Open, Major, last updated
2019-06-08, no fix version, no links). A privately reported case
where the Overseer stopped processing operations until a new
Overseer node was elected, with a LockTree `lock_is_leaked` warning
logged at the time and a delete-collection task failing with
"Could not find collection". The reporter's own 2019-04-22 comment
lists three hypotheses and says none of them is strong enough, and
points at an OverseerTaskProcessor change from SOLR-12801. A second
comment (ichattopadhyaya, same day) says the case they observed may
have been dropped messages rather than a hang, and links it to
SOLR-13410; that dropped-messages mechanism is a different
explanation and is not checked here.

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29).
The solr/ paths are unmodified there; the checkout is shallow (363
commits back to June 2026), so no whole-history claim is made here.
No source file on main references SOLR-13416.

What retired the 2019 hypotheses:

- Hypothesis 1 (runningTasks never removed, hitting the 100 cap and
  sticking): retired. The main loop calls cleanUpRunningTasks every
  iteration, removing every done future from runningTasks, and waits
  on waitLock while the size is over MAX_PARALLEL_TASKS (100), with
  Runner notifying waitLock in its finally block. A failed
  processMessage still completes its future, so it still gets
  cleaned up.
- Hypothesis 3 (a lock never unlocked when something other than
  NodeExists or InterruptedException is thrown, or when the Runner
  is not executed): retired for the Runner path. Runner.run
  unlocks in a finally block, on every path including KeeperException
  and InterruptedException, and resets the task for retry when it
  did not succeed.
- The lockTree.clear() bug the reporter found (lockTask clearing
  whenever runningTasks hit zero because sessionId never updated):
  retired. lockTask now creates a fresh LockTree Session per batch
  (batchSessionId increments each iteration, sessionId is updated
  to match), and no clear() call remains on that path.
- The ticket's named warning, `lock_is_leaked`: gone from solr/ at
  this checkout. Per the pool record on fork branch
  solr-13416-draft-open, it was removed by 3e42286e9e (SOLR-14546,
  OverseerTaskProcessor ordering, 2020-06-23), which changed
  ordering rather than fixing a stuck Overseer.

What is still open: the submit window. In OverseerTaskProcessor's
dispatch loop, after lockTask returns a lock, the guarded try covers
only the runningMap put (NodeExists and InterruptedException both
unlock and continue). Then, outside any try that unlocks:

- Runner runner = createRunner(...);
- Future<?> future = tpe.submit(runner);
- runningTasks.put(head.getId(), future);

If tpe.submit throws, the throw lands in the loop's outer catch,
which logs "Exception processing" and continues. The lock is never
unlocked, the task is never put in runningTasks, and the task stays
in the work queue. The next iteration builds a fresh batch session,
but a new session does not release a node lock held by the old one
(LockTree Node.lock returns null while myLock is set, and only
unlock clears it), so every later task on that lock path is
re-blocked, iteration after iteration, for the life of this
Overseer process. A new Overseer election builds a fresh LockTree
and clears it. That is the ticket's symptom, produced by this
window.

Reachability, stated plainly: in the steady state this throw is
hard to reach. Dispatch stops submitting once runningTasks reaches
100, and each submitted Runner holds one of at most 100 threads
against a SynchronousQueue, so a submission normally finds a free
thread. The realistic triggers are a shutdown race (close() landing
mid-iteration, so the executor rejects) or a thread-creation
failure. The window is real and unguarded, but the 2019 incident
itself is not reproduced here, and no run shows this throw firing.

## Existing pool record (agrees, and this verdict closes its gap)

Fork branch solr-13416-draft-open at cadded37454 (pool record,
"remediation redo, keep-open") read the same release paths and left
exactly one open question: the code between lock acquisition and
the guarded try, where an exception could skip unlock. This verdict
answers it: the guarded try covers the runningMap put only, and
the createRunner/submit/put sequence after it is the unguarded
stretch. The narrow implementation item is to unlock on a submit
failure (or move the submission inside the guarded try), with a
test that drives a rejecting executor and asserts the lock path
frees. No PR, no board receipt, and no draft exist for this
ticket beyond that pool record.

## Verdict justification

STILL LIVE, promote to implementation, not closure: the reported
hang mechanism has one surviving path, narrow enough to fix and
test, and no fix commit or reproduction run closes the ticket.
A close verdict would need either a fix commit naming the ticket
or a run showing the Overseer does not stall under the same
conditions; neither exists. The dropped-messages alternative
(SOLR-13410) is unexamined and does not cancel the unguarded
window found here.
