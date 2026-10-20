# Skip record: SOLR-9492

- Ticket: SOLR-9492, "Request status API returns a completed status even if the collection API call failed". JIRA status read 2026-10-20T13:46:19Z (UTC): Open, resolution None, updated 2020-07-22. Comments: 2018 (gerlowskija) could not reproduce by failing SPLITSHARD in several ways.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9492-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:46:19Z (UTC).

## Ticket reading

A failed SPLITSHARD response shows the request status as completed, with per-node responses that are status 0 and an inner `STATUS=completed`. The reporter says a failed call should not look completed.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/cloud/DistributedApiAsyncTracker.java:246-263`: a stored response is reported COMPLETED when it has neither a `failure` nor an `exception` key at the top level. Otherwise, a missing response or an error response is reported FAILED (line 274).
- So a status can read completed when the stored response has no top-level failure or exception key, even if a sub-request inside it failed. The ticket's example has the `success` key and nested per-node `STATUS` values, which this check does not inspect.
- The enum is `solr/solrj/src/java/org/apache/solr/client/solrj/response/RequestStatusState.java`.

## History

- evidence.py at the pinned head: no commits naming SOLR-9492.

## Reproduction attempts

Not run. The 2018 comment reports failed SPLITSHARD runs that did not produce `completed`. A run would fail a split at a sub-request and read the status API; the missing piece is that run.

## Gap analysis

The check reads only the top-level failure keys. Whether a split that fails inside a sub-request writes a top-level failure key is what decides the ticket, and was not checked by a run.

## Verdict justification

keep-open meets the rubric: attempts made are the completion-check reading (DistributedApiAsyncTracker.java:246-274) and the 2018 comment; the missing piece is a split-failure run and the status read. close-cannot-reproduce is not supported: no reproduction was run here. close-fixed and close-obsolete are not supported: the check is present and the path exists.

## Limits

This record does not establish the status for a split failure. It does not check other collection APIs.
