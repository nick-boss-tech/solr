# Review of the opened update PRs, group 1 (review-opened-28-g1)

Group 1: SOLR-3657 (PR 5069), SOLR-4841 (PR 5070), SOLR-5065 (PR 5071), SOLR-5505 (PR 5072).

## SOLR-3657 (PR 5069)

Verdict: FIX FIRST

Findings:

1. PR title does not match the changelog title (check 3). Current PR title: `SOLR-3657: error message only refers to "source" field when problem parsing value for "dest" field of copyField`. Fragment `changelog/unreleased/SOLR-3657-copyfield-error-names-destination.yml`, line 2 at head 14edaca577c0: "The error for a value that cannot be indexed into a copyField destination now names the destination field as well as the source field." Corrected PR title: `SOLR-3657: The error for a value that cannot be indexed into a copyField destination now names the destination field as well as the source field.`

2. Body, `## Limits` (draft `pr-drafts/update-processing/SOLR-3657.md` line 29, same in the PR body). The section does not open with a bold one-line summary (presentation rule). Current line 31 starts "- The vector message names the destination twice". Corrected: add a blank line and this bold line directly under `## Limits`: `**Two small gaps are left as they are.**`

Checks passed: head 14edaca577c0 matches the receipt's gated head; headRefName solr-3657-submit; baseRefName main; body equals the draft; proof counts (DocumentBuilderTest 17/17, TolerantUpdateProcessorTest 11/11, 2 pre-fix failures, 2026-10-07 at 14edaca577c0) match the receipt; blob anchors DocumentBuilder.java L286-L297 (outer message wrapper), L390-L406 (copyField wrapper), DocumentBuilderTest.java L429-L451 and L454-L482 hold the code described; the diff against merge base 97d973814336 touches only DocumentBuilder.java, DocumentBuilderTest.java and the fragment; the fragment parses as YAML with type changed; no em dash, no first-person plural wording.

## SOLR-4841 (PR 5070)

Verdict: FIX FIRST

Findings:

1. PR title does not match the changelog title (check 3). Current PR title: `SOLR-4841: DetectedLanguage constructor should be public`. Fragment `changelog/unreleased/SOLR-4841-detectedlanguage-public-constructor.yml`, line 2 at head e4c878627108: "The DetectedLanguage constructor in the langid module is now public, so custom LanguageIdentifierUpdateProcessor subclasses can return results." Corrected PR title: `SOLR-4841: The DetectedLanguage constructor in the langid module is now public, so custom LanguageIdentifierUpdateProcessor subclasses can return results.`

2. Body, "What happens today", first paragraph (draft `pr-drafts/update-processing/SOLR-4841.md` line 9). The link `DetectedLanguage.java#L24` at head e4c878627108 shows `public DetectedLanguage(String lang, Double certainty) {`, so its anchor contradicts "has a package-private constructor". Base line 24 reads `DetectedLanguage(String lang, Double certainty) {`. Corrected: in that first sentence only, replace `blob/e4c878627108ee2347cb1d5ee915e3e953c4249f/solr/modules/langid/src/java/org/apache/solr/update/processor/DetectedLanguage.java#L24` with `blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/modules/langid/src/java/org/apache/solr/update/processor/DetectedLanguage.java#L24` (the merge base with upstream main). The head link stays in "What this change does", where L24 is correct. This is the one link that cannot follow the head-SHA rule, because the head line shows the fixed code.

3. Body, Proof (draft line 22). Current: "with production reverted to base, the new test fails." The receipt records a compile failure. Corrected: "with production reverted to base, the new test fails to compile because the constructor is not public."

4. Body, Limits (draft line 44). Current: "Its one assertion checks the two getters." The test has two assertEquals calls (CustomLanguageIdentifierTest.java L46-L47 at head). Corrected: "Its two asserts check the two getters."

Checks passed: head e4c878627108 matches the receipt's gated head; headRefName solr-4841-submit; baseRefName main; body equals the draft; proof counts (CustomLanguageIdentifierTest 1/1, LangDetect 13/13, OpenNLP 13/13, SolrInputDocumentReaderTest 4/4, 2026-10-07 at f8850ffd421) match the receipt; the delta f8850ffd421 to e4c878627108 is the changelog type line only, as the body says; anchors LanguageIdentifierUpdateProcessor.java L345, LangDetect L40, OpenNLP L41 and CustomLanguageIdentifierTest.java L29-L48 hold the code described; the diff touches only DetectedLanguage.java, the new test and the fragment; the fragment parses as YAML with type added; the choice section is a real decision (public API is a cost a maintainer could reject); no em dash, no first-person plural wording. Non-blocking: the body is 3,796 characters, above the roughly 3,500 guide.

