# Gate: PR 5238 client search dispatch, round 1

Status: OPEN

- Tags: gate, premise-run, review. Intended host: any capable pool Linux gate host with the branch's JDK/toolchain, supporting thread CPU timing. One gate at a time under the host build lock.
- Source: Nick's direct October 26 request to code regressions and a fix for the PR 5238 review finding, then send it to a gate agent.
- Fork branch: `nick-boss-tech/solr:solr-11994-pr5238-submit`.
- Exact candidate F1: `ede5646e2b2281bf4fc1483c2867f0f047088527`.
- Parent / negative base P: apache/solr PR 5238 head `cc9ea9b1b6b153317217ee458ff6287b5e24cc65`.
- Implementation claim: `claims/pr-5238-client-dispatch-windows-round-1.md`.
- Implementation report: `reports/pr-5238-client-dispatch-windows-round-1.md`.
- Receipt target: `receipts/PR-5238-client-dispatch.md`.
- State: Implemented, Proof Pending. Seven files, 132 additions, eight deletions above P. Windows performed source review and whitespace checks only; no Gradle, compiler, formatter, tests, or retired queue ran.

## Scope and claim

Read the live board bootstrap, host capabilities, competing claims, and current fork head. Run the detached tidy preflight before claiming according to WORKFLOW.md; publish a separate immutable gate claim for this job. Record the host, UTC time, JDK, build lock, detached source checkout, background runner and raw log/result paths. Preserve fresh JUnit XML before cleanTest replaces it. Do not run the same gate on two hosts.

Confirm F1 is exactly one commit above P and that the outbound diff is the seven files described in the report. The fix records the original selected handler under `SolrRequestHandler.class` in the request context at `SolrCore.execute` and `ApiBag.ReqHandlerToApi.call`. `putIfAbsent` preserves that original handler during same-request delegation. SearchHandler tests this identity instead of looking the context path up again in the v1 handler registry. Pathless, nested, shard, ping and export exclusions must remain effective.

The new routing configset deliberately contains `standard`, a v2-only `/v2-only-select`, and no `/select`. The fixture is a second collection; the helper now selects a replica of the requested collection on node zero explicitly. No production API added by the fix is referenced by the new tests.

Do not modify the contributor's fork, PR head/body, apache/solr, or Jira. The inherited tolerant-shard CPU accounting issue is separate PR 5239 / SOLR-18591 and is outside this repair. The old PR 5232 receipt does not prove this candidate.

## Formatting and positive proof

Run `./gradlew tidy` at F1 and require a clean tree. If tidy changes scoped files, commit as Nick Shanin (`nick.boss.us@gmail.com`) with no trailers, push plainly to this fork branch, and add a superseding gate naming the resulting F2 before proving it. Record inherited formatting changes separately. Do not silently prove a dirty tree or carry proof from an older head.

Run the complete focused class from fresh results:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestListenerTest -Ptests.seed=5238C0DE
```

Require all 20 methods executed, zero failures/errors/skips, including `testCpuTime`. Verify specifically:

- `testV2OnlySearchReported`: v1 lookup is null, a real v2 request returns nonempty documents, exactly one event belongs to the original node, and the handler path is `/v2-only-select`.
- `testEmbeddedQtFallbackReported` and `testEmbeddedDefaultFallbackReported`: `/select` is absent; each embedded query succeeds with nonempty documents, exactly one event on node zero, and context path `/select`. The explicit qt case uses `standard`; the other exercises the default alias.
- Existing ping, pathless internal, shard and export exclusions, both nested subquery tests, top-level embedded, ordinary v1/v2, failure/partial-result, listener exception, CPU and no-listener behavior remain green.

Run affected neighbors from fresh results, retaining evidence separately:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.PingRequestHandlerTest --tests org.apache.solr.core.TestSolrConfigHandler --tests org.apache.solr.request.TestSolrRequestInfo --tests org.apache.solr.response.transform.TestSubQueryTransformerDistrib -Ptests.seed=5238C0DE
```

Require zero failures/errors/skips and report actual counts. Run `./gradlew check -x test`, preserving its real result and any failing task. On vm4 and earlier listener/license gates the host shell's `NODE_ENV=production` skipped ref-guide devDependencies. Inspect this condition; a per-run `env -u NODE_ENV ./gradlew check -x test` is permitted and must be recorded explicitly. Do not silently skip Antora or change the host's persistent environment. An environment failure before any tests execute may be corrected and relaunched once, with both attempts retained. Finish with clean tidy, `git diff --check`, and a clean exact-head checkout.

## Negative proof on P with only the test delta

Use a separate clean detached checkout of P. Overlay ONLY these three files from the final candidate being proved:

- `solr/core/src/test/org/apache/solr/handler/component/SearchRequestListenerTest.java`
- `solr/core/src/test-files/solr/configsets/search-request-listener-routing/conf/schema.xml`
- `solr/core/src/test-files/solr/configsets/search-request-listener-routing/conf/solrconfig.xml`

Keep all four changed production files at P. Record the overlay's file hashes and synthetic tree/diff. Run:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestListenerTest.testV2OnlySearchReported --tests org.apache.solr.handler.component.SearchRequestListenerTest.testEmbeddedQtFallbackReported --tests org.apache.solr.handler.component.SearchRequestListenerTest.testEmbeddedDefaultFallbackReported -Ptests.seed=5238C0DE
```

Require all three methods to execute and fail on the callback count assertion, expected one event and actual zero, after the handler-registration and nonempty-response assertions succeed. A compile/setup/route failure, skip, missing method or passing parent does not prove the finding. If the premise differs, investigate and repair the test/fix with a documented new head and superseding gate rather than accepting a different failure or blindly retrying.

## Receipt and completion

GREEN requires all positive and negative legs, clean formatting, successful root checks and preserved neighboring behavior at the exact final remote head. Retain complete logs and fresh XML; report commands, seed, actual counts, CPU execution, negative assertion messages, host/toolchain and every repair or material execution condition. If a test executed and failed, follow the board repair workflow before retrying.

Write the named receipt and an add-only completion report. The receipt's final section should draft a concise upstream follow-up with the pinned P-to-tested-head diff and fast-forward pull-in commands. Return the receipt for Windows review; this gate authorizes no public posting. A later PR review comment is governed by Nick's standing exact-head clean-gate policy, including a fresh live PR/discussion read and ancestry check.
