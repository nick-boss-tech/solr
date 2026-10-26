# Comment sweep batch 8 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-4821
- Decision: HOLD
- Affected versions (JIRA): 4.3
- Updated: 2013-05-14, status: Open (no fixVersion, no comments, no issue links)
- Draft: none (HOLD: the ticket asks to restore a restriction that main still has commented out, and uncommenting it would reject sub-requests that non-search handlers legitimately serve. Intent is an owner call.)
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/SolrCore.java:2886-2891 (restriction commented out, TODO cites /spell breakage); solr/core/src/java/org/apache/solr/handler/component/ShardHandler.java:84 (every sub-request sets isShard=true); solr/core/src/java/org/apache/solr/handler/PingRequestHandler.java:116, 242-253 (non-SearchHandler that reads isShard); solr/core/src/java/org/apache/solr/handler/component/ActiveTasksListHandler.java:38, 45 (non-search handler that reads isShard)

## SOLR-4857
- Decision: CLOSE
- Affected versions (JIRA): 4.3
- Updated: 2013-05-24, status: Open (no fixVersion, no comments, no issue links)
- Draft: From code reading against apache/main b73f03976fa, RELOAD is documented to reload a core from its existing registered configuration, and a simple RELOAD cannot apply changes such as dataDir or IndexWriter settings (solr/solr-ref-guide/modules/configuration-guide/pages/coreadmin-api.adoc:295-297, 322-328). The RELOAD operation reads only the core parameter (solr/core/src/java/org/apache/solr/handler/admin/CoreAdminOperation.java:103-105), and creating a core under an existing name is refused (solr/core/src/java/org/apache/solr/core/CoreContainer.java:1519-1525). [~awidhani] Is closure OK?
- Citations checked: yes, solr/solr-ref-guide/modules/configuration-guide/pages/coreadmin-api.adoc:295-297, 322-328; solr/core/src/java/org/apache/solr/handler/admin/CoreAdminOperation.java:101-105; solr/core/src/java/org/apache/solr/core/CoreContainer.java:1519-1525

## SOLR-4874
- Decision: HOLD
- Affected versions (JIRA): 4.3
- Updated: 2013-05-29, status: Open (1 comment, a quoted mailing-list post from 2013-05-29; no fixVersion)
- Draft: none (HOLD: the ticket is an open investigation of run-time growth. Collation verification re-runs the full query, including all original fq parameters, once per try. Whether that cost is a defect needs a measurement, which code reading cannot give.)
- Citations checked: yes, solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java:108-109, 161-163; solr/solr-ref-guide/modules/query-guide/pages/spell-checking.adoc:476-484 (documented spellcheck.collateParam.* overrides)

## SOLR-4913
- Decision: CANDIDATE
- Affected versions (JIRA): 4.3
- Updated: 2013-06-10, status: Open (no comments, no fixVersion, no issue links)
- Draft: From code reading against apache/main b73f03976fa, the dynFields loop in PolyFieldTest.testSchemaBasics still keeps iterating after seen is set to true (solr/core/src/test/org/apache/solr/schema/PolyFieldTest.java:50-54). Adding break; after line 52 would end the loop at the first match and give the same assertion result. [~adriannistor] Is this still wanted?
- Citations checked: yes, solr/core/src/test/org/apache/solr/schema/PolyFieldTest.java:40-55

## SOLR-4979
- Decision: CANDIDATE
- Affected versions (JIRA): 4.3.1
- Updated: 2013-06-29, status: Open (no comments, no fixVersion, no issue links)
- Draft: From code reading against apache/main b73f03976fa, the attribute loop in DocumentAnalysisRequestHandler.readDocument keeps overwriting fieldName with each name attribute and has no break (solr/core/src/java/org/apache/solr/handler/DocumentAnalysisRequestHandler.java:314-319). Adding break; after line 317 gives the same fieldName whenever the element has one name attribute, which is the case the ticket assumes. [~adriannistor] Is this still wanted?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/DocumentAnalysisRequestHandler.java:269-270, 314-319

