# Assignment: PR #5156 (SOLR-9396) Ref Guide update (round 1)

Status: DONE (claim claims/pool-windows-pr5156-refguide-round-1.md; deliverable reports/pool-pr5156-refguide-round-1.md)
Intended host: Any host with implementation capability.

Posted by the phone agent at the owner's direction, 2026-10-22. Source: Eric Pugh's "Nick-Boss-Tech Breach Report" (Claude.ai artifact, October 2026, Refresh 2), quoted on the dev list. Claim before starting by adding `claims/pool-<host>-{name}.md` per WORKFLOW.md. Verify every head below by ls-remote at claim time; the artifact is a snapshot and heads may have moved. If a tip has moved, work at the live tip and record it.

## The critique

the subquery transformer asks the index for the fields its subquery references, so those fields no longer have to be listed in `fl`. The artifact marks this PR ✗ NEEDS REF GUIDE: a real capability with no documentation.

## Branch

- PR #5156, branch `solr-9396-submit` at `f62c06588f` when checked 2026-10-22. Ticket: SOLR-9396.
- Final branch head: `ef437b5feafe4e9607c5b4729de70f68405aa3b1`, verified on the fork and PR #5156 on 2026-10-23.

## Work

1. Read the diff and the ticket. Find the Ref Guide page or pages that document this behavior today (search `solr/solr-ref-guide` for the feature's parameters and transformer or function names).
2. Write the update in the guide's own style: state the new behavior, the parameter or syntax it involves, and any interaction a reader needs (for example what happens when the old and new forms are combined). Keep it concise; the repo's AGENTS.md asks for concise documentation aimed at the end user.
3. Commit the documentation to the branch. Verify the page builds or at least parses as AsciiDoc within the guide's structure; if a full guide build is impractical on the claiming host, record that the build was not run and what syntax check stood in for it.
4. Record the new page state in the branch's receipt and note in the report whether the PR description's documentation claims need an edit to match (make the edit if they do).

## Deliverable

`reports/pool-pr5156-refguide-round-1.md`: the pages changed, the final head, the build or syntax verification result.

## Limits

Pushes to the branch are authorized (it is the owner's own open PR). No PR state change, no comments, no Jira edits. If the work changes the PR's claims, update the PR description to match and record the edit in the report; description edits on the owner's own PRs carry standing authorization. No em dashes. Commit as Nick Shanin with no trailers.
