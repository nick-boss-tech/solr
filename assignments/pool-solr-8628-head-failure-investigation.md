# Pool assignment: SOLR-8628 head-leg failure investigation

Status: DONE

Capability tags: `review`. Intended host: windows for the reading; any confirmation run is spec'd for a Linux host, not run by the claimant. Staffing: 1. Claim path: claims/pool-solr-8628-head-failure-investigation.md.

## What happened

Branch `solr-8628-submit` at head `ce8211e05e062444e7afc1c5ea33fb6eff581d8f`. The branch changes `SolrCore.initIndex` in solr/core/src/java/org/apache/solr/core/SolrCore.java (branch lines 856-874): a directory holding only `write.lock` counts as no index, via the new `containsOnlyWriteLock` helper, so a core creates a fresh index over it. The scenario test is `testCreateCoreOverIndexDirWithOnlyWriteLock` in solr/core/src/test/org/apache/solr/core/TestCoreContainer.java (`org.apache.solr.core.TestCoreContainer` in `:solr:core`): it creates `core1/data/index` holding only a `write.lock` file, creates the core, checks the searcher reads 0 documents, then asserts at line 134 that a `segments_` file now exists in the index directory ("a new index must have been created").

The premise run in `gates/SOLR-8628-premise.md` finished with the premise not holding at head, recorded NO GATE by finding:

- Parts 1 and 2 (vm3): the write.lock-only state is reachable; an abrupt halt on Lucene 10.4.0 leaves exactly `write.lock`. An empty directory already works on main and is out of scope.
- Part 3 merge-base leg (vm3): production SolrCore.java at merge-base `cabedd1d968059215188f4e7563fb303241899ed`, branch test retained, seed `8628C0FFEE8628A1`: 1 test, 1 failure, the expected AssertionError `a new index must have been created` at TestCoreContainer.java:134. Recorded in `gates/SOLR-8628-premise.md`; the base JUnit XML sits in `/workspace/gates/logs/vm3/8628-premise-base-proof-junit.xml` on vm3, which is down, so work from the recorded summary for the base leg.
- Part 3 branch-head leg (vm1, 2026-10-11T15:10-15:12Z): the same selector at the branch head, same seed: rc=1, 1 test, 1 failure, 0 errors, 0 skipped. The failure is the identical AssertionError `a new index must have been created` at TestCoreContainer.java:134. Log: `~/workspace/tools/g8628-premise.log` on vm1. Fresh JUnit XML: `~/workspace/solr-worktrees/solr-8628-premise/solr/core/build/test-results/test/TEST-org.apache.solr.core.TestCoreContainer.xml` on vm1.

Both legs reached line 134, so on both trees the core was created and the 0-document searcher check before it passed. What never appeared, on either tree, is a `segments_` file in the index directory.

## Question

Classify the head-leg failure as exactly one of:

- (a) The same premise failure: the branch's fix does not cover the path this scenario exercises. Say which path the scenario actually takes through core creation, and why the `initIndex` change does not change the outcome on it, with file and line citations on the branch tree.
- (b) A different failure the fix introduces: the head takes a different path than the base, and the fix's own path is what leaves the assertion unsatisfied. Show the divergence point and the mechanism.
- (c) A test-shape problem: the branch behaves as intended, but the scenario test does not exercise or does not observe what it names. Say what the test actually sets up and observes, and what it would have to do to observe the behavior the branch implements.

## Required reading

Read on the branch tree at `ce8211e05e062444e7afc1c5ea33fb6eff581d8f` unless stated otherwise:

1. The full head-leg failure: the log and the fresh JUnit XML named above (failure class, message, complete stack), and the recorded base-leg result in `gates/SOLR-8628-premise.md`. State precisely what is identical and what differs between the two legs.
2. The scenario test end to end: what `init(home, CONFIGSETS_SOLR_XML)` builds, where the `write.lock` file is placed relative to the data directory the created core actually uses, the order of the checks, and what each check can and cannot observe.
3. `SolrCore.initIndex` and its whole create path: `containsOnlyWriteLock`, `getNewIndexDir`, `DirectoryFactory.exists` and `get` semantics for a directory holding one file, what the "create the index" branch does after the debug log line, and at what point, if ever, a `segments_` file is first written to disk when a core is created over no index (IndexWriter open, first commit, searcher open: which of these writes it).
4. The core creation path from `CoreContainer.create` to the `SolrCore` constructor: which directory the core's index lives in, whether anything creates or commits an index before `initIndex` runs, and whether the searcher the test obtains was opened over the on-disk directory the assertion lists.
5. The merge-base version of the same path: how the base tree reaches line 134 with the same message, so the classification rests on a real comparison of the two executions, not on the shared message alone.

## Deliverable

`reports/solr-8628-head-failure-investigation.md`, with:

- The classification first: (a), (b), or (c), in one sentence.
- The evidence: the two failure records compared, and the traced path on the branch tree with file and line citations for every step the classification depends on.
- For (a) or (b): the smallest branch change that would make the scenario pass, spec'd precisely (file, location, behavior change) but not implemented, plus the exact confirmation run that would prove it (selector `:solr:core:test --tests org.apache.solr.core.TestCoreContainer.testCreateCoreOverIndexDirWithOnlyWriteLock`, seed `8628C0FFEE8628A1`, expected result at merge-base and at the changed head), marked as a run spec for a Linux host.
- For (c): the corrected test shape (setup, the action that triggers the observable, and the assertion that matches the branch's implemented behavior), and the same confirmation run spec against the corrected test.
- A short list of paths checked that did not change the classification.

## Limits

This assignment is read-only apart from the claim and the report. Do not edit the branch, implement the change, write the corrected test into the branch, run a build or test, or post a gate job. The main side decides what happens to the branch after the report lands. Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
