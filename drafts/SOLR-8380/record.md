# Skip record: SOLR-8380

- Ticket: SOLR-8380, "CollectionsAPIDistributedZkTest frequent failures with non ok status: 500, message:Server Error". JIRA status Open, resolution none, fix versions listed as 5.5 and 6.0, created 2015-12-07, last updated 2016-05-09, checked 2026-10-19 UTC. Reference: SOLR-8469 (Open).
- Verdict: keep-open.
- Draft branch: solr-8380-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

Intermittent failures of `CollectionsAPIDistributedZkTest` (3 tests) with "Error from server ... non ok status: 500, message:Server Error" during `waitForNon403or404or503`. Comments: shalin (2015-12-07) attaches failure logs from Jenkins on several branches; shalin's commit adds the core name to an assert message (2015-12-08); markrmiller (2015-12-28) files SOLR-8469 for the generic server error text; markrmiller (2016-01-12) says the failure is not Jetty-upgrade related and appears on 5x too, and suggests the request buffer size may be too small for the headers; markrmiller (2016-01-13) says he could not reproduce it locally by beasting.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/test/org/apache/solr/cloud/api/collections/CollectionsAPIDistributedZkTest.java`: the test exists at the pinned head.
- The only commit that names the ticket, `d6b3312d82` (2015-12-08), changes one line in `solr/core/src/java/org/apache/solr/cloud/ZkController.java` (an assert message). It does not change behavior.
- `git grep -n "setRequestBufferSize\|requestBufferSize" -- solr/solrj/src/java solr/core/src/java`: no match. The buffer-size change suggested in the comments is not on main.
- The 500 with a generic message is the server's response when a request fails in a handler; SOLR-8469 (linked, open) is the ticket that asks for a specific message. No code path for this 500 was traced in this closeout.

## History

- `d6b3312d82` (2015-12-08), SOLR-8380: assert message only. Ancestor of the pinned head: yes.
- The fix versions 5.5 and 6.0 on the ticket do not match a fixing commit on main; the record does not treat them as evidence of a fix.
- No other commit on the fork names SOLR-8380 (`git log --grep` at the pinned head).

## Reproduction attempts

1. (Earlier, 2016-01-13, markrmiller) beasting locally: no reproduction. This is not a run on the pinned head.
2. (This closeout) not attempted. A run at the pinned head would beast `CollectionsAPIDistributedZkTest` over a seed sweep (for example 20 seeds, each once) and count the 500 responses from the fresh XML. That run is the missing piece; it was not run here because a multi-minute distributed test per seed needs many seeds to mean anything.

## Gap analysis

The failure has not been seen in the ticket's record for about ten years, and the only commit in its history is a message change. Whether the 500 still happens is not shown on the pinned head either way. The open question (what server path returns the generic 500 under load) is also the subject of SOLR-8469, which is open.

## Verdict justification

keep-open: close-cannot-reproduce needs a reproduction attempt on the pinned head with a result, and none was run here (the 2016 attempt was on an older head). close-fixed does not apply: the only fixing-named commit is an assert message. close-obsolete does not apply: the test still exists. The keep-open minimum is met: the attempts and the commit are named, and the missing piece is a seed sweep on the pinned head with the 500 responses counted.

## Limits

This record does not run the test. It does not read the Jenkins logs attached to the ticket. It does not trace the server path for the generic 500 beyond the statement above. It does not check SOLR-8469 in detail.
