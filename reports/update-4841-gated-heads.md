# SOLR-4841 draft review at bda9f9d640d9: counts and links hold, but the protected-constructor argument is wrong

Result: the draft's counts, dates, heads, links, and changelog line match their sources, but the choice section wrongly rules out the protected option, and seven FIX items need edits before the text is used.

Count: 7 FIX, 10 NOTE.

Scope: read-only review of `origin/pr-prepare` at `ce8edcfedf9`. Draft: `pr-drafts/update-processing/SOLR-4841.md`. Receipt: `receipts/SOLR-4841.md`. Line numbers for the draft and the receipt are on `origin/pr-prepare`. Code lines are at `bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082` unless marked base (`b6b2b8f10e9827e3b86e44649fb7966ce646c185`). No builds, tests, fetches, or public calls were made.

## Findings

### 1. FIX: the protected option is wrongly ruled out

- File: `pr-drafts/update-processing/SOLR-4841.md`, line 35 (choice section). Also line 38.
- Public text now: "- Make it `protected`. Only the same package and `DetectedLanguage` subclasses reach it, so a custom processor still cannot."
- Evidence:
  - bda9 `solr/modules/langid/src/java/org/apache/solr/update/processor/DetectedLanguage.java` line 20 reads `public class DetectedLanguage {`. The class is not final. Line 24 is the public constructor.
  - Java Language Specification 6.6.2.2: a protected constructor can be called by a subclass constructor with `super(...)`, and by an anonymous class creation `new C(...) {...}`, from any package. So a custom processor can return `new DetectedLanguage("sv", 0.75) {}`. This is my reading of the spec. I did not compile it.
  - Receipt line 8 shows only that a plain `new DetectedLanguage(...)` from another package fails under a protected constructor. It does not test a subclass.
  - The same wrong argument appears in `audits/update-processing/SOLR-4841.md` line 30.
  - Round-28 report, `material/last-reviews/4841-round28-pipeline-report.md` line 58, asks the right question: "rather than protected plus asking custom identifiers to subclass the bean?" The draft drops that question.
- Replacement for line 35: "- Make it `protected`. A custom processor can still build results, but only by subclassing `DetectedLanguage`, for example with `new DetectedLanguage("sv", 0.75) {}`. A protected constructor adds less public API."
- Replacement for line 38: "Was a public constructor the right call, rather than a protected constructor that custom identifiers reach by subclassing the bean?"

### 2. FIX: "Both in-tree subclasses" is not accurate

- File: draft line 9, "Both in-tree subclasses sit in the processor package".
- Evidence: `git grep` at bda9 finds three subclasses of `LanguageIdentifierUpdateProcessor`. LangDetect (line 40) and OpenNLP (line 41) are production code in package `org.apache.solr.update.processor`. The test's `FixedLanguageIdentifier` (`solr/modules/langid/src/test/org/apache/solr/update/processor/custom/CustomLanguageIdentifierTest.java` line 32) is in package `...processor.custom`, which is outside the package. Line 17 of the test file gives that package.
- Replacement: change `Both in-tree subclasses` to `Both production subclasses`. Keep both links. Optional: add `#L17` to the two links, since the sentence relies on the package line.

### 3. FIX: the garbled sentence and the "maintainer" claim

- File: draft line 26, first two sentences: "Two commits sit past the tested head. Both change only the changelog fragment's type, ending at changed at a maintainer's suggestion during review."
- Evidence:
  - `git log --oneline f8850ffd421..bda9f9d640d9` lists two commits: `e4c87862710` and `bda9f9d640d`. The count in the draft is correct.
  - The fragment's line 3 is `type: fixed` at f8850ffd421, `type: added` at e4c878627108, and `type: changed` at bda9f9d640d9.
  - `git diff --stat f8850ffd421 bda9f9d640d9` shows only the changelog file, with one line changed. No code or test file changed after the tested head.
  - The bda9 commit subject is "SOLR-4841: changelog type changed at review".
  - Receipt line 12 credits "Jan Hoy Dahl's review suggestion on the PR". No source calls that person a maintainer.
- Replacement for those two sentences: "Two commits sit past the tested head. Each changes one line in the changelog fragment: the `type` goes from `fixed` to `added`, then from `added` to `changed`. No code or test file changed after the tested head."

