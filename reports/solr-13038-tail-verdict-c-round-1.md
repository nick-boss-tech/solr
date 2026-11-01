# Verdict: SOLR-13038, Overseer actions fail with NoHttpResponseException following a node restart

Verdict: STILL LIVE, promote to implementation, not closure. The 2018
transport is gone, but the failure class survives in the load-balancer
failover rules: a collection-admin request that dies on a stale pooled
connection after a restart is rethrown, not retried, unless the failure
is a connect failure or the request never left the client. Nothing
closes without a maintainer's or Nick's go.

Ticket: https://issues.apache.org/jira/browse/SOLR-13038 (2018, reporter
Jason Gerlowski, Open, Major, last updated 2019-06-08, unassigned since
2019-01-02, no fix version). The description says overseer operations
executed right after a Solr node restart fail with a SolrServerException
wrapping NoHttpResponseException ("failed to respond"). The reporter's
own diagnosis: when a non-overseer node restarts, the overseer never
notices its connections are invalid and reuses them for subsequent
requests. Three options are named in the ticket: retry logic in
SolrHttpRequestRetryHandler, retry logic in HttpShardHandler for
retryable overseer commands only, or more aggressive connection-pool
eviction (noting a similar attempt on SOLR-6944 had already failed).
Comments: a JUnit reproduction (create a 1-shard 2-replica collection,
stop the non-leader replica, restart it, then deleteCollection and
assert no failure) on 2018-12-03; a strawman patch adding a retryable
flag through HttpShardHandler on 2018-12-04, with the caveat that
NoHttpResponseException is ambiguous (the server may or may not have
processed the request), so only idempotent operations may be retried;
and the reporter unassigning himself on 2019-01-02 while still calling
the issue important for test flakiness and production behavior. Links:
SOLR-13028 Resolved (test hardening, unrelated) and SOLR-13100 Open
(pool management for intra-cluster communication).

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29).
The tree is unmodified there; the checkout is shallow (363 commits back
to June 2026), so no whole-history claim is made here, and no commit
in that shallow range names SOLR-13038.

The exact 2018 mechanism is gone:

- A whole-tree grep for NoHttpResponseException finds exactly one
  non-test hit: solr/solrj-zookeeper/src/java/org/apache/solr/client/solrj/impl/SolrClientNodeStateProvider.java:301,
  which is only the message string of a retry-exhausted error on the
  node-state path ("Could not get remote info after many retries on
  NoHttpResponseException"), not a handler on the shard path.
- SolrHttpRequestRetryHandler has no references anywhere in the tree.
- HttpShardHandler no longer calls an Apache HttpClient directly. It
  builds an LBSolrClient.Req
  (solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java:372-381),
  and the load-balancing implementation is LBAsyncSolrClient, whose
  class comment reads "formerly known as LBHttp2SolrClient, using
  Http2SolrClient (jetty)".
- The strawman was never merged: a grep for "retryable" in ShardRequest
  and HttpShardHandler returns nothing. There is no per-request retry
  flag on the shard path.

The failure class survives in the failover rules:

- LBSolrClient.request
  (solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:576)
  computes isAdmin as request type ADMIN with requiresCollection()
  false, and isNonRetryable as UPDATE or isAdmin.
- CollectionAdminRequest is ADMIN (its constructor calls super with
  SolrRequestType.ADMIN, CollectionAdminRequest.java:86) and its
  requiresCollection() returns false (CollectionAdminRequest.java:141),
  so a deleteCollection request counts as non-retryable in these rules.
- In LBSolrClient.doRequest (LBSolrClient.java:634-700), a
  non-retryable request fails over to another server only on a
  ConnectException (the SocketException branch) or, in the
  SolrServerException branch, on a connect exception or a
  RequestNotSentException, with the comment that nothing of the request
  reached the server, so replaying it elsewhere is safe even though it
  is not idempotent. A pooled connection that dies after the request
  was sent surfaces as a generic IOException or end-of-stream failure,
  not as a connect failure, so that branch rethrows it instead of
  failing over. Retryable (query-type) requests do fail over and have
  the dead server zombie-marked; the ticket's collection-admin shape
  does not.
- That is the current form of the reported failure: the first attempt
  after a restart can still fail outright when it lands on the stale
  connection, and the only retries the code now grants are the ones
  that are safe precisely because nothing was sent.

Two further checks:

- The ticket's exact reproduction assertion is not in the tree.
  LeaderTragicEventTest.testOtherReplicasAreNotActive
  (solr/core/src/test/org/apache/solr/cloud/LeaderTragicEventTest.java:187)
  still restarts a non-leader replica, but it now asserts leader
  identity after a corrupt-leader exercise, not the ticket's
  deleteCollection-after-restart with an assertNull failure check. The
  restart-then-act shape remains, but the assertion that exhibited
  this failure drifted out.
- The linked pool-management option is undelivered: SOLR-13100 is
  still Open (reporter Chris M. Hostetter, updated 2019-06-08).

## Scope note

The fix shape is a verdict note, not a design, and it inherits the
ticket's own caveat: a retry is only safe for operations known to be
idempotent, or for failures known to have sent nothing. An
implementation assignment has three directions to pick from: a
per-operation retry decision for overseer and collection-admin
operations in LB terms; pool eviction keyed on node-down knowledge
(the SOLR-13100 direction); or stale-connection detection before send
so the failure becomes a RequestNotSent and LB retries it. The
assignment should start by re-running the ticket's
restart-then-delete reproduction at the current head: this verdict ran
no build or test, the same limit the prior record carried.

## Relation to the earlier record

This verdict agrees with the 2026-10-20 pool keep-open record on fork
branch solr-13038-draft-open (dcbd33cced), which found no retry on the
shard path and no fix commit, and updates it in two places. First, the
transport changed: the Apache HttpClient shard path is gone and LB
failover now exists, but it excludes exactly this case (non-retryable
admin requests failing mid-request). Second, the reproduction assertion
drifted out of LeaderTragicEventTest. Neither change closes the ticket:
no fix commit, no retry for this shape, and the linked pool ticket is
still open.
