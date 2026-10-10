# Search components round 1, sub-batch 4, part s5: SOLR-15144, 17372, 17841, 6207

Result: 6207 is draftable and its draft is written (documentation test, no fail-before by design). 15144 is held as an owner decision and not drafted; its changelog title and code comment go past the record. 17372 failed its gate and is audit only. 17841 is a retire candidate, and its fix already landed upstream through PR #4724 (commit 3beb0dc5f28).

Checked against: claim `claims/search-components-4.md`, assignment `assignments/search-components-4.md`, `pr-formula.md`. Upstream main at check: 8e62c2686882. Heads audited are the remote-tracking refs (`origin/<branch>`), which match the claim table and the receipts.

| Ticket | Live head (origin) | Receipt head | Merge-base with upstream main | Behind upstream | Trial merge |
|---|---|---|---|---|---|
| 15144 | 68b0fc31e05663a441d5efdc465b8e4f222831f8 | same | b5c71bc5573 | 67 | clean (rc=0) |
| 17372 | 048862fda8ab76f36f259c6849ec5c5a8cfafabe | same | e2cdb2d7e8ae | 56 | clean (rc=0) |
| 17841 | 478731e676021c20a1dff6d412ff706b7e99925d | same | 14c7aac0d151 | 66 | clean (rc=0) |
| 6207 | b099a9f1be5a3fb529767ca625f42e7860e62f13 | same | cabedd1d968 | 37 | clean (rc=0) |

Trial merges used `git merge-tree --write-tree` and wrote no ref. No head moved since the claim.

## Findings

1. FIX (15144, changelog title). File: `changelog/unreleased/SOLR-15144.yml` line 1, head 68b0fc31e05. Current title: "A distributed request whose timeAllowed ran out in the first phase now still fetches the documents of the ids it merged, instead of returning a count with an empty document list." Evidence: receipt 15144 line 8 says the distributed test failed the same way at head and base, and that the production change alone does not give the end-to-end behavior. The branch diff against b5c71bc5573 has three files (TimeAllowedLimit.java, TimeAllowedLimitTest.java, the changelog), and none is on the coordinator path. Replacement (option a only): "Document fetch requests are no longer skipped when timeAllowed is used up in an earlier phase."

2. FIX (15144, code comment). File: `solr/core/src/java/org/apache/solr/search/TimeAllowedLimit.java` lines 99-100, head 68b0fc31e05. Current: "the ids were already merged; skipping the fetch of their fields, or limiting it, would / only return the count without documents." Evidence: "would only return the count without documents" is the end-to-end claim that receipt line 8 says does not hold. "Limiting it" does not describe the code either, which removes the timeAllowed value (line 101). Replacement: "// the ids were already merged; skipping the fetch of their fields leaves them without documents, / // so the fetch is not time limited here".

3. FIX (15144, PR text, option a only). File: `TimeAllowedLimit.java` line 101 (`params.remove(CommonParams.TIME_ALLOWED)`). Evidence: the fetch request loses its timeAllowed, so the shard runs that fetch with no time limit. The formula (section 2) needs this stated openly, because it is wider than the ticket's narrow reading. Replacement sentence for "What this change does": "When the time is used up before the document fetch, the fetch request is sent without a timeAllowed value, so the shard runs that fetch with no time limit." Add to Limits: "The coordinator path is not changed. A fetch that starts with time left still runs under the shard's own timeAllowed, as before. I can open a follow-up for the coordinator path if you want it."

4. NOTE (15144, cross-batch). `SearchHandler.java` line 587 at upstream main (`queryLimits.adjustShardRequestLimits(sreq, shard, params, rb)`) is the only caller of the shard-limit hook in `solr/core` (grep). Option (b) would change that handler flow, which is sub-batch 2's territory. Not audited here.

5. NOTE (15144 and 17372, commit subjects). These commits carry process words into the commit list of any PR: 820f47ae5df "SOLR-15144: testing handoff for external reviewer"; 68b0fc31e05 "SOLR-15144: drop the TESTING.md handoff note"; 2869441fb32 "SOLR-17372: add hypothetical-reproduction handoff doc"; 048862fda8a "SOLR-17372: remove the hypothetical-reproduction handoff doc from the outbound branch". No branch was edited. Owner decision: rebuild or squash before any PR.

