# SOLR-10424 (PR 5159): V2 id-less update check, second retry

Status: DONE. Verdict: CONFIRMED (escalated to the main agent for the fix shape).

Assignment: assignments/pool-review-2026-10-17-10424-retry-round-1.md. Claim: claims/pool-review-2026-10-17-10424-retry-round-1.md. Run by vm2, 2026-10-17, from 06:19Z to 06:37Z. Runner gates/g10424-retry.sh; summary logs/vm2/10424-retry-result.txt; logs logs/vm2/10424-retry-HEAD.log and logs/vm2/10424-retry-MAIN.log. Seed 10424C0FFEE10424, passed as -Ptests.seed and -Dtests.seed.

Scratch test: reports/2026-10-16-pr-batch/SOLR-10424-scratch-test.java.txt (the file vm2 ran; not committed to the fork). Earlier INCONCLUSIVE result (first retry, 05:35Z) was superseded: the test itself had two bugs, fixed here (see Requests).

## Requests

- V2 `/api/collections/tp/update/json?commit=true`, body `[{"name":"v2-path"}]`, via the cluster client.
  - Head 5b087b1a38: HTTP 400, `Document is missing mandatory uniqueKey field: id`. No document added.
  - Main 4b5b922383: status 0, `responseHeader={status=0}`. One document added, with a UUID id (the test asserts one new id that matches the UUID pattern, and it passes).
- Legacy `/tp/update/json/docs`, body `[{"name":"docs-path"}]`, via GenericSolrRequest with setRequiresCollection(true) on the cluster client.
  - Head 5b087b1a38: status 0. One document added, with a UUID id (test passes).
  - Main 4b5b922383: status 0. One document added, with a UUID id (test passes).

Test bugs fixed from the first retry, both found in this round's evidence:
1. Count by `name`. Main drops the `name` field on both paths (the techproducts schema does not declare it, and main's default update chain does not keep unknown fields), so counting by name read 0 on main. The test now counts the ids added by each request (ids before and after the request), which does not depend on `name`.
2. Routing. The legacy request lacked setRequiresCollection(true), so the request went to the node root or to no live server. With the flag set, it reaches the collection.

## Two legs

- Head 5b087b1a389c892850401f79154824607cb338ac, log 10424-retry-HEAD.log, fresh XML 06:26:32Z: tests=2, failures=1, errors=0, skipped=0. Failure: testIdlessDocOnV2UpdateJsonGetsUuid (the HTTP 400 above). testIdlessDocOnUpdateJsonDocsGetsUuid passes.
- Main 4b5b922383, log 10424-retry-MAIN.log, fresh XML 06:36:05Z: tests=2, failures=0, errors=0, skipped=0. Both pass.

## Verdict

CONFIRMED. Upstream main gives the V2 document a UUID id. The head rejects the same V2 request with a missing-uniqueKey error, so the head adds no document and no UUID. The legacy path gives a UUID on both legs, so the loss is specific to the V2 route.

Context from the head's configset: solr/server/solr/configsets/sample_techproducts_configs/conf/solrconfig.xml adds an initParams block for /update/json/docs with update.chain json-docs-uuid. Main has no such block. The V2 route on the head is rewritten to /update/json/docs (UpdateAPI.java lines 44 and 60), but the head's V2 request still gets the missing-id error, so the chain attached to the legacy path does not reach the V2 request.

Not done: no fix, no fork commit, no PR or comment. The fix shape is the main agent's decision (the decision row in the assignment).
