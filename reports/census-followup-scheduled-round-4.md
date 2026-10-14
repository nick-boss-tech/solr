# Census follow-up, scheduled round 4 (six slices)

- Trigger: the two-hourly scheduled check. Recorded tip `81053789b72`; live tip at the check `036878ef342`. Claim: `claims/pool-census-followup-round-4-scheduled.md` (pushed at `eb7d4b87cc8`).
- The new commits before the check were vm2's records: the SOLR-10305 text gate (`f559126972`), the SOLR-9124 title change (`64b8256cde`), the SOLR-15003 proof recount, the SOLR-8954 head and base results, the SOLR-17055 premise run, and claims for the SOLR-17055 fix and the SOLR-11356 header-copy gate. No new assignment was posted.
- Worktree: `wt/pr-prepare-suggester`. Live fork tips checked by `git ls-remote origin` for every ticket. Each draft names the head its receipt or record names.
- Read only for branches: no build, test, Gradle or gate run; no commit or push on any `solr-*-submit` branch; no GitHub write; no receipt, gate file or claim edited by the review side.
- Written by the parent from the six subagent replies. The subagents could not write report `.md` files.

## Result

- 24 tickets. 19 SATISFIED, 5 HOLD. Three gate jobs filed this round, plus the existing SOLR-17055 gate.

## Slice S1: settlements of the new vm2 records

