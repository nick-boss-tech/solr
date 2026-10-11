# Query parsing draft fidelity, slice 4

Assignment: `assignments/query-parsing-round-1.md` (commit 9bfcb2ff1c1). Claim: `claims/query-parsing-round-1.md` (commit c87876a40f2). Round roll-up: `reports/query-parsing-round-1.md`. Category parts checked: `reports/query-parsing-round-1-q1.md`, `-q2`, `-q5`, `-q6`, `-q7` (q8 only for the 17796 pairing). Formula: `pr-formula.md`.

Slice drafts (all in `pr-drafts/query-parsing/`): SOLR-17311, SOLR-17796, SOLR-4824, SOLR-6014, SOLR-874.

Worktree HEAD: d627304e96b, as the lead named it. Receipts present for all five: `receipts/SOLR-17311.md`, `SOLR-17796.md`, `SOLR-4824.md`, `SOLR-6014.md`, `SOLR-874.md`.

Checks run: read-only git (`ls-remote`, `show`, `cat-file`, `merge-base`, `diff --stat`), Read and Grep, the Jira packets under `research/jira-context/`, and one read-only `gh run view` (see SOLR-6014). No build, test, Gradle, fetch, commit, push, PR, comment, or Jira call. No draft, receipt, claim, assignment, or other report was edited.

Material: a grep of `material/` for 17311, 17796, 4824, 6014 and 874 found no hits. No answers material exists for this slice, so limits and choices were checked against the round 1 reports only.

Merge bases used for pre-change citations: SOLR-17796 is 56ec140e3636d5f4150fa87fbf7103529536ac99; SOLR-874 is cabedd1d968059215188f4e7563fb303241899ed.

## Verdicts

| Draft | Head checked (`ls-remote origin refs/heads/solr-<n>-submit`) | Verdict |
|---|---|---|
| SOLR-17311 | 9f7524582f9de4d6931779bc31811013c414a45f (match) | CONSISTENT |
| SOLR-17796 | 661165d2673de9211846e276c05daf1fd0e879d6 (match) | DRIFT (2 items) |
| SOLR-4824 | 76c777e4661e9240ac66dd01247c0dd1286cacff (match) | CONSISTENT (held by round 1: FIX 1 and the parameter-name call) |
| SOLR-6014 | 505849d2d4e7d8699a2fe34d410773b8ecf3a55a (match) | DRIFT (4 items) |
| SOLR-874 | ac9ab33753751a6fef05b5f100f5c1c5683510b2 (match) | DRIFT (4 items) |

Across all five: no em dashes, and no internal process vocabulary in the draft text. The one run identifier (SOLR-6014) is covered below. Every draft has the AI header and footer, and a verification date except SOLR-6014. The changelog links for SOLR-4824, SOLR-6014 and SOLR-874 point at suffixed fragment names (for example `SOLR-4824-fuzzy-max-expansions.yml`) rather than the bare `SOLR-<n>.yml` pattern. Each file exists at its head with a matching title, so these are treated as conforming. Renaming them would be a branch change.

## SOLR-17311

Verdict: CONSISTENT.

Checked: head matches. `changelog/unreleased/SOLR-17311.yml` exists at head and its title matches the draft's symptom and scope. `ChildFieldValueSourceParser.java` lines 63-67 (`setTopValue`) hold the null pass-through the draft describes. Line 132-133 limits the wrapper to `Type.STRING`. The new test `testCursorMarkPagingOverMissingChildValue` is at `TestNestedDocsSort.java` lines 107-145. Proof "12/12" and "verified 2026-10-04" match `receipts/SOLR-17311.md` lines 6-7. The Lucene `TermOrdValComparator` claim matches q5 finding 11 (javap on lucene-core 10.4.0 and 9.12.3).

Notes, not blocking:
- The live-head NPE failure line is not on disk. The only on-disk fail-before output is from superseded head bb964d8 (q5 finding 10). The receipt records only that the pre-fix check passed at this head. Round 1 owner decision 7: confirm before posting.
- The test method at `TestNestedDocsSort.java` line 107 has no `@Test` annotation (q5 finding 12). It still runs under the randomized runner. Optional branch-side fix; no draft change.

