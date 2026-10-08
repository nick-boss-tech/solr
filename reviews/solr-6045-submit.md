# solr-6045-submit

- Branch: origin/solr-6045-submit
- Head: dcdef50d7006
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits. `AtomicUpdateDocumentMerger.java` (+27/-3: `isAtomicUpdate` and `mergeDocHavingSameId` read `getFirstValue()`; new `atomicOperations(sif)` helper flattens all operation maps and rejects mixed values), `AtomicUpdatesTest.java` (+74: two new tests), changelog `SOLR-6045-atomic-update-repeated-field.yml` (+8, type `fixed`)
- Verdict: Nearly
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

Owner instruction followed: the round 33 sequencing proposal (SOLR-6045 vs SOLR-12703) is not implemented here. This review covers the branch as it stands.

## Delta check against earlier disposition

- Handoff: no earlier disposition listed for this ticket; the bulk review is not in the Group C notes.
- Head unchanged: `origin/solr-6045-submit` is at `dcdef50d7006`, as listed.

## Verified code facts

- `SolrInputField.getFirstValue()` returns the first element of a collection, or the value itself, and returns `null` for an empty collection (`solrj/.../SolrInputField.java`, around lines 102-108). The new calls cannot throw on an empty field.
- `SolrInputField.getValues()` returns the collection, or a singleton list for a non-collection value (around lines 124-131). `atomicOperations` therefore handles single-map fields exactly as before.
- `isAtomicUpdate` still excludes child documents: `SolrInputDocument` extends `SolrDocumentBase`, so `!(val instanceof SolrDocumentBase)` keeps child documents out (`SolrInputDocument.java:36`, `SolrDocumentBase.java:24`).
- Test helper signatures resolve: `AtomicUpdateDocumentMerger(SolrQueryRequest)` (line 74), `ContentStreamBase.create(RequestWriter, SolrRequest)` (`ContentStreamBase.java:307`), inherited `handleRequestBody(SolrQueryRequest, SolrQueryResponse)` from `ContentStreamHandlerBase`, `JavaBinRequestWriter` (implicit no-arg constructor), `SolrQueryRequestBase.setContentStreams(Iterable)`. Imports in the test match the uses. Compilation itself was not run.

## Findings (ranked)

1. **LOW, verified. Mixed input now returns 400 where it used to index.** Before the change, a multi-valued field whose first value is a Map was not treated as atomic, so the maps were indexed as their `toString()`. Now a field that mixes operation maps with plain values throws `BAD_REQUEST` ("mixes atomic update operations with plain values", `atomicOperations`). The changelog says maps are "recognized and applied instead of being indexed as the maps' toString()", but it does not mention the new 400 for mixed input. Owner may want the changelog to say so. The 400 is the intended outcome, not a defect.

2. **LOW, verified. Child-document field values now enter the operation path when they are the first value of a multi-valued field.** `isChildDoc` (`AtomicUpdateDocumentMerger.java:696`) treats `SolrDocumentBase` values as children, and `mergeDocHavingSameId` iterates every field of the update document. A multi-valued field whose first value is a child `SolrInputDocument` now goes through `atomicOperations`, which iterates the child's fields as operation names and fails with "Unknown operation". Before the change, the same field was a plain set. Single-valued child document fields already behaved this way on base. Child documents added through `SolrInputDocument.addChildDocument` are kept in `_childDocuments` (`SolrInputDocument.java:269-274`), not as field values, so the normal path is not affected. No existing test covers this case (checked: the only core tests combining child documents with atomic maps are `AtomicUpdatesTest` and `TestInPlaceUpdatesStandalone`, and the latter never mixes the two in one document). Hypothesis that the edge case matters in practice: low.

3. **LOW, hypothesis. Javabin wire behaviour is asserted, not checked.** `testRepeatedAddFieldOfOperationsViaJavabin` depends on JavaBinCodec returning a collection of maps for repeated `addField`. The test comment also says the XML writer folds repeated maps into one map. Neither was traced in this checkout. The test is the end-to-end check for the ticket's wire path, so the gate run should confirm it.

4. **Positive, verified by reading. Both new tests should discriminate on base (not run).** `testRepeatedAddFieldOfOperationsIsAtomicUpdate` asserts `isAtomicUpdate(cmd)` is true. On base, `getValue()` returns a collection, so it is false and the test fails. `testRepeatedAddFieldOfOperationsViaJavabin` queries the stored values, which on base would be the maps' `toString()`. The expected `["bbb","ddd"]` follows from the operations as written: `doSet` calls `toDoc.setField` (replace, line 486-489) and `doAdd` calls `toDoc.addField` (append, line 491-500), applied in order.

5. **LOW, verified. Operation order is preserved, and a later `set` replaces earlier `add`s from the same request.** `atomicOperations` flattens maps in field-value order, and the loop applies them in that order. Not a defect, but it is the semantics a caller will see.

## Owner call (not decided here)

None raised by this branch's diff. The round 33 sequencing question (SOLR-6045 vs SOLR-12703) is outside this review and was not implemented. Point 1 above is a changelog wording decision the owner may want to make: whether mixed input should be documented as a 400.

## Not checked

- Not compiled, formatted (spotless), or run. Pass/fail on base and head is a hypothesis.
- Javabin reader and XML writer behaviour for repeated maps (finding 3).
- `getNativeFieldValue` (type conversion of op values) was not read; it applies equally to single-map and repeated-map input.
- Nothing pushed or posted to GitHub or JIRA.
