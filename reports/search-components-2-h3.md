# Search components round 1, sub-batch 2, part h3 (QueryComponent family)

Result: all three tickets are draftable and each has holds before a PR (8939 small; 17748 owner decisions and a commit-subject fix; 17976 commit-subject fix and changelog wording); no hunk overlap, all trial merges clean, and the 17748 receipt's "no known trigger" claim is only partly right.

Scope: SOLR-8939 (head a855a2d8965c), SOLR-17748 (head dfacaf347668), SOLR-17976 (head 56ea43c448ed). Heads are the origin refs in this worktree, which match the claim table. Read only. No builds, tests, gh write calls, or posting. Nothing committed.

## Findings

1. FIX. Public commit subjects carry internal process vocabulary. Rule: the claim's "Public text carries no internal process vocabulary (... handoff ...)".
   - 17748: `07d3de7c170` "SOLR-17748: add testing handoff"; live head `dfacaf34766` "SOLR-17748: reword changelog for the single-pass scenario, drop testing handoff".
   - 17976: `7b90b627a9e` "SOLR-17976: add testing handoff".
   - Evidence: `git log 56ec140e3636..origin/solr-17748-submit` and `git log 56ec140e3636..origin/solr-17976-submit`.
   - Replacement: squash `07d3de7c170` and `7b90b627a9e` into their neighbors, and reword `dfacaf34766` to "SOLR-17748: reword changelog for the single-pass scenario". Any rewrite moves the head, so this needs owner approval and a fresh gate (see Owner decisions 3).

2. FIX (receipt and draft, 8939 Proof). `QueryComponentIdFormatTest` is not a failing-before proof. Its two tests call `QueryComponent.idToString`, which does not exist on the base (`git grep` for `idToString` at 97d973814336 finds nothing). The receipt's premise line (SOLR-8939.md line 7) reports 2 tests with 1 failure, which matches `StoredFieldsShardRequestFactoryTest` only.
   - Evidence: `QueryComponentIdFormatTest.java` lines 26 and 31 at the head; receipt line 7.
   - Replacement (in the draft, already written): "On the base code, `testDateUniqueKeyIsSentAsIsoWithMilliseconds` fails. `QueryComponentIdFormatTest` calls the new helper directly, so it does not compile on the base code and gives no failing-before result."

3. FIX (receipt and draft, 17976 Proof). `TestShardTieBreak` sets `ShardDoc.shardName` (line 31 at the head), so it does not compile on the base. The receipt records no base run for it. The only recorded failing-before result is `TestShardTieBreakCluster`.
   - Replacement (in the draft): "`TestShardTieBreak` 2 of 2 pass at this head. The class sets the new field, so it does not compile on the base code and gives no failing-before result."

4. FIX (17976 changelog, `changelog/unreleased/SOLR-17976.yml` line 1 and line 2). The title is 236 characters, and `type: fixed` is used for a behavior change. `type: changed` is in use in the repo (131 `changed` lines against 159 `fixed` in changelog YAML at main; both are valid).
   - Replacement: `type: changed`, and title "Distributed search breaks ties between equal scores by shard name, not replica address, so tied documents keep the same order across requests". Changing the file moves the head (Owner decision 5).

5. FIX (8939 local branch, not the live head). Local `solr-8939-submit` (35a8b97435f) is not the audited head. It has three local-only commits, a `SOLR-8939-TESTING.md` file, no `StoredFieldsShardRequestFactoryTest.java`, and a package-private `idToString`. Evidence: `git diff origin/solr-8939-submit solr-8939-submit` shows the 8939 test file removed, the testing file added, and `static String idToString` in place of `public static`.
   - Replacement: do not push the local branch. Any PR for 8939 must come from `origin/solr-8939-submit` at a855a2d8965c. Owner decides whether to discard or rename the local branch (Owner decision 6).

