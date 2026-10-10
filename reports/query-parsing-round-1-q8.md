# Query parsing round 1, part q8 report

Result: SOLR-10897 is draftable at head d9240d0750f (draft written), but hold it until two FIX items clear; SOLR-16570 is audit only, not drafted, and its branch carries a handoff file that must come out before any submission.

Head checked: worktree at claim commit c87876a40f2. Branch heads read with read-only git only: `solr-10897-submit` d9240d0750f, `solr-16570-submit` 974c44f9608, `solr-17796-submit` 661165d2673. Base for 10897 is c3cdf7b46e8 (merge-base with upstream/main 8e62c268688). Base for 16570 is cabedd1d968.

## Findings

1. FIX. `solr/core/src/test/org/apache/solr/search/TestSimpleQParserPlugin.java` line 598 at head d9240d0750f. The branch adds a `@Test` annotation directly above the existing method `testQueryAnalyzerIsUsed` (line 599). Evidence: the base (c3cdf7b46e8, line 580) and upstream/main (8e62c268688, line 580) both have no annotation on that method. The head has 16 `@Test` lines and 18 `test*` methods. The receipt's count of 18 matches the methods, which suggests unannotated `test*` methods still run. That is inferred from the count, not checked in the runner source. Replacement: delete line 598 (the line `  @Test`) so the method matches upstream. This changes the tree, so re-run `TestSimpleQParserPlugin` at the new head before the draft's Proof names that head.

2. FIX (before any PR, owner decision). Commit subjects on `solr-10897-submit`, which become the public PR commit list. `6a3828edafe` reads "SOLR-10897: SOLR-10897: simple query parser builds point queries for point fields" (key doubled). `e3fb2e11345` reads "SOLR-10897: add hypothetical-reproduction handoff doc", and `d9240d0750f` reads "SOLR-10897: remove hypothetical-reproduction handoff doc". The last two put an internal process name in public history. Replacement: squash the four commits into one commit with the subject "SOLR-10897: match point fields in the simple query parser". The net diff is four files, so a squash alone leaves the tree unchanged. Combined with FIX 1 it changes the tree. A force push to the fork needs the owner's direction.

3. NOTE. The receipt line 6 says "20 of 20 focused tests at the head, including TestSimpleQParserPlugin 18." The branch changes one test class, and that class has 18 test methods at head. The other two tests are not named anywhere on disk. Replacement for the receipt line: "18 of 18 in TestSimpleQParserPlugin at d9240d0750f; any other focused tests must be named from the gate log." The draft uses 18 only.

4. NOTE. `research/branch-reviews/round-28/SOLR-10897-review.md`, the "Ticket premise and evidence" paragraph, says "the local JIRA packet says" the analyzer-based TermQuery cannot match point fields. The packet, `research/jira-context/SOLR-10897.json`, has a Summary only, `"Description": null`, and no comments. The mechanism claim comes from the code and the test run, not from the ticket. The draft says it that way. Replacement for the review sentence: cite the code at SimpleQParserPlugin.java lines 184-217 instead of the packet.

5. NOTE. The review's compatibility question about `schema.getField(field)` at SimpleQParserPlugin.java line 190 at head. By reading the source, the base default path already rejects an unknown `qf` field with BAD_REQUEST: `IndexSchema` `SolrQueryAnalyzer.getWrappedAnalyzer` calls `getDynamicFieldType`, which throws "undefined field <name>" (upstream/main IndexSchema.java). The head path throws BAD_REQUEST from `getField` with the text `undefined field: "<name>"`. Same status and outcome, different message text. The review's "not a confirmed regression" holds. No replacement needed. Not run.

6. NOTE. Prefix terms on point fields. SimpleQParserPlugin.java line 240 at head calls `type.getPrefixQuery` for non-text fields. Upstream PointField.java lines 241-247 throws BAD_REQUEST "Can't run prefix queries on numeric fields" for any non-empty prefix. This branch does not change that path, so the failure is pre-existing. In a mixed `qf` it fails the whole request. Replacement: none in the branch (not new code). The draft names it in Limits with a follow-up plan.

7. NOTE. Fuzzy terms on point fields. SimpleQParserPlugin.java lines 271-274 at head build `new FuzzyQuery(new Term(field, text), fuzziness)` for non-text fields with no error. By reading the code, a point field has no terms, so the query matches nothing. Not run. The draft names it in Limits.

8. NOTE. Proof logs and queue status. The receipt names `g10897-gate.log` (line 5) and `g10897-premise.log` (line 7). Neither was found by name within four levels of the workspace root. The round-28 review (2026-10-07) says `queue-report.ps1` returned no row for SOLR-10897. The receipt (2026-10-05) says gate green. The draft relies on the receipt, as the rules require. Replacement: add the log path or queue row to the receipt before submission.

