# SOLR-4841 language-detection follow-up, round 1 (test build, gate, self-review)

Assignment: assignments/pool-solr-4841-langid-followup-round-1.md. Claim: claims/pool-solr-4841-langid-followup-round-1.md (vm4). Gate job: gates/SOLR-4841-langid-followup.md. Receipt: receipts/SOLR-4841-followup.md.

## What was built

- Branch solr-4841-langid-followup-submit on the fork, head bf1b01d0dc, base apache/solr upstream main c8ab71430f.
- Test-only, four files in :solr:modules:langid:
  - solr/modules/langid/src/test/org/apache/solr/update/processor/custom/CustomLanguageIdentifierProcessingTest.java: runs documents through an update chain whose processor is a custom identifier defined outside the processor package.
  - solr/modules/langid/src/test/org/apache/solr/update/processor/custom/ContentLanguageIdentifierFactory.java: the factory, and a nested identifier that reads the document text and returns "sv" when the text contains "hej", "en" otherwise, with certainty 0.9.
  - solr/modules/langid/src/test-files/langid/solr/custom/conf/solrconfig-custom.xml and schema-custom.xml: a configset with the custom_langid chain.
- No production edits. No changelog fragment (test-only, per dev-docs/changelog.adoc).

## Design against the assignment's points

1. The run goes through processing: documents are sent with update.chain=custom_langid, the base class's processAdd calls the subclass's detectLanguage, and the assertion reads language_s from the indexed document. A document sent without the chain has no language_s.
2. Discriminating: two inputs produce two different language values ("sv" and "en").
3. Base rules: extends SolrTestCase and uses EmbeddedSolrServerTestRule. Does not extend the J4 harness and does not use Mockito (langid has no Mockito dependency, so none was needed).

## Pre-fix legs

- Parent state (the DetectedLanguage constructor not public, the merged change reverted by one line), compileTestJava: rc=1. Quoted: "DetectedLanguage(String,Double) is not public in DetectedLanguage", in ContentLanguageIdentifierFactory.java:67, and in the merged CustomLanguageIdentifierTest.java:40 and :45. Log: /workspace/gates/logs/vm4/SOLR-4841-followup-prefix-compile.log.
- No compiling leg exists on the parent, so no failure-content check applies. The test does not compile before the change, which is the expected outcome in the assignment.
- Note: apache main holds the merged change as a squash commit with a different hash from the fork's a99ff066f4. The pre-fix state here is the same tree with the one-line revert.

## Gate

Recorded in receipts/SOLR-4841-followup.md: tidy clean, Error Prone compile rc=0, focused tests 1 of 1 and 1 of 1, `:solr:modules:langid:check -x test` rc=0.

## Review rounds

- Round 1, self-review by the builder (vm4), 2026-10-18: the three points above hold. Open note: the identifier's content rule ("hej") is simple by design. It shows the processAdd path writes a detected value, not that detection quality works.
- Rounds 2 and 3: not run. The assignment requires at least two further rounds by a reviewer other than the builder, each on the current head, to reach SATISFIED. The assignment is therefore not closed. The branch is ready for those rounds.

## Not done

- No JIRA ticket (Nick's call, public write). Commits carry SOLR-4841 until the ticket exists.
- No PR. The branch joins an openings slate when SATISFIED, for Nick's approval.

## Review rounds 2 and 3 (vm5, 2026-10-18T19:45:04ZZ)

Reviewer: vm5, not the builder (vm4). Head reviewed: `bf1b01d0dc`. Claim: `claims/pool-vm5-solr-4841-review-rounds-2-3.md`.

Round 2, reading:
- The processing path is the one the test claims. `LanguageIdentifierUpdateProcessor.processAdd` calls `process(doc)`, which calls `detectLanguage(doc)`, which reaches the `Reader` overload the custom identifier overrides. The chain is chosen by `update.chain`, so the request goes through the update processor, not around it.
- Assertions: doc 1 expects `sv`, doc 2 expects `en`, doc 3 with no chain expects no language. The custom identifier returns `sv` only when the text contains `hej`, so the pair of positive assertions cannot both pass if detection is skipped.
- Pre-fix leg: the parent state does not compile (the public constructor is reverted). That shows the constructor change is needed, not that the assertions discriminate.

Round 2 mutation check (scratch worktree, not committed): `detectLanguage` returns an empty list. Run: `:solr:modules:langid:test --rerun --tests CustomLanguageIdentifierProcessingTest`, log `/workspace/gates/logs/vm5/r4841/mutant-no-detect.log`. Result: rc 1, 1 test, 1 failure at the first assertion, `expected:<[sv]> but was:<[]>`. The assertion discriminates.

Round 3, second pass on the same head, reading the test end to end: no defects found. The test uses the same base class and embedded rule as the sibling tests, the schema has the fields it reads (`id`, `text`, `language_s`), and the configset resource path matches the test's `getResource`.

Verdict: SATISFIED. Non-blocking notes:
- The other two assertions (`en` for doc 2, no language for doc 3) are not separately mutation-tested. A mutant that always returns `sv` would fail doc 2. That run is optional.
- The pre-fix leg is a compile failure, which is stated above and is the expected outcome for this change.

Next, for Nick: file the new ticket (draft at `pr-drafts/langid-4841/SOLR-XXXXX-ticket.md`), then approve opening the PR (draft at `pr-drafts/langid-4841/PR-test-only-draft.md`). The PR title and commit subjects need the new key.
