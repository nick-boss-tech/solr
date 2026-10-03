# SOLR-17363 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## Round 4 review changes

See `research/branch-reviews/round-4/SOLR-17363-review.md`.

- The "which failed cores still count" decision is extracted into the
  package-private static `SolrConfigHandler.failedCoresStillActive(...)` so it
  can be unit-tested; behaviour is unchanged (a failed core absent from a fresh
  `getActiveReplicas` read is still excused).
- Added `TestConfigWaitForReplicas` (not compiled or run) for that helper, and
  `changelog/unreleased/SOLR-17363.yml`. The helper runs for every overlay
  command and for `/config/params`, not only `set-user-property`.
- Not changed (needs a decision): the helper excuses every replica that is not
  `ACTIVE` (including `DOWN`/`RECOVERING` ones that still exist), not only
  deleted ones; the request still waits the full 30 s because the deleted
  replica's task is not stopped early; and the ticket's replica log (`CLOSING
  SolrCore` 30 s before the failure) is also what a reload logs, so the reporter's
  failure may be cause (2), which this patch does not address.
- A real mid-wait replica deletion is not tested (racy; needs a cluster).

## What the patch does

`SolrConfigHandler` `set-user-property` waits for every replica from a
point-in-time cluster-state read to report the new property-overlay version
(`waitForAllReplicasState`, 30s hard timeout). If a replica was deleted (or
otherwise left the active set) while the wait ran, its `PerReplicaCallable`
could never succeed — the whole config request then failed with SERVER_ERROR.

Fix: when tallying per-replica results after the wait, a failed replica is
checked against a fresh `getActiveReplicas()` read (done lazily, once, only
if there is at least one failure). Replicas no longer in the active set are
logged and skipped instead of failing the request. Genuinely live replicas
that never picked up the version still fail the request exactly as before.

This targets case 1 from the ticket (deleted/closing replica race). Case 2
(per-node reload serialization under the 30s timeout) is a throughput/tuning
matter and is intentionally untouched.

File changed:
- `solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java`
  (`waitForAllReplicasState`, failure-tally loop)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. Config request where a replica disappears from cluster state mid-wait →
   request succeeds instead of throwing SERVER_ERROR. (May need to mock
   `ZkController`/`ZkStateReader` or drive `getActiveReplicas` with a
   mutated `ClusterState`.)
2. Negative control: a live replica that never picks up the version still
   triggers the SERVER_ERROR within the timeout.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Matching is by `replica.getCoreUrl()` against a fresh active-set read; a
  replica deleted and re-added under the same core URL is still required to
  report (correct — it is active again).
- A replica whose node died mid-wait is also excused (it is not in the
  active set); on restart it reads the current overlay version from ZK.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
