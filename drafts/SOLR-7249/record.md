# Skip record: SOLR-7249

- Ticket: SOLR-7249, Solr engine misses null-values in "OR null" part for eDisMax parser. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-7249-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Solr engine misses null-values in "OR null" part for eDisMax parser.

## Code reading

unclear | Commit 0eca10b618 (SOLR-18256) adds autoFixPureNegative in QParser.java and applies it at solr/core/src/java/org/apache/solr/search/SolrQueryParser.java:34. ExtendedSolrQueryParser (ExtendedDismaxQParser.java:974) extends that class, but the ticket's edismax query was not traced. Next step: run the ticket query with defType=edismax at this sha.

## History

- `0eca10b618`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
