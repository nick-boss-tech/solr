# SOLR-7390 (PR 5136) check: QueryElevationComponentTest at head

Status: escalated (test fails at head; the test expectation was not changed, per the decisions table).

- Head: `solr-7390-submit` at `7463dd006a7825db46f00b8a4790650c5bb51e61`.
- Command: `:solr:core:test --rerun --tests org.apache.solr.handler.component.QueryElevationComponentTest` with `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1`, run through `pool/bin/sweep-run.sh`.
- Result: 16 tests, 1 failure: `QueryElevationComponentTest.testMarker`. Conclusive: the failure reproduces with the seed below and the cause is pinned to one commit.
- Seed: `997E388750B5B333`.
- Log: `/workspace/gates/logs/vm5/code-round-1/item2-7390-r1.log` (XML in `item2-7390-r1.xml/`).
- Failure: `testMarker` line 1072 (the `[elev]` request, `q=AAAA&fl=id, score, [elev]`) returns status 400, `Unknown DocTransformer: elev`.

## Cause

Commit `4ca1ce4da1` ("SOLR-7390: unknown document transformer in fl is a 400 Bad Request", Nick Shanin, 2026-10-06) changes `solr/core/src/java/org/apache/solr/search/SolrReturnFields.java` (line 387) to throw `BAD_REQUEST` for an unregistered bracketed transformer. The same commit updates only `ReturnFieldsTest`. `QueryElevationComponentTest.testMarker` still expects an unknown `[elev]` to be silently ignored (`not(//result/doc[1]/bool[@name='[elev]'][.='false'])`, with the comment "there is no Transformer registered w/ that, so we shouldn't get a result").

The test config is not the cause. `testMarker` uses `solrconfig.xml` through `init("schema12.xml")`, and no test config overrides the editorial marker name, so the default `elevated` applies and `elev` is unregistered.

## Escalation

The choice is between the two behaviors: keep the 400 and update `testMarker` to expect it, or revert the 400 in `SolrReturnFields`. Per the assignment's decisions table, the test expectation was not changed. Main agent to decide.
