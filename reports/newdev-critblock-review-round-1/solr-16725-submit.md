# Review round 1, unit 14: SOLR-16725 (branch solr-16725-submit), vm3

## Pass 1 (vm3, 2026-10-21T02:25:12Z)

- Head reviewed: `550af283134e2f` (fork tip at claim time). Base `14c7aac0d1`. Diff: 3 files, 92 insertions, 1 deletion (changelog, `ClusterStatus.java`, `LocalFSCloudIncrementalBackupTest.java`).
- Ticket SOLR-16725 (Open, Critical): CLUSTERSTATUS shows replication and replica-count values as strings for CREATE collections and as numbers for RESTOREd ones. The ticket's example shows the string form as the CREATE output.
- Change: `normalizeCollectionCountTypes` converts `replicationFactor`, `nrtReplicas`, `tlogReplicas` and `pullReplicas` to strings in `buildResponseForCollection`, after the collection JSON is read and before the response is built. Matches the ticket's direction.
- Surrounding code check: the ticket's example also lists `maxShardsPerNode` as a number on the RESTOREd collection. No `maxShardsPerNode` appears in `solr/core/src/java` or `solr/solrj/src/java` at the head, so the code path no longer emits that field; the ticket example predates it. Not a gap on this branch.
- Test: the new `assertReplicaCountTypesMatch` compares the four keys between a CREATE and a RESTOREd collection, checking presence, class and value. It covers exactly the four keys the fix converts.
- Changelog: present, `type: fixed`, links SOLR-16725.
- Repairs in pass 1: none.
- Regate: `gates/SOLR-16725-review-pass-1.md`, GREEN at `550af28313`. Logs `gates/logs/vm3/solr-16725-review-pass-1/`. Changelog rc 0; tidy rc 0, status empty; compile rc 0 with `-Pvalidation.errorprone=true --rerun` (`compileJava` and `compileTestJava` executed, no Error Prone skip warning in the compile log); `LocalFSCloudIncrementalBackupTest` seed `16725C0FFEE16725`, fresh XML: 1 class, 7 tests, 0 failures, 0 errors, 1 skipped; `:solr:core:check -x test` rc 0.

