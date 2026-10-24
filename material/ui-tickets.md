# UI tickets: the census

Enumerated 2026-10-23 from ASF Jira. The new UI working set is every SOLR ticket carrying the `new-ui` label, plus every ticket reported by malliaridis (Christos Malliaridis), plus the five tickets a `wasmJs` text search returns (all already in the first two sets or the Admin UI component). Jira text search is not usable for "kotlin" (it stems to hundreds of unrelated tickets), so membership rests on the label and the reporter, which the UI series uses consistently; every ticket assessed in the four round reports is in this set, and SOLR-18349 (Compose test v2) checks in via the label.

Set size: 67 tickets. Not resolved or closed: 28. Assessed in the four 2026-10-23 rounds: 19 (all of them in the open set). The legacy Admin UI component holds 769 tickets of the old Angular UI and is deliberately not enumerated here.

| Ticket | Status | Reporter | Updated | Assessed | Title |
|---|---|---|---|---|---|
| SOLR-14414 | Open | marcussorealheis | 2025-09-01 |  | New Admin UI |
| SOLR-17326 | Closed | malliaridis | 2025-10-17 |  | Wrong class resolution in generated API response classes |
| SOLR-17358 | Closed | malliaridis | 2025-10-17 |  | Escape + symbol in Documentation The Boolean Operator "+" |
| SOLR-17382 | Closed | malliaridis | 2025-10-17 |  | CLI: Deprecate addlopts in RunExampleTool.java |
| SOLR-17383 | Closed | malliaridis | 2025-01-28 |  | CLI: Resolve overlapping arguments |
| SOLR-17384 | Closed | malliaridis | 2025-10-17 |  | Update ExportTool's argument representation |
| SOLR-17406 | Resolved | malliaridis | 2025-10-17 |  | Introduce Gradle Version Catalogs |
| SOLR-17423 | Closed | malliaridis | 2025-10-17 |  | CLI: Resolve -h argument conflict (help/host) |
| SOLR-17431 | Closed | malliaridis | 2025-01-28 |  | CLI: Resolve -p flag conflict (port, property, param, params) |
| SOLR-17442 | Closed | malliaridis | 2025-10-17 |  | CLI: Resolve -v flag conflict (version, value, verbose) |
| SOLR-17457 | Closed | malliaridis | 2025-03-12 |  | StatusTool does not support --max-wait-secs |
| SOLR-17459 | Closed | malliaridis | 2025-01-28 |  | ConfigTool does not support -scheme |
| SOLR-17469 | Closed | malliaridis | 2025-10-10 |  | CLI: Resolve -f flag conflicts |
| SOLR-17470 | Closed | malliaridis | 2025-10-10 |  | CLI: Resolve -s flag conflicts |
| SOLR-17473 | Closed | malliaridis | 2026-03-04 |  | Apply formatting to build source files |
| SOLR-17479 | Closed | malliaridis | 2025-01-28 |  | CLI: Resolve flag conflicts from AssertTool |
| SOLR-17480 | Closed | malliaridis | 2025-01-28 |  | CLI: Merge solr-data with data-home and resolve -t |
| SOLR-17481 | Closed | malliaridis | 2025-01-28 |  | CLI: Resolve -n flag conflicts |
| SOLR-17482 | Closed | malliaridis | 2025-01-28 |  | CLI: Merge --recurse with --recursive |
| SOLR-17488 | Closed | malliaridis | 2025-01-28 |  | CLI: Resolve -d conflicts |
| SOLR-17489 | Closed | malliaridis | 2025-01-28 |  | CLI: Merge solr-url variants |
| SOLR-17501 | Closed | malliaridis | 2026-03-04 |  | CLI: Reduce responsibilities of SolrCLI |
| SOLR-17511 | Closed | malliaridis | 2025-01-28 |  | CLI: Resolve -i conflicts (async-id, cluster-id) |
| SOLR-17531 | Closed | malliaridis | 2026-03-04 |  | CLI: Optimize GC log options |
| SOLR-17533 | Closed | malliaridis | 2026-03-04 |  | Rearrange and cleanup solr.cmd |
| SOLR-17563 | Closed | malliaridis | 2026-03-04 |  | Cleanup and merge dev-docs |
| SOLR-17567 | Closed | malliaridis | 2026-03-04 |  | Improve Stream CLI implementation |
| SOLR-17571 | Resolved | malliaridis | 2026-08-17 |  | Introduce dependabot |
| SOLR-17572 | Open | malliaridis | 2025-03-02 |  | Migrate Gradle files to use Kotlin DSL |
| SOLR-17585 | Open | malliaridis | 2025-01-30 |  | CodeCache full during Gradle builds |
| SOLR-17595 | Closed | malliaridis | 2025-01-30 |  | CLI examples are broken on Windows |
| SOLR-17598 | Open | malliaridis | 2025-10-10 |  | Windows Script logic that handles SOLR_LOGS_DIR is faulty |
| SOLR-17657 | Open | malliaridis | 2025-11-02 |  | Evaluate and Update checksum and signature verification |
| SOLR-17658 | Closed | malliaridis | 2026-03-04 |  | Implement Admin UI Start Screen |
| SOLR-17659 | Resolved | malliaridis | 2025-09-01 |  | Implement basic authentication in Admin UI |
| SOLR-17660 | Open | malliaridis | 2025-09-01 | yes | Implement basic list component in Admin UI |
| SOLR-17672 | Closed | malliaridis | 2026-03-04 |  | Enable spotless in new UI |
| SOLR-17676 | Open | malliaridis | 2025-02-19 | yes | Enable wasmJs tests |
| SOLR-17845 | Patch Available | malliaridis | 2026-02-22 | yes | Implement authentication with OAuth in Admin UI |
| SOLR-17846 | Open | malliaridis | 2025-09-01 | yes | Introduce Configset Management in New UI |
| SOLR-17847 | Closed | malliaridis | 2026-03-04 |  | Introduce Navigation to Configsets |
| SOLR-17848 | Open | malliaridis | 2025-09-01 | yes | Introduce Index & Query in New UI |
| SOLR-17849 | Open | malliaridis | 2025-10-24 | yes | Implement Index & Query section header |
| SOLR-17850 | Open | malliaridis | 2025-08-09 | yes | Implement solr.SearchHandler UI form |
| SOLR-17861 | Open | malliaridis | 2025-08-12 | yes | Extend updateLicenses task to cover KMP module |
| SOLR-17885 | Reopened | malliaridis | 2025-09-30 | yes | Add support for User Authentication to New UI |
| SOLR-17886 | Open | malliaridis | 2025-09-01 | yes | Implement Reusable Components in New UI |
| SOLR-17887 | Open | malliaridis | 2025-09-10 | yes | Implement File Tree Component |
| SOLR-17896 | Closed | malliaridis | 2026-03-04 |  | Implement a Reusable Tab Navigation Component |
| SOLR-17898 | Open | malliaridis | 2026-06-07 |  | Updating collection property via v2 API endpoint fails with 500 response |
| SOLR-17909 | Open | malliaridis | 2025-09-09 | yes | Implement a Breadcrumb Navigation Component |
| SOLR-17911 | Open | malliaridis | 2025-09-10 | yes | Implement File Content Viewer Component |
| SOLR-17912 | Open | malliaridis | 2025-09-10 | yes | Introduce Collections Management UI |
| SOLR-17913 | Open | malliaridis | 2025-09-10 | yes | Implement Collections Details Card |
| SOLR-17914 | Open | malliaridis | 2025-09-10 | yes | Implement Create Collection Dialog |
| SOLR-17924 | Resolved | malliaridis | 2026-07-21 |  | Create Configset Dialog |
| SOLR-17930 | Closed | malliaridis | 2026-03-04 |  | Improve MultiAuthPlugin compatibility with BasicAuthPlugin |
| SOLR-17936 | Closed | sandbergja | 2026-03-04 |  | New admin ui navigation sidebar is missing focus indicators |
| SOLR-17970 | Open | malliaridis | 2025-12-02 | yes | Start publishing new UI desktop clients to nightlies |
| SOLR-18170 | Resolved | malliaridis | 2026-04-05 |  | Invalid Create Configsets API validation |
| SOLR-18178 | Open | malliaridis | 2026-10-07 |  | Exported configsets from a windows instance are invalid |
| SOLR-18191 | Open | malliaridis | 2026-10-08 |  | Disable lenient json parsing at API level |
| SOLR-18214 | Open | malliaridis | 2026-09-04 |  | Base configset during configset creation is not optional |
| SOLR-18222 | Open | malliaridis | 2026-05-03 | yes | Create Configset Overview Scene |
| SOLR-18223 | Open | malliaridis | 2026-05-04 | yes | Implement editing for solrconfig.xml |
| SOLR-18349 | Resolved | sandbergja | 2026-09-02 |  | Use v2 of runComposeUiTest in the new ui tests |
| SOLR-18493 | Resolved | sandbergja | 2026-10-08 |  | [new ui] Rename Queries & Operations -> Index & Query |
