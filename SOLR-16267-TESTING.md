# SOLR-16267 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

`avg(sqrt(TotalCpuUsec))` divided by the number of docs in the bucket
instead of the number of docs that actually have the field, because the
nested `sqrt(...)` ValueSource reported `exists(doc) == true` for docs
where the underlying field was missing (returning `Math.sqrt(0.0) = 0.0`
for them). The bare-field `avg(TotalCpuUsec)` correctly divided by the
non-missing count. Same root cause made `countvals(sqrt(field))` count
docs without the field.

Root cause: the single-arg math wrappers in
`solr/core/src/java/org/apache/solr/search/ValueSourceParser.java`
(`DoubleParser.Function`, backing `sqrt`, `cbrt`, `log`, `deg`, `rad`,
...) override only `doubleVal` in their anonymous `DoubleDocValues`;
Lucene's `FunctionValues.exists(int)` defaults to `true` (verified via
javap on lucene-queries-10.4.0). The facet slot accs trust `exists()` to
skip missing values (`AvgSlotAcc.collect`: `if (val != 0 ||
values.exists(doc))`, `CountValsAgg`, variance/stddev accs), so the 0.0
"missing" values were counted.

Fix (10 lines, one file): override `exists(int doc)` on the
`FunctionValues`:

- `DoubleParser.Function`: `return vals.exists(doc);`
- `Double2Parser.Function` (e.g. `pow`, two-arg forms): `return
  aVals.exists(doc) && bVals.exists(doc);` — mirrors Solr's
  `DualDoubleFunction`, which overrides `exists` via
  `MultiFunction.allExists(...)`.

This fixes avg/countvals/variance/stddev/minmax for all single- and
two-arg math functions at once. No other callers depend on the old
always-true behavior (`func` query sorting uses `doubleVal`, not
`exists`).

Files changed:
- `solr/core/src/java/org/apache/solr/search/ValueSourceParser.java`

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Behavior changes to state in the PR (round-3 review): the same `exists()` fix
also changes `min`/`max` (`MinMaxAgg` skips a doc when `val == 0 && !exists`, so
`min(sqrt(f))` stops returning 0 for missing docs), `missing(sqrt(f))`,
`percentile` (`PercentileAgg` skips non-existing docs), the `exists(...)`
function query and `def(...)`, for the functions in `DoubleParser` and
`Double2Parser`.

Tests added (round-3 patch pass, **not compiled or run**), in `TestJsonFacets`
(`doStatsTemplated`, so they run standalone and distributed), using the existing
`sparse_num_d` fixture (only docs 1 and 4, both in bucket `A`, have a value: 6
and -4):

- Global stats over `floor(sparse_num_d)` and `pow(sparse_num_d,2)` (one and two
  argument functions): `avg` 1.0 and 26.0, `countvals` 2, `missing` 4, and for
  `pow` also `min` 16.0 / `max` 36.0 (before the fix `min` would be 0.0 and the
  counts would be 6). `floor` is used because `sqrt(-4)` is NaN.
- Per bucket: `countvals(floor(sparse_num_d))` is 2 for `A` and 0 for `B`.

Not covered: `variance`/`stddev`, `percentile`, and the `exists()`/`def()`
function queries (a `TestFunctionQuery` case would cover those).

Queued for the verification run: `org.apache.solr.search.facet.TestJsonFacets`
and `org.apache.solr.search.function.TestFunctionQuery`, with Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Other single-source wrappers outside `ValueSourceParser` were not
  audited; if the pattern recurs elsewhere it is a follow-up.
- Changelog fragment added: `changelog/unreleased/SOLR-16267.yml`.
- Remove this file before opening the upstream PR.
