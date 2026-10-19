# Skip record: SOLR-13678

- Ticket: SOLR-13678, ZkStateReader.removeCollectionPropsWatcher can deadlock with concurrent zkCallback thread on props watcher. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-08-02, updated 2020-01-21, linked to SOLR-11960 (Closed); five comments, the last on 2019-08-06.
- Verdict: close-fixed.
- Draft branch: solr-13678-draft-close (the existing close draft; this record replaces its draft files, which had a truncated pinned head and a check dated 2026-10-18).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Fix commit, ancestry and changed code checked at this head (detached worktree).
- Source: remediation redo of batch 06, item 3, under assignments/pool-aaa-skip-verdict-remediation-06.md. The earlier draft's claim (fix is SOLR-17720, commit 67a642fe02) is checked here.

## Ticket reading

`removeCollectionPropsWatcher` takes the collection's lock inside a `compute()` call on the observers map, while a zkCallback thread that fires the same watchers takes the locks in the opposite order. The thread dump in the ticket shows the blocked thread at `removeCollectionPropsWatcher`.

## Fix commit and code at the pinned head

- Fix commit: 67a642fe02 (2025-04-22), "SOLR-17720: Fix rare deadlock in CollectionProperties (#3304)". `git merge-base --is-ancestor 67a642fe02 498a09aea97aac86c2cc7ac114113b946d249942` succeeds. The commit changes solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/CollectionPropertiesZkStateReader.java (25 lines) and solr/CHANGES.txt.
- The commit moves `synchronized (getCollectionLock(collection))` out of the `collectionPropsObservers.compute(...)` call. At the pin, CollectionPropertiesZkStateReader.java:39-57 has the lock at :44 (`synchronized (getCollectionLock(collection))`) and the `compute` call inside it at :45. The comment at :42-43 says the lock must be taken outside compute() to avoid the deadlock. The changed code is present.
- The earlier commit a052fb5436 (2019-08-05, SOLR-13678) hardened CollectionPropsTest.testReadWriteCached; it did not change the lock order and does not fix the deadlock.

## Reproduction attempts

Not run. The thread dump in the ticket is from an older head, and the deadlock is a timing race, so a run at the pin would need a deliberate interleaving. This record rests on the code change.

## Gap analysis

The lock-order inversion the ticket describes is removed in code by 67a642fe02. The record does not reproduce the deadlock before and after the commit.

## Verdict justification

close-fixed: a commit on main (67a642fe02, SOLR-17720) makes the lock-order change that removes the deadlock the ticket describes, the commit is an ancestor of the pinned head, and the changed code is present at CollectionPropertiesZkStateReader.java:44-45. The earlier test-hardening commit a052fb5436 is not the fix.

## Limits

This record does not run a deadlock test. It does not post anything to JIRA or GitHub. No PR is opened from this draft.
