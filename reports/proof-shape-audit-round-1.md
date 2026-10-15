# Proof-shape audit, round 1

Assignment: `assignments/pool-proof-shape-audit-round-1.md` (claim `claims/pool-proof-shape-audit-round-1.md`, under `claims/coordinator-review-rounds.md`). Review lane, Windows host. No build, test, Gradle, or GitHub write was run here. Every gate is a job file for a Linux host. The job files are written here and not yet posted; the coordinator posts or commits them.

Status: PARTIAL. Twenty-two tickets audited (the twenty listed, plus SOLR-8430 and SOLR-18391 from the receipts sweep for INCONCLUSIVE). Every ticket has a decided shape, except the four marked REMAINING in the table, which need one more reading before a job can be fully specified. Two items need the owner.

Sweep: `git grep -i INCONCLUSIVE` over `receipts/` on origin/pr-prepare returned 23 files: the 20 listed tickets, SOLR-14187 (precedent, not audited), and SOLR-8430 and SOLR-18391 (added here). SOLR-6831 and SOLR-6975 are settled (2026-10-15) and were not reopened.

Method, for each ticket: current receipt and gate records read; live branch head confirmed by `git ls-remote origin` for the branches a job names; changed files compared with the merge-base (`git diff --name-status`); the new-API symbols each inconclusive test uses checked against the merge-base by `git grep` or `git show`; the proof shape chosen by the decision default (a recorded decision wins; otherwise the harder shape where a vehicle is possible; otherwise a named plain record).

Shapes used: (A) base-leg run with the branch-added files removed from the base tree only (SOLR-18178 construction); (B) base-leg run with a scratch vehicle that uses base API or reflection (SOLR-14187 precedent); (C) plain record, the third outcome, with the draft wording saying so; (D) revert-at-head leg (the SOLR-6975 settlement type), accepted only where the reverted code is the base behavior on the tested path.

## Audit table

