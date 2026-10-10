# Receipt refresh round 1, part r1: SOLR-10403 and SOLR-11391

Result: SOLR-10403 is HOLD with no draft: the one-hop helper proof is sound, but the ticket's own case still rounds to whole USD cents between two conversions. SOLR-11391 is HOLD, with a HOLD draft at `pr-drafts/query-parsing/SOLR-11391.md`: the refreshed receipt does not change the ticket mismatch.

Written from the subagent's hand-back text, which was not saved to a file by the subagent. Heads checked read-only: `solr-10403-submit` at `d8e03755cb5` and `solr-11391-submit` at `b0f15a22856`, both matching the claim table.

## Findings

1. **FIX (10403, hold).** The ticket case still rounds the intermediate value.
   - Files: `solr/core/src/java/org/apache/solr/schema/CurrencyFieldType.java` lines 686-687 (`RawCurrencyValueSource.longVal`, the first conversion to the default USD minor unit, rounded in `CurrencyValue.java` line 146) and lines 477-479 (`ConvertedCurrencyValueSource.doubleVal`, the second conversion, reading `amounts.longVal`). The chain is wired by `solr/core/src/java/org/apache/solr/search/ValueSourceParser.java` line 526 (`currency(field, code)`).
   - Evidence: the ticket packet `research/jira-context/SOLR-10403.json` says a stored 2554.55 KRW with rate 1116.071429 gives `currency(price,'KRW') = 2544`. The stored value is 2555 KRW (`CurrencyValue.parse` line 77 rounds to 0 digits). On base: 2555 KRW to 228 cents (truncated), then 228 × 1116.071429 / 100 = 2544.64, truncated to 2544, which matches the ticket. On the branch: 2555 to 229 cents, then 229 × 1116.071429 / 100 = 2555.80, rounded to 2556. The receipt's proof is one `convertAmount` call (`CurrencyFieldTypeTest.java` lines 151-157), so it never runs `currency()`.
   - Replacement for `receipts/SOLR-10403.md` line 8, last sentence "The premise discriminates.": "This proves the one-hop helper only. The ticket's case, `currency(price,'KRW')` with a USD default, converts in two hops and still rounds to whole USD cents between them (2556 KRW on this branch, 2544 on base, stored value 2555)."

2. **NOTE (10403 receipt wording).** `receipts/SOLR-10403.md` line 8 says "on base production". Replacement: "On base production code (`cabedd1d968`) with the branch's test file, the 3 parametrized variants of `testConvertAmountRoundsInsteadOfTruncating` fail with `expected:<2555> but was:<2554>`." The receipt names no base SHA. The test exists only on the branch (`cabedd1d968..d8e03755cb5` adds 12 lines to `CurrencyFieldTypeTest.java`).

3. **NOTE (10403 coverage).** The gate ran `CurrencyFieldTypeTest` only. Changed-rounding callers at query time: `CurrencyFieldType.java` 289 (`getFieldQuery` via `convertTo`), 477, 484, 686; `CurrencyValue.java` 184; `RangeFacetRequest.java` 858 and `FacetRangeProcessor.java` 988 (facet range gaps); sorting through `RawCurrencyValueSource`. Not run: `solr/core/src/test/org/apache/solr/search/CurrencyRangeFacetCloudTest.java` and the other currency test classes. Limits sentence for any draft: "The focused run covers CurrencyFieldTypeTest only. Currency range facets, facet gaps, sorting and mixed-currency range queries use the same rounding and were not run."

4. **NOTE (10403 negative amounts).** `CurrencyValue.java` line 146 is `Math.round(value)`. Exact negative halves round toward zero (`Math.round(-2.5)` is -2), while positive halves round away from zero. `parse()` line 77 already uses `Math.round`, so input and conversion agree. No change needed. Do not write "half up" without this qualifier.

5. **NOTE (10403 local ref).** `refs/heads/solr-10403-submit` and `wt-solr-10403-submit` are at `1e285cd730c`, one commit behind `refs/remotes/origin/solr-10403-submit` (`d8e03755cb5`). The table's live head matches origin. The ref is shared, so do not move it.

