# SOLR-18460 CoreAdmin v2 test-port scoping, round 1: premise does not match the ticket

Status: OPEN (premise defect). No scoping was done and no draft is proposed.

## What the assignment says

`assignments/eric-stream-stock-round-1.md`, unit 4: "SOLR-18460 CoreAdmin v2 test-port scoping. Eric's ticket on porting CoreAdmin tests to the v2 API."

## What the ticket says

SOLR-18460 (Resolved, Major, reporter Eric Pugh, created 2026-09-20): "Ensure all curl commands follow the same pattern." The description asks that Ref Guide curl examples use one order (curl, URL, headers, then body) and no `$` prompt. It is about documentation formatting, not about CoreAdmin or v2 tests.

Fixed on apache/solr main at the pinned SHA `e59ce3c97ddb`:
- `c3e18f1e45` SOLR-18460: Ensure all curl commands follow the same pattern (#4925)
- `e2cdb2d7e8` SOLR-18460: Drop the redundant -X GET from curl commands in the Ref Guide (#5007)

## Search for a CoreAdmin v2 test-port ticket

Jira search, 2026-10-27: `project = SOLR AND reporter = epugh AND text ~ "CoreAdmin"` returns four tickets: SOLR-18498 (Resolved, "Ensure examples work on Core Admin"), SOLR-18495 (Resolved, placeholder name in URL), SOLR-16599 (Open, ventilated prose in ASCII docs), and SOLR-15372 (Resolved, techproducts cores). None is about porting CoreAdmin tests to v2.

## Verdict

The unit cannot be done as written. Its named ticket is a resolved documentation-formatting ticket, and no open ticket by Eric matches the CoreAdmin v2 test-port description. Scoping a port without a ticket would invent the scope. The assignment needs correcting (either the ticket key or the description). This verdict is not a closure of any ticket.

## Reproduce

`git log --oneline --grep=SOLR-18460 e59ce3c97ddb`
`curl -s -G https://issues.apache.org/jira/rest/api/2/search --data-urlencode 'jql=project = SOLR AND reporter = epugh AND text ~ "CoreAdmin"'`
