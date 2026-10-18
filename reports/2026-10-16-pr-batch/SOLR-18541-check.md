# SOLR-18541 (PR 5117) check: a short timeAllowed list throws before the skip

Host: vm2. Fork branch `combinedquerycomponent-merge-totals`, head `81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d` (ls-remote at the start of this tick, about 05:26Z, unchanged). Merge-base with upstream/main `597ed67740`. JDK 21.0.12.1, Gradle 9.7.0 from the branch wrapper; the wrapper jar matches the committed `gradle-wrapper.jar.sha256`. Seed for every Gradle run: `18541C0FFEE18541`.

## Status: confirmed (throw reproduced at head); fix escalated

- The throw reproduces at head for a short per-query list: `IndexOutOfBoundsException` from `CombinedQueryComponent.java:342`, which is before the skip at line 393.
- Reaching that shape from a real timed-out shard is supported by code reading. It is not reproduced end to end: no timeAllowed run was made, because the timing would need a distributed test that is not deterministic.
- No production change. The Limits require a main-agent decision before any production change. A fix that skips a short-list shard would change `numFound` and `approximateTotalHits` for that query, which the Decisions table says to escalate.

## Code path (head)

- `CombinedQueryComponent.java:340-342`: `docList.get(queryIndex)` runs for each shard response. It is not guarded by the partial check.
- `CombinedQueryComponent.java:393-398`: the partial skip. It runs after line 342, so a short list never reaches it.
- Shard side, `CombinedQueryComponent.java:183-221`: one `response_per_query` entry is added per sub-query, after `doFieldSortValues(thisRb, ...)` at line 210. The `sort_values_<k>` key is added at line 213, also after that call.
- `QueryComponent.java:848`: the coordinator sets `fsv=true` on every non-grouped shard request, so the shard always runs `doFieldSortValues`.
- `QueryComponent.java:571-573`: on `ExitingReaderException` (the timeAllowed exit), the code adds an empty `sort_values` and rethrows.
- `SearchHandler.java:507-509`: the rethrow is caught at the top of the handler and `shortCircuitedResults` marks the response partial. The entries already added stay in the response.
- `SolrIndexSearcher.java:337-339`: a timeout during the search itself is caught and only sets partial results. The sub-query still returns a result, so the short list comes from the sort-value stage.

Resulting shape: a timeout during the sort-value stage of sub-query k returns `response_per_query` with k entries, `sort_values_0..k-1`, and partial results. For k = 0 the key is absent, `getSubsectionFromShardResponse` returns null under partial, and no throw happens. For k >= 1 the coordinator reaches `get(queryIndex)` with queryIndex = k and throws.

## Run at head

Scratch test (not committed, not pushed; patch in `SOLR-18541-shortlist-test.patch`): `shortPerQueryListThrowsBeforeSkip` in `CombinedQueryComponentMergeTotalsTest`. Two combiner queries `q0`, `q1`. Int sort on `intDefault`. Shard response: partial header, one `response_per_query` entry, `sort_values_0` with one value. The test asserts that `mergeIds` throws `IndexOutOfBoundsException` and that the first `CombinedQueryComponent` frame is line 342.

- Run A: whole class, 10 of 10 passed, rc=0. Log `head-mergetotals.log`. This run used a score sort with no `sort_values_0`. That variant also throws at line 342, but it is not the realistic shape, so the int-sort variant is the record.
- Run B: `shortPerQueryListThrowsBeforeSkip` alone, int sort, 1 of 1 passed, rc=0. Log `head-shortlist-intsort.log`. The assertion that the throw is at line 342 holds.

## Not checked

- `numFound` and `approximateTotalHits` still take `max()` at lines 455 to 456, as decided. Not changed.
- The choice between max and sum for those totals is left to the main agent.
- Whether a real timeAllowed run hits the window. Not attempted.
