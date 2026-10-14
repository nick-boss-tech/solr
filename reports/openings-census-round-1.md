# Openings census, round 1: every prepared branch, by category

Built 2026-10-13 (evening, MDT) from the live records: `receipts/` and the category records on this branch at tip `e2c2b4cf2b`, live fork tips from `git ls-remote` taken the same evening, and the live open-PR list on apache/solr (63 open PRs whose head branches are on this fork). One record landed while the census was being built and is incorporated: the SOLR-16272 title gate at its new head (see its row).

Why this exists: the W1 ready lists (`reports/category-ready-lists-round-1.md`) count 30 READY drafts, but they cover only the search-components and query-parsing verdicts. The fork holds 342 prepared branches across all categories. This census classifies every one of them that is not already an open PR and not retired, so the morning openings work from the whole pool, not from two categories.

## How to read a row

- Gate at tip: the receipt (or the main receipts ledger, for the suggester branches that carry no receipt file) read against the live fork tip. GREEN with a date means a gate record at the exact live tip. STALE TIP means the gate record stands at an older head and names it. VERIFIED is the word the receipt itself uses for script-only and docs-only evidence shapes (BATS runs, light gates, harness runs) at the live tip.
- Draft: CONVERGED or SATISFIED names the category record that settled the draft text; written means a draft file exists under `pr-drafts/` with no convergence verdict on record; owed means no draft file exists.
- Disposition is exactly one of: READY TO OPEN (gate green at the live tip and draft converged), GATE OWED, REVIEW OWED, HOLD (the hold is named), OWNER ITEM (the item is named).
- A receipt at the exact live tip settles gate state. Where a receipt and a tip disagreed, the row follows the later record and the cross-check section at the end names every such case.

## Totals

| Category | Branches | READY TO OPEN | GATE OWED | REVIEW OWED | HOLD | OWNER ITEM |
|---|---|---|---|---|---|---|
| Search components | 66 | 19 | 0 | 40 | 5 | 2 |
| Query parsing | 22 | 11 | 3 | 7 | 1 | 0 |
| SolrCloud | 25 | 4 | 2 | 13 | 4 | 2 |
| Core admin | 28 | 15 | 2 | 10 | 1 | 0 |
| SolrJ | 20 | 8 | 1 | 10 | 0 | 1 |
| eDisMax | 13 | 0 | 0 | 11 | 2 | 0 |
| Schema | 11 | 7 | 0 | 3 | 1 | 0 |
| Replication and backup | 11 | 6 | 0 | 2 | 3 | 0 |
| Streaming | 10 | 4 | 1 | 3 | 1 | 1 |
| CLI | 12 | 7 | 1 | 2 | 1 | 1 |
| Build and docs | 13 | 5 | 1 | 5 | 1 | 1 |
| Configsets | 5 | 4 | 0 | 1 | 0 | 0 |
| Security | 4 | 1 | 0 | 0 | 2 | 1 |
| Spellcheck | 9 | 0 | 0 | 8 | 0 | 1 |
| Suggester | 8 | 0 | 0 | 5 | 0 | 3 |
| Highlighting | 2 | 0 | 0 | 0 | 2 | 0 |
| Metrics | 1 | 0 | 0 | 1 | 0 | 0 |
| Admin UI | 2 | 0 | 0 | 0 | 2 | 0 |
| Update processing | 1 | 0 | 0 | 0 | 1 | 0 |
| **Total** | **263** | **91** | **11** | **121** | **27** | **13** |

**READY TO OPEN: 91 branches** (gate green at the live tip and draft converged), against the 30 in the W1 lists. The other 172 rows carry a named next step: 121 review rounds, 11 gates, 27 holds and 13 owner items. The top-up section at the end lists the gate jobs and review assignments this census posted so the short rows keep moving tonight.

