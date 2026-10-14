# Census follow-up, scheduled round 2 (six slices, four tickets each)

- Trigger: the two-hourly scheduled check. Recorded tip `b9d0e39a893`; live tip at the check `1ff400c79ba`. Claim: `claims/pool-census-followup-round-2-scheduled.md` (pushed at `cb72bf200c3`).
- The new commits before the check were vm2's gate results and backlog claims, not a new assignment: the SOLR-874 and SOLR-14381 title gates, the SOLR-14231 fail-before record, the SOLR-10234 receipt refresh, and the gate claims for SOLR-8554 and SOLR-3865.
- Worktree: `wt/pr-prepare-suggester`. Live fork tips checked by `git ls-remote origin` for every ticket. Every draft names the head its receipt names, and each one matched its live tip.
- Read only for branches: no build, test, Gradle or gate run; no commit or push on any `solr-*-submit` branch; no GitHub write; no receipt or claim edited by the review side.
- Written by the parent from the six subagent replies. The subagents could not write report `.md` files.

## Result

- 24 tickets. 22 SATISFIED. 2 HOLD, each with a named step.

## Slice S1: settlements of held items whose gates landed

- SOLR-874: SATISFIED. `pr-drafts/query-parsing/SOLR-874.md`, gated head `a283e050904a` (live tip). Citations re-pointed; Proof 10 of 10 and 4 of 4 at the new head, verified 2026-10-14. The base failures carry over from `ac9ab3375375` (2026-10-06); the test files are unchanged. The title at the gated head now drops a leading AND, OR, && or || only, which is the narrowing the gate job asked for. I checked the fragment title myself.
- SOLR-14381: SATISFIED. `pr-drafts/search-components/SOLR-14381.md`, gated head `7caf8d33d3` (live tip). The receipt quotes the replacement title, which matches the gate job. Citations re-pointed; symptom files linked to the merge-base `b5c71bc5573`; Proof names the merge-base and the counts at `a28b3f672cb4` (2026-10-07, code unchanged).
- SOLR-14231: HOLD. `pr-drafts/streaming/SOLR-14231.md`. `receipts/SOLR-14231.md` has no fail-before line. The base `GraphExpressionTest` XML reads 10 tests and 4 failures, but the file holds 3 failure elements, and the class-level ObjectTracker failure is unexplained. The pass counts at `593463f398` have no verification date. A HOLD comment was added; Proof is unchanged. Owed from vm2: restate the counts, explain the ObjectTracker failure, and add the fail-before line.
- SOLR-10234: SATISFIED. `pr-drafts/solrcloud/SOLR-10234.md`, gated head `8745afd00c` (live tip). The internal hold comment is removed; citations re-pointed; Proof 1 of 1 at `8745afd00c` (2026-10-14); the base leg carries over from `eb8c4060d9` (2026-10-12). The "changelog parses" clause is dropped. Owner decision 8 still applies: the gate job rewrote commit `48784679f96`'s message, so confirm before opening.

## Slice S2: search components wording

- SOLR-10424: SATISFIED. Bold summary on Limits; Proof reads "verified 2026-10-08 at this head" (the receipt confirms the status-line date).
- SOLR-10844: SATISFIED. Bold summaries on Proof, A choice to check and Limits. The choice lead-in changed to "Options considered:". The Proof date was already present (2026-10-05).
- SOLR-11310: SATISFIED. Bold summary on Limits.
- SOLR-11364: SATISFIED. Bold summary on Limits. The label for the earlier head (`8b3fc5c8880`) is kept. Proof reads "verified 2026-10-08 at this head" to match the formula template.

## Slice S3: search components wording

- SOLR-11470: SATISFIED. Bold summary on Limits; the wrapper bullet is folded into the summary.
- SOLR-13245: SATISFIED. Bold summary on Limits; repeated bullets trimmed; Proof reads "verified 2026-10-05" from the gate date.
- SOLR-14451: SATISFIED. Bold summary on Limits.
- SOLR-12044: HOLD on S4. Bold summary on Limits and the Proof date (2026-10-09) applied. The branch's changelog type is `optimized`, which dev-docs does not list. The change is a behavior change, so the type should be `changed`. The branch edit is not ours to make: gate job `gates/SOLR-12044-changelog-type.md` (OPEN) asks vm2 for the one-line change and a gate at the new head.

## Slice S4: search components wording

