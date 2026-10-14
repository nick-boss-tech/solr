# Census follow-up, scheduled round 5 (six slices)

- Trigger: the two-hourly scheduled check. Recorded tip `917994c2c67`; live tip at the check `a57d9fb030f`. Claim: `claims/pool-census-followup-round-5-scheduled.md` (pushed at `d8eb1af575b`).
- The new commits before the check were vm2's records and main-side corrections: the SOLR-17055 fix gate, the SOLR-13369 dated base run, the SOLR-9060 changelog narrowing gate, the SOLR-15003 recount classification, the SOLR-8954 receipt correction and premise conflict record, and queued claims. No new assignment was posted.
- Worktree: `wt/pr-prepare-suggester`. Live fork tips checked by `git ls-remote origin` for every ticket. Each draft names the head its receipt or record names.
- Read only for branches: no build, test, Gradle or gate run; no commit or push on any `solr-*-submit` branch; no GitHub write; no receipt, gate file or claim edited by the review side.
- Written by the parent from the six subagent replies. The subagents could not write report `.md` files.

## Result

- 25 tasks. 16 SATISFIED, 9 HOLD. Five gate jobs filed this round, none run.

## Slice S1: settlements

- SOLR-17055: HOLD. The fail-before leg is not established: at the base `97d973814336` the run failed at the score-only assertion, not the sort assertion. The fix commit `2fe0986158` is not on the fork; the live tip `solr-17055-submit` is still `c2396f425fa2`. The HOLD comment names this. The proof-base choice (the coordinator named `c2396f425fa2`) is the main agent's.
- SOLR-13369: SATISFIED. `pr-drafts/solrcloud/SOLR-13369.md` at head `dfa0db5bdf96` (live tip). The base sentence is kept and dated: base `c3cdf7b46e8c`, 1 of 1, verified 2026-10-14. The "because" cause is attributed to the 2026-10-13 head run, since the 2026-10-14 receipt records no cause.
- SOLR-9060: SATISFIED. `pr-drafts/spellcheck/SOLR-9060.md` at gated head `49040cb7b88` (live tip). The pending marker is removed. Counts: 1 of 1 pass at head; the fail-before with `SpellCheckComponent.java` reverted fails 1 test, the tie order, dated 2026-10-14. Head citations are re-pointed. Gap: `49040cb7b88` is not in this clone, so the changelog-only claim comes from the receipt and the gate file, not a diff.
- SOLR-15003: SATISFIED with named gaps. `pr-drafts/core-admin/SOLR-15003.md` at head `1004abee39ab` (live tip). Limits rewritten: the three NOT_PROVEN tests are settled as guards that cannot fail on the base. The reload and missing-directory checks have no fail-before test; that is named as a gap. The recount date is added to Proof.

## Slice S2: SOLR-8954 and suggester

- SOLR-8954: HOLD. The head `1d981abe7000` matches the corrected receipt. The premise record recommends close or test-only, but records no adoption. Owner item (decision 1): close SOLR-8954, or make it a test-only follow-up with SOLR-8009 carrying the all-shards fix. No draft written.
- SOLR-9227: SATISFIED. `pr-drafts/suggester/SOLR-9227.md` at gated head `3c23fc5cfa6`. Proof is dated "Verified 2026-10-06 at head". "Audit" is changed to "review". Limits covers reload requests (`SolrSuggester.java` L211 to L213), taking the harder option on round 4's open item 1.
- SOLR-9968: SATISFIED. `pr-drafts/suggester/SOLR-9968.md` at `c31d2ff1a3f`. The diff from `b1865b605184` adds two tests and ref-guide text (gated at `d688e1efdf2`, 2026-10-06), then changelog and ref-guide wording only; production is unchanged. The tests moved, but the subagent relied on a recorded green top-up gate (13 tests, 2026-10-09) rather than a gate at `c31d2ff1a3f`. **Owner to confirm** that the top-up gate ran at the live head.
- SOLR-14171: SATISFIED. `pr-drafts/suggester/SOLR-14171.md` at gated head `faa262eedc5`. The context-path sentence is fixed to `Boolean.parseBoolean` (checked in code). A `javap` read of the pinned `lucene-suggest` 10.4.0 jar confirms the default for all-terms-required is true. The 2026-10-09 gate date is restored to Proof, since the formula requires the verification date.

