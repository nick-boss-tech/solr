# Pool assignment: drift between open PR descriptions and drafts, round 1

Capability tags: `review`, `public-edit` (authorised by the owner for PR descriptions only). Intended host: windows. Staffing: read-only subagents to compare; the review agent applies edits. Claim: `claims/pool-pr-drift-round-1.md`.

## Authorisation

The owner authorised edits to PR descriptions on apache/solr for the open PRs authored by `nick-boss-tech`, to resolve drift between each live description and its draft and receipt. Scope limits:
- Descriptions only. A title changes only where a report records the corrected title and no owner decision is open.
- No head changes, no force pushes, no commit-message rewrites, no comments, no review replies, no closes.
- A change that needs an owner decision is not made. It is recorded in the report.

## Method

1. Pull each open PR's live title, head SHA and body (read-only), saved to the scratchpad.
2. Match each PR to its draft by ticket number. Compare the live head against the draft's named head, and the live body against the draft body.
3. Classify each difference: HEAD (the body names a stale head), DATE (a verification date that was removed from the draft), COUNT, LINK, WORDING, TITLE, or DECISION (needs the owner, not changed).
4. Build the corrected body by applying only the classified corrections to the live body, so that no text outside the drift changes.
5. Read back each edited PR and confirm the body equals the corrected body.

## Rules

- Public writes only through `research\gh.ps1 pr edit <n> --repo apache/solr --body-file <file>`, one PR at a time, after the body file is checked.
- Every corrected body is checked for dashes (none added), for process words (gate, receipt, claim, vm1, vm2, job), and for placeholders.
- No PR number in file names or commit messages in the repository.

## Deliverable

`reports/pr-drift-round-1.md`: one row per open PR (number, ticket, head, drift class, change made or reason not made), the list of owner decisions, and the read-back result for each edit.
