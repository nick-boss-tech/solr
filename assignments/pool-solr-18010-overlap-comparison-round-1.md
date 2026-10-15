# Pool assignment: SOLR-18010 overlap comparison, round 1

Status: DONE

Capability tags: `review`. Intended host: windows (review coordinator). Staffing: 1 subagent. Claim: under the standing coordinator claim, `claims/coordinator-review-rounds.md`.

## Why this round exists

`reports/recent-pr-overlap-check-2026-10-12.md` (class A, item 1) found a live duplicate effort. SOLR-18010 covers corruption of security.json when two edits run in parallel. Another contributor, sarkaramrit1993, has PR #5065 open on apache/solr: "SOLR-18010: Fix security.json corruption from parallel edits", created 2026-10-09, head branch SOLR-18010-security-json-corruption, base main, still open at the time of the check. This side's branch solr-18010-submit carries a fix for the same ticket and the same defect. It is GATE GREEN at c3685bb37d9d (`receipts/SOLR-18010.md`) and has not been opened as a PR. Two implementations of one fix exist; theirs is the open PR.

Before Nick decides whether this side opens its own PR, the two implementations need a side by side comparison. That is this round. It is read-only: no branch edits, no gate, no posts, no comments.

## What to compare

**(1) The two diffs.** Read this side's branch diff (solr-18010-submit at c3685bb37d9d against its merge-base) and the other PR's diff (PR #5065's head, read through the GitHub API: the files endpoint and the diff itself). Compare and report:

- Mechanism: does each side fix the parallel-edit corruption the same way, or by different means? This side's fix, per its receipt, is an instance lock on edits plus a temp file with an atomic move. State the other side's mechanism in the same plain terms.
- File overlap: which files each side changes, and where the two change sets intersect.
- Test coverage: which tests each side adds or changes, and what each test actually drives. This side's receipt records SecurityConfHandlerTest 3 of 3 (including both concurrent-edit tests), BasicAuthStandaloneTest 1 of 1 and V2SecurityAPIMappingTest 5 of 5 at the gated head, with both new concurrent-edit tests failing against pre-fix production.
- Edge cases: any case one side handles and the other does not (for example forced interleaving of two writes, a failed or partial write, edits arriving through different entry points, or behaviour when the file is missing). Ground each claim in the diff or the tests, not in the PR descriptions.

**(2) The other PR's review state.** From the GitHub API: reviews and their states, requested changes and whether they have been answered, review comments that raise substantive points, CI state on its current head, and the date of its last activity (push, review or comment). Report what a maintainer has asked for, if anything, and whether the PR looks active, stalled, or close to merging.

**(3) This side's standing.** Restate from `receipts/SOLR-18010.md` what the gated branch proves: the gate counts above, the pre-fix proof (both concurrent-edit tests fail on pre-fix production with a lost assignment and a hybrid document), and the settling run recorded there (the real persistConf driven from two threads corrupting 5 of 5 forced interleaves on base). Note anything in the receipt that bears on the comparison, including that the earlier marker-only head did not fix the corruption and the live tip is the real fix.

## Recommendation

End the report with one recommendation, in exactly one of these three shapes:

- **Support theirs.** Their PR adequately covers the defect, so this side should not open its own. Name this side's branch as a retire item for Nick's deletion call, and state what, if anything, their PR is missing that this side would simply let go.
- **Open ours.** Their PR has a gap that this side's branch covers, or is stalled in a way that justifies a competing PR. Name the differentiation plainly: the specific mechanism, test or edge case this side's PR would lead with.
- **Combine.** Their PR is the vehicle, and this side has specific elements worth offering to it. Name each element (a test, an edge case, a mechanism detail) and include a draft comment text offering them, written for Nick to approve before anything is posted. The round drafts the comment; it does not post it, and nothing in the report should read as if contact has been made.

Whichever shape is chosen, give the evidence for it from parts (1) to (3), including where the two implementations are equivalent and the choice rests on review state or timing rather than on code.

## Rules

- Read-only throughout. No branch edits, no gate run, no GitHub posts or comments, no Jira writes.
- Code claims check against the two diffs themselves; review-state claims against the API responses read this round, with dates.
- If the other PR's state has changed since the overlap check (merged, closed, new head), report the current state and adjust the recommendation to it.

## Deliverable

`reports/solr-18010-overlap-comparison-round-1.md`: the part (1) comparison first (mechanism, file overlap, test coverage, edge cases), then part (2) review state, then part (3) standing, and the recommendation last, in one of the three shapes above. If the shape is combine, the draft comment text sits inside the recommendation section, clearly marked as a draft for Nick's approval.
