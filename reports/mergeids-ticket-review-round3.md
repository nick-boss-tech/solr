# Review: mergeIds ticket draft, round 3 confirmation

Verdict: **SATISFIED**. Items not confirmed: 0.

## Item 1: draft line 49 order (confirmed)

- Code order at the base commit `cabedd1d968059215188f4e7563fb303241899ed`, `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`: approximateTotalHits (accumulated at 1162 and 1164), maxScore (1169), numFound (1171), hitCountIsExact (1174, guarded at 1173). All four sit before the skip (`if` at 1193, `continue` at 1194).
- The set is complete. Reading lines 1150 to 1196 shows only the pre-skip flags and the `sortFieldValues` and `needsUnmarshalling` setup as other writes; none touches the four totals. The later `numFound--` at 1225 is in the push-rejected path after the skip.
- Draft line 49 now reads `{{approximateTotalHits}} / {{maxScore}} / {{numFound}} / {{hitCountIsExact}}`. That matches the code order.
- The diff from `a29a87d9ac8` to `ec3b96467a2` for `material/mergeids-ticket-draft.md` is one hunk, one line removed and one added, both at line 49. The only difference is the order of numFound and maxScore. The four names are the same set. No other draft text changed.
- The "Correction, draft line 49" text in the round 2 settlement section (`reports/mergeids-ticket-review-round2.md`) matches the diff exactly.

## Notes (no change required)

- Other draft lines name numFound before maxScore: the title (line 12), the symptom bullets (around lines 20 and 22), the reproduction result (around line 41) and the option 2 closing sentence (line 50). None is an accumulation-order list, so the order rule does not apply to them. The round 2 settlement sentence "Only the fix option 1 parenthetical had numFound before maxScore" is true only for accumulation-order lists.
- The settlement's citation of `hidden_files/side-finding-mergeids-verification.md` (section 1, "in this order") is not checked. The file is not in the worktree. The code-order claim was checked against the code itself.

## Scope and method

- Draft at `ec3b96467a2` (live tip at review). Claim `claims/pool-mergeids-ticket-review-round3.md`.
- Two read-only subagents: the code order at the base commit, and the scope of the diff with the wider draft text. Both returned CONFIRMED. The line numbers agree with the ones read directly in round 2.
- The SOLR-8628 receipt updated in the same origin push (gate green at `5b606dc779`) is in another lane and is not reviewed here.
- No draft edit, no Jira filing, no email, no comment, no build, test or gate.
- For SATISFIED, the main side passes the final text of `material/mergeids-ticket-draft.md` to Nick. Nick files the ticket. This review does not file anything.
