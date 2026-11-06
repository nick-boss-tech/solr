# Gate: unused Overseer task queues in distributed mode

Status: OPEN. Tags: gate, review, records. Intended host: capable Linux CPU
gate host with JDK 21 and this branch's pinned Gradle/dependencies.

- Source claim: `claims/ref-fork-porting-windows-source-round-1.md`.
- Ticket: SOLR-17946; related assignment
  `assignments/solr-17946-overseer-disabled-queues-round-1.md`.
- Fork: `nick-boss-tech/solr`, branch `solr-17946-submit`.
- Exact head: `dab14772d9151e90d3c69dc714370f18bc2439b0`.
- Original production/base: `c0c219e374611e0edc475c0f793c9bd688b13292`.
- Fixed seed: `17946C0FFEE`.
- Receipt: `receipts/ref-fork-solr-17946-disabled-queues-round-1.md`.

This owner-directed Windows source phase implemented the separable patch;
the existing assignment's Linux proof is now this exact-head job. No Windows
Gradle/compiler/test ran. No existing paths are deleted. In distributed mode,
five legacy task queue/map paths should remain absent, but `/overseer/queue`
and `/overseer_elect` remain for QUIT and cluster-singleton leadership.

The patch gates eager map/queue creation and collection-processor construction.
It retains the updater for QUIT, uses its own tenure ID for exit cleanup, and
runs disabled-mode prioritization without the collection queue. Preferred-node
requests use the existing executor because startup must remain asynchronous.
No default, role default or rolling-upgrade behavior is changed.

Claim separately, serialize under the host lock, verify the live fork head,
and use full clean checkouts. Capture exact commands, toolchain, exit codes,
fresh logs/XML, tests/failures/errors/skips and actual coordination modes.

## Required legs

1. Head: `./gradlew tidy`, clean tree, then
   `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
2. Head focused:
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.OverseerDisabledQueuesTest --tests org.apache.solr.cloud.OverseerRolesTest --tests org.apache.solr.cloud.ZkControllerTest --tests org.apache.solr.cloud.OverseerTest -Ptests.seed=17946C0FFEE`.
   The new suite must execute both tests. Its mode test covers enabled and
   disabled startup, Collection/ConfigSet create/delete, QUIT reelection and
   restart. Its second test covers preferred-node takeover in distributed
   mode with no task queue.
3. Retained real singleton consumer in forced distributed mode:
   `SOLR_CLOUD_OVERSEER_ENABLED=false ./gradlew :solr:core:test --tests org.apache.solr.cluster.events.ClusterEventProducerTest -Ptests.seed=17946C0FFEE`.
   SolrCloudTestCase reads this environment setting when building its cluster.
   Confirm `overseerEnabled=false` in the actual run, and confirm the suite's
   lifecycle/failover tests execute. A setting alone is not mode evidence.
4. Executable fail-before: at the original base copy only
   `solr/core/src/test/org/apache/solr/cloud/OverseerDisabledQueuesTest.java`
   from the fixed head; production stays unchanged. Run
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.OverseerDisabledQueuesTest.testTaskPathsFollowOverseerMode -Ptests.seed=17946C0FFEE`.
   Require the disabled-mode task-path assertion to fail on an actually
   present legacy path. Keep the test-only diff, fresh log/XML and exact
   assertion. Compilation or startup errors do not qualify.
5. Head: `./gradlew :solr:core:check -x test -Pvalidation.errorprone=true`,
   then `./gradlew check -x test -Pvalidation.errorprone=true`.

## Review and repair

Audit all consumers of optional legacy queues/maps and the preferred-node
startup path. Review interrupted shutdown, leadership loss, singleton start/
stop and retained QUIT consumption. The ConfigSet removal branch is separate:
do not compose it into this job or infer joint compatibility from this gate.

Investigate executed failures before retrying; distinguish environment refusal
from runtime assertions. A source/formatting repair needs a new committed,
verified fork head and superseding gate. The named receipt must state the
exact tested head and proof limits. No public PR/Jira writes are authorized.
