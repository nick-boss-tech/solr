# Assignment: review the wt routing bug and decide what to do, round 1

Status: OPEN (posted by vm6, 2026-10-29). Supersedes `assignments/solr-wt-non-hosting-node-round-1.md`, whose questions 1 to 3 vm6 has answered (`receipts/solr-wt-non-hosting-validation-round-1.md`).

- Tags: review, records, draft, jira-decision.
- Intended host: any host that can clone `apache/solr` and read JIRA. Gradle is not needed; vm6 ran every build and probe and the evidence is in the receipts.
- Owner request: review the finding, check it against the code, search for existing reports, and decide the next step: whether to open a JIRA, comment on an existing one, or do neither. Also say which fix option the evidence favours, and whether the SOLR-16398 gates are affected. Draft only. Do not post.

## What is established (vm6, upstream main `852b427c1154ce9eec988b9adaeb212294442126`)

- `POST /api/collections/{c}/reload` with `?wt=javabin`, on a one-shard, one-replica collection in a two-node cluster: the node hosting the replica returns javabin and succeeds. The other node returns JSON, and the SolrJ client throws `Expected mime type in [javabin, octet-stream] but got application/json`. The reload still runs. Run `wt-main-clean`; excerpt in the receipt.
- Mechanism, confirmed with temporary prints (reverted): on the non-hosting node `V2HttpCall` returns early on the remote-proxy branch, so `solrReq` stays null. The request property is set to null (`V2HttpCall.java:326`). `MediaTypeOverridingFilter` reads null, skips the `wt` override, and falls through to JSON (`MediaTypeOverridingFilter.java:69-78`). Run `wt-main-dbg`.
- The servlet request is set on the container request unconditionally (`V2HttpCall.java:331`), even when `solrReq` is null. So the filter could read `wt` from it.
- Not testable on this head: split (no JAX-RS route, `Cannot find API`) and rebalance-leaders (not JAX-RS on `main`; the port is only on the SOLR-16398 branch). The SOLR-16398 round 3 split and move failures are consistent with this mechanism, but that is evidence on the branch, not on `main`.
- Not binary-compatibility. Not related to SOLR-18600.

## Questions to settle

1. Check the evidence against the code. Clone `apache/solr` at `852b427c11`, or read it on GitHub. Confirm the line references in `receipts/solr-wt-non-hosting-validation-round-1.md` and the mechanism in the excerpts. Say where you disagree.
2. Duplicates: search JIRA (anonymous REST, read-only). Suggested JQL: `project=SOLR AND text ~ "wt=javabin"`, `project=SOLR AND text ~ "MediaTypeOverridingFilter"`, `project=SOLR AND text ~ "mime type" AND text ~ "javabin"`, `project=SOLR AND text ~ "REMOTEPROXY"`, `project=SOLR AND text ~ "remote proxy" AND text ~ "v2"`, `project=SOLR AND text ~ "Expected mime type"`. For each hit, give the key, status, and one line on whether it is the same defect. Also check the ticket history for `V2HttpCall` and `MediaTypeOverridingFilter` for any earlier fix that changed this behaviour.
3. Fix options. The receipt lists two:
   1. Set the request object before the early return in `V2HttpCall`, so the filter sees `wt`.
   2. Make `MediaTypeOverridingFilter` read `wt` from the servlet request (`HTTP_SERVLET_REQ`) when the request property is null.
   Pick the one the evidence favours, or say neither. Give the reason with file and line references. Check whether option 2 affects any other response type or content negotiation, and whether option 1 changes routing or proxying.
4. Impact: does this affect SOLR-16398 (`gates/SOLR-16398-command-jaxrs.md`, DONE at `b135a03`)? Say whether the split and move tests in `CollectionCommandsV2IntegrationTest` can fail intermittently on that branch, and whether that should block the branch or be tracked separately. Check whether other JAX-RS endpoints on `main` are affected (reload is confirmed; list any others you can verify by reading code, without running them).
5. Decision (decision ladder, `material/decision-ladder.md`):
   - Rung 1 and 2: the evidence above, plus anything you find in the code.
   - Rung 3: the intent of the ticket or the conventions in `dev-docs/apis-v2-conventions.adoc`, if any applies.
   - Rung 4: if two fixes are defensible, pick the one with the strongest evidence and record the options.
   - Rung 5: opening a JIRA or posting a comment is a public commitment. Put it to the owner as one question with a recommendation. Do not post.
   Record which rung settles each point in your receipt.

## Deliverables

- Receipt `receipts/solr-wt-non-hosting-review-round-1.md`: questions 1 to 4 with your evidence, the duplicate search results, the fix choice and reasons, and the decision-ladder rung for each point.
- If you recommend a ticket: `jira/solr-wt-non-hosting-ticket-draft-round-1.md` with summary, reproducer (cite the probe at `assignments/solr-wt-non-hosting-node-round-1/ProbeRoutingTest.java` on the board), expected and actual, affected versions, and the fix option. Mark it "not posted".
- A one-question owner decision in your receipt if rung 5 applies, with your recommendation.
- Set this assignment's status to DONE on the board with the receipt path.

## Constraints

- No JIRA or GitHub post, no PR, no fork push, no code change to Solr.
- Board writes limited to this assignment's records.
- Commit identity Nick Shanin, no trailers, if you need a commit at all (none expected).
- Heartbeat your claim at least hourly. Take a claim before starting.
