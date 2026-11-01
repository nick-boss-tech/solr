# SOLR-16793 implementation, round 1

- Branch: `solr-16793-submit` on the fork (nick-boss-tech/solr).
- Head: `747afc8153f703b778c5ed4848b36bc611d0e5d5`, tree `4d96b23f26fa743576959f15964733da42e738fe`.
- Base: `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`.
- Author: Nick Shanin, no trailers. Not posted to Jira; no PR opened.

## Mechanism, confirmed in the code at base

`Overseer.start` creates one `Stats` (`Overseer.java`, the `stats = new Stats()` line in `start`) and passes it to two queues:

- the state update queue, through `ClusterStateUpdater` (`ClusterStateUpdater`'s constructor calls `getStateUpdateQueue(zkClient, zkStats)`), exposed as gauge `solr_overseer_state_update_queue_size`;
- the collection work queue, through `OverseerCollectionConfigSetProcessor`'s public constructor (`overseer.getCollectionQueue(zkStateReader.getZkClient(), stats)`), exposed as gauge `solr_overseer_collection_work_queue_size` in `OverseerTaskProcessor`.

`ZkDistributedQueue.fetchZkChildren` calls `stats.setQueueLength(...)` on whichever queue fetched last, so both gauges read the last writer's value.

## Fix

`OverseerCollectionConfigSetProcessor.java` line 53: the collection work queue is built with `overseer.getCollectionQueue(zkStateReader.getZkClient())`, which already creates its own `Stats`. Op counters (`stats.success` / `stats.error` in `OverseerTaskProcessor`) still use the shared instance, so OVERSEERSTATUS collection counts are unchanged.

## Test

`solr/core/src/test/org/apache/solr/cloud/OverseerQueueLengthTest.java`, extends `SolrTestCase`, one test `testStateUpdateAndCollectionQueuesReportTheirOwnLength`. It builds the state update queue with the shared `Stats` (as `ClusterStateUpdater` does), constructs the processor through its public constructor with a mocked Overseer whose collection queue is real, fetches a state queue with 1 child and a collection queue with 3 children, and asserts 1 and 3.

## Results

| Leg | Head | Result | Log |
|---|---|---|---|
| Test against base, first attempt | base + new test | not a bug result: mock Overseer returned null `SolrCloudManager`, constructor NPE (test setup, fixed in test) | `gates/logs/vm5/solr-16793-impl-round-1/prefix-test.log` |
| Test against base, final test | base + final test | FAIL `expected:<1> but was:<3>` at the state queue assertion (the bug) | `prefix-test-3.log` |
| Test with fix, final test | `747afc8153` (before tidy) | PASS, 1 test, 0 failures | `postfix-test-3.log` |
| `:solr:core:tidy` | final tree | BUILD SUCCESSFUL | `tidy.log` |
| Test with fix, final tree | `747afc8153` | PASS, 1 test, 0 failures (JUnit XML `tests="1" failures="0"`) | `final-test.log` |

The test is randomized; each run used its own seed, printed in its log's reproduce line. Gradle ran `./gradlew :solr:core:test --tests org.apache.solr.cloud.OverseerQueueLengthTest` under `flock /workspace/tools/.gradle-vm5.lock`.

## Notes

- Two runs did not finish because of host problems, not test results: a run was killed when the session ended, and a post-fix run could not clear its test-results directory because an orphaned Gradle daemon from this worktree held a file open. That daemon was stopped; the directory was cleared; the run was repeated.
- Error Prone (`-Pvalidation.errorprone=true`) and the module `check` were not run; they belong to phase 2 per the assignment.
- Changelog entry added: `changelog/unreleased/SOLR-16793-overseer-queue-length.yml` (type fixed). Author line uses the committer name; adjust if the owner wants the reporter credited.
