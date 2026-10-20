# Skip record: SOLR-12781

- Ticket: SOLR-12781, OverseerCollectionConfigSetProcessor reports "node with improper name: qnr-0000000002".
- Verdict: keep-open.
- Draft branch: solr-12781-draft-open (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 4 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-09-18, updated 2019-06-08, no fix versions, no links, no comments. The report: a debug log line "Found child node with improper name: qnr-0000000002" appears in a Jenkins test log, from the Overseer collection config-set processor. The reporter's reading: the Overseer task queue's response nodes (named `qnr-`) are read by a queue that expects a different prefix.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/cloud/ZkDistributedQueue.java:387-393`: while reading the queue's children, a child whose name does not start with the queue prefix is logged at debug ("Found child node with improper name: {}") and skipped with `continue`.
- `solr/core/src/java/org/apache/solr/cloud/OverseerTaskQueue.java:46-51`: `OverseerTaskQueue extends ZkDistributedQueue` with `RESPONSE_PREFIX = "qnr-"`; the class does not override the child-name check, so response nodes that share the directory reach the debug line.
- `OverseerTaskQueue.java:258-263` (`createResponseNode`): response nodes are created with the `qnr-` prefix under the queue directory, which is why the message fires for them.
- The message is at debug level and the node is skipped, so the read is correct; the log line is noise for response nodes that are not tasks.

## History

- No commit at the pin names SOLR-12781 (the evidence tool lists none).
- The history of the child-name check was not traced in this pass.

## Reproduction attempts

Not run. This pass ran no builds or tests. The reporter's log comes from a debug-level run of the Overseer config-set test; a run at the pin with debug logging for `ZkDistributedQueue` would show the same line for each response node.

## Gap analysis

- The log line fires as the code describes; the read is skipped correctly, so the ticket's symptom is a noisy debug message, not a wrong result.
- Whether the response nodes should be filtered before the debug line (a logging change) or left as is, is a small maintainer choice; the ticket records no harm beyond the log.
- A fix would be a logging or filtering change, not a correctness fix.

## Verdict justification

keep-open: the code path is confirmed and it behaves as the ticket describes, but the ticket reports no wrong result, the behavior is a debug message, and whether to silence it is a maintainer choice. close-fixed and close-obsolete do not fit (the message is still emitted); pr-needed would be a logging change that the record cannot justify as a defect.

Missing for this verdict: a maintainer decision on whether the response-node case should be filtered without the debug line, or the ticket should close as expected debug output.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
