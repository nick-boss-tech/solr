# Pool assignment: Verify removed-feature tickets, round 1

Status: OPEN

- Slug: `pool-skip-review-removed-round-1`.
- Capability tags: review.
- Intended host: vm4 (after A3), or any Linux host. Claim under `claims/` before starting.
- Posted by: vm5 (main agent coordinator), 2026-10-18, at the direction of Nick.

### Items

1. **Check each ticket.** For each row, apply the Method in the Decisions table and record the verdict. Output: one row per ticket in the deliverable.
2. **Record.** Write results into the deliverable as you go, so a stop keeps partial results. Name any ticket you did not reach.

### Tickets

| Ticket | Summary | Skip note (from queue) | Verdict |
| --- | --- | --- | --- |
| SOLR-1658 | Negative query weirdness | obsolete: nested pure negative with AND is handled (TestSolrQueryParser ~L1936) | |
| SOLR-2976 | stats.facet no longer works on single valued trie fields that don't use precision step | Trie precision step stats, removed | |
| SOLR-3064 | Solrj sets no content type on ContentStreamUpdateRequest.addFile | obsolete: ContentStreamUpdateRequest removed | |
| SOLR-3124 | explain output is confusing when using trie fields (or any field type where the indexed terms are... | explain readability for Trie, Trie removed | |
| SOLR-3201 | Recurrence resolver into VariableResolver | DIH removed | |
| SOLR-4113 | Fullpath for db-config file causes error on SolrCloud | DIH removed | |
| SOLR-4183 | The "clean" parameter in DataImport causes the other nodes to empty and commit before indexing is... | DIH removed | |
| SOLR-4382 | edismax, qf, multiterm analyzer, wildcard query bug | obsolete: edismax validates qf fields (validateQueryFields) | |
| SOLR-4390 | SolrJ does not URL-encode query string argument names but should | obsolete: SolrParams.toQueryString URL-encodes names | |
| SOLR-4522 | SimpleFacets.getListedTermCounts wont work for docvalues-only fields (or other custom fieldtypes) | 4.x docvalues facet refinement term query; Trie/points reworked | |
| SOLR-4610 | Calling query.toString will print binary values for encoded integer values in terms. | query toString display of encoded ints, Trie removed | |
| SOLR-5383 | Group Faceting does not play nice with Trie fields that use precsionStep | Trie precisionStep with group.facet, Trie removed | |
| SOLR-5717 | group.field returns no groups for trie fields with non zero precisionstep | Trie field with precisionStep, removed | |
| SOLR-6142 | Solr Core pom.xml should not depend on Jetty and others | build/pom dependency scope, obsolete (Gradle) | |
| SOLR-6166 | HttpSolrServer does not properly implement the Serializable interface | HttpSolrServer serialization, old class removed | |
| SOLR-6303 | JDBC drivers not being released causing permgen memory leak | JDBC leak with DIH, removed | |
| SOLR-6395 | If the overseer queue is large, then the cloud tree view (admin UI) hangs | Admin UI angular, obsolete | |
| SOLR-6875 | No data integrity between replicas | 4.10 replica inconsistency, LIR era, obsolete | |
| SOLR-7009 | Body of a request does not print out in XMLLoader when setting log level to TRACE | obsolete: XMLLoader trace log removed/changed | |
| SOLR-7221 | ConcurrentUpdateSolrServer does not work with HttpClientBuilder (4.3.1) | 4.3 SolrJ client migration, obsolete | |
| SOLR-7517 | When you can't persist a core.properties file we simply log an error and continue. | obsolete: CorePropertiesLocator throws on persist failure | |
| SOLR-7773 | Can not overwrite mem settings with env variable | bin/solr env override, obsolete script | |
| SOLR-7953 | BaseEditorialTransformer (elevated) doesn't work with LazyField | obsolete: getKey uses IndexableField.stringValue, which LazyField supports | |
| SOLR-7998 | Solr start/stop script is currently incompatible with SUSE 11 | bin/solr script on SUSE 11, obsolete | |
| SOLR-8024 | Json facet api fails on multivalue trieint with docvalues | 5.3 Trie field, removed | |
| SOLR-8076 | SEVERE: Unable to load Transformer: foo.FieldTransformer | DIH removed | |
| SOLR-8087 | Look into defensive check in publish that will not let a replica in LIR publish ACTIVE. | LIR removed, obsolete | |
| SOLR-8089 | Support query parsers being able to set enablePositionIncrements | enablePositionIncrements removed in Lucene; design | |
| SOLR-8158 | Analytics module not supporting multi-valued int values | analytics component removed | |
| SOLR-8463 | FacetComponent throws NPE when using shards.tolerant and a shard fails to respond | obsolete: FacetComponent.countFacets skips shards without facet_counts | |
| SOLR-8843 | Missing fallback for NoChildrenForEphemeralsException on ZkController.getLeaderPropsWithFallback ... | 5.4 rolling upgrade fallback, obsolete | |
| SOLR-8991 | UI: Ping option doesn't update menu with response time if it's 0ms | Admin UI angular, obsolete | |
| SOLR-9094 | ReRanker should gracefully handle sorts without score | 4.10.4 reranker config, obsolete | |
| SOLR-9126 | shutdownAndAwaitTermination interruption handling faulty? | obsolete: ExecutorUtil.awaitTermination now handles interrupt | |
| SOLR-9202 | UninvertingReader needs multi-valued points support | UninvertingReader points, moot (Lucene removed) | |
| SOLR-9267 | Cloud MLT field boost not working | obsolete: AbstractMLTQParser now parses field boosts with SolrPluginUtils.parseFieldBoosts | |
| SOLR-9366 | replicas in LIR seem to still be participating in search requests | LIR removed, committed fixes | |
| SOLR-9400 | Casting error when using SortedMapBackedCache | DIH removed from Solr core | |
| SOLR-9677 | edismax treat operator as a keyword when a query parameter 'qf' contains inexist field. | obsolete: ExtendedDismaxQParser.validateQueryFields fails on unknown qf fields | |
| SOLR-9749 | /admin/plugins endpoint doesn't have a paramset in ImplicitPlugins.json | obsolete: /admin/plugins not in ImplicitPlugins.json on main | |
| SOLR-9809 | TrieField.createFields produces useless IndexableField instances when field is stored=false index... | TrieField removed | |

### Decisions

| Decision | Answer or default |
| --- | --- |
| Head | decided: the fork origin/main sha 498a09aea9 at posting time. Record the sha you read. Do not move it. |
| Method | decided: read the ticket description (anonymous JIRA REST at issues.apache.org/jira/rest/api/2/issue/KEY, read only), then check the named code path on the pinned head with `git grep` or `git show`. Cite file path and line. |
| Verdict set | decided: exactly one of: gone (cite the removal or the grep showing no hits), present (cite the code path still there), fixed (cite the commit that changed it), unclear (say what is missing). No other verdicts. |
| Unclear | default: mark unclear and state the missing piece; do not guess a verdict. |
| Gradle | decided: none. No builds or test runs in a review item. |
| Closing or commenting | decided: none. No JIRA, GitHub or email posts. |
| Scope | decided: the rows in this table. |

### Deliverable

`reports/2026-10-18-skip-repro/B1-removed-round-1.md`: one row per ticket with verdict and the cited path or grep.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

