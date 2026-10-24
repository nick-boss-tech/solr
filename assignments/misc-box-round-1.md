# Misc box, round 1: work with no ticket number and no PR number

- Status: OPEN. This file is a standing box, not a single task. It stays OPEN while any item below is unclaimed or unfinished; item state lives in the claim and report files, not here.
- Purpose: a home for useful work that belongs to no Jira ticket and no pull request, so it is neither forced into a ticket-shaped assignment nor lost. The owner adds items through the phone agent. Anyone may propose an item to the owner; proposing is not adding.
- Capability tags: per item.
- Claiming: one item per claim, claim file `claims/misc-<item-slug>.md`, one live claim per host, per WORKFLOW.md. Deliverables land in `reports/` unless the item says otherwise.
- Limits that apply to every item: nothing is posted to GitHub or Jira, no PR is opened, and no public text is drafted, unless the item itself says so with the owner's go recorded in it. Commit as Nick Shanin with no trailers. No em dashes in authored text.

## Item 1: benchmark, authorization check vs cache validation vs response generation

- Slug: `benchmark-cache-auth-vs-response`. Tags: `jvm-tests`, `records`. Intended host: any pool Linux host holding those tags.
- Question: for a read request to a core-level v2 API (the schema read is the canonical example), how expensive is each step per request: the authorization evaluation, the HTTP cache validation that answers a conditional request with 304, and generating the full response?
- Setup: a local test Solr built from the current origin/main head (record the exact SHA). One small collection (about 10,000 documents) on a configset that leaves the httpCaching element absent, so the code default applies. A security.json with the rule-based authorization plugin and a handful of rules, including a catch-all rule and a mapping for the permission the measured API requires.
- Measure, all requests made as a fully authorized user (this is a cost study, not a permission-outcome study):
  1. Full request, response generated (HTTP 200): median and p95 latency per request.
  2. Conditional request carrying the current ETag in If-None-Match, answered 304 with no response body generated: median and p95.
  3. The authorization evaluation isolated: the delta between identical full requests with the authorization plugin enabled and disabled; if the harness allows a direct microbenchmark of the authorizer call, report that too, labelled as a microbenchmark.
- Method discipline: same host, same JVM, a fixed warmup (at least 200 requests per path) before at least 1,000 measured requests per path; no other build or test load on the host during the runs; record JDK version, host, document count, and the exact request URLs and headers.
- Deliverable: `reports/misc-benchmark-cache-auth-vs-response-round-1.md` with the numbers table (one row per path: what it measures, median, p95, request count), the method and environment, and the path of the raw measurement log on the host.
- Limits: measurement only. No Solr code changes; any scratch harness stays in the worktree, uncommitted and unpushed. Do not measure or record permission outcomes for unauthorized callers; every request is authorized.
