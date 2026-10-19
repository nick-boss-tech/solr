# Skip record: SOLR-5878

- Ticket: SOLR-5878, Incorrect number of rows returned in distributed search with group.format=simple. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-5878-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Incorrect number of rows returned in distributed search with group.format=simple.

## Code reading

unclear | No commit at the pinned sha cites SOLR-5878. QueryComponent.java:1733-1737 sets docsToCollect from rows for simple format, and SimpleEndResultTransformer.java:39-80 emits every scoreDoc of each group without trimming to rows. Whether this is a bug or a wiki error is a maintainer decision; a distributed test is needed.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
