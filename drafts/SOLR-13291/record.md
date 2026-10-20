# Skip record: SOLR-13291

- Ticket: SOLR-13291, Failed to create collection due to lock held by this virtual machine.
- Verdict: keep-open.
- Draft branch: solr-13291-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-1, 2026-10-04, deferred) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-03-04, updated 2019-06-08, no fix versions. Link: SOLR-13021 (Open). The report: a workload deletes and re-creates a collection with the same name in a short time, and a core is occasionally left locked, so the re-create fails with a "lock held by this virtual machine" error. The one comment (erickerickson, 2019-03-04) says it may be related to SOLR-13021, which is in the tests.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteCollectionCmd.java:115-116`: the delete sends a core UNLOAD with `DELETE_INSTANCE_DIR` set to true for each replica. The unload is issued per replica through the core admin path. The code read does not show the delete waiting for the core's index lock to be released. The comment at :129 covers the case where a replica on shared storage does not receive the unload.
- `DeleteReplicaCmd.java:301` (`UNLOAD`) and the wait at :325 ("check if the core unload removed the corenode zk entry") show the replica-side check is on the ZooKeeper entry, not on the Lucene directory lock.
- The message "lock held by this virtual machine" is the JDK's `OverlappingFileLockException` text. Solr code does not contain it (`git grep -n 'virtual machine' ... -- solr/core/src/java` shows only an unrelated `SystemInfoProvider` comment).
- `solr/core/src/test/org/apache/solr/cloud/CollectionsAPISolrJTest.java:311-312`: `testCreateAndDeleteShard` is `@AwaitsFix(bugUrl = "https://issues.apache.org/jira/browse/SOLR-13021")`. It is a shard create-and-delete test, not the collection delete-and-recreate of this ticket, and it is disabled at the pin. The annotation came in `75b1831967` (SOLR-12801, 2018-11-29).

So the code shape allows a re-create to race a still-running unload (no lock-release wait is seen in the path read), which is consistent with the report. That is a reading, not a run.

## History

- `75b1831967` (SOLR-12801): the test-level `@AwaitsFix` for SOLR-13021 appears here. Ancestor of the pin.
- No commit at the pin names SOLR-13291 (evidence tool: none).

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- The race shape is present in the reading, and the failure message comes from the JVM file lock, which is consistent with a core left open by an unload that has not finished.
- Not reproduced. A run would delete and re-create a collection with the same name in a tight loop on a cluster and capture the lock error and the core state.
- SOLR-13021 is the disabled test; enabling that test is a separate question and is not checked here.

## Verdict justification

keep-open: the race is plausible from the code shape, the report is not reproduced, and the linked SOLR-13021 is a different disabled case. Close verdicts need a run (close-cannot-reproduce) or a fix (close-fixed); pr-needed needs the race reproduced with its failing run.

Missing for a closing or PR verdict: a delete-and-re-create loop run on the pin with the lock error and the core state, and its log path.

## Limits

This record does not establish a reproduction. It does not check the Lucene lock behavior beyond the JDK message, and it does not check whether SOLR-13021's test would pass if enabled. It does not post anything to JIRA or GitHub.