## SOLR-17796

Verdict: DRIFT (2 items).

1. Draft says: "Under `q.op=AND`, the parser wraps the lone required clause in a BooleanQuery. The wrapper asks the clause for a Weight. [CollapsingPostFilter](https://github.com/nick-boss-tech/solr/blob/661165d2673de9211846e276c05daf1fd0e879d6/solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java#L269) does not implement `createWeight`, so the request fails."
   - Evidence: this is pre-change symptom code. The branch changes four files (`git diff --stat 56ec140e36 661165d`): the changelog, `QueryParser.jj`, `QueryParser.java`, `TestCollapseQParserPlugin.java`. `CollapsingQParserPlugin.java` is not among them. The class is at line 269 at the merge-base too, and it has no `createWeight` there. `pr-formula.md` says pre-change symptom citations link the merge-base commit, and the text says so.
   - Replacement: "Under `q.op=AND`, the parser wraps the lone required clause in a BooleanQuery. The wrapper asks the clause for a Weight. [CollapsingPostFilter](https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java#L269), as it is at the merge-base, does not implement `createWeight`, so the request fails."

2. Draft says: "- A negated collapse, such as `-{!collapse field=x}`, is still wrapped as before. It has no test here."
   - Evidence: q2 finding 5. A negated `CollapsingPostFilter` takes the same wrapper path that throws in the ticket. q2 says to add a negated collapse case to a run before posting, and that "A Limits line is not enough." Round roll-up owner decision 5 is still open. The draft states the case only as a Limits line and does not say the same exception may recur.
   - Replacement: "- A negated collapse, such as `-{!collapse field=x}`, is still wrapped as before, so the same `UnsupportedOperationException` may still occur there. It has no test here."
   - Hold: run a negated collapse case, or get the owner's decision, before posting. If the case passes, restate this bullet and name the new test.

Notes, not blocking:
- The round 1 comment fix (q2 finding 4) is still pending: `QueryParser.jj` lines 236-237 and `QueryParser.java` lines 251-252. It moves the head, so every head link and the changelog link must be regenerated. The current comment says PostFilters do not support Weight evaluation. That is too broad (`FunctionRangeQuery` has `createWeight`), so the draft must not repeat it.
- The header framing matches the Jira body, not its title, as the receipt asks. The body says the filter works with `q.op OR` and fails with `q.op AND` (Jira packet line 4).
- Proof "21 of 21" is receipt-only. The source has 17 `@Test` lines (q2 finding 12). Confirm from the JUnit XML before posting.
- The multi-clause Limits bullet matches q2 finding 6, which was checked by reading only.
- Formula presentation rule: the Limits section has no bold one-line summary. Suggested opening line: "**Only a filter with one clause is covered.**"

## SOLR-4824

Verdict: CONSISTENT (held by round 1).

Checked: head matches. `LuceneQParser.java` lines 53-56 read the parameter. `SolrQueryParserBase.java` lines 677-683 (`newFuzzyQuery`) pass `getFuzzyMaxExpansions()` to the five-argument `FuzzyQuery` constructor. `TestSolrQueryParser.java` lines 87-118 are `testFuzzyMaxExpansions`. The receipt's count of 38 of 38 and its base failure `mismatch: '75'!='50'` match the draft. q1 finding 3 explains that the message lists expected first, so the draft's reading is right. The Lucene default of 50 and the three-argument constructor claim match q1 (9.12.3 and 10.4.0 both checked). The Limits bullet on invalid values matches the code at head.

Notes, not blocking:
- Held by round 1: FIX 1 (q1 finding 1). Non-numbers and values of zero or less must return a 400, not a server error. When FIX 1 lands, delete the Limits bullet that begins "A value that is not a whole number", and re-check the head. Round 1 owner decision 2 (parameter name and default) is still open. The Choice section names a real alternative, so it is kept.
- The count is 38 of 38 by receipt. q1 finding 6 counts 37 `void test` methods by grep and asks for the gate XML to be checked before posting.
- The Jira comment also suggested `maxExpansions` and a solrconfig setting (Jira packet line 59). The Choice names only `maxFuzzyExpansions`. Optional wording change.

