# Skip record: SOLR-5628

- Ticket: SOLR-5628, "Cloud test harness can cause index files to be deleted before IndexWriter is closed". JIRA status Open, resolution none, fix versions none. Created 2014-01-13, updated 2018-01-11. Checked 2026-10-18 UTC.
- Verdict: keep-open.
- Draft branch: solr-5628-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main). No upstream sha cited; the ticket's SVN revisions are quoted below as the ticket states them.
- Checked by: vm5, 2026-10-18 UTC.

## Ticket reading

The original symptom: Jenkins found a seed that causes an IndexWriter assertion failure in `TestDistribDocBasedVersion` on the 4x branch. The reporter's reading is that the cloud test framework deletes index directories before IndexWriter is closed. Later, failures on other seeds added a stall at test cleanup, where the runner has to stop threads.

The ticket notes that the failing seed stopped reproducing when an unneeded `commit()` was added, and that the test ends with uncommitted documents.

Comments that matter:
- 2014-01-13 (hossman, citing McCandless): the main thread removes the index directory before another thread calls IndexWriter.close.
- 2014-01-13 (dweiss): a seed is only valid while the code is unchanged, because the class name is mixed into the seed.
- 2014-03-06: workaround commits on dev/trunk (r1574941) and branch_4x (r1574942), "work arround for this test to avoid whatever bug is in the cloud test framework".

Reproduction recipe in the description: `svn update -r 1574381`, then run `TestDistribDocBasedVersion` with `-Dtests.seed=1249227945045A2E -Dtests.locale=ko_KR -Dtests.timezone=America/Monterrey -Dtests.file.encoding=ISO-8859-1` on the 2014 code. The ticket's own text says this recipe is for an older trunk.

No linked issues.

## Code reading

- solr/core/src/test/org/apache/solr/cloud/TestDistribDocBasedVersion.java:104: `commit(); // work arround SOLR-5628`. The workaround is still in the test at the pinned head (`grep -rn "work arround SOLR-5628" solr/core/src/test/org/apache/solr/cloud/` finds this one line).
- The ticket names no framework code path beyond the test class, so the framework's index-directory cleanup was not read at the pinned head. That is part of the missing piece below.

## History

- b221115ebc, 2014-03-06, "SOLR-5628: work arround for this test to avoid whatever bug is in the cloud test framework". Ancestor of 498a09aea9: yes. This is the workaround commit; it is not a fix for the framework behavior.
- No commit on main names SOLR-5628 as a fix.

## Reproduction attempts

All runs use `:solr:core:test --rerun --tests TestDistribDocBasedVersion` at head 498a09aea9, with the line `commit(); // work arround SOLR-5628` removed in the repro worktree only. The workaround line was restored after each series; the repro worktree is clean at 498a09aea9.

1. 5628-mut-r1: seed 5C08B289F49822BA. Result: PASS (2 run, 0 failures). Log `/workspace/gates/logs/vm5/jira/5628-mut-r1.log`, XML `5628-mut-r1.xml/`. Conclusive: no. One pass with the workaround removed is a weak signal.
2. 5628-mut-r2: seed A724BBA2BE293D7E. Result: PASS (2 run, 0 failures). Log `/workspace/gates/logs/vm5/jira/5628-mut-r2.log`, XML `5628-mut-r2.xml/`. Conclusive: no, same reason.
3. 5628-mut-r3 to r6: seeds 66D54ADD1FBC0370, 2CC62960048BE36E, 43422EEDA46C6F09, C9B32D7322B8F225. Result: BUILD SUCCESSFUL on each. Logs `/workspace/gates/logs/vm5/jira/5628-mut-r{3,4,5,6}.log`; XML for r6 only, `5628-mut-r6.xml/`. Conclusive: no. Six runs without the workaround and no failure is consistent with the bug being gone, or with it being rare, and the runs cannot tell those apart.

Not attempted: the ticket's seed 1249227945045A2E on the head. The ticket states that a seed is valid only for the code it was recorded on, and the head differs from the 2014 code. Not attempted: the 2014 revision r1574381. That revision needs the ant-era build, and this pool host runs Gradle only.

## Gap analysis

The ticket describes two failures: an IndexWriter assertion on a specific seed, and a stall at cleanup. The workaround commit in 2014 hid the failure by adding a commit that the test does not need. On the current head, six runs with the workaround removed passed, using new seeds. That is evidence the failure is rare or gone on this code path, but not proof, because the failure was intermittent in 2014 and the original seed cannot be replayed on this code.

Uncertainty that remains: whether the framework deletion race still exists, which only the original seed or the 2014 revision could show.

## Verdict justification

keep-open, per the rubric's "inconclusive after the attempts listed" rule. Evidence met: pinned head sha, JIRA status and date, code path with file and line, git history with ancestry, and six reproduction attempts with command, seeds, results, and log paths.

Missing for a conclusive verdict: either the original seed replayed on code that matches 2014 (svn r1574381, which needs an ant-era build), or a failing seed on the current head with the workaround removed. Either would show whether the framework behavior is still present.

## Limits

This record does not establish that the 2014 failure is absent from the framework. It does not run the original seed. It does not test the 4x branch.