## Slice S3: suggester and streaming

- SOLR-17215: SATISFIED on the text. `pr-drafts/suggester/SOLR-17215.md` at gated head `04d35df186d`. Fixes: the merge-base label and link; the breadth of the `IllegalStateException` catch is stated; "guard" and gate-check jargon is replaced; "Verified 2026-10-09" is removed under the date rule. Gap: `receipts/SOLR-17215.md` is missing. Gate job `gates/SOLR-17215-receipt.md` (OPEN) asks vm2 to write it.
- SOLR-17393: SATISFIED on scope. The round 4 hold is discharged: the merge-only scope and the per-shard follow-up are in Limits, as ratified on 2026-10-09. `pr-drafts/suggester/SOLR-17393.md` at head `626241e647e`. Fixes: the fidelity flag F7 is reworded (no claim about the reporter's weights); the merge-base label is fixed. Gap: `receipts/SOLR-17393.md` is missing. Gate job `gates/SOLR-17393-receipt.md` (OPEN).
- SOLR-14200: HOLD. `pr-drafts/streaming/SOLR-14200.md` is drafted at head `10991af9bda` (matches the receipt). Hold: the owner's retire-or-keep call, not a ladder item. Limits states the ticket scenario does not fail on main.
- SOLR-15326: HOLD. `pr-drafts/streaming/SOLR-15326.md` is drafted at head `dab6a466316` (matches the receipt). The same retire-or-keep hold. Drafting fix: the schema request runs for every `/export` open with a sort, not only sorts that lack a key.

## Slice S4: streaming and schema

- SOLR-17433: SATISFIED as a draft. `pr-drafts/streaming/SOLR-17433.md` at head `42b6c4fc915`. The opening is held on the retire-or-ship call (the receipt says pending; the S6 audit recommends retire). Premise: the 60-second symptom is a SolrJ 9.4 to 9.6 change. At the base commit the cap is 600 seconds, and the draft says so.
- SOLR-10131: SATISFIED. `pr-drafts/schema-analysis/SOLR-10131.md` at head `fd495167cd0`. Scope: submit with the "500 already fixed" statement (audit option a). The query path keeps the all-path rule and discloses it.
- SOLR-10403: SATISFIED. `pr-drafts/schema-analysis/SOLR-10403.md` at head `d8e03755cb5`. Ties round up, with the exact-half choice stated.
- SOLR-15712: SATISFIED as a draft. `pr-drafts/schema-analysis/SOLR-15712.md` at head `cb988a2ff4c`. The duplicate is kept as a separate core change, with SOLR-15777 as precedent. The compatibility change is an ICU-style version gate (the audit recommendation), filed as gate job `gates/SOLR-15712-udvas-version-gate.md` (OPEN). The opening is held until that change is gated; the draft is then rewritten.
- Premise note: the assignment's "NO GATE" label is stale. All three schema receipts record GREEN at the live tip.

## Slice S5: configsets, edismax leftover, build and docs

- SOLR-7323: SATISFIED. `pr-drafts/configsets/SOLR-7323.md` at gated head `b832dc0048c`. A choice section on the repeated server path is added.
- SOLR-3923: HOLD. The draft is at `pr-drafts/edismax/SOLR-3923.md` with a HOLD banner. The live head `723f61ee4ab` skips only unsigned lone parentheses; `+(` and `-)` still reach the phrase-field text (`ExtendedDismaxQParser.java` L310 to L317, read from code, not run). The receipt does not name the failing check. Steps: a branch fix, a new focused gate at the new head, then the premise run. Gate job `gates/SOLR-3923-premise.md` (OPEN, not run). The receipt records one fail-before result (`testPfPs`, 1 failure); the draft cites it with that caveat.
- SOLR-11700: SATISFIED. `pr-drafts/build-docs/SOLR-11700.md` at gated head `8a945757e66`. Proof states the light docs-only evidence.
- SOLR-17356: SATISFIED. `pr-drafts/build-docs/SOLR-17356.md` at gated head `9fe2801de07`. The branch docs cite LUCENE-7785, which no receipt verifies, so the draft omits it. Check the branch text before any posting.

## Slice S6: build and docs, CLI, hold and metrics

- SOLR-7119: HOLD (packaging only). `pr-drafts/build-docs/SOLR-7119.md` at gated head `4c652c93` (live tip). Owner decision 14 (drop the changelog) is adopted, and gate job `gates/SOLR-7119-changelog-drop.md` (OPEN) is filed. The Changelog line comes out when that job is done.
- SOLR-7924: SATISFIED. `pr-drafts/cli/SOLR-7924.md` at gated head `96ef3a52` (live tip). Opening slate: after SOLR-18339, per the CLI landing order. The AIX evidence stays simulated; a real AIX run is a named follow-up, since no AIX host is in the pool.
- SOLR-10390: HOLD for Nick. `pr-drafts/cli/SOLR-10390.md` at head `5a86f908` (live tip). The premise job ran on vm2 (DONE 2026-10-11, at `4af4a683`), so it is not owed. Owner decision 8 is the retire call: withdraw as superseded by SOLR-18339, or keep the test as a guard. No branch change filed. The branch's changelog title says the fix "no longer depends on lsof", but lsof still runs first; the test comment cites unverified ptrace kernels.
- SOLR-3684: HOLD stands as an owner item, decision 1: submit or retire. The record recommends retire, and no record names a planned test that needs the knob. The in-body hold moved to `reports/internal-blocks-round-1.md` on 2026-10-12, so the draft body reads as postable.
- SOLR-17987: HOLD on the rename clause only. The head `38abf642` matches, and everything else confirms. The draft carries the gated head's name, `SOLR_METRICS_DISABLEDREGISTRIES`. The rename is adopted and lands at opening packaging with a re-gate; the draft line changes then. **Owner to confirm** the rename is deferred to opening.

## Gate jobs filed this round (all OPEN, none run)

- `gates/SOLR-17215-receipt.md`
- `gates/SOLR-17393-receipt.md`
- `gates/SOLR-15712-udvas-version-gate.md`
- `gates/SOLR-3923-premise.md`
- `gates/SOLR-7119-changelog-drop.md`

## Owner items raised by this round

1. SOLR-8954: close, or a test-only follow-up with SOLR-8009.
2. SOLR-14200 and SOLR-15326: retire or keep.
3. SOLR-17433: retire or ship.
4. SOLR-10390: withdraw as superseded by SOLR-18339, or keep the test as a guard.
5. SOLR-3684: submit or retire.
6. SOLR-17987: confirm the rename is deferred to opening.
7. SOLR-17055: the proof-base choice (`c2396f425fa2`), since the fail-before leg is not established.
8. SOLR-9968: confirm the top-up gate of 2026-10-09 ran at `c31d2ff1a3f`.
9. SOLR-7119: confirm owner decision 14 (drop the changelog).
10. SOLR-3923: the branch needs a fix before the premise run.
11. SOLR-15003: the reload and missing-directory checks have no fail-before test.
12. SOLR-9060: `49040cb7b88` is not in this clone, so the changelog-only claim has not been diffed.
13. SOLR-17356: the branch docs cite LUCENE-7785, which no receipt verifies.
14. SOLR-7924: the real AIX run is a named follow-up.
15. Missing receipts for SOLR-17215 and SOLR-17393 (gate jobs filed for vm2 to write them).
16. The schema assignment's "NO GATE" label is stale (all three receipts record GREEN).

## Still open from earlier rounds

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

## Not started

- SOLR-10937, SOLR-11844, SOLR-9637, SOLR-10252, SOLR-11922: owner items (no gate on record, or retire-round material).
- SOLR-17292: receipt reconcile.
- The census is otherwise complete at 25 tasks in this round set.
