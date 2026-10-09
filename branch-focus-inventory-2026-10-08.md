# Branch focus inventory, 2026-10-08

Refresh of the 2026-10-06 inventory against the live fork. The 2026-10-06 file is not edited; use this copy from now on.

Purpose: classify every `solr-*-submit` branch on the fork by the part of the code it touches, so review rounds can be batched by focus area.

Snapshot: `git ls-remote --heads origin` captured 2026-10-08 22:31 -04:00 (local time), after `git fetch origin --prune` in source. The fork moves while this is being read, so re-run the refresh before relying on a head. Population: 329 branches, every `solr-*-submit` head plus 5 named non-submit branches promoted to rows. Against the 2026-10-06 snapshot of 297: 197 unchanged, 100 moved (current head shown; the Since 10-06 column says how), 32 new (rows added), 0 gone.

Excluded from the population: `ci/*` scratch branches (not listed), the fork `main`, the coordination branches, and the named non-submit branches that are not rows. The closing section "Branches outside the population" gives each one a code area and its reason for being outside.

State labels: as in 2026-10-06, plus `candidate` (kept as a candidate by owner decision on 2026-10-08; not yet registered for the pipeline). Existing rows keep their 2026-10-06 state and topic, except solr-18119-submit, which is now retire candidate. Main files for moved rows are the 2026-10-06 values, not recomputed.

Focus areas: existing rows keep their 2026-10-06 area. New rows were placed by hand from their primary changed file; the changes section lists the judgment calls.

## Area summary

| Focus area | Branches | States | Tickets |
|---|---|---|---|
| eDisMax and extended dismax | 13 | PR-ready 6, awaiting pipeline 2, gated, no PR 4, held 1 | 2309, 2988, 3243, 3729, 3923, 3962, 4362, 6009, 6320, 7120, 12092, 14638, 14913 |
| Update processing and atomic updates | 29 | PR-ready 10, awaiting pipeline 6, in pipeline 1, gated, no PR 8, held 1, live PR 1, candidate 2 | 3657, 4841, 5065, 5505, 5754, 5887, 5939, 5941, 6045, 6065, 6973, 7022, 7504, 11475, 11483, 12245, 12703, 12705, 12864, 13265, 13696, 13943, 14262, 14718, 16356, 16655, 16673, 16910, 18505 |
| Suggester | 8 | PR-ready 2, awaiting pipeline 3, gated, no PR 2, held 1 | 9227, 9637, 9968, 10937, 11844, 14171, 17215, 17393 |
| Spellcheck | 9 | PR-ready 3, awaiting pipeline 4, gated, no PR 2 | 1877, 3701, 4366, 4367, 4399, 9060, 10252, 10789, 17612 |
| Highlighting | 5 | awaiting pipeline 5 | 2632, 2681, 3704, 4540, 16885 |
| Configsets and config API | 7 | PR-ready 1, awaiting pipeline 2, gated, no PR 2, live PR 2 | 6960, 7267, 7323, 13706, 15478, 17363, 18178-verify |
| Query parsing | 25 | awaiting pipeline 5, gated, no PR 17, live PR 2, retire candidate 1 | 874, 4824, 6014, 8977, 9048, 9149, 10897, 11391, 11761, 12212, 12532, 12608, 12871, 13202, 13838, 13903, 15615, 15906, 16130-test-followup, 16267, 16570, 17280, 17311, 17796, 17882 |
| Schema, analysis and field types | 11 | PR-ready 1, awaiting pipeline 3, gated, no PR 7 | 9349, 10131, 10403, 14199, 15357, 15358, 15712, 15945, 16977, 17047, 18134 |
| Search components | 71 | PR-ready 11, awaiting pipeline 14, gated, no PR 36, held 3, live PR 4, retire candidate 3 | 3044, 4374, 5394, 6193, 6207, 6759, 6831, 6975, 7390, 7498, 7520, 7550, 8003, 8009, 8020, 8051, 8088, 8240, 8767, 8939, 8954, 9124, 9148, 9396, 9595, 9864, 10305, 10424, 10492, 10694, 10844, 11129, 11153, 11310, 11364, 11470, 12044, 12543, 12556, 13245, 13568, 13851, 13876, 14381, 14451, 14678, 14931, 15018, 15041, 15144, 15319, 15331, 15479, 15895, 16155, 16290, 16444, 17051, 17055, 17155, 17372, 17539, 17748, 17791, 17841, 17976, 18109, 18196, 18356, 18482, 18506 |
| SolrCloud, overseer and cluster state | 31 | PR-ready 5, awaiting pipeline 6, gated, no PR 12, held 2, live PR 5, retire candidate 1 | 3865, 4754, 5813, 7394-recovery, 9155, 10234, 10641, 11288, 11479, 12651, 12991, 12998, 13136, 13186, 13239, 13369, 14919, 15035, 15106, 15386, 15674, 15863, 16013, 16437, 17281, 17292, 17680, 17733, 18277, 18391, 18391-graceful-create |
| Core admin and collections API | 33 | PR-ready 3, awaiting pipeline 8, gated, no PR 17, held 1, live PR 3, retire candidate 1 | 4502, 4989, 5011, 5262, 6438, 8275, 8554, 8576, 8628, 9750, 11431-core-init-503, 11939, 12007, 12849, 12916, 13097, 13246, 14098, 15003, 15024, 15805, 16108, 16499, 16725, 16849, 16887, 17297, 17377, 17708, 17731, 18010, 18278, 18317-server |
| Replication and backup | 13 | PR-ready 1, awaiting pipeline 5, gated, no PR 5, live PR 2 | 5589, 6711, 8430, 9091, 9382, 9598, 9865, 11650, 12085, 12246, 17287, 18249, 18280 |
| Streaming expressions | 11 | PR-ready 1, awaiting pipeline 2, gated, no PR 3, held 1, live PR 1, retire candidate 3 | 9852, 10322, 10882, 11922, 12505, 12657, 13524, 14200, 14231, 15326, 17433-17143 |
| SolrJ and clients | 24 | PR-ready 8, awaiting pipeline 7, gated, no PR 5, live PR 4 | 2018, 3498, 3722, 3999, 4335, 4336, 4422, 4424, 5220, 6046, 7709, 8536, 10198, 10364, 11356, 12094, 14187, 14298, 14967, 15823, 15823-levels, 17866, 18129, 18341 |
| CLI, bin scripts and packaging | 11 | PR-ready 2, awaiting pipeline 4, gated, no PR 5 | 7924, 9342, 10390, 10667, 12347, 16272, 16813, 17029, 17598, 18132, 18339 |
| Security and authentication | 4 | awaiting pipeline 2, gated, no PR 2 | 10627, 11678, 12161, 18368 |
| Metrics and monitoring | 1 | awaiting pipeline 1 | 17987 |
| Admin UI | 3 | awaiting pipeline 3 | 9759, 9818, 9831 |
| Build, docs and misc | 20 | awaiting pipeline 6, gated, no PR 6, live PR 2, retire candidate 6 | 3684, 5821, 6430, 7119, 9039, 11700, 12743, 13705, 16322, 16914, 17252, 17356, 17722, 17752, 17825, 17842, 18119, 18119-jvm, 18317, 18523 |

Total: 329 branches in 19 areas with at least one member.

## eDisMax and extended dismax (13)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-2309 | solr-2309-submit | 06f5a1c4a87 | PR-ready | ExtendedSolrQueryParser doesnt support stopword in case of a fuzzy query | org/apache/solr/search/ExtendedDismaxQParser.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-2988 | solr-2988-submit | d2d144dfd9f | PR-ready | edismax does not respect pf params using non-tokenized fields | org/apache/solr/search/ExtendedDismaxQParser.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-3243 | solr-3243-submit | 1db99c13662 | PR-ready | eDismax and non-fielded range query | org/apache/solr/search/ExtendedDismaxQParser.java<br>(3 files total) | diff | moved, ff +2 |
| SOLR-3729 | solr-3729-submit | 0fe7e503945 | PR-ready | ExtendedDismaxQParser (edismax) doesn't parse (*:*) properly | org/apache/solr/search/ExtendedDismaxQParser.java<br>(3 files total) | diff | moved, ff +3 |
| SOLR-3923 | solr-3923-submit | 059804fcec8 | awaiting pipeline | eDismax: complex fielded query with parens is not recognized | org/apache/solr/search/ExtendedDismaxQParser.java<br>(4 files total) | diff |  |
| SOLR-3962 | solr-3962-submit | e7d5f350503 | PR-ready | For the match-all-docs query *:*, (e)dismax parser passes '*:*' to tokenizer, sub-optimal (<1.0) hit scores | org/apache/solr/search/ExtendedDismaxQParser.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-4362 | solr-4362-submit | e96a057439c | gated, no PR | edismax, phrase query with slop, pf parameter | org/apache/solr/search/ExtendedDismaxQParser.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-6009 | solr-6009-submit | a41bb034a1f | gated, no PR | edismax mis-parsing RegexpQuery | org/apache/solr/search/ExtendedDismaxQParser.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-6320 | solr-6320-submit | cb710c96353 | PR-ready | ExtendedDismaxQParser (edismax) parser fails on some queries | org/apache/solr/search/ExtendedDismaxQParser.java<br>(3 files total) | diff | moved, ff +2 |
| SOLR-7120 | solr-7120-submit | 6a434a7fc3d | awaiting pipeline |  | org/apache/solr/search/ExtendedDismaxQParser.java<br>(4 files total) | diff |  |
| SOLR-12092 | solr-12092-submit | ca9573dabd3 | gated, no PR | Edismax - Stopwords - Should exclude ManageStopFilterFactory | org/apache/solr/search/ExtendedDismaxQParser.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-14638 | solr-14638-submit | 44cf1c8fc80 | held | Edismax boost function zero score [PARKED, under review on Nick side] | org/apache/solr/search/ExtendedDismaxQParser.java<br>(4 files total) | diff |  |
| SOLR-14913 | solr-14913-submit | b80221f46d3 | gated, no PR | Including non-existing field in edismax field alias breaks parsing of boolean query | org/apache/solr/search/ExtendedDismaxQParser.java<br>(3 files total) | diff | moved, ff +1 |

