# Spellcheck round 5 report: audits

Round: `assignments/spellcheck-round-5.md` (commit `1ea7e2c7811`). Claim: `claims/spellcheck-round-5.md` (commit `17707d8d4f1`). Audits: `audits/spellcheck/SOLR-<ticket>.md`, one per ticket. Trial-merge matrix and proposed order: this report's interaction section. The subagent's scratch copy is outside the repo.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents wrote the audits and the matrix (five audits and the matrix in one, four audits in the other). The lead checked the live tips, spot-checked the code claims, and wrote this report. Audits only: no gates, tests, builds, drafts, PRs, comments, or submit-branch edits. Nothing was compiled or run.

## Summary

- Nine audits. None is ready to draft outright. Seven are held. Two are "draft with named Limits or Choices": SOLR-3701 and SOLR-10789.
- No receipt is visible for any of the nine. Gate state below is "no receipt visible", not "no gate". The main side answers with its ledger, as in rounds 3 and 4.
- All nine live tips match the claim and the inventory head (2026-10-08).
- Two of ten pairs conflict among the five branches that touch `SpellCheckComponent.java`: SOLR-4366 with SOLR-4367, and SOLR-4366 with SOLR-17612. Both are keep-both insertions. The proposed order rebases only SOLR-4366.
- Every audit has zero em dashes. The matrix has none either.

## Per-branch table

| Ticket | Live tip | Gate state as visible | Last review on record | Verdict | Blocker that matters most |
|---|---|---|---|---|---|
| SOLR-1877 | `0d5916186797` | No receipt visible. The tip's commit message says "gate complete". | Round 28: Needs work (High) | Held | Reload closes the previous reader (`IndexBasedSpellChecker.java` L74-75) while a concurrent search or build may still hold it (`AbstractLuceneSpellChecker.java` L151, L159). |
| SOLR-3701 | `aabd678dec7` | No receipt visible. Gate owed. | Ledger head `77137cf17a9` is superseded. | Draft with named Limits or Choices | No blocking code defect. The test is ASCII and token-level only. |
| SOLR-4366 | `5613b311952e` | No receipt visible. Awaiting pipeline. | None on record. | Held | Root `SOLR-4366-TESTING.md` is in the diff, which the workspace diff rule bars. The distributed no-spellcheckers path still NPEs (`SpellCheckComponent.java` L391, L450-451), the same as upstream main. |
| SOLR-4367 | `0af6087f43fa` | No receipt visible. | Round 28: Needs work. Ledger head `24e06b98e36f` is superseded. | Held | The JIRA packet says any other top-level name should throw. The check covers only `classname` (L698), so the class alias is still ignored. |
| SOLR-4399 | `de6cc6b27edd` | No receipt visible. | Ledger head `823b9769f91b` is superseded. | Held | The init-time guard (L63-71) rejects index-only configs that never build. Review: Medium. |
| SOLR-9060 | `704ca28bf79d` | No receipt visible. | Round 28: Not ready | Held | The coordinator merge uses the default queue (`SolrSpellChecker.java` L118), so `comparatorClass=freq` is not honored. The changelog overstates. |
| SOLR-10252 | `a2be0f3adfe6` | No receipt visible. Docs and config only. | None on record. | Held | The note's "catch-all" is wrong for the shipped schema: `copyField` is commented out (`managed-schema.xml` L128). "If you configure them" is wrong for synonyms. |
| SOLR-10789 | `d9077048d74d` | No receipt visible. Gate owed. | None on record. | Draft with named Limits or Choices | No blocker visible. |
| SOLR-17612 | `cd0426e40a72` | No receipt visible. | Round 28: Not ready | Held | Multi-filter thresholds sum per-shard minima (`SpellCheckComponent.java` L304-354). For example, 20 where the correct value is 110. Also the TESTING doc in the diff, a missing blank line (L355), and a fail-before that depends on random routing. |

Verdict count: two draft with named Limits or Choices (3701, 10789); seven held. No branch is "draft".

## SpellCheckComponent interaction and proposed order

All five branches (SOLR-1877, 4366, 4367, 9060, 17612) touch `SpellCheckComponent.java`, so none was dropped. Each trial merge is `git merge-tree --write-tree` at the live tips.

| Pair | Result | Detail |
|---|---|---|
| 1877 x 4366, 1877 x 4367, 1877 x 9060, 1877 x 17612 | Clean | None |
| 4366 x 4367 | **Conflict** | `SpellCheckComponentTest.java`, 1 hunk. Both add a test method at the same insertion point. Keep both. |
| 4366 x 9060 | Clean | None |
| 4366 x 17612 | **Conflict** | `SpellCheckComponent.java`, 1 hunk. Both add a private helper after `process()`. Keep both. |
| 4367 x 9060, 4367 x 17612 | Clean | None |
| 9060 x 17612 | Clean | Both change the shard branch of `process()` and both add to `DistributedSpellCheckComponentTest.java`. Re-run the distributed test after the second one lands. |

