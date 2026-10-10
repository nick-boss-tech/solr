# Search components round 1, sub-batch 4, part s4: managed resources (SOLR-15895, 16444, 17791)

Result: 15895 and 16444 are drafted with owner checks. 17791 is drafted but held: it needs a code fix that moves the head, a merge fix against main, a commit cleanup, and an owner API call.

Scope: read only. Heads used are the origin refs, which match the claim table: `solr-15895-submit` 02930909397ad, `solr-16444-submit` 8ffee94a5e75, `solr-17791-submit` a39c1c97376. Bases are the three-dot merge bases with `upstream/main` (8e62c2686882): 15895 at b5c71bc5573, 16444 at 22a8cfebbbd, 17791 at 56ec140e363. No builds, no tests, no gh calls, nothing posted, nothing committed. No ref was written. Two dangling commit objects were written by `git commit-tree` for a composition trial; they can be pruned.

## Findings

1. FIX (15895 head, commit 02930909397). The message ends with `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>` (`git -C <worktree> log -1 --format=%B 02930909397`). The owner rule is no Claude trailers on commits pushed to the repo, and AGENTS.md says not to add Co-Authored-By trailers. Replacement message, one line: `SOLR-15895: validate managed resource ids at REST creation against the file storage (platform-accurate); drop the registration-wide blacklist; tests and changelog`. Rewriting the fork branch is an owner call. The other 15895 commit and all 16444 and 17791 commits carry no trailer.

2. FIX (17791 code, `solr/modules/ltr/src/java/org/apache/solr/ltr/store/rest/ManagedFeatureStore.java` line 179, head a39c1c97376). An empty child id creates a feature store named "". Evidence: `RestManager.java` line 254 (`resolveResourceId`) does not strip a trailing slash. At lines 292 to 310, for `/schema/feature-store/` the parent resource is found and `childId = resourceId.substring(lastSlashAt + 1)` is the empty string, not null. `withDefaultStore` at line 179 checks only `childId == null`, so "" reaches line 194 (`putIfAbsent(store, "")`). `getFeatureStore` at lines 98 to 106 maps only null to `_DEFAULT_`, so "" creates a new store. The base code ignored the child id, so these features went to `_DEFAULT_`. This is a regression for that input. Replacement at line 179: `    if (childId == null || childId.isEmpty()) {`. Add one unit test that a "" child id keeps features in `_DEFAULT_`. This changes the head, so a focused proof and a rewritten draft Proof are needed. Not checked: whether Solr's dispatcher keeps a trailing slash in the request path.

3. FIX (17791 against main). `git merge-tree --write-tree upstream/main origin/solr-17791-submit` exits 1. The only conflict is `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-11.adoc`. Main added `=== V2 collections tree` at its lines 45 to 55 (SOLR-18450, commit 0cfeeb26a6c), and this branch appends at the same place. Replacement: keep main's lines 1 to 55 unchanged, add one blank line, then the branch's lines 45 to 51 (from `== Changes in Default Behavior` to the "Models that reference" line). Main's V2 section sits under "Removed Features"; that placement is main's and was not reviewed.

4. FIX (17791 commit history). Commit 345cf76949e, "SOLR-17791: add testing handoff" (adds `SOLR-17791-TESTING.md`), and the subject of a39c1c97376, "drop the handoff doc", put the word "handoff" into public commit subjects. The final tree has no handoff file: the three-dot diff lists 8 files and none is a note. Replacement: fold 345cf76949e into the next commit (866b795ce3f) so no handoff commit remains, and reword a39c1c97376 to "SOLR-17791: document POST in the guide and add an upgrade note". Rewriting is an owner call.

5. NOTE (15895 proof). The proof is inconclusive by construction. The new `TestManagedFileStorage` test calls `validateStoredResourceId` (the test method starts at line 52 at head), which main's `ManagedResourceStorage.java` does not have, so the test cannot compile on the base. Counts by `@Test` recount at head: TestManagedFileStorage 2 (main 1), TestRestManager 5 (main 4). The record does not say which branch of the test ran (`Constants.WINDOWS`). Owner check: confirm the Windows branch ran at 02930909397, or cut the draft sentence that says it covers the bug.

6. NOTE (15895 pin test). `testRegisterResourceIdWithFilenameSpecialChars` (`TestRestManager.java` lines 151 to 162) passes on the base too, because registration never calls the check. It guards the design and pins that registration skips the check, so a future registration check would break it. The draft says it does not prove the fix.

