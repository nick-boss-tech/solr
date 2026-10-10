# Query parsing round 1, part q2: grammar pair (SOLR-12212, 17796) and SOLR-17882 audit

Result: SOLR-17796 is draftable after one comment fix; SOLR-12212 is draftable only after its grammar fix and a parser regeneration; SOLR-17882 stays a retire candidate and gets no draft.

## Findings

1. **FIX (SOLR-12212, grammar accessors).** `solr/core/src/java/org/apache/solr/parser/QueryParser.jj` at `876953fdc92`, lines 231, 232, 237, 240.
   Evidence: lines 237 and 240 call `getOccur()` and `getQuery()`. The same branch's `QueryParser.java` uses `occur()` and `query()` in the matching spots (lines 252 and 255). On `upstream/main`, `getOccur()` appears only at `QueryParser.jj:231`. `occur()` appears at five call sites in four files under `solr/core/src/java` outside the parser package (for example `SolrPluginUtils.java:471`). The 17796 grammar already changes lines 231 and 232 to `occur()` and `query()`.
   Replacement, with the file's indentation:
   - line 231: `    if (clauses.size() == 1 && clauses.get(0).occur() == BooleanClause.Occur.SHOULD) {`
   - line 232: `      Query firstQuery = clauses.get(0).query();`
   - line 237: `    if (clauses.size() == 1 && clauses.get(0).occur() == BooleanClause.Occur.MUST) {`
   - line 240: `      Query onlyQuery = clauses.get(0).query();`
   Lines 231 and 232 are pre-existing. They are included so the grammar produces the checked-in file.

2. **FIX (SOLR-12212, generated parser).** `solr/core/src/java/org/apache/solr/parser/QueryParser.java` at `876953fdc92`.
   Evidence: JavaCC names lookahead helpers after the grammar line they come from. On `upstream/main`, `jj_3R_MultiTerm_329_3_3` matches `QueryParser.jj:329` (`text=<TERM>`), and `jj_3R_Clause_251_7_4` matches line 251. The 12212 branch adds eight grammar lines above those productions. A regenerated file would therefore rename every helper below `Query` by +8 (for example `jj_3R_MultiTerm_337_3_3`). The checked-in file has no renames. The 17796 branch shows the expected pattern: 329 became 338 after its +9 change.
   Replacement: no text edit. Regenerate `QueryParser.java` from the fixed grammar with the javacc task at the owner's verify session. The diff should show the helper renames and the block at lines 252 to 259, and nothing else. Not run here.

3. **FIX (SOLR-12212, test comment).** `solr/core/src/test/org/apache/solr/search/TestSolrQueryParser.java` at `876953fdc92`, line 590.
   Evidence: the comment says the behavior holds "including the ticket's versions" (6.6.2, 7.3, 8.0, from `research/jira-context/SOLR-12212.json`). Those releases were not run.
   Replacement for line 590: `    // off (the behavior at luceneMatchVersion before 10.2) the`
   The current line reads `    // off (the behavior at luceneMatchVersion before 10.2, including the ticket's versions) the`.

4. **FIX (SOLR-17796, comment in the grammar and the generated parser).** `QueryParser.jj` lines 236 and 237, and `QueryParser.java` lines 251 and 252, at `661165d2673`.
   Evidence: the comment says wrapping a PostFilter "would force Weight-based evaluation, which PostFilters do not support." That is too broad. `FunctionRangeQuery` implements `PostFilter` and has a `createWeight` (`solr/core/src/java/org/apache/solr/search/FunctionRangeQuery.java:33` and `:44`). `CollapsingPostFilter` has none (`CollapsingQParserPlugin.java:269`, and the ticket message says so).
   Replacement, in both files, with the same 8-space indent. It replaces the two comment lines with these three:
   `        // A lone required PostFilter is returned as is. Wrapping it in a BooleanQuery makes Lucene`
   `        // ask it for a Weight, and some PostFilters, such as CollapsingPostFilter, do not implement`
   `        // createWeight. A negated one must stay wrapped.`

5. **NOTE (SOLR-17796, negated collapse has no test).** `QueryParser.jj:235` checks `MUST`, so a negated post filter stays wrapped. A negated `CollapsingPostFilter` would then take the same wrapper path that throws in the ticket. `TestCollapseQParserPlugin.java:307-328` uses a negated `frange`, which has a Weight, so it does not test this case. Not run. Before posting, add a negated collapse case to a run. If it throws, the branch needs its own fix before submission. A Limits line is not enough.

