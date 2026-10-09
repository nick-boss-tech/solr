# SOLR-7504 null-count fix: drafts brought up to the new head

Claim: `claims/update-7504-nullfix.md` (commit `473f9ecbd28`). Receipt: `receipts/SOLR-7504.md` (commit `0e567a0d7db`). Head: `solr-7504-submit` at `2fe06bfd917f`, which matches the receipt and the live tip.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents edited two drafts. The lead checked the diffs and made one further fix. Nothing was posted. No PR body or title was changed, and no submit branch was edited. No builds, Gradle or tests were run.

## What changed

- **SOLR-7504 draft.** Cites the new head `2fe06bfd917f` throughout, with line ranges re-checked at that head. "A null operand counts as 0" now describes the fixed code, which reads the values once and treats null as no values. Proof states the receipt's facts only: 30 of 30 and 42 of 42 at the new head, and the new test `testCountValuesNullValue` as the one test that fails on the pre-fix code at `22b77196e662`. The combined run with SOLR-12705 is described as a run at the pre-fix head, before the null fix. The "Before this change" link points at base `97d973814336`, not the branch head.
- **SOLR-12705 draft.** The combined-run sentence now says the run was at SOLR-7504's pre-fix head, before its null fix, and does not cover `2fe06bfd917f`. The Proof heading no longer implies that the current SOLR-7504 state was combined.

## Other drafts

Read-only check of the other drafts for the SOLR-7504 head and the combined-run wording: no other draft cites `22b77196e662`, `2fe06bfd917f`, or SOLR-7504 in a way that needs a change. The other "combined" hits concern other tickets.

## For your confirmation

1. **Combined tree at the fixed head.** The only combined run recorded is at the pre-fix head. Neither the drafts nor the PR can claim a combined run at `2fe06bfd917f`. The main side should decide whether to re-run the combined check.
2. **Live PR.** The SOLR-7504 pull request (5081) still points at the old head until the main side updates it. The PR body should not be updated until the owner rules.

## Not done

No gate, build or test was run. No PR was edited, and no submit branch was changed.
