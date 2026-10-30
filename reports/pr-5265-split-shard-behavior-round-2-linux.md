# Report: PR 5265 split-shard behavior repairs, round 2, Linux gate (phone agent)

Status: GATE GREEN at the superseding candidate.

- Job chain: `gates/pr-5265-split-shard-behavior-round-2.md` (candidate `b1f09ab79a3d3f1943e62898512447f836342b34`), superseded on its own tidy rule by `gates/pr-5265-split-shard-behavior-round-2-superseding.md`.
- Claim: `claims/pr-5265-split-shard-behavior-round-2-phone-agent.md`.
- Tested and live head: `009f2c60513636b4aba2c93f4a4137aa39ccea64` (`b1f09ab79a` plus tidy commit `009f2c6051`, "SOLR-16398: Tidy the split-shard behavior repairs", formatting only). At the end of the run the fork ref and the live PR #5265 head were both read back equal to this SHA.
- Host: phone agent, Linux. JDK: Temurin 21.0.12.1+1 (`~/workspace/tools/jdk21/jdk-21.0.12.1+1`). Build: `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, serialized queue lock). Workspace: fresh isolated worktree `~/workspace/solr-worktrees/16398-split-shard-r2gate`. Logs and mutant diffs: `~/workspace/tools/gate-5265-r2/` on this host.
- Seed: `16398C0DE` for every test invocation.
- Run window: 2026-10-30, tidy and compile at the original candidate 03:23Z to 03:28Z; superseding positive chain 03:29Z to 03:35Z; mutation legs and closing positive run 03:36Z to 03:40Z (UTC).

## Tidy supersede

Tidy at `b1f09ab79a` reformatted three branch files with line rewraps only (`AdminAPIBase.java`, `SplitShardAPITest.java`, `SplitShardMessageMappingTest.java`; verified in the diff, no semantic change). Per the job's rule the repair was committed as `009f2c6051` under the Nick Shanin identity and pushed as a normal fast-forward; proof restarted at the new head. The Error Prone compile at the original candidate had already passed (rc 0) and the regenerated `ShardsApi.SplitShard` returns `SplitShardResponse`.

## Positive proof at `009f2c6051`

- Tidy `:solr:core:tidy` rc 0; tracked tree 0 entries afterward (log `r2b-tidy.log`).
- Compile `:solr:api:compileJava :solr:solrj:compileJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` rc 0 (log `r2b-compile.log`).
- Focused plus neighboring tests, one invocation, rc 0 (log `r2b-tests.log`; fresh JUnit XML read for each suite):
  - `SplitShardMessageMappingTest`: 14 tests, 0 skipped, 0 failures.
  - `SplitShardAPITest`: 5 tests, 0 skipped, 0 failures (includes the timing assertions through the regenerated typed client and the HTTP 400 conflict case).
  - `TestCollectionAPIs`: 2 tests, 0 failures.
  - `CreateShardAPITest`: 6 tests, 0 failures.
  - `DeleteShardAPITest`: 4 tests, 0 failures.
  - `V2ApiIntegrationTest`: 13 tests, 0 failures.
- Module check `:solr:core:check -x test` rc 0 (log `r2b-check.log`).
- Full patch `git diff --check` rc 0; final head equals the candidate; tracked tree clean.

## Regression discrimination (controlled mutations of the candidate)

Each mutant was applied to `SplitShard.java` only, run with seed `16398C0DE`, then restored with `git checkout`; the tree read 0 tracked entries after every restore. Mutant diffs: `mutant-timeout.diff`, `mutant-timing.diff`, `mutant-validation.diff` beside the logs.

- Timeout mutation (submission timeout `DEFAULT_COLLECTION_OP_TIMEOUT * 5` reduced to `DEFAULT_COLLECTION_OP_TIMEOUT`): `testSplitShardUsesExtendedTimeout` failed (run rc 1) with a Mockito verification error: wanted `runCollectionCommand(..., 900000L)`, actual invocation carried `180000L`. Discrimination established (log `mutant-timeout.log`).
- Timing mutation (timing-copy block omitted, response type declaration kept): `testSplitShardPreservesTimingAndSubResponses` failed (run rc 1) with an assertion error at the timing assertion (`SplitShardMessageMappingTest.java:174`). Discrimination established (log `mutant-timing.log`).
- Validation mutation (`validateSplitOptions(requestBody)` call omitted): the five `testRejects*` methods all failed, 5 tests and 5 failures (run rc 1), because the resource accepted and submitted the conflicting options. Discrimination established (log `mutant-validation.log`).

Closing leg: after the last restore, the positive focused suite reran at the unmodified candidate, rc 0: `SplitShardMessageMappingTest` 14/14, `SplitShardAPITest` 5/5, `TestCollectionAPIs` 2/2 (log `r2b-final-positive.log`). End state: head `009f2c6051`, tracked tree clean.

## Limits

- SOLR-17241 and the non-hosting JavaBin path are excluded, as the job specifies; this JSON-client proof says nothing about them.
- The PR body still cites `0495ffa2b0` and the generic accumulating response; a corrected body text is prepared locally on this host and is not posted under this job's authority.
- No GitHub comment or review, no JIRA write, and no force push were made.