## Update processing and atomic updates (29)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-3657 | solr-3657-submit | 14edaca577c | PR-ready | error message only refers to 'source' field when problem parsing value for 'dest' field of copyField | org/apache/solr/update/DocumentBuilder.java<br>(3 files total) | diff | moved, ff +2 |
| SOLR-4841 | solr-4841-submit | f8850ffd421 | awaiting pipeline | DetectedLanguage constructor should be public | org/apache/solr/update/processor/DetectedLanguage.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-5065 | solr-5065-submit | c0a0ce1b8d9 | PR-ready | ParseDoubleFieldUpdateProcessorFactory is unable to parse "+" in the exponent | org/apache/solr/update/processor/ParseDoubleFieldUpdateProcessorFactory.java<br>org/apache/solr/update/processor/ParseFloatFieldUpdateProcessorFactory.java<br>org/apache/solr/update/processor/ParseNumericFieldUpdateProcessorFactory.java<br>(5 files total) | diff | moved, ff +1 |
| SOLR-5505 | solr-5505-submit | 44c444aa5cd | awaiting pipeline | LoggingInfoStream not usabe in a multi-core setup | org/apache/solr/update/LoggingInfoStream.java<br>org/apache/solr/update/SolrIndexConfig.java<br>(5 files total) | diff | moved, not ff (+2) |
| SOLR-5754 | solr-5754-submit | 36fe859d5fb | awaiting pipeline | SolrStreamingServers returns a synchronizedList of Errors. | org/apache/solr/update/SolrCmdDistributor.java<br>org/apache/solr/update/StreamingSolrClients.java<br>(5 files total)<br>also: Streaming expressions | diff |  |
| SOLR-5887 | solr-5887-submit | c4c57ef7bcb | awaiting pipeline | Document exception don't give core information | org/apache/solr/update/AddUpdateCommand.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-5939 | solr-5939-submit | 8f7a36fa6dd | candidate | streaming update errors recorded against the wrong request | changelog/unreleased/SOLR-5939.yml<br>solr/core/src/java/org/apache/solr/update/SolrCmdDistributor.java<br>solr/core/src/java/org/apache/solr/update/StreamingSolrClients.java<br>(7 files total) | diff | new |
| SOLR-5941 | solr-5941-submit | 62516cc338e | candidate | autocommit runs through the default update processing chain | changelog/unreleased/SOLR-5941.yml<br>solr/core/src/java/org/apache/solr/update/CommitTracker.java<br>solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java<br>(7 files total) | diff | new |
| SOLR-6045 | solr-6045-submit | dcdef50d700 | PR-ready | atomic updates w/ solrj + BinaryRequestWriter aren't working when adding multiple fields w/ same name in a sin | org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java<br>(3 files total) | diff |  |
| SOLR-6065 | solr-6065-submit | 6aef011ee8d | PR-ready | Solr should give you clear error if you try to add too many docs | org/apache/solr/update/DirectUpdateHandler2.java<br>(3 files total) | diff |  |
| SOLR-6973 | solr-6973-submit | 4c6092614e5 | awaiting pipeline | signature partial update with no signature fields computes an empty signature | SOLR-6973-TESTING.md<br>changelog/unreleased/SOLR-6973-signature-partial-update.yml<br>solr/core/src/java/org/apache/solr/update/processor/SignatureUpdateProcessorFactory.java<br>(4 files total) | diff | new |
| SOLR-7022 | solr-7022-submit | 6a233ab2fdb | PR-ready | ERROR UpdateHandler java.lang.InterruptedException | org/apache/solr/update/DirectUpdateHandler2.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-7504 | solr-7504-submit | e3fdc8eb58f | PR-ready | CountFieldValuesUpdateProcessorFactory handles atomic update maps (Jira title not recorded in the branch repor | org/apache/solr/update/processor/CountFieldValuesUpdateProcessorFactory.java<br>(3 files total) | diff |  |
| SOLR-11475 | solr-11475-submit | 42fb8817b25 | in pipeline | Endless loop and OOM in PeerSync [in pipeline: gate paused mid-run, resumable] | org/apache/solr/update/PeerSync.java<br>(3 files total) | diff |  |
| SOLR-11483 | solr-11483-submit | 4431a250f66 | PR-ready | Keep more transaction log files when maxNumLogsToKeep is specified | org/apache/solr/update/UpdateLog.java<br>(4 files total) | diff |  |
| SOLR-12245 | solr-12245-submit | 4a93167458b | gated, no PR | DistributedUpdateProcessor doesn't set MDC in some errors | org/apache/solr/update/processor/DistributedUpdateProcessor.java<br>(3 files total) | diff |  |
| SOLR-12703 | solr-12703-submit | ed95d555e62 | PR-ready | Better validation of bad atomic updates | org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java<br>(3 files total) | diff |  |
| SOLR-12705 | solr-12705-submit | b053944b127 | PR-ready | ParseDateFieldUpdateProcessorFactory does not work for atomic update values | org/apache/solr/update/processor/FieldMutatingUpdateProcessor.java<br>(3 files total) | diff |  |
| SOLR-12864 | solr-12864-submit | 8c5455d3d24 | awaiting pipeline | test-only pin: echo with mapUniqueKeyOnly | SOLR-12864-TESTING.md<br>solr/core/src/test/org/apache/solr/handler/JsonLoaderTest.java<br>(2 files total) | diff | new |
| SOLR-13265 | solr-13265-submit | c134b34aa27 | gated, no PR | TLOG replica, updateHandler errors in metrics, no logs | org/apache/solr/update/DirectUpdateHandler2.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-13696 | solr-13696-submit | 05ab4664dac | held | DimensionalRoutedAliasUpdateProcessorTest / RoutedAliasUpdateProcessorTest failures due commitWithin/openSearc [held: banked untouched, real fix or retire call pending] | SOLR-13696-TESTING.md<br>org/apache/solr/update/processor/RoutedAliasUpdateProcessorTest.java | diff |  |
| SOLR-13943 | solr-13943-submit | b37d7abfa2e | gated, no PR | TimeRoutedAliasUpdateProcessorTest.testDateMathInStart: multi-threaded race condition due to ZK assumptions | changelog/unreleased/SOLR-13943.yml<br>org/apache/solr/update/processor/TimeRoutedAliasUpdateProcessorTest.java | diff | moved, ff +1 |
| SOLR-14262 | solr-14262-submit | 1e8d2b0075d | PR-ready | report a commit skipped while the update log is not ACTIVE (Jira title not recorded in the branch report) | org/apache/solr/update/processor/DistributedUpdateProcessor.java<br>(3 files total) | diff | moved, ff +2 |
| SOLR-14718 | solr-14718-submit | 29c09959791 | gated, no PR | Multiple flaws in tracking which UpdateCommand is associated with a given failure logged by ErrorReportingConc | org/apache/solr/update/SolrCmdDistributor.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-16356 | solr-16356-submit | 39c0585072f | gated, no PR | DBQ is noisy during core close | org/apache/solr/update/UpdateLog.java<br>(3 files total) | diff |  |
| SOLR-16655 | solr-16655-submit | aa7898d972a | gated, no PR | Indexing nested documents and URPs | org/apache/solr/update/processor/FieldMutatingUpdateProcessor.java<br>(4 files total) | diff |  |
| SOLR-16673 | solr-16673-submit | d5c19e64ba1 | gated, no PR | Strange error when using Schema Designer | org/apache/solr/update/processor/ParseDoubleFieldUpdateProcessorFactory.java<br>org/apache/solr/update/processor/ParseFloatFieldUpdateProcessorFactory.java<br>org/apache/solr/update/processor/ParseIntFieldUpdateProcessorFactory.java<br>(6 files total) | diff |  |
| SOLR-16910 | solr-16910-submit | 9fce3e9a705 | gated, no PR | Adjusting LogUpdateProcessorFactory's log level has side effects on SolrCore logging and changes "slowUpdateTh | org/apache/solr/update/processor/LogUpdateProcessorFactory.java<br>(4 files total) | diff | moved, ff +3 |
| SOLR-18505 | solr-18505-submit | e28739b4069 | live PR #5028 | DirectUpdateHandlerTest.testExpungeDeletes is flaky: a deletion-driven merge races the post-commit searcher sa | org/apache/solr/update/DirectUpdateHandlerTest.java | diff | moved, ff +1 |

## Suggester (8)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-9227 | solr-9227-submit | 3c23fc5cfa6 | PR-ready | Solr Suggester should report full error when it fails to build | org/apache/solr/spelling/suggest/SolrSuggester.java<br>(3 files total) | diff |  |
| SOLR-9637 | solr-9637-submit | c50fa4ffd93 | held | gated, awaiting Nick's stacking call [held: gated, stacked on SOLR-17393; stacking call pending] | org/apache/solr/handler/component/SuggestComponent.java<br>(3 files total) | diff |  |
| SOLR-9968 | solr-9968-submit | d688e1efdf2 | PR-ready | Cannot use special characters in Suggester Context Query | org/apache/solr/spelling/suggest/SolrSuggester.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-10937 | solr-10937-submit | 9d8151c43a8 | awaiting pipeline | No space left on device when building suggester index | SOLR-10937-TESTING.md<br>changelog/unreleased/SOLR-10937-suggester-tmpdir-doc.yml<br>solr/solr-ref-guide/modules/query-guide/pages/suggester.adoc | diff |  |
| SOLR-11844 | solr-11844-submit | 4e226462f3b | awaiting pipeline | Sort suggestions in solr based on the position of keyword | SOLR-11844-TESTING.md<br>changelog/unreleased/SOLR-11844-blended-weight-docs.yml<br>solr/solr-ref-guide/modules/query-guide/pages/suggester.adoc | diff |  |
| SOLR-14171 | solr-14171-submit | 942acabd26e | gated, no PR | allTermsRequired does not work when using context filter query | org/apache/solr/handler/component/SuggestComponent.java<br>org/apache/solr/spelling/suggest/SolrSuggester.java<br>org/apache/solr/spelling/suggest/SuggesterOptions.java<br>(7 files total) | diff |  |
| SOLR-17215 | solr-17215-submit | 66be0171976 | awaiting pipeline | Solr Replication doesn't work for suggester for FreeTextLookupFactory | org/apache/solr/spelling/suggest/SolrSuggester.java<br>(4 files total) | diff |  |
| SOLR-17393 | solr-17393-submit | dd6c82924ff | gated, no PR | Solr Suggester not working as expected for collection with multiple shards(distributed search) | org/apache/solr/handler/component/SuggestComponent.java<br>(4 files total) | diff |  |

## Spellcheck (9)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-1877 | solr-1877-submit | 0d591618679 | gated, no PR | Investigate unclosed Reader in IndexBasedSpellCheck | org/apache/solr/handler/component/SpellCheckComponent.java<br>org/apache/solr/spelling/AbstractLuceneSpellChecker.java<br>org/apache/solr/spelling/IndexBasedSpellChecker.java<br>(6 files total) | diff |  |
| SOLR-3701 | solr-3701-submit | aabd678dec7 | awaiting pipeline | Solr Spellcheck for words with apostrophe | org/apache/solr/spelling/SpellingQueryConverter.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-4366 | solr-4366-submit | 5613b311952 | awaiting pipeline | NPE sometimes in SpellCheckComponent when dictionary name not defined | org/apache/solr/handler/component/SpellCheckComponent.java<br>(4 files total) | diff |  |
| SOLR-4367 | solr-4367-submit | 0af6087f43f | PR-ready | SpellCheckComponent doesn't complain if its spellchecker list is malformed | org/apache/solr/handler/component/SpellCheckComponent.java<br>(3 files total) | diff |  |
| SOLR-4399 | solr-4399-submit | de6cc6b27ed | PR-ready | NPE in Lucene if Solr spellcheck sourceLocation parameter is missing for FileBasedSpellChecker or IndexBasedSp | org/apache/solr/spelling/FileBasedSpellChecker.java<br>(3 files total) | diff |  |
| SOLR-9060 | solr-9060-submit | 704ca28bf79 | PR-ready | Spellcheck sort by frequency in solrcloud | org/apache/solr/handler/component/SpellCheckComponent.java<br>(3 files total) | diff |  |
| SOLR-10252 | solr-10252-submit | a2be0f3adfe | awaiting pipeline | Example spellcheck config uses _text_ as default field | SOLR-10252-TESTING.md<br>changelog/unreleased/SOLR-10252-spellcheck-config-note.yml<br>solr/server/solr/configsets/_default/conf/solrconfig.xml<br>also: Configsets and config API | diff | moved, ff +1 |
| SOLR-10789 | solr-10789-submit | d9077048d74 | awaiting pipeline | SpellCheckCollator prohibits setting several query parser params | org/apache/solr/spelling/SpellCheckCollator.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-17612 | solr-17612-submit | cd0426e40a7 | gated, no PR | spellcheck.maxResultsForSuggest percentage is flawed when multiple shards | org/apache/solr/handler/component/SpellCheckComponent.java<br>(4 files total) | diff |  |

## Highlighting (5)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-2632 | solr-2632-submit | 1d7018f3fd7 | awaiting pipeline | Highlighting does not work for embedded boost query that boosts a dismax query | org/apache/solr/highlight/DefaultSolrHighlighter.java<br>(4 files total) | diff |  |
| SOLR-2681 | solr-2681-submit | 4cb25b1691b | awaiting pipeline | Lucene highlighting unable to extract terms from sub-query of a FunctionQuery | org/apache/solr/highlight/DefaultSolrHighlighter.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-3704 | solr-3704-submit | de63d4e5d5d | awaiting pipeline | Date range queries fail when highlighting | org/apache/solr/highlight/DefaultSolrHighlighter.java<br>org/apache/solr/schema/DatePointField.java<br>(5 files total)<br>also: Schema, analysis and field types | diff | moved, not ff (+2) |
| SOLR-4540 | solr-4540-submit | 62c06439fb0 | awaiting pipeline | High QTime when wildcards in hl.fl are used | org/apache/solr/highlight/DefaultSolrHighlighter.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-16885 | solr-16885-submit | 2b9b80119a9 | awaiting pipeline | UnifiedHighlighter guard for term vectors without positions | SOLR-16885-TESTING.md<br>changelog/unreleased/SOLR-16885-unified-highlighter-tv-without-positions.yml<br>solr/core/src/java/org/apache/solr/highlight/UnifiedSolrHighlighter.java<br>(5 files total) | diff | new |

## Configsets and config API (7)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-6960 | solr-6960-submit | 9bef536fc12 | PR-ready | Config reporting handler is missing initParams defaults | org/apache/solr/core/RequestHandlers.java<br>org/apache/solr/core/SolrConfig.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-7267 | solr-7267-submit | 59f34a339b0 | awaiting pipeline | Confusion over "cz" (vs "cs") naming convention for Czech fields in sampel configs | SOLR-7267-TESTING.md<br>changelog/unreleased/SOLR-7267-czech-cs-field-type.yml<br>org/apache/solr/schema/DefaultConfigSetCzechTest.java<br>(5 files total) | diff | moved, ff +2 |
| SOLR-7323 | solr-7323-submit | fb034dc5f87 | awaiting pipeline | configset-not-found message names the configset and base dir | SOLR-7323-TESTING.md<br>changelog/unreleased/SOLR-7323-configset-not-found-message.yml<br>solr/core/src/java/org/apache/solr/core/FileSystemConfigSetService.java<br>(4 files total) | diff | new |
| SOLR-13706 | solr-13706-submit | 590dd5c24d9 | live PR #5015 | Config API output is broken for "highlight" component | org/apache/solr/core/PluginInfo.java<br>org/apache/solr/core/SolrConfig.java<br>(6 files total) | diff | moved, ff +1 |
| SOLR-15478 | solr-15478-submit | 0478bdf0ac5 | gated, no PR | Schema Changes are Not Visible after Reuse of ConfigSet | org/apache/solr/cloud/ZkConfigSetService.java<br>(3 files total) | diff |  |
| SOLR-17363 | solr-17363-submit | b8e8e1c4846 | gated, no PR | ConfigRequest could fail when user property is updated | org/apache/solr/handler/SolrConfigHandler.java<br>(3 files total) | diff |  |
| SOLR-18178 | solr-18178-verify | b75e7d4d3c4 | live PR #4968 | fix configset archive path handling | changelog/unreleased/SOLR-18178.yml<br>solr/core/src/java/org/apache/solr/core/FileSystemConfigSetService.java<br>solr/core/src/java/org/apache/solr/handler/configsets/DownloadConfigSet.java<br>(9 files total) | diff | new |

## Query parsing (25)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-874 | solr-874-submit | ac9ab337537 | awaiting pipeline | Dismax parser exceptions on trailing OPERATOR | org/apache/solr/util/SolrPluginUtils.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-4824 | solr-4824-submit | 76c777e4661 | awaiting pipeline | Fuzzy / Faceting results are changed after ingestion of documents past a certain number | org/apache/solr/parser/SolrQueryParserBase.java<br>org/apache/solr/search/LuceneQParser.java<br>(5 files total) | diff | moved, not ff (+2) |
| SOLR-6014 | solr-6014-submit | 505849d2d4e | awaiting pipeline | Nested subquery containing stop words only can invalidate whole query | org/apache/solr/search/DisMaxQParser.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-8977 | solr-8977-submit | 395b24964fd | gated, no PR | graph qparser's traversalFilter doesn't support pure negative queries | org/apache/solr/search/join/GraphQueryParser.java<br>(3 files total) | diff |  |
| SOLR-9048 | solr-9048-submit | b145018563c | gated, no PR | {!parent } {!child } throws NPE if underneath query parser yields no clauses | org/apache/solr/search/join/FiltersQParser.java<br>(3 files total) | diff |  |
| SOLR-9149 | solr-9149-submit | 151dfed119e | gated, no PR | bug when nested query precedes the main query | org/apache/solr/parser/SolrQueryParserBase.java<br>(3 files total) | diff |  |
| SOLR-10897 | solr-10897-submit | d9240d0750f | gated, no PR | SimpleQParserPlugin doesn't work with PointFields | org/apache/solr/search/SimpleQParserPlugin.java<br>(4 files total) | diff |  |
| SOLR-11391 | solr-11391-submit | 825d7f81d12 | awaiting pipeline | join with an unknown method returns 400, not an unhandled exception | SOLR-11391-TESTING.md<br>changelog/unreleased/SOLR-11391-join-unknown-method-bad-request.yml<br>solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java<br>(4 files total) | diff | new |
| SOLR-11761 | solr-11761-submit | 41893ee9ce6 | gated, no PR | Query parsing with comments fail in org.apache.solr.parser.QueryParser | org/apache/solr/parser/SolrQueryParserBase.java<br>(3 files total) | diff |  |
| SOLR-12212 | solr-12212-submit | 876953fdc92 | gated, no PR | SolrQueryParser not handling q.op=AND correctly for parenthesized NOT fq | org/apache/solr/parser/QueryParser.java<br>org/apache/solr/parser/QueryParser.jj<br>(4 files total) | diff |  |
| SOLR-12532 | solr-12532-submit | 69c06da4467 | gated, no PR | Slop specified in query string is not preserved for certain phrase searches | org/apache/solr/parser/SolrQueryParserBase.java<br>(4 files total) | diff |  |
| SOLR-12608 | solr-12608-submit | d1dd8a1f9f0 | gated, no PR | Edismax: Out of memory error with a query full of *. | org/apache/solr/parser/SolrQueryParserBase.java<br>(3 files total) | diff |  |
| SOLR-12871 | solr-12871-submit | c79a49320cd | gated, no PR | sort=childfield(currency_field) desc fails with exception about REWRITABLE field type | org/apache/solr/search/join/ChildFieldValueSourceParser.java<br>(4 files total) | diff | moved, ff +2 |
| SOLR-13202 | solr-13202-submit | c0aec3a7ae0 | live PR #5012 | Three NullPointerExceptions in org.apache.solr.search.JoinQuery.hashCode() | org/apache/solr/search/JoinQParserPlugin.java<br>org/apache/solr/search/join/AuxIndexJoinQParserPlugin.java<br>org/apache/solr/search/join/CrossCollectionJoinQParser.java<br>(9 files total)<br>also: Core admin and collections API | diff |  |
| SOLR-13838 | solr-13838-submit | 4520c25abe2 | gated, no PR | igain query parser generating invalid output | org/apache/solr/search/IGainTermsQParserPlugin.java<br>(6 files total) | diff |  |
| SOLR-13903 | solr-13903-submit | 73ef411dd11 | gated, no PR | Classification Model Confusion Matrix Discrepancy | org/apache/solr/search/TextLogisticRegressionQParserPlugin.java<br>(6 files total) | diff |  |
| SOLR-15615 | solr-15615-submit | be77267bf55 | gated, no PR | SolrCloud MLT does not work with Time Routed Alias (TRA) | org/apache/solr/search/mlt/CloudMLTQParser.java<br>(4 files total) | diff | moved, ff +4 |
| SOLR-15906 | solr-15906-submit | 50139a8e979 | gated, no PR | Query parsing ignores rest of query when 'v' local-param is used | org/apache/solr/search/QParser.java<br>(4 files total) | diff | moved, ff +3 |
| SOLR-16130 | solr-16130-test-followup | 3c48dec4b79 | live PR #5004 | cross-collection export regression test; test-only, the export code is on solr-16130-export-join | solr/core/src/test-files/solr/configsets/ccjoin/conf/solrconfig.xml<br>solr/core/src/test/org/apache/solr/search/join/CrossCollectionJoinQueryTest.java<br>(2 files total) | diff | new |
| SOLR-16267 | solr-16267-submit | 8f4b0c6d2fb | gated, no PR | JSON Facet Stats methods include docs with no field value when using nested function | org/apache/solr/search/ValueSourceParser.java<br>(3 files total) | diff |  |
| SOLR-16570 | solr-16570-submit | 974c44f9608 | awaiting pipeline | expand assertion counts main results only; top_fc hint NPE on docValues-only fields | SOLR-16570-TESTING.md<br>changelog/unreleased/SOLR-16570-collapse-top-fc-docvalues-only.yml<br>solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java<br>(5 files total) | diff | new |
| SOLR-17280 | solr-17280-submit | 40817c5cb7e | gated, no PR | SolrRangeQuery can trigger "IllegalStateException: Recursive update" in CaffeineCache / ConcurrentHashMap | org/apache/solr/query/SolrRangeQuery.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-17311 | solr-17311-submit | 9f7524582f9 | gated, no PR | SortSpectParsing add null field even the sort direction is found | org/apache/solr/search/join/ChildFieldValueSourceParser.java<br>(3 files total) | diff |  |
| SOLR-17796 | solr-17796-submit | 661165d2673 | gated, no PR | Exclusion tag on fq collapse does not work with q.op OR | org/apache/solr/parser/QueryParser.java<br>org/apache/solr/parser/QueryParser.jj<br>(4 files total) | diff |  |
| SOLR-17882 | solr-17882-submit | d328eb392d9 | retire candidate | Coordinator fail to handle ltr queries with error "Cannot invoke \"org.apache.solr.ltr.store.rest.ManagedModel [premise-dead; retire-or-re-aim call pending] | org/apache/solr/ltr/search/LTRQParserPlugin.java<br>(2 files total) | diff |  |

## Schema, analysis and field types (11)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-9349 | solr-9349-submit | 1e79bb42123 | gated, no PR | Schema API should never delete fields used elsewhere in the schema | org/apache/solr/schema/ManagedIndexSchema.java<br>(3 files total) | diff |  |
| SOLR-10131 | solr-10131-submit | b93cf24a9d9 | awaiting pipeline | UUIDField accepts non-hex characters in 36-char values | SOLR-10131-TESTING.md<br>changelog/unreleased/SOLR-10131-uuidfield-validates-hex-digits.yml<br>solr/core/src/java/org/apache/solr/schema/UUIDField.java<br>(4 files total) | diff | new |
| SOLR-10403 | solr-10403-submit | 1e285cd730c | awaiting pipeline | Currency sum : wrong results | org/apache/solr/schema/CurrencyValue.java<br>(4 files total) | diff |  |
| SOLR-14199 | solr-14199-submit | e743c90da79 | PR-ready | Enforce omitNorms for PointFields | org/apache/solr/schema/FieldType.java<br>org/apache/solr/schema/PointField.java<br>(11 files total) | diff |  |
| SOLR-15357 | solr-15357-submit | d717899b873 | gated, no PR | sub-fields of copyField targets should themselves be recorded as targets | org/apache/solr/schema/AbstractSubTypeFieldType.java<br>org/apache/solr/schema/BBoxField.java<br>org/apache/solr/schema/CurrencyFieldType.java<br>(9 files total) | diff | moved, ff +2 |
| SOLR-15358 | solr-15358-submit | cbb345f2e7d | gated, no PR | CurrencyFieldType doesn't support docValues | org/apache/solr/schema/CurrencyFieldType.java<br>(5 files total) | diff |  |
| SOLR-15712 | solr-15712-submit | 555f9cab6b7 | awaiting pipeline | CollationField no longer treats docValues as stored by default | SOLR-15712-TESTING.md<br>changelog/unreleased/SOLR-15712-collationfield-no-docvalues-as-stored.yml<br>solr/core/src/java/org/apache/solr/schema/CollationField.java<br>(4 files total) | diff | new |
| SOLR-15945 | solr-15945-submit | adb0fd450c0 | gated, no PR | DateRangeField type fails to initialize if the field is indexed="false" | org/apache/solr/schema/AbstractSpatialPrefixTreeFieldType.java<br>(4 files total) | diff |  |
| SOLR-16977 | solr-16977-submit | 1142f9563ab | gated, no PR | DenseVector queries fail with unclear error message when either query or document is an all 0’s vector | org/apache/solr/schema/DenseVectorField.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-17047 | solr-17047-submit | 1ba7e33bfe7 | gated, no PR | (SolrCore's) CodecFactory validation ignores schema based KnnVectorsFormat options on init | org/apache/solr/core/SchemaCodecFactory.java<br>org/apache/solr/core/SolrCore.java<br>org/apache/solr/schema/BinaryQuantizedDenseVectorField.java<br>(9 files total)<br>also: Core admin and collections API | diff | moved, ff +1 |
| SOLR-18134 | solr-18134-submit | c5a0bdb21e8 | gated, no PR | ancestor_path field type no longer works; Solr-10 upgrade notes suggested change also does not work | org/apache/solr/analysis/ZeroPositionIncrementFilter.java<br>org/apache/solr/analysis/ZeroPositionIncrementFilterFactory.java<br>(13 files total) | diff | moved, ff +1 |

## Search components (71)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-3044 | solr-3044-submit | 04b877e9de1 | held | Incrementally deprecate NamedList & replace with typesafe API [held: parked, retarget call pending] | org/apache/solr/handler/component/CombinedQueryComponent.java<br>org/apache/solr/handler/component/QueryComponent.java<br>org/apache/solr/util/PivotListEntry.java<br>(6 files total) | diff |  |
| SOLR-4374 | solr-4374-submit | 801c62290f7 | awaiting pipeline | Solr could not support numeric field name in return | org/apache/solr/search/SolrReturnFields.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-5394 | solr-5394-submit | 967445622f9 | awaiting pipeline | facet.method=fcs seems to be using threads when it shouldn't | org/apache/solr/request/SimpleFacets.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-6193 | solr-6193-submit | ec94bf50c80 | gated, no PR | using facet.* parameters as local params inside of facet.field causes problems in distributed search | org/apache/solr/handler/component/PivotFacet.java<br>org/apache/solr/handler/component/PivotFacetField.java<br>org/apache/solr/handler/component/PivotFacetValue.java<br>(5 files total) | diff |  |
| SOLR-6207 | solr-6207-submit | b099a9f1be5 | awaiting pipeline | SolrQueryRequestBase.getParamString() is misleading if an updated value is set | org/apache/solr/request/SolrQueryRequest.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-6759 | solr-6759-submit | 62974ef8d18 | awaiting pipeline | ExpandComponent does not call finish() on DelegatingCollectors | org/apache/solr/handler/component/ExpandComponent.java<br>(6 files total) | diff |  |
| SOLR-6831 | solr-6831-submit | 96b33ba5f6f | PR-ready | Make facet pivots respect timeout from SolrQueryTimeoutImpl | org/apache/solr/handler/component/PivotFacetProcessor.java<br>(3 files total) | diff |  |
| SOLR-6975 | solr-6975-submit | 761aa629bb8 | PR-ready | Sort on _docid_ fails for distributed search | org/apache/solr/handler/component/ShardFieldSortedHitQueue.java<br>(3 files total) | diff |  |
| SOLR-7390 | solr-7390-submit | 7463dd006a7 | PR-ready | Throw an exception when requesting an unknown document transformer | org/apache/solr/search/SolrReturnFields.java<br>(3 files total) | diff |  |
| SOLR-7498 | solr-7498-submit | 2050d8e447a | gated, no PR | Error adding field 'stream_size'='null' msg=For input string: "null" using ContentStreamUpdateRequest | org/apache/solr/handler/extraction/ExtractionBackend.java<br>(3 files total) | diff |  |
| SOLR-7520 | solr-7520-submit | 10b6931e1c0 | gated, no PR | Post filter DelegatingCollector.finish not called for multi-shard queries specifying grouping | org/apache/solr/search/grouping/CommandHandler.java<br>(3 files total) | diff |  |
| SOLR-7550 | solr-7550-submit | 687165651f9 | gated, no PR | PeerSync fails if a replica returns 500 error | org/apache/solr/handler/component/ShardResponse.java<br>org/apache/solr/update/PeerSync.java<br>(4 files total)<br>also: Update processing and atomic updates | diff |  |
| SOLR-8003 | solr-8003-submit | f1c99a44961 | awaiting pipeline |  | org/apache/solr/response/TextResponseWriter.java<br>org/apache/solr/response/transform/DocTransformer.java<br>org/apache/solr/response/transform/DocTransformers.java<br>(8 files total) | diff |  |
| SOLR-8009 | solr-8009-submit | 8795661ddc9 | gated, no PR | RealTimeGet NPE with implicit router-based collection | org/apache/solr/handler/component/RealTimeGetComponent.java<br>(3 files total) | diff |  |
| SOLR-8020 | solr-8020-submit | 79f790523d2 | PR-ready | partial results can generate bad responses | org/apache/solr/handler/component/SearchHandler.java<br>(4 files total) | diff |  |
| SOLR-8051 | solr-8051-submit | 44588ce6719 | awaiting pipeline | Global stats NPE if not all cores are up | org/apache/solr/search/stats/ExactStatsCache.java<br>(4 files total) | diff |  |
| SOLR-8088 | solr-8088-submit | 2398c9bea08 | awaiting pipeline | non-numeric multiValued group field returns 400 | SOLR-8088-TESTING.md<br>changelog/unreleased/SOLR-8088-grouping-multivalued.yml<br>solr/core/src/java/org/apache/solr/search/grouping/distributed/command/SearchGroupsFieldCommand.java<br>(5 files total) | diff | new |
| SOLR-8240 | solr-8240-submit | fad7a1dd8e2 | PR-ready | mapUniqueKeyOnly=true silently ignores field mapping params f=.. when indexing custom JSON | org/apache/solr/handler/loader/JsonLoader.java<br>(4 files total) | diff |  |
| SOLR-8767 | solr-8767-submit | 3b5f2d23573 | gated, no PR | RealTimeGetComponent and stored/copyField exclusion | org/apache/solr/handler/component/RealTimeGetComponent.java<br>(3 files total) | diff |  |
| SOLR-8939 | solr-8939-submit | a855a2d8965 | PR-ready | Date millisecond resolution lost on distributed queries | org/apache/solr/handler/component/QueryComponent.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-8954 | solr-8954-submit | 1d981abe700 | PR-ready | RealTimeGet NPE | org/apache/solr/handler/component/RealTimeGetComponent.java<br>(3 files total) | diff |  |
| SOLR-9124 | solr-9124-submit | 101e12d2085 | awaiting pipeline | Grouped Results does not support ExactStatsCache | org/apache/solr/handler/component/QueryComponent.java<br>(5 files total) | diff |  |
| SOLR-9148 | solr-9148-submit | 30f0d7a42d5 | PR-ready | SQLHandler should pass through filter queries | org/apache/solr/handler/sql/SQLHandler.java<br>org/apache/solr/handler/sql/SolrTable.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-9396 | solr-9396-submit | a5ab2eda4e6 | gated, no PR | [subquery] transformer doesn't automatically request needed fields, only other fields in fl can be used as inp | org/apache/solr/response/transform/SubQueryAugmenterFactory.java<br>(3 files total) | diff |  |
| SOLR-9595 | solr-9595-submit | 7ff1350ab7b | awaiting pipeline | Cache Multi* creations of the SlowCompositeReaderWrapper | org/apache/solr/index/SlowCompositeReaderWrapper.java<br>(4 files total) | diff |  |
| SOLR-9864 | solr-9864-submit | b5826e466b9 | gated, no PR | SolrQuery.getCopy() doesn't copy sortClauses | org/apache/solr/client/solrj/request/SolrQuery.java<br>(3 files total) | diff |  |
| SOLR-10305 | solr-10305-submit | c21ca8c0e75 | awaiting pipeline |  | org/apache/solr/handler/component/QueryComponent.java<br>(4 files total) | diff |  |
| SOLR-10424 | solr-10424-submit | e629ab8bb29 | awaiting pipeline | /update/docs/json is swallowing all fields | SOLR-10424-TESTING.md<br>changelog/unreleased/SOLR-10424-techproducts-json-docs.yml<br>org/apache/solr/handler/TechproductsJsonDocsParamsTest.java<br>(4 files total)<br>also: Configsets and config API | diff | moved, ff +2 |
| SOLR-10492 | solr-10492-submit | ecf21e2192c | gated, no PR | problem with group faceting, facet.limit in solrcloud | org/apache/solr/request/SimpleFacets.java<br>(3 files total) | diff |  |
| SOLR-10694 | solr-10694-submit | 093d90c62de | awaiting pipeline | CSV writer drops map, array and NamedList cells | SOLR-10694-TESTING.md<br>changelog/unreleased/SOLR-10694-csv-structured-values.yml<br>solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java<br>(4 files total) | diff | new |
| SOLR-10844 | solr-10844-submit | e87515c56d0 | PR-ready | group.facet failures when the grouping field is Points based (or Trie w/docValues??) | org/apache/solr/request/SimpleFacets.java<br>(3 files total) | diff |  |
| SOLR-11129 | solr-11129-submit | 6c1356bdff7 | gated, no PR | Distributed facet search with localparm facet.mincount doesn't work in a multi shard cloud env. | org/apache/solr/handler/component/FacetComponent.java<br>(3 files total) | diff |  |
| SOLR-11153 | solr-11153-submit | 093df0d65bd | gated, no PR | Incomplete schema results in mysterious error | org/apache/solr/response/SchemaXmlWriter.java<br>(3 files total) | diff |  |
| SOLR-11310 | solr-11310-submit | e38ddec5279 | gated, no PR | QueryElevation doesn't work with Solr LTR | org/apache/solr/search/ReRankCollector.java<br>(3 files total) | diff |  |
| SOLR-11364 | solr-11364-submit | 562d3e7e685 | gated, no PR | Fields with useDocValuesAsStored=false never be returned in case of pattern matching | org/apache/solr/search/SolrDocumentFetcher.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-11470 | solr-11470-submit | f44c294da37 | gated, no PR | Negative queries always return "No Results" with rq parameter. | org/apache/solr/handler/component/ResponseBuilder.java<br>(3 files total) | diff |  |
| SOLR-12044 | solr-12044-submit | 6185b96e52a | awaiting pipeline | Optimize MatchAllDocsQuery for DocSets more | org/apache/solr/search/SolrIndexSearcher.java<br>(4 files total) | diff |  |
| SOLR-12543 | solr-12543-submit | 88d236db6b7 | gated, no PR | Export Handler errors come back with HTTP 200 | org/apache/solr/handler/ExportHandler.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-12556 | solr-12556-submit | 033ec65a0e1 | gated, no PR | JSON Field Facet refinement can return incorrect counts/stats for sorted buckets -- when using processEmpty | org/apache/solr/search/facet/FacetMerger.java<br>org/apache/solr/search/facet/FacetModule.java<br>org/apache/solr/search/facet/FacetRequestSortedMerger.java<br>(5 files total) | diff |  |
| SOLR-13245 | solr-13245-submit | 16e62ab6542 | PR-ready | Status checking of streaming daemon-s is buggy and misleading | org/apache/solr/handler/StreamHandler.java<br>(3 files total) | diff |  |
| SOLR-13568 | solr-13568-submit | ac5d60c214c | live PR #5014 | Expand component should not cache group queries in the filter cache | org/apache/solr/handler/component/ExpandComponent.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-13851 | solr-13851-submit | b60f4d642d1 | held | SolrIndexSearcher.getFirstMatch trips assertion if multiple matches [held: submission-held per receipts] | org/apache/solr/search/SolrIndexSearcher.java<br>(4 files total) | diff |  |
| SOLR-13876 | solr-13876-submit | 2e110473dba | gated, no PR | MaxScore is always NaN in expand component | org/apache/solr/handler/component/ExpandComponent.java<br>(3 files total) | diff |  |
| SOLR-14381 | solr-14381-submit | a28b3f672cb | gated, no PR | Handle integer overflow in grouping | org/apache/solr/handler/component/ResponseBuilder.java<br>org/apache/solr/search/Grouping.java<br>org/apache/solr/search/grouping/CommandHandler.java<br>(19 files total) | diff | moved, ff +2 |
| SOLR-14451 | solr-14451-submit | e60891d8716 | gated, no PR | debug=query does not reliably ensure json.facet debug info returned in cloud clusters | org/apache/solr/handler/component/DebugComponent.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-14678 | solr-14678-submit | 5d94e6cf398 | gated, no PR | [child] DocTransformer doesn't work unless (request) fl inlcudes '*' | org/apache/solr/response/transform/ChildDocTransformer.java<br>org/apache/solr/response/transform/DocTransformer.java<br>org/apache/solr/response/transform/DocTransformers.java<br>(6 files total) | diff | moved, ff +4 |
| SOLR-14931 | solr-14931-submit | 1a7d678d9a1 | gated, no PR | Macros in appends/invariants parameters not getting expanded in Solrcloud | org/apache/solr/request/json/RequestUtil.java<br>org/apache/solr/request/macro/MacroExpander.java<br>(5 files total) | diff | moved, ff +4 |
| SOLR-15018 | solr-15018-submit | d0f29b4630c | gated, no PR | Atomic update deletes child documents if schema has catch-all ignore field | org/apache/solr/handler/component/RealTimeGetComponent.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-15041 | solr-15041-submit | 55fca0a7c24 | gated, no PR | CSV update handler can't handle line breaks/new lines together with field split/separators for multivalued fie | org/apache/solr/handler/loader/CSVLoaderBase.java<br>(3 files total) | diff |  |
| SOLR-15144 | solr-15144-submit | 68b0fc31e05 | gated, no PR | "timeAllowed" param with "numFound" having a count value but doc list is empty | org/apache/solr/search/TimeAllowedLimit.java<br>(3 files total) | diff |  |
| SOLR-15319 | solr-15319-submit | 4bda91f46fc | gated, no PR | ExactStatsCache not always producing Distributed IDF | org/apache/solr/search/stats/ExactStatsCache.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-15331 | solr-15331-submit | 6769b4cd8a1 | gated, no PR | 'Missing' count lost when convert facetResponse. | org/apache/solr/client/solrj/response/json/BucketBasedJsonFacet.java<br>(3 files total) | diff |  |
| SOLR-15479 | solr-15479-submit | 57bd53ce4d0 | gated, no PR | rq/rerank does not properly adjust maxScore | org/apache/solr/search/SolrIndexSearcher.java<br>(3 files total) | diff | moved, ff +2 |
| SOLR-15895 | solr-15895-submit | 02930909397 | gated, no PR | Managed Resources with invalid filename characters on Windows | org/apache/solr/rest/ManagedResourceStorage.java<br>org/apache/solr/rest/RestManager.java<br>(5 files total) | diff |  |
| SOLR-16155 | solr-16155-submit | 0881de1ed68 | gated, no PR | DocumentBuilder should not include field values in error messages | org/apache/solr/handler/loader/JavabinLoader.java<br>org/apache/solr/update/DocumentBuilder.java<br>(6 files total)<br>also: Update processing and atomic updates | diff |  |
| SOLR-16290 | solr-16290-submit | ff760120c81 | held | gated pin, awaiting Nick's disposition [held: gated pin; pin PR, real fix, or bank call pending] | org/apache/solr/search/facet/TestJsonFacetsWithNestedObjects.java | diff |  |
| SOLR-16444 | solr-16444-submit | 8ffee94a5e7 | PR-ready | intermittent document update failures with IllegalStateException from ManagedStopFilterFactory (symptom as rec | org/apache/solr/rest/RestManager.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-17051 | solr-17051-submit | 148544e9ed5 | gated, no PR | Min Facet count setting is ignored when using json facet Api for "missing" count | org/apache/solr/search/facet/FacetFieldMerger.java<br>org/apache/solr/search/facet/FacetFieldProcessor.java<br>(4 files total) | diff |  |
| SOLR-17055 | solr-17055-submit | f0c401a290b | awaiting pipeline | KnnVectorQuery: Wrong number of search results when running in cloud-mode | org/apache/solr/handler/component/QueryComponent.java<br>org/apache/solr/search/vector/SolrKnnByteVectorQuery.java<br>org/apache/solr/search/vector/SolrKnnFloatVectorQuery.java<br>(6 files total) | diff |  |
| SOLR-17155 | solr-17155-submit | 1413237f7a7 | gated, no PR | Groupby query reports null pointer when unique key is not stored | org/apache/solr/search/grouping/distributed/shardresultserializer/TopGroupsResultTransformer.java<br>(3 files total) | diff |  |
| SOLR-17372 | solr-17372-submit | 048862fda8a | gated, no PR | Reproducing failure in StatsComponentTest.testPercentiles | SOLR-17372-TESTING.md<br>org/apache/solr/handler/component/StatsComponentTest.java | diff | moved, ff +1 |
| SOLR-17539 | solr-17539-submit | f9d201a278b | live PR #4998 | Increased memory usage after upgrading to Solr 9.6.0 - DocValuesIteratorCache | org/apache/solr/core/SolrConfig.java<br>org/apache/solr/handler/component/RealTimeGetComponent.java<br>org/apache/solr/search/DocValuesIteratorCache.java<br>(13 files total)<br>also: Configsets and config API | diff | moved, ff +1 |
| SOLR-17748 | solr-17748-submit | dfacaf34766 | gated, no PR | NPE in QueryComponent.returnFields with shards.info | org/apache/solr/handler/component/QueryComponent.java<br>(4 files total) | diff |  |
| SOLR-17791 | solr-17791-submit | a39c1c97376 | gated, no PR | Features always end up in default-store(_DEFAULT_) | org/apache/solr/rest/ManagedResource.java<br>org/apache/solr/rest/RestManager.java<br>org/apache/solr/ltr/store/rest/ManagedFeatureStore.java<br>(8 files total)<br>also: Replication and backup | diff |  |
| SOLR-17841 | solr-17841-submit | 478731e6760 | retire candidate | Investigate multithreaded search performance bottlenecks [retire call pending] | org/apache/solr/bench/search/NumericSearch.java<br>(3 files total) | diff |  |
| SOLR-17976 | solr-17976-submit | 56ea43c448e | gated, no PR | Solr 9.5 distributed search tie breaking logic is non-deterministic | org/apache/solr/handler/component/CombinedQueryComponent.java<br>org/apache/solr/handler/component/QueryComponent.java<br>org/apache/solr/handler/component/ShardDoc.java<br>(7 files total) | diff |  |
| SOLR-18109 | solr-18109-submit | b19395e1f60 | gated, no PR | Fix deprecated SolrPluginUtils.doStandardDebug | org/apache/solr/handler/MoreLikeThisHandlerTest.java<br>org/apache/solr/handler/component/DebugComponentTest.java | diff |  |
| SOLR-18196 | solr-18196-submit | f3248fe2310 | retire candidate | Java serialization broken for QueryResponse - SimpleOrderedMap implements Map breaks ObjectOutputStream  [PR #4995 merged; register row 38 retire candidate] | org/apache/solr/client/solrj/response/QueryResponseTest.java<br>org/apache/solr/common/util/SimpleOrderedMapTest.java<br>also: SolrJ and clients | diff |  |
| SOLR-18356 | solr-18356-submit | c179cd35713 | retire candidate | Remove DocsStreamer 2-arg convertLuceneDocToSolrDoc overload [branch deletion pending Nick call] | org/apache/solr/response/DocsStreamer.java<br>(3 files total) | diff |  |
| SOLR-18482 | solr-18482-submit | 49ca9099d8e | live PR #5009 | JSON range facet silently ignores `limit`, `offset`, `sort` and the refine parameters | org/apache/solr/search/facet/FacetRangeParser.java<br>(4 files total) | diff |  |
| SOLR-18506 | solr-18506-submit | 77c019e1ff0 | live PR #5029 | TestThinCache.testSimple is flaky: a warming-phase eviction can hit the second cache's own scope | org/apache/solr/search/TestThinCache.java | diff |  |

## SolrCloud, overseer and cluster state (31)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-3865 | solr-3865-submit | 363e8f0651e | awaiting pipeline | CloudSolrServer connection leak when using wrong zk connection string | org/apache/solr/client/solrj/impl/ZkClientClusterStateProvider.java<br>(4 files total) | diff |  |
| SOLR-4754 | solr-4754-submit | d2e9038881f | awaiting pipeline | normalizeHostName accepts a blank host | SOLR-4754-TESTING.md<br>changelog/unreleased/SOLR-4754-fail-on-empty-host.yml<br>solr/core/src/java/org/apache/solr/cloud/ZkController.java<br>(4 files total) | diff | new |
| SOLR-5813 | solr-5813-submit | 90b8baa08ae | awaiting pipeline | Creating a SolrCore with a collection name of empty string should fail nicely in SolrCloud mode. | org/apache/solr/cloud/CloudDescriptor.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-7394 | solr-7394-recovery | 9857d9ee802 | live PR #5003 | clear recovering shard-terms entry when recovery is abandoned | changelog/unreleased/SOLR-7394.yml<br>solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java<br>solr/core/src/java/org/apache/solr/cloud/ShardTerms.java<br>(9 files total) | diff | new |
| SOLR-9155 | solr-9155-submit | 9f08d033023 | PR-ready | Improve ZkController::getLeader exception handling | org/apache/solr/cloud/ZkController.java<br>(3 files total) | diff |  |
| SOLR-10234 | solr-10234-submit | 16825538a76 | awaiting pipeline | "Too many open files" in distrib tests due to fixed HandleLimitFS (regardless of num nodes in test) | org/apache/solr/BaseDistributedSearchTestCase.java<br>org/apache/solr/cloud/SolrCloudTestCase.java<br>(5 files total) | diff |  |
| SOLR-10641 | solr-10641-submit | 4ab4bd2040f | awaiting pipeline | OverseerTaskQueue.remove does setData and delete in one multi | SOLR-10641-TESTING.md<br>changelog/unreleased/SOLR-10641-overseer-task-queue-remove-multi.yml<br>solr/core/src/java/org/apache/solr/cloud/OverseerTaskQueue.java<br>(4 files total) | diff | new |
| SOLR-11288 | solr-11288-submit | cd094c3c623 | PR-ready | String.split(",") used in inappropriate places | org/apache/solr/cloud/api/collections/BalanceReplicasCmd.java<br>org/apache/solr/cloud/api/collections/MigrateReplicasCmd.java<br>org/apache/solr/handler/admin/ClusterStatus.java<br>(5 files total)<br>also: Core admin and collections API | diff |  |
| SOLR-11479 | solr-11479-submit | 7e538e844c4 | awaiting pipeline | Collections API: ADDREPLICA fails if you try to specify property.coreNodeName=foo | org/apache/solr/cloud/api/collections/AddReplicaCmd.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-12651 | solr-12651-submit | f3131d1ee84 | gated, no PR | Restore collection should clean up if the operation failed | org/apache/solr/cloud/api/collections/RestoreCmd.java<br>(3 files total) | diff | moved, ff +2 |
| SOLR-12991 | solr-12991-submit | 1a86966179e | PR-ready | RecoveryStrategy logs the cause when the leader is unreachable | org/apache/solr/cloud/RecoveryStrategy.java<br>(3 files total) | diff |  |
| SOLR-12998 | solr-12998-submit | 62a17a116b5 | live PR #5010 | Race condition between requery recovery and the core load completing | org/apache/solr/cloud/SyncStrategy.java<br>org/apache/solr/handler/admin/CoreAdminOperation.java<br>(5 files total)<br>also: Core admin and collections API | diff | moved, ff +1 |
| SOLR-13136 | solr-13136-submit | 486b3877556 | live PR #5017 | Queries fail during shard creation [testcase included] | org/apache/solr/cloud/api/collections/CreateShardCmd.java<br>org/apache/solr/cloud/overseer/CollectionMutator.java<br>org/apache/solr/handler/component/ShardResponse.java<br>(6 files total)<br>also: Search components | diff | moved, ff +1 |
| SOLR-13186 | solr-13186-submit | b436d90d2a8 | PR-ready | When a node wins the Overseer election there is a race that can cause an invalid Overseer leader node to be re | org/apache/solr/cloud/OverseerElectionContext.java<br>(3 files total) | diff |  |
| SOLR-13239 | solr-13239-submit | 699a1fce368 | held | CollectionStateWatcher reports new collections before they really exist [held: submission-held per receipts] | org/apache/solr/common/cloud/ZkStateReader.java<br>(4 files total) | diff |  |
| SOLR-13369 | solr-13369-submit | dfa0db5bdf9 | gated, no PR | TriLevelCompositeIdRoutingTest failure: same route prefix maped to multiple shards | SOLR-13369-TESTING.md<br>changelog/unreleased/SOLR-13369-trilevel-composite-id-routing-test.yml<br>org/apache/solr/cloud/TriLevelCompositeIdRoutingTest.java | diff | moved, ff +1 |
| SOLR-14919 | solr-14919-submit | 84e7bcaeb57 | gated, no PR | IgnoreCommitOptimizeUpdateProcessor does not work during replication | org/apache/solr/cloud/RecoveryStrategy.java<br>org/apache/solr/update/processor/IgnoreCommitOptimizeUpdateProcessorFactory.java<br>(4 files total)<br>also: Update processing and atomic updates | diff |  |
| SOLR-15035 | solr-15035-submit | 12d7491e82c | gated, no PR | core.properties different when using ADDREPLICA .vs. when the replica created with CREATE | org/apache/solr/cloud/api/collections/AddReplicaCmd.java<br>org/apache/solr/handler/admin/CoreAdminHandler.java<br>(4 files total)<br>also: Core admin and collections API | diff |  |
| SOLR-15106 | solr-15106-submit | 40b7e5d0efa | gated, no PR | Thread in OverseerTaskProcessor should not "return" | org/apache/solr/cloud/Overseer.java<br>(4 files total) | diff | moved, not ff (+3) |
| SOLR-15386 | solr-15386-submit | ca8cb61ee95 | gated, no PR | Internal DOWNNODE request will mark replicas down even if their host node is now live | org/apache/solr/cloud/ZkController.java<br>org/apache/solr/cloud/overseer/NodeMutator.java<br>(5 files total) | diff | moved, not ff (+4) |
| SOLR-15674 | solr-15674-submit | ba01c83d4c5 | gated, no PR | When attempting to recreate a deleted Solr Collection with a different initial schema the old schema appears t | org/apache/solr/cloud/ZkSolrResourceLoader.java<br>org/apache/solr/core/SolrConfig.java<br>org/apache/solr/schema/IndexSchemaFactory.java<br>(5 files total)<br>also: Configsets and config API | diff |  |
| SOLR-15863 | solr-15863-submit | f381fd8d4dd | PR-ready | backup properties record the oldest segment Lucene version (Jira title not recorded in the branch report) | org/apache/solr/cloud/api/collections/BackupCmd.java<br>org/apache/solr/core/backup/BackupProperties.java<br>org/apache/solr/handler/IncrementalShardBackup.java<br>(5 files total)<br>also: Replication and backup | diff | moved, ff +2 |
| SOLR-16013 | solr-16013-submit | ba26b702891 | gated, no PR | Overseer gives up election node before closing - inflight commands can be processed twice | org/apache/solr/cloud/ZkController.java<br>(3 files total) | diff | moved, ff +2 |
| SOLR-16437 | solr-16437-submit | 673ae584de0 | gated, no PR | ADDREPLICAPROP does not sanity-check inputs | org/apache/solr/cloud/api/collections/CollApiCmds.java<br>(4 files total) | diff |  |
| SOLR-17281 | solr-17281-submit | ba21ca32791 | held | Solr elected a down replica as a leader [PARKED, under review on Nick side] | org/apache/solr/cloud/ShardLeaderElectionContext.java<br>(2 files total) | diff |  |
| SOLR-17292 | solr-17292-submit | e43200b0fb6 | gated, no PR | PerReplicaStatesOps.persist should propagate failure | org/apache/solr/common/cloud/PerReplicaStatesOps.java<br>(3 files total) | diff |  |
| SOLR-17680 | solr-17680-submit | f4b8ce83365 | gated, no PR | Unable to create Dimensional Routed Alias(DRA) | org/apache/solr/cloud/api/collections/CreateAliasCmd.java<br>org/apache/solr/handler/admin/api/CreateAlias.java<br>(5 files total)<br>also: Core admin and collections API | diff |  |
| SOLR-17733 | solr-17733-submit | 636196b7954 | gated, no PR | Cluster filestore v2 API fails in cloud mode | org/apache/solr/filestore/DistribFileStore.java<br>(3 files total) | diff |  |
| SOLR-18277 | solr-18277-submit | 17c0e644812 | retire candidate | SplitShard cleanupAfterFailure race flaw [PR #4959 merged] | org/apache/solr/cloud/api/collections/SplitShardCmd.java<br>(4 files total) | diff |  |
| SOLR-18391 | solr-18391-submit | 3000eeede7a | live PR #4997 | Fail collection creation cleanly when the collection config already exists (graceful create split) | org/apache/solr/cluster/placement/impl/PlacementPluginAssignStrategy.java<br>(2 files total) | diff |  |
| SOLR-18391 | solr-18391-graceful-create-submit | adcda10b501 | live PR #5027 | Clean up a collection when creation fails partway (graceful create, PR #5027 branch) | org/apache/solr/cloud/api/collections/CreateCollectionCmd.java<br>org/apache/solr/cluster/placement/impl/PlacementPluginAssignStrategy.java<br>(6 files total) | diff | moved, ff +3 |

## Core admin and collections API (33)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-4502 | solr-4502-submit | 4491f5162c1 | awaiting pipeline | ShardHandlerFactory not initialized in CoreContainer when creating a Core manually. | org/apache/solr/core/CoreContainer.java<br>(4 files total) | diff |  |
| SOLR-4989 | solr-4989-submit | 5bac95376cd | PR-ready | Implement show=ALL in LukeRequestHandler | org/apache/solr/handler/admin/LukeRequestHandler.java<br>(3 files total) | diff |  |
| SOLR-5011 | solr-5011-submit | f20ffe48078 | awaiting pipeline | Manage to close all ResourceLoaders when cores are unloaded/reloaded | org/apache/solr/core/SolrCore.java<br>org/apache/solr/core/SolrResourceLoader.java<br>(5 files total) | diff |  |
| SOLR-5262 | solr-5262-submit | ade8b80264a | awaiting pipeline |  | org/apache/solr/core/CoreDescriptor.java<br>(4 files total) | diff |  |
| SOLR-6438 | solr-6438-submit | 8c77988d591 | gated, no PR | MergeIndex command ignores srcCore values when both indexDir and srcCore are mentioned | org/apache/solr/handler/admin/api/MergeIndexes.java<br>(3 files total) | diff |  |
| SOLR-8275 | solr-8275-submit | e52e10fa50a | PR-ready | Unclear error message during recovery | org/apache/solr/handler/admin/PrepRecoveryOp.java<br>(3 files total) | diff |  |
| SOLR-8554 | solr-8554-submit | 32697b6c3f8 | awaiting pipeline |  | org/apache/solr/handler/admin/api/ForceLeader.java<br>(4 files total) | diff |  |
| SOLR-8576 | solr-8576-submit | 4c46f95c785 | awaiting pipeline | Add additional Collection API error testing for collection already exists and related. | SOLR-8576-TESTING.md<br>changelog/unreleased/SOLR-8576-create-collection-exists-tests.yml<br>org/apache/solr/cloud/CollectionsAPISolrJTest.java<br>also: SolrCloud, overseer and cluster state | diff | moved, ff +1 |
| SOLR-8628 | solr-8628-submit | ce8211e05e0 | awaiting pipeline | index dir holding only write.lock fails to open | SOLR-8628-TESTING.md<br>changelog/unreleased/SOLR-8628-index-dir-with-only-write-lock.yml<br>solr/core/src/java/org/apache/solr/core/SolrCore.java<br>(4 files total) | diff | new |
| SOLR-9750 | solr-9750-submit | f97da6da14a | gated, no PR | The paramset for the /graph implicit RequestHandler is named _ADMIN_GRAPH but should be _GRAPH | changelog/unreleased/SOLR-9750-graph-paramset-name.yml<br>solr/core/src/resources/ImplicitPlugins.json<br>org/apache/solr/core/TestImplicitPlugins.java<br>(4 files total)<br>also: Configsets and config API | diff |  |
| SOLR-11431 | solr-11431-core-init-503 | 968fad873c4 | live PR #5002 | report 503 instead of 500 for a core that fails to initialize | changelog/unreleased/SOLR-11431.yml<br>solr/core/src/java/org/apache/solr/core/CoreContainer.java<br>solr/core/src/java/org/apache/solr/core/SolrCoreInitializationException.java<br>(4 files total) | diff | new |
| SOLR-11939 | solr-11939-submit | d4cff5e7643 | awaiting pipeline | Collection API: property.name ignored when creating collections | SOLR-11939-TESTING.md<br>changelog/unreleased/SOLR-11939-property-name-docs.yml<br>solr/solr-ref-guide/modules/deployment-guide/pages/collection-management.adoc | diff |  |
| SOLR-12007 | solr-12007-submit | bdeba582fd6 | gated, no PR | When a SolrCore is closed, cleanupOldIndexDirectories is called in a background thread that will race with Dir | org/apache/solr/core/SolrCore.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-12849 | solr-12849-submit | 6b92223bc24 | live PR #5011 | collection parameter referencing an alias being handled differently when sent as GET than when sent as POST | org/apache/solr/servlet/HttpSolrCall.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-12916 | solr-12916-submit | ebe5db37433 | awaiting pipeline | Fail to parse queries for listeners added via Config API | org/apache/solr/core/QuerySenderListener.java<br>(4 files total) | diff |  |
| SOLR-13097 | solr-13097-submit | f0e7395f58f | live PR #5016 | RuleBasedAuthorizationPlugin is not fully fonctionnal in Solr standalone mode | org/apache/solr/servlet/HttpSolrCall.java<br>(5 files total) | diff | moved, ff +1 |
| SOLR-13246 | solr-13246-submit | 6817c6c0c26 | PR-ready | Reduce QuerySenderListener log messages | org/apache/solr/core/QuerySenderListener.java<br>org/apache/solr/search/SolrIndexSearcher.java<br>(4 files total)<br>also: Search components | diff |  |
| SOLR-14098 | solr-14098-submit | dd4995515e8 | gated, no PR | inconsistent replica state treatment in RequestApplyUpdatesOp | org/apache/solr/handler/admin/RequestApplyUpdatesOp.java<br>(4 files total) | diff |  |
| SOLR-15003 | solr-15003-submit | 1004abee39a | gated, no PR | SolrCloud Snapshot metadata inconsistent after core replication | org/apache/solr/core/SolrCore.java<br>org/apache/solr/handler/IndexFetcher.java<br>(4 files total)<br>also: Replication and backup | diff | moved, ff +3 |
| SOLR-15024 | solr-15024-submit | f95b5010b3f | gated, no PR | Admin UI doesnt' show CharFilters correctly | org/apache/solr/handler/admin/LukeRequestHandler.java<br>(4 files total) | diff |  |
| SOLR-15805 | solr-15805-submit | 3432f950f0a | gated, no PR | SolrCloud node can be zombie with some startup exceptions | org/apache/solr/servlet/CoreContainerProvider.java<br>(3 files total) | diff |  |
| SOLR-16108 | solr-16108-submit | 70ad7371b31 | gated, no PR | Incorrect distribution of records in shards after a split with splitByKeyprefix, when using the CompositeId ro | org/apache/solr/handler/admin/SplitOp.java<br>(3 files total) | diff |  |
| SOLR-16499 | solr-16499-submit | 6a2ff7618d9 | gated, no PR | REPLACENODE API doesn't obey 'parallel', 'timeout' params | org/apache/solr/client/api/model/ReplaceNodeRequestBody.java<br>org/apache/solr/handler/admin/CollectionsHandler.java<br>org/apache/solr/handler/admin/api/ReplaceNode.java<br>(6 files total)<br>also: SolrJ and clients | diff |  |
| SOLR-16725 | solr-16725-submit | be1838ef8cc | gated, no PR | CLUSTERSTATUS API output has inconsistent data types for a few values against the newly restored collection | org/apache/solr/handler/admin/ClusterStatus.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-16849 | solr-16849-submit | d612b055da2 | gated, no PR | COLSTATUS API is not working for readonly collection | org/apache/solr/handler/admin/SegmentsInfoRequestHandlerTest.java | diff |  |
| SOLR-16887 | solr-16887-submit | 42675f65d6f | gated, no PR | The new "Crash On Out Of Memory Error" capability breaks auto-restart on the docker container | org/apache/solr/servlet/CoreContainerProvider.java<br>(8 files total) | diff | moved, ff +2 |
| SOLR-17297 | solr-17297-submit | c0ab38fc0a8 | gated, no PR | Classloading issue with plugin and modules | org/apache/solr/core/NodeConfig.java<br>(3 files total) | diff |  |
| SOLR-17377 | solr-17377-submit | 22b5f209a11 | gated, no PR | ClusterSingleton defined in solr.xml cannot be loaded from a module | org/apache/solr/core/NodeConfig.java<br>org/apache/solr/core/SolrXmlConfig.java<br>(4 files total) | diff |  |
| SOLR-17708 | solr-17708-submit | 10a7fa07a79 | gated, no PR | JAX-RS v2 APIs go through authorization twice | org/apache/solr/api/V2HttpCall.java<br>org/apache/solr/servlet/HttpSolrCall.java<br>(5 files total) | diff | moved, ff +2 |
| SOLR-17731 | solr-17731-submit | f2b4ba164f5 | gated, no PR | Some v2 APIs are overshadowed at runtime so can't be used | org/apache/solr/client/api/endpoint/CollectionSnapshotApis.java<br>org/apache/solr/client/api/endpoint/GetAliasByNameApi.java<br>org/apache/solr/client/api/endpoint/ListAliasesApi.java<br>(10 files total)<br>also: SolrJ and clients | diff | moved, ff +3 |
| SOLR-18010 | solr-18010-submit | c3685bb37d9 | gated, no PR | Adding a new role corrupts security.json file | org/apache/solr/handler/admin/SecurityConfHandlerLocal.java<br>(4 files total) | diff |  |
| SOLR-18278 | solr-18278-submit | bf19c9fa449 | retire candidate | SOLR JETTY dependency decoupling didn't work properly [retire call pending] | org/apache/solr/core/HttpSolrClientProvider.java | diff |  |
| SOLR-18317 | solr-18317-server-submit | ae918a03fa7 | held | standalone solr 10 logging UI tries to access zookeeper: NullPointerException (server-side branch variant) [banked rebuilt history, not the PR branch] | org/apache/solr/handler/admin/LoggingHandler.java<br>org/apache/solr/handler/admin/MetricsHandler.java<br>org/apache/solr/handler/admin/SystemInfoHandler.java<br>(16 files total) | diff |  |

## Replication and backup (13)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-5589 | solr-5589-submit | c707aa95e2a | awaiting pipeline | Disabled replication in config is ignored | org/apache/solr/handler/ReplicationHandler.java<br>(5 files total) | diff |  |
| SOLR-6711 | solr-6711-submit | 5a120cd69d6 | awaiting pipeline | [ Legacy Scaling and Distribution] HTTP API  replication?command=disablereplication  & replication?command=dis | org/apache/solr/handler/IndexFetcher.java<br>org/apache/solr/handler/ReplicationHandler.java<br>(6 files total) | diff |  |
| SOLR-8430 | solr-8430-submit | 49af21be589 | awaiting pipeline | ReplicationHandler throttling should be applied across all concurrent replication requests | org/apache/solr/handler/admin/api/ReplicationAPIBase.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-9091 | solr-9091-submit | e31bdaa4d27 | awaiting pipeline |  | org/apache/solr/handler/RestoreCore.java<br>(4 files total) | diff |  |
| SOLR-9382 | solr-9382-submit | c0b5fec1be2 | awaiting pipeline | Replication of managed resources on standalone servers | org/apache/solr/handler/ReplicationHandler.java<br>(4 files total) | diff |  |
| SOLR-9598 | solr-9598-submit | c8407773f76 | PR-ready | Solr RESTORE api doesn't wait for the restored collection to be fully ready for usage | org/apache/solr/client/api/model/RestoreCollectionRequestBody.java<br>org/apache/solr/cloud/api/collections/RestoreCmd.java<br>org/apache/solr/handler/admin/api/RestoreCollection.java<br>(5 files total)<br>also: SolrCloud, overseer and cluster state | diff |  |
| SOLR-9865 | solr-9865-submit | 4937608bb18 | gated, no PR | RestoreCore failing can roll an index back in time. | org/apache/solr/handler/RestoreCore.java<br>(3 files total) | diff |  |
| SOLR-11650 | solr-11650-submit | e4f5e941cd8 | gated, no PR | Credentials used for BasicAuth displayed in clear text on slave nodes | org/apache/solr/handler/ReplicationHandler.java<br>org/apache/solr/common/util/URLUtil.java<br>(5 files total)<br>also: SolrJ and clients | diff | moved, ff +1 |
| SOLR-12085 | solr-12085-submit | c8dba502339 | gated, no PR | IndexFetcher does not honor SolrDeletionPolicy | org/apache/solr/handler/IndexFetcher.java<br>(3 files total) | diff |  |
| SOLR-12246 | solr-12246-submit | 3ffc2539ec7 | gated, no PR | Any full recovery complains about checksum mismatch for a .liv file | org/apache/solr/handler/IndexFetcher.java<br>(3 files total) | diff |  |
| SOLR-17287 | solr-17287-submit | 6957daf8261 | gated, no PR | RESTORECORE should reset/clear the UpdateLog | org/apache/solr/handler/RestoreCore.java<br>org/apache/solr/update/UpdateLog.java<br>(6 files total)<br>also: Update processing and atomic updates | diff | moved, ff +2 |
| SOLR-18249 | solr-18249-submit | deffea4c51b | live PR #4969 | Fix non-atomic write in ShardBackupMetadata.store() that can destroy backup metadata | org/apache/solr/cloud/api/collections/DeleteBackupCmd.java<br>org/apache/solr/core/backup/ShardBackupMetadata.java<br>org/apache/solr/core/backup/repository/BackupRepository.java<br>(9 files total) | diff | moved, ff +1 |
| SOLR-18280 | solr-18280-submit | 0da92abd9e9 | live PR #4996 | reproducible TestReplicationHandler.testUrlAllowList failures | org/apache/solr/handler/ReplicationTestHelper.java<br>org/apache/solr/handler/TestReplicationHandler.java<br>org/apache/solr/handler/TestReplicationHandlerUrlAllowList.java | diff |  |

## Streaming expressions (11)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-9852 | solr-9852-submit | 31f58dbe8e6 | awaiting pipeline | Solr JDBC doesn't implement columns' metadata | org/apache/solr/client/solrj/io/sql/DatabaseMetaDataImpl.java<br>(4 files total) | diff |  |
| SOLR-10322 | solr-10322-submit | 80ce9d7a3c8 | PR-ready | Streaming expressions Daemon can't connect to topic checkpoint when basic authentication is enabled | org/apache/solr/client/solrj/io/stream/TopicStream.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-10882 | solr-10882-submit | 83fc3dfeb24 | awaiting pipeline |  | org/apache/solr/client/solrj/io/eval/ArrayEvaluator.java<br>(4 files total) | diff |  |
| SOLR-11922 | solr-11922-submit | da29a963236 | held | parallel - cartesianProduct [held: premise-dead, retire-or-keep call pending] | SOLR-11922-TESTING.md<br>org/apache/solr/client/solrj/io/stream/StreamDecoratorTest.java | diff |  |
| SOLR-12505 | solr-12505-submit | 5f20e1171bc | gated, no PR | Streaming expressions - fetch() does not work as expected | org/apache/solr/client/solrj/io/stream/FetchStream.java<br>(3 files total) | diff |  |
| SOLR-12657 | solr-12657-submit | 16a0a69e839 | gated, no PR | Facet streaming expression doesn't support min / max correctly for date fields. | org/apache/solr/client/solrj/io/stream/FacetStream.java<br>(3 files total) | diff | moved, ff +2 |
| SOLR-13524 | solr-13524-submit | f08e7c12a8e | live PR #5013 | Or Stream Evaluator produces incorrect results with more than 2 arguments | org/apache/solr/client/solrj/io/eval/OrEvaluator.java<br>org/apache/solr/client/solrj/io/eval/RecursiveBooleanEvaluator.java<br>(4 files total) | diff | moved, ff +1 |
| SOLR-14200 | solr-14200-submit | 10991af9bda | retire candidate | Shard Tolerance in Streaming API [retire call pending] | org/apache/solr/client/solrj/io/stream/ParallelStream.java<br>org/apache/solr/client/solrj/io/stream/SqlStream.java<br>org/apache/solr/client/solrj/io/stream/TupleStream.java<br>(5 files total) | diff |  |
| SOLR-14231 | solr-14231-submit | 2f73d00a399 | gated, no PR | The scoreNodes function fails with only one result and should use a POST rather then GET | org/apache/solr/client/solrj/io/stream/ScoreNodesStream.java<br>(4 files total) | diff |  |
| SOLR-15326 | solr-15326-submit | dab6a466316 | retire candidate | Missing one record in solr streaming expressions [retire call pending] | org/apache/solr/client/solrj/io/stream/CloudSolrStream.java<br>(3 files total) | diff |  |
| SOLR-17433 / SOLR-17143 | solr-17433-17143-submit | 42b6c4fc915 | retire candidate | SolrStream by default creates a Http2SolrClient that can only stream for 60 seconds / Streaming with multiple  [retire call pending] | org/apache/solr/client/solrj/io/SolrClientCache.java<br>(3 files total) | diff |  |

## SolrJ and clients (24)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-2018 | solr-2018-submit | 213457aa91f | awaiting pipeline | waitFlush="false" currently doens't work | org/apache/solr/client/solrj/SolrClient.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-3498 | solr-3498-submit | 812598302de | awaiting pipeline | ContentWriterUpdateRequest.setCommitWithin is a no-op | SOLR-3498-TESTING.md<br>changelog/unreleased/SOLR-3498-content-writer-commit-within.yml<br>solr/solrj/src/java/org/apache/solr/client/solrj/request/ContentWriterUpdateRequest.java<br>(4 files total) | diff | new |
| SOLR-3722 | solr-3722-submit | f9d3dda1d3d | PR-ready | NPE from NamedList constructor if shard fails to return auxiliary data about all docs | org/apache/solr/common/util/NamedList.java<br>(3 files total) | diff |  |
| SOLR-3999 | solr-3999-submit | e3af6f36e31 | awaiting pipeline | No serialVersionUID in SolrInputDocument and SolrInputField | org/apache/solr/common/SolrDocument.java<br>org/apache/solr/common/SolrDocumentBase.java<br>org/apache/solr/common/SolrInputDocument.java<br>(7 files total) | diff | moved, not ff (+1) |
| SOLR-4335 | solr-4335-submit | 8f6e267d648 | PR-ready | Solrj UpdateRequest can send illegal XML to Solr | org/apache/solr/common/util/XML.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-4336 | solr-4336-submit | 798618aa08f | PR-ready | 4.1 no longer treats blank request params the same way as 4.0 | org/apache/solr/common/params/SolrParams.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-4422 | solr-4422-submit | 3e5b7afecdd | PR-ready | SolrJ DocumentObjectBinder class loses Map.Entry order when repopulating dynamic field values | org/apache/solr/client/solrj/beans/DocumentObjectBinder.java<br>(3 files total) | diff |  |
| SOLR-4424 | solr-4424-submit | 2e947b7f622 | PR-ready | Solr should complain if a parameter has no name in solrconfig.xml | org/apache/solr/common/util/NamedList.java<br>(3 files total) | diff |  |
| SOLR-5220 | solr-5220-submit | e415c409044 | awaiting pipeline | Marking server as zombie due to 4xx response is odd | org/apache/solr/client/solrj/impl/LBSolrClient.java<br>(4 files total) | diff | moved, not ff (+2) |
| SOLR-6046 | solr-6046-submit | 3b833e369a0 | PR-ready | Atomic Updates using a String[] for multiple values do not work unless you are using the BinaryRequestWriter | org/apache/solr/client/solrj/util/ClientUtils.java<br>(3 files total) | diff |  |
| SOLR-7709 | solr-7709-submit | 501078f2220 | gated, no PR | Solr JavaBinCodec multi valued fields take only the last value per document from the javabin buffer | org/apache/solr/common/util/JavaBinCodec.java<br>(3 files total) | diff |  |
| SOLR-8536 | solr-8536-submit | 28552ddc26e | gated, no PR | MDC handling in MDCAwareThreadPoolExecutor uses even non-solr MDC parameters | org/apache/solr/common/util/ExecutorUtil.java<br>(3 files total) | diff |  |
| SOLR-10198 | solr-10198-submit | 9448cda146f | live PR #4965 | EmbeddedSolrServer embedded behavior different from HttpSolrClient | org/apache/solr/client/solrj/embedded/EmbeddedSolrServer.java<br>org/apache/solr/response/DocsStreamer.java<br>(5 files total)<br>also: Search components | diff |  |
| SOLR-10364 | solr-10364-submit | 502bdbf033f | awaiting pipeline | Solr bean binding only support List and Map? | org/apache/solr/client/solrj/beans/DocumentObjectBinder.java<br>(4 files total) | diff |  |
| SOLR-11356 | solr-11356-submit | 8474e5a3a26 | awaiting pipeline | ConcurrentUpdateJettySolrClient reuses a stream across different credentials | SOLR-11356-TESTING.md<br>changelog/unreleased/SOLR-11356-cusc-credentials-per-stream.yml<br>solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/ConcurrentUpdateJettySolrClient.java<br>(4 files total) | diff | new |
| SOLR-12094 | solr-12094-submit | 8d957a73f4f | gated, no PR | JsonRecordReader ignores root record fields after the split point | org/apache/solr/common/util/JsonRecordReader.java<br>(3 files total) | diff |  |
| SOLR-14187 | solr-14187-submit | 45b0f7ce34f | awaiting pipeline | async admin helpers pass per-request credentials to waitForAsyncRequest | SOLR-14187-TESTING.md<br>changelog/unreleased/SOLR-14187-wait-for-async-request-credentials.yml<br>solr/solrj/src/java/org/apache/solr/client/solrj/request/CollectionAdminRequest.java<br>(4 files total) | diff | new |
| SOLR-14298 | solr-14298-submit | 67e75ea1eea | PR-ready | LBSolrClient.checkAZombieServer should be less stupid | org/apache/solr/client/solrj/impl/LBSolrClient.java<br>(3 files total) | diff |  |
| SOLR-14967 | solr-14967-submit | ee76643f5b3 | gated, no PR | CloudSolrClient does not pass _stateVer_ if ModifiableSolrParams isn't used | org/apache/solr/client/solrj/impl/CloudSolrClient.java<br>(3 files total) | diff |  |
| SOLR-15823 | solr-15823-submit | 1f60cd39baa | live PR #5030 | Split v2 /node/logging API into separate GET and PUT APIs and deal with single node/all nodes logic | org/apache/solr/client/api/endpoint/NodeLoggingApis.java<br>org/apache/solr/client/api/model/LoggingResponse.java<br>org/apache/solr/handler/admin/LoggingHandler.java<br>(10 files total)<br>also: Core admin and collections API | diff | moved, ff +1 |
| SOLR-15823 | solr-15823-levels-submit | 6fa54c4de8c | live PR #5031 | Add nodes broadcast to the V2 logging levels GET endpoint (SOLR-15823 follow-up) | org/apache/solr/client/api/endpoint/NodeLoggingApis.java<br>org/apache/solr/client/api/model/LoggingResponse.java<br>org/apache/solr/handler/admin/LoggingHandler.java<br>(10 files total)<br>also: Core admin and collections API | diff |  |
| SOLR-17866 | solr-17866-submit | 3f9367d796a | PR-ready | GenericSolrRequest honors an explicitly passed collection | org/apache/solr/client/solrj/SolrRequest.java<br>org/apache/solr/client/solrj/request/GenericSolrRequest.java<br>(4 files total)<br>also: Search components | diff | moved, ff +1 |
| SOLR-18129 | solr-18129-submit | 657e443d866 | live PR #5000 | update-requesthandler API broken | org/apache/solr/common/util/CommandOperation.java<br>(5 files total) | diff | moved, ff +1 |
| SOLR-18341 | solr-18341-submit | 458b098719d | gated, no PR | SolrJ: let each SolrRequest declare whether it may be retried | org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java<br>org/apache/solr/client/solrj/SolrRequest.java<br>org/apache/solr/client/solrj/WrappedSolrRequest.java<br>(15 files total) | diff |  |

## CLI, bin scripts and packaging (11)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-7924 | solr-7924-submit | 96ef3a52bdb | awaiting pipeline | Solr Script on IBM AIX | SOLR-7924-TESTING.md<br>changelog/unreleased/SOLR-7924-spinner-integer-sleep.yml<br>solr/bin/solr<br>(4 files total) | diff | moved, ff +1 |
| SOLR-9342 | solr-9342-submit | 833e11192a7 | PR-ready | GC log follows SOLR_TIMEZONE by exporting TZ in bin/solr (Jira title not recorded in the branch report) | changelog/unreleased/SOLR-9342-gc-log-timezone.yml<br>solr/bin/solr<br>solr/packaging/test/test_start_solr.bats | diff | moved, ff +1 |
| SOLR-10390 | solr-10390-submit | 4af4a6834e2 | awaiting pipeline | lsof has too many kernel dependencies | SOLR-10390-TESTING.md<br>changelog/unreleased/SOLR-10390-start-without-lsof.yml<br>solr/bin/solr<br>(4 files total) | diff |  |
| SOLR-10667 | solr-10667-submit | 32b594f280c | awaiting pipeline | assemblePackaging never copies the LTR module example directory | SOLR-10667-TESTING.md<br>changelog/unreleased/SOLR-10667-ltr-example-packaging.yml<br>gradle/solr/packaging.gradle<br>(4 files total) | diff | new |
| SOLR-12347 | solr-12347-submit | b77acba2ad6 | awaiting pipeline | Raise the default SOLR_STOP_WAIT from 3 minutes. | SOLR-12347-TESTING.md<br>changelog/unreleased/SOLR-12347-stop-wait-default.yml<br>solr/bin/solr<br>(7 files total) | diff |  |
| SOLR-16272 | solr-16272-submit | d2cf8173916 | gated, no PR | bin/solr package install is not repeatable if first install fails with key error | org/apache/solr/packagemanager/RepositoryManager.java<br>(3 files total) | diff |  |
| SOLR-16813 | solr-16813-submit | 1b288170e8a | gated, no PR | PackageLoader not copying manifest.json file to filestore | org/apache/solr/pkg/SolrPackageLoader.java<br>(3 files total) | diff | moved, ff +1 |
| SOLR-17029 | solr-17029-submit | 4a98ef0a89d | gated, no PR | SOLR_OPTS and '-a' both break with quoted/escaped whitespace | changelog/unreleased/SOLR-17029.yml<br>solr/bin/solr<br>solr/packaging/test/test_start_solr.bats | diff |  |
| SOLR-17598 | solr-17598-submit | 8c91cf047a9 | PR-ready | Windows Script logic that handles SOLR_LOGS_DIR is faulty | changelog/unreleased/SOLR-17598-solr-cmd-logs-dir.yml<br>solr/bin/solr.cmd | diff |  |
| SOLR-18132 | solr-18132-submit | 54835cac6f8 | gated, no PR | SSL and external zookeeper | org/apache/solr/cli/CLIUtils.java<br>(6 files total) | diff |  |
| SOLR-18339 | solr-18339-submit | 47e53884609 | gated, no PR | Port bin/solr to use StatusTool for waiting for solr to come up | changelog/unreleased/SOLR-18339.yml<br>solr/bin/install_solr_service.sh<br>solr/bin/solr<br>(6 files total) | diff |  |

## Security and authentication (4)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-10627 | solr-10627-submit | 5a15dc0ba22 | gated, no PR | Security API should not let users create permission with collection:null for per collection permissions | org/apache/solr/security/AutorizationEditOperation.java<br>org/apache/solr/security/Permission.java<br>(4 files total) | diff |  |
| SOLR-11678 | solr-11678-submit | 55d8cd189d1 | awaiting pipeline | new SSL key manager password setting | SOLR-11678-TESTING.md<br>changelog/unreleased/SOLR-11678-ssl-key-manager-password.yml<br>solr/bin/solr<br>(13 files total) | diff | new |
| SOLR-12161 | solr-12161-submit | 1725cbd8489 | awaiting pipeline | test-only pin: no-credentials batch update expects SolrException | SOLR-12161-TESTING.md<br>solr/core/src/test/org/apache/solr/security/BasicAuthIntegrationTest.java<br>(2 files total) | diff | new |
| SOLR-18368 | solr-18368-submit | a7ec9a1b65c | gated, no PR | CloudSolrClient.Builder.withInternalClientBuilder | solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc | diff |  |

## Metrics and monitoring (1)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-17987 | solr-17987-submit | dba26c39877 | awaiting pipeline | SOLR_METRICS_DISABLEDREGISTRIES maps to solr.metrics.disabledRegistries | SOLR-17987-TESTING.md<br>changelog/unreleased/SOLR-17987-disable-metrics-by-registry.yml<br>solr/core/src/java/org/apache/solr/metrics/SolrMetricManager.java<br>(7 files total) | diff | new |

## Admin UI (3)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-9759 | solr-9759-submit | 31e702622da | awaiting pipeline | stream screen posts long expressions in the request body | SOLR-9759-TESTING.md<br>changelog/unreleased/SOLR-9759-admin-ui-stream-post.yml<br>solr/webapp/src/test/org/apache/solr/webapp/AdminUiStreamScreenTest.java<br>(5 files total) | diff | new |
| SOLR-9818 | solr-9818-submit | 63f2d7ce926 | awaiting pipeline | Admin UI replays only GET and HEAD after a status 0 failure | SOLR-9818-TESTING.md<br>changelog/unreleased/SOLR-9818-admin-ui-no-blind-retry.yml<br>solr/webapp/src/test/org/apache/solr/webapp/AdminUiRetryPolicyTest.java<br>(4 files total) | diff | new |
| SOLR-9831 | solr-9831-submit | f269a70e84f | awaiting pipeline | logging level cell prints showTrace | SOLR-9831-TESTING.md<br>changelog/unreleased/SOLR-9831-admin-ui-logging-level-column.yml<br>solr/webapp/src/test/org/apache/solr/webapp/AdminUiLoggingScreenTest.java<br>(4 files total) | diff | new |

## Build, docs and misc (20)

| Ticket | Branch | Head | State | Topic | Main files changed | Src | Since 10-06 |
|---|---|---|---|---|---|---|---|
| SOLR-3684 | solr-3684-submit | 663b8ce754b | retire candidate | Frequently full gc while do pressure index [retire call pending] | org/apache/solr/embedded/JettyConfig.java<br>org/apache/solr/embedded/JettySolrRunner.java<br>(4 files total) | diff |  |
| SOLR-5821 | solr-5821-submit | b8af2d1ce2f | gated, no PR | Search inconsistency on SolrCloud replicas | solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc | diff |  |
| SOLR-6430 | solr-6430-submit | 22f83870c4a | awaiting pipeline | Date sort order for null and dates < 1970 is wrong | SOLR-6430-TESTING.md<br>changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml<br>solr/solr-ref-guide/modules/indexing-guide/pages/field-type-definitions-and-properties.adoc | diff |  |
| SOLR-7119 | solr-7119-submit | 9593f4bd0d6 | awaiting pipeline | Tag and exclude local params don't work for interval facet as documented | SOLR-7119-TESTING.md<br>changelog/unreleased/SOLR-7119-interval-facet-ex-doc.yml<br>solr/solr-ref-guide/modules/query-guide/pages/faceting.adoc | diff |  |
| SOLR-9039 | solr-9039-submit | 13faf68fe85 | awaiting pipeline | Get to the bottom of why clientAuth (SSL certificate) testing on OSX doesn't work | org/apache/solr/SolrTestCaseJ4.java<br>(4 files total) | diff |  |
| SOLR-11700 | solr-11700-submit | c513388be05 | awaiting pipeline | WordDelimiterGraphFilterFactory token positions | SOLR-11700-TESTING.md<br>changelog/unreleased/SOLR-11700-wdgf-doc-positions.yml<br>solr/solr-ref-guide/modules/indexing-guide/pages/filters.adoc | diff |  |
| SOLR-12743 | solr-12743-submit | 1bb4b227dfe | retire candidate | Memory leak introduced in Solr 7.3.0 [retired in records; branch deletion pending] | org/apache/solr/util/ConcurrentLRUCache.java<br>(2 files total) | diff |  |
| SOLR-13705 | solr-13705-submit | 5f141fb2af3 | gated, no PR | Double-checked Locking Should Not be Used | org/apache/solr/util/configuration/SSLConfigurationsFactory.java<br>(4 files total) | diff |  |
| SOLR-16322 | solr-16322-submit | 65e0b8d7c79 | awaiting pipeline | beast failure summary uses the failing task seed | SOLR-16322-TESTING.md<br>changelog/unreleased/SOLR-16322.yml<br>gradle/testing/failed-tests-at-end.gradle<br>(3 files total) | diff | new |
| SOLR-16914 | solr-16914-submit | cd878023d3d | gated, no PR | Japanese Synonyms Search not workinh | solr/solr-ref-guide/modules/indexing-guide/pages/language-analysis.adoc | diff |  |
| SOLR-17252 | solr-17252-submit | d730a266a04 | gated, no PR | Invoking nano from the release wizard destroys terminal | dev-docs/releasing.adoc<br>dev-tools/scripts/README.md<br>dev-tools/scripts/releaseWizard.py | diff |  |
| SOLR-17356 | solr-17356-submit | ea7fc15cade | awaiting pipeline | The Ukrainian LanguageAnalysis page is very outdated | SOLR-17356-TESTING.md<br>changelog/unreleased/SOLR-17356-ukrainian-dictionary-docs.yml<br>solr/solr-ref-guide/modules/indexing-guide/pages/language-analysis.adoc | diff |  |
| SOLR-17722 | solr-17722-submit | fee3a26beb3 | retire candidate | CrossDC module mirrors update content type param.  [premise-dead; retire call pending] | org/apache/solr/crossdc/manager/messageprocessor/SolrMessageProcessor.java<br>(3 files total) | diff |  |
| SOLR-17752 | solr-17752-submit | e5c0a64f993 | gated, no PR | SolrResponseUtil should not log query on error | org/apache/solr/util/SolrResponseUtil.java<br>(3 files total) | diff |  |
| SOLR-17825 | solr-17825-submit | 0ef08ec86c6 | retire candidate | Upgrade commons-beanutils jar to 1.11.0+ to fix CVE-2025-48734  [branch deletion pending Nick call] | SOLR-17825-DECISION.md<br>solr/cross-dc-manager/build.gradle | diff |  |
| SOLR-17842 | solr-17842-submit | 008973f6313 | gated, no PR | Publish benchmarks for important features | solr/benchmark/README.md<br>solr/benchmark/docs/release-benchmarking.md | diff |  |
| SOLR-18119 | solr-18119-submit | 723022d35fc | retire candidate | Changes.html generation is quietly skipped if python3 not found, can cause downstream tasks to fail confusingl | gradle/documentation/changes-to-html.gradle | diff | superseded by solr-18119-jvm on 2026-10-08; PR #4999 still open |
| SOLR-18119 | solr-18119-jvm | 660faedd026 | live PR #5061 | changelog-to-HTML converter ported to Java; supersedes solr-18119-submit | build-tools/build-infra/build.gradle<br>build-tools/build-infra/src/main/java/org/apache/lucene/gradle/ChangesToHtml.java<br>build-tools/build-infra/src/main/java/org/apache/lucene/gradle/ChangesToHtmlTask.java<br>(10 files total) | diff | new |
| SOLR-18317 | solr-18317-submit | fadbaee999f | retire candidate | standalone solr 10 logging UI tries to access zookeeper: NullPointerException [PR #5001 merged] | changelog/unreleased/SOLR-18317.yml<br>org/apache/solr/webapp/AdminUiLoggingStandaloneTest.java<br>solr/webapp/web/js/angular/app.js<br>(5 files total) | diff |  |
| SOLR-18523 | solr-18523-submit | 26678c3737c | live PR #5062 | remove the checkJavadocLinks crawl; DocLint validates javadoc links | build.gradle<br>changelog/unreleased/SOLR-18523.yml<br>dev-tools/scripts/checkJavadocLinks.py<br>(5 files total) | diff | new |

## Closing sections
Written 2026-10-06. Updated 2026-10-08 only where noted: the Metrics paragraph, batching items 17 and 18, and the branches outside the population.


### Branches that span two areas

32 branches have production files in a second area (primary + secondary):

- SOLR-3704 (solr-3704-submit): Highlighting + Schema, analysis and field types
- SOLR-5754 (solr-5754-submit): Update processing and atomic updates + Streaming expressions
- SOLR-7550 (solr-7550-submit): Search components + Update processing and atomic updates
- SOLR-8576 (solr-8576-submit): Core admin and collections API + SolrCloud, overseer and cluster state
- SOLR-9598 (solr-9598-submit): Replication and backup + SolrCloud, overseer and cluster state
- SOLR-9750 (solr-9750-submit): Core admin and collections API + Configsets and config API
- SOLR-10198 (solr-10198-submit): SolrJ and clients + Search components
- SOLR-10252 (solr-10252-submit): Spellcheck + Configsets and config API
- SOLR-10424 (solr-10424-submit): Search components + Configsets and config API
- SOLR-11288 (solr-11288-submit): SolrCloud, overseer and cluster state + Core admin and collections API
- SOLR-11650 (solr-11650-submit): Replication and backup + SolrJ and clients
- SOLR-12998 (solr-12998-submit): SolrCloud, overseer and cluster state + Core admin and collections API
- SOLR-13136 (solr-13136-submit): SolrCloud, overseer and cluster state + Search components
- SOLR-13202 (solr-13202-submit): Query parsing + Core admin and collections API
- SOLR-13246 (solr-13246-submit): Core admin and collections API + Search components
- SOLR-14919 (solr-14919-submit): SolrCloud, overseer and cluster state + Update processing and atomic updates
- SOLR-15003 (solr-15003-submit): Core admin and collections API + Replication and backup
- SOLR-15035 (solr-15035-submit): SolrCloud, overseer and cluster state + Core admin and collections API
- SOLR-15674 (solr-15674-submit): SolrCloud, overseer and cluster state + Configsets and config API
- SOLR-15823 (solr-15823-levels-submit): SolrJ and clients + Core admin and collections API
- SOLR-15823 (solr-15823-submit): SolrJ and clients + Core admin and collections API
- SOLR-15863 (solr-15863-submit): SolrCloud, overseer and cluster state + Replication and backup
- SOLR-16155 (solr-16155-submit): Search components + Update processing and atomic updates
- SOLR-16499 (solr-16499-submit): Core admin and collections API + SolrJ and clients
- SOLR-17047 (solr-17047-submit): Schema, analysis and field types + Core admin and collections API
- SOLR-17287 (solr-17287-submit): Replication and backup + Update processing and atomic updates
- SOLR-17539 (solr-17539-submit): Search components + Configsets and config API
- SOLR-17680 (solr-17680-submit): SolrCloud, overseer and cluster state + Core admin and collections API
- SOLR-17731 (solr-17731-submit): Core admin and collections API + SolrJ and clients
- SOLR-17791 (solr-17791-submit): Search components + Replication and backup
- SOLR-17866 (solr-17866-submit): SolrJ and clients + Search components
- SOLR-18196 (solr-18196-submit): Search components + SolrJ and clients

### Stacked branches

- solr-9637-submit is stacked on solr-17393-submit: its diff was taken against that branch stack, so its row reflects only its own files.

### Thin classifications

No row in this snapshot rests on a report or a title alone; all 297 rows rest on computed diffs. The thin rows are the ones whose diffs contain no production code, so the area follows from the test or documentation files alone:

- docs-only (22): SOLR-5821 (Build, docs and misc), SOLR-6430 (Build, docs and misc), SOLR-7119 (Build, docs and misc), SOLR-7924 (CLI, bin scripts and packaging), SOLR-9342 (CLI, bin scripts and packaging), SOLR-10252 (Spellcheck), SOLR-10390 (CLI, bin scripts and packaging), SOLR-10937 (Suggester), SOLR-11700 (Build, docs and misc), SOLR-11844 (Suggester), SOLR-11939 (Core admin and collections API), SOLR-12347 (CLI, bin scripts and packaging), SOLR-16914 (Build, docs and misc), SOLR-17029 (CLI, bin scripts and packaging), SOLR-17252 (Build, docs and misc), SOLR-17356 (Build, docs and misc), SOLR-17598 (CLI, bin scripts and packaging), SOLR-17825 (Build, docs and misc), SOLR-17842 (Build, docs and misc), SOLR-18119 (Build, docs and misc), SOLR-18339 (CLI, bin scripts and packaging), SOLR-18368 (Security and authentication)
- test-only (17): SOLR-7267 (Configsets and config API), SOLR-8576 (Core admin and collections API), SOLR-9750 (Core admin and collections API), SOLR-10424 (Search components), SOLR-11922 (Streaming expressions), SOLR-13369 (SolrCloud, overseer and cluster state), SOLR-13696 (Update processing and atomic updates), SOLR-13943 (Update processing and atomic updates), SOLR-16290 (Search components), SOLR-16849 (Core admin and collections API), SOLR-17372 (Search components), SOLR-18109 (Search components), SOLR-18196 (Search components), SOLR-18280 (Replication and backup), SOLR-18317 (Build, docs and misc), SOLR-18505 (Update processing and atomic updates), SOLR-18506 (Search components)

### Cross-references for a security review batch

Only 2 branches classify primarily as Security and authentication. These further branches sit in other areas but touch security, authentication, or SSL files, or carry an authentication topic, and belong in a security batch:

- SOLR-9039 (solr-9039-submit, filed under Build, docs and misc): Get to the bottom of why clientAuth (SSL certificate) testing on OSX doesn't work
- SOLR-10322 (solr-10322-submit, filed under Streaming expressions): Streaming expressions Daemon can't connect to topic checkpoint when basic authentication i
- SOLR-11650 (solr-11650-submit, filed under Replication and backup): Credentials used for BasicAuth displayed in clear text on slave nodes
- SOLR-13097 (solr-13097-submit, filed under Core admin and collections API): RuleBasedAuthorizationPlugin is not fully fonctionnal in Solr standalone mode
- SOLR-13705 (solr-13705-submit, filed under Build, docs and misc): Double-checked Locking Should Not be Used
- SOLR-17708 (solr-17708-submit, filed under Core admin and collections API): JAX-RS v2 APIs go through authorization twice
- SOLR-18010 (solr-18010-submit, filed under Core admin and collections API): Adding a new role corrupts security.json file
- SOLR-18132 (solr-18132-submit, filed under CLI, bin scripts and packaging): SSL and external zookeeper 

### Metrics and monitoring

SOLR-17987 (solr-17987-submit) is the first primary member, added 2026-10-08. Metric-adjacent rows filed elsewhere: SOLR-13265 (Update processing and atomic updates), SOLR-18317 (Core admin and collections API).

### Suggested review batching order

Coherent families first (one file or one package family, uniform state), catch-all areas after, docs and infra last:

1. eDisMax and extended dismax: one parser family; six members already went through two review rounds.
2. Update processing and atomic updates: contains the Review1 atomic family (6045, 6046, 6065, 7504, 7022) and the nested-update cluster (12703, 12705, 15018).
3. Suggester, then Spellcheck: adjacent components; submit 9227 before 9968 because both edit the same suggester test class.
4. Highlighting: small, one component family.
5. Configsets and config API: small; pull in the config-machinery rows filed under Core admin (SOLR-6960, SOLR-13706).
6. Query parsing: parser family outside eDisMax.
7. Schema, analysis and field types: analyzer and schema family.
8. Search components: the largest area and the least coherent; sub-batch by main file (the SimpleFacets rows 5394, 10492, 10844 together; doc transformers together; handler components together).
9. SolrCloud, overseer and cluster state: cluster machinery; several held or parked members stay out of batches.
10. Core admin and collections API: admin handlers and v2 APIs.
11. Replication and backup: one machinery family.
12. Streaming expressions: one machinery family.
13. SolrJ and clients: client library; several rows are serialization and params handling.
14. CLI, bin scripts and packaging: scripts and package tooling; two members are Windows script changes with no local gate.
15. Security and authentication: the two primary rows plus the cross-referenced rows above.
16. Build, docs and misc: docs-only and test-infra changes; least review coherence, batch last.
17. Admin UI (new 2026-10-08): 9759, 9818, 9831, all under solr/webapp; small, batch on their own.
18. Metrics and monitoring (new 2026-10-08): 17987 alone; batch on its own.


## Changes since 2026-10-06

### Population

- Population: 329 branches (live `solr-*-submit` heads plus 5 named non-submit branches promoted to rows). 2026-10-06 snapshot: 297. Unchanged 197, moved 100, new 32, gone 0.
- Moved: 81 fast-forward, 19 not fast-forward (the old head is no longer an ancestor; the branch was rebased or rewritten).

### Owner decisions recorded 2026-10-08

- SOLR-5939 and SOLR-5941 are kept as candidates (state candidate), not retired.
- solr-18119-jvm (PR #5061) is a row. It supersedes solr-18119-submit, which the owner no longer needs, so that row is now retire candidate. PR #4999 on solr-18119-submit is still open; closing it is a public action and needs an explicit go-ahead.
- Four named branches with open PRs are rows, though their names are not submit: solr-11431-core-init-503 (PR #5002), solr-7394-recovery (PR #5003), solr-16130-test-followup (PR #5004), solr-18178-verify (PR #4968). Their tickets have no submit row.
- Code areas: every row has one. The branches outside the population have one too, except the coordination branches and main, which hold documents or the default branch.
- Coverage: every fork head is a row or is listed in "Branches outside the population", except the ci/* heads, which are counted only. Uncovered heads: 0.

### New rows

- awaiting pipeline (24): SOLR-10131, SOLR-10641, SOLR-10667, SOLR-10694, SOLR-11356, SOLR-11391, SOLR-11678, SOLR-12161, SOLR-12864, SOLR-14187, SOLR-15712, SOLR-16322, SOLR-16570, SOLR-16885, SOLR-17987, SOLR-3498, SOLR-4754, SOLR-6973, SOLR-7323, SOLR-8088, SOLR-8628, SOLR-9759, SOLR-9818, SOLR-9831. Each is registered as a pushed hypothetical-reproduction fix in research/pipeline/HANDOFF-task3-skiplist.md or queue.json (SOLR-14187 is implemented-hypothetical in queue.json). Tests not run.
  - Five of these have a live tip newer than the head their register records (SOLR-10667, 12161, 16322, 16570, 17987). They were pushed again after registration, and the register has not been updated.
- candidate (2): SOLR-5939, SOLR-5941. Skip rows in research/pipeline/queue.json with audit verdict deferred (2026-10-06, not built, design call needed), and each fork branch carries a fix commit dated 2026-10-07. Kept as candidates by owner decision; this refresh does not change the register.
- live PR (6): SOLR-11431 (solr-11431-core-init-503), PR #5002; SOLR-16130 (solr-16130-test-followup), PR #5004; SOLR-18119 (solr-18119-jvm), PR #5061; SOLR-18178 (solr-18178-verify), PR #4968; SOLR-18523 (solr-18523-submit), PR #5062; SOLR-7394 (solr-7394-recovery), PR #5003. The open-PR section below checks each head against its fork tip. Five of these branches were made rows by owner decision: solr-18119-jvm and the four named branches above.
- Judgment calls in the new rows: 12864 (JsonLoaderTest) under Update processing and atomic updates; 16570 (CollapsingQParserPlugin) under Query parsing; 11678 (SSL key manager password) under Security and authentication; 10667 (packaging.gradle) under CLI, bin scripts and packaging; 7323 (FileSystemConfigSetService) under Configsets and config API; 8628 (SolrCore.initIndex) under Core admin and collections API; 9759, 9818 and 9831 (solr/webapp) under a new Admin UI area; 17987 (SolrMetricManager) as the first Metrics and monitoring member; 18119 (changelog-to-HTML converter) under Build, docs and misc; 11431 (CoreContainer) under Core admin and collections API; 7394 (RecoveryStrategy, ShardTerms) under SolrCloud, overseer and cluster state; 16130-test-followup (test-only, CrossCollectionJoinQueryTest) under Query parsing, though its title says export; 18178 (FileSystemConfigSetService) under Configsets and config API.

### Open PRs from the fork

- Open upstream PRs whose head is on nick-boss-tech/solr: 25. Heads that differ from the fork tip: 0.

### Moved, not fast-forward (19)

- solr-4841-submit: 524afb62181 -> f8850ffd421
- solr-5505-submit: 5fc18d6d661 -> 44c444aa5cd
- solr-5887-submit: edbcdfbd5b5 -> c4c57ef7bcb
- solr-3701-submit: 77137cf17a9 -> aabd678dec7
- solr-2681-submit: 1eb119ed747 -> 4cb25b1691b
- solr-3704-submit: 9f16a7e5c59 -> de63d4e5d5d
- solr-4540-submit: 4a0a97d5383 -> 62c06439fb0
- solr-874-submit: 456f7134a7d -> ac9ab337537
- solr-4824-submit: d305216518e -> 76c777e4661
- solr-6014-submit: bc4a1d5e811 -> 505849d2d4e
- solr-4374-submit: 1f9a223c6f1 -> 801c62290f7
- solr-5394-submit: 576e8b441c0 -> 967445622f9
- solr-6207-submit: 2f6755062eb -> b099a9f1be5
- solr-5813-submit: 220d2ed9187 -> 90b8baa08ae
- solr-15106-submit: 6263b9ef18b -> 40b7e5d0efa
- solr-15386-submit: 994cf509179 -> ca8cb61ee95
- solr-2018-submit: 33fe0039ffb -> 213457aa91f
- solr-3999-submit: 687d27601e0 -> e3af6f36e31
- solr-5220-submit: a2c7b2af83f -> e415c409044

### Round 28 assignments that moved

- Round 28 assigns the rows whose 2026-10-06 state is PR-ready or gated, no PR: 187 rows. Of these, 62 have moved heads: 2309, 2988, 3243, 3729, 3962, 4362, 6009, 6320, 12092, 14913, 3657, 5065, 7022, 13265, 13943, 14262, 14718, 16910, 9968, 6960, 12871, 15615, 15906, 17280, 15357, 16977, 17047, 18134, 8939, 9148, 11364, 12543, 14381, 14451, 14678, 14931, 15018, 15319, 15479, 16444, 17372, 12651, 13369, 15106, 15386, 15863, 16013, 12007, 15003, 16725, 16887, 17708, 17731, 11650, 17287, 10322, 12657, 4335, 4336, 17866, 9342, 16813.
- Round 28 already asks reviewers to report the snapshot head and the head inspected, so these are covered by its process. The list is for awareness.

### Branches outside the population

Every non-ci fork head that is not a row, with its code area and why it is outside. The `ci/*` heads (373) are out of scope by request and are not listed. Coordination branches and main have no code area: they hold documents or the default branch.

| Branch | Code area | Why outside | Note |
|---|---|---|---|
| `main` | none | Default branch | Not a submit branch. |
| `assignment-18523-doclint` | Build, docs and misc | Coordination | Review assignment. Carries the SOLR-18523 change; same files as solr-18523-submit. |
| `review-18523-pr` | Build, docs and misc | Coordination | Review coordination. Carries the SOLR-18523 change; same files as solr-18523-submit. |
| `assignment-pair-18119-18523` | none | Coordination | Assignment, claims, reviews and PR body drafts for SOLR-18119 and SOLR-18523. No code. |
| `review-18119-jvm` | none | Coordination | Assignment, claim and review for solr-18119-jvm. No code. |
| `code-review` | none | Coordination | Review channel. Holds documents only. |
| `gradle-queue` | none | Coordination | Gradle queue status documents only. |
| `pr-prepare` | none | Coordination | PR preparation branch. Holds the PR formula and this inventory; no code. |
| `solr-18317-ready` | Core admin and collections API | Companion of a submit row | Ready variant of SOLR-18317. solr-18317-submit is a retire candidate (PR #5001 merged). |
| `solr-18339-ready` | CLI, bin scripts and packaging | Companion of a submit row | Ready variant of SOLR-18339. solr-18339-submit is gated, no PR. |
| `solr-16130-export-join` | Streaming expressions | Named, no PR | No submit row for SOLR-16130. Changes the export writer. |
| `solr-18135-merge-timer` | Update processing and atomic updates | Named, no PR | No submit row for SOLR-18135. Changes SolrIndexWriter. |
| `solr-8291-13217` | Streaming expressions | Named, no PR | No submit row for SOLR-8291 or SOLR-13217. Changes the export writer and its useFilter test. |

Three named branches have no PR and no submit row for their ticket. They stay outside the population, with a code area; say if any should become rows.
