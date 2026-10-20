# Skip record: SOLR-9550

- Ticket: SOLR-9550, "innerJoin can succeed with bad sorting". JIRA status read 2026-10-20T13:35:36Z (UTC): Open, resolution None, updated 2019-06-08. No comments.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9550-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:35:36Z (UTC).

## Ticket reading

`innerJoin` requires both streams to be sorted by the join key. A query that sorts by the wrong key can still succeed and return wrong results. The reporter's expected result is a useful exception.

## Code reading (pinned head)

- `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/InnerJoinStream.java:29-30` documents the requirement that both streams be sorted by the joined fields.
- `InnerJoinStream.java:54-110` (`read`): a merge join that compares the current tuples of each stream (line 98) and advances. No check that a stream's tuples are in non-decreasing order is present in this method.

## History

- evidence.py at the pinned head: no commits naming SOLR-9550.

## Reproduction attempts

Not run. A run would build the reporter's two streams with a wrong sort and check whether the join returns wrong rows without an error.

## Gap analysis

The merge join relies on the input order and does not check it. A check (for example, that each tuple's key is not less than the previous one) is possible, but its cost, its behavior on legitimately unsorted inputs, and whether it should throw or skip are design choices.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading of `read` and the documented requirement; the missing pieces are the run and the design choice on validation. close-fixed and close-obsolete are not supported: no check exists and the join is present. pr-needed is not chosen, since the behavior the ticket wants (throw or skip) is a design choice.

## Limits

This record does not run the mis-sorted query. It does not check the non-streaming joins.
