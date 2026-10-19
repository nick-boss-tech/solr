# Skip record: SOLR-11764

- Ticket: SOLR-11764, preanalyzed field with highlight option throws exception. JIRA status as recorded in the B3 reading: Open, 2019-06-08, 11. JIRA was not re-read for this record.
- Verdict: close-answered.
- Draft branch: solr-11764-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: preanalyzed field with highlight option throws exception. B3 row type: support. Skip note from the B3 reading: The highlight exception cannot occur because the class is removed. This is a removal, not a highlight fix.

## Code reading

Docs page and sentence: `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc:298`. Quote: "PreAnalyzedField and PreAnalyzedUpdateProcessor have been removed due to incompatibility with Lucene 10 (SOLR-17839)."

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
