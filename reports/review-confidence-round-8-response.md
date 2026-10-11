# Response: review confidence round 8 settlement (main side, 2026-10-11)

Round 8 (`reports/review-confidence-round-8.md`) returned 2 SATISFIED and 1 STILL OPEN over the three bodies the round 7 settlement applied. The two SATISFIED bodies (SOLR-18178, PR #4968; SOLR-18505, PR #5028) needed no action. The one open item, SOLR-13568 (PR #5014), is settled here. The body edit was applied through the GitHub API on 2026-10-11 and verified by comparing the returned body to the intended text (line endings and trailing whitespace normalized). No comment was posted, the PR's state and draft status are unchanged (open, not a draft), and its head is unchanged at `ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721`. No branch was edited.

## SOLR-13568, PR #5014: APPLIED (the "rarely hit" ruling lands as FURTHER SOFTEN)

Round 8 confirmed every round 7 item as landed, accepted the Limits sentence the round 7 settlement reworded (below), and ruled FURTHER SOFTEN on one scope defect: the sentence "Only a repeat of the same page could hit" understates the case. At the head the group filter is a `TermInSetQuery` over the page's group values (`ExpandComponent.java` lines 840 to 897), and `TermInSetQuery` equality is over the term set, so any later request whose page holds the same groups builds an equal filter and would hit on the base cache. `receipts/SOLR-13568.md` records the premise run (filter cache size expected:<3> but was:<5>) and carries no hit, hit-rate or eviction record and nothing on scope, so the code decides it, as round 8 held.

- Applied verbatim, in "A choice to check": "Only a repeat of the same page could hit, and this change does not measure how often that happens." is now "Only a later request whose page holds the same groups could hit, and this change does not measure how often that happens."
- PATCH verified: the returned body matches the intended text, the new sentence appears once, and the old sentence appears nowhere.

## Round 8 qualifiers, honored as written

- The sentence before the replacement ("Moving to another page never reuses these entries, because each page's group query is built from that page's own groups.") stays as written; round 8 confirms it accurate, since pages of one result set have disjoint groups. The switch sentence that follows is likewise unchanged.
- Limits judgment: round 8 accepts the sentence the round 7 settlement reworded in main-side wording, "...paid in exchange for the filter cache no longer filling with per-page group queries while paging", as stating the measured premise with no rate, and accepts the Limits example ("the same page of the same query, requested again by any user") as accurate. No Limits text was changed in this settlement.
- Round 8 item 3 (a tighter scoping of the "What happens today" sentence, "entries that no other page of those results can reuse") was marked optional and not required for the ruling. It is not applied. Round 9 confirms the body as it stands, so the point stays visible there.

## Flagged by round 8, not settled here (for the main side)

Round 8 notes, outside the body: `ExpandComponent.java` line 437 at the head still reads "The groups depend on the page, so this filter is rarely reused; keep it out of the filter cache rather than filling the cache with entries of little value." The string last changed in commit `6b89f649071` on this branch, so the PR diff carries the unmeasured claim the body dropped. Round 8's suggested comment replacement is "so no other page can reuse it" in place of "so this filter is rarely reused". Fixing it needs a new commit, which moves the head and requires the gate again, so it is a main-side decision and is not made in this lane. No branch edit was made.

Round 8 also observed, as not an open item, that `receipts/SOLR-18505.md` names only the focused counts and the module check for head `e28739b4069d`, not the tidy and Error Prone checks the body mentions. Noted; the receipt is left as it stands.

## What round 9 confirms

`assignments/pool-review-confidence-round-9.md` is posted with this settlement: a confirmation pass over the SOLR-13568 body only. On SATISFIED, the 19-body set that rounds 4 to 9 covered is fully settled.
