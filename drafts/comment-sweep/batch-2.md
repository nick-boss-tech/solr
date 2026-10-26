# Comment sweep batch 2 (vm5, 2026-10-26), drafts only, nothing posted

Source: apache/main commit b73f03976fa (read via git show / git grep on base-main8003). JIRA read via GET only. Open-PR status was not checked (gh CLI is not installed on this host); none of the ten JIRA records links a PR. No ticket has an empty affected-version field or a 7.x+ fixVersion, so no HOLD is driven by those rules.

## SOLR-2386
- Decision: CLOSE
- Affected versions (JIRA): 3.2, 4.0-ALPHA
- Updated: 2011-12-09, status: Open (no resolution; fixVersions empty)
- Draft: From code reading against apache/main b73f03976fa, the filter cache lookup uses the Query as parsed with no clause sorting at that point (solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:1045-1050), so reordered OR clauses are not normalized by Solr. Yonik Seeley's comment in this ticket (2011-02-27) rejected clause sorting for cache keys and suggested sorting terms in the client. Is closure OK, [~krausfm]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:1045-1050 (git show). Grep for "sort" in solr/core/src/java/org/apache/solr/search/QueryUtils.java returned no hits. Lucene's BooleanQuery.equals is not in this repo, so no claim about it is made.

## SOLR-2964
- Decision: NONE
- Affected versions (JIRA): 3.5, 4.0-ALPHA
- Updated: 2011-12-13, status: Open (no resolution; fixVersions empty)
- Draft: none. The ticket asks to explore consolidating prepare/process with the distributed methods. It reports no defect and no ruling. The code still has both paths (solr/core/src/java/org/apache/solr/handler/component/SearchComponent.java:48, 56, 63), so a status comment would not settle anything.
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/SearchComponent.java:48, 56, 63 (git grep / git show).

## SOLR-2998
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-01-04, status: Open (no resolution; fixVersions empty)
- Draft: none. Reason: the FVH path joins all values of a multivalued field with one separator (solr/core/src/java/org/apache/solr/highlight/SolrFragmentsBuilder.java:69-80, default " "). Returning one matched value per fragment means changing fragment construction, not a parameter, and the code reading does not settle whether that is wanted. Owner design call.
- Citations checked: yes, solr/core/src/java/org/apache/solr/highlight/SolrFragmentsBuilder.java:69-80 (git grep / git show).

## SOLR-3027
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-01-11, status: Open (no resolution; no comments; fixVersions empty)
- Draft: none. Reason: the report is that plugins are instantiated twice, observed in a 2012 test harness. Source shows PluginBag creating one holder per PluginInfo (solr/core/src/java/org/apache/solr/core/PluginBag.java:345-348, createPlugin at :158), but the instance count at runtime needs a test run, which this drafting task does not do.
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/PluginBag.java:158, 345-348 (git grep / git show).

## SOLR-3034
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-02-01, status: Open (no resolution; fixVersions empty)
- Draft: none. Reason: the master-side part is settled by code. replicateAfter=commit overrides optimize (solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java:1374-1376), which explains the deduped UI list, and the optimize callback is registered only for optimize-only configs (:1396-1398). The reporter's second question, whether an optimized index stays optimized on the slave, is not answered by reading source, so no comment is drafted until that is checked.
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java:1374-1376, 1396-1398 (git grep / git show).

## SOLR-3028
- Decision: NONE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-02-29, status: Open (no resolution; fixVersions empty)
- Draft: none. This is a parent feature-parity ticket. The reporter split its asks per the ticket comments into SOLR-3099 (exact match), SOLR-3100 (term quorum, now moved to LUCENE-3833) and SOLR-3101 (word order). A GET on 2026-10-26 shows SOLR-3099, LUCENE-3833 and SOLR-3101 all Open, so nothing on the parent needs closing or changing.
- Citations checked: no code citations used (JIRA GET only for SOLR-3099, SOLR-3100 (redirects to LUCENE-3833), SOLR-3101).

## SOLR-3050
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-03-05, status: Open (no resolution; fixVersions empty)
- Draft: From code reading against apache/main b73f03976fa, DefaultSolrHighlighter returns no highlights for any TrieField, with a TODO that waits on LUCENE-3080 (solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java:535-538), and JIRA lists LUCENE-3080 as Closed. One defensible change is to remove that early branch so Trie fields take the normal highlighter path, with a test that highlights an int field. Is this still wanted, [~manyutomar]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java:535-538 (git show). The LUCENE-3080 status comes from the JIRA issue link, not from code.

## SOLR-2393
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-03-20, status: Open (no resolution; patch attached, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, the Solr tree has no PhoneticFilterFactory or DoubleMetaphoneFilter Java class, and the only phonetic artifact in the tree is solr/licenses/lucene-analysis-phonetic-10.4.0.jar.sha1. The reference guide documents the current split: the Phonetic Filter does not produce the alternate DoubleMetaphone encoding that the Double Metaphone Filter produces (solr/solr-ref-guide/modules/indexing-guide/pages/phonetic-matching.adoc:87), and solr/core/src/test-files/solr/collection1/conf/schema_latest.xml:661 still uses solr.DoubleMetaphoneFilterFactory. Is closure OK, [~billnbell]?
- Citations checked: yes, phonetic-matching.adoc:87 and schema_latest.xml:661 (git show), plus the git ls-tree check for phonetic files and the git grep for class names (no hits in solr/ or the whole tree's Java sources).

## SOLR-3271
- Decision: CLOSE
- Affected versions (JIRA): 3.6, 4.0-ALPHA
- Updated: 2012-03-29, status: Open (no resolution; fixVersions empty)
- Draft: From code reading against apache/main b73f03976fa, ShardHandlerFactory is still an abstract class with a concrete default method (solr/core/src/java/org/apache/solr/handler/component/ShardHandlerFactory.java:28-34), which is the back-compatibility design ryantxu argued for in this ticket (2012-03-29). The reporter said the change matters little either way. Is closure OK, [~gbowyer@fastmail.co.uk]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/ShardHandlerFactory.java:28-34 (git show, git grep for the class declaration).

## SOLR-3018
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-04-20, status: Open (no resolution; no comments; fixVersions empty)
- Draft: none. Reason: the ticket asks for per-document results in a batch. The code loops over the request streams and lets the first loader exception propagate (solr/core/src/java/org/apache/solr/handler/ContentStreamHandlerBase.java:98-100). Reporting per-stream errors would change the error contract of the update handler for every request, which is a scope decision for the owner. CommonsHttpSolrServer, named in the ticket, is absent from solr/ (git grep returned no hits).
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/ContentStreamHandlerBase.java:98-100 (git show, git grep).

---

## Final list: holds and nones

- HOLD SOLR-2998: a per-value FVH result needs a fragment-builder change, not a parameter, and that is a design call.
- HOLD SOLR-3027: the double-init claim needs a runtime instance count, which this read-only pass cannot give.
- HOLD SOLR-3034: the master-side question is answered by code; the reporter's slave-side question is open and not settled by source.
- HOLD SOLR-3018: per-document batch errors change the update error contract for all requests; owner scope call.
- NONE SOLR-2964: exploration ticket with no reported defect or ruling.
- NONE SOLR-3028: parent meta ticket; its three asks are in open child issues (SOLR-3099, LUCENE-3833 (formerly SOLR-3100), SOLR-3101).
