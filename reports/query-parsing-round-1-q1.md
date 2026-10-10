# Query parsing round 1, part q1 (SOLR-4824, 9149, 11761, 12532)

Result: 9149 is draftable as is. 11761 is draftable with one Choice. 12532 is draftable with its scope stated. 4824 has a draft, but it is held for one FIX (bad `fuzzy.maxExpansions` values are expected to become server errors) and an owner call on the parameter name. No textual conflicts in the cluster; suggested landing order 9149, 11761, 12532, 4824.

Scope: read only. No build, test, gh call, fetch, commit, or post. Heads are the `origin/solr-<ticket>-submit` refs in this repo, which match the claim's live heads (76c777e4661, 151dfed119e, 41893ee9ce6, 69c06da4467). The local `refs/heads/solr-<ticket>-submit` refs are older mid-branch commits and were not used. Drafts: `pr-drafts/query-parsing/SOLR-4824.md`, `SOLR-9149.md`, `SOLR-11761.md`, `SOLR-12532.md`.

## Findings

1. **FIX (4824).** `solr/core/src/java/org/apache/solr/search/LuceneQParser.java` lines 53 to 56 pass the raw value to `Integer.parseInt` with no check.
   Evidence: lucene-core 9.12.3 and 10.4.0 both throw `IllegalArgumentException("maxExpansions must be positive.")` for a value of zero or less (javap on the `FuzzyQuery` five-argument constructor, `ifgt` check). A non-number throws `NumberFormatException`. Neither is a `SyntaxError`. `QueryComponent.java` line 191 calls `parser.getQuery()` and line 235 maps only `SyntaxError` to 400, so these values are expected to surface as a server error. Not run.
   Replacement for lines 53 to 56:
   ```java
       String fuzzyMaxExpansions = getParam(FUZZY_MAX_EXPANSIONS);
       if (fuzzyMaxExpansions != null) {
         int maxExpansions;
         try {
           maxExpansions = Integer.parseInt(fuzzyMaxExpansions);
         } catch (NumberFormatException e) {
           throw new SyntaxError(FUZZY_MAX_EXPANSIONS + " must be a positive whole number");
         }
         if (maxExpansions <= 0) {
           throw new SyntaxError(FUZZY_MAX_EXPANSIONS + " must be a positive whole number");
         }
         lparser.setFuzzyMaxExpansions(maxExpansions);
       }
   ```
   Add to `TestSolrQueryParser.java` after line 113, inside the `try` of `testFuzzyMaxExpansions`:
   ```java
         assertQEx(
             "non-numeric fuzzy.maxExpansions",
             req("q", "fuzzyexp_s:abc~1", "fuzzy.maxExpansions", "abc"),
             SolrException.ErrorCode.BAD_REQUEST);
         assertQEx(
             "zero fuzzy.maxExpansions",
             req("q", "fuzzyexp_s:abc~1", "fuzzy.maxExpansions", "0"),
             SolrException.ErrorCode.BAD_REQUEST);
   ```
   `assertQEx(String, SolrQueryRequest, ErrorCode)` exists in `solr/test-framework/.../SolrTestCaseJ4.java` line 1042. The 4824 draft's Limits bullet on invalid values must be deleted once this lands. A new focused gate is needed, because the code changes.

2. **FIX (9149, 11761, 12532). Commit subjects carry process words.** A PR shows every commit, so these are public text.
   - 9149: `c7d6e8893b7` "SOLR-9149: add hypothetical-reproduction handoff doc", `151dfed119e` "SOLR-9149: remove the handoff doc before submission".
   - 11761: `c3790836396` "SOLR-11761: add hypothetical-reproduction handoff doc", `41893ee9ce6` "SOLR-11761: Fix gate findings in the new test and the parser base class, and drop the handoff doc".
   - 12532: `4a895790075` "SOLR-12532: add hypothetical-reproduction handoff doc", `69c06da4467` "SOLR-12532: remove the handoff doc and correct the graph phrase test comments".
   The handoff files are added and then removed, so the net diff is clean. The history is not.
   Replacement: one squashed commit per ticket with these subjects: "SOLR-9149: keep phrase slop after a nested query clause", "SOLR-11761: start each parse with a new token manager", "SOLR-12532: keep query-string slop on phrases from token graphs". 4824 needs no change (`fc027cdf898`, `76c777e4661`). A squash or force push is the owner's call. A new head SHA means the draft links must be regenerated.

