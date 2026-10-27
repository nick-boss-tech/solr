# Assignment: SOLR-17887 PR preparation after Christos's reply

Status: OPEN
Tags: implementation, records
Tag note: the `ui` tag was removed at Nick Shanin's direction on 2026-10-27 so any capable pool host can claim this; only vm6 held `ui`, which left the other pool hosts unable to take Christos's assignments. The ui build facts are in `UI-PLAYBOOK.md`.
Posted at Nick Shanin's direction on 2026-10-27: every reply from Christos Malliaridis gets its own assignment. This is the assignment for his SOLR-17887 reply.

## Source

Christos Malliaridis, Jira comment 18133017 on SOLR-17887 (2026-10-27): the FileTree scope is confirmed. A single integration so the component does not lie around as dead code could also be implemented, or preferably be defined in a separate ticket later. "Feel free to create a PR and I will pick that up and review it, later I can create follow-up tickets that define the components that should use this composable."

## Work

Candidate branch: `solr-17887-file-tree`, gated head `9c7f72aa0e` (verify the live fork tip before starting; the gated head is the substance of the tip).

1. Verify the branch still compares clean against current apache main and that its gate record stands at the live head. If main has moved under it, record what a rebase would change and stop for the owner if the diff content would change; a content-neutral rebase may be prepared but not pushed without recording it here first.
2. Draft the PR description to `drafts/pr-solr-17887-file-tree.md` following `pr-formula.md`, naming Christos as the sponsor and promised reviewer from his comment. The Limits section names the integration follow-up as Christos's separate ticket to create later; do not file any ticket.
3. Stop at "PR ready to open, back to the owner." The opening itself needs Nick's explicit go.

## Deliverable

A short report at `reports/christos-reply-17887-pr-prep-round-1.md`: live head verified, compare result, gate record cited, PR draft location, and anything that would change the branch before opening.

## Limits

No PR opening, no Jira post or comment, no GitHub comment. No scope change to the component.
