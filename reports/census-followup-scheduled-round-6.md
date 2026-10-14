# Census follow-up, scheduled round 6 (settlements and a spot-check of the morning slates)

- Trigger: the two-hourly scheduled check. Recorded tip `b0792c5f840`; live tip at the check `7dc0ccc026f`. Claim: `claims/pool-census-followup-round-6-scheduled.md` (pushed at `b40db99ce19`).
- The new commits before the check were vm2's gate records and claims (the SOLR-17215 and 17393 gate runs, the SOLR-7119 changelog drop, the SOLR-15712 gate failure at `2d65ed6329`, the SOLR-11356 header-copy gate green at `8b2e826154`, the SOLR-17055 gate claims), the main side's SOLR-17055 proof-base decision (`cba5907c2c0`), and the morning openings slates (`reports/morning-openings-slates-2026-10-14.md`). No new assignment was posted.
- Worktree: `wt/pr-prepare-suggester`. Live fork tips checked by `git ls-remote origin` for every ticket. Each draft names the head its receipt or record names.
- Read only for branches: no build, test, Gradle or gate run; no commit or push on any `solr-*-submit` branch; no GitHub write; no receipt, gate file, claim or slates report edited by the review side.
- Written by the parent from the four subagent replies. The subagents could not write report `.md` files.

## Result

- 14 tasks: 6 settlements and 8 slate spot-check rows. 2 SATISFIED, 4 HOLD among the settlements. Of the 8 slate rows, 7 match and 1 is a discrepancy; one more row has a Proof-date discrepancy.

## Settlements (slices S1 and S2)

