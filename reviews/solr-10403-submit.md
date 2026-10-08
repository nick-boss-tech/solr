# solr-10403-submit

- Branch: origin/solr-10403-submit
- Head: 1e285cd730c2 (matches the listed head; checked against the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 4 files, +50/-11. `solr/core/src/java/org/apache/solr/schema/CurrencyValue.java` (4-argument `convertAmount`: digit shift by `Math.pow(10, |delta|)` as one division or multiplication; `Math.round` instead of `(long)`), `solr/core/src/test/org/apache/solr/schema/CurrencyFieldTypeTest.java` (new `testConvertAmountRoundsInsteadOfTruncating`, five assertions), changelog `changelog/unreleased/SOLR-10403-currency-rounding.yml` (`type: fixed`, author Nick Shanin), and `SOLR-10403-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Nearly
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no test run, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-10403-TESTING.md` is labeled "hypothetical reproduction (nothing was compiled or run)" and is treated as unverified.

## Findings (ranked)

1. **MEDIUM, hypothesis (the author flags it). Results change for every caller.** The 4-argument `convertAmount` is reached from the currency-code overloads and so from `CurrencyFieldType.java:477, 484, 686`, `RangeFacetRequest.java:858`, and `FacetRangeProcessor.java:988` at `upstream/main`. Switching from truncation to rounding can change any existing expectation that was computed by truncation, and can move range-query and facet-range boundaries by one unit. The author did not check the existing expectations (`testCurrencyRangeSearch`, `testFunctionUsage`, `testCurrencySort`, `testCurrencyPointQuery`, and the `ExchangeRateProvider` range tests). The Linux-side focused run is where this shows up. Not patched: whether an existing expectation should move is a test-data decision.

2. **Owner call A (see below). Rounding mode is unspecified.** `Math.round` is half-up toward positive infinity, so `-2.5` becomes `-2` and `-2.6` becomes `-3`. The JIRA says precision should be applied at the end, not which rounding mode. Negative currency amounts are probably rare, but the mode is a product choice.

3. **Verified (checked, no issue). The five assertions hold by hand.** `convertAmount(1.0, 2, 255455L, 0)` is 2554.55, which rounds to 2555. `convertAmount(1.0/1116.071429, 0, 2555L, 2)` is about 2.2891, times 100 is about 228.91, which rounds to 229. The two exact shifts give 300 and 3. The `700 -> 7` case gives 7. The digit-shift branches match the signs: a negative delta divides by `Math.pow(10, -delta)`, a positive delta multiplies.

4. **Verified (checked, no issue). Fail-before is a real failure.** On `main`, `255455 * 0.1 * 0.1` gives about 2554.55, and `(long)` truncates it to 2554, so the first assertion fails. The second gives about 228.91, truncated to 228, so the second assertion fails. The method exists on `main`, so each failure is a value mismatch, not a missing API.

5. **Verified (checked, no issue). Return type and imports.** `Math.round(double)` returns `long`, so the method's return type is unchanged. `CurrencyFieldTypeTest` already uses `@Test` and `assumeTrue` in the same class.

6. **LOW, verified. Negative amounts round asymmetrically.** Half values round toward positive infinity (`-2.5` gives `-2`), which is the same as the owner call above. Non-negative amounts are unaffected.

## Owner calls (not decided here)

- **A. Rounding mode.** Options: (a) keep `Math.round` (half-up toward +infinity, current branch); (b) half-up away from zero; (c) half-even via `BigDecimal`. Pose it. Not patched.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no fail-before run, no Spotless.
- The existing currency test expectations and the range-boundary tests (finding 1). The Linux-side run is where this is decided.
- The `CurrencyFieldType`, `RangeFacetRequest`, and `FacetRangeProcessor` call sites were located by grep, not read in full.
- `SOLR-10403-TESTING.md` is treated as unverified. Left in place.
