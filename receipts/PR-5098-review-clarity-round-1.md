# Receipt: PR 5098 review clarity gate, round 1

Verdict: GATE GREEN at H.

- Job: `gates/pr-5098-review-clarity-round-1.md`
- Implementation: `claims/pr-5098-review-clarity-implementation-round-1.md`, `reports/pr-5098-review-clarity-implementation-round-1.md`
- Execution claim: `claims/pr-5098-review-clarity-round-1-phone-agent.md`
- Executor: phone-agent (Linux, this VM), 2026-11-06T18:33:27Z to 2026-11-06T18:58:47Z
- Candidate H: `981b7c230bdd1428b60867dd1ea700576b94b393` on fork branch `solr-18530-submit`; parent `d822b88b1bd58c197e4ad09c53ffff0920b76190`; base main; PR #5098 open
- Scope verified locally: parent..H touches exactly two test files, `RecoveryAfterSoftCommitTest.java` and `AbstractFullDistribZkTestBase.java` (comment correction, boolean parameter rename and documentation). Test-only; no changelog fragment, as the job states. This receipt is an H run; the parent's green direct-leader receipt is not claimed as one.
- Toolchain: Gradle 9.7.0 via `solr-gradle.sh`, vendored JDK 21, builds serialized under the host build lock. Separate clean clones for the positive legs and the mutation leg; the root check ran in the positive clone (a normal clone, not a worktree).
- Evidence: `evidence/pr-5098-review-clarity-round-1/pr-5098-clarity-round-1-evidence.tar.gz`, SHA-256 `1b5d3355471f94062e3e79170d73fa5c61ccc842fe493b4673c73647d97b877a` (logs, fresh JUnit XML for every run, mutation diff and hashes, runner scripts)

## Preflight

- Live fork ref and live PR #5098 head both read as H before claiming and again at close; PR open throughout.
- `./gradlew tidy` at H: exit 0, no tracked change. `git diff --check`: exit 0.

## Focused proof at H (fresh JUnit XML per run)

`RecoveryAfterSoftCommitTest.test` (`:solr:core:test`), one independent invocation per seed, XML preserved before each next `cleanTest`. Every run: 1 test, 0 failures, 0 errors, 0 skipped.

- Seed `A57A22E346C237C6`: pass (XML 2026-11-06T18:38:42Z)
- Seed `18530C0DE0000001`: pass (XML 2026-11-06T18:39:50Z)
- Seed `18530C0DE0000002`: pass (XML 2026-11-06T18:40:46Z)
- Seed `18530C0DE0000003`: pass (XML 2026-11-06T18:41:42Z)
- Seed `18530C0DE0000004`: pass (XML 2026-11-06T18:42:45Z)
- Seed `18530C0DE0000005`: pass (XML 2026-11-06T18:43:48Z)

## Root check

- `./gradlew check -x test` at pristine H: exit 0, no extra exclusions.

## Configuration sensitivity mutation

Separate H clone; the test file left unchanged (SHA-256 `1c3c08f3a394ec63d1a209bd164a378c50b7f1c55e56154c5d5ec6991dbc941c`). The only change omits the single statement `builder.sendDirectUpdatesToShardLeadersOnly();` from `createNewCloudSolrClient` in `AbstractFullDistribZkTestBase.java` (mutated helper SHA-256 `40e14339f93da19ec786f36958171d13ec24438488e9254baf5ce5f8da7a7398`; exact diff in the evidence). The mutant compiles. At seed `A57A22E346C237C6`: 1 test executed, exactly 1 failure, an AssertionError at `RecoveryAfterSoftCommitTest.java:111`, which is `assertTrue(leaderClient.isDirectUpdatesToLeadersOnly())`; the preceding leader-routing assertion at line 110 passed, since execution reached line 111. This is the synthetic test-configuration mutation the job specifies, not a reproduction of the intermittent production failure.

The helper was then restored (restored SHA-256 `0437dc09291561504ef6f08900c699d2b19de29fe88fbb37d11c052dca95459f`, byte-identical to the H blob) and the same seed rerun in the restored tree: 1 test, 0 failures, 0 errors, 0 skipped.

## Close

All GREEN conditions in the job are met: clean tidy at H, all six seed runs, the specified compiling mutation with its exact failure and the restored pass, root `check -x test`, clean trees in both clones, and final fork and PR read-backs matching H. No PR comment, PR body edit, Jira write, or other public communication was made.
