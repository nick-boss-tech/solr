# Query parsing draft fidelity, slice 2

Assignment: `assignments/pool-draft-fidelity-configsets-query-schema.md`. Claim: `claims/pool-draft-fidelity-configsets-query-schema.md`, slice A2 (SOLR-10897, 11391, 11761, 12212, 12532). Worktree HEAD `d627304e96b`.

Read only. No build, Gradle, test, gate run, test-queue command, fetch, commit, push, or post. Category report: `reports/query-parsing-round-1.md` with parts `reports/query-parsing-round-1-q1.md` to `-q8.md`. Receipts: `receipts/<TICKET>.md`. Material: a grep of `material/` for the five numbers returned no matches, and no query-parsing answers file exists there.

Head check (`git ls-remote origin refs/heads/<branch>`):

- solr-10897-submit: live `d9240d0750faf55d21fb125668ea66db0142be79`, draft `d9240d0750f`. Match.
- solr-11391-submit: live `b0f15a22856a887d4e369cd1542c0ce09a687059`, draft `b0f15a22856`. Match.
- solr-11761-submit: live `41893ee9ce6101224f46e0287a1f2f0bfe6a6974`, draft `41893ee9ce6`. Match.
- solr-12212-submit: live `876953fdc92714a1692615bb67685b22c3814483`, draft `876953fdc92`. Match.
- solr-12532-submit: live `69c06da446797cec5c3c8dd885e19a28381789d3`, draft `69c06da4467`. Match.

