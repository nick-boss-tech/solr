# Query parsing full-category review: summary

Assignment: `ASSIGNMENT.md` on this branch (query parsing, full-category review to SATISFIED).
Reviewer: Windows review host, claim `claims/query-parsing-review.md`, 2026-10-13.
Base: this branch at `98691e0eb2d`. Sub-batch reports: `reports/query-parsing-review-q1.md` to `-q4.md`.

## Status

The assignment is not discharged. Three drafts are OPEN (gate): 6014, 12608 and 17796. Each needs a branch change that goes through a gate, and this lane has no gate path. The review on those three continues on the gated head.

What is done:
- Every draft under `pr-drafts/query-parsing/` (18) was checked against its receipt, its live tip, its citations at the SHA they name, its merge-base, and its Lucene or branch-line claims.
- Every wording fix found was applied in this tree.
- Every code or changelog fix that the findings call for is written as a gate request below. None is patched in this pass.

## The 25 round 1 tickets, one line each

Drafted, SATISFIED and ready to open (opening needs Nick's approval; PR opening is reserved):

1. SOLR-874: SATISFIED, ready to open. No edits.
2. SOLR-4824: SATISFIED, ready to open. Two wording edits (no "We"; an unchecked expectation removed). Lucene default 50 checked in 9.12.3 and 10.4.0.
3. SOLR-8977: SATISFIED, ready to open. No edits. Cross-line check: 9.x and 10.0 have no query-parser repair.
4. SOLR-9048: SATISFIED, ready to open. Base citation fixed. One sentence ("An empty v takes the same path today") is not checked; main side to verify or drop.
5. SOLR-9149: SATISFIED, ready to open. One wording edit (no "We").
6. SOLR-10897: SATISFIED, ready to open. No edits.
7. SOLR-11391: SATISFIED, ready to open. Jira link placeholder fixed. Receipt is GREEN at the live tip, so this is gated (round 1 said NO GATE; see disagreements).
8. SOLR-11761: SATISFIED, ready to open. No edits.
9. SOLR-12212: SATISFIED, ready to open. Proof counts and date added. Grammar pair checked: generated `QueryParser.java` matches `QueryParser.jj`.
10. SOLR-12532: SATISFIED, ready to open. Lucene sentence corrected (single-token paths stay term queries; cited as release source). One "We" removed.
11. SOLR-12871: SATISFIED, ready to open. Verification date added; one unverified metadata claim removed.
12. SOLR-15615: SATISFIED, ready to open. Failed-lookup wording corrected from the receipt's trace (only server and I/O errors are skipped; a refusal ends in the 400, not a 403).
13. SOLR-16267: SATISFIED, ready to open. Limits corrected: `def()` and `percentile` are not reached by the new assertions. One "We" removed.
14. SOLR-17280: SATISFIED, ready to open. Cache tradeoff moved into Limits (the receipt says it is Limits material). Two "we" removed. PR 1481 checked: closed, not merged.
15. SOLR-17311: SATISFIED, ready to open. Lucene null-top-value claim checked in 9.12.3 and 10.4.0.

OPEN, gate required (wording now matches the record; a branch change is requested):

16. SOLR-6014: OPEN (gate). Limits now states the 2026-10-13 verdicts plainly, and the Change line is qualified. In-branch fix requested after a base run (G1, decision D1).
17. SOLR-12608: OPEN (gate). Premise sentence added to Proof; an internal bullet in Limits replaced. The branch changelog title overclaims a memory benefit (G2, decision D3).
18. SOLR-17796: OPEN (gate). Limits states the negated-collapse verdict plainly. Branch changelog title overclaims; the negated case needs an in-branch fix (G3, decision D2).

Not drafted, and excluded:

19. SOLR-13202: EXCLUDED. PR #5012 is merged upstream (receipt, 2026-10-10 status correction). Live tip matches.
20. SOLR-13838: NO DRAFT YET. Reconciled green at the live tip, but the receipt has no gate log and no date. Main side to supply them.
21. SOLR-13903: NO DRAFT YET. Same record gap as 13838.
22. SOLR-15906: NO DRAFT YET. Gate green at the live tip (`g15906r35-gate.log`). Queue for drafting; its history (an earlier head's regression) goes in Choice or Limits.
23. SOLR-16130: LIVE PR, TEXT REVIEW ONLY. No draft file exists in this folder. The live body (PR 5004) was reviewed read-only. Three wording findings are for the main side (q4). No PR edits from this lane.
24. SOLR-16570: NO DRAFT YET (audit only). Receipt GREEN at the live tip `9d3466101a`. Noted as a collapse pair with 17796, not audited.
25. SOLR-17882: EXCLUDED. Premise dead; retire candidate. The retire-or-re-aim call stays with the owner, as the record says.

Counts, 25 tickets: SATISFIED and ready to open 15 (of the 18 drafts); OPEN (gate) 3; no draft yet 4 (13838, 13903, 15906, 16570); excluded 2 (13202, 17882); live-PR text review 1 (16130). 15 + 3 + 4 + 2 + 1 = 25.

## Gate requests (for the gate coordinator to stock; not posted here)

Gates live on `pr-prepare` under `gates/`. The rule in the coordinator handoff is that a review finding needing a code change and a gate is written up as a gate job request for VM2 to stock. These are those requests. Each one names its branch, head, base and test shape.

G1. SOLR-6014, branch `solr-6014-submit`, head `505849d2d4e7d8699a2fe34d410773b8ecf3a55a`.
- Step 1 (information only, no code change): run the same stopword-only dismax probe at merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. Cover the filter query, `cache`, and `cost` with `AND` cases. Record for each: `NullPointerException` (with the throwing frame), whole corpus, or no documents, with numFound.
- Step 2 (after step 1 is recorded): in-branch fix for the cases the change does not cover (decision D1). Written by the Windows review coordinator on the branch, pushed, then gated on a Linux host.
- Gate shape: focused class `org.apache.solr.search.TestExtendedDismaxParser`, seed `6014C0FFEE6014`, with new test cases for the fixed cases; fail-before at the merge-base.
- Stop rule: if step 1 shows the `NullPointerException` exists on the merge-base and is unrelated to the blank-clause fix, record it in the gate file and escalate the scope to the main agent before any in-branch change.

G2. SOLR-12608, branch `solr-12608-submit`, head `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2`.
- Changelog-only change. Replace the three-line title block in `changelog/unreleased/SOLR-12608-collapse-repeated-wildcards.yml`:
  - current: "Runs of unescaped * in a wildcard query term are collapsed into a single *, which avoids the excessive memory use seen with edismax queries made of thousands of * characters."
  - replacement: "Runs of unescaped * in a wildcard query term are collapsed into a single *."
- Gate shape: changelog parse and tidy, then the focused class `TestSolrQueryParser` at the new head.
- Main-side item with it: quote the observed base failure line from the premise log (`g12608-premise.log`) for the draft's Proof. The receipt does not include it.

G3. SOLR-17796, branch `solr-17796-submit`, head `661165d2673de9211846e276c05daf1fd0e879d6`.
- Part (b), the in-branch fix (decision D2): a negated collapse under `q.op=AND` (`fq=-{!collapse field=x}`) still throws `UnsupportedOperationException` from `CollapsingPostFilter.createWeight`. Fix in the `Query` production of `QueryParser.jj` (L232-245 at head), then regenerate `QueryParser.java`, so the checked-in generated file matches the grammar. Add a test that fails on the merge-base `56ec140e3636d5f4150fa87fbf7103529536ac99` and passes with the fix.
- Part (a), the changelog title. Keep the current title only if part (b) lands. If part (b) does not land, narrow it to: "A lone required collapse or other post filter in a filter query, such as fq={!tag=t}{!collapse field=x}, no longer fails with an UnsupportedOperationException when q.op is AND."
- Gate shape: focused class `TestCollapseQParserPlugin`, seed `17796C0FFEE17796`, changelog parse, and a grammar regeneration check.
- Ordering note: 12212 also changes the `Query` production (`QueryParser.jj` L237-244 at its head). A textual conflict is likely when the two branches land. Not verified by rebase.

## Decisions (recorded under the decision default)

- D1 (SOLR-6014): the `cache`, `cost` and filter-query cases are the same stopword-only `{!dismax}` clause the ticket covers. The harder option applies: an in-branch fix, after the base run in G1. Until then, the draft's Limits states the verdicts plainly.
- D2 (SOLR-17796): a negated collapse under `q.op=AND` is the same defect class as the ticket. The harder option applies: an in-branch fix (G3b). Until then, the draft's Limits states the verdict.
- D3 (SOLR-12608): the branch changelog claims a memory benefit that the record does not show. The title is corrected, not the Limits softened (G2).
- D4 (SOLR-11391): the receipt at the live tip is treated as the current gate record over round 1's "NO GATE". Main side to confirm the record.
- D5 (SOLR-16130): no draft file exists in this folder, so there is nothing to edit in this tree. The live body was reviewed read-only. Findings go to the main side.

## Owner items

None survived the three-rung ladder. Each open question either has a gate step that gives the missing information (G1 step 1) or has a decision that the default settles (D1 to D3).

Still with the owner, as the record already says: the retire-or-re-aim call on SOLR-17882.

## Round 1 disagreements with the receipts

- SOLR-11391: round 1 said NO GATE and audit only. The receipt now records GATE GREEN at `b0f15a22856` (gate finished 2026-10-09). The receipt stands at the live tip, so it is treated as gated (D4).
- SOLR-16570: round 1 named `974c44f96` as the live tip and NO GATE. The live tip is now `9d3466101a`, and the receipt records GREEN at that head.
- SOLR-13202: round 1 said APPROVED and awaiting merge. The receipt now says merged (PR #5012), per the 2026-10-10 status correction.
- SOLR-6014: round 1 said gate green at `505849d2d4e`. The 2026-10-13 test run adds verdicts. No head disagreement.
- Heads that agree: 874, 4824, 6014, 8977, 9048, 9149, 10897, 11761, 12212, 12532, 12871, 13838, 13903, 15615, 15906, 16267, 17280, 17311, 17796, 17882, and the live PR 5004 head.

## Interactions noted (not audited; no draft to audit against)

- 12212 and 17796 both change the `Query` production in `QueryParser.jj`. Both generated files match their grammar. Landing order needs a rebase check (see G3).
- 8977 and 12212 are the auto-fix premises. 15906 changes `QParser.java`. If 15906 lands, the framing of 8977 and 12212 needs a check. No 15906 draft exists to audit.
- 16570 and 17796 both touch the collapse path. 16570 changes `CollapsingQParserPlugin.getTopFieldCacheReader`. 17796's tests are in `TestCollapseQParserPlugin`, which 16570 also extends.

## Not checked

- Jira text for every ticket. No Jira packet was opened, so the statements that rest on tickets or comments are unverified: 874's earlier patches, 4824's hit-count report, 17280's maintainer comment and the ticket's hash-collision cause, 6014's ticket example, 8977's report, and 12608's Solr 7.2.1 and 7.4 report.
- Gate logs. They are not on disk here, so counts are taken as the receipts record them.
- Full test contents. Only the cited blocks were read.
- Sentences with no citation: 9048 "An empty `v` takes the same path today"; 9048 "children outside the parents"; 8977 "the `doGraph` assertion also passes on the base"; 11761 the name of the discriminating test; 12608 the observed base failure; 17311 "only string child fields use this comparator"; 16267 "missing" as a facet stat name; 16130 CI commit `4c151ce1b79` and the body's description of the test.
- No builds, no tests, no gate runs, and no writes to GitHub.

## Push state

- Claim commit `9ecdc53f8a2` is on `origin/assignment-query-parsing-review`. It was pushed through `research\push-pr-prepare.ps1 -Branch assignment-query-parsing-review`, after the wrapper was updated to take a `-Branch` parameter with an allowlist. The fast-forward rule and the dirty-tree check are unchanged.
- This report set and the draft edits are committed on top of the claim, and are pushed by the same wrapper in the same session. The result is recorded in the final message, not here, because the hash is not known until after the push.
