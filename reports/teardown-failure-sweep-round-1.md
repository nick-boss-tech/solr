# Repo-wide teardown failure census and static sweep, round 1

Status: COMPLETE (static audit; no probe executed)
Claim: `claims/teardown-failure-sweep-round-1-windows-audit.md`
Assignment: `assignments/teardown-failure-sweep-round-1.md`
Host: windows, owner-directed static slice, 2026-10-25 UTC

## Outcome

The sightings are a family of teardown symptoms with several distinct mechanisms.
SOLR-18564 already fixes one proven mechanism: a closeThreadPool timeout skipping
CoreContainer's trailing closes. It does not fix every failed client close, every
executor's cleanup order, or the test framework's cross-suite accounting.

The best next unit is a bounded interrupted Jetty-client close probe, motivated
by two independent selector sightings. Next are factory metrics cleanup after an
executor failure, early CoreContainer executor failure, sequential executor closes,
and interrupted executor waits returning before workers stop. These are candidates, not five
newly proven defects. No speculative source fix is part of this audit.

The DistributedExpand settling assignment remains stood down. None of the probes
below repeats its whole-class branch_9x/main settling pair.

## Inputs, coverage, and counting

- Census snapshot: `a0994a06c10a17ef471cbb0f190ee9400c210abe` on coordination-ui.
  Claim posted at `f7bfb7b2c18a0d12b6231ed44f147f5d7fef8343`. Prepublication
  snapshot `77e6d1dcc0e2c133b8b6132416251ac6033c122f` has no changed assignment,
  competing sweep claim, or report; its other additions concern PR 5224.
- Read retained `reports/`, `receipts/`, `claims/`, `gates/`, and `assignments/`;
  searched `material/` too. Date-folder day scans live under `receipts/`, including
  2026-10-22 and 2026-10-23. There is no root day-scan directory in this snapshot.
  Searches included classMethod, teardown, ThreadLeakError, ObjectReleaseTracker,
  ObjectTracker, unreleased/not released, shutdown errors/timeouts, and leaked
  contexts, harnesses, executors, selectors and threads. Ordinary stack frames
  named TestRuleSetupTeardownChained or ThreadLeakControl are not failure sightings.
- Also read the pre-existing `research/teardown-objecttracker/` evidence packet:
  three full CI logs, provenance, source diagnosis, reproduction-state JSON, and
  class-run logs. They were supplied by previous work; this audit downloaded no
  job logs and ran no tests. The census below carries the essential evidence onto
  the board rather than requiring that local packet to interpret the result.
- Main source snapshot, fetched from upstream: **M =
  `d298cd0b42602247bee7bf9e2bcf302e725f6690`**. 9.x comparison: **N =
  `dae5ac66f9c821d0edb26ca62493a1896c001d3b`**. Fixed SOLR-18564 branch,
  fetched from the fork: **F = `dd65b21b41dc1d008c51031b71f61964ee2ca169`**.
  All code below was read from Git objects, not the dirty source checkout.
- Repo-wide source discovery at M searched `solr/` for close/shutdown declarations
  and shutdownAndAwaitTermination/shutdownNowAndAwaitTermination: 446 matching
  lines. Inspected the lifecycle paths in the pattern map, including production,
  SolrJ, ZooKeeper, and test-framework siblings. This is a targeted static sweep
  of discovered lifecycle risks, not a claim to have verified every close method.
- Recurrence means distinct executions, never repeated stack traces, two failures
  emitted by one suite, or the same event copied into another report. Unknown
  denominators and seeds stay unknown. Negative controls are kept separate from
  spontaneous CI failures. This is a census of the retained evidence, not an
  exhaustive search of every public Solr CI run.

## A. Census

Board links in this table are pinned to the census snapshot. Local log line
numbers are original full-log lines, not excerpt line numbers.

