# Assignment: validate the V2 `wt` routing bug on a non-hosting node, and decide on a JIRA, round 1

Status: OPEN (posted by vm6, 2026-10-29).

- Tags: validation, records, draft, jira-decision.
- Intended host: a host that can run a Solr Gradle test on JDK 21 (vm6 or vm2 are known to). Windows only if the Gradle test suite is known to run there; if unsure, say so in the receipt and use a Linux host.
- Owner request: validate the explanation in `receipts/solr-wt-ignored-non-hosting-node-round-1.md` against upstream `main`, check JIRA for an existing report, and recommend whether to open a ticket. Draft only. Do not post.

## Background (read first)

- Found while gating SOLR-16398 round 3 (`receipts/SOLR-16398.md`). A V2 request with `?wt=javabin` returns JSON when the receiving node hosts no replica of the collection and the endpoint is a local Jersey resource. The client then throws `Expected mime type in [javabin, octet-stream] but got application/json`. The work done by the request still happens.
- Explanation and options: `receipts/solr-wt-ignored-non-hosting-node-round-1.md`. Earlier "not reproduced" conclusions in `receipts/solr-v2request-default-parser-wire-vm6.md` are superseded by it.
- Current evidence was taken on the SOLR-16398 branches, which are based on `74197140a666d4f0396b68484c2bf9df7e79221e`. The routing and filter files are identical on that base and on those branches. Upstream `main` is now `852b427c1154ce9eec988b9adaeb212294442126`. The bug has not been checked on that head.
- Not binary-compatibility. Not related to SOLR-18600.

## The code involved (base `74197140a6`)

- `solr/core/src/java/org/apache/solr/api/V2HttpCall.java`, `init` (around lines 150-185): when the collection has no local core, `extractRemotePath` sets `action = REMOTEPROXY`. The branch then sets `ADMIN_OR_REMOTEPROXY` and returns before `initAdminRequest`, so `solrReq` stays null.
- `V2HttpCall.java`, `invokeJerseyRequest` (around line 326): `containerRequest.setProperty(RequestContextKeys.SOLR_QUERY_REQUEST, solrReq)` stores null.
- `solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java` (around lines 66-79): reads that property, sees no `wt`, and sets `Content-Type: application/json`.

## Evidence the reviewer should check (paths are on vm6 unless marked as board files)

1. `receipts/solr-wt-ignored-non-hosting-node-round-1.md` (board).
2. Probe source: `assignments/solr-wt-non-hosting-node-round-1/ProbeRoutingTest.java` (board). Output of the gate run `solr16398-probe-routing`: `/workspace/gates/logs/vm6/solr16398-probe-routing/focused-tests.xml/TEST-org.apache.solr.handler.admin.api.ProbeRoutingTest.xml`. Expected: `PROBE hosted default split OK`; `PROBE other default split RemoteSolrException ... Expected mime type`. The log also shows `sqr=false wt=noparams ... val=null` for the other node, but only when the two temporary prints in step 3 are added.
3. Debug trace (temporary prints, not committed): `/workspace/gates/logs/vm6/solr16398-split-dbg2/focused-tests.xml/TEST-org.apache.solr.handler.admin.api.CollectionCommandsV2IntegrationTest.xml`, containing `V2DBG setSqr ... solrReqNull=true` and `FILTERDBG ... val=null`.
4. Round 3 failing run (intermittent, seed-dependent): `/workspace/gates/logs/vm6/solr16398-r3-green/focused-tests.xml/TEST-org.apache.solr.handler.admin.api.CollectionCommandsV2IntegrationTest.xml`.

## Questions to settle

1. Does the bug reproduce on upstream `main` at `852b427c11`? Use a scratch clone. Copy `ProbeRoutingTest.java` into `solr/core/src/test/org/apache/solr/handler/admin/api/`. Run `:solr:core:test --tests org.apache.solr.handler.admin.api.ProbeRoutingTest` with the host lock. Report the two `PROBE` lines verbatim.
2. Does the mechanism match? Add the two temporary prints from the debug run (one in `V2HttpCall.invokeJerseyRequest` after `setProperty`, one in `MediaTypeOverridingFilter` before the `wt` check). Confirm that the non-hosting leg shows `solrReqNull=true` and `val=null`, and the hosting leg shows `sqr=true`. Revert the prints afterwards and say so in the receipt.
3. Is it limited to split and move? Pick two more JAX-RS endpoints that run locally and return a SolrJerseyResponse (for example `POST /api/collections/{c}/rebalance-leaders` on a one-shard collection, and one read endpoint such as `GET /api/collections/{c}`, if it is a Jersey resource on that base). Send each with `wt=javabin` to the non-hosting node. Report the Content-Type or mime error for each. Only check, do not fix.
4. Duplicates: search JIRA (anonymous REST, read-only) for existing reports. Suggested queries: `project=SOLR AND text ~ "wt=javabin"`, `project=SOLR AND text ~ "MediaTypeOverridingFilter"`, `project=SOLR AND text ~ "mime type" AND text ~ "javabin"`, `project=SOLR AND text ~ "REMOTEPROXY"`, `project=SOLR AND text ~ "remote proxy" AND text ~ "v2"`. List any hit with key, status and a one-line reason it does or does not match.
5. Recommendation: open a JIRA, comment on an existing ticket, or neither. Give the reason. If open, draft the ticket under `jira/` (summary, reproducer, expected/actual, the two candidate fixes from the receipt, affected versions). Do not file or post it.

## Optional (only if time allows; do not block on it)

- Try option 1 or option 2 from the receipt in the scratch clone. Confirm the non-hosting leg returns javabin and the mime error goes away. Report the diff size. Do not push to any fork or branch.

## Deliverables

- Receipt `receipts/solr-wt-non-hosting-validation-round-1.md`: host and OS, JDK, the `main` head used, the two `PROBE` lines, answers to questions 2-5, the duplicate search results, and any step that could not be run.
- `jira/solr-wt-non-hosting-ticket-draft-round-1.md` only if the recommendation is to open a ticket.
- Set this assignment's status to DONE on the board with the receipt path.

## Constraints

- Read and probe only. No commit to any fork branch, no PR, no JIRA or GitHub post, no board write other than this assignment's records.
- Revert every temporary print and remove the probe test from the scratch clone before finishing. Record that in the receipt.
- Scratch work under a new directory; commit identity Nick Shanin, no trailers, for any commit (none expected).
- Take a claim before running Gradle, and heartbeat at least hourly.
