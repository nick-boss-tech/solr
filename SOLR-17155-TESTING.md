# SOLR-17155 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

Distributed grouping's shard-result serializer NPE'd when the collection's
unique-key field was `stored=false` (even with `docValues=true` and
`useDocValuesAsStored=true`): `TopGroupsResultTransformer.retrieveDocument()`
loads via `SolrDocumentFetcher.doc(doc, Set.of(uniqueField.getName()))`,
which only reads stored fields, so `doc.getField(uniqueKey)` came back null
and `FieldType.toExternal(null)` threw NPE at the `f.stringValue()` call.

Fix (local to the serializer, per the research note — the hot
document-fetch path is untouched): when the unique-key field is absent from
the fetched `Document`, fall back to reading its docValues value for that doc
and add it to the document as a synthetic `StoredField`, so the existing
`toExternal(doc.getField(...))` call sites work unchanged. Handles
`SortedDocValues` (the common string-id case), `SortedSetDocValues` (first
ord), and `NumericDocValues` (long). If the field has no docValues value
either, the field stays null and the old failure mode is preserved (a schema
where the unique key is neither stored nor in docValues cannot supply an id
at all — out of scope).

Notes for the reviewer:

- The coordinator side (`transformToNativeShardDoc`) already tolerates a null
  `id` with a logged error, so no downstream crash is introduced.
- Externalization relies on `FieldType.toExternal`'s existing
  stringValue/binaryValue handling; numeric ids externalize via
  `StoredField(long).stringValue()` decimal form (and `TrieField` overrides
  `toExternal` for its encoding).
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).

Files changed:
- `solr/core/src/java/org/apache/solr/search/grouping/distributed/shardresultserializer/TopGroupsResultTransformer.java`
  (`retrieveDocument` + new `readDocValuesField` helper; both serialize
  call sites now pass `rb.req.getSearcher()`)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. Distributed grouping with a uniqueKey field configured `stored=false,
   docValues=true, useDocValuesAsStored=true`: group query whose top groups
   serialize (needs ≥2 shards or a mocked shard-result path) → no NPE, and
   the returned group document ids match the actual doc values.
2. Regression: same test with a stored uniqueKey field still works.
3. Edge: uniqueKey with neither stored nor docValues → behavior unchanged
   from before (still fails, no silent wrong id).

## Patch limits and follow-ups

- **Not compiled or tested.**
- Multi-valued uniqueKey takes the first docValues ord — unique keys are
  single-valued by definition, so this is a degenerate-schema fallback only.
- Remove this file before opening the upstream PR.
