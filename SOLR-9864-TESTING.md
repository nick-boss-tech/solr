# SOLR-9864 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-9864 - "SolrQuery.getCopy() doesn't copy sortClauses" (Lyubov Romanchuk). A later comment (Bram VD) confirms it is still the case on 7.7/master; Jason Gerlowski offered to review a cleaned-up patch with a test.
- Branch: `solr-9864-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`SolrQuery.getCopy()` copies every request parameter, including `sort`, but not the private `sortClauses` list that backs `getSorts()`/`addSort()`/`removeSort()`. On the copy, `getSorts()` is empty, and the next `addSort` re-serializes only the new clause, overwriting the copied `sort` param. Still true on main (`SolrQuery.getCopy`, solrj `request` package).

## What the branch changes
- `SolrQuery.getCopy()`: also copies `sortClauses` into a new list (`SortClause` is immutable).
- New `SolrQueryTest.testGetCopyKeepsSortClauses`: copy has equal `getSorts()` and `sort` param; adding a sort to the copy keeps the old ones and leaves the original untouched.

## What was guessed (verify these first)
1. Expected `sort` strings (`price asc,id desc`) assume `serializeSorts` output format `field order` joined by `,` with `ORDER` lower-case (`asc`/`desc`) as in the other tests.
2. `assertEquals` on `List<SortClause>` relies on the same clause instances (no `equals` on `SortClause` assumed).
3. `SolrTestCase` (JUnit3-style method naming, no `@Test` needed) as in the neighbouring tests.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:solrj:spotlessApply
.\gradlew :solr:solrj:test --tests "org.apache.solr.client.solrj.request.SolrQueryTest"
```
Fail-before: revert only `SolrQuery.java`; `getSorts()` on the copy is empty.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
