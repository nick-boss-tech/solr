# Review confidence round 8: confirmation of the round 7 settlement

Counts: **3 settled bodies. 2 SATISFIED, 1 STILL OPEN.**

- SOLR-18178 (PR 4968): SATISFIED.
- SOLR-13568 (PR 5014): STILL OPEN (one item; the "rarely hit" ruling is FURTHER SOFTEN).
- SOLR-18505 (PR 5028): SATISFIED.

Read-only. Live bodies were read through the GitHub wrapper; fork tips were resolved with `git ls-remote` and match each API head (`b75e7d4d3c4`, `ac5d60c214c`, `e28739b4069d`). Run head SHAs were read with `gh run view`. Nothing was edited, posted, built or gated. Readiness is not judged.

The two round 6 decisions were applied: C1 (body dates are the author's local MDT dates; not re-flagged) and C2 (a fork run is cited "at this head" only when it executed that head; otherwise the body names the CI commit).

## SOLR-18178, PR 4968: SATISFIED

Head `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2`, open, not a draft.

- The sentence "passes 5 of 5 at head `b75e7d4d3c4` (run 2026-10-10)" is gone from the GitHub-runner bullet, which now ends "...which the Windows run above covers."
- The new bullet is present verbatim, immediately after the runner bullet: "- A separate run on 2026-10-10, not on the GitHub runner, passed `DownloadConfigSetAPITest` 5 of 5 at head `b75e7d4d3c4`, covering archives containing subdirectories."
- `receipts/SOLR-18178.md` line 15 records a full-class run at `b75e7d4d3c4` on vm1 (evidence restoration round 1): 5 tests, 0 failures. Line 14 says no GitHub-runner run of the class is on record. The venue and the count match.
- The head has 5 `@Test` methods in `DownloadConfigSetAPITest` (lines 67, 78, 87, 99 and 135), matching "5 of 5".
- This is not a fork-run citation, so C2 does not apply. The receipt ties the run to the head.

## SOLR-13568, PR 5014: STILL OPEN (one item)

Head `ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721`, open, not a draft.

Settled items, checked:
- "What happens today" reads verbatim: "...so paging through expanded results fills the filter cache with entries that no other page can reuse." "Rarely reused" is gone.
- "A choice to check" reads verbatim: "Always-off is the simpler rule. Moving to another page never reuses these entries, because each page's group query is built from that page's own groups. Only a repeat of the same page could hit, and this change does not measure how often that happens. If maintainers would rather..." "The cached entries are rarely hit" is gone.
- The Limits wording "...paid in exchange for the filter cache no longer filling with per-page group queries while paging." is accepted: it states the measured premise (the base test expects 3 and gets 5 while paging, `receipts/SOLR-13568.md` line 7) and no rate. `ExpandComponent.java` lines 439 to 441 wrap the page group query with `setCache(false)`, and `SolrIndexSearcher` routes uncached filters around the filter cache (lines 1244 to 1251 and 1561 to 1563).

**Ruling on "rarely hit": FURTHER SOFTEN.**

1. No unmeasured frequency word remains in the body. "Rarely" appears nowhere. The remaining frequency wording, "how often that happens" and "a recurring cost that has not been measured", is flagged as unmeasured. The receipt has no hit, hit-rate or eviction record.

2. **Scope defect, needs the further soften.** "Only a repeat of the same page could hit" is contradicted by the code. At the head the group filter is a `TermInSetQuery` over the page's group values (`ExpandComponent.java` lines 840 to 897; the ordinal path builds it from `ordBytes` at 888 to 897). `TermInSetQuery` equality is over the term set, so any later request whose page holds the same groups builds an equal filter, and on the base cache it would hit. The query, sort or filter that produced the page does not matter. The receipt is silent on scope, so the code decides it.
   - **Exact replacement**, in "A choice to check": replace "Only a repeat of the same page could hit, and this change does not measure how often that happens." with "Only a later request whose page holds the same groups could hit, and this change does not measure how often that happens."
   - The sentence before it ("Moving to another page never reuses these entries...") is accurate, because pages of one result set have disjoint groups. The switch sentence can stay as written.

3. Optional, not required for the ruling: "What happens today" says "entries that no other page can reuse." That is accurate within one paging sequence. To read it as scoped to the paged results, "entries that no other page of those results can reuse" would do.

4. Limits: "the same page of the same query, requested again by any user, ... could find the filter in the cache" is accurate as an example, and "it now recomputes the group filter every time" matches the uncached path. No change.

**Outside the body (for the main side, not a body item).** `ExpandComponent.java` line 437 at the head still reads "The groups depend on the page, so this filter is rarely reused; keep it out of the filter cache rather than filling the cache with entries of little value." The string last changed in commit `6b89f649071` on this branch, so the PR diff carries the unmeasured claim that the body dropped. Fixing it needs a new commit, which moves the head and needs the gate again, so the main side decides. Suggested replacement in the comment: "so no other page can reuse it" in place of "so this filter is rarely reused".

## SOLR-18505, PR 5028: SATISFIED

Head `e28739b4069d`, open, not a draft.

- The settled item is present verbatim in the "Update 2026-10-06" paragraph: "...a GitHub Actions run of the same class on the fork also passed on this code (run 37577283523, CI commit `71a978d5982`, which adds only the workflow file)." "At the same head" is gone.
- Run `37577283523` (`gh run view`): conclusion success, head SHA `71a978d598278002b6f752cd88455733b5e66e9e`. The body's `71a978d5982` is its 11-character prefix. Match.
- CI commit `71a978d598278002b6f752cd88455733b5e66e9e`: parent `e28739b4069d`, subject "ci: 18505-duht-r31", adds only `.github/workflows/fork-test-runner.yml` (48 insertions). The diff from the CI commit to the head removes only that file.
- `receipts/SOLR-18505.md` line 12 names CI commit `71a978d5982` as the executed commit and no longer says "succeeded at head e28739b4069d". Match.
- The diff from the gated parent `8ca33200e37` to the head is a comment block in `testExpungeDeletes` only (5 insertions, 15 deletions), so "comment-only" holds. The head test has the `FilterMergePolicy` wrapper (line 497) and try-with-resources sample requests (lines 522 and 529). Base `22a8cfebbbdb` line 494 is the first-sample assertion, as cited.
- Not itemised in the receipt: "tidy clean, Error Prone compile clean" at `e28739b`. The receipt cites only `DirectUpdateHandlerTest` 7/7 and "module check rc=0", plus gate log `g18505r30-gate.log`, which is not in the worktree and was not read. Not an open item; the main side may want those two checks named in the receipt.

## Method

- Live bodies read through `research\gh.ps1 pr view`; fork tips matched the API heads.
- Run head SHAs read with `gh run view`; CI commit contents with `git show --stat` and `git diff`.
- Round 7 and its settlement read in full for each body; each applied item checked as present and each replaced text checked as gone.
