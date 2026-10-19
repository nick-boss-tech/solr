# Skip record: SOLR-15297

- Ticket: SOLR-15297, Creating a collection with TLOG replicas fails with placement rules. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-03-26, updated 2021-04-12, no fix versions, linked to SOLR-10233 (Resolved), no comments.
- Verdict: close-obsolete.
- Draft branch: solr-15297-draft-close (the existing close draft; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree). Upstream sha not cited.
- Source: remediation redo of batch 03, item 17, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (vm2, from the B2 reading) cited a grep that was not re-run; this record re-runs it.

## Ticket reading

Creating a collection with `rule=replica:<2,node:*` and `tlogReplicas=2` fails with HTTP 500: "TLOG or PULL replica types not supported with placement rules or cluster policies". The reporter wants TLOG replicas to work with placement rules.

## Grep at the pinned head

- `git grep -n "not supported with placement rules" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0. The error text is not in any Java file at the pin.
- `git grep -n -i "placement" 498a09aea97aac86c2cc7ac114113b946d249942 -- 'solr/core/src/java/*' | grep -i "tlog\|pull\|replicatype\|replica type"` returns three lines, all in the new placement API: solr/core/src/java/org/apache/solr/cluster/placement/impl/PlacementPlanFactoryImpl.java:40 (builds a ReplicaPlacementImpl with a replica type), solr/core/src/java/org/apache/solr/cluster/placement/impl/ReplicaPlacementImpl.java:94 (passes the replica type through), and solr/core/src/java/org/apache/solr/cluster/placement/plugins/OrderedNodePlacementPlugin.java:940 (a comment on replica types with pending placements). None of them refuses TLOG or PULL replicas.

## Code reading

The refusal the ticket reports is absent. Replica types are carried through the placement plugin API at the pin (the three lines above), so the old check that produced the error is not present.

## History

- None cited in the B2 reading. The commit that removed the refusal was not identified; `git log -S "not supported with placement rules"` was not run in this pass.

## Reproduction attempts

Not attempted. A run would need a cluster with placement rules and a create with tlogReplicas. This pass did not start a cluster, so the TLOG-with-rules create is not shown to succeed on the pin.

## Gap analysis

close-obsolete rests on the removed refusal: the error text has no match at the pin and the placement path carries replica types. The ticket's wish (TLOG replicas work with rules) is not confirmed by a run on this head. If a run shows it works, the closer verdict would be close-fixed with the commit that lifted the restriction; that commit is not identified here.

## Verdict justification

close-obsolete: the code the ticket reports (the refusal of TLOG or PULL replicas with placement rules) has been removed, the grep at the pinned head returns 0 for its error text, and the replica types now pass through the placement API. The record does not claim the TLOG create with rules was run.

## Limits

This record does not reproduce the create. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
