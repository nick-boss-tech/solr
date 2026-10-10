# Core admin round 1, part k6: SOLR-6438, 8275, 8576, 15003, 16725, 16887, 17731

Result: Draftable: 6438, 8275 (PR-ready), 8576 (after FIX 1), 16725 (one Choice, FIX 5), 17731 (after FIX 2, 3, 4). Held: 15003 (tip moved, gate owed), 16887 (FIX 6 and 7; the receipt says PR-ready and this audit disagrees).

Scope: read only. No builds, tests, BATS runs, gh write calls, pushes, or commits. Heads are the origin refs in `source/.git` (all seven match the claim table). Receipts are taken as the record; their gate logs are not on disk. Drafts are in `pr-drafts/core-admin/` (SOLR-6438.md, SOLR-8275.md, SOLR-8576.md, SOLR-16725.md, SOLR-17731.md). Each draft names its head in the Proof. Each draft ends with an INTERNAL comment that must be removed before posting. No draft names a Lucene version.

## Findings

**FIX 1 (SOLR-8576): the alias check tests the wrong thing.**
File: `solr/core/src/test/org/apache/solr/cloud/CollectionsAPISolrJTest.java` at 4c46f95c7851, lines 1185-1188.
Evidence: line 1188 is `assertFalse(solrClient.getClusterState().hasCollection(aliasName));`. It checks that no collection has the alias's name. Line 1185 says the alias must be undisturbed, but no line reads the alias.
Replacement for lines 1185-1188:
```java
    // the rejected creates must not have changed the collection or the alias
    assertEquals(
        collectionName, solrClient.getClusterState().getCollection(collectionName).getName());
    cluster.getZkStateReader().aliasesManager.update();
    assertEquals(
        collectionName, cluster.getZkStateReader().getAliases().resolveSimpleAlias(aliasName));
    assertFalse(solrClient.getClusterState().hasCollection(aliasName));
```
The `aliasesManager.update()` call matches line 1042 of the same file on main. The edit is not run here. The class must be run at the new head before the draft is posted.

**FIX 2 (SOLR-17731): a changed test class has no recorded run.**
File: `solr/core/src/test/org/apache/solr/handler/admin/api/ListAliasesAPITest.java` (4 lines changed; `git diff --stat upstream/main...origin/solr-17731-submit`).
Evidence: the receipt counts only `V2ResourcePathOverlapTest` 2 of 2. `ListAliasesAPITest` changes because `getAliasByName` moved to `GetAliasByName`, and no count is recorded for it at the head.
Replacement: none in code. Owed: a run of `ListAliasesAPITest` at the head (main side). The draft does not claim it.

**FIX 3 (SOLR-17731): malformed license header in two new files.**
Files: `solr/core/src/java/org/apache/solr/handler/admin/api/GetAliasByName.java` line 16 and `solr/core/src/test/org/apache/solr/handler/admin/api/V2ResourcePathOverlapTest.java` line 16, at f2b4ba164f56.
Evidence: both lines read ` */ package org.apache.solr.handler.admin.api;`. The package line sits on the license comment's closing line. Commit dc6ce063c72 ("apply tidy formatting to the new API files") did not fix it. The receipt says "tidy clean", which disagrees. Not checked: which tool would rewrite the line.
Replacement for line 16 in both files:
```java
 */
package org.apache.solr.handler.admin.api;
```

**FIX 4 (SOLR-17731): comments state a Jersey rule as fact.**
Files: `solr/api/src/java/org/apache/solr/client/api/endpoint/CollectionSnapshotApis.java` lines 37-39, and `solr/api/src/java/org/apache/solr/client/api/endpoint/GetAliasByNameApi.java` lines 29-31.
Evidence: both comments say JAX-RS picks one resource class per path. The main-side notes call that rule the lowest-confidence point of the fix ("does Jersey merge identical class-level paths?"). The routing rule is not verified here. The receipt shows only the observed result (405 before, pass after). The same lowest-confidence point is at `C:/Users/shaninna/dev/Solr-issues/research/pipeline/HANDOFF.md` line 193.
Replacement for `CollectionSnapshotApis.java` lines 37-39:
```java
  // The Create and Delete resources share one class-level path. Before that, POST to this path
  // was answered with a 405 by the Delete resource (see V2ResourcePathOverlapTest).
```
Replacement for `GetAliasByNameApi.java` lines 29-31:
```java
 * <p>This shares its class-level path with {@link DeleteAliasApi}. Before that, the delete
 * resource answered {@code GET /aliases/{aliasName}} with a 405 (see V2ResourcePathOverlapTest).
```