9. NOTE. Upstream drift. The base is 47 commits behind upstream/main. No upstream commit has touched `SimpleQParserPlugin.java`, `TestSimpleQParserPlugin.java`, or `schema-simpleqpplugin.xml` since the base. `git merge-tree` of base, upstream/main, and head shows zero conflict markers. No fix needed.

10. Checked, no change. The changelog fragment `changelog/unreleased/SOLR-10897-simple-parser-point-fields.yml` matches the upstream format (sample: `changelog/unreleased/PR#4157-fix-getFirstLiveDoc.yml`). Author is Nick Shanin. Commit authors and committers are Nick Shanin, with no Claude trailers. No "ICLA pending" or workspace placeholder appears in the changed files. `schema-simpleqpplugin.xml` is used only by `TestSimpleQParserPlugin`.

11. FIX (before any submission). `SOLR-16570-TESTING.md` is present at the tip tree of `974c44f9608` (46 lines in the diff from base). The commit `5676646936d` is titled "SOLR-16570: add hypothetical-reproduction handoff doc". Replacement: remove the file and squash the branch before any push that feeds a PR. The pipeline or owner does this. I did not edit the branch.

12. NOTE. Collapse pairing with SOLR-17796 (fix not audited). Production code: 16570 changes `CollapsingQParserPlugin.java` (hunk `@@ -620,0 +621,4 @@`) and `schema11.xml`. 17796 changes `QueryParser.java` and `QueryParser.jj` only. No production overlap. Shared test class `TestCollapseQParserPlugin.java`: 16570 adds 28 lines at base line 1026 (hunk `@@ -1026,0 +1027,28 @@`), and 17796 adds 43 lines at line 286 (hunk `@@ -286,0 +287,43 @@`). `git merge-tree` from the merge-base of the two heads, `56ec140e3636`, shows zero conflict markers, and no objects were written. Landing order: none required. The second branch to land re-checks its hunk.

13. NOTE. Register state for 16570. The receipt says the tip moved once and was re-registered at `974c44f9608` on 2026-10-08, and the live tip matches. The inventory line 576 in `branch-focus-inventory-2026-10-08.md` still lists 16570 as a register head older than the live tip. One of the two records is stale. The receipt is the named source, so the owner should reconcile.

## Task results

**SOLR-10897: draftable.** The draft is at `pr-drafts/query-parsing/SOLR-10897.md`, written against head d9240d0750f. It is about 3,900 bytes, a little over the 3,500 guide. Verdict: ready to submit after FIX 1 and FIX 2, which both change the head. The focused class must run again at the new head before the Proof can name it. The Proof rests on the receipt's counts (18 of 18 at head; one base failure, `testPointFieldQuery`). The logs are not on disk. The one Choice is whether a non-number on a point field should be skipped or raise a 400. The Limits name prefix terms, fuzzy terms, quoted phrases, and untested point types.

**SOLR-16570: audit only, not drafted (per assignment).** Verdict: not submittable. No gate and no pipeline run exist. The tip carries `SOLR-16570-TESTING.md` (FIX 11). The pairing with 17796 is in the shared test class only, and it merges cleanly at 17796's merge-base. The fix was not audited.

## Owner decisions

1. Remove the stray `@Test` on `testQueryAnalyzerIsUsed` (FIX 1, recommended), then re-run `TestSimpleQParserPlugin` at the new head.
2. Squash the 10897 branch into one commit with a clean subject (FIX 2). The fork push needs your direction.
3. Confirm the Choice in the draft: skip a non-number on a point field, or return a 400.
4. Name the other two tests in the "20 of 20" receipt line, or correct it to 18.
5. Confirm the follow-up plan named in the draft Limits: a follow-up PR for prefix and fuzzy terms on point fields.
6. For 16570: remove `SOLR-16570-TESTING.md` and squash before any pipeline run that leads to a PR (FIX 11).
7. Reconcile the 16570 register state between the receipt and inventory line 576 (NOTE 13), and the 10897 queue row against the receipt (NOTE 8).

## Not checked

- Live Jira. The apache-jira MCP is not in this session's tools. The draft uses the local packet, which has a Summary only and an empty description.
- Gate logs `g10897-gate.log` and `g10897-premise.log`. Not found by name within four levels of the workspace root. The full-tree search was stopped. Counts come from the receipt only.
- Test runs. None were run, per the claim.
- Whether the runner executes unannotated `test*` methods. Inferred from the count, not checked in the runner source.
- Lucene 9.x and 10.x. Not checked. The draft names no Lucene behavior. Quoted phrases on point fields were not traced into Lucene.
- Other point types (long, float, double, date). Not run or read beyond the shared code path.
- Unknown-field behavior (NOTE 5). Read from source, not run.
- The 16570 fix. Not audited, per assignment.
- The 10897 ticket body. Empty in the local packet, so the draft does not quote it.
- No `gh` calls, no posting, no builds, no commits, no branch edits. The only writes are this report and the draft.
