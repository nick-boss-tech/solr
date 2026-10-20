# Skip record: SOLR-13175

- Ticket: SOLR-13175, Rollup does not roll up NULL values.
- Verdict: keep-open.
- Draft branch: solr-13175-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 11 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-01-29, updated 2021-08-13, no fix versions, no links. The report: on a two-shard `sample_techproducts_configs` collection, `rollup(search(eoe, q="*:*", fl="popularity", qt="/export", sort="popularity asc"), over="popularity", count(*))` returns a separate bucket for NULL popularity, next to the bucket for 0. One comment (2019-01-29, erickerickson): NULL and zero are treated equivalently, which is the cause the commenter identifies.

## Code reading

Read at the pin:

- `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/RollupStream.java:241-262`: each tuple's bucket values are read with `buckets[i].getBucketValue(tuple)`, a `HashKey` is built from them, and a new rollup bucket is emitted when `hashKey.equals(currentKey)` is false.
- Because the rollup emits a bucket whenever the key changes, two rows that are NULL and 0 but arrive adjacent in the input stream get different keys and become two buckets. Rollup needs its input sorted by the bucket fields; the NULL and 0 rows are split only if the export sort places them apart.
- No code in RollupStream maps NULL to 0 or back. Whether NULL and 0 should group together is the semantic question the ticket raises, and the ticket does not settle it.

## History

No commit at the pin names SOLR-13175. The history of `RollupStream` bucket handling was not traced.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run at the pin: the ticket's two-shard expression, then the same expression with the `popularity` values listed to show whether the null and zero rows are adjacent in the sorted export.

## Gap analysis

- The reading explains the split from the comparison in `RollupStream`, given an input where NULL and 0 rows interleave with the sort; the run would confirm the input order.
- The fix is a semantic choice (group NULL with 0, or keep them apart and document it), not a clear defect; the ticket does not record the choice.
- The export sort for missing values is outside `RollupStream`, so a complete fix may also touch the export sort.

## Verdict justification

keep-open: the mechanism is clear from reading, but the expected grouping of NULL and 0 is a semantic choice the ticket leaves open, and the run that confirms the input order has not been made. close-fixed and close-obsolete do not fit (no fix commit; the feature is present). pr-needed needs the grouping decision.

Missing for this verdict: a run of the ticket's expression at the pin with the sorted output shown, and a maintainer decision on NULL versus 0 grouping.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
