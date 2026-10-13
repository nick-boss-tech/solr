# CombinedQueryComponent ticket review, round 12

Verdict: STILL OPEN. Three items remain: O2 (corrected text, not applied), the line-68 label, and the line-50 attribution. Each has its exact replacement text below.

Basis: `assignments/pool-combinedquerycomponent-ticket-review-round12.md` at pr-prepare `09b00ed9ebf`. Probe facts from the export `material/combinedquery-probe-export/PROBE-SUMMARY.md`, not from memory or earlier reports. Code read at main `d76fc14b9857`. Claim: `claims/pool-combinedquery-round-12-review.md`.

Staffing: the assignment named one subagent. Scheduled step 5 asks for at least two, so this round used two. Subagent A checked each round 11 replacement's before text and code claim. Subagent B checked the draft's probe figures against the export and swept the whole draft.

## A5, taken from the export

- M1: FLAG SET. Shard 1 header sets `maxHitsTerminatedEarly` true (export line 315). The merged `approximateTotalHits` 21 was read from the response header (lines 329 to 334), so the header carries it.
- M2: FLAG SET. Shard 1 header sets it (export line 349). The merged value 7 was read from the header (lines 363 to 368).

## Applied this round

Each edit was confirmed present exactly once after the edit, and the draft has no dashes.

- O1, line 20. Before: "its approximateTotalHits includes the skipped shard's approximate count (15 instead of 5)." After: "...count (15 instead of 5, confirmed by run, case P2 below)."
- O3, line 68 (FLAG SET variant, applied as written). Before: "(run result), so the smaller one does not appear in it". After: "(run result, read from the response header, which carries it only when a shard sets maxHitsTerminatedEarly), so the smaller one does not appear in it". The arithmetic issue in this label is open item 2 below.
- S1, line 12 (title). Before: "...folds hitCountIsExact inverted". After: "...folds hitCountIsExact inverted (from code reading; run results in Reproduction)".
- S2, line 64. Before: "uses at lines 1173 to 1174, so that". After: "uses at lines 1173 to 1174 (from code reading, not run), so that".
- S3, line 64. Before: "The flag is one flag shared by every query". After: "The flag is one flag shared by every query (from code reading, line 315)".
- S4, line 64. Before: "so that for the lists a single sub-query contributes, the merged flag is true only when every contributing list reports its count exact". After: "so that, for the lists a single sub-query contributes, the merged flag would be true only when every contributing list reports its count exact".
- S5, line 66. Before: "which is a fold over every shard that sends a per-query entry (from code reading, not run) and cannot be un-folded without per-shard tracking." After: "which is a fold over every shard that sends a per-query entry and cannot be un-folded without per-shard tracking (from code reading, not run)."
- S6, line 70. The round 11 "before" text had no wiki braces, so the edit was made to the braced passage. Before: "which counts a skipped partial shard in the merged totals (from code reading, not run in this ticket) and whose {{approximateTotalHits}} fallback adds the running total,". After: "which counts a skipped partial shard in the merged totals and whose {{approximateTotalHits}} fallback adds the running total (from code reading, not run in this ticket),".
- S7, line 48. Before: "X1 (all shards exact, merged flag false) and X2 (all shards inexact, merged flag true)". After: "X1 (one sub-query; all shards exact, merged flag false) and X2 (one sub-query; all shards inexact, merged flag true)". Line 52 already tags both, so the two lines now agree.

## Not applied, and why

- O2, line 66. The round 11 replacement says the one-line change "settles the single-sub-query case". The code does not support that. Line 343 folds the exactness of every shard that sends a per-query entry, before the skip at lines 399 to 400. So the one-line change alone still lets a skipped shard fold its exactness in. The reorder in fix option 1 is what removes that. The corrected text is open item 1 below. I did not apply the round 11 text, because it states a claim the code contradicts.

## Open items

1. **Line 66, O2 (corrected).** Current text: "(case X1 confirms this by run; the single-sub-query scope is from code reading, not run)". Replace with: "(case X1 confirms this by run for one sub-query; that the one-line change corrects the fold for shards that are not skipped is from code reading, not run)". The code basis is lines 343 and 399 to 400 at `d76fc14b9857`.

2. **Line 68, the run label on arithmetic.** The label "(run result, read from the response header, which carries it only when a shard sets maxHitsTerminatedEarly)" covers the per-query totals 7 and 21 (M1), and 7 and 3 (M2). The export records only the merged values (M1: 21 and numFound 11; M2: 7 and numFound 5). The per-query totals are code arithmetic from the probe inputs. Replace the label with: "(run result for the merged values, read from the response header, which carries them only when a shard sets maxHitsTerminatedEarly; the per-query totals are code arithmetic from the probe inputs, not recorded in the export)".

3. **Line 50, the attribution of 15.** The P bullet says "merged approximateTotalHits was 15 instead of 5." The export records no approximate total for case P. The 15 is the assertion of case P2 ("P2 approximateTotalHits expected:<5> but was:<15>"). Replace "and merged {{approximateTotalHits}} was 15 instead of 5." with "and merged {{approximateTotalHits}} was 15 instead of 5 (case P2)."

## Notes that do not hold the verdict

- The commit `d76fc14b9857` and the phrase "unmodified main" (lines 5 and 48) are not stated in the export. The draft names the premise report as its source for them, and that is a named source, so I did not treat them as errors.
- The whole-draft sweep also suggested labels on lines 62 and 64 for code locations that read as present-tense facts. Round 12's rules count only items beyond round 11's list as label-placement preferences, which do not hold the verdict open. They are left for the owner's judgement.

## Verdict

STILL OPEN. Round 13 needs to apply items 1 to 3 above. The consolidated new-ticket email does not go out, since the ticket text is not final.

## Not done

- No PR, comment, Jira write, or email.
- No build, test, or gate. The probe was not re-run.
