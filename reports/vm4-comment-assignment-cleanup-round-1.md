# vm4 comment-assignment cleanup, round 1

- Assignment: `assignments/vm4-comment-assignment-cleanup-round-1.md`. Claim: `claims/vm4-comment-assignment-cleanup-round-1.md`.
- Board tip verified: `2153d0d51410fc6ba2f07acd4ef9576e28b470ab` (2026-10-25T19:45:25Z).
- Locator: `assignments/comment-drafting-locator-round-1.md`.

## Verification table

| Path | Presence at tip |
| --- | --- |
| `assignments/backlog-first-100-pack-slices-round-1.md` | present |
| `assignments/backlog-first-100-pack-round-1.md` | present |
| `assignments/harmonize-audits-comment-drafts-round-1.md` | present |
| `material/harmonized-backlog/INDEX.md` | present |
| `material/harmonized-backlog/batches/h1-*.json` | 56 files |
| `drafts/jira-comments/` six family files | all six present |
| `jira/jira-comment-<KEY>-draft.md` | 106 files |
| `reports/backlog-first-100-pack-round-1.md` | absent (pack index, to be written) |
| `receipts/comment-ledger/STUB-backlog-first-100.md` | absent (ledger stub, to be written) |
| `claims/backlog-first-100-unit-3.md` | present (vm6, CLAIMED) |
| `claims/harmonize-h2-*-vm2.md` (six) | present, all read LIVE |
| `assignments/critical-tickets-comments-round-1.md` | present (separate line) |
| `assignments/phone-agent-2-critical-closure-drafts-round-1.md` | present (separate line) |

## Ambiguities found

1. **Unit 3 claim.** The slices addendum says no unit is claimed. At this tip, `claims/backlog-first-100-unit-3.md` exists (vm6). Resolved: the addendum was accurate when written; the locator reports the current state.
2. **Pack parent host line.** The parent says "Intended host: vm2 for the pack." Resolved: the slices addendum makes each unit claimable by any pool Linux host, and the parent's line is superseded for units. Flag for owner: the parent file still reads vm2 and has not been edited (records are add-only).
3. **Grandparent rules.** The harmonize-audits file still says the QA gate applies before H2, and the pack file says its no-Bugs rule and QA gate are superseded. Resolved: the pack file governs the pack; the locator lists the superseded parts. Flag for owner: the grandparent file is unchanged.
4. **Stale H2 claims.** The six `claims/harmonize-h2-*-vm2.md` claims still read LIVE, though their deliverables exist. Resolved: treated as delivered, per the assignment. Flag for owner: the claims need a superseding DONE marker from vm2 or the owner; this host does not write them.
5. **Unit order.** The slices file names the unit contents but not a numbered order across batches. Resolved by the rule in the file: batch order, ascending batch number, then record order. The locator confirms this and quotes the rule.
6. **Missing landing files.** The pack index and the ledger stub do not exist yet. Expected: they land after the units. Not an error.

## Not done

- No drafts filled. No unit claimed. No Jira or GitHub text. No edits to the pack files or other hosts' claims.
