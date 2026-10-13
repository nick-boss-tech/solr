# Search components review, sub-batch 3: SOLR-10424, 11153, 11364, 12543, 13245, 14678, 15041, 4374, 7390, 7498, 8240, 9148, 9396, 9864

Reviewer: Windows review host (claim `claims/search-components-review.md`), 2026-10-13.
Scope: the fourteen drafts under `pr-drafts/search-components/` in the third earlier sub-batch.

Method: the same as sub-batches 1 and 2. Receipt heads were compared with the live fork tips (`git ls-remote`); all fourteen agree, and the 7390 receipt records its gated head in the status line. Counts and dates were compared with the receipts. Citations were read at the SHA they name (cite-check). Merge-bases and changed files were checked with `git merge-base` and `git diff --stat`. Uncited sentences were checked where a short read settled them. Public-text lint: no "we", no em or en dashes, and no process words in the fourteen drafts. Nothing was built, no test was run, and nothing was written to GitHub.

## Summary

| Ticket | Verdict | Edits in this tree |
|---|---|---|
| SOLR-10424 | SATISFIED | none |
| SOLR-11153 | SATISFIED | none |
| SOLR-11364 | SATISFIED | 1 (commit description now matches the final commit) |
| SOLR-12543 | SATISFIED | none (already says "partial") |
| SOLR-13245 | SATISFIED | 1 (Limits: collection-wide version as a named follow-up) |
| SOLR-14678 | SATISFIED | none (decision recorded below) |
| SOLR-15041 | SATISFIED | none |
| SOLR-4374 | SATISFIED | none |
| SOLR-7390 | SATISFIED | none |
| SOLR-7498 | SATISFIED | none |
| SOLR-8240 | SATISFIED | none |
| SOLR-9148 | SATISFIED | none |
| SOLR-9396 | SATISFIED | none |
| SOLR-9864 | SATISFIED | none |

## SOLR-10424: SATISFIED

- Head `e629ab8bb293eb54249b426b69a559f1da0f08d7` equals the live tip and the receipt. `TechproductsJsonDocsParamsTest` 1 of 1, matching the Proof.
- Premise: base `cabedd1d968` with only the fixed test file fails with exactly one failure (the `mapUniqueKeyOnly` absence check), matching the Proof. The receipt's history (the earlier head failed with a path defect, so its premise was unestablished) is correctly not in the draft.
- Cites checked, all at the SHAs they name: base `params.json` L3-L5, `ImplicitPlugins.json` L28-L31, `solrconfig.xml` L650-L654 (`df` is `text`), `managed-schema.xml` L180 (the `text` field is indexed, not stored); head `managed-schema.xml` L232 (the `*` dynamic field is commented out), test L30-L42, changelog L1.
- Cross-ticket order with SOLR-8240 matches that draft: each says this change should land before or with 8240.
- Not checked: "the sample solrconfig has no chain that adds unknown fields" (uncited; NOTE for the main side).

## SOLR-11153: SATISFIED

- Head `093df0d65bdce7444b3ef9a0da857b9c40edd00c` equals the live tip and the receipt. The 33 tests are the receipt's counts: `SchemaXmlWriterMissingNameTest` 1, `TestSolrQueryResponse` 15, `TestManagedSchema` 15, `TestSchemaManager` 2 with 1 skipped. The draft's "33 tests in all" matches.
- The Proof's note that the counts are for the final test text (`SolrTestCase`) matches the receipt's conversion note.
- Cites checked: base `SchemaXmlWriter.java` L89 (the unchecked `toString()`), `SchemaXmlResponseWriter.java` L28, `IndexSchema.java` L449-L458 (`persist`), `ManagedIndexSchemaFactory.java` L390, `IndexSchema.java` L514 (version default 1.0); head `SchemaXmlWriter.java` L89-L98 (the presence checks), test file L1, changelog L1.
- The Limits claim "Solr always sets a version ... uses 1.0 when the file has none" matches base `IndexSchema.java` L514.

## SOLR-11364: SATISFIED

