# Gate job: SOLR-17680 end-to-end test for the v2 two-router path

- Branch: solr-17680-submit. Head: `ce59bbe112d063e3d3c93b3be6a1a5269a38fc37` (fork tip). Merge-base: `56ec140e3636d5f4150fa87fbf7103529536ac99`.
- Status: OPEN, claimable by vm2. The block recorded below was settled by the main side on 2026-10-13 and the job is re-issued on that basis; the decision and its reason are recorded at the top of the section "Blocked at claim".
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
