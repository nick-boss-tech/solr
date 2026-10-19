# Skip record: SOLR-6836

- Ticket: SOLR-6836, highlighting doesn't formatting the snippet if there are more than 5 characters before the matching string. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-6836-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: highlighting doesn't formatting the snippet if there are more than 5 characters before the matching string.

## Code reading

unclear | DefaultSolrHighlighter.java:644 passes FRAGSIZE to the FVH path. The 4.10 default was original, but the default is now unified (HighlightComponent.java:177). Next: run the ticket query with hl.method=original and hl.method=unified at the pinned sha and compare snippets.

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