## Search components (66)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-3044 | solr-3044-submit | 04b877e9de18 | NO GATE (recorded) | owed | HOLD | parked |
| SOLR-4374 | solr-4374-submit | 801c62290f79 | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-5394 | solr-5394-submit | 967445622f92 | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-6193 | solr-6193-submit | ec94bf50c80c | GREEN 2026-10-05 | written | HOLD | scope disputed in the receipt; a gate job is open |
| SOLR-6207 | solr-6207-submit | b099a9f1be5a | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-6759 | solr-6759-submit | 42f03eb52376 | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed |
| SOLR-6831 | solr-6831-submit | 96b33ba5f6f8 | GREEN 2026-10-06 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-6975 | solr-6975-submit | 761aa629bb83 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-7390 | solr-7390-submit | 7463dd006a78 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-7498 | solr-7498-submit | 2050d8e447a7 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-7520 | solr-7520-submit | 10b6931e1c05 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-7550 | solr-7550-submit | 687165651f92 | GREEN 2026-10-03 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-8003 | solr-8003-submit | 17c516a607a8 | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed |
| SOLR-8009 | solr-8009-submit | 8795661ddc98 | GREEN 2026-10-12 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-8020 | solr-8020-submit | 79f790523d2f | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-8051 | solr-8051-submit | e50a2aa3437c | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed; SOLR-15319 lands after this branch |
| SOLR-8088 | solr-8088-submit | 567efa9b78c3 | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed |
| SOLR-8240 | solr-8240-submit | fad7a1dd8e23 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-8767 | solr-8767-submit | 3b5f2d235732 | GREEN 2026-10-03 | owed | REVIEW OWED | draft owed |
| SOLR-8939 | solr-8939-submit | a855a2d8965c | GREEN 2026-10-06 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-8954 | solr-8954-submit | 1d981abe7000 | GREEN 2026-10-05 | owed | REVIEW OWED | draft owed |
| SOLR-9124 | solr-9124-submit | 17a279d4dce1 | GREEN undated | owed | REVIEW OWED | draft owed |
| SOLR-9148 | solr-9148-submit | 30f0d7a42d50 | GREEN 2026-10-06 | SATISFIED (W1 verdicts) | READY TO OPEN | changelog fragment type confirmation stands for the opening pass (W1 advisory) |
| SOLR-9396 | solr-9396-submit | a5ab2eda4e6a | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-9595 | solr-9595-submit | bf99cd0e89c2 | GREEN undated | owed | REVIEW OWED | draft owed |
| SOLR-9864 | solr-9864-submit | b5826e466b9a | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-10305 | solr-10305-submit | c43de86d4c13 | GREEN undated | owed | REVIEW OWED | draft owed |
| SOLR-10424 | solr-10424-submit | e629ab8bb293 | GREEN 2026-10-08 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-10492 | solr-10492-submit | ecf21e2192cb | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-10694 | solr-10694-submit | 64e86811548b | GREEN 2026-10-09 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-10844 | solr-10844-submit | e87515c56d04 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-11129 | solr-11129-submit | dc82676b8096 | GREEN 2026-10-12 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-11153 | solr-11153-submit | 093df0d65bdc | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-11310 | solr-11310-submit | e38ddec5279c | GREEN 2026-10-03 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-11364 | solr-11364-submit | 562d3e7e685a | GREEN 2026-10-08 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-11470 | solr-11470-submit | f44c294da374 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-12044 | solr-12044-submit | c92bbf9349b1 | GREEN 2026-10-09 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-12543 | solr-12543-submit | 88d236db6b7d | GREEN 2026-10-08 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-12556 | solr-12556-submit | 033ec65a0e1b | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-13245 | solr-13245-submit | 16e62ab65425 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-13851 | solr-13851-submit | b60f4d642d1e | GREEN 2026-10-09 | owed | HOLD | submission-held |
| SOLR-13876 | solr-13876-submit | 2e110473dbad | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-14381 | solr-14381-submit | a28b3f672cb4 | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-14451 | solr-14451-submit | e60891d8716d | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-14678 | solr-14678-submit | 5d94e6cf3981 | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-14931 | solr-14931-submit | 1a7d678d9a15 | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-15018 | solr-15018-submit | d0f29b4630c0 | GREEN 2026-10-03 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-15041 | solr-15041-submit | 55fca0a7c243 | GREEN 2026-10-03 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-15144 | solr-15144-submit | 68b0fc31e056 | GREEN 2026-10-03 | owed | HOLD | decision-prep recommendation: hold for a gated distributed run |
| SOLR-15319 | solr-15319-submit | 4bda91f46fc1 | GREEN 2026-10-03 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-15331 | solr-15331-submit | 6769b4cd8a12 | GREEN 2026-10-03 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-15479 | solr-15479-submit | 57bd53ce4d07 | GREEN 2026-10-03 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-15895 | solr-15895-submit | 02930909397a | GREEN 2026-10-03 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-16155 | solr-16155-submit | 0881de1ed68b | GREEN 2026-10-04 | owed | REVIEW OWED | draft owed |
| SOLR-16290 | solr-16290-submit | ff760120c817 | GREEN 2026-10-05 | owed | OWNER ITEM | pin disposition: pin PR, fund the real facet fix, or bank it |
| SOLR-16444 | solr-16444-submit | 8ffee94a5e75 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-17051 | solr-17051-submit | 148544e9ed54 | GREEN 2026-10-04 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in the W1 lists; the receipt stale sentence is a main-side record correction |
| SOLR-17055 | solr-17055-submit | c2396f425fa2 | GREEN undated | owed | REVIEW OWED | draft owed |
| SOLR-17155 | solr-17155-submit | 1413237f7a75 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-17372 | solr-17372-submit | 048862fda8ab | FAILED at the live tip (receipt, 2026-10-12); premise not supported | owed | OWNER ITEM | retire word owed (retire round: premise not supported) |
| SOLR-17748 | solr-17748-submit | dfacaf347668 | GREEN 2026-10-04 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-17791 | solr-17791-submit | a39c1c973766 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-17841 | solr-17841-submit | 478731e67602 | GREEN 2026-10-04 | owed | REVIEW OWED | draft owed |
| SOLR-17976 | solr-17976-submit | 56ea43c448ed | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-18109 | solr-18109-submit | b19395e1f60a | GREEN 2026-10-04 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-18541 | solr-18541-combined-query-merge-totals | fcd92f23ba65 | GREEN 2026-10-12 | written | HOLD | variant branch superseded by the open PR branch combinedquerycomponent-merge-totals |

## Query parsing (22)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-874 | solr-874-submit | ac9ab3375375 | GREEN 2026-10-06 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-4824 | solr-4824-submit | 76c777e4661e | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-6014 | solr-6014-submit | 505849d2d4e7 | GREEN 2026-10-07 | written | GATE OWED | the search-time premise job is claimed by vm2 (claims/pool-vm2-gate-backlog-6014.md); an in-branch fix for the cache, cost and fq cases also needs a branch commit and a gate (verdicts landing report) |
| SOLR-8977 | solr-8977-submit | 395b24964fd0 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-9048 | solr-9048-submit | b145018563cb | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-9149 | solr-9149-submit | 151dfed119ee | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-10897 | solr-10897-submit | d9240d0750fa | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-11391 | solr-11391-submit | b0f15a22856a | GREEN 2026-10-09 | SATISFIED (W1 verdicts) | HOLD | D4 confirmation open with the main side; draft wording steps also owed |
| SOLR-11761 | solr-11761-submit | 41893ee9ce61 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-12212 | solr-12212-submit | 876953fdc927 | GREEN 2026-10-05 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-12532 | solr-12532-submit | 69c06da44679 | GREEN 2026-10-04 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-12608 | solr-12608-submit | d1dd8a1f9f0a | GREEN 2026-10-05 | written | GATE OWED | the changelog title (finding G2) needs a branch commit and a gate; the patch-loop draft edits are recorded in the verdicts landing report; job filed in this census |
| SOLR-12871 | solr-12871-submit | c79a49320cdb | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-13838 | solr-13838-submit | 4520c25abe28 | GREEN undated | owed | REVIEW OWED | draft owed |
| SOLR-13903 | solr-13903-submit | 73ef411dd117 | GREEN undated | owed | REVIEW OWED | draft owed |
| SOLR-15615 | solr-15615-submit | be77267bf554 | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | REVIEW OWED | draft wording steps in reports/category-ready-lists-round-1.md |
| SOLR-15906 | solr-15906-submit | 50139a8e979c | GREEN 2026-10-07 | owed | REVIEW OWED | draft owed |
| SOLR-16267 | solr-16267-submit | 8f4b0c6d2fb0 | GREEN 2026-10-04 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-16570 | solr-16570-submit | 9d3466101a29 | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed |
| SOLR-17280 | solr-17280-submit | 40817c5cb7ec | GREEN 2026-10-07 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-17311 | solr-17311-submit | 9f7524582f9d | GREEN 2026-10-04 | SATISFIED (W1 verdicts) | READY TO OPEN | W1 ready list |
| SOLR-17796 | solr-17796-submit | 661165d2673d | GREEN 2026-10-04 | written | GATE OWED | the negated-premise job is claimed by vm2 (claims/pool-vm2-gate-backlog-17796.md); the negated-collapse fix, the title and the grammar regeneration also need code and a gate, and SOLR-12212 changes the same QueryParser.jj production (landing order) |

