# Search components round 1, sub-batch 4, part s2: grouping family (SOLR-7520, 14381, 14931, 17155)

Result: all four drafted. 7520 is draftable as is. 14381 is draftable after one comment fix and owner calls. 14931 is draftable after two stray-character fixes, which move the head and need a new gate run. 17155 is draftable with an owner Choice, and its textual conflict with 14381 must be resolved at the second landing.

Scope: read only. No builds, no tests, no gh write calls, nothing posted, nothing committed. Heads used are the live origin refs that match the claim table: 7520 `10b6931e1c05`, 14381 `a28b3f672cb4`, 14931 `1a7d678d9a15`, 17155 `1413237f7a75`. Base for 7520 and 17155 is `14c7aac0d15`. Base for 14381 is `b5c71bc5573`. Base for 14931 is `9b3a84b1c46`, the merge-base with `upstream/main`. The receipts do not name the 14931 base.

## Findings

1. FIX (14381). The compat test says the previous coordinator casts the per-group `totalHits` to Integer. It does not. Evidence: base `b5c71bc5573`, `TopGroupsResultTransformer.java` line 144 reads `Number totalGroupHits = (Number) groupResult.get("totalHits");`. Line 101 reads the command `totalHits` as `Number`. The real Integer casts are `matches` (base line 103) and the top-level `totalHitCount` (`SearchGroupShardResponseProcessor.java` base line 152). Head file `solr/core/src/test/org/apache/solr/search/grouping/distributed/shardresultserializer/TopGroupsResultTransformerCompatTest.java`, lines 44-46, 85 and 111.
   Replacements:
   - Lines 45-46 (javadoc): "{@code TopGroupsResultTransformer} reads {@code matches} with an {@code (Integer)} cast, and the top-level {@code totalHitCount} is read the same way)" in place of the "per-group totalHits" wording.
   - Line 85: `// The previous version's coordinator casts matches to Integer; that must not throw.`
   - Line 111: `// The previous version reads the per-group totalHits as Number; this pins the type anyway.`
   - The `boxCount` javadoc just above head line 340 in `TopGroupsResultTransformer.java`: change "which reads these values as {@code Integer}" to "which reads matches and totalHitCount as {@code Integer}".

2. FIX (14931). Stray backtick in the test configset. File `solr/core/src/test-files/solr/configsets/cloud-macro-appends/conf/schema.xml`, line 27: `  <field name="_nest_path_" type="_nest_path_"/>\``. Replacement: `  <field name="_nest_path_" type="_nest_path_"/>`. It parses as text, so the tests are not affected, but it is visible in the PR diff.

3. FIX (14931). Stray colon in the test configset. File `solr/core/src/test-files/solr/configsets/cloud-macro-appends/conf/solrconfig.xml`, line 55: `:  </indexConfig>`. Replacement: `  </indexConfig>`. Fixes 2 and 3 change the head. The receipt at `1a7d678d9a15` no longer covers the branch, so a new gate run is needed. The 14931 draft names the current head and must be re-pointed.

4. FIX (17155 with 14381). Textual conflict at the second landing. `git merge-tree --write-tree origin/solr-14381-submit origin/solr-17155-submit` exits 1 with a conflict in `TopGroupsResultTransformer.java` (conflicted tree written by the trial, no ref). The hunks sit at the end of the file. 14381 inserts `boxCount` before `retrieveDocument`. 17155 replaces `retrieveDocument` with `retrieveUniqueKey` (head lines 343-366). Resolution: keep the 14381 `boxCount` block and the 17155 `retrieveUniqueKey` block, and drop `retrieveDocument`. Not compiled. Recommended landing order: 17155 first (it needs no owner call on API), then 14381 resolves. Trial merges of 14381 with 7520, 14931, and 7520 with 17155 are clean.

5. FIX before opening (7520 and 14931). Commit subjects carry process vocabulary. 7520 `3abc1f0bacd` "SOLR-7520: add hypothetical-reproduction handoff doc". 14931 `74c5b69004c` "SOLR-14931: add hypothetical-reproduction handoff doc" and `890db21959f` "SOLR-14931: remove the speculative testing handoff doc". The 7520 final diff has no doc file. Replacement: squash these into the feature commits, or reword them, before the PR opens. Rewriting changes the head, so the owner decides and the gate follows.