## SOLR-6014

Verdict: DRIFT (4 items).

1. Draft says: "A CI run of the same class also passed at this head (run 37639870647)."
   - Evidence: a read-only `gh run view 37639870647 --repo nick-boss-tech/solr --json` returned headSha 8a631ed70f0b433f25b2073964680ae98590a02f, headBranch `ci/6014-dismax-stopword-r28`, conclusion success, workflow "Fork test runner". That commit is a child of the live head 505849d2d4e7 and differs from it only by removing `.github/workflows/fork-test-runner.yml` (`git diff --stat`). So the run did not run at this head. The run number is also an internal CI artifact. The receipt (line 8) carries the same "at this head" claim.
   - Replacement: delete the sentence "A CI run of the same class also passed at this head (run 37639870647)."

2. Draft says: "At head `505849d2d4e7d8699a2fe34d410773b8ecf3a55a`, the class passes 39 of 39."
   - Evidence: no verification date. `receipts/SOLR-6014.md` line 4 (pushed 2026-10-07) and line 10 (recorded 2026-10-07). The SOLR-4824 draft uses the same date convention.
   - Replacement: "At head `505849d2d4e7d8699a2fe34d410773b8ecf3a55a`, verified 2026-10-07, the class passes 39 of 39."

3. Draft says: "- A nested or top-level stopword-only `{!dismax}` clause that also sets a `cache` or `cost` local parameter is not covered by the new checks."
   - Evidence: q6 FIX 4. `QParser.java` `getQuery` (line 216, `if (localParams != null) {`) wraps the query with `extendedQuery` even when it is null. `WrappedQuery` accepts null. The likely result is a failure at search time, where the base matched nothing. Round 1 holds 6014 for FIX 4 and says to delete this bullet once FIX 4 lands.
   - Replacement: "- A nested or top-level stopword-only `{!dismax}` clause that also sets a `cache` or `cost` local parameter is not fixed by this change. It is likely to fail at search time instead of matching nothing."
   - Hold: if FIX 4 lands first, delete this bullet and add the FIX 4 test from q6.

4. Draft says: "- A stopword-only `{!dismax}` clause used as a filter query (`fq`) is not covered by the new checks."
   - Evidence: q6 NOTE 5. `QueryUtils.parseFilterQueries` (lines 284-287) adds `fqp.getQuery()` with no null check, so a stopword-only `fq` probably reaches a null filter (traced, not run). Round roll-up owner decision 10 keeps the bullet until the owner decides.
   - Replacement: "- A stopword-only `{!dismax}` clause used as a filter query (`fq`) is not covered by the new checks. Reading the code, it may fail at search time instead of matching no documents."

Notes, not blocking:
- The changelog title (line 1 at 505849d) says the nested stopword clause is "dropped like an empty clause". The cache and cost case (q6) does not match that wording. Revisit with FIX 4.
- No Choice section. q6 found no live alternative, so this is correct.
- The dismax-only scope and the edismax bullet match q6 NOTE 11.
- Formula presentation rule: the Limits section has no bold one-line summary. Suggested opening line: "**The change covers the dismax parser only, and three edge cases are not covered.**"

## SOLR-874

Verdict: DRIFT (4 items).

1. Draft says: "**A trailing or leading AND, OR, NOT, && or || makes the dismax parser fail with a parse error.**"
   - Evidence: the draft's own body says a leading NOT is valid ("A leading NOT is kept, because `NOT ipod` is a valid query"). At the merge-base `SolrPluginUtils.java` (cabedd1d968, lines 651-654) there is no leading-boolean pattern, so a leading NOT is not an error at base. Only a leading AND, OR, && or || fails.
   - Replacement: "**A trailing AND, OR, NOT, && or || and a leading AND, OR, && or || makes the dismax parser fail with a parse error.**"