6. NOTE (17748 receipt, "guards wider than the ticket have no known trigger"). Only partly right.
   - The cause guard has a path. `LBSolrClient` throws `SolrServerException` with no cause when no earlier exception was recorded (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java` lines 940 to 941 at main and at the head; no change between 56ec and main). The base `returnFields` calls `getCause()` at `QueryComponent.java` line 1488 and dereferences the result at line 1499 with no null check (the base has no `nl` guard either, lines 1483 to 1484). The guard at head line 1496 matches the existing guard in `mergeIds` at line 1043.
   - The entry-creation branch (head lines 1487 to 1492) has no path I could find. `mergeIds` records every failed response under its name (lines 1041 to 1098), and a shard that returned documents in the id phase also has an entry.
   - Replacement: the draft's "A choice to check" section (already written) states this as the owner's decision.

7. NOTE (17748 trigger and route). The premise run covers one route: a one-pass query with an empty shard name. Facts from the code:
   - The empty name comes from `HttpShardHandler.submit` with a slice that has no replicas (`HttpShardHandler.java` line 251 to 255 at the head, the "no servers hosting shard" failure). The name is `""` because `rb.shards[i]` is the empty join (`HttpShardHandler.java` line 557 at main, `String.join` of an empty list).
   - That route needs `shards.tolerant=true`. Without it, `HttpShardHandler.java` line 521 to 539 throws a 503 before the field step.
   - One-pass covers `distrib.singlePass=true` and also requests whose `fl` needs only the id and score (`QueryComponent.java` lines 803 to 815). The receipt and the changelog say "single-pass" only.
   - A second route also reaches the base NPE: a named shard with an existing entry and an error with no cause (base lines 1483 to 1488). It applies to one-pass and two-phase queries. The branch has no test for it. `testExistingErrorEntryIsKept` starts with an error already recorded. `testFailureWithoutInfoEntryOrCauseIsRecorded` starts with no entry.
   - The Jira packet (`research/jira-context/SOLR-17748.json`, SOLR-17748, version 9.6) says "when some of the shards are down and shards.info is set" and shows the stack through `handleRegularResponses`. It does not mention one-pass, `shards.tolerant`, or the cause-less route.
   - Replacement: the draft's "What happens today" names both routes and the two conditions, and the Limits say the ticket does not name the query shape. Owner decision 2 covers whether to add a test.

8. NOTE (17748 test coverage). `TestShardsInfoResponse` asserts `responseHeader.status` 0, `numFound` above 0, and the three shards.info keys. It does not assert `partialResults`. The flag comes from `mergeIds` (main `QueryComponent.java` lines 1239 to 1243). The draft says the flag is kept and gives the code location, not a test result.

9. NOTE (17976 receipt, earlier failed run). The receipt (`receipts/SOLR-17976.md` line 8) says "an earlier combined run failed at the focused test step and is grouped in the record with the dispatch-error cases." It gives no cause for that failure at this head. The draft cites only the head counts. Owner check before the combined counts are cited (Owner decision 7).

10. NOTE (17976 Jira and the choice). The Jira packet (`research/jira-context/SOLR-17976.json`, comment 18032208) quotes Hoss Man: the merge "should use the 'shard name' as the tie-breaker." The implemented route is the one the ticket asks for. No live alternative is named in the receipt or the Jira thread, so the draft has no "A choice to check" section.

11. NOTE (17976 public API). `ShardDoc.shardName` is a new public field (`ShardDoc.java` lines 30 to 33). The two merge paths set it (`QueryComponent.java` line 1232, `CombinedQueryComponent.java` line 424). Test code builds `ShardDoc` without it (`ReciprocalRankFusionTest.java` lines 67, 73, 80; `RankQueryTestPlugin.java` lines 281, 596 at main), so those keep the address order. A search of `solr/` at main finds no other `ShardDoc` construction, and `ShardFieldSortedHitQueue` is used only by the two component classes.
    - Replacement: the draft's "What this change does" and Limits (already written).

12. NOTE (17976 combined path). `CombinedQueryComponent.java` uses the same comparator (lines 323 and 424). No test checks tie order on that path. The draft's Limits say so.

13. NOTE (8939 helper and Javadoc). `QueryComponent.idToString` is `public static` (line 1457) because `StoredFieldsShardRequestFactory` (another package, line 79) calls it. The Javadoc at lines 1453 to 1456 says dates "must be written in ISO-8601 (with milliseconds)". `Instant.toString()` omits the fraction when the milliseconds are zero, so that wording is too strong. Replacement, optional and it moves the head: "must be written in ISO-8601 instant form, with milliseconds when present." The draft's Limits say that zero-millisecond dates are sent without a fraction.

14. NOTE (8939 receipt and tests). The premise log named in the receipt (`g8939tc-premise.log`) is not in `research/test-queue`, and I did not search further. `StoredFieldsShardRequestFactoryTest` calls `assumeWorkingMockito()` (line 62 at the head), so a skipped run would show as a skip, not a failure. Before the Proof is final, check that the 2 of 2 counts are runs, not skips.

15. NOTE (8939 changelog file name). The real file is `changelog/unreleased/SOLR-8939-date-unique-key-millis.yml`, not the template's `SOLR-<ticket>.yml`. The draft names the real file. No change needed.

16. NOTE (worktrees behind the live heads). `wt/SOLR-17748` (branch `wt-solr-17748-submit`) is at f9a303c4c84, two commits behind the live head dfacaf34766. `wt/SOLR-17976` (branch `wt-solr-17976-submit`) is at dd567e66e76, three commits behind the live head 56ea43c448ed. Check them before any push.

17. NOTE (interactions: overlap and landing order). No hunk overlap.
    - 8939 at head: `createRetrieveDocs` call site line 1445 and the helper at lines 1453 to 1462.
    - 17748 at head: `returnFields` block at lines 1478 to 1507.
    - 17976 at head: helper at lines 971 to 989, `mergeIds` use at line 1056, and the set at line 1232.
    - Trial merges with `git merge-tree --write-tree` (no ref written): 8939 with 17748 gives e96bac227dd6; 8939 with 17976 gives 4d29438ab364; 17748 with 17976 gives 2e7f37c1d63c. All clean.
    - Against current upstream/main 8e62c268688: 8939 gives 73f0545f1289; 17748 gives de51a47b8505; 17976 gives 112f20bfb5fb. All clean.
    - No order is forced. Suggested order: 8939, then 17748, then 17976, so the ordering change lands last and gets its own review.

18. NOTE (17976 and the SOLR-3044 NamedList work, for the h6 lead). 17976 makes no NamedList assumption. Its diff has no `NamedList`, `SimpleOrderedMap`, or `asShallowMap` line. Its fork point 56ec140e3636 already contains SOLR-18373 (`55e3b8838b4` is an ancestor). The claim's "production hunks are already on main via SOLR-18373" holds only for removing `asShallowMap`. The 3044 code (`abd4e761f7f`, `indexOf`, `setVal`, `add`) is not on main. Main uses `getResponseHeader().put(...)` at `QueryComponent.java` lines 1239 to 1243 and `CombinedQueryComponent.java` lines 457 to 461. The lead should reword the 3044 line.

19. NOTE (premise base for 17748). The receipt's premise base is 14c7aac0d15, which is on main but not an ancestor of the branch. The branch forks at 56ec140e3636. The two commits differ by 11 commits, but `git diff --stat 56ec140e3636 14c7aac0d15` on the component directory is empty, so the premise code matches the branch's base. Recorded only.

## Task results

**SOLR-8939: draftable.** Head a855a2d8965c8eae5df171ad9b5148b7e95f7425 (origin). Draft: `pr-drafts/search-components/SOLR-8939.md` (about 2,700 bytes with URLs). The fix is small and isolated: the plain distributed path and the grouping stored-fields path both call the new helper. Receipt counts match the test method counts at the head: 2 methods in each of the two classes. The proof is unit level only, and the draft says so. Holds before a PR: Finding 5 (do not push the local branch). Finding 14 (check the counts are runs, not skips) is a check, not a hold.

**SOLR-17748: draftable, with two owner decisions and a commit-subject fix.** Head dfacaf347668e43a433baef650268b281649c6fc (origin). Draft: `pr-drafts/search-components/SOLR-17748.md` (about 4,100 bytes with URLs; about 3,000 without). The empty-name skip is the main fix and matches the one-pass route traced in Finding 7. The entry-creation branch has no reachable path (Finding 6). Holds before a PR: Finding 1 (handoff in the head subject and in `07d3de7c170`), Owner decision 1 (keep or drop the entry-creation branch), Owner decision 2 (which NPE route the reporter hit, and whether to add a test for the cause-less route with an existing entry).

**SOLR-17976: draftable, with a commit-subject fix and changelog wording.** Head 56ea43c448ed4fea75131d34c390d11233bab5cb (origin). Draft: `pr-drafts/search-components/SOLR-17976.md` (about 4,000 bytes with URLs). The tie-order change for every SolrCloud user is stated plainly, with the public `ShardDoc` field. No Choice section: the Jira thread asks for the shard name (Finding 10). Holds before a PR: Finding 1 (`7b90b627a9e` subject), Finding 4 (changelog type and title; this moves the head), Finding 9 (the earlier failed combined run).

## Owner decisions

1. 17748 entry-creation branch (`QueryComponent.java` lines 1487 to 1492): keep it and state it in the PR (current head, the draft's choice section), or drop it (smaller diff; no reachable trigger found). The branch's tests call `returnFields` directly, so dropping it would remove the entry-creation test case too.
2. 17748 trigger: confirm which route SOLR-17748 reporter hit (the empty-name one-pass route or the cause-less route). If the cause-less route, a test with an existing entry is missing. Adding one is new code and a new head, so it needs a fresh gate.
3. Handoff commit subjects on 17748 (`07d3de7c170`, `dfacaf34766`) and 17976 (`7b90b627a9e`): squash or reword. Either moves the head and needs a fresh gate.
4. 17976 choice section: none drafted (the Jira asks for the shard name). Confirm.
5. 17976 changelog: type `changed` and the shorter title (Finding 4). This moves the head.
6. 8939 local branch: discard or rename. Do not push it.
7. 17976 earlier failed combined run: confirm it was a dispatch or harness failure before the combined counts go in a PR.

## Not checked

- No builds, tests, Spotless, tidy, or Error Prone. Merged trees were not compiled. Merge results are textual trial merges only.
- Gate and premise logs named in the receipts (`g8939tc-*`, `g17748-*`, `g17976-*`) were not found under `research/test-queue`. The proof counts are the receipts' counts, not re-run results.
- GitHub run numbers in the receipts were not re-checked. None of the three tickets is a live PR, so no `gh` call was made.
- Heads are the origin refs in this worktree. No `ls-remote` was run.
- Jira text was read from the local packets `research/jira-context/SOLR-8939.json`, `SOLR-17748.json`, and `SOLR-17976.json`. Live Jira was not fetched.
- The route the 17748 reporter hit is traced in code only (Finding 7).
- The HTTP 200 for the 17748 case is inferred from the test passing with no exception. The test does not assert the status code.
- Lucene 9.x and 10.x: no draft names Lucene behavior, so no version check was owed.
- Only `solr/` was searched for `ShardDoc` and `ShardFieldSortedHitQueue` users, at main.
