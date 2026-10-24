# PR #5202 CI leak review, round 2

Reviewer: phone agent, 2026-10-23. Scope per `assignments/pool-ci-pr5202-custom-sort-review-round-2.md`: challenge the round-1 conclusion in `reports/ci-pr5202-custom-sort-leak.md` that PR #5202 does not cause the `DistributedQueryComponentCustomSortTest` leak report in CI run 39903819776 (job 120373474924). No code changed, nothing rerun, nothing posted anywhere else.

## Verdict

**Not involved.** The round-1 conclusion stands, and on points 1 and 2 the CI log itself makes it stronger than round 1 stated. Round 1's mailing-list table has one defective citation (the #1883 row), recorded under point 3; it does not change the verdict, because the remaining four verified main-branch leaks include one with a creation stack identical to the PR run's.

## The failure, from the job log

The job log was fetched in full from the GitHub API (job 120373474924). Line numbers below are lines of that log.

- The checkout is the merge ref: line 75, "HEAD is now at c601b1ea825 Merge 7e0251ee029125152c4d1791660189ef1e9dea06 into 79b23df58daac14b92cc35f23b8b81f72098b4ba". This is the merge commit point 2 asked to confirm.
- Line 5992: `DistributedQueryComponentCustomSortTest > classMethod FAILED`; line 5993: "ObjectTracker found 2 object(s) that were not released!!! [SolrMetricsContext, HttpJettySolrClient]". The suite seed is `FFF5A3C3AAC69084` (line 6350).
- The run's second failure is a consequence of the first, not a separate defect: lines 6321-6322, `IllegalStateException: No reporter found for test descriptor: Test classMethod(...)`, the Gradle reporter crash the assignment describes. Line 8442: "6119 tests completed, 2 failed, 286 skipped".

## Point 1: the single-class passes, and where the leaked objects were created

The challenge was that a single-class pass cannot reproduce full-suite JVM sharing, and that an earlier class in the same worker JVM might have created the leaked objects. For the two reported instances, the creation stacks in the log exclude that. The `SolrMetricsContext` tracker entry (line 5994) was created at `HttpShardHandlerFactory.initializeMetrics` (HttpShardHandlerFactory.java:461) via `SolrMetricsContext.getChildContext`, called from `CoreContainer.load` (CoreContainer.java:792), inside `JettySolrRunner.start`, inside `BaseDistributedSearchTestCase.createServers` (BaseDistributedSearchTestCase.java:300), under this suite's own `ShardsStatement.evaluate` and `RandomizedRunner.runSingleTest`. The `HttpJettySolrClient` entry (line 6068) was created at `HttpShardHandlerFactory.init` (HttpShardHandlerFactory.java:316) through the identical path. Both objects were born in this suite's own shard-server startup and were still unreleased at this suite's teardown check. The tracker is JVM-wide, so leftovers from an earlier suite would also surface at a later suite's check; these two are not leftovers. What the local single-class passes (head and base, one run each at the CI seed) establish is narrower than round 1 needed: the class can tear down cleanly at this seed. They say nothing about the leak's rate; see point 4.

## Point 2: the base comparison and the merge ref

The merge commit is `c601b1ea825`, confirmed from the log (line 75 above): PR head `7e0251ee02` merged into base `79b23df58d`. The merge tree differs from the base tree by the PR's own files: `HttpSolrCall.java`, `V2HttpCall.java`, tests, and docs. The suite and its Jetty startup do load `HttpSolrCall`: every HTTP request to the suite's runners passes through it, so the PR's code is on the request path. That does not connect the PR to the leak. Neither leaked object is created, held, or released by any PR-changed code: creation runs through `HttpShardHandlerFactory` and `CoreContainer.load` (stacks above), and release is the shard handler factory's and the core container's teardown, which the PR does not touch. The PR changes how the `collection` parameter is resolved against the URL path inside request handling; it does not change any lifecycle method of the factory that owns the two leaked objects.

## Point 3: the mailing-list evidence

Each cited notification was read in full through the lists.apache.org thread API (the Jenkins consoleText endpoints 404, as round 1 recorded). Jenkins build numbers below are Jenkins identifiers, not GitHub run IDs, as round 1 noted.

| Build | Round-1 claim | Verified |
| --- | --- | --- |
| Solr-NightlyTests-main #1945, 2026-10-10 | `TestUserManagedReplicationWithAuth.classMethod`; 6 objects including `HttpJettySolrClient` | Yes. The notification reads "ObjectTracker found 6 object(s) that were not released!!! [MockDirectoryWrapper, HttpJettySolrClient, MockDirectoryWrapper, ...]" for that class. |
| Solr-NightlyTests-main #1931, 2026-09-26 | `PeerSyncTest.classMethod`; 11 objects including multiple `SolrMetricsContext` | Yes. The notification lists 11 objects, seven of them `SolrMetricsContext`, for `PeerSyncTest.classMethod`. |
| Solr-Test-main #14946 (through #14951), 2026-09-26 | `TestSolrQueryParser.classMethod`; 11 to 13 objects including `SolrMetricsContext` | Yes for #14946: 12 objects, seven of them `SolrMetricsContext`. The range across #14947 to #14951 was not re-read message by message. |
| Solr-NightlyTests-main #1923, 2026-09-18 | `TestDistributedSearch.classMethod`; 1 `HttpJettySolrClient` | Yes, exactly: "ObjectTracker found 1 object(s) that were not released!!! [HttpJettySolrClient]", created at `HttpShardHandlerFactory.init` (line 316) via `CoreContainer.load` from `BaseDistributedSearchTestCase.createServers`, the same creation path as the PR run's leaked client. |
| Solr-NightlyTests-main #1883, 2026-08-09 | `TestDistributedSearch.classMethod`; both object types | **No, not as cited.** The linked thread resolves to the Build #1886 notification, which contains no `TestDistributedSearch` failure and no ObjectTracker entry at all. Build #1883 may well have shown what the row claims, but the cited link does not establish it; the row should be corrected or re-cited. |

Four of the five rows check out against the notification text itself, and #1923 is the strongest of them: the identical factory creation path leaking the identical object type on unmodified main, in the same base test harness.

## Point 4: intermittency, and the proposed repeat run (not run)

One pass per seed on head and base cannot bound an intermittent teardown race; round 1 said this and it remains the honest limit of the current evidence. A bounded repeat run would answer the rate question for the head. Proposal, for the owner to approve before anything runs:

- Command (per run, alternating head and base worktrees): `:solr:core:test --tests org.apache.solr.handler.component.DistributedQueryComponentCustomSortTest --console=plain -Ptests.seed=FFF5A3C3AAC69084`, the same invocation round 1 used, under the shared test lock.
- Size: 10 runs on the PR head and 10 on the base. A single-class run with warm caches takes roughly 4 to 8 minutes of lock time in this environment, so the sweep costs roughly 1.5 to 3 hours of the shared lock, serialized. A cheaper first step is 5 head runs (roughly 30 to 45 minutes).
- What it can show: 0 failures in 10 head runs still only excludes leak rates above about 26 percent at 95 percent confidence; excluding a 10 percent rate takes 29 clean runs. The sweep is worth doing if a number is wanted, but no feasible sweep proves the rate is zero, and none is needed for the involvement verdict, which rests on the creation stacks and the lifecycle code the PR does not touch.

## What this review did not do

No tests were run, no CI was triggered, the full `:solr:core:test` suite was not run, and nothing was posted to the PR or to Jira. Whether to run the proposed sweep, rerun CI, or comment on PR #5202 stays with the owner, as the assignment states.
