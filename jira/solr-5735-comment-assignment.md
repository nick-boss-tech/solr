# Jira assignment: comment on SOLR-5735 (ChaosMonkey failures reproduce on current main)

Status: HELD (waiting for Nick's approval in the Decisions table; do not claim until it reads "approved")

- Capability tags: jira, comment.
- Intended host: the Windows host (hosts/windows.md lists the jira tag). Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: pool host vm2, 2026-10-23, at the direction of Nick.

## Task

Post one comment on https://issues.apache.org/jira/browse/SOLR-5735, using the text in `pr-drafts/SOLR-5735/jira-comment.md` verbatim. Do not change the text, the markup, or the header.

The comment reports that the ChaosMonkey tests still fail on current main, with a seed that reproduces the failures. It recommends keeping the ticket open.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Post the comment | default: do not post | if Nick has recorded "approved" in this row, post the text once; otherwise nothing | Nick |
| Ticket status | decided: no change | do not close, reopen, resolve, or change fields on SOLR-5735 | Nick |
| Other tickets | decided: none | do not comment on or edit any other ticket | Nick |
| Text edits | decided: none | if Jira rejects the text (length or markup), stop and record the rejection in the claim instead of editing the text | Nick |

No row may leave the choice to the claimant.

## Deliverable

`jira/solr-5735-comment-result.md`: the comment URL, and a one-line note that the posted text matches `pr-drafts/SOLR-5735/jira-comment.md` (re-read the posted comment to confirm). If the row says "do not post", the deliverable is the claim marked DONE with "not posted".

## Limits

- One comment. No second comment, no edits to the first, no status change, no other ticket.
- No PR, no GitHub comment, no code change. No force push.
- Commit as Nick Shanin with no trailers. No em dashes in anything authored around the posting.
