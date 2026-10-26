# Receipt: PR 5232 listener correctness follow-up, Linux gate round 1 (vm4)

Gate: `gates/pr-5232-listener-followup-round-1.md` (head b3a11484f1, tidy-dirty by one line). Superseding gate: `gates/pr-5232-listener-followup-f2-round-1.md` (exact head F2). Claim: `claims/pr-5232-listener-followup-linux-vm4-round-1.md`. Host: vm4 (`hosts/vm4.md`).

Result: GREEN at F2 `23c68215cf0c858b7dd1ceda6b945d2d9daaa174` for the positive legs, and both findings show discriminating fail-before evidence on the parent. Limits are listed at the end.

## Heads and remote state

- Fork: `nick-boss-tech/solr`, branch `solr-11994-submit`. Read back by `git ls-remote` at `23c68215cf0c858b7dd1ceda6b945d2d9daaa174` after the push.
- Implementation commit `b3a11484f13feaab55fe4f27504bfce3d1bdd2ef`, "SOLR-11994: correct listener CPU accounting and exclude nested searches", Nick Shanin, no trailers.
- Tidy repair `23c68215cf0c858b7dd1ceda6b945d2d9daaa174`, "SOLR-11994: tidy the CPU assertion line in SearchRequestListenerTest", Nick Shanin, no trailers. One line changed in `solr/core/src/test/org/apache/solr/handler/component/SearchRequestListenerTest.java` (a wrapped `assertEquals` joined onto one line; no logic change). It is a fast-forward from b3a1.
- Original PR proof base: apache/solr PR 5232 head `2c9243b111b38367677d7e0c9b30b8e72c554d2c` (`sarkaramrit1993/solr:SOLR-11994-search-request-listener`).
- Delta from the PR base to b3a1: seven files, 167 insertions, 3 deletions. F2 adds the one-line tidy change on top.

## Preflight

| Step | Head | Command | Result | Log |
| --- | --- | --- | --- | --- |
| Preflight tidy | b3a1 | `./gradlew tidy` through the runner | rc 0; changed `SearchRequestListenerTest.java` (one line) | `preflight-tidy.log` |
| Tidy at F2 | F2 | `./gradlew tidy` through the runner | rc 0; tree clean afterwards | `preflight-tidy-f2.log` |

## Legs

Host runner: `/workspace/tmp/pool-bin/sweep-run.sh` under `/workspace/tools/.gradle-vm4.lock`, one build at a time. Driver: `/workspace/tmp/pr5232/driver.sh`; sequence and exit codes in `f2-driver.status`. JDK: Temurin 21.0.12.1+1 at `/workspace/tools/jdk21`. Gradle 9.7.0 via the repo wrapper. Seed `5232C0DE` on every test leg.

| Leg | Head | Command (task form) | Exit | Start to end (UTC) | Log |
| --- | --- | --- | --- | --- | --- |
| 1 focused | F2 | `:solr:core:cleanTest :solr:core:test --tests ...SearchRequestInfoTest --tests ...SearchRequestListenerTest -Ptests.seed=5232C0DE` | 0 | 16:16:05 to 16:21:34 | `f2-focused.log`, `f2-focused.xml/` |
| 2 neighbor | F2 | `:solr:core:cleanTest :solr:core:test --tests ...TestSolrRequestInfo --tests ...TestSubQueryTransformerDistrib -Ptests.seed=5232C0DE` | 0 | 16:21:34 to 16:23:34 | `f2-neighbor.log`, `f2-neighbor.xml/` |
| 3 root check | F2 | `check -x test` | 1, then 0 on relaunch (see below) | first attempt 16:23:34 to 16:39:47; relaunch 17:04:54 to 17:24:57 | `f2-check.log` (failed), `f2-check-r2.log` (passed) |
| 4 diff check | F2 | `git diff --check` in the full worktree, and `git show --check` at F2 | 0 | 16:39 | `f2-diffcheck.txt` |
| 5 negative | parent `2c9243b1` + two F2 test files | three named methods, seed 5232C0DE | 1 (expected) | 16:40 to 16:50:24 | `f2-negative.log`, `f2-negative.xml/`, `f2-negative-overlay.txt` |

### Leg 3 relaunch, recorded

- The first `check -x test` run in `/workspace/worktrees/gate-5232` exited 1 at `:solr:solr-ref-guide:buildLocalAntoraSite`, after every module `check` task had run. No test executed (the run was `-x test`). Cause: the host shell has `NODE_ENV=production`, so npm skipped devDependencies. `solr/solr-ref-guide/.gradle/node/node_modules/@asciidoctor` contains only `core`, not `tabs`. This is the same environment cause recorded for PR 5221 leg 4 (`reports/js-client-npm-failure-cause-round-1.md`).
- The one allowed relaunch used a shallow clone of the fork branch at F2 (`/workspace/worktrees/gate-5232-std`). A linked worktree is rejected by Antora as a local content source. `NODE_ENV` was unset for that run only. Host configuration was not changed.
- A stray earlier launch of the same relaunch script ran against a half-rebuilt directory and failed at startup (status line `17:02:28`). No build ran in that attempt. The clean relaunch is the one at `17:04:54`.
- Result of the clean relaunch: `BUILD SUCCESSFUL in 19m 59s`, 557 tasks executed, runner rc 0, no `FAILED` lines. Log `f2-check-r2.log`.
- No Gradle `errorProneSkipped` check was asked for by this gate, and none was run.

