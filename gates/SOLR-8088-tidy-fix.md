# SOLR-8088 tidy fix and re-gate

Status: OPEN

## Why

Commit 0f473402de035508ec9c1b1cb9a2d5453ee72a29 on solr-8088-submit added testMultiValuedGroupFieldIsBadRequest to TestDistributedGrouping and dropped the nick line from the changelog fragment. Its verification gate (receipts/SOLR-8088.md, section "Moved tip, 2026-10-15") stopped GATE INVALID at tidy: spotless joins the new test's expectThrows call onto one line, so the pushed tree is not tidy-clean. A diagnostic on the tidied tree passed every remaining step (Error Prone compile, both proof legs by content, SearchGroupsFieldCommandTest 2 of 2, TestDistributedGrouping 3 of 3, module check), so the only known defect is the formatting.

## Steps

1. Verify the fork tip of solr-8088-submit is 0f473402de035508ec9c1b1cb9a2d5453ee72a29 (git ls-remote). If it has moved, stop and record the new tip here.
2. In a fresh worktree (copy gradle.properties in), run tidy for :solr:core. Confirm the only change is the one-line spotless join in TestDistributedGrouping.java; if tidy changes anything else, stop and record what it changed. Commit the tidy change alone (Nick Shanin identity, no trailer, no em dashes).
3. Run the full gate at the resulting head: changelog YAML parse of changelog/unreleased/SOLR-8088-grouping-multivalued.yml with a real parser; tidy again with the GATE INVALID guard (restore gradle/libs.versions.toml; the tree must be unchanged); Error Prone compile (-Pvalidation.errorprone=true); proof legs on base cabedd1d968059215188f4e7563fb303241899ed with the branch tests in place (SearchGroupsFieldCommandTest: 2 tests, exactly 1 failure, testMultiValuedFieldIsRejected; TestDistributedGrouping.testMultiValuedGroupFieldIsBadRequest: fails with "expected:<400> but was:<500>"); head runs counted from fresh JUnit XML (SearchGroupsFieldCommandTest 2 of 2, TestDistributedGrouping 3 of 3), seed 8088C0FFEE8088; module check :solr:core:check -x test. Blocking flock; never two Gradle builds at once; the claiming host's Gradle route per its host file.
4. On green: push plainly to solr-8088-submit (the branch backs the owner's own draft PR; standing push authorization), refresh receipts/SOLR-8088.md, and update PR #5144's Proof to the new verified head, covering both tests (the distributed test fails on base code with a 500 and passes at the head). Mark this job DONE and the claim DONE, and append the outcome to the takeover log.
5. On an executed-test failure: record it here and in the takeover log with the failing test, the seed and the reproduce line; do not relaunch.
