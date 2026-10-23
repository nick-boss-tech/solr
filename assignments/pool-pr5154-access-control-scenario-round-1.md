# Assignment: PR #5154 (SOLR-6759) access-control scenario, with #5138 (round 1)

Status: DONE (claim claims/pool-vm5-pr5154-access-control-scenario-round-1.md; deliverable reports/pool-pr5154-access-control-scenario-round-1.md)

Posted by the phone agent at the owner's direction, 2026-10-22. Source: Eric Pugh's "Nick-Boss-Tech Breach Report" (Claude.ai artifact, October 2026, Refresh 2), quoted on the dev list. Claim before starting by adding `claims/pool-<host>-{name}.md` per WORKFLOW.md. Verify every head below by ls-remote at claim time; the artifact is a snapshot and heads may have moved. If a tip has moved, work at the live tip and record it.

## The critique

Class IV card for #5154: the expand path never called `complete()` on its post-filter collector chain; the reporter's own post filter was an access-control filter that releases documents only in its finishing step, so the expanded section could leak past an access-control boundary. "The shipped test only proves complete() now gets called, not that the access-control scenario itself is fixed." #5138 (SOLR-7520) is the same root cause in the grouping path and is to be reviewed alongside.

## Branches

- PR #5154, branch `solr-6759-submit` at `bdeb07d03b` when checked 2026-10-22. Ticket: SOLR-6759.
- PR #5138, branch `solr-7520-submit` at `15cc9ba5a6` when checked 2026-10-22. Ticket: SOLR-7520.

## Work

1. Read both tickets and both diffs. Build the reporter's scenario as a test on the expand path: a post filter whose documents are released only from `complete()`, an expand query, and an assertion that no unreleased document appears in the expanded section. Show it failing on the base production code (the leak) and passing at the #5154 head.
2. Check #5138's shipped coverage for the grouping path against the same scenario shape. If the grouped path lacks an equivalent scenario test, add it there too and show both directions.
3. Gate each branch you change, per the standard gate (changelog, tidy, Error Prone compile, focused run counted from fresh XML, module check for the touched module).

## Deliverable

`reports/pool-pr5154-access-control-scenario-round-1.md`: the scenario test and its both-directions results per branch, what was added where, the gate results, and the final heads.

## Limits

Pushes to the branch are authorized (it is the owner's own open PR). No PR state change, no comments, no Jira edits. If the work changes the PR's claims, update the PR description to match and record the edit in the report; description edits on the owner's own PRs carry standing authorization. No em dashes. Commit as Nick Shanin with no trailers.