2. Draft says: "The cleanup step in [SolrPluginUtils.java lines 658-663](https://github.com/nick-boss-tech/solr/blob/ac9ab33753751a6fef05b5f100f5c1c5683510b2/solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java#L658-L663) removes dangling + and - signs."
   - Evidence: this is pre-change behavior. The merge-base has the same +/- cleanup at lines 651-654. The branch edits the method, but this sentence describes base. `pr-formula.md` says pre-change citations link the merge-base commit, and the text says so.
   - Replacement: "The cleanup step in [SolrPluginUtils.java lines 651-654 at the merge-base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java#L651-L654) removes dangling + and - signs."

3. Changelog title (branch file, `changelog/unreleased/SOLR-874-dismax-dangling-boolean-operator.yml` line 1 at ac9ab337). It reads "a trailing or leading AND/OR/NOT/&&/|| ...", which includes a leading NOT. The code has no leading NOT (`LEADING_BOOL_PATTERN`, lines 649-650), and the draft says a leading NOT is kept. Source: q6 FIX 3. This is a branch-file change, not a draft change.
   - Replacement (changelog line 1, branch): `title: "dismax: a trailing AND, OR, NOT, && or || and a leading AND, OR, && or || in the user query (for example q=ipod AND) is dropped instead of causing a parse error"`

4. Draft Limits (after "- Operator-only queries, such as `q=AND`, still return a parse error (see the second choice above).") omits two gaps that q6 traced at head.
   - Evidence: q6 FIX 1. `ipod - AND` leaves `ipod -`, and `ipod AND - OR` leaves `ipod AND -`. Both are parse errors, traced from code. q6 FIX 2. `!` is a NOT spelling (`QueryParser.jj` line 138), but it is not in `DANGLING_BOOL_PATTERN` (`SolrPluginUtils.java` line 647), so `ipod !` still fails.
   - Replacement: add after the operator-only bullet: "- A boolean word after a sign at the end, such as `ipod - AND` or `ipod AND - OR`, still returns a parse error. A trailing `!`, the other spelling of NOT, such as `ipod !`, also still returns a parse error."
   - Hold: q6 FIX 1 to 3 are branch changes, and the draft is held until they land and a focused run is done. When they land, delete the two new bullets, add `!` to the "What this change does" summary, and update the Proof counts.

Choice section: CONSISTENT. Both choices are real alternatives, and they match q6 owner decisions 2 and 3. The round 28 report for this ticket is not on disk, so the choices are proposals (q6 NOTE 8). The owner should confirm them.

Proof: counts match `receipts/SOLR-874.md` lines 6-7 (SolrPluginUtilsTest 10 of 10, DisMaxRequestHandlerTest 4 of 4, one base failure in each). The date "recorded 2026-10-06" is present. The formula template wants "verified <date> at this head", so this is an optional wording change. The Proof counts will change after FIX 1 to 3.

Notes, not blocking:
- The "What this change does" link to `DisMaxQParser.java#L195` is unchanged code (the same call is at line 195 at the merge-base). A merge-base link would fit the rule. Optional.
- Choice 1 says the escaped words "usually match nothing". That is not tested. The Jira packet (line 51) suggests they fail to match only when they are stopwords. Optional softer wording.
- Formula presentation rule: the Limits section has no bold one-line summary. Suggested opening line: "**Operator-only queries and the edismax parser are not changed.**"

## Not done

- No build, test, Gradle, fetch, commit, push, PR, comment, or Jira call. The only live GitHub call was the read-only `gh run view 37639870647`.
- Not checked: the 874 round 28 report (not on disk), gate and premise logs, and JUnit XML. Every count is receipt-only.
- Lucene 9.x and 10.x claims were checked through the q1 and q5 javap results, not rechecked here.
- The 17311 live-head NPE failure line is not on disk. Only the superseded head's output exists.
- The brief's claim commit e84522fa5bc was not used to confirm worktree identity. HEAD is d627304e96b, as the lead named it.
