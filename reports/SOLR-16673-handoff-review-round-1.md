# SOLR-16673 handoff review, round 1 (vm5, 2026-10-19)

Reviewed heads: `solr-16673-uniquekey` at `70c6eb0c49008cf9518c456dedd6c4981642d52c` and `solr-16673-rootfield` at `5fb792482fc3d8fef851513bb91eb3231409a033`. Fork main base `498a09aea97aac86c2cc7ac114113b946d249942`; merge-base `e044bf20b405b8ca985730be3553d77d1fc8ff31`.

Verdict: neither head is ready for the gate. Both gate jobs in `assignments/pool-solr-16673-gate-handoff-round-1.md` are held until the items below are resolved. No branch was changed by this review; all probes ran in a scratch worktree that has been removed.

## Findings

Rung 2 probes are noted with their run results. Logs are under `/workspace/gates/logs/vm5/` (`review-uniquekey`, `review-uniquekey-fixed`, `review-uniquekey-probe`, `review-uniquekey-mutant`, `review-base-nested`, `probe16673`).

1. **Blocking, rootfield: test does not compile.** `TestSchemaDesigner.java:339` has `assertEquals(1, response.numDocs)`, which javac rejects as ambiguous between `assertEquals(Object, Object)` and `assertEquals(long, long)`. The existing file uses `assertEquals(Integer.valueOf(2), response.numDocs)` at line 143; the same form fixes this. Observed in `probe16673/probe-compile-fail.log`. The gate's Error Prone compile (step 2) would fail on this head.

2. **Blocking, both: the regression tests build an unloadable schema.** Both tests set the uniqueKey `id` to `plong`. Solr rejects a Points uniqueKey on load: `uniqueKey field (id) can not be configured to use a Points based FieldType: plong` (`IndexSchema.java:622-629`). On uniqueKey head, `testNumericUniqueKeyDocIds` fails at line 323 (`expected:<plong> but was:<string>`), because `replaceField` does not update `getUniqueKeyField()` before persist. When that assertion is moved after reload (scratch only), the next failure is the load error above, and it cascades into `testSuggestFilmsXml`. The rootfield test fails at the same line 323.

3. **uniqueKey fix: correct, and verified by a discriminating probe.** A string uniqueKey with a `Long` sample value (`setField("id", 42L)`, expect `docIds == ["42"]`):
   - Base cast `(String)` in `SchemaDesigner.java`: fails with `ClassCastException: Long cannot be cast to String`.
   - Branch head: passes (focused run, 1m 32s).
   Recommended replacement for the committed test, built on the default string uniqueKey with no `replaceField` and no `plong`:

   ```java
   @Test
   public void testNumericUniqueKeyDocIds() throws Exception {
     String configSet = "testNumericUniqueKeyDocIds";
     schemaDesigner.prepNewSchema(configSet, null);
     ManagedIndexSchema schema = schemaDesigner.loadLatestSchema(getMutableId(configSet));
     assertEquals("string", schema.getUniqueKeyField().getType().getTypeName());
     SolrInputDocument sampleDoc = new SolrInputDocument();
     sampleDoc.setField("id", 42L);
     SchemaDesignerAnalyzeResponse response =
         schemaDesigner.buildSchemaDesignerResponse(
             SchemaDesignerAnalyzeResponse.class, configSet, schema, null, List.of(sampleDoc));
     assertEquals(List.of("42"), response.docIds);
   }
   ```

4. **Blocking, rootfield: the premise of the fix is not demonstrated.** A nested-docs analyze with a string uniqueKey (`[{"id":"1","_childDocuments_":[{"id":"2"}]}]`) indexes zero sample docs (`Failed to index 1 sample docs into temp collection`). It fails the same way on fork base `498a09aea9` and on the rootfield head. The reload-before-indexing change in `SchemaDesigner.analyze` does not make it pass. The cause is not identified in this round, so the change is not shown to fix anything, and the regression test cannot serve as its proof.

5. **Open question, rootfield: reachability.** With the default configset (`plong`, no trie long type), a numeric uniqueKey is rejected at load (finding 2). The `_root_` / uniqueKey mismatch from Issue 3 needs a numeric uniqueKey type that Solr accepts, and no such type is in the default configset. Whether the reporter's schema used one is unknown (`reports/SOLR-16673-remaining-issues.md` says the reporter path is unconfirmed). Owner decision needed: find a non-Points numeric type the designer can load and build the test on it, or drop the rootfield change as unreachable with current types.

6. **Code-read concern, rootfield, not verified.** `enableNestedDocsFields` adds `_root_` only when it is missing. If `_root_` already exists with another type, and the uniqueKey type is changed later through the field-update path (`SchemaDesignerConfigSetHelper.java:431`), `_root_` keeps the old type. This is a possible gap in the creation-time fix. It needs a probe after findings 2 and 4 are settled.

7. **Changelogs.** Both changelog files parse as YAML and name the ticket and author. Titles read correctly against the code.

## Tidy base drift (from the earlier gate attempt)

Unchanged: tidy removes `oshai-logging` from `gradle/libs.versions.toml` on the merge-base as well. The decision in the handoff assignment still applies to that item, but it cannot be reached until findings 1 to 4 are fixed, because a new head is needed anyway.

## Recommended next steps

1. uniqueKey branch: replace `testNumericUniqueKeyDocIds` with the string-uniqueKey test from finding 3. Re-run the focused class, then gate the new head.
2. Rootfield branch: owner decides finding 5 first. If the change stays, fix finding 1, find out why finding 4 indexes nothing (base behaviour first), and only then rewrite the test. Finding 6 is checked after that.
3. Both gate jobs stay OPEN but on hold until a new head is pushed and this review is answered. The gate jobs must then be re-posted at the new heads, because the heads named in `gates/` are no longer the tips.
