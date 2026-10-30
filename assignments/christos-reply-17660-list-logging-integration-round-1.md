# Assignment: SOLR-17660 list component, logging integration (Christos reply, round 1)

Status: OPEN. Capability tags: implementation, records. Host: any capable pool Linux host.

Trigger: Christos Malliaridis replied on SOLR-17660 on 2026-10-30 (comment 18134873): "Please include an integration for the list and create a pull request after. I believe the logging screen would be a good usage for this one that won't be changed any time soon. You may mock the API call in the PR if the API is not currently serving the data in the format we need it (e.g. filters not supported)."

Work:
1. Start from the existing 17660 branch. FIRST remove the factory workflow file the branch still carries; it must not reach a PR.
2. Integrate the list component into the logging screen; mock the API call if the current API cannot serve the needed format, and say so in the PR body if mocked.
3. Prepare a draft PR package: branch tidy at its head, PR body per pr-formula.md.
4. Do NOT open the PR and do NOT post on Jira. Opening waits for Nick's explicit go. UI PRs open as drafts.

Deliverable: reports/christos-reply-17660-round-1.md with the branch, head, confirmation the workflow file is gone, and the prepared body text.
