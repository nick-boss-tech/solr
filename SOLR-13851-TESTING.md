# SOLR-13851 Testing Handoff — getFirstMatch trips assertion on multiple matches

## What changed

`solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java`
(`lookupId(String, BytesRef)` and the `getFirstMatch` javadoc):

- The `assert docs.nextDoc() == DocIdSetIterator.NO_MORE_DOCS;` is replaced with
  a thrown `IllegalStateException` ("More than one document matches term ...
  on field ...; getFirstMatch is only intended for unique fields"). Previously
  the failure only surfaced when assertions were enabled, and was silent
  otherwise.
- `getFirstMatch` javadoc now documents the unique-field contract and the
  exception (also fixed a stray `"` typo in that javadoc).

No behavior change for the existing callers (LukeRequestHandler,
QueryComponent.doProcessSearchByIds, RealTimeGetComponent), which all look up
uniqueKey terms and never hit the multi-match path.

## How to validate

This branch was pushed deliberately uncompiled/untested (pipeline phase 2).
External review should:

1. Compile (serial build only — never two Gradle builds at once on this VM):
   `~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true`
   from the worktree (copy `~/workspace/solr/gradle.properties` into the worktree first).
   If `gradle/libs.versions.toml` shows tidy churn, restore it with
   `git checkout -- gradle/libs.versions.toml`.
2. Suggested new coverage: call `getFirstMatch` with a term matching two
   documents on a non-unique field → expect `IllegalStateException` (not
   `AssertionError`, and not a silent return when assertions are disabled);
   unique-term lookup still returns the doc id; missing term still returns -1.
3. Regression: existing RTG / search-by-id tests still pass
   (e.g. tests around RealTimeGetComponent and TestRTGBase).

## Review notes

- The heavier deprecate-and-rename redesign discussed on the ticket was
  deliberately left out — maintainers only required the assert→exception swap
  plus documentation.
- Remove this file before opening the upstream PR; keep the AI-disclosure
  header/footer convention in the PR description.