## SolrCloud (25)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-3865 | solr-3865-submit | f483bc4c32e0 | GREEN 2026-10-11 | owed | REVIEW OWED | draft owed |
| SOLR-4754 | solr-4754-submit | e4ea4bfde80e | GREEN 2026-10-11 | owed | REVIEW OWED | draft owed |
| SOLR-5813 | solr-5813-submit | 90b8baa08aef | GREEN 2026-10-13 | written | REVIEW OWED | batch A reviewed; base dated runs done as expected; draft confirmation owed |
| SOLR-9155 | solr-9155-submit | 29bf37382899 | GREEN 2026-10-13 | written | READY TO OPEN | slate round 2: ready for owner approval |
| SOLR-10234 | solr-10234-submit | eb8c4060d943 | GREEN 2026-10-12 | owed | REVIEW OWED | draft owed |
| SOLR-10641 | solr-10641-submit | fbc1f47b2808 | GREEN 2026-10-11 | owed | REVIEW OWED | draft owed |
| SOLR-11288 | solr-11288-submit | cbf0c678f277 | GREEN 2026-10-13 | written | REVIEW OWED | base run done and the premise holds; the title re-gate landed at this head; draft confirmation owed |
| SOLR-11479 | solr-11479-submit | 6f739cfbbd38 | GREEN at the live tip (carry-over; tree identical to the gated head 360dca60fcfade) | written | HOLD | opens only after SOLR-15035 settles |
| SOLR-12651 | solr-12651-submit | de3f3604f0ee | GREEN 2026-10-13 | CONVERGED (SolrCloud rounds 2 and 3) | READY TO OPEN | the restore-cleanup question from Tomas Lobbe stands as the draft Choice |
| SOLR-12991 | solr-12991-submit | 1a86966179e1 | GREEN 2026-10-13 | written | REVIEW OWED | batch A reviewed; base dated runs done as expected; draft confirmation owed |
| SOLR-13186 | solr-13186-submit | b436d90d2a88 | GREEN 2026-10-06 | written | REVIEW OWED | batch A reviewed; base dated runs done as expected; draft confirmation owed |
| SOLR-13239 | solr-13239-submit | 699a1fce368c | GREEN 2026-10-02 | owed | HOLD | submission-held |
| SOLR-13369 | solr-13369-submit | dfa0db5bdf96 | GREEN 2026-10-13 | written | REVIEW OWED | dated-run record at this head; draft confirmation owed |
| SOLR-14919 | solr-14919-submit | 483b3885f643 | STALE TIP (green at 8f1f4ef91e79, 2026-10-13) | written | GATE OWED | precision re-gate; job already OPEN for vm2 (gates/SOLR-14919-precision-round-1-gate.md); owner item (c) follows the gate |
| SOLR-15035 | solr-15035-submit | 12d7491e82cd | GREEN at the live tip (hardened gate; premise runs 2026-10-13) | written | HOLD | owner settlement line from HOLD to READY still owed; changelog parse job filed in this census (gates/SOLR-15035-changelog-parse.md) |
| SOLR-15106 | solr-15106-submit | 40b7e5d0efa7 | GREEN 2026-10-13 | written | OWNER ITEM | owner checks: the 1200-message claim and the draft length |
| SOLR-15386 | solr-15386-submit | ca8cb61ee957 | GREEN 2026-10-13 | written | OWNER ITEM | owner checks: the single-check Choice confirmation and the draft length |
| SOLR-15674 | solr-15674-submit | 75ddf9ba518a | GREEN 2026-10-13 | written | REVIEW OWED | history scrub gated green at this head; draft confirmation owed; SOLR-15478 lands after it |
| SOLR-15863 | solr-15863-submit | 78dff1d52f6c | GREEN 2026-10-13 | written | REVIEW OWED | base run done (compile failure as expected); draft confirmation owed |
| SOLR-16013 | solr-16013-submit | 37b01337eb4c | GREEN 2026-10-13 | written | REVIEW OWED | draft cites an earlier head; refresh to the gated fixture head 37b01337eb |
| SOLR-16437 | solr-16437-submit | a23f770e4b95 | GREEN 2026-10-13 | CONVERGED (SolrCloud r3) | READY TO OPEN |  |
| SOLR-17281 | solr-17281-submit | ba21ca32791b | NO GATE (recorded) | owed | HOLD | parked, under review on the owner side; the receipt says do not draft it |
| SOLR-17292 | solr-17292-submit | f614a42fbc80 | GREEN 2026-10-13 | written | REVIEW OWED | a duplicate receipt needs reconciling; the dated run of record stands (4 of 4 at this head) |
| SOLR-17680 | solr-17680-submit | 52b641ca1a26 | GREEN 2026-10-13 | CONVERGED (SolrCloud r3) | READY TO OPEN | slate round 2: ready for owner approval |
| SOLR-17733 | solr-17733-submit | cf1b28327264 | STALE TIP (green at 636196b7954a, 2026-10-04) | written | GATE OWED | title-narrowing re-gate; job already OPEN for vm2 (gates/SOLR-17733-title-narrowing-round-1-gate.md) |

