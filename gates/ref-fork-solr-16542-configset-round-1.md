# Gate: ConfigSet migration with deterministic before/after proof

Status: OPEN. Tags: gate, review, records. Intended host: capable Linux CPU
gate host with JDK 21 and this branch's pinned Gradle/dependencies.

- Source claim: `claims/ref-fork-porting-windows-source-round-1.md`.
- Ticket: SOLR-16542; original assignment
  `assignments/solr-16542-overseer-configset-handler-round-1.md`.
- Fork: `nick-boss-tech/solr`, branch `solr-16542-configset-port`.
- Exact head: `e08978bcabe469775dd56640c453408b902bd034`.
- Inherited implementation: `85ce6f5475a259f61f786edcbf0058852a9f5db2`
  on `solr-16542-submit`, preserved as an ancestor and not rewritten/moved.
- Original production/base: `f489a30a31b1b97c7c8aa7a2c05c15d80114dc70`.
- Fixed seed: `16542C0FFEE16542`.
- Receipt: `receipts/ref-fork-solr-16542-configset-round-1.md`.

The inherited implementation removes OverseerConfigSetMessageHandler, always
constructs the distributed runner and routes ConfigSets through it while
preserving explicit Collection API mode selection. Its prior phase-1 report
is `reports/solr-16542-implementation-round-1.md`; its compile/focused results
cover the old head only and are not this gate.

This follow-up pins the new test to `.withOverseer(true)` and uses an existing
updater accessor in its mode assertion. The old test randomized the mode and
used the newly added isOverseerEnabled API, which prevented an executable
negative leg against the original production code. No production changes
were added by the follow-up. Windows ran no Gradle/compiler/test.

Claim separately, serialize under the host lock, verify the fork head, use
full clean checkouts and record toolchain, commands, exit codes and fresh
logs/XML. Record failures/errors/skips; missing or skipped suites do not pass.

## Required legs

1. Head: `./gradlew tidy`, require a clean tree; then
   `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
2. Head focused:
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.ConfigSetAlwaysDistributedTest --tests org.apache.solr.cloud.TestConfigSetsAPI --tests org.apache.solr.cloud.TestConfigSetsAPIExclusivity --tests org.apache.solr.cloud.ConfigSetApiLockingTest --tests org.apache.solr.cloud.OverseerCollectionConfigSetProcessorTest --tests org.apache.solr.cloud.TestRebalanceLeaders -Ptests.seed=16542C0FFEE16542`.
   Confirm the exact suite paths/class names before running. The new suite
   must run in Overseer-enabled Collection API mode with a present distributed
   ConfigSet runner. Assess both collection modes in neighboring tests; if
   random selection does not exercise both, rerun the affected ConfigSet API
   suites with `SOLR_CLOUD_OVERSEER_ENABLED=true` and `false`, same seed, and
   record actual cluster properties. These two forced mode runs are authorized
   proof legs when needed, not repeated retries of a failure.
3. Executable fail-before: in a clean checkout of the original production base,
   copy only the fixed head's ConfigSetAlwaysDistributedTest.java. Run
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.ConfigSetAlwaysDistributedTest -Ptests.seed=16542C0FFEE16542`.
   Require compilation success and runtime failure at the assertion that the
   distributed runner is present while the Overseer is enabled. Record the
   test-only diff, fresh log/XML and exact assertion. Missing APIs and startup
   failures do not qualify.
4. Head: `./gradlew :solr:core:check -x test -Pvalidation.errorprone=true`,
   then `./gradlew check -x test -Pvalidation.errorprone=true`.

## Compatibility and repair

Review all runner-presence checks, collection async tracking and shutdown:
the runner is now present for ConfigSets even with the Overseer enabled, so
presence no longer determines Collection API mode. Review the discussion in
SOLR-16542 about legacy queue contents, in-flight requests and upgrades. Do
not promise transparent rolling mode transitions or lossless queued-work
migration. State the compatibility limit and any unresolved release decision.

Do not compose the SOLR-17946 patch into this gate. Investigate executed
failures before retrying, separate environment refusals, and require a pushed
verified repair head plus superseding gate after source/formatting changes.
No public PR/Jira writes are authorized. The named receipt must distinguish
inherited old-head evidence from the complete new-head proof.
