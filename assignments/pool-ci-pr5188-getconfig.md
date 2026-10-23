# Assignment: pool-ci-pr5188-getconfig (CI failure on apache/solr PR 5188)

- Status: CLAIMED by the phone agent (vm1) in session at the owner’s direction, 2026-10-23; vm4 should not start it. Claim: `claims/pool-phone-agent-ci-pr5188-getconfig.md`. Capability tags: gate, investigation.
- Requested by: Nick Shanin (vm4 agent relayed the request, 2026-10-23).
- Scope: reproduce and locate one failing test on PR 5188. Do not push to the PR branch, do not comment on the PR, and do not change production code.

## Context

- PR: https://github.com/apache/solr/pull/5188, "fl= fix some minor issues, mostly with globs", head `ReturnFields_wantsLuceneField` at `54c8ae098e` on fork `dsmiley/solr`, base `main`.
- CI run: https://github.com/apache/solr/actions/runs/39522717278/job/118970125110 (job log saved at `/tmp/ci-5188/job.log` on vm4, not on coordination).
- Failure: `org.apache.solr.handler.admin.api.GetConfigTest.testGetConfigFromCore` reported 4 times in the same run, seed `47D7DB78A5A00E95`, always `java.lang.AssertionError` from a plain `assertTrue` at `GetConfigTest.java:65` in the CI tree.
- The PR head file puts that assertion at a different line: head line 65 is a closing brace. CI most likely built the merge of the PR with main, so the line numbers are off. Do not rely on line numbers from CI.
- Suspects (not verified): the `containsKey` assertions at head lines 55 to 64. The PR adds `renameToExternalFullConfigNames` in `GetConfig.java`, which renames `requestHandler` to `requestHandlers` and `searchComponent` to `searchComponents` on the v2 `getConfig` path. The `updateProcessor` key is asserted on main too.

## Steps

1. Ticket check: fetch PR head `54c8ae098e` into a scratch worktree `/workspace/worktrees/ci-5188` (detached). Do not touch the gate worktrees.
2. Run under the Gradle lock, from the scratch worktree, with the exact CI test and seed:
   `flock /workspace/tools/.gradle-vm4.lock bash -c 'source /workspace/tools/vm4-env.sh && ./gradlew :solr:core:test --tests org.apache.solr.handler.admin.api.GetConfigTest --console=plain -Ptests.seed=47D7DB78A5A00E95'`
   Keep the log and JUnit XML under `/workspace/gates/logs/vm4/ci-5188/`.
3. Record the exact failing assertion (file line in the head tree) and the actual map keys printed for `response.config`, if the test output shows them.
4. Run the same test on the base `c8ab71430f` (scratch worktree, same seed) to say whether the failure is new in the PR.
5. Do not relaunch a run that executed tests and failed. Record the result and stop.
6. Remove the scratch worktree after the report is pushed. Keep the logs.

## Deliverable

- `reports/ci-pr5188-getconfig.md`: failing assertion (head line), the keys seen, the base comparison, the log paths, and one sentence on the likely cause.
- Status line above set to DONE, HANDED BACK, or NEARLY with the step, in the same push as the report.

## Owner decisions (not for this assignment)

- Whether to change the PR, comment on it, or ask the author. These stay with the owner.