| ID | Class / shape | Source, seed, and recurrence | Current status |
| --- | --- | --- | --- |
| C1 | CloudHttp2SolrClientTest.classMethod: MiniSolrCloudCluster shutdown error, closeThreadPool timeout | PR 5030, Crave job 119565807999, 2026-10-22 UTC. Seed A35B8669BA78E261. One CI execution. `pr5030-job.log` 7103-7111, 11550-11625. | Ticketed mechanism with fix: SOLR-18564 / PR 5192. Original CI incident was not reproduced locally; do not claim an exact-incident proof. |
| C2 | TestJavaBinCodec.classMethod: 16 unreleased SolrMetricsContext objects, created by the preceding cluster's node handlers/plugins | Same PR 5030 execution and seed as C1; one charge, not 16 failures or an independent run. Raw class label at 11789, tracker at 11790, creation stacks 11791-12990. | Recorded cross-suite charge; consistent with the already-fixed SOLR-18564 mechanism. The assignment records seed-determined JVM pairing; the raw log independently establishes the class label and creation ownership. |
| C3 | DistributedQueryComponentCustomSortTest.classMethod: two tracked objects (factory SolrMetricsContext and HttpJettySolrClient), plus one suite-scope selector thread | PR 5202, 2026-10-23 UTC; CI merge c601b1ea825, head 7e0251ee029125152c4d1791660189ef1e9dea06. Seed FFF5A3C3AAC69084. One execution, two class-level diagnostics. Raw `pr5202-job.log` 5992-6141, 6740-6818: interrupted ManagedSelector.doStop, then `httpShardExecutor-311-thread-2` RUNNABLE in EPoll/ManagedSelector. Job number not retained in packet provenance. | Recorded-only; no causal fix proven. A whole-class isolated run at the same seed passed once on the older baseline described below. This is the strongest additional selector sighting. |
| C4 | DistributedExpandComponentTest.classMethod: ThreadLeakError, one idle ManagedSelector in httpShardExecutor; all methods pass | PR 5198, Crave job 121178733450, seed 85F9F77FD74C1D41. One execution. [Parent record](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/assignments/distributed-expand-threadleak-settling-round-1.md). | Recorded-only, explicitly [stood down](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/assignments/distributed-expand-threadleak-settling-round-1-standdown.md). Different class from C3; this does not establish a second occurrence of DistributedExpand itself. |
| C5 | TestTaskManagement.classMethod: cluster shutdown failure with suppressed closeThreadPool timeout | RC3 smoke run 003, 2026-10-24; run seed 5AB00AEB1AE2392 (report-level seed, not independently extracted per class). One in five completed runs; runs 004/007 are incomplete. [Smoke report](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/reports/rc3-smoke-loop-round-1.md). | Ticketed/fixed mechanism, SOLR-18564. Same class pairing is not proven from XML: no JVM id. |
| C6 | TestTermsQParserPlugin.classMethod: 16 unreleased SolrMetricsContext objects | Same RC3 run 003 as C5; one in five completed runs, not five sightings. XML timestamps C5 14:04:05 and C6 14:06:10 UTC. [Mechanism report](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/reports/rc3-termsqparser-metrics-leak-round-1.md). | SOLR-18588 closed as duplicate of SOLR-18564 per that report. Original TermsQ failure was not rerun on the fixed tree. |
| C7 | TestThinCache.testSimple assertion, followed by classMethod tracker charge for parent plus two cache contexts | PR 5128 CI, merge fccb649d48c, head 91500069e48cbdaec20d868cca04c1d042e2fda3; seed 1668E8BD438E6830. One leak execution in the packet. `thincache-job.log` 6248-6456. The receipt's 14 historical testSimple failures are assertion-family sightings, not 14 proven context leaks. | Separate cleanup candidate fixed and gated at 5262d15678d0f21ec1b0a9cfcbf968252a0de5aa; [receipt](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/receipts/SOLR-18506.md). PR 5029's capacity fix is distinct from this try-with-resources cleanup candidate. No pre-fix control for the cleanup candidate. |
| C8 | GCSInstallShardTest.classMethod: Error shutting down MiniSolrCloudCluster; nested restore/index errors | PR 5069, job 114192758248, 2026-10-10; seed B94347D2600CC75D. One characterized execution. [RCA](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/reports/ci-red-prs-rca-round-1.md): log 6385; nested IndexNotFoundException and restore UnsupportedOperationException at 11633-12552. | Recorded-only; distinct restore-path failure, not evidence of the close-pool timeout mechanism. The old settling recommendation is a record, not authorization from this audit. |
| C9 | TestInitParams: replaced embedded harnesses leaked after two mid-class initCore swaps | SOLR-8240 failed gate at 97cd22159b; seed/count of leak failures absent from retained receipt. At least one failed gate, exact incident count unknown. [Receipt](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/receipts/SOLR-8240.md). Its referenced gates/SOLR-8240.md is absent at the census snapshot. | Fixed on ticket branch at 78ecddf01f5c76736d415b9d031095f22aa38b17 by closing swapped harnesses with deleteCore; eight TestInitParams tests pass, no classMethod tracker text. Do not invent missing historical stacks. |
| C10 | test_post.bats teardown_file: stop fails without lsof; then stop timeout/zombie failure after lsof is installed | vm4 PR 5204 phase R at 7eb288a6f5777770d4472fc2c550b66e0d1cec12. At least two distinct failed executions (`phase-r-post-probe`, `phase-r-post-probe-lsof`); no random seed. [Report](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/reports/pr-5204-cli-review-implementation-vm4-round-1.md), [reaper claim](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/claims/pr-5204-phase-r-post-probe-reaper-vm4-round-1.md). | Host environment: non-reaping python PID 1, about 50 zombie entries recorded; entire-command subreaper wrapper yields teardown-clean 12-test run. Picocli wording selection is an unrelated assertion defect. No relationship to Jetty ManagedSelector is established. |
| C11 | test_security_manager.bats allowPaths test: teardown stop returns 1, attach fails, process force-killed | vm2 PR 5225 follow-up; branch 096da6567e3 and unchanged-main baseline 07fdebdda54a; at least two executions, no seed. [Report](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/reports/bats-5225-accurate-assertion-round-1.md). 1s and 30s waits, security-manager on/off variants are mentioned without a full run ledger. | Host-associated, underlying mechanism unclassified. A zombie cause on vm2 is not established by vm4's record. Java 24+ assertion leg is still unproven in this report. |