- SOLR-10305: SATISFIED. `pr-drafts/search-components/SOLR-10305.md` at gated head `f559126972` (live tip). vm2's link edits match the receipt and the branch at that head. Owner decision 3 (the standalone 403 stays) is named in Limits. The Proof says the head moved to `f559126972` with text-only changes, and that no test was run there.
- SOLR-9124: SATISFIED. `pr-drafts/search-components/SOLR-9124.md` at head `64b8256cde` (live tip). The title-only change is over `17a279d4dce1`. The new title claims no grouped-versus-ungrouped equality (the test compares control and sharded grouped queries) and names no cache, so the LRU claim is gone. Decision for the owner: owner decision 4 is treated as met by the title change plus the Limits line on proof scope. Nick to confirm.
- SOLR-15003: SATISFIED with named gaps. `pr-drafts/core-admin/SOLR-15003.md` at head `1004abee39ab`. The recount settles the test count (4 of 4; the class has 28 `@Test` methods) and one fail-before (`testFullCopyKeepsSnapshotFilesInOldIndexDir` fails on `b5c71bc5573`). The other three new tests pass on base; they are NOT_PROVEN and stated in Limits. The receipt's pre-fix claim at `8f5b6f8360a` is contradicted by the recount and is not used.
- SOLR-8954: HOLD. The recorded measured run confirms the round 3 reading: head `1d981abe7000` fails `FullSolrCloudDistribCmdsTest#testRTGCompositeRouterWithRouterField` (expected 0, was 1); base `e432df19c4a5` passes. Owner decision 1 (close, or a test-only follow-up with 8009) is open. No draft. The receipt still says GATE GREEN and was not corrected (not the review side's file).

## Slice S2: query parsing and core admin

- SOLR-15906: HOLD. `pr-drafts/query-parsing/SOLR-15906.md` (held). The live head `50139a8e979` matches the receipt. Reading the branch, the subagent found that a sort or field-list spec and a stray `)` now reach the Lucene path, which the subagent reports breaks `TestFunctionQuery`, `SOLR749Test` and `TestNestedUpdateProcessor`. No receipt covers those. Gate job `gates/SOLR-15906-sort-paren-fix.md` (OPEN). The owner sets the timing. This is a reading, not a run; vm2 has to confirm it.
- SOLR-16570: SATISFIED on the text. `pr-drafts/query-parsing/SOLR-16570.md` at gated head `9d3466101a2` (2026-10-09). Owner items still open: (1) which category holds SOLR-16570, since the landing verdicts flag it and there is no ruling on record; (2) the ticket title describes an empty-collection NPE that SOLR-16611 changed on main, while this fix covers a docValues-only trigger.
- SOLR-8576: SATISFIED. `pr-drafts/core-admin/SOLR-8576.md` at head `883bb522ca`. Applied CA-LR-03, CA-LR-05 and CA-LR-06. CA-LR-04 is not applied: the bold claim already holds, since no test at the merge-base carries those messages.
- SOLR-17055: HOLD. `pr-drafts/search-components/SOLR-17055.md`. Limits now carries the recorded premise run (9 documents against 3; numFound not captured). The hold is the fix gate `gates/SOLR-17055-sort-path.md`, steps 2 to 6, claimed by vm2.

## Slice S3: SolrCloud

- SOLR-5813: SATISFIED. `pr-drafts/solrcloud/SOLR-5813.md` at gated head `90b8baa08aef`. The dated base run (2026-10-13, merge-base `cabedd1d968`, 3 tests, 1 failure) and the exact expected and actual failure are added.
- SOLR-12991: SATISFIED. `pr-drafts/solrcloud/SOLR-12991.md` at head `1a86966179e`. The dated base run (2026-10-13, merge-base `97d973814336`, 1 test, 1 failure on the WARN assertion) is added. Test-internal details that no receipt records are removed.
- SOLR-11288: SATISFIED. `pr-drafts/solrcloud/SOLR-11288.md` at head `cbf0c678f277`. The stale `cd094c3c623` base reference is replaced by the live-head base run (1 failure at the exact repeat). Limits names the gap: the space-padded check is never reached on base, read from the code. Flag for the main side: `receipts/SOLR-11288.md` says the base run reaches the padded case, but the run output stops at the exact repeat. Not edited.
- SOLR-10641: SATISFIED, a new draft. `pr-drafts/solrcloud/SOLR-10641.md` at head `fbc1f47b2808`. Proof 9 of 9 at head (2026-10-11); base 9 of 9 at `cabedd1d968`; the result is marked inconclusive by construction. Decision: the ship-alone call follows the recorded recommendation, since its conditions have landed. Conflict for the owner: the answers file still marks item 11 DISCUSS, so the ship-alone call contradicts that record. The opening stays with the main agent.

## Slice S4: SolrCloud confirmations

- SOLR-13186: SATISFIED. `pr-drafts/solrcloud/SOLR-13186.md` at live head `b436d90d2a88`. The base leg cites the dated base run (2026-10-13, merge-base `97d9738`, 1 test, 1 failure) in place of the 2026-10-06 ledger date.
- SOLR-15863: SATISFIED. `pr-drafts/solrcloud/SOLR-15863.md` at live head `78dff1d52f6c`. "(recorded 2026-10-08)" and the earlier-head sentence are dropped. The base leg cites the dated 2026-10-13 compile failure. Flag for the main side: the receipt pairs the `8f5e94cf314` failure with "BackupCoreAPITest 6 of 6" at that head, and the log is not reachable here. Settle the earlier-head sentence from `g15863-gate.log` before any reinstatement.
- SOLR-13369: HOLD on the base run. The draft is at live head `dfa0db5bdf96` (matches). The head run is dated 2026-10-13. The takeover date is removed from the base-pass sentence. The base sentence stays undated and held on the base run. Gate job `gates/SOLR-13369-base-premise.md` (OPEN). Drop the base sentence if the base run fails.
- SOLR-15674: SATISFIED. `pr-drafts/solrcloud/SOLR-15674.md` at live head `75ddf9ba518a` (the tree is identical to the superseded `ba01c83`). Five code links and the changelog link are repointed. The Proof date moves from the ledger's 2026-10-03 to 2026-10-13.
- Premise note: round 1 marked SOLR-13186, 13369 and 15674 CONVERGED on text, so "no verdict on record" was wrong for them. The main side should reconcile the convergence records.

## Slice S5: spellcheck drafts owed (first four)

- SOLR-1877: SATISFIED. `pr-drafts/spellcheck/SOLR-1877.md` at gated head `5533ba27b17e`. The Proof is a compile-level result, and it says so. Choice section: lock versus dropping the reload close.
- SOLR-4366: SATISFIED. `pr-drafts/spellcheck/SOLR-4366.md` at gated head `558864454f7f`. Gap named: the receipt gives the focused counts and the pre-fix result only, with no Error Prone or module-check line and no base failure text. Choice section: silent 200.
- SOLR-4399: SATISFIED. `pr-drafts/spellcheck/SOLR-4399.md` at gated head `82c0c11373d0`, which matches the live tip after the 2026-10-11 push. Gaps named: the receipt header still says "not pushed"; the proof ran on the intermediate init-guard version, not the merge base (the draft says so). Choice section: build versus init check.
- SOLR-9060: HOLD. `pr-drafts/spellcheck/SOLR-9060.md` at tip `704ca28bf79d`, which matches the receipt. The adopted changelog narrowing (round 5 decision 7) is not on the branch, and the receipt has no test counts or base result. The Proof carries a visible `[pending a recorded run]` marker, which is intended for a held draft. Gate job `gates/SOLR-9060-changelog-narrow.md` (OPEN): the text commit, changelog parse, focused run and fail-before at the new head. Choice section: shard payload cost.

## Slice S6: spellcheck remaining four

- SOLR-10789: SATISFIED. New `pr-drafts/spellcheck/SOLR-10789.md` at gated head `d9077048d74` (receipt: `SpellCheckCollatorTest` 13 of 13, 2026-10-09). The WARN handling is kept and named in Limits (round 5 decision 9).
- SOLR-3701: SATISFIED at `aabd678dec7`. The merge-base citation, the suffix wording and the vague Proof summary are fixed. The head pass count is not claimed, since the receipt gives none.
- SOLR-4367: SATISFIED at `0af6087f43fa`. Receipt counts added (15 of 15; base 1 failure of 15). The "not stated" line is removed; anchors and the merge-base citation are fixed. Not re-read: the JIRA wording ("any top-level name other than queryAnalyzerFieldType"), since no JIRA packet is on this host.
- SOLR-17612: SATISFIED at `3a456c27dda`. The gate of record is `gates/SOLR-17612.md`, run at this head, GREEN 2026-10-12. The receipt's first section is the `dd2708fbb7e` baseline, and the Proof says so. The rolling-upgrade limit (the threshold can come out too small) and the one-seed, random-handler limit are added. Non-blocking: a stray comment wrap in `SpellCheckComponent.java` at the 17612 head.

## Gate jobs filed this round (all OPEN, none run)

- `gates/SOLR-15906-sort-paren-fix.md`
- `gates/SOLR-13369-base-premise.md`
- `gates/SOLR-9060-changelog-narrow.md`

## Owner items raised by this round

1. SOLR-8954: decision 1 (close, or a test-only follow-up with 8009). The measured run confirms the breakage. The receipt's GATE GREEN is stale and needs a main-side correction.
2. SOLR-15906: the reading that the branch breaks three tests needs vm2 to confirm before any timing decision. Owner decides the timing.
3. SOLR-16570: which category holds it, and the title against the SOLR-16611 change on main.
4. SOLR-9124: confirm that owner decision 4 is met by the title change.
5. SOLR-10641: ship-alone against the DISCUSS item 11 in the answers file.
6. SOLR-15863: settle the earlier-head sentence from `g15863-gate.log`.
7. SOLR-11288: correct the receipt's claim that the base run reaches the padded case.
8. Convergence records: SOLR-13186, 13369 and 15674 were marked CONVERGED on text in round 1, so the round 4 premise was off. Reconcile.
9. SOLR-4399 receipt header: "not pushed" is stale.
10. SOLR-4367: re-read the JIRA wording when a JIRA packet is available on this host.

## Still open from earlier rounds

- SOLR-14231: vm2 to restate the counts and add the fail-before line.
- SOLR-4754: HOLD from round 1; gate `gates/SOLR-4754-text-fixes-and-premise.md` is OPEN.
- SOLR-8628 and SOLR-10234: opening held on gates `gates/SOLR-8628-packaging.md` (OPEN) and decision 8.
- SOLR-3865: owner decision 8.
- SOLR-13838: a fail-before run at `4520c25abe2` and the ticket text are owed.
- SOLR-14098: the ledger date conflict is with the main side.
- SOLR-18278: owner retire call.
- SOLR-16887: gate `gates/SOLR-16887-errorfile-bats-name.md` (OPEN).
- SOLR-15003: the three NOT_PROVEN tests are stated in Limits; no gate yet.

## Not started (remaining census follow-up)

- Suggester: 5 draft confirmations (`assignments/pool-census-followup-suggester-round-1.md`).
- Streaming-schema-configsets: 7 drafts owed (`assignments/pool-census-followup-streaming-schema-configsets-round-1.md`).
- Build-docs-cli-metrics: 7 drafts owed (`assignments/pool-census-followup-build-docs-cli-metrics-round-1.md`).
- Edismax leftover: 1 (`assignments/pool-census-followup-edismax-leftover-round-1.md`).
- SolrJ: confirmations for SOLR-4424, 5220, 7709, 12094 and 14967 were done in round 3; the round 3 length flags remain open.
- SOLR-17292 receipt reconcile.
