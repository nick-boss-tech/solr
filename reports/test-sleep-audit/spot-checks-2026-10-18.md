# Test sleep audit: plan and spot checks (vm5, 2026-10-18)

Pinned head: apache/solr `main` `c8ab71430f`. Inventory: `inventory-2026-10-18.md`, `sleeps-apache-main.csv`.

## Summary

- Nine sleeps were read in context. They fall into four patterns, and only one of them is a direct latch conversion.
- The largest bucket (poll loops, 150 calls) polls state that the test cannot observe through any existing hook. A latch does not apply there without a production-side hook, which is a scope decision.
- One slice was implemented and checked: the sleep at `CollectionsRepairEventListenerTest.java:173`. It is removable, and the check is the final `waitForActiveCollection`.

## Patterns

| Pattern | What the sleep waits for | Conversion | Count in sample |
| --- | --- | --- | --- |
| A. Poll on state with no hook | ZK or Collection API state that the code under test changes asynchronously | Keep, unless a production countdown or watcher is added (scope widening) | 4 |
| B. Test-controlled sequencing | A step the test itself performs next | Latch counted down before the step (SOLR-16630 pattern) | 1 (converted in #5106) |
| C. Deliberate delay | A cache TTL, a timeout, or a fault-injection window | Keep with a reason, or inject a time source | 3 |
| D. Redundant with a later wait | Nothing the test needs, because a later wait covers it | Remove, verified by a run | 1 |

## Spot checks

| Location | Sleep | Pattern | Verdict |
| --- | --- | --- | --- |
| `CollectionPropsTest.java:165` | 10 ms in a loop reading collection properties from ZK | A | Keep. No collection-properties watcher or latch exists in SolrJ or core. |
| `DeleteStatusTest.java:51` | 1 s between `requestStatus` polls | A | Keep. `RequestStatus.waitFor` in SolrJ has the same sleep but deletes the id, so the test copies the loop. Changing SolrJ is production work. |
| `OverseerRolesTest.java:69` | 100 ms between overseer leader reads | A | Keep. |
| `TestStressLiveNodes.java:125` | 200 ms, up to 10 times, for watchers to finish | A | Keep unless the test counts watcher calls (not checked). |
| `ZkCollectionPropsCachingTest.java:74` | 10 s to pass the cache timeout | C | Keep, or inject a time source. |
| `ClusterStateProviderTest.java:513` | 2 s in `waitForCSPCacheTimeout` | C | Keep, or inject a time source. |
| `TestCloudSearcherWarming.java:364` | Sleep inside a `newSearcher` hook | C | Keep. The sleep widens the window the test checks. A latch would change the race. |
| `CollectionsRepairEventListenerTest.java:173` | `waitFor` seconds before awaiting `completed` | D | Removed (see below). |
| `TestCoordinatorRole.java` (#5106) | Ten sleeps | B | Converted. |

The sample is nine calls out of 374, chosen to cover each pattern. It is not a statistical sample.

## Implemented slice: CollectionsRepairEventListenerTest:173

### Rung 1: code and history

- `CollectionsRepairWrapperListener.onEvent` counts `completed` down right after `delegate.onEvent(event)`.
- `handleNodesDown` only records the lost node's timestamp. Repair runs on a fixed-rate scheduler every `waitFor` seconds (`CollectionsRepairEventListener.java:243`) and acts only on nodes lost for at least `waitFor` seconds (line 146).
- So the existing `completed.await(60s)` returns once the event is recorded. It does not wait for repair. The sleep was the only wait for the scheduled repair. The real completion check is the final `waitForActiveCollection(collection, 1, 3)`, which has a 30 s budget.

### Change (scratch branch, not pushed)

```diff
@@ -170,8 +170,6 @@
     eventsListener.waitForExpectedEvent(10);
     cluster.waitForActiveCollection(collection, 1, 2);
 
-    Thread.sleep(TimeUnit.MILLISECONDS.convert(waitFor, TimeUnit.SECONDS));
-
     // wait for completed processing in the repair listener
```

`TimeUnit` and `waitFor` are still used, so there is no unused import.

### Rung 2: probes

| Run | Seed | Result | Test time |
| --- | --- | --- | --- |
| Sleep removed | `FFC0988B10E05867` | pass, 1/1 | 35.4 s |
| Sleep removed | `1A2B3C4D5E6F7081` | pass, 1/1 | 18.5 s |
| Sleep removed, repair disabled (`if (true) return;` at the top of `runRepair`) | `FFC0988B10E05867` | fail at `CollectionsRepairEventListenerTest.java:178`, the final `waitForActiveCollection`: "Timeout waiting to see state for collection" | 38.6 s |

The third run shows the final wait is the real check: with repair disabled, the test fails. The scratch worktree was reverted after that probe.

### Risk to note in the PR

Without the sleep, the test waits for the first repair pass. That pass fires at most two `waitFor` periods after the loss, which is at most 18 s. The 30 s budget leaves about 12 s of margin, so a loaded host could come close. A PR could set an explicit timeout derived from the randomized `waitFor`.

### Not run yet

- `tidy`, `check -x test`, and `compileTestJava -Pvalidation.errorprone=true` on this change. These are needed before any PR.
- Other test classes in the same package.

Logs: `/workspace/gates/logs/vm5/sleep-spot/` (`nosleep`, `mutant-norepair`, `nosleep-seed2`).

## Cost of the work

- One class run takes about 40 s of test time. A first run also compiles solr-core, which took about 6 minutes on this host.
- Each file needs at least one run, and any run that changes shared test-framework code needs a broader one.

## Decisions for Nick

1. Scope: is the ticket limited to patterns B and D in `solr/core` cloud tests? Pattern A needs production hooks, which widen the scope. Pattern C needs a time source. Those two would be separate tickets.
2. Pattern A: is adding production countdown or watcher hooks in scope? This is a scope question for the owner.
