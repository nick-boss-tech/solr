# SOLR-17311 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

Sorting with `cursorMark` by a `childfield()` join function NPE'd when a page
boundary doc had a missing child-field value:

```
java.lang.NullPointerException: Cannot invoke "java.lang.CharSequence.length()" because "text" is null
    at org.apache.lucene.util.BytesRef.<init>(BytesRef.java:84)
```

Cause: `BlockJoinSortFieldValueSource.BytesToStringComparator.value(slot)`
returns `null` for missing values (null-safe already), and cursorMark paging
feeds that null back into `setTopValue(String)`, which did `new
BytesRef(value)` unconditionally.

Fix: null-guard the wrap — `byteRefs.setTopValue(value == null ? null : new
BytesRef(value))`. Verified by decompiling Lucene 10.4.0 that the delegate
(`ToParentBlockJoinSortField$1 extends TermOrdValComparator`) fully supports a
null top value: `setTopValue` just assigns the field, and the leaf
comparator's constructor maps a null top value to `missingOrd` — so paging over
a missing-value boundary stays stable and consistent with how missing values
sorted on the first page. (Round-3 review correction: `childfield()` builds its
`ToParentBlockJoinSortField` from the field type only, so the schema field's
`sortMissingLast` is not applied; the comparator uses its default missing
ordering on every page. That limitation predates this patch and is not changed.)

(The reporter's side question about `SortSpecParsing` adding a null field is
NOT a bug: `fields.add(null)` is the by-design parallel-list marker for sorts
with no SchemaField — function sorts, score, docid — and is untouched.)

File changed:
- `solr/core/src/java/org/apache/solr/search/join/ChildFieldValueSourceParser.java`
  (`BytesToStringComparator.setTopValue`)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Test added (round-3 patch pass, **not compiled or run**):

- `TestNestedDocsSort#testCursorMarkPagingOverMissingChildValue`: six parents
  with one child each; the odd parents' children have no `name_s1`, so their
  `childfield(name_s1,$q)` sort value is null. Pages through
  `childfield(name_s1,$q) asc, id asc` with `rows=1` and `cursorMark`, and
  asserts the concatenated pages equal the single un-paged result. Before the
  fix the second request (cursor holding a null sort value) throws the NPE.

Queued for the verification run: `org.apache.solr.search.join.TestNestedDocsSort`
and `org.apache.solr.search.join.TestCloudNestedDocsSort` (existing sort
coverage), with Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.**
- The null-top semantics come from the delegate's existing default missing
  ordering — no new missing-value behavior is introduced (see the correction
  above about `sortMissingLast`).
- Changelog fragment added: `changelog/unreleased/SOLR-17311.yml`.
- Only `STRING` child fields go through `BytesToStringComparator`; other types
  are unaffected by this change.
- Remove this file before opening the upstream PR.
