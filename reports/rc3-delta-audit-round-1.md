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

## Item 2: the cherry-picked fixes (eight commits)

Method: match each backport to its main-branch original by subject, then compare the two patches by `git patch-id`. Identical patch-id means the same change, so the test files that came with the original are in the backport. A patch that differs is read line by line.

| Backport (RC3) | Main original | Patch | Tests in backport | Result |
| --- | --- | --- | --- | --- |
| fd42ef4d7f SOLR-18031 `bin/solr post --filetypes` | d97bc70cef | identical | 1 | CLEAN |
| e884a1bbd8 SolrTestCase reset system properties | 9d7cc2884e | identical | 0 | CLEAN |
| fb56d1f6cc SOLR-17398 V1 to V2 links | 1d7a1e9062 | identical | 0 | CLEAN |
| c1e73c1392 SOLR-18400 plugins screen 500 | e34067ae64 | identical | 2 | CLEAN |
| a3603260ea SOLR-17820 CLUSTERSTATUS health | 7e3b175021 | differs (AGENTS.md hunk only) | 1 | FINDING, cosmetic (below) |
| c8c86434cd SOLR-18520 package uninstall | 2f2b21be65 | identical | 0 | CLEAN |
| d43e574fd2 SOLR-18166 linkconfig vestiges | 31d089b1c9 | identical | 0 | CLEAN |
| a8cda55615 Documented V2 API Conventions | bc428ba81b | identical | 0 | CLEAN |

Finding, a3603260ea (SOLR-17820). The `ClusterStatus.java` and `ClusterStatusTest.java` hunks are the same change as the main commit. The difference is in `AGENTS.md`, which the main commit also edits:
- The backport adds the line "System properties set in a test are restored after each test by the base classes (`SystemPropertiesRestoreRule`)...". The main-branch line comes from `5e044a331f` (CLI commit, #5054), which is not in the RC2 to RC3 delta. The line is accurate at RC3: `SystemPropertiesRestoreRule` is in `solr/test-framework/.../SolrTestCase.java`. It is still an extra, out of this ticket's scope.
- The main commit changes "Avoid SolrTestCaseJ4 for new tests" to "Use SolrTestCase instead of SolrTestCaseJ4 for new tests". The backport does not make that edit, and RC3 still says "Avoid".
- Effect: `AGENTS.md` is a contributor file, not a shipped artifact. It does not affect the vote. Severity: cosmetic.

Not verified: the backports were not built or tested here. A patch that applies cleanly shows the premise holds textually, not that it compiles at RC3.

Verdict for item 2: seven of eight CLEAN. One cosmetic finding (a3603260ea, AGENTS.md hunk).

## Item 4: the changelog state (1a45846c45, 3702672a00, bba4024eed, 749712b719)

Verdict: FINDING on the dependency list (cross-category duplicates remain). The SOLR-18450 check is CLEAN. The cuVS point is a question for the owner, not a defect. Checked at RC3 `749712b719` (CHANGELOG.md and `changelog/v10.1.0/`). The RC2 complaint text is not in this assignment, so the "two biggish issues besides" were not checked.

1. Dependency list de-duplicated? Partly. No artifact appears twice within one list. Across lists it does not hold:
   - `langchain4j`: CHANGELOG.md line 291 lists "langchain4j-* (5 jars) 1.19.0" under "added since 10.0.0". Line 292 lists the same five jars under "upgraded since 10.0.0" (cohere and hugging-face 0.35.0 → 1.19.0-beta29; core, mistral-ai and open-ai 0.35.0 → 1.19.0). At 10.0.0 the 0.35.0 jars were present (`solr/licenses`, e.g. `langchain4j-core-0.35.0.jar.sha1`). They are upgrades, not additions. The version for cohere and hugging-face also differs between the two lines (1.19.0 vs 1.19.0-beta29).
   - `google-api-services-storage-v1`: line 291 lists rev20260524 under "added", and line 293 lists rev20240621 under "removed". It is one upgrade. The same pattern: `changelog/v10.1.0/dependency-changes-3-removed.yml` carries the removed name.
   Cause (rung 1, `dev-tools/scripts/dependencyChanges.py`): the summary compares license filenames. A version bump changes the filename, so it appears as a removal plus an addition, or as an addition plus an upgrade. RC3 `solr/licenses` holds `langchain4j-core-1.19.0.jar.sha1` and no 0.35.0 file, which confirms it.
2. SOLR-18450 not announced: CLEAN. `grep` for 18450, and for the relocated endpoint, finds nothing in CHANGELOG.md or `changelog/v10.1.0/`. Line 96 mentions `/api/cluster/commands`, which is SOLR-16390, not the reverted change.
3. The cuVS note in the changelog: the changelog carries no 10.1 breakage line. It lists the cuVS feature (line 355, SOLR-17892) and a GPU fix (line 186, SOLR-18210, "Fix for GPU vector indexing silently falling back to using CPU instead") in the same release where GPU indexing is documented as broken (upgrade notes). Whether a known-issue line belongs in the changelog is the owner's convention call. Not a defect. Nothing on this branch shows a known-issues section in the changelog to follow.
4. The dependencyChanges.py changes do not explain the difference. Commit 1a45846c45 deletes solrbot's per-PR `dependency_update` entries. Commit 3702672a00 extends that to `.yaml` files and to the released folder. Neither changes the filename-based categorization, and 1a45846c45's own docstring says jars that moved are "listed together". The split above is the open issue. Suggested fix for the release manager: key the comparison on artifact name, not filename, or hand-check the three categories against `solr/licenses` before the vote.

Bearing on the vote: none of these change an artifact. They are release-note accuracy findings, and the owner decides whether they block the RC.

Research only; no build or test was run.

## Ranking of findings

No finding changes a release artifact. Each item below is a release-note or contributor-doc accuracy point. Clean items are not listed.

Bears on the vote (release-note accuracy, read by the vote thread and users):
1. Item 4, dependency list. Cross-category duplicates remain. `langchain4j` (five jars) and `google-api-services-storage-v1` each appear as added and as upgraded or removed. The cause is filename-based comparison in `dependencyChanges.py`. Fix: key on artifact name, or hand-check the three categories against `solr/licenses` before the vote.
2. Item 3, finding 1. `major-changes-in-solr-10.adoc` line 88 and `dense-vector-search.adoc` line 1038 say the cuVS problem "is expected to be fixed in a subsequent Solr 10.x release". SOLR-18556 has no fix version and no target. The release note states an expectation the ticket does not hold. Fix: "no fix is scheduled yet; see SOLR-18556".

Cosmetic:
3. Item 3, finding 2. Typo "accelertion" (line 85) and the wording "refer SOLR-18556 for updates" (line 84).
4. Item 2, a3603260ea. `AGENTS.md` gains a line from `5e044a331f` (outside this delta). The main commit's own "Use SolrTestCase" edit is not carried. Contributor doc only.
5. Item 4, finding 3. Whether a known-issue line belongs in the changelog for cuVS is a convention question for the owner, not a defect.

Owner questions (not findings): the cuVS note does not say what a user sees when `cagra_hnsw` is used on 10.1 (item 3, not verified), and whether the RC3 known-issue convention needs a changelog line (item 4.3).

Overall: no artifact correctness finding in the RC2 to RC3 delta. Two release-note items bear on the vote; the rest are cosmetic.
