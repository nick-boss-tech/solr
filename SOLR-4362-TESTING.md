# SOLR-4362 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-4362 - "edismax, phrase query with slop, pf parameter" (Ahmet Arslan). For `q="phrase query"~10 term` with `pf2`, a document containing "10 term" gets boosted. A later comment (Elizabeth Haubert, 7.6) pins the cause on the list of "normal clauses" used for pf/pf2/pf3: `~10` is still in it.
- Branch: `solr-4362-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`splitIntoClauses` yields `"phrase query"` (phrase), `~10` (bare word `\~10`, special syntax) and `term`. `parseOriginalQuery` consumes the slop, but `parse()` passes the original clause list to `addPhraseFieldQueries`, which drops the phrase and keeps `~10` and `term` as the bare clauses, producing the pf2 shingle `"10 term"`. Still true on main.

## What the branch changes
- `addPhraseFieldQueries`: a bare clause directly after a phrase clause whose value is the escaped `~` followed only by digits (`isSlopClause`) is not added to `normalClauses`.
- New `TestExtendedDismaxParser.testPf2DoesNotUseSlopOfPhraseAsTerm`: the parsed query of `"phrase query"~10 term` (`qf=pf2=phrase_sw`) must not contain `10 term`.

## What was guessed (verify these first)
1. The clause value for the slop is exactly `\~10` (backslash-escaped by `splitIntoClauses`, as the ticket's `val = \~10` output shows).
2. `phrase_sw` (used by `testPfPs`) tokenizes the pf2 shingle so that `10 term` would show up verbatim in `parsedquery_toString` on the broken code; the assertion may need a different field or check.
3. Slop with whitespace (`"a b" ~3`) and fractional or negative values are not handled; field-qualified phrases (`f:"a b"~2`) go through the same `isPhrase` path.
4. A phrase with slop followed directly by a literal term that starts with `~` and digits would also be skipped (unlikely edge).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestExtendedDismaxParser"
```
Fail-before: revert only `ExtendedDismaxQParser.java`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
