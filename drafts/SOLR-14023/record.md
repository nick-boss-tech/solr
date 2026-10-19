# Skip record: SOLR-14023

- Ticket: SOLR-14023, tests.src.home points at ${user.dir}" for tests (?!). JIRA status not recorded in the B2 reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-14023-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B2 reading (reports/2026-10-18-skip-repro/B2-removed-round-2.md, vm3, round 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: tests.src.home points at ${user.dir}" for tests (?!).

## Code reading

solr/test-framework/src/java/org/apache/solr/util/ExternalPaths.java:71-95 (determineSourceHome) still searches upward from the working directory for test-framework/build.gradle, with tests.src.home as the fallback at line 81. gradle/testing/randomization.gradle:132 now sets tests.src.home to null, so the ${user.dir} setting is gone.

## History

- None cited in the B2 reading.

## Reproduction attempts

Not attempted. The B2 scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. B2 ran no builds.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
