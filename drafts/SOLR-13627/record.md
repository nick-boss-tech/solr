# Skip record: SOLR-13627

- Ticket: SOLR-13627, newly created collection can see all replicas go into recovery immediately on first document addition. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-07-13, updated 2019-07-26, no fix versions, no links; three comments, all from 2019-07-13 (hossman, with two commits that hardened DeleteReplicaTest).
- Verdict: keep-open.
- Draft branch: solr-13627-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Greps, commit checks and code reads run at this head (detached worktree).
- Source: remediation redo of batch 06, item 5, under assignments/pool-aaa-skip-verdict-remediation-06.md. The earlier record (vm2, from the C reading) cited the legacy-cloud removal as a possible cause; that is checked here.

## Ticket reading

Creating a 1x3 collection and adding documents made both non-leader replicas go into recovery straight away. The reporter saw it in a DeleteReplicaTest run, and the test itself was then hardened. The mechanism was not identified in the ticket.

## Grep and commit checks at the pinned head

- `git grep -n 'LEGACY_CLOUD\|legacyCloud' 498a09aea97aac86c2cc7ac114113b946d249942 -- 'solr/core/src/java/*' | wc -l` gives 0. The legacy-cloud mode is not in the core source. The removal commit 1ab9b811c6 (SOLR-12823, "remove /clusterstate.json") is an ancestor of the pin.
- The commit naming the ticket, 7ddba3b712 (2019-07-13, "Harden DeleteReplicaTest"), changes DeleteReplicaTest.java and makes a one-line visibility and annotation change in solr/core/src/java/org/apache/solr/cloud/ZkController.java (UnloadCoreOnDeletedWatcher becomes package-visible with `@lucene.internal`). It does not change recovery behavior, so it is not a fix for the reported recovery.

## Code reading

The recovery trigger on the first add (in ZkController or RecoveryStrategy) was not traced in this pass. The ticket's comments hold the logs that would name it, and they were not reproduced here.

## Reproduction attempts

Not run. A reproduction needs a 1x3 collection, a first document add, and the replica states after it, which this pass did not run.

## Gap analysis

The ticket's cause is not identified. The commit that names it is a test hardening with a visibility change, and the legacy-cloud removal that the earlier record suggested is not shown to be the mechanism.

## Verdict justification

keep-open: the attempts made are the greps and commit checks above at the pinned head, the Jira read, and the reading of 7ddba3b712. The missing piece is the mechanism: a run of the first-add scenario on a 1x3 collection, with the recovery trigger identified in the logs. close-fixed is not supported (the named commit is not a fix). close-obsolete is not supported (the recovery path is in use).

Missing for a closing or PR verdict: the recovery trigger and the run output.

## Limits

This record does not run the scenario. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
