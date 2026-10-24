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

## Item 3: the cuVS note (bafa4eb93b, #5182)

Verdict: CLEAN on substance. Two cosmetic wording findings below; neither changes an artifact. Checked at RC3 `749712b719` on `branch_10_1` (fetched from apache/solr).

Checked and correct:
- The module and the trigger are named right. The codec path is `knnAlgorithm="cagra_hnsw"` (`solr/modules/cuvs/src/java/org/apache/solr/cuvs/CuVSCodec.java` line 66, `DenseVectorField.CAGRA_HNSW_ALGORITHM`). The guide lists the value at `dense-vector-search.adoc` line 129.
- The Lucene claim holds. RC3 uses Lucene 10.4.0 (`gradle/libs.versions.toml` line 39). The module depends on `cuvs-lucene` 25.12.0 (toml line 78). I read the POM of every published cuvs-lucene release from 25.10.0 to 26.10.0 on Maven Central: each declares `lucene-core` 10.2.0. So "a cuVS-lucene version compatible with Lucene 10.4 is not available" is accurate as of 2026-10-24. This is the declared dependency, not a compile run.
- The placement is right. The note is under `== Solr 10.1` (`major-changes-in-solr-10.adoc` line 37, subsection line 80), so a user reading the 10.1 section sees it. The dense-vector guide carries a matching warning at line 1038.
- The ticket reference is accurate. SOLR-18556 exists: Open, affects 10.1, no fix version (public Jira read, 2026-10-24).
- The one action is stated: skip Solr 10.1 and stay on 10.0.x.

Findings (cosmetic, wording only):
1. `major-changes-in-solr-10.adoc` line 88, and the warning at `dense-vector-search.adoc` line 1038: "is expected to be fixed in a subsequent Solr 10.x release". SOLR-18556 carries no fix version and no target. The note states an expectation the ticket does not hold. Suggested: "no fix is scheduled yet; see SOLR-18556", or keep only the ticket link.
2. `major-changes-in-solr-10.adoc` line 84: "(refer SOLR-18556 for updates)" reads as "see". Line 85: "accelertion" is a typo for "acceleration".

Not verified: the note does not say what a user sees when `cagra_hnsw` is used on 10.1 (an error at index time, or something else). It says only "does not work". Not reproduced: a run needs a GPU or a stubbed codec, and was not attempted.

Research only; no build or test was run.
