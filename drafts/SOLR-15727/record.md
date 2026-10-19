# Skip record: SOLR-15727

- Ticket: SOLR-15727, Split-brain can occur with heavy ingest and violent shutdown. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-15727-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Split-brain can occur with heavy ingest and violent shutdown.

## Code reading

solr/core/src/java/org/apache/solr/update/UpdateLog.java:383 still defaults numRecordsToKeep to 100, the mechanism the ticket cites for lost updates after an unclean restart. This is a code-path verdict; the split-brain was not reproduced.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. The C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
