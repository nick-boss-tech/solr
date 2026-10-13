# SOLR-18539 PR draft text, round 1: findings from both reviews

Verdict: READY for Nick's review. Opening still needs Nick's approval. No PR, comment, JIRA write or email was made.

Assignment: `assignments/pool-mergeids-pr-draft-text-round-1.md` (`dafb9fea6b6`). Claim: `claims/pool-mergeids-pr-draft-text-round-1.md` (`8badc0b4fe0`). Draft: `pr-drafts/search-components/mergeids-skipped-shard-totals.md`.

Inputs: `reports/mergeids-pr-short-review-round-1.md` (SR) and `reports/mergeids-packaged-pr-review-round-1.md` (PR). Branch `mergeids-skipped-shard-totals` at packaged head `4eb996121804`, receipt `receipts/mergeids-fix.md`, and the base `d76fc14b9857` (the current tip of upstream/main). Two read-only subagents: A checked the branch code, the test lines and the receipt counts; B checked the live JIRA text of SOLR-18539 and that SOLR-18541 exists. Nothing was built, run or gated.

## Findings and dispositions

Where SR and PR raise the same item, it is applied once and the overlap is noted.

### Required in SR

- **SR-1, the Choice must cover all four values.** Applied as prescribed. The bold line and question are the reviewer's text. Overlap with PR "Disagreement: the Choice section": PR wants a numFound-only Choice ("Was leaving them out the right call?"). Rebutted for that wording. A's diff check shows the change alters maxScore, approximateTotalHits and numFoundExact too (`QueryComponent.java`, base to head: the totals block moves below the skip). A numFound-only Choice would leave three changed values unexplained.
- **SR-2, the CombinedQueryComponent sentence.** Applied with a change. The ordering sentence is the reviewer's text. The follow-up offer is dropped. I added a pointer to SOLR-18541 in its place. B confirmed that SOLR-18541 exists, Open, titled "CombinedQueryComponent.mergeIds gives wrong numFound, approximateTotalHits and numFoundExact". Because the ticket already exists, the Limits line can name it and no new ticket is implied. **For Nick:** strike the link if you want the line without it.
- **SR-3, delete the split offer.** Applied as prescribed. Overlap with PR-6 (add a split bullet): rebutted. The fallback is the second defect this PR fixes (head `L1188-L1193` changes it), and B confirmed JIRA SOLR-18539 says "part of the same fix". It is not a limit.

### Wording in SR

- **SR-W1, Choice after Proof.** Applied. The section order now matches `pr-formula.md`.
- **SR-W2, Limits bold summary.** Applied: "**Not covered: multi-node runs and CombinedQueryComponent.**" Overlap with PR-7 (the "Two named limits" heading). Applied once.
- **SR-W3, cut repeated claims.** Applied. Links kept, sentences cut.
- **SR-W4, split the symptom sentence.** Applied: "approximateTotalHits was 11, not 4." Overlap with PR-3 (numFoundExact in the symptom): already present, kept.
- **SR-W5, "headerless".** Applied with the reviewer's wording.
- **SR-W6, the all-skipped zero.** Applied. A verified `L1238-L1243`: the code clears `hitCountIsExact` when no shard merged and a skipped shard reported hits. It writes no zero. The zero comes from skipped shards adding nothing, and the text says so.
- **SR-W7, unchanged flags.** Applied with a code link. Overlap with PR-4, which says JIRA states that `partialResults` and `maxHitsTerminatedEarly` fold in from every shard. **Rebutted for that premise.** B found JIRA SOLR-18539 does not name either flag. The sentence stays because the code supports it. A verified the folds at `L1144` and `L1156-L1161`, both before the skip at `L1180`, and the diff does not touch them.
- **SR-W8, the trigger sentence.** Applied. A verified it (HOLDS): a shard is skipped when its partial header is true, it sent no sort values, and the sort uses a field other than score or doc (`SortSpec.java` L94-100). "Timed-out" is defined in Limits.
- **SR-W9, name the test and shard sizes.** Applied. "In `mergedNumFoundCountsOnlyMergedShards`, the skipped shard holds 2 documents and the other shard holds 3." Overlap with the test-name point in PR "Recommended".
- **SR-W10, the Proof sentence.** Applied with a change. The reviewer's text says the seven failures fail "with the values under What happens today". A verified that only five of the seven are described there. The mixed-header failure (13 instead of 14) and the all-skipped failure (5 instead of 0) are not. The Proof now names both.
- **SR-W11, delete "The two heads differ only in the changelog file."** Applied. A verified that `9e85ce5c16` to `4eb996121804` changes only the changelog file (+10 lines). The 9e85 Proof line is kept.
- **SR-W12, ordering words.** Applied. JIRA says "sorted on a field" (B verified). The draft now uses that phrase and "tests for the partial-results skip".
- **SR-W13, base commit once.** Applied. The symptom links are introduced as pointing at upstream/main, `d76fc14b9857`. I checked that this is the current upstream/main tip.