**FIX 5 (SOLR-16725): the draft's Limits must name `maxShardsPerNode`.**
Files: the ticket text (`research/jira-context/SOLR-16725.json`, description example) and `solr/core/src/java/org/apache/solr/handler/admin/ClusterStatus.java` lines 341-353 at be1838ef8ccf.
Evidence: the ticket shows `"maxShardsPerNode":"1"` for a CREATE collection and `"maxShardsPerNode":3` for a restored one. The helper converts only `replicationFactor`, `nrtReplicas`, `tlogReplicas` and `pullReplicas`. The test checks the same four keys (`LocalFSCloudIncrementalBackupTest.java` lines 179-181).
Replacement: the Limits line already in the draft: "The ticket's example also shows `maxShardsPerNode` as a string for CREATE and as a number for RESTORE. This change does not touch that key. A follow-up can cover it on request." Owner decision 5 covers the alternative of normalizing it in this PR, which would need a re-gate.

**FIX 6 (SOLR-16887): the branch drops `-XX:ErrorFile`, which the ticket does not ask for.**
Files: `solr/bin/solr` (at 16887 head, `ExitOnOutOfMemoryError` at line 1332; the old line was base line 1336) and `solr/bin/solr.cmd` (head line 1106; base line 1121). Also the doc example `solr/solr-ref-guide/modules/configuration-guide/pages/system-info-handler.adoc` around line 237.
Evidence: `-XX:ErrorFile=.../jvm_crash_%p.log` is the file the JVM writes for any fatal error, not only OOM. The branch removes it in both scripts. The ticket asks only for Exit instead of Crash. The changelog and the ref guide do not mention the removal. Nothing else in the repo reads `jvm_crash` files (grep of main and the branch found only these lines). The effect is that native crash files move to the JVM default location.
Replacement for `bin/solr` lines 1331-1332 (keep the comment as is):
```sh
    "-XX:+ExitOnOutOfMemoryError" "-XX:ErrorFile=${SOLR_LOGS_DIR}/jvm_crash_%p.log" \
```
Replacement for `solr.cmd`: add after line 1106:
```bat
set START_OPTS=%START_OPTS% -XX:ErrorFile="%SOLR_LOGS_DIR%\jvm_crash_%%p.log"
```
In the doc example, keep `"-XX:ErrorFile=/opt/solr/../logs/jvm_crash_%p.log",` under the Exit line. If the owner drops the flag on purpose, the changelog and the draft Limits must say that native crash files now go to the JVM default location. The receipt's "PR-ready" is not accepted until this is decided.

**FIX 7 (SOLR-16887): the BATS test name claims an OOM that the test never triggers.**
File: `solr/packaging/test/test_start_solr.bats` line 135 at 42675f65d6fc.
Evidence: the test is `@test "solr exits on OutOfMemoryError instead of crashing"`. Its body starts Solr, reads `/solr/admin/info/system`, and checks the flag text. No OOM is caused.
Replacement for line 135:
```bash
@test "solr starts with ExitOnOutOfMemoryError" {
```
After the change, the BATS focused run is owed at the new head. The receipt's BATS counts apply to the old name and the old flag set.

**FIX 8 (SOLR-15003): the live tip moved past the gated head, and the receipt understates the move.**
Files: `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java` (tip lines 759-768) and `solr/core/src/test/org/apache/solr/handler/TestReplicationHandler.java` (tip, new test `testFullCopyWithReloadRemovesOldIndexDir` at line 384).
Evidence: gated head 8f5b6f8360a is an ancestor of the tip 1004abee39a. Three commits follow it: afeab98ea82 (production change in `IndexFetcher.java`: the cleanup runs on the current core after a reload), 5c6e8753a39 (test: adds the reload test and hardens the others), and 1004abee39a (test: 7 lines). The receipt says "moved twice ... a replication RCA fix, then a test-only fix" and records 3 of 3. At the tip the class has 4 new tests, and the fourth has never been run. The production change has no gate.
Replacement for the receipt wording: "Moved three commits past gated head 8f5b6f8360a: afeab98ea82 (production, IndexFetcher.java), 5c6e8753a39 and 1004abee39a (test-only, adds testFullCopyWithReloadRemovesOldIndexDir). Gate owed at 1004abee39ab07d5eae1a5ea6327572997ff50d2." No draft.

**NOTE 1 (SOLR-6438, 8576, 16887, 17731): commit subjects name internal handoff docs.**
Evidence: `git log upstream/main..origin/<branch>` shows "add hypothetical-reproduction handoff doc" and "remove the ... handoff doc" on 6438 (6dc9151aaa1, 8c77988d591), 8576 (8777926b0ce, 4c46f95c785), 16887 (5563bbd08f0d, 2c196113225), and 17731 (f2aa2a3ada1, f2b4ba164f5). The net diffs are clean. A squash merge can carry commit subjects into the squash message. Replacement: squash to clean commits before the PR opens (owner decision 9), or accept.

