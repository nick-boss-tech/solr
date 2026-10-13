# Gate job: SOLR-17680 end-to-end test for the v2 two-router path

- Branch: solr-17680-submit. Head: `ce59bbe112d063e3d3c93b3be6a1a5269a38fc37` (fork tip). Merge-base: `56ec140e3636d5f4150fa87fbf7103529536ac99`.
- Status: re-gate at 52b641ca1a26 ran 2026-10-13 and is NOT GREEN under the all-rc-0 rule (fail-before and proof legs rc=1, both labelled nonzero expected). Not relaunched; not DONE; no fork push; left for the main side to rule. The 766dd13e9e run FAILED on a test-fixture defect; the negative case was corrected on the branch (commit amended, unpushed). See "Gate run 2026-10-13" and "Classification and fix, 2026-10-13" below. The block recorded in "Blocked at claim" was settled by the main side on 2026-10-13.
- Capability tags: gate.
- Intended host: vm2 (Linux). Windows never claims build work.
- Job type: a new focused test, then the focused run and the fail-before run. vm2 may patch the branch to add the test, under the patch-loop rule: the patching agent pushes with a plain fast-forward push, and the gating host refreshes the receipt.
- Why: the v2 two-router path is covered only by a mock test that checks the message, not by a command run. The draft's Limits says so. Nick wants the path run.

## The test to add

In `solr/core/src/test/org/apache/solr/cloud/CreateRoutedAliasTest.java`, after `testV2` (around line 83; the POST to `/____v2/aliases` is near line 129). Keep the file's style and its helper methods.

1. Positive case: POST `/____v2/aliases` with two routers, a time router (`field`, `start`, `interval`) and a category router (`field`, `maxCardinality`), and a create-collection config name. Assert:
   - the response is 200;
   - the alias exists;
   - the alias metadata has `router.name` = `Dimensional[time,category]`, `router.0.field` set, and `router.1.field` set.
2. Negative case (settled to the current behaviour, see the decision in "Blocked at claim"): the same request with a category router that has no `maxCardinality`. Assert the response is 200 and the alias is created. At this head `maxCardinality` is optional for a category router, so the request succeeds without it; this case pins the current behaviour and does not ask production to reject the request.

## Steps

1. Confirm the fork tip with `ls-remote origin refs/heads/solr-17680-submit`. If it is not `ce59bbe112d`, stop and record the new tip.
2. Grep the branch for the exact package of CreateRoutedAliasTest before composing the run spec.
3. Add the two cases. Keep the change to that test file.
4. Run the focused class at the head with JUnit XML. Also run the two existing v1 tests in the same batch: `testDimensionalRoutedAliasWithoutTopLevelFieldV1` and `testDimensionalRoutedAliasMissingDimensionFieldFails`. Record the counts, the seed and the date.
5. Fail-before: at the merge-base with the branch's test file kept, run the new positive case. Record the failure text. It should be "A routed alias requires these params".
6. Commit the test as Nick Shanin, no trailers, no dashes, and push it with a plain fast-forward push (`push-fork-ff`). Do not force.
7. Refresh `receipts/SOLR-17680.md` with the new head, the counts, the fail-before text and the date. Mark this job DONE.

## Checklist

- [ ] fork tip confirmed
- [ ] package grepped
- [ ] two test cases added, test file only
- [ ] focused run with JUnit XML; v1 tests in the same batch; counts and date recorded
- [ ] fail-before text recorded at the merge-base
- [ ] commit pushed plainly; receipt refreshed; job DONE

## Blocked at claim