### 4. FIX: the second test run has no verification date

- File: draft line 26, the sentence that starts "At the new head `bda9f9d640d`".
- Evidence: `pr-formula.md` section 3 requires "the verification date and head." Receipt line 12: "Top-up GREEN 2026-10-09 at bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082". Receipt line 12 also gives the same four counts as the first run (1, 13, 13, 4), with 0 failures.
- Replacement for that sentence and the list that follows it: "Verified 2026-10-09 at head `bda9f9d640d9`: the same four test classes pass with the same counts, and the langid Error Prone compile passes."
- Note: this also removes "the changelog parses" and "focused classes". The parse check is a gate step, not a test outcome, and the formula says proof reports outcomes only. The owner may keep it if wanted.

### 5. FIX: one line per test class, and plain words

- File: draft line 21.
- Evidence: the four classes share one bullet. `pr-formula.md` section 3 asks for "One line per test class, counts inline, with the verification date and head." The counts match receipt line 7: CustomLanguageIdentifierTest 1/1, LangDetect 13/13, OpenNLP 13/13, SolrInputDocumentReaderTest 4/4. The draft also says "Neighbors" (jargon) and "The module checks pass." Receipt line 7 says only "module check rc=0". Do not put "rc=0" in public text.
- Replacement for line 21 (the bullet and its sentences):
  - Verified 2026-10-07 at head `f8850ffd421`:
  - `CustomLanguageIdentifierTest` 1/1
  - `LangDetectLanguageIdentifierUpdateProcessorFactoryTest` 13/13
  - `OpenNLPLangDetectUpdateProcessorFactoryTest` 13/13
  - `SolrInputDocumentReaderTest` 4/4
  - The module check passes.

### 6. FIX: the planned follow-up has no source

- File: draft line 44: "A follow-up submission is planned for a test that runs language detection through a custom identifier."
- Evidence: no file on `origin/pr-prepare` names a planned follow-up for SOLR-4841. A search for "follow-up" across the drafts and the SOLR-4841 files finds only this line. `pr-formula.md` asks for an offer "to open a follow-up ticket and PR for it on request."
- Replacement: "A test that runs language detection through a custom identifier would be a follow-up. A follow-up ticket and PR for that test can be opened on request."

### 7. FIX: the changelog line is not a link

- File: draft line 46.
- Evidence: `pr-formula.md` presentation rule: "link each file citation to the blob at the PR head SHA." The other file citations in the draft are links. This one is backticked text only.
- Replacement: "Changelog: [`changelog/unreleased/SOLR-4841-detectedlanguage-public-constructor.yml`](https://github.com/nick-boss-tech/solr/blob/bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082/changelog/unreleased/SOLR-4841-detectedlanguage-public-constructor.yml) (type: changed)."

### 8. NOTE: "Nothing else changes" is broader than the diff

- File: draft line 13 (summary): "**The `DetectedLanguage` constructor becomes public. Nothing else changes.**"
- Evidence: `git diff --stat b6b2b8f10e9827e3b86e44649fb7966ce646c185 bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082` shows three files. `DetectedLanguage.java` changes one line. The test (49 lines) and the changelog fragment (8 lines) are new. No other production code changes.
- Optional replacement: "**The `DetectedLanguage` constructor becomes public. No other production code changes.**"

### 9. NOTE: the proof summary names only the old head

- File: draft line 19: "**The tests pass at head `f8850ffd421`, and the new test does not compile on base.**"
- Evidence: receipt line 12 shows the four classes pass at `bda9f9d640d9` too.
- Replacement: "**The tests pass at heads `f8850ffd421` and `bda9f9d640d9`, and the new test does not compile on base.**"

### 10. NOTE: the proof body repeats the summary, and the protected run is under-described

- File: draft line 22: "- Without the fix, with production reverted to base, the new test does not compile, because the constructor is not public on base. A run with a protected constructor instead fails the same way."
- Evidence: the first clause repeats line 19. The second clause does not say what the probe covered. Receipt line 8 says only that the protected run "fails the same way."
- Replacement: "- Reverting production to base, the new test fails to compile. With a protected constructor, the test's direct `new DetectedLanguage(...)` also fails to compile."
- Optional: add the observed failure. The round-28 report line 20 quotes this compiler message: "DetectedLanguage(String,Double) is not public in DetectedLanguage; cannot be accessed from outside package". That report is not the receipt, and its gate log is not on disk. The owner should confirm the wording before using it.