6. FIX (17372, only under option i). File: `solr/core/src/test/org/apache/solr/handler/component/StatsComponentTest.java` lines 2298-2301, head 048862fda8a. Current comment attributes the miss to "random digest compression / merge order". Receipt 17372 line 7 says the 2024 failure needed a feed order that the current local path does not produce at any seed tried. Replacement for lines 2298-2301: "// percentiles are approximated with a t-digest, so mid-range values can differ from the exact / // value by a few percent. Allow 10% of the expected value, and at least 1.0." Code on lines 2302-2303 stays.

7. NOTE (17372, seed). The Jira packet `research/jira-context/SOLR-17372.json` (under `C:\Users\shaninna\dev\Solr-issues`) gives the reproducing seed 2074D8EC40F42163. The receipt's runs use seed 17372C0FFEE17372 and a "ticket seed" that it does not define. The record does not show a base run at 2074D8EC40F42163. Not checked. Owner should ask for that run before choosing option (i) or (iii).

8. NOTE (17841, the fix is already on main). Commit 3beb0dc5f2814fb607aa458fb4274453dbf4ec12, "SOLR-17841: Use faster DocSetCollector when multiThreaded=true (#4724)", is an ancestor of upstream main (`merge-base --is-ancestor`). The Jira packet `research/jira-context/SOLR-17841.json` also lists the cherry-picks on branch_10x (0c3f7b20142) and branch_9x (ea7ded42893), and its status reads Resolved. The branch base 14c7aac0d151 is older than that commit, so the receipt's "passes against the base production tree" is a pre-fix tree.