7. NOTE (15895 scope). The fix covers creation only. The write path is unchanged: `FileStorageIO.openOutputStream` (`ManagedResourceStorage.java` lines 228 to 230) builds the file name by string concatenation. `delete` (lines 232 to 236) uses `Path.of` at line 234, the call the ticket shows failing. Existing undeletable resources stay undeletable, and schema-declared handles are not checked. Replacement Limits wording is in the draft.

8. NOTE (15895 status 400). The 400 comes from the error type: `SolrException(BAD_REQUEST)` at `ManagedResourceStorage.java` lines 202 to 205 and 208 to 213. No test calls the endpoint. The draft says so.

9. NOTE (15895 platform). On Linux and macOS the check passes ids with a colon or a backslash, as the test's non-Windows branch states. The change is effectively Windows only. Not run.

10. NOTE (16444 reporter link). The record does not connect the reporter's intermittent case to the late-registration path. The Jira packet `research/jira-context/SOLR-16444.json` says the failure is intermittent during updates. Comment 17614196 says "most likely not a bug", and comment 17614197 asks whether the standard stopword filter fixes it. The draft Limits says the path is not confirmed. Owner decision on that wording.

11. NOTE (16444 choice). The record names no live alternative, so the draft has no "A choice to check" section. The owner may disagree.

12. NOTE (16444 changelog name). The file is `changelog/unreleased/SOLR-16444-late-managed-resource-observer.yml` (8 lines), not the template name `SOLR-16444.yml`. The draft links the real file.

13. NOTE (inventory count). `branch-focus-inventory-2026-10-08.md` line 248 says "(3 files total)" for 16444. The diff lists 4 files, including the changelog. Replacement: "(4 files total)".

14. NOTE (inventory tag). `branch-focus-inventory-2026-10-08.md` lines 255 and 502 tag 17791 with "Replication and backup". The diff has no replication file. Replacement: remove that tag.

15. NOTE (17791 proof scope). Per the receipt, the two new REST tests in `TestManagedFeatureStoreRest.java` (new file, 2 tests) fail on the base. The four new unit tests in `TestManagedFeatureStore.java` (lines 115 to 160) call the new overloads, so they cannot compile on the base. Their count of 10 is a pass count only. The draft says so.

16. NOTE (17791 API shape). The receipt leaves this open. Both options are in the draft's choice section. The draft should not be posted until the owner picks one.

17. NOTE (17791 changelog type). `changelog/unreleased/SOLR-17791.yml` line 2 says `type: fixed`. The change is listed under "Changes in Default Behavior". If the changelog types include `changed` (not checked), that may fit better.

18. NOTE (stale local refs). Local refs differ from the live origin heads: `solr-16444-submit` at 7d90810dae9 (an older chain with an "add hypothetical-reproduction handoff doc" commit), `wt-solr-15895-submit` at 95a861fae61, and `wt-solr-17791-submit` at 866b795ce3f. I used the origin refs. Do not push or open a PR from the local refs.

19. NOTE (interactions, RestManager.java). Each branch changes `RestManager.java` in a different class, with no shared line. 16444 changes lines 230 to 242 in `Registry.registerManagedResource` (synchronized, line 168). 15895 changes line 474 in `RestManagerManagedResource.doPut`. 17791 changes lines 341 to 346 in the endpoint dispatch. `ManagedResource.java` is touched only by 17791, and `ManagedResourceStorage.java` only by 15895. The 15895 pin test and the 16444 registration change both sit on registration, and neither adds a check there, so they do not conflict.

20. NOTE (interactions, composition). Trial results, no refs written. 15895 onto main: clean. 16444 onto main: clean. 15895 plus 16444 on the 22a8cfebbbd base: clean, tree e57b0bb087c7. Adding 17791: the only conflict is finding 3. Suggested landing order, which is a risk order and not a merge requirement: 16444 first (its failure record is complete and no owner call is open), 15895 second (after finding 1 and the Windows check), and 17791 last (after the API call, the commit cleanup, finding 2, and the conflict resolution).

21. NOTE (Lucene). Main pins Lucene 10.4.0 (`gradle/libs.versions.toml`, `apache-lucene`). The test code uses `org.apache.lucene.util.Constants` (15895 test), and `WhitespaceTokenizer`, `CharTermAttribute`, `TokenStream` (16444 test). No draft names Lucene behavior, so the 9.x and 10.x check is not needed. The APIs themselves were not checked against either line.

