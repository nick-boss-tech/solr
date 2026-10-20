# Skip verdict re-audit, batch 02 (deferred-own-host items)

Assignment: `assignments/pool-aab-deferred-reaudit-round-1.md`. Claim: `claims/pool-aab-deferred-reaudit-batch-02.md`. Auditor: vm2 (not the excluded host, vm3). Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC).

## Result

| Ticket | Recorded verdict (draft branch) | Re-audit | Missing floor items |
| --- | --- | --- | --- |
| SOLR-11378 | keep-open (solr-11378-draft-open, c34792305c) | suspect | closeout claim with DONE line; reproduction attempt; read of UpdateLog pre/post commit state |
| SOLR-11246 | close-fixed (solr-11246-draft-close, 54e402e5ff) | suspect | closeout claim with DONE line |
| SOLR-11009 | close-fixed (solr-11009-draft-close, 8ea024b4c7) | suspect | closeout claim with DONE line; reproduction attempt not required by close-fixed but the record says none was run |

Tally: pass 0, suspect 3, deferred 0. The three are added to `material/skip-audit/remediation-current.json` as open in the same push.

## SOLR-11378, keep-open

- JIRA (2026-10-20): Open, no resolution, updated 2022-02-22. Reported: an auto-commit in progress during a core unload leaves a TransactionLog referenced; found by an ObjectReleaseTracker failure in AutoscalingHistoryHandlerTest.
- Code read at the pinned head: `solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:865` calls `ulog.preCommit(cmd)`; `:905` calls `core.openNewSearcher`; `:910` calls `ulog.postCommit(cmd)` only after that. If `openNewSearcher` throws, `postCommit` is skipped on that path. This is a candidate mechanism. Whether it holds a tlog reference depends on the state `UpdateLog.preCommit` and `postCommit` keep, which this pass did not read.
- The record's code reading was empty ("missing"). The test it came from is gone and no reproduction was attempted.
- Verdict: keep-open stands, because the question is open. The record must add the code path above, the UpdateLog state read, and a reproduction attempt or the reason it is not possible.

## SOLR-11246, close-fixed

- JIRA (2026-10-20): Open, no resolution, updated 2022-03-24. Reported: MoveReplica does not preserve replica type, so a tlog or pull replica comes back as NRT after a move.
- Code read at the pinned head: `solr/core/src/java/org/apache/solr/cloud/api/collections/MoveReplicaCmd.java:247` and `:370` pass `ZkStateReader.REPLICA_TYPE` with `replica.getType().name()` into the create parameters.
- Fix commit: `86a40c1cd5` ("SOLR-13843: The MOVEREPLICA API ignores replica type and always adds 'nrt' replicas"). It is an ancestor of the pinned head and touches `MoveReplicaCmd.java`. Its subject describes the same symptom under SOLR-13843. No commit cites SOLR-11246, so the link is by symptom and code path. The record should say so.
- Verdict: close-fixed is supported by the code. The item stays suspect until a closeout claim with a DONE line exists and the record states the SOLR-13843 link.

## SOLR-11009, close-fixed

- JIRA (2026-10-20): Open, no resolution, updated 2019-06-08. Reported: FacetMerger.Context is null until the first shard response, and with shards.tolerant=true it may never be initialized when shards fail.
- Code read at the pinned head: `solr/core/src/java/org/apache/solr/search/facet/FacetModule.java:181-182` returns early when `facetState.mcontext == null`. `:308-311` initialize the merger and context only when a response is merged. The null check was introduced by `9c066f60f1` (SOLR-14482, a warnings cleanup; `git log -S` finds the check there).
- Verdict: close-fixed is supported by the code. The item stays suspect until a closeout claim with a DONE line exists. A reproduction is not required by the close-fixed floor, but the record should say none was run.

## Notes for the next host

- No skip closeout claim exists for any of the three tickets (`claims/` has no file naming them). The assignment floor requires one, so each item fails that floor regardless of its code reading.
- Redo of these items is a remediation claim, not a re-audit. Per the work-stores rule, a re-audit never takes a second redo.
