# SOLR-15906 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-15906 - "Query parsing ignores rest of query when 'v' local-param is used" (David Smiley, 2022). Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-15906-submit` off `apache/solr` main `e2cdb2d7e8ae`

## The bug, as understood
`QParser.getParser` parses a leading `{!...}`. If the local-params contain `v`, the value comes from `v` and the remainder of the string after `}` is thrown away (the code even has a TODO asking "throw an error? fall back to the lucene QParser?"). So `q={!parser v=$qq} OR other` silently means just `{!parser v=$qq}`. Experienced users know to add a leading space.

## What the branch changes (the behavior Smiley asks for in the ticket)
In `QParser.getParser`, if the local-params contain `v` **and** the text after the closing brace is non-blank, local-params parsing is skipped and the whole original string goes to the lucene query parser (which natively understands `{!...}` clauses). No change when there is no trailing text, or when `v` is absent (trailing text is then the value, as before).

Test: `TestSolrQueryParser.testTextAfterLocalParamsWithExplicitValueIsNotIgnored` (`{!term f=text v=$qq} OR id:3` with a non-matching and a matching `qq`, the leading-space variant, and the unchanged no-trailing-text case).

## This is a behavior change; guesses to verify first
1. Existing tests or users that rely on trailing text being ignored (e.g. `{!func v=...} something`, or trailing whitespace-only text which is handled by `isBlank()`). Run the query-parser tests broadly (`TestSolrQueryParser`, `TestQueryParsing`-style tests, `TestLocalParams*`, and anything using `{!...v=` in `solr/core/src/test`).
2. `defaultParser` = `func` also allows local-params: the fallback then switches to the lucene parser for such a string. Maybe the fallback should keep the default parser name instead of forcing `lucene`.
3. The `stringIncludingLocalParams` / `localParamsEnd` fields now describe a parse without local-params in the fallback case (set to the full string / -1); check users such as the debug output and `QParser.getLocalParams()` callers that assume non-null local params when the string started with `{!`.
4. A conservative alternative is to throw BAD_REQUEST instead of falling back; the ticket suggests falling back.
5. The ref guide's local-params page may want a sentence.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestSolrQueryParser"
```
Fail-before: revert `QParser.java` only; the first assertion (numFound==1) fails with 0.

## Not done
No JIRA comment, no PR.
