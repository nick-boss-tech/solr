# Search components round 1, sub-batch 1, part f2 (SOLR-10492, SOLR-11129)

Result: SOLR-10492 is draftable at head ecf21e2192cb, after its commit history is cleaned and the owner decides two wording items. SOLR-11129 is held at head 6c1356bdff70: its facet.offset change applies the offset twice on the coordinator path (FIX 1, by code reading, not run).

## Findings

1. FIX (SOLR-11129, head 6c1356bdff70). `solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java` L1397 and L1403.
   Evidence: the field facet constructor now reads local params (L1397), so `this.offset` at L1403 comes from the local `facet.offset`. The shard also applies a local offset. Base `SimpleFacets.java` at e2cdb2d7e8a builds `SolrParams.wrapDefaults(localParams, global)` (L191) and reads `int offset = params.getFieldInt(field, FACET_OFFSET, 0)` (L442). The coordinator removes the global offset (FacetComponent.java L554) and the field-level offset (L580) from shard requests, so the local copy reaches the shard and is applied there. The coordinator then skips `dff.offset` again at L1127-L1128 (count sort) and L1134 (index sort). By code reading, base returns the control's page for one shard (the coordinator used offset 0). This head would skip the offset twice. No base test sends a local facet.offset on a field facet (grep of base tests: no hits). The new test does not cover offset.
   Replacement: at L1403, replace `this.offset = params.getFieldInt(field, FacetParams.FACET_OFFSET, 0);` with `this.offset = rb.req.getParams().getFieldInt(field, FacetParams.FACET_OFFSET, 0);` and add the comment "a local facet.offset is applied by the shards, so the coordinator keeps the request value here". This restores base behavior for offset. A focused test with a local facet.offset would confirm it before posting. Not run.

2. NOTE (SOLR-11129, scope). `FacetComponent.java` L907-L911 (head 6c1356bdff70). Base used `rb.req.getParams().getFieldInt(field, FACET_MINCOUNT, 0)` at the same loop (diff hunk `@@ -904,8 +904,7 @@` from 14c7aac0d15). Now the loop uses `ent.getValue().minCount`. With facet.zeros=false and no facet.mincount, `fillParams` sets minCount to 1 (L1412). So a global facet.zeros=false request now drops zero-count terms at the coordinator. Base did not. The changelog names facet.mincount only.
   Replacement (optional, owner call): title "Distributed field faceting now honors per-field facet params given as local params, such as facet.mincount, and drops zero-count terms when facet.zeros=false." Otherwise keep the scope sentence in the draft.

3. FIX (SOLR-10492, SOLR-11129, SOLR-6193; before any PR opens). Public commit subjects carry internal process vocabulary. Add and remove pairs: 10492 has 8e58bae3050 ("add hypothetical-reproduction handoff doc") and ecf21e2192c ("remove the handoff doc"). 11129 has 6ef2fd67dab and 6c1356bdff7. 6193 has 76467e19fd0 and ec94bf50c80. The net diffs are clean: `git diff --stat` shows three files for 10492 and 11129, and five for 6193.
   Replacement: rebuild each branch on its merge base as the same net change with no handoff-doc commits. Keep the existing changelog and test commits. This rewrites pushed fork branches, so it needs the owner's go-ahead.

4. NOTE (SOLR-10492, round 28 review is wrong on its only blocker). `C:\Users\shaninna\dev\Solr-issues\research\branch-reviews\round-28\SOLR-10492-review.md`, Finding 1, says the new test "does not assert the response". The test calls `query(...)`, and `BaseDistributedSearchTestCase.query` calls `compareResponses(rsp, controlRsp)` (`solr/test-framework/src/java/org/apache/solr/BaseDistributedSearchTestCase.java` L660 at e2cdb2d7e8a). The comparison is the assertion. The receipt's base failure is consistent with that.
   Replacement: strike Finding 1 and say the "Needs work" verdict does not rest on it.

5. NOTE (SOLR-10492, single-node edge is asymmetric). `solr/core/src/java/org/apache/solr/request/SimpleFacets.java` L328-L335 at ecf21e2192c (unchanged) read group.field only from the global params for facet.query and throw BAD_REQUEST. facet.field now accepts group.field from the request (L792-L797). So group.facet=true with group.field and no group=true works for facet.field and fails for facet.query.
   Replacement: the draft already states this in Limits. If the owner wants symmetry, the same fallback at L330 needs its own test; that is an owner call.

6. NOTE (SOLR-10492, SimpleFacetsTest not in the recorded focused set). `SimpleFacets.java` changed, but the receipt's focused set lists TestDistributedGrouping, TestGroupingSearch and DistributedFacetPivotSmallTest. By code reading, SimpleFacetsTest does not reach the new fallback: every base `group.field` use there also sends `group=true` (for example L419-L421, L2806-L2808, L2958-L2960, L3914-L3916), and `testGroupFacetErrors` (L4481-L4533) sends no group.field.
   Replacement: none required. The owner may add SimpleFacetsTest to the next queue run.

