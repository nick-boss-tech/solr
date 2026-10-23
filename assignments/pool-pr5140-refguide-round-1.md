# Assignment: PR #5140 (SOLR-8003) Ref Guide update (round 1)

Status: DONE (claim claims/pool-vm5-pr5140-refguide-round-1.md; deliverable reports/pool-pr5140-refguide-round-1.md)

Posted by the phone agent at the owner's direction, 2026-10-22. Source: Eric Pugh's "Nick-Boss-Tech Breach Report" (Claude.ai artifact, October 2026, Refresh 2), quoted on the dev list. Claim before starting by adding `claims/pool-<host>-{name}.md` per WORKFLOW.md. Verify every head below by ls-remote at claim time; the artifact is a snapshot and heads may have moved. If a tip has moved, work at the live tip and record it.

## The critique

a glob pattern (`fl=prefix*`) combined with a raw value transformer now writes every matching field raw. The artifact marks this PR ✗ NEEDS REF GUIDE: documented behavior changed and no Ref Guide page says so.

## Branch

- PR #5140, branch `solr-8003-submit` at `a24414bf3d` when checked 2026-10-22. Ticket: SOLR-8003.

## Work

1. Read the diff and the ticket. Find the Ref Guide page or pages that document this behavior today (search `solr/solr-ref-guide` for the feature's parameters and transformer or function names).
2. Write the update in the guide's own style: state the new behavior, the parameter or syntax it involves, and any interaction a reader needs (for example what happens when the old and new forms are combined). Keep it concise; the repo's AGENTS.md asks for concise documentation aimed at the end user.
3. Commit the documentation to the branch. Verify the page builds or at least parses as AsciiDoc within the guide's structure; if a full guide build is impractical on the claiming host, record that the build was not run and what syntax check stood in for it.
4. Record the new page state in the branch's receipt and note in the report whether the PR description's documentation claims need an edit to match (make the edit if they do).

## Deliverable

`reports/pool-pr5140-refguide-round-1.md`: the pages changed, the final head, the build or syntax verification result.

## Limits

Pushes to the branch are authorized (it is the owner's own open PR). No PR state change, no comments, no Jira edits. If the work changes the PR's claims, update the PR description to match and record the edit in the report; description edits on the owner's own PRs carry standing authorization. No em dashes. Commit as Nick Shanin with no trailers.
