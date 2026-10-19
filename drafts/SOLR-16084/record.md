# Skip record: SOLR-16084

- Ticket: SOLR-16084, When Solr server runs with HTTPS java clients can't connect but curl can.. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-16084-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: When Solr server runs with HTTPS java clients can't connect but curl can..

## Code reading

The named Http2SolrClient was renamed to solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java by 2c55000296 (SOLR-18005). Jetty is now 12.1.12 (gradle/libs.versions.toml:84), not the 9.4 in the trace. Missing: whether the HTTPS broken-pipe behavior persists in HttpJettySolrClient, which needs a repro over HTTPS.

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
