# Claim: receipt refresh round 2 (SOLR-12651 and SOLR-17292), with their drafts

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` moved from `27f34310ce0` to `a266ae9c509` (commit "Receipts: SOLR-12651 and SOLR-17292 gate green at their live tips"). That commit changes `receipts/SOLR-12651.md` and `receipts/SOLR-17292.md` only. It comes with no new assignment or material file.

Scope, two parts, one ticket each:

1. **SOLR-12651.** Check the refreshed receipt (commit `a266ae9c509`) against the branch at its live head. Check `pr-drafts/solrcloud/SOLR-12651.md` against that receipt and head. Its head references (including the old gated head `90032e274b7`) must match the live head `f3131d1ee846`.
2. **SOLR-17292.** Check the refreshed receipt against the branch at its live head. The receipt says the in-branch remedy (commit `f614a42fbc8`) is adopted, so the draft's "head is owed before opening" note and its old head `e43200b0fb6` can be closed. Update `pr-drafts/solrcloud/SOLR-17292.md` to the live head and to the remedy, with Proof numbers only from the receipt. Check the remedy against the code at the head.

## Heads checked live on 2026-10-10

| Ticket | Branch | Live head | Receipt head | Result |
|---|---|---|---|---|
| 12651 | `solr-12651-submit` | `f3131d1ee846` | `f3131d1ee846` (refreshed at `a266ae9c509`) | matches |
| 17292 | `solr-17292-submit` | `f614a42fbc80` | `f614a42fbc80` (refreshed at `a266ae9c509`) | matches |

Both heads were fetched explicitly on this round (`git fetch origin solr-12651-submit solr-17292-submit`) and confirmed with `git cat-file -e`.

## Shared rules for every part

- Edit only the named draft, in place. Read only otherwise. No commit, push, checkout, reset, merge, stash, or fetch of anything new beyond the two heads above.
- `git show`, `git log`, `git diff`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree` are fine.
- No builds, no Gradle, no tests. No `gh` write calls. Post nothing.
- Gate logs named in the receipts (`g12651-livetip-gate.log`, `g17292-regate.log`) are not on disk. Say so. Proof numbers come only from the receipt for the ticket.
- Drafts follow `pr-formula.md`: header line, Jira link, bold one-line summary per section, Proof, optional "A choice to check", Limits, Changelog link, AI footer. Citations link to the blob at the head SHA. Public text carries no internal process vocabulary (no receipt, gate, round, takeover, premise, owed). Plain words. No em dash and no en dash.
- Every finding: file and line, evidence, and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so.
- Return the part report as your final message text. The lead writes the report file.

## Parts

**Part a: SOLR-12651.** Receipt at `receipts/SOLR-12651.md` (refreshed). Draft at `pr-drafts/solrcloud/SOLR-12651.md`. Check: the receipt's counts and the draft's Proof against the receipt; the "two commits since the older gate" wording against `git log 90032e274b7..f3131d1ee846` if the old head is reachable; the Choice (the ticket question on restore cleanup) is present and framed as a decision for the submission; the Changelog link and head.

**Part b: SOLR-17292.** Receipt at `receipts/SOLR-17292.md` (refreshed). Draft at `pr-drafts/solrcloud/SOLR-17292.md`. Check: the remedy in `ZkController.publishNodeAsDown` at `f614a42fbc80` (a try/catch around the per-collection `PerReplicaStatesOps` persist call, logging a warning); the draft's description of the behavior change, which must match the code, not the old head; the Proof numbers (4 of 4 and the one failure at the merge-base with the two files reverted) against the receipt; the "owed" notes removed only where the receipt now closes them.

## Deliverables

1. Part reports, returned as text: part a and part b. The lead writes `reports/receipt-refresh-round-2-a.md`, `reports/receipt-refresh-round-2-b.md`, and `reports/receipt-refresh-round-2.md`.
2. Edits in place to `pr-drafts/solrcloud/SOLR-12651.md` and `pr-drafts/solrcloud/SOLR-17292.md` (only where the receipt and head require a change).

## Not in scope

Opening or editing PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, and test runs. Changes to the receipts are the main side's, not ours.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: receipts/. The work was performed by the original claimant; this mark is a record correction, not a new claim.