### Open items in SR

- **SR-O1, the verified date.** Applied. The Proof shows both MDT and UTC: "2026-10-12 20:51 MDT (2026-10-13 02:51 UTC)". The 9e85 line shows "2026-10-11 15:43 MDT (21:43 UTC)". Times from the receipt, timestamps `2026-10-13T02:51:05Z` and `2026-10-11T21:43Z`.
- **SR-O2, JIRA's reasons for rejecting alternatives.** Applied. B found both reasons in the JIRA text. The exact-flag-only reason is already the Choice's "would leave the counts wrong". The subtraction reason is added as one sentence: "Subtracting the shard afterwards cannot work, because maxScore cannot be decremented."

### Items in PR

- **PR-1, code links at the packaged head.** Already applied in `197beb2f756`. Checked: every change-produced link uses `4eb996121804`, and the symptom links use `d76fc14b9857`. No change.
- **PR-2, the Proof date and head.** Applied. Overlap with SR-O1.
- **PR-3, numFoundExact in the symptom.** Already present. No change.
- **PR-4, `partialResults` and `maxHitsTerminatedEarly`.** Applied as the wording in SR-W7. The premise that JIRA says so is rebutted (see SR-W7).
- **PR-5, "including the four added during review".** Already removed. No change.
- **PR-6, add a split bullet for the fallback.** Rebutted (see SR-3).
- **PR-7, Limits bold summary.** Applied (see SR-W2).
- **PR-8, length.** Applied in the short version. The visible text is 3,553 characters (5,123 bytes with URLs), about 50 over the formula's guide of about 3,500. The guide allows more for an unusually complex ticket, and the reviews asked for test names, shard sizes and dates in both zones, so I kept them. **For Nick:** this is a judgment call, not a blocker.
- **PR "Disagreement: the Choice section".** Rebutted for the numFound-only wording (see SR-1). The choice follows the four-value text.
- **PR "Recommended": sentences over 35 words.** Applied through the rewrite. No sentence is over 35 words now. The longest is the Choice question, at 33 words.
- **PR "Recommended": "pin" and "behaviour".** Not present in the short draft (grep). No change.
- **PR "Recommended": named cases "(twice ...)" and "13 instead of 14".** Not present in the short draft. The Proof now names the mixed-header and all-skipped cases in plain words.
- **PR "Recommended": "clean cherry-pick" and a bare commit link.** Not present. No change.
- **PR "Recommended": SOLR-15595 link.** Not present. No change.
- **PR "Recommended": changelog link text.** Already matches the template. No change.

## Confirmation pass

Re-read against `pr-formula.md`, with mechanical checks:

- Section order: header, Jira link, What happens today, What this change does, Proof, A choice to check, Limits, Changelog, AI assistance. Matches the template.
- Each section opens with a bold one-line summary. Yes, for all five body sections.
- Citations are blob links. Change-produced code links to `4eb996121804`: `L1144`, `L1156-L1161`, `L1180-L1203`, `L1188-L1193`, `L1238-L1243`, the test file, and the changelog line. Symptom links go to `d76fc14b9857`: `L1160-L1175` and `L1193-L1195`. The text says they point to upstream/main.
- Every line anchor was checked against the file at its SHA (`git show` on the source checkout).
- No em or en dashes in the draft.
- No process words in the PR text. "Drafting" appears only in the required footer.
- No internal references: no claim, probe, harness, round, receipt, gate, vm, PR number or local path in the PR text.
- Counts match the receipt: 17 of 17 at both heads, and the seven named failures.

## Branch items for the main side

None. No finding needed a branch change. The packaged head's gate stands on the receipt.

## Changes this round

- `pr-drafts/search-components/mergeids-skipped-shard-totals.md`: the text changes above. The staging notes above the first `---` gained one line about this round.
- `claims/pool-mergeids-pr-draft-text-round-1.md`: marked DONE.
- This report.

Not touched: the receipt, the reviews, the assignment, any other draft, the branch, and any submit branch.

## Not done

- No PR, comment, JIRA write, email or gate.
- No build, test or Gradle run.
- The live tip moved while this round ran (`8d389ea9ab1`, the SOLR-10667 lane). None of its files overlap this round's.