- Head `562d3e7e685a06acd1fbe3baf2fd3bf168979067` equals the live tip and the receipt. `TestUseDocValuesAsStored2` 1 of 1 at the live tip, recorded 2026-10-08, matching the Proof. The class has one test method, `testSchemaAPI` (head L50).
- Edit: the Proof said "The final commit only moves one lookup out of a loop". The final commit's title is "Hoist the non-stored docValues lookup out of the explicit-name loop; pin the explicit-plus-glob case", and its diff adds a test check as well (7 insertions, 1 deletion across two files). The sentence now says both.
- Cites checked: base `TestUseDocValuesAsStored2.java` L69-L76 (the `a3` field), head `SolrDocumentFetcher.java` L883-L892 (the explicit-name branch), test L122-L130, changelog L1-L9.
- The earlier head `8b3fc5c8880` and its 58-test run match the receipt.
- Not checked: "An alias such as `fl=a3:a1` ... the raw `a3` value can be returned under the name `a3`" (uncited; NOTE for the main side).
- Record note (main side, not a draft change): the receipt records that the branch was 45 commits behind `upstream/main` at the spot-check, and a rebase before the PR is an owner item in the record.

## SOLR-12543: SATISFIED

- Head `88d236db6b7d4a538248b915447ea02e31ac83cf` equals the live tip and the receipt. `TestExportHandlerHttpStatus` 3 of 3, `TestExportWriter` 19 of 19, matching the Proof.
- Base proof: two of the three new HTTP tests fail on base with expected 400 and actual 200, and `testLocalParamSortExports` is a guard that passes on base, matching the receipt.
- Scope: the draft says "This is a partial fix" and names the three errors that still return 200 (score sort, `fl` with score, missing `rq={!xport}`). That matches the receipt's scope note, which asks for "partial" in the PR text.
- Cites checked: `ExportWriter.java` L176-L192 (the body with status 400), L221-L225 and L275-L280 (the sort and `fl` checks), L236-L240, L288-L292 and L262-L266 (the three remaining errors); `ExportHandler.java` L135-L155 (the handler checks, including the local-parameter sort); `TestExportWriter.java` L966-L994; changelog L1.

## SOLR-13245: SATISFIED

- Head `16e62ab654257b475ac07ee128686626bd1c8631` equals the live tip and the receipt. `DaemonStreamApiTest` 2 of 2, matching the Proof.
- Premise: base `StreamHandler` with this change's test fails `testDaemonListedOnEveryCoreOfCollection` ("daemon not listed by core checkpointColl_shard2_replica_n2"), and `testAPIs` also fails there, matching the receipt. The draft says the second failure does not isolate the handler change, which is correct.
- Cites checked: base `StreamHandler.java` L91 (the per-handler map), head L113-L126 (the collection-keyed map), test L204-L245, changelog L1.
- Start path, checked at head: `daemons.containsKey(id)` stops the old daemon (`remove(id).close()`) before `put`, so "a start with a name ... stops that daemon, then starts the new one" holds.
- Edit (Limits): the receipt records the node-level versus collection-wide question as the owner's call. The record reserves that choice, and the collection-wide version would need either ZooKeeper registration or a request to each replica, which are two designs with different costs. So the draft's alternative is now a named follow-up in Limits, which the owner can reopen when the PR is opened. See the decisions list.
- Not checked: "A daemon still running after its collection is deleted stays listed" (uncited, and the Limits say no test covers it).

## SOLR-14678: SATISFIED

- Head `5d94e6cf3981bd8a141a2063992148893e6d1bbb` equals the live tip and the receipt. `TestChildDocTransformerHierarchy` 18 of 18, verified 2026-10-07, matching the Proof.
- Premise: 18 tests with exactly one failure on base (`testExtraResponseFieldsDoNotLeakAcrossDocuments`), matching the receipt.
- Base cites (labelled "at the base commit"): `JSONWriter.java` L97, `SolrReturnFields.java` L554-L565, `RealTimeGetComponent.java` L366-L375 (the `/get` path, which the Limits name). Head cites: `ChildDocTransformer.java` L342-L350 and L153, `SolrReturnFields.java` L558, `DocTransformer.java` L119-L140 (the new hook; its Javadoc matches the per-document rule), test L155-L218 and L260-L326, changelog L1.
- Decision (framing question): the receipt records the per-document versus request-global framing as the owner's call. The decision default applies here, and the per-document route (implemented) is the one that does not leak. The draft's own Choice gives the defect of the request-global route: an ordinary root field that shares a nest-path name would pass a projection that leaves it out. So the per-document route is adopted, and the draft's Choice stays as PR text for the owner's review at opening.

