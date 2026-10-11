# Review confidence round 7: confirmation of the round 6 settlement

Counts: **15 settled bodies. 12 SATISFIED, 3 STILL OPEN.** Slice A (7 bodies): 5 SATISFIED, 2 STILL OPEN. Slice B (8 bodies): 7 SATISFIED, 1 STILL OPEN.

Read-only. Live bodies were read through the GitHub wrapper on 2026-10-11; fork tips were resolved with `git ls-remote` and matched the API head SHA for all 15. Run head SHAs were read with `gh run view`. No body was edited, no comment posted, no branch or draft changed, no build, test or gate run.

The two decisions from round 6 were applied as decided: C1 (body dates are the author's local MDT dates; not re-flagged) and C2 (a fork run is cited "at this head" only when it executed that head; otherwise the body names the CI commit). The four bodies round 6 returned SATISFIED are out of scope and were not re-judged.

## Verdicts

| Body | PR | Verdict | Open items |
|---|---|---|---|
| SOLR-18178 | 4968 | STILL OPEN | 1 |
| SOLR-18391 (open) | 5027 | SATISFIED | 0 |
| SOLR-18129 | 5000 | SATISFIED | 0 |
| SOLR-16130 | 5004 | SATISFIED | 0 |
| SOLR-18482 | 5009 | SATISFIED | 0 |
| SOLR-12849 | 5011 | SATISFIED | 0 |
| SOLR-13568 | 5014 | STILL OPEN | 2 (one ruling) |
| SOLR-13706 | 5015 | SATISFIED | 0 |
| SOLR-13097 | 5016 | SATISFIED (fidelity only) | 0 |
| SOLR-18505 | 5028 | STILL OPEN | 1 |
| SOLR-18506 | 5029 | SATISFIED | 0 (optional note) |
| SOLR-18119 | 5061 | SATISFIED | 0 |
| SOLR-18523 | 5062 | SATISFIED | 0 |
| SOLR-18531 | 5101 | SATISFIED | 0 |
| SOLR-3704 | 5103 | SATISFIED | 0 |

Readiness is not judged anywhere. SOLR-13097 (5016) is CONFLICTING with changes requested, and SOLR-18505 (5028) and others have their own readiness states; a fidelity verdict says nothing about them.

## Open items

### SOLR-18178, PR 4968: STILL OPEN

Head `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2`. The round 6 items landed (the drive-letter bullet is gone; 1b closed by C1).

- **New item.** In the Proof bullet that begins "On the fork's GitHub Actions runner, 2026-10-04, at CI commits that are not the head:", the live text says `DownloadConfigSetAPITest` "passes 5 of 5 at head `b75e7d4d3c4` (run 2026-10-10), covering archives containing subdirectories." The record (`receipts/SOLR-18178.md` line 15) says that 2026-10-10 run was on vm1, under evidence restoration round 1, not on the GitHub runner. The sentence sits inside the GitHub-runner bullet and reads as one. Round 6 item 7a for SOLR-12849 had the same venue problem.
- **Replacement:** take the sentence out of that bullet and make it its own bullet: "- A separate run on 2026-10-10, not on the GitHub runner, passed `DownloadConfigSetAPITest` 5 of 5 at head `b75e7d4d3c4`, covering archives containing subdirectories."

### SOLR-13568, PR 5014: STILL OPEN (one ruling)

Head `ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721`. The round 6 changelog link landed. The C1 item is closed and not re-flagged.

**Ruling on "the cached entries are rarely hit": SOFTEN.** The mechanism holds at the head, but the frequency has no record. The receipt (`receipts/SOLR-13568.md`, all nine lines) has counts, the premise run, the round 29 test-only change and a Limit that says the cost was not benchmarked, but no hit count, hit rate or eviction record. A worktree search finds only earlier notes that flagged this same sentence as unmeasured. The code at the head (`ExpandComponent.java`, group query built from the page's own collapsed docs, about lines 300 to 345, added with `setCache(false)` at 439 to 441) supports "moving to another page never reuses it", not a frequency. The body's own Limits describes a popular first page requested again by any user, which can hit the cache, so "rarely hit" conflicts with the body.

- **Live, "What happens today":** "...so paging through expanded results fills the filter cache with entries that are rarely reused." **Replacement:** "...so paging through expanded results fills the filter cache with entries that no other page can reuse."
- **Live, "A choice to check":** "Always-off is the simpler rule and the cached entries are rarely hit, but if maintainers would rather keep cache hits available for repeated identical page requests, the switch is the route to take." **Replacement:** "Always-off is the simpler rule. Moving to another page never reuses these entries, because each page's group query is built from that page's own groups. Only a repeat of the same page could hit, and this change does not measure how often that happens. If maintainers would rather keep cache hits available for repeated identical page requests, the switch is the route to take."
- **Not counted but the same care applies:** the Limits sentence "the filter cache no longer churning while paging" is supported only by a cache-size check (expected 3, was 5), so "churning" is also unmeasured. Apply the same wording care when the ruling lands.

### SOLR-18505, PR 5028: STILL OPEN (one C2 citation)

Head `e28739b4069d`. Items 1 and 2 landed as specified.

- **Live, Update 2026-10-06 paragraph in Proof:** "a GitHub Actions run of the same class on the fork at the same head also passed." The API gives fork run `37577283523` a head SHA of `71a978d598278002b6f752cd88455733b5e66e9e` (branch `ci/18505-duht-r31`), not `e28739b`. `git diff 71a978d5982 e28739b` removes only `.github/workflows/fork-test-runner.yml`, so the executed commit is the head plus the workflow file. `receipts/SOLR-18505.md` line 12 names the same CI tip. Round 6 accepted the run as "at e28739b", which was wrong under C2.
- **Replacement:** "...DirectUpdateHandlerTest` passes 7/7 locally; a GitHub Actions run of the same class on the fork also passed on this code (run 37577283523, CI commit `71a978d5982`, which adds only the workflow file)."
- **Receipt side (not a body defect):** `receipts/SOLR-18505.md` line 12 says the run "succeeded at head e28739b4069d"; it should name CI commit `71a978d5982` as the executed commit.

## Satisfied bodies: items that remain as record notes

These need no body change. Each is a receipt wording or a flag for the main side.

- **SOLR-18391 (open):** `receipts/SOLR-18391.md` dates the tip check and the proof decision but not the focused gate run. The 2026-10-10 date rests on a takeover-log entry that is not in the worktree. Add the focused run's date to the receipt.
- **SOLR-18129:** `receipts/SOLR-18129.md` line 9 still reads "GitHub corroboration for TestUtils at this head: fork run 37569145964", the wording C2 bars. Name CI commit `19f7433f7a7`.
- **SOLR-16130:** `receipts/SOLR-16130.md` line 8 says run 37581056636 "succeeded at this head". Name CI commit `4c151ce1b79`.
- **SOLR-13706:** `receipts/SOLR-13706.md` line 8 says run 37568486924 "succeeded at this head". Replace with "succeeded on CI commit 8cb51cd86c5 (this head plus the workflow file) before the Actions suspension." The Limits sentence "no code reads the serialized children by name" has no receipt line; the receipt points back to the PR's own Limits, which is circular.
- **SOLR-18506 (optional):** the appended sentence "The commit title says race" is accurate for commit `3c098439882`, but the live PR title no longer uses "race". A reader could take "the commit title" to mean the PR title. Optional replacement: "Commit 3c098439882 on this branch is titled with the word race, though the PR title no longer uses it;" in place of "The commit title says race;".
- **SOLR-13097:** "the first listed name that has a governing permission" and "no serving core, unchanged" have no receipt line (round 6 recorded them as not verified). The probe artifacts are in the main side's goal files, not in this worktree, so the outcome claim rests on the receipt's statement.

## Method

- Live bodies read through `research\gh.ps1 pr view`. Fork tips matched the API head SHA for all 15.
- Run head SHAs read with `gh run view`; commit contents with `git show --stat` and `git diff`.
- Round 6 and its settlement read in full for each body; each applied item checked as present and the replaced text checked as gone.
- Re-pointed citations checked to land on the cited lines at the base commit with "(base commit)" in the link text; kept head citations checked at the head.
