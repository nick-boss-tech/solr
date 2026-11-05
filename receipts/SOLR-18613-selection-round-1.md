# Receipt: SOLR-18613 standalone selection gate, round 1

Status: **GATE FAILED** at H `e88dc392bcab67ac388a45340d96ac52c616f35b`.

- Job: `gates/SOLR-18613-selection-round-1.md`. Claim: `claims/SOLR-18613-selection-gate-phone-agent.md`.
- Executor: phone-agent (Linux, JDK 21, Gradle 9.7.0 via solr-gradle.sh), host test-queue flock, one build at a time.
- Run window (UTC): 2026-11-05T19:33:25Z to 19:47:48Z. A first launch was killed by a service restart during clone preparation, before any build ran; it is not counted as an attempt.
- H: `e88dc392bcab67ac388a45340d96ac52c616f35b`, tree `40843a7f4f083f5ee036c0cbbf60b812731c072a`, verified in a clean detached full clone (`~/workspace/solr-clones/18613-sel-h`). P: `2399d0312fa320563773ea87884ee4f534594974`, second clean detached full clone. Seed `5B407A2D9C1310E7` throughout.
- Logs and XML: `~/workspace/tools/gate-18613-sel/` (leg logs, `xml-h/`, `xml-p/`, `results.txt`).

## Positive legs at H

1. Tidy: rc 0, but the tree was NOT clean afterwards. One tracked change: `solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java`, a single comment-only Javadoc rewrap in the `pickServer` documentation (the repair commit's new `@param aliveServerList` text wraps differently than the formatter requires). Formatting defect in the repair commit.
2. Error Prone compile (`:solr:solrj:compileJava -Pvalidation.errorprone=true`): rc 0. Disclosure: legs 2 and 3 ran in the same clone after tidy, so their working tree carried that comment-only rewrap; no code bytes differ from H beyond it.
3. Focused suites: rc 0. Fresh JUnit XML totals 49 tests, 0 failures, 0 errors, 0 skipped: LBSolrClientAttemptLimitTest 14 (both new methods executed: testStandaloneInterleavedRequestDoesNotHideHealthyEndpoint, testStandaloneRetryUsesCustomPickerWithUntriedEndpoints), LBAsyncSolrClientAttemptLimitTest 12, LBSolrClientTest 8, LBAsyncSolrClientTest 4, LBSolrClientRetryUnsentTest 3, LB2SolrClientTest 7, LBHttpSolrClientBadInputTest 1.
4. Root `check -x test`: **rc 1, FAILED**. `:solr:solrj:forbiddenApisTest` rejects the new test code: `java.util.concurrent.Executors#newSingleThreadExecutor()` at `LBSolrClientAttemptLimitTest.java:135` (threads must be named through a thread factory). This is a branch defect in the repair commit's test, not an environment failure; the rest of the check was still running through its tasks (262 actionable tasks) when this task failed the build. `git diff --check` rc 0.

## Negative leg at P

Second clone at P with only H's `LBSolrClientAttemptLimitTest.java` copied in (synthetic diff: exactly that one file, +90/-1; sha256 `b70cde12c7a54e8cd695b2fdff20fd702273590008577226b044e23d76e57aad`, identical before and after the run). Both new methods executed and failed for the intended reason: `testStandaloneInterleavedRequestDoesNotHideHealthyEndpoint` with "Primary request failed after [http://one.invalid/solr, http://one.invalid/solr]" and `testStandaloneRetryUsesCustomPickerWithUntriedEndpoints` with "Request failed after [http://one.invalid/solr, http://one.invalid/solr]", the [A, A] dispatch trace the job specifies. This leg PASSED as specified.

## Carried-forward historical proof

The four original-production failures recorded in `receipts/SOLR-18613.md` were not rerun. The four method names are still present in H's test files with their assertions (verified by name in the H clone's two attempt-limit test files). This proof remains carried forward, not exact-head evidence, as the job allows.

## Verdict and repair path

GATE FAILED at H. The branch head cannot be called green: its test code calls a forbidden API and its production Javadoc is not tidy-formatted. Per the job's repair rules, the fix is an ordinary Nick Shanin commit on the fork branch (named thread factory in the test; the tidy rewrap), a plain push with independent read-back, and a superseding exact-head gate (round 2) that reruns every leg. The positive test counts above were recorded at H with the comment-only tidy rewrap in the tree; they do not substitute for the round-2 run at the repaired head.

## Repair applied (same day, per the job's repair rules)

Two ordinary Nick Shanin commits on the fork branch, plain-pushed and read back through the fork refs API and the live PR:

- `20e244407e` "SOLR-18613: tidy the pickServer Javadoc wrap": exactly the rewrap tidy produced in leg 1, no other bytes.
- `fe557633a1` "SOLR-18613: use an MDC-aware executor in the interleaving test": the test now creates its executor through `ExecutorUtil.newMDCAwareSingleThreadExecutor(new SolrNamedThreadFactory("attemptLimitTest"))`, the replacement the forbidden-apis signature prescribes. (A first attempt with a plain thread factory still failed `forbiddenApisTest`, which bans the ThreadFactory overload as well for missing MDC context; that intermediate state was never committed.) No assertion, latch, or dispatch logic changed.

Repaired head H2: `fe557633a169c34dcb835696470cc6e69ba00be4`, tree `6a0c8c7bfe43420ba03284f727c420af3996ebf4`. Pre-push verification in the repair worktree: `:solr:solrj:tidy` clean over the two files, `forbiddenApisMain` and `forbiddenApisTest` pass, `LBSolrClientAttemptLimitTest` 14 of 14 at seed `5B407A2D9C1310E7`. Full verification reruns under `gates/SOLR-18613-selection-round-2.md`.
