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
