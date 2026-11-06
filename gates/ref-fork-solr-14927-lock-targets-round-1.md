# Gate: distributed command locks for unresolved shard targets

Status: OPEN. Tags: gate, review, records. Intended host: capable Linux CPU
gate host with JDK 21 and this branch's pinned Gradle/dependencies.

- Source claim: `claims/ref-fork-porting-windows-source-round-1.md`.
- Fork: `nick-boss-tech/solr`, branch `solr-14927-distributed-lock-targets`.
- Exact head: `6ab0486cc20b691ed4544bf19770e5e954e427b8`.
- Original production/base: `c0c219e374611e0edc475c0f793c9bd688b13292`.
- Fixed seed: `14927C0FFEE`.
- Receipt: `receipts/ref-fork-solr-14927-lock-targets-round-1.md`.

Owner requested scoped port branches that advance Overseer removal. This
patch promotes a distributed SHARD lock with no shard name to a COLLECTION
lock. It adds assertions for SPLITSHARD and DELETEREPLICA in both ordering
directions, independent collections, release/progress and nested ADDREPLICA.
It does not change legacy LockTree, named-shard concurrency or other resource
footprints. No production runtime failure or speed measurement has been run.

Claim this gate separately, serialize under the host lock, fetch and verify
the exact fork ref, and use full clean checkouts. Record host/toolchain,
checkout paths, all commands, exit codes, fresh logs and JUnit XML. No skips
or missing tests satisfy a named leg.

## Required legs

1. Head: `./gradlew tidy`; require a clean tree. Then
   `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
2. Head focused:
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.api.collections.CollectionApiLockingTest --tests org.apache.solr.cloud.ZkDistributedLockTest -Ptests.seed=14927C0FFEE`.
3. Executable fail-before: in an isolated checkout of the original base,
   copy only the head's CollectionApiLockingTest.java to its identical source
   path. Production remains byte-identical to the base. Run
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.api.collections.CollectionApiLockingTest.monothreadedApiLockTests -Ptests.seed=14927C0FFEE`.
   Require the new assertion that an unspecified shard conflicts with a real
   shard to fail at runtime. Preserve the test-only diff, log, XML and exact
   assertion. Compilation/startup failures do not qualify.
4. Head: `./gradlew :solr:core:check -x test -Pvalidation.errorprone=true`,
   then `./gradlew check -x test -Pvalidation.errorprone=true`.

## Review and repair

Check that named shards still acquire independently and nested commands use
the promoted parent lock without deadlock. This is a correctness change with
a deliberate concurrency tradeoff for unresolved targets. End-to-end shard
split races, aliases, node-wide operations and performance are not covered.

Investigate any executed failure before retrying. Record environment refusals
separately. Any source/formatting repair requires a committed and verified
fork head plus a superseding exact-head gate before it can be green. Do not
reuse older XML or call a compile failure fail-before proof. No PR or Jira
posting is authorized by this job. Finish with the named receipt, including
all proof legs and remaining limitations.
