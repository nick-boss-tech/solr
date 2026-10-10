# eDisMax round 3, part e5 (SOLR-3923, SOLR-7120, SOLR-14638)

Result: 3923 is not ready for a draft (premise unproven, and the guard misses signed parens); 7120 is sound in shape but its committed handoff note must come out first; 14638 matches its ticket and park note and stays parked for the owner's default decision.

Heads used are the live origin heads named in the claim: 3923 at 723f61ee4ab, 7120 at 6a434a7fc3d, 14638 at 44cf1c8fc80. The local refs solr-3923-submit (059804fcec8) and solr-14638-submit (3443f6db40f) are stale and were not used. Bases: cabedd1d968 for 3923 and 7120, 86bc6f29224 for 14638. upstream/main (8e62c2686882) has the same ExtendedDismaxQParser.java and TestExtendedDismaxParser.java as cabedd1d968.

## Findings

1. **FIX (3923).** File `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java`, lines 310-317 at 723f61ee4ab.
   Evidence: the new guard (line 315) sits inside `if (clause.isBareWord())` (line 310). `isBareWord()` is `must == 0 && !isPhrase` (upstream/main lines 722-724). A signed lone paren such as `+(` or `-)` has `must` set, so it skips the guard, and its `\(` or `\)` still enters the pf phrase text (`addShingledPhraseQueries` builds the phrase from `clause.val`, upstream/main lines 635-640).
   Replacement for lines 310-317:
   ```
           if (clause.isBareWord()) {
             String s = clause.val;
             // avoid putting explicit operators in the phrase query
             if ("OR".equals(s) || "AND".equals(s) || "NOT".equals(s) || "TO".equals(s)) continue;
           }
           // a lone paren, with or without a leading + or -, analyzes to nothing and leaves a hole
           if (isOnlyParens(clause.raw.replaceFirst("^[+-]", ""))) continue;
           normalClauses.add(clause);
   ```
   A test case for `+(` is optional, but the premise run should cover it if this fix is made.

2. **NOTE (3923, premise).** No premise run exists. The handoff note at 059804fcec8 (`SOLR-3923-TESTING.md`, "Guesses to verify first") says the new assertion may pass without the fix if the field drops the paren without leaving a hole. The trace supports that risk. The pf list has wordGrams 0 (upstream/main line 1742), so `addShingledPhraseQueries` uses shingleSize 0 (line 631) and puts all normal clauses in one phrase, `"\( \) \(zzzz xxxx\) "`. Whether a hole appears depends only on how `text_sw` treats a token that is just "(". `text_sw` is at `solr/core/src/test-files/solr/collection1/conf/schema12.xml` lines 508-528 (MockTokenizer, SynonymGraph, WordDelimiterGraph, lowercase, Porter, Flatten). Not run.
   Premise run to request (main side): on upstream/main, add only the new assertion and run the focused test. `parsedquery` must contain `?`, so assertion 2 fails. If it does not, the assertion does not discriminate. Then change the test field to one whose analyzer keeps the gap, as the note suggests, and leave the assertion alone. The changelog title says the holes exist. Keep that claim only if the run shows them.

3. **NOTE (3923, behavior change to state).** With the guard, the pf phrase runs across a removed lone paren. For `a ( b ) c`, the base phrase keeps position gaps where the parens were. The head phrase reads "a b c". The PR "What this change does" section must say so in one plain sentence: "A lone paren no longer takes a position in the pf phrase, so the words on either side sit next to each other in that phrase."

4. **NOTE (3923, assignment drift).** The assignment and the claim say the branch still carries its TESTING.md note. At the live head it does not. Commit 723f61ee4ab ("remove handoff doc") deletes `SOLR-3923-TESTING.md`. The note exists only at 059804fcec8. Correct the claim table and the assignment to say so. The note's open guesses still stand (Finding 2).

5. **FIX (7120).** File `SOLR-7120-TESTING.md` at the repository root of 6a434a7fc3d (28 lines, added by that commit). It is a handoff note ("nothing was compiled or run", "Guesses to verify first") and must not ship. Replacement: delete the file in a branch commit before any PR. No other text change. The 3923 branch removed its equivalent in its last commit. The 14638 branch has the same problem (Finding 8).

6. **NOTE (7120, status change).** Line 1733 changes the base's bare `RuntimeException` (line 1733 on upstream/main, which a client sees as a 500) to `new SolrException(SolrException.ErrorCode.BAD_REQUEST, e.getMessage(), e)`. The 400 matches the DisMax path. `QueryComponent.java` lines 235-236 wrap the same `SyntaxError` as `BAD_REQUEST` (upstream/main). The message "Neither qf nor df are present." comes from `DisMaxQParser.parseQueryFields`, lines 57-62 (upstream/main). `SolrException` and `ErrorCode` are already imported (base lines 46-47), so no import is missing. The changelog and the PR must both say the status changes from 500 to 400.

