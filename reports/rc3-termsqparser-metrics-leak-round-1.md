# TestTermsQParserPlugin metrics leak (unit 3, round 1)

Verdict: mechanism identified as SOLR-18564 (the unit's amendment closes on this). Step 1 shows the co-occurrence in one run only, so it is a single-occurrence finding. Step 2 shows the SOLR-18564 fix removes the timeout and the leak on the reproduction the unit builds, on one seed. No new fix branch was created under this assignment.

## Step 1: logs only (no Gradle)

Source: vm5 RC3 smoke loop run logs and test XML (`/workspace/smoke-rc3/tmp/run-00N/unpack/solr-10.1.0/solr/core/build/test-results/test/`). Per-run failure lists were parsed from the XML of `TestTaskManagement` and `TestTermsQParserPlugin`:

| Run | TestTaskManagement failures | TestTermsQParserPlugin failures |
|---|---|---|
| 001 | 0 | 0 |
| 002 | 0 | 0 |
| 003 | 1 (classMethod: "Error shutting down MiniSolrCloudCluster") | 1 (classMethod: "ObjectTracker found 16 object(s) that were not released" [SolrMetricsContext]) |
| 005 | 0 | 0 |
| 006 | 0 | 0 |

- Run 004 (died at host reboot) and run 007 (stopped) have no test XML, so they are not counted.
- In run 003 the `TestTermsQParserPlugin` XML starts 2 minutes after `TestTaskManagement` (`timestamp` 14:04:05 and 14:06:10 UTC). The XML carries no JVM id, so it cannot confirm that both ran in the same forked JVM. That is the step 1 limit.
- Limit: one occurrence in five completed runs. The data is consistent with "the leak charge follows a shutdown timeout in the same fork", but it does not establish it.

## Step 2: reproduction on the RC3 tip (focused runs, host lock, JDK 21.0.12.1)

The regression test `CoreContainerShutdownTest` exists only in the fix commit `d7caa8cb784` (on branch `corecontainer-shutdown-closes`, based on apache/main), so the RC3 tip has no such test. The two runs below use the RC3 tip at `749712b7194`, with the test file added from the fix commit.

| Run | Source | Result | Failure messages | Seed | Log |
|---|---|---|---|---|---|
| base | RC3 tip, unmodified `CoreContainer.java`, plus the test | 3 tests reported, 3 failed: 1 test method and 2 class-level failures | `testShutdownRunsTrailingClosesWhenClosePoolTimesOut`: "Timeout waiting for pool to shutdown [closeThreadPool]"; `classMethod`: "ObjectTracker found 15 object(s) not released" [SolrMetricsContext], twice | D4DAB631E49B77A8 | `/workspace/smoke-rc3/termsq-repro/base-rc3-shutdowntest-v2.log` |
| fixed | RC3 tip plus `CoreContainer.java` from `d7caa8cb784` | 1 test, passed; no class-level failure | none | E89FE6C82AB0D707 | `/workspace/smoke-rc3/termsq-repro/fixed-rc3-shutdowntest.log` |

Base reproduces the signature: a close-pool timeout and 15 unreleased `SolrMetricsContext` objects, which is the SOLR-18564 mechanism. The base run also has an earlier attempt (`base-rc3-shutdowntest.log`) that found no tests, because the test file was missing; it is not counted.

## Limits

- One seed per arm. The fixed run is one passing run of the regression test, not a repeat sweep.
- The regression test is the unit's reproduction. The original TermsQ failure (run 003) was not re-run on the fixed tree, because the timeout that triggers the leak is intermittent and a focused TermsQ run may not produce it.
- SOLR-18588 (the TermsQ ticket Nick opened) was closed as a duplicate of SOLR-18564 on 2026-10-24; this report supports that closure on the evidence above.

## Not done

- No branch pushed, no PR, no JIRA comment, no dev-list text.
- The RC3 tip worktree `/workspace/worktrees/tlog-rc3-tip` still has the fix staged for the fixed run; it is a scratch worktree and is not committed.
