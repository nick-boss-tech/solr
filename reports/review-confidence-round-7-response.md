# Response: review confidence round 7 settlement (main side, 2026-10-11)

Round 7 (`reports/review-confidence-round-7.md`) returned 12 SATISFIED and 3 STILL OPEN over the 15 bodies settled from round 6. All three open items are settled here. Every body edit below was applied through the GitHub API on 2026-10-11 and verified by comparing the returned body to the intended text (line endings and trailing whitespace normalized). No comment was posted, no draft state changed (all three PRs were open and not drafts before and after), and no branch was edited; each PR's head is unchanged. Each replacement was checked against its receipt before applying; none contradicted the record, so all three were applied as specified, with one wording point on SOLR-13568 flagged below for round 8.

## SOLR-18178, PR #4968: APPLIED

Verified against `receipts/SOLR-18178.md`: its closing record states the 2026-10-10 run of `DownloadConfigSetAPITest` as a full class (5 tests, 0 failures, head `b75e7d4d3c4`) was the evidence restoration run on vm1, not a GitHub runner run, which is exactly what the replacement says.

- The sentence "`DownloadConfigSetAPITest` passes 5 of 5 at head b75e7d4d3c4 (run 2026-10-10), covering archives containing subdirectories." is removed from the GitHub Actions runner bullet in Proof.
- It now stands as its own bullet immediately after that bullet: "- A separate run on 2026-10-10, not on the GitHub runner, passed `DownloadConfigSetAPITest` 5 of 5 at head `b75e7d4d3c4`, covering archives containing subdirectories."
- PATCH verified; PR open, not a draft, head `b75e7d4d3c46` unchanged.

## SOLR-13568, PR #5014: APPLIED (the "rarely hit" ruling lands as SOFTEN)

Verified against `receipts/SOLR-13568.md`: the receipt records counts, the premise run (filter cache size expected:<3> but was:<5>) and a Limit stating the per-page cost was not benchmarked; it carries no hit count, hit rate or eviction record, so no frequency claim has record support. Both of round 7's specified replacements were applied verbatim:

- "What happens today": "...so paging through expanded results fills the filter cache with entries that are rarely reused." is now "...so paging through expanded results fills the filter cache with entries that no other page can reuse."
- "A choice to check": "Always-off is the simpler rule and the cached entries are rarely hit, but if maintainers would rather keep cache hits available for repeated identical page requests, the switch is the route to take." is now "Always-off is the simpler rule. Moving to another page never reuses these entries, because each page's group query is built from that page's own groups. Only a repeat of the same page could hit, and this change does not measure how often that happens. If maintainers would rather keep cache hits available for repeated identical page requests, the switch is the route to take."
- One further word changed under round 7's "the same care applies" note, in main-side wording rather than round-specified text: the Limits sentence "paid in exchange for the filter cache no longer churning while paging" is now "paid in exchange for the filter cache no longer filling with per-page group queries while paging". "Churning" was the same unmeasured-frequency shape as "rarely hit"; the replacement states only what the premise run measured (per-page group queries landing in the filter cache). Round 8 is asked to judge this sentence explicitly, since its exact words are the main side's.
- PATCH verified; PR open, not a draft, head `ac5d60c214cf` unchanged.

## SOLR-18505, PR #5028: APPLIED, and the receipt corrected

Verified against `receipts/SOLR-18505.md` and round 7's API reading: run 37577283523 executed CI commit `71a978d5982` (branch `ci/18505-duht-r31`), and the diff from `71a978d5982` to head `e28739b4069d` removes only `.github/workflows/fork-test-runner.yml`; the receipt itself already noted the CI tip is the head plus the runner workflow commit. The replacement follows the C2 convention decided in round 6.

- In the "Update 2026-10-06" paragraph in Proof, "a GitHub Actions run of the same class on the fork at the same head also passed." is now "a GitHub Actions run of the same class on the fork also passed on this code (run 37577283523, CI commit `71a978d5982`, which adds only the workflow file)."
- Receipt side, corrected in the same push: `receipts/SOLR-18505.md` no longer says the run "succeeded at head e28739b4069d"; it names CI commit `71a978d5982` as the executed commit, as round 7's receipt note asked.
- PATCH verified; PR open, not a draft, head `e28739b4069d` unchanged.

## What round 8 confirms

`assignments/pool-review-confidence-round-8.md` is posted with this settlement: a confirmation pass over these three bodies only, per-body SATISFIED or STILL OPEN, including an explicit final ruling on the SOLR-13568 "rarely hit" question as the body now stands. Round 7's remaining record notes (receipt wording on SOLR-18391, SOLR-18129, SOLR-16130, SOLR-13706, and the optional SOLR-18506 title sentence) were not in this lane's scope and are unchanged.
