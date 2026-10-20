# Skip record: SOLR-9573

- Ticket: SOLR-9573, "Parameter debugQuery=true is behaving incorrectly for the normal two phase distributed search". JIRA status read 2026-10-20T13:23:40Z (UTC): Open, resolution None, fix version 7.0 listed, updated 2019-06-08. No comments.
- Verdict: keep-open (kept; re-read at the pinned head). The fix-version field is not a fix commit.
- Draft branch: solr-9573-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:23:40Z (UTC).

## Ticket reading

With `debugQuery=true` on a normal two-phase distributed search, the results are not ordered by score and the debug information is wrong. The ticket links a 2016 mailing-list thread. It has no comments.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/component/DebugComponent.java:153-182` (`modifyRequest`): shard requests that fetch fields (`PURPOSE_GET_FIELDS`) get `DEBUG_QUERY` set to true when the request is debugAll, or the `query` and `results` debug parts; other shard requests get `DEBUG_QUERY` and `DEBUG` set to false. So the per-purpose split that the ticket is about is explicit in the code.
- The ordering question is in the query and merge components, which were not traced line by line in this pass.

## History

- evidence.py at the pinned head: no commits naming SOLR-9573.
- The fix version 7.0 in JIRA is not matched by a commit naming the ticket.

## Reproduction attempts

Not run. A run needs a multi-shard collection, a query with score sort, and `debugQuery=true`, comparing the result order and the debug output with and without the parameter. That is a SolrCloud test case, not built in this pass.

## Gap analysis

The debug split is explicit in code, so the reported ordering effect would have to come from the query or merge path. Whether it still occurs is unknown.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading of DebugComponent and the history search; the missing piece is the multi-shard run. close-fixed is not supported: no fix commit is named. close-obsolete is not supported: the debug path is present.

## Limits

This record does not run the distributed query. It does not trace the merge path.
