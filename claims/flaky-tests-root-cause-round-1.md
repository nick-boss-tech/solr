# Claim: flaky test root causes, round 1

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` moved from `1aef9356a83` to `3dbcd5ac70d`, one commit: "Assignment: flaky test root causes round 1", which adds `assignments/flaky-tests-root-cause-round-1.md`. The assignment asks for four CI flakes from 2026-10-10 to be analyzed: the mechanism of each, ranked root-cause hypotheses with citations, the evidence for and against, a proposed fix or hardening (described, not applied), and the main-side verification run that would settle it.

This round is analysis only. No builds, no tests, no branch edits, no drafts, no production or test code changes. The review side does not run builds or tests.

## Code base

Every code citation in this round reads `upstream/main` at `8e62c2686882` (local ref, not re-fetched), with the commit date 2026-10-09. The failing runs are from 2026-10-10. If main has moved since, the report says so for each citation it could not confirm at this SHA.

Path check at `8e62c2686882`:
- `GCSInstallShardTest.java`, `RecoveryAfterSoftCommitTest.java`, `TestCoordinatorRole.java`, `LeaderElectionIntegrationTest.java`: present.
- `solr/core/src/java/org/apache/solr/embedded/JettySolrRunner.java` (the path the assignment gives for test 4): not present. The subagent for test 4 locates the class with `git grep` and cites where it is.

## Staffing

Four subagents, one per test, in parallel. The lead writes the roll-up and the cross-test pattern section (the assignment's closing section), from the four reports. Four is within the cap of six at once, so no other round runs while this one does.

## Parts

- Test 1: `GCSInstallShardTest`, suite teardown collects restore errors.
- Test 2: `RecoveryAfterSoftCommitTest.test`, HTTP/2 channel closed mid-request.
- Test 3: `TestCoordinatorRole.testNRTRestart`, same exception shape after a restart.
- Test 4: `LeaderElectionIntegrationTest.testSimpleSliceLeaderElection`, port already in use.

Tests 2 and 3 are read by their own subagents. The pattern question (one mechanism or two) is answered by the lead from both reports, and the report commits to one answer with the code that decides it.

## Deliverables

1. `reports/flaky-tests-root-cause-round-1.md`: one section per test with the four deliverable parts, then the pattern section, then the owner decisions. Written by the lead from the four part reports.
2. Part reports as text from each subagent, written to `reports/flaky-tests-root-cause-round-1-t1.md` through `-t4.md` by the lead.

## Standing rules for the text

- No em dashes anywhere, including the report.
- Voice avoid-list from the assignment: load-bearing, seam, delve, robust, seamless, crucial, leverage (as a verb), "it's important to note", "in terms of", "not just X but Y". Plain concrete wording.
- No PR numbers in commit messages or file names. Source runs are cited by workflow run id, as the assignment does.
- A code claim needs a file and line citation, or a quote from the failure record. Inferences are marked as inferences.

## Not in scope

Opening or editing PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, test runs, and any change to production or test code.
