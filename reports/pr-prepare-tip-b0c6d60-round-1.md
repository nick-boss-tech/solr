# pr-prepare tip b0c6d60: draft fidelity round 1

- Date: 2026-10-14 (UTC).
- Claim: claims/pool-pr-prepare-tip-b0c6d60-round-1.md (claim commit 515c0d73172, on top of the live tip b0c6d60d7b9).
- Last handled tip before this round: 62392e23e18.
- Scope: the 19 tickets whose receipt, gate file or draft changed between those two tips. Each draft ends as SYNC (matches the receipt at the live fork head, no edit), REFRESH (edited so every head, count, date and link comes from the newest green gate at the live fork head), or HOLD (not ready to post; one HTML comment at the top of the draft gives the reason).
- Staffing: five subagents, about four tickets each. Each read its receipts, gate files and drafts, checked the live fork head with ls-remote, and edited only its own drafts. No builds, Gradle or tests. No gh, PR, comment, JIRA or live PR body call. No fork push.
- Main-side checks, read-only: fetched the fork branches for 10305, 10424, 10492, 8051, 6759, 7390, 6831, 8088, 9864 and 16725 and compared tree hashes.

## Verification run by the main side

- 10305: rewritten head 86ddbd433dba has tree c170170d, the same tree the receipt names. The newest green gate, c43de86d4c13, has tree 9ec36b81, a different tree. The gate at f559126972 that carries c170170d ran only the changelog, spotless and compile steps, with no focused run.
- 10424, 10492, 8051: the carried heads 0072435a9d12, ba2ee092aafc and 65d0c968d8a4 have trees d52d1c5a, e0da2965 and 75461d72, matching the receipts.
- 6759: head 7f8d08e34b11 has tree b1b42510, the same as the gated head 42f03eb52376.
- 7390: the diff from the gate commit 72caa363e29 to the current head 7463dd006a7 is only the deletion of SOLR-7390-TESTING.md.
- 16725: at 550af283134e the ClusterStatus helper sits at L341 to L353 and its call at L377, so the draft's anchors hold.
- 6831 and 8088: the new heads change only the changelog title line. 9864: the new head changes only the test file (four lines), and the draft's production links are unaffected.
- 17055: the fork branch solr-17055-submit is at c2396f425fa2, the proof base named in the implementation-choice report.

## Outcomes

| Ticket | Outcome | Head in draft | Newest green gate | Notes |
|---|---|---|---|---|
| SOLR-6831 | REFRESH | 24bd15ebb84e | 24bd15ebb84e, 2026-10-14 | Links re-pointed. Live PR body re-point still owed. |
| SOLR-8088 | REFRESH | c36cfc094af4 | c36cfc094af4, 2026-10-14 | Title and two bold summaries narrowed to "non-numeric", per the gate record. Main side to confirm the live body. |
| SOLR-9864 | REFRESH | 176ed67491b5 | 176ed67491b5, 2026-10-14 | Four links re-pointed. The receipt says the live body is already re-pointed. |
| SOLR-5394 | HOLD | none (gate ran at local 6e4c302c418b) | local head only, not pushed | Fork head 967445622f is unchanged. The push waits for Nick's go. |
| SOLR-10305 | HOLD | 86ddbd433dba (rewrite) | c43de86d4c13, 2026-10-09 (tree 9ec36b81) | Rewritten head has tree c170170d with no focused run. Needs a ruling on the text gate or a focused run at 86ddbd43. Replacement opening needs Nick's approval. |
| SOLR-10424 | REFRESH | 0072435a9d12 | e629ab8bb293, carried by the rewrite (tree d52d1c5a) | Date dropped from the counts line. Replacement opening needs approval. |
| SOLR-10492 | REFRESH | ba2ee092aafc | ecf21e2192cb, carried by the rewrite (tree e0da2965) | Replacement opening needs approval. |
| SOLR-8051 | REFRESH | 65d0c968d8a4 | e50a2aa3437c, carried by the rewrite (tree 75461d72) | Replacement opening needs approval. |
| SOLR-6759 | REFRESH | 7f8d08e34b11 | 42f03eb52376, carried by the rewrite (tree b1b42510) | One Limits bullet added: the reporter's access-control case is not reproduced. Replacement opening needs approval. |
| SOLR-6975 | SYNC | 761aa629bb83 | 761aa629bb83, DONE 2026-10-05 | No verification date in the draft. See open items. |
| SOLR-7390 | REFRESH | proof names gate commit 72caa363e29 | 72caa363e29 | Head delta is only the handoff doc deletion. Date form open. |
| SOLR-7498 | SYNC | 2050d8e447a7 | 2050d8e447a7 | Date is the 2026-10-05 record date. Date form open. |
| SOLR-8003 | REFRESH | 17a53056b7c5 | 17a53056b7c5, fixture gate 2026-10-14 | Proof 4 of 4, base run 4 tests with exactly 2 failing. Receipt says the live body was re-pointed; not checked. |
| SOLR-9396 | REFRESH | dc862ccfd375 | a5ab2eda4e6a, carried by the rewrite (tree f2d58a851ac2, per receipt) | Replacement opening needs approval. Tree not re-fetched here. |
| SOLR-9595 | REFRESH | e18a8063e6af | bf99cd0e89c2, carried by the rewrite (tree 49fd20f7, per receipt) | Replacement opening needs approval. Tree not re-fetched here. |
| SOLR-17055 | HOLD (no edit) | c2396f425fa2 (fork tip) | score-path gate at c2396f, 2026-10-10 | Sort-path fix not landed. Rerun gate is OPEN. Winner 2fe0986 is not on the fork. |
| SOLR-15906 | SYNC | c23ee8912eb1 | c23ee8912eb1, 2026-10-14 | Draft matches the receipt. Receipt line 11 still says the draft carries OWED. |
| SOLR-16725 | REFRESH | 550af283134e | 550af283134e, 2026-10-14 | Links and proof line moved from be1838ef8ccf. Counts unchanged (7 tests, 1 skipped, 0 failures). |
| SOLR-14919 | HOLD | none (hold line added) | 8f1f4ef91e, 2026-10-13 | Newest gate at 483b3885f643 has proof leg rc 1 with 2 marker hits. Not promoted to GREEN. |