6. NOTE (14381). SolrJ source change. `solr/solrj/src/java/org/apache/solr/client/solrj/response/GroupCommand.java`, head line 112 `public long getMatches()` (base line 112 `public int getMatches()`), and head line 123 `public Long getNGroups()` (base `public Integer getNGroups()`). Callers that assign these to int or Integer will not compile. Drafted as a Choice. Owner decision.

7. NOTE (14381). Wire type change for every grouped response, not only overflow. Evidence: `TestGroupingSearch.java` head lines 249, 265 and 331 move from `int[@name='matches']` to `long[@name='matches']`. `TestMissingGroups.java` moves `int[@name='ngroups']` to `long`. The changelog title does not say it. Replacement for the title tail: "...SolrJ GroupCommand getMatches and getNGroups now return long and Long. Grouped responses now type matches and ngroups as long in XML." Draft states it under What this change does.

8. NOTE (14381). Test coverage at the head is thin. `TestDistributedGrouping.java` head lines 1340-1341, 1610 and 1650 are edited, with no run recorded at any head. `GroupingWideCountsTest`, `TestGroupingSearch` (17) and `TestMissingGroups` (1) are counted only at the earlier head `dd8d16441dc`. Later commits changed `TopGroupsResultTransformer.java` and `QueryComponent.java`, so those counts are not head counts. The draft says so. Owner decision: run `TestDistributedGrouping` before opening, or keep the Limits line.

9. NOTE (14381). The receipt says the compat test fails "on the earlier production" with ClassCastException. It does not give a SHA. The draft says "production code from before the boxing change". Commit `8f8f82d3d27` introduced `boxCount`. I did not confirm that the fail-before run used that code. Owner to name the SHA.

10. NOTE (14381). Mixed-version limit. An older coordinator still casts `matches` (base line 103) and `totalHitCount` (base SearchGroupShardResponseProcessor line 152) to Integer. A count above Integer.MAX_VALUE from a new shard still fails there. Stated in the draft Limits. The `boxCount` javadoc's "could not have produced or consumed anyway" is a slight overstatement and is covered by finding 1.

11. NOTE (14381). Jira packet missing. `research/jira-context/` has no SOLR-14381.json, and the ASF Jira CSV at `C:\Users\shaninna\dev\Solr-issues\ASF Jira 2026-08-18T20_45_55+0000.csv` has no match. The draft's "What happens today" rests on `research/branch-reviews/round-28/SOLR-14381-review.md` and the code. Hydrate the ticket before opening.

12. NOTE (14931). Route choice. The Jira report suggests not applying the handler's appends and invariants a second time on shards. This branch expands them on the shard instead (`RequestUtil.java` head lines 147, 166, 282-292). Drafted as a Choice. Owner decision.

13. NOTE (14931). `TestShardMacroExpansion` checks one value of the appended pseudo-field (head line 66, `doc.getFieldValue("appended")`). It would not catch a duplicated entry in `fl`. The appends path concatenates onto the existing array (`RequestUtil.java` base lines 149-160), so duplication depends on whether the coordinator already forwards the appended entry. Not checked. Owner decision whether to add an `fl` assertion.

14. NOTE (14931). The receipt says "base production" for the premise run and does not name the SHA. The draft says "base production code" without a SHA. Owner to name it.

15. NOTE (14931). Receipt GitHub runs (37688240904, 37675283014) do not name a repository. The drafts cite no run. Owner to confirm before citing.

16. NOTE (17155). The docValues branch converts with `value.toString()` (head line 365). The stored branch uses `uniqueField.getType().toExternal` (head line 352). These match for string keys, which is all the test covers. Not checked for other types. The draft's Limits says so.

17. NOTE (17155). Behavior change. A unique key with neither a stored value nor docValues now throws `SolrException` SERVER_ERROR (head lines 355-364) instead of a NullPointerException. The draft states it. It is not in the changelog title.

18. NOTE (17155). The receipt says "the non-stored unique-key scope question remains open" and gives no options. The draft's Choice is bracketed for owner confirmation. I did not invent a second option in the public text.

19. NOTE (17155). GitHub evidence is only at the pre-conversion head `82936c0b705`. The draft cites no run.

20. NOTE (local refs). Local `solr-7520-submit` is at `3abc1f0bacd` and local `solr-14931-submit` is at `74c5b69004c`. Both are behind origin. There are no local branches for 14381 or 17155. The audit used the origin refs. Local refs were not changed.