7. **NOTE (7120, TESTING guesses checked).** (a) `req("q", "foo")` gets no default `df`. `SolrTestCaseJ4.req(String...)` calls `lrf.makeRequest` (SolrTestCaseJ4.java lines 1228-1229). With two arguments, `LocalRequestFactory.makeRequest` in `solr/test-framework/src/java/org/apache/solr/util/TestHarness.java` (lines 457-466) uses only the key and value pairs plus `wt=xml`. (b) No broad catch around parser construction appears in `QueryComponent`, `QParser`, `QParserPlugin`, `SearchHandler`, or `ExtendedDismaxQParserPlugin` (`git grep` on upstream/main returned nothing). Other callers were not searched. (c) The fail-before reasoning holds: base throws a plain RuntimeException, so `expectThrows(SolrException.class, ...)` fails. Not run.

8. **FIX (14638).** File `OPEN-QUESTIONS-SOLR-14638.md` at the root of 44cf1c8fc80 (added by that commit, 16 lines). It says "remove before the PR" and "This branch should not go upstream as is." Replacement: delete the file. It must be gone before the branch moves at all.

9. **NOTE (14638, scope).** The head applies the identity to every multiplicative boost (`ExtendedDismaxQParser.java` lines 551-552: `boosts.add(new DefFunction(List.of(vs, new ConstValueSource(1.0f))));`). The base (lines 551-553) changed only a top-level `QueryValueSource` with default 0. Round 11 (`research/branch-reviews/round-11/SOLR-14638-review.md`, section 3) lists the wider effects: recency decay (undated documents move up), sum and product over a missing field, and `query()` with an explicit default (a 0.5 demotion becomes 1.0). Round 11 read these from `lucene-queries-10.4.0` bytecode. I did not re-check that. The changelog title (`changelog/unreleased/SOLR-14638-edismax-boost-missing-value.yml`, line 1) covers only the ticket's case. If the owner keeps the global rule, the title should say: "In edismax, a multiplicative boost with no value for a document counts as 1 for that document, so the document keeps its score. This changes ranking for boosts such as recency decay and arithmetic over a missing field."

10. **NOTE (14638, the proof is stale).** `research/61-solr14638-research-note.md` (workspace root, not on the branch) says "Verification: focused edismax boost regression passed" (lines 69-79). That run covered the earlier string-wrapping version (lines 51-56: "wraps every multiplicative boost expression in def(...,1.0) before parsing"). The head is the DefFunction version, and OPEN-QUESTIONS says "Written uncompiled, not run." Replacement for that Verification section: "Not run at 44cf1c8fc80. The earlier run covered the string-wrapping version, which is no longer on the branch."

11. **NOTE (14638, the only narrowing I checked, owner Q2).** If the owner wants `query()` defaults kept, lines 550-552 could read as below. The `QueryValueSource` import removed at diff line 35 must come back. Round 11 (section 2) advises against narrowing, because field sources and `sum` would then disagree.
   ```
           ValueSource vs = subQuery(boostStr, FunctionQParserPlugin.NAME).parseAsValueSource();
           // the default score should be 1, not 0
           if (vs instanceof QueryValueSource qvs && qvs.getDefaultValue() == 0.0f) {
             vs = new QueryValueSource(qvs.getQuery(), 1.0f);
           } else if (!(vs instanceof QueryValueSource)) {
             vs = new DefFunction(List.of(vs, new ConstValueSource(1.0f)));
           }
           boosts.add(vs);
   ```

12. **NOTE (14638, API check).** `DefFunction` and `ConstValueSource` exist in this tree: `ValueSourceParser.java` on upstream/main imports both from `org.apache.lucene.queries.function.valuesource` (lines 36-37), and line 1120 calls `new DefFunction(fp.parseValueSourceList())`, so the constructor takes a `List<ValueSource>`. The branch's call matches. Lucene 9.x was not checked.

13. **NOTE (family map, for the lead).** Trial merges with `git merge-tree --write-tree` all exit 0 with no text conflicts.
   - Onto upstream/main: 3923 gives tree db1b5575a929, 7120 gives 051185e22dc6, 14638 gives 86cf76071d61.
   - 3923 with 3962: 79141a0797df in both orders. Both edit the same loop in `addPhraseFieldQueries`, and both add a helper after the method. The merged file reads coherently: 3962's `isMatchAllDocsClause` skip (line 310), then 3923's guard (line 317), then both helpers. The second branch to land must keep the guard.
   - 3923 with 4362: 616a7ec50a2 in both orders. 4362 edits the same loop (its `previousClause` and slop logic, lines 344-350). Clean.
   - 3923 with 7120 and with 14638: clean. 7120 with 14638: clean. 14638 with 6320 (549c4fcb3670 in reverse order): clean. 14638 with 2309 and with 2988 (imports): clean. 7120 with 14913 (test file): clean. 14913's 81-line test insert sits near 7120's test (994-1002 on their heads). The lead should check the order of the two test methods in the merged file.
   - Hunk regions at each head (EDP, new-side line numbers, which differ by head): 2309 imports 19-60 and 1481-1518; 2988 45, 640-643, 1471-1501; 3243 1135-1139; 3729 737-799 and 940-941; 3923 314-315 and 356-364; 3962 309-310, 319-320, 358-428; 4362 296-332 and 344-350; 6009 962, 1162-1170, 1487-1488; 6320 499-565 (foundOperators, rebuildUserQuery, and the boundary with getMultiplicativeBoosts); 7120 1733 (isolated); 12092 56 and 1492-1522; 14638 34-36 and 551-552; 14913 42 and 1182-1474.
   - Test-file regions for my three: 3923 at 1295-1306, 7120 at 994-1002, 14638 at 747-767. No other branch's test hunk is near 747 or 1295.

