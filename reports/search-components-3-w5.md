# Search components round 1, sub-batch 3, part w5 (SOLR-7498, 9148, 9864, 8003, 18356)

Result: 7498, 9148 and 9864 are draftable after small fixes (drafts written, run dates to fill in); 8003 is held and 18356 is retired, both audit only; the most important FIX is the misplaced comment in the 9864 test file (Finding 1) and the JDBC sentence in the 9148 reference guide (Finding 2).

## Findings

1. **FIX (SOLR-9864, test placement).** `solr/solrj/src/test/org/apache/solr/client/solrj/request/SolrQueryTest.java` lines 128-131 hold the block comment for `testGetSortImmutable`, but the new `testGetCopyKeepsSortClauses` now sits under it (lines 132-146). `testGetSortImmutable` (line 148) has no comment. Evidence: tip `b5826e466b9a`, diff hunk `@@ -129,6 +129,22 @@` inserts the method between the comment and `testGetSortImmutable`. Replacement: move lines 132-146 (the method and the blank line after it) above line 128, so the existing comment stays above `testGetSortImmutable`. The draft does not depend on this.

2. **FIX (SOLR-9148, reference guide).** `solr/solr-ref-guide/modules/query-guide/pages/sql-query.adoc` line 454 says "The JDBC driver cannot set filter queries, since it has no request parameters." That overstates it. Evidence at tip `30f0d7a42d50`: `CalciteSolrDriver.java:92` passes the JDBC `info` Properties to `new SolrSchema(info, ...)`; `SolrSchema.java:58-73` stores them as `properties`; `SolrTable.java:941-943` returns them as the query properties; `SolrTable.java:123-131` reads `solr.sql.fq.N` from them. So a JDBC connection can carry the same keys. Replacement for line 454: "The JDBC driver adds no filter queries of its own." (The draft uses this wording.)

3. **NOTE (SOLR-9148, changelog scope).** `changelog/unreleased/SOLR-9148-sql-filter-queries.yml` line 2 lists "(select, stats, group by)". The code also covers select distinct under map_reduce (`SolrTable.java:805`, reached from `:519-520`). Optional replacement: "...translated into (select, stats, group by, select distinct)." The title is also one long line while the other fragments wrap; that is style only.

4. **NOTE (SOLR-9148, receipt headline).** `receipts/SOLR-9148.md` line 3 says "GATE GREEN at the live tip", while line 6 says the tip count is "a labeled smoke: the live-tip commit is docs only". Checked: `git diff --stat f8758ebe763 30f0d7a42d5` touches only `sql-query.adoc` (+4). The draft follows the receipt's own split: full checks at `f8758ebe763`, one test-class run at the tip. Suggested receipt wording: "Full checks green at f8758ebe763. At the tip 30f0d7a42d5, which changes only a docs page since then, TestSQLHandler 35 of 35 ran as a smoke check."

5. **NOTE (SOLR-9148, round 28 review).** `C:\Users\shaninna\dev\Solr-issues\research\branch-reviews\round-28\SOLR-9148-review.md` cites `SQLHandler.java:130-138` and `TestSQLHandler.java:465-506`. At the tip the lines are 133-139 and 465-508. The review also says the tests cover "filters in facet, map-reduce, and stats plans". That holds for group by under map_reduce (test loop at line 489), not for select distinct under map_reduce, which is untested. No draft change needed; the draft's Limits names the gap.

6. **NOTE (SOLR-7498, receipt counts).** `receipts/SOLR-7498.md` line 6 counts 10 tests across three classes. This branch adds only `ExtractionBackendMetadataTest` (2 tests). `XmlSanitizingReaderTest` and `ExtractingRequestHandlerPermissionTest` are unchanged regression runs. The draft says so.

7. **NOTE (SOLR-7498, unknown-size path not traced).** Base `solr/solrj/src/java/org/apache/solr/common/util/ContentStream.java:39` documents `Long getSize(); // size if we know it, otherwise null`. Base `solr/core/src/java/org/apache/solr/servlet/SolrRequestParsers.java:465-467` sets `HttpRequestContentStream` size only when a Content-Length header is present. So the reporter's null may come from an upload with no Content-Length. If so, a client-side fix would address the root cause, and the server fix only covers the symptom. Not traced here; the draft's Limits says so. This is an owner decision (see Owner decisions).

