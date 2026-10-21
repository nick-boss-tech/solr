# Live PR text edits: inventory (vm3, 2026-10-21)

Owner ruling (Nick, 2026-10-21): live title and body edits on existing PRs do not need approval. This inventory finds the edits that were held for approval and says where each one stands.

Method: live title and body read through the GitHub REST API for apache/solr on 2026-10-21. Each draft's sentences were checked for verbatim presence in the live body. Status lines in the drafts were read and compared with the live text. Where a draft's status line said "not applied" but the live body matches it, the record below says applied. Draft headers were not edited.

## Still pending

| PR | Draft | Pending edit | Why it waits |
| --- | --- | --- | --- |
| 5028 (SOLR-18505) | `reports/update-processing-final-round.md`, section "SOLR-18505 (closing item)" | One sentence. The corroboration run was on the fork at `71a978d5982` (head `e28739b4069` plus a CI workflow commit), not on the PR head. The live body has neither. | The draft says the wording must be confirmed by the owner. Ready to apply once it is. |
| 5104 (SOLR-2681) | `pr-drafts/highlighting/SOLR-2681-limits-reword.md` | The Limits bullet says "A follow-up pull request is planned for the nested forms." | Nothing on record confirms a plan. The ticket question for the nested forms is open (`pr-drafts/highlighting-nested/ticket-draft.md`). The reword and the ticket decision go together. |

## Applied, draft status line still says "not applied"

| PR | Draft | Evidence |
| --- | --- | --- |
| 5088 | `pr-drafts/2026-10-18-review/PR-5088-behavior-change.md`, `PR-5088-and-5089-limits.md` | The live body has the "Behavior change" section and no "A choice to check". The limits draft records its own application at 2026-10-18T02:02:06Z after Nick approved it. Later bodies (head `960355fce3`) rewrote some wording. |
| 5089 | `PR-5088-and-5089-limits.md`, `PR-5088-and-5089-13059.md` | The limits draft was applied at 2026-10-18T02:02:06Z. The live Limits section now says the six tests run in normal mode. The later rewrite supersedes the draft's wording. |
| 5116, 5117, 5130, 5131, 5132, 5135 | `pr-drafts/2026-10-16-review/PR-*-text.md` | Round 2 applied these on 2026-10-18 (`reconciliation-round-2.md`, "Applied" table). Live bodies match the drafts for 24 to 28 of their sentences. 5135 was rewritten for head `6c033ba6ea`. |
| 5134 | `PR-5134-text.md`, `PR-5134-reconcile-5c1124bd.md` | Applied 2026-10-18 at head `5c1124bdef` (6,739 raw characters, per the round 2 "Applied" table). The live body no longer has "so the early stop does not apply there". The reconcile draft still says DRAFT. The live body is about 5,230 visible characters, over the 3,500 guideline. That is a separate cut, not an approval item. |
| 5144 | `receipts/SOLR-8088.md`, "PR #5144 body re-point" | The live body names `0a5eaf4b2c` seven times, and the head is `0a5eaf4b2c`. The re-point looks applied. The receipt still says "drafted, not applied". |
| 5118 | `pr-drafts/solr-18010/PR-5118-body-round-3.md` | Applied 2026-10-21 (title and body), with the owner's statement that updates are allowed. |

## Opened PRs whose drafts were approved

- 5180 (SOLR-18119), 5181 (SOLR-18552): opened at Nick's approval. Both are ready, so the flip was approved too.

## Drafts not opened or filed (still need approval)

These are public writes, not edits to a live PR, and the approval rule still applies: `pr-drafts/core-admin/SOLR-16887.md`, `pr-drafts/highlighting-nested/PR-draft.md`, `pr-drafts/langid-4841/PR-test-only-draft.md`, `pr-drafts/langid-4841/SOLR-XXXXX-ticket.md`, `pr-drafts/slice-order/*`, `pr-drafts/solr-16673/*`, `pr-drafts/solr-18503/PR-draft.md`, `pr-drafts/solr-18549/PR-draft.md`, `pr-drafts/test-latches/ticket-draft.md`, `pr-drafts/highlighting-nested/ticket-draft.md`. No open PR from Nick's account uses the head branches for 18503 or 18549, so those are still unopened.

## Stale status lines

The draft headers above still say "not applied" where the edit was made. The table in this report is the record. Headers were left as written so the history of each draft stays intact.
