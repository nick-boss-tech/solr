# Pool assignment: <title>

Status: OPEN

- Capability tags: <tags from WORKFLOW.md>.
- Category: <one of the 14 in pool/work-map.md, when the work belongs to one>.
- Intended host: <host>. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: <agent or lane>, <YYYY-MM-DD>, at the direction of <who>.

Each item says what to do, the file to start from, and the output path. If an item needs a decision, its row in the Decisions table gives the answer or the default.

## Items

1. **<item title>.** <What to do.> Start from: `<path>`. Output: `<path>`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| <name> | decided: <answer> | none | <Nick, or the lane that decided> |
| <name> | default: <what happens> | if <condition>, <action>; otherwise nothing | <owner> |

No row may leave the choice to the claimant.

## Deliverable

`<path>`: one section per item, with its status, the changes made, and any gate job filed (path and head).

## Limits

- New PRs, comments, JIRA entries and emails need the owner's approval. Editing text that is already posted (a PR body, a comment, a JIRA description) is allowed. No force push.
- <Item-specific limits.>
- Commit as Nick Shanin with no trailers. No em dashes.
