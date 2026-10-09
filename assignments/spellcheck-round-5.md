# Round 5 assignment: spellcheck audits

Opened by the main side on 2026-10-09, following the inventory's suggested batching order (`branch-focus-inventory-2026-10-08.md`, item 3: "Suggester, then Spellcheck"). Audits only in this round: no gates, tests, builds, drafts, PRs, comments, or submit-branch edits. Drafts follow in a later round once the audits settle and any owner decisions are recorded.

## In scope (9)

| Ticket | Branch | Inventory head (2026-10-08) | Inventory state |
|---|---|---|---|
| SOLR-1877 | solr-1877-submit | `0d591618679` | gated, no PR |
| SOLR-3701 | solr-3701-submit | `aabd678dec7` | awaiting pipeline (moved, not ff, +2 at inventory time) |
| SOLR-4366 | solr-4366-submit | `5613b311952` | awaiting pipeline |
| SOLR-4367 | solr-4367-submit | `0af6087f43f` | PR-ready |
| SOLR-4399 | solr-4399-submit | `de6cc6b27ed` | PR-ready |
| SOLR-9060 | solr-9060-submit | `704ca28bf79` | PR-ready |
| SOLR-10252 | solr-10252-submit | `a2be0f3adfe` | awaiting pipeline (docs/config only; also filed under Configsets) |
| SOLR-10789 | solr-10789-submit | `d9077048d74` | awaiting pipeline (moved, ff +1 at inventory time) |
| SOLR-17612 | solr-17612-submit | `cd0426e40a7` | gated, no PR (also flagged tidy-only DRIFT in the VM2 sweep) |

## Rules for the audits

- Verify every live tip with `git ls-remote` at audit time. The inventory heads above are 2026-10-08 values and three rows already showed moves then; an audit covers the tip it records, and says so when it differs from the inventory.
- Per branch, the audit states: what the change does (from the diff, not the ticket title), the gate state it can see, the last review on record, blockers with line references at the live tip, interactions with the other eight, and a draft-readiness verdict in the round 4 shapes (draft, draft with named Limits or Choices, held).
- Interaction section is required, not optional: SOLR-1877, SOLR-4366, SOLR-4367, SOLR-9060, and SOLR-17612 all touch `SpellCheckComponent.java`. Trial merges (`git merge-tree --write-tree`) between the five, and a proposed ordering, belong in the report.
- Gate receipts and gate logs live on the main side and are not visible from the review workspace. Where an audit finds no receipt, it says "no receipt visible", not "no gate"; the main side answers with the ledger records, as in rounds 3 and 4.
- SOLR-10252 is a config/docs change (the `_default` configset's example solrconfig). Audit it as such: factual accuracy of the note, and whether a config-only change answers its ticket.
- Owner decisions go in the report as numbered items with the facts each one turns on. The main side answers from the records and the owner's standing practice; genuinely tough calls are flagged DISCUSS and not taken.
- Claim first: a claim file under `claims/` on this branch before the audits, naming the tips claimed.
- No em dashes in any file. Authored text never uses "we" for the owner plus the agent.

## Deliverables

- `audits/spellcheck/SOLR-<ticket>.md`, one per ticket.
- `reports/spellcheck-round-5.md`: per-branch table (live tip, gate state as visible, last review, verdict, the blocker that matters most), the SpellCheckComponent interaction and ordering section, owner decisions, and gaps or discrepancies against the records (inventory rows, prior reviews) with the correction stated.
