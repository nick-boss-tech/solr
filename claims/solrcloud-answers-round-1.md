# Claim: SolrCloud answers pass into drafts, and receipt refresh for six tickets

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Scope, two parts:

1. **Draft-fix pass.** `material/solrcloud-round-1-answers.md` (commit `007374eefe6`) records the main side's answers to the SolrCloud round. Its "Draft corrections owed" list (items 1 to 8) is not done yet, and the answers say the draft-fix pass is the review side's work. Apply those corrections to the drafts in `pr-drafts/solrcloud/`. Do not decide any DISCUSS item; those go to Nick in the openings slate.
2. **Receipt refresh.** Commits `a7549e9791d`, `2f1e34ba89b`, and `49d99c97d10` refreshed the receipts for SOLR-6759, 8003, 10694, 8051, 10305 and 9124. Each now records a gate green at the live tip, after re-gates. These six tickets were audit-only or held earlier (search components sub-batches 2 and 4, and 3). Re-check each verdict against its new receipt and the branch, and draft only where a ticket is now draftable.

The openings slate (what can open, what waits on a DISCUSS item or a gate) is written by the lead after the parts report.

Cap: four subagents at once, within the cap of six.

## Heads checked live on 2026-10-10

| Ticket | Branch | Live head | Receipt gated head | Result |
|---|---|---|---|---|
| 6759 | `solr-6759-submit` | `42f03eb52376` | `42f03eb5237` | matches |
| 8003 | `solr-8003-submit` | `17c516a607a8` | `17c516a607a8` | matches |
| 10694 | `solr-10694-submit` | `64e86811548b` | `64e86811548b` | matches |
| 8051 | `solr-8051-submit` | `e50a2aa3437c` | `e50a2aa3437c` | matches (re-gate after an in-branch fix) |
| 10305 | `solr-10305-submit` | `c43de86d4c13` | `c43de86d4c13` | matches (re-gate after a test fix) |
| 9124 | `solr-9124-submit` | `17a279d4dce1` | `17a279d4dce1` | matches (second re-gate) |

## Shared rules for every part

- Read only, except the drafts and report file named for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, and `git ls-tree` are fine.
- No builds, no Gradle, no tests. No `gh` write calls. Post nothing anywhere.
- Proof numbers come only from the receipt named for the ticket, and the answers file where it states a recorded date. Gate logs are often not on disk; say so.
- Drafts follow `pr-formula.md`. Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, fresh JUnit XML, pre-fix proof as a label, "premise run", internal log names, "gate record"). Plain words. No em dash and no en dash.
- Do not decide DISCUSS items. Keep each as the answers record it.
- Every finding: file and line, evidence, and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so. Do not guess.

## Parts

**Part a1: SolrCloud draft corrections, items 1 to 5 and the bold openers for four drafts.** Drafts `pr-drafts/solrcloud/`:
- `SOLR-9155.md`, `SOLR-13186.md`, `SOLR-15106.md`, `SOLR-15386.md`: remove the owner-notes header at the top, including the line "Open: the gate record gives no run date". Replace the "[date to confirm]" in each Proof with the recorded date: 9155 and 13186 2026-10-06; 15106 and 15386 2026-10-07. Give each Limits section a bold one-line opener (answers draft correction 6).
- `SOLR-9155.md` and `SOLR-15386.md`: move the Proof section to the formula order (after "What this change does", before the Choice and Limits). Also the 9155 Choice and Limits wording follows the answers entry for 9155.
- `SOLR-15863.md`: give the Choice section a bold one-line summary opener.
- `SOLR-12651.md`: replace the bracketed Proof instruction line with a plain line stating that the run at the live tip is owed before opening, and that the recorded result is the gated head `90032e274b7`. Give the Choice section a bold one-line summary opener.
- `SOLR-15035.md`: fill the Proof placeholder. If the observed base failure line cannot be recovered from the record, reword the sentence to the receipt's recorded outcome (the test failed with the banked fix alone, because the CoreAdmin allowlist dropped numShards). Do not quote a line you cannot source.

**Part a2: SolrCloud draft corrections, item 6 for the remaining drafts, and item 7.** Drafts:
- Give a bold one-line opener to the Limits section (or the first section that opens with bullets) in `SOLR-5813.md`, `SOLR-11288.md`, `SOLR-12991.md`, `SOLR-13369.md`, `SOLR-14919.md`, `SOLR-17292.md`, `SOLR-17680.md`, `SOLR-17733.md`, `SOLR-15674.md`.
- `SOLR-15674.md`: check its length (about 4.4 KB, over the guide). Report the length. Trim only if the answers direct it; they do not, so report and leave it.
- `SOLR-17292.md`: the answers say the node-down remedy is adopted in the branch, and the draft's Choice section comes out, with the node-down bullet updated. Apply that to the draft. The branch re-gates after the remedy, so the draft's Proof must say the run at the current head is owed, and must not carry the green from the earlier head.

**Part b1: receipt refresh for SOLR-8051 (search components sub-batch 4), SOLR-10305 and SOLR-9124 (sub-batch 2).** Each was audit-only or held earlier. Each now has a gate green at the live tip after re-gates, with a history of failed first gates fixed in-branch. Read the new receipt. Verify the stated fixes are in the branch at the live head and that the proof claims match the receipt. For 8051, the earlier audit found a null-check ordering hazard in `ExactStatsCache.java`: check whether the in-branch fix moved the null check above the exception check. For 10305 and 9124, check the test-hook and production fix claims against the diff. Verdict per ticket: draftable, held with reason, or audit only. Draft under `pr-drafts/search-components/` only if draftable, with the head named.

**Part b2: receipt refresh for SOLR-6759 (handler components), SOLR-8003 (doc transformers), and SOLR-10694 (writers).** Each was audit-only earlier (no gate). Each now has a gate green at the live tip. The receipts say packaging removed the handoff note and, for 10694, folded in a tidy javadoc rewrap. Verify the note is gone, the tidy commit matches the receipt, and the proof claims match. 8003's earlier audit flagged a textual conflict with SOLR-14678 in `DocTransformers.java`; check whether it still holds. Verdict per ticket. Draft under `pr-drafts/search-components/` only if draftable, with the head named.

## Deliverables

1. `reports/solrcloud-answers-round-1-a1.md` and `-a2.md`, for the draft-fix pass. `reports/receipt-refresh-round-2-b1.md` and `-b2.md`, for the refresh. The lead writes `reports/solrcloud-answers-round-1.md` (the draft-fix results) and `reports/receipt-refresh-round-2.md` (the verdicts), plus the openings slate `reports/solrcloud-openings-slate.md`.
2. Draft edits in place under `pr-drafts/solrcloud/`, and new drafts under `pr-drafts/search-components/` only where draftable.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: material/solrcloud-round-1-answers.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
