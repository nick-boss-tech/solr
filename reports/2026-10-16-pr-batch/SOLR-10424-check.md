# SOLR-10424 (PR 5159): V2 id-less update check, retry

Status: DONE. Verdict: INCONCLUSIVE (escalated to the main agent).

Assignment: assignments/pool-review-2026-10-17-10424-retry-round-1.md (posted d74dcb7ec7). Claim: claims/pool-review-2026-10-17-10424-retry-round-1.md. Run by vm2, 2026-10-17, 05:05Z to 05:35Z. Runner gates/g10424-retry.sh; summary logs/vm2/10424-retry-result.txt; logs logs/vm2/10424-retry-HEAD.log and logs/vm2/10424-retry-MAIN.log. Seed 10424C0FFEE10424, passed as -Ptests.seed and -Dtests.seed.

The scratch test is at gates/10424-retry/TechproductsV2IdlessUpdateTest.java, not committed to the fork. Test changes against the first check: (a) the legacy case posts the id-less document to /update/json/docs through GenericSolrRequest on collection tp, not through /api/collections; (b) the V2 case posts to /api/collections/tp/update/json with commit=true; (c) each request records its response body in the test output, and a commit runs before each count.

## Requests

| Leg | Request | Result |
| --- | --- | --- |
| Head 5b087b1a38 | V2 `/api/collections/tp/update/json?commit=true` | Error from server: `Document is missing mandatory uniqueKey field: id`. No UUID was assigned. HTTP status was not captured (SolrJ wrapper). |
| Head 5b087b1a38 | Legacy `/tp/update/json/docs` | SolrServerException: `No live SolrServers available to handle this request`. The request never reached the server. |
| Main 4b5b922383 | V2 `/api/collections/tp/update/json?commit=true` | Status 0, body `{responseHeader={rf=1, status=0, QTime=561}}`. Count of documents found by `name:v2-path`: 0. |
| Main 4b5b922383 | Legacy `/tp/update/json/docs` | SolrServerException: `No live SolrServers available to handle this request`. |

## Two legs

- Head 5b087b1a389c892850401f79154824607cb338ac, log 10424-retry-HEAD.log, fresh XML 05:23:46Z: tests=2, failures=2, errors=0, skipped=0. Failures: testIdlessDocOnV2UpdateJsonGetsUuid (error from the V2 request above); testIdlessDocOnUpdateJsonDocsGetsUuid (no live SolrServers).
- Main 4b5b922383, log 10424-retry-MAIN.log, fresh XML 05:34:59Z: tests=2, failures=2, errors=0, skipped=0. Failures: testIdlessDocOnV2UpdateJsonGetsUuid (`v2-path expected:<1> but was:<0>`); testIdlessDocOnUpdateJsonDocsGetsUuid (no live SolrServers).

## Verdict

INCONCLUSIVE. Neither leg shows a UUID for the V2 document:

- Upstream main accepted the V2 request (status 0) but the query found no document. A UUID on main is not established, so CONFIRMED does not hold.
- The head rejected the V2 request for a missing uniqueKey field. That is a failure, not a UUID, so REFUTED does not hold either.
- The legacy request failed in both legs with the same cluster-harness error seen in earlier SolrCloud probes (`No live SolrServers`). It gives no evidence about the legacy path.

Possible test defect, not verified: the count query `name:v2-path` is unquoted and the hyphen may split the term during parsing. The legacy count would have the same problem. A query on a fixed-value field, or a query for all documents, would separate the two causes. The decision row says INCONCLUSIVE with the responses recorded and escalation to the main agent. No fix and no fork commit were made.

Item 1 of code round 1 stays INCONCLUSIVE.
