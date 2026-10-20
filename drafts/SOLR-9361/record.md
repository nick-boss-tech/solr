# Skip record: SOLR-9361

- Ticket: SOLR-9361, "Concept of replica state being down is confusing and misleading (especially w/ DELETEREPLICA)". JIRA status read 2026-10-20T14:58:13Z (UTC): Open, resolution None, updated 2019-06-08. Linked: SOLR-11427 (Resolved).
- Verdict: keep-open (kept; re-read at the pinned head). The state model is a design question.
- Draft branch: solr-9361-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:58:13Z (UTC).

## Ticket reading

The "down" replica state is used for cleanly shut down replicas, while the Admin UI shows replicas on vanished nodes as "Gone". DELETEREPLICA with `onlyIfDown=true` works only for a replica in state DOWN, so a replica on a crashed node cannot be removed that way. The 2016 comments say "Gone" means no live node in ZooKeeper and "down" should mean stale state or a connection being recovered.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteReplicaCmd.java:273-287`: with `onlyIfDown=true`, the command throws BAD_REQUEST unless `replica.getState() == DOWN`. A replica whose stored state is not DOWN (for example a stale ACTIVE after a crash) is refused, which is the behavior the ticket describes.
- The option is declared at `CollectionHandlingUtils.java:100` and passed through `solr/core/src/java/org/apache/solr/handler/admin/api/DeleteReplica.java:74`.
- The Admin UI and CLUSTERSTATUS state labels were not re-read in this pass.

## History

- evidence.py at the pinned head: no commits naming SOLR-9361. SOLR-11427 (Resolved) is linked by JIRA; not located here.

## Reproduction attempts

Not run. A run would start a three-node cluster, kill a node without a clean shutdown, and call DELETEREPLICA with onlyIfDown=true.

## Gap analysis

The check is the one the ticket describes. Changing it or the state labels changes the meaning of "down" for every client, which is a design decision for the maintainers.

## Verdict justification

keep-open meets the rubric for an open design question: attempts made are the code-path reading of DeleteReplicaCmd.java:273-287; the missing piece is the design decision on the state model and the crash run. close-fixed is not supported without a named commit; close-obsolete is not supported since the check is present.

## Limits

This record does not run the crash case. It does not read the Admin UI.
