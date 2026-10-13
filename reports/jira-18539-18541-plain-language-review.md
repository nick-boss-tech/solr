# SOLR-18539, SOLR-18540, SOLR-18541: plain-language and internal-reference review

Verdict: none of the three texts is ready as written. Each needs a plain-language pass, and each has internal references that a reader outside the project cannot follow. SOLR-18541 needs the most work: it has 75 sentences, 27 of them over 40 words, and about 44 parenthetical evidence labels. The rest of this report gives the findings per ticket, the cross-ticket patterns, and the points where the reviews disagree.

Basis: the three JIRA texts, fetched read-only into the scratchpad. The standard is the simple-language rule in `pr-formula.md`, and the public-text rule in `WORKFLOW.md` (standing limits). Three read-only subagents reviewed the texts, one per ticket. Nothing was edited in JIRA. Claim: `claims/pool-jira-18539-18541-plain-language-review.md`.

## SOLR-18541 (the CombinedQueryComponent ticket)

- **Title.** 23 words, jargon, and it omits the third defect. Suggested: "CombinedQueryComponent.mergeIds gives wrong numFound, approximateTotalHits and numFoundExact".
- **Sentences.** 27 of 75 are over 40 words. The longest, in the Reproduction-to-Mechanism section, is over 130 words and carries five or more ideas. Split each long sentence at its semicolons and labels.
- **Jargon.** "fold" (24 uses), "shape" (10), "constructed", "headerless", "operand", "pins", "rides with". Suggested swaps: "fold" becomes "add into", "shape" becomes "case", "constructed" becomes "hand-built", "headerless" becomes "without the header", "pins" becomes "shows".
- **Internal references.** The case tags (P, P2, F, X1, X2, M1, M2) mean nothing outside this text. "The probe", "scratch probe harness", "export", "draft", "claim", and a dangling reference to a "QueryComponent fix" that the text never describes. "From a grep" appears twice. Suggested: replace the case tags with plain names such as "partial-skip case" and "all-exact case", and say "a test class, CombinedQueryComponentMergeTotalsTest" in place of "scratch probe harness".
- **Evidence labels.** About 44 parentheticals. Most of them are status notes, such as "(from code reading, not run)" on line references. Suggested: state once, in Reproduction, that a test run on unmodified main confirms the listed cases and that everything else comes from reading the code. Then delete the per-sentence labels, keeping line numbers. Make each run result a sentence: "The run gave 7 instead of 3."
- **Structure.** Move Reproduction before Mechanism, so the failure comes before the code walk. The timeAllowed analysis appears in Symptom and again in "End to end"; keep it once. Give the open question its own heading after Fix options. Reproduction is not actionable as written, because it names a scratch class that is not attached. Attach the test, or list the inputs as steps.
- **Header.** The file has the AI footer but no header. Decide whether the header belongs on this Jira text; `pr-formula.md` section 6 requires it on PRs.
- **Markup.** Some Mechanism text has stray braces and backslashes from the draft. Check the rendering after the text is pasted.

## SOLR-18539 (the sibling defect in QueryComponent)

- **Sentences.** 18 plain-language items, the worst at 56 words in the Symptom opening and 90 words in the reproduction paragraph. Split them. Define NPE once. Say what SOLR-15595 narrowed.
- **Labels.** Five. The one at the header line reads "(11 instead of 4 in the reproduction below)". Make it a sentence: "The header value is 11, where it would be 4 without the skipped shard."
- **Internal references.** The word "main", written as if it were a branch name, becomes "the main branch of apache/solr". The reproduction paragraph has a local mock setup described as a "mock harness". "Harness" should become "mock setup".
- **Planning text.** The closing paragraph says the three defects are "left as a follow-up, as one ticket covering all three of its defects", and that reads as a planned new ticket. The standing limits allow a new ticket only when a maintainer asks or Nick directs. Name the defects in plain words, and check with Nick before keeping any follow-up sentence.
- **Option 2 cross-reference.** The text says option 2's move applies "to the no-shard-merged case only". Option 2 sets the flag to false and moves no code, so the reference is wrong. Suggested: "When no shard is merged and a skipped shard reported hits, set hitCountIsExact to false."

## SOLR-18540 (the gradlew fix under Git Bash)

- **Sentences.** Nine plain-language items. The worst is the 50-word "On main at 498a09aea97" paragraph, and the 43-word branch sentence. Split both into a plain sequence of steps, and keep every number.
- **Internal references.** The local commit `498a09aea97` and the branch `gradlew-msys-double-conversion` exist only in the fork, not on apache/solr. Remove the branch name and the hash. The text should describe the patch in words. "Probes" and "the run confirms them" are process language. Local paths such as `C:/Users/...` should become `C:/.../`.
- **Labels.** Four status lines: "Confirmed by run", "Stated plainly, the scope of the run", "The SOLR-16641 validation still passes", and "the cygpath probes ... the run confirms them". Keep the scope of what was run as one plain list: "Only gradlew -version was run. Not run: gradlew check, gradlew build, any test task, gradlew.bat, Linux, and macOS."
- **Terms.** Define cygpath and MSYS once. "The cited snapshot" is undefined: say that line numbers refer to gradlew at `d76fc14b9857`.
- **Footer.** The AI footer uses "review" and "drafting". The reviewer judged that acceptable for a disclosure, and it matches the approved wording. Keep it.

## Patterns across the three

- Process vocabulary (claim, probe, harness, draft, export, round) appears in 18541, and a little in 18539 and 18540. Remove all of it from public text.
- Local branch names, commit hashes and paths appear in 18540, and the CQC ticket uses local paths less visibly.
- The status labels in 18541 are the largest single problem. A reader cannot tell which numbers were run and which came from reading the code, and the labels make that harder, not clearer.
- The AI header is present in 18539 and absent from 18541. Pick one form for all three.

## Where the reviews disagree

- **Labels in 18541.** One reviewer wants the per-sentence labels removed and one statement added at the top of Reproduction. That matches the rule that the reader needs to know what was run, so I recommend it. It does not match the earlier review rounds, which required labels in the draft text. The JIRA text is a different artifact from the draft, so the change is defensible. Nick should confirm it.
- **The sibling sentence.** SOLR-18539 names the three defects in its closing paragraph. SOLR-18541 says nothing about them. Keep the defect names in whichever ticket describes them, and link them, rather than writing about the other ticket's status.

## Recommended order

1. SOLR-18541 first, since it is the ticket behind your question. Apply the title, the label statement, and the case-name swaps. Then split the long sentences.
2. SOLR-18539: remove the planning text and the "main branch" wording, and split the long sentences.
3. SOLR-18540: remove the branch and hash references and the status lines, and split the 50-word paragraph.

## Not done

- No edit in JIRA. No comment, no email, nothing posted.
- No build, test, or gate.
