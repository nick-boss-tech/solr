# Skip record: SOLR-13416

- Ticket: SOLR-13416, Overseer node getting stuck.
- Verdict: keep-open.
- Draft branch: solr-13416-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier C4 reading (vm2, 2026-10-19) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-04-22, updated 2019-06-08, no fix versions. The report: a privately reported case where the Overseer stops processing operations until a new Overseer is elected, with a LockTree warning logged at the time. The 2019-04-22 comments (caomanhdat) list hypotheses and say none is strong enough, and point to an OverseerTaskProcessor change from SOLR-12801; a second comment (ichattopadhyaya) says the observed case may have been dropped messages, not a hang, and links it to SOLR-13410.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/cloud/LockTree.java:93-126`: `Session.lock` returns null and marks the path busy when the lock cannot be taken (`:123-126`); a failed path stays marked until the session is replaced, so later tasks do not overtake it.
- `LockTree.java:56-60` (`LockImpl.unlock`): releases the node under `synchronized (LockTree.this)` and removes the lock from `allLocks`.
- `solr/core/src/java/org/apache/solr/cloud/OverseerTaskProcessor.java:375` and `:380`: the lock is released when marking the task as running fails (`NodeExistsException`, `InterruptedException`).
- `OverseerTaskProcessor.java:636-637`: the `finally` of the execution path calls `lock.unlock()`, so a task that throws releases its lock.
- `lock_is_leaked` (the warning the ticket names) has no match in `solr/` at the pin. It was removed by `3e42286e9e` (SOLR-14546, "OverseerTaskProcessor can process messages out of order", 2020-06-23), an ancestor of the pin. That commit changes ordering, not the lock-release paths above; it is not a fix for the stuck report.

Not read in this pass: the code between taking the lock and the `try` at `OverseerTaskProcessor.java:365`, where an exception not caught there could skip `unlock`. Whether that path can throw is the open question for a leaked lock.

## History

- `3e42286e9e` (SOLR-14546): removed the leaked-lock warning and changed message ordering. Ancestor of the pin.
- No commit at the pin names SOLR-13416 (evidence tool: none).

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- The leaked-lock explanation is narrowed, not ruled out: every release path read (:375, :380, :636-637) unlocks, and the gap is the code between lock acquisition and the `try` at :365.
- The reporter's stuck symptom is not explained by the code read. The 2019 comments suggest dropped messages (SOLR-13410), which is a different mechanism; that is not checked at the pin.
- Missing piece: a trace of one Overseer with a stuck queue (the lock tree state and the task queue at the time), or a run that throws between lock acquisition and the `try` to see whether the lock is released.

## Verdict justification

keep-open: no commit names the ticket, the symptom is not reproduced, and the code read narrows the leaked-lock explanation without ruling it out. A close verdict needs a run showing the Overseer does not stall under the same conditions (close-cannot-reproduce) or a fix commit (close-fixed). A pr-needed verdict needs the throw path and its line.

Missing for a closing or PR verdict: a trace or run as described above, with its log path.

## Limits

This record does not establish a reproduction. It does not check the code between the lock acquisition and the `try` at :365 in detail. It does not post anything to JIRA or GitHub.
