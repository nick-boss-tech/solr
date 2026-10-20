# Skip record: SOLR-13038

- Ticket: SOLR-13038, Overseer actions fail with NoHttpResponseException following a node restart.
- Verdict: keep-open.
- Draft branch: solr-13038-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 18 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-12-03, updated 2019-06-08, no fix versions. Links: "Reference SOLR-13028 (Resolved)"; "Reference SOLR-13100 (Open)". The report: Overseer operations sent right after a node restart fail with an IOException or `NoHttpResponseException` when talking to the restarted node. Three comments: gerlowskija gives a JUnit reproduction (2018-12-03) and attaches a strawman patch that adds a retry check to `HttpShardHandler` (2018-12-04); the ticket is unassigned on 2019-01-02 as the reporter has no time to pursue it.

## Code reading

Read at the pin:

- No retry handler for the shard HTTP path: `git grep -n -i 'retryhandler\|RetryHandler\|retryable\|setRetry\|maxRetries'` under `solr/core/src/java` and `solr/solrj/src/java` returns only `RecoveryStrategy.java` (its own `maxRetries` for recovery, lines 108-161, 481) and a comment in `SolrCmdDistributor.java:132` that mentions a retryable request. The strawman's `retryable` plumbing through `ShardRequest` is not in the tree.
- The retry question is therefore open at the pin: a request that failed after the node restarted is not retried on the shard path, and the ticket's fix would add a retry, which changes behavior for every request type that goes through it.
- SOLR-13028 is linked as Resolved, but its commits at the pin (`46d7994045`, `40502b9ba8`) are `@AwaitsFix` changes for `AutoAddReplicasPlanActionTest`; they do not address this ticket.

## History

- No commit at the pin names SOLR-13038 (the evidence tool lists none).
- The strawman patch was never committed.

## Reproduction attempts

Not run. This pass ran no builds or tests. The ticket's JUnit reproduction (a collection test that restarts a node and runs an Overseer action) would show the failure at the pin.

## Gap analysis

- The failure path (no retry for shard requests after a restart) is present in the code read.
- A retry is not free: the ticket's own discussion raises that some Overseer actions are not idempotent, so a retry needs a per-API decision on which operations may be retried.
- The linked SOLR-13028 does not carry the fix.

## Verdict justification

keep-open: the missing retry is present, the reproduction has not been run at the pin, and the fix needs a per-API idempotency decision that the ticket does not record. close-fixed does not fit (no fix commit; the linked SOLR-13028 is unrelated). close-obsolete does not fit (the path is present).

Missing for this verdict: a run of the ticket's restart reproduction at the pin, and the per-API decision on which Overseer operations may be retried.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
