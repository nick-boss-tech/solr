# SOLR-6009 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-6009 - "edismax mis-parsing RegexpQuery" (Evan Sayer). `q={!edismax qf='text'} /.*elec.*/` produced `RegexpQuery(<U+FFFC x3>:/.*elec.*/)`, i.e. edismax's `IMPOSSIBLE_FIELD_NAME` leaked into the final query. Vitaliy Zhovtyuk's comment describes the cause and attached a patch.
- Branch: `solr-6009-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`ExtendedDismaxQParser.ExtendedSolrQueryParser` overrides `getPrefixQuery`, `getWildcardQuery`, `getFuzzyQuery`, `getRangeQuery` and friends to record the clause (`QType`) and route it through `getAliasedQuery()`, which expands the alias (`IMPOSSIBLE_FIELD_NAME` -> `qf` fields) and calls `super.getXxxQuery` per field. There is no override for `getRegexpQuery`, so the base class builds the regexp query directly on the placeholder field. Still true on main (no `getRegexpQuery` in `ExtendedDismaxQParser`).

## What the branch changes
- `QType.REGEXP`, an override `getRegexpQuery(field, val)` that stores the type/value and calls `getAliasedQuery()`, and a `case REGEXP` in `getQuery()` calling `super.getRegexpQuery`.
- New `TestExtendedDismaxParser.testRegexQueryUsesQueryFieldsNotImpossibleFieldName`: `q=/.*apper/` with `qf=name` matches doc 44 ("The Zapper"), and the parsed query contains `name:/.*apper/` and no U+FFFC character.

## What was guessed (verify these first)
1. Doc 44's `name` is indexed so that a term ending in `apper` exists and `/.*apper/` matches it; the exact parsed-query rendering (`name:/.*apper/`) may differ (e.g. wrapped in a DisjunctionMaxQuery, or the term lowercased).
2. edismax's pre-processing leaves a `/.../` clause alone (the ticket's output shows the regex reaching the parser).
3. `getRegexpQuery` in the base class is `protected` with this signature (`SolrQueryParserBase` ~L1333); `analyzeIfMultitermTermText` may throw for the placeholder field if the alias is not resolved first.
4. Behavior difference to consider: before, an unmatched regex returned a query on a nonsense field; now regex clauses are applied to every qf field (OR via the DisjunctionMax alias).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestExtendedDismaxParser"
```
Fail-before: revert only `ExtendedDismaxQParser.java`; the parsed query contains U+FFFC.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