- SOLR-7119: SATISFIED. `pr-drafts/build-docs/SOLR-7119.md` at gated head `af336af5ab8a` (live tip; the receipt's packaged head; lint rc=0, 2026-10-14). The Changelog line is deleted, citations are re-pointed to `af336af5`, and the docs build is kept at `4c652c93` (2026-10-11) with an inheritance sentence. Gap: `af336af5` is not in this clone, so the "page unchanged" claim rests on `receipts/SOLR-7119.md`.
- SOLR-17215: HOLD at `gates/SOLR-17215-receipt.md`, step 8. `receipts/SOLR-17215.md` is not on the branch. Commit `7dc0ccc026f` changed only `gates/` and `claims/`. The live tip `04d35df186dd` matches the gate head. The draft is not edited. The coordinator's run records in the gate file are not GREEN (the pre-fix leg returned rc=1), so they are not receipts. **Main-side decision**: whether the expected pre-fix rc=1 counts as the pass. Then vm2 writes the receipt.
- SOLR-17393: HOLD at `gates/SOLR-17393-receipt.md`, step 8. `receipts/SOLR-17393.md` is absent; commit `febe813579e` changed only `gates/` and `claims/`. The live tip `626241e647e5` matches. Same decision as SOLR-17215.
- SOLR-11356: SATISFIED. `pr-drafts/solrj/SOLR-11356.md` at gated head `8b2e826154` (live tip; receipt green 2026-10-14). Head citations, Proof (base 13 tests, 2 failures; head 13 of 13 on Jetty and JDK), and Limits are updated from the receipt. The header-copy hold and the JDK line are cleared. The draft does not state a push. **Owner item E1**: there is no owner yes on record for the packaged push to the open PR head.
- SOLR-15712: HOLD. `pr-drafts/schema-analysis/SOLR-15712.md`. The banner names the failed gate: head `2d65ed6329`, 4 of 5 focused tests failed, not relaunched. The next step is to fix and re-gate, or revisit the decision. The draft still describes `cb988a2ff4c`.
- SOLR-17055: HOLD. `pr-drafts/search-components/SOLR-17055.md`. The banner names the decided proof base `c2396f425fa` (the live tip; the fix commit `2fe0986158` is not on the fork or in the clone, so the parent is taken from the record). Proof and Limits name the pending sort-case fail-before re-run, `gates/SOLR-17055-sort-path-proof-rerun.md`, which has not run.

## Spot-check of slate rows 1 to 4 (slice S3): SOLR-4374, 5394, 6207, 6759

All four rows are in the Search components slate, as-is (no `*` marker). Drafts and receipts exist for all four.

- SOLR-4374: MATCHES. Tip `801c62290f79` (ls-remote); the change line matches the draft's bold summary; receipt GATE GREEN at that head (16 of 16). Census READY TO OPEN; ready-list READY.
- SOLR-5394: MATCHES. Tip `967445622f92`; the change line matches; receipt GREEN at that head (49 tests, 0 failures). Census REVIEW OWED; follow-up round 1 SATISFIED. The changelog type "fixed" is an open owner item, not a marked row.
- SOLR-6207: MATCHES. Tip `b099a9f1be5a`; the change line matches; receipt GREEN at that head (1 of 1). The draft's Proof has no verification date; the owner's advisory ruling covers it, and the slate caveat notes it.
- SOLR-6759: DISCREPANCY. The tip `42f03eb52376`, the change line, and the receipt all match. But `reports/receipt-refresh-round-2-b2.md` (2026-10-10) holds it: changelog line 2 ("for example block collapse") overclaims, and the try/finally fix is not applied at the tip. The owner call is F2. The census round 3 record (SATISFIED, 2026-10-14) flagged the changelog but did not hold it, and the slate lists it as-is with no packaging step or owner item. **Main side**: reconcile the slate row with the receipt-refresh hold.

## Spot-check of slate rows 5 to 8 (slice S4): SOLR-6831, 6975, 7390, 7498

All four rows are in the Search components slate, as-is (no `*` marker, and not in an owner-approval item).

- SOLR-6831: MATCHES. Tip `96b33ba5f6f8`; the change line matches; receipt GATE GREEN at the same head (TestQueryLimits 4 of 4). Ready-list hold was only S2 (no Proof date), which the owner's advisory ruling clears.
- SOLR-6975: MATCHES. Tip `761aa629bb83`; the change line matches; receipt GREEN at the same head (DistributedQueryComponentOptimizationTest 10 of 10). The draft's "A choice to check" is open for the reviewer and is not in the owner items list.
- SOLR-7390: MATCHES on the change line and the head; **Proof-date discrepancy**. Tip `7463dd006a78`; receipt GREEN at the same head (ReturnFieldsTest 15 of 15). The receipt says the gate ran at `72caa363e29` with identical content, while the draft says "Verified 2026-10-05 at this head". **Main side**: correct the Proof date to the receipt's gate date and head.
- SOLR-7498: MATCHES. Tip `2050d8e447a7`; the change line matches; receipt GREEN at the same head (ExtractionBackendMetadataTest 2 of 2). The receipt does not record a changelog YAML parse; the ready-list hand-read covers it.

## Gate jobs filed this round

None. Every settlement in this round was either cleared on the record or held on a decision or a missing receipt.

## Owner items and main-side decisions raised by this round

1. SOLR-17215 and SOLR-17393: decide whether the pre-fix rc=1 counts as the pass; then vm2 writes the receipts. The 2026-10-09 GREEN in `material/suggester-17215-receipt.md` rests on the receipts ledger, which the date rule does not accept.
2. SOLR-11356: owner yes or no on the packaged push to the open PR head (E1).
3. SOLR-15712: fix and re-gate, or revisit the decision.
4. SOLR-17055: the pending sort-case fail-before re-run. The decided proof base is `c2396f425fa`.
5. SOLR-6759: the slate lists it as-is, but the receipt-refresh round 2 holds it (changelog overclaim; the try/finally owner call F2). Reconcile the slate.
6. SOLR-7390: correct the Proof date to the receipt's gate date and head (`72caa363e29`).
7. SOLR-5394: the changelog type "fixed" is an open owner item.
8. SOLR-6207: the Proof has no verification date (advisory).
9. SOLR-7119: the "page unchanged" claim rests on the receipt, because `af336af5` is not in this clone.
10. SOLR-6831 and 6975: the "A choice to check" sections are open for the reviewer; neither is in the owner items list.

## Still open from earlier rounds

- SOLR-8954: owner decision 1 (close, or a test-only follow-up with SOLR-8009).
- SOLR-14200 and SOLR-15326: retire or keep.
- SOLR-17433: retire or ship.
- SOLR-10390: withdraw as superseded by SOLR-18339, or keep the test as a guard.
- SOLR-3684: submit or retire.
- SOLR-17987: confirm the rename is deferred to opening.
- SOLR-3923: a branch fix before the premise run (`gates/SOLR-3923-premise.md`, OPEN).
- SOLR-9968: confirm the 2026-10-09 top-up gate ran at `c31d2ff1a3f`.
- SOLR-14231: vm2 to restate the counts and add the fail-before line.
- SOLR-4754: HOLD from round 1; gate `gates/SOLR-4754-text-fixes-and-premise.md` is OPEN.
- SOLR-8628 and SOLR-10234: opening held on gates `gates/SOLR-8628-packaging.md` (OPEN) and decision 8.
- SOLR-3865: owner decision 8.
- SOLR-13838: a fail-before run at `4520c25abe2` and the ticket text are owed.
- SOLR-14098: the ledger date conflict is with the main side.
- SOLR-18278: owner retire call.
- SOLR-16887: gate `gates/SOLR-16887-errorfile-bats-name.md` (OPEN).
- SOLR-15906: the vm2 gate on the sort-paren reading (`gates/SOLR-15906-sort-paren-fix.md`) is still OPEN.
- SOLR-10641: ship-alone against the DISCUSS item 11 in the answers file.
- SOLR-17292: receipt reconcile.
