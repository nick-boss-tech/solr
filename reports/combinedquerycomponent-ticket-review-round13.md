# CombinedQueryComponent ticket review, round 13

Verdict: SATISFIED. The ticket text is final on this review. The consolidated new-ticket email is not sent by this round; it goes out only once Nick sends it.

Basis: the three round 12 open items, applied and checked here, and a whole-draft sweep under the settled labelling scope. Probe facts come only from `material/combinedquery-probe-export/PROBE-SUMMARY.md`. Code read at main `d76fc14b9857`. Claim: `claims/pool-combinedquery-round-13-review.md`.

Staffing: two read-only subagents, as the scheduled step asks. Subagent A checked the three round 12 edits against the code and the export. Subagent B swept the whole draft for probe figures, unlabelled factual claims, and cited code lines.

## Round 12 items

- Item 1, line 66 (case X1, one sub-query; the one-line change corrects the fold for shards that are not skipped): CORRECT. The export gives X1 as one sub-query. The line-343 fold applies to every shard with a per-query entry, and the parenthetical does not claim the change stops a skipped shard's folding.
- Item 2, line 68 (the run label): CORRECT WITH EDIT, applied in this round. The first check found that the probe reads `numFound` from the response docs (probe lines 328 and 362), and the response header never carries it. The label now reads: "(run result for the merged values: approximateTotalHits was read from the response header, which carries it only when a shard sets maxHitsTerminatedEarly, and numFound was read from the response docs; the per-query totals are code arithmetic from the probe inputs, not recorded in the export)".
- Item 3, line 50 (case P2 attribution of the 15): CORRECT. The export records the 15 only for P2, and records no approximate total for P.

## Whole-draft sweep

- Task 1, probe figures: none. Every figure on lines 20, 21, 22, 48, 50, 51, 52 and 68 matches the export.
- Task 2, factual claims: one item, the line-68 label, already corrected above. No other unlabelled claim falls within the scope.
- Task 3, cited code lines: none. Every cited line matches `d76fc14b9857`.

## Notes that do not hold the verdict

- **Provenance of the commit.** The export does not record the commit the probe ran on. Lines 5 and 48 of the draft cite `d76fc14b9857` from the premise report, which the draft names as its source. That is a named source, so it stands. If the export is to be the only record of probe facts, the owner may add the commit line to `PROBE-SUMMARY.md`.
- **Fix option 2 (line 63) is incomplete.** The proposed null guard covers the fallback operand at line 384. The loop at line 411 (`docs.size()`) has no null guard, so a shard with no per-query entry can still throw there under the score-only-sort shape. The draft leaves that shape unsettled (lines 20 and 56), so the factual text is accurate. The fix text is a proposal, and it is outside the labelling scope. The owner may want the proposal to say that line 411 needs the same guard.

## Corrections made in this round

- The line-68 label, as above. It corrects the round 12 item 2 wording, which had said the header carried the merged values.

## Not done

- No PR, comment, Jira write, or email. The consolidated email is Nick's to send.
- No build, test, or gate. The probe was not re-run.
