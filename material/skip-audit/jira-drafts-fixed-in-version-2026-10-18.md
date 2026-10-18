# Draft JIRA comments: 10 fixed-in-version skips with commits on main

Status: drafts only. Nothing has been posted or changed in JIRA. Posting is manual until we find a way to automate it.
Checked: 2026-10-18 against origin/main 498a09aea9 (fork) and live JIRA REST. All ten are Open with no resolution.
Commit hashes come from the fork's origin/main. Confirm each hash on apache/solr before posting. The JIRA bot comments already show the same hashes for most of them.

## Summary

| Ticket | Recommendation | Why |
| --- | --- | --- |
| SOLR-13066 | Close as Fixed | Reload-failure close fix is on main |
| SOLR-11008 | Close as Fixed | Test fix on main; reporter says the test passes |
| SOLR-13678 | Close as Fixed | Deadlock fixed by SOLR-17720 (2025) |
| SOLR-7804 | Close as Fixed | Test bugs fixed; product bug split to SOLR-7829 (closed) |
| SOLR-4753 | Close as Cannot Reproduce (maintainer call) | Assert on main; ticket does not show the reporter's path |
| SOLR-14159 | Keep open | Failures reported after the commit; parent SOLR-13486 still open |
| SOLR-16430 | Keep open | The failing test's 20-second wait was not changed; failure recurred |
| SOLR-5516 | Keep open | The 2.5-second sleep is still the only mitigation; author said it's not enough |
| SOLR-5628 | Keep open | Test workaround still in place; underlying bug never identified |
| SOLR-8380 | Keep open | Linked SOLR-8469 is open; no cause found on main |

Five can close now, with four clear and one needing a maintainer decision. The other five stay open. The "fixed in 4.9/6.0" note was wrong for most of the set.

---

## SOLR-13066: Close as Fixed

Checked on main (498a09aea9). The fix from commit 7de72c9bc7 is still in place. In `CoreContainer.reload`, `newCore` is closed in the `finally` block when the reload did not succeed and the open count is still above zero (`solr/core/src/java/org/apache/solr/core/CoreContainer.java`, around line 2054). The branch_7x backport is 4bcad18084. The commit added no regression test.

Proposed resolution: Fixed

---

## SOLR-11008: Close as Fixed

Checked on main. Commit 7c4584bd4f changed `MetricsHandlerTest` to use `solrconfig-minimal.xml` and to wait until the core is up before the test starts (`solr/core/src/test/org/apache/solr/handler/admin/MetricsHandlerTest.java`, line 40). The reporter said in 2018 that the test no longer fails, and the change was committed on that basis.

Proposed resolution: Fixed

---

## SOLR-13678: Close as Fixed

The deadlock is in `removeCollectionPropsWatcher`. Commit a052fb5436 (2019) only hardened `CollectionPropsTest.testReadWriteCached` and did not fix the lock ordering. The fix is SOLR-17720, commit 67a642fe02 (April 2025). It moves the lock on the collection outside `collectionPropsObservers.compute()`, which removes the lock-order inversion this ticket describes (`solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/CollectionPropertiesZkStateReader.java`).

I checked the code change, not the thread dump attached to this ticket.

Proposed resolution: Fixed (link SOLR-17720)

---

## SOLR-7804: Close as Fixed

The failures were test bugs, plus a product bug that was split out to SOLR-7829, which is closed.

- 1692552: `TestCloudPivotFacet` generated the same `facet.pivot` parameter twice.
- 1693160: epsilon comparison was wrong for negative expected values.
- 1e0e8d45e5 (2018): removed assertions on lossy `stats.field` values. This is the change that fixes the seed failure reported on 2018-10-31.

Caveat for the maintainer: the 2018 change removes checks on lossy stats values. Coverage was reduced on purpose.

Proposed resolution: Fixed

---

## SOLR-4753: Close as Cannot Reproduce (maintainer call)

The ticket reports "Too many close [count:-1] for SolrCore" from Solr 4.2.1. On current main the message still exists and it still asserts (`solr/core/src/java/org/apache/solr/core/SolrCore.java`, lines 1727-1730). Over-closes now fail tests instead of going unnoticed.