- Decision (main side, 2026-10-13, under the owner's decision default): option (b) below. The SOLR-17680 branch is a message-only change; making `maxCardinality` required in the v2 path is a behaviour change outside its scope and is not approved inside this job. The negative case is changed to assert the current behaviour (the request succeeds, 200), and the job is re-issued on that basis. If requiring `maxCardinality` is ever wanted, it is a separate scope item for the owner. The rest of this job, including the two-router positive coverage, is unchanged. The block record below is kept as the history of the block.
- Checked at the fork tip ce59bbe112d063e3d3c93b3be6a1a5269a38fc37 (merge-base 56ec140e3636): the tip matches the job and the package is org.apache.solr.cloud.
- The negative case cannot pass as written. Step 3 asks for a category router with no maxCardinality to return 400 naming router.1.maxCardinality. At this head the category router's maxCardinality is optional: solr/api/src/java/org/apache/solr/client/api/model/CategoryRoutedAliasProperties.java has no required annotation, solr/core/src/java/org/apache/solr/handler/admin/api/CreateAlias.java (category validate() checks only field) and CategoryRoutedAlias.java accept a missing value, and the string "router.1.maxCardinality" appears in no error message in solr/core/src/java.
- So the test as specified would get 200 and fail. Making it pass needs a production change (requiring maxCardinality for v2 dimensional category routers), which is outside the test-only scope of this job.
- Owner decision needed: either (a) approve a production change to require maxCardinality for category routers in the v2 path, with its own job, or (b) change the negative case to the behaviour the code has (200 without maxCardinality) and re-issue this job. No test, commit, gate, or fork push has been made.

## Gate run 2026-10-13 (FAILED, not relaunched)

- Log: logs/vm2/queue-g17680-v2-gate-20261013T130656Z.out. GATE START 13:06:56Z, GATE END 13:17:01Z. Runner: gates/g17680-v2-gate.sh, worktree gates/solr-17680-v2.
- Head run: 766dd13e9e9675a5ced323c29d677eed9b9f20a7, one commit above the fork tip ce59bbe112d. That commit exists only in the gate worktree; the fork tip is unchanged, so this run did not test the pushed branch. Step 1 of this job (fork tip must be ce59bbe112d) was not re-recorded at the run. Delta against the merge-base: CreateRoutedAliasTest.java +161, CreateAliasCmd.java, CreateAlias.java, CreateAliasAPITest.java, changelog/unreleased/SOLR-17680.yml.
- rc lines: tidy 0, tidy-clean empty status, focused head 1, fail-before 1 (the script expects nonzero), restore status empty.
- Focused head, JUnit XML: CreateRoutedAliasTest tests=17 failures=1; CreateAliasAPITest tests=13 failures=0; gradle reported 30 tests completed, 1 failed. Failing test: testV2DimensionalCategoryRouterWithoutMaxCardinality, "Unexpected status: 400 expected:<200> but was:<400>" at CreateRoutedAliasTest.java:232. Seed 1D2AFF88101521D3:5F496A954451627D. Passing in the same batch: testV2DimensionalTwoRouters, testDimensionalRoutedAliasWithoutTopLevelFieldV1, testDimensionalRoutedAliasMissingDimensionFieldFails.
- Fail-before at the merge-base (test file kept, CreateAliasCmd.java and CreateAlias.java reverted): testV2DimensionalTwoRouters failed with "Unexpected status: 400 expected:<200> but was:<400>". This is not the text "A routed alias requires these params" that step 5 expects.
- Open question for the main side: the negative case is 200 by decision (option b), but the head returns 400 for a category router without maxCardinality. Either the head's CreateAlias.java change makes maxCardinality required (which the decision excluded), or the test expectation is wrong. The positive case at merge-base also returns 400, so the fail-before result does not isolate the test change.
- Nothing pushed, no receipt refreshed, no fork push. The gate worktree gates/solr-17680-v2 is left in place for the main side.

## Classification and fix, 2026-10-13 (vm2, after the FAILED run)

- Failing test: testV2DimensionalCategoryRouterWithoutMaxCardinality (CreateRoutedAliasTest.java:232 at 766dd13e9e; seed 1D2AFF88101521D3:5F496A954451627D). Assertion: "Unexpected status: 400 expected:<200> but was:<400>".
- Server message, from the same run's log: 400 "router.maxCardinality must be a valid Integer, instead got: null", raised by CategoryRoutedAlias.parseMaxCardinality, unchanged from the merge-base. The decision's premise (maxCardinality optional at this head, so the request succeeds) does not hold; the current behaviour is a 400 that names router.maxCardinality.
- Classification: test-fixture defect. The expectation of 200 is wrong about the code. Production is consistent and no production change is made.
- Premise check at the merge-base: the positive case's server message at the merge-base is "A routed alias requires these params: [router.field, router.name] plus some create-collection prefixed ones" (logs/vm2/17680-v2-failbefore.log). The earlier note that the fail-before showed "the same 400" only described the assertion line; the server text is the premise reason.
- Fix, under the decision's option (b) as pinned to the current behaviour: the negative case asserts 400 and the message "router.maxCardinality must be a valid Integer". The helper now returns the request and each test chooses assertSuccess or assertFailure. Test file only. Commit amended on the unpushed test commit, now 52b641ca1a26; no production file changed.
- Re-gate: runner gates/g17680-v2-fix-gate.sh, logs logs/vm2/queue-g17680-v2-fix-gate-*.out and logs/vm2/17680-v2-fix-*.log/.xml. Tidy with a clean tree, focused run of CreateRoutedAliasTest and CreateAliasAPITest with JUnit XML, fail-before of testV2DimensionalTwoRouters and testV2DimensionalCategoryRouterWithoutMaxCardinality at the merge-base production. The push of 52b641ca1a26 to solr-17680-submit is a plain fast-forward from ce59bbe112d and happens only if the re-gate is green.

## Re-gate run at 52b641ca1a26, 2026-10-13 (vm2 queue record)

- Fix gate, GATE START 13:34:34Z, GATE END 13:41:12Z, log logs/vm2/queue-g17680-v2-fix-gate-20261013T133434Z.out. Delta: CreateRoutedAliasTest.java only (61 insertions).
- tidy rc=0, tree clean after tidy. Focused head leg rc=0: CreateRoutedAliasTest 17 tests, 0 failures; CreateAliasAPITest 13 tests, 0 failures (JUnit XML copies in logs/vm2/TEST-*CreateRoutedAliasTest.xml and TEST-*CreateAliasAPITest.xml).
- Fail-before leg, production reverted to merge-base for CreateAliasCmd.java and CreateAlias.java: rc=1 (nonzero expected). 2 tests, 2 failures: testV2DimensionalTwoRouters ("Unexpected status: 400 expected:<200> but was:<400>") and testV2DimensionalCategoryRouterWithoutMaxCardinality ("Didn't find expected error string within response"). XML logs/vm2/17680-v2-fix-failbefore.xml.
- Proof run, PROOF START 13:42:03Z, PROOF END 13:48:36Z, log logs/vm2/queue-g17680-v2-fix-proof-20261013T134203Z.out. Full classes at merge-base production: proof rc=1 (nonzero expected). CreateRoutedAliasTest 17 tests, 3 failures: testV2DimensionalTwoRouters, testV2DimensionalCategoryRouterWithoutMaxCardinality, and testDimensionalRoutedAliasWithoutTopLevelFieldV1 (400 expected 200, not a new test). CreateAliasAPITest 13 tests, 1 failure: testRemoteMessageCreationForMultiDimensionalRoutedAlias (expected 11, was 10). XML logs/vm2/17680-v2-fix-proof-*.xml.
- Module check, :solr:core:check -x test: rc=0.
- Not GREEN under the all-rc-0 rule. Two rc=1 legs are the designed fail-before and proof legs, and two of the proof-leg failures are existing tests outside the new test. The main side rules on whether the proof is accepted and on the fork push. The push to solr-17680-submit has not happened.