6. **FIX (11391, hold, still open).** `changelog/unreleased/SOLR-11391-join-unknown-method-bad-request.yml` lines 6-8 name SOLR-11391 in the links block. The Jira packet `research/jira-context/SOLR-11391.json` has the summary "JoinQParser for non point fields should use the GraphTermsCollector". No comment in the packet mentions an unknown method or a 400. Replacement: the owner's new key, as `links:` / `  - name: SOLR-<new key>` / `    url: https://issues.apache.org/jira/browse/SOLR-<new key>`, or drop the branch.

7. **NOTE (11391 receipt wording).** `receipts/SOLR-11391.md` line 7, "The premise discriminates.", is code-level only. Base `JoinQParserPlugin.java` line 221 calls `Method.valueOf` directly. Replacement for line 7: "Proof: on base production code (`cabedd1d968`) with the branch's test in place, the class runs 5 tests with 1 failure: `testUnknownJoinMethodIsBadRequest`, where an unknown join method answers 500 and the branch answers 400."

8. **NOTE (11391 code, no change).** Head line 220 calls `parseMethodString` (lines 268-276, which returns 400 "Provided join method '...' not supported"). The `assertQEx(String, String, SolrQueryRequest, ErrorCode)` overload exists at `SolrTestCaseJ4.java` lines 1069-1073. The code reads as correct. Not run.

## Task results

**SOLR-10403: HOLD, no draft.** The live head `d8e03755cb5` matches origin and the receipt. The packaging commit removes `SOLR-10403-TESTING.md` (24 deletions). The branch diff against `cabedd1d968` is three files. The proof was checked by hand: on base, 255455 × 0.1 twice truncates to 2554; on the branch, 255455 / 100 rounds to 2555. The branch's five assertions give 2555, 229, 300, 3, 7, and all pass. The count of 45 equals 15 test-named methods times 3 variants (`testPerformance` is `@Ignore` at line 286). That fits the file if the runner also runs test-named methods without `@Test` (`testExpectedProvider` line 375 and `testFunctionUsage` line 384 have no annotation). The earlier audit holds at query time: the index path (`createFields`, `CurrencyFieldType.java` 177-195) stores the raw parsed amount, and `convertAmount` has no index-time caller. The ticket is not met (finding 1), so a draft would overstate the fix.

**SOLR-11391: HOLD.** The live head `b0f15a22856` matches origin. The packaging commit removes `SOLR-11391-TESTING.md` (23 deletions). The code change and the new test (`TestScoreJoinQPNoScore.java` lines 61-68) match the receipt. The class has five test-named methods (three annotated at lines 61, 70, 264; two unannotated at lines 212, 224), which fits "5 of 5". The new receipt does not change the hold: the Jira packet says nothing about unknown methods (finding 6). The HOLD draft is at `pr-drafts/query-parsing/SOLR-11391.md`. It has no Jira key and no ticket link, and it is marked not for posting.

## Owner decisions

1. SOLR-10403: (a) keep the one-hop helper change, state in Limits that `currency()` still rounds to cents between hops, and keep the ticket on hold; or (b) carry the unrounded value through `ConvertedCurrencyValueSource` and `RawCurrencyValueSource` (`CurrencyFieldType.java` lines 452-499 and 576-690), round once at the end, and add a test that calls `currency(price,'KRW')`. Only (b) meets the ticket. It also widens query-time changes, so the other currency classes need a run before any proof claim.
2. SOLR-11391: a new Jira key for the 400 change, or drop the branch. It cannot go under SOLR-11391.

## Not checked

- No builds, tests, Gradle, `gh` calls, or fetch. Live heads came from the local remote-tracking refs.
- The gate logs (`g10403-gate.log`, `g10403-regate.log`, `g11391-gate.log`) are not on disk. The counts come from the receipts only.
- The runner's pickup of test-named methods without `@Test` is assumed from the randomized-runner convention, not checked.
- The exchange-rate direction comes from the ticket and the test. `FileExchangeRateProvider` was not read.
- Ticket text comes from local packets (last updated 2017 and 2021), not live JIRA.
- `CurrencyRangeFacetCloudTest` and other currency tests were not run, and not read for half-cent ties.
- The changelog YAML was read by eye, not parsed.
- Current upstream main was not checked (local ref `8e62c2686882`). The base is the merge base `cabedd1d968`.
