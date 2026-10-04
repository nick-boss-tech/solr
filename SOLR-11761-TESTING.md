# SOLR-11761 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-11761 - "Query parsing with comments fail in org.apache.solr.parser.QueryParser" (Andreas Presthammer; 6.2.1/6.6.2/8.0). Steven Rowe posted a repro test; Kai Chan attached a patch and the same test (patch not reviewed in the ticket). Check the ticket for it before using this branch.
- Branch: `solr-11761-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`QueryParser.jj` tracks nested comments in the token manager field `commentNestingDepth`. `"/*"` increments it and `"*/"` decrements it, switching back to the DEFAULT lexical state only at depth 0.
After a parse that ends inside `/*`, the depth stays at 1. `QueryParserTokenManager.ReInit(CharStream)` resets the lexical state but **not** `commentNestingDepth`, so on the next parse `/* foo */` ends at depth 1 and the lexer stays in COMMENT state: `SyntaxError`
forever for any query with a comment, until a new parser instance is created. `SolrQueryParserBase.parse` re-initialises via `ReInit(CharStream)`.

## What the branch changes
- `SolrQueryParserBase`: declares the generated `ReInit(QueryParserTokenManager)` as abstract (the generated `QueryParser` already has it) and `parse()` now calls it with a fresh `QueryParserTokenManager`. This avoids touching the generated parser or the grammar.
- `TestSolrQueryParser.testCommentsAfterSyntaxError`: valid comment, `/*` (SyntaxError), then valid comment / plain / valid comment again.

## What was guessed (verify these first)
1. **Compile**: `QueryParserTokenManager(CharStream)` is public and `ReInit(QueryParserTokenManager)` is public on the generated class (both seen in main's generated sources); `FastCharStream`/`QueryParserTokenManager` are in the same package as `SolrQueryParserBase`.
2. **Behavioral difference**: `ReInit(QueryParserTokenManager)` does not set `jj_lookingAhead = false` the way `ReInit(CharStream)` does. It starts false and should only be true mid-lookahead, but a `TokenMgrError` thrown inside lookahead could leave it true; if that matters, reset it or fall back to resetting `commentNestingDepth` in a `.jj` hook.
3. **Other callers**: nothing else on main calls `ReInit(CharStream)`; subclasses (`SolrQueryParser`, the edismax parser) inherit `parse()`.
4. **Performance**: one small allocation per `parse()` call (token manager has fixed-size arrays); `testParsingPerformance` exists and may be worth a glance.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestSolrQueryParser" --tests "org.apache.solr.search.TestExtendedDismaxParser"
```
Fail-before: revert only `SolrQueryParserBase.java`; the second `/* foo */ bar` assertion should throw `SyntaxError`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