8. **NOTE (SOLR-8003, claim wording).** The assignment and claim say 8003 "adds only a handoff doc". That is true of tip `f1c99a44961` only (`SOLR-8003-TESTING.md`, 33 lines). The branch also has code commit `a8a217dbc2a`: 6 files, 177 insertions and 21 deletions (`TextResponseWriter.java`, `DocTransformer.java`, `DocTransformers.java`, `RawValueTransformerFactory.java`, `SolrReturnFields.java`, `TestRawTransformer.java`). Replacement wording: "Tip f1c99a44961 adds only the handoff doc. The branch also carries code commit a8a217dbc2a (6 files) and has no gate."

9. **NOTE (SOLR-8003, interactions).** Trial merges with `git merge-tree --write-tree --name-only` (no ref written): `origin/solr-8003-submit` with `origin/solr-14678-submit` conflicts in `solr/core/src/java/org/apache/solr/response/transform/DocTransformers.java`. With `origin/solr-4374-submit` and `origin/solr-7390-submit` the trial merges are clean, although all three touch `SolrReturnFields.java`. w1 and w2 should see this before any landing order is set.

10. **NOTE (SOLR-8003, handoff doc still on tip).** `SOLR-8003-TESTING.md` is still on the tip. The 7498 and 9864 branches removed their handoff docs in "before submission" commits (`2050d8e447a7`, `b5826e466b9a`). 8003 has not. Replacement: none now; before any PR, remove the file the same way.

11. **NOTE (SOLR-18356, upstream landing verified).** `upstream/main` commit `e11a34a5141` ("SOLR-18356: Remove DocsStreamer.convertLuceneDocToSolrDoc(Document, IndexSchema) (#4789)", 2026-08-23) deletes the same 12 lines. Its `DocsStreamer.java` index line (`ab317e883e1..c90ce1170bb`) matches the branch's `DocsStreamer.java` change against its merge base `86bc6f292245`. Upstream callers already use the 3-arg overload (`TextResponseWriter.java:188`, `SolrDocumentFetcher.java:904`, `ReturnFieldsTest.java:432,445,471`). The claim holds. No replacement needed.

12. **NOTE (SOLR-18356, queue result is not proof for the tip).** `research/test-queue/results/SOLR-18356.json` (SUCCESS, finished 2026-08-19, seed `70F951BE744DCFD6`) ran `ReturnFieldsTest.testTwoArgConvertLuceneDocToSolrDocRemoved`. That test is not in `origin/solr-18356-submit` (`git grep` finds nothing). Do not cite that result for `c179cd35713`.

13. **NOTE (SOLR-18356, branch state for the owner).** `origin/solr-18356-submit` is at `c179cd35713`. Local `solr-18356-submit` is at `6d3b45315b7` (behind). The worktree `C:/Users/shaninna/dev/Solr-issues/wt/SOLR-18356` is on local `wt-solr-18356-submit` at `c179cd35713`. Deleting the fork branch does not touch the worktree or the local branches. The tip also still carries `SOLR-18356-TESTING.md` ("remove before the PR") and a mixed-case changelog file name. These matter only if the branch is kept.

14. **NOTE (local branch heads are stale).** For four tickets the local branch names point behind the live heads: `solr-7498-submit` at `40fd1e632522` (live `2050d8e447a7`), `solr-9148-submit` at `bbd7c442b0b4` (live `30f0d7a42d50`), `solr-9864-submit` at `f7d6028dddec` (live `b5826e466b9a`), `solr-18356-submit` at `6d3b45315b7` (live `c179cd35713`). This audit used the `origin/` refs, which match the claim table. Do not push from the local names without checking.

## Task results

**SOLR-7498: draftable.** Draft: `pr-drafts/search-components/SOLR-7498.md`, written against head `2050d8e447a733966d5f6e4742a1a8ea931a2b00`. The branch diff matches the receipt: three files (`ExtractionBackend.java`, the new `ExtractionBackendMetadataTest.java`, the changelog), and the tip commit removes the handoff doc. The test's anonymous `ExtractionBackend` matches the interface as read (`close()` is a default method, `name()` and both abstract methods are implemented). Nothing was compiled. The base failure is consistent with the receipt. Limits name the metadata-level test and the existing "null" values. Open items: run date (placeholder in the draft) and Finding 7. No choice section: I could not verify a live alternative without tracing the SolrJ path.

