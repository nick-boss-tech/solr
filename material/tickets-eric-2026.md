# Tickets reported by Eric Pugh in 2026

Enumerated 2026-10-23 from ASF Jira (reporter = epugh, created in 2026): 123 tickets, 42 of them not resolved or closed. Eric is one of the two maintainers Nick works with; his open tickets are a working set for this board, in the same pull order as the other sets: assessed first, volunteered on only under the comment formula.

| Ticket | Status | Priority | Assignee | Created | Title |
|---|---|---|---|---|---|
| CALCITE-7730 | Open | Major |  | 2026-08-20 | Provide a hook in the JDBC driver to inject a custom SqlOperatorTable without mutating the |
| SOLR-18053 | Open | Major |  | 2026-01-03 | Have good solr.xml designed for testing |
| SOLR-18057 | Resolved | Minor | epugh | 2026-01-05 | Review Path.of for places where Path.resolve should be used |
| SOLR-18067 | Resolved | Minor | epugh | 2026-01-10 | Migrate EmbeddedSolrServerTestBase to EmbeddedSolrServerTestRule |
| SOLR-18068 | Open | Minor |  | 2026-01-11 | Support existing xpath and short hand query test syntax + solrTestRule |
| SOLR-18078 | Open | Minor | epugh | 2026-01-20 | Allow "deletion" of automatically created request handlers and query response writers via  |
| SOLR-18079 | Resolved | Minor |  | 2026-01-20 | Allow users to provide their own ImplicitPlugins.json |
| SOLR-18085 | Resolved | Blocker | epugh | 2026-01-25 | wt=standard isn't a helpful concept, replace with wt=json |
| SOLR-18091 | Resolved | Minor | epugh | 2026-01-28 | Separate Core specific information from SystemInfoHandler |
| SOLR-18095 | Resolved | Major | epugh | 2026-01-31 | Allow users to remap writertypes and request handlers to No Operation verion |
| SOLR-18103 | Resolved | Minor | epugh | 2026-02-07 | Remove deprecated SolrXmlConfig.getHiddenSysProps |
| SOLR-18104 | Resolved | Minor | epugh | 2026-02-07 | Remove Snapshots |
| SOLR-18105 | Resolved | Major | epugh | 2026-02-07 | Remove StandardRequestHandler |
| SOLR-18106 | Resolved | Major | epugh | 2026-02-07 | Remove SolrQueryRequest.getCloudDescriptor Deprecation tags |
| SOLR-18108 | Resolved | Major | epugh | 2026-02-07 | Remove deprecated method AuditEvent.getRequestUrl |
| SOLR-18109 | Resolved | Major |  | 2026-02-07 | Fix deprecated SolrPluginUtils.doStandardDebug |
| SOLR-18110 | Resolved | Major | epugh | 2026-02-07 | Remove deprecated attributes from TraceUtils |
| SOLR-18111 | Reopened | Major |  | 2026-02-07 | Remove org.apache.solr.legacy.Legacy* Field Types |
| SOLR-18113 | Closed | Minor |  | 2026-02-09 | Review ZookeeperInfoHandler |
| SOLR-18118 | Resolved | Minor | epugh | 2026-02-11 | Automate bin/solr start -e cloud on CLI by providing input on cli |
| SOLR-18120 | Resolved | Major | epugh | 2026-02-15 | Remove remnant Aliases code from Solr Admin Webapp |
| SOLR-18122 | Resolved | Minor |  | 2026-02-17 | Remove VMParamsAllAndReadonlyDigestZkACLProvider |
| SOLR-18137 | Resolved | Trivial | epugh | 2026-02-26 | prevent FQN class names from being used |
| SOLR-18138 | Open | Minor |  | 2026-02-26 | Handle in CLI when Solr hosting ZK is not reachable |
| SOLR-18139 | Resolved | Minor | epugh | 2026-02-27 | Remove use of Collections.emptyList() and Collections.emptySet() |
| SOLR-18140 | Resolved | Minor | epugh | 2026-02-27 | Enhance Ref Guide page for DELETENODE command |
| SOLR-18145 | Open | Minor |  | 2026-03-03 | Resolve the use of queryUUID and taskUUID in the TaskManagement API |
| SOLR-18148 | Resolved | Minor | epugh | 2026-03-08 | Remove ZkClientClusterStateProvider.createFromJsonSupportingLegacyConfigName |
| SOLR-18149 | Resolved | Minor |  | 2026-03-08 | Remove ClusterState.createFromJson |
| SOLR-18152 | Resolved | Minor |  | 2026-03-10 | Cosmetic improvements and migration to JAX-RS (v2 schemadesigner API) |
| SOLR-18153 | Resolved | Critical | epugh | 2026-03-10 | Admin UI --> Documents Tab --> File Upload runs but shows error in browser |
| SOLR-18162 | Open | Major |  | 2026-03-17 | Support Scale down and up based on Node count |
| SOLR-18163 | Resolved | Blocker | gerlowskija | 2026-03-17 | Figure out V2 API use in Solr 10.1 and later |
| SOLR-18164 | Resolved | Major | epugh | 2026-03-17 | Remove use of singletons |
| SOLR-18167 | Resolved | Blocker | epugh | 2026-03-20 | Conversion of old to new property name not working |
| SOLR-18179 | Resolved | Minor | epugh | 2026-03-29 | Update Concepts and Glossary Ref Guide pages around Cloud/Cluster concepts |
| SOLR-18180 | Open | Minor | epugh | 2026-03-29 | Use Solrj Upload in ConfigSetUploadTool |
| SOLR-18212 | Open | Major | epugh | 2026-04-25 | Cosmetic improvements and migration to JAX-RS (v2 Package API) |
| SOLR-18238 | Open | Minor |  | 2026-05-13 | Add Stronger Validation to V2 API Parameters |
| SOLR-18248 | Resolved | Major |  | 2026-05-18 | Cosmetic improvements and migration to JAX-RS (task crud APIs) |
| SOLR-18269 | Resolved | Blocker | epugh | 2026-05-28 | Restore -c as a noop command in bin/solr start in Solr 10.1 |
| SOLR-18318 | Resolved | Major | epugh | 2026-07-24 | GetNodeSystemInfo (v2 `/api/node/system`) throws NPE when the nodes param is set |
| SOLR-18321 | Resolved | Major | epugh | 2026-07-26 | bin/solr delete should not use ZK if ZK host not provided |
| SOLR-18324 | Resolved | Major | epugh | 2026-07-27 | V2 (JAX-RS) requests can throw a cascading NPE / RTimer assertion failure when a `SolrQuer |
| SOLR-18348 | Open | Major | epugh | 2026-08-15 | `buildOnCommit=true` suggester rebuild blocks commit (and the client request) indefinitely |
| SOLR-18351 | Open | Major |  | 2026-08-18 | Remove ContentStream  |
| SOLR-18352 | Resolved | Major |  | 2026-08-18 | Improve DX by more accurately communicating deprecated code status |
| SOLR-18353 | Resolved | Minor |  | 2026-08-18 | Remove SolrDocumentBase.getChildDocumentCount() |
| SOLR-18354 | Resolved | Major |  | 2026-08-18 | Remove SolrTestCaseJ4 ignoreException/unIgnoreException/resetExceptionIgnores |
| SOLR-18355 | Resolved | Major |  | 2026-08-18 | Remove GeohashFunction and GeohashHaversineFunction |
| SOLR-18356 | Resolved | Major |  | 2026-08-18 | Remove DocsStreamer 2-arg convertLuceneDocToSolrDoc overload |
| SOLR-18357 | Resolved | Minor | epugh | 2026-08-18 | Remove SolrTestCaseJ4 systemSetPropertyEnableUrlAllowList/systemClearPropertySolrEnableUrl |
| SOLR-18358 | Open | Minor | epugh | 2026-08-18 | Remove CollectionAdminRequest.Backup.setIncremental(boolean) |
| SOLR-18359 | Resolved | Minor |  | 2026-08-18 | Remove CollectionAdminRequest.CreateTimeRoutedAlias.setMaxFutureMs(Integer) overload |
| SOLR-18360 | Resolved | Minor |  | 2026-08-18 |  Remove HttpJettySolrClient.addListenerFactory(HttpListenerFactory)  |
| SOLR-18361 | Resolved | Minor |  | 2026-08-18 | Remove LegacyFieldValueFeature (contrib/ltr) |
| SOLR-18362 | Resolved | Minor |  | 2026-08-18 | Remove org.apache.solr.spelling.Token |
| SOLR-18363 | Open | Minor |  | 2026-08-18 | Remove EarlyTerminatingSortingCollector |
| SOLR-18364 | Resolved | Minor |  | 2026-08-18 |  Remove SolrCore.isWriterLocked()  |
| SOLR-18365 | Resolved | Minor | epugh | 2026-08-18 |  Remove DefaultSolrCoreState(DirectoryFactory) single-arg constructor  |
| SOLR-18366 | Resolved | Minor |  | 2026-08-18 | Remove AbstractFullDistribZkTestBase.getTotalReplicas |
| SOLR-18367 | Resolved | Minor |  | 2026-08-18 | waitForFinalState removal |
| SOLR-18368 | Resolved | Major |  | 2026-08-18 | CloudSolrClient.Builder.withInternalClientBuilder |
| SOLR-18369 | Open | Minor |  | 2026-08-18 | Remove deprecated code from Solr 11 |
| SOLR-18370 | Resolved | Major | epugh | 2026-08-18 | Remove ADDROLE/REMOVEROLE Overseer Roles API |
| SOLR-18371 | Resolved | Major | epugh | 2026-08-18 |  Remove deprecated pre-QueryCommand SolrIndexSearcher methods  |
| SOLR-18372 | Resolved | Major | epugh | 2026-08-18 | Remove FunctionQParser.parseMultipleSources |
| SOLR-18373 | Resolved | Minor |  | 2026-08-18 | Remove NamedList.asShallowMap()/get(String,int) and SolrParams.toNamedList() |
| SOLR-18374 | Open | Minor |  | 2026-08-18 | Remove NamedList.NamedListEntry and MapWriterMap |
| SOLR-18375 | Resolved | Minor | epugh | 2026-08-18 | Remove zkHost-based streaming connection API |
| SOLR-18376 | Resolved | Minor |  | 2026-08-18 | Remove ScalarQuantizedDenseVectorField compress/confidenceInterval |
| SOLR-18377 | Resolved | Minor |  | 2026-08-18 | Remove DenseVectorField.getHnswMaxConn/getHnswBeamWidth |
| SOLR-18378 | Resolved | Minor |  | 2026-08-18 | Remove CoreContainer.getCores()/SolrCores.getCores()  |
| SOLR-18379 | Open | Minor |  | 2026-08-18 | Remove DocsStreamer.KNOWN_TYPES |
| SOLR-18380 | Resolved | Minor |  | 2026-08-18 | Remove CloudSolrClient connect()/legacy Builder constructor |
| SOLR-18381 | Resolved | Minor |  | 2026-08-18 | Remove ClusterState.createFromCollectionMap/getReplicaNamesPerCollectionOnNode |
| SOLR-18382 | Resolved | Minor |  | 2026-08-18 |  Remove DocCollection.getReplicas()  |
| SOLR-18383 | Resolved | Minor |  | 2026-08-18 | Remove CollectionParams.FROM_NODE |
| SOLR-18384 | Resolved | Minor | epugh | 2026-08-18 | Remove ShardParams.SHARD_URL | |
| SOLR-18385 | Resolved | Trivial |  | 2026-08-18 | Remove CollectionAdminRequest.PROPERTY_PREFIX |
| SOLR-18386 | Resolved | Minor | epugh | 2026-08-18 | Remove Utils.getBaseUrlForNodeName(String, String) overloads |
| SOLR-18387 | Open | Minor |  | 2026-08-18 | Remove ZkStateReader.getCollectionPathRoot/getCollectionPath |
| SOLR-18388 | Resolved | Minor | epugh | 2026-08-18 | Remove ZkStateReader.getCollectionPathRoot/getCollectionPath |
| SOLR-18389 | Resolved | Minor |  | 2026-08-18 | Remove SolrCore.getAdminResponseWriter |
| SOLR-18390 | Resolved | Minor | dsmiley | 2026-08-18 | Remove test-framework HTTP client convenience helpers |
| SOLR-18404 | Open | Major |  | 2026-08-26 | Re-evaluate use of caching headers in all Solr APIs |
| SOLR-18405 | Open | Minor |  | 2026-08-26 | Adopt SLF4J 2.x lazy-argument-evaluation logging on hot paths |
| SOLR-18425 | Resolved | Major |  | 2026-09-08 | Break up CLUSTERSTATUS V2 API  |
| SOLR-18426 | Open | Major |  | 2026-09-08 | Add v2 API equivalent for "List Shards" |
| SOLR-18427 | Open | Major |  | 2026-09-08 | Add v2 API equivalent for "List Replicas" |
| SOLR-18428 | Open | Major |  | 2026-09-08 | Add v2 API equivalent for "Fetch Single Shard" |
| SOLR-18429 | Open | Major |  | 2026-09-08 | Add v2 API equivalent for "Fetch Single Replica" |
| SOLR-18430 | Open | Major |  | 2026-09-08 | Add v2 API equivalent for "List Replica Props" |
| SOLR-18431 | Open | Major |  | 2026-09-08 | Add v2 API equivalent for "Fetch Replica Prop" |
| SOLR-18438 | Resolved | Major |  | 2026-09-10 | Unify retrieving "live node" information in Solr Admin to a single API |
| SOLR-18439 | Open | Major | epugh | 2026-09-10 | Some v2 "command" APIs report failures via HTTP 200 instead of a proper 4xx/5xx status |
| SOLR-18449 | Open | Minor |  | 2026-09-15 | Ensure test oriented system properties start with test. |
| SOLR-18450 | Resolved | Major |  | 2026-09-16 | Evolve v2 /api/cluster to be limited to limited to collection state and live under V2 /api |
| SOLR-18457 | Open | Major |  | 2026-09-18 | Convert V2 "update" apis to JAX-RS |
| SOLR-18459 | Open | Major |  | 2026-09-20 | Buff up the Ref Guide Umbrella Issue |
| SOLR-18460 | Resolved | Major |  | 2026-09-20 | Ensure all curl commands follow the same pattern |
| SOLR-18461 | Open | Major |  | 2026-09-20 | Make more example commands curl based and using techproducts |
| SOLR-18462 | Resolved | Major |  | 2026-09-20 | Review pages for XML centricity |
| SOLR-18463 | Resolved | Major |  | 2026-09-20 | Reference our cli tools as "Solr CLI"....  looking at you Post Tool |
| SOLR-18464 | Resolved | Major |  | 2026-09-20 | Fix up various antora linking errors |
| SOLR-18465 | Open | Major |  | 2026-09-20 | Spatial page has mis formatted curl commands. |
| SOLR-18467 | Open | Major |  | 2026-09-20 | Look a bit at SEO |
| SOLR-18468 | Resolved | Major |  | 2026-09-20 | Make sure we documented --prompt-inputs |
| SOLR-18469 | Resolved | Major |  | 2026-09-20 | Introduce tabs for v1 and v2 api examples everywhere |
| SOLR-18483 | Open | Minor |  | 2026-09-25 | Prevent wt= parameter from working in V2 land |
| SOLR-18494 | Resolved | Minor |  | 2026-10-01 | Standard formatting for commands in ref guide |
| SOLR-18495 | Resolved | Major |  | 2026-10-02 | Make placeholder name in URL clearer.  /mycollection/blah --> /{collectionName}/blah |
| SOLR-18498 | Resolved | Major |  | 2026-10-04 | Ensure examples work on Core Admin |
| SOLR-18499 | Resolved | Major |  | 2026-10-04 | Re look at v2 api ref guide page |
| SOLR-18500 | Open | Major |  | 2026-10-04 | Get our arms around single,double,no quote on curl commands |
| SOLR-18501 | Open | Blocker | epugh | 2026-10-04 | Solr MCP Project IP Clearance |
| SOLR-18526 | Resolved | Minor |  | 2026-10-10 | Review Ref Guide for User Managed language and commands and move to Standalone |
| SOLR-18527 | Open | Minor |  | 2026-10-10 | Make a "secure" Solr example |
| SOLR-18528 | Open | Major |  | 2026-10-10 | Make a tutorial for security |
| SOLR-18529 | Open | Major |  | 2026-10-10 | Wire in a ngrok/letsencrypt demo |
| SOLR-18545 | Resolved | Major |  | 2026-10-14 | highlight.js doesn't have proper licensing or attribution. |
| SOLR-18546 | Open | Minor |  | 2026-10-14 | Simplify our getting started tutorial by using the collection name "techproducts" instead  |
| SOLR-18553 | Open | Major |  | 2026-10-17 | Review params and path segments for plurality and consistency |