The closest code path I found that can over-close a core is a reload that fails, which SOLR-13066 fixed (7de72c9bc7). The ticket does not show which path the reporter hit, so I cannot confirm that it is the same bug. Solr 4.2.1 is also out of support.

The fixVersion values 4.9 and 6.0 are not correct for this ticket.

Proposed resolution: Cannot Reproduce (or Fixed, if the maintainer accepts SOLR-13066 as the fix)

---

## SOLR-14159: Keep open

Commit 6b3e7feba1 (2020-01-22) replaced spin loops in `TestCloudConsistency` and `TestTlogReplayVsRecovery` with `waitForState` and `expectThrows`. It does not address the two failure modes in the ticket ("Server refused connection" and "Timeout waiting for active collection").

Evidence:
- Erick's 2020-01-23 comment reports two failures in about 1,000 runs after the commit, one of them a 404 on a `text/html` response.
- The last comment (2020-04-14) says there were no failures in four weeks, which is the only "not reproducing" evidence and is thin.
- The ticket was moved over from SOLR-13486 "as per Hoss". SOLR-13486 (race between leader replay on startup and recovery from leader) is still Open, last updated 2020-07-24.

Next step, if wanted: beast `TestCloudConsistency` on main. A clean run count would help, but it does not cover the SOLR-13486 race. This ticket should be closed only together with a decision on SOLR-13486.

---

## SOLR-16430: Keep open

Commit 9e72a75caa changed one wait in `AbstractIncrementalBackupTest` (`solr/test-framework/src/java/org/apache/solr/cloud/api/collections/AbstractIncrementalBackupTest.java`) from 20 to 30 seconds. The failure in the ticket is `expected:<COMPLETED> but was:<RUNNING>`.

Evidence on main:
- `testRestoreToOriginalCollection` (line 196) still waits 20 seconds at line 215. That is the test krisden's 2022-10-21 build link names. The fix did not touch it.
- `testRestoreToOriginalSucceedsWithErrors` (line 493) still waits 20 seconds at line 516.
- Krisden's 2022-10-21 comment says the failure was still happening after the commit.

This looks like a partial fix. Raising the two remaining 20-second waits to 30 is a small test change, and it would need a PR, so it is not a close-without-PR action. Do not close.

---

## SOLR-5516: Keep open

Commit 2cc87df6ef (2013) replaced the 1.5-second pause with a 2.5-second sleep, only for replacement leaders. Commit 07a9b5d1b0 (2020) moved that code into `ShardLeaderElectionContext.runLeaderProcess`. The sleep is still there (`solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java`, around line 209), so the mitigation the ticket relies on is still in place.

Mark Miller's last comment (2013-12-06) says the wait helped in his testing, is not enough for real life, and some runs on slow boxes still fail. Nothing in the ticket shows the underlying cause (updates arriving during leader sync) was fixed. `ChaosMonkeyNothingIsSafeTest` is still on main.

Do not close without a repro or a maintainer decision.

---

## SOLR-5628: Keep open

Commit b221115ebc (2014-03-06) adds `commit(); // work arround SOLR-5628` to `TestDistribDocBasedVersion` (`solr/core/src/test/org/apache/solr/cloud/TestDistribDocBasedVersion.java`). The workaround is still there on main.

The ticket describes a test framework bug where index directories are deleted before IndexWriter closes. Hoss's last comment (2014-03-06) says the workaround avoids "whatever bug is in the cloud test framework", so the framework bug was never identified. Do not close as fixed.

---

## SOLR-8380: Keep open

Commit d6b3312d82 (2015) only changed an assertion message in `ZkController`. The ticket's symptom is a generic `500 Server Error` on a remote request from `CollectionsAPIDistributedZkTest`, which is linked to SOLR-8469 ("how HttpSolrCall#remoteQuery handles IOException and passing error messages back"). SOLR-8469 is still Open.

Mark's 2015-12-29 comment suspected the 4096-byte request buffer on the client side. On main I found no `setRequestBufferSize` in `solr/core/src/java`, and Jetty's `requestHeaderSize` in `solr/server/etc/jetty.xml` defaults to 8192 (`solr.jetty.request.header.size`). I did not confirm that this change is the cause, so the question remains open.

Do not close.
