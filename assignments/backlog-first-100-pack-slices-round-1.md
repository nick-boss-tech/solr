# Addendum: backlog first-100 pack, sliced into claimable units (round 1)

Status: OPEN
Tags: draft, records, sweep
Parent: `assignments/backlog-first-100-pack-round-1.md` (its rules and exclusions govern in full).

State found 2026-10-25: the parent pack has sat unclaimed since it posted. The six family files are done (`drafts/jira-comments/`), so what is missing is the filling. The pack is one 100-draft unit, which is larger than one claim under the small-claims rule; it is sliced here into four production units of 25 that different hosts can fill in parallel, plus one assembly unit. Claim each as `claims/backlog-first-100-unit-<N>.md`.

## Production units (each delivers 25 filled drafts or stops with the shortfall named)

- Unit 1: `closure-not-a-bug`, records 1 to 25 in batch order.
- Unit 2: `closure-fixed`, records 1 to 25 in batch order.
- Unit 3: `closure-obsolete`, records 1 to 25 in batch order.
- Unit 4: `closure-duplicate` (all 8 records), then `ping-stale` (all 11), then `closure-not-a-bug` records 26 to 31: 25 total.

Records come from the H1 batches (latest revision per batch) in that family's disposition, in batch order. Per the parent: skip priority Critical/Blocker, owner-excluded, PR-carrying, and conflict-flagged records; live-check every fill at fill time (Jira still Open, no linked or mentioned open PR, not the owner's ticket, no owner comment already on it); a record that fails is dropped and replaced by the family's next record, with the drop and reason listed in the unit report. Drafts land at `jira/jira-comment-<KEY>-draft.md`. Unit report: `reports/backlog-first-100-unit-<N>.md` listing the 25 keys with family, evidence one-liner, live-check time, and every drop.

## Assembly unit (claim after the four unit reports exist; not by a unit producer)

- Build `reports/backlog-first-100-pack-round-1.md`: the 100 in posting order by the parent's family order (not-a-bug, fixed, obsolete, duplicate, ping-stale), the overflow list of the next 30 eligible records, and the combined skipped list.
- Write the ledger stub at `receipts/comment-ledger/STUB-backlog-first-100.md`.
- Open the spot-QA step from the parent as its own claim for a second host: 10 pack entries re-derived from sources and live tickets before the pack goes to the owner.

Nothing posts. The assembled pack goes to the owner as one approval.
