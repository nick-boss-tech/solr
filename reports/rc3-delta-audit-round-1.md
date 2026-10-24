# Report: rc3-delta-audit-round-1

Read-only audit of the Solr 10.1.0 RC2 (`dfda5e1d48`) to RC3 (`749712b719`, the tip of `branch_10_1`) delta. Each section is one claimed item and ends with a verdict. No code changed, no comments posted.

## Item 1: revert completeness (SOLR-18450)

Commits: relocation `2db17cf258` and test change `3a86b4943b` (both in the delta), reverts `aec4a3fbf2` and `e7ef1a935f` (both in the delta). Method: compare RC3 with the parent of the relocation commit (`2db17cf258^`), the state the reverts should restore, on every path the four commits touch (20 paths).

Result:
- The relocated endpoint files are identical to the pre-relocation parent at RC3: `solr/api/.../client/api/model/ListCollectionsResponse.java` (it existed before the relocation), `.../endpoint/ListCollectionsApi.java`, `solr/core/.../handler/admin/api/ListCollections.java`, `solr/core/.../handler/ClusterAPI.java`, and `solr/packaging/test/test_auth.bats`.
- The removed detail tests and the `SOLR-18450-v2-collections-detailed.yml` changelog entry are absent at RC3 (`git grep` for `ListCollectionsDetailed`, `SOLR-18450`, `collections-detailed` returns nothing).
- The collection-management ref-guide page added by the relocation is absent at RC3. Its changes to `cluster-node-management.adoc` and `v2-api.adoc` are also back to the pre-relocation state.
- Three of the 20 paths differ from the pre-relocation parent, and each difference comes from a different delta commit, not from the relocation: `ClusterStatus.java` (SOLR-17820, `a3603260ea`, moves the replica cross-check call), `CollectionsHandler.java` (SOLR-17398, `fb56d1f6cc`, adds V2 equivalent javadoc and imports), and `solr/webapp/.../app.js` (SOLR-18400, `c1e73c1392`, a 510 handler).
- `GET /api/cluster` is the live path at RC3 (the test in `test_auth.bats` lines 56 and 60 uses it), which matches the pre-relocation behavior. CHANGELOG.md still lists the old `GET /api/cluster` entries; those are the 10.0 notes and describe the original CLUSTERSTATUS endpoint, so they are not stale.

Not verified: runtime behavior. The check is by file content, not by running the endpoint at RC2 and RC3.

Verdict: CLEAN. No vestige of the relocation at RC3.
