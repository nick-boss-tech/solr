# Skip record: SOLR-6934

- Ticket: SOLR-6934, bin/solr -e cloud has poor failure behavior when solr has (certain types of) startup errors. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-6934-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: bin/solr -e cloud has poor failure behavior when solr has (certain types of) startup errors.

## Code reading

unclear | The named SolrCLI.SolrResponseHandler has zero grep hits under solr/ (removed by b4b28e52d7, SOLR-16504). AssertTool exists (solr/core/src/java/org/apache/solr/cli/AssertTool.java), but solr/bin/solr never calls it. -e cloud waits via StatusTool.waitToSeeSolrUp (StatusTool.java:268). Next step: check whether a half-started node now fails the -e cloud run.

## History

- `b4b28e52d7`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
