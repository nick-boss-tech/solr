# Assignment: SOLR-17850 SearchHandler UI form, scoped implementation (Christos reply, round 1)

Status: OPEN. Capability tags: implementation, records. Host: any capable pool Linux host.

Trigger: Christos Malliaridis replied on SOLR-17850 on 2026-10-30 (comment 18134872), setting the scope: the main elements (defType, q.op, wt) go in the same PR; highlighting, faceting, spatial and spellcheck wait for proper designs because they add too many fields; "Feel free to create a PR, add the missing fields and leave out the toggles at the bottom of the form for now."

Work:
1. Locate any existing 17850 work (branch census via ~/workspace/tools/solr-work.sh SOLR-17850).
2. Implement to his scope: main elements plus the missing fields, bottom toggles left out, the design-pending groups untouched.
3. Prepare a draft PR package: branch tidy at its head, PR body per pr-formula.md with the scope he set stated in Limits.
4. Do NOT open the PR and do NOT post on Jira. Opening waits for Nick's explicit go. UI PRs open as drafts.

Deliverable: reports/christos-reply-17850-round-1.md with the branch, head, field inventory against his scope, and the prepared body text.