7. NOTE (SOLR-11129 and SOLR-6193, overlap). No file or hunk overlap. 11129 changes FacetComponent.java only. 6193 changes PivotFacet.java, PivotFacetField.java and PivotFacetValue.java (plus its test). Both use `SolrParams.wrapDefaults(localParams, ...)`. At ec94bf50c80, PivotFacetField.java L73 reads a local offset into `facetFieldOffset`, and L81 and L242 apply it in the merge. The pivot shard request keeps the local offset (FacetComponent.java L645 removes only the field-level offset at head). So the same double-offset question as Finding 1 may apply to pivot.
   Replacement: none here. Part f3 should check it.

8. NOTE (SOLR-10492 and SOLR-11129, layers). No file overlap. 10492 changes shard-side refinement in SimpleFacets.java (L792-L797, L979-L996, L1005-L1028 at ecf21e2192c). 11129 changes the coordinator in FacetComponent.java. A request with group.facet, group.field and a local facet.mincount runs both changes, and no test covers that combination. Part f1 owns the overlap with SOLR-5394 and SOLR-10844 in SimpleFacets.java.

9. NOTE (heads). The local refs `solr-10492-submit` (8e58bae3050) and `solr-11129-submit` (6ef2fd67dab) are behind the origin tips (ecf21e2192c, 6c1356bdff7). `git merge-base --is-ancestor` confirms they are ancestors. Use the origin refs. The origin refs match the claim table. A push from the local refs would rewind the branch.
   Replacement: none; reset the local refs only with the owner's go-ahead.

## Task results

**SOLR-10492 (head ecf21e2192cb91e1ba4a44abce2c43ab8a5409b9). Verdict: draftable.** The branch diff has three files: the changelog fragment, SimpleFacets.java (46 changed lines), and TestDistributedGrouping.java (58 added lines). Its shape matches the receipt. The receipt's group.field fallback edge is the third bullet of "What this change does", and the Limits lines state the facet.query asymmetry (Finding 5). Proof numbers come from the receipt only (TestDistributedGrouping 2, TestGroupingSearch 17, DistributedFacetPivotSmallTest 1, all passing; base failure shown with the strengthened test). Draft: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester\pr-drafts\search-components\SOLR-10492.md`, about 3,400 characters, drafted against ecf21e2192cb. Before posting: Finding 3 (commit history) and the owner calls on Findings 5 and 6. Finding 4 means the round-28 "Needs work" should not block this ticket. The draft names no Lucene behavior, so no 9.x or 10.x check applies.

**SOLR-11129 (head 6c1356bdff706328a8b78d39782f9e94845827d4). Verdict: held; draft written with a HOLD comment.** The branch diff has three files: the changelog fragment, FacetComponent.java (two hunks), and the new test DistributedFacetLocalParamsMinCountTest.java (76 lines). The mincount fix holds by code reading: the coordinator drops by the field's own minimum (L907-L911), and its initial mincount uses the local value. The receipt's scope note is confirmed, and Finding 2 adds the global facet.zeros case. The offset change is not safe to publish (Finding 1). The draft is `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester\pr-drafts\search-components\SOLR-11129.md`, about 3,400 characters, drafted against 6c1356bdff70. Its HOLD comment lists the edits that follow each owner decision. The draft names no Lucene behavior.

## Owner decisions

1. SOLR-11129 offset: take the one-line replacement in Finding 1 for this PR and state offset as a Limit (recommended), or do a full fix with a focused test.
2. Commit history for 10492, 11129 and 6193: rebuild the branches without handoff-doc commits before any PR opens (needs your go-ahead, it rewrites pushed fork branches).
3. SOLR-10492 single-node edge: keep it as drafted, or also change facet.query to match (Finding 5).
4. SOLR-10492: add SimpleFacetsTest to the next queue run (Finding 6), or not.
5. Round-28 review of SOLR-10492: correct Finding 1 (Finding 4).
6. SOLR-11129 changelog: keep the mincount-only title, or use the wider title in Finding 2.

## Not checked

- No builds, tests, Gradle, gh calls, Jira writes, or posts. Nothing committed. The only files written are this report and the two drafts.
- Jira: read from the local packets `C:\Users\shaninna\dev\Solr-issues\research\jira-context\SOLR-10492.json` and `SOLR-11129.json`, not live. Their last updates are 2021 and 2019.
- Gate logs (g10492-gate.log, g10492-premise.log, g10492r35-ab.log, g11129-gate.log, g11129-premise.log) are not on disk in research/, receipts/, material/, audits/, or the worktree (searched to depth 3). Proof numbers are from the receipts only.
- The CI run numbers in the receipts (37317300581, 37315684347) were not checked.
- Live PR state for 10492 and 11129: not checked. Neither is in the consistency pass.
- Head refs: origin refs were compared to the claim table, not re-fetched with ls-remote.
- The one-line offset replacement (Finding 1) and the facet.zeros behavior (Finding 2) were read, not run. Local params facet.missing, facet.prefix and facet.sort were traced only as far as the coordinator fields.
- The cost of grouped refinement counts (`getGroupedListedCounts` with limit -1) was not measured.
- `BaseDistributedSearchTestCase.compareResponses` was checked only at its call in `query` (L660), not read in full.
- The pivot offset question (Finding 7) belongs to part f3 and was not audited.
- Overlap of SimpleFacets.java hunks with SOLR-5394 and SOLR-10844 belongs to part f1.
