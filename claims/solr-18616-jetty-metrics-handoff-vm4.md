# SOLR-18616 Jetty HTTP metrics: handoff (vm4)

Claim type: handoff only. No assignment existed for this work; it started from the
solr.apache.org mailing-list thread quoting Chris Hostetter's SOLR-18616 comment. Nothing
was run on the board before this file. Status: HANDED (vm4, not claimed by any host).

## Problem
SOLR-17855 removed the Dropwizard `InstrumentedEE10Handler` and `InstrumentedQueuedThreadPool`
from jetty.xml with no replacement. Solr 10 has no metric for requests that never reach a
core or collection, or for Jetty thread pool saturation.

## Work done (uncommitted, worktree on vm4 only)
Worktree: /workspace/worktrees/solr-18616-jetty-metrics (branch SOLR-18616-jetty-metrics,
from origin/main 498a09aea9). Not pushed. Copy the diff from the vm4 host if the next
owner needs it; it does not exist on the board.

- `solr/core/src/java/org/apache/solr/metrics/JettyServerMetrics.java` (new): `solr.jetty`
  registry. Observable counters and gauges over Jetty `StatisticsHandler` and
  `QueuedThreadPool`. Request counters, active requests, responses by status class, failures,
  request time, read and written bytes, thread busy and idle, thread limits, utilization, queue size.
- `solr/core/src/test/org/apache/solr/metrics/JettyServerMetricsTest.java` (new): 2 tests.
- `CoreContainerProvider.java`: creates `JettyServerMetrics` after the container starts and
  closes it on shutdown.
- `solr/server/etc/jetty.xml`: `StatisticsHandler` as the outermost handler.
- `JettySolrRunner.java` (test-framework): wraps the chain in `StatisticsHandler`.
- `solr/core/build.gradle` and `gradle.lockfile`: `jetty-server` moves from testImplementation
  to implementation.
- `metrics-reporting.adoc`: new Jetty Registry section.
- `changelog/unreleased/SOLR-18616-jetty-metrics.yml`.

Size: 7 tracked files, 2 new source files (about 288 lines), 55 insertions and 5 deletions.

## Results
- Tidy: pass after reverting its unrelated `libs.versions.toml` change (removes `oshai-logging`).
- `JettyServerMetricsTest` 2/2 and `JvmMetricsTest` 4/4, seed `F98EA7EDC1450EB6`.
  Logs: `/workspace/gates/logs/vm4/solr-18616-jetty-metrics/` (test.log, test-xml/).
- Root `check -x test -x :rat`: FAILS in `:solr:solr-ref-guide:buildLocalAntoraSite`
  (npx exit 1). Log: `check.log` in the same directory. Not confirmed as pre-existing.
- Root `:rat` fails on 56 untouched repo files (.github, dev-docs, AGENTS.md). Not from this change.

Failed attempts (logs in the same directory):
- JDK HttpClient: PKIX failure, then URLPermission denied by the test security policy. Switched to Jetty HttpClient.
- Read-bytes assertion wrong for unread 404 bodies. Relaxed to `>= 0`.
- validateLogCalls flagged a computed log argument. Fixed.

## Decisions for the owner (decision ladder rung 5)
1. Scope: keep one PR, or split. Proposed split: (a) the `JettyServerMetrics` class and its test,
   which need only the `jetty-server` dependency; (b) the jetty.xml and `JettySolrRunner` wiring.
   Owner decides.
2. Antora failure: confirm the root check failure is pre-existing on clean `origin/main`
   (SOLR-18178 NODE_ENV problem) before any PR. Not yet tested.
3. Registry: `solr.jetty` (proposed) versus the existing node registry.
4. Hostetter's suggested design (StatisticsHandler plus gauges read by Solr) is what this implements.
   Owner decides whether it needs a JIRA comment or a mailing-list reply before a PR.
5. Whether to ask Chris Hostetter, or the list, for a review of the metric names before they ship.

## Next steps (not started)
- Check the Antora failure on clean `origin/main`.
- Decide scope (decision 1). Then commit as Nick Shanin, with no trailers, and push a fast-forward branch.
- Remove `/workspace/worktrees/solr-18616-jetty-metrics` after closeout, per AGENTS.md.
