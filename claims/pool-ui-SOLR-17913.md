# Claim: pool-ui-SOLR-17913

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, one ticket per claim.
- Ticket type: display card with a domain model (the component type appears two claims back, SOLR-17909). The immediately previous claim was a screen (SOLR-17850), so owner complaint 5 is met. SOLR-17914, the dialog-type candidate, has no fork branch and would be a new implementation, so it is not the choice this round.
- Slice: SOLR-17913 (collection details card), gate only. The implementation is on fork branch `solr-17913-collection-details-card` at `0a04d2239e`, based on `c8ab71430f`: `CollectionDetails.kt` (domain), `CollectionDetailsCard.kt`, `CollectionDetailsCardTest.kt`, and a draft PR description. This claim verifies the branch's tests and spotless on a fresh checkout. No code change, no push to the branch.
- Capability tags used: ui, jvm-tests, review.
- Started: 2026-10-24T09:09:05Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17913@0a04d2239e.
- Links: Ticket: SOLR-17913 (parent SOLR-17912). Category: ui. Siblings: `claims/pool-ui-SOLR-17850.md`, `claims/pool-ui-SOLR-17909.md`. The collections report (`reports/new-ui-collections-configsets-round-1.md`) calls this a review task, not new code. Supersedes: none.
- Decision ladder: rung 1 for the model and card code. GL: to be read from the card code in the Result.
- Constraints: no JIRA or GitHub post, no PR. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-24T09:09:05Z

Run: job `pool-ui-17913-0a04d2239e-091339`, log dir `/workspace/gates/logs/vm6/pool-ui-17913/`, head `0a04d2239e` (fork branch, not pushed by this claim), started 2026-10-24T09:13:48Z (UTC). Steps: spotless, then `:shared:jvmTest --tests org.apache.solr.ui.views.collections.CollectionDetailsCardTest`.

Heartbeat: 2026-10-24T09:13:48Z

Result: 2026-10-24T09:19:35Z: PASS at head 0a04d2239e (job `pool-ui-17913-0a04d2239e-091339`). spotless rc=0 (BUILD SUCCESSFUL in 25s); `:shared:jvmTest --tests org.apache.solr.ui.views.collections.CollectionDetailsCardTest` rc=0 (BUILD SUCCESSFUL in 1m 13s). Tests 2, failures 0, errors 0, skipped 0 (testShowsAutoAddReplicasState, testShowsCollectionDetails). XML `ui/shared/build/test-results/jvmTest/TEST-org.apache.solr.ui.views.collections.CollectionDetailsCardTest.xml` in worktree `/workspace/worktrees/pool-vm6-17913`. Log dir `/workspace/gates/logs/vm6/pool-ui-17913/`. Conclusive for the software-renderer JVM tests. GL: grep for Canvas, DrawScope, drawWithCache, graphicsLayer, drawBehind, drawWithContent and shader in the collections views and CollectionDetails.kt at 0a04d2239e finds none, so not gl-blast-radius. Fork branch not pushed by this claim.

DONE: 2026-10-24T09:19:35Z. Deliverables: this claim only.
