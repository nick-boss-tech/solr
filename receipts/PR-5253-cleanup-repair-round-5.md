# Receipt: SOLR-18601 / PR 5253 cleanup repair, round 5 (vm2, Linux): NOT GREEN (pre-existing neighbour failure)

Head: K `21804641d0ebbb5199a1c205f031af47c9424290` (commit "SOLR-18601: enable metrics for the migrate regression class", parent J `45dbc3ac0b`). Fork ref `solr-18601-submit` read back as K. Clone status clean at K for every positive leg.

## Results at K

- Tidy: rc 0, tracked and untracked changes 0, `git diff --check` clean.
- Focused (seed 5253C0DE; MigrateRouteKeyTest, MigrateCmdCleanupTest, SliceMutatorRoutingRuleTest): 18 run, 0 failed, 0 skipped.
- 5253BEEF (four MigrateRouteKeyTest methods): rc 0.
- 5253CAFE (same four methods): rc 0.
- Neighbours (seed 5253C0DE; CoreAdminOperationTest, CoreAdminHandlerTest, V2CoreAPIMappingTest, TestClusterStateMutator, TestRecovery.testBuffering, testBufferedMultipleCalls): rc 1. 61 tests, 1 failure: `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` ("The data directory was not cleaned up on unload after a failed core reload").
- Base control, same class and seed at `32b91612edde057c19bf1a7963e8276b816551df` (the commit before the PR, clean tree): rc 1, 8 tests, 1 failure, the same method. The failure is pre-existing and not caused by this PR.
- Root check (`:check -x test`): rc 0.
- `:solr:solr-ref-guide:checkSiteLinks`: rc 0.

## Negative legs (each must fail to show the new tests catch the fix)

- A at base `32b91612`, with the I test harness and a MigrateCmd mutation (patch-A): 3 of 3 fail (testFailedMultiShardMigratePreservesEarlierRange, testFailedMigratePreservesEarlierForwardedUpdates, testFailedMigrateRestoresExistingRule). Hashes in `negA-hashes.txt`.
- B at K, MigrateCmd mutation (patch-B2): 4 of 4 fail (MigrateCmdCleanupTest: testRemovalFailureKeepsTargetBuffering, testRemovalTimeoutKeepsTargetBuffering, testInterruptedRemovalKeepsTargetBuffering, testRemovalAssertionKeepsTargetBuffering). Hashes in `negB-hashes.txt`.
- C at H `98d1127e4f5d`, with the I MigrateRouteKeyTest only: 1 of 1 fails (testRetryAfterMigrateAssertionFailure). Hashes in `negC-hashes.txt`.

## Verdict

NOT GREEN under the strict rule (every neighbour test passes). The only failing neighbour test also fails at the base commit with the same seed, so the PR adds no new failure there. Whether a pre-existing failure in the neighbour set is acceptable is an owner decision. Recorded as a decision, not a waiver.

## Scope change for review (not a test result)

Commit `5cbffe2f24` removes the `REQUESTDROPBUFFEREDUPDATES` core admin action, its two tests in `CoreAdminOperationTest`, and the enum entry in SolrJ `CoreAdminParams`. The PR title does not mention this. The owner needs to decide whether the public action should be removed.

## Run notes

- The VM restarted at about 00:42 UTC during the root check. The root check was rerun from scratch at K (00:52 rc 0); nothing from the interrupted run is used.
- `steps.txt` has an earlier `FINAL` and `POSITIVEDONE` at 00:34:21 from an aborted resume attempt, where the `gr` helper was missing. They are not evidence. The valid `FINAL` is at 01:01:29 and the valid `POSITIVEDONE` follows it.
- Negative leg A was prepared once at 00:34 and killed before its result; its artifacts were overwritten by the run that produced the result above.

## Evidence

Under `repair-I/`: `steps.txt`, `tidy.log`, `focused.log`/`focused-xml/`, `beef.log`/`beef-xml/`, `cafe.log`/`cafe-xml/`, `neighbors.log`/`neighbors-xml/`, `ctl-base.log`/`ctl-base-xml/`, `root-check.log`, `site-links.log`, `negA.log`/`negA-xml/`, `negB.log`/`negB-xml/`, `negC.log`/`negC-xml/`, and the hash and patch records.

No GitHub, PR, or JIRA action was taken. No new head was pushed.
