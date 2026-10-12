# SolrCloud readiness for opening PRs, round 1

Verdict. Of the 19 SolrCloud drafts, five are ready to open, six need an owner decision, and eight are held for a record, a branch commit or an ordering choice. Every fork tip equals its receipt's gated head. Nothing is opened, posted or pushed to a submit branch. Opening stays with Nick.

Inputs: `assignments/pool-solrcloud-readiness-round-1.md`, claim `claims/pool-solrcloud-readiness-round-1.md`, the drafts in `pr-drafts/solrcloud/`, the receipts in `receipts/`, and the slate `reports/solrcloud-openings-slate.md`. Five read-only reviewers checked the 19 drafts in groups of three or four. Their per-draft citation and count checks are not in the repo, so this report records their verdicts. Tips were re-read with `git ls-remote`, and the gated heads, receipt lines and draft lines below were re-read directly.

## Readiness table

| Ticket | Verdict | Tip (equals gated head) | Open item |
|---|---|---|---|
| SOLR-5813 | READY TO OPEN | 90b8baa08aef | Reject route is posed to the maintainer (slate). Gate green at the tip (receipt lines 3 to 4). |
| SOLR-12991 | READY TO OPEN | 1a86966179e1 | WARN adopted; the Choice is posed to the maintainer (slate). Gate green (receipt lines 3 to 4). |
| SOLR-16437 | READY TO OPEN | f72a9edd0a8b | Gate green at this head, dated 2026-10-12 (receipt lines 3 to 5). The head's subject carries "gate" (cross-cutting 2). See note A. |
| SOLR-17292 | READY TO OPEN | f614a42fbc80 | The node-down remedy is the head commit (receipt line 8). Gate green (receipt lines 3 to 4). |
| SOLR-11479 | READY TO OPEN, after SOLR-15035 | 360dca60fcfa | Gate green at this head, dated 2026-10-11 (receipt lines 3 to 4). Draft line 38: lands after SOLR-15035. Draft line 22 has a run-on ("passes.- Base commit") to fix. |
| SOLR-11288 | DECISION | cd094c3c623f | Blank-only meaning (slate decision 3). The slate also asks for a branch changelog title commit first, which moves the head and needs a re-gate. |
| SOLR-13369 | DECISION | dfa0db5bdf96 | Confirm the Choice (slate). The slate gives no recommendation. The proof is by construction, as the slate states. |
| SOLR-14919 | DECISION | 84e7bcaeb57d | Ship the RecoveryStrategy marker, or the processor alone (slate decision 1). The slate lists it under Held. |
| SOLR-15386 | DECISION | ca8cb61ee957 | Confirm the owner question on the cached live-node check. The slate says the answers adopt the position. |
| SOLR-16013 | DECISION | 408317d7e241 | History rewrite (slate decision 4). Already pushed and re-gated green. See note A. The draft cites 408317d7e241 (lines 15 and 38), not the old head. |
| SOLR-9155 | DECISION | 9f08d0330233 | Declared-`InterruptedException` route (slate; no recommendation recorded). The slate asks to confirm the recorded date (2026-10-06). |
| SOLR-12651 | HOLD | f3131d1ee846 | Draft line 39: lands after SOLR-9598. Draft lines 25 to 31: the owner Choice on Tomas Lobbe's cleanup question, which receipt line 8 calls a submission decision. The slate says the changelog title needs a branch commit; not re-checked here. Live-tip gate green (receipt lines 3 to 5). |
| SOLR-13186 | HOLD | b436d90d2a88 | The receipt body has no gate date. Line 9 is a ledger citation. Record a dated gate line. |
| SOLR-15035 | HOLD | 12d7491e82cd | Confirm the ticket summary (slate). SOLR-11479 waits on this. |
| SOLR-15106 | HOLD | 40b7e5d0efa7 | Draft line 21 says the base code cannot compile the test, so no base failure is shown. Receipt line 7 says the premise step "fails as designed". Record the base failure text and a dated gate line (line 9 is a takeover-log citation). |
| SOLR-15674 | HOLD | ba01c83d4c5a | About 4,984 characters, over the guide. Draft lines 29 to 33 still ask the adopted mzxid choice as a question; restate it as settled (slate: adopted). |
| SOLR-15863 | HOLD | f381fd8d4dd1 | Changelog capitalisation needs one branch commit (slate), then a re-gate at the new head. |
| SOLR-17680 | HOLD | f4b8ce833654 | A dated gate line is owed (group). The slate also asks to confirm the Proof's "10 before, 11 now" count. The head's commit body says "The branch is gated now; the receipts live in the queue records" (cross-cutting 2). |
| SOLR-17733 | HOLD | 636196b7954a | A dated gate record is owed, or a re-gate at 636196b (group). That commit is dated 2026-10-05. |

