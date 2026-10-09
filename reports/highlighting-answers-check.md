# Highlighting answers: check of the two changed drafts

Claim: `claims/highlighting-answers-check.md` (commit `3d800892f6b`). Answers: `material/highlighting-round-1-answers.md` (commit `24f69b3d9e4`). Per-draft reports: `reports/highlighting-answers-check-2681.md` and `reports/highlighting-answers-check-4540.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two read-only subagents checked one draft each. The lead wrote this roll-up. Nothing was applied to a draft, committed to a branch, posted, or opened.

## Result

Both drafts stay on hold. Neither is postable yet.

- **SOLR-2681 (`4cb25b1691b9`): HOLD until the recount is recorded.** The draft still states 36 of 36 as fact in two places. The source has 31 `@Test` methods at this head and 30 at base. The "follow-up submission is planned" sentence has no record behind it. The changelog title says "a query nested inside a function query", but the code handles only `query(...)` as the function's value source. The title needs narrowing, which takes a branch edit and a re-gate. The internal note is gone, and the code links check out. Exact replacement texts are in `reports/highlighting-answers-check-2681.md`.
- **SOLR-4540 (`62c06439fb06`): HOLD until the base result is cleared and internal text is removed.** The OPEN line is filled, but the receipt records only that the premise was grounded by a run. The seed, the log name, and the base result are not in the workspace, so the statement cannot be verified here. The draft still has an internal comment at the top and the seed and log name in public text. The changelog title still carries the slowdown claim, which is to be corrected at packaging with a re-gate. The code links check out. Exact replacement texts are in `reports/highlighting-answers-check-4540.md`.

## Decisions for you

1. **SOLR-2681 recount.** The posting waits for the main side's recount. Confirm that the 31 `@Test` count does not change the decision.
2. **SOLR-4540 base result.** The statement rests on a main-side run. Confirm it can stand, or ask the main side for the record that the receipt should carry.
3. **Changelog titles for 2681 and 4540.** Both are to be fixed at packaging, which means a branch edit and a re-gate. Confirm that is the route.

## Not done

No draft was changed. No PR, comment, Jira item, or branch was touched. No build, Gradle run or test was run.