**NOTE 2 (SOLR-8576): the changelog fragment is optional for a test-only change.**
Evidence: `dev-docs/changelog.adoc` (upstream/main, around line 130) exempts PRs that touch only tests. The `other` type is for "test infrastructure" and says most such changes need no entry. The draft keeps the Changelog line because the formula asks for one. Owner decision 7.

**NOTE 3 (SOLR-16725): the test builds the source collection by hand, and the reason is not written down.**
File: `LocalFSCloudIncrementalBackupTest.java` lines 117-126 at be1838ef8ccf.
Evidence: the one-line `CollectionAdminRequest.createCollection` call was replaced with a raw `GenericSolrRequest` that sends `replicationFactor` explicitly. No comment or commit message says why. The likely reason is that the SolrJ helper does not send `replicationFactor`, but this was not checked.
Replacement: the author adds a one-line comment saying why, or reverts to the helper if the output is the same. No exact text can be given until the reason is known.

**NOTE 4 (SOLR-16725): the javadoc describes the restored type from the ticket, not from code.**
File: `ClusterStatus.java` lines 337-340 at be1838ef8ccf.
Evidence: "Collections created through CREATE store them as strings and restored collections as numbers". The CREATE half is confirmed by the test's String check on the source. The restored half comes from the ticket and was not checked in code.
Replacement: "Reports the replica count properties as strings. Some collections, such as restored ones, store them as numbers, so without this a client can see both types."

**NOTE 5 (SOLR-17731): the changelog title says "reachable again".**
File: `changelog/unreleased/SOLR-17731-v2-overlapping-resource-paths.yml` lines 2-3.
Evidence: the ticket says these endpoints "exist in code but aren't accessible". "Again" implies they worked before.
Replacement for the title text: "The v2 APIs POST /collections/{name}/snapshots/{snapshotName} and GET /aliases/{aliasName} now answer instead of returning HTTP 405, because another resource with the same path was answering first."

**NOTE 6 (SOLR-17731): the test pins the alias wire key `name`.**
File: `V2ResourcePathOverlapTest.java` lines 54-56; the model `solr/api/src/java/org/apache/solr/client/api/model/GetAliasByNameResponse.java` lines 21-22 (`@JsonProperty("name") public String alias;`, unchanged on the branch).
Evidence: the test's comment says the key follows "the same convention as the other v2 by-name responses". That claim was not checked. The draft says only that the key is unchanged.
Replacement for the comment: drop the phrase "the same convention as the other v2 by-name responses", and keep "GetAliasByNameResponse serializes its alias field under the wire key name".

**NOTE 7 (SOLR-17731): the open owner call is about the fix's shape.**
Evidence: `C:/Users/shaninna/dev/Solr-issues/research/pipeline/HANDOFF.md` lines 182 and 193 (the fix is a class-level path change, and its mechanism is the lowest-confidence point). `C:/Users/shaninna/dev/Solr-issues/research/pipeline/SKIP-AUDIT-HANDOFF.md` line 132 says the general fix needs "ApiBag/PathTrie precedence redesign". The draft's Choice presents both routes. Owner decision 3.

**NOTE 8 (SOLR-16887): the heap dump question from the ticket is not answered in the branch.**
Evidence: Houston Putman (ticket comment 17742824) asked whether heap dumps are lost. Shawn Heisey's reply (17743255) says the heap dump options are not changed, and his manual check (17743260) wrote a dump with `SOLR_HEAP_DUMP=true`. No test in the branch covers heap dumps. The receipt does not record this check.
Replacement for the Limits, if the owner wants the point made: "Heap dump options are not changed. Checked by hand in the ticket; no test covers it." Owner decision 2.

**NOTE 9 (SOLR-16887): shared files with other branches (interactions).**
Evidence: a scan of all `origin/solr-*-submit` diffs against upstream/main found other branches touching `solr/bin/solr` (10390, 11678, 12347, 17029, 18132, 18339, 7924, 9342), `solr/bin/solr.cmd` (12347, 17598), `test_start_solr.bats` (10390, 12347, 17029, 18339, 7924, 9342), and `CoreContainerProvider.java` (15805). Base-line hunk ranges do not overlap: 16887 edits `bin/solr` base lines 1330-1332, and the nearest others are 1346-1353 (9342) and 1380-1410 (18339). `solr.cmd` base line 1121 is clear of 824 (12347) and 896-900 (17598). `CoreContainerProvider.java` base 218-246 is clear of 187-191 (15805). No trial merge was run.

