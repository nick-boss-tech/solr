# Spellcheck draft round report

Round: `material/spellcheck-round-5-answers.md` (commit `27a30874916`), which answers the ten owner decisions of `reports/spellcheck-round-5.md`. Claim: `claims/spellcheck-draft-round.md` (commit `83999925a7d`). Drafts: `pr-drafts/spellcheck/SOLR-<ticket>.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents drafted and checked. The lead checked their output and wrote this report.

## Summary

- Two drafts written: SOLR-3701 and SOLR-4367. Both are adopted decisions with receipts at the live tip.
- Seven branches held. Six wait on DISCUSS items or a pending gate. SOLR-10789 waits for its gate receipt.
- Two live tips moved since the answers file, both from packaging commits the main side launched. SOLR-4366 and SOLR-17612 are held at their new tips.
- No draft is ready to post. SOLR-4367 has a marked owner-fill line in its Proof. SOLR-3701's changelog title needs an owner decision.
- Nothing was posted. No PR was opened or changed, and no submit branch was edited.

## Drafts

| Ticket | Head (live, checked) | Draft state | Open points |
|---|---|---|---|
| SOLR-3701 | `aabd678dec7` | Draft. Receipt: GATED, PUSHED 2026-10-06, premise grounded. Decision 2 adopted: the rule as implemented, with the U+2019 alternative in the pattern, named in Limits. | The receipt gives "exactly 1 failure on base production" but names neither the failing method nor a pass count. The changelog title repeats "pandora's's", which no source supports. Owner to decide whether to narrow it. |
| SOLR-4367 | `0af6087f43fa` | Draft, with the Proof counts marked for the owner to supply. Decision 5 adopted: the `classname`-only check and the throw from `inform`. | The answers file gives no run counts and no base-code failure. The marked line stays until the owner supplies them. The widening rationale in decision 5 was left out, because it does not read cleanly against the code. |

Checks on the drafts:

- SOLR-3701: the regex and test lines were checked against the head by reading. The token results come from a hand-equivalent of the pattern, not from a run. The draft's Limits say so for the one-letter elisions (`l'homme` and `d'accord` do not change).
- SOLR-4367: the throw traces to core load (`SolrCore.java` L1141, and the constructor catch at L1167-L1186). The top-level loop reads only `spellchecker`, `classname`, and `queryAnalyzerFieldType`.
- Em dash count is 0 in both. Neither draft cites a PR number.

## Held

| Ticket | Live tip | Reason |
|---|---|---|
| SOLR-1877 | `0d5916186797` (unchanged) | Decision 1 is DISCUSS. The recommended change makes the reader close safe and needs a re-gate. Close sits at `IndexBasedSpellChecker.java` L74-75. |
| SOLR-4366 | `558864454f7f` (moved from `5613b311952e`) | Decision 3 is DISCUSS. No gate receipt yet. The packaging commit removed the root `SOLR-4366-TESTING.md`. Decision 4 is adopted as a Limit. The NPE is still at `SpellCheckComponent.java` L391 and L450-451. |
| SOLR-4399 | `de6cc6b27edd` (unchanged) | Decision 6 is DISCUSS. The recommended change moves the init guard to build and retitles the changelog. The guard is at `FileBasedSpellChecker.java` L63-71. |
| SOLR-9060 | `704ca28bf79d` (unchanged) | Decision 7 is DISCUSS. The changelog narrowing is a text commit, so it moves the head. The default queue is at `SolrSpellChecker.java` L118. |
| SOLR-10252 | `a2be0f3adfe6` (unchanged) | Decision 8 is DISCUSS. The `copyField` is commented out in `managed-schema.xml` L128 (comment L126-127). The field question is the owner's. |
| SOLR-10789 | `d9077048d74d` (unchanged) | Decision 9 is adopted. The gate is launched and no receipt is recorded. |
| SOLR-17612 | `dd2708fbb7e5` (moved from `cd0426e40a72`) | Decision 10 is DISCUSS. No receipt. The two new commits are the handoff-doc removal (`2fcced09678`) and a tidy formatting commit (`dd2708fbb7e`). |

## Owner decisions and items to supply

1. **SOLR-4367 Proof counts.** Supply the run counts and the base-code failure from the takeover log. The draft stays marked until then.
2. **SOLR-3701 failing method and pass count.** The receipt names neither. Supply them, or accept "exactly 1 failure" as written.
3. **SOLR-3701 changelog title.** It repeats "pandora's's", a symptom no source supports. Narrow it, or keep it.
4. **SOLR-17612 moved tip.** The owner should see that the tip moved. Decision 10 stays DISCUSS.
5. **SOLR-4366 moved tip.** Same. The gate for the packaged head is launched.

## Record correction for the main side

The answers file says a possessive-only rule "is not expressible in the converter's pattern". That is too strong. A `'s`-only pattern can be written, but it would also match `it's` and `let's`. The draft does not use the phrase. The main side should correct the answers file.

## Not done

No gates, tests, builds, Gradle runs, PRs, comments, or submit-branch edits. No token results were produced by a run; the SOLR-3701 results are hand-checked. Nothing was posted.
