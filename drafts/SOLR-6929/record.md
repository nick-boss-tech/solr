# Skip record: SOLR-6929

- Ticket: SOLR-6929, Highlighting in search - not working for special characters without spaces. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-6929-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Highlighting in search - not working for special characters without spaces.

## Code reading

unclear | Highlighting lives in solr/core/src/java/org/apache/solr/highlight/UnifiedSolrHighlighter.java. The only special-character test (solr/core/src/test/org/apache/solr/highlight/TestUnifiedSolrHighlighter.java:526) uses "#" with spaces, so the no-space case is not covered. Next step: run a no-space special-character query at this sha and check the highlight output.

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