6. **NOTE (SOLR-17796, multi-clause filters).** `SolrIndexSearcher.java` on `upstream/main`, lines 1238 to 1252, takes post filters only from the top-level filter list. A post filter inside a larger BooleanQuery is not taken out. So `+{!collapse field=x} +a:1` is not covered and has no test. The draft's Limits says so and offers a follow-up. By reading only.

7. **NOTE (SOLR-17796, title and body disagree).** The packet `research/jira-context/SOLR-17796.json` has the title "Exclusion tag on fq collapse does not work with q.op OR". Its body reports the failure with `q.op=AND`. The draft quotes the body, as the receipt asks. The PR title should follow the body. Owner decision.

8. **NOTE (SOLR-17796, changed lines).** The 17796 grammar also changes the pre-existing lines 231 and 232 to `occur()` and `query()`. The draft's "What this change does" says so. No change needed.

9. **NOTE (upstream drift, before either branch).** On `upstream/main`, `QueryParser.jj:231-232` use `getOccur()` and `getQuery()`, while `QueryParser.java` uses `occur()` and `query()`. The checked-in parser is not what its grammar produces. 17796 fixes this on its branch. 12212 needs finding 1.

10. **NOTE (landing order and textual merge).** The grammar files are identical at `upstream/main` and at both branch merge bases (`git diff` is empty). `git merge-tree --merge-base=upstream/main` between the two tips auto-merges `QueryParser.jj` and `QueryParser.java` with no conflict. The merged grammar has 17796's block, then 12212's block, and 12212's block still uses `getOccur()` and `getQuery()` until finding 1 is fixed. The test files do not overlap: `TestSolrQueryParser` for 12212, `TestCollapseQParserPlugin` for 17796. Recommended order: 17796 first, then 12212. 17796 already fixes the shared lines and regenerates the whole file. The second branch's compiled code changes after the rebase, so its receipt no longer covers its tip and needs a new gate.

11. **NOTE (auto-fix flag, SOLR-12212).** The new check does not read `autoFixPureNegative`. Its auto-fix-off test calls `setAutoFixPureNegative(false)` (`QParser.java:203` on `upstream/main`). By reading only: with the auto-fix on, `SolrQueryParser.getBooleanQuery` (`SolrQueryParser.java:32-35`) has already repaired the nested query, so the new check does not fire there. If SOLR-15906 changes `QParser.java`, that is part q7's question.

12. **NOTE (counts do not reproduce from source).** `TestSolrQueryParser.java` on the base has 19 lines with `@Test`, but the receipt says 38 of 38 on base. `TestCollapseQParserPlugin.java` at `661165d2673` has 17 lines with `@Test`, but the receipt says 21. The JUnit XML is not on disk, so both drafts quote receipt counts only. The 12212 draft has a placeholder for the per-class count.

13. **NOTE (local branch refs are stale).** Local `solr-12212-submit` is at `b189ea96924`, and local `wt-solr-17796-submit` is at `3215b66083a`. Both are older than the live tips. The origin refs match the claim table. This part used the origin refs: `origin/solr-12212-submit` at `876953fdc92`, `origin/solr-17796-submit` at `661165d2673`, and `origin/solr-17882-submit` at `d328eb392d9`.

14. **NOTE (SOLR-17882, premise confirmed by reading).** `solr/core/src/java/org/apache/solr/core/SyntheticSolrCore.java` lines 80 to 86 on `upstream/main` return a bare `new RestManager()` that is never initialized. The branch's fallback, `modelStore` in `solr/modules/ltr/src/java/org/apache/solr/ltr/search/LTRQParserPlugin.java` at line 150, turns the NPE into the NOT_FOUND error that `ManagedModelStore.getManagedModelStore` raises. The branch's own commit message and `SOLR-17882-TESTING.md` say the fallback does not help coordinators. Verdict: not a fix.

15. **FIX if the branch is ever submitted (SOLR-17882, stray file).** `SOLR-17882-TESTING.md` at the repo root (99 lines, added in `74d2726e29b`, kept in `d328eb392d9`). 12212 and 17796 both removed their handoff docs. Replacement: delete the file in any branch before a PR. Not done here.

16. **NOTE (SOLR-17882, lock on every lookup).** `LTRQParserPlugin.java:150` is `private synchronized ManagedModelStore modelStore(SolrCore core)`. Line 197 calls it for every named model, so every LTR query with a model takes the plugin lock, even after `mr` is set. Not measured.

17. **NOTE (SOLR-17882, branch doc is older than the receipt).** `SOLR-17882-TESTING.md` cites round 4 (`research/branch-reviews/round-4/SOLR-17882-review.md`, head `74d2726e29b`). The receipt cites rounds 12 and 15 at the live tip. Both say the fallback does not fix the ticket. The verdict does not conflict.

