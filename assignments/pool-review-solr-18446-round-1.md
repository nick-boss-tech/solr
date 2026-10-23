# Review assignment: SOLR-18446 path-scoped collection routing (round 1)

Status: OPEN

Posted by the phone agent at the owner's direction, 2026-10-22. One reviewer, one branch. Claim before starting by adding `claims/pool-<host>-review-SOLR-18446-round-1.md` per WORKFLOW.md, and work under that claim's lease.

## Why this review exists

Branch `solr-18446-path-routing-phone-agent-2` was built by phone-agent-2 and gated green (its gate was re-run by the phone agent on vm1). No one has read the change independently. The standing rule is that a host never audits its own closeout, and the owner wants an independent read before any PR opening. The reviewer must not be phone-agent-2 and must not be the phone agent.

## The branch

- Branch: `solr-18446-path-routing-phone-agent-2`, single squashed commit at `bc15134268e2fba2e29fb3fbc62546065906713d`. Verify the tip by ls-remote at claim time; if it has moved, stop and record the new tip here.
- Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Diff, 4 files: `solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java`; `solr/core/src/test/org/apache/solr/servlet/HttpSolrCallCloudTest.java`; `solr/core/src/test/org/apache/solr/cloud/AliasIntegrationTest.java`; `changelog/unreleased/SOLR-18446-path-controls-collection-routing.yml`.
- Records: `receipts/SOLR-18446.md` carries the gate evidence and the PR draft as its final section. Ticket: https://issues.apache.org/jira/browse/SOLR-18446

## What to review

Read the whole diff against the base, then answer each question in the report with file and line citations at the reviewed head.

1. Precedence: does the change make the URL path decide the collection in every path through HttpSolrCall, or are there entry paths where the `collection` parameter still wins? Name them.
2. Ticket fit: is path-over-parameter the behavior SOLR-18446 asks for, at the scope the ticket states? Quote the ticket text the answer rests on.
3. Aliases: AliasIntegrationTest changed in this diff. Establish why, and whether alias requests still resolve to the aliased collections as before. If the alias behavior changed, say whether that change is intended by the ticket or is a side effect.
4. Other callers: search the tree for code that relies on the parameter overriding the path (admin handlers, alias creation, internal forwarding). Report any reliance found, or state the searches run and that none was found.
5. Test quality: does the new HttpSolrCallCloudTest coverage discriminate? It must fail on the base production code and pass at the head. The gate record claims a passing proof leg; verify that claim from the gate log and fresh XML in the receipt's evidence, or rerun the focused class once per side if the evidence is not convincing.
6. Draft accuracy: read the PR draft in `receipts/SOLR-18446.md` against the diff. Every factual claim in it must hold. If one does not, correct the draft in the receipt and record the correction in the report.

## Patch loop

A defect in repair scope (small and mechanical: formatting, an import, a wrong constant, a test that does not compile) is fixed by the reviewer as one fast-forward commit on the branch, the focused class rerun at the new head, and the fix recorded in the report with its commit. Anything larger, and anything that changes the routing behavior beyond the ticket's ask, is a FIX FIRST verdict with the fix specified, not a repair. No history rewrite; if a repair seems to need one, stop and report instead.

## Deliverable

`reports/pool-review-solr-18446-round-1.md` with:

- The verdict, one of: OPEN-READY; FIX FIRST (each fix specified); WON'T DO (evidence attached).
- The six answers, each with citations.
- Any repair commits made, with their files and the rerun result.
- The reviewed head confirmed by ls-remote.

## Limits

No PR opening or state change, no comments, no Jira edits. No em dashes in any file written for this assignment. Commit as Nick Shanin with no trailers.
