# PR draft: test-only follow-up to SOLR-4841 (for Nick to approve; no PR exists yet)

Status: DRAFT. Not opened. Opening needs Nick's approval and the new ticket key. Replace SOLR-XXXXX in the title and body with that key.
Head: solr-4841-langid-followup-submit at bf1b01d0dc (gate green, receipts/SOLR-4841-followup.md). Base: apache/solr main c8ab71430f.
Changelog: none (test-only).

---

Title: SOLR-XXXXX: Test language detection through a custom identifier

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-XXXXX

## What happens today

**No test runs language detection through a custom identifier.**

The merged test in [CustomLanguageIdentifierTest.java](https://github.com/apache/solr/blob/c8ab71430f/solr/modules/langid/src/test/org/apache/solr/update/processor/custom/CustomLanguageIdentifierTest.java#L31) says in its own comment that compiling it is the main check. Its one method builds a `DetectedLanguage` and reads two getters. Nothing calls `detectLanguage` on a document, as [#5070](https://github.com/apache/solr/pull/5070) noted in its Limits.

## What this change does

**It adds a test that sends documents through a custom identifier in an update chain.**

The test starts an embedded Solr with a small test configset. The configset has a chain whose processor is a test factory, [ContentLanguageIdentifierFactory.java](https://github.com/nick-boss-tech/solr/blob/bf1b01d0dc0ecb0455a19c2cb3276f5a1318d437/solr/modules/langid/src/test/ContentLanguageIdentifierFactory.java#L46-L69). The factory's identifier is a subclass of `LanguageIdentifierUpdateProcessor`, defined outside the processor package. It returns a language based on the text it reads.

The test sends three documents. Two go through the chain and one goes through the default chain. It asserts the detected language on the indexed documents, as [CustomLanguageIdentifierProcessingTest.java](https://github.com/nick-boss-tech/solr/blob/bf1b01d0dc0ecb0455a19c2cb3276f5a1318d437/solr/modules/langid/src/test/CustomLanguageIdentifierProcessingTest.java#L55-L65) shows.

No production code changes.

## Proof

**The test does not compile before SOLR-4841, and it passes after.**

- Before SOLR-4841 (the merged constructor reverted by one line, same tree), `compileTestJava` fails with `DetectedLanguage(String,Double) is not public in DetectedLanguage`. The error is in the new test factory and in the merged test. The constructor before the change is [DetectedLanguage.java#L24 at 8e62c26868](https://github.com/nick-boss-tech/solr/blob/8e62c2686882aa704480ab13b6a60ee8f7b5c8af/solr/modules/langid/src/java/org/apache/solr/update/processor/DetectedLanguage.java#L24).
- At head bf1b01d0dc, 2026-10-18, JDK 21, seed 4841C0FFEE4841:
  - `CustomLanguageIdentifierProcessingTest`: tests=1, failures=0, errors=0, skipped=0.
  - `CustomLanguageIdentifierTest`: tests=1, failures=0, errors=0, skipped=0.
  - Tidy clean, Error Prone compile passes, `:solr:modules:langid:check -x test` passes.

The proof for this change is a compile result, not a failing run. The base cannot run the test, so a run on the base has nothing to report.

## A choice to check

**The test drives the update chain instead of calling the processor directly.**

- Implemented: an embedded Solr with a small configset. The test exercises the real `processAdd` path and the indexed document.
- Alternative: a unit test that builds the processor directly. That needs a request with an `IndexSchema`, which needs a core, so the test would be a mock-heavy setup in a module without a Mockito dependency.

Is the embedded setup worth its size for a test-only change, or should the test be lighter?

## Limits

**The test checks the wiring, not the quality of detection.**

- The identifier decides by the word "hej". The test shows that a detected value reaches the indexed document. It does not show that detection works.
- The map fields, fallback, threshold, and the built-in OpenNLP and LangDetect identifiers are not covered.
- Commit subjects carry SOLR-4841 until the new key is used. They are not rewritten.

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
