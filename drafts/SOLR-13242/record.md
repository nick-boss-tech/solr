# Skip record: SOLR-13242

- Ticket: SOLR-13242, RegexReplaceProcessorFactory not making accurate replacement. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-13242-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: RegexReplaceProcessorFactory not making accurate replacement.

## Code reading

RegexReplaceProcessorFactory is still a thin Pattern.matcher(...).replaceAll(...) wrapper (solr/core/src/java/org/apache/solr/update/processor/RegexReplaceProcessorFactory.java:81, 124), unchanged in history. The reporter later attributed the result to the own config's whitespace pattern, and maintainers could not reproduce it. The config that produced the bad output is the missing piece.

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
