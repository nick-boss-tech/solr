# SOLR-10882 - hypothetical reproduction (nothing was compiled or run)

JIRA: umbrella "Restructure and Cleanup Stream Evaluators" (2017). Earlier audit note: "refactor umbrella, committed in
pieces". The last comment is an open defect: `arraySort`'s replacement, `array(..., sort=asc)`, "doesn't take into account
differing types (double and long). Will correct with a type normalization pass." Inputs are now normalized to `BigDecimal`
(`RecursiveEvaluator.normalizeInputType`), so numbers of different Java types do sort; the remaining gaps are below.

## Change
`ArrayEvaluator.doWork` with a sort:
- a null value made the "non-Comparable" error message throw `NullPointerException` (`value.toString()`); now `%s` of the
  value.
- values of different kinds (a number and a string, a boolean and a string) passed the `Comparable` check and then failed in
  the sort with a raw `ClassCastException`; now an `IOException` "values of different types (X and Y)". All `Number`s count as
  one kind.

## Test
`ArrayEvaluatorTest`: mixed Double/Long sort ascending (pins the normalization, probably passes on main) and a string/number
mix expecting the `IOException` message.

## Guesses to verify first
- `Tuple` values `"a"` (String) and `2L` reach `doWork` as `String` and `BigDecimal`, i.e. the check sees two kinds.
- A field name that is missing from the tuple gives `null` and either reaches `doWork` (fixed message) or is dropped earlier;
  no test was written for it.
- `evaluate` rethrows the `IOException` unchanged (only `UncheckedIOException` is unwrapped).
- `Boolean` is `Comparable`, so `[true, false]` still sorts.

## Fail-before
Expected: on main the mixed test dies with `ClassCastException` (not `IOException`), so `expectThrows` fails.
