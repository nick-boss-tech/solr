# Internal review of the 28 opened update-processing PRs: roll-up

Assignment: `assignments/review-opened-28.md` (commit `42d9492fc79`). Claim: `claims/review-opened-28.md` (commit `20f84d1b293`). Group reports: `reports/review-opened-28-g1.md` to `reports/review-opened-28-g7.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Seven subagents reviewed four PRs each, read only. The lead merged their reports and wrote this roll-up. No PR was edited, no draft was flipped, no comment was posted, and no submit branch was changed. No builds, Gradle or tests were run.

## Summary

- **Ready to flip: 0 of 28.**
- **Fix first: 26.** Most fixes are in the PR text, and the most common is the PR title.
- **Owner call: 2.** SOLR-5505 and SOLR-16356 (title only).
- **One blocking code defect.** SOLR-7504 throws a NullPointerException on a null-valued counted field, which the base code does not. The fix is on the submit branch and needs a re-gate before that PR can flip.
- **Systemic.** All 28 titles are Jira summaries, not the changelog-title rule in the amendment. Several Limits sections make no follow-up plan. Several Proof sections cite results the receipts do not record.
- **Heads.** All 28 PR heads match their receipt heads, and all 28 bodies are byte-identical to their drafts.

## Verdict table

| Ticket | PR | Verdict | Main reasons |
|---|---|---|---|
| 3657 | 5069 | Fix first | Title. Limits has no bold summary. |
| 4841 | 5070 | Fix first | Title. The "today" link points at head, where the constructor is already public; cite base. Compile-failure wording not in the receipt. "One assertion" should read two. |
| 5065 | 5071 | Fix first | Title. Locale gap has no follow-up offer. |
| 5505 | 5072 | **Owner call** | Always-on `[core]` format change, with no opt-in and no choice section. Title typo ("usabe"). |
| 5754 | 5073 | Fix first | Title names a class that does not exist. Proof says no run count, but the 5939 receipt records 2 of 2 in the combined run. |
| 5887 | 5074 | Fix first | Title. "Every path" is false: `ClassificationUpdateProcessor` bypasses the wrapper. Limits says DocumentBuilderTest was not run, but its receipt records 17 of 17. |
| 5939 | 5075 | Fix first | Title. "Before, the map kept them" is false; the map is new in this change. The combined run's 5754 head is not named. Body is 6,990 characters. |
| 5941 | 5076 | Fix first | Title. Proof cites tidy and Error Prone, which the receipt does not record. Pre-fix result is missing. A five-class not-re-run list is not in the receipt. Anchor should be L71-L112. |
| 6045 | 5077 | Fix first, plus owner call | Proof omits two new tests. Limits has no follow-up plan. Title. "Mixed field is rejected" holds on the merger path only; the factory path wraps it as a set. |
| 6065 | 5078 | Fix first | Title. Bold summary missing. Proof wording differs from the receipt, same fact. |
| 6973 | 5079 | Fix first | Title. Proof claims changelog parse, tidy and Error Prone, which the receipt does not record. |
| 7022 | 5080 | Fix first | Proof says the live tip was never gated; the refreshed receipt says the top-up gated it. Title. Bold summary missing. |
| 7504 | 5081 | **Fix first, blocking** | **Null-valued counted field throws NullPointerException** at `CountFieldValuesUpdateProcessorFactory.java` L78 (base stored 0). Branch fix and re-gate needed. Bullet 5 says the operation map with no operation is rejected; the code rejects only a field with no `set`. "The one behavior change" is false. "Merges without conflicts with SOLR-12705" is false: an import-block conflict. Title. No follow-up plan. |
| 11475 | 5082 | Fix first | Title. Bold summary missing. |
| 11483 | 5083 | Fix first | Title. Bold summary missing. One sentence repeats the summary. |
| 12245 | 5084 | Fix first | Title is the Jira summary. Limits says "No test covers a null guard": false, because `testStatusCodeOnDistribError_NotSolrException` covers the `req == null` guard. Bold summary missing. Two cosmetic wording fixes. |
| 12703 | 5085 | Fix first | Bold summary missing. Stale link to the 6045 branch (`e4b77fa`, should be `bcae04d`). Changelog's RunUpdateProcessor symptom is not supported (branch content). Title. No follow-up plan. Proof "verified at this head" lacks the date. |
| 12705 | 5096 | Fix first, plus owner call | Proof has no fail-before result; the receipt records pass counts only. Landing order omits a second textual conflict with SOLR-16655. Title. No follow-up plan. Owner call on default scope. |
| 12864 | 5086 | Fix first | Title says the bug exists; body says it does not reproduce. Remove "A choice to check": no live design decision. "This draft" should read "this PR". Bold summary missing. |
| 13265 | 5087 | Fix first | Title is the Jira summary. What credits the change with the `DistributedUpdateProcessor` flag, which the diff does not touch. "Fails at its assertion" is not in the receipt, and the gate log it names was not found. Bold summary missing. |
| 13696 | 5088 | Fix first | Title. Proof cites tidy, Error Prone, seeds and hashes that the receipt does not record. The time-route sub-choice is a scope question with no alternative cost; remove it. |
| 13943 | 5089 | Fix first | "Touches tests only" is false for the PR diff against main, which includes SOLR-13696's production files. State the stack first. "3 of 5 runs" is not in the receipt. Title names the old method. Tidy and Error Prone not in the receipt. |
| 14262 | 5090 | Fix first | Title. Choice question needs a blank line before it. Cloud bullet wording. |
| 14718 | 5091 | Fix first | Title. "What happens today" cites a link where the copy is already present; cite base `9b3a84b1c460`. SOLR-5939 links point at a commit other than the PR head. Proof calls a helper a new test. |
| 16356 | 5092 | **Owner call (title only)** | Title is the Jira summary. |
| 16655 | 5093 | Fix first | Limits says "confined to the child-document descent", but the diff also moves the field loop into `mutateDocument`. Title is 260 characters. "The commit before this change" is ambiguous. Link label "gated descent" is internal wording. Time "08:03 MDT" is not in the receipt. |
| 16673 | 5094 | Fix first | What omits that the Long and Double changes also reach the Schema Designer type guess. Proof numbers (43 of 43, tidy, Error Prone, fail-before) are not in the receipt. Title (shared title call). |
| 16910 | 5095 | Fix first | Bold summary says details are cleared after both logs; the code clears them before logging. Title (shared title call). |

## Blocking

1. **SOLR-7504 null-count regression (branch).** A counted field with a null value throws NullPointerException at `CountFieldValuesUpdateProcessorFactory.java` L78, where `src.getValues()` is read. Base stored 0. The PR must not flip until the branch is fixed and re-gated. The fix is a submit-branch edit, which this review does not make.

## Fix items by kind

**Titles (all 28, systemic).** The amended rule says `SOLR-<ticket>: <changelog title>`. The openings report says all 28 titles are Jira summaries. Titles run 178 to 361 characters. Retitling is a public write, so it needs your go-ahead (owner call O1).

**Proof text not in the receipts.** Remove or correct the claims the receipt does not record, from the drafts and the PR bodies: tidy and Error Prone (5941, 6973, 13696, 13943), build checks (6973), seeds and hashes (13696), the "3 of 5 runs" figure (13943), pre-fix numbers (13265, 16673), and times (16655). The 7022 refreshed receipt also dropped its 2026-10-06 counts and pre-fix result; the main side should restore them.

**Citations.** Point base-code links at base and head-code links at the head: 4841 ("today" link), 14718 ("today" link and the SOLR-5939 links), 12703 (stale 6045 link), 5941 (anchor L71-L84 should be L71-L112).

**Presentation.** Bold one-line summary missing under Limits in several PRs (3657, 6065, 7022, 11475, 11483, 12864, 13265, 12703, and others). Repeated summary sentences (11483, 12245, 12864, 13265). Internal wording ("gated descent", "this draft"). Blank line before a Choice question (14262).

**Follow-up plans in Limits.** The owner's standing rule asks for a plan to submit a follow-up. Several Limits say only "on request" or name a gap with no follow-up (5754, 5939, 6045, 7504, 12703, 12705, 16655 and others). Owner call O2 sets the wording.

**Claims that are false or unsupported (description).** 5887 ("every path", and the DocumentBuilderTest "not run" line), 5939 ("Before, the map kept them"), 12245 (the null-guard sentence, which is my error from the correction round), 12864 (choice section), 13943 ("tests only"), 14718 (helper described as a test), 16655 ("confined to"), 16673 (missing Schema Designer note), 16910 (clearing order), 13265 (DistributedUpdateProcessor credit).

**Branch content, not description.** 12703 changelog text (RunUpdateProcessor symptom). 5505 always-on format (owner call O3). 7504 null-count defect (blocking, above). 5887 `ClassificationUpdateProcessor` bypass (scope or code; owner call O8).

## Owner calls

- **O1. PR titles.** Confirm the rule (the changelog title) and approve retitling the 28 PRs. The amendment and the openings report disagree.
- **O2. Follow-up plans in Limits.** Adopt the standing rule's wording ("a planned follow-up submission") across the drafts, or accept "on request" wording.
- **O3. SOLR-5505 format change.** Keep the always-on `[core]` format with a choice section (type stays `changed`), or make it opt-in (type becomes `added`).
- **O4. SOLR-6045 factory path.** Enforce the mixed-field rejection in the factory path too, or scope the claim to the merger path.
- **O5. SOLR-12705 default scope.** Mutate atomic operands by default for every field-mutating processor (as written, with a per-class opt-out), or default off with only the date parser opting in.
- **O6. SOLR-16356 title.** Confirm the changelog title as the PR title (part of O1).
- **O7. SOLR-7504 branch fix.** Authorize the null-count fix on the submit branch and a re-gate, which is outside this review.
- **O8. SOLR-5887 bypass.** Scope the claim to the paths the change covers, or fix `ClassificationUpdateProcessor` on the branch.

## Next steps for the lead

1. Correct the drafts for the description and Proof items above, including the SOLR-12245 null-guard sentence that I introduced. Drafts are repository files and can be edited without a public write.
2. Leave the PR bodies and titles unchanged until the owner decides O1 to O8. Changing them is a public write.
3. Ask the main side to restore the 2026-10-06 counts and pre-fix result to the SOLR-7022 receipt, and to fix the 7504 null-count defect on its branch.

## Not done

No PR was edited, commented on, flipped, or closed. No submit branch was changed. No draft was edited in this roll-up. No build, Gradle run or test was run.
