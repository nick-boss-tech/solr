# SOLR-12212 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12212 - "SolrQueryParser not handling q.op=AND correctly for parenthesized NOT fq" (Michael Braun, 6.6.2/7.3/8.0). The ticket has no comments.
- Branch: `solr-12212-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)
- Research note: `research/pipeline/research-notes/SOLR-12212.md` in the Solr-issues workspace

## The bug, as understood
`fq=(NOT(eee_s:(Y)))` with `q.op=AND` returns nothing. In `QueryParser.jj`, `Query()` unwraps a lone clause only when it is `SHOULD` (q.op=OR). With q.op=AND `addClause` makes the lone parenthesized clause `MUST`,
so the result is a `BooleanQuery` holding one `MUST` clause whose query is itself a pure-negative `BooleanQuery` (`-eee_s:Y`). Lucene matches nothing for a pure-negative boolean, and
`QueryUtils.makeQueryable` only repairs the *top-level* query, so the nested one is never given its `*:*`. With q.op=OR the lone clause is unwrapped, the negative query becomes top-level and gets repaired.

## What the branch changes
- `QueryParser.jj` and the checked-in generated `QueryParser.java`: in `Query()`, when there is exactly one `MUST` clause whose query `QueryUtils.isNegative`, return that inner query (as the `SHOULD` case already does)
  so the existing top-level repair applies.
- `TestSolrQueryParser.testParenthesizedPureNegativeWithDefaultOpAnd`: the ticket's case for q.op OR and AND, the no-outer-parens control, and a negative control that must return 0 hits.
  Uses the existing doc `id=12` (`eee_s=X`), so no new data.

## What was guessed (verify these first)
1. **Generated parser**: I hand-edited `QueryParser.java` to mirror the `.jj` change. The checked-in `.java` already differed from the `.jj` in accessor names (`occur()`/`query()` vs `getOccur()`/`getQuery()`),
   so the build may regenerate it; if a javacc/`regenerate` check task fails, regenerate with the project's task and keep only the intended hunk.
2. **Root cause**: reasoned from the grammar, not from a debug print. Confirm with `debug=query` that the pre-fix parsedquery is `+(-eee_s:Y)` (or similar) for q.op=AND.
3. **Scope**: only a lone MUST clause is handled. Pure-negative sub-queries nested alongside other clauses (e.g. `a AND (NOT b)`) are a different, documented Lucene behavior and are untouched.
4. **Risk**: returns the inner query without a surrounding boolean for `+(-a)`/`AND` forms. Run the rest of `TestSolrQueryParser` and `TestExtendedDismaxParser`/`TestStandardQParsers`.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestSolrQueryParser"
```
Fail-before: revert only the two `QueryParser.*` files; the q.op=AND assertions should fail with numFound 0.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
