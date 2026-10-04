# SOLR-11364 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-11364 - "Fields with useDocValuesAsStored=false never be returned in case of pattern matching" (Cao Manh Dat). Two-sentence ticket quoting David Smiley (SOLR-8344): with `fl=foo*,dvField` and `dvField` `useDocValuesAsStored=false`, `calcDocValueFieldsForReturn` does not return `dvField` although it is explicitly named.
- Branch: `solr-11364-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`SolrDocumentFetcher.calcDocValueFieldsForReturn` has three branches. `wantsAllFields` and the no-pattern branch honor explicitly requested `useDocValuesAsStored=false` fields; the `hasPatternMatching()` branch only collects
`getNonStoredDVs(true)` fields that `wantsField`, i.e. only `useDocValuesAsStored=true` ones. In `SolrReturnFields.parseFieldList`, a glob clears the lucene `fields` set, so explicit names next to a glob are not seen through
`getLuceneFieldNames()` either. Still true on main (`SolrDocumentFetcher.java` ~L877).

## What the branch changes
- `SolrDocumentFetcher.calcDocValueFieldsForReturn`: in the pattern branch also adds fields from `returnFields.getRequestedFieldNames()` that are non-stored DV fields regardless of `useDocValuesAsStored`.
- `TestUseDocValuesAsStored2.testSchemaAPI`: extra assertion `fl=id,a*,a3` returns `a3` (while the existing `fl=id,a*` assertion still excludes it).

## What was guessed (verify these first)
1. **`getRequestedFieldNames()` contents**: assumed to hold explicit names (and renamed/keyed names) but not globs, and to be non-null here. If it also contains glob strings they are harmless (they will not match a field name); if it is null for this `fl`, the fix does nothing.
2. **`getNonStoredDVs(false)`** is the set of all non-stored DV fields (used by the other two branches the same way); a field that is both stored and useDocValuesAsStored=false would not be affected.
3. **Test**: `TestUseDocValuesAsStored2.testSchemaAPI` is not annotated `@Test` in the version I read (it is named `test*`, picked up by the JUnit3-style runner of `RestTestBase`/`SolrTestCaseJ4`); I only added an assertion inside it.
4. Returned value for `a3` in `fl=id,a*,a3`: expecting `'a3':'3'` like the existing `fl=id,a1,a2,a3` assertion.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.schema.TestUseDocValuesAsStored2" --tests "org.apache.solr.schema.TestUseDocValuesAsStored"
```
Fail-before: revert only `SolrDocumentFetcher.java`; the new assertion should miss `a3`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
