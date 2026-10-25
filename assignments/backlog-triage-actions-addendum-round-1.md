# Addendum: backlog triage actions beyond the comment (round 1)

Status: OPEN, drafts and records only.
Tags: draft, records, review.
Parents: `assignments/backlog-first-100-pack-round-1.md` (rules) and `assignments/backlog-first-100-pack-slices-round-1.md` (units). This file adds actions; the parents' comment rules still govern the comment.
Formula: `jira/comment-formula.md` governs every Jira text. Jira has a footer disclosure and no header. A one-sentence comment carries no disclosure line.

## Why

The dev list asked for more than a comment on each backlog record: reply to the reporter with clarifying questions where the report is not enough to act on, set a better severity, classify the component, set a label, and, for complex items, help land a design. Each action becomes a reviewable draft attached to the record. Nothing changes a JIRA field; the owner approves each post.

## Order for each record (decision ladder rung 3, then rung 4 for the dependencies)

1. **Clarifying question to the reporter**, first. The dev-list request names it first, and its answer is the input the next steps need when a report is thin.
2. **Severity and component proposals**, together, after the reporter has answered or the report is complete. Both are proposals, not field edits.
3. **Label proposal**, after the component is proposed. A JIRA label is not ours to set: the pool account may not be allowed to add labels, and this is an open question for the owner (below). Until it is answered, the label is written as a line in the drafted comment that names the proposed label, not as a JIRA field change.
4. **Design routing**, last, and only where the record is complex (new behavior, API or format change, or several components). A design-routing draft names a proposed design owner and dev thread; it does not write the design.

## Drafts and where they go

- Clarifying question: `jira/jira-comment-<KEY>-clarify-draft.md`. Jira wiki markup, first person, addressed to the reporter with `[~REPORTER]`, asking only what the record cannot answer. Formula footer disclosure. The text is a question, so it does not follow the ask-comment shape.
- Severity, component, label and design proposals: `jira/jira-triage-<KEY>.md`, one file per key, one section per action. Records, not comments.
- Each draft is listed in `jira/COMMENTS.md` under "Drafts in hand, not posted", one line per key, by an add-only append.
- Triage index: `reports/backlog-triage-round-1.md`, listing each key, the actions drafted, the live-check time, and every drop.

## Proposal rules

- Severity: proposed priority with one evidence line (impact, scope, regression, as the report states them). Critical and Blocker remain excluded from the pack.
- Component: proposed name with path or code evidence. The live JIRA component list must be checked at fill time; the board export has no components. A proposal that does not match a live component is dropped and listed.
- Label: proposed from labels already in use on Solr issues (the archived export has 1,030 distinct labels, e.g. `newdev`, `performance`, `security`). A label not in use is listed as an open question, not proposed.

## Live check and drops

Live check at fill time for every action: status still Open, not the owner's ticket, no owner comment already on it. A record that fails drops out of every action for that key and is listed in the index with the reason.

## Flag clearing (decided by vm6, owner may reverse)

The conflict flags on the closure-family records are cleared for the pack, per the claim note `claims/backlog-first-100-unit-3.md` (status note 2026-10-25T18:00Z; pushed 17:52Z). Critical and Blocker stay excluded by priority.

## Open questions for the owner

- Can the pool's JIRA account add labels? If not, is a label line in the comment text acceptable (the default above), or should the label proposal wait?
- Who approves a severity change before it is applied?
- Which dev thread takes design routing for the complex items, and who owns it?

## Deliverables

- `jira/jira-comment-<KEY>-clarify-draft.md` and `jira/jira-triage-<KEY>.md` for each record with an action.
- `jira/COMMENTS.md` appended with one line per key.
- `reports/backlog-triage-round-1.md`.
