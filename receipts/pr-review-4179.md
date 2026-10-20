# Receipt: PR review apache/solr #4179 (vm4)

- PR: https://github.com/apache/solr/pull/4179, open, not draft, head branch `copilot/migrate-splitcoreapi-to-jaxrs` on `epugh/solr`, base `812408433c`, 14 commits, 23 files, +1433/-895, `mergeable_state` unknown at the snapshot (GitHub files API).
- Files and sizes: changelog entry `changelog/unreleased/migrate-splitcoreapi-to-jaxrs.yml` (added, 7), `SplitCoreAPI.java` (+612/-50), `SplitShardAPI.java` (+161/-34), `SplitOp.java` (+30/-577), `CollectionsHandler.java` (+4/-57), `AdminAPIBase.java` (+40/-8), new models `SplitCoreRequestBody.java`, `SplitCoreResponse.java`, `SplitShardRequestBody.java`, `SplitShardResponse.java`, new tests `SplitCoreAPITest.java`, `SplitShardAPITest.java`, renamed `SplitHandlerTest.java`, changed `SplitShardTest.java` (+22), `TestApiFramework.java`, `TestCollectionAPIs.java` (-14), `V2CoreAPIMappingTest.java` (-36), `V2ShardsAPIMappingTest.java` (-61), docs `coreadmin-api.adoc` (+93) and `shard-management.adoc`.
- Base checks at `c8ab71430f` (`git ls-tree -r`): `SplitCoreAPI.java`, `SplitShardAPI.java`, `SplitOp.java`, `AdminAPIBase.java`, `RenameCore.java`, `CreateShard.java`, `DeleteShard.java` exist. `SplitCoreRequestBody.java` and `SplitShardResponse.java` do not, so the added models do not collide.
- Dash check on added lines (GitHub patches): 0 em or en dashes.
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4179.md`.
