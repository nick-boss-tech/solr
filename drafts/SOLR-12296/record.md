# Skip record: SOLR-12296

- Ticket: SOLR-12296, Test fail in TimeRoutedAliasUpdateProcessorTest RE "We need to create a new time routed collection but for unknown reasons were unable to do so.". JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-12296-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Source: A3 other round 1 (reports/2026-10-18-skip-repro/A3-other-round-1.md). Checked here from that report; the runs were not repeated.

## Ticket reading

The summary as recorded in the queue: Test fail in TimeRoutedAliasUpdateProcessorTest RE "We need to create a new time routed collection but for unknown reasons were unable to do so.". Test class from the report: org.apache.solr.update.processor.TimeRoutedAliasUpdateProcessorTest.

## Code reading

Not read for this record; the verdict rests on the run data and the grep result given below.

## History

No commit history was used for this record.

## Reproduction attempts

Command: :solr:core:test --rerun --tests org.apache.solr.update.processor.TimeRoutedAliasUpdateProcessorTest --rerun --tests org.apache.solr.update.processor.TimeRoutedAliasUpdateProcessorTest (seed printed by Gradle: 408F5D26E50AABAA). Result: build SUCCESSFUL. Counts tests/failures/errors/skipped: 7 / 0 / 0 / 7. All tests were skipped, so the run exercised none of them. Conclusive: no.

## Gap analysis

The class is skipped at this head; a run that enables its tests is needed.

## Verdict justification

keep-open: the class ran, but every test in it was skipped, so the run did not exercise the ticket. The result is not evidence either way.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub.