## Core admin (28)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-4502 | solr-4502-submit | 3695620b22ab | GREEN 2026-10-12 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-4989 | solr-4989-submit | 5bac95376cd4 | GREEN 2026-10-06 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-5011 | solr-5011-submit | db801dbd3bdb | GREEN 2026-10-13 | CONVERGED (Core admin r1/r2) | HOLD | shared-schema analysis returned UNSAFE; draft stays on hold |
| SOLR-5262 | solr-5262-submit | 616efd008b05 | GREEN 2026-10-11 | owed | REVIEW OWED | draft owed |
| SOLR-6438 | solr-6438-submit | 8c77988d5917 | GREEN 2026-10-05 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-8275 | solr-8275-submit | e52e10fa50a3 | GREEN 2026-10-06 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-8554 | solr-8554-submit | 654c1a7c3570 | GREEN 2026-10-11 | owed | REVIEW OWED | draft owed |
| SOLR-8576 | solr-8576-submit | 883bb522caf9 | GREEN 2026-10-12 | written | REVIEW OWED | draft refresh after the landed alias fix (gate green at this head) |
| SOLR-8628 | solr-8628-submit | 5b606dc779d0 | GREEN 2026-10-11 | owed | REVIEW OWED | draft owed |
| SOLR-9750 | solr-9750-submit | f97da6da14aa | GREEN 2026-10-05 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-11939 | solr-11939-submit | cdb9903506e9 | GREEN 2026-10-12 (packaging gate job DONE at this head) | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-12007 | solr-12007-submit | bdeba582fd63 | GREEN 2026-10-08 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-12916 | solr-12916-submit | d62ddfc24616 | GREEN 2026-10-13 | written | REVIEW OWED | round 3 carries the draft against the landed retitle head |
| SOLR-13246 | solr-13246-submit | 6817c6c0c267 | GREEN 2026-10-06 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-14098 | solr-14098-submit | dd4995515e88 | GREEN 2026-10-02 | owed | REVIEW OWED | draft owed |
| SOLR-15003 | solr-15003-submit | 1004abee39ab | GREEN 2026-10-08 | owed | REVIEW OWED | draft owed |
| SOLR-15024 | solr-15024-submit | f95b5010b3fe | GREEN 2026-10-03 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-15805 | solr-15805-submit | 6d78603d8257 | GREEN 2026-10-12 at the live tip (tree identical to the gated commit) | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-16108 | solr-16108-submit | 70ad7371b31c | GREEN 2026-10-03 | owed | REVIEW OWED | draft owed |
| SOLR-16499 | solr-16499-submit | 51b7addddd45 | GREEN at the packaged head, which is the live tip | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-16725 | solr-16725-submit | be1838ef8ccf | GREEN 2026-10-07 | CONVERGED (Core admin r1/r2) | GATE OWED | two recorded branch edits owed, then a top-up gate at the moved head |
| SOLR-16849 | solr-16849-submit | d612b055da20 | GREEN 2026-10-04 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-16887 | solr-16887-submit | 42675f65d6fc | GREEN 2026-10-07 | owed | REVIEW OWED | draft owed |
| SOLR-17297 | solr-17297-submit | c0ab38fc0a8b | GREEN 2026-10-04 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-17377 | solr-17377-submit | 22b5f209a11a | GREEN 2026-10-05 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-17708 | solr-17708-submit | 2eceb5473037 | GREEN 2026-10-11 | CONVERGED (Core admin r1/r2) | READY TO OPEN |  |
| SOLR-17731 | solr-17731-submit | 18165d35a7e3 | GREEN 2026-10-12 | CONVERGED (Core admin r1/r2) | GATE OWED | head, counts and links update rides the posted job (gates/SOLR-17731.md, OPEN) |
| SOLR-18278 | solr-18278-submit | bf19c9fa4498 | GREEN 2026-10-04 | owed | REVIEW OWED | draft owed |

## SolrJ (20)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-2018 | solr-2018-submit | 213457aa91fd | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-3498 | solr-3498-submit | 2ca0b3743427 | GREEN 2026-10-11 | owed | REVIEW OWED | packaged head pushed by vm2 on 2026-10-13; draft owed |
| SOLR-3722 | solr-3722-submit | f9d3dda1d3de | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-3999 | solr-3999-submit | e3af6f36e312 | GREEN 2026-10-10 | CONVERGED (SolrJ r2) | READY TO OPEN |  |
| SOLR-4335 | solr-4335-submit | 8f6e267d6482 | GREEN 2026-10-10 | CONVERGED (SolrJ r2) | READY TO OPEN |  |
| SOLR-4336 | solr-4336-submit | 798618aa08fd | GREEN 2026-10-10 | CONVERGED (SolrJ r2) | READY TO OPEN |  |
| SOLR-4422 | solr-4422-submit | 3e5b7afecddb | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-4424 | solr-4424-submit | 2e947b7f622c | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-5220 | solr-5220-submit | e415c409044c | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-6046 | solr-6046-submit | 3b833e369a0b | GREEN 2026-10-10 | CONVERGED (SolrJ r2) | READY TO OPEN |  |
| SOLR-7709 | solr-7709-submit | 501078f22203 | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-8536 | solr-8536-submit | 28552ddc26eb | GREEN 2026-10-10 | CONVERGED (SolrJ r2) | READY TO OPEN |  |
| SOLR-10364 | solr-10364-submit | efb1e6717f9f | GREEN 2026-10-11 | CONVERGED (SolrJ r2) | READY TO OPEN |  |
| SOLR-11356 | solr-11356-submit | 6120dae28d04 | GREEN 2026-10-11 | written | GATE OWED | header-copy patch gate owed; job already posted (gates/SOLR-11356-sj-r1-49-header-copy.md) |
| SOLR-12094 | solr-12094-submit | 8d957a73f4fb | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-14187 | solr-14187-submit | 29023ec1ecab | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-14298 | solr-14298-submit | 67e75ea1eea4 | GREEN 2026-10-10 | CONVERGED (SolrJ r2) | READY TO OPEN |  |
| SOLR-14967 | solr-14967-submit | ee76643f5b3f | GREEN 2026-10-10 | written | REVIEW OWED | draft written; no convergence verdict on record |
| SOLR-17866 | solr-17866-submit | 3f9367d796a6 | GREEN 2026-10-10 | written | OWNER ITEM | submit ahead of the design conversation: owner flag stands |
| SOLR-18341 | solr-18341-submit | 458b098719d7 | GREEN 2026-10-10 | CONVERGED (SolrJ r2) | READY TO OPEN |  |

