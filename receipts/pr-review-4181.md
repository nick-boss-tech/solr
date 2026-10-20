# Receipt: PR review apache/solr #4181 (vm4)

- PR: https://github.com/apache/solr/pull/4181, open, not draft, head branch `copilot/migrate-realtimegetapi-to-jax-rs` on `epugh/solr`, base `ae262ba33e`, 12 commits, 9 files, +409/-32, `mergeable_state` unknown at the snapshot (GitHub files API).
- Files: changelog entry (added, 7), `RealTimeGetApi.java` (added, +45), `GetDocumentsResponse.java` (added, +78), `RealTimeGetHandler.java` (+8/-3), `RealTimeGetAPI.java` (+122/-12), `MessageBodyWriters.java` (+3/-1), `SolrJacksonMapper.java` (+48), `V2ApiIntegrationTest.java` (+3/-16), `RealTimeGetAPITest.java` (added, +95).
- Base checks at `c8ab71430f` (`git ls-tree -r`): `RealTimeGetAPI.java`, `SolrJacksonMapper.java`, `MessageBodyWriters.java`, `JerseyResource.java` exist. `RealTimeGetApi.java` and `GetDocumentsResponse.java` do not, so the new files do not collide. `SolrJacksonMapperTest.java` exists at base and is not changed by the PR.
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4181.md`.