| Ticket | Proof state now (verified) | Decided shape | Applied or posted |
| --- | --- | --- | --- |
| SOLR-10641 | Inconclusive: the two pins pass on base (receipt: 9 run, 0 failures, fresh XML). The head change makes setData and delete one ZK multi, with a fallback to the two separate calls on failure (`OverseerTaskQueue.java`, head). The fallback is the base behavior, so no state check separates head from base. | C. The premise is one round trip, which a state test cannot see. A count-based check (a spy on the multi call) is a named follow-up, not needed for the PR. | No edit. The draft Proof already says the pins pass on base (`pr-drafts/solrcloud/SOLR-10641.md`). |
| SOLR-11475 | Inconclusive: the base run of the branch's test ends in the endless loop and OutOfMemoryError (receipt line 8; the log is not on pr-prepare). Head tree `3223028c6f4e` equals the gated tree (verified). Base line 835 throws IndexOutOfBoundsException for the pair as the only entry. | B. A base vehicle with the pair as the only entry (clean IndexOutOfBoundsException expected), and the endless-loop test removed from the base copy only. | Written `gates/SOLR-11475-proof-shape-round-1.md`. The draft's Proof wording (held for the owner, proposed text in the job) also fixes "never ends" and the citation to base. |
| SOLR-12998 (PROPOSAL) | Inconclusive by compile at the merge-base. `SyncStrategyTest.java` is branch-added and does not compile. `CoreAdminOperationTest.java` (modified) uses only base API (checked: CoreAdminHandler(CoreContainer), CallInfo ctor, isCoreLoading, doRecovery), so it should compile at base, and base fails its two loading-path tests on content. The 2026-10-10 "no run owed" ruling is superseded only if the main side ratifies this. | A. Delete `SyncStrategyTest.java` from the base tree; run `CoreAdminOperationTest` at base. `SyncStrategyTest` stays inconclusive by compile. | Written `gates/SOLR-12998-proof-shape-round-1.md` marked PROPOSED. Main side ratifies or rejects. |
| SOLR-15331 | Inconclusive by construction: the test calls `BucketBasedJsonFacet.getMissingCount()`, an accessor base does not have. The premise ("missing" silently ignored) has no base-API observable. | C. An accessor addition. A base probe can only show the accessor is absent, which is not a behavior failure. | No edit. The draft Proof already says inconclusive (`pr-drafts/search-components/SOLR-15331.md`). |
| SOLR-15357 | Inconclusive by compile (`CopyFieldSubFieldsTest.java` is branch-added; two of four methods call `FieldType.getSubFields`). The two `/get` methods were never run at base; "would not fail on base" is a reading. | B. A base-compilable copy with the two API methods and their helpers removed. Outcome decides pin or fail-before. | Written `gates/SOLR-15357-proof-shape-round-1.md`. Draft Proof cites head `d717899b8736`; the operative head is `e9e7cc3a0c1c` (receipt). Correction held for the run result. |
| SOLR-15386 | Inconclusive by construction: the head test uses `NodeMutator.ONLY_IF_NODE_NOT_LIVE`, absent at base. The constant is the string `"onlyIfNodeNotLive"`. | B. The same test with the string in place of the constant. Expected: 1 failure at base, `testConditionalDownNodeSkippedWhenNodeIsLiveAgain`. | Written `gates/SOLR-15386-proof-shape-round-1.md`. Draft wording held for the run. |
| SOLR-15674 | Inconclusive by construction, carried over unchanged to head `75ddf9ba518a` (the scrub changed no code). The test calls `IndexSchemaFactory.getFromCache` and `VersionedConfig`, both new. | REMAINING. The premise (a znode deleted and recreated with the same data version is a stale cache hit at base) needs a ZK-backed vehicle. Next step: specify it as a gate job; the vehicle is a SolrCloud-style test that deletes and recreates `schema.xml` and reloads. | No job this round. The draft's Proof already says inconclusive (`pr-drafts/solrcloud/SOLR-15674.md`). |
| SOLR-15863 | Inconclusive by compile. `BackupCmdTest.java` is branch-added and calls `BackupCmd.minIndexVersion`, absent at base. `BackupCoreAPITest.java` reads `response.indexVersion`, also absent at base. The earlier premise log at `8f5e94cf314` is not on pr-prepare. | REMAINING. A base leg does not separate the segment version from LATEST when the fixture is written by the same Lucene version. A fixture written by an older Lucene is needed. Next step: find or specify the fixture, then post a job of shape A. | No job this round. The draft Proof is already plain (`pr-drafts/solrcloud/SOLR-15863.md`). |
| SOLR-15895 | Inconclusive by compile (`validateStoredResourceId` absent). The premise is Windows-only: the Linux branch of the test checks that colon and backslash ids still work, and base accepts them, so a Linux base run cannot fail on the bug. | C for the Linux half. The Windows half needs a Windows base run, which the standing rule excludes (no builds on the Windows host). | Draft Proof edited in the pool worktree: the "cannot show a failure" sentences now name the Windows-only premise (`pr-drafts/search-components/SOLR-15895.md`). Owner decision pending (below). |
| SOLR-16108 | Inconclusive by compile (`SplitOp.getHashHistogramFromRouteField` is new). Base's `SplitOp.getHashHistogram` is the same histogram, truncating each term at the first separator (base, inside the term loop), so it can probe the route-field premise. | B. The helper calls base `getHashHistogram` with the route field name; one test removed from the base copy. | Written `gates/SOLR-16108-proof-shape-round-1.md`. No draft for this branch is in pr-drafts. |
| SOLR-17433 | Inconclusive by construction for the floor test, which calls the new two-argument `SolrClientCache(int, int)`. The request-timeout test uses base API. Base `HttpSolrClient.Builder` copies the seed's request timeout (base lines 470 to 471). | B. Base copy with the floor test removed; expected failure on the request timeout. | Written `gates/SOLR-17433-proof-shape-round-1.md`. Draft wording held for the run. |
| SOLR-17539 | Partly settled by receipt: shipped `TestConfigOverlay` fails 1 of 2 on base on its new assertion (base compiles it). `TestDocValuesIteratorCache` and `SolrCoreTest` are compile-inconclusive at base. A reflective probe fails 2 of 2 on base (receipt). | C with the base result cited. The `TestConfigOverlay` base failure is the fail-before for its assertion. The two compile-inconclusive classes keep the probe as their scratch evidence. | No edit. The probe and the base failure logs are on vm2 only (not on pr-prepare), so they are cited from the receipt and not re-read here. |
| SOLR-17987 | Inconclusive by construction: the test calls the new three-argument `SolrMetricManager` constructor, and base has no disabled-registry option. | C. The premise is an option that base does not have; no base run can show behavior. | No edit. The draft Proof already says inconclusive (`pr-drafts/metrics/SOLR-17987.md`). |
| SOLR-18277 | RETIRED by owner ruling 2026-10-13; PR #4959 merged 2026-10-05. Fail-before inconclusive by construction; fork runs show no fail-before (receipt). | C, final. No proof is owed for a retired ticket. | No edit. Nothing to draft. |
| SOLR-18341 | Inconclusive by construction: `isRetriable()` is new. `SolrRequestRetriableTest.java` is branch-added. Four modified classes also use new API in places (`WrappedSolrRequestTest`, and the new tests in `CloudSolrClientCacheTest`, `LBAsyncSolrClientTest`, `LBSolrClientRetryUnsentTest`). | REMAINING. Next step: compile-check the three modified classes at base after deleting `SolrRequestRetriableTest.java` and the `WrappedSolrRequestTest` new method. If they compile, post a shape-A job: `CloudSolrClientCacheTest` (update replayed after a 503 RouteException) should fail at base; `LBAsyncSolrClientTest` (unsafe update retried when never sent) is expected to pass at base, a pin. | No job this round. The draft Proof already says inconclusive (`pr-drafts/solrj/SOLR-18341.md`). |
| SOLR-1877 | Inconclusive by compile: `close()` is new. The reload assertions use base API (`reader` is protected, `reload(SolrCore, SolrIndexSearcher)` exists at base). | B. The head test with the two close statements removed. Expected: the reload assertion fails at base (reader not closed). | Written `gates/SOLR-1877-proof-shape-round-1.md`. Draft wording held for the run. |
| SOLR-3684 | Inconclusive by construction: `withMaxThreads` and `getConfiguredMaxThreads` are new. The default-value pin passes at base by design. | C. A knob addition; the default pin is base behavior. | No edit. The draft Proof already says inconclusive (`pr-drafts/build-docs/SOLR-3684.md`). |
| SOLR-7550 | Receipt at `687165651f9` says inconclusive by construction. The fork head moved to `5f4570b20ad`. Gate job `gates/SOLR-7550.md` is OPEN at that head and unclaimed; its proof leg reverts the one new branch at head. Base counts a 500 from a versions request as a failure (base `PeerSync.java` 379 to 410 handle only connect and socket exceptions, 503, and 404), so the revert restores base behavior on the tested path. | D. Accept the open job's revert leg as the proof shape. No new job. | No edit. The job's receipt section will refresh the record. |
| SOLR-9155 | Inconclusive by compile: `ZkController.getLeader(CloudDescriptor, int)` is private at the merge-base (line 1675). The live tip `29bf373828` changed the log line only; the premise leg was not re-run. | B. The head test with the private method called by reflection. Expected: the interrupt surfaces as a SolrException, not an InterruptedException (base generic catch). | Written `gates/SOLR-9155-proof-shape-round-1.md`. Draft wording held for the run. |
| SOLR-11678 | Superseded: the live-tip receipt (`2c68a83c64`) has a base-leg content failure from fresh XML (`EnvUtilsTest` 13 run, 1 failure, `sslKeyManagerPasswordMapsToNothing`, `expected:<> but was:<null>`, at merge-base `cabedd1d968`). The earlier compile state is gone. | Resolved. The proof stands on the base-leg content failure. No run owed. | No edit. The draft Proof is consistent with the receipt. The base log is on vm2, not on pr-prepare. |
| SOLR-18391 (sweep) | Receipt: `CreateCollectionCleanupTest` has two base failures (message assertion; stale view 500 where head returns 400). Both are recorded and accepted on 2026-10-10 (receipt lines 21 and 25). `CreateCollectionCmdRetryTest` is branch-added and calls `CreateCollectionCmd.runWithBoundedRetries`, absent at base. | C for the retry test. A base copy of the retry logic would test the copy, not the code. The cleanup test is resolved. | No edit. The receipt already carries the second failure. The live PR #4997 body was not read in this lane. |
| SOLR-8430 (sweep) | Cap half: content failure at the pre-cap head `49af21be58` (`expected not same`, line 52). Branch leg at the merge-base: compile failure (`rateLimiterFor` is new). Sharing half: base builds one limiter per `DirectoryFileStream` (base `ReplicationAPIBase.java` 308 to 313, private field). | REMAINING for the sharing half. The probe needs an instance of the inner class, which needs its outer `ReplicationAPIBase` instance; the construction must be read on Linux before a job can be specified. The cap half stands as recorded. | No job this round. The draft's Proof is consistent (`pr-drafts/replication-backup/SOLR-8430.md`). |

