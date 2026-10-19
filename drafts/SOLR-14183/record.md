# Skip record: SOLR-14183

- Ticket: SOLR-14183, "replicas do not immediately/synchronously reflect state=RECOVERYING when receiving REQUESTRECOVERY commands". JIRA status Open, resolution none, fix versions none, created 2020-01-11, updated 2020-01-21. Links SOLR-13486 (Open). Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14183-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14183-vm2.md).

## Ticket reading

After a network partition heals and a leader is restarted, the leader asks replicas to recover (REQUESTRECOVERY) and marks itself ACTIVE. A client that checks the cluster state sees every replica as ACTIVE, but the other replicas only mark themselves RECOVERING later, when their asynchronous recovery starts. A query to a non-leader replica can then miss documents. The ticket proposes marking RECOVERING synchronously in the request handler, or letting the leader set it. It was spun off of SOLR-13486 (open), which covers TestTlogReplayVsRecovery. No comments.

## Code reading

- solr/core/src/java/org/apache/solr/handler/admin/CoreAdminOperation.java:151-165 (origin/main 498a09aea97): REQUESTRECOVERY_OP looks up the core and calls `core.getUpdateHandler().getSolrCoreState().doRecovery(...)`. Its comment reads "This can take a while, but doRecovery is already async so don't worry about it here". The RECOVERING state is not set in this handler.
- The same request path exists for the v2 API: solr/core/src/java/org/apache/solr/handler/admin/api/RequestCoreRecoveryAPI.java (its javadoc says it is analogous to the v1 REQUESTRECOVERY command).
- The point at which a replica publishes RECOVERING is inside the asynchronous recovery, which the ticket describes; that publish was not located by line in this pass.

Commands at the pinned head:
- git show origin/main:solr/core/src/java/org/apache/solr/handler/admin/CoreAdminOperation.java | sed -n 151,165p: the handler and its doRecovery call.
- git log --oneline --grep='SOLR-14183\|SOLR-13486' origin/main: 9a2497f6377 and 0fac7c1a263 (SOLR-13486 commits, not SOLR-14183). Neither changes the REQUESTRECOVERY handler; they are linked as the parent ticket and are not treated as the fix.

## History

- Commits on origin/main naming SOLR-14183: none.
- SOLR-13486 commits (9a2497f6377, 0fac7c1a263): name the parent ticket; not checked as changes to REQUESTRECOVERY.

## Reproduction attempts

Not run. The ticket's scenario is a network partition, a leader restart, and a query to a non-leader replica, which needs a test with a simulated partition (the style of TestTlogReplayVsRecovery). That was not set up in this pass.

## Gap analysis

The handler still returns before the replicas' RECOVERING state is set, so the window the ticket describes is present in the code read. Whether a query in that window misses documents, and which fix is right (synchronous state, or the leader setting it), is not established by a run, and the fix is a design choice.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit changes the REQUESTRECOVERY handler (the SOLR-13486 commits are the parent ticket and do not touch it). close-obsolete does not hold: the handler is in use. close-cannot-reproduce does not hold: no reproduction was run. The missing pieces are (1) a partition test that queries a non-leader replica after REQUESTRECOVERY and before its RECOVERING state, and (2) a maintainer decision on where RECOVERING should be set.

## Limits

This record does not reproduce the missed documents. It does not locate the line where the replica publishes RECOVERING. It does not evaluate the proposed designs.