Counts: READY 5, DECISION 6, HOLD 8.

## Decisions

Adopted, because the decision ladder says to take the recorded recommendation or the position the answers adopted. None of these is committed to a branch in this round.

- **SOLR-14919:** processor alone (slate decision 1). The slate lists the ticket under Held, so the branch commit that removes the marker and its gate follow.
- **SOLR-11288:** keep blank-only equal to omitted (slate decision 3). Needs the changelog title commit and a re-gate first.
- **SOLR-16013:** approve the history rewrite, without squashing (slate decision 4). Already done; see note A.
- **SOLR-15386:** the cached live-node check, which the answers adopt (slate).

Open, with no recorded recommendation, so not decided here:

- **SOLR-13369:** the Choice (slate says "confirm the Choice").
- **SOLR-9155:** the declared-`InterruptedException` route.
- **SOLR-12651:** the Choice on optional restore cleanup, and the order after SOLR-9598.

## Note A: force-pushed rewrites

- SOLR-16437: the head `f72a9edd0a8b` is a message-only rewrite of `aa2a6b8afb6`. The tree is identical. It was pushed with `--force-with-lease` (receipt line 5).
- SOLR-16013: the history was rewritten at `408317d7e241` and pushed with `--force-with-lease` (receipt line 10), after a green re-gate (receipt lines 5 to 9).

AGENTS.md says the fork is never force-pushed. Nick should confirm both rewrites stand.

## Cross-cutting findings

1. **Gate dates.** Holds that cite a missing body date: SOLR-13186 (line 9 is a ledger citation) and SOLR-15106 (line 9 is a takeover-log citation). The group reports list SOLR-17680 and SOLR-17733 as well. SOLR-16437 (line 3) and SOLR-11479 (line 4) state their dates in the body. SOLR-17292, SOLR-5813 and SOLR-12991 state none, but their READY verdicts do not need one.
2. **Process words and history.** The head of SOLR-16437 has the subject "remove the handoff note (packaging for the first gate)", so "gate" shows on the PR's commit list. The head of SOLR-17680 has the body "The branch is gated now; the receipts live in the queue records". Rewording either changes the head, which means a re-gate or a recorded carry-over.
3. **Draft length.** Group counts: SOLR-15674 about 4,984 characters with links, SOLR-15386 about 4,465, SOLR-15106 about 4,072, SOLR-15863 about 4,035. The guide is about 3,500.
4. **Slate items now overtaken.** Checked here: SOLR-16437 "first gate owed" (green at its head, receipt lines 3 to 5); SOLR-11479 "first gate owed" (green, receipt lines 3 to 4); SOLR-17292 "remedy not in the branch" (receipt line 8); SOLR-16013 "history rewrite and re-gate owed" (done, receipt lines 5 to 10); SOLR-12651 "run at the live tip owed" (green, receipt lines 3 to 5). Still open per the slate: SOLR-13186's date, SOLR-15035's summary, SOLR-15863's capitalisation. `reports/solrcloud-openings-slate.md` should be refreshed from this table.

## Opening order (Nick's call)

SOLR-5813, SOLR-12991, SOLR-16437 and SOLR-17292 do not depend on each other and can open in any order. SOLR-15035 goes next once its summary is confirmed, then SOLR-11479.

## Not done

- Nothing was opened, posted, or pushed to a submit branch.
- No build, test, gate or JIRA call was made.
