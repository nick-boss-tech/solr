# q7 report: SOLR-15906 (QParser auto-fix), SOLR-16267, SOLR-17280

Result: SOLR-15906 is HELD (two FIX items that break existing tests, found by reading code, not run). SOLR-16267 is DRAFTABLE. SOLR-17280 is DRAFTABLE after one changelog FIX.

Heads checked against the claim table: origin/solr-15906-submit 50139a8e979, origin/solr-16267-submit 8f4b0c6d2fb, origin/solr-17280-submit 40817c5cb7e. All three match. Nothing was built, run, fetched, or posted. No gh calls.

## Findings

**1. FIX (SOLR-15906, blocks any draft). Sort and fl specs now go to the lucene parser.**
- Where: `solr/core/src/java/org/apache/solr/search/QParser.java` line 495 (the new branch is at 495-503).
- Evidence: `SortSpecParsing.java:112` calls `QParser.getParser(funcStr, FunctionQParserPlugin.NAME, optionalReq)`. A sort spec such as `{!func v=$sortfunc} desc` has trailing text " desc", so the new branch runs. The parser then has `localParams == null`. `SortSpecParsing.java:214` asserts `parser.getLocalParams() != null`. Tests run with assertions on (`gradle/testing/randomization.gradle:91`). Without assertions, `SortSpecParsing.java:215` sets `sp.pos = start + localParamsEnd`, which is start minus 1. The fl path has the same shape (`SolrReturnFields.java:395-397`).
- Existing tests that pass on base and depend on this path: `TestFunctionQuery.java` lines 396-403 and 411-423 (testGeneral, starts at line 213), and lines 733, 748, 766 (testSortByFunc, starts at line 660). The 15906 receipt ran TestSolrQueryParser only.
- Exact replacement, QParser.java line 495:
```java
      if (val != null
          && !FunctionQParserPlugin.NAME.equals(parserName)
          && isFurtherQueryText(qstr.substring(localParamsEnd))) {
```
  `parserName` is the caller's default parser at that point (set at lines 481-483 and not changed before 495). `FunctionQParserPlugin.NAME` is "func" (`FunctionQParserPlugin.java:28`), and QParser.java already uses it at line 395.

**2. FIX (SOLR-15906, blocks any draft). A stray closing parenthesis after a v block now throws.**
- Where: `QParser.java` lines 411-414 (`isFurtherQueryText`).
- Evidence: trailing ")" has no blank check, so it goes to the lucene parser. `TopLevelQuery` needs `Query <EOF>` (`solr/core/src/java/org/apache/solr/parser/QueryParser.jj` lines 201-208), so a lone ")" is a parse error. Base ignores it. Tests with a stray ")" that pass on base and break on this head: `SOLR749Test.java` lines 128, 140, 156 (fq strings ending in `})`), and `TestNestedUpdateProcessor.java` lines 782 and 796 (q strings ending in `})`).
- Exact replacement, QParser.java lines 412-414:
```java
    if (trailing.isBlank() || trailing.strip().chars().allMatch(c -> c == ')')) {
      // only closing parentheses: not query text, stays ignored as before
      return false;
    }
```
  The other option is in Owner decisions.

**3. FIX (SOLR-15906, public text). The changelog title overstates.**
- Where: `changelog/unreleased/SOLR-15906-local-params-v-trailing-text.yml` lines 1-3.
- Evidence: text attached to the closing brace with no space, parenthesis, brace or caret is still ignored (`QParser.java` 411-426 returns false when there is no separator). `TestSolrQueryParser.java` line 233-234 pins `{!v=$qq}foo` as ignored (expects doc 1).
- Exact replacement for the title block:
```yaml
title: >
  A query such as {!parser v=$qq} OR other no longer silently ignores the text after the local-params when v is given and
  that text is set off from the closing brace by a space, a parenthesis, a brace or a caret; the whole string is parsed by
  the lucene query parser.
```

**4. NOTE (SOLR-15906, proof wording).** The second new test, `testLocalParamsSuffixesAreNotFurtherQueryText` (`TestSolrQueryParser.java` lines 216-235), passes on base too. The receipt's only base failure is `testTextAfterLocalParamsWithExplicitValueIsNotIgnored` (receipt line 7). The second test guards the recursion fix only. Any draft must say that.

**5. NOTE (SOLR-15906, gate scope).** The receipt covers TestSolrQueryParser 39 of 39 only (receipt line 6). Findings 1 and 2 touch TestFunctionQuery, SOLR749Test and TestNestedUpdateProcessor, which no receipt covers. A focused run of all four classes is needed after the fixes. The owner decides when.

