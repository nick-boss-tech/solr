# Gate: SOLR-18295 leaders-first routing, round 1

Status: OPEN

- Tags: gate, settling-run. Intended host: any capable Linux gate host; serialize under its build lock. Windows implementation only; no local Gradle or tests ran.
- Source: Nick's explicit October 24 request to implement a branch, and `claims/solr-18295-routing-implementation-round-1.md`.
- Ticket: SOLR-18295 (ReplicationFactorTest is flaky).
- Fork branch: `nick-boss-tech/solr:solr-18295-submit`.
- Exact final head: `b273b245115beff0cc131448c277dc9bde909a0c`.
- Base: upstream/main `13c693d805f6541f7cec684cd27d28eef7b3b614`.
- Outbound file: `solr/core/src/test/org/apache/solr/cloud/ReplicationFactorTest.java` only, 8 additions and 2 deletions. Test-only changelog exemption applies.
- Module/class: `:solr:core`; `org.apache.solr.cloud.ReplicationFactorTest` (whole class, both testRf3 and testRf2NotUsingDirectUpdates scenarios).
- Related evidence: apache/solr #5098 (SOLR-18530), and https://iamsanjay.github.io/solr-notes/RecoveryAfterSoftCommitTest.html. The report's 16/100 vs 0/100 runs concern RecoveryAfterSoftCommitTest, not this gate.
- Competing work: Jan Høydahl's open apache/solr #4563, head `54f375b0bed805a08e1b456bd782e15dfce74723`, retries single-document sends. This branch is a requested fork-only alternative; do not open a PR or post comments.
- Runner: use the Linux host's documented preflight and background step/sweep runners, under its build lock. Record host, runner paths, raw log roots, seed list, and XML paths in a new claim before running.

## Why this shape

The inherited cloud-client factory randomizes leaders-first versus any-replica routing. The test closes non-leader proxies but keeps sending single adds, batches, delete-by-ID, and delete-by-query through that client, which can otherwise choose the cut replica. This test-local override passes `true` for shardLeadersOnly. It deliberately preserves the helper's independently randomized directUpdatesToLeadersOnly and parallelUpdates settings. Leaders remain healthy; explicit non-direct requests still exercise the healthy shard1 leader and follower. No SolrJ retry rule, RF assertion, partition step, or shared harness is changed.

Do not add a test that only asserts a builder flag. The existing whole class is the meaningful acceptance test. Repetition is comparison evidence, not a deterministic fail-before claim.

## Leg 1: exact final head

1. Detached preflight at the exact final head; verify author, scoped diff, and clean tree. Run `tidy` and `:solr:core:spotlessJavaCheck`. Tidy must leave the tree clean. If it changes this branch, commit and push the ticket-scoped formatting repair and create a superseding exact-head gate record; never carry old-head proof forward silently. The job and implementation claim are add-only.
2. `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
3. `:solr:core:cleanTest`, then `:solr:core:test --tests org.apache.solr.cloud.ReplicationFactorTest -Ptests.seed=18295ABC0001`.
4. Require executed, non-skipped whole-class tests and fresh XML. Logs must reach both the 1x3 and 2x2 scenario markers and final completion. Keep security-manager/default harness setup intact. A compile, startup, worker, or environment failure is not a test pass.

## Leg 2: risky baseline comparison

Use a separate scratch worktree at base `13c693d805f6541f7cec684cd27d28eef7b3b614`. Remove only the SOLR-18295 AwaitsFix import/annotation. Add exactly the head's CloudSolrClient import and createCloudClient override, except pass `false` rather than `true` to createNewCloudSolrClient. This forces risky any-replica routing instead of relying on the inherited coin flip, while keeping the random-number consumption aligned with the final-head override. This is a synthetic baseline, not untouched upstream. Save its complete diff with the logs. Do not push it or change the shared harness.

Run 20 separate whole-class invocations, using hexadecimal seeds `18295ABC0001` through `18295ABC0014` inclusive (hex suffixes 0001..0009, 000A..000F, 0010..0014). Before each invocation, run `:solr:core:cleanTest`; then run `:solr:core:test --tests org.apache.solr.cloud.ReplicationFactorTest -Ptests.seed=<seed>`. Archive logs and XML before the next clean. Do not use tests.iters: this class creates fixed-named collections and needs a fresh suite lifecycle per run. Keep the existing local retry configuration; do not opt into CI mode, which enables Develocity retries. If retries occur anyway, count first attempts and preserve retry outcomes separately.

A baseline failure supports the premise only when the trace identifies a cloud update reaching a deliberately cut follower endpoint and a transport exception after request commit (ClosedChannelException / Broken pipe or equivalent). Record method, seed, endpoint vs closed proxy port, cause chain, and failure count. RF assertion failures or setup failures are separate findings and require investigation. Inconclusive request-commit classification stays inconclusive. Do not quietly rerun a failed gate.

## Leg 3: matched final-head repetitions and checks

At the unmodified exact final head, run the same 20 seeds as 20 fresh whole-class invocations with the same clean/log archival policy. Leg 1's smoke is reported separately from this set. Require all matched final-head tests to execute and pass, with both scenarios completed and no RF assertions weakened. Report counts and any retries. Randomization of direct-update failover remains as shipped; do not force or instrument a different setting and call it exact-head proof.

Then run `:solr:core:check -x test` and final `:solr:core:spotlessJavaCheck`; require a clean git tree. No repo-wide test suite, UI build, or unrelated tests are required.

## Acceptance and failure decisions

- GREEN requires exact-head format/compile/check success and all 20 matched final-head runs passing. A baseline non-reproduction is a documented limit, not a reason to invent fail-before proof or to run without bound. If all baseline runs pass, receipt wording must say "baseline did not reproduce in 20 forced-any-replica runs; routing fix remains code-path-based".
- If the expected baseline failure reproduces and final-head runs pass, state the observed sample counts. Do not extrapolate them into a claimed production failure rate or transfer #5098's counts to this ticket.
- Any final-head RF assertion, update transport, or recovery failure: stop the gate, preserve evidence, diagnose it, and publish a failure/repair record. A code repair needs a new pushed head and superseding gate. Do not remove assertions, expand exception retries, alter shared harness routing, or silently relaunch.
- An invocation that never starts tests may be repaired and relaunched once under WORKFLOW.md, with the reason and setup change recorded.
- No public PR/JIRA writes. Do not move this pinned head while claimed; any changed head needs a superseding job. Review current claims and branch before starting.

## Deliverables

Add a gate claim and report under coordination-ui; write `receipts/SOLR-18295.md` as the gating host's finishing record. Include exact SHA/base, synthetic baseline diff, raw command/seed list, first-attempt counts, per-failure classification, evidence paths, final clean status, and a short PR description draft that acknowledges #4563 and every proof limit. No worker edits to this job or implementation claim; completion is derived from the receipt/report.