Proposed order: **SOLR-1877, then SOLR-4367, then SOLR-9060, then SOLR-17612, then SOLR-4366.**

Why this order: SOLR-1877 is clean with everything, so it lands first at no cost. SOLR-4367's only conflict is with SOLR-4366, so it lands before SOLR-4366. SOLR-9060 and SOLR-17612 are clean with each other in text, but both change the shard path, so 9060 lands first and 17612 re-runs its distributed test. SOLR-4366 lands last, so it is the one branch rebased, with two conflicted hunks. Putting 4366 first would force rebases on 4367 and 17612 instead.

Caveats: the conflict analysis is textual. Nothing was compiled, and the combined code is not shown to build or pass. Each branch's readiness decides when it can land. The round 28 reviews say 1877 and 4367 need work and 9060 and 17612 are not ready. SOLR-4366 has no round-28 review.

## Owner decisions

Each item names the facts it turns on. DISCUSS marks a call the main side should not take alone.

1. **SOLR-1877 reload close (DISCUSS).** Keep the reload-time close with a refcount or lock, or drop it and keep only `close()`. The round 28 review rates it High.
2. **SOLR-3701 apostrophe rule.** The rule changes contractions and elisions as well as possessives. Decide the breadth, and whether to keep or drop the U+2019 alternative.
3. **SOLR-4366 silent success (DISCUSS).** `build` and `reload` return 200 silently when there are no spellcheckers. Keep that, or fail.
4. **SOLR-4366 distributed NPE.** Guard the distributed no-spellcheckers path in this PR, or name it as a Limit. It is the same on upstream main.
5. **SOLR-4367 scope.** Widen the check to every unsupported top-level key, or name the `classname`-only scope. Separately: throw from `inform`, or warn.
6. **SOLR-4399 timing (DISCUSS).** Fail at init, or fail at build. Also whether to use the ticket's default-name option.
7. **SOLR-9060 (DISCUSS).** Does the tie-break fix answer the ticket, given that `comparatorClass=freq` is not honored? Also the changelog wording, and whether to accept the shard-side cost.
8. **SOLR-10252 (DISCUSS).** Comment-only, or a field change. The `copyField` premise no longer holds in the shipped schema.
9. **SOLR-10789.** An unparseable `collateParam` override gives a silent empty collation with a WARN. Keep that, or make it a visible error.
10. **SOLR-17612.** When no shard reports, fall back silently to the old local count, or fail? Also the strength of the fail-before proof, which depends on random routing.

## Gaps and discrepancies against the records

- **Ledger heads are superseded.** SOLR-3701 (`77137cf17a9`), SOLR-4367 (`24e06b98e36f`), and SOLR-4399 (`823b9769f91b`) have moved on. None is an ancestor of the live tip.
- **Skip rationales are contradicted.** `research/pipeline/skips.md` gives "obsolete" for SOLR-4366 and "code rewritten" for SOLR-1877. Upstream main still lacks the build and reload null checks and the reader close methods, so neither rationale holds.
- **The VM2 "tidy-only DRIFT" flag on SOLR-17612 does not hold** at the live tip. The change is not tidy-only.
- **"Moved, ff +1" for SOLR-10252 and SOLR-10789.** The assignment's table describes these as moves "at inventory time". The inventory's "Since 10-06" column records a move from 10-06 to 10-08. Neither branch moved after the inventory.
- **SOLR-1877's commit message says "gate complete".** No receipt is visible to confirm it.
- **SOLR-9060's changelog overstates** what the change does, as above.
- **SOLR-4366 carries a TESTING doc in its diff**, which the workspace rule bars.

## Lead checks

- Live tips for all nine matched the claim and the inventory head at `ls-remote` on 2026-10-09.
- Spot-checked at the live tips: `SOLR-4366-TESTING.md` appears in the SOLR-4366 diff; the SOLR-1877 reader close is at `IndexBasedSpellChecker.java` L74-75. The SOLR-4367 classname check at L698 was not separately checked.
- Em dash count is 0 in all nine audits and the matrix. No PR numbers in file names.
- The two subagents' trial merges agree on 1877, 4366 and 4367.

## Not done

No gates, tests, builds, Gradle runs, PRs, comments, or submit-branch edits. `research/` was read from the main checkout, read-only. `upstream/main` was not refetched. Review dates are file modification times, not review dates recorded in the reviews.
