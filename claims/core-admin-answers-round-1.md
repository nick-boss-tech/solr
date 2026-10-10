# Claim: core admin answers pass into drafts

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Scope: `material/core-admin-round-1-answers.md` (commit `9968fe293f6`). The main side's answers pass for the core-admin round. Its "Draft corrections owed" list (items 1 to 8) is not done yet. It says the draft-fix pass is the review side's work. Apply items 1 to 7 to the drafts in `pr-drafts/core-admin/`. Item 8 is the main side's checked-clean list; re-verify it after the edits.

Do not decide any DISCUSS item (the answers file lists 13). Do not edit branches, live PRs, or receipts. The branch and receipt corrections in the answers file are not ours to make here.

The openings slate is already in the answers file. This round does not write a second one.

Cap: two subagents, within the cap of six.

## What changes, by item

- **Item 1.** Remove the `<!-- INTERNAL ... -->` blocks in `SOLR-6438.md`, `SOLR-8275.md`, `SOLR-8576.md`, `SOLR-16725.md`, and `SOLR-17731.md`.
- **Item 2.** Add the bold one-line summary opener to the Limits section of `SOLR-4989.md`, `SOLR-13246.md`, `SOLR-12007.md`, `SOLR-17297.md`, `SOLR-17377.md`, `SOLR-6438.md`, `SOLR-8275.md`, `SOLR-8576.md`, `SOLR-16725.md`, and `SOLR-17731.md`. For `SOLR-15024.md`, add it to the Choice and Limits sections.
- **Item 3.** `SOLR-17297.md` and `SOLR-17377.md`: fill each Proof placeholder with the observed base failure line from the main-side record, or reword it to the recorded outcome. The base-failure logs (`g17297-harden.log`, `g17377-premise.log`) are not on disk, so the recorded outcome is the only source. `SOLR-17297.md` has a second placeholder for the reverse-order probe result; reword that too, to the recorded result, or leave it marked owed if the record has none.
- **Item 4.** `SOLR-15805.md`: the Proof's base-failure sentence is reworded to a code reading, as the answers entry says, unless the log is recoverable (it is not on disk).
- **Item 5.** Notes only for `SOLR-8576.md`, `SOLR-17731.md`, and `SOLR-17708.md`: the head, counts, and links update after the branch fixes and runs. Do not change the head references yet; mark the update as owed in the draft, in the formula's plain wording.
- **Item 6.** `SOLR-17297.md`: remove the duplicate Changelog line inside "What this change does". The Changelog line that closes the draft stays.
- **Item 7.** `SOLR-15024.md`: the ticket text is not on disk. Note the check as owed in the draft's owner list; do not change the summary line.
- **Item 8.** Re-verify after the edits: no internal process vocabulary in the PR text, no em or en dashes, Proof numbers match the receipts, and each draft names its head.

## Parts

**Part c1: items 1 and 2.** Drafts: all named in items 1 and 2 above.

**Part c2: items 3 to 8.** Drafts: `SOLR-17297.md`, `SOLR-17377.md`, `SOLR-15805.md`, `SOLR-8576.md`, `SOLR-17731.md`, `SOLR-17708.md`, `SOLR-15024.md`. Item 8 covers all drafts in the folder.

## Shared rules

- Edit only the named drafts in `pr-drafts/core-admin/`, in place. No commit, push, checkout, reset, merge, stash, or fetch.
- No builds, no tests, no `gh` write calls, no posting.
- Plain words, short sentences. No em dash and no en dash.
- Proof numbers come only from the receipt, and the answers file where it states a recorded outcome. Do not take numbers from memory.
- Do not decide a DISCUSS item.
- Every change: old line, new line. Mark anything you could not do, and why.

## Deliverables

1. `reports/core-admin-answers-round-1-c1.md` and `-c2.md`. Each lists the changes, the items left owed, and the checks. The lead writes `reports/core-admin-answers-round-1.md`.
2. Edits in place under `pr-drafts/core-admin/`.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: material/core-admin-round-1-answers.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
