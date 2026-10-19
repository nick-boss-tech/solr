# SOLR-1542 closeout record

Verdict: keep-open (draft branch `solr-1542-draft-open`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, no fix versions (removed 2012-06-14). Created 2009-11-04, updated 2012-06-14. Description read in full. Five comments read in full: four are bulk fix-version edits, and one (2012) removes the fix version for lack of attention.
- Audit verdict on record: `confirmed-skip` (audit-3, 2026-10-06): IndexFetcher sets the replication commit point after a fetch, so a repeater with replicateAfter=optimize serves the fetched commit.

## Code paths read at the pinned head

- `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java:1352-1354`: `replicateOnOptimize` is set only when `replicateAfter` contains optimize and not commit.
- `ReplicationHandler.java:1376`: the optimize callback is registered with `getCommit = replicateOnOptimize`. On a repeater, the commit callback at `:1381` runs with `getCommit = replicateOnCommit`, which is false for optimize-only. So the repeater's commit callback does not set the commit point.
- `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:1049`: after a fetch, `replicationHandler.indexCommitPoint = commitPoint` is set directly. This is the path that lets the repeater serve the fetched commit, and it matches the audit.
- `solr/core/src/test/org/apache/solr/handler/TestReplicationHandler.java`: the replicateAfter tests cover startup, write-to-follower and core reload. None covers a repeater with replicateAfter=optimize.

## Attempts

- Read the mechanism at the cited line and at the callback registration. The audit's reading holds on the code path.
- Searched the test tree for replicateAfter with optimize: no test for the optimize-only repeater case.
- No test run. A reproduction needs a new integration test (leader optimizes, repeater with replicateAfter=optimize pulls and serves the optimized commit). Writing it is source work, which is out of scope for this closeout.

## What the verdict does not cover

- Whether the repeater serves the optimized commit end to end on current main. Not run.
- Whether the repeater also needs a postOptimize-triggered fetch, which the ticket asks for. Not decided.

## What discharges keep-open

An integration test on the replication path for the optimize-only repeater case. If it passes on main, close-cannot-reproduce or close-fixed (with the commit that added the path) applies. If it fails, the ticket becomes a pr-needed candidate.
