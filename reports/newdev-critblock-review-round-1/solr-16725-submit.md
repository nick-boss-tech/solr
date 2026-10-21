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


## Pass 2 (vm3, 2026-10-21T02:52:55Z)

- Head reviewed: `550af283134e2f` (unchanged from pass 1). Base `14c7aac0d1`.
- Review: re-read the test assertions against the production change. `assertReplicaCountTypesMatch` checks presence, class and value for the four keys, so a regression to Long on either side fails. Re-checked the surrounding code: the four keys are the ones `ClusterStatus` reports as counts; `maxShardsPerNode` is no longer emitted (pass 1).
- Proof leg, on base `14c7aac0d1` with `ClusterStatus.java` at base and the head's test file: `testCustomProperties` FAILED with `AssertionError: Source replica count for replicationFactor must be reported as a string expected:<class java.lang.String> but was:<class java.lang.Long>`. This is the expected failure, so the test discriminates the change at this head. Log `/workspace/gates/logs/vm3/solr-16725-review-pass-2/proof-base.log`, counts `/workspace/gates/logs/vm3/solr-16725-review-pass-2/count-proof.txt` (7 tests, 1 failure). Base worktree restored to clean.
- Repairs in pass 2: none.
- Regate: `gates/SOLR-16725-review-pass-2.md`, GREEN at `550af28313`. Changelog rc 0; tidy rc 0, empty status; Error Prone compile rc 0 with `--rerun` (`compileJava` and `compileTestJava` executed); focused head run 1 class, 7 tests, 0 failures, 0 errors, 1 skipped (fresh XML, `count-core.txt`); check rc 0.

Pass 2 result: GREEN at `550af28313`, proof leg fails on base as expected.
