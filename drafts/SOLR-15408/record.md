# Skip record: SOLR-15408

- Ticket: SOLR-15408, ConcurrentUpdate issue of solr 8.8.1. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-05-17, updated 2021-06-23, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-15408-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 9, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) and the B3 reading are background only.

## Ticket reading

Solr 8.8.1, SolrCloud with 3 nodes, 500 collections, 1 shard with 3 replicas. Under load (30 update threads plus 10 deleteByQuery threads), the update client reports "Request processing has stalled for 20091ms with 100 remaining elements in the queue". The reporter says that during deleteByQuery only the lead node receives update transactions, and the other two get none.

## Code reading

- solr/solrj/src/java/org/apache/solr/client/solrj/impl/ConcurrentUpdateBaseSolrClient.java:203: the stall time is `solr.cloud.client.stallTime`, default 15000 ms, with a minimum applied at :208-209. The detector is created at :212.
- solr/solrj/src/java/org/apache/solr/client/solrj/impl/StallDetection.java:101-120, `stallCheck()`: if the queue is non-empty and the processed count has not moved since the last check, a start time is recorded; once more than `stallTimeMillis` has passed, and the queue is still non-empty, it throws IOException "Request processing has stalled for ...". The message in the ticket matches this throw.
- ConcurrentUpdateBaseSolrClient.java:501, :539, :613 call `stallCheck()` from the send path; :341 increments the processed count per completed request.

So the 8.8.1 message comes from a timer on the processed count. A stall is reported when no request completes for the stall time while the queue has items, which is the reporter's case if the send path is blocked (for example by the deleteByQuery load on the other nodes).

## History

- SOLR-17294 (c72562aeb3, 2025-04-21, "ConcurrentUpdateSolrClient no longer detects 'false-positive' stalls") is an ancestor of the pin. It moved the check into StallDetection.java and added StallDetectionTest.java. This is the candidate fix: if the 8.8.1 report was a false positive (slow but moving progress counted as a stall), the new logic may no longer fire. The commit does not name SOLR-15408.
- The 8.8.1 detector is not on the pin; the current detector is the one read above.

## Reproduction attempts

Not run in this pass. A reproduction needs a SolrCloud or a client test with a deleteByQuery load that blocks the send path for longer than the stall time, run on the pinned head, with and without the candidate logic. The existing StallDetectionTest is a unit test with a fake time source; it was not run here.

## Gap analysis

The mechanism is clear in the code: the client throws when the processed count does not move for the stall time while the queue is non-empty. What is not settled is whether the reporter's situation was a real stall (no requests completing, which the ticket's "only the lead node gets updates" suggests) or a false positive that SOLR-17294 removed. The ticket has no reply after 2021-06 and no logs beyond the one exception.

## Verdict justification

keep-open: the attempts made are the code path reads above, the Jira read, and the history check against SOLR-17294. close-fixed is not supported yet: the commit fixes a false-positive class, and the reporter's symptom is not shown to be that class. The missing piece is a load run on the pin (deleteByQuery plus updates at the reporter's rate) that shows whether the stall still fires, and whether the blockage is in the send path or on the server.

Missing for a closing verdict: (1) a run of the load case on the pinned head with its result and the log path; (2) the reporter's per-node logs showing whether requests completed during the stall.

## Limits

This record does not reproduce the stall. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
