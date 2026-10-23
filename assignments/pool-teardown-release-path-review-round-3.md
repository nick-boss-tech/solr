# Assignment: pool-teardown-release-path-review-round-3 (review of the release-path candidate, its proof, and PR 5213)

- Status: OPEN (unclaimed). Requested by vm4 on behalf of Nick Shanin, 2026-10-23.
- Scope: review and challenge the current release-path work and its evidence, and report concerns. Do not change production code on any PR branch, do not push to any PR branch, do not comment on any PR, do not open a PR, do not file a JIRA, do not rerun CI. Scratch branches may be pushed to the fork `nick-boss-tech/solr` as fast-forward pushes only.

## Current state (as of 2026-10-23 19:45 UTC)

- Round 1 diagnosis (`assignments/pool-teardown-objecttracker-leak.md`, DONE, report `reports/teardown-objecttracker-leak.md`): three CI classes showed leaks (`DistributedQueryComponentCustomSortTest`, `CloudHttp2SolrClientTest`, `TestThinCache`). Single-class and full-core runs on `main` did not reproduce them.
- Round 2 (`assignments/pool-teardown-release-path-round-2.md`, `assignments/pool-teardown-release-path-round-2-linux-proof.md`): HANDED BACK by vm4 with the evidence below.
- Candidate branches (implementor, pushed): `solr-18564-release-path-r2` at `ee1eb2474f`, and the test-only baseline `solr-18564-release-path-r2-before` at `f92d771512`. Report: `reports/teardown-release-path-round-2.md` on coordination at `3d4e598c8b`.
- vm4 pushed `f0adb05094` to `nick-boss-tech/solr` branch `fix-interrupted-http-client-close`. It is the client-only change. `ee1eb2474f` contains a cherry-pick of it.
- PR #5213 (SOLR-18578, open, by nick-boss-tech): `ObjectReleaseTracker.tryClose()` gains an `AutoCloseable` branch. It is commit `35faf539e9`, branch `phone-agent-2-objectrelease-autocloseable-candidate`.

## Evidence from the Linux proof run (vm4, candidate `ee1eb2474f6650f9c8f8a51c6729383496a4256e`)

- `./gradlew tidy`: exit 0, no file changes. Recorded in `candidate-tidy.log`.
- Item 1, command 1, `HttpShardHandlerFactoryReleaseTest.testExecutorTimeoutStillClosesMetrics`: exit 0, passed.
- Item 1, command 2, `CoreContainerReleasePathTest.testInterruptedContainerWaitReturnsBeforeFactoryClose`: the test body passed. The class-level tracker check failed with `ObjectTracker found 4 object(s) that were not released: [HttpJettySolrClient, HttpJettySolrClient, SolrMetricsContext, HttpJettySolrClient]`. The `SolrMetricsContext` is owned by `HttpSolrClientProvider`. The report expects this check to be empty after the latch opens, so this is a failure. Logs: `/workspace/gates/logs/vm4/linux-proof/candidate-interrupted-wait.log` and `xml-candidate-interrupted-wait/` on vm4.
- Commands 3 and 4 (container tail, client interrupt) were not run, because the stop rule ends the assignment at the first failure. The fail-before baseline was not run either.

## Concerns raised by vm4 (review these, do not assume they are right)

1. **The proof's own tracker output is not clean.** The interrupted-wait experiment leaves three clients and one `HttpSolrClientProvider` context tracked. Either the test creates objects it never closes (a test-hygiene bug), or the release path does not cover the update shard handler's `HttpSolrClientProvider`. The report does not mention `HttpSolrClientProvider` at all. Decide which, with evidence.
2. **PR #5213 would hide the evidence.** Its `tryClose()` change closes leftover `AutoCloseable` objects only after a class fails. With it, a failing proof could show an empty tracker for the wrong reason. It does not fix the release path. It must stay out of every proof tree, and any reviewer should say whether it should land at all, separately from teardown.
3. **The nested-wait mechanism is still a hypothesis.** The report says so. The 60-second stall seen in CI has not been reproduced locally. Single-client probes did not show it: an interrupted close returns in 3 ms, a plain close after requests takes 0-4 ms, and a close during an in-flight request takes 7 ms. Any claim about why CI waited needs a stack capture or a shard-server-stop reproduction.
4. **Baseline and CI mismatch.** The `before` branch is pinned to Apache `79b23df58d`. The round-1 runs used the fork's `498a09aea9`, which the round-1 report calls a stale baseline. The full-core run on `498a09aea9` did not reproduce the leak, and its four failures (`TestTlogReplica`, `CoreAdminHandlerTest`, `TestCoordinatorRole`) do not match the CI failures. Check whether those four fail on CI too.
5. **Candidate and client-fix provenance.** `ee1eb2474f` cherry-picks vm4's client-only commit onto a different base, and its regression test covers only the interrupted-stop case. Check that the cherry-pick matches `f0adb05094` and that the test still asserts what it should.
6. **Scratch worktrees and processes.** vm4 still has worktrees `/workspace/worktrees/linux-proof-candidate` and `/workspace/worktrees/linux-proof-before`, plus leftover Gradle daemons from its runs. These will be removed after the hand-back is confirmed. Owners of other scratch worktrees should check their own.

## Steps

1. Read the round-2 report and the two linux-proof assignments on coordination, then this assignment's concerns list.
2. Diff `ee1eb2474f` against `f92d771512` and against `f0adb05094`. For each production change, state whether a CI leak report could still occur after it. Say which production files each candidate change touches.
3. For concern 1: list every object the interrupted-wait test creates and every close the test performs. Say whether the three clients and `HttpSolrClientProvider` belong to the test's cores. Propose a test-side fix only on a scratch branch, if it is the test's fault.
4. For concern 3: decide whether a stack capture from the CI job is possible, or whether a shard-server-stop reproduction is needed. Do not start a full core run without the main agent's OK.
5. For concern 2: state in writing whether PR #5213 should be closed, changed, or kept separate from teardown, and why.
6. Run no more than one focused test per candidate test name, and only on a scratch branch, under the host's Gradle lock. Do not retry a failing test. Record the result and stop.
7. Remove your scratch worktrees after the report is pushed.

## Deliverable

- `reports/teardown-release-path-review-round-3.md`: the review, the answer to each numbered concern, any scratch branch name and SHA, and a short list of owner decisions.
- Status line above set to DONE or HANDED BACK, in the same push as the report.

## Owner decisions (not for this assignment)

- Whether to file a JIRA for the teardown problem and whether to open a PR for `fix-interrupted-http-client-close` or `solr-18564-release-path-r2`.
- Whether PR #5213 and PR 5192 land, and in what order.
- Whether to rerun CI, run a full suite, or comment on any PR.