## Task results

**SOLR-3923 (no gate, first audit, head 723f61ee4ab). Verdict: not ready for a draft.** The branch claims that a lone "(" or ")" clause no longer goes into the pf, pf2, or pf3 phrase text. The diff matches the mechanism of the ticket's second form, `(( special_cats:string1 ) (Kitchen Sink))`, which SOLR-3923's last comment shows giving `"? ? kitchen sink"` (`research/jira-context/SOLR-3923.json`, comment 14366063). Three things hold it back. The premise has not been run (Finding 2). The guard misses signed parens (Finding 1). The behavior change needs stating (Finding 3). The test addition (lines 1295-1306) is a single block with two assertions, and assertion 2 is the only one that can fail on base. No draft.

**SOLR-7120 (no gate, first audit, head 6a434a7fc3d). Verdict: sound in shape; remove the handoff note first, then run the premise.** The code change is one line and matches the ticket. The bare RuntimeException that hides "Neither qf nor df are present." becomes a 400 with that message. The ticket (`research/jira-context/SOLR-7120.json`) asks for the unhelpful exception to go. Shawn Heisey's comment (14503066) offers two routes, rethrowing or wrapping the SyntaxError. The branch wraps it as a 400, the same status the DisMax path already gives. The test path is confirmed (Finding 7). The fail-before result is expected but not run: on base, `expectThrows(SolrException.class, ...)` sees a RuntimeException. The PR text must state the 500 to 400 change (Finding 6). No draft.

**SOLR-14638 (parked, audit only, head 44cf1c8fc80). Verdict: matches the ticket and the park note; stays parked.** The branch fixes the ticket's single case (`boost=field(field_name)` with no value scores 0 on 7.7.2), but it changes every multiplicative boost (Finding 9). The park note (Finding 8) and the receipt agree: no gate, held on the owner's side. The round 11 review ("fix-first, and the fix is a decision, not code") is current, and I agree with its scope reading. To move the branch, in order: (1) the owner chooses route 1 (the global rule, with a committer's agreement) or route 3 (a documentation paragraph on `boost=def(field(f),1)`, no Java, a different branch, as round 11 recommends); (2) if route 1, the owner answers Q2 through Q4 (Finding 11 and round 11 section 2), a reference-guide paragraph and an upgrade note are written, and the changelog title is rewritten (Finding 9); (3) the OPEN-QUESTIONS file is removed (Finding 8) and the research note is refreshed (Finding 10); (4) a gate and a fail-before run at the new head; (5) rebase, since the trial merge onto upstream/main is clean; (6) nothing is posted without the owner's authorization. No draft.

## Owner decisions

- None of the four decisions on record in the claim (3729, 6320 twice, 14913) touches 3923, 7120, or 14638.
- 14638: the park stands. The open calls are the owner's, as posed on the branch note and in round 11. These are not new questions for this round. Q1: change the default for every boost (route 1), or document only (route 3). Q2: narrow to plain field sources or not. Q3: whether `{!boost}` follows. Q4: the upgrade note and guide text, if route 1.
- 7120: no call needed. The 400 status follows the DisMax path. Raise it only if the owner wants the 500 kept.
- 3923: no call. The behavior change in Finding 3 is a fact to state, not a choice with a live alternative.

## Not checked

- No builds, tests, Gradle, `gh` calls, fetches, or posts. No gate logs exist for these three tickets (the receipts say NO GATE), so no proof numbers are cited.
- Live JIRA was not re-read. The three JSON packets in `research/jira-context/` may be older than the current ticket state.
- Premise facts not run: whether `text_sw` leaves a position gap for a "(" only token; whether a Lucene PhraseQuery prints `?` for gaps; whether the 14638 score expectations (100.0 and 1.0) hold; whether the 7120 test fails on base. Lucene behavior was not checked against Lucene source (none in this tree). Lucene 9.x and 10.x were not checked. For 14638, round 11's 10.4.0 bytecode reading was not re-verified.
- 7120: the 500 for a bare RuntimeException is the standard mapping. It was not traced through SolrDispatchFilter or SolrCall.
- Existing `TestExtendedDismaxParser` tests: a pattern search only, for pf combined with a lone paren. Not every pf test was read.
- Other callers of the ExtendedDismaxQParser constructor, beyond the files named in Finding 7, were not searched.
- The trial merges prove only that text merges. No semantic check of the merged code was run.
- The 3923 and 7120 changelog YAML was not parsed against Solr's changelog check.
- The local refs for solr-3923-submit and solr-14638-submit are stale. Origin refs were used. No fetch was run.
- No drafts were written, as assigned. Nothing was committed.