**NOTE 10 (SOLR-15003): the class is nightly.**
Evidence: `@LuceneTestCase.Nightly` at `TestReplicationHandler.java` line 97, on the gated head and the tip. The receipt is correct. Any future draft must say the class runs only with nightly tests on.

**NOTE 11 (SOLR-15003): cross-file, stated once.**
SOLR-15003 is also filed under "Replication and backup" (branch-focus-inventory line 489). This round owns the ticket. Overlaps (base-line ranges, no trial merge): `SolrCore.java` at base 650-678 does not overlap 12007 (1840, 3496-3518), 17047 (134, 1601), 5011 (1891), or 8628 (855-859). `IndexFetcher.java` at base 748-757 does not overlap 11650, 12085, 12246, or 6711. `TestReplicationHandler.java` (15003 inserts after base 199) does not overlap 18280 (base 258-270), but both edit the same class.

**NOTE 12 (SOLR-8576): shared class with SOLR-16437.**
`CollectionsAPISolrJTest.java` is also edited by 11479 (base 496) and is the class of the SolrCloud corroboration run for 16437. The 8576 insert is at base 1156, so there is no line overlap with 11479. The class counts (25 tests, 1 skipped) appear only in the 8576 draft. The 16437 text must not restate them. Owner decision 10.

**NOTE 13 (interactions, no overlap found): 6438, 8275, 17731, 16725.**
6438 (`MergeIndexes.java`, `MergeIndexesTest.java`) and 8275 (`PrepRecoveryOp.java`, `TestPrepRecovery.java`) are not edited by any other submit branch in the scan. 17731 `CollectionsHandler.java` edits base 184 and 1205, and 16499 edits base 995-996: no overlap. 16725 `ClusterStatus.java` edits base 336 and 358, and 11288 edits base 19-26 and 171: no overlap.

**NOTE 14 (hygiene): local branch refs are behind origin.**
Evidence: `refs/heads/solr-<t>-submit` differs from `origin/solr-<t>-submit` for 6438 (6dc9151aaa1c vs 8c77988d5917), 8275 (881e7546eee7 vs e52e10fa50a3), 8576 (8777926b0cec vs 4c46f95c7851), 16725 (0a857703c022 vs be1838ef8ccf), 16887 (5563bbd08f0d vs 42675f65d6fc), and 17731 (f2aa2a3ada1d vs f2b4ba164f56). This audit used the origin refs, which match the claim table. No action in this round.

## Task results

**SOLR-6438 (head 8c77988d59175e0a59a32ef00dabffd3ca9a76fa): draftable.** The receipt matches the branch: three files (changelog, `MergeIndexes.java`, `MergeIndexesTest.java`). The check is correct as read, and the v1 action reaches the same class through `MergeIndexesOp.java`. The reference guide already treats the two sources as alternatives, so no docs change is needed. Counts are from the receipt (4 of 4, plus 1 of 1 and 44 of 44 neighbors); the logs are not on disk. Draft: `pr-drafts/core-admin/SOLR-6438.md`, with a Choice (reject or merge both). Open: NOTE 1.

**SOLR-8275 (head e52e10fa50a374196d2c6adea26e5e55e37cdf57): PR-ready, agrees with the receipt.** The message change is limited to the timeout path. The PREPRECOVERY request goes to the leader (`RecoveryStrategy.java` lines 696 and 959-989), so "the last state the leader observed" in the changelog is accurate. The test class sets the leader conflict wait to 5 seconds, so the new test is short. Draft: `pr-drafts/core-admin/SOLR-8275.md`, no Choice, Limits from the receipt. No FIX.

**SOLR-8576 (head 4c46f95c7851f2adbf3377d746aadc9c2249cdb4): draftable after FIX 1.** The new test is a pin: it passes on the base code, and the draft says so. The alias check is wrong (FIX 1). The edit needs its own run of `CollectionsAPISolrJTest` at the new head; the gated counts (25 tests, 1 skipped) cover the old head only. Draft: `pr-drafts/core-admin/SOLR-8576.md`, held with an INTERNAL note until the edit and its run are in. The class count appears only in this draft (NOTE 12). Changelog optional (NOTE 2).

**SOLR-15003 (live tip 1004abee39ab07d5eae1a5ea6327572997ff50d2; gated head 8f5b6f8360a): held.** Not drafted. The tip is three commits past the gated head, with one production change and one new test that has never run (FIX 8). A gate at the tip is main-side work still owed before any opening. The receipt's 3 of 3 is correct for the gated head. Cross-file note is NOTE 11; the nightly note is NOTE 10.