**6. NOTE (SOLR-15906 and its neighbors, the auto-fix thread).** Nothing changes in SOLR-8977 or SOLR-12212.
- 8977 changes `GraphQueryParser.java` and `GraphQueryTest.java`. Its v blocks in `BJQParserTest.java` (for example lines 203, 467, 489) are followed by a quote or nothing, so no trailing text reaches the new branch. `GraphQueryTest` has no v block.
- 12212's new tests (its diff hunk at line 568 of `TestSolrQueryParser.java`) call `QParser.getParser("(NOT(eee_s:(Y)))", req)`. They have no `{!` prefix, so the new branch never runs for them.
- `autoFixPureNegative` is set from luceneMatchVersion at `QParser.java` lines 113-118. The new branch does not change it.
- Tip-to-tip textual merges with 8977, 12212, 4824, 9149, 11761, 12532, 12608, 16267 and 17280 are clean (`git merge-tree --write-tree`, exit 0). 15906 and 12212 both add tests to `TestSolrQueryParser.java` at different places.

**7. NOTE (SOLR-15906, branch and inventory).** The inventory row says "gated, no PR" with "(4 files total)" (`branch-focus-inventory-2026-10-08.md` line 163). The live diff has 3 files: the changelog, `QParser.java` and `TestSolrQueryParser.java`. The local ref `refs/heads/solr-15906-submit` is `fd7ae2fe490`, four commits behind `origin/solr-15906-submit`. The local ref is stale. I did not change anything.

**8. NOTE (SOLR-15906 and SOLR-17280, commit subjects).** These public commit subjects carry internal wording: "add hypothetical-reproduction handoff doc" (15906 `fd7ae2fe490`; 17280 `2f4339eaf21`), "remove the hypothetical handoff doc" (15906 `50139a8e979`), and "remove the handoff doc and note the nested-range caching loss..." (17280 `40817c5cb7e`). The public text rule covers commit subjects too. Owner decision: squash or rewrite before any PR opens.

**9. NOTE (SOLR-17280, inventory and stale ref).** The inventory row says "(4 files total)" (`branch-focus-inventory-2026-10-08.md` line 167). The live diff has 3 files. The local ref `refs/heads/solr-17280-submit` is `2f4339eaf21`, which is stale. The live head is `origin/solr-17280-submit` at `40817c5cb7e`.

**10. FIX (SOLR-17280, changelog scope). The changelog says "inside another filter", which is too narrow.**
- Where: `changelog/unreleased/SOLR-17280-range-query-recursive-cache-update.yml` lines 1-4.
- Evidence: the `put` is removed for every caller in `getSegState` (`SolrRangeQuery.java` lines 482-489). The threshold is 16 terms (`SolrRangeQuery.java` line 372). Only the filter path caches through `getAndCacheDocSet` (`SolrIndexSearcher.java` lines 1007-1008). So a range query used as a clause of `q`, or of any larger query, also loses its side-effect cache entry. A range query used directly as a filter is still cached unless `cache=false`.
- Exact replacement for the title block:
```yaml
title: >
  SolrRangeQuery no longer puts its DocSet into the filterCache from inside another cache computation, which could
  fail with "IllegalStateException: Recursive update". A range query that is not used directly as a filter, such as a
  clause of q, is no longer cached on its own as a side effect. A range query used directly as a filter is still cached
  by the searcher unless it has cache=false.
```

**11. NOTE (SOLR-17280, design choice).** Round 28 finding 1 (`research/branch-reviews/round-28/SOLR-17280-review.md`) asked the owner to confirm the cache tradeoff. The JIRA thread names a live alternative: keep the put and detect recursion (the PR 1481 approach). The assignment calls the tradeoff Limits material. The formula's bar for a choice section looks met. The draft has both a choice section and a Limits line.

**12. NOTE (SOLR-17280, test wording).** The test Javadoc (`TestFiltering.java` lines 109-114) describes the exception. The test does not reproduce it (round 28 finding 2). The draft says so. No code change needed.

**13. NOTE (SOLR-16267, head evidence).** The queue records on disk point to a different commit. `research/test-queue/results/SOLR-16267.json` has headSha `67ffcb2b63f` (merge base `56ec140e`). `SOLR-16267.failbefore.json` has verdict PASS at the same commit, with failures in testStats and testStatsDistrib (`mismatch 1.0 != 0.333 @ facets/a1`). The three changed files are identical at `67ffcb2b63f` and `8f4b0c6d2fb` (`git diff` is empty), so the code under test matches. The receipt's log `g16267-harden.log` is not on disk. The draft uses only receipt numbers. The owner should confirm the receipt head before posting.