## eDisMax (13)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-2309 | solr-2309-submit | 06f5a1c4a87e | GREEN 2026-10-07 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-2988 | solr-2988-submit | d2d144dfd9f4 | GREEN 2026-10-07 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-3243 | solr-3243-submit | 1db99c13662d | GREEN 2026-10-07 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-3729 | solr-3729-submit | 0fe7e503945c | GREEN 2026-10-08 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-3923 | solr-3923-submit | 723f61ee4ab7 | GREEN 2026-10-09 | owed | REVIEW OWED | no draft yet; a premise run is also owed; the eDisMax final pass excludes it, so it rides the census follow-up assignment |
| SOLR-3962 | solr-3962-submit | e7d5f3505035 | GREEN 2026-10-07 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-4362 | solr-4362-submit | e96a057439c8 | GREEN 2026-10-07 | written | HOLD | held on a branch defect (draft fidelity round); the eDisMax final pass excludes it |
| SOLR-6009 | solr-6009-submit | a41bb034a1f4 | GREEN 2026-10-07 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-6320 | solr-6320-submit | cb710c963531 | GREEN 2026-10-08 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-7120 | solr-7120-submit | 4664014a919c | GREEN 2026-10-09 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-12092 | solr-12092-submit | ca9573dabd38 | GREEN 2026-10-07 | written | REVIEW OWED | eDisMax final pass in flight (assignments/pool-edismax-final-review-round-1.md) |
| SOLR-14638 | solr-14638-submit | 44cf1c8fc805 | NO GATE (recorded) | owed | HOLD | parked |
| SOLR-14913 | solr-14913-submit | b80221f46d3c | GREEN (gated tree; the tip adds a comment-only commit and the evidence carries) | written | REVIEW OWED | eDisMax final pass is in flight (assignments/pool-edismax-final-review-round-1.md) |

## Schema (11)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-9349 | solr-9349-submit | 1e79bb42123c | GREEN 2026-10-05 | CONVERGED (Schema r1) | READY TO OPEN |  |
| SOLR-10131 | solr-10131-submit | fd495167cd09 | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed |
| SOLR-10403 | solr-10403-submit | d8e03755cb5d | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed |
| SOLR-14199 | solr-14199-submit | 2c549d9fb384 | GREEN 2026-10-12 | CONVERGED (landing re-check settlement accepted) | READY TO OPEN | record job settled; no holds stand |
| SOLR-15357 | solr-15357-submit | e9e7cc3a0c1c | GREEN 2026-10-03 | CONVERGED (Schema r1/r2) | READY TO OPEN |  |
| SOLR-15358 | solr-15358-submit | cbb345f2e7df | GREEN 2026-10-03 | written | HOLD | owner call on default docValues-as-stored; lands after SOLR-15357 |
| SOLR-15712 | solr-15712-submit | cb988a2ff4c3 | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed |
| SOLR-15945 | solr-15945-submit | 793765c92764 | GREEN 2026-10-03 | CONVERGED (Schema r1) | READY TO OPEN |  |
| SOLR-16977 | solr-16977-submit | c65d242718e7 | GREEN 2026-10-12 | CONVERGED (Schema r1) | READY TO OPEN |  |
| SOLR-17047 | solr-17047-submit | 79fc289bfc1a | GREEN 2026-10-12 | CONVERGED (Schema r1) | READY TO OPEN |  |
| SOLR-18134 | solr-18134-submit | 78134dde678d | GREEN 2026-10-12 | CONVERGED (Schema r1) | READY TO OPEN |  |

## Replication and backup (11)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-5589 | solr-5589-submit | ddcd75020eea | GREEN 2026-10-12 | owed | REVIEW OWED | draft owed |
| SOLR-6711 | solr-6711-submit | 73c5e6e0d243 | GREEN 2026-10-12 | CONVERGED (Replication r1) | READY TO OPEN | landing order in its chain: after SOLR-12246, SOLR-12085 and SOLR-11650 |
| SOLR-8430 | solr-8430-submit | 0d64c35c5a7d | GREEN 2026-10-12 | CONVERGED (Replication r3) | READY TO OPEN |  |
| SOLR-9091 | solr-9091-submit | a596a39d2e1e | GREEN 2026-10-13 | written | REVIEW OWED | confirm the draft against the landed retitle head; main-side packaging steps at opening (W4 list) |
| SOLR-9382 | solr-9382-submit | 2952bae9d392 | GREEN 2026-10-10 at the packaged head, which is the live tip | CONVERGED (Replication r2) | READY TO OPEN | packaging state is on the fork; the main side recreates it at opening if a later record requires (W4 list) |
| SOLR-9598 | solr-9598-submit | c8407773f76c | GREEN 2026-10-05 | CONVERGED (Replication r2 (text)) | HOLD | owner hold stands; wait-by-default adopted and carried in the draft |
| SOLR-9865 | solr-9865-submit | 4937608bb181 | GREEN 2026-10-11 | CONVERGED (Replication r2 (text)) | HOLD | lands after SOLR-18532 merges; packaging drops the overlapping rollback hunk |
| SOLR-11650 | solr-11650-submit | e4f5e941cd8e | GREEN 2026-10-07 | CONVERGED (Replication r2 (text)) | HOLD | posting hold stands; owed title, javadoc and test-comment commits have not landed |
| SOLR-12085 | solr-12085-submit | a220b0728dc8 | GREEN 2026-10-13 | CONVERGED (Replication r2) | READY TO OPEN |  |
| SOLR-12246 | solr-12246-submit | 3ffc2539ec76 | GREEN undated | CONVERGED (Replication r2) | READY TO OPEN |  |
| SOLR-17287 | solr-17287-submit | 6957daf82610 | GREEN 2026-10-11 | CONVERGED (Replication r1) | READY TO OPEN |  |

## Streaming (10)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-9852 | solr-9852-submit | 5fe174425514 | GREEN 2026-10-10 at the packaged head, which is the live tip | CONVERGED (Streaming r1) | READY TO OPEN |  |
| SOLR-10322 | solr-10322-submit | 80ce9d7a3c8a | GREEN 2026-10-13 | CONVERGED (Streaming r1) | READY TO OPEN |  |
| SOLR-10882 | solr-10882-submit | b12b0ffab487 | GREEN 2026-10-13 | CONVERGED (Streaming r2) | READY TO OPEN |  |
| SOLR-11922 | solr-11922-submit | da29a963236f | NO GATE by finding (premise run done; the premise does not hold on current main) | owed | HOLD | test-only branch whose test passes on base production; retire-round material, not an opening |
| SOLR-12505 | solr-12505-submit | 1a1875d95f36 | GREEN 2026-10-13 | written | OWNER ITEM | Choice, Unicode widening and Jira attributions are owner items; the changelog title gate waits on the Choice |
| SOLR-12657 | solr-12657-submit | 23cc27a98925 | GREEN 2026-10-10 | CONVERGED (Streaming r1) | READY TO OPEN |  |
| SOLR-14200 | solr-14200-submit | 10991af9bdac | GREEN 2026-10-10 | owed | REVIEW OWED | draft owed |
| SOLR-14231 | solr-14231-submit | 593463f398cb | GREEN 2026-10-10 | written | GATE OWED | fail-before leg owed; job already OPEN (gates/SOLR-14231-fail-before.md) |
| SOLR-15326 | solr-15326-submit | dab6a4663169 | GREEN 2026-10-10 | owed | REVIEW OWED | draft owed |
| SOLR-17433 | solr-17433-17143-submit | 42b6c4fc9158 | GREEN 2026-10-10 | owed | REVIEW OWED | draft owed |

