# solr-16437-submit

- Branch: origin/solr-16437-submit
- Head: 673ae584de03 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base e2cdb2d7e8ae, 35 commits behind, 4 commits ahead)
- Scope: 4 commits, 4 files. `CollApiCmds.java` (`AddReplicaPropCmd.call`: before queueing the async state update, resolves collection, shard and replica from the cluster-state snapshot and throws `BAD_REQUEST` when the replica is absent), `CollectionsAPISolrJTest.java` (one new test, `testAddReplicaPropRejectsUnknownReplica`), changelog `SOLR-16437-addreplicaprop-validate-inputs.yml` (`type: fixed`), and `SOLR-16437-TESTING.md` (author handoff, says nothing was run).
- Verdict: Nearly (unchanged from the bulk verdict). The check is correct: it runs before any state is queued, it compiles against the base API, and it does not read a stale cache. The test asserts the 400 only for the replica case. The shard and collection cases reach the same branch but are not pinned.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-16437-review.md` (Nearly) reviewed this head (`673ae584de0`). No delta.

- Bulk F1 (P2, assert the error code for the unknown-shard and unknown-collection cases): **confirmed.** `CollectionsAPISolrJTest.java` asserts `400` only for the unknown replica (`expectThrows` with `e.code()`), and only `expectThrows(RemoteSolrException.class, …)` for the other two. The code path is the same `replica == null` branch for all three, so the fix is a test assertion, not a behavior change.
- Bulk F2 (P2, remove `SOLR-16437-TESTING.md` from the ticket diff): **changed to a ship-time item, not a branch defect.** The round-36 handoff says to leave TESTING.md in place and that the Linux side removes it at ship time, after gating. No action on this branch.
- Bulk concurrency note (validation is a best-effort check against a snapshot taken just before the async command is queued; a concurrent delete after validation remains possible): **confirmed as an owner note.** See owner call 1.

## Findings (ranked)

1. **LOW, verified. Shard and collection not-found cases are not pinned.** The three inputs (unknown collection, unknown shard, unknown replica) all fail the same check: `docCollection == null`, `slice == null`, or `replica == null` (`CollApiCmds.java`, the new block in `AddReplicaPropCmd.call`), and all throw `BAD_REQUEST`. The test asserts 400 only for the replica case. Add `assertEquals(400, e.code())` to the other two `expectThrows` calls (bulk F1). This matches the changelog's stated 400 for each.

2. **LOW, verified (checked, no issue). The lookup is live, not cached.** `ccc.getZkStateReader().getClusterState().getCollectionOrNull(collectionName)` resolves through `ClusterState.getCollectionOrNull(name)`, which is `allowCached=false` and so calls `CollectionRef.get(false)` (`solr/solrj/src/java/org/apache/solr/common/cloud/ClusterState.java:128-146`). The method exists with that signature, and the base already calls it from `CreateAliasCmd`. So a freshly created collection is not reported as absent because of a stale cache. Read, not run.

3. **LOW, verified (checked, no issue). Nothing is queued on the error path.** The new check runs before `cloneZkPropsWithOperation` and the state-update call, so a 400 leaves the cluster state untouched.

4. **LOW, verified (checked, no issue). `checkRequired` runs first.** The three names come from `checkRequired(message, COLLECTION_PROP, SHARD_ID_PROP, REPLICA_PROP, …)` at the top of `call`, so `message.getStr(...)` cannot return null for them.

5. **LOW, hypothesis. The 400 reaches the caller only if `call` runs on the request thread.** The comment in the diff says the state update is applied asynchronously and its failures are only logged, which implies the check itself runs synchronously in the handler. That is consistent with the test's `e.code()` assertion, but the dispatch path was not traced and the test was not run.

6. **Ship-time item, not a branch defect.** `SOLR-16437-TESTING.md` is in the diff (bulk F2). Per the round-36 handoff it stays until ship time.

## Owner calls (not decided here)

1. **Is a snapshot check acceptable as the contract?** The check is best-effort. A collection, shard, or replica deleted between the check and the async state update still produces a logged failure and an HTTP 200, as before. That is a race the typo fix does not claim to close. Whether the owner accepts the snapshot check as the contract, or wants the state mutator to report the miss to the caller, is a design call. This review does not decide it.

## Proposed fixes (not applied; the owner decides)

- Finding 1: add `assertEquals(400, e.code())` to the unknown-shard and unknown-collection `expectThrows` blocks in `testAddReplicaPropRejectsUnknownReplica`. Keep the test name or rename it to cover all three inputs.
- Owner call 1: no code change proposed. If the owner wants the race closed, that is a larger change in the state mutator and is out of this ticket's scope.

## Not checked

- Nothing compiled, formatted, or run.
- The dispatch path that decides whether `AddReplicaPropCmd.call` runs on the request thread (finding 5).
- The changelog's wording that the 400 covers "the collection, shard or replica" was checked against the three branches of the code, not against a run.
- The JIRA packet was not re-read here.