## Task results

Heads: all three match the claim table at the origin refs. 12212 is `876953fdc92`, 17796 is `661165d2673`, and 17882 is `d328eb392d9`, with the previous tip `74d2726e29b`, as the claim says.

**SOLR-12212: draftable after findings 1 to 3; hold posting until the fixes move the head.** Draft: `pr-drafts/query-parsing/SOLR-12212.md`, written against `876953fdc92`. The Proof uses what the receipt supports. The default-version test passes on base, so the draft calls it a guard. The auto-fix-off test separates base from the fix, because the receipt's round 2 "discriminated" the case. The receipt gives no failure count for that run, so the draft says the base code fails `q.op=AND`, and the owner must confirm that from the premise log. The Lucene check is by code, not by a build. The boundary is `QParser.java:113-118`, which uses `onOrAfter(LUCENE_10_2_0)`. Values below 10.2 include all of 9.x, 10.0, and 10.1. The draft has one choice to check (fix at the top level, or inside the parser), with a pointed question. Its per-class count is a placeholder.

**SOLR-17796: draftable after finding 4; hold until finding 5 is decided and the PR title is set.** Draft: `pr-drafts/query-parsing/SOLR-17796.md`, written against `661165d2673`. The receipt records 21 of 21 at the head, and exactly one failure on base (`testCollapseFilterIsNotWrappedWhenRequired`). The negated frange test passes on base. It does guard the negation, because a wrongly unwrapped negation would return the wrong count. The draft has no Choice section. The multi-clause and negated collapse cases go to Limits with a follow-up offer. The draft quotes the Jira body, which names AND. The generated parser is consistent with its grammar by reading: the action code matches, and the helper renames follow the +9 shift. No JavaCC run was done.

**SOLR-17882: audit only, no draft. Verdict: stays a retire candidate.** The branch does not fix the reported symptom, confirmed by reading `SyntheticSolrCore.initRestManager()` (finding 14). The retire-or-re-aim call is the owner's. A re-aim is a separate change in `solr/core`, and the branch's own note says it needs a design decision and a node-roles test. Finding 15 applies if the branch is kept.

**Grammar pair: landing order 17796 first, then 12212.** See finding 10. Both need a fresh gate after the second one lands, because the second branch's compiled code changes on rebase.

## Owner decisions

- SOLR-17882: retire the branch, or re-aim it as a separate `solr/core` change in `SyntheticSolrCore`.
- SOLR-17796: the PR title should follow the Jira body (AND), not the Jira title (OR). Confirm.
- Landing order: 17796 first, then 12212 (recommended).
- SOLR-17796: add a negated collapse case before posting (finding 5). If it throws, a branch fix comes first.
- SOLR-12212: regenerate the parser at the verify session (finding 2). This needs your Gradle go-ahead.
- SOLR-12212: confirm from `g12212-premise.log` that round 2 recorded the `q.op=AND` failure on base with the auto-fix off. Fill the per-class count from `g12212-gate.log`.
- SOLR-12212: keep the top-level fix (option 1) as the draft's choice, or switch the draft to the parser-level fix (option 2).

## Not checked

- No Gradle, no JavaCC run, no build, no test, no `gh`, no fetch, no commit. Only read-only git was used.
- The generated-file match is by reading: action code, accessor names, and helper names. A full regeneration diff was not produced.
- The Lucene accessor split (`occur()` and `query()` versus `getOccur()` and `getQuery()`) is from repo usage only. No Lucene jar was opened, so the 9.x and 10.x APIs were not checked directly.
- Whether a Lucene BooleanQuery builds a Weight for a prohibited clause. Finding 5 depends on it.
- Gate logs (`g12212-gate.log`, `g12212-premise.log`, `g17796-gate.log`, `g17796-premise.log`) and JUnit XML are not on disk. Per-class counts and the base failure output were not checked.
- Callers that skip `makeQueryable`, the cost named in the 12212 draft, were not listed.
- The nested cases in the 12212 and 17796 Limits were checked by reading only.
- SOLR-15906 (`QParser.java`) and its effect on 12212 and 17796: part q7.
- SOLR-16570 and its pairing with 17796 (`CollapsingQParserPlugin.java`): part q8. Not audited here.
- The main-side goal files behind the 17882 receipt (rounds 12 and 15) were not read. The 17882 verdict was checked against the round 4 review and the code.
- Jira: only the local packets in `research/jira-context/` were read. No live Jira call.
