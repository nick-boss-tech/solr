# Skip record: SOLR-15848

- Ticket: SOLR-15848, "Fix some BadApple tests for branch_8_11". JIRA status Open, resolution none, fix versions none, created 2021-12-14, updated 2022-02-02. Checked 2026-10-19 UTC.
- Verdict: keep-open (changed from close-fixed; this redo supersedes the recorded verdict).
- Draft branch: solr-15848-draft-open (the earlier close-fixed branch solr-15848-draft-close is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15848-vm2.md).

## Ticket reading

The ticket is a release-branch maintenance item: during the 8.11.1 release some tests were unstable, and they were marked BadApple on branch_8_11. The two JIRA comments name the commits on branch_8_11: b895ff469e4 (SOLR-15848 BadApple failing tests in branch_8_11, PR #2632) and 0b002b11819 (SOLR-15848 BadApple ScheduledTriggerIntegrationTest). No test class is named in the ticket text.

## Code reading

- The fix commits are on branch_8_11. The fork has no branch_8_11 (git ls-remote --heads origin: no match), so the fix is not on the pinned head.
- 0b002b11819 marks ScheduledTriggerIntegrationTest. That class was removed from main by cc0c111949d (SOLR-14656, Removing Autoscaling Framework, 2020). `git grep -l ScheduledTriggerIntegrationTest origin/main -- solr` returns no match. The named test is gone on main.
- b895ff469e4 changed seven test files (14 insertions, 1 deletion). Three of them are on main: OverseerRolesTest.java, UnloadDistributedZkTest.java and DistributedQueryComponentCustomSortTest.java. None of the three carries a BadApple or AwaitsFix marker at the pinned head (git grep -c 'BadApple' origin/main over solr/core/src/test lists seven files, none of them these three). Two others on main are absent: CloudSolrClientTest and ConcurrentUpdateSolrClientBuilderTest are not in the tree at origin/main, and ConnectionManagerTest and CdcrBootstrapTest are also absent by file name (the git ls-tree check returned no match).

Commands at the pinned head:
- git cat-file -t b895ff469e41987c993fbb49f51fbe9882a10fca: commit (present in this repository).
- git ls-tree -r --name-only origin/main | grep -c '/<file>$': per-file checks above.
- git grep -c 'BadApple' origin/main -- solr/core/src/test: seven files.

## History

- b895ff469e4 (2021-12-14, branch_8_11): SOLR-15848 BadApple failing tests in branch_8_11 (#2632). Not on main.
- 0b002b11819 (2021-12-14, branch_8_11): SOLR-15848 BadApple ScheduledTriggerIntegrationTest. Not on main.
- cc0c111949d (2020-07-20): SOLR-14656 removing the Autoscaling framework, which removed ScheduledTriggerIntegrationTest from main.
- Commits on origin/main naming SOLR-15848: none.

## Reproduction attempts

Not attempted. The question on main is whether the three surviving tests are stable without the marker; that needs a run of each test on the pinned head, which was not done in this pass.

## Gap analysis

On main, the named test is removed, and the surviving tests from PR #2632 are not marked BadApple, so the markers the ticket describes are not on main. The ticket's subject is the 8.11 release branch, which is not in this repository, so whether the 8.11 markers still need fixing is a maintenance decision for the owner. Whether the three surviving tests are stable on main is not established.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: the fix commits are on branch_8_11, which is not on the pinned head, so no fix for main exists. close-obsolete does not hold as a whole: the named test is removed, but the ticket is about the 8.11 branch, which is not in this repository, and the other tests in PR #2632 are not all accounted for. The missing pieces are (1) an owner decision on whether branch_8_11 still needs these markers, and (2) for the surviving tests on main, a run to confirm they are stable.

## Limits

This record does not check branch_8_11 (it is not in this repository). It does not run the three surviving tests. It does not account for every file in PR #2632 on main.