3. **NOTE (4824). The receipt's mismatch text reads expected first.** `solr/test-framework/src/java/org/apache/solr/JSONTestUtil.java` lines 224 and 282 format the message as `"mismatch: '" + expected + "'!='" + val`. So the receipt's `mismatch: '75'!='50'` means expected 75, got 50, at the `fuzzy.maxExpansions=200` check. The default check (expects 50) passed on base. The 4824 draft says it this way. No replacement needed.

4. **NOTE (4824). Inventory row is stale.** `branch-focus-inventory-2026-10-08.md` line 147 says "awaiting pipeline" and "(5 files total)". The receipt says gate green at 76c777e4661 (recorded 2026-10-07), and the branch diff against its merge base has 4 files.
   Replacement for the status and count in that row: "gated green, no PR" and "(4 files total)".

5. **NOTE (4824). No round 28 source on disk for the name Choice.** The assignment cites a round 28 report for 4824. There is no `SOLR-4824-review.md` in `research/branch-reviews/round-28/`. `PARKED.md` line 32 lists 4824 at snapshot `d305216518e`, which is the stale local ref, not the live head. The Choice in the draft comes from the JIRA packet (`research/jira-context/SOLR-4824.json`, comment 13665103, Jack Krupansky, 2013-05-23, which suggests `maxExpansions`/`maxFuzzyExpansions` and a higher default) and from the branch. Owner to confirm.

6. **NOTE (all four). Test counts are one above the declared methods.** The receipts say TestSolrQueryParser 38, 37, 38, 38 at the four heads. `grep "void test"` on the head files gives 37, 36, 37, 37. The same +1 appears on 15906 (39 against 38). It is probably a suite-level entry in the JUnit XML, but the XML is not on disk. The drafts use the receipt numbers. Replacement: confirm each count against the gate XML before posting, or drop the per-class count.

7. **NOTE (11761). The failing assertion is not recorded.** The receipt says "the premise then discriminated" (log `g11761-premise.log`, not on disk). The draft says only that the new test fails on the base code. The receipt also says the gated test was fixed for setup (`createParser` with null params). That is a test fix, not a behavior change, so the draft does not mention it.

8. **NOTE (11761). Framing of the bug.** `LuceneQParser.java` line 42 creates a new `SolrQueryParser` for each query. The bug needs a reused parser, which the ticket's comment says too (Steve Rowe, 2017-12-14, `research/jira-context/SOLR-11761.json`). The draft's "What happens today" says this.
   Replacement: the draft's first section as written.

9. **NOTE (11761). Live alternative for the Choice.** The branch uses the route in JIRA comment 16315707 (Kai Chan, 2018-01-08). The other route resets the count in the grammar's token manager. `QueryParserTokenManager.java` line 25 holds `commentNestingDepth`, and `ReInit(CharStream)` at line 1582 does not reset it. The grammar route means regenerating the checked-in parser. The draft's Choice section carries this.

10. **NOTE (12532). The ticket's cause text is out of date.** `research/jira-context/SOLR-12532.json` says the graph phrase becomes a `SpanNearQuery`. In lucene-core 9.12.3 and 10.4.0, `QueryBuilder.analyzeGraphPhrase` builds a `BooleanQuery` (javap, `BooleanQuery$Builder`). Replacement: do not quote the SpanNearQuery sentence. The draft says BooleanQuery.

11. **NOTE (12532). The change is wider than the ticket.** `SolrQueryParserBase.java` lines 563 to 586 (`applySlop`) walk every `BooleanQuery`. Any quoted phrase with an explicit slop whose analysis gives a BooleanQuery of phrases now takes that slop. Multi-word synonym graphs may take this path too. Not tested. The draft says so under "What this change does".

