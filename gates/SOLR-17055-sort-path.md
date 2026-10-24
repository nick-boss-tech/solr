# Gate job: SOLR-17055 sort path (premise run first, then fix and gate)

- Branch: solr-17055-submit. Head: `c2396f425fa2a67a40fea7afee341840c3896358` (live tip, read with `git ls-remote origin refs/heads/solr-17055-submit` on the Windows review host, 2026-10-14; matches the gated head in receipts/SOLR-17055.md).
- Module: `:solr:core`. Focused test class: `org.apache.solr.handler.component.DistributedKnnTopKTest` (GradleTask `:solr:core:test`).
- Intended host: a Linux host with the Gradle toolchain (premise-run and gate capabilities). The Windows host does not run it.
- Posted by: the review round for search components drafts (pool-census-followup-search-components-drafts-round-1, slice S4). Evidence by reading at the head, no run: `QueryComponent.java` lines 1018 to 1024 trim the merge queue only when `sort == null`, and lines 1238 to 1240 cap `numFound` for every knn request. `SortSpecParsing` returns a null sort only for the default score order. The earlier audit in reports/search-components-2-h6.md (finding 2) recorded the same gap at an older head.
Status: HANDED (to the main agent, status correction by the main side 2026-10-17: vm2's claim claims/pool-vm2-gate-backlog-17055-fix.md is CLOSED and STALE; its run record says the premise proof did not establish the failure and the item was left for the main agent. Pool hosts skip this file.)
- Decision recorded here (decision default): no recommendation is on record for the sort path. The harder, more thorough option is taken, so the sort path is fixed in this branch, not deferred. The ladder's first rung (get more information) comes first: the premise run below must show the failure before the fix is written.

## Premise run (get more information)

1. In a scratch copy of `DistributedKnnTopKTest` at the head above, add one case beside the existing ones: query `{!knn f=vector topK=3}[1.0, 0.0, 0.1, 0.1]` with `sort=id asc`, `rows=20`, `fl=id,score`, on the existing 3-shard, 24-document setup. Assert that `numFound` is 3 and that the result list holds 3 documents.
2. Expected on the head: the list holds up to 9 documents (3 shards times topK 3) while `numFound` is 3. Record the observed list size and `numFound` here before going on.
3. If the list holds 3 documents, the gap is not real in practice. Stop, record why here, and send the job back to the review round.

## Fix (do the hard thing)

Acceptance bar, for a distributed knn request with any sort: the response holds at most topK documents in total; they are the topK best matches by score across all shards; they are ordered by the requested sort; `start` and `rows` apply to that set. A single-node search of the same query returns the same set.

Suggested route, for the implementer to confirm: when `knnTopK > 0`, choose the global topK by score from the shard hits (a queue of size topK in score order), then order that set by the requested sort and apply `start` and `rows`. Leave the score-order path unchanged.

Test additions: the sort case from the premise run, asserting the ids of the topK documents in sort order, plus the existing assertions.

Changelog: keep the sentence in `changelog/unreleased/SOLR-17055-knn-topk-distributed.yml` only after the sort case passes.

## Steps

1. Premise run as above at `c2396f425fa2`. Record the counts and observed numbers in this file.
2. Implement the fix in `QueryComponent.mergeIds` (and the knn query classes only if needed). Add the sort case to `DistributedKnnTopKTest`.
3. Formatting and compile: `:solr:core:spotlessApply`, then `:solr:core:spotlessJavaCheck`, the Error Prone compile, and `:solr:core:compileTestJava`.
4. Focused test run: `DistributedKnnTopKTest`. Both cases must pass.
5. Fail-before check: the new sort assertion must fail against the proof base production code, with the test file in place. The base first written here was `97d973814336`; the main agent ruling at the end of this file supersedes it: the proof base is `c2396f425fa2` with the fix commit's test files overlaid, run under gates/SOLR-17055-sort-path-proof-rerun.md.
6. Module check for `:solr:core`.

## On green

- Push plainly (fast-forward) to `solr-17055-submit` under the Nick Shanin identity, with no co-author trailer.
- Refresh `receipts/SOLR-17055.md`: new head, the score-order and sort-order counts, the fail-before result, and the module check.
- Re-point every link in `pr-drafts/search-components/SOLR-17055.md` to the new head, and rewrite its Limits: the sort gap is closed, so that paragraph goes. Re-read the draft against the diff.
- Mark this job DONE with the new head.

## Step checklist

- [ ] premise run at c2396f425fa2 recorded (observed list size and numFound)
- [ ] fix and sort case committed; spotless, Error Prone and test compile recorded
- [ ] DistributedKnnTopKTest 1 of 1 at the new head, with the score and sort cases both passing inside it
- [ ] fail-before result recorded (at 97d973814336 it hit the score-only assertion; superseded by the ruling below, rerun at the decided proof base c2396f425fa2 under gates/SOLR-17055-sort-path-proof-rerun.md)
- [ ] module check recorded
- [ ] pushed plainly; receipt refreshed; draft links re-pointed and Limits rewritten; job DONE

## Premise run result (2026-10-14)

- Run: logs/vm2/queue-g17055-premise-gate-20261014T101544Z.out at head `c2396f425fa2a67a40fea7afee341840c3896358`, seed `17055C0FFEE17055`, ended 2026-10-14T10:24:36Z.
- Observed: the sort-case query (`rows=20`, `sort=id asc`) failed at the control-versus-distributed comparison with `.response.size():9!=3`. The distributed response holds 9 documents; the single-node control holds 3. The comparison fails before the `numFound` assertion, so `numFound` was not captured in this run.
- Premise rc=1, the expected outcome if the gap is real. The list holds more than 3 documents, so the gap is real and the fix stage proceeds.


## Premise run result (vm2, 2026-10-14T10:44ZZ): the gap is real

- Head c2396f425fa2 (no branch change). Scratch sort case added to `DistributedKnnTopKTest` (`{!knn f=vector topK=3}[1.0, 0.0, 0.1, 0.1]`, `sort=id asc`, `rows=20`, `fl=id,score`), not committed; the file was restored and the worktree is clean.
- Result: `DistributedKnnTopKTest` 1 test, 1 failure, seed 17055C0FFEE17055 (JUnit XML 2026-10-14T10:24:03Z). The failure comes from `BaseDistributedSearchTestCase.query`, which compares the distributed response with the single-node control. `compareResponses(rsp, controlRsp)` reports `.response.size():9!=3`: the distributed response holds 9 documents (3 shards times topK 3), the control holds 3.
- Step 3 therefore does not apply: the list holds more than 3 documents with a sort, so the gap shows in practice. The premise assertions I added after the helper call were not reached, because the helper failed first; the evidence is the helper's comparison.
- Log: logs/vm2/17055-premise.log; queue log logs/vm2/queue-g17055-premise-gate-*.out.
- Fix (steps 2 and 3 onward): not started. Waiting on a decision to proceed.

## Fix gate result (2026-10-14T11:55:19Z)

- Fix commit: `2fe0986158391908ac89564090eb97e245788023` on `c2396f425fa2a67a40fea7afee341840c3896358`, in the detached worktree /workspace/gates/solr-17055-fix. Not pushed to the fork.
- Gate log: logs/vm2/queue-g17055-gate-20261014T113414Z.out (GATE END 11:54:13Z).
- Fix: score-only merge unchanged. For a knn request with a sort, shard docs are collected, sorted by score, the top topK are kept, and only those are ordered by the requested sort and windowed by start and rows. The shard request also asks for scores for a knn query, and the merged max score is suppressed when the request did not ask for scores.
- Results: changelog rc 0, spotless rc 0, tree clean, compile with Error Prone rc 0, DistributedKnnTopKTest 1 of 1, 0 failures at the head, module check `:solr:core:check -x test` rc 0.
- Fail-before at the job's base 97d973814336: rc 1, failing at the score-only assertion (`.response.numFound:9!=3`, test line 57). The base has no knn topK handling in QueryComponent, so the sort assertion is not reached there. The fail-before is therefore not established for the sort assertion at this base.
- Not GREEN by the coordinator rule (a run has a nonzero rc). No fork push. Left for the main agent: choose a proof base for the fail-before, for example the parent `c2396f425fa2`, where the premise run already showed the sort assertion failing (list 9, expected 3).
- Status: OPEN until the main agent rules on the fail-before base. Not DONE.

## Main agent ruling on the fail-before base (2026-10-14)

- Decision: the proof base for the sort-path fix is `c2396f425fa2a67a40fea7afee341840c3896358`, the fix commit's parent and the current fork tip, with the test files from the fix commit `2fe0986158391908ac89564090eb97e245788023` overlaid. The only production difference between that proof base and the gate head is the fix commit itself.
- Why the job's original base `97d973814336` cannot measure the sort premise: that commit is the branch's merge-base, where QueryComponent has no knn topK handling at all. The test class asserts both paths in its single `test()` method, score order first. At the merge-base the score-order case fails first (`.response.numFound:9!=3`, test line 57 in the fix worktree), so the run never reaches the sort case; the fail-before leg measured the branch's original score-path cap, not this fix. Between the merge-base and the fix commit's parent, the branch's own commits added the score-path cap (merge queue trimmed when `sort == null`, `numFound` capped for every knn request), so at the parent the score-order case passes and only the sort case fails.
- Evidence the parent base discriminates: the premise run at `c2396f425fa2` (recorded above, seed 17055C0FFEE17055) failed at the sort assertion, `.response.size():9!=3` from the control-versus-distributed comparison, with the score-order case passing.
- Expected fail-before at the decided base: `DistributedKnnTopKTest` fails at the sort assertion (`.response.size():9!=3`) and the score-order case passes. If the score-order case also fails there, the overlay is wrong and the run does not count.
- The re-run is issued as a separate job: gates/SOLR-17055-sort-path-proof-rerun.md (OPEN, for vm2). This job stays OPEN until that rerun records the fail-before at the decided base and the on-green steps complete.
