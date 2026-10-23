# Skip record: SOLR-11787

- Ticket: SOLR-11787, "StackOverflowError in leader election". JIRA status read 2026-10-23 (UTC): Open, resolution None, created 2017-12-21. One comment (2024-06-08, Christos Malliaridis) asks whether this duplicates SOLR-6213.
- Verdict: duplicate of SOLR-6213, keep open. This replaces the close-obsolete verdict of 2026-10-20 (`claims/skip-SOLR-11787-vm2.md`), which rested on the log4j 1.x frames in the stack trace.
- Draft branch: solr-11787-draft-close (the branch name is kept; the drafts on it now carry the keep-open verdict).
- Pinned head: b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb (origin/main), the base the SOLR-6213 drafts cite.
- Checked by: phone-agent, 2026-10-23, under `assignments/solr-11787-reverdict-round-1.md`.

## Ticket reading (trace reread from JIRA on 2026-10-23)

The description's trace (Solr 7.x, thread coreZkRegister-1-thread-1) repeats one cycle at least three full times before it is cut off:

- `ShardLeaderElectionContext.runLeaderProcess` (ElectionContext.java:435)
- `LeaderElector.runIamLeaderProcess` (LeaderElector.java:170)
- `LeaderElector.checkIfIamLeader` (LeaderElector.java:135)
- `LeaderElector.joinElection` (LeaderElector.java:307, then 216)
- `ShardLeaderElectionContext.rejoinLeaderElection` (ElectionContext.java:729)
- back to `runLeaderProcess`.

Inside each round: `SyncStrategy.sync` (108), `syncReplicas` (129), `syncWithReplicas` (180), `PeerSync.sync` (348), `PeerSync.handleResponse` (485). That is the rejoin loop: a leader process whose sync does not complete rejoins the election on the same thread, and each rejoin nests the whole process one level deeper.

The log4j 1.x and `java.text` frames sit at the top of a single round: `handleResponse` logs a warning through the slf4j log4j adapter, the pattern layout and date formatting (`Category.forcedLog`, `WriterAppender.subAppend`, `DatePatternConverter`, `SimpleDateFormat`, `DecimalFormat`). They appear once. They are the call the thread was inside when the stack ran out, not a cycle of their own.

## Code reading (pinned head)

- The rejoin loop is still on main: `ShardLeaderElectionContext.rejoinLeaderElection` calls `joinElection` on the same thread (ShardLeaderElectionContext.java line 494 at b73f03976f), and a test that forces failed syncs shows the stack growing 12 frames over 4 rejoins (`pr-drafts/SOLR-6213/jira-comment.md`; the test branch is `solr-6213-rejoin-fix-test` at 64624f4726f).
- The 2026-10-20 grep result still holds: no `org.apache.log4j` (log4j 1.x) under `solr/` at the pinned head, so the trace cannot recur in its exact form. What changes is the conclusion: removing the logging stack removes the frames at the overflow point, not the loop that consumed the stack.

## Why the earlier verdict does not stand

close-obsolete treated the frames at the overflow point as the failure mechanism. The repeating frames are the leader-election cycle, and that cycle is the SOLR-6213 mechanism, still present on main. The re-check's change condition (the trace cannot reach the rejoin path on main) is not met: the trace is the rejoin path.

## Verdict justification

duplicate of SOLR-6213, keep open: same call shape and same loop, and SOLR-6213 is open with the loop unfixed on main. The 2024 comment on this ticket asks the same question. The draft comment answers it; nothing is posted and no duplicate link is set without Nick's approval.

## Limits

- The SOLR-11787 overflow itself is not reproduced. A real overflow needs a long run of failed syncs, and each rejoin waits at the leader throttle (5 seconds on main). The evidence is the frame mapping plus the SOLR-6213 growth test, not a new run of this ticket's scenario.
- Line numbers in the trace are the 7.x numbers from 2017; the mapping to current main is by method and call shape (ElectionContext.java is now ShardLeaderElectionContext.java).
- Drafts only: no Jira post, no ticket edit, per the assignment.
