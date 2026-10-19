# Skip record: SOLR-13698

- Ticket: SOLR-13698, Querying suggester with alias fails. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-08-15, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-13698-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and greps run at this head (detached worktree).
- Source: remediation redo of batch 06, item 1, under assignments/pool-aaa-skip-verdict-remediation-06.md. The earlier record (vm2, from the C reading) cited the two code paths; both are confirmed here.

## Ticket reading

With an alias that points to six collections, a /suggest request fails on 8.x: the SolrJ client reports no live servers for the alias URL, and an HTTP request times out on one member collection. The reporter says 7.3.1 worked.

## Code reading and grep at the pinned head

- solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java:217-229, `distributedProcess`: the first stage clears `shards` on the shard request (:226) and queues one request per shard with `PURPOSE_GET_TOP_IDS`. That is the distributed suggest path.
- solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:763, `addCollectionParamIfNeeded(List<String> collections)`, called from :318: this is where the collection list of an alias is turned into a collection parameter. Whether it handles a multi-collection alias for suggest was not traced beyond the call.
- `git grep -l -i "suggest" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/test/*` returns 34 test files. None of them mentions an alias (checked by reading each file for "alias"; count 0).

## History

- No commit on the pin names SOLR-13698.

## Reproduction attempts

Not run. A reproduction needs a SolrCloud with an alias over several collections and a /suggest request. This pass did not start a cluster.

## Gap analysis

The distributed suggest path and the alias helper exist, and no test covers suggest over an alias. Whether the path works over several collections, or fails the way the reporter saw, is not shown by reading.

## Verdict justification

keep-open: the attempts made are the code reads and the test-coverage count above at the pinned head, the Jira read, and the call-site trace. The missing piece is a run: a multi-collection alias with a suggest request on the pin, with the result and log path. close-fixed and close-obsolete are not supported (the path exists and no fix is identified). pr-needed is not supported without a reproduction.

Missing for a closing or PR verdict: the run output.

## Limits

This record does not reproduce the failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
