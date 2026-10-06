# SOLR-10789 - hypothetical reproduction (nothing was compiled or run)

JIRA: SpellCheckCollator prohibits setting several query parser params (tie, pf, pf2, pf3, bq, bf).
The skip audit (Tier 3) had this as "already fixed in 7.0 (fix version set)". The fix version is set but
`SpellCheckCollator` on `upstream/main` still removes those params *after* applying the
`spellcheck.collateParam.*` overrides, so an override of them is silently discarded.

## Change
`SpellCheckCollator.collate`: the six `params.remove(DisMaxParams.*)` calls now run before the override loop,
so the default stays "strip scoring params from the collation check" and an explicit
`spellcheck.collateParam.bq=...` (etc.) wins.

## Test (guessed)
`SpellCheckCollatorTest.testCollateParamOverrideOfScoringParams`, two requests on `/spellCheckCompRH`:
1. valid `collateParam.bq` -> collation still found (control).
2. unparseable `collateParam.bq=teststop:(` -> the check query throws, `SpellCheckCollator` logs a WARN and the
   collation has 0 hits, so no collation is returned. Without the fix the bq is stripped and a collation is returned.

## What was guessed (verify first)
- edismax parses `bq` eagerly enough that a syntax error aborts the check (it should: `addBoostQuery` -> SyntaxError -> SolrException).
- `teststop` is a field in the test schema (used by `testCollateWithOverride`, same request shape).
- `count(...)=0` xpath is accepted by `assertQ`.

## Fail-before
Revert the move of the six `remove` calls (put them back after the loop): request 2 returns a collation and fails.
