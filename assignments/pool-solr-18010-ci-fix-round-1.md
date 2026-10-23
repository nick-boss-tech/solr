# Assignment: fix the upstream CI failure on PR #5118 (SOLR-18010)

Pool-open, any Linux host. Claim-first as claims/pool-solr-18010-ci-fix-round-1.md. Deliverable: reports/pool-solr-18010-ci-fix-round-1.md.

## The failure

Upstream CI on PR #5118 fails at exactly one place (Crave build 307012 and the GitHub Actions run agree; the solrj suite passed 1357 tests and nothing else in either log failed). Task :solr:core:compileTestJava, Error Prone running with -Werror as CI does:

```
solr/core/src/test/org/apache/solr/handler/admin/SecurityConfHandlerFailurePathsTest.java:361: warning: [ReferenceEquality] Comparison using reference equality instead of value equality
    if (group == FAKE_GROUP) {
```

Local builds skip Error Prone by default, which is how this reached CI. Branch: solr-18010-submit at 46869c4d8bc9ab26366a6ba97ba884bc2040911b (verify by ls-remote before touching it; if the tip has moved, work from the live tip and say so in the report).

## The work

1. Fix line 361 with a value comparison. Choose between Objects.equals(group, FAKE_GROUP) and group.equals(FAKE_GROUP) from the code: if group can be null at that point, Objects.equals is the correct one. Read the surrounding test before choosing; the wrong choice trades a compile error for an NPE.
2. Scan the branch's whole diff against its merge-base for the same pattern (reference equality on objects or strings, in test and production code alike). CI stops at the first error, so a second instance may be hiding behind this one. Fix any you find the same way; if an instance is deliberate (an identity sentinel compared on purpose), leave it and name it in the report with the reason.
3. Verify locally with Error Prone ON: :solr:core:compileTestJava -Pvalidation.errorprone=true must pass, then run the focused class org.apache.solr.handler.admin.SecurityConfHandlerFailurePathsTest and count from fresh JUnit XML. Then the branch's normal gate scope for the touched module if the focused run or the scan changed anything beyond the one file.
4. Push to solr-18010-submit (this branch only; pushes to the owner's own open PR branches are authorized). Verify by ls-remote. Do not flip the PR out of draft, do not comment on the PR, do not post anywhere.

## Standing rules

Commits as Nick Shanin, no trailers, no "Claude" in author, committer or trailer, no em dashes in any written text. Tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4. Evidence stands on the run's log file and fresh JUnit XML on disk only. If the branch's premise breaks while you are in it (the fix turns out not to be the whole failure), stop, record the evidence in the report, and hand it back rather than widening the change.
