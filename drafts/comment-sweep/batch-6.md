# Comment sweep batch 6 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-4366
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0
- Updated: 2013-01-26, status: Open
- Draft: From code reading against apache/main b73f03976fa, SpellCheckComponent.process returns a 404 when the dictionary is missing (solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:234-239), but prepare() calls spellChecker.build and spellChecker.reload on the unchecked result of getSpellChecker (solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:115-122), so the spellcheck.build and spellcheck.reload NPE is still reachable. A null check in prepare() that throws the same SolrException(NOT_FOUND) as process() would close that gap. [~jkrupan] Is this still wanted?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:108-127, 130-139, 234-239

## SOLR-4367
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0
- Updated: 2013-01-28, status: Open
- Draft: From code reading against apache/main b73f03976fa, SpellCheckComponent.inform handles only the "spellchecker" init arg in its loop and has no else branch for other names (solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:697-712), so a misplaced entry such as a bare str named "name" under the component is silently ignored. Throwing a SolrException at that loop for any init-arg name other than "spellchecker" and "queryAnalyzerFieldType" (read at solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:727) would make the misconfiguration fail at core load. [~jkrupan] Is this still wanted?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:692-712, 727

## SOLR-4355
- Decision: CLOSE
- Affected versions (JIRA): 4.0, 4.1
- Updated: 2013-01-28, status: Open
- Draft: From code reading against apache/main b73f03976fa, SolrJ parses the response body for non-2xx status codes and passes its "error" member into RemoteSolrException (solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:243-264), and RemoteSolrException appends the remote errorClass and msg to its message (solr/solrj/src/java/org/apache/solr/client/solrj/RemoteSolrException.java:63-70, 89-92). The generic "non ok status" text is now used only for text/html responses or when no error body is present (solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:224-231, 254-260). [~ebbesson] Is closure OK?
- Citations checked: yes, solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:197-265; solr/solrj/src/java/org/apache/solr/client/solrj/RemoteSolrException.java:52-92

## SOLR-4368
- Decision: CLOSE
- Affected versions (JIRA): 4.0
- Updated: 2013-01-29, status: Open
- Draft: From code reading against apache/main b73f03976fa, no TriField class exists under solr/ (the only numeric class is TrieField at solr/core/src/java/org/apache/solr/schema/TrieField.java:90), so the TriField type named in the report is absent. The readableToIndexed call that threw in the report is not on the current stats-facet path, because FieldFacetStats.facetTermNum keys its buckets from DocValues ordinals without any conversion (solr/core/src/java/org/apache/solr/handler/component/FieldFacetStats.java:91-120, reached from solr/core/src/java/org/apache/solr/handler/component/StatsField.java:463). [~harpe1999] Is closure OK?
- Citations checked: yes, solr/core/src/java/org/apache/solr/schema/TrieField.java:90; solr/core/src/java/org/apache/solr/handler/component/FieldFacetStats.java:91-120; solr/core/src/java/org/apache/solr/handler/component/StatsField.java:463; repo-wide grep for TriField under solr/ returned no matches

## SOLR-4397
- Decision: HOLD
- Affected versions (JIRA): 4.1
- Updated: 2013-02-01, status: Open
- Draft: none (HOLD: feature request for filtering after sorting; the code applies a post filter as the outermost collector before the sorting collector (solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:314-317), so no post-sort hook exists, and the requested partial-count semantics need a maintainer design decision that the code does not settle)
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:310-317, 1190-1193

## SOLR-4399
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0
- Updated: 2013-02-02, status: Open
- Draft: From code reading against apache/main b73f03976fa, FileBasedSpellChecker passes sourceLocation to the resource loader without a null check (solr/core/src/java/org/apache/solr/spelling/FileBasedSpellChecker.java:105, 121, 126), and SolrResourceLoader.openResource calls resource.trim() first (solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:350-351), so a missing sourceLocation still throws an NPE instead of a readable error. Validating sourceLocation in FileBasedSpellChecker.init (solr/core/src/java/org/apache/solr/spelling/FileBasedSpellChecker.java:60-64) would close the file-based case. The IndexBased sourceLocation is optional by design (solr/core/src/java/org/apache/solr/spelling/IndexBasedSpellChecker.java:71-77), and the field value is read without a check (solr/core/src/java/org/apache/solr/spelling/SolrSpellChecker.java:66) before it reaches HighFrequencyDictionary (solr/core/src/java/org/apache/solr/spelling/IndexBasedSpellChecker.java:80). [~jkrupan] Is this still wanted?
- Citations checked: yes, solr/core/src/java/org/apache/solr/spelling/FileBasedSpellChecker.java:60-64, 105, 121, 126; solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:350-351; solr/core/src/java/org/apache/solr/spelling/IndexBasedSpellChecker.java:71-80; solr/core/src/java/org/apache/solr/spelling/SolrSpellChecker.java:66