### Induced failures and adjacent signals, counted separately

| ID | Class / shape | Evidence and status |
| --- | --- | --- |
| I1 | CoreContainerShutdownTest.testShutdownRunsTrailingClosesWhenClosePoolTimesOut plus classMethod metrics charges | RC3 749712b7194 plus regression test from d7caa8cb784: base seed D4DAB631E49B77A8, timeout and 15 contexts (two class-level reports); fixed seed E89FE6C82AB0D707, one pass. One induced base execution, not two new CI incidents. Different seeds mean this is not an RC3 matched pair. [Clean receipt](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/receipts/rc3-vm5-clean-receipt-round-1.md). |
| I2 | LeaderElectionTest negative control: stack overflow, plus teardown ThreadLeakError from runaway elector threads | SOLR-6213 watch-retry re-gate, seed 6213; one explicitly recorded leak execution on unpatched logic, clean head at a55a45e8babf0e235ba32f0e70091f0fe25afee7. [Receipt](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/receipts/SOLR-6213.md). A secondary effect of the deliberate negative control. |
| I3 | SecurityConfHandlerTest.testReaderNeverSeesPartialSecurityJsonDuringEdits: failed test leaves writer thread | SOLR-18010 earlier negative-control revision on main; seed/run count not retained. [Receipt lines 192-198](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/receipts/SOLR-18010.md#L192). Fixed with finally join at 7f1389ed10 before gated PR head 691eba654c. |
| A1 | Production core shutdown hangs reported by Jason Gerlowski, SOLR-18568 and SOLR-18244 | [2026-10-22 day scan](https://github.com/nick-boss-tech/solr/blob/a0994a06c10a17ef471cbb0f190ee9400c210abe/receipts/2026-10-22/receipt.md#L47): reporter explicitly says different thread-hang paths with the same end result. No class, seed, CI count, or shared mechanism established. An adjacent signal, not a test failure census row. |

Do not merge C1/C2 with C5/C6 into four independent reproductions. They are two
executions with shutdown/metrics co-occurrence; only the PR 5030 assignment records
the same-JVM attribution, while the RC3 XML lacks the JVM identifier. C3/C4 are
two independent selector-thread sightings across different classes and branches,
not proof that the same race occurred twice.

### Historical non-reproduction and evidence integrity

The local packet's `reproduction-state.json` pins isolated whole-class runs to
498a09aea97aac86c2cc7ac114113b946d249942 (product-equivalent ancestor
e044bf20b405b8ca985730be3553d77d1fc8ff31; fork-only workflow file differs):
CustomSort seed FFF5A3C3AAC69084, 1/0/0; CloudHttp2 seed A35B8669BA78E261,
20/0/0; ThinCache seed 1668E8BD438E6830, 2/0/0 (tests/failures/errors), all
2026-10-23. These are single isolated class runs, not exact CI merge trees, a
same-worker class pair, or proof of full-task non-reproduction. The assignment
mentions additional full-suite attempts; their separate artifacts are not in
this packet and no new count is assigned to them.

The raw TestJavaBinCodec label corrects the packet's ci-findings.md grouping of
the PR 5030 tracker charge under CloudHttp2. Original tracker allocation stacks
still establish the container owners. Historical packet text is supporting
evidence, not authority over raw class labels or current code.

Original full-log SHA-256 values (from packet provenance, retained for retrieval):

- `pr5030-job.log`, 2,705,057 bytes:
  `1f3d264cea8858d5322c566b6fdfc5a14ca96a8c1261403bd8b715c26c794a24`.
- `pr5202-job.log`, 966,485 bytes:
  `965987429dab7f4d1a8f7710b0b09d461d19f61304bfa3f21d0fb7272f493c93`.
- `thincache-job.log`, 902,943 bytes:
  `b87839de5ff9368ab58ee5aae61918c96022ca3d3f765ecad80b2c210a15d099`.

## B. Pattern map with source citations

Every finding below identifies the exact source snapshot and the observation it
supports. Incident attribution beyond those observations remains a hypothesis.

### P1. Jetty close can leave a tracked client after partial cleanup

At M, [HttpJettySolrClient.java:347-370](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L347)
waits for outstanding requests, stops/destroys HttpClient, stops the scanner,
wraps stop exceptions, and shuts its owned executor in finally. Tracker release
comes after the finally and is skipped when stop or executor termination throws.
An exception from stop also skips destroy/scanner stop. C3 shows the corresponding
interrupted ManagedSelector close stack and retained client. This identifies a
reachable incomplete-close path; whether a live resource survives needs a probe.

At N, [Http2SolrClient.java:365-388](https://github.com/apache/solr/blob/dae5ac66f9c821d0edb26ca62493a1896c001d3b/solr/solrj/src/java/org/apache/solr/client/solrj/impl/Http2SolrClient.java#L365)
has the same stop/exception/release ordering. That makes the 9.x selector sighting
relevant without assuming identical dependency versions or incident causes.
Git line history traces this close shape through 9f21a71bbd8 (keystore scanner)
and 96ddf2c4c7c (Jetty 10); this is not newly introduced by the unrelated PR 5202
routing patch.

Important correction to a tempting hypothesis: at M,
[AsyncTracker.waitForComplete:949-960](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L949)
catches InterruptedException, terminates the phaser and restores the flag. It
does not rethrow that interruption. A still-set interrupt can reach Jetty stop;
do not describe interruption of this wait as itself bypassing close.

### P2. Shard-factory metrics cleanup is outside its protective finally blocks

At M, [HttpShardHandlerFactory.java:345-364](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java#L345)
attempts load-balancer close, quiet default-client close, and commExecutor
termination using nested finally blocks. Gauge close and producer metrics close
are later statements. A propagating load-balancer exception or commExecutor
timeout skips them. Quieting a client exception does not release its tracker entry.
[Lines 460-465](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java#L460)
create the factory's child context and instrument its executor. C3's two retained
identities match these owners, but the exact branch skipping the metrics close
was not traced dynamically. SOLR-18564 does not edit this close method.

### P3. Early executor failures and other escaping exceptions can still skip cleanup

At M, [CoreContainer.java:1230-1237](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L1230)
waits for three executors before setting isShutDown and entering its guarded
shutdown body. Failure here bypasses all later closes. Within the body an
escaping exception still runs the finally but then bypasses the tail. At F,
[lines 1230-1237](https://github.com/nick-boss-tech/solr/blob/dd65b21b41dc1d008c51031b71f61964ee2ca169/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L1230)
are unchanged in this respect: the SOLR-18564 fix catches only the later
[closeThreadPool wait at 1315-1325](https://github.com/nick-boss-tech/solr/blob/dd65b21b41dc1d008c51031b71f61964ee2ca169/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L1315).
It must not be described as a general guarantee that every shutdown exception
allows all remaining closes. C1/C5 motivate the family, not this particular early path.

The same early pattern exists in [SolrCore.doClose:1739-1757](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/core/SolrCore.java#L1739):
coreAsyncTaskExecutor termination precedes metrics and component cleanup. In
contrast, [searcherExecutor termination at 1812-1820](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/core/SolrCore.java#L1812)
catches ordinary failures and continues. The distinction is visible source
control flow, not evidence that either executor timed out in the census.

At M, [CoreContainerProvider.java:112-133](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java#L112)
nulls its container reference before calling shutdown. A later provider close
cannot retry that container through the same field if shutdown throws.

### P4. Sequential executor closes leave later executors untouched after failure

At M, [UpdateShardHandler.java:224-239](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/update/UpdateShardHandler.java#L224)
closes updateExecutor and then recoveryExecutor in one try. Failure in the first
skips the second. Finally still attempts metrics and both clients, so this is a
specific executor gap, not a claim that all its cleanup is skipped.

Source siblings with the same sequential-wait risk are
[CoreAdminHandler.TaskProcessor.shutdown:416-419](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java#L416)
(standard then expensive executor),
[CollectionPropertiesZkStateReader.close:149-154](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/CollectionPropertiesZkStateReader.java#L149)
(cache cleaner then notification executor), and
[HttpJdkSolrClient.close:544-554](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpJdkSolrClient.java#L544)
(main executor then request-body executor, then tracker release). No census row
identifies one of these executors as the live leak owner. Keep them as static
siblings, not additional incident counts. Last-file history for UpdateShardHandler
is 553da8f55e11189e1fe0bdb0668b93014d702ce6 (metrics listener wrapper change);
that history check does not establish when this close-order gap first appeared.

### P5. Executor termination returning does not always mean threads terminated

At M, [ExecutorUtil.java:139-157](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj/src/java/org/apache/solr/common/util/ExecutorUtil.java#L139)
waits 60 seconds, calls shutdownNow, waits another 60 seconds, then throws if
still running. On InterruptedException it calls shutdownNow and restores the
flag, returning without a further join. This creates a route for a caller to
continue while a worker is still completing. It is not evidence that increasing
the test framework's linger would fix the underlying lifecycle problem.
The existing [ExecutorUtilTest:48-81](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj/src/test/org/apache/solr/common/util/ExecutorUtilTest.java#L48)
already uses the package-private short-timeout overload to test a noninterruptible
worker and cleans it up in finally. Do not spend two minutes proving that helper
timeout again when a fake executor can decide a caller's control-flow question.

### P6. Cluster shutdown collects worker exceptions, but interrupted submission/wait has a gap

At M, [MiniSolrCloudCluster.java:615-645](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java#L615)
quietly closes clients, snapshots shutdown callables, clears jettys, creates
executorCloser, invokes all stops, then terminates that executor. There is no
finally protecting executorCloser if invokeAll throws InterruptedException.
ZooKeeper shutdown is in an outer finally. Clearing jettys first also removes
the cluster's normal list of retry targets; a probe must retain its own runners.

[checkForExceptions:718-735](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java#L718)
aggregates worker causes as suppressed exceptions. Thus C8's generic classMethod
message is insufficient to infer a timeout; its suppressed restore exceptions
matter. `close()` [at 604-611](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java#L604)
logs shutdown exceptions, while direct shutdown propagates them.
[SolrCloudTestCase:157-164](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/cloud/SolrCloudTestCase.java#L157)
always nulls the static cluster after shutdown, including on failure.

### P7. Test-harness cleanup can hide a close failure or lose an old fixture

At M, [BaseDistributedSearchTestCase.destroyServers:342-377](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/BaseDistributedSearchTestCase.java#L342)
quietly closes clients and logs Jetty stop exceptions inside concurrent tasks.
Only pool termination is awaited; worker stop errors are not collected through
Futures here. A pool timeout skips list clearing. This is a route from a passing
method to teardown diagnostics such as C3, not proof the assertion caused the leak.

[JettySolrRunner.stop:626-661](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java#L626)
catches server.stop TimeoutException, then performs pool/reserved-executor joins;
other stop exceptions can bypass the later cleanup. Its server.join loop ignores
interruption, whereas the earlier qtp.join call is not enclosed by that catch.
No exact Jetty-internal defect is asserted without a dependency-source probe.

[SolrTestCaseJ4.teardownTestCases:283-317](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/SolrTestCaseJ4.java#L283)
catches an Exception from deleteCore with a generic log, then closes testExecutor;
the outer finally resets some globals. [deleteCore:736-764](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/SolrTestCaseJ4.java#L736)
nulls h only after h.close returns. A failure can therefore leave the fixture
reference live. C9 separately records an already-fixed lost-fixture problem from
replacing harnesses; it is not evidence that every initCore call leaks.

### P8. Tracker ownership explains later innocent-class charges

At M, [ObjectReleaseTracker.java:37-60](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj/src/java/org/apache/solr/common/util/ObjectReleaseTracker.java#L37)
uses a JVM-global static map with creation traces and no suite owner key.
[SolrTestCase's rule:95-109](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/test-framework/src/java/org/apache/solr/SolrTestCase.java#L95)
checks/clears it after a successful suite; on errors it calls tryClose instead.
[tryClose:117-131](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/solrj/src/java/org/apache/solr/common/util/ObjectReleaseTracker.java#L117)
handles java.io.Closeable and ExecutorService, not arbitrary AutoCloseable.
[SolrMetricsContext:53-67](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/metrics/SolrMetricsContext.java#L53)
implements AutoCloseable and tracks itself in assertions. Consequently a failed
suite's metrics contexts need not be closed by the failure fallback, and the next
successful suite can be charged. C2 demonstrates the misattribution risk; C6 is
consistent but its same-JVM relationship remains unproven.

[SolrMetricsContext.getChildContext:80-94](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/metrics/SolrMetricsContext.java#L80)
creates a separate context; it does not register it for parent close merely by
creating it. Registering closeables is explicit. [close:284-287](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/core/src/java/org/apache/solr/metrics/SolrMetricsContext.java#L284)
releases tracking before quietly closing its registered closeables. A tracker
charge proves that release was not reached; a cleared tracker does not prove all
threads or underlying resources terminated. Creation ownership, close outcome,
and live thread state must be recorded separately. Do not fix attribution by
clearing the tracker earlier or weakening leak checks.

### P9. BATS process teardown is a distinct failure plane

At M, [test_post.bats:25-27](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/packaging/test/test_post.bats#L25)
calls solr stop --all from teardown_file. The launcher
[stop_solr:529-579](https://github.com/apache/solr/blob/d298cd0b42602247bee7bf9e2bcf302e725f6690/solr/bin/solr#L529)
checks process state, attempts diagnostics/kill for live processes, and explicitly
returns 1 for a remaining Z zombie entry. Current M already recognizes zombies;
do not infer that it blindly treats every Z as a live JVM from an older vm4 log.
C10's full-run subreaper is a demonstrated environment remedy on that host. C11
still needs its own process-state evidence before borrowing that diagnosis.

## C. Ranked candidates and exact deciding probes

Ranking uses observed recurrence first, then cross-suite contamination potential,
then the cost of a deterministic discriminator. No numeric incident frequency is
assigned where the source provides no denominator. A failed injected probe proves
its injected boundary, not that it reproduced the original CI race.

All probes below are **specified, not executed**. Each proposed new method or
class is scaffold work for a subsequent claim; none is an existing test filter
ready to run unchanged. Source input is pinned below, and the probe claimant must
record the scaffold's commit or complete diff and SHA-256 before executing it.
No production modification, new hook, dependency upgrade, seed loop or full suite
is needed to decide the proposed boundary. Linux JDK 21.0.12.1+1, security manager
on, ISO-8859-1, one test JVM, one host lock and at most two Gradle workers. Stop at
the stated cap and record unresolved results without an automatic retry.

| Rank | Candidate and why it earns the slot | Proposed probe, exact tree/class/seed/run/cap |
| --- | --- | --- |
| 1 | P1 interrupted Jetty close, with P5 interrupt propagation. C3/C4 provide two independent selector sightings, and C3 includes the stop interruption and retained client. High cross-suite potential; strongest direct resource evidence. | M plus test-only methods in existing `org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest`: `testCloseProbeWithoutInterrupt`, `testCloseProbeWithInterrupt`. Seed FFF5A3C3AAC69084. One `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test` invocation selecting only those two methods. Two fresh clients, otherwise identical setup. Each gets a dedicated close thread; interrupt that thread immediately before close only in the second arm. Record exception/cause, interrupt status, tracked client identity, HttpClient lifecycle state and selector/executor thread snapshots before and after explicit cleanup. Capture evidence before finally cleanup; join/release every fixture on both outcomes. Cap: 30s per close arm including bounded observation, 20min total including compile. A clean pre-interrupted close is a valid negative result; do not escalate into a DistributedExpand or whole-class seed chase. |
| 2 | P2 factory metrics/gauge tail skipped by a close failure. C3 directly names the factory child context, though the exact escaping branch is unproven. Low-cost control-flow discriminator, distinct from root container metrics fix. | M plus new `org.apache.solr.handler.component.HttpShardHandlerFactoryCloseProbeTest.testMetricsAfterExecutorTimeout`. Seed FFF5A3C3AAC69084. One core cleanTest/test invocation, only this method. Initialize a real factory and its metrics against a real parent context; save its commExecutor, then a test subclass replaces it with an executor whose awaitTermination returns false immediately twice and whose shutdown calls are counted. Control uses a terminating executor. Record whether close throws, whether factory child identity remains tracked, and whether a sentinel callback registered with that child was closed. Restore and close all real clients/executors/context resources in finally. No real stuck thread and no 120s wait. Cap: 5s per arm, 15min total with compile. Do not claim the artificial executor reproduces Jetty scheduling. |
| 3 | P3 early CoreContainer executor failure persists even on the SOLR-18564 fix branch. C1/C5 motivate cleanup completeness; no natural sighting has been assigned to this early executor. Highest potential blast radius, slightly more fixture setup. | F plus new method `org.apache.solr.core.CoreContainerShutdownTest.testEarlyExecutorTimeoutProbe`. Seed 18564ABC0001. Select this method only, not the existing two-minute timeout method. Load the existing standalone fixture; save private non-final coreContainerAsyncTaskExecutor and replace it test-side by reflection with the immediate-false-await executor from rank 2. Record thrown timeout, close-tracking handler callback, root metrics identity and isShutDown. Control has normal termination. In finally restore original executor and perform real shutdown while the fixture remains available. No production hook. Cap: 10s per arm, 15min total. This isolates a sibling on the fixed tree rather than redoing the already-proven late closeThreadPool timeout. |
| 4 | P4 UpdateShardHandler skips recoveryExecutor after updateExecutor failure; CoreAdmin, ZooKeeper and JDK-client source siblings increase breadth but not recurrence. No incident-specific ownership proof. | M plus new `org.apache.solr.update.UpdateShardHandlerCloseProbeTest.testSecondExecutorAttemptedAfterFirstTimeout`. Seed 18564ABC0001. One core cleanTest/test invocation selecting this method. Save the two mutable executor fields, replace updateExecutor with immediate-false-await executor and recoveryExecutor with a counting delegating wrapper; call close, capture whether recovery.shutdown was called, then restore and close originals in finally. A terminating first-executor control must call the second. Record metrics/client closes separately because they already run in finally. Cap: 5s per arm, 15min total. Do not broaden to all four sibling classes in the same claim. |
| 5 | P5 an interrupted termination wait can return before a worker exits. This is a common bridge from close interruption to suite-boundary thread checks; none of the census logs alone proves this specific handoff. Cheapest discriminator and no cluster fixture. | M plus new method `org.apache.solr.common.util.ExecutorUtilTest.testInterruptedTerminationWaitProbe`. Seed FFF5A3C3AAC69084. One solrj cleanTest/test invocation selecting only this method. Start one executor worker blocked on a release latch; it records interruption but remains blocked until explicitly released. On a dedicated closer thread, set the interrupt flag then call shutdownAndAwaitTermination on that executor. Capture return, restored interrupt flag, shutdown state, isTerminated and worker-exited latch before releasing it. Control releases the worker first and closes without interruption. Finally release the worker and join/terminate every thread. Cap: 5s per arm, 10min including compile. This tests a documented return boundary, not a new bug verdict or an instruction to change interrupt policy. |

Each invocation uses `--no-daemon --max-workers=2`, the indicated module's
`cleanTest` and `test`, `--tests` filters exactly as named, and
`-Ptests.jvms=1 -Ptests.seed=<above> -Ptests.useSecurityManager=true
-Ptests.file.encoding=ISO-8859-1`. A clean compile failure before the methods
execute is an environment/scaffold result, not a negative result for the premise.
Archive command, source tree, scaffold hash, JDK, stdout, fresh XML and explicit
cleanup state. Assertions should use captured state after finally cleanup so an
expected observation does not itself leak a fixture into the next suite.

### Proposed next units, one claim each

1. **Linux client-interrupt discriminator**: rank 1 only; report
   `reports/teardown-client-interrupt-probe-round-1.md`. This is the recommended
   first execution unit under the owner's direction to hand Linux testing back
   after this audit. Its separate assignment carries the complete scope and cap.
2. **Factory-close boundary discriminator**: rank 2 only; report factory context,
   callbacks, executor counts, and controls. No container regression re-run.
3. **Early container-close discriminator**: rank 3 only on F; preserve original
   SOLR-18564 gate and report a sibling result separately.
4. **Second-executor discriminator**: rank 4 only; expand to the other named
   siblings only if the owner selects another unit after seeing this result.
5. **Interrupted executor-wait discriminator**: rank 5 only; record worker state
   at return and prove it is cleaned up after releasing the latch.
6. **Records-only attribution aid**, if requested: annotate future CI evidence
   with JVM identity, allocation owner and preceding suite; P8 already explains
   the static accounting behavior. No tracker suppression or clearing change.

P6's interrupted cluster-closer path is a lower-priority follow-up. Its closer
executor is a local variable, so a probe must first design bounded containment
and cleanup (for example an isolated child JVM with retained runner references).
No executable unit for that path is posted here: waiting for the cached pool's
idle expiry or relying on the framework to kill the leaked thread would violate
the cheap, controlled-probe requirement.

No automatic repro loop follows a clean rank-1 run. No fix branch follows a failed
probe without classifying whether it is a live resource leak, skipped bookkeeping,
or both. BATS host preparation stays with the existing vm4/packaging work; do not
queue a broad packaging run as a consequence of this report. The existing RC3
different-seed experiment is not upgraded to a matched proof pair by this audit.

## Completion and limits

Phases A-C are delivered by this report. No source checkout changed, no Gradle,
test queue, reproduction, fix, PR/Jira text, comment or upstream write occurred.
Existing receipt results are attributed to their hosts and pinned heads. Static
control flow and historical passing classes are not fresh Linux validation.
The owner authorized this Windows audit after the initial host-mismatch refusal;
that refusal is superseded by the claim, not deleted or rewritten.
