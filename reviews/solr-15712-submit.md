# solr-15712-submit

- Branch: origin/solr-15712-submit
- Head: 555f9cab6b72 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: 3 commits. `solr/core/src/java/org/apache/solr/schema/CollationField.java` (+21: useDocValuesAsStored guard in `init` and `checkSchemaField`), `solr/core/src/test/org/apache/solr/schema/TestCollationFieldDocValues.java` (+21: `testDocValuesAreNotUsedAsStored`), `changelog/unreleased/SOLR-15712-collationfield-no-docvalues-as-stored.yml`, and `SOLR-15712-TESTING.md` (hypothetical-reproduction note, kept in place).
- Verdict: Needs work
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code. Patches: none.

## Premise check

- VERIFIED: base `CollationField` has no useDocValuesAsStored guard. `ICUCollationField` does (`ICUCollationField.java:116-134`), which is the SOLR-15777 fix this branch copies.
- VERIFIED: schema version 1.6 or later turns useDocValuesAsStored on by default (`FieldType.java:190`). A collation docValues field therefore defaults to UDVAS=true on base.
- VERIFIED: `SolrDocumentFetcher.decodeDVField` decodes SORTED doc values with `bRef.utf8ToString()` for every type except `BoolField` (`SolrDocumentFetcher.java:641-650`). Collation keys are SORTED doc values, so they are decoded as UTF-8 whenever their doc values are returned.
- VERIFIED: the `fl=*` and default-fl path returns UDVAS=true doc-values fields (`SolrDocumentFetcher.java:866`). The branch removes CollationField from that set.
- VERIFIED (gap): an explicit `fl` that names a UDVAS=false doc-values field is still decorated (`SolrDocumentFetcher.java:872-873` and `:890`, with the comment at `:887-889`). So an explicit `fl=sort_de` still reaches `utf8ToString`. The TESTING doc says this is "Not addressed".
- The JIRA summary (`research/jira-context/SOLR-15712.json`) is "Certain unicode field values throw exceptions for query and backup operations". The fl=* default path is covered. The explicit-fl query path is not.

## Findings (ranked)

MEDIUM (verified): An explicit `fl` still decodes collation keys. The guard clears UDVAS, so fields drop out of the `fl=*` set (`SolrDocumentFetcher.java:866`). But an explicit `fl=<collation field>` is still decorated (`:872-873`, `:890`), and `decodeDVField` falls through to `utf8ToString` (`:649`). The ticket's exception is still reachable with `fl=sort_de` or any explicit collation field name. Owner design call: what an explicit `fl` should return for a collation field. Options: (a) return the key as an encoded string, (b) omit the field, (c) reject the `fl` with 400, or (d) leave it and document it. The fix point would be `CollationField.toObject` or a special case in `decodeDVField`. Not patched, because the choice is a design decision and not a clear defect in the diff.

MEDIUM (direction call, verified): The hard-fail policy does not match ICU. ICU sets `failHardOnUdvas` from `luceneMatchVersion >= 9.0` and only warns below that (`ICUCollationField.java:126`, `:116-122`, `:127-130`). This branch always passes `failCondition = true` (`CollationField.java:92-94` and `:101-104`), so an explicit `useDocValuesAsStored="true"` on a CollationField now refuses to load under every schema version. The TESTING doc says this is "the same call as the ICU field". The function call is the same, but the policy is not. Existing user schemas that set it would fail to load where ICU would only warn. In-tree: no collation field sets it. The hits found in test-files and configsets are on other field types. Owner call: match ICU (gate on luceneMatchVersion), or keep the unconditional hard fail.

LOW (verified): Test coverage. `testDocValuesAreNotUsedAsStored` covers the default and `fl=*` paths only. It does not cover an explicit `fl`, which is the gap above. There is also no test for the explicit-true failure path, as the TESTING doc notes.

LOW (verified, cosmetic): The message is built with `+ CollationField.class`, so it reads "useDocValuesAsStored is forbidden for class org.apache.solr.schema.CollationField". ICU builds its message the same way, so this matches local practice.

LOW (verified, nit): The comment on `UDVAS_MESSAGE` (`CollationField.java:85-86`) cites ticket numbers. AGENTS.md asks for no code comments about the change. ICU does the same, so this follows local precedent.

## Verified correct (by reading; not run)

- Init order. `FieldType.setArgs` sets the 1.6+ default (`FieldType.java:190`), applies `trueProperties` (`:199-203`), then calls `init` (`:207`). `CollationField.init` checks `trueProperties` for an explicit true and then clears the bit (`CollationField.java:101-105`). The default and explicit cases are both handled.
- Field inheritance. `SchemaField` takes its properties from the type (`SchemaField.java:56`, `this(name, type, type.properties, null)`), so fields pick up the cleared bit. `checkSchemaField` therefore fires only for an explicit field-level true (`CollationField.java:92-94`).
- The test. `schema-collate-dv.xml` is schema version 1.7 (line 21), so on base the sort_* fields default to UDVAS=true and the `assertFalse` fails. The sort fields are `stored="false"` (lines 42-48), so after the fix `fl=*` returns no `sort_` entries. Doc id 4 exists (`TestCollationFieldDocValues.java:39`). By reading, the test fails before the fix and passes after.
- Imports. `assertWarnOrFail` is statically imported the same way ICU imports it (`CollationField.java:19`).
- The changelog fragment matches the upstream format. `type: fixed` is valid.
- All three commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions posed (not decided, no patch made)

1. Explicit `fl` on a collation field: which of options (a) to (d) above (see the first MEDIUM).
2. Hard-fail policy: unconditional, as in this branch, or ICU's `luceneMatchVersion >= 9.0` gate with a warning below it (see the second MEDIUM).

## Not checked

- Nothing was compiled, run, or tested.
- Not every configset in the tree was checked for an explicit `useDocValuesAsStored="true"` on a collation type. The hits that were checked are on other field types.
- The explicit-`fl` behavior was verified by reading the shared decode path. ICU's behavior was not run.
- The `count(//result/doc/*[starts-with(@name,'sort_')])` assertion was not evaluated.
- Upstream conflicts were not checked. The branch is 16 commits behind `upstream/main`.
