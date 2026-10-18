# B1 removed-feature tickets, round 1

Assignment: `assignments/pool-skip-review-removed-round-1.md`
Claim: `claims/pool-skip-review-removed-round-1.md` (vm3)
Pinned head: fork `origin/main` at `498a09aea97aac86c2cc7ac114113b946d249942`. Not moved.

Method: JIRA description read through the anonymous REST endpoint (read only). Named code paths checked on the pinned head with `git grep`, `git show` and `git log -S`. Paths are relative to the repo root. No builds or test runs. No JIRA, GitHub or email posts.

Scope: all 41 rows of the assignment table were checked.

## Corrections to the queue notes

- The queue notes call Trie fields "removed" for SOLR-2976, SOLR-3124, SOLR-4610, SOLR-5383, SOLR-5717, SOLR-8024 and SOLR-9809. That is not true at the head. `solr/core/src/java/org/apache/solr/schema/TrieField.java` is still present, deprecated since 7.0 (line 87-89). Those rows are judged on their code paths.
- SOLR-9749 is Resolved/Invalid in JIRA. The verdict below comes from the code only.

## Verdicts

| Ticket | JIRA status | Verdict | Evidence |
| --- | --- | --- | --- |
| SOLR-1658 | Open | fixed | Commit `0eca10b618` (SOLR-18256) adds nested pure negative handling in `solr/core/src/java/org/apache/solr/search/QParser.java` and `SolrQueryParser.java`. Head test `solr/core/src/test/org/apache/solr/search/TestSolrQueryParser.java:1932-1940` covers `pureneg AND foo AND (-bar)` and `(NOT bar)`. Test not run. |
| SOLR-2976 | Open | fixed | `TrieField.isTokenized()` returns false (`solr/core/src/java/org/apache/solr/schema/TrieField.java:311-313`), changed by `8fba601ed1`. Multivalued check corrected by `47541d03bc` (SOLR-3642). Stats dispatch in `solr/core/src/java/org/apache/solr/handler/component/StatsValuesFactory.java:79-86`. The commit subjects do not cite SOLR-2976; linkage is by code path. |
| SOLR-3064 | Open | gone | `ContentStreamUpdateRequest.java` deleted by `78862e7f54` (SOLR-18351). `git grep ContentStreamUpdateRequest` at the head has no hits. |
| SOLR-3124 | Open | present | `solr/core/src/java/org/apache/solr/handler/component/DebugComponent.java:443` sets `parsedquery_toString` from raw `query.toString()`. Explain text is Lucene's, with no Solr-side decode (`DebugComponent.java:453-461`). Only `parsedquery` is decoded (`QueryParsing.java:195`). |
| SOLR-3201 | Open | gone | DIH removed by `a57ba25400` (SOLR-14783). `changelog/archive.md:1033` records the removal. No DIH or VariableResolver source at head. |
| SOLR-4113 | Open | gone | DIH removed by `a57ba25400`. No DIH code at head. |
| SOLR-4183 | Open | gone | DIH removed by `a57ba25400`. No DIH code at head. |
| SOLR-4382 | Open | fixed | `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:146` calls `validateQueryFields`, which throws SyntaxError for an unknown qf field (`:223-229`, `checkFieldInSchema` at `:276-281`). Added by `9481c1f623` (SOLR-5163). The reported `qf=sku doesNotExit` case is now rejected rather than producing a phrase query. |
| SOLR-4390 | Open | fixed | `solr/solrj/src/java/org/apache/solr/common/params/SolrParams.java:416-425` URL-encodes parameter names (`nameEnc` at `:423`). Used by `HttpJdkSolrClient.java:282, 336`. Added by `f4fe17cce8` (SOLR-5971). Linkage is by the illegal-URL symptom; the commit subject does not cite SOLR-4390. |
| SOLR-4522 | Open | fixed | `solr/core/src/java/org/apache/solr/request/SimpleFacets.java:991` calls `ft.getFieldTermQuery(null, sf, term)` (defined at `solr/core/src/java/org/apache/solr/schema/FieldType.java:1143`) instead of `getFieldQuery`. Changed by `841cbe0b94` (SOLR-8319). |
| SOLR-4610 | Open | present | `DebugComponent.java:443` prints bare `query.toString()`. Schema-aware decoding exists only in `QueryParsing.toString(Query, IndexSchema)` (`QueryParsing.java:195, 211`), which callers must opt into (`DebugComponent.java:442`). |
| SOLR-5383 | Open | unclear | Group-facet path `getGroupedCounts` (`SimpleFacets.java:775-835`) has no trie-prefix filter. `getNumericHidingWrapper` (`:836-850`) hides only single-valued non-DV numerics. Missing: whether Lucene's `TermGroupFacetCollector` enumerates precision-step terms (Lucene source is not in this repo), or a head reproduction with a multiValued trie field, precisionStep above 0, and `group.facet=true`. The reproduction needs a build, which this item does not allow. |
| SOLR-5717 | Open | fixed | `solr/core/src/java/org/apache/solr/search/grouping/CommandHandler.java:189-200` uses `ValueSourceGroupSelector` when `getNumberType() != null`, and `TermGroupSelector` otherwise. Same split in `search/grouping/distributed/command/SearchGroupsFieldCommand.java:98-118`. The numeric branch came with `caf329717e` (LUCENE-7701), which touches `CommandHandler.java`. Linkage is by code path; no repro run. |
| SOLR-6142 | Open | gone | No `pom.xml` is tracked anywhere at the head (`git ls-tree -r`). The build is Gradle (`build-tools/build-infra/declare-repositories.gradle`). The generated-pom dependency scope the ticket names does not exist. |
| SOLR-6166 | Open | gone | `HttpSolrServer.java` removed by `4a6d85790a` (SOLR-6895). `ls-tree` finds no such path. Replacement is `HttpSolrClient.java`. |
| SOLR-6303 | Open | gone | DIH removed by `a57ba25400`. No JDBC DataSource code at head. |
| SOLR-6395 | Open | present | `solr/webapp/web/js/angular/controllers/cloud.js:752-782`: `buildZkSubtree` walks the whole ZK tree on `initTree` (line 778). No lazy child loading. The runtime hang was not reproduced; verdict is code-level. |
| SOLR-6875 | Open | unclear | The named mechanisms are gone: `StreamingSolrServers` has no hits (last removed by `ef809a0f10`, SOLR-6895), and LIR was removed by `a37a213975` (SOLR-11812). The reported symptom (replica document counts diverging) is not tied to one path at head, so absence is not shown. Showing it needs a cluster test, which needs a build and is out of scope here. |
| SOLR-7009 | Open | fixed | `solr/core/src/java/org/apache/solr/handler/loader/XMLLoader.java:137-143` logs `"body: {}"`. Changed by `04193d5252` (SOLR-14207). |
| SOLR-7221 | Open | gone | `ConcurrentUpdateSolrServer.java` removed by `4a6d85790a` (SOLR-6895). Its replacements (`ConcurrentUpdateBaseSolrClient`, `ConcurrentUpdateJdkSolrClient`) do not call `setFollowRedirects` or `getParams`. |
| SOLR-7517 | Open | fixed | `solr/core/src/java/org/apache/solr/core/CorePropertiesLocator.java:101-107`: a persist failure now logs and throws a SolrException. Added by `412e4ae2c1` (SOLR-10719). |
| SOLR-7773 | Open | fixed | `solr/bin/solr.in.sh:35` has `SOLR_HEAP` commented out. `solr/bin/solr:1186-1190` uses `SOLR_JAVA_MEM` only when `SOLR_HEAP` is unset, so an environment value is not overwritten. Changed by `8ae3304c86` (SOLR-7580). |
| SOLR-7953 | Open | fixed | `solr/core/src/java/org/apache/solr/response/transform/BaseEditorialTransformer.java:61-63` checks `instanceof IndexableField`. Changed by `2502af9f3f` (SOLR-10286; the subject does not cite SOLR-7953). |
| SOLR-7998 | Open | present | `solr/bin/solr:1381-1387`: a version gate on `lsof -v` output (grep for "revision") is followed by `lsof -t -PniTCP:$SOLR_PORT_LISTEN -sTCP:LISTEN` with errors suppressed. This is the same clustered `-PniTCP:` form the ticket reports. Not run against lsof 4.80. |
| SOLR-8024 | Open | fixed | `solr/core/src/java/org/apache/solr/search/facet/FacetField.java:157` routes multi-valued numeric DocValues to HashDV, not UIF. UIF is chosen only when `!sf.hasDocValues()` (`:90`, `:162`). Trie multi-valued DocValues use `SortedSetDocValuesField` (`TrieField.java:666-675`). Changed by `f66aa9ed07` (SOLR-17649). |
| SOLR-8076 | Open | gone | DIH removed by `a57ba25400`. No DIH transformer loader at head. |
| SOLR-8087 | Open | gone | LIR removed by `a37a213975` (SOLR-11812): `LeaderInitiatedRecoveryThread.java` deleted. `git grep` for LeaderInitiatedRecovery and lirState in `solr/core/src` and `solr/solrj/src` has no hits. |
| SOLR-8089 | Open | gone | `git grep -i enablePositionIncrement` finds no Java at head. Only historical notes (`changelog/archive.md:8874`, commented lines in `solr/core/src/test-files/solr/collection1/conf/schema-tagger.xml:170, 178`). No removal commit located; the verdict rests on the absence. |
| SOLR-8158 | Open | gone | Analytics component removed by `67afcc228d` (SOLR-16661). `changelog/v10.0.0/SOLR-16661-removed deprecated -analytics- component.yml` records it. No `olap=` handling at head. |
| SOLR-8463 | Open | unclear | `FacetComponent.java:723-724` and `:985-986` have null guards at head. The countFacets guard predates the ticket (`900363a9b6`, SOLR-3557). No commit mentions SOLR-8463. Missing: the NPE stack from the original report (the description has none), and a shards.tolerant run on the head to see which site still throws. That needs a build. |
| SOLR-8843 | Open | gone | `getLeaderPropsWithFallback` and `NoChildrenForEphemeralsException` have no hits in `solr/core/src` or `solr/solrj/src`, and `git log -S` finds neither in this history. `ZkController.java:1709` (`getLeaderProps`) has no ephemeral fallback. |
| SOLR-8991 | Open | fixed | `bdaddafcce` ("SOLR-8991 Fix ping when ms=0"). `solr/webapp/web/index.html:253` uses `ng-show="showPing"`; `solr/webapp/web/js/angular/app.js:644-646` sets `showPing` on success. |
| SOLR-9094 | Open | fixed | `solr/core/src/java/org/apache/solr/search/ReRankCollector.java:136-138` populates scores only when `sort != null`, and the scoreMode is COMPLETE_NO_SCORES for sorts that do not need scores. Changed by `55bfadbce1` (LUCENE-8412). Missing: no regression test for a non-score sort was found in `TestReRankQParserPlugin`; not run. |
| SOLR-9126 | Open | fixed | `fa9dfac8a7` (SOLR-16187). `solrj/src/java/org/apache/solr/common/util/ExecutorUtil.java:116-136` calls the await helper once; the helper (`:139-155`) catches InterruptedException, calls `shutdownNow`, and restores the interrupt flag. |
| SOLR-9202 | Open | present | `solr/core/src/java/org/apache/solr/uninverting/UninvertingReader.java:331` provides `getNumericDocValues` only. No SortedNumeric path from points exists in the uninverting package (grep empty). |
| SOLR-9267 | Open | fixed | `solr/core/src/java/org/apache/solr/search/mlt/AbstractMLTQParser.java:120-122` parses qf with `parseFieldBoosts`, which strips `^boost`. `CloudMLTQParser.java:100-131` no longer looks up the boosted name. Changed by `2b4e3dd941`. |
| SOLR-9366 | Open | gone | The trigger (replica put into LIR) was removed with LIR by `a37a213975`. Residual log string "Ignoring commit while not ACTIVE" is at `DistributedUpdateProcessor.java:1181`; not traced further. |
| SOLR-9400 | Open | gone | DIH removed by `a57ba25400`. No SortedMapBackedCache code at head. |
| SOLR-9677 | Open | fixed | `ExtendedDismaxQParser.java:146, 223-229, 276-281`: an unknown qf field throws SyntaxError rather than becoming a keyword. Added by `9481c1f623` (SOLR-5163). |
| SOLR-9749 | Resolved/Invalid | gone | `solr/core/src/resources/ImplicitPlugins.json:60-83` lists `/admin/ping`, `segments`, `luke`, `info`, `file`; there is no `/admin/plugins`. The entry was removed by `94e9dfe356` (SOLR-17853). |
| SOLR-9809 | Open | present | `TrieField.java:579-588` `createField` builds a Legacy field when docValues is set, and `createFields` (`:666-670`) adds it unconditionally. The only mitigation is the in-place-update filter at `solr/core/src/java/org/apache/solr/update/DocumentBuilder.java:69`. |

## Tally

- fixed: 16 (SOLR-1658, 2976, 4382, 4390, 4522, 5717, 7009, 7517, 7773, 7953, 8024, 8991, 9094, 9126, 9267, 9677)
- present: 6 (SOLR-3124, 4610, 6395, 7998, 9202, 9809)
- gone: 16 (SOLR-3064, 3201, 4113, 4183, 6142, 6166, 6303, 7221, 8076, 8087, 8089, 8158, 8843, 9366, 9400, 9749)
- unclear: 3 (SOLR-5383, 6875, 8463)

## Notes for the owner

- Fixed and gone rows that are linked by code path, not by the ticket number in the commit subject: SOLR-2976, 4382, 4390, 4522, 5717, 7517, 7773, 7953, 8024, 9094, 9267, 9677, 1658. A reader who needs the ticket link should confirm from the commit bodies.
- No repro was run for any row. Several "present" and "fixed" verdicts are code-level only.
