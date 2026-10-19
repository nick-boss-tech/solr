# Skip record: SOLR-10706

- Ticket: SOLR-10706, SolrJ wrongfully uses URLEncoder.encode for constructing url fragments. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-10706-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: SolrJ wrongfully uses URLEncoder.encode for constructing url fragments.

## Code reading

Only SolrJ use is SolrParams.toQueryString (SolrParams.java:423-425), a query string where form encoding is correct; no URLEncoder on URL path fragments in solrj main; reporter gave no concrete failing request

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
