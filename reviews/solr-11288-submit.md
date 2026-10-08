# solr-11288-submit

- Branch: origin/solr-11288-submit
- Head: cd094c3c623f (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind, 2 commits ahead)
- Scope: 2 commits, 5 files. `BalanceReplicasCmd.java` (`nodes` string parser: `Set.of(split)` replaced by trim, drop empties, `toSet`), `MigrateReplicasCmd.java` (`getNodesFromParam` string branch, same change), `ClusterStatus.java` (`shard` parser, same change), `TestCollectionAPI.java` (one new test, `clusterStatusWithRepeatedShard`, ClusterStatus only), changelog `SOLR-11288-set-of-split-duplicates.yml` (`type: fixed`). Imports were checked: `Collectors` and `Arrays` are present where used, and `Set` remains in use, so the diff should compile.
- Verdict: Needs work (changed from the bulk verdict of Nearly). The parsing fix is correct for repeated and padded names. Blank-only input now reaches the "no filter" or "default" branch of each command instead of an error. For balance and migrate that is a cluster-wide action. The changelog does not say so.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-11288-review.md` (Nearly) reviewed this head (`cd094c3c623`). No delta.

- Bulk F1 (LOW, add direct coverage for both replica-management parsers): **confirmed.** The only new test is `clusterStatusWithRepeatedShard` (`TestCollectionAPI.java:318-345`). Nothing exercises `BalanceReplicasCmd` or `MigrateReplicasCmd`, and nothing exercises a blank-only value. Kept at LOW as a coverage point, but see finding 1: the missing tests are the ones that would catch the semantic change.
- Bulk escaped-comma note (the umbrella JIRA suggests escaping-aware parsing; the branch uses raw `split`): **kept as a scope note, not a defect.** The bulk review found no evidence that these identifiers may contain commas. This review agrees. See owner call 3.

## Findings (ranked)

1. **MEDIUM, verified (code path). Blank-only values now select the default instead of erroring.** The base code produced a single-element set for `""` and `" "` (`"".split(",")` is `[""]`). That gave `BalanceReplicasCmd` "Cannot balance across a single node" (400), `ClusterStatus` "shard not found" (400), and `MigrateReplicasCmd` a bogus node name carried into the migration. The new parser drops blanks, which leaves an empty set. Each command then treats an empty set as its default:
   - `BalanceReplicasCmd`: an empty `nodes` goes to `assignStrategy.computeReplicaBalancing(..., nodes, ...)` (`BalanceReplicasCmd.java`, after the parse; the `nodes.size() == 1` check no longer fires). `BalanceRequestImpl.create` then uses all live data nodes when the set is empty (`solr/core/src/java/org/apache/solr/cluster/placement/impl/BalanceRequestImpl.java:57-73`). A blank `nodes` value therefore starts a cluster-wide rebalance. The base code rejected it with "Cannot balance across a single node".
   - `MigrateReplicasCmd`: an empty `targetNodes` takes the default "use all other live nodes" (`MigrateReplicasCmd.java:76-80`). A blank target value therefore starts a migration to every other live node. The base code treated `" "` as a bogus target name.
   - `ClusterStatus`: an empty `requestedShards` returns all shards (`ClusterStatus.java:260-261`, documented at `:249-250`). A blank `shard` value therefore returns the full cluster state instead of the base code's "shard not found" 400.
   - The base code already produced an empty set for a comma-only value (`",".split(",")` is an empty array). The branch extends that path to empty and whitespace-only input, which is what changes.
   - The changelog's "now ignore blanks around names" is true for mixed input. It does not say that a blank-only value now selects the default.
   - Proposed fix (not applied): after trimming, if the raw parameter was present but the list is empty, throw `BAD_REQUEST` as before. Owner call 1 decides whether the default behavior should instead be kept and documented.

2. **LOW, verified. The balance success message prints the empty node set.** `BalanceReplicasCmd.java` (success block, `String.join(", ", nodes)`) reports "completed successfully across nodes : []" when the balancer ran over all live nodes. Misleading output for the blank case. Fix with finding 1.

3. **LOW, verified. Coverage gap, see the delta note.** No test covers `BalanceReplicasCmd` or `MigrateReplicasCmd` parsing, and none covers blank-only input. A test per command for repeated, padded, and blank-only values would have shown finding 1.

4. **LOW, verified (checked, no issue). Imports and compile surface.** `MigrateReplicasCmd` already imports `java.util.stream.Collectors` and `java.util.Set`. `BalanceReplicasCmd` and `ClusterStatus` add `Arrays` and `Collectors`, and both still use `Set`. No unused imports were introduced by the diff. Not compiled, so this is a reading.

5. **LOW, verified. The changelog wording needs the blank-only case.** The changelog title (`changelog/unreleased/SOLR-11288-set-of-split-duplicates.yml:2`) names the three commands and the blank-trimming. It should state what a blank-only value now does, once owner call 1 is settled.

## Verdict change and why

The bulk verdict was Nearly, on the strength of the parser fix and the one test. Finding 1 changes the verdict to Needs work. It is a behavior change on an admin path that can start a cluster-wide action, and the branch does not disclose it. The fix is small (one check after trimming). Once owner call 1 is settled and the check or the changelog is in place, the verdict should move back toward Nearly.

## Owner calls (not decided here)

1. **Blank-only semantics.** Should a blank-only `nodes`, `sourceNodes`, `targetNodes`, or `shard` value (a) keep the base code's `BAD_REQUEST`, which this review recommends, or (b) be treated as "not given", so the command uses its default? Option (b) is internally consistent, but it turns a malformed value into a cluster-wide balance or migration.
2. **Parser scope.** Should the same trim-and-drop parser be shared by all three commands? Today each file has its own copy of the same stream. This is a design choice; the review does not force it.
3. **Escaped commas.** The umbrella JIRA suggests escaping-aware parsing instead of raw `split`. The bulk review and this review found no evidence that node or shard names may contain commas. Whether the umbrella's escaping is in scope for this ticket is the owner's call.

## Proposed fixes (not applied; the owner decides)

- Finding 1: add a check after the trim-and-drop step, in each of the three parsers: if the raw value was non-null and the resulting set is empty, throw `SolrException(BAD_REQUEST, ...)` with the same wording as the base error for that command. Add a test for `""` and `" , "` in each command.
- Finding 2: follows from finding 1 if option (a) is chosen. If option (b) is chosen, print the resolved node list (`liveNodes`) in the success message.
- Finding 3: add `BalanceReplicasCmd` and `MigrateReplicasCmd` tests for repeated, padded, and blank-only values. Blank-only should expect a 400 under option (a).
- Finding 5: reword the changelog after owner call 1.

## Not checked

- Nothing compiled, formatted, or run. The import and compile conclusions in finding 4 are from reading only.
- `PlacementPluginAssignStrategy.computeReplicaBalancing` and the default `Assign.AssignStrategy` path were read only as far as the empty-set handoff. The default interface method returns `Map.of()`, so on that path the blank case does nothing. Which strategy runs in a given cluster was not determined.
- `ReplicaMigrationUtils` and `ClusterStatus` response shape for the blank case were not traced beyond the filter.
- The umbrella JIRA was not re-read; the escaped-comma suggestion is taken from the bulk review.
