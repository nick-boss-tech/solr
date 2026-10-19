# Skip record: SOLR-15226

- Ticket: SOLR-15226, org.apache.lucene.index.IndexNotFoundException: no segments* file found in LockValidatingDirectoryWrapper(NRTCachingDirectory. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-03-08, updated 2024-08-16, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-15226-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep run at this head (detached worktree).
- Source: remediation redo of batch 03, item 21, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (vm2, from the B3 reading) is background only; its grep is re-run here.

## Ticket reading

After a network failure, one of three standalone cores ("sei-protocols") fails with "Error opening new searcher" and an IndexNotFoundException: no segments* file, although the index directory is not empty and lists segment files. Restarts did not help. The reporter asks how to fix it.

## Grep and code reading at the pinned head

- `git grep -n -i "no segments" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' '*.adoc'` gives three lines: solr/core/src/java/org/apache/solr/handler/admin/api/UpgradeCoreIndex.java:363 (a code comment, "no segments to process/validate"), solr/core/src/test/org/apache/solr/handler/admin/UpgradeCoreIndexActionTest.java:230 (a test comment), and solr-ref-guide/modules/indexing-guide/pages/reindexing.adoc:141 (a sentence about checking that an index has no segments). None of these is the exception in the ticket. The exception is raised in Lucene, outside this repository.
- Recovery documentation: solr/solr-ref-guide/modules/deployment-guide/pages/user-managed-index-replication.adoc:351 has a section "Resolving Corruption Issues on Follower Servers". It applies to a replication follower that is out of step with its leader. The ticket does not say the core is a replication follower, so the section may not apply.
- `git grep -n -i "checkindex"` in solr/solr-ref-guide finds only spell-checking index directory settings; the reference guide has no CheckIndex or index repair page.

## History

- None cited in the earlier B3 reading. No commit names SOLR-15226.

## Reproduction attempts

Not run. The exception comes from the index files on disk after a network failure, which this environment cannot produce.

## Gap analysis

The answer depends on a fact the ticket does not give: whether the core is a replication follower. If it is, the follower section at user-managed-index-replication.adoc:351 is the documented recovery and could answer the question (close-answered with that link). If it is a standalone core, the guide has no repair page for this exception, and the answer is a recovery procedure the project would have to write.

## Verdict justification

keep-open: the attempts made are the greps above and the Jira read. The missing piece is whether the reporter's core is a replication follower (and whether the follower section's steps apply to it), and the reporter's index state after the failure. close-answered is not supported until the follower fact is known, because the section is for followers only.

Missing for a closing verdict: (1) the reporter's replication setup (leader or follower, and whether the core replicates); (2) if a follower, a check that the section's steps cover this state.

## Limits

This record does not reproduce the exception. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