## Decisions taken (recorded here, per the decision default)

1. SOLR-11475: a pair-only vehicle, not the endless-loop input, is the base fail-before; the endless-loop test is removed from the base copy only, and the receipt keeps its OOM record.
2. SOLR-12998: proposed shape A; the prior "no run owed" ruling is superseded only on ratification (the CoreAdminOperation half compiles at base by reading).
3. SOLR-15386, SOLR-1877, SOLR-9155, SOLR-16108, SOLR-17433, SOLR-15357: base vehicles with the new-API or private-access substitution stated in each job. Where a vehicle cannot compile, the job says INCONCLUSIVE by compile and stops.
4. SOLR-7550: accept the open job's revert leg (shape D). Justified by the base rule for 500 (above).
5. SOLR-10641, SOLR-15331, SOLR-3684, SOLR-17987: plain record (shape C). Reason in each row: the premise is an API or round-trip change with no base-observable state.
6. SOLR-15895: Linux half plain record; Windows half is an owner decision.
7. SOLR-17539: the TestConfigOverlay base failure is the fail-before for its assertion; the two compile-inconclusive classes keep the reflective probe as scratch evidence, as recorded.
8. SOLR-18277: no proof owed (retired). SOLR-11678: resolved.
9. SOLR-18391: no receipt edit needed; the second base failure is already recorded and accepted.

