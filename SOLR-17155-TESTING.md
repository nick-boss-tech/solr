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

Fix (local to the serializer; the hot document-fetch path is untouched):
`retrieveUniqueKey` returns the external id string. It reads the stored field
when present; otherwise it reads the value through the existing
`SolrDocumentFetcher.decorateDocValueFields` with one `DocValuesIteratorCache`
per serialize call. If the field has neither a stored value nor docValues for
the doc, it now throws a `SolrException` naming the field, instead of the old
NullPointerException.

Round-3 review rework (the first version differed): the first version added a
synthetic `StoredField` to the `Document` returned by
`SolrDocumentFetcher.doc(int, Set)`, which can be a shared `documentCache` entry
(not thread-safe, visible to other requests), and re-implemented docValues
decoding by hand (including numeric and sorted-set branches that cannot occur,
because the schema rejects point-field and multi-valued unique keys). Both are
gone: the fetched document is never modified and decoding is the existing code.

Notes for the reviewer:

- Scope question that remains open: `IndexSchema` logs a warning that with a
  non-stored unique key "distributed search and MoreLikeThis will not work". This
  patch fixes the grouping shard serializer only; other distributed paths were
  not checked. Say so in the PR, and expect a question about whether non-stored
  unique keys are meant to be supported.
- The coordinator side (`transformToNativeShardDoc`) already tolerates a null
  `id` with a logged error; unchanged.
- The value is converted with `toString()`, which equals the external form for
  string unique keys (the supported, practical case).
- `uniqueKey` with neither stored nor docValues now fails with the clear message
  above rather than an NPE.

Files changed:
- `solr/core/src/java/org/apache/solr/search/grouping/distributed/shardresultserializer/TopGroupsResultTransformer.java`
  (`retrieveUniqueKey`, called from both serialize methods)
- `solr/core/src/test/org/apache/solr/search/grouping/distributed/shardresultserializer/TopGroupsResultTransformerTest.java` (new)
- `changelog/unreleased/SOLR-17155.yml` (new)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Test added (round-3 patch pass, **not compiled or run**):

- `TopGroupsResultTransformerTest#testSecondPhaseWithUniqueKeyNotStored`:
  embedded server (`EmbeddedSolrServerTestRule`) on the minimal config set with
  the `id` field rewritten to `stored="false" docValues="true"`; sends the
  request a shard receives in the second phase of distributed grouping
  (`group.distributed.second=true` with `group.topgroups.grp_s=a,b`) and checks
  that the returned group documents carry ids 1, 2 and 3. Before the fix this
  request fails with the NullPointerException from the ticket.

Queued for the verification run:
`org.apache.solr.search.grouping.distributed.shardresultserializer.TopGroupsResultTransformerTest`,
plus the existing `org.apache.solr.TestDistributedGrouping` (stored unique key,
regression), with Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.** The test assumes the second-phase response is
  `secondPhase -> grp_s -> <group value> -> documents -> id`.
- Not covered: a multi-shard end-to-end run with a non-stored unique key.
- Remove this file before opening the upstream PR.
