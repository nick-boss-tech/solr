# Skip record: SOLR-10841

- Ticket: SOLR-10841, TestTlogReplica.testRecovery sometimes fails. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-10841-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Source: A1 flake round 1 (report reports/2026-10-18-skip-repro/A1-flake-round-1.md). Checked here from that report; the runs were not repeated.

## Ticket reading

The summary as recorded in the queue: TestTlogReplica.testRecovery sometimes fails. Test class from the report: org.apache.solr.cloud.TestTlogReplica.

## Code reading

Not read for this record; the verdict rests on the run data and the grep result given below.

## History

No commit history was used for this record.

## Reproduction attempts

Command: :solr:core:test --rerun --tests org.apache.solr.cloud.TestTlogReplica (seed printed by Gradle: B2EDDADF9B4A881F). Result: pass 1 FAIL (rc 1); then rerun at the same seed: FAIL. Counts tests/failures/errors: 14/2/0. Pass 1 and the rerun at the printed seed both failed with 2 failing methods (testKillLeader: Expected collection to be 1x2; testRecovery: Replica didn't recover). Index fetch raised FileSystemException: Too many links on segment files. Conclusive: the failure repeats at the same seed, but the environmental cause is not ruled out.

## Gap analysis

The reproduction is consistent, but the cause could be the test environment rather than Solr. A run on a different filesystem would separate the two.

## Verdict justification

keep-open: the failure reproduced at the pinned head with the same seed, but the error is a filesystem limit, not yet a product defect. Whether it is specific to this environment is not shown.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub.