## SOLR-5035
- Decision: HOLD
- Affected versions (JIRA): 4.3.1
- Updated: 2013-07-12, status: Open (no comments, no fixVersion, no issue links)
- Draft: none (HOLD: the ticket asks for grouping to be split out of QueryComponent. Main still couples them: QueryComponent parses grouping in prepare, builds the GroupingSpecification, and runs grouped distributed phases in process. Choosing how a grouping component is wired into handler configs is a design decision the code cannot settle.)
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:239-240, 270-271, 425-433

## SOLR-4998
- Decision: HOLD
- Affected versions (JIRA): 4.3, 4.3.1
- Updated: 2013-07-23, status: Open (34 comments; no fixVersion, no issue links)
- Draft: none (HOLD: the ticket is a naming cleanup across code and docs, and its final ruling in the comments (keep Slice/Shard/Replica, rename wrong "shard" uses) is not settled by one code path. The one named item, maxShardsPerNode, is absent on main after SOLR-12847, per the test comment, but the broader scope needs an owner call.)
- Citations checked: yes, solr/core/src/test/org/apache/solr/cloud/api/collections/CollectionTooManyReplicasTest.java:46-47 (maxShardsPerNode removed in SOLR-12847); solr/solrj/src/java/org/apache/solr/common/cloud/Slice.java:43; git grep for maxShardsPerNode and maxReplicasPerNode over solr/ on b73f03976fa (no main-code match)

## SOLR-5123
- Decision: CLOSE
- Affected versions (JIRA): 4.3
- Updated: 2013-08-08, status: Open (no comments, no fixVersion, no issue links)
- Draft: From code reading against apache/main b73f03976fa, the Data Import Handler is no longer part of Solr (solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-9.adoc:470-471), and no JdbcDataSource source file exists in that commit, so the JdbcDataSource code in the report is not in this tree. [~tszadel] Is closure OK?
- Citations checked: yes, solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-9.adoc:470-471; git grep -l JdbcDataSource over b73f03976fa (only CHANGELOG.md and changelog/archive.md match, no solr/ source file); solr/contrib/ absent from the commit (git ls-tree empty)

## SOLR-5158
- Decision: CLOSE
- Affected versions (JIRA): 4.2.1
- Updated: 2013-08-15, status: Open (no comments, no fixVersion, no issue links)
- Draft: From code reading against apache/main b73f03976fa, PostingsSolrHighlighter is deprecated in favour of the UnifiedHighlighter (solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-7.adoc:215-216), and no PostingsSolrHighlighter class is present under solr/core/src/java/org/apache/solr/highlight/ in that commit. [~stewartsims] Is closure OK?
- Citations checked: yes, solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-7.adoc:214-216; ls-tree of solr/core/src/java/org/apache/solr/highlight/ on b73f03976fa (no Postings class)

## SOLR-4304
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0
- Updated: 2013-08-19, status: Open (2 comments: jkrupan 2013-01-14, hossman 2013-08-19; no fixVersion, no issue links)
- Draft: From code reading against apache/main b73f03976fa, SpellCheckComponent.inform leaves queryConverter unset when more than one queryConverter is configured (solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:714-733), and process() then calls queryConverter.convert when spellcheck.q is absent (same file, lines 146-153). Throwing a SolrException from inform() when more than one queryConverter is configured would make this a configuration error, as the 2013-08-19 comment on the ticket proposes. [~jkrupan] Is this fail-fast change OK?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:99, 146-154, 714-733

## Holds and nones
- SOLR-4821 (HOLD): the restriction on non-search handlers in distributed search is still commented out in SolrCore.java:2886-2891. Uncommenting it would also reject sub-requests to PingRequestHandler and ActiveTasksListHandler, which read isShard. The intent is an owner call.
- SOLR-4874 (HOLD): an open performance investigation. The code re-runs each collation check with all original fq parameters, and whether that is a defect needs a measurement.
- SOLR-5035 (HOLD): splitting grouping out of QueryComponent is a design choice about component wiring. Main still couples them.
- SOLR-4998 (HOLD): a broad Slice/Shard/Replica naming cleanup with no settled scope. The named maxShardsPerNode item is already gone on main.
- NONE: none.
