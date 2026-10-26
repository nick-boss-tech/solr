# Gate: PR 5232 listener correctness follow-up, round 1

Status: OPEN

- Tags: gate, premise-run, review. Intended host: any capable Linux gate host with the branch's JDK/toolchain; one gate at a time under the host build lock.
- Source: Nick's October 26 request to produce a branch on top of PR 5232 fixing the two review findings.
- Fork branch: `nick-boss-tech/solr:solr-11994-submit`.
- Exact implementation head: `b3a11484f13feaab55fe4f27504bfce3d1bdd2ef`.
- Parent / negative proof base: apache/solr PR 5232 head `2c9243b111b38367677d7e0c9b30b8e72c554d2c`, from `sarkaramrit1993/solr:SOLR-11994-search-request-listener`.
- Implementation claim: `claims/pr-5232-followup-windows-round-1.md`.
- Implementation report: `reports/pr-5232-listener-followup-windows-round-1.md`.
- Receipt target: `receipts/PR-5232-listener-followup.md`.
- State: Implemented, Proof Pending. Source reviewed, `git show --check` passed, clean implementation worktree, fork head verified. No Gradle or test ran on Windows.

## Preflight and scope

Read live WORKFLOW.md and START-HERE.md, check competing claims and the actual fork head, and publish the gate claim before execution. Record the host, UTC start, JDK, build lock, full detached checkout, background runner, log paths, and result paths. Retain the full source tree. Confirm that this head is one commit directly above the pinned PR head, and inspect only this follow-up delta: seven files, 167 additions, three deletions.

The two repairs are:

1. `SearchRequestInfo` adds coordinator CPU to a header CPU value only when `SearchHandler` actually published a shard aggregate in the log. A header containing only `RequestHandlerBase`'s local fallback is counted once.
2. `SearchHandler` excludes requests nested on the current `SolrRequestInfo` stack. Document `[subquery]` children run through an embedded server and lack the outer request's `isShard` flag. A top-level embedded search remains eligible.

No change to the contributor's fork, live PR 5232, or apache/solr is authorized. This gate is for Nick's derived branch only.

## Formatting and positive leg

At the pinned head, run `./gradlew tidy` and require a clean tree. If tidy changes files, commit the scoped formatting repair with Nick Shanin's ICLA identity and no trailers, push plainly to the fork branch, and add a superseding gate naming that new exact head before proving it. Do not edit this immutable gate or carry results forward from a different head. Record any changes to inherited PR code separately.

Run both focused classes from fresh results:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestInfoTest --tests org.apache.solr.handler.component.SearchRequestListenerTest -Ptests.seed=5232C0DE
```

Require 17 executed tests, zero failures/errors/skips: four CPU snapshot tests and thirteen listener tests. The Linux host must support thread CPU timing so the existing CPU test executes. Retain the complete log, fresh class output, and both JUnit XML files under `solr/core/build/test-results/test/` before another cleanTest run.

Inspect these specific behaviors in the source and execution evidence:

- `testDistributedFailureCountsLocalCpuOnce`: fixed nonzero 7 ms local fallback produces 7 ms, not 14 ms.
- `testDistributedCpuIncludesLatestShardTotal`: latest 11 ms shard aggregate plus 7 ms local produces 18 ms; earlier aggregate entries do not win.
- Local and unavailable CPU snapshots retain their original semantics.
- `testCpuTime`: the real two-shard malformed-query path records a failure, two shards, no published shard CPU, and a callback value equal to local CPU. This supplements the nonzero deterministic test; a rounded zero alone cannot prove the repair.
- `testDistributedSubQueriesNotReported` and `testLocalSubQueriesNotReported`: each returned document has the correct actual child result, and exactly one event is recorded on the original coordinator. The distributed test uses two-pass fetching so the children execute on field-fetch shards.
- `testTopLevelEmbeddedSearchReported`: one callback is still delivered for a legitimate top-level embedded request.
- Existing coordinator, v2, throwing-listener, handler-exclusion, and no-listener cases remain green.

Run the affected neighboring behavior from fresh results, retaining evidence separately:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.request.TestSolrRequestInfo --tests org.apache.solr.response.transform.TestSubQueryTransformerDistrib -Ptests.seed=5232C0DE
```

Require zero failures/errors/skips and record actual counts. Then run `./gradlew check -x test` as required by the branch's AGENTS.md, preserving the real result. Do not silently exclude checks. Finish with `git diff --check` and a clean tree at the exact verified head.

## Negative leg: original PR code plus the new tests

Use a separate clean detached checkout of parent `2c9243b111b38367677d7e0c9b30b8e72c554d2c`. Overlay ONLY these two test files from the implementation head (or the superseding formatting head):

- `solr/core/src/test/org/apache/solr/handler/component/SearchRequestInfoTest.java`
- `solr/core/src/test/org/apache/solr/handler/component/SearchRequestListenerTest.java`

Keep all production files at the parent revision. Record the synthetic tree and test-file hashes, and confirm that no new production method is needed by the overlaid tests. Run:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestInfoTest.testDistributedFailureCountsLocalCpuOnce --tests org.apache.solr.handler.component.SearchRequestListenerTest.testDistributedSubQueriesNotReported --tests org.apache.solr.handler.component.SearchRequestListenerTest.testLocalSubQueriesNotReported -Ptests.seed=5232C0DE
```

Require exactly these three executed methods to fail for the intended assertions: CPU `expected 7 / actual 14`, and more than one event in each subquery test after successful child-result assertions. Capture the actual observed counts and assertion messages. Compilation failure, startup failure, skip, missing method, or a passing parent is not fail-before evidence. If one method passes, investigate and correct the premise/test rather than declaring that finding proven.

## Completion and failure handling

GREEN requires exact-head positive proof, discriminating negative proof for both findings, clean tidy, successful required checks, preserved neighboring behavior, and retained fresh XML/logs. If executed tests fail, investigate and repair under WORKFLOW.md rather than retrying blindly. An environment refusal before tests execute may be repaired and relaunched once with its cause recorded.

Write the receipt with the exact final fork head, original PR proof base, all commands and seeds, executed counts/skips, failure reasons on the negative leg, JDK/toolchain, runner/raw evidence paths, and limitations. Its final section should give a concise suggested upstream update for Nick to review. No public posting is authorized.
