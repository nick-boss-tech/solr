# Receipt: SOLR-18601 / PR 5253 cleanup repair, round 3 (vm2, Linux): NOT GREEN

Result: NOT GREEN. Heads and results:

- I `6a99e731dadd4c3004b46e2bb42a0e6cad59cfc4`: not proof. Its preflight tidy rewrote one tracked test file (whitespace-only), so the gate's clean-tidy rule failed. Round 4 superseding gate `gates/pr-5253-cleanup-repair-superseding-45dbc3a-round-4.md` (board `f9db938e3c5`).
- J `45dbc3ac0b21785f9d0f41112ee6dea98ee55394`: formatting-only repair commit (parent I), pushed plain to `solr-18601-submit`, live ref read back as J. Tidy at J: rc 0, no tracked or untracked change, `git diff --check` clean.
- At J, focused leg (seed 5253C0DE, 18 methods): 18 run, 1 failure, 0 errors, 0 skipped. MigrateRouteKeyTest 8 run / 1 failed; MigrateCmdCleanupTest 5 run / 0 failed; SliceMutatorRoutingRuleTest 5 run / 0 failed. Failed: `testFailedMigratePreservesEarlierForwardedUpdates`.

Not run at J: the 5253BEEF and 5253CAFE cloud regressions, the neighbor set, `check -x test`, `checkSiteLinks`, negative legs A, B and C. The positive driver was stopped after the focused leg; beef had just started.

## Failure at J

`SolrMetricTestUtils.getGaugeDatapoint` (called from the injection hook, MigrateRouteKeyTest line ~280) reads the core's Prometheus metric reader through `MetricManager.getPrometheusMetricReader(registryName)`. The reader is `null` for this replica's core registry in the MiniSolrCloud test. The hook therefore throws a `NullPointerException` (wrapped as `RuntimeException`) before its buffered-operation assertion. The migration reports `NullPointerException ... "reader" is null`, and the test's injected-failure marker assertion fails at line 302.

This is a test oracle failure, not evidence about cleanup behaviour. Confirmed from `repair-I/focused-xml/TEST-org.apache.solr.cloud.MigrateRouteKeyTest.xml` and the server log in `repair-I/focused.log`. The metric lookup returned null, so the remaining hook assertions (BUFFERING state, operation count 3, source/target versions) did not run.

Options (a design decision, not made here):
1. Read the buffered-operation count directly from the target's `UpdateLog` in the test, which needs a public accessor or a documented test-visible path.
2. Enable the Prometheus reader for the core registry in the cloud test configuration, which affects other cloud tests.

## Environment incident (affects reproducibility, not the failure)

Clone creation failed twice before this run because the source store `/workspace/solr/.git` was a partial clone (`remote.upstream.partialclonefilter=blob:none`, promisor packs). I ran `git repack -a -d` on it to shrink the store. This dropped packed objects reachable only from unnamed commits, including the fork commits I, H, G and F, which had been fetched by SHA rather than into a ref. The fork branch was fetched into `refs/remotes/origin/solr-18601-submit` to restore reachability, and the gate sources were rebuilt from the GitHub fork (`/workspace/worktrees/gate18601-src`, blob-less, checked out at I, H and O, then local hardlinked clones per leg). Clean status was verified for every clone at its head before use.

The shared `/workspace/solr` store still has 247k loose objects, and the detached HEAD `aaaf6796e7e` is missing objects. Those were not used by the gate. This is recorded so the store can be repaired with the owner's approval.

## Evidence

- `repair-I/steps.txt`, `steps-prior-attempt.txt`, `steps-I-attempt.txt`: step logs.
- `repair-I/tidy.log`, `tidy-diff.txt`, `diff-check.txt`: I-head tidy result (I) and clean J tidy (`tidy-H.log`, not used).
- `repair-I/focused.log`, `focused-xml/`: J focused leg.
- `repair-I/setup5.txt`, `setup6.txt`, `gate-src.txt`: clone and source setup records.

No public GitHub or JIRA action was taken. The contributor branch and public PR 5253 are unchanged.
