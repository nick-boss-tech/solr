# Addendum: backlog triage actions beyond the comment (round 1)

Status: OPEN, drafts and records only.
Tags: draft, records, review.
Parents: `assignments/backlog-first-100-pack-round-1.md` (rules) and `assignments/backlog-first-100-pack-slices-round-1.md` (units). Written by vm6 to widen what a pack record produces; the parents' comment rules still govern the comment.

## Why

The dev list asked for more than a comment on each backlog record: reply to the reporter with clarifying questions where the report is not enough to act on, set a better severity, classify the component, set a label, and, for complex items, help land a design. A comment alone does none of these. This addendum makes each one a drafted, reviewable item attached to the record, so an owner can approve each action and the factory does not change JIRA fields itself.

## Actions per record

Each action is a draft at `jira/triage/<KEY>-triage-draft.md`, one file per key, with one section per action that applies. A record may have any subset.

1. **Clarifying question to the reporter.** Applies when the report lacks version, reproduction steps, or the expected behavior needed to act. The draft is a comment in Jira wiki markup, first person, addressed to the reporter with `[~REPORTER]`, asking only the questions the record cannot answer itself. Footer disclosure as in the family texts.
2. **Severity proposal.** Proposed priority with one evidence line (what the report says about impact, scope, and regression). Records only; no field change. Critical and Blocker remain excluded from the pack.
3. **Component proposal.** Proposed component name with the path or code evidence. The live JIRA component list must be checked at fill time; the export on the board does not carry components, so a proposal that does not match a live component is dropped and listed.
4. **Label proposal.** Proposed label from labels already in use on Solr issues (the archived export has 1,030 distinct labels; for example `newdev`, `performance`, `security`). A label that is not in use is a new label and is listed as an open question, not proposed as a label.
5. **Design routing.** For complex items (new behavior, API or format change, or several components), a record saying a design is needed, with a proposed design owner or dev-thread link. No design is written under this addendum unless a separate assignment gives it.

## Rules

- Nothing is posted to JIRA, GitHub or the dev list. Field changes are not made. The owner approves each action in the pack approval.
- Live check at fill time for every action: status still Open, not the owner's ticket, no owner comment already on it (as in the parent).
- A record that fails the live check drops out of every action for that key and is listed in the unit report with the reason.
- Triage drafts do not add a record to the pack count. The pack's 100 comment drafts are unchanged.

## Flag clearing (decided by vm6, owner may reverse)

The conflict flags on the closure-family records are cleared for the pack. Basis is recorded in the Unit 3 claim, status note 2026-10-25T18:00Z. Critical and Blocker stay excluded by priority.

## Open questions for the owner

- Can the pool's JIRA account add labels, or does a JIRA admin need to enable them? Labels cannot be proposed as settled until this is answered.
- Who approves a severity change before it is applied? (Proposal only here.)
- Which dev thread takes design routing for the complex items?

## Deliverables

- `jira/triage/<KEY>-triage-draft.md` for each record that carries at least one action.
- Triage index: `reports/backlog-triage-round-1.md` listing each key, the actions drafted, the live-check time, and every drop.
