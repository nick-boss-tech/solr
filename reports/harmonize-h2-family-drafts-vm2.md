# Harmonize H2: comment family drafts (vm2)

Assignment: `assignments/harmonize-audits-comment-drafts-round-1.md`, phase H2. Requested by the owner on 2026-10-25 ("I want the drafts for the backlog done"). Built from rev4 batch records (`material/harmonized-backlog/batches/h1-NNN-rev4.json`). Claims: `claims/harmonize-h2-<family>-vm2.md` (six, one per family).

## Status

Drafts only. Nothing is posted to Jira or GitHub. H1 QA by a second host has not been published, and the assignment makes H2 start only after QA passes. The drafts are ready for review and are not approved for use until QA passes.

## Families

| Family | Eligible now | Why | File |
| --- | ---: | --- | --- |
| ping-stale | 11 | Bugs are not barred from this family. All 11 are Major and have a skip verdict. | `drafts/jira-comments/ping-stale.md`, list `families/ping-stale-eligible.txt` |
| status-note | 69 | Owner PR or gated branch exists, Jira does not mention it. 33 other candidates were already told, and 6 Critical and 4 Blocker tickets were barred. | `drafts/jira-comments/status-note.md`, list `families/status-note-eligible.tsv` |
| closure-fixed | 0 | All 78 records are Bugs, barred by the bug rule. No fix version is recorded. | `drafts/jira-comments/closure-fixed.md` |
| closure-obsolete | 0 | All 68 records are Bugs, barred by the bug rule. | `drafts/jira-comments/closure-obsolete.md` |
| closure-not-a-bug | 0 | All 127 records are Bugs, barred by the bug rule. | `drafts/jira-comments/closure-not-a-bug.md` |
| closure-duplicate | 0 | All 8 records are Bugs, barred by the bug rule. | `drafts/jira-comments/closure-duplicate.md` |

## Decisions for the owner

1. The bug rule removes every closure record. All 281 closure-disposition records are Bugs, and the assignment bars bugs from the four closure families. If the owner wants closure drafts for bugs, the rule has to change, and fix versions or evidence citations will be needed for closure-fixed.
2. Status notes go to 69 tickets, each naming an owner PR or branch. Each needs the owner's go before anything is posted. The "told" check is a heuristic run once, and each comment should be re-read before posting.
3. Ping-stale drafts state no current-release result. The owner should confirm the wording before any batch is posted.

## Fill rules and gaps

- Ping-stale: the last-activity date comes from the metadata CSV `Updated` column, which is a store source. The H1 records do not carry it.
- Status-note: the PR summary sentence must be read from the live PR body at fill time. The record does not carry it.
- Closure families: placeholders need citations the H1 records do not carry (fix version, removal commit, explanation, surviving ticket). Such tickets are not fillable until H2 QA adds them.

## Limits

- Live Jira comment check only for the 114 implemented candidates (done). Ping drafts were not checked live for activity beyond the metadata CSV date.
- Reporter names are placeholders in every template, because the store does not carry them for every key.
- No Jira or GitHub write. The owner approves each family text once, per the assignment.
