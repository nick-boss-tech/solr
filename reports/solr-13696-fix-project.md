# SOLR-13696 fix project report

- Branch: `solr-13696-submit` on the fork. Starting head `05ab4664dace`. Base `upstream/main` at `4b58db1a42b`, merge-base `c3cdf7b46e8`.
- Assignment: `assignments/solr-13696-fix-project.md`. Claim: `claims/solr-13696-fix-project.md`.
- Status: in progress. Sections 1 and 2 were written before any change to the branch. Sections 3 to 6 are added after the repair.
- Read-only on the main side. Nothing built, run, or posted. No Gradle. The gate runs from the main side.
- Evidence tags: **verified** means seen in the code, the diff, the history or a record cited here. **hypothesis** means not run and not traced end to end.

## 1. Drift characterization

### Where the cluster tests get their schema

- The cloud tests create their configset from `_default` (`RoutedAliasUpdateProcessorTest.createConfigSet`, base `_default`). **Verified.**
- `_default` on the cluster comes from `ExternalPaths.DEFAULT_CONFIGSET`, which is `server/solr/configsets/_default/conf` (`solr/test-framework/.../util/ExternalPaths.java:50-51`). `SolrTestCase.beforeSolrTestCase` points `solr.configset.default.confdir` at it, and `ConfigSetService.bootstrapDefaultConf` uploads it. **Verified by reading.**
- The test-files `_default` (`solr/core/src/test-files/solr/configsets/_default`) is what single-core `initCore` tests use. The cluster does not load it. It defines only `id`, `_version_`, `*_bs`, and a `text_general` type. **Verified by reading.**
- `createConfigSet` sets `update.autoCreateFields` to false (the "no data driven" line, `RoutedAliasUpdateProcessorTest.java:135`). With that setting, a document field that is not in the schema is rejected. **Verified.**
- The routed-alias test classes define no fields of their own. Every field they send has to resolve in the server `_default` schema.

### Fields the re-enabled tests send

The re-enabled classes are `CategoryRoutedAliasUpdateProcessorTest` and `DimensionalRoutedAliasUpdateProcessorTest`. There are no other subclasses. `TimeRoutedAliasUpdateProcessorTest` keeps its own class-level `@AwaitsFix(SOLR-13059)` at line 75 on this branch. **Verified.**

| Field | Sent by | Server `_default` schema on `upstream/main` | Resolves |
|---|---|---|---|
| `id` | all three | field at line 114, `uniqueKey` at line 172 | yes |
| `integer_i` | base `inc` processor, Category and Dimensional `newDoc` | `*_i` pint, line 136 | yes |
| `ship_name_en` | Category, `categoryField` (line 53), `newDoc` (lines 499-509) | none. There is no `*_en` pattern. `*_txt_en` (line 345) does not match | **no** |
| `cat_s` | Dimensional, `catField` (line 63), `newDoc` (lines 627-640) | `*_s` string, line 138 | yes |
| `timestamp_dt` | Dimensional, `timeField` (line 62), `newDoc` | `*_dt` pdate, line 148 | yes |

**Verified by reading the schema and the test code.**

### The unresolved reference

- The only unresolved field is `ship_name_en`. Under `autoCreateFields=false`, every document in the Category tests fails with `unknown field 'ship_name_en'`. That matches the failure recorded on 2026-10-05.
- The field entered the test in SOLR-13131 (`d8f2a02fdb1`, 2019-03-13). That is the same commit that added `update.autoCreateFields=false` to the helper. The helper never added the field through the schema API.
- No `_default` configset in this repository has ever had a `*_en` dynamic field, on any branch (`git log --all -S'*_en"'` on `solr/server/solr/configsets/_default/` returns nothing). So the reference has never resolved. **Verified by history.**
- This repository does not show how the Category tests passed before SOLR-13696 added `@AwaitsFix` (`33e44b2fd69`). Either they did not pass, or they ran against something this history does not show. That is not established here. A run would settle it, and that run is main-side.

### What the characterization does not cover

- The other failures in the 2026-10-05 gate, if any, are not in the workspace, so they cannot be checked here. By reading, the only unresolved reference in the re-enabled classes is `ship_name_en`.
- The Dimensional failure recorded as `expected:<16> but was:<15>` is consistent with the commitWithin race, not with a schema error. Its fields all resolve.

## 2. Premise: does the commitWithin race still reproduce on current main?

Answer: the mechanism is still on main, the race has not been reproduced, and the repair does not depend on reproducing it.

- **Verified by reading (main):** `DirectUpdateHandler2.addDoc0` calls `commitTracker.addedDocument(cmd.commitWithin, ...)` (lines 489-494). Each core schedules its own commit on its own timer. Nothing in the add path makes the replicas of a shard commit together. So with `commitWithin=500`, the replicas of a shard can have their searchers open at different times.
- **Hypothesis:** the old poll reads through the alias, which sends each query to one replica per shard. A poll can therefore see all the documents on replicas that have already committed, while the next assertion lands on a replica whose searcher is not open yet. This is consistent with the 15-of-16 result and with the handoff's description. It was not run and not traced end to end.
- **Verified by reading (main):** the explicit commit that replaces it is a barrier. `SolrClient.commit(collection)` calls `commit(collection, true, true)` (`SolrClient.java:422-423`). The commit goes through `DistributedZkUpdateProcessor`, which calls `cmdDistrib.distribCommit` (lines 222 and 246). The comment at line 244 says that call blocks and retries until the replicas respond. With `waitSearcher=true`, each replica answers after its searcher is open. **Code trace, not run.**
- So the race is not moot on main, because the per-core commit timers are unchanged. The branch removes the path that can hit it. Whether the old helper fails in practice is still hypothesis.

## 3. Repairs

Not yet made. Added after the change.

## 4. Finding dispositions

Not yet written. Added after the change.

## 5. New head

Not yet made.

## 6. Verdict

Not yet given.
