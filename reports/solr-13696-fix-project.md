# SOLR-13696 fix project report

- Branch: `solr-13696-submit` on the fork. Starting head `05ab4664dace`. Base `upstream/main` at `4b58db1a42b`, merge-base `c3cdf7b46e8`.
- Assignment: `assignments/solr-13696-fix-project.md`. Claim: `claims/solr-13696-fix-project.md`.
- Status: repaired and pushed. Sections 1 and 2 were written before any change to the branch. Sections 3 to 6 describe the repair. New head `1deddc51958`.
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

The repair is one commit on the branch, `1deddc51958`, on top of `05ab4664dac`. The branch's earlier change, `32dbb7a4bb2`, is kept as it was: it removes commitWithin from `addDocsAndCommit`, removes `@AwaitsFix` from the base class, and commits every alias collection explicitly.

1. **Drift (test code).** `CategoryRoutedAliasUpdateProcessorTest.java:53`: `categoryField` changes from `ship_name_en` to `ship_name_s`. The server `_default` defines `*_s` (string). Routing uses the raw field value, so the field type does not change which collections are created.
   - Rejected: defining `ship_name_en` through the schema API in `createConfigSet`. That needs a text field type and more code, and the test does no text analysis.
   - Rejected: turning `update.autoCreateFields` back on. That changes what the helper tests, and the "no data driven" setting was deliberate in SOLR-13131.
2. **Finding 3 guard (test code).** `RoutedAliasUpdateProcessorTest.java`: `assertNotNull("alias ... is not listed", aliasCollections)` before the commit loop. The comment above the loop now states a fact about commits, not the change.

Checks done by reading, since nothing was built:
- `queryNumDocs` removal: the only other caller is `TimeRoutedAliasUpdateProcessorTest`, which defines its own private method, so nothing breaks.
- `AwaitsFix` and `Collectors` removal: no remaining use in the base. `assertNotNull`, `SolrServerException`, `IOException`, `ExecutorUtil` and `SolrNamedThreadFactory` are still used.
- `CollectionAdminRequest.ListAliases().process(...).getAliasesAsLists()` exists on main with the used signature.
- No line over 100 columns is added. Two long lines in the touched files were already on `upstream/main` and are outside the diff.
- Formatting is not verified. The gate's spotless apply covers it.

## 4. Finding dispositions

| Finding | Disposition | Evidence class |
|---|---|---|
| 1, scope of re-enabling | Confirmed as intended. Exactly two subclasses run again: Category and Dimensional. The base's `@AwaitsFix` is gone and no annotation remains in their hierarchy. `TimeRoutedAliasUpdateProcessorTest` keeps its own class-level `@AwaitsFix(SOLR-13059)` at line 75, so it stays disabled. | Verified by reading |
| 2, commitWithin coverage | **Dropped. A decision for the owner, not a side effect.** The re-enabled tests have no commitWithin exercise left. The old commitWithin check was a poll-until-visible loop, and the race is in that loop. A deterministic version would assert the per-core commit timer, which tests commitWithin, not routed aliases. The TESTING note records that a commenter on the ticket (Gus Heck) called that part orthogonal to routed aliases. Recommendation: keep it dropped. To restore coverage, add a separate commitWithin test that waits per collection with a timeout. Not decided here. | Recommendation, owner call |
| 3, possible NPE | **Real, not reachable in these tests, fixed.** `CollectionAdminResponse.getAliasesAsLists()` (`CollectionAdminResponse.java:72-74`) returns the alias map, and `get(alias)` returns null when the alias is absent. The old loop dereferenced that null. Every caller creates the alias before adding documents, so the normal flow never hits it. The guard turns a missing alias into a clear assertion. The same pattern in the `!aliasOnly` branch is unreachable, because nothing calls `addDocsAndCommit(false, ...)`. It is left unchanged. | Verified by reading |
| 4, no fail-before | Partly changed. The drift repair has a deterministic fail-before: with `ship_name_en`, every Category add is rejected under `autoCreateFields=false`. The commitWithin race stays intermittent, so a revert shows it only under beasting. | Verified by reading (drift); hypothesis (race) |

Items from the TESTING note's "What was guessed" list:
- `@AwaitsFix` inheritance: no longer matters for Category and Dimensional, since the base annotation is removed. Time keeps its own class-level annotation, which does not depend on inheritance.
- `ListAliases` and `aliasOnly`: the request has no `aliasOnly` option. `ListAliases()` returns every alias, and the alias is present when it exists. The server-side listing was traced only to the SolrJ getter.
- Subclass stability: unknown. Only the gate can show it. Dimensional should be beasted, since its failure is the race.
- Spotless and unused imports: `Collectors` is removed. Nothing else became unused by reading. Formatting is left to the gate.

## 5. New head

- `solr-13696-submit` is at `1deddc51958`, one commit on `05ab4664dac`. Pushed fast-forward. The fork's history for this project is `32dbb7a4bb2` (the branch's change), `05ab4664dac` (handoff note), `1deddc51958` (this repair). Author Nick Shanin, no trailers.
- The outbound patch still carries `SOLR-13696-TESTING.md` from `05ab4664dac`. It says nothing was run and lists guesses that this report answers. It is a packaging item, removed before any PR, as with the other branches' notes. It was not removed here, because the assignment did not ask for that.

## 6. Verdict

**Ready for the main side to gate.**

- The drift the handoff named is repaired, and the characterization covers every field reference in the re-enabled classes, by reading.
- The premise is answered with its evidence class. Findings 1 to 3 have dispositions. Finding 2 is an owner decision that the gate does not need.
- The change is test-only. Production code is untouched.

Not verified: no build, test or beast run from this work. The gate should run `CategoryRoutedAliasUpdateProcessorTest` and `DimensionalRoutedAliasUpdateProcessorTest` with spotless, and beast Dimensional for the commitWithin race. If the gate shows failures beyond `ship_name_en`, the drift section is incomplete. It covers references read from the code, not failures observed in a run.