### 11. NOTE: say plainly that the proof is a compile failure

- File: draft lines 19 and 22.
- Evidence: `pr-formula.md` section 3 says to say so plainly when the proof is inconclusive by construction, such as a new API. This change widens an access level, so the base failure is a compile error, not a test failure.
- Optional sentence to add at the end of line 22: "This is a compile error, because the change only widens an access level."

### 12. NOTE: the limits paragraph misses the unused custom identifier

- File: draft lines 42 to 44.
- Evidence: the test compiles `FixedLanguageIdentifier` (test lines 32 to 42). Nothing calls its `detectLanguage` method. The test body (lines 45 to 47) only calls the two getters.
- Optional sentence to add after "It does not run language detection.": "The test also defines a custom identifier, `FixedLanguageIdentifier`. It compiles, but nothing calls its `detectLanguage` method."

### 13. NOTE: the base link in section 1

- File: draft line 9, the `DetectedLanguage.java` link.
- Evidence: the link points to base `b6b2b8f10e9827e3b86e44649fb7966ce646c185` line 24, which reads `DetectedLanguage(String lang, Double certainty) {` with no modifier. That is the right code for a base fact. `pr-formula.md` says to link to the PR head SHA, so this is a judgment call.
- Optional: keep the base link, and say so in the text: "On the base, `DetectedLanguage` has a package-private constructor ([DetectedLanguage.java](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/modules/langid/src/java/org/apache/solr/update/processor/DetectedLanguage.java#L24))."

### 14. NOTE: changelog type `changed` is a judgment call

- File: draft line 46 and fragment line 3.
- Evidence: `dev-docs/changelog.adoc` at bda9, lines 67 to 70, describes `changed` as "For improvements; not opt-in." with "Modifying behavior or performance of existing requests/configuration." Lines 71 to 72 describe `fixed` as "For improvements that are deemed to have fixed buggy behavior." The ticket is a Bug (round-28 report line 5). Receipt line 12 says the type changed at a review suggestion.
- The draft, the fragment, and the receipt agree on `changed`. No text change is needed if the owner keeps it.

### 15. NOTE: the draft is over the length guide

- File: whole draft.
- Evidence: 4,263 characters (4,267 bytes), counted with `wc -m` under UTF-8. `pr-formula.md` gives a guide of about 3,500 characters.
- The Limits paragraph (lines 42 to 44) and the paragraph after the proof (line 26) are the best places to cut after the FIX edits. I did not write a trimmed version.

### 16. NOTE: the receipt header is stale (owner action, not draft text)

- File: `receipts/SOLR-4841.md` line 4: "- Gated head: e4c878627108 (the live tip; verified at this head, see below)".
- Evidence: receipt line 9 says the gate ran green on f8850ffd421 and calls e4c878627108 "the one commit on top". Receipt line 12 names bda9f9d640d9 for the top-up. Git shows two commits past f8850ffd421. The draft's head f8850ffd421 agrees with receipt lines 5 and 9 and with git. Only the header disagrees.
- Suggested line 4: "- Gated head: f8850ffd421 (gate, 2026-10-07). Top-up head: bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082 (2026-10-09)."

### 17. NOTE: internal audit notes repeat the wrong argument (owner action, not public text)

- File: `audits/update-processing/SOLR-4841.md`, line 30 (protected argument, see Finding 1). Line 34 says `type: fixed`, which was true at f8850ffd421 and is stale at bda9.
- Fix these before anyone reuses the audit.

## Task results

**Task 1, draft against receipt.** The draft matches the receipt on every count: CustomLanguageIdentifierTest 1/1, LangDetect 13/13, OpenNLP 13/13, and SolrInputDocumentReaderTest 4/4 (receipt line 7). The first date and head match the receipt body: gate 2026-10-07 (receipt line 5) and a green gate at f8850ffd421 (receipt line 9). The receipt header (line 4) still names e4c878627108. That is a receipt error (Finding 16). The draft has no seed, and it should not, because the receipt seed (line 12) is internal. "Two commits sit past the tested head" is correct. `git log f8850ffd421..bda9f9d640d9` lists two commits: `e4c878627108` (fixed to added) and `bda9f9d640d9` (added to changed). Receipt line 9 names one commit on top, but it was written before bda9 existed, and line 12 covers bda9. The second run has no date (Finding 4). Limits contains no counts.