22. NOTE (base moved). Main has changed no file under the touched REST code paths since each proof base (path log checked for `solr/core/src/java/org/apache/solr/rest`, the rest tests, and the ltr store/rest paths). Overall 324 files changed on main since 22a8cfebbbd; they were not reviewed.

Verified, no finding: in base, `ManagedFeatureStore` has no `doPost` override, so POST and PUT behave the same, and the new 3-argument `doPost` keeps that. ManagedWordSetResource, ManagedSynonymGraphFilterFactory, ManagedModelStore and ManagedLanguageModelStore override neither `doPut` nor `doPost`, so their behavior is unchanged. Changelog authors are "Nick Shanin" in all three branches, and the commit author is nick.boss.us@gmail.com. No placeholder text.

## Task results

**SOLR-15895: draftable, with owner checks.** Draft at `pr-drafts/search-components/SOLR-15895.md`, written against head 02930909397. Counts match the receipt (2 and 5 by `@Test` recount). The Proof says the proof is inconclusive by construction and claims no pass. The GitHub run cited in the receipt (37586157900) is not in the draft. Before posting: finding 1 (trailer) and finding 5 (Windows branch). The draft has a choice section: platform check versus a fixed list. Head matches the claim table; no live PR is recorded, so no gh call was made.

**SOLR-16444: draftable.** Draft at `pr-drafts/search-components/SOLR-16444.md`, written against head 8ffee94a5e7. Counts match (TestRestManager 5, base 4; TestManagedStopFilterFactory 3, base 2). The failure on base 22a8cfebbbd is as the receipt records it; the premise log is not on disk. The draft cites no GitHub run conclusion (run 37706665083 is not stated). No choice section (finding 11). Limits say the reporter's path is not confirmed (finding 10). Ready once the owner accepts that wording.

**SOLR-17791: draftable, held.** Draft at `pr-drafts/search-components/SOLR-17791.md`, written against head a39c1c97376. Counts match (TestManagedFeatureStoreRest 2 new; TestManagedFeatureStore 10, of which 4 are new; TestModelManager 3, unchanged). Held for finding 2 (a code fix that moves the head and needs a focused proof), finding 3 (conflict with main), finding 4 (commit subjects), and finding 16 (API shape). The draft must be rewritten at the new head after the fix. The REST tests are the only failure-on-base evidence on record.

## Owner decisions

1. Approve rewriting the fork branches: drop the 15895 trailer (finding 1), and squash and reword the 17791 handoff commits (finding 4).
2. 17791 API shape: overloads on `ManagedResource` (implemented) or a child-id getter on the endpoint.
3. 17791 empty child id (finding 2): approve the code change and a new focused proof before the draft is reissued.
4. 15895: confirm which test branch ran at 02930909397, or cut the sentence about it.
5. 15895: keep the platform check (implemented) or switch to a fixed list.
6. 16444: accept the narrower Limits wording, or show that the reporter takes the late-registration path.
7. Landing order: 16444, then 15895, then 17791 (finding 20).
8. 17791 changelog type: `fixed` or `changed` (finding 17).
9. Inventory corrections (findings 13 and 14).

## Not checked

- No build, test, Gradle run, or gh call. Nothing committed, no ref written.
- Gate logs for these tickets are not on disk. No matching file under `research/test-queue/logs` or `results`. Counts come from the receipts and my `@Test` recount. The tests were not run.
- The GitHub run conclusions named in the receipts (15895 run 37586157900, 16444 run 37706665083) were not checked and are not in the drafts.
- Jira facts come from `research/jira-context` snapshots dated 2022, 2023 and 2025. Live Jira was not re-read.
- The Windows path behavior of `validateStoredResourceId` comes from reading Java path rules. It was not run on Windows.
- Whether Solr's dispatcher keeps a trailing slash in the request path (finding 2).
- The changelog YAML was read by eye, not parsed (no Python on this machine).
- The combined state was trial-merged only, not compiled.
- Lucene 9.x and 10.x for the test APIs (finding 21).
- Runtime behavior of the Jetty-based `TestManagedFeatureStoreRest` and of `TestRestManager`.
- No PR numbers exist for these three tickets, so no live PR check was made.