## CLI (12)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-7924 | solr-7924-submit | 96ef3a52bdbf | GREEN 2026-10-10 | owed | REVIEW OWED | draft owed |
| SOLR-9342 | solr-9342-submit | 833e11192a7f | VERIFIED at the live tip (BATS evidence shape) | CONVERGED (CLI r2) | GATE OWED | changelog title correction claimed by vm2 on 2026-10-13; the landed head gets its parse check in that pass |
| SOLR-10390 | solr-10390-submit | 5a86f908052f | GREEN 2026-10-11 | owed | REVIEW OWED | draft owed |
| SOLR-10667 | solr-10667-submit | 9ce9ef1db5e9 | GREEN 2026-10-13 | CONVERGED (CLI r2) | READY TO OPEN |  |
| SOLR-12347 | solr-12347-submit | 56d38389c75a | GREEN 2026-10-10 | CONVERGED (CLI r1) | READY TO OPEN |  |
| SOLR-16272 | solr-16272-submit | fbcd65f71652 | GREEN at the live tip (title gate recorded 2026-10-13) | CONVERGED (CLI r2) | READY TO OPEN | the branch moved to the corrected-title head while this census was being built; the row follows the gate record that landed with it |
| SOLR-16813 | solr-16813-submit | 1b288170e8aa | GREEN 2026-10-07 | CONVERGED (CLI r1) | READY TO OPEN |  |
| SOLR-17029 | solr-17029-submit | 4a98ef0a89d1 | VERIFIED at the live tip (BATS evidence shape; re-verified 2026-10-08) | CONVERGED (CLI r1) | READY TO OPEN |  |
| SOLR-17598 | solr-17598-submit | 8c91cf047a96 | NO GATE APPLIES (recorded) | CONVERGED (CLI round 1) | READY TO OPEN | no gate applies (solr.cmd-only change); the confirming Windows run stays deferred under the owner Windows rule and the draft states that limit |
| SOLR-18132 | solr-18132-submit | 54835cac6f85 | GREEN 2026-10-05 | CONVERGED (CLI r1) | READY TO OPEN |  |
| SOLR-18339 | solr-18339-submit | 47e53884609c | GREEN 2026-10-04 (BATS and packaging check; verified at the same head 2026-10-07) | CONVERGED (CLI r1) | OWNER ITEM | opening timing is the owner call; secured-node Choice posed in the draft |
| SOLR-18339 | solr-18339-ready | dc25ea78f88a | no gate record at this head (the gated head 47e53884609c is on solr-18339-submit) | CONVERGED (CLI r1) | HOLD | older variant of solr-18339-submit; one branch per ticket goes forward at opening |

## Build and docs (13)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-3684 | solr-3684-submit | 663b8ce754b6 | GREEN 2026-10-03 | written | REVIEW OWED | build-docs round 1 response records an owner-decision HOLD standing on this draft; confirm or discharge it in this round |
| SOLR-5821 | solr-5821-submit | 327547d693e0 | GREEN 2026-10-12 (docs build at the wording-refresh head, which is the live tip) | CONVERGED (Build/docs r2) | READY TO OPEN |  |
| SOLR-6430 | solr-6430-submit | 78cddfb520d6 | GREEN 2026-10-13 (docs-shaped gate at the live tip, round 2 record) | CONVERGED (Build/docs r2) | READY TO OPEN |  |
| SOLR-7119 | solr-7119-submit | 4c652c93b99c | GREEN (docs-shaped gate at the packaged head, which is the live tip) | owed | REVIEW OWED | draft and gate both short; review round first |
| SOLR-9039 | solr-9039-submit | 13faf68fe858 | NO GATE (recorded); premise unverified | owed | REVIEW OWED | fresh-arrival registration only; premise check and draft both owed |
| SOLR-11700 | solr-11700-submit | 8a945757e668 | GREEN (docs-only gate at the packaged live tip) | owed | REVIEW OWED | draft and gate both short; review round first |
| SOLR-13705 | solr-13705-submit | 73504e7a8dcb | GREEN 2026-10-11 | CONVERGED (Security r1) | READY TO OPEN |  |
| SOLR-16322 | solr-16322-submit | 68dd5e2b154e | GREEN at the final packaged head, which is the live tip | written | HOLD | owner decision 17 stays open; the draft carries the HOLD line (build-docs round 1 response) |
| SOLR-16914 | solr-16914-submit | cd878023d3d0 | VERIFIED at the live tip 2026-10-08 (docs light gate) | CONVERGED (Build/docs r1) | READY TO OPEN |  |
| SOLR-17252 | solr-17252-submit | d730a266a042 | VERIFIED at the live tip (script harness, 2026-10-07) | written | READY TO OPEN | the draft poses its Choices; owner decisions 1 to 3 are recorded in it |
| SOLR-17356 | solr-17356-submit | 9fe2801de07c | GREEN 2026-10-11 (docs-only gate at the packaged live tip) | owed | REVIEW OWED | draft and gate both short; review round first |
| SOLR-17752 | solr-17752-submit | e5c0a64f993c | GREEN 2026-10-04 | written | OWNER ITEM | branch base decision: open against 56ec140e3636 or rebase onto 14c7aac0d151 first |
| SOLR-17842 | solr-17842-submit | 008973f63133 | VERIFIED at the live tip 2026-10-08 (docs light gate) | CONVERGED (Build/docs r1) | GATE OWED | the placeholder fix is branch work owed before opening (job OPEN: gates/SOLR-17842-placeholder-fix.md); the published-results question is a named follow-up, not a hold (owner call discharged 2026-10-13) |

