# Assignment: PRs #5117 and #5146 pair review, CombinedQueryComponent overlap (round 1)

Status: OPEN

Posted by the phone agent at the owner's direction, 2026-10-22. Source: Eric Pugh's "Nick-Boss-Tech Breach Report" (Claude.ai artifact, October 2026, Refresh 2), quoted on the dev list. Claim before starting by adding `claims/pool-<host>-{name}.md` per WORKFLOW.md. Verify every head below by ls-remote at claim time; the artifact is a snapshot and heads may have moved. If a tip has moved, work at the live tip and record it.

## The critique

Class IV cards for both: #5117 (SOLR-18541) fixes CombinedQueryComponent.mergeIds returning wrong numFound, approximateTotalHits, and numFoundExact; #5146 (SOLR-8939) fixes date unique keys losing milliseconds and same-second date keys collapsing in the merge, and also touches CombinedQueryComponent.java, QueryComponent.java, and StoredFieldsShardRequestFactory.java. The artifact: "review the two together, since a fix to one can shift what the other needs to account for, not just conflict on lines." It also flags that #5147 (SOLR-9124) and #5158 (SOLR-10305) touch QueryComponent.java, so merge order among the four matters.

## Branches

- PR #5117, branch `combinedquerycomponent-merge-totals` at `81a88e40dd` when checked 2026-10-22.
- PR #5146, branch `solr-8939-submit` at `1a4a312bb7` when checked 2026-10-22.
- Read-only context: PR #5147 `solr-9124-submit` at `3dd985b93a`, PR #5158 `solr-10305-submit` at `52e32f892c`.

## Work

1. Read both diffs against their bases. Establish whether the two changes interact semantically: does #5146's key handling change any input to the totals #5117 corrects, and does #5117's merge change alter what #5146's date-key handling must produce? Answer with file and line citations, not impressions.
2. Establish a safe merge order for the four QueryComponent-touching PRs (#5117, #5146, #5147, #5158), or state that any order is safe and why.
3. Verdict per PR: OPEN-READY, FIX FIRST (fixes specified), or WON'T DO, each resting on the interaction analysis. Defects in repair scope follow the standing patch loop (fix, re-gate the focused classes, record); anything larger is a FIX FIRST verdict.
4. Do not re-gate what the records already prove at the live heads unless the interaction analysis gives a concrete reason; say which receipts were relied on.

## Deliverable

`reports/pool-pr5117-5146-pair-review-round-1.md`: the interaction analysis with citations, the merge-order statement, and one verdict per PR.

## Limits

Pushes to the branch are authorized (it is the owner's own open PR). No PR state change, no comments, no Jira edits. If the work changes the PR's claims, update the PR description to match and record the edit in the report; description edits on the owner's own PRs carry standing authorization. No em dashes. Commit as Nick Shanin with no trailers.
