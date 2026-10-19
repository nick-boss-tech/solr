# Skip record: SOLR-7806

- Ticket: SOLR-7806, SolrCloud to use 127.0.01 instead of localhost. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-7806-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: SolrCloud to use 127.0.01 instead of localhost.

## Code reading

unclear | The advertised host now comes from AddressUtils.getHostToAdvertise (solr/core/src/java/org/apache/solr/util/AddressUtils.java:34, used at ZkController.java:987). Commit 3b2b1b7f79 (SOLR-16926) introduced it and did not target this request. The literal "localhost" remains at solr/core/src/java/org/apache/solr/cli/CLIUtils.java:108. Next step: decide whether this answers the report.

## History

- `3b2b1b7f79`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