## Configsets (5)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-6960 | solr-6960-submit | 9bef536fc12a | GREEN 2026-10-09 (top-up at the live tip) | CONVERGED (Configsets r1) | READY TO OPEN | owner items stand for the slate, not the draft: changelog title fix and a second test run (configsets round 1 response) |
| SOLR-7267 | solr-7267-submit | 59f34a339b0d | GREEN (gated tree; the tip adds a docs-only commit and the evidence carries) | CONVERGED (Configsets r1) | READY TO OPEN | one Choice posed in the draft |
| SOLR-7323 | solr-7323-submit | b832dc0048c0 | GREEN 2026-10-09 | owed | REVIEW OWED | draft owed |
| SOLR-15478 | solr-15478-submit | 0478bdf0ac5c | GREEN 2026-10-03 | CONVERGED (Configsets r1) | READY TO OPEN | landing order: after SOLR-15674 |
| SOLR-17363 | solr-17363-submit | b8e8e1c48461 | GREEN 2026-10-05 | CONVERGED (Configsets r1) | READY TO OPEN |  |

## Security (4)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-10627 | solr-10627-submit | 5a15dc0ba220 | GREEN 2026-10-05 | written | HOLD | changelog title landing and parse owed; job already posted (gates/SOLR-10627-sec-r1-02-title.md) |
| SOLR-11678 | solr-11678-submit | 2c68a83c64ee | GREEN 2026-10-13 | written | HOLD | keystore-type reading owed on vm2; job already posted (gates/SOLR-11678-sec-r1-07-keystore-reading.md) |
| SOLR-12161 | solr-12161-submit | cdd265168a2a | GREEN at the live tip (security round 2 record: the packaged head is gated; the receipt first section names the superseded head) | CONVERGED (Security r2) | READY TO OPEN |  |
| SOLR-18368 | solr-18368-submit | a7ec9a1b65c0 | GREEN 2026-10-10 | written | OWNER ITEM | key choice at opening (round 2 flags banner option (a) for confirmation) |

## Spellcheck (9)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-1877 | solr-1877-submit | 5533ba27b17e | GREEN 2026-10-12 | owed | REVIEW OWED | draft owed |
| SOLR-3701 | solr-3701-submit | aabd678dec73 | GREEN 2026-10-06 | written | REVIEW OWED | draft and gate both short; review round first |
| SOLR-4366 | solr-4366-submit | 558864454f7f | GREEN 2026-10-09 | owed | REVIEW OWED | draft and gate both short; review round first |
| SOLR-4367 | solr-4367-submit | 0af6087f43fa | GREEN 2026-10-06 | written | REVIEW OWED | draft and gate both short; review round first |
| SOLR-4399 | solr-4399-submit | 82c0c11373d0 | GREEN 2026-10-11 (re-gate at the build-time guard head, which is the live tip) | owed | REVIEW OWED | draft and gate both short; review round first |
| SOLR-9060 | solr-9060-submit | 704ca28bf79d | GREEN 2026-10-05 | owed | REVIEW OWED | draft and gate both short; review round first |
| SOLR-10252 | solr-10252-submit | a2be0f3adfe6 | NO GATE APPLIES (docs and config only) | owed | OWNER ITEM | round 5 owner decision: comment-only versus a field change; two wording corrections ride it |
| SOLR-10789 | solr-10789-submit | d9077048d74d | GREEN 2026-10-09 | owed | REVIEW OWED | draft and gate both short; review round first |
| SOLR-17612 | solr-17612-submit | 3a456c27dda5 | GREEN at the live tip (spellcheck round 5 record); baseline receipt at dd2708fbb7e5 | written | REVIEW OWED | draft and gate both short; review round first |

## Suggester (8)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-9227 | solr-9227-submit | 3c23fc5cfa6c | GREEN 2026-10-06 | written | REVIEW OWED | the draft carries the named Choices and Limits from its ledger record; a confirmation round is owed |
| SOLR-9637 | solr-9637-submit | c50fa4ffd932 | GREEN 2026-10-05 | written | OWNER ITEM | submission shape: submit SOLR-17393 first, fold both into one PR, or rework onto plain main |
| SOLR-9968 | solr-9968-submit | c31d2ff1a3fd | STALE TIP (ledger green at b1865b605184, 2026-10-06; the tip has moved and what moved is not recorded) | written | REVIEW OWED | draft confirmation also owed |
| SOLR-10937 | solr-10937-submit | 7d0cd11bafdc | NO RECORD (no gate on record) | written | OWNER ITEM | owner call: docs-only or code for the tmpdir lookups (suggester round 4); the branch tip has also moved since that record |
| SOLR-11844 | solr-11844-submit | cbd08c20e9a4 | NO RECORD (no gate on record) | written | OWNER ITEM | owner question: whether a ref-guide note is an acceptable resolution (suggester round 4); the premise check against the Lucene formula is still owed |
| SOLR-14171 | solr-14171-submit | faa262eedc55 | GREEN 2026-10-09 | written | REVIEW OWED | draft confirmation owed (suggester round 4: draft with named Limits) |
| SOLR-17215 | solr-17215-submit | 04d35df186dd | GREEN 2026-10-09 | written | REVIEW OWED | suggester round 4 lists it held with no review on record; the gate is green at the tip, so the draft review is the remaining step |
| SOLR-17393 | solr-17393-submit | 626241e647e5 | GREEN 2026-10-09 | written | REVIEW OWED | merge-only scope ratified 2026-10-09 with the per-shard top-count cut as the named follow-up; suggester round 4 lists it held, so the draft review reconciles the two records |

## Highlighting (2)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-2632 | solr-2632-submit | 1d7018f3fd7b | NO GATE (recorded) | written | HOLD | test-only pin; premise spurious on main; no gate by design |
| SOLR-16885 | solr-16885-submit | 2b9b80119a92 | NO GATE (recorded); the premise did not reproduce | owed | HOLD | premise did not reproduce; Jira comment route, not a PR |

## Metrics (1)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-17987 | solr-17987-submit | 38abf6423126 | GREEN 2026-10-10 | written | REVIEW OWED | draft confirmation owed; the branch-side registry rename is packaging work |

## Admin UI (2)

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-9818 | solr-9818-submit | 63f2d7ce9267 | NO GATE (recorded) | owed | HOLD | premise holds in part; premise run deferred (Windows) |
| SOLR-9831 | solr-9831-submit | f269a70e84f0 | NO GATE (recorded) | owed | HOLD | premise holds; premise run deferred (Windows) |

## Update processing (1)

Every update-processing branch except the one variant row below is already an open PR (the 28 update PRs) or merged (SOLR-4841), so the category has no other census rows.

| Ticket | Branch | Live tip | Gate at tip | Draft | Disposition | Note |
|---|---|---|---|---|---|---|
| SOLR-16130 | solr-16130-export-join | 632e0340b0a8 | STALE TIP (green at 3c48dec4b79f, 2026-10-10) | owed | HOLD | variant branch superseded by the open PR branch solr-16130-test-followup |

