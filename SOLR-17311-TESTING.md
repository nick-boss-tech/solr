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
comparator's constructor maps a null top value to `missingOrd`, honoring the
sortMissingLast/First policy — so paging over a missing-value boundary stays
stable and consistent with how missing values sorted on the first page.

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

Suggested tests (not written):

1. Index parent/child nested docs where some parents lack the child field;
   run a `childfield(...) asc` sort with `cursorMark=*` and page through;
   expect no NPE and stable paging (no dupes/skips across the boundary).
2. Unit-level: `setTopValue(null)` on the comparator must not throw (the
   exact reported crash); also `setTopValue("x")` still works.

## Patch limits and follow-ups

- **Not compiled or tested.**
- The null-top semantics (missing sorts first/last per the field's
  `sortMissingLast`) come from the delegate's existing policy — no new
  missing-value behavior is introduced.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
