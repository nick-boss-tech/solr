# Receipt: PR 5265 split-shard behavior repairs, round 2 gate

Verdict: GREEN at `009f2c60513636b4aba2c93f4a4137aa39ccea64`.

- Jobs: `gates/pr-5265-split-shard-behavior-round-2.md`, superseded on its tidy rule by `gates/pr-5265-split-shard-behavior-round-2-superseding.md` after tidy reformatted three files at `b1f09ab79a` (formatting only; repair commit `009f2c6051`, normal push).
- Claim: `claims/pr-5265-split-shard-behavior-round-2-phone-agent.md`. Full detail: `reports/pr-5265-split-shard-behavior-round-2-linux.md`.
- Host: phone agent, Linux; JDK Temurin 21.0.12.1+1; seed `16398C0DE`; 2026-10-30 03:29Z to 03:40Z. Fork ref and live PR #5265 head read back equal to the tested SHA at the end of the run.
- Legs at `009f2c6051`: tidy rc 0 with a clean tree; Error Prone compile rc 0 (generated `ShardsApi.SplitShard` returns `SplitShardResponse`); `SplitShardMessageMappingTest` 14/14, `SplitShardAPITest` 5/5, `TestCollectionAPIs` 2/2, `CreateShardAPITest` 6/6, `DeleteShardAPITest` 4/4, `V2ApiIntegrationTest` 13/13; `:solr:core:check -x test` rc 0; `git diff --check` rc 0.
- Mutation discrimination, all as specified: timeout mutant failed `testSplitShardUsesExtendedTimeout` (wanted 900000L, actual 180000L); timing mutant failed `testSplitShardPreservesTimingAndSubResponses`; validation mutant failed all five `testRejects*` methods. Each mutant restored; closing positive run at the unmodified candidate green (14/14, 5/5, 2/2).
- The `0495ffa2b0` receipt is stale for this head. The PR body still cites the prior head; a corrected body text is prepared locally, not posted. SOLR-17241 remains separate.