## Gate jobs written (pool worktree, `gates/`, not yet posted or committed)

- `gates/SOLR-11475-proof-shape-round-1.md`
- `gates/SOLR-15386-proof-shape-round-1.md`
- `gates/SOLR-9155-proof-shape-round-1.md`
- `gates/SOLR-1877-proof-shape-round-1.md`
- `gates/SOLR-17433-proof-shape-round-1.md`
- `gates/SOLR-12998-proof-shape-round-1.md` (PROPOSED)
- `gates/SOLR-15357-proof-shape-round-1.md`
- `gates/SOLR-16108-proof-shape-round-1.md`

All eight are OPEN, intended for vm2 or any capable Linux host, and none is claimed.

## Files written by this round (pool worktree; the coordinator commits)

- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\reports\proof-shape-audit-round-1.md` (this report)
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\gates\SOLR-11475-proof-shape-round-1.md`
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\gates\SOLR-15386-proof-shape-round-1.md`
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\gates\SOLR-9155-proof-shape-round-1.md`
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\gates\SOLR-1877-proof-shape-round-1.md`
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\gates\SOLR-17433-proof-shape-round-1.md`
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\gates\SOLR-12998-proof-shape-round-1.md`
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\gates\SOLR-15357-proof-shape-round-1.md`
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\gates\SOLR-16108-proof-shape-round-1.md`
- `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-windows\pr-drafts\search-components\SOLR-15895.md` (Proof, two sentences edited)

No receipt was edited in this round. No comment, PR body, JIRA item, mail, or fork push was made.

## Pending approval (owner or main side)

1. Ratify or reject the SOLR-12998 proposal (shape A).
2. Authorize or decline a Windows base-leg run for SOLR-15895 (its only fail-before is Windows-only). Standing rule: no builds on the Windows host.
3. Public draft wording, held until each job's run result: SOLR-11475 (also owed: the Finding 6 test comment decision and the re-gate question in `reports/update-11475-cleaned-head.md`), SOLR-15386, SOLR-9155, SOLR-1877, SOLR-17433, SOLR-15357 (head citation `e9e7cc3a0c1c`).
4. Copy the SOLR-17539 `TestConfigOverlay` base log and the reflective probe log from vm2 onto pr-prepare, or accept the receipt citation as is.
5. Confirm the SOLR-7550 open gate job goes ahead at head `5f4570b20ad` (the receipt at `687165651f9` is the prior head).
6. Name the fixture for SOLR-15863 (an older-Lucene segment), or accept the plain record.

## Remaining, exactly (this round is PARTIAL)

- SOLR-15674: ZK-backed vehicle not yet specified (gate job owed).
- SOLR-15863: older-Lucene fixture not yet identified (gate job owed).
- SOLR-18341: base compile of three modified classes not yet checked (gate job owed if they compile).
- SOLR-8430: sharing-half vehicle needs the inner-class construction read on Linux (gate job owed).

## Not verified here

- Logs and JUnit XML on vm2 or in the main workspace: SOLR-11475 OOM log, SOLR-17539 probes and `TestConfigOverlay` run, SOLR-15863 `8f5e94cf314` log, SOLR-18391 reruns, SOLR-11678 base log. Cited from receipts only.
- Live GitHub PR bodies and comments (not read in this lane; only fork heads were checked with `git ls-remote`).
- The base compile of the vehicles. Each job's compile step is the check, and a compile failure is recorded as INCONCLUSIVE by compile in the job, not relabeled.
- The `DocCollection.create` and `ClusterState` signatures used by the SOLR-15386 vehicle were read at the merge-base (`create` has seven parameters, matching the head call; `ClusterState(Set, Map)` exists); no compile was run.
