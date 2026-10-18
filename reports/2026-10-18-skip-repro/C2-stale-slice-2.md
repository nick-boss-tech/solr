# C2 stale-slice-2 partial report

Pinned source: `origin/main` at `498a09aea97aac86c2cc7ac114113b946d249942`.
This partial pass checked 11 of 61 assignment rows. The remaining 50 were not
reached and are listed below so the assignment can stay open for another
reviewer. The separate Tier 2 audit batch is recorded against
`upstream/main` at `c8ab71430f6e7547c773da596101d680862310ff` in
`material/skip-audit/tier2-audit-2026-10-17-c.json`.

No builds or tests were run. No JIRA, GitHub, or email posts were made.

| Ticket | Status | Verdict | Evidence or next step |
| --- | --- | --- | --- |
| SOLR-5559 | reviewed | fixed | `solr/core/src/java/org/apache/solr/core/SolrXmlConfig.java:237-243` explicitly rejects the old `solr/cores` configuration used by the report. |
| SOLR-5566 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-5579 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-5712 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-5736 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-5759 | reviewed | unclear | `HighlightComponent.java:59,61,177,186` keeps `original` selectable while defaulting to `unified`; the ticket lacks a self-contained current reproduction, so verify the reported case with `hl.method=original` and the full analyzer/query settings. |
| SOLR-5788 | reviewed | unclear | `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:226-262` builds remote exceptions from HTTP response errors. The old report's exact message path was not established; a current request/schema reproduction is needed. |
| SOLR-5833 | reviewed | gone | No `forceOverSeer` hit on the pinned Solr tree. `solr/core/src/java/org/apache/solr/cloud/OverseerElectionContext.java:42,64` uses an ephemeral elected leader node; the old force-overseer hook is gone. |
| SOLR-5850 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-5878 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-5919 | reviewed | present | `solr/test-framework/src/java/org/apache/solr/cloud/AbstractFullDistribZkTestBase.java:384-407` still creates and uses `control_collection`; this is the test framework's cloud topology discussed by maintainers. |
| SOLR-5990 | reviewed | unclear | `solr/core/src/java/org/apache/solr/core/NodeConfig.java:103,210-211,327,603,756` and `solr/solr-ref-guide/modules/configuration-guide/pages/configuring-solr-xml.adoc:156` expose `coreLoadThreads`. Current logs and settings are missing, so incomplete startup cannot be attributed to a product defect. |
| SOLR-5995 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6184 | reviewed | present | `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java:1654` defines `commitReserveDuration`; `solr/solr-ref-guide/modules/deployment-guide/pages/user-managed-index-replication.adoc:133,154` documents the setting. A current reproduction is still needed to establish whether the reported recovery failure remains. |
| SOLR-6255 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6366 | reviewed | fixed | `solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java:67-107` bounds collation attempts by `maxTries` and `maxCollations`. |
| SOLR-6429 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6468 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6525 | reviewed | unclear | `solr/core/src/java/org/apache/solr/cloud/Overseer.java:136,282,413` shows the current queue processor. The umbrella ticket has no current workload, latency measurements, or reproducible slow operation to isolate a defect. |
| SOLR-6534 | reviewed | fixed | The JIRA discussion attributes the geodetic-bounds issue to Spatial4j; pinned `gradle/libs.versions.toml:175,406` uses LocationTech Spatial4j 0.8, and the ticket records the fix in Solr 5.0. |
| SOLR-6561 | reviewed | gone | The old `LBHttpSolrServer` class is absent; current `solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:123,186` contains its replacement and only a historical mention. JIRA says the reporter stopped passing a null `ResponseParser`. |
| SOLR-6582 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6678 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6712 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6756 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6782 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6811 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6822 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6828 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6836 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6890 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6903 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6922 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6929 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-6934 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7088 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7153 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7192 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7249 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7295 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7343 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7415 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7482 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7536 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7559 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7612 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7721 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7806 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7846 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7867 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7973 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-7982 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8079 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8098 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8106 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8117 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8171 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8310 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8365 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8469 | not reached |  | Not reached in this partial C2 pass. |
| SOLR-8572 | not reached |  | Not reached in this partial C2 pass. |
