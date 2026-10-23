# Release-path review round 3

Status: source review complete; repaired test IMPLEMENTED, PROOF PENDING. No builds or tests ran on Windows. This reviewer authored the round-2 candidate, so this is a correction and investigation, not independent approval.

Parent: `assignments/pool-teardown-release-path-review-round-3.md`. Claim: `claims/pool-windows-teardown-release-path-review-round-3.md`. Related: SOLR-18564 / PR 5192, SOLR-18578 / PR 5213, and `fix-interrupted-http-client-close`.

## 1. What the failed proof establishes

The failed proof is not green. The posted vm4 record says factory-timeout command 1 passed at `ee1eb2474f6650f9c8f8a51c6729383496a4256e`; command 2 passed its method body but failed its class tracker check with three clients and one provider context. Remaining commands and fail-before never ran. Tidy changed no files. This review uses that posted record; original vm4 stdout/XML were not published on coordination and were not accessible from Windows. Do not invent object identities or completion times from the summary.

There is a definite test synchronization defect. The test interrupts the thread awaiting the container's close pool and deliberately allows shutdown to return while its worker closes are incomplete. It then joins only its synthetic factory wrapper and the test's outer runner. Neither join proves completion of the update handler or default client provider, which run on different container close workers. The final tracker check therefore races those owners. The round-2 report omitted this ownership branch; that omission is corrected here.

The provider is not owned by `UpdateShardHandler`. Both are separate fields of `CoreContainer`, initialized separately at Apache 79b23df58d (`CoreContainer.java:795-798`) and closed separately (`:1304-1308`). Source ownership:

| Tracked resource / test fixture | Created by | Intended close / test synchronization |
| --- | --- | --- |
| Container root context and normal handler/plugin contexts | `CoreContainer.load()` and handler initialization | Shutdown tail closes handlers, plugins and root. The marker authorization plugin and handler assertions observe this tail; parent close does not recursively own every child. |
| Factory default Jetty client, load balancer, factory metrics child and communication executor | Real `HttpShardHandlerFactory.init/initializeMetrics` during load | Synthetic `BlockingFactory` holds then delegates to the real factory. Its finished latch proves only this delegate close returned. The balancer is tracker-registered too; an alive-check executor is conditional on use. |
| Update-only and recovery-only Jetty clients, update metrics child, update/recovery executors | `UpdateShardHandler` constructor and initializeMetrics | Separate update-handler close task. Close awaits its executors, then in finally closes the metrics context and quietly closes both clients. Its metrics release happens before its two client closes. |
| Third Jetty client and provider metrics child | `HttpSolrClientProvider` constructor and initializeMetrics | Separate provider close task. It quietly closes its client, then its instrumented listener, whose producer close closes the provider context. Ordinary client-close exceptions are caught by IOUtils, so they do not by themselves skip the context close. A stuck client close postpones it. |
| Added marker handler and its child context | `addHandler()` | Registered with the container handler bag and closed by the tail. |
| Marker authorization plugin | Test security.json through the normal plugin loader | Closed by the container tail; marker itself is not tracker-registered. |
| Blocking wrapper, three latches and failure reference | `replaceFactory()` | Wrapper adds no client/context; release latch opens and finished latch joins its delegate. |
| Outer runner executor, returned latch, thread/failure references and interrupt marker | Interrupted-wait test | Runner invokes container shutdown; returned latch and runner shutdown join the caller, not the container's internal close pool. |

One container is loaded, no cores or requests are created, and the test directly constructs no extra Jetty clients. Three remaining clients match the two update clients plus the provider client. This is an ownership/count match, not verified per-object attribution of vm4's original four entries: those creation stacks remain needed. The source does not demonstrate a missing provider close statement. It demonstrates that this test failed to wait for all the closures it disrupted. Whether the original entries were still progressing, never started, or escaped via a different error cannot be settled from the posted summary.

### Test-side correction only

Scratch branch `solr-18564-release-path-r3-test` at `68fd05e53002f8c0a1fbff65a5623b5fea96028d` is pushed and verified. Its parent is the original candidate ee1eb2474f. Exactly one file changes: `solr/core/src/test/org/apache/solr/core/CoreContainerReleasePathTest.java`. No production or tracker code changes.

The corrected interrupted-wait method captures the three sibling clients and update metrics context through their owners' getters, and the newly created provider context from its tracked owner label. It prints all five identities, waits up to 30 seconds for their tracker releases before injecting the interrupt, and then performs the existing factory/parent assertions. This makes the factory task the deliberately outstanding owner at the injection boundary. A final explicit tracker assertion runs in the body before failure fallback. No object is manually untracked, no global tracker clear is added, and no sibling owner is force-closed by the test.

This precondition isolates the intended experiment; it does not establish a fix for simultaneous interruption of the sibling owners or prove successful socket/thread termination. A precondition timeout is a real failure to capture and hand back, not something to silence. The repaired test has not been compiled or run on this host. Continuation: `assignments/pool-teardown-release-path-round-3-test-proof.md`.

## 2. PR 5213: keep separate, do not close on the hiding-evidence premise

Read live PR state: OPEN at `35faf539e94e613d5f5528c71962e1970786bc93`, with only tracker production code and `TestObjectReleaseTracker` changed. `SolrTestCase` is unchanged at this head. The production delta adds an AutoCloseable branch after the existing Closeable and executor branches of `ObjectReleaseTracker.tryClose()`.

The claim that this would turn the reported tracker-check failure green is not supported by the control flow. `SolrTestCase.java:98-108` runs `clearObjectTrackerAndCheckEmpty()` on the successful-body path, stores its diagnostic string, and asserts it is null. The tracker method captures the report and clears the map before returning. When that assertion fails, failure cleanup cannot undo the stored diagnostic or the assertion, and the original tracked entries have already been cleared. This applies to the reported class-check failure.