## SOLR-15041: SATISFIED

- Head `55fca0a7c2431f0fe335aaf6fb7387c7343a66bd` equals the live tip and the receipt. `TestCSVLoader` 8 of 8, recorded 2026-10-03, matching the Proof.
- The Proof's base statement ("the new test fails on the base code") matches the receipt's "pre-fix proof PASS". The draft gives no base count, which matches the receipt (it records none).
- Cites checked: base `CSVLoaderBase.java` L147-L151 (the first record only), head L168-L181 (the join), L193-L242 (the terminator scan), test L143-L160, changelog L1.
- Not checked: "CRLF stays CRLF" (the join code keeps the recorded terminator, and the test asserts the intact CRLF in JSON; NOTE for the main side that the sentence is supported by test L157-L159 and no further check was made).

## SOLR-4374: SATISFIED

- Head `801c62290f79483e714e88f907a6564e504dfca5` equals the live tip and the receipt. `ReturnFieldsTest` 16 of 16, verified 2026-10-07, matching the Proof. Base proof: 16 tests with one failure, `testDigitLeadingFieldName`, matching the receipt.
- Cites checked: base `SolrReturnFields.java` L219-L238 (the identifier check), head L240-L264 (the schema lookup helper, which uses `getFieldTypeNoEx` and so covers explicit and dynamic fields), head L286-L289 (the call site), test L399-L406, changelog L1-L8.
- The Limits match the code: the test uses `1001_s`, not the ticket's exact field, and the rename path is untouched.

## SOLR-7390: SATISFIED

- Head `7463dd006a7825db46f00b8a4790650c5bb51e61` equals the live tip, and the receipt's status line names it as the gated head (the receipt has no separate head line; this is a format note only). `ReturnFieldsTest` 15 of 15, plus the three neighbor classes, matching the Proof. The draft says the run used an earlier commit with identical source and test files, which the receipt records (gate at 72caa363e29). That identity claim is the receipt's, and was not re-verified here.
- Merge-base `0cc328310f8` confirmed. Cites checked: base `SolrReturnFields.java` L386-L389 (the commented-out throw), head L386-L389 (the throw), test L293-L300, `QueryElevationComponent.java` at upstream main `8e62c2686` L232-L239 (the editorial-marker factory, which is why `[elevated]` needs its component), changelog L1-L8.
- Base failure, matching the receipt: `testTransformers` fails with "Expected exception SolrException but no exception was thrown".

## SOLR-7498: SATISFIED

- Head `2050d8e447a733966d5f6e4742a1a8ea931a2b00` equals the live tip and the receipt. `ExtractionBackendMetadataTest` 2 of 2, verified 2026-10-05, matching the Proof. Base failure (`testUnknownStreamSizeIsNotAdded`, the text "null") matches the receipt.
- Cites checked: base `ContentStream.java` L39 (the nullable size), base `ExtractingDocumentLoader.java` L139, base `ExtractionBackend.java` L55 (the `String.valueOf`), head `ExtractionBackend.java` L55-L58 (the null check), changelog L1.
- The PR notes from the receipt are in the draft's Limits (existing indexes keep "null", the test is at the metadata level).

## SOLR-8240: SATISFIED

- Head `fad7a1dd8e2397330e0973e809aeba60490d2f08` equals the live tip and the receipt. `JsonLoaderTest` 32 of 32 and `TestInitParams` 7 of 7, verified 2026-10-05, matching the Proof.
- Base failure ("Expected exception SolrException but no exception was thrown") matches the receipt's premise run. The shipped test sends `df=_catch_all` as well as `f`, which the receipt records as the one-line correction made during the gate; the draft describes the request in general terms and is accurate.
- Merge-base `e432df19c4a` confirmed. Cites checked: base `JsonLoader.java` L295-L327 (the mapping code), head `JsonLoader.java` L249-L255 (the check), reference guide L68 (the documented rule), `params.json` L3-L5 at head, test L192-L214, changelog L1.
- The Limits claim that the reference guide's "Setting JSON Defaults" section still shows `mapUniqueKeyOnly` is correct: the section starts at head L856 and shows `mapUniqueKeyOnly` true at L869.
- Cross-ticket order with SOLR-10424 matches that draft.

## SOLR-9148: SATISFIED

