# Skip record: SOLR-17765

- Ticket: SOLR-17765, "Nodes should publish themselves as DOWN ASAP during shutdown". JIRA status Open, resolution none, created and last updated 2025-05-20, checked 2026-10-19 UTC. Links: SOLR-17744 (closed), SOLR-15590 (closed).
- Verdict: keep-open.
- Draft branch: solr-17765-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

An improvement idea, not a bug. It asks that `CoreContainerProvider.close()` publish the node as DOWN before anything else during shutdown, by uncommenting a block that calls `zkController.publishNodeAsDown`. The block was commented out in SOLR-15590 (2021). The description notes that Mark Miller suggested the change and that it "causes test failures" in its current form.

Comments (three, all 2025-05-20):
- hossman: uncommenting the block and running the tests with several seeds surfaced one failure, in `ZkFailoverTest` for some seeds, which he thought was related to PRS randomization.
- markrmiller: there is no reason to log it. Replicas are considered down because the live node disappears, and the only publish-down that matters for correctness happens on startup.
- hossman: a follow-up comment showing a change that adds a SolrException catch-and-ignore around the publish call.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java:112-126`: `close()` still has the suggestion as a comment, with the `publishNodeAsDown` call commented out at line 126.
- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:2986`: `publishNodeAsDown(String nodeName)` exists and is used on other paths (lines 694, 826, 1203). So the method the ticket wants is available.
- The commented block is from `5b6d9dbb34` (2021-11-29, SOLR-15590, "Start CoreContainer with Context Listener (#416)"), found by `git log -S"Mark Miller suggested"`.

## History

- `5b6d9dbb34` (2021-11-29), SOLR-15590: the source of the comment. Ancestor of the pinned head: yes.
- SOLR-17744 (closed, linked from the ticket): not checked on the fork; the ticket does not depend on it for its question.
- No commit on the fork names SOLR-17765 (grep on `git log --grep` at the pinned head is empty).

## Reproduction attempts

Not attempted by vm5. The only attempt in the record is the one in the comments: uncommenting the block with several seeds, which failed in `ZkFailoverTest` for some seeds. That attempt is not a reproduction of a bug; it is a test-side effect of a proposed change. No failing seed or output is recorded in the ticket, so this closeout does not rerun it.

## Gap analysis

The question is whether publishing DOWN first is worth the change, and whether the `ZkFailoverTest` failure is a real behavior change or a test assumption. Markrmiller's comment says live-node loss already marks replicas down, so the change would add a clearer signal, not a correctness fix. The uncertainty is about the `ZkFailoverTest` failure: its cause (a test that counts states, or a real race) is not recorded.

## Verdict justification

keep-open: the record names the attempts (code reads at the pinned head, the history commit, the comment-level probe) and the missing piece: the cause of the `ZkFailoverTest` failure, and a maintainer decision on whether the improvement is wanted. None of the rubric's close verdicts fits: it is not fixed, not obsolete, not a support question, and not a reproduced bug. The keep-open minimum is met.

## Limits

This record does not run `ZkFailoverTest` or any other test. It does not check SOLR-17744 in detail. It does not establish whether the change is wanted.
