# Skip record: SOLR-12819

- Ticket: SOLR-12819, TestPointFields.testIntPointFieldSortAndFunction failure.
- Verdict: keep-open.
- Draft branch: solr-12819-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 2 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-10-01, updated 2019-06-08, no fix versions, no links, no comments. The report: a Jenkins master run on Linux (JDK 11) failed in `TestPointFields.testIntPointFieldSortAndFunction`; the reporter says it also reproduces locally on Java 8. The ticket text is cut off after the environment lines, so the failing assertion and the seed are not in the ticket text available here.

## Code reading

Read at the pin:

- `solr/core/src/test/org/apache/solr/schema/TestPointFields.java:179`: `public void testIntPointFieldSortAndFunction() throws Exception`. The test exists under the ticket's name, and no `@AwaitsFix` or `@BadApple` marker is on the method in the file read.
- The assertions of the test (the sort order and the function query results on an int point field) were not traced in this pass; the ticket's failing assertion is not in the text available.
- No commit at the pin names SOLR-12819 (the evidence tool lists none).

## History

- The test's history was not traced in this pass.
- No commit at the pin names the ticket.

## Reproduction attempts

Not run. This pass ran no builds or tests. The ticket's own report (a Jenkins failure on JDK 11, reproducible on Java 8 for the reporter) names the reproduction: run `TestPointFields#testIntPointFieldSortAndFunction` at the pin under JDK 11 and JDK 8, and record the failing assertion.

## Gap analysis

- The test is present and unmarked at the pin; the failure's assertion is not in the ticket.
- The ticket gives two JDK versions, so the first check is whether the test fails under JDK 8 or JDK 11 at the pin.
- Without the failing assertion or a run, the cause cannot be attributed to a line.

## Verdict justification

keep-open: the test is present and unmarked, the failure's assertion is missing from the ticket, and no run at the pin has been made. close-cannot-reproduce needs a reproduction attempt at the pin with the ticket's JDK; close-fixed and close-obsolete do not fit.

Missing for this verdict: a run of `TestPointFields#testIntPointFieldSortAndFunction` at the pin under the ticket's JDK versions, with the failing assertion or the pass result and the test XML.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
