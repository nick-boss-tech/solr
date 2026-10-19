# Skip record: SOLR-9593

- Ticket: SOLR-9593, ConcurrentDeleteAndCreateCollectionTest failures in nightly builds. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-9593-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Source: A3 other round 1 (reports/2026-10-18-skip-repro/A3-other-round-1.md). Checked here from that report; the runs were not repeated.

## Ticket reading

The summary as recorded in the queue: ConcurrentDeleteAndCreateCollectionTest failures in nightly builds. Test class from the report: org.apache.solr.cloud.api.collections.ConcurrentDeleteAndCreateCollectionTest.

## Code reading

Not read for this record; the verdict rests on the run data and the grep result given below.

## History

No commit history was used for this record.

## Reproduction attempts

Command: :solr:core:test --rerun --tests org.apache.solr.cloud.api.collections.ConcurrentDeleteAndCreateCollectionTest --rerun --tests org.apache.solr.cloud.api.collections.ConcurrentDeleteAndCreateCollectionTest (seed printed by Gradle: C017E8F2BC867882). Result: build SUCCESSFUL. Counts tests/failures/errors/skipped: 2 / 0 / 0 / 2. All tests were skipped, so the run exercised none of them. Conclusive: no.

## Gap analysis

The class is skipped at this head; a run that enables its tests is needed.

## Verdict justification

keep-open: the class ran, but every test in it was skipped, so the run did not exercise the ticket. The result is not evidence either way.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub.
