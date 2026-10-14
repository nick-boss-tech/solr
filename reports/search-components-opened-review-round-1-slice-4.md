# Search components opened review, round 1, slice 4 (PRs 5142 to 5145)

- Assignment: `assignments/pool-search-components-opened-review-round-1.md`. Claim: `claims/search-components-opened-review-round-1.md`.
- Reviewer: review agent, Windows host. All four PRs stay draft and open; no head moved. No comments, pushes, code commits, ready flips or Jira edits. No builds or tests.
- The formula is `pr-formula.md` at the branch root. Merge-bases come from `git merge-base` against upstream main, and each agreed with a read-only compare call: 8020 `22a8cfeb`, 8051 and 8088 `cabedd1d968`, 8240 `e432df19`.

## PR #5142 (SOLR-8020), head `79f790523d2f`. SATISFIED after 1 edit.

- Diff is scoped (4 files). The one-condition change in `SearchHandler` matches the body. The receipt (5 of 5, 2026-10-05) and the base run's failure text match the body.
- Edit: the symptom link text "[base code]" became "[pre-change code at the merge-base]". The URL is unchanged.

## PR #5143 (SOLR-8051), head `e50a2aa3437c`. SATISFIED after 1 edit, with a non-blocking history finding.

- Diff is scoped (3 files). The test has no assertion, as the body says. The receipt (1 of 1 at head, re-gate 2026-10-09) matches.
- Checked the SOLR-15319 claim against the local branch `4bda91f46fc1`: it still calls `getException()` before the null read, so it does not fix that path. The Limits states "SOLR-15319 lands after this change" in plain words.
- Edit: "[test lines 28-36]" with `#L28-L36` became "[test lines 30-36]" with `#L30-L36`. Line 28 is the class line; the method is 30 to 36.
- Gate job filed (non-blocking): `gates/SOLR-8051-review-history.md`. Commits `44588ce6719` and `55b24f64a8bd` name a "hypothetical-reproduction handoff doc" in public commit messages. The net diff is clean. A rewrite is a force-push, so it needs Nick's direction for this PR.

## PR #5144 (SOLR-8088), head `567efa9b78c3`. BLOCKED. Move: gate lane (changelog), then the main side directs the head move.

- Diff is scoped (4 files). `checkGroupable` skips numeric fields, and the 400 text matches the code. The receipt (2 of 2 with one failure) matches. The base error wording ("IllegalStateException about docValues") comes from the code comment and changelog, not the receipt, and the body does not present it as receipt-backed.
- Finding: the title, the bold summaries and the changelog say "a multiValued field". The code and the body's own Limits exclude numeric multiValued fields.
- Edits (live):
  1. Title: "Grouping on a multiValued field is rejected up front with a 400 error" became "Grouping on a multiValued non-numeric field is rejected up front with a 400 error".
  2. Summary, "What happens": "Distributed grouping on a multiValued field is accepted" became "... on a multiValued non-numeric field is accepted".
  3. Summary, "What changes": "Grouping on a multiValued field is rejected up front" became "... on a multiValued non-numeric field is rejected up front".
- Gate jobs filed:
  - `gates/SOLR-8088-review-changelog-scope.md` (blocker). The proposed changelog title is "Distributed grouping on a multiValued non-numeric field now fails with a clear 400 instead of an IllegalStateException from Lucene docValues." Until it lands, the changelog overstates the change.
  - `gates/SOLR-8088-review-history.md` (non-blocking; the history rewrite needs Nick's direction). The same handoff-commit issue as 8051 (`2398c9bea08`, `567efa9b78c3`).

## PR #5145 (SOLR-8240), head `fad7a1dd8e23`. SATISFIED after 5 edits.

- Diff is scoped (4 files). The check at head L249 to L255 and the test at L192 to L214 match the body. The shipped test carries `df=_catch_all`, as the receipt records. The receipt counts (JsonLoaderTest 32 of 32, TestInitParams 7 of 7, 2026-10-05) match.
- Edits:
  1. Section order: Proof moved from after Limits to before "A choice to check", the template order. No text change.
  2. Limits summary: "and the reference guide page is not updated here" became "and the guide's Setting JSON Defaults section is not updated here".
  3. Limits, new last bullet: SOLR-10424 should land before, or together with, this change. The record gives this order (owner decision 1), and the body had none.
  4. "What this change does": "The reference guide documents the rule ..." became "This change adds one sentence to the reference guide's `mapUniqueKeyOnly` section ...", because the head adds that sentence and the old text implied prior documentation.
  5. The symptom link text "[mapping code at the base commit]" became "[pre-change code at the merge-base]".

## Record disagreements (flagged for the main side)

- The assignment says the slate notes that SOLR-15319 lands after 8051. The slate row has no such note. The order is in `assignments/pool-census-followup-search-components-drafts-round-1.md` (line 23) and the wording-round assignment (line 21). The 8051 body matches that record.
- The slate row for 8088 repeats the overbroad "multiValued field" wording.
- Owner item: the "planned follow-up pull request" sentence in 8240 conflicts between records. The later fidelity response (item 54) says no edit and no owner agreement under the 2026-10-11 ladder. The sentence is left as is. Nick to confirm.
- `reports/branch-review-verdicts-landing-round-1.md` (line 83) records 8240 as SATISFIED at `1ad4d52f55b`. That is a pr-prepare record commit, not the fork head `fad7a1dd8e23`. The verdict predates edits 1 to 5 and needs re-stamping.