## SOLR-4419
- Decision: HOLD
- Affected versions (JIRA): 4.0
- Updated: 2013-02-08, status: Open
- Draft: none (HOLD: feature request for a configurable currency precision; the code fixes the scale at Currency.getDefaultFractionDigits (solr/core/src/java/org/apache/solr/schema/CurrencyValue.java:77, 206), so a configurable precision changes the stored encoding and needs a maintainer design decision that the code does not settle)
- Citations checked: yes, solr/core/src/java/org/apache/solr/schema/CurrencyValue.java:70-80, 200-210

## SOLR-4423
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0
- Updated: 2013-02-09, status: Open
- Draft: From code reading against apache/main b73f03976fa, DOMUtil.parseVal handles only str, int, long, float, double, bool and null, and silently returns null for any other element name (solr/solrj/src/java/org/apache/solr/common/util/DOMUtil.java:163-179), so an entry such as a string element is stored with a null value. NamedList.toSolrParams still calls val.toString() on that value (solr/solrj/src/java/org/apache/solr/common/util/NamedList.java:363-364), which is the NPE in the report. Throwing a SolrException that names the element type and the entry name in the unknown-type branch of parseVal would give a readable error at startup. [~jkrupan] Is this still wanted?
- Citations checked: yes, solr/solrj/src/java/org/apache/solr/common/util/DOMUtil.java:133-155, 160-193; solr/solrj/src/java/org/apache/solr/common/util/NamedList.java:349-369

## SOLR-4427
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0
- Updated: 2013-02-11, status: Open
- Draft: From code reading against apache/main b73f03976fa, SolrConfig.getPluginInfo already throws when more than one entry of a type is configured (solr/core/src/java/org/apache/solr/core/SolrConfig.java:902-913), which covers the directoryFactory case read at solr/core/src/java/org/apache/solr/core/DirectoryFactory.java:439. The queryConverter case is still silent, because SpellCheckComponent.inform uses the converter only when exactly one is loaded and otherwise leaves it unset without a message (solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:714-733). Logging a warning or throwing a SolrException when queryConverters.size() is greater than one at that point would address the remaining case. [~jkrupan] Is this still wanted?
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/SolrConfig.java:902-913; solr/core/src/java/org/apache/solr/core/DirectoryFactory.java:439; solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:714-733

## SOLR-4445
- Decision: CLOSE
- Affected versions (JIRA): 4.1
- Updated: 2013-02-11, status: Open
- Draft: From code reading against apache/main b73f03976fa, the documented multi-shard join requires matching shard counts, router settings, and collocated shards, so the from side is evaluated against the local shard by design (solr/solr-ref-guide/modules/query-guide/pages/join-query-parser.adoc:193-199). The default join path runs the from query on the local searcher (solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java:186-191), and fromIndex must name a single-shard collection (solr/solr-ref-guide/modules/query-guide/pages/join-query-parser.adoc:141-144). [~cbartolo] Is closure OK?
- Citations checked: yes, solr/solr-ref-guide/modules/query-guide/pages/join-query-parser.adoc:141-144, 193-199; solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java:142-195

---

Holds and nones:
- SOLR-4397 HOLD: feature request, post-sort filtering has no hook on main (solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:314-317 puts the post filter ahead of the sorting collector); desired semantics need an owner decision.
- SOLR-4419 HOLD: feature request, currency precision is fixed by ISO fraction digits (solr/core/src/java/org/apache/solr/schema/CurrencyValue.java:77, 206); a configurable scale changes the stored encoding, so it needs an owner decision.
- NONE: no ticket in this batch was judged to need no comment.