## Positive results, parsed from fresh XML (leg 1 and leg 2)

Leg 1 (focused), 17 tests, 0 failures, 0 errors, 0 skipped:

- `SearchRequestInfoTest` (4): `testLocalCpuNotDoubled`, `testDistributedFailureCountsLocalCpuOnce`, `testCpuUnavailable`, `testDistributedCpuIncludesLatestShardTotal`. All passed.
- `SearchRequestListenerTest` (13): `testCalledOnceOnCoordinator`, `testNoListenersLeavesResponseUnchanged`, `testV2Select`, `testFailedSearch`, `testDistributedSubQueriesNotReported`, `testCpuTime` (2.279 s, executed, not skipped), `testShardsTolerant`, `testTopLevelEmbeddedSearchReported`, `testLocalSubQueriesNotReported`, `testThrowingListenerDoesNotChangeResponse`, `testRequestInfo`, `testExportNotReported`, `testRealTimeGetNotReported`. All passed.

The gate asked for 4 CPU snapshot tests and 13 listener tests, which matches.

Leg 2 (neighbor), 4 tests, 0 failures, 0 errors, 0 skipped:

- `TestSolrRequestInfo`: 3 tests, all passed.
- `TestSubQueryTransformerDistrib`: 1 test, passed.

Leg 3 (root `check -x test`): BUILD SUCCESSFUL on the clean relaunch. This is the repository's module-level check with tests excluded; it is not a full test run.

## Negative results (leg 5, parent code plus the two F2 test files)

Production files: `git diff 2c9243b1 -- solr/core/src/java` is empty. The overlay adds two test files: `SearchRequestInfoTest.java` (new in the PR, so absent on the parent) and `SearchRequestListenerTest.java`. Overlay SHA-256 values are in `f2-negative-overlay.txt`:

- `SearchRequestInfoTest.java`: `61265485f99773b2d9f5faa9671008578e26fd96626a73a68f413d9c7f8a3272`
- `SearchRequestListenerTest.java`: `e541d98a0848cd4b863f6115051b7df14334d2379cc4556f535b525999bb5307`

The overlaid tests compile against the parent, so no new production method is needed. Three executed, three failed, each on an intended assertion:

| Method | Failure message | Frame | Finding |
| --- | --- | --- | --- |
| `SearchRequestInfoTest.testDistributedFailureCountsLocalCpuOnce` | `expected:<7> but was:<14>` | `SearchRequestInfoTest.java:81` | CPU fallback counted twice (7 ms local becomes 14 ms). |
| `SearchRequestListenerTest.testDistributedSubQueriesNotReported` | `expected:<1> but was:<4>` | `SearchRequestListenerTest.java:151` (`assertEquals(1, events.size())`) | Nested `[subquery]` children reported as top-level searches. |
| `SearchRequestListenerTest.testLocalSubQueriesNotReported` | `expected:<1> but was:<2>` | `SearchRequestListenerTest.java:151` | Same, for the local-search variant. |

Each subquery failure comes after the child-result assertions passed, as the gate requires, so the document content was correct and only the event count was wrong. Counts: 3 tests, 3 failures, 0 errors, 0 skipped. No compile error, startup failure, skip, or missing method.

## Tree state

- Positive worktree `/workspace/worktrees/gate-5232` at F2, clean after every leg (`git status --porcelain` empty).
- Shallow clone `/workspace/worktrees/gate-5232-std` at F2, clean after the relaunch.
- Full-history check: `git show --check` and `git diff --check b3a1 F2` both exit 0.

## Limits

- The root check with tests excluded is not a test run of the whole repository. Only the two focused classes and the two neighbor classes ran as tests.
- The first check attempt failed on the host's NODE_ENV setting. That is an environment defect of the host shell, not of this branch. The host shell still has `NODE_ENV=production` and is not changed by this gate. Nick's decision on the host fix is still open.
- The parent proof was a negative premise check: it shows the new tests discriminate the two findings, not that the parent is otherwise correct.
- Windows was not used for any gate leg. The earlier Windows implementation report is unchanged.
- No PR, comment, or Jira action was taken.

## Suggested upstream update (draft for Nick, not posted)

The PR 5232 follow-up at `solr-11994-submit` (F2 `23c68215cf`, implementation `b3a11484f1`) fixes two review findings: CPU fallback double counting, and `[subquery]` children being reported as top-level searches. On Linux, the two focused classes ran 17 of 17 green with `testCpuTime` executed, the neighbor classes ran 4 of 4 green, and the root check with tests excluded passed. On the parent code with the new tests, the three discriminating methods fail as expected: CPU 14 against 7, and one subquery event count of 4 and 2 against 1. Suggested wording for review: list those results, state the limits above, and say that a PR or comment is Nick's decision.
