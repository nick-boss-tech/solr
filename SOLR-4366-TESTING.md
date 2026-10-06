# SOLR-4366 - hypothetical reproduction (nothing was compiled or run)

JIRA (2013, 4.0): an unknown `spellcheck.dictionary` gave an NPE in `prepare` (with `spellcheck.build` / `spellcheck.reload`) and
in `process` (with `spellcheck.q`). The audit note said "obsolete: throws NOT_FOUND for unknown dictionaries
(`testInvalidDictionary`)". That test only covers the `process` path.

## What main did (read on `upstream/main`)
- `prepare` called `getSpellChecker(params)` and went straight to `spellChecker.build(...)` / `.reload(...)`: NPE for an unknown name.
- `getSpellChecker(params)` with several `spellcheck.dictionary` values built a `ConjunctionSolrSpellChecker` and added
  `spellCheckers.get(dn)` without a null check, so one unknown name among valid ones failed inside `addChecker`.
- On a coordinator, `modifyRequest` / `finishStage` dereference `getSpellChecker(params)` too; `prepare` runs first, so throwing
  there covers them.

## Change
`prepare` throws the same NOT_FOUND ("Specified dictionaries do not exist: ...", shared helper `dictionariesDoNotExist`) when
the checker is null and spellcheckers are configured; with no spellcheckers it returns quietly (as `process` does).
`getSpellChecker` returns null if any requested name is unknown. New `testInvalidDictionaryWithBuildOrReload`.

## Guesses to verify first
- The multi-dictionary message order ("INVALID default") follows `getDictionaryNameAsSingleString`; adjust if it differs.
- The `ConjunctionSolrSpellChecker.addChecker` NPE shape is read from the loop, not run.
- A request with `spellcheck=true` and no spellcheckers plus `build=true` used to NPE and now returns without a `command` entry.

## Fail-before
Expected: `testInvalidDictionaryWithBuildOrReload` fails on `upstream/main` with a NullPointerException (HTTP 500 vs 404).
