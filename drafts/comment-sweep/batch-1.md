# Comment sweep batch 1 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-2425
- Decision: CLOSE
- Affected versions (JIRA): 1.4.1, 3.1, 4.0-ALPHA
- Updated: 2011-03-14, status: Open (no resolution)
- Draft: From code reading against apache/main b73f03976fa, the searcher executor is stalled by a latch that is released only after inform() has run for all components (solr/core/src/java/org/apache/solr/core/SolrCore.java:1123-1128, 1141, 1189). The firstSearcher task is submitted whenever currSearcher is null, with no check on the listener count (solr/core/src/java/org/apache/solr/core/SolrCore.java:2640-2646), so listeners registered during inform() are still called. Is closure OK, [~koji]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/SolrCore.java 1120-1190 and 2636-2650, 748-749

## SOLR-2507
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2011-05-11, status: Open (no resolution)
- Draft: From code reading against apache/main b73f03976fa, the message "not allowed as first character in WildcardQuery" no longer appears anywhere under solr/, and leading wildcards are allowed by default (solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:137). getWildcardQuery (solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:1275-1329) has no leading-character rejection. Is closure OK, [~diegomontoya]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java 137, 1275-1329; grep for the message text under solr/ returned no match

## SOLR-2506
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2011-06-16, status: Open (no resolution)
- Draft: none. HOLD: the EOFException is raised while reading a JavaBin response through EmbeddedSolrServer, and the reporter could not reproduce it; a two-document repro index was posted as a comment and has to be run to settle the question, which code reading cannot do.
- Citations checked: yes, solr/core/src/java/org/apache/solr/client/solrj/embedded/EmbeddedSolrServer.java 150-216 (request path)

## SOLR-2597
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2011-06-17, status: Open (no resolution)
- Draft: none. HOLD: the XmlCharFilter patch never landed (no XmlCharFilter or XML strip filter under solr/), and the ticket has no ruling on whether a Woodstox-based XML char filter is still wanted; naming and packaging were left open in the comments, so a maintainer decision is needed before a comment can be made.
- Citations checked: yes, git grep for XmlCharFilter, XmlStripCharFilter and XmlTagCharFilter under solr/ returned no match

## SOLR-2648
- Decision: HOLD
- Affected versions (JIRA): 3.4, 4.0-ALPHA
- Updated: 2011-07-11, status: Open (no resolution)
- Draft: none. HOLD: the documented analyzer and tokenizerFactory synonym arguments (solr/solr-ref-guide/modules/indexing-guide/pages/filters.adoc:3196-3210) partly cover the request, but the ticket asks for a full configured chain (charfilters, tokenizer, token filters) applied to synonym entries, which is a design change; the synonym parser sits outside solr/ (Lucene), so the choice is for a maintainer.
- Citations checked: yes, solr/solr-ref-guide/modules/indexing-guide/pages/filters.adoc 3196-3210; solr/core/src/java/org/apache/solr/rest/schema/analysis/ManagedSynonymGraphFilterFactory.java 392-399 (delegate)

## SOLR-2837
- Decision: CLOSE
- Affected versions (JIRA): 3.5, 4.0-ALPHA
- Updated: 2011-10-16, status: Open (no resolution)
- Draft: From code reading against apache/main b73f03976fa, the langid module under solr/modules/langid/src/java/org/apache/solr/update/processor/ contains no code that lists its supported languages. The reference guide defers the per-implementation language lists to the upstream projects (solr/solr-ref-guide/modules/indexing-guide/pages/language-detection.adoc:26). Is closure OK, [~koji]?
- Citations checked: yes, solr/solr-ref-guide/modules/indexing-guide/pages/language-detection.adoc 24-26; git grep for supported-language or getDisplayLanguage listing under solr/modules/langid/src/java returned no listing code

## SOLR-2865
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2011-10-30, status: Open (no resolution)
- Draft: none. HOLD: testMultiThreaded still exists, in solr/solrj/src/test/org/apache/solr/client/solrj/LargeVolumeTestBase.java:49 and is inherited by solr/solrj/src/test/org/apache/solr/client/solrj/embedded/LargeVolumeBinaryJettyTest.java:28; the 2011 Hudson failure ("Some threads threw uncaught exceptions") is a runtime result that code reading cannot settle.
- Citations checked: yes, solr/solrj/src/test/org/apache/solr/client/solrj/LargeVolumeTestBase.java 38, 49; solr/solrj/src/test/org/apache/solr/client/solrj/embedded/LargeVolumeBinaryJettyTest.java 28

## SOLR-2720
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2011-11-04, status: Open (no resolution)
- Draft: From code reading against apache/main b73f03976fa, there is no TestLBHttpSolrServer class and no testReliability or testSimple method under solr/, so the test named in this ticket is not on main. The LB client tests now sit under solr/solrj/src/test/org/apache/solr/client/solrj/impl/ (for example solr/solrj/src/test/org/apache/solr/client/solrj/impl/LBSolrClientTest.java:32). Is closure OK, [~rcmuir]?
- Citations checked: yes, solr/solrj/src/test/org/apache/solr/client/solrj/impl/LBSolrClientTest.java 32; git grep for TestLBHttpSolrServer and testReliability under solr/ returned no match

## SOLR-2897
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2011-11-13, status: Open (no resolution)
- Draft: From code reading against apache/main b73f03976fa, DataImportHandler is no longer in the source tree: there is no solr/contrib/dataimporthandler directory and no file under solr/ references dataimport or FileListEntityProcessor, so the onError attribute named in this ticket has no code path on main. Is closure OK, [~erickerickson]?
- Citations checked: yes, git grep -i dataimport and FileListEntityProcessor under solr/ returned no match; solr/ top-level tree listing (no contrib directory)

## SOLR-2936
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2011-12-02, status: Open (no resolution)
- Draft: From code reading against apache/main b73f03976fa, the core is acquired in a try-with-resources block (solr/core/src/java/org/apache/solr/client/solrj/embedded/EmbeddedSolrServer.java:194), so the unknown-handler throws at lines 209 and 215 release the core reference on exit. The explicit core.close() before the throw proposed in this ticket is therefore not needed on main. Is closure OK, [~tom_lt]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/client/solrj/embedded/EmbeddedSolrServer.java 164-216

---

Final list of holds and nones:
- SOLR-2506 HOLD: needs a run of the reporter's repro index; code reading cannot settle the EOFException.
- SOLR-2597 HOLD: feature never landed and no ruling in the ticket; maintainer must decide whether an XML char filter is still wanted.
- SOLR-2648 HOLD: documented analyzer and tokenizerFactory options partly cover it; the proposed full-chain design is a maintainer decision and the parser lives outside solr/.
- SOLR-2865 HOLD: testMultiThreaded still exists; the 2011 Hudson failure needs a runtime result to settle.
- NONE: none.
- Open PR check: no PR link appears in the JIRA issuelinks or comments for any ticket; GitHub was not searched.
