# SOLR-10403 - hypothetical reproduction (nothing was compiled or run)

JIRA: `currency(price,'KRW')` on 2554.55 KRW gives 2544, an error of about 0.4%; Jan agreed the precision should be applied
at the end. The audit note "fixed in 7.0" is a fix-version tag. On `upstream/main`
`CurrencyValue.convertAmount(double,int,long,int)` still does `(long) value` (truncation) after shifting digits with
repeated `value *= 0.1` (0.1 is not exact, so a result like 2554.5500000000002 or 2.9999999 loses a whole unit).

I did NOT reproduce the ticket's full 2544 (that depends on how the reporter chained conversions); this fixes the
conversion primitive: truncation and inexact digit shifts.

## Change
`Math.round` instead of `(long)`; digit shift is one division/multiplication by `Math.pow(10, |delta|)`.

## Test (guessed)
`CurrencyFieldTypeTest.testConvertAmountRoundsInsteadOfTruncating`: 255455 (2 digits) to 0 digits is 2555; 2555 at rate 1/1116.071429 to 2 digits is 229; exact shifts.

## Guesses to verify first (risky)
- Existing currency tests (`testCurrencyRangeSearch`, `testFunctionUsage`, `testCurrencySort`, `testCurrencyPointQuery`) and the
  `ExchangeRateProvider`-based range tests may have expected values computed with truncation; some may flip by one unit.
- Range-query boundaries use `convertAmount` too (lines ~477/484/686 of `CurrencyFieldType`): rounding may move which docs match at a boundary.
- The mock provider rates in the test config (USD/EUR 0.8, etc.) may produce half-way values.

## Fail-before
Revert: first assertion returns 2554 (255455 * 0.1 * 0.1 = 2554.55 truncated), second returns 228.
