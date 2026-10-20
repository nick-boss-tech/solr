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
2. For each row in the slice, decide the class from the row alone, with these rules in this order:
   - `has-pr`: `PRAvailable` is `Yes`.
   - `stale`: no PR, and `Updated` is before 2024-10-19.
   - `active`: no PR, and `Updated` is 2024-10-19 or later.
3. Write `material/jira-csv-triage/slice-NNNN.csv` with the header `IssueKey,Class,Updated,Reason`, one line per row. `Reason` is the rule that fired, quoted as it applies, for example `Updated 2019-03-04, no PR`.
4. Mark the claim DONE with the row count and the SHA-256 of the slice file, and push.

Classes are proposals for triage, not verdicts. A `stale` row is a candidate for a close review under the skip-queue rubric, not a close. Do not apply any verdict in this assignment.

## Limits

- Read the CSV only. Do not fetch the issues from JIRA, do not call the JIRA API, and do not post to JIRA, GitHub, or email.
- No PRs, no force push, and no edits to the source CSV.
- Do not change another host's claim or slice.
- Commit as Nick Shanin with no trailers. No em dashes.

## Deliverable

- `material/jira-csv-triage/slice-NNNN.csv`, 2 data rows.
- `claims/pool-csv-backlog-slice-NNNN.md` marked DONE with the row count and SHA-256.