**Task 2, code citations at bda9.** All six links point to the right path. Five use `bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082`. The DetectedLanguage link in section 1 uses base `b6b2b8f10e9827e3b86e44649fb7966ce646c185`, which is correct for that sentence. Both SHAs are local (`git cat-file -e`). I did not fetch anything. Lines shown: base DetectedLanguage.java line 24 is package-private. bda9 LanguageIdentifierUpdateProcessor.java line 345 is `protected abstract List<DetectedLanguage> detectLanguage(Reader solrDocReader);`. LangDetect line 40 and OpenNLP line 41 are class declarations. bda9 DetectedLanguage.java line 24 is public. Test lines 29 to 48 cover the class header through the test method. Base-to-bda9 diff for the cited files: only DetectedLanguage.java line 24 changed. LanguageIdentifierUpdateProcessor, LangDetect, and OpenNLP have no diff. The test file is new. The class-line anchors on LangDetect and OpenNLP do not show the package line (Finding 2).

**Task 3, changelog.** The fragment at bda9 is `changelog/unreleased/SOLR-4841-detectedlanguage-public-constructor.yml`. That matches the draft line 46. Line 3 is `type: changed`, which matches the draft. Lines 4 and 5 list `- name: Nick Shanin`. A search for "ICLA pending", "Solr Issues Workspace", and "Claude" found nothing in the file. By reading (no parser run), the YAML is well formed: a comment line, a plain title with no ": " inside it, a flat `type`, an `authors` list, and a `links` list with `name` and `url`. `changed` is in the allowed list (`dev-docs/changelog.adoc` line 46). All four branch commits have Nick Shanin as author and committer, and no commit message has a Co-Authored-By, Claude, or Signed-off-by line. The type question is Finding 14.

**Task 4, formula, wording, and vocabulary.** The AI header (line 1) and Jira link (line 3) match the template. The five sections (lines 5, 11, 17, 28, 40) each open with a bold one-line summary. The choice section is justified only after Finding 1, because the protected option is a live alternative once corrected. The pointed question stands. Limits names the gaps. The changelog line is not linked (Finding 7). The footer (lines 48 to 50) matches the template. Length is 4,263 characters (Finding 15). The draft has none of these internal terms: gate, receipt, top-up, seed, log, takeover, rc=0, JUnit XML, "pre-fix proof", or an internal log file name. Plain-language misses are "Neighbors", "focused classes", and "module checks" (Findings 4 and 5). The draft has no em dash. Section 1 names the cause (a package-private constructor) and no fix, which the symptom rule allows. Line 22 repeats line 19 (Finding 10). The garbled sentence is Finding 3.

## Not checked

- No builds, javac, Gradle, or tests. The protected and anonymous-subclass result (Finding 1) comes from reading JLS 6.6.2.2 and the class declaration. A compile probe would confirm it, and it needs the owner's approval.
- The gate logs (`g4841-gate.log`, `g4841-changedtype-topup.log`, `g4841-protected-build.log`) are not on disk. Proof numbers come from the receipt only.
- Live state was not checked: `solr-4841-submit` on the remote, PR #5070 title, body, head, and CI. No `gh` or network calls were made. The round-28 review report says the live PR title did not match the changelog title at e4c878627108 (`reports/review-opened-28-g1.md` line 23). I did not verify that live.
- The JIRA thread was not checked. The round-28 report says Uwe Schindler suggested a protected constructor (`material/last-reviews/4841-round28-pipeline-report.md` lines 5 and 58). No JIRA calls were made.
- Whether `b6b2b8f10e9827e3b86e44649fb7966ce646c185` is the upstream/main base was not checked, because no fetch was allowed. I checked only that it is local and that it is the parent of `a0dc26bc4484`.
- The receipt says "module check rc=0" and does not say what that check runs. I did not infer its contents.
- The 3,796-character figure in `reports/review-opened-28-g1.md` line 31 uses a method I do not know. My figure is for the draft as it stands on `origin/pr-prepare`.
- Whether `changed` or `fixed` is the right changelog type is the owner's call (Finding 14).
