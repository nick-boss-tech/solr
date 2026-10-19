# Skip record: SOLR-17376

- Ticket: SOLR-17376, Direct buffer memory leak in Solr Java Client. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-17376-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Direct buffer memory leak in Solr Java Client.

## Code reading

The named Http2SolrClient was renamed to HttpJettySolrClient by 2c55000296 (see SOLR-16084). No commit in history addresses the direct-buffer pool, and the Jetty-side leak was not verified against Jetty 12. Missing: the leak check on HttpJettySolrClient under Jetty 12.

## History

- `2c55000296`: cited in the C4 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
