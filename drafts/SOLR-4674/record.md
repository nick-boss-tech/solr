# Skip record: SOLR-4674

- Ticket: SOLR-4674, "repeaters must be configured to replicate on startup in order to work properly when replicating config files". JIRA status read 2026-10-20T15:14:25Z (UTC): Open, resolution None, updated 2018-01-10.
- Verdict: keep-open (kept; re-read at the pinned head). The requirement is a replication configuration design question.
- Draft branch: solr-4674-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T15:14:25Z (UTC).

## Ticket reading

On a repeater (a node that is both a slave and a master), a commit of the leader reaches the repeater, but the repeater's last replicable commit point is set correctly on core reload only if the repeater replicates on startup. The 2013 report says the workaround is to enable replicate-on-startup on the repeater. No comments.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java:241` declares `replicateOnStart`, and line 1384 sets it when the configured `replicateAfter` includes `startup`.
- The commit-point update on install and on reload is the behavior the ticket describes; the reload branch was not traced line by line in this pass.
- `git grep -n -i repeater` under the index replication guide: no match in `solr/solr-ref-guide/modules/deployment-guide/pages/index-replication.adoc`, so the repeater requirement is not documented there.

## History

- evidence.py at the pinned head: no commits naming SOLR-4674.

## Reproduction attempts

Not run. A run would build a master, a repeater, and a slave with replicate-on-commit only on the repeater, reload the repeater's core, and check the slave's version.

## Gap analysis

The startup flag is the workaround the ticket names, and the code keeps it. Whether the repeater needs it for correct behavior (the ticket's claim) or the reload path already sets the commit point is the open question, and the run decides it.

## Verdict justification

keep-open meets the rubric: attempts made are the flag reading and the docs check; the missing piece is the repeater reload run. close-answered is not supported: the docs do not cover repeaters. close-fixed and close-obsolete are not supported.

## Limits

This record does not run the repeater case. It does not trace the reload branch.
