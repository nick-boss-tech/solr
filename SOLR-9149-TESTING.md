# SOLR-9149 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-9149 - "bug when nested query precedes the main query" (Matteo Grolla). `_query_:"ABC" name_t:"white cat"~3` is parsed with the slop dropped (`name_t:"white cat"`); reversing the order or nesting both works. Yonik Seeley reproduced it with the simple form.
- Branch: `solr-9149-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`SolrQueryParserBase.getFieldQuery(field, text, slop)` only applies the clause slop `if (subQParser == null)`, to avoid re-slopping the result of a nested parser. But `subQParser` is a parser-wide field set by an earlier `_query_`/magic-field clause and never reset, so every later phrase clause in the same query skips the slop. Still true on main (`SolrQueryParserBase` ~L550, field at ~L1047).

## What the branch changes
- `getFieldQuery(String, String, int)`: set `subQParser = null` before delegating, so the check reflects only the current clause (the delegate sets it again when the current clause is itself a nested query).
- `TestSolrQueryParser.testNestedQueryModifiers`: parsed query for `_query_:"foo" text:"how brown"~2` must be `text:foo text:"how brown"~2`.

## What was guessed (verify these first)
1. The exact `parsedquery` string (`text:foo text:"how brown"~2`) depends on the default field `text` and its analyzer in `solrconfig.xml`/`schema.xml` for this test class; `_query_:"foo"` is assumed to parse via the default lucene sub parser to `text:foo`.
2. Other state of the same kind in the class (e.g. `subQParser` used elsewhere) was not audited; only the slop path.
3. If SOLR-12532's `applySlop` change (separate branch) also touches this method there may be a trivial merge conflict.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestSolrQueryParser"
```
Fail-before: revert only `SolrQueryParserBase.java`; the slop disappears from the debug output.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
