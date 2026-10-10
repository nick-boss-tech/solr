# SOLR-4841 and SOLR-6973 drafts at their gated heads: round roll-up

Claim: `claims/update-4841-6973-gated-heads.md` (commit `d27be70f3f2`). Upstream commit checked: `ce8edcfedf9` ("SOLR-6973 and SOLR-4841 drafts and receipts at their new gated heads").

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two read-only subagents reviewed the two drafts and receipts. Their full reports are `reports/update-4841-gated-heads.md` and `reports/update-6973-gated-heads.md`. No build, Gradle run, or test was run. No draft, receipt, submit branch, live PR, or comment was touched.

Live heads checked with `git ls-remote` and a read-only fetch on 2026-10-09. Both match the heads the new drafts name:
- `solr-4841-submit` at `bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082`
- `solr-6973-submit` at `51fcd05e695ae1a5983ed69a3d2afc50e7f7bc1e`

The gate logs named in the receipts are not on disk. The receipts are the only source for Proof numbers here.

## SOLR-4841: 7 FIX, 10 NOTE

The counts, dates, links, and changelog line match their sources. The changelog type is `changed` in the draft, the fragment, and the receipt. The author is Nick Shanin. The problem is in the wording.

Owner decisions and verified findings:

1. **FIX, verified: the choice section rules out the wrong option.** The draft says a protected constructor "still cannot" help a custom processor. `DetectedLanguage` is `public` and not `final` at `bda9f9d640d9`, so a custom processor can reach a protected constructor with an anonymous subclass, `new DetectedLanguage("sv", 0.75) {}`. Java Language Specification 6.6.2.2 allows that. The reviewer read the spec and did not compile it. A compile probe would confirm it, and it needs your approval. The pointed question still stands, but its alternative must be reworded.
2. **FIX: "Both in-tree subclasses" is wrong.** The test's `FixedLanguageIdentifier` is a third subclass, in a package outside the processor package. Use "Both production subclasses".
3. **FIX: the sentence "ending at changed at a maintainer's suggestion during review" is garbled.** It also calls a reviewer a maintainer, which no source supports. Replace with: "Each changes one line in the changelog fragment: the `type` goes from `fixed` to `added`, then from `added` to `changed`. No code or test file changed after the tested head."
4. **FIX: the second test run has no verification date.** Add "Verified 2026-10-09 at head `bda9f9d640d9`." The formula asks for the date and head.
5. **FIX: the four test classes share one bullet.** The formula asks for one line per class, with counts inline. The draft also uses "Neighbors" and "module checks", which are not plain words.
6. **FIX: the planned follow-up has no source.** No file names it. Replace with the formula's offer: a follow-up ticket and PR "can be opened on request".
7. **FIX: the changelog line is not a link.** The formula asks for file citations to be links.
8. **NOTE: the draft is 4,263 characters.** The guide is about 3,500. The Limits paragraph and the paragraph after the proof are the places to cut.
9. **NOTE: the receipt header is stale.** It names `e4c878627108` as the gated head. The receipt's own lines name `f8850ffd421` (gate) and `bda9f9d640d9` (top-up). This is an owner action on the receipt, not draft text.
10. **NOTE: `audits/update-processing/SOLR-4841.md` repeats the wrong protected argument** (line 30). Fix before anyone reuses the audit.

## SOLR-6973: 6 FIX, 5 NOTE

The head SHA, the cited test lines, the changelog file, and the footer check out. The drafted behavior change is too narrow, and the proof wording claims a base run the receipt does not give.

Owner decisions and findings:

1. **FIX, most important: the behavior change covers existing documents too.** The draft says the change affects "a partial update that creates a new document". The reviewer read the code. A partial update with no signature fields used to set the stored `signatureField` to the empty signature on an existing document. Its `updateTerm` then deleted other documents that carry that term. The new code skips both steps. I spot-checked the three code lines behind this: `getUpdatedDocument` merges atomic updates at `DistributedUpdateProcessor.java` L723-L724, and normal fields are treated as a "set" at `AtomicUpdateDocumentMerger.java` L195. The chain order rests on the javadoc at `UpdateRequestProcessorChain.java` L104-L105. The reviewer read this from code and did not run it. The owner should confirm the full merge path before the text is used.
2. **FIX: the re-verify sentence claims a base count the receipt does not give.** The only base count (7 tests, 1 failure) is from the earlier head `fa5b59ba07b4`. The receipt gives only "PASS" at `51fcd05e695`.
3. **FIX: "The core module checks also pass" names a module the receipt never names.** Delete the sentence. A build check is not a test outcome.
4. **FIX: the bold summary drops the condition the body needs.** The bug needs a `fields` list. Add "and a `fields` list" to the summary.
5. **FIX: the loop claim in "What happens today" has no link that shows it.** The head moved the lines. Link the base lines for the loop, `cabedd1d968` L169-L178. The formula asks for head links; head no longer has that code. The owner decides which SHA to use.
6. **FIX: Limits says only new documents were checked.** The new test checks processor output only. It does not index documents.
7. **NOTE: the review-fix sentence narrates a review round inside Proof.** Option A is to delete it. Option B is to keep the edge as a behavior note: a partial update with no signature fields no longer loads the signature class. The draft names no review bot, and should not.
8. **NOTE: "A follow-up submission is planned" commits more than the source supports.** The audit says the branch cannot settle the shard-routing question. Offer a follow-up on request instead.
9. **NOTE: "The ticket comments suggest shard routing" is stronger than the audit.** The audit says the ticket is not conclusive. The reviewer did not read the Jira ticket.
10. **NOTE: three public commit messages use internal words** (`04eb4ef911f`, `4c6092614e5`, `fa5b59ba07b`). Changing them needs a history rewrite and a force push, which the fork rules do not allow without your direction.
11. **NOTE: the draft is about 3,517 characters** in UTF-8 (the `wc -m` figure is 3,523). The guide is about 3,500. Option A of finding 7 removes about 115 characters.

## Owner decisions that are still open

- SOLR-4841: reword the choice section for the protected option (finding 1). Confirm the compile probe, if you want it.
- SOLR-4841 and SOLR-6973: apply the factual fixes (SOLR-4841 findings 2 to 7; SOLR-6973 findings 2 to 4 and 6). These change public drafts, so they need your go-ahead before any edit.
- SOLR-6973: confirm the existing-document behavior (finding 1). This is the one that changes what the PR claims.
- SOLR-6973: pick Option A or B for the review-fix sentence.
- Both receipts: fix the stale header lines.
- SOLR-6973 commit messages: decide whether to rewrite before a PR is opened.

## Not done

No draft or receipt was edited. No submit branch, PR, live PR body, comment, or JIRA item was touched. No builds, Gradle runs, or tests. The Jira ticket for SOLR-6973 and SOLR-4841 was not read. Whether either PR is open was not checked.
