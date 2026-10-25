# Assignment: repo-wide teardown-failure sweep (evidence first, round 1)

Status: OPEN
Tags: premise-run, records, review, sweep
Intended host: the strongest available pool Linux host holding the premise-run, records, and review tags. Not phone-agent-2 (directed drafting load), not Windows (no Gradle work if a probe is later approved).

Owner direction (2026-10-25, refined): the single DistributedExpandComponentTest settling run stays stood down (`assignments/distributed-expand-threadleak-settling-round-1-standdown.md`); flakes usually do not reproduce and queue time is not spent chasing one sighting. This sweep exists because teardown-shape failures have recurred across several independent sightings, so the pattern earns a census and a static sweep, not a reproduction chase. The stood-down settling run is not revived as-is by this assignment.

## Sightings that motivate the sweep (starting set, not the census)

- PR #5030's Crave run (job 119565807999): two classMethod teardown failures in one JVM pairing: CloudHttp2SolrClientTest erroring on MiniSolrCloudCluster shutdown, and TestJavaBinCodec charged at teardown with 16 unreleased SolrMetricsContext objects that a deep RCA traced back to the earlier suite's cluster; the pairing was seed-determined. Local full-suite runs at the recorded seed did not reproduce either failure; outcome recorded as no fix without a reproduction.
- RC3 smoke loop run 003: TestTaskManagement classMethod shutdown error in the same run where TestTermsQParserPlugin was charged with 16 unreleased SolrMetricsContext objects; that charge's mechanism is SOLR-18564 (CoreContainer.shutdown skipping later closes after a close-pool timeout), which HAS a fix (PR #5192). Do not redo the SOLR-18564 fix work; it is one proven mechanism inside this family, cited as a reference point.
- PR #5198's Crave job 121178733450: DistributedExpandComponentTest classMethod ThreadLeakError, one idle Jetty ManagedSelector thread in httpShardExecutor at seed 85F9F77FD74C1D41, all methods passing.

## Work

Phase A, census. From board records (reports, receipts, claims, day-scan folders) and the named public job logs, build the census of recent teardown-shape failures: classMethod failures, ThreadLeakError, ObjectReleaseTracker charges, MiniSolrCloudCluster shutdown errors or timeouts, unreleased metrics contexts, leaked executor or selector threads. Each entry: class, shape, source (PR, job, run, or report), seed if recorded, recurrence count, current status (fixed, ticketed, recorded-only, unclassified). Records first; fetch a job log only for a sighting the records name but do not characterize.

Phase B, static sweep. Read the lifecycle code for the patterns that produce these shapes: thread pools and Jetty selectors whose close is not deterministic (client close paths, selector stop under interruption), MiniSolrCloudCluster shutdown ordering and exception rethrow at teardown, CoreContainer close ordering and timeout behavior (the SOLR-18564 mechanism is already fixed; look for siblings, not a redo), metrics-context ownership and close paths, the JVM-global tracker charging a leak to a later innocent class, and test-harness teardown ordering that can leak threads past a suite boundary. Cite file and line for every pattern claimed; a pattern with no code citation is not a finding.

Phase C, ranking. Rank candidates by recurrence, cross-suite charging potential, and whether a cheap deciding probe exists. For each of the top candidates (about five), specify the cheapest deciding probe exactly: class, seed, tree, run shape, and its cost cap. Probes are specified in the report, not run in this assignment.

## Deliverable

`reports/teardown-failure-sweep-round-1.md`: the census table, the pattern map with code citations, the ranked candidate list with a specified deciding probe per candidate, and proposed next units sized as single claims.

## Limits

No broad queue burn: no reproduction suites, no speculative fix, no branch work in this assignment. Fixes, probes at scale, and any Jira or GitHub text are out of scope and wait for owner direction after the report lands.
