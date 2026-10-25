# Comment-drafting locator (standing index, round 1)

This is a standing index, not a work unit. Verified at board tip `2153d0d51410` (2026-10-25). The map below is ordered as the assignment asks: entry point, rules parent, grandparent, source records, family texts, landing paths, and who may claim.

## Entry point

- `assignments/backlog-first-100-pack-slices-round-1.md`: the operative entry point. Four production units of 25 drafts each (Unit 1 `closure-not-a-bug`, Unit 2 `closure-fixed`, Unit 3 `closure-obsolete`, Unit 4 `closure-duplicate` plus `ping-stale` plus `closure-not-a-bug` records 26 to 31), plus an assembly unit that waits for the four unit reports.
- Claim a unit as `claims/backlog-first-100-unit-<N>.md`.

## Rules parent

- `assignments/backlog-first-100-pack-round-1.md`: eligibility, exclusions (Critical and Blocker priority, owner-excluded, PR-carrying, conflict-flagged), the live check at fill time, and the spot-QA step.

## Grandparent

- `assignments/harmonize-audits-comment-drafts-round-1.md`: built the store and the family structure. Its no-Bugs rule and its QA-before-H2 gate are superseded for the pack by the pack file. Do not apply them to the pack.

## Source records

- `material/harmonized-backlog/INDEX.md`.
- `material/harmonized-backlog/batches/h1-*.json`: 56 files on the tip. Use the latest revision per batch.

## Family texts (done; do not redo)

- `drafts/jira-comments/closure-not-a-bug.md`, `closure-fixed.md`, `closure-obsolete.md`, `closure-duplicate.md`, `ping-stale.md`, `status-note.md`. All six present.
- Treat the six family files as delivered. The `claims/harmonize-h2-*-vm2.md` claims still read LIVE, though their deliverables exist. Stale paperwork; not a reason to rewrite the families.

## Landing paths

- Filled drafts: `jira/jira-comment-<KEY>-draft.md` (106 files on the tip, including drafts from before this pack).
- Pack index: `reports/backlog-first-100-pack-round-1.md` (not yet written).
- Ledger stub: `receipts/comment-ledger/STUB-backlog-first-100.md` (not yet written).
- Unit reports: `reports/backlog-first-100-unit-<N>.md`.

## Who may claim

- Any pool Linux host. The pack parent's "Intended host: vm2" line is superseded by the slices addendum, which makes each unit claimable by any pool Linux host.
- The spot-QA unit is for a second host, not the host that filled the pack.

## Unit order

- Records run in batch order, ascending batch number, then record order within each batch. The slices file is the source of the order; the locator confirms it and does not change it.

## Current state at the tip

- Unit 3 is claimed by vm6 (`claims/backlog-first-100-unit-3.md`). The slices addendum's "no unit is claimed yet" was true earlier, not at this tip.
- Units 1, 2 and 4 have no claim on the tip.
- The Critical comment line is separate: `assignments/critical-tickets-comments-round-1.md` and its directed file `assignments/phone-agent-2-critical-closure-drafts-round-1.md`. It is not part of the backlog pack.
