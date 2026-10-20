# SOLR-2534: min and max over string fields

Draft PR description, not opened. Branch `solr-2534-string-minmax` at `04bbcee582`, base `apache/main` at `c8ab71430f`.

## Summary

`min(...)` and `max(...)` in the function parser accept string fields. With only string fields as arguments, the result is the lexicographic minimum or maximum of those values, returned as a string. Numeric arguments keep the existing float behavior.

## Choices (owner row 8, answer A, and ladder rung 4)

- Dispatch: at parse time, if every argument is a `StrFieldSource`, the string function is used. If none are, the numeric function is used. A mix is a `BAD_REQUEST`.
- Missing values are ignored. A document with no value in any argument has no value.
- Multi-valued string fields are an error. This comes from the existing `checkFieldCacheSource` guard, not new code.
- Comparison: byte order of the UTF-8 values (`BytesRef`), which matches Lucene's term order. `String.compareTo` would order some supplementary characters differently.
- Type scope: only the `StrField` value source. Other string-like types (for example text fields) keep their existing behavior, which is an error for min and max.
- Result: the string value. `fl=mn:min(...)` returns it as a string.

## Caveats

- Sorting by a string min or max is not supported. The result's `floatVal` throws `UnsupportedOperationException`, and the default sort path uses numeric values. This was not tested, so a sort request may fail with a server error. A maintainer may want a string sort or a clear error.
- `exists` is true when any argument has a value. This differs from `MultiStringFunction`, which requires every argument.
- No change to the numeric `min` and `max` code beyond the dispatch.

## Tests

- `TestStringMinMaxFunction`, 5 tests: missing values ignored, code-point ordering, multi-valued error, mixed-type error, numeric unchanged.
- `TestFunctionQuery`, 23 tests, no failures.
- Gate: `/workspace/gates/logs/vm4/solr-2534/test2.log`, XML under `/workspace/gates/logs/vm4/solr-2534/xml/`.
