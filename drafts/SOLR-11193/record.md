# Skip record: SOLR-11193

- Ticket: SOLR-11193, EnumField: multivalued sort and range queries don't work. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11193-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Source: A2 flake locate round 1 (reports/2026-10-18-skip-repro/A2-flake-locate-round-1.md). Checked here from that report; the runs were not repeated.

## Ticket reading

The summary as recorded in the queue: EnumField: multivalued sort and range queries don't work. Test class from the report: org.apache.solr.schema.EnumFieldTest.

## Code reading

Not read for this record; the verdict rests on the run data and the grep result given below.

## History

No commit history was used for this record.

## Reproduction attempts

Command: :solr:core:test --rerun --tests org.apache.solr.schema.EnumFieldTest --rerun --tests org.apache.solr.schema.EnumFieldTest (seed printed by Gradle: 364457875F164E62). Result: build SUCCESSFUL. Counts tests/failures/errors/skipped: 15 / 0 / 0 / 1. Rerun: not run. Conclusive: no, because the test is flaky and one passing run cannot rule out the failure.

## Gap analysis

One passing run does not settle a flaky test. Repeated runs at several seeds would be needed.

## Verdict justification

keep-open: a single run passed, which is not a reproduction of a flaky test. A passing run does not show the problem is gone.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub.