**14. NOTE (SOLR-16267, coverage).** The changelog title names `exists()`, `def()` and `percentile` (`changelog/unreleased/SOLR-16267.yml` line 1). None has a direct assertion (round 28 finding 1). The draft's Limits says so. Owner decision in the list below.

**15. NOTE (SOLR-16267, def()).** The changelog says `def()` sees no value. `def` is Lucene's `DefFunction` (`ValueSourceParser.java` lines 37 and 1115-1122). Its source is not in this tree, so I did not confirm that it calls `exists()`. Check before posting.

## Task results

**SOLR-15906: HELD.** The design fits the ticket. A query with an explicit v followed by more query text goes to the lucene parser, and the suffix guard stops the recursion. At head 50139a8e979, two FIX items break existing tests (findings 1 and 2), and the title overstates (finding 3). The receipt's 39 of 39 covers TestSolrQueryParser only. No draft is written. Once findings 1 to 3 are in and the four test classes are green in a focused run, a draft can follow. Its Proof must say the suffix test passes on base (finding 4).

**SOLR-16267: DRAFTABLE.** Draft: `pr-drafts/query-parsing/SOLR-16267-draft.md`, written against head `8f4b0c6d2fb0c89c833512de502f39eb1bec560e`. Counts from the receipt: TestJsonFacets 30 of 30, TestFunctionQuery 23 of 23. Limits names `exists()`, `def()` and `percentile` (finding 14). Confirm findings 13 and 15 before posting. The branch merges cleanly with upstream main tip to tip, with no landing conflict.

**SOLR-17280: DRAFTABLE after finding 10.** Draft: `pr-drafts/query-parsing/SOLR-17280-draft.md`, written against head `40817c5cb7ec96b6f4119a46bec3db8ae6a1f6de`. Proof: TestFiltering 5 of 5 in the focused run, which is the whole class (it has five test methods). On base the same run has one failure, the new test. The draft has a choice section (finding 11) and says the exception is not reproduced directly. Before posting: fix the changelog (finding 10), decide on the handoff commit subjects (finding 8), and re-check the PR 1481 status, which comes from the round 28 review and was not re-checked here.

**Interactions.** SOLR-16267 and SOLR-17280 touch different files from each other and from 15906, and their tip-to-tip merges are clean. Landing order: 16267 and 17280 in any order; 15906 after findings 1 to 3 are fixed. 8977 and 12212 do not change tests or framing when 15906 lands (finding 6).

## Owner decisions

1. SOLR-15906, stray ")" (finding 2): keep it ignored (recommended, the replacement above, five test inputs unchanged), or make it a syntax error and edit the five test inputs in `SOLR749Test.java` and `TestNestedUpdateProcessor.java`. The second option is a visible behavior change.
2. SOLR-15906, gate: after findings 1 to 3, run a focused gate on TestSolrQueryParser, TestFunctionQuery, SOLR749Test and TestNestedUpdateProcessor before any draft.
3. SOLR-15906 and SOLR-17280, commit subjects with handoff wording (finding 8): squash or rewrite before the PRs open.
4. SOLR-17280: keep the choice section (drafted) or move it to Limits only, as the assignment wording suggests.
5. SOLR-16267: add direct assertions for `exists()`, `def()` and `percentile` and re-gate, or submit with the Limits line.
6. Inventory: correct the "4 files total" counts for SOLR-15906 and SOLR-17280, and refresh the stale local refs (findings 7 and 9).

## Not checked

- No builds, tests, JUnit XML, or gh calls. Findings 1 and 2 come from reading the code and grammar, not from a run. The assertion setting is from `gradle/testing/randomization.gradle:91`.
- Lucene `DefFunction` (finding 15) and the state of PR 1481 (finding 11, from the round 28 review) were not re-verified.
- The gate logs named by the receipts (`g15906r35-gate.log`, `g16267-harden.log`, `g17280r35-gate.log`) are not on disk under those names. No 15906 or 17280 log is in `research/test-queue/logs` or `windows-gate-results`. I did not read the timestamped 16267 logs for numbers.
- The receipt's 39 count for TestSolrQueryParser was not reconciled with the 38 `public void test` methods in the file.
- Sweeps used grep for a v local-params block followed by text in core tests, contrib, modules, solrj tests and core main code. Strings built by concatenation were not swept.
- Local `upstream/main` is `8e62c268688`, not fetched. Merge checks are tip to tip, not rebased.
- Jira text came from the packets in `research/jira-context`. It was not refetched.
- The claim that a range query used as a clause of `q` loses its cache entry (finding 10) comes from reading `SolrRangeQuery.java` and `SolrIndexSearcher.java`, not from a run.
