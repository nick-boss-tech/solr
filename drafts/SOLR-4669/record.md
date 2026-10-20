# Skip record: SOLR-4669

- Ticket: SOLR-4669, "conf file replication can cause new index to be loaded before new core (with new configs) is loaded". JIRA status read 2026-10-20T15:20:35Z (UTC): Open, resolution None, updated 2018-01-10. Linked: SOLR-4629 (Closed).
- Verdict: keep-open (kept; re-read at the pinned head). The window the ticket describes is present in the install order, and the fix changes replication ordering, which is a design decision.
- Draft branch: solr-4669-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T15:20:35Z (UTC).

## Ticket reading

When replication sees both changed index files and changed configuration, the index is installed and used with the old configuration until the core reloads with the new configuration. A 2014 comment says the window is small in practice.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:691-696`: when conf files changed, `reloadCore = true` and the conf files are downloaded.
- Lines 696-726: the index is installed next (`modifyIndexProps` or `moveIndexFiles`), and the log line "Configuration files are modified, core will be reloaded" follows.
- Lines 740-741: `reloadCore()` runs after the index is installed and the index writer is reopened. So the window in the ticket is in the code order.

## History

- evidence.py at the pinned head: no commits naming SOLR-4669.

## Reproduction attempts

Not run. A run would change an index and a config on a leader, and sample the follower's searches during the install to see which config answers.

## Gap analysis

The order is index first, reload second. Changing it (install configs before the index, or reload before the index install) is a replication ordering change, with effects on the reload path that were not traced. The window's effect is the open question.

## Verdict justification

keep-open meets the rubric: attempts made are the reading of the install order (IndexFetcher.java:691-741); the missing pieces are the window run and the design decision on ordering. close-fixed and close-obsolete are not supported: the order is present. pr-needed is not chosen because the ordering change is a design decision.

## Limits

This record does not measure the window. It does not trace the reload effects.