## Open items for the main side

Nothing below was done in this round. Each item needs the main side or Nick.

1. Live PR body re-points, none done here (no gh calls): PR #5134 (SOLR-6831) to 24bd15ebb8, owed by the claim. PR #5144 (SOLR-8088): confirm the live summaries read "non-numeric". PR #5140 (SOLR-8003): confirm it shows 17a53056. PR #5131 (SOLR-5394): re-point after the push. SOLR-15906 and SOLR-16725: check links if a PR is open on either branch.
2. Decisions for Nick:
   - Push the SOLR-5394 changelog type change (the owner ruling approving it is recorded for 2026-10-14).
   - SOLR-10305: rule the text gate GREEN for the identical tree, or run a focused test at 86ddbd43 under a verify request.
   - Approval for replacement openings for the closed PRs: #5151 (10305), #5152 (10424), #5153 (10492), #5143 (8051), #5133 (6759), #5148 (9396), #5149 (9595).
3. SOLR-17055: solr-17055-submit is already at c2396f425fa2, the proof base. The implementation-choice report says the restore is a main-side act, and this round did not do it, so confirm who moved the branch. The winner 2fe0986 is still not on the fork. The rerun gate is OPEN. The records do not say whether the commit choice needs Nick's sign-off.
4. SOLR-14919: the main side classifies whether the proof leg rc 1 is the intended result.
5. Date form: 6975 has no verification date. 7390 and 7498 say "Verified 2026-10-05", but that date is the takeover record date, not a run date. Pick one form and apply it to all three. The adopted rule (material/solrcloud-round-1-answers.md) uses record dates in "recorded" form.
6. Stale records: receipts/SOLR-15906.md line 11 still says the draft carries OWED. The gate file headers for 10424, 10492 and 8051 still say "Status: OPEN, needs Nick's direction", while their outcome sections say the rewrite ran. The step checklist in gates/SOLR-14919-precision-round-1-gate.md is unticked.
7. Staging blocks: the drafts for 10424, 10492 and 8051 carry a staging-notes block at the top. Strip it before any posting.
8. Evidence gaps: there are no gate files for 6759, 6975, 7390, 7498, 8003, 9396 or 9595, and the logs the receipts name are not in the tree. The 5394 local gate head 6e4c302c418b is not in this clone, so its HOLD rests on the gate record. The 9396 and 9595 tree identities rest on the receipts.

## Checks on the drafts

- No em dashes in any added line. No trailers or attribution beyond the template's AI header and footer.
- Proof numbers come only from receipts or gate records named in each draft.
- The draft set changed is 15 files. The other four in scope (SOLR-6975, 7498, 15906, 17055) are unchanged.