Merge bases (`git merge-base <head> upstream/main`): 10897 `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622`; 11391 `cabedd1d968059215188f4e7563fb303241899ed`; 11761, 12212 and 12532 `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. All eight SHAs named in the drafts and here resolve in the worktree object store (`git cat-file -t`).

Common checks: all five receipts exist; every Proof count matches its receipt; every draft has a verification date; every changelog fragment exists at its head and its title matches the draft's lead claim; no em dash or en dash appears in any draft; no internal process word appears in posting text (the 11391 HOLD block is the exception, see its notes). Several citations link the head SHA for code the branch does not change. Under `pr-formula.md`, that code links the merge base and the text says so. Those items are numbered below.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-10897 | `d9240d0750f`, match | DRIFT (1 item) |
| SOLR-11391 | `b0f15a22856`, match | DRIFT (1 item) |
| SOLR-11761 | `41893ee9ce6`, match | DRIFT (3 items) |
| SOLR-12212 | `876953fdc92`, match | DRIFT (3 items) |
| SOLR-12532 | `69c06da4467`, match | DRIFT (1 item) |

## SOLR-10897

Verdict: DRIFT (1 item).

1. Draft says: "The point field type rejects prefix queries ([PointField.java](https://github.com/apache/solr/blob/8e62c2686882aa704480ab13b6a60ee8f7b5c8af/solr/core/src/java/org/apache/solr/schema/PointField.java#L241-L247))."
   - Evidence: the branch does not change PointField.java (`git diff --stat c3cdf7b46e8 d9240d0750f` lists four files, none of them this one). The link points at upstream main `8e62c268`, not the merge base. Lines 241 to 247 are identical at both SHAs. Per `pr-formula.md`, pre-change code links the merge base and the text says so.
   - Replacement: "The point field type rejects prefix queries ([PointField.java as it stood before this change](https://github.com/nick-boss-tech/solr/blob/c3cdf7b46e8cfff3673f76d881f32cf8e7b00622/solr/core/src/java/org/apache/solr/schema/PointField.java#L241-L247))."

Checked and consistent: the Proof (TestSimpleQParserPlugin 18 tests, one failure, `testPointFieldQuery`, `mismatch: '1'!='0' @ response/numFound`, verified 2026-10-05) matches the receipt. The head links hold: `SimpleQParserPlugin.java` L184-L217 (the point branch is at L193 to L203), `TestSimpleQParserPlugin.java` L580-L596 (`testPointFieldQuery`), and `schema-simpleqpplugin.xml` L53-L56. The changelog fragment (8 lines) exists at head. The Choice (skip or 400) matches q8 finding 1 and owner decision 3. The Limits (prefix, fuzzy, quoted phrases, other point types) match q8 findings 6, 7 and the Not checked list.

Optional notes, not blocking:
- Hold, not text drift. q8 FIX 1 and FIX 2 are still open at head. Line 598 of `TestSimpleQParserPlugin.java` carries a stray `@Test` on `testQueryAnalyzerIsUsed` at `d9240d0750f`; I confirmed it is absent at `c3cdf7b46e8`. After FIX 1 (delete that line) and FIX 2 (squash the four commits), the head changes. Re-run the class, then re-cut the Proof head and every link. The draft has no hold marker; add one or post only after those land.
- The receipt line says "20 of 20" at head. The draft uses only the 18 that the receipt names for this class, which matches q8 finding 3.
- Receipt log paths `g10897-gate.log` and `g10897-premise.log` are not on disk (q8 finding 8).
- Commit subjects still carry the doubled key and the handoff-doc wording (q8 finding 2). Squash before a PR.
- Length is 3,901 bytes, a little over the roughly 3,500 guide.
- Plain language: "analyzed terms" and "boosts" are compressed.

## SOLR-11391

Verdict: DRIFT (1 item).

1. Draft says: "A query such as `{!join from=dept_ss to=dept_id_s method=nosuchmethod}title_s:MTS` reaches [`Method.valueOf`](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java#L221) in the join query parser."
   - Evidence: the SHA and line are right. Line 221 at the merge base is `final Method explicitMethod = Method.valueOf(localParams.get(METHOD));`. The public text does not say the link is the state before the change. Only the HOLD block on line 1 says it, and that block is to be deleted before posting. Per `pr-formula.md`, the text must say so.
   - Replacement: "A query such as `{!join from=dept_ss to=dept_id_s method=nosuchmethod}title_s:MTS` reaches [`Method.valueOf`, as it stood before this change](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java#L221) in the join query parser."

Checked and consistent: the `parseMethodString` call at head L220 (the "What this change does" link) is right, and the helper at L268 to L276 is the existing 400 path. `TestScoreJoinQPNoScore.java` L61-L68 is the new test at head. The Proof (5 of 5, base answers 500 where the branch answers 400, verified 2026-10-09) matches the current receipt. The changelog fragment (8 lines) exists at head, and its title matches the draft. The Limits match q3 (the branch changes only the error path).

Optional notes, not blocking:
- Line 1 is a HOLD block with internal words ("owner", "Drafted against head", "not for posting"). The Jira line is still `SOLR-<key to be chosen>`. Both are expected for a hold. Delete line 1 and fill the Jira line before any posting. Not counted as DRIFT.
- The changelog link is correct at head, but the fragment's links block (lines 6 to 8) still names SOLR-11391. q3 finding 1 says the branch must change that before posting. The draft's Changelog line then stays as it is.
- Limits bullet 2 ("does not change join speed on non-point fields") repeats the subject of the ticket this branch must not link. Consider cutting it.
- The receipt is current: it names `b0f15a22856` and says the handoff note is removed. q3 finding 10, which says the receipt names `825d7f81d12`, is superseded.
- Round q1 lists the draft as "none", and q3 says "no draft". A HOLD draft file exists and was checked as a hold. The main side should reconcile the two round reports.

## SOLR-11761

Verdict: DRIFT (3 items).

1. Draft says: "The ticket repro calls `parse()` on one parser instance in this order: `/*` (correctly rejected), `/* foo */ bar` (wrongly rejected), `bar` (accepted), and `/* foo */ bar` (still rejected)."
   - Evidence: `research/jira-context/SOLR-11761.json`, Description. The repro first calls `parse("/* foo */ bar")` with the comment "works fine", then `parse("/*")` (rejected), then `parse("/* foo */ bar")` (the bug), then `parse("bar")` (works), then `parse("/* foo */ bar")` (still failing). The draft leaves out the first call, so "in this order" is not accurate.
   - Replacement: "The ticket repro calls `parse()` on one parser instance in this order: `/* foo */ bar` (accepted), `/*` (correctly rejected), `/* foo */ bar` (wrongly rejected), `bar` (accepted), and `/* foo */ bar` (still rejected)."

2. Draft says: "The lucene parser creates a new `SolrQueryParser` for each query ([LuceneQParser.java](https://github.com/nick-boss-tech/solr/blob/41893ee9ce6101224f46e0287a1f2f0bfe6a6974/solr/core/src/java/org/apache/solr/search/LuceneQParser.java#L42))"
   - Evidence: the branch does not change LuceneQParser.java (empty diff against `14c7aac0d151`). Line 42 (`lparser = new SolrQueryParser(this, defaultField);`) is identical at both SHAs. This is pre-change symptom code, so it links the merge base and says so.
   - Replacement: "The lucene parser creates a new `SolrQueryParser` for each query ([LuceneQParser.java as it stood before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/LuceneQParser.java#L42))"

3. Draft says: "The generated token manager keeps a comment nesting count, and `ReInit(CharStream)` does not reset it ([QueryParserTokenManager.java](https://github.com/nick-boss-tech/solr/blob/41893ee9ce6101224f46e0287a1f2f0bfe6a6974/solr/core/src/java/org/apache/solr/parser/QueryParserTokenManager.java#L25))."
   - Evidence: the branch does not change QueryParserTokenManager.java (empty diff against `14c7aac0d151`). Line 25 (`int commentNestingDepth ;`) is identical at both SHAs. `ReInit(CharStream)` is at lines 1582 to 1592 at head and does not touch `commentNestingDepth`. The draft cites only line 25 for the "does not reset" claim.
   - Replacement: "The generated token manager keeps a comment nesting count, and `ReInit(CharStream)` does not reset it ([QueryParserTokenManager.java as it stood before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/parser/QueryParserTokenManager.java#L25), where [`ReInit(CharStream)`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/parser/QueryParserTokenManager.java#L1582-L1592) does not reset it)."

Checked and consistent: `SolrQueryParserBase.java` L238-L245 at head (javadoc 238 to 242, the new `ReInit(QueryParserTokenManager)` at 243 to 245). The generated `QueryParser.java` already has `ReInit(QueryParserTokenManager)` at L799, so "the generated parser overrides that method" holds. `TestSolrQueryParser.java` L545-L559 is `testCommentsAfterSyntaxError`. The Proof counts (38, 39, 7, 5 = 89) match the receipt, and the date (2026-10-05) is present. The Choice (new token manager per parse, or reset in the grammar and regenerate) matches q1 finding 9 and q6, and gives a live alternative with its cost. The Limits claim that no code under `solr/core` calls `ReInit(CharStream)` outside the parser was re-checked with `git grep` at head. The only callers are the parser's own token manager, and two test stubs (`TestReversedWildcardFilterFactory.java` L209, `SolrQueryParserBaseTest.java` L48).

Optional notes, not blocking:
- The Proof lists `TestExtendedDismaxParser` 39 of 39. This branch does not change or extend that class (q6 finding 7). Add a clause saying these classes ran as regression checks, so a reader does not assume a change.
- The receipt gives 38 for `TestSolrQueryParser`. q1 finding 6 found 37 declared `@Test` methods in the file at head. Confirm against the gate XML before posting. The receipt supports 38.
- Branch commit subjects carry "hypothetical-reproduction handoff doc" wording (q1 finding 2). They show in the PR commit list, so squash before a PR.
- Plain language: "token manager", "ReInit(CharStream)", "comment nesting count" are compressed.

## SOLR-12212

Verdict: DRIFT (3 items).

1. Draft says: "- Verified 2026-10-05 at `876953fdc92`. TestSolrQueryParser: [per-class count to fill from the run log]. One run of 80 focused tests, 0 failures."
   - Evidence: `receipts/SOLR-12212.md` says "Counts: 80 focused tests, 0 failures, at the head." It gives no per-class count. The gate log `g12212-gate.log` is not on disk. The round report (row for 12212) and q2 (finding 12, owner decisions) both call the bracketed text a placeholder. It is unfilled text in copy meant for posting.
   - Replacement: "Verified 2026-10-05 at `876953fdc92`: one run of 80 focused tests, 0 failures."

2. Draft says: "The auto-fix is on only from luceneMatchVersion 10.2 ([QParser.java lines 113 to 118](https://github.com/nick-boss-tech/solr/blob/876953fdc92714a1692615bb67685b22c3814483/solr/core/src/java/org/apache/solr/search/QParser.java#L113-L118))."
   - Evidence: the branch does not change QParser.java (empty diff against `14c7aac0d151`). Lines 113 to 118 are identical at both SHAs. This is pre-change code that causes the symptom, so it links the merge base and says so.
   - Replacement: "The auto-fix is on only from luceneMatchVersion 10.2 ([QParser.java lines 113 to 118, as it stood before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/QParser.java#L113-L118))."

3. Draft says: "as [SolrQueryParser.getBooleanQuery](https://github.com/nick-boss-tech/solr/blob/876953fdc92714a1692615bb67685b22c3814483/solr/core/src/java/org/apache/solr/search/SolrQueryParser.java#L32-L35) already does when the auto-fix is on"
   - Evidence: the branch does not change SolrQueryParser.java (empty diff against `14c7aac0d151`). Lines 32 to 35 are identical at both SHAs. "already does" describes pre-change code, so it links the merge base and says so.
   - Replacement: "as [SolrQueryParser.getBooleanQuery, as it stood before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/SolrQueryParser.java#L32-L35) already does when the auto-fix is on"

Checked and consistent: `QueryParser.jj` L237 to L244 and `QueryParser.java` L252 to L259 at head hold the MUST block described. Both new tests sit at `TestSolrQueryParser.java` L571-L584 and L586-L616. The Proof structure matches the receipt (the default-version test is a guard; the auto-fix-off test separates base from the fix). The Choice (option 1, top-level `makeQueryable`; option 2, inside the parser) matches q2 owner decisions. The Limits match q2 findings 3 and 11 and the 10.2 boundary (9.x, 10.0 and 10.1 are below it). The ticket releases (6.6.2, 7.3, 8.0) are correctly described as not run. The changelog fragment (9 lines) exists at head, and its title matches the draft.

Optional notes, not blocking:
- Hold per round. The draft is draftable only after the grammar fix: `QueryParser.jj` lines 231, 232, 237 and 240 must use `occur()` and `query()` (q2 finding 1), the parser must be regenerated (q2 finding 2), and the test comment at `TestSolrQueryParser.java` line 590 ("including the ticket's versions") must be corrected (q2 finding 3). Each of these changes the head. Re-cut every link (`QueryParser.jj` L237-L244, `QueryParser.java` L252-L259, the two test ranges) after the fresh gate.
- The receipt names no failure output for the auto-fix-off run. It says only "Round 2 discriminated with the auto-fix off". `g12212-premise.log` is not on disk (q2 owner decisions). Confirm that log before posting the claim that base fails `q.op=AND`.
- Length is 4,540 bytes, well above the roughly 3,500 guide. Most of it is the Choice and Limits.
- Plain language: "pure negative", "auto-fix", "makeQueryable", "top-level" and "luceneMatchVersion" are compressed. For example, "a query that only excludes documents" and "automatic repair".

## SOLR-12532

Verdict: DRIFT (1 item).

1. Draft says: "eDismax has its own slop path in [ExtendedDismaxQParser.java](https://github.com/nick-boss-tech/solr/blob/69c06da446797cec5c3c8dd885e19a28381789d3/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L1067-L1074)."
   - Evidence: the branch does not change ExtendedDismaxQParser.java (empty diff against `14c7aac0d151`). Lines 1067 to 1074 (the `getFieldQuery` override that sets slop) are identical at the merge base. The draft describes pre-change code that the Limits say is not changed, so it links the merge base and says so.
   - Replacement: "eDismax has its own slop path in [ExtendedDismaxQParser.java, as it stood before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L1067-L1074). The same graph phrase in eDismax is not changed or tested here. We can open a follow-up for it on request."

Checked and consistent: `SolrQueryParserBase.java` L563-L586 (`applySlop`) at head. `TestSolrQueryParser.java` L718-L732 (`testQueryStringSlopOnGraphPhrase`). The Proof (38 of 38, 0 skipped, base fails 1 expected 0 found, verified 2026-10-04) matches the receipt. The BooleanQuery statement matches q1 finding 10 (javap on Lucene 9.12.3 and 10.4.0). The scope statements (wider than word-delimiter graphs, multi-word synonyms untested, eDismax unchanged, shared `schema12.xml` with 27 classes not run) match q1 findings 11 to 13. No Choice section, which matches q1 ("the eDismax question is an owner decision, so it sits in Limits"). The changelog fragment (9 lines) exists at head, and its title matches.

Optional notes, not blocking:
- Confirm the 38 count against the gate XML before posting (q1 finding 6). The receipt supports it.
- The changelog title says "for phrase queries that analyze into a token graph", which is narrower than the wider behavior the draft states. The draft is not wrong. If you want the changelog to match the draft's scope, that is a branch edit.
- Branch commit subjects carry "handoff doc" wording (q1 finding 2). Squash before a PR.
- Plain language: "token graph", "BooleanQuery" and "eDismax" are compressed.

## Not done

- No builds, Gradle, tests, gate runs, test-queue commands, fetches, commits, pushes or posts (per the brief).
- Gate logs, premise logs and JUnit XML named in the receipts are not on disk. Every proof count is checked against its receipt only.
- The Lucene javap results (q1 finding 10, q3 finding 7) were not re-derived.
- Jira text was checked only against the local packets in `research/jira-context/` (the SOLR-11761 repro order and the SOLR-12212 description). The 12212 release list and the 11761 maintainer comment were not checked.
- Cited SHAs were checked in the local object store (`git cat-file -t`, `git show`). They were not checked on GitHub.
- The changelog YAML parse was not run (q1 notes the same gap).
- Other pool slices (A1, A3 to A6) and the claim were not touched. The claim is not marked DONE here; the lead does that.