When an earlier body/setup failure exists, the successful-class check is skipped and afterAlways invokes tryClose. AutoCloseable cleanup can then reduce leftover resources or later secondary leak reports. That is failure-recovery hygiene; the original failure remains a failure. A proof that only inspects state after fallback could misattribute the cleanup, which is why both proof trees exclude it and the repaired test asserts before fallback.

Recommendation: keep PR 5213 separate as test-failure cleanup. Do not close it merely because it supposedly fixes or conceals this class-check failure; neither claim is established. This is not a recommendation to merge without its own gate/review. It is not a production teardown fix and supplies no readiness evidence for either release-path candidate. No PR was commented on, edited, closed or merged.

## 3. Nested wait: partial experiment, historical CI cause still unknown

The source and elapsed-time alignment still permit nested cancellation: outer distributed-test close pool -> Jetty/container worker -> inner container close pool -> factory worker -> communication executor. An interrupt in ExecutorUtil's await handler returns without joining the interrupted pool. vm4's method-body observations support the synthetic early-return behavior, but its class check failed, and the original CI selector stall was not reproduced.

The original GitHub run `39903819776` is completed with conclusion failure. Its artifact API reports zero artifacts. The archived log contains the selector thread-leak stack and client stop exception; it has no contemporaneous post-exception factory stack. A completed Actions worker cannot now be attached to for a new live stack capture. Therefore a future focused shard-server-stop reproduction with stack capture at the blocked close and interrupt boundaries is needed for a historical-cause claim. No full suite or CI rerun was started here.

## 4. Baselines and the four full-core failures

The before branch f92d771512 starts from the assignment's explicit Apache `79b23df58daac14b92cc35f23b8b81f72098b4ba`, the recorded CustomSort PR base. Round 1 used fork `498a09aea97aac86c2cc7ac114113b946d249942`, whose product ancestor is older. Those runs are not fail-before evidence for the candidate. Neither is an exact execution of the CI merge revision `c601b1ea825`.

The four reported full-core failures concern `TestTlogReplica.testRecovery`, `TestTlogReplica.testKillLeader`, `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure`, and `TestCoordinatorRole.testNRTRestart`. Their class names do not occur in the three archived incident job logs. A bounded scan selected the 10 latest failed `Solr Tests via Crave` runs. Five failed-job logs were readable: 39903819776, 39882574428, 39745575206, 39699700526 and 39669769551. None contains those class names. Run 39629219125's log was unavailable (`log not found`), so the scan stopped and the remaining four selected runs were not inspected. Run 39882574428 failed in core test compilation, so its negative result says nothing about runtime behavior. Provenance and hashes: `material/teardown-release-path-r3-ci-scan.json`.

Absence from selected failed-job logs does not prove those tests passed or even ran, including in other jobs of the same workflow. Whether those four failures occur elsewhere in CI remains unestablished. A different failure list across refs, seeds and suite partitions does not by itself diagnose an environment difference.

## 5. Candidate provenance and limits

The client deltas in original f0adb05094 and cherry-pick ee1eb2474f have identical stable patch ID `9cb522a5da8cfcd6c3e7f4457d525ca070b0eea5`, including the regression test. The base difference is upstream abortable-request support, retained by the cherry-pick, not an extra client change introduced during integration. Test files at before f92d771512 and candidate ee1eb2474f are identical.

| Production file changed relative to before | Behavior added | Why a CI leak report can remain |
| --- | --- | --- |
| `solr/core/src/java/org/apache/solr/core/CoreContainer.java` | Catch/log close-pool RuntimeException and continue the shutdown tail | Does not join unfinished worker closes on interruption, remove initial stalls, or close every independently owned child. Other earlier/tail errors can still interrupt cleanup. |
| `solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java` | Gauge/context cleanup in outer finally | Runs only after the preceding close operations return or throw. A still-blocked executor/client postpones this finally. Gauge Error can also interrupt this sequence. |
| `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` | Destroy attempted after stop failure; tracker release attempted after owned-executor shutdown | Async completion wait remains before the protected block and can stall; the TLS scanner stop is still skipped when HTTP stop/destroy throws. Tracker release alone does not establish thread/socket termination. An exception from destroy can replace the stop exception. |

The inherited interrupted-close regression expects a RuntimeException and relies on the class tracker check for cleanup verification. It does not check the specific InterruptedException cause, scanner teardown, in-flight/shared-executor behavior, or every cleanup boundary. Its scope is an interrupted-stop release regression, not full close correctness. No new client or container-tail result is available from the stopped proof run.

## 6. Worktrees and processes

Windows launched no Gradle process for either round. Both chat-owned scratch checkouts were clean after their source/report pushes. Process inspection found zero Java/shell command lines mentioning either task worktree. No other host's worktrees or processes were touched. This assignment authorizes removal of this chat's two scratch worktrees after the report is pushed; fork branches and commits are retained. Removal is recorded in the closeout.

## Disposition and owner decisions

Keep both production candidates unsubmitted pending focused proof. Existing SOLR-18564 covers the container tail; a new client-specific JIRA/PR remains an owner decision after its narrower behavior is validated. Keep PR 5213 independent; no ordering dependency with the release fixes was established. No public action is requested merely to continue investigation. Full suites, CI reruns, and live PR/JIRA actions remain held.

Review is complete as source analysis with an explicit test-side defect and scratch repair. It is not a green teardown verdict. Linux continuation runs only the changed interrupted-wait method once at the repaired head, preserves original/raw evidence if accessible, and stops on failure.
