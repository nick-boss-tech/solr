# Gate job: SOLR-17055 sort path (premise run first, then fix and gate)

- Branch: solr-17055-submit. Head: `c2396f425fa2a67a40fea7afee341840c3896358` (live tip, read with `git ls-remote origin refs/heads/solr-17055-submit` on the Windows review host, 2026-10-14; matches the gated head in receipts/SOLR-17055.md).
- Module: `:solr:core`. Focused test class: `org.apache.solr.handler.component.DistributedKnnTopKTest` (GradleTask `:solr:core:test`).
- Intended host: a Linux host with the Gradle toolchain (premise-run and gate capabilities). The Windows host does not run it.
- Posted by: the review round for search components drafts (pool-census-followup-search-components-drafts-round-1, slice S4). Evidence by reading at the head, no run: `QueryComponent.java` lines 1018 to 1024 trim the merge queue only when `sort == null`, and lines 1238 to 1240 cap `numFound` for every knn request. `SortSpecParsing` returns a null sort only for the default score order. The earlier audit in reports/search-components-2-h6.md (finding 2) recorded the same gap at an older head.
- Status: OPEN.
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
5. Fail-before check: the new sort assertion must fail against the base production code at `97d973814336`, with the test file in place.
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
- [ ] fail-before result at 97d973814336 recorded
- [ ] module check recorded
- [ ] pushed plainly; receipt refreshed; draft links re-pointed and Limits rewritten; job DONE

## Premise run result (2026-10-14)

- Run: logs/vm2/queue-g17055-premise-gate-20261014T101544Z.out at head `c2396f425fa2a67a40fea7afee341840c3896358`, seed `17055C0FFEE17055`, ended 2026-10-14T10:24:36Z.
- Observed: the sort-case query (`rows=20`, `sort=id asc`) failed at the control-versus-distributed comparison with `.response.size():9!=3`. The distributed response holds 9 documents; the single-node control holds 3. The comparison fails before the `numFound` assertion, so `numFound` was not captured in this run.
- Premise rc=1, the expected outcome if the gap is real. The list holds more than 3 documents, so the gap is real and the fix stage proceeds.
