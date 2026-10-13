# SOLR-18539 short PR text: review, round 1

Verdict: NOT READY. Three required changes, and a set of wording changes. The draft was not edited in this round.

Basis: the PR text in `pr-drafts/search-components/mergeids-skipped-shard-totals.md` (after the first "---"), the receipt `receipts/mergeids-fix.md`, the fork tip (`4eb996121804`), the JIRA text of SOLR-18539, and the code at `4eb996121804` and at the base `d76fc14b9857`. Two read-only subagents: A checked shape, length, plain language and leakage; B checked link anchors, counts, dates, behavior, JIRA fit and the Choice. Claim: `claims/pool-mergeids-pr-short-review-round-1.md`.

## What passes

- Every link path exists at its commit, and each cited line range holds the code it describes.
- Every count matches the receipt, at the head it was recorded at: 17 of 17 at `4eb996121804` (2026-10-13 02:51 UTC) and at `9e85ce5c16` (2026-10-11), and 7 failing with the listed values at both heads.
- The behavior the text describes matches the code: the skip runs before the fold, the fallback adds the shard's own count, and the all-skipped rule.
- The header, the footer and the section headings match the template. The text has no dashes and no process words, and "drafting" in the footer is required.

## Required changes

1. **The Choice must cover all four values.** Replace the Choice's bold line and question with: "**Merged numFound, maxScore, approximateTotalHits and numFoundExact now cover only the merged shards.** A skipped shard contributes to none of the four. Setting numFoundExact false alone would leave the counts wrong, so the change excludes the shard instead. Should numFoundExact also go false when a skipped shard reported hits, as the all-skipped zero rule does?" Reason: numFoundExact stays true when the merged shards are exact (test lines 93 to 94 and 187 to 188), so a count that excludes a skipped shard still reads as exact.
2. **The CombinedQueryComponent sentence.** Replace "`CombinedQueryComponent.mergeIds` has the same defects. It is not changed here. A follow-up ticket and PR can be opened on request." with "`CombinedQueryComponent.mergeIds` has the same ordering: it adds totals and the running-total fallback before its skip. It is not changed here." Reason: "the same defects" does not say which, and the offer is not authorized.
3. **Delete the split offer.** Delete "It can be split into a separate PR on request." The JIRA text says the fallback is part of the same fix, so it is not a limit.

## Wording changes

- **Shape.** Put the Choice after the Proof, as the template orders them. Give the Limits heading a bold one-line summary, for example "**Not covered: multi-node runs, and the same ordering in CombinedQueryComponent.**"
- **Repeated claims.** Cut the sentence "Skipped shards are excluded before the totals are added." and the sentence "The fallback adds `docs.getNumFound()` for the current shard." Keep their links. The bold line already says both.
- **Symptom.** Split "A test on unmodified main gave 3 documents with numFound 5, numFoundExact true, maxScore 99.0 from the skipped shard, and approximateTotalHits 11 instead of 4." into short sentences. Say "approximateTotalHits was 11, not 4."
- **"Headerless."** Replace "Two headerless shards of 2 and 3 documents give 2 instead of 5." with "Two shards with no approximateTotalHits header, holding 2 and 3 documents, give 2 instead of 5."
- **All-skipped zero.** Replace with "If every shard is skipped, the merged total is 0. That 0 is marked exact only when no skipped shard reported hits."
- **Unchanged flags.** Replace with "partialResults and maxHitsTerminatedEarly still combine values from every shard. The per-shard numbers in shards.info are unchanged."
- **The trigger.** Add one plain sentence saying what makes a shard skipped: a partial response with no sort values, when the sort needs its values unmarshalled. "Timed-out" first appears in Limits, so define it there.
- **Test inputs.** Name the test and the shard sizes where the counts are first given, so the reader can find the case.
- **Proof.** Replace "With `QueryComponent.java` reverted to the base commit and the new tests kept, 7 of the 17 fail with the values above." with "At `4eb996121804`, with `QueryComponent.java` reverted to the base commit and the new tests kept, 7 of the 17 fail with the values under What happens today. The same 7 failed at `9e85ce5c16`."
- **Dropped sentence.** Delete "The two heads differ only in the changelog file." A maintainer does not need the history of the superseded head. Keep the 9e85 Proof line, since it is where the earlier count was recorded.
- **Ordering.** Replace "checks for the partial-results skip" with "tests for the partial-results skip," and say "sorted on a field" rather than "sorted distributed search," to match the JIRA text.
- **Base commit.** Say once that `d76fc14b9857` is upstream/main, since the symptom links point at it.

## Length

With link URLs the PR text is about 3,880 characters, and about 2,580 visible characters without them. The formula's guide is about 3,500 characters for the whole description. The visible text is within the guide. Whether links count is Nick's call; the text is short either way.

## Open items

- The "verified" date of 2026-10-13 is in UTC. The environment date is 2026-10-12, so the reader should see both, or only UTC. The gate record is outside this review.
- JIRA's reasons for rejecting subtraction and an exact-flag-only fix are missing from the text. Add them if they fit in one sentence.

## Verdict

NOT READY until the three required changes are made. The text should be re-reviewed after the edit. Nothing is opened, posted or merged.

## Not done

- No edit to the draft, no PR, no comment, no JIRA write, no email.
- No build, test or gate.
