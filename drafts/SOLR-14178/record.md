# Skip record: SOLR-14178

- Ticket: SOLR-14178, "Tests emitting megabytes of output". JIRA status Open, resolution none, fix versions none, created 2020-01-08, updated 2020-01-21. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14178-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14178-vm2.md).

## Ticket reading

The Gradle test runner warns when a test writes more than 10 MB of output. The ticket lists five tests from January 2020 (DocValuesTest, TestCloudJSONFacetSKG, TestRandomFaceting, TestJsonFacets, TestDistributedSearch) with 10 to 39 MB of output each, and asks that the logging be reduced. One comment (dweiss, 2020-01-08) says the solr/core JUnit XML output for a full run was about 400 MB.

## Code reading

- The ticket is about test logging volume, not product code. The named test classes are at the pinned head (for example solr/core/src/test/org/apache/solr/schema/DocValuesTest.java; a bounded search of the tree found the class).
- No commit on main names SOLR-14178 (`git log --oneline --grep=SOLR-14178 origin/main`: no output).
- The output volume was not measured at the pin in this pass. Measuring it needs the test runs whose logs the runner reports on.

Commands at the pinned head:
- git ls-tree -r --name-only origin/main | grep -E 'DocValuesTest.java|TestJsonFacets.java|TestRandomFaceting.java': the named test classes are present.
- git log --oneline --grep='SOLR-14178' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14178: none.

## Reproduction attempts

Not run. Measuring the output of the five named tests needs their runs with the runner's byte counts, which were not made in this pass.

## Gap analysis

The problem is measured in 2020 output sizes. Whether the same tests still write megabytes of output on the pin, and whether any logging change has been made, is not established. The fix (less logging) is a test-hygiene change and would be judged by a measured size before and after.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, the code paths (the named test classes) are cited, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket. close-obsolete does not hold: the named tests are present. close-cannot-reproduce does not apply to a measurement request without a run. The missing piece is the measured output size of the five named tests on the pin.

## Limits

This record does not measure the output. It does not change any test's logging. It does not check the commits that touched the named tests.
