# Skip record: SOLR-9426

- Ticket: SOLR-9426, "TesReplicationHandler.doTestIndexAndConfigAliasReplication() failure". JIRA status read 2026-10-20T14:15:39Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9426-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:15:39Z (UTC).

## Ticket reading

A seeded branch_6x run of `doTestIndexAndConfigAliasReplication` fails with "expected:<1> but was:<0>" at `TestReplicationHandler.java:1331`. The reporter says it reproduces only without the method filter. Two 2016 comments trace the logs to a "can't write replication.properties twice" exception, which the mock directory enforces, and to a failed IndexWriter close.

## Code reading (pinned head)

- `solr/core/src/test/org/apache/solr/handler/TestReplicationHandler.java:1304`: the method is present. The test clears the leader index, copies a leader config, and then compares replicated results (the replication steps follow the same pattern as the other replication tests).
- The two exceptions in the 2016 comments are the mock-directory guard and an IndexWriter close after a fetch; the guard fires only in tests. The current fetch code path for writing the properties was not traced in this pass.

## History

- evidence.py at the pinned head: no commits naming SOLR-9426.

## Reproduction attempts

Not run. A seeded run (`-Dtests.seed=8D2C8D836AE82A81`, the reporter's seed) would show whether the failure still happens on this head; the seed is a branch_6x seed and may not reproduce here.

## Gap analysis

The reported exceptions come from the fetch and the test directory guard. Whether the fetch still writes the properties file twice, and whether the assertion at line 1331 now checks the same thing, is not settled by reading.

## Verdict justification

keep-open meets the rubric: attempts made are the test-presence check and the reading of the two 2016 explanations; the missing piece is a seeded run on this head. close-fixed and close-obsolete are not supported: the test and path are present. close-cannot-reproduce is not supported: no run was made.

## Limits

This record does not establish the cause at the pinned head. It does not check the branch_6x code.
