# Report: split-shard migration behavior repairs, round 2

Status: IMPLEMENTED, PROOF PENDING

- Target: [PR 5265](https://github.com/apache/solr/pull/5265), SOLR-16398.
- Claim: `claims/pr-5265-split-shard-behavior-round-2-windows.md`.
- Branch: `nick-boss-tech/solr:solr-16398-split-shard`.
- Previous head: `0495ffa2b00772189d2f92d83b32140f1b5e8b39`.
- Pushed fix: `b1f09ab79a3d3f1943e62898512447f836342b34`, `SOLR-16398: Preserve split timeout, timing and option validation`.
- Normal fast-forward push completed. The fork ref and live PR head were independently read back equal to the full fix SHA. The PR head moved; the prior head's receipt does not prove this one.
- Gate: `gates/pr-5265-split-shard-behavior-round-2.md`; Linux host must claim and run it. No runtime or formatting pass is claimed by this implementation report.

## Findings addressed

1. **Timeout:** timeout-aware `AdminAPIBase` overloads preserve the existing defaults while split-shard explicitly requests the old handler's five-times-default timeout. Tests assert both the split timeout and the generic accumulating helper's unchanged default.
2. **Timing output:** the endpoint declares a new `SplitShardResponse` extending the existing accumulating response. It exposes timing as a map; the resource copies the command's timing NamedList and preserves nested phases for Jersey's existing NamedList serializer. Unit coverage checks common fields and timing together; the generated-client cloud test checks numeric total and `splitParentCore` phase times through HTTP.
3. **Option conflicts:** reject ranges plus splitKey, numSubShards plus either, and splitFuzz plus either, before submission. The error messages match the old handler. Five unit cases assert the code, message and no command-runner interaction. A real-client case asserts HTTP 400 for ranges plus numSubShards. The old all-properties test is divided into nonconflicting mapping alternatives, preserving all body-field coverage.

## Outbound files

Six files, 297 insertions and 31 deletions:

- `solr/api/src/java/org/apache/solr/client/api/endpoint/SplitShardApi.java`
- `solr/api/src/java/org/apache/solr/client/api/model/SplitShardResponse.java` (new)
- `solr/core/src/java/org/apache/solr/handler/admin/api/AdminAPIBase.java`
- `solr/core/src/java/org/apache/solr/handler/admin/api/SplitShard.java`
- `solr/core/src/test/org/apache/solr/handler/admin/api/SplitShardAPITest.java`
- `solr/core/src/test/org/apache/solr/handler/admin/api/SplitShardMessageMappingTest.java`

The new response class has an ASF header. No extra fixture, schema, configset, dependency or generated build output was added. The changelog already accurately scopes this PR to split-shard with Nick Shanin as author. The v1 route and its existing option checks are unchanged. Permission remains collection-edit. No new trust boundary is introduced.

## Evidence and limits

- Read the complete staged patch, old handler checks and timeout, command timing production, NamedList conversion/serializer, response models, generated-client template and neighboring helper/test usage.
- `git diff --cached --check` passed; tree clean after commit. Git author/committer is the required Nick Shanin identity; no attribution trailer.
- No Gradle, compilation, formatting tool or test ran on Windows. The gate requires exact-head tidy, Error Prone/OpenAPI generation, focused and neighboring tests, module check, and three controlled mutations proving the new assertions detect the old behaviors.
- The helper's default overloads still select `DEFAULT_COLLECTION_OP_TIMEOUT`; the split-specific path alone supplies the multiplier. The timing model uses existing dynamic map and NamedList serialization patterns.
- SOLR-17241 stays separate; this patch and its planned JSON-client proof make no claim about non-hosting-node JavaBin behavior.
- The live PR body still cites `0495ffa2b0` and the generic accumulating response. That proof is historical after this push; any body update must describe the new response and pending/new gate accurately. No body edit, public comment or JIRA write was made.

The local implementation worktree is `C:/Users/shaninna/dev/Solr-issues/wt/SOLR-16398-PR5265-behavior-r2`. Both full remote SHA read-backs matched `b1f09ab79a3d3f1943e62898512447f836342b34` after pushing.
