# Skip record: SOLR-10096

- Ticket: SOLR-10096, Parallel SQL not returning the correct results for count distinct aggregation. JIRA status as recorded in the B3 reading: Open, 2019-06-08, 2. JIRA was not re-read for this record.
- Verdict: close-answered.
- Draft branch: solr-10096-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Parallel SQL not returning the correct results for count distinct aggregation. B3 row type: support. Skip note from the B3 reading: Docs answer the question: per-group COUNT(DISTINCT) is unsupported.

## Code reading

Docs page and sentence: `solr/solr-ref-guide/modules/query-guide/pages/sql-query.adoc:305-306`. Quote: "Computing the number of distinct values for a specific field within each group using COUNT(DISTINCT <field>) is not currently supported by Solr; only COUNT(*) can be computed for each GROUP BY dimension."

## History

None cited as an ancestor in the B3 reading.

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The documentation answers the question as asked; no code change is involved.

## Verdict justification

close-answered: the reference guide answers the question. The cited page and sentence are below.

Missing for this verdict: No reproduction; the answer is documentation.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
