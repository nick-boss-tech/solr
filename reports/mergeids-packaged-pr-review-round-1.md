# SOLR-18539 PR draft at the packaged head: review, round 1

Verdict: NOT READY as written. The branch is green at the packaged head `4eb996121804`, but the PR text names an older head for its code and Proof, leaves out two JIRA symptoms, and runs about 5,700 characters against the formula's guide of about 3,500. None of these needs a new gate. They need edits to the PR text, and the draft was not edited in this round.

Basis: `pr-drafts/search-components/mergeids-skipped-shard-totals.md`, PR text (below the first "---" line), the receipt `receipts/mergeids-fix.md`, the fork tip (`4eb996121804`, checked with `ls-remote`), the JIRA text of SOLR-18539, and the code at the packaged head and at the merge-base `d76fc14b9857`. Claim: `claims/pool-mergeids-packaged-pr-review-round-1.md`. Two read-only subagents: A checked language, formula and leakage; B checked heads, numbers, date and JIRA fit.

## Checks that pass

- Heads: the fork tip and the changelog link name `4eb996121804`. The base links (`d76fc14b9857`) are the merge-base, and their line anchors match.
- Numbers: every count matches the receipt's Step 3 and Step 4 at `9e85ce5c16`. The seven premise values (3 vs 5, 3.0 vs 99.0, 4 vs 11, 5 vs 2, 14 vs 13, 0 vs 5, and the pair) are quoted correctly, and 7 fail plus 10 pass makes 17.
- Changes between heads: `9e85ce5c16` to `4eb996121804` changes only the changelog file, so code citations at either head name the same lines.
- No staging item (receipt, gate, claim, job, vm1, vm2, takeover, round, probe, scratch file), no local path, no status label, and no em or en dash appears in the PR text.
- Header, Jira link, section headings and footer match the approved template.

## Required changes

1. **Code links and Proof link.** Replace `9e85ce5c162086ae806f617419c4405512792dde` with `4eb996121804b043f1ee53f5bb7e22b9c3021b0a` in "What this change does" and in the Proof's test-file link. Keep the anchors.
2. **Proof date and head.** Replace the Proof's bold summary with: "**QueryComponentPartialResultsTest: 17 of 17 pass at head `9e85ce5c16`, verified 2026-10-11; 17 of 17 pass at the packaged head `4eb996121804`, verified 2026-10-12 20:51 MDT (2026-10-13 02:51 UTC).**" The bare date "2026-10-13" reads as a future date.
3. **Symptom gap.** JIRA says the merged count is also reported with numFoundExact=true. Add to the Symptom: "The merged response shows 3 documents with numFound 5 and numFoundExact=true, a maxScore of 99.0, and so on."
4. **Behavior gap.** JIRA says partialResults and maxHitsTerminatedEarly still fold in from every shard. Replace "The partialResults flag and the per-shard numbers in shards.info are unchanged." with "The partialResults and maxHitsTerminatedEarly flags still fold in from every shard. The per-shard numbers in shards.info are unchanged."
5. **"Four added during review."** Replace "including the four added during review" with "including four pin tests". The phrase reads as internal review history.
6. **Adjacent fallback.** Add a Limits bullet: "The fallback fix (one line, with its own tests) can be split into a separate PR, on request." This is the adjacent-work offer the formula requires, and it replaces the narrow-scope Choice (see below).
7. **Limits heading.** Replace "**Two named limits.**" with a plain summary such as "**The tests stop at the merge step, and a sibling method has related defects.**"
8. **Length.** About 5,700 characters against the guide of about 3,500. Trim the CombinedQueryComponent Limits bullet and the two long Symptom sentences. The ticket is complex, but the three-defect fix is not so complex that it needs more than the guide.

## Disagreement: the Choice section

- Subagent A says to remove the Choice. Splitting the fallback into its own PR is a narrow-scope call, which the formula excludes on a straightforward patch.
- Subagent B says to keep a Choice, but to rewrite it around dropping skipped shards' totals versus clearing numFoundExact only.
- Both agree that the choice is not the narrow-scope question. The live alternative is the behavior change itself: merged numFound stops counting skipped shards. A's suggested text fits that: "Merged numFound now leaves out skipped shards. Keeping them in was the other option. Was leaving them out the right call?" The head comment at L1238 to L1240 says the query did match those documents, which supports the answer. Recommendation: use A's wording and drop B's.

## Recommended, not required

- Plain language: five sentences in the PR text are over 35 words. A lists them at lines 16, 18 (twice), and 30. Split them.
- "Pin" and "behaviour": use "check" and "behavior" to match the rest of the text.
- Named cases: "(twice, including the exact-flag case)" and "mixed headers 13 instead of 14" name cases a reader cannot follow. Say what each test checks.
- Git jargon: "a clean cherry-pick" should be plain words. The bare commit `d211794596b` should link to its commit page.
- SOLR-15595 should be linked where it first appears.
- Changelog link text should read `changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml`, as in the template.

## Not done

- No edit to the draft or the receipt. No PR, comment, JIRA write, or email.
- No build, test, or gate.
