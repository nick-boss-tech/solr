# Assignment: Flaky test root causes round 1 (four CI flakes from 2026-10-10)

Claim first: add `claims/flaky-tests-root-cause-round-1.md`, then work. Report: `reports/flaky-tests-root-cause-round-1.md`, one section per test, in the order below.

Staffing: run this round with 4 subagents in parallel, one per test, with the lead writing the roll-up and the cross-test pattern section. Hard cap from Nick (2026-10-09): never more than 6 to 7 subagents running at once across ALL assignments combined, on either side; subagents slow each other down past that. Run rounds one after another, not together.

## Why this round exists

On 2026-10-10, four apache/solr CI test failures were classified as flakes: tests the triggering branches do not touch, failing on transport errors, a port collision, and a suite teardown exception collection. The main side's RCA record (kept main-side, quoted in each section below) says what happened in each run. It does not say why the test or the infrastructure allowed it. That is this round's job: for each test, find the mechanism, rank the candidate root causes against the code, and propose the fix or the test hardening that would stop the flake from recurring. A flake whose mechanism is understood stops producing fire drills; one that is only re-run until it passes comes back.

## Boundary: analysis only, no runs on this side

The review side does not run builds or tests (standing rule; Windows builds are revoked). This round is code-and-log analysis: read the failing test, the production and test-framework code it drives, and the failure record quoted below. Every verification run named in this assignment is main-side work. Classification settling runs for tests 1 to 3 (the same class and seed on current main) are already queued on the main side; this round does not wait for them and does not duplicate them. Where a settling result would change the ranking, say so and state which way.

Deliverable shape, per test:

1. A ranked root-cause hypothesis: the most likely mechanism first, each entry with code citations (file and line) for every step of the causal chain.
2. The evidence for and against each hypothesis, from the failure record and the code. Separate what the record proves from what it merely suggests.
3. A proposed fix or test hardening: the exact change described (file, method, what changes and why), not applied. If the honest conclusion is that the test is fine and the defect sits in shared infrastructure, say that and put the fix there instead.
4. The main-side verification run that would settle it: module, test class and method, seed, and the tree it runs on (current main, or main plus the proposed fix), plus what result confirms the hypothesis and what result refutes it.

## Test 1: GCSInstallShardTest, suite teardown collects restore errors

- Source run: workflow run 37987579785, job 114192758248, triggered by the SOLR-3657 branch at head 14edaca577c0c1c38dc50b1ffa632c31fd1613de. That branch changes only `DocumentBuilder.java` and its test in `:solr:core`; nothing in this module.
- Test: `org.apache.solr.gcs.GCSInstallShardTest`, `classMethod` failure (suite teardown), 35 tests, 1 failure, 6 skipped. Module `:solr:modules:gcs-repository`. Test file: `solr/modules/gcs-repository/src/test/org/apache/solr/gcs/GCSInstallShardTest.java`.
- Seed: `B94347D2600CC75D`.
- Failure signature, from the RCA record: `MiniSolrCloudCluster.shutdown` calls `checkForExceptions`, which throws "Error shutting down MiniSolrCloudCluster". The suite output shows restore attempts failing with `IndexNotFoundException: no segments* file found` in `data/restore.<timestamp>` directories, in a suite that deliberately installs an error-throwing backup repository (`ErrorThrowingTrackingBackupRepository` appears in the run log). An exception recorded on a server thread during that error path is what the shutdown check collects.

Questions to answer:

- Which server thread records the restore exception, and through what path does it reach the set `checkForExceptions` reads at shutdown? Cite the recording code and the collection code.
- Is the recorded exception an expected product of the suite's own error injection (a restore the test meant to fail, whose error escaped onto a thread the teardown treats as unexpected), or a restore that should have succeeded? What in the test's setup decides which of the two it is?
- Where does the `IndexNotFoundException` come from: what asks for a segments file in a restore directory that has none, and is that read part of the injected failure or a second, unintended one?
- Does the same collection hazard exist for other MiniSolrCloudCluster suites that inject errors, or is it specific to how this suite installs its repository? Name any other suite that shares the pattern.

## Test 2: RecoveryAfterSoftCommitTest.test, HTTP/2 channel closed mid-request

- Source run: workflow run 37987632639, job 114192700428, triggered by the SOLR-11483 branch at head 4431a250f6650494f8d87bd689a7f45d5b4835ab. That branch changes an UpdateLog default, `TestRecovery`, and a ref-guide page; it does not touch this test or the HTTP transport.
- Test: `org.apache.solr.cloud.RecoveryAfterSoftCommitTest.test`. Module `:solr:core`. Test file: `solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java`.
- Seed: `A57A22E346C237C6`.
- Failure signature, from the RCA record: `client.add` at RecoveryAfterSoftCommitTest.java:106 fails with `SolrServerException: IOException occurred when talking to server`, caused by `java.nio.channels.ClosedChannelException` in Jetty's HTTP/2 session shutdown path. The connection dropped mid-request; no recovery assertion was reached.

