# CSV Blocker and Critical slice 03 (vm4)

Assignment: `assignments/pool-csv-critical-blocker-round-1.md`, slice 03. Claim: `claims/pool-csv-critblock-03.md`.
Basis: JIRA REST `issues.apache.org/jira/rest/api/2/issue/<KEY>` read 2026-10-20 (UTC). Upstream reference `c8ab71430f` (apache/main). The local clone is shallow; upstream history is checked through the GitHub API.

Status: PARTIAL. Rows marked PENDING have not been classified yet. The claim stays open until every row has a class and evidence.

| Ticket | Summary | Class | Evidence | Next step |
| --- | --- | --- | --- | --- |
| SOLR-14311 | Shared schema should not have access to core level classes | PENDING | Open, updated 2020-11-04. Links: SOLR-14232 (Open), SOLR-14040 (Closed) | Read the code path and the linked issues |
| SOLR-14346 | Solr fails to check zombie server in LbHttpSolrClient | STILL-PRESENT | Same zombie probe as SOLR-12415 (`LBSolrClient.checkAZombieServer`, lines 746 to 768). The ticket uses base URLs with no core (`http://host2:8983/solr`), so the probe takes the null-collection path. Reported on 8.3.1; base not run. Fix is the same rung-4 choice as SOLR-12415 (owner decision on the probe endpoint) | Owner decision on the probe endpoint, shared with SOLR-12415 |
| SOLR-14593 | Package store API to disable file upload over HTTP | STILL-PRESENT | Feature/security request to disable package file upload over HTTP. Base still allows package store upload through the API; no option found to disable it. Feature, not a slice-sized fix | Owner scoping |
| SOLR-14638 | Edismax boost function zero score | PENDING | Open, updated 2020-07-20. No links | Read the code path and the linked issues |
| SOLR-14649 | Package manager should use SHA512, not SHA1 | ALREADY-FIXED | Package manifests are verified with SHA-512: `PackageAPI.java` carries `manifestSHA512` (lines 186, 195, 226), and `solr/core/src/java/org/apache/solr/packagemanager` has no `sha1` or `DigestUtils` hit at base. Fixing commit not identified | Owner note: confirm the signing path if the ticket is still open for the store, not the manifest |
| SOLR-14788 | Solr: The Next Big Thing | STILL-PRESENT | Open umbrella 'Solr: The Next Big Thing' with linked SOLR-12037 (Open). Not a defect; umbrella | Owner scoping: umbrella, not a slice-sized fix |
| SOLR-14833 |  Empty highlight entry on match only for some queries  | PENDING | Open, updated 2020-10-09. No links | Read the code path and the linked issues |
| SOLR-14996 | Facet incorrect counts when FQ exclusion applied with collapsing | PENDING | Open, updated 2025-07-23. No links | Read the code path and the linked issues |
| SOLR-15096 | [REGRESSION] Collection Delete Performance significantly degraded in Java 11 v 8 | PENDING | Open, updated 2022-02-10. No links | Read the code path and the linked issues |
| SOLR-15237 | Distributed search with index sharding is not working with basic authentication  | PENDING | Open, updated 2024-02-27. Links: SOLR-15235 (Resolved), SOLR-15236 (Resolved) | Read the code path and the linked issues |
| SOLR-15326 | Missing one record in solr streaming expressions | PENDING | Open, updated 2021-04-12. No links | Read the code path and the linked issues |
| SOLR-15412 | Strict validation on Replica metadata can cause complete outage | PENDING | Open, updated 2021-09-02. No links | Read the code path and the linked issues |
| SOLR-15596 | Ability for packages to be upgraded over major versions | PENDING | Open, updated 2021-09-28. No links | Read the code path and the linked issues |
| SOLR-15759 | clean cloud collection terms | PENDING | Open, updated 2021-11-17. No links | Read the code path and the linked issues |
| SOLR-15998 | replicaType=TLOG oom killer event occurs | PENDING | Open, updated 2022-05-12. No links | Read the code path and the linked issues |
| SOLR-16100 | Solr tool does not work with custom zkACLProvider | PENDING | Open, updated 2022-05-12. No links | Read the code path and the linked issues |
| SOLR-16139 | [Regression] JSON stat facet functions not working on analysed String (SortableT | PENDING | Open, updated 2024-02-16. Links: SOLR-8319 (Closed), SOLR-13056 (Open) | Read the code path and the linked issues |
| SOLR-16155 | DocumentBuilder should not include field values in error messages | STILL-PRESENT | Still present at base: `DocumentBuilder.java` lines 274 to 285 put the field value into the error message (`"'" + field.getValue() + "' msg="`). The ticket's patch is attached (Patch Available, updated 2026-09-30) and is not in base. Not run | Follow-up: drop the value from the message, with a test, then a gate |
| SOLR-16450 | Proper handling on watcher registration failure in `ZkStateReader#waitForState | PENDING | Reopened, updated 2024-10-24. No links | Read the code path and the linked issues |
| SOLR-16509 | All query classes should be immutable | STILL-PRESENT | Status Patch Available with a linked SOLR-16489 (also Patch Available). The immutable query classes change is not in base; the patches were not reviewed here | Owner or main agent: review the patch chain |
| SOLR-16655 | Indexing nested documents and URPs | PENDING | Open, updated 2026-10-09. No links | Read the code path and the linked issues |
| SOLR-16725 | CLUSTERSTATUS API output has inconsistent data types for a few values against th | PENDING | Open, updated 2023-06-06. No links | Read the code path and the linked issues |
| SOLR-16914 | Japanese Synonyms Search not workinh | PENDING | Open, updated 2024-12-20. No links | Read the code path and the linked issues |
| SOLR-17122 | Solr Unique, HLL, numBuckets returns incorrect counts | PENDING | Open, updated 2024-01-18. No links | Read the code path and the linked issues |
| SOLR-17143 | Streaming with multiple shards can trigger unexpected IdleTimeout | PENDING | Open, updated 2025-03-11. Links: SOLR-17286 (Closed) | Read the code path and the linked issues |