12. **NOTE (12532, 9149). eDismax is not changed.** `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java` lines 1067 to 1074 override `getFieldQuery(String, String, int)` and do not call `super`, so neither the 9149 reset (`SolrQueryParserBase.java` line 547) nor the 12532 `applySlop` runs for eDismax. The round 28 review of 12532 (`SOLR-12532-review.md`) left eDismax scope as an owner decision. Both drafts say so under Limits, with an offer.

13. **NOTE (12532). Shared test schema.** 32 test classes under `solr/core/src/test` load `schema12.xml`. The gate ran only TestSolrQueryParser. A grep found no schema field-count assertion in LukeRequestHandlerTest, PrimitiveFieldTypeTest, TestSchemaManager, ReturnFieldsTest, or SegmentsInfoRequestHandlerTest. The other 27 were not read. The draft's Limits says those classes were not run.

14. **NOTE (12532). Test document 41 is not removed.** `TestSolrQueryParser.java` line 720 adds `id 41` and never deletes it. The class already does the same with `id 40` (line 699 at this head), and no `numFound` check in the class matches doc 41 (lines 495 to 731 read). Keep as is.

15. **NOTE (9149, 12532). Changelog title style.** Both use a folded `title: >` block. This is valid YAML. The dev guide example uses a one-line title. The CI workflow `validate-changelog.yml` checks only that a file exists under `changelog/unreleased/`. Optional: make each title one line.

16. **NOTE (9149). Gate-time test edit.** Commit `ffde13713ce` changed the expected debug string from `'text:foo text:"how brown"~2'` to `'text:foo PhraseQuery(text:"how brown"~2)'`. The new form matches the base output recorded in the receipt, so the Proof holds. No change needed.

17. **NOTE (cluster). Textual merges are clean.** `git merge-tree --write-tree` finds no conflicts for any of the six pairs of the four tickets. Both orders were checked for 9149 with 12532 and 11761, and for 4824 with 11761. Each of the four merges cleanly onto `upstream/main` `8e62c268688`. 9149 and 12532 both edit `getFieldQuery(String, String, int)` (lines 545 to 547 and 551 at 12532). The merged method reads correctly: reset line, then the `applySlop` call. Pairs with 12608 and 15906 are also clean (q6 and q7 cover them).

## Task results

### Cluster: interactions and landing order

No production-code conflicts and no test-class conflicts, textual or by reading. Hunks per branch:
- 4824: `SolrQueryParserBase.java` line 143 (field), lines 355 to 366 (accessors), lines 677 to 683 (`newFuzzyQuery`); `LuceneQParser.java`; `TestSolrQueryParser.java` lines 87 to 118 (new method).
- 9149: `SolrQueryParserBase.java` lines 545 to 547 (reset in `getFieldQuery` with slop); `TestSolrQueryParser.java` lines 298 to 301 (new assertion in `testNestedQueryModifiers`).
- 11761: `SolrQueryParserBase.java` lines 238 to 245 (new `ReInit` overload) and 280 to 283 (`parse`); `TestSolrQueryParser.java` lines 545 to 559 (new method).
- 12532: `SolrQueryParserBase.java` lines 545 to 556 and 563 to 586 (`applySlop`); `TestSolrQueryParser.java` lines 718 to 732 (new method); `schema12.xml` (shared).

Pair by pair: 9149 and 12532 share one method but not one hunk (production overlap only in the same method, merged cleanly and read correctly). 9149 and 11761, 11761 and 12532, 4824 and any other: separate hunks, no conflict. Test class: all four add separate methods or one assertion, so only the shared class is touched, with no overlap.

Suggested landing order: 9149 (smallest, one reset line), then 11761 (adds the public `ReInit` overload), then 12532 (widest behavior change, eDismax decision open), then 4824 last (new public names and a parameter name still under review). Any order merges cleanly, so this is for review load and risk only.

### Per ticket

**SOLR-4824. Verdict: held.** Draft at `pr-drafts/query-parsing/SOLR-4824.md`, written against 76c777e4661. Receipt: gate green, TestSolrQueryParser 38 of 38, base fails with one failure (see Finding 3). Lucene check: `FuzzyQuery.defaultMaxExpansions` is 50 in 9.12.3 and 10.4.0, and the three-argument constructor passes 50 and `true` in both, so the five-argument call with the default is the same call. Main pins Lucene 10.4.0 (`gradle/libs.versions.toml` line 39). The draft's Choice is the parameter name (`fuzzy.maxExpansions` against the ticket's `maxFuzzyExpansions`) and keeping the default at 50. Hold until FIX 1 lands and the owner settles the name. Limits: edismax and other parsers keep 50; which 50 terms are kept is not tested; invalid values until FIX 1.

