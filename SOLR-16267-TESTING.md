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

Suggested tests (not written):

1. JSON-facet test (FacetTestBase style): index docs where some lack the
   numeric field; assert `avg(sqrt(field)) ==
   sum(sqrt(field))/countvals(field)` and `countvals(sqrt(field)) ==
   countvals(field)`.
2. Regression: `avg(field)` unchanged; `min`/`max`/`stddev`/`variance`
   with a nested function on a partially-missing field.
3. Existing function-query / facet test suites.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Other single-source wrappers outside `ValueSourceParser` were not
  audited; if the pattern recurs elsewhere it is a follow-up.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
