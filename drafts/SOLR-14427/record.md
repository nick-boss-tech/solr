# Skip record: SOLR-14427

- Ticket: SOLR-14427, "Potential bug in optimistic concurrency with versions=true and failOnVersionConflicts=false". JIRA status Open, resolution none, fix versions none, created 2020-04-23, updated 2020-05-27. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14427-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14427-vm2.md).

## Ticket reading

With update parameters versions=true and failOnVersionConflicts=false, a batch update of an existing document with a stale version and a document with _version_ -1 returns errors (DistributedUpdatesAsyncException, ERROR CONFLICT). The documentation says the conflicting document should be skipped. The test case is a 3-node cluster, 3 shards, 2 replicas, two documents. Related tickets: SOLR-8733 and SOLR-7404. No comments.

## Code reading

- solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java:418-421 (origin/main 498a09aea97): when the stored version does not match the update's version, and failOnVersionConflicts is false, the leader returns true (the document is skipped). Otherwise it throws CONFLICT ("version conflict for ...").
- DistributedUpdateProcessor.java:769-771: the same flag, for the case where the document to update is not found (returns false, skipping it); otherwise the CONFLICT error is thrown.
- The flag is read from the request params (cmd.getReq().getParams().getBool(CommonParams.FAIL_ON_VERSION_CONFLICTS, true)). Whether the flag reaches the requests forwarded to the other shards' leaders and replicas, which is where the reported DistributedUpdatesAsyncException arises, was not traced.

Commands at the pinned head:
- git grep -n 'FAIL_ON_VERSION_CONFLICTS\|failOnVersionConflicts' origin/main -- solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java: lines 199, 418, 769.
- git log --oneline --grep='SOLR-14427' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14427: none.

## Reproduction attempts

Not attempted. The report's case needs a 3-shard, 2-replica cluster with the two updates sent in one request, which needs a cluster test. Not set up in this pass.

## Gap analysis

The leader-side handling of failOnVersionConflicts=false is in place at the pin. The reported error is an asynchronous distributed update exception, so the question is whether the forwarding step (the parameters sent to the other shards, and the handling of their conflict responses) keeps the flag. That path was not traced.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket. close-cannot-reproduce does not hold: no reproduction, and the distributed forwarding path is not traced. The missing pieces are (1) the trace of the forwarded request parameters and the async conflict handling, and (2) a cluster run of the report's two-document update.

## Limits

This record does not reproduce the error. It does not trace the forwarding path. It does not compare with SOLR-8733 or SOLR-7404.
