# Pool assignment: triage the open-issue CSV when a host has no other work

Status: PAUSED (owner direction, 2026-10-20: stop CSV triage; `assignments/pool-newdev-critblock-review-round-1.md` takes priority). Slices already claimed keep their claims. Earlier note: Claim a slice under `claims/` before starting (WORKFLOW.md). The CSV is on this branch at `material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv`. Backlog by owner direction (2026-10-19): claim a slice only once the JSON queue (remediation and audit work) has nothing open for you. Carve-out (owner direction, 2026-10-19, later): the 100 open Blocker and Critical rows are a separate priority round, `assignments/pool-csv-critical-blocker-round-1.md`, claimable without that wait; work that round before these slices.

- Capability tags: records.
- Intended hosts: any pool host that has no claimed work on its tick. Work this only when nothing else is open for you.
- Posted by: vm2, 2026-10-19, at the direction of Nick.
- Depends on: `assignments/pool-post-issues-csv-vm2.md`, which is DONE. The source file is `material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv`.

## Scope

The CSV has 18,364 rows. 14,286 are Done. The open set is the 4,078 rows whose `StatusCategory` is not `Done` (4,016 To Do, 62 In Progress), in file order. The open set is cut into 2,039 slices of 2 rows each.

Slice NNNN covers open rows (NNNN-1)*2+1 and NNNN*2 in file order, with NNNN from 0001 to 2039.

## Task

1. Claim one slice as `claims/pool-csv-backlog-slice-NNNN.md`. Take the lowest unclaimed NNNN.
2. For each issue key, check for earlier ticket work before applying the CSV rule. Search records under `material/skip-audit/`; earlier `material/jira-csv-triage/slice-*.csv` files; `reports/csv-critblock-*.md`; and ticket-specific records in `assignments/`, `claims/`, `reports/`, `drafts/`, `gates/`, and `receipts/` on `origin/coordination`. Use a targeted `git grep -n -F` on `origin/coordination` for the exact key in those record paths. Example: `git grep -n -F SOLR-10407 origin/coordination -- material/skip-audit material/jira-csv-triage assignments claims reports drafts gates receipts`. Open each relevant match and confirm it is work on this ticket, not an incidental mention as a sibling or example.
3. Apply these status rules to a relevant prior record:
   - A live claim means the ticket is already being worked. Record the claim path and heartbeat/status; do not duplicate its ticket work.
   - A completed claim with its named deliverable, or a recorded prior ticket result, means the ticket has already been processed in that store. Record the exact note/report path and its stated result; do not repeat the investigation.
   - A `queue-current.json` entry is prior work, but it is not automatically resolved and its note may lag newer records. Include its verdict and audit state, then compare dates and heads in matching claims, reports, gates and receipts. Cite conflicting records and state which result is newer; a gate receipt proves only its recorded head. If `remediation-current.json` says `open`, record that status and the remediation assignment or report; it remains open and provisional even when a different branch has a green gate.
   - A stale or conflicting record is not evidence of completion. Follow `pool/advisory-wont-do.md` and report the actual record state without editing another host's claim or deliverable.
4. If a ticket-specific prior record exists, use `overlap` as its `Class` and put the prior paths and current status in `Reason`. Include the CSV rule that would otherwise apply, such as `CSV class stale; overlap: queue verdict keep-open, remediation open; see ...`. Do not remove an overlapping row or change slice numbering. If no prior record exists, decide the class from the CSV row with these rules in this order:
   - `has-pr`: `PRAvailable` is `Yes`.
   - `stale`: no PR, and `Updated` is before 2024-10-19.
   - `active`: no PR, and `Updated` is 2024-10-19 or later.
5. Write `material/jira-csv-triage/slice-NNNN.csv` with the header `IssueKey,Class,Updated,Reason`, one line per row. For `overlap`, `Reason` names the CSV class that would otherwise apply, the exact prior record path, and its current status or result. Quote CSV fields as required when they contain commas. Keep exactly two data rows.
6. Mark the claim DONE with the row count and the SHA-256 of the slice file, and push.

Classes are proposals for triage, not verdicts. A `stale` row is a candidate for a close review under the skip-queue rubric, not a close. Do not apply any verdict in this assignment.

## Decisions

| Situation | Decision |
| --- | --- |
| A ticket has prior work in another store | Keep the CSV row, mark `overlap`, cite the record path and status, and do not repeat that ticket investigation. |
| The skip queue has a verdict but remediation is open | Record the verdict and open remediation path; the ticket remains provisional and is not described as resolved. |
| Queue and ticket records disagree | Compare dates and heads, cite both states, and do not treat a green receipt as proof beyond its recorded head or as clearing a separate open promise or remediation. |
| No ticket-specific prior record exists | Use the CSV-only `has-pr`, `stale`, or `active` rule above. |

## Limits

- Read the CSV and the coordination records named above. Do not fetch issues from JIRA, call the JIRA API, or post to JIRA, GitHub, or email.
- No PRs, no force push, and no edits to the source CSV.
- Do not change another host's claim or slice.
- Commit as Nick Shanin with no trailers. No em dashes.

## Deliverable

- `material/jira-csv-triage/slice-NNNN.csv`, 2 data rows.
- `claims/pool-csv-backlog-slice-NNNN.md` marked DONE with the row count and SHA-256.
