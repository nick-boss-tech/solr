# Skip record: SOLR-12850

- Ticket: SOLR-12850, SOLR Unaware When Index Has Corrupt Checksum.
- Verdict: keep-open.
- Draft branch: solr-12850-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 25, the last item of the batch (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-10-11, updated 2019-06-08, no fix versions, no links, no comments. The report: a node whose index has a corrupt checksum still reports as healthy in the cluster status, while every write routed to it fails silently. The reporter asks that the node report as Down, which would then lead to a recovery trigger.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:256`: `replicasMetTragicEvent` is declared as a map from replica key to `Throwable`.
- `ZkController.java:1989`: the only use of that map in the tree is `replicasMetTragicEvent.remove(collection + ":" + coreNodeName)`. No code path adds an entry to it, so the map is never populated at the pin.
- `ZkController.java:904-942` (the leader give-up path): a leader that meets a tragic exception gives up its leadership; this does not set a replica state to DOWN.
- `solr/core/src/java/org/apache/solr/core/CoreContainer.java:1810-1821`: a `CorruptIndexException` during core creation is handled by a recovery attempt on the core load, not by publishing a DOWN state for the replica.
- So a corrupt index on a running replica does not, at the pin, lead to a DOWN replica state or a recovery trigger; the ticket's observed behavior is present.

## History

- No commit at the pin names SOLR-12850 (the evidence tool lists none).
- The `replicasMetTragicEvent` map has no population site; its history was not traced in this pass.

## Reproduction attempts

Not run. This pass ran no builds or tests. A run would corrupt a segment file of a replica (for example by flipping bytes in a `.cfs` or `.si` file on one node) and then check the replica's state in the cluster status and whether writes to that node fail.

## Gap analysis

- The reading confirms the ticket's gap: no DOWN publication or recovery trigger follows a corrupt checksum at the pin.
- The ticket asks for a behavior change (a DOWN state and a recovery trigger). Whether that is the right fix, and which corruption errors should count, is a maintainer design decision the ticket does not settle.
- The unused `replicasMetTragicEvent` map is a finding in its own right: a half-built path that never fills.

## Verdict justification

keep-open: the gap is confirmed by reading, but the fix is a design change (which errors mark a replica DOWN, and the recovery trigger) and the behavior has not been run. close-fixed and close-obsolete do not fit (no fix; the path is present). pr-needed needs the design decision and a corruption run.

Missing for this verdict: a maintainer decision on the DOWN state and the trigger, and a run that corrupts a replica's segment and records the published state and write behavior at the pin.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
