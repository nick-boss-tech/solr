# Receipt: PR review apache/solr #4042 (vm4)

- PR: https://github.com/apache/solr/pull/4042, open, not draft, head `7f244b2906` on `epugh/solr` branch `SOLR-18068`, base `8d3f995c6a`. 5 commits, 3 files, +427/-3 (from the GitHub files API).
- Files: `solr/core/src/test/org/apache/solr/spelling/suggest/TestPhraseSuggestions.java` (modified, +28/-3), `solr/test-framework/src/java/org/apache/solr/SolrClientTestHelpers.java` (added, +277), `solr/test-framework/src/java/org/apache/solr/SolrXPathTestCase.java` (added, +122).
- Base checks at `c8ab71430f`: no existing `SolrXPathTestCase` or `SolrClientTestHelpers` (grep empty in `solr/test-framework` and `solr/core/src/test`). `EmbeddedSolrServerTestRule` and `SolrTestCaseJ4Test` exist at base (core tests reference `newRandomConfig`).
- Gate: none run. The POC migrates one test; a before and after run is the gate if the owner wants it.
- Outcome: NEEDS-OWNER. Draft: `drafts/pr-review/4042.md`.
