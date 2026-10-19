# Skip record: SOLR-9202

- Ticket: SOLR-9202, UninvertingReader needs multi-valued points support. JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-9202-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: UninvertingReader needs multi-valued points support.

## Code reading

solr/core/src/java/org/apache/solr/uninverting/UninvertingReader.java:331 provides getNumericDocValues only. No SortedNumeric path from points exists in the uninverting package (grep empty).

## History

- None cited in the B1 reading.

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading shows the reported path is still present, but the behavior has not been shown on a build.

## Verdict justification

keep-open: the code reading does not settle the question alone. The reading found the path but needs a build to show the behavior (for example a test with a specific input).

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. B1 ran no builds.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
