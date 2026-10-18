# Ticket draft: SOLR-4841 follow-up test (for Nick to file; no ticket exists yet)

Status: DRAFT. Not filed. Filing is a public write and needs Nick's approval. Replace the summary placeholder with the new key once it exists, and use that key in the PR title and commit subjects.

Issue type: Test. Priority: Minor. Component: langid (modules). Related: SOLR-4841.

Summary: Test language detection through a custom identifier in the update chain

Description:

SOLR-4841 made the DetectedLanguage constructor public, so a language identifier outside the processor package can build its results. The merged test only compiles and checks two getters. No test runs detectLanguage on a document.

This follow-up adds a test that sends documents through an update chain whose processor is a custom identifier. The test checks that the detected language is written to the indexed document, and that a document sent without that chain gets no language.

Scope: test sources and a test configset in the langid module. No production change.

---

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
