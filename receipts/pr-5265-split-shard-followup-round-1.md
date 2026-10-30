# Receipt: PR 5265 split-shard follow-up gate, round 1 (phone agent): PASS

- Gate: `gates/pr-5265-split-shard-followup-round-1.md`. Assignment: `assignments/pr-5265-split-shard-followup-round-1.md`. Claim: `claims/pr-5265-split-shard-followup-round-1-phone-agent.md`.
- Fork: nick-boss-tech/solr, branch `solr-16398-split-shard`. Tested head: `0495ffa2b00772189d2f92d83b32140f1b5e8b39`. Remote head verified equal before and after the run.
- Host: phone agent (this VM). JDK: Temurin 21.0.12.1+1 (vendored, via `~/workspace/tools/solr-gradle.sh`, serialized Gradle lock).
- Seed: `16398C0DE`. Run window: 2026-10-30T02:47Z to 02:52Z. Logs: `~/workspace/tools/gate-5265-followup/` (`tidy.log`, `compile.log`, `tests.log`, `check.log`, `gate-summary.log`); fresh JUnit XML in the worktree's `solr/core/build/test-results/test/`.

## Per-step results

| Step | Command | Result |
|---|---|---|
| tidy | `:solr:core:tidy` | exit 0; tracked tree clean afterward (0 entries) |
| changelog parse | `check-changelog-yaml.sh changelog/unreleased/SOLR-16398.yml` (pre-commit) | parses OK |
| compile, Error Prone | `:solr:api:compileJava :solr:solrj:compileJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | exit 0 |
| focused tests | `:solr:core:test` on SplitShardAPITest, TestCollectionAPIs, SplitShardMessageMappingTest, `-Dtests.seed=16398C0DE` | exit 0; SplitShardAPITest tests=4, TestCollectionAPIs tests=2, SplitShardMessageMappingTest tests=3; 0 skipped, 0 failures, 0 errors in each |
| module check | `:solr:core:check -x test` | exit 0 |
| diff check | `git diff --check` | exit 0 |
| final state | `git rev-parse HEAD`, `git status --porcelain` | head still `0495ffa2b0`; tree clean |

## Verdict

PASS at `0495ffa2b0`. The PR is Implemented and Proven at this head for the assignment's scope: request-body mapping coverage and the narrowed changelog. SOLR-17241 was not part of this gate and no claim is made about it here.
