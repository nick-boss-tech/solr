# Assignment: SOLR-17914 PR preparation after Christos's reply

Status: OPEN
Tags: implementation, records
Tag note: the `ui` tag was removed at Nick Shanin's direction on 2026-10-27 so any capable pool host can claim this; only vm6 held `ui`, which left the other pool hosts unable to take Christos's assignments. The ui build facts are in `UI-PLAYBOOK.md`.
Posted at Nick Shanin's direction on 2026-10-27: every reply from Christos Malliaridis gets its own assignment. This is the assignment for his SOLR-17914 reply.

## Source

Christos Malliaridis, Jira comment 18133018 on SOLR-17914 (2026-10-27, edited the same day): the branch implements the former designs of the create collection dialog; new designs are still work in progress in Figma. For the meantime the former designs and the branch can be integrated as a temporary way of creating collections. "So feel free to create a PR for that so that I can review it."

## Work

Canonical candidate branch: `solr-17914-create-collection-dialog-stacked` at `5ea0055190` (per the Figma wrap-up reconciliation; the non-stacked branch is not the PR vehicle). Verify the live fork tip before starting.

1. Verify the stacked branch's gate record and its compare against its base. The branch is stacked on the SOLR-17913 collections details card work, and SOLR-17913 has no PR and no reply from Christos yet. State plainly in the deliverable what opening this PR requires: a SOLR-17913 PR first, a PR targeted at the 17913 branch, or a restack onto main; prepare the branch state for the first of these that the record supports and flag the choice for the owner. Do not silently restack.
2. Draft the PR description to `drafts/pr-solr-17914-create-collection-dialog.md` following `pr-formula.md`, naming Christos as sponsor and promised reviewer, and stating in the description that this implements the former designs as a temporary integration while the new Figma designs are still in progress, citing his comment.
3. Stop at "PR ready to open, back to the owner." The opening itself needs Nick's explicit go.

## Deliverable

A short report at `reports/christos-reply-17914-pr-prep-round-1.md`: live head verified, stacking dependency stated with the options, gate record cited, PR draft location.

## Limits

No PR opening, no Jira post or comment, no GitHub comment. No redesign work toward the in-progress Figma designs.