21. NOTE (7520). `CommandHandler.java` head lines 259-268: the `finally` runs `complete()` even when the search throws. If `complete()` then throws, the original exception is lost. The base `SolrIndexSearcher` (base lines 327-336) has the same pattern, so this is consistent. No change proposed.

22. NOTE (Lucene). The 14381 test javadoc (`GroupingWideCountsTest.java`) says Lucene's TopGroups still carries an int totalHitCount. The 9.x and 10.x sources could not be fetched (see Not checked). The repo pins Lucene 10.4.0 (`gradle/libs.versions.toml`, line 39 at `14c7aac0d15`). The only local evidence is the `(int) WIDE_MATCHES` cast into the TopGroups constructor in that test, which compiles at the receipt head. The drafts do not name the Lucene field.

23. NOTE (interactions). Trial merges are clean for 7520 with 14381, 7520 with 17155, and 14931 with each of the other three. All four branches merge cleanly with the local `upstream/main` (`8e62c2686882`). No upstream commit since each base touches these files (empty `git log` checks).

## Task results

- SOLR-7520. Draftable as is. Gate green at `10b6931e1c05` per receipt. Counts 1, 1, 17, 2 match the receipt. The premise run fails on base `14c7aac0d15` with "analytics section missing for grouped shard query". The coordinator gap is in Limits with a follow-up offer. Draft written against `10b6931e1c05`. Finding 5 must be settled before opening.

- SOLR-14381. Draftable after finding 1, and with owner decisions on finding 6, finding 8 and finding 9. The proof is stated per head. Counts from the earlier head are labeled as such, and the TestDistributedGrouping run is listed as not done. Draft written against `a28b3f672cb4`.

- SOLR-14931. Draftable after findings 2 and 3, which change the head and need a new gate. Route choice is drafted as a Choice. Proof counts (TestMacros 2, TestShardMacroExpansion 1) match the receipt at `1a7d678d9a15`. Draft written against `1a7d678d9a15`, to be re-pointed after the fixes.

- SOLR-17155. Draftable with an owner Choice on the non-stored unique-key scope. Counts (TopGroupsResultTransformerTest 1 of 1) match the receipt at `1413237f7a75`. The fail-before NPE is on base `14c7aac0d15`. No GitHub run is cited for this head. Conflict with 14381 is finding 4. Draft written against `1413237f7a75`.

## Owner decisions

1. 14381: change the SolrJ `GroupCommand` getter types (as drafted), or keep `getMatches()` and `getNGroups()` and add long-returning accessors beside them.
2. 14381: run `TestDistributedGrouping` before opening, or keep the Limits line as drafted.
3. 14381: name the production SHA that the compat fail-before ran against.
4. 14931: confirm the route, expanding the handler's appends and invariants on the shard (this branch) or skipping the second application (the reporter's suggestion).
5. 14931: name the base SHA used for the premise run. Approve fixes 2 and 3 (new head, new gate).
6. 17155: confirm the options and wording of the non-stored unique-key Choice. The record names the question open without options.
7. Landing order: recommend 17155 first, then 14381 resolves the `TopGroupsResultTransformer.java` conflict. Confirm.
8. Squash or reword the "handoff" commit subjects on 7520 and 14931 before opening (finding 5). This changes the heads.

## Not checked

- No builds, tests or gate logs. Compile and test numbers come from receipts only. Gate logs are not on disk.
- Lucene `TopGroups` int field type on the 9.x and 10.x lines. WebFetch to raw.githubusercontent.com and github.com was blocked from this sandbox. The repo pins 10.4.0. The drafts avoid the claim.
- SOLR-14381 ticket text (no packet on disk, finding 11). The SOLR-7520, SOLR-14931 and SOLR-17155 packets were read from `research/jira-context/`.
- Whether a coordinator-forwarded `fl` already holds the appended entry, so that a shard appends it twice (finding 13).
- Whether `value.toString()` matches the field type's external form for non-string unique keys (finding 16).
- Whether the SERVER_ERROR path can be reached through a real request, and what the client sees.
- Which repository the receipt GitHub runs belong to (finding 15).
- Live PR state for these four tickets. No gh calls were made. The claim lists only 18506 as a live PR in this batch.
- Other parts' drafts in `pr-drafts/search-components/` (for example SOLR-10424, SOLR-15319, SOLR-17051). Not read or checked by me.
- Drafts were checked for dashes (none found in the folder) and length (2,551 to 3,242 characters). They were not checked against the public-text rules beyond the process-word scan.
