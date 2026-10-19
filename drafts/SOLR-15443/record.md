# Skip record: SOLR-15443

- Ticket: SOLR-15443, Atomic Update of parent document in nested documents fails and needs solr restart. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-05-28, updated 2021-06-23, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-15443-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 7, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

An atomic update to a child document in a nested block returns HTTP 500: "Did not find child ID person-1 in parent ID ..." (the trace names trader-2 as the child). The reporter says the error persists until Solr is restarted. The stack trace is from an older release: at the pinned head the throw is at AtomicUpdateDocumentMerger.java:112, not :110.

## Code reading

- solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java:755-763: the old root document is fetched by a realtime get with `Resolution.ROOT_WITH_CHILDREN` (RealTimeGetComponent.java:398, :669 pass the same resolution). The comment on :763 says the fetch returns the root with its children.
- DistributedUpdateProcessor.java:767-783: when the fetched root exists, `docMerger.merge(sdoc, oldRootDocWithChildren)` is called (:782). The merged result replaces the command's document.
- solr/core/src/java/org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java:92-112: `merge` calls `mergeChildDocRecursive` on the fetched tree, searching by the child's id; if no node matches, it throws IllegalStateException at :112 with the message in the ticket.

So the failure happens when the fetched root-with-children does not contain a child whose id is in the update. The code shows only where the failure is raised; it does not show why the fetched tree is missing that child. Two explanations fit the ticket: (a) the child is not visible to the realtime get until a commit or a restart (a searcher or update-log state problem), or (b) the child documents were indexed without the nested-path fields that the tree lookup relies on (the 8.x _nest_path_ and _root_ behavior, which SOLR-15540 also touches).

## History

`git log -S "Did not find child ID"` gives 4cb3ad4a1c (SOLR-14923, 2021-01-07, nested docs indexing). The message and the merge logic were already in that commit's code, so the 2021 report and the pinned head share the same throw.

## Reproduction attempts

Not run in this pass. A reproduction needs a SolrCloud or standalone core with a nested block, an atomic update to one child, and then the same update after a commit and before a restart. The existing tests (NestedAtomicUpdateTest.java and NestedShardedAtomicUpdateTest.java) cover nested atomic updates but this record did not read whether they include the reporter's sequence (an update without a commit, or after a restart).

## Gap analysis

The code path is clear about the failure point and the caller. The missing piece is which of the two explanations applies: whether the realtime get returns the child before a commit, and whether the reporter's child documents carry the nested fields. Neither is settled by reading, and the ticket has no reply from the reporter since 2021-06.

## Verdict justification

keep-open: the attempts made are the code path reads above at the pinned head and the Jira read. The missing piece is a run of the reporter's sequence (nested block, atomic update to one child, update without commit, then again after a restart) on the pinned head, with the result, and a check of which explanation holds. Until that run, neither close nor PR is supported.

Missing for a closing or PR verdict: (1) the sequence run and its output; (2) the reporter's index shape (whether the child documents were indexed with the nested fields).

## Limits

This record does not reproduce the 500 error. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
