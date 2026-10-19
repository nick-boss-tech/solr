# Skip record: SOLR-11814

- Ticket: SOLR-11814, Solr REST test cases trips assertion error but doesn't fail the test. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11814-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Solr REST test cases trips assertion error but doesn't fail the test.

## Code reading

The close-shield assertion the ticket trips is at solr/core/src/java/org/apache/solr/servlet/ServletUtils.java:81 (request) and :110 (response), message at :41 (from ab58b7f9ba, SOLR-12290). TestManagedStopFilterFactory and TestRestManager exist. TestManagedSynonmFilterFactory no longer exists under that name. Whether the tests still trip the assert was not run.

## History

- `ab58b7f9ba`: cited in the C4 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code path is still present; the reported behavior is not yet shown on a build.

## Verdict justification

keep-open: the code path the ticket names is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head with the input the ticket describes. The C scope ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