**SOLR-9149. Verdict: draftable as is.** Draft at `SOLR-9149.md`, written against 151dfed119e. Receipt: gate green, 48 of 48 focused tests, TestSolrQueryParser 37 of 37, base fails in `testNestedQueryModifiers` (base debug output has no `~2`). Production change is one reset line with a comment. No Choice section: this is a bug fix with no live alternative. Limits: eDismax overrides the method (Finding 12). Clear the commit subjects first (Finding 2).

**SOLR-11761. Verdict: draftable, with the Choice section.** Draft at `SOLR-11761.md`, written against 41893ee9ce6. Receipt: gate green, 89 of 89 (TestSolrQueryParser 38, TestExtendedDismaxParser 39, SolrQueryParserBaseTest 7, TestReversedWildcardFilterFactory 5). The fail-before run is recorded only as "the premise then discriminated," so the draft says only that the test fails on base (Finding 7). The draft's first section states that the bug needs a reused parser (Finding 8). Clear the commit subjects first (Finding 2).

**SOLR-12532. Verdict: draftable, with the scope stated.** Draft at `SOLR-12532.md`, written against 69c06da4467. Receipt: gate green, TestSolrQueryParser 38 of 38, 0 skipped; base fails `testQueryStringSlopOnGraphPhrase` with 1 expected and 0 found. The draft uses BooleanQuery, not the ticket's SpanNearQuery (Finding 10), says the change is wider than word delimiter graphs (Finding 11), and says eDismax is not changed (Finding 12). No Choice section: the eDismax question is an owner decision, so it sits in Limits. Clear the commit subjects first (Finding 2).

## Owner decisions

1. Approve FIX 1 (4824 input check and two `assertQEx` lines), then run a new focused gate before 4824 is posted.
2. 4824 parameter name (`fuzzy.maxExpansions` or `maxFuzzyExpansions`) and whether the default stays at 50.
3. 11761 route: keep the per-parse token manager (the route the ticket commenter proposed), or reset the count in the grammar and regenerate the parser.
4. 12532: take eDismax into this PR or as a follow-up, and accept the wider phrase behavior (Finding 11).
5. Squash the commit history of 9149, 11761, and 12532 with the subjects in Finding 2, which changes the head SHAs and the draft links.
6. Approve the landing order: 9149, 11761, 12532, 4824.
7. Confirm the TestSolrQueryParser counts against the gate XML before any of the four is posted (Finding 6).
8. Update the 4824 row in the inventory (Finding 4).

## Not checked

- Gate logs, JUnit XML, and queue rows for the four tickets are not on disk. `research/test-queue/queue.json` has no row for them. Every proof number comes from the receipt, and the dates in the drafts are the receipts' recorded dates (2026-10-07, 2026-10-05, 2026-10-05, 2026-10-04).
- FIX 1 behavior (server error instead of 400) is by reading the code. It was not run.
- Lucene checks used javap on the lucene-core 9.12.3 and 10.4.0 jars in the Gradle cache. They confirm constructors, constants, and the builders used. They do not confirm which 50 terms a fuzzy query keeps.
- The failing assertion in the 11761 fail-before run.
- 27 of the 32 `schema12.xml` test classes.
- Where the `PhraseQuery(...)` wrapper in the 9149 debug string comes from. The expected string passed at the gate, so I did not trace it.
- JIRA content came from the local packets in `research/jira-context/`, not a live fetch. The local copies may be older than the tickets.
- The round 28 reviews of 9149, 11761, and 12532 are read. They have no test results. Their verdict is "Close," which the round 28 README defines as correct with minor items left.
- Changelog YAML parsing with logchange was not run.
- 12608 and 15906 were checked only for textual merges with this cluster (q6 and q7 own them).
