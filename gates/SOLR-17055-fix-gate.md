# Gate job: SOLR-17055 sort-path fix at the new fork head (vm2)

- Branch: solr-17055-submit. Base (parent): `c2396f425fa2a67a40fea7afee341840c3896358` (fork tip before this job, verified by ls-remote on the Windows review host, 2026-10-14).
- New head: `2d154d1a402ed9c1705d187773e7a3009ed0645d` (one commit, "Order a distributed knn query with a sort after taking the global topK by score", Nick Shanin identity, no trailers). Pushed as a plain fast-forward with `research/push-fork-ff.ps1` (`c2396f425fa..2d154d1a402`). ls-remote after the push: `2d154d1a402ed9c1705d187773e7a3009ed0645d`.
- Files in the commit (two, nothing else): `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`, `solr/core/src/test/org/apache/solr/handler/component/DistributedKnnTopKTest.java`.
- Module: `:solr:core`. Focused test class: `org.apache.solr.handler.component.DistributedKnnTopKTest` (GradleTask `:solr:core:test`), single method `test()`, 1 test.
- Seed: `17055C0FFEE17055` (the seed of the premise run in gates/SOLR-17055-sort-path.md).
- Posted by: the Windows review agent, handoff round 1 item 1 (assignments/pool-vm2-open-items-handoff-round-1.md; claim claims/vm2-open-items-handoff-round-1.md), 2026-10-14. No build, test, Gradle or gate run on the Windows host.
- Capability tags: gate. Intended host: vm2 (Linux, Gradle toolchain).
Status: DONE

## Change record (before and after)

Before (parent `c2396f425fa2`):
- `createMainQuery`: `shardQueryIncludeScore` is true when the outer request asks for score, or when `shardRows != 0` and the sort includes score. A knn request with a sort and no score in `fl` gets no score from the shards.
- `mergeIds`: the queue is capped to topK only when `sort == null`. With a sort, every shard hit (up to shards times topK) goes into the sort queue, and `numFound` is capped to topK afterwards, so the list can hold up to shards times topK documents (9 against 3 in the premise run).

After (`2d154d1a402`):
- `createMainQuery`: adds `|| (shardRows != 0 && getKnnTopK(rb.getQuery()) > 0)`, so the shards return scores for a knn request, whatever the sort.
- `mergeIds`: when `knnTopK > 0` and `sort != null`, shard hits first go through `knnTopKQueue`, a score-ordered `ShardDocQueue` of size topK. Its topK documents are then pushed into `shardDocQueue`, which orders them by the requested sort and applies start and rows (`offset + count`). The score-only path (`sort == null`) is unchanged.
- `mergeIds`: in that sorted knn path only, `maxScore` is set to null when the request neither asks for scores (`GET_SCORES` unset) nor sorts on score, so the response shape for such a request does not gain a `maxScore` key. This is not exercised by the focused test, which asks for `fl=id,score`.
- `DistributedKnnTopKTest.test()`: one case added after the paging case: `query("q", knn, "fl", "id,score", "rows", "20", "sort", "id asc")`, asserting `numFound` is 3, the list holds 3 documents, and the ids at positions 0 to 2 are "1", "2", "3".
- Changelog `changelog/unreleased/SOLR-17055-knn-topk-distributed.yml`: not changed in this commit.
- Not touched in this slice: `receipts/SOLR-17055.md`, `pr-drafts/search-components/SOLR-17055.md`, `gates/SOLR-17055-sort-path.md`, `gates/SOLR-17055-sort-path-proof-rerun.md`. The on-green steps of those files wait for this gate.

## Proof shape

- Fix leg: at `2d154d1a402` (both files), `DistributedKnnTopKTest` must pass, 1 of 1, both the score-order case and the sort case.
- Premise (fail-before) leg: production code at the parent `c2396f425fa2` with the test file from `2d154d1a402` overlaid (only `QueryComponent.java` differs from the head). `DistributedKnnTopKTest` must fail, and the failure must be the sort case's control-versus-distributed comparison, `.response.size():9!=3`, with the score-order case passing. The premise run at this base already produced that failure with the same query shape (gates/SOLR-17055-sort-path.md, "Premise run result (vm2, 2026-10-14T10:44ZZ)"). If the score-order case fails at the parent, the overlay is wrong; record that and do not count the run.

## Steps, in order, at `2d154d1a402`

1. [ ] Head: `git ls-remote origin refs/heads/solr-17055-submit` equals `2d154d1a402ed9c1705d187773e7a3009ed0645d`. `git diff --stat c2396f425fa2..2d154d1a402` shows exactly the two files above. Stop and record here if anything else differs.
2. [ ] Changelog YAML parse of `changelog/unreleased/SOLR-17055-knn-topk-distributed.yml` with a real YAML parser: rc 0 (file unchanged by this commit).
3. [ ] Spotless: `:solr:core:spotlessApply`, then `:solr:core:spotlessJavaCheck`. If `spotlessApply` changes any file, stop, record the diff here, and do not commit on vm2. The next head is decided on the Windows side.
4. [ ] Tidy guard: `gradlew tidy` for `:solr:core`. Restore `gradle/libs.versions.toml` if the run modified it, then the tree must be clean. A tidy-modified tree is GATE INVALID. rc 0.
5. [ ] Error Prone compile with `-Pvalidation.errorprone=true`: `:solr:core:compileJava` and `:solr:core:compileTestJava`. rc 0 both.
6. [ ] Fail-before (premise) leg: check out `c2396f425fa2` production code for `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java` only, keep the test file from `2d154d1a402`, and run `:solr:core:test --tests org.apache.solr.handler.component.DistributedKnnTopKTest` with `-Ptests.seed=17055C0FFEE17055`. Record the exit code and the failure text from the fresh JUnit XML (`solr/core/build/test-results/test/TEST-org.apache.solr.handler.component.DistributedKnnTopKTest.xml`). Expect rc 1 with `.response.size():9!=3` in the sort case and the score-order case passing. Then restore `QueryComponent.java` to `2d154d1a402` and confirm the tree is clean and HEAD is `2d154d1a402`.
7. [ ] Fix leg: at `2d154d1a402`, the same focused run with the same seed. Expect 1 test, 0 failures, 0 errors, 0 skipped, counted from fresh JUnit XML. Record the UTC run date, the suite timestamp and the seed.
8. [ ] Module check: `:solr:core:check -x test`, rc 0. Tree clean afterward.
9. [ ] Record each result in this file. Then the on-green steps belong to the main side (receipt, draft links, and the Limits in pr-drafts/search-components/SOLR-17055.md), not to this job.

## Owner and main-side questions (not decided by this job)

- Q1, two fix commits on one parent. vm2 holds fix commit `2fe0986158391908ac89564090eb97e245788023` on `c2396f425fa2` (not on the fork; its gate is gates/SOLR-17055-sort-path-proof-rerun.md). This job's commit `2d154d1a402` is an independent implementation on the same parent, and it is now the fork tip. Both cannot land as plain fast-forwards from `c2396f425fa2`. The owner or main agent must choose which commit lands, and the proof-rerun job's head target must follow that choice. This job does not force anything.
- Q2, order not discriminated by the fixture. With `sort=id asc`, the topK ids 1, 2, 3 are also the score order, so the sort case proves the topK set and its count but not that the order follows the requested sort. An `id desc` case would discriminate. It is not added here because the job names `sort=id asc`. Owner call.
- Q3, `maxScore` suppression. Included in the change (see the change record). The focused test does not exercise it. Keep or drop is the owner's call.
