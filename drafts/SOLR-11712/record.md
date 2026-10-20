# Skip record: SOLR-11712

- Ticket: SOLR-11712, "Streaming throws IndexOutOfBoundsException against an alias when a shard is down". JIRA status read 2026-10-20T08:25:39Z (UTC): Open, resolution None, updated 2021-08-13.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-11712-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T08:25:39Z (UTC).

## Ticket reading

A streaming expression over an alias returns IndexOutOfBoundsException (Index 0, Size 0) when one shard of one collection behind the alias is down. The reporter's stack is in CloudSolrStream.constructStreams. The ticket has a long comment thread (2018) on a patch with a test, and on whether the test fails without the fix. The thread does not settle whether a down shard should error or return partial results.

## Code reading (pinned head)

- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:456, 471, 1002`: slice replica iteration on the client side. The stream stack in the ticket is in `solr/solrj/src/java/org/apache/solr/client/solrj/io/stream/CloudSolrStream.java` (constructStreams), not re-read line by line here.
- The queue record's claim that `TupleStream.getReplicas` now skips slices with no active replica does not hold at the pinned head as stated: `git grep "getReplicas("` under `solr/solrj/src/java` finds no such method in `TupleStream.java`. That claim is not used in this verdict.
- The tests named in the thread (StreamingAliasColTest, TestStreamErrorHandling) are not in `solr/core/src/test` at the pinned head (grep empty). The thread's test is not present.

## History

- `git log --grep=SOLR-11712` path-scoped on CloudSolrStream.java was not run in this pass (the pickaxe-scale search times out in this repo).
- evidence.py at the pinned head: no commits naming SOLR-11712.

## Reproduction attempts

Not run. A run would start an alias over two collections, take one shard down, and call the stream expression, to see whether the error is an IndexOutOfBounds or a clean error.

## Gap analysis

The thread shows a fix and a test that did not agree on the failing case. At the pinned head, the stream code path has changed, and the named tests are gone. Whether the current behavior is the bug, or the intended partial-result behavior, is the open design question the ticket leaves unanswered.

## Verdict justification

keep-open meets the rubric: the attempts made are the code-path reading and the test-presence check; the missing pieces are a reproduction against the current stream code and a decision on error versus partial results. close-fixed is not supported: no fixing commit is named. close-obsolete is not supported: the stream path is present.

## Limits

This record does not establish the current behavior with a run. It does not verify the queue note's claim about TupleStream.getReplicas beyond the grep above.
