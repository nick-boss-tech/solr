# Skip record: SOLR-15522

- Ticket: SOLR-15522, Service Solr Stop WAITING (parking) Removed solr.pid Files. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-07-07, updated 2021-09-28, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-15522-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 4, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

On Solr 8.6.3 with external ZooKeeper, `service solr stop` hung twice. A thread dump showed an executor thread parked on a LinkedBlockingQueue (WAITING, parking) and the process was killed. The reporter says the solr.pid files in SOLR_PID_DIR were removed, which the reporter calls critical because the start and stop scripts depend on them.

## Code reading

- solr/bin/solr:513-566, `stop_solr`. The script sends the STOP.PORT command (:525), then polls `ps` for the pid up to SOLR_STOP_WAIT seconds (:526-545).
- solr/bin/solr:547: `rm -f "$SOLR_PID_DIR/solr-$SOLR_PORT_LISTEN.pid"` runs right after the wait loop, whether or not the process has exited. If the wait times out, the pid file is removed while the Solr process is still alive.
- solr/bin/solr:556-566: if the process still runs, the script takes a thread dump (JSTACK or JATTACH), then sends kill -9 (:564) and removes the pid file again (:566).

So the removal of the pid file during a hung stop comes from the design at :547 and :566, not from a stray delete. The "WAITING (parking)" thread in the dump is an idle executor thread, which fits the thread dump the reporter saw; it is not the cause of the removal by itself.

## History

No commit history was used for this record. The ticket names no fix, and the fork log search for SOLR-15522 returned no commits.

## Reproduction attempts

Not attempted in this pass. A reproduction needs a built Solr distribution with a node that does not exit within SOLR_STOP_WAIT, then `bin/solr stop` and a check of the pid file and the process. That needs a built binary distribution, which this worktree does not have.

## Gap analysis

The code path shows that a hung stop removes the pid file while the process is still running, until the kill -9 path. That matches the symptom the reporter describes, but it is a code-path argument, not a run. Nothing on main changes this behavior, and nothing makes the ticket obsolete. Whether removing the pid file before the process exits is a defect or an intended cleanup is a maintainer question: the kill -9 path is documented at :511.

## Verdict justification

keep-open: the attempts made are the code path reads above at the pinned head and the Jira read. The missing piece is a run on a built distribution: start a node, make the stop wait longer than the node takes to exit (or make it hang), and check whether the pid file is gone while the process is still alive. If the run confirms it, the next verdict is pr-needed with the change to move the removal after the process exits.

Missing for a closing or PR verdict: (1) the stop run with the pid file and process state recorded; (2) whether removal before exit is the intended behavior.

## Limits

This record does not reproduce the stop hang. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
