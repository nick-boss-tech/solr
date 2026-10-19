# Skip record: SOLR-15234

- Ticket: SOLR-15234, PRS has issues with many concurrent creations. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-03-09, updated 2021-04-12, no fix versions, no links. Three comments (2021-03-09): mdrob says the first error looks benign; ichattopadhyaya asks whether 8.8.1 has it; mdrob says it was seen on master.
- Verdict: keep-open.
- Draft branch: solr-15234-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited line grepped at this head (detached worktree).
- Source: remediation redo of batch 03, item 20, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (vm2, from the C reading) cited two lines; both are confirmed here, with one correction below.

## Ticket reading

Creating many 2x2 collections on one node, ten threads, with per-replica states (PRS) enabled. The reporter sees "Multi-op exception" from the Overseer state update (no collection name in the message) and "core not found" in the recovery preparation for a replica of a collection being created. The reporter asks for MDC logging so the collection can be identified.

## Grep and code reading at the pinned head

- `git grep -n "Multi-op exception" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` gives one match: solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/PerReplicaStatesOps.java:116, `log.error("Multi-op exception: {}", zkClient.getChildren(znode, null));`. The message is logged at ERROR, and it prints the children of the znode, not the collection name. The earlier record said the line logs "with no collection name or MDC"; that part is right, and the level is ERROR.
- `git grep -n "core not found" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` gives solr/core/src/java/org/apache/solr/handler/admin/PrepRecoveryOp.java:80 and :102, both `"core not found: " + cname`.
- `class PerReplicaStatesOps` is at PerReplicaStatesOps.java:39 and `class PrepRecoveryOp` at PrepRecoveryOp.java:47.

So both messages are still in the code, and the logging request (collection name in the Overseer message) is not implemented.

## History

- None cited in the C reading. No commit names SOLR-15234. The commit that added the multi-op logging was not identified in this pass.

## Reproduction attempts

Not run. A reproduction needs a cluster with per-replica states on, and ten concurrent 2x2 collection creates on one node, then the logs checked for both messages. This pass did not start a cluster.

## Gap analysis

The first message may be benign (mdrob's comment), and the "core not found" may be a race during creation. The ticket asks for logging and does not give a failing result, so there is no defect shown yet. The 8.8.1 question in the comments is not answered either.

## Verdict justification

keep-open: the attempts made are the code reads and greps above at the pinned head, the Jira read, and the comment check. The missing piece is a run of the concurrent creates on the pin with per-replica states, showing whether "core not found" leads to a failed create or a retry, and whether the Overseer message is benign. The logging request is a design choice (MDC or a collection name in the message) for maintainers. No close verdict is supported; no PR verdict is supported without a failure to fix.

Missing for a closing or PR verdict: (1) the concurrent-create run with the log output and the create results; (2) the 8.8.1 check the comments asked for.

## Limits

This record does not reproduce the errors. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