**SOLR-16725 (head be1838ef8ccf21a21938a9669d418abeec66e29d): draftable with one Choice.** The draft is `pr-drafts/core-admin/SOLR-16725.md`, at head be1838ef8ccf21a21938a9669d418abeec66e29d. The strings-or-numbers Choice is real: numbers would change every CREATE collection's output. The Limits must name `maxShardsPerNode` (FIX 5). The raw CREATE request (NOTE 3) and the javadoc (NOTE 4) need the author's edits. The receipt's proof (`testCustomProperties` fails on base) matches the test.

**SOLR-16887 (head 42675f65d6fc4362e427d3b4927801466b062ca6): held.** The receipt says PR-ready, and this audit disagrees because the branch removes `-XX:ErrorFile` (FIX 6) and the BATS test name claims an OOM it does not cause (FIX 7). No draft is written. After the fixes, a BATS run at the new head is owed; the current BATS counts (head leg ok 1, premise leg fails at the flag) apply to the old branch only. Interactions: NOTE 9. Heap dump note: NOTE 8.

**SOLR-17731 (head f2b4ba164f565274286708d15989f1b1807b84c6): draftable after FIX 2, FIX 3 and FIX 4.** The receipt's V2ResourcePathOverlapTest 2 of 2 matches the code. The fix works in the receipt's test (405 before, pass after), but its routing rule is unverified (FIX 4). The owner call is open (NOTE 7). Draft: `pr-drafts/core-admin/SOLR-17731.md`, with a Choice (shared path or router change), held with an INTERNAL note until the header, comments and run are in. Other overlapping v2 paths were not searched; the draft's Limits says so.

## Owner decisions

1. SOLR-16887: restore `-XX:ErrorFile` (recommended), or drop it on purpose and say so in the changelog and the draft (FIX 6).
2. SOLR-16887: hold the draft until FIX 6 and FIX 7 land and the BATS run is owed at the new head. The heap dump line is optional (NOTE 8).
3. SOLR-17731: confirm the open owner call. Shared class-level path (implemented, draft written with this Choice) or a router precedence change (NOTE 7).
4. SOLR-17731: approve FIX 3 and FIX 4 (comment and header edits) and owe the run of `ListAliasesAPITest` (FIX 2) before any opening.
5. SOLR-16725: confirm strings (implemented) over numbers. Keep `maxShardsPerNode` in Limits (recommended), or normalize it in this PR, which needs a re-gate.
6. SOLR-6438: keep the reject choice, or drop the choice section and keep the rejection in the draft.
7. SOLR-8576: approve FIX 1 (alias check) and its run. Keep the test-only changelog fragment (the draft's Changelog line needs it), or drop it and the line.
8. SOLR-15003: gate the tip `1004abee39a` (main-side work), or keep the held status. Decide whether the ungated reload test stays in the branch.
9. SOLR-6438, 8576, 16887, 17731: squash the handoff-named commits before opening, or accept them (NOTE 1). Squashing rewrites a pushed fork branch, so it needs your go.
10. SOLR-8576 and SOLR-16437: the class count appears only in the 8576 draft. The SolrCloud round must not restate it.
11. SOLR-8275: confirm PR-ready. No Choice is owed.

## Not checked

- Live heads: `git ls-remote` was not re-run. The origin refs in the local clone match the claim table for all seven. No gh call was made; none of the seven is listed as a live PR in the claim.
- Gate logs are not on disk for these tickets (receipts name logs such as `g6438-gate.log`). Counts are the receipts' counts, not re-run.
- No builds, tests, BATS runs, or Java compile. Behavior is read from code and receipts.
- Jersey and router routing (the 405 mechanism) was not verified beyond the receipts' test results (FIX 4).
- Trial merges were not run. Overlaps are base-line hunk ranges from `git diff -U0` against upstream/main.
- SOLR-15003 Jira text is not on disk in full (the candidates.csv row is cut off; no jira-context packet).
- Diff base: merge-base with upstream/main (8e62c2686882) per branch. The receipts do not record their gate base for these tickets.
- Whether restored collections store numbers in state.json (16725) was not checked in code.
- Whether SolrJ's Create helper omits `replicationFactor` (16725 NOTE 3) was not checked.
- A grep of core, solrj and test-framework for integer reads of CLUSTERSTATUS replica counts found none. That is not a full review of the test tree.
- Which tool would rewrite the malformed header (FIX 3) was not checked. The receipt says tidy clean.
- The drafts for other parts already in `pr-drafts/core-admin/` (for example SOLR-17708) were not read or changed.