- Head `30f0d7a42d501316bd9d5336fe564608e78bfbba` equals the live tip and the receipt. `TestSQLHandler` 35 of 35 at the live tip, verified 2026-10-06, matching the Proof. The tip differs from `f8758ebe763` only by a 4-line reference guide edit (`git diff --stat`), which is the Proof's claim.
- Base failure at `f8758ebe763` (1 tuple expected, 3 returned) matches the receipt's premise.
- Cites checked: base `SQLHandler.java` L121-L127 (the allowed-parameter forwarding), head L133-L139 (the `fq` forwarding), `SolrTable.java` L313, L544, L663, L805 and L897 (the five builders), changelog L1.
- The recorded limit ("the map-reduce distinct builder receives fq by the same call but is not exercised by the test") is in the draft's Limits.
- Not checked: "The JDBC driver adds no filter queries of its own" (uncited; NOTE for the main side).

## SOLR-9396: SATISFIED

- Head `a5ab2eda4e6a9bcad5668653e80c0d0b510e6388` equals the live tip and the receipt. `TestSubQueryTransformer` 11 of 11, `TestSubQueryTransformerCrossCore` 3 of 3, `TestSubQueryTransformerDistrib` 1 of 1, `TestChildDocTransformer` 2 of 2, verified 2026-10-05, matching the Proof. The base run (1 of 11 failing, the new test) matches the receipt.
- Cites checked: base `SubQueryAugmenterFactory.java` L344-L365 (the failing invocation), head L307-L362 (the pattern and the scan), test L257-L283, changelog L1.
- The pattern at head is `\$\{?row\.([\w.\-]*\w)`, so it matches both `$row.` and `${row.`, as the Limits say.
- Not checked: "The query parser never treats that spelling as a row reference" (uncited; NOTE for the main side, since the Limits rely on it to call the `${row.` scan a read that nothing uses).

## SOLR-9864: SATISFIED

- Head `b5826e466b9a8664adf11e0f247cff220f2f5953` equals the live tip and the receipt. `SolrQueryTest` 17 of 17, `QueryRequestTest` 5 of 5, `TestUpdateRequest` 5 of 5, matching the Proof (27 in all).
- Base failure (`testGetCopyKeepsSortClauses`, the empty copy sorts) matches the receipt.
- Cites checked: base `SolrQuery.java` L1182-L1188 (`getCopy`), head L1187-L1189 (the copied list), `SortClause` at head L1293 with `private final String item` and `private final ORDER order`, so the draft's "SortClause has final fields, so sharing the objects is safe" holds; changelog L1.
- Not checked: "The ticket lists 4.8.1, 6.3, 7.7, and 9.0 as affected versions" (Jira text; not opened).

## Decisions recorded in this sub-batch

- SC-D2 (SOLR-13245, collection-wide visibility): the record reserves the node-level versus collection-wide choice. The two collection-wide designs (ZooKeeper registration, or a request to each replica) have different costs and neither dominates, so the default cannot pick between them. The collection-wide version is a named follow-up in Limits, reopenable at PR opening.
- SC-D3 (SOLR-14678, per-document nest-path names): the record reserves the framing question. The request-global alternative leaks an ordinary root field's projection, so the per-document route is adopted under the decision default and the Choice stays as PR text.
- No other scope item in this sub-batch changes a draft's behavior. Each other Choice section asks a question about a route that fixes the reported symptom, and the implemented route stands.

## Record gaps and notes for the main side

- SOLR-7390: the receipt has no separate head line and records the gate at a received head (72caa363e29) whose code it says is identical to the shipped head. The identity claim was not re-verified here.
- SOLR-11364: the branch is 45 commits behind `upstream/main` at the spot-check; a rebase before opening is an owner item in the record.
- SOLR-10424: the goal file's header still says "GATE PENDING" (record correction, per the receipt).
- Uncited sentences, not run: 13245 "a daemon still running after its collection is deleted stays listed"; 11364 alias behavior; 9148 "the JDBC driver adds no filter queries"; 9396 the query parser's treatment of `${row.`; 15041 "CRLF stays CRLF"; 10424 "no chain that adds unknown fields".

## Not checked

- Jira text for every ticket. No Jira packet was opened.
- Gate logs (not on disk here); counts are taken as the receipts record them.
- Full test bodies; only the cited blocks were read.
- No builds, no tests, no gate runs, and no writes to GitHub.
