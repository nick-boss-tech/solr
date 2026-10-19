# Skip record: SOLR-5793

- Ticket: SOLR-5793, SignatureUpdateProcessorFactoryTest routinely fails on J9. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-5793-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Source: A1 flake round 1 (report reports/2026-10-18-skip-repro/A1-flake-round-1.md). Checked here from that report; the runs were not repeated.

## Ticket reading

The summary as recorded in the queue: SignatureUpdateProcessorFactoryTest routinely fails on J9. Test class from the report: org.apache.solr.update.processor.SignatureUpdateProcessorFactoryTest.

## Code reading

Not read for this record; the verdict rests on the run data and the grep result given below.

## History

No commit history was used for this record.

## Reproduction attempts

Command: :solr:core:test --rerun --tests org.apache.solr.update.processor.SignatureUpdateProcessorFactoryTest (seed printed by Gradle: 84BC03CE7EE0459A). Result: pass 1 PASS (rc 0). Counts tests/failures/errors/skipped: 6/0/0. Rerun: not run. Conclusive: no, because the test is flaky and one passing run cannot rule out the failure.

## Gap analysis

One passing run does not settle a flaky test. Repeated runs at several seeds would be needed.

## Verdict justification

keep-open: a single run passed, which is not a reproduction of a flaky test. A passing run does not show the problem is gone.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub.