Questions to answer:

- What closes the channel at that point in the test: a server-side node restart or shutdown racing the request, the client closing a session it believes is stale, or the HTTP/2 layer aborting a stream on a session error? Trace the client the test uses (`cluster.getSolrClient()` and the HTTP/2 client under it) and the server state at line 106.
- Is there a window in this test where a request is legitimately sent to a node or core that is going away, so a transport failure is an expected race the test should absorb (retry, or wait for the new state before sending), rather than a defect?
- Does the SolrJ HTTP/2 client retry an idempotent-looking failure like this one anywhere, and should an update request be retried at all? Answer from the client code, not from preference.

## Test 3: TestCoordinatorRole.testNRTRestart, same ClosedChannelException shape after a restart

- Source run: workflow run 38009158573, job 114192567483, triggered by the SOLR-11475 branch at head 229947201fd797e57e2d3db1ff2c6552097c9734. That branch changes only `PeerSync` and `PeerSyncTest`.
- Test: `org.apache.solr.search.TestCoordinatorRole.testNRTRestart`. Module `:solr:core`. Test file: `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java`.
- Seed: `681E2A715B2CE1D3`.
- Failure signature, from the RCA record: `client.add` at TestCoordinatorRole.java:306 fails with the same shape as test 2: `SolrServerException` caused by `java.nio.channels.ClosedChannelException` in the HTTP/2 client transport, while talking to the coordinator collection's `/update` endpoint, right after the test's NRT restart step.

Questions to answer:

- Same tracing as test 2, for this test: what is restarting at the moment of the add, which endpoint and node receive it, and what closes the channel.
- The pattern question, answered jointly with test 2 in the report's pattern section: do tests 2 and 3 share one mechanism (for example, a client session to a restarted node being reused for the first post-restart request), or are they two different races that happen to surface as the same exception type? The report must commit to one answer and cite the code that decides it.
- If they share a mechanism, the proposed fix is written once, in the pattern section, and each test's section says whether it also needs a test-side change of its own.

## Test 4: LeaderElectionIntegrationTest.testSimpleSliceLeaderElection, port already in use

- Source run: workflow run 37991927523, job 114192610971, triggered by the SOLR-7504 branch at head 2fe06bfd917f193b3591dfa4c088ef0994279ed6. That branch changes only `CountFieldValuesUpdateProcessorFactory` and its test. The main side classified this one as infrastructure: a port collision on the shared runner. This round's job is to check whether that classification is the whole story.
- Test: `org.apache.solr.cloud.LeaderElectionIntegrationTest.testSimpleSliceLeaderElection`. Module `:solr:core`. Test file: `solr/core/src/test/org/apache/solr/cloud/LeaderElectionIntegrationTest.java`.
- Seed: `3E3D9FF553211ED6`.
- Failure signature, from the RCA record: `java.net.BindException: Address already in use` inside `JettySolrRunner.start` (`retryOnPortBindFailure`) while binding a node port. No assertion in the test was reached.

Questions to answer:

- How does the port get chosen for a runner this test starts: who allocates it, from what range or mechanism, and how long is the gap between allocation and bind? Cite `JettySolrRunner` and the test framework code that feeds it ports (`solr/core/src/java/org/apache/solr/embedded/JettySolrRunner.java` and the MiniSolrCloudCluster and base test classes that call it).
- What does `retryOnPortBindFailure` actually retry, and why did the retry not save this run: does it rebind the same port, pick a new one, or give up after a fixed count?
- Is the collision a race inside one test JVM (two runners in the same suite grabbing the same free port before either binds), a leftover process or socket in TIME_WAIT from an earlier suite on the shared runner, or a cross-suite allocation conflict? State which the code makes possible and which it rules out.
- How widespread is the pattern: how many suites start runners through the same allocation path, and would the proposed fix cover them or only this test?

## Deliverables

1. `reports/flaky-tests-root-cause-round-1.md`: one section per test with the four deliverable parts above, plus a closing pattern section covering the test 2 and test 3 question and the test 4 reach question. Each proposed fix names the main-side verification run that would settle it.
2. Nothing else: no branch edits, no drafts, no test or production code changes. Fixes are described in the report for the main side to apply and verify.

## Standing rules

- No em dashes anywhere in the report. Voice avoid-list: load-bearing, seam, delve, robust, seamless, crucial, leverage (as a verb), "it's important to note", "in terms of", "not just X but Y". Plain concrete wording.
- Keep PR numbers out of commit messages and file names on this branch; refer to the source runs by their workflow run ids, as this assignment does.
- A claim about code behavior needs a citation (file and line) or a quote from the failure record. Mark inferences as inferences.
- Owner decisions, if any surface, go in the report as a short list at the end, with the options stated plainly and a recommendation recorded.
