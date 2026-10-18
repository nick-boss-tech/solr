# SOLR-7390 (PR 5136) check: QueryElevationComponentTest at head

Status: resolved. The stale test expectation is updated on the PR branch in commit `edd451863f`; the focused test passes on that head.

- Head: `solr-7390-submit` at `7463dd006a7825db46f00b8a4790650c5bb51e61`.
- Command: `:solr:core:test --rerun --tests org.apache.solr.handler.component.QueryElevationComponentTest` with `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1`, run through `pool/bin/sweep-run.sh`.
- Result: 16 tests, 1 failure: `QueryElevationComponentTest.testMarker`. Conclusive: the failure reproduces with the seed below and the cause is pinned to one commit.
- Seed: `997E388750B5B333`.
- Log: `/workspace/gates/logs/vm5/code-round-1/item2-7390-r1.log` (XML in `item2-7390-r1.xml/`).
- Failure: `testMarker` line 1072 (the `[elev]` request, `q=AAAA&fl=id, score, [elev]`) returns status 400, `Unknown DocTransformer: elev`.

## Cause

Commit `4ca1ce4da1` ("SOLR-7390: unknown document transformer in fl is a 400 Bad Request", Nick Shanin, 2026-10-06) changes `solr/core/src/java/org/apache/solr/search/SolrReturnFields.java` (line 387) to throw `BAD_REQUEST` for an unregistered bracketed transformer. The same commit updates only `ReturnFieldsTest`. `QueryElevationComponentTest.testMarker` still expects an unknown `[elev]` to be silently ignored (`not(//result/doc[1]/bool[@name='[elev]'][.='false'])`, with the comment "there is no Transformer registered w/ that, so we shouldn't get a result").

The test config is not the cause. `testMarker` uses `solrconfig.xml` through `init("schema12.xml")`, and no test config overrides the editorial marker name, so the default `elevated` applies and `elev` is unregistered.

## Resolution

- The test expected an unregistered `[elev]` to be silently ignored. Commit `4ca1ce4da1` changed that behavior on purpose: an unknown bracketed transformer in `fl` is a 400, and the same commit's `ReturnFieldsTest` change expects it. The test was stale.
- Fix: `QueryElevationComponentTest.testMarker` now expects a 400 with `Unknown DocTransformer: elev`, using `expectThrows`. Commit `edd451863f` on `solr-7390-submit`, pushed fast-forward on top of `7463dd006a`, which updates PR 5136.
- Rerun at `edd451863f`: `:solr:core:test --tests org.apache.solr.handler.component.QueryElevationComponentTest`, seed `676940F74E50DCB`, BUILD SUCCESSFUL, all 16 tests pass including `testMarker` (1.62 s). Log: `/workspace/gates/logs/vm5/code-round-1/7390-fix-r1.log`.
- The earlier failing run (seed `997E388750B5B333`, head `7463dd006a`) is not rerun unchanged. Its result stands for the old head.

The escalation to the main agent is withdrawn: there is no main agent. The decision was made under the PR's stated intent, which is the commit title and the PR body's behavior change.
