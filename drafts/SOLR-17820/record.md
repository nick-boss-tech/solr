# Skip record: SOLR-17820

- Ticket: SOLR-17820, Shard and Collection health is GREEN with down replicas. JIRA status as recorded in the B3 reading: Resolved (Fixed), 2026-10-14, 5. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-17820-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Shard and Collection health is GREEN with down replicas. B3 row type: skip note: not reproducible. Skip note from the B3 reading: The skip note checks only the state=down case. Replicas on dead nodes still count as up before the cross-check, so GREEN persists at the pinned head. JIRA marked Fixed on 2026-10-14; the fix is not on the pinned head.

## Code reading

solr/core/src/java/org/apache/solr/handler/admin/ClusterStatus.java:329 (health computed), :375 (liveness cross-check runs after health), :290-292 (dead-node replicas set DOWN only there); the fix commit 7e3b175021 is not an ancestor of the pinned head

## History

None cited as an ancestor in the B3 reading.

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The B3 reading gives the missing piece; see the code reading and notes.

## Verdict justification

keep-open: the code path is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head, or the missing piece named in the B3 reading. B3 ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
