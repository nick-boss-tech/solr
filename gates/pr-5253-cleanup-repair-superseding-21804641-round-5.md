# Gate: SOLR-18601 cleanup repair, superseding round 5 at 21804641

Status: OPEN. Immutable once posted. Supersedes `gates/pr-5253-cleanup-repair-superseding-45dbc3a-round-4.md` for proof at the head below. Round 4's requirements apply unchanged, with K in place of J.

## Decision recorded before the run (decision ladder, `material/decision-ladder.md`)

Failure at J `45dbc3ac0b21785f9d0f41112ee6dea98ee55394`: the hook's gauge read threw `NullPointerException` because the cluster had no Prometheus readers.

- Rung 1 (code and history): `MigrateRouteKeyTest` does not set `metricsEnabled`. `TestTlogReplica`, `MigrateReplicasTest`, `ReplaceNodeTest`, `TestPullReplica` and `TestPullReplicaWithAuth` all set it in `@BeforeClass` and read the same gauge. Settled: the test class is missing the setup its neighbours use.
- Rung 2 (probe, scratch clone only, not gate evidence): the core's registry name was `solr.core.preserveUpdates-target.shard1.replica_n1`, and the cluster's meter-provider map was empty. The reader is null because no provider is registered, not because the name is wrong.
- Rung 3 (intent): the gate and the repair report name the existing `solr_core_update_log_buffered_ops` gauge as the operation-count oracle. The oracle is kept; only the test setup changes.
- Choice: add `System.setProperty("metricsEnabled", "true");` to `MigrateRouteKeyTest.setupCluster()`, as `MigrateReplicasTest` does. Rejected: a production accessor for the buffered count, because the metric already exists and the production surface should not widen for a test.

## Input

- Repair commit K `21804641d0ebbb5199a1c205f031af47c9424290` on `nick-boss-tech/solr:solr-18601-submit`, parent J. Author Nick Shanin `<nick.boss.us@gmail.com>`, subject `SOLR-18601: enable metrics for the migrate regression class`, no trailers. Plain fast-forward; live ref read back as K.
- Tidy at K: rc 0, the diff is exactly one inserted line.
- Positive head K; negative A overlays K's `TestInjection.java` and `MigrateRouteKeyTest.java` onto O; negative B mutates K; negative C overlays K's `MigrateRouteKeyTest.java` onto H.

Any further change requires another pushed head and superseding gate.
