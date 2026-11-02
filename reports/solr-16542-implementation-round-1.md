# Report: SOLR-16542 phase 1 implementation, round 1 (phone-agent)

Ticket: https://issues.apache.org/jira/browse/SOLR-16542 (Open, Major, reporter
David Smiley, last updated 2025-10-17). Summary: Remove
OverseerConfigSetMessageHandler; always do distributed mode.

Branch: `solr-16542-submit` on nick-boss-tech/solr, head
85ce6f5475a259f61f786edcbf0058852a9f5db2, base current main
f489a30a31 (SOLR-18548). One commit, author Nick Shanin
<nick.boss.us@gmail.com>, no trailers. No PR, no Jira post.

## Premise, confirmed at current main

The handler was still live: `OverseerConfigSetMessageHandler` was constructed in
`OverseerCollectionConfigSetProcessor` and selected for every `configsets:`
prefixed message off the shared `/overseer/collection-queue-work` queue, while
`ConfigSetAPIBase` sent ConfigSet commands to that queue whenever the
distributed runner was absent. The Overseer path was still the default:
`ZkController` defaults `overseerEnabled` to true, and the distributed runner
was constructed only when the Overseer was disabled. The distributed ConfigSet
path already existed and was self-contained: the runner's
`ConfigSetCommandRunner` takes ZK distributed locks
(`ZkDistributedConfigSetLockFactory`) and calls the same `ConfigSetCmds`
create/delete methods as the handler, with no dependence on distributed
cluster-state updates (ConfigSet operations touch no per-replica state), so a
ConfigSet-only slice does not inherit the Collection API coupling that the
`CollectionsHandler` comment warns about.

## Change (10 production files, 1 test file, 1 changelog fragment; +43/-181)

- Deleted `OverseerConfigSetMessageHandler`.
- `ConfigSetAPIBase` always routes through the distributed runner; the
  overseer-queue branch (`sendToOverseer`, `handleResponse`) is gone.
- `OverseerCollectionConfigSetProcessor` always selects the collection message
  handler; the class name is kept.
- `ZkController` always constructs the distributed runner and records
  `overseerEnabled` behind a new `isOverseerEnabled()` accessor; the
  `overseerConfigSetQueue` field and getter are gone, and
  `waitForPendingTasksToComplete()` now always stops the runner and drains
  the overseer queue only when the Overseer is enabled.
- Collection routing is unchanged in behavior: `CollectionsHandler`,
  `ClusterCommands`, `OverseerStatusCmd`, and `TestRebalanceLeaders` switched
  from the runner's `Optional` presence to `isOverseerEnabled()`.
- `Overseer.getConfigSetQueue` (both overloads) removed; class docs updated.
- New `SolrCloudTestCase` `ConfigSetAlwaysDistributedTest`: on a 1-node
  cluster with the Overseer enabled, asserts the runner is present, then
  creates and deletes a ConfigSet through the API and verifies both through
  list. The structural assertion fails on the old code (runner absent when
  the Overseer is enabled); the create/delete legs guard the behavior.
- Changelog fragment `changelog/unreleased/SOLR-16542.yml` (type changed),
  YAML check rc=0.

## Evidence in this run (not a gate; phase 2 gate is a separate claim)

- Error Prone compile `:solr:core:compileJava :solr:core:compileTestJava
  -Pvalidation.errorprone=true` BUILD SUCCESSFUL in 4m 4s (log
  `~/workspace/tools/16542-compile.log`).
- Root tidy BUILD SUCCESSFUL in 1m 19s, tree clean after except the intended
  change set (log `~/workspace/tools/16542-tidy.log`).
- New test post-tidy, seed 16542C0FFEE16542, BUILD SUCCESSFUL in 1m 48s,
  fresh JUnit XML 2026-11-02T07:26:23Z, 1 test, 0 failures (copy
  `~/workspace/tools/16542-focused2-TEST.xml`; log
  `~/workspace/tools/16542-focused2.log`). A pre-tidy run of the same test
  also passed (XML 2026-11-02T07:22:41Z).
- No pre-fix proof pair exists: this is a removal with a new structural test,
  not a bug fix, so the phase 2 gate should record the new test failing at
  the fix commit's parent (runner absent with the Overseer enabled) and
  passing at the head, same seed, plus the existing ConfigSet API suites
  (`TestConfigSetsAPI`, `TestConfigSetsAPIExclusivity`,
  `OverseerCollectionConfigSetProcessorTest`, `ConfigSetApiLockingTest`) and
  the module check.

## Open points for phase 2 (not settled here)

- The runner is now constructed on every node even with the Overseer enabled;
  its executors create threads lazily and its async tracker writes nothing at
  construction, but the phase 2 gate should watch for any startup cost or
  duplicate-registration surprise in a multi-node run.
- `OverseerCollectionConfigSetProcessor` keeps its ConfigSet name although it
  now handles collection messages only; renaming it was out of the removal's
  scope.
