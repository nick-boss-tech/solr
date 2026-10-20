# Skip record: SOLR-9468

- Ticket: SOLR-9468, "HTTP api endpoint /replication does not handle errors propely". JIRA status read 2026-10-20T13:57:08Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: keep-open. This replaces the recorded close-fixed: the status is now ERR for the generic error branch, but the HTTP status is still 200 there, and no fixing commit is named.
- Draft branch: solr-9468-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:57:08Z (UTC).

## Ticket reading

A backup through the replication endpoint fails with "Unable to create snapshot directory", but the response has status OK and an exception string, and the HTTP code is 200. The reporter wants a 4xx or 5xx code, or at least a bad status in the body.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java:655-672`: a `SolrException` is rethrown, so it gets its HTTP error code. Other exceptions go to `reportErrorOnResponse` (line 374-380), which sets `status` to ERR and the message and exception. The success status `OK` is added only after `doSnapShoot` returns normally (line 664).
- So the ticket's OK-with-exception response is not produced by this branch. A generic error is reported with status ERR, but the HTTP code is still 200 on that branch.

## History

- evidence.py at the pinned head: no commits naming SOLR-9468. The API rewrite that the batch note mentions is not named by a commit here.

## Reproduction attempts

Not run. A run would call the backup endpoint with an unwritable location and record the HTTP code and the body.

## Gap analysis

The body status is fixed for the generic branch; the HTTP status is not, and the fix commit the record would need is not named. The run decides which branch the reporter's error takes.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading of ReplicationHandler.java:655-672 and 374-380; the missing pieces are a run of the backup failure and the fixing commit if one exists. close-fixed is not supported: no fixing commit is named and the HTTP code is still 200 on the generic branch.

## Limits

This record does not run the backup. It does not check the v2 backup API.