## Cross-check against the fork

`git ls-remote` on the fork returns 730 refs. They divide as follows:

| Ref family | Count | Treatment |
|---|---|---|
| `ci/*` scratch branches (verification checkouts, not prepared PRs) | 374 | outside the prepared universe |
| `assignment-*` coordination branches | 4 | outside the prepared universe |
| `review-r1-*` review branches | 5 | outside the prepared universe |
| `pr-prepare`, fork `main`, `HEAD`, `code-review`, `gradle-queue` | 5 | coordination and plumbing, outside the prepared universe |
| Prepared branches (`solr-*` plus the three named PR branches) | 342 | the census universe |

The 342 prepared branches divide as: 63 already open as PRs on apache/solr (excluded; counted from the live API list, not from memory), 8 merged (excluded: SOLR-18135, PR 4939; SOLR-8291-13217, PR 4953; SOLR-18277, PR 4959, also retired by owner ruling 2026-10-13; SOLR-10198, PR 4965; SOLR-18196, PR 4995; SOLR-18317, PR 5001; SOLR-13202, PR 5012; SOLR-4841, PR 5070), 6 retired and not merged (excluded: SOLR-17722, SOLR-17882, SOLR-12743, SOLR-17825, SOLR-9759, SOLR-18356, per `reports/retire-packs-round-1.md` and the owner ruling on SOLR-18277), 2 variants of the merged SOLR-18317 work (excluded: `solr-18317-ready`, `solr-18317-server-submit`), and the 263 rows in this census. 63 + 8 + 6 + 2 + 263 = 342. Every prepared branch is accounted for; no branch is left unclassified.

Three census rows are second branches for a ticket whose main branch is an open PR or also in this census; each is marked HOLD with its reason in its row: `solr-18541-combined-query-merge-totals`, `solr-16130-export-join`, `solr-18339-ready`.

Records that conflict, and what the row follows:

- SOLR-9968: the main receipts ledger records a green gate at `b1865b605184` (2026-10-06); the live tip has moved past it and no record says what the move carries. The row says STALE TIP and the suggester follow-up assignment establishes what moved.
- SOLR-17292: two receipt records exist for the same branch; the dated run of record (4 of 4 at the live tip) stands, and the duplicate is named for reconciling in the SolrCloud follow-up assignment.
- SOLR-17612: the receipt file is the baseline gate at `dd2708fbb7e5`; the spellcheck round 5 record gates the live tip. The row follows round 5.
- SOLR-12161: the receipt first section names the earlier packaged head; the security round 2 response records the gate at the live tip. The row follows round 2.
- SOLR-18339: two branches exist. The receipt gates `solr-18339-submit` at its live tip `47e53884609c` (BATS suite and packaging check, 2026-10-04; the round 35 verification on 2026-10-07 made no code change at the same head). The `solr-18339-ready` branch at `dc25ea78f88a` is the older variant and carries no gate record; it is marked HOLD.
- SOLR-4399: the receipt describes its gated head as local to vm2 and not pushed; `ls-remote` now shows that head as the fork tip, so a later push landed it. The row follows `ls-remote`.
- SOLR-10937 and SOLR-11844: no gate record exists anywhere on this branch or in the ledger, and the suggester round 4 record holds both on owner calls. The rows say no gate on record rather than guessing one.
- SOLR-11922: its receipt records the premise run as done and the premise as not holding on current main. The row is HOLD, not a gate or review item.

## Top-up posted from this census

Gate jobs filed under `gates/`, OPEN and unclaimed (the GATE OWED rows that already carry a posted job are named in their rows and were not duplicated):

- `gates/SOLR-12608-title-gate.md`: land the corrected changelog title (finding G2) as a branch commit, then the gate steps at the landed head.
- `gates/SOLR-15035-changelog-parse.md`: the changelog YAML parse at the live tip; the ticket itself stays HOLD on its owner settlement line.
- `gates/SOLR-16725-branch-edits-topup-gate.md`: apply the two recorded branch edits (a raw-request test comment and a ClusterStatus javadoc), then the top-up gate at the moved head.

Review assignments posted under `assignments/`, each directing the patch loop (apply the fixes, re-review until SATISFIED) and batched by category:

- `assignments/pool-census-followup-search-components-wording-round-1.md`: the 28 W1 HOLD drafts (bold summaries, internal wording, changelog title items).
- `assignments/pool-census-followup-search-components-drafts-round-1.md`: the 12 gated search-components branches with no draft.
- `assignments/pool-census-followup-query-parsing-round-1.md`: 3 wording drafts and 4 drafts owed.
- `assignments/pool-census-followup-solrcloud-round-1.md`: 13 draft confirmations and refreshes, including the SOLR-16013 draft refresh and the SOLR-17292 receipt reconcile.
- `assignments/pool-census-followup-core-admin-round-1.md`: 7 drafts owed and 2 draft refreshes.
- `assignments/pool-census-followup-solrj-round-1.md`: 10 draft confirmations, one draft owed (SOLR-3498).
- `assignments/pool-census-followup-spellcheck-round-1.md`: 8 drafts owed or to confirm, from the round 5 record.
- `assignments/pool-census-followup-suggester-round-1.md`: 5 draft confirmations, reconciling the round 4 holds against the ledger gates.
- `assignments/pool-census-followup-streaming-schema-configsets-round-1.md`: drafts owed for SOLR-14200, SOLR-15326, SOLR-17433 (streaming), SOLR-10131, SOLR-10403, SOLR-15712 (schema) and SOLR-7323 (configsets).
- `assignments/pool-census-followup-build-docs-cli-metrics-round-1.md`: drafts owed for SOLR-11700, SOLR-17356, SOLR-7119 (build and docs), SOLR-7924, SOLR-10390 (CLI), the SOLR-3684 hold confirmation, and the SOLR-17987 draft confirmation (metrics).

The eDisMax rows ride the already-posted final pass (`assignments/pool-edismax-final-review-round-1.md`). Its one excluded ticket, SOLR-3923, rides `assignments/pool-census-followup-edismax-leftover-round-1.md`, posted with this census (draft writing, plus filing the premise gate job).

An addendum to `assignments/pool-category-closeout-overnight-round-1.md` directs the W4 morning slates assembly to cover this whole census, not only the close-out batch categories.