**SOLR-9148: draftable after Finding 2.** Draft: `pr-drafts/search-components/SOLR-9148.md`, written against head `30f0d7a42d501316bd9d5336fe564608e78bfbba`. The base proof is from `f8758ebe763` per the receipt, and the only later change is a 4-line docs edit (verified). `TestSQLHandler` has 34 `@Test` methods at base and 35 at the tip, which matches the receipt. Every one of the five builders named in the draft has a call to the helper (lines 313, 544, 663, 805, 897). The test covers select, group by facet, group by map_reduce and stats; it does not cover select distinct under map_reduce, which is named in Limits. Open items: Finding 2 and the run date.

**SOLR-9864: draftable.** Draft: `pr-drafts/search-components/SOLR-9864.md`, written against head `b5826e466b9a8664adf11e0f247cff220f2f5953`. The fix is three lines in `getCopy()`. `SortClause` has final fields, so sharing the objects is safe. Counts check statically: `SolrQueryTest` 16 public tests at base and 17 at the tip, `QueryRequestTest` 5, `TestUpdateRequest` 5, total 27, as the receipt says. Finding 1 should be fixed before the PR; the draft text is unaffected. Open item: run date.

**SOLR-8003: held, audit only.** No gate, premise unverified (per the receipt and the handoff doc's own "guesses to verify first"). The branch's code commit touches six files, including two w2 files and `SolrReturnFields.java` (w1). It conflicts textually with 14678 in `DocTransformers.java` (Finding 9). The handoff doc is still on the tip (Finding 10). No draft. It would need a gate, the premise check, a decision on the glob-level raw-marker design, and the handoff doc removed before any PR.

**SOLR-18356: retired, audit only.** The claim is confirmed (Finding 11): upstream `e11a34a5141` landed the identical removal. The only open question is the fork branch: delete it or keep it. No draft. The 18356 queue result does not cover the tip (Finding 12).

## Owner decisions

1. Run dates for the three drafts. The receipts give branch dates and "recorded" dates, not run dates. The drafts carry placeholders "[run date to confirm]".
2. SOLR-7498: trace why the SolrJ upload has no size (Content-Length, Finding 7) before submitting, or submit with the metadata-level limit as drafted.
3. SOLR-9148: add a test for select distinct under map_reduce before submitting, or keep it as the Limits follow-up offer as drafted.
4. SOLR-9148: approve the JDBC wording (Finding 2) and the changelog scope (Finding 3).
5. SOLR-18356: delete `origin/solr-18356-submit`, or keep it. Deleting it does not remove the local worktree or local branches; those are a separate choice.
6. SOLR-8003: whether it stays in the queue at all, given the open design choice and the 14678 conflict. Coordinate landing order with w2.

## Not checked

- Live heads: no `git ls-remote` or fetch. Used the local `origin/` remote-tracking refs, which match the claim table.
- GitHub: no `gh` calls. None of the five has a PR number in the inventory or the round 28 reviews, so no `gh pr view` or `gh pr checks` was possible. The receipt's Actions run numbers (37293241776, 37565158326, 37307682549) were not checked.
- Jira: no live JIRA query. Used the hydrated packets in `research/jira-context/`, which are snapshots.
- Gate logs named in the receipts (`g7498-gate.log`, `g7498-premise.log`, `g9148tc-gate.log`, `g9148-premise.log`, `g9148-premise2.log`, `g9864-gate.log`, `g9864-premise.log`) are not under `research/` (bounded search, no hits). All proof counts come from the receipts only.
- Compile, tidy, Error Prone, Spotless, and module check results: taken from the receipts, not rerun. No builds or tests run.
- Lucene: none of the three drafts names Lucene behavior, so no 9.x or 10.x check was needed.
- 8003: code not audited beyond the diff stat, the changelog, the handoff doc, and the `SolrReturnFields` hunk. Premise not verified.
- Changelog validator: not run.
- Other parts' drafts in `pr-drafts/search-components/` were not touched.
- Nothing committed, pushed, or posted.
