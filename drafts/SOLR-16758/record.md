# Skip record: SOLR-16758

- Ticket: SOLR-16758, "GraphQuery Performing Slower in Solr 9". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-04-20, updated 2023-05-02, no fix version. Six comments: magibney asks for a query and configs (2023-04-20); kiratraynor attaches a patch and a 2-million-document test (2023-04-30); magibney reports no difference in an in-test run with `testGraphQueryPerformance()` (2023-05-01); kiratraynor says the difference shows with separate Solr instances and high variance (2023-05-02); magibney asks whether the baseline is the same version (2023-05-02); kiratraynor confirms it is the same version and plans `cache=false` (2023-05-02).
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-16758-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:41-14:42 UTC.

## Ticket reading

The reporter says SOLR-14185 changed GraphQuery to iterate a `DocIdSetIterator` instead of a `Filter`'s `DocIdSet`, and that this roughly halves the speed of a graph join with `traversalFilter`. Reverting that change in a local patch brought the latency back near Solr 8. The maintainers could not reproduce a difference in-test. The reporter's numbers vary from about 200 ms to much more across runs, and the thread has no comment after 2023-05-02.

## Code reading

- `solr/core/src/java/org/apache/solr/search/join/GraphQuery.java:117-200`: `createWeight` and the scorer path. The scorer's `explain` uses `scorer(context)` and `advance`. The matching set is built by `getDocSet()` (line 183 onward), which allocates a `FixedBitSet` of `maxDoc` bits and runs the breadth-first traversal to build the result before scoring.
- The reporter's patch concerns how that set is consumed (a `Filter` versus a `DocIdSetIterator`). The current pin builds the set eagerly as a `DocSet`, so the patch's change point is not the same as the pin's code.

What this shows at the pin: the traversal is built as a bitset up front, which is one plausible place for the cost. Whether the Filter-based approach the reporter reverted to matters at the pin is not shown: the pin's code path differs from the code the patch changed, and the ticket's measurements are not reproducible from the text.

## History

- No commit on the pinned head names SOLR-16758 (evidence tool: no hits).
- SOLR-14185 (the change the reporter reverts) is not traced in this pass.

## Reproduction attempts

Not attempted. A conclusive check needs a benchmark run on the pinned head with `cache=false`, a fixed data set, the reporter's query, and repeated timings. That is a test or benchmark run, so it is not part of this review-only pass.

## Gap analysis

The performance claim is unresolved. The reporter's own in-test check found no difference, and the separate-instance check varied too much to read a 50% change. The pinned code is not the code the reporter patched, so the patch result does not transfer directly.

Missing piece for a verdict change: a repeatable benchmark on the pinned head (the reporter's query, `cache=false`, enough documents, repeated runs with a median), compared against a build with the reporter's patch applied. With that result, the record can say whether there is a regression to fix or none to act on.

## Verdict justification

keep-open meets the rubric: the code path is read (`GraphQuery.java:117-200`), no attempt is run, and the missing piece is named (the benchmark on the pin). close-cannot-reproduce needs a reproduction attempt, which has not been run here. pr-needed is not met: the regression is not shown at the pin, and the reporter's change does not map onto the pin's code.

## Limits

This record does not measure the query latency and does not confirm or refute the 50% figure. It does not trace SOLR-14185. It does not post anything to JIRA or GitHub.