9. NOTE (17841, test scope). File: `solr/core/src/test/org/apache/solr/search/SolrIndexSearcherTest.java`, head 478731e6760. Lines 138-185, `testReRankCollectorFallsBackWhenRescoringAborts`, test ReRankCollector fallback. That is not multithreaded search and belongs in the rerank topic of sub-batch 3 if kept. Lines 187-216, `testMultiThreadedSearchMatchesSingleThreadedResults`, overlaps `TestMultiThreadedSearcher.testMultiThreadedDocSetMatchesSingleThreaded` (base file line 127 at 14c7aac0d15, so it predates #4724). The receipt's count of 17 is 15 base methods plus these 2.

10. NOTE (17841, benchmark). File: `solr/benchmark/src/java/org/apache/solr/bench/search/NumericSearch.java` line 68 (`@Param multiThreaded`) and line 272 (`intRange`), plus one line in `solr/benchmark/src/resources/solr.xml`. Upstream main's `solr/benchmark` has no multiThreaded toggle (git grep). This is the only part of the branch with no upstream counterpart. Owner decision: keep or drop.

11. NOTE (6207, inventory row). File: `branch-focus-inventory-2026-10-08.md` line 196 reads "awaiting pipeline" and "(4 files total)". The receipt records a green gate at b099a9f1be5 on 2026-10-07, and the branch diff against cabedd1d968 has 3 files. The row is stale. Suggested state: gated, with 3 files.

12. NOTE (6207, changelog). File: `changelog/unreleased/SOLR-6207-getparamstring-javadoc.yml`. The file name is descriptive, while the formula template says `SOLR-<ticket>.yml`. `dev-docs/changelog.adoc` accepts descriptive names, so the draft links the actual file. Type `other` fits, but the same doc says most minor changes need no entry. Owner may drop it.

13. NOTE (6207, javadoc scope). File: `solr/core/src/java/org/apache/solr/request/SolrQueryRequest.java` lines 128-134, head b099a9f1be5. The javadoc says defaults, appends and invariants are excluded. That holds on the request-handler path, where `RequestUtil.processParams` sets the merged params with `req.setParams(newParams)` (`solr/core/src/java/org/apache/solr/request/json/RequestUtil.java` line 182). Other request constructions were not checked. The draft's Limits says so.

14. NOTE (15144, minor code). `TimeAllowedLimit.java` lines 98-103 return before `params.set(USED_PARAM, ...)` (line 108) and the debug log (line 109). This is harmless, because the timeAllowed value is removed on that path. Noted only.

15. NOTE (local refs). The local branches `solr-17372-submit` (2869441fb32), `solr-17841-submit` (2d02b5ed416c) and `solr-6207-submit` (2f6755062ebc) are older than their `origin/` tips. There is no local `solr-15144-submit`. None of these were changed. The audit used `origin/`.

## Task results

**SOLR-15144: held for owner decision, not drafted.** The head matches the receipt. The only logic change is in `TimeAllowedLimit.adjustShardRequestLimit`. The unit test (2 of 2 at head, per the receipt) matches the diff. The code reading agrees with the receipt that base fails `testDocumentFetchIsNotSkippedWhenTimeIsUsedUp`. The record supports only a unit-level claim. Under option (a), findings 1 to 3 give the corrected title, comment and behavior sentence. Option (b) widens into SearchHandler and has no end-to-end evidence yet (finding 4). Option (c) holds the branch. No option in the record shows the Jira symptom (empty docs when QTime exceeds timeAllowed) fixed, so the ticket stays open under (a). Commit subjects need a decision too (finding 5).

**SOLR-17372: failed gate, audit only, not drafted.** The head matches the receipt. The diff is one test file, which replaces the fixed tolerance of 1.0 with `max(1.0, 10%)`, the approach a 2024 Jira comment proposed. Upstream `StatsComponentTest.java` is unchanged since base e2cdb2d7e8ae, so the assertion still exists on main. The record shows no failing base run at the gate seeds, and it classes the premise as not real. That leaves the change with no fail-before proof, so it can only be a stabilization. Option (i) needs a rewritten Proof and finding 6. Option (ii), hold as moot, rests on the record's premise call, not on an upstream change. Option (iii) is new work. Finding 7 applies to all three.

**SOLR-17841: retire candidate, audit only, not drafted.** The head matches the receipt. The branch has no production change. The fix is already on main through #4724 (finding 8), and the Jira status reads Resolved. The receipt's pass is on a tree that predates the fix. The branch's parts are the benchmark (no upstream counterpart, finding 10), one rerank test that is off-topic (finding 9), and one multithreaded test that overlaps existing coverage (finding 9). Recommendation: retire the branch as a PR candidate. The owner decides whether to keep the benchmark toggle. No test run was done to show the tests on current main.

**SOLR-6207: draftable, draft written.** Draft: `pr-drafts/search-components/SOLR-6207.md`, written against head b099a9f1be5a3fb529767ca625f42e7860e62f13. The Proof says plainly that this is a documentation test that passes on base too, with no fail-before by design (receipt line 7). The draft includes one choice to check, document or deprecate, citing the 2014 comment on the ticket. Checks: the javadoc link targets exist (`getOriginalParams`, `setParams`, `getParams` in `SolrQueryRequest.java`); the production edit is javadoc only; the test is at lines 25-39; the trial merge is clean. The GitHub run cited in the receipt (37645962226) was not checked and is not cited in the draft. No Lucene behavior is named, so the 9.x and 10.x check did not apply.

## Owner decisions

1. SOLR-15144: pick option (a), (b) or (c). If (a), approve the corrected title, comment and behavior sentence (findings 1 to 3).
2. SOLR-15144 and SOLR-17372: rebuild or squash the branches before any PR, since their commit subjects carry process words (finding 5).
3. SOLR-17372: pick option (i), (ii) or (iii). If (i) or (iii), first get a base run at Jira seed 2074D8EC40F42163 (finding 7).
4. SOLR-17841: make the retire call, and decide whether the benchmark toggle is kept (finding 10).
5. SOLR-6207: approve the draft, choose document or deprecate, and keep or drop the changelog entry (finding 12).
6. Update the inventory row for SOLR-6207 (finding 11).

## Not checked

- No builds, Gradle or tests. No gate logs are on disk. I searched the workspace outside the source trees for `g15144*`, `g17372*`, `g6207*`, `g17841*`, and the round 35 review files the receipts name; none exist.
- No `gh` calls. None of the four tickets has a live PR per the inventory. The GitHub run numbers in the receipts (37639175484 for 17372, 37645962226 for 6207) were not verified.
- SearchHandler and the coordinator QueryComponent path were not audited (sub-batch 2). The round 35 end-to-end finding was not re-derived.
- Whether the SolrIndexSearcherTest core loads `test-files/solr/solr.xml` (which sets `indexSearcherExecutorThreads` to 4) was not confirmed. That decides whether the 17841 multithreaded test takes the multithreaded path.
- Whether the test code compiles (the ReRankCollector constructor in 17841, the anonymous SolrQueryRequestBase subclasses in 15144 and 6207).
- Existing `TestQueryLimits` expectations for the fetch phase were not re-read beyond lines 138-150.
- Whether every request construction path puts raw params into `origParams` (6207 Limits covers this).
