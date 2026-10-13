# SOLR-18539 PR draft: summary of the saved long version, and the short version

## The saved version

The long version is saved at `material/mergeids-skipped-shard-totals-v1-long.md` (7,503 bytes; its PR text is about 5,700 characters). It is also in git history on `pr-prepare`.

What it says, in order:

- **What happens today.** Three defects in one paragraph, with the base-commit line links, the test values, and the arrival-order arithmetic.
- **What this change does.** Three changes, each with line links at the packaged head, and a long list of behavior changes.
- **Proof.** 17 of 17 at two heads, the seven failing values, and a sentence for each of the ten passing pins.
- **A choice to check.** A question about splitting the fallback fix into its own PR. The reviewer found that this is a scope call, not a real choice.
- **Limits.** Two bullets: the merge-level test scope, and the same defects in `CombinedQueryComponent`.
- **Staging notes** above the PR text, which are not part of the PR.

Problems the reviews found: the code links and the Proof point at an older head (`9e85ce5c16`) instead of the packaged head (`4eb996121804`). The Proof has no UTC date. The JIRA symptoms `numFoundExact=true` and the `maxHitsTerminatedEarly` fold are missing. The text runs about 1.4 times the formula's guide. It has sentences of 40 to 60 words, phrases such as "including the four added during review", and a long Mechanism walk-through.

## The short version

The short version is now `pr-drafts/search-components/mergeids-skipped-shard-totals.md`. Its PR text is about 3,850 bytes with link URLs, and about 2,670 visible characters.

What changed:
- The code links and the Proof name the packaged head `4eb996121804`, with a UTC date. The `9e85ce5c16` head is kept in the Proof as the earlier gate.
- The symptoms now include `numFoundExact=true`, and the text says `maxHitsTerminatedEarly` still folds in from every shard, as the JIRA text does.
- The Mechanism walk-through is gone. Each section has one bold line and two or three short sentences.
- The Choice is one plain question: merged numFound now counts only merged shards, so was leaving skipped shards out the right call?
- The phrase "including the four added during review" is gone.
- Limits has three bullets: merge-level tests, the same defects in `CombinedQueryComponent` (follow-up on request), and the fallback split (on request).
- The title is shorter: "SOLR-18539: Skip partial shards before adding their totals in QueryComponent.mergeIds".

The AI header and footer, the Jira link and the changelog link are unchanged. The PR text has no dashes, and its only process word is "drafting" in the required footer.

## What is not changed

- The gate and the receipt. The packaged head is green in the record, and nothing here reruns it.
- The code. The short version describes the same change.

## Next

The short version needs one more review pass against the packaged head before it goes to Nick. The numbers and line links are the same as in the reviewed version, but the text has changed.