## SOLR-5065 (PR 5071)

Verdict: FIX FIRST

Findings:

1. PR title does not match the changelog title (check 3). Current PR title: `SOLR-5065: ParseDoubleFieldUpdateProcessorFactory is unable to parse "+" in exponent`. Fragment `changelog/unreleased/SOLR-5065-parse-double-exponent-plus.yml`, line 2 at head ab894a996c8: "ParseDoubleFieldUpdateProcessorFactory and ParseFloatFieldUpdateProcessorFactory now accept a plus sign in the exponent and a lowercase exponent marker (for example 4.5E+10 or 4.5e3)." Corrected PR title: `SOLR-5065: ParseDoubleFieldUpdateProcessorFactory and ParseFloatFieldUpdateProcessorFactory now accept a plus sign in the exponent and a lowercase exponent marker (for example 4.5E+10 or 4.5e3).`

2. Body, Limits (draft line 33). The locale gap is named, but it has no follow-up offer. Current end of the first bullet: "A locale-aware parse would be a separate choice." Corrected: "A locale-aware parse would be a separate choice. I can open a follow-up for it on request."

Checks passed: head ab894a996c8 matches the receipt's gated head; headRefName solr-5065-submit; baseRefName main, and the body's base 0cc328310f8 is the merge base; body equals the draft; proof counts (ParsingFieldUpdateProcessorsTest 44/44, DefaultSchemaSuggesterTest 2/2, module check, 2026-10-09 at ab894a996c8) match the receipt; anchors ParseNumericFieldUpdateProcessorFactory.java L49-L65, ParseDoubleFieldUpdateProcessorFactory.java L102-L116 and L104, ParseFloatFieldUpdateProcessorFactory.java L93, DefaultSchemaSuggester.java L294-L300, ParsingFieldUpdateProcessorsTest.java L642-L719, and DefaultSchemaSuggesterTest.java L31-L49 and L33-L41 hold the code described; the Limits claims hold (no new test uses a non-ROOT locale or spaces around the value); the diff touches only three processor sources, two tests and the fragment, with no docs; the fragment parses as YAML with type fixed; no em dash, no first-person plural wording. Not a finding: the Proof says the suggester pin test fails on base, but the receipt says only "the new test fails on unmodified base 0cc328310f8" without naming it, so the lead may confirm the test name against the gate log. Non-blocking: the body is 3,845 characters, above the roughly 3,500 guide.

## SOLR-5505 (PR 5072)

Verdict: OWNER CALL

Owner call: the change adds a `[core]` segment to every infoStream line for every user, with no opt-in. The body says "No setting keeps the old format" (draft line 19) and has no Choices section. Option (a): ship the always-on segment, keep the changelog type `changed` (dev-docs/changelog.adoc defines it as not opt-in), and add a choice section asking maintainers whether this format change is acceptable. Option (b): add an opt-in setting that keeps the old format before submitting, which would make the type `added`, as that doc defines it for opt-in changes.

Also required, not a decision: the PR title does not match the changelog title (check 3). Current PR title: `SOLR-5505: LoggingInfoStream not usabe in a multi-core setup` (typo "usabe"). Fragment `changelog/unreleased/SOLR-5505-infostream-core-name.yml`, line 1 at head 44c444aa5cd: "LoggingInfoStream (IndexWriter infoStream logging) now includes the core name in every message so multi-core logs can be told apart". Corrected PR title: `SOLR-5505: LoggingInfoStream (IndexWriter infoStream logging) now includes the core name in every message so multi-core logs can be told apart`.

Checks passed: head 44c444aa5cd3 matches the receipt's gated head; headRefName solr-5505-submit; baseRefName main; body equals the draft; proof counts (TestInfoStreamLogging 2/2 with one pre-fix failure, TestSolrIndexConfig 2/2, 2026-10-07) match the receipt; anchors SolrIndexConfig.java L185-L188 and L261-L265, LoggingInfoStream.java L38-L40, L46 and L49, and TestInfoStreamLogging.java L40-L47 hold the code described; the reload claim holds (toIndexWriterConfig builds a new IndexWriterConfig per call, SolrIndexConfig.java L237-L239); the diff touches only LoggingInfoStream.java, SolrIndexConfig.java, TestInfoStreamLogging.java and the fragment; the fragment parses as YAML (the `nick` key is documented in dev-docs/changelog.adoc); no em dash, no first-person plural wording.
