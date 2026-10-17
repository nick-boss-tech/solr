# SOLR-7390 (PR 5136): QueryElevationComponentTest at head, check result

Assignment: assignments/pool-review-2026-10-16-code-round-1.md, item 2. Claim: claims/pool-review-2026-10-16-code-round-1-item-2.md. Run by vm2, 2026-10-17, 04:10Z to 04:20Z. Runner gates/g7390-item2.sh; log logs/vm2/7390-item2-head.log; summary logs/vm2/7390-item2-result.txt (both on vm2 under /workspace/gates/logs/vm2/).

## Status: CONFIRMED, escalated

The finding reproduces at the head: `testMarker` fails with the unregistered-transformer error. Per the assignment's decision row for item 2, the test expectation was not changed and no source was edited. Escalated to the main agent for the choice between the two behaviors below.

## Head

- Branch solr-7390-submit, live at the start of the run: 7463dd006a7825db46f00b8a4790650c5bb51e61 (ls-remote matched). Commits 4ca1ce4da1 (unknown transformer in fl is a 400) and 7463dd006a (changelog fragment).

## Command and seed

`./gradlew --no-daemon --console=plain -Ptests.workDir=/tmp/7390-tests -Ptests.seed=7390C0FFEE7390 -Dtests.seed=7390C0FFEE7390 :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.QueryElevationComponentTest`

Focused run at the head only. No base run, no proof leg, no module check, as the item asks for the focused class.

## Result, counted from fresh JUnit XML

- QueryElevationComponentTest: tests=16, failures=1, errors=0, skipped=0.
- Failing test: testMarker, at QueryElevationComponentTest.java:1072 (the assertQ call; its request is at line 1074).
- Failure: `java.lang.RuntimeException: Exception during query`, wrapping `org.apache.solr.common.SolrException: Unknown DocTransformer: elev`, thrown from SolrReturnFields.add (SolrReturnFields.java:387) during parseFieldList.
- The other 15 tests pass.
- Head gradle rc=1. Tree clean afterward.

## Marker name check

The finding's open question is settled. solrconfig-elevate.xml sets no editorial marker field name, so the default `elevated` applies (QueryElevationComponent.java:135, parseEditorialMarkerFieldName reads the init arg and falls back to the default). The request at line 1074 asks for `[elev]`, which is not registered, so it is rejected.

## Choice for the main agent (not decided here)

1. Keep the 400 and change the test to request `[elevated]` instead of `[elev]` (this is the test-side change the decision row does not allow the claimant to make).
2. Register `elev` as a marker alias, so the request succeeds (a production behavior change).

Option 1 keeps the current behavior of the branch. Option 2 changes what the branch does with an unknown transformer name.

## Not done

- Items 1, 3, 4, 5 of the assignment: not taken under this claim.
- No gate job filed. No fix commit. Branch solr-7390-submit unchanged.
