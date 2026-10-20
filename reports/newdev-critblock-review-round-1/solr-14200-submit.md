# Review: SOLR-14200, branch solr-14200-submit (unit 9)

Head reviewed: 10991af9bdac7ec464ac615de98e39d17b384d69 (ls-remote 2026-10-20T15:04Z). Base: 14c7aac0d1. Diff: 5 files, +62/-1 (TupleStream, ParallelStream, SqlStream, StreamingTest, changelog).

## Pass 1

Review (2026-10-20, vm5):

- Change. `TupleStream.getShards` returns `List.of()` when a collection is missing from the manual shards map (was null, so callers NPE), and copies the configured list before the core filter (so the caller's list is no longer mutated). `SqlStream` and `ParallelStream` throw `IOException("No shards available for collection ...")` on an empty list.
- Finding, MAJOR (blocker, HANDED BACK): `SignificantTermsStream.java:285` calls `callShards(getShards(...))` and iterates the result. With the new `List.of()` for a missing collection, `callShards` runs on an empty list and the stream returns an empty result without an error. The NPE was the old failure; this is a silent wrong answer, which hides the misconfiguration. Not covered by the branch or its tests. Fix is a behavior choice (throw, or report) outside the review repair scope.
- Finding, MINOR (HANDED BACK with the above): `DeepRandomStream.java:280` computes `r / shardUrls.size()`. For an empty list this is `ArithmeticException: / by zero`: still a failure, but without the clear message the ticket asks for. `CloudSolrStream` already throws the clear `IOException` on an empty list, so the message is inconsistent across the streams.
- Coverage. The tests cover `getShards` for the missing entry, `SqlStream`/`ParallelStream` on an empty list, and the caller-list mutation. Neither `SignificantTermsStream` nor `DeepRandomStream` is tested on the empty path.
- Changelog: present, `type: fixed`, author and link set. Not re-parsed this pass.

Gate (vm5, on 10991af9bd):
- `:solr:solrj-streaming:compileJava :solr:solrj-streaming:test --tests org.apache.solr.client.solrj.io.stream.StreamingTest` (offline): rc=0. `StreamingTest` tests=37, skipped=1, failures=0, errors=0 (`/workspace/gates/logs/vm5/g14200-test.log`). Matches the receipt's count.

Outcome: HANDED BACK (implementation). The two findings change behavior and need the implementer's decision on the failure mode for `SignificantTermsStream` and `DeepRandomStream`; the review repair scope does not cover it.
