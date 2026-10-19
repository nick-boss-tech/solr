# Skip record: SOLR-13616

- Ticket: SOLR-13616, Possible racecondition/deadlock between collection DELETE and PrepRecovery ? (TestPolicyCloud failures).
- Verdict: keep-open.
- Draft branch: solr-13616-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the earlier B3 reading (vm2, round 1) is replaced by this pass, which re-reads the ticket and the code.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-07-09, updated 2019-07-27, no fix versions. The reporter suspects a deadlock when a collection DELETE is issued while recovery is in progress, and says the cause was not located. The 2019-07-10 comments attribute the TestPolicyCloud failure to the thread that runs a CollectionStateWatcher swallowing an exception in ZkStateReader.waitForState as used by PrepRecoveryOp, and suggest auditing the watcher users. The three named test commits harden tests and do not change the code path.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java:907-940` (`sendPrepRecoveryCmd`): sends a WaitForState request to the leader with onlyIfLeader and checkLive set; the read timeout is the leader conflict wait plus a property default of 8000 ms; the request runs synchronously inside a try-with-resources client.
- `RecoveryStrategy.java:942-944` (`cancelPrepRecoveryCmd`): cancels the stored FutureTask with `cancel(true)`.
- `solr/core/src/java/org/apache/solr/handler/admin/PrepRecoveryOp.java:88-96`: calls `ZkStateReader.waitForState` with the conflict wait as its timeout.
- `PrepRecoveryOp.java:207-209`: maps a TimeoutException or InterruptedException from that wait to NotInClusterStateException.
- `solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/ZkStateReader.java:1649-1709` (`waitForState`): the watcher at :1673-1682 catches SolrException only (:1683); a second SolrException counts the latch down and ends the wait; the wait ends by latch timeout or by the rethrown SERVER_ERROR; the finally at :1709 removes the watcher.

Grep at the pin: `git log --grep=SOLR-13616` names three commits, all test hardening: 8a277cab7d (2019-07-10, TestPolicyCloud), 7ddba3b712 (2019-07-13, DeleteReplicaTest), 4050ddc59b (2019-07-26, RulesTest). A fourth match, 75b6bf69bd, is a substring match in an unrelated svn-id and is not a reference.

## History

Test hardening commits only, listed above. The history of the watcher catch at `ZkStateReader.java:1683` was not traced in this pass.

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- The paths read do not show a lock cycle between collection DELETE and PrepRecovery. Each wait on the PrepRecovery side is bounded by a timeout (PrepRecoveryOp.java:88 and :207), so a stall would show as a timeout rather than a permanent deadlock. This is an inference from the read code, not a run.
- The watcher catch at ZkStateReader.java:1683 is limited to SolrException, which is the exception-handling concern from the 2019 comments. Whether an exception there explains the TestPolicyCloud failure is not shown.
- Not close-obsolete: the code path is present. Not close-fixed: no commit names the ticket except test hardening. Not pr-needed: no defect is established.
- Missing piece: a reproduction of a collection DELETE during PrepRecovery, or the thread dump and log from one failing run.

## Verdict justification

keep-open: the ticket asks whether a deadlock exists, the paths read show a timeout-bounded wait and a narrow watcher catch, and neither a run nor a fixing commit settles the question.

Missing for a closing verdict: a run of a collection DELETE during recovery at the pin, or a thread dump from one failing TestPolicyCloud run.

## Limits

This record does not establish a reproduction, and the code reading is from the pin only. It does not post anything to JIRA or GitHub.