- SOLR-14678: SATISFIED. Bold summary on Limits. Tip `5d94e6cf3981`. Decision: the Proof does not state the premise-leg failure (receipt: 1 of 18 fails, `testExtraResponseFieldsDoNotLeakAcrossDocuments`), because that is not a recorded step. The receipt does not say "base production" outright.
- SOLR-14931: SATISFIED. Bold summaries on A choice to check, Proof and Limits. Tip `1a7d678d9a15`.
- SOLR-15018: SATISFIED. Bold summary on Limits. Tip `d0f29b4630c0`.
- SOLR-15331: SATISFIED. Bold summary on Limits. Tip `6769b4cd8a12`. Decision for the owner: Limits keeps "If SOLR-17051 lands first or in the same release." The word "lands" is not on the banned list, and it matches the plain form ruled for SOLR-15319. The owner may override.

## Slice S5: search components wording

- SOLR-15479: SATISFIED. Bold summary on Limits.
- SOLR-15895: SATISFIED. The sentence "The tidy check and the Error Prone compile also passed." is deleted from Proof. The date was already present.
- SOLR-16444: SATISFIED. The same sentence is deleted. The date was already present.
- SOLR-17155: SATISFIED. Bold summaries on A choice to check, Proof and Limits; "verified 2026-10-05" added to Proof from the receipt's test-conversion date. Owner item, not a hold: the choice section has no options list or alternative cost, which the template asks for. Left unchanged.

## Slice S6: search components wording

- SOLR-17051: SATISFIED. Bold summary on Limits; "on 2026-10-04" added to Proof from the receipt's gate line. The round 2 flag is confirmed: `FacetFieldProcessor.java` lines 534 to 542 and `FacetFieldMerger.java` lines 139 to 145 match the draft, so the wording is unchanged. Main side: correct the stale "only at the coordinator" sentence in `receipts/SOLR-17051.md`. Not edited here.
- SOLR-17748: SATISFIED. Bold summary on Limits; a duplicated "other shapes" sentence removed. Owner choice, not applied: whether the branch title should also cover the error-with-no-cause route.
- SOLR-17791: SATISFIED. The tidy and Error Prone sentence is deleted from Proof. "The four unit tests" becomes "The four new unit tests" to remove a dangling reference.
- SOLR-17976: SATISFIED. Bold summary on Limits.

## Gate jobs filed this round (OPEN, none run)

- `gates/SOLR-12044-changelog-type.md`: the one-line changelog type change on `solr-12044-submit`, then a gate at the new head.

## Owner items raised by this round

1. SOLR-10234 decision 8: confirm the rewritten commit message `48784679f96` before opening.
2. SOLR-15331: keep or override "lands" in Limits.
3. SOLR-17155: the choice section needs an options list and alternative costs, or an owner waiver.
4. SOLR-17748: whether the branch title should also cover the error-with-no-cause route.
5. SOLR-17051: correct the stale "only at the coordinator" sentence in `receipts/SOLR-17051.md` (main side).
6. SOLR-874: the receipt names the title correction but does not quote it; the title on the branch now reads correctly, so no further action beyond the receipt wording.

## Still open from earlier rounds

- SOLR-14231: vm2 to restate the counts and add the fail-before line (above).
- SOLR-12044: vm2 to apply the changelog type change (above).
- SOLR-8554, SOLR-8628, SOLR-3865, SOLR-4754 and SOLR-10234 gates: opening still held on their gate jobs; SOLR-4754 is still HOLD from round 1.
- SOLR-13838: a fail-before run at `4520c25abe2` and the ticket text are owed.
- SOLR-14098: the ledger date conflict is with the main side.

## Not started (remaining census follow-up)

- Search components drafts owed: all 12 (SOLR-6759, 8003, 8051, 8088, 8767, 8954, 9124, 9595, 10305, 16155, 17055, 17841).
- Query parsing: drafts owed SOLR-15906 and SOLR-16570.
- Core admin: drafts owed SOLR-15003, 16887 and 18278; refreshes for SOLR-12916 and SOLR-8576.
- SolrJ: confirmations for SOLR-4424, 5220, 7709, 12094 and 14967.
- SolrCloud: SOLR-5813, 10641, 11288, 12991, 13186, 13369, 15674, 15863 and the SOLR-17292 receipt reconcile.
- Spellcheck, suggester, streaming-schema-configsets, build-docs-cli-metrics, and the edismax leftover: not started.
