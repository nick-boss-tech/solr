# Skip record: SOLR-12220

- Ticket: SOLR-12220, TestReplicationHandle.doTestStressReplication() fails on Windows because an index directory can't be deleted. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-12220-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Source: A2 flake locate round 1 (reports/2026-10-18-skip-repro/A2-flake-locate-round-1.md). Checked here from that report; the runs were not repeated.

## Ticket reading

The summary as recorded in the queue: TestReplicationHandle.doTestStressReplication() fails on Windows because an index directory can't be deleted. Test class from the report: org.apache.solr.handler.TestReplicationHandler.

## Code reading

Not read for this record; the verdict rests on the run data and the grep result given below.

## History

No commit history was used for this record.

## Reproduction attempts

Command: :solr:core:test --rerun --tests org.apache.solr.handler.TestReplicationHandler --rerun --tests org.apache.solr.handler.TestReplicationHandler (seed printed by Gradle: 265F41BA244CFB3). Result: build SUCCESSFUL. Counts tests/failures/errors/skipped: 25 / 0 / 0 / 25. All tests were skipped, so the run exercised none of them. Conclusive: no.

## Gap analysis

The class is skipped at this head; a run that enables its tests is needed.

## Verdict justification

keep-open: the class ran, but every test in it was skipped, so the run did not exercise the ticket. The result is not evidence either way.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub.
