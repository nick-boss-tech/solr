# SOLR-8977 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-8977 - "graph qparser's traversalFilter doesn't support pure negative queries" (Chris Hostetter). Found while documenting the parser with the techproducts example.
- Branch: `solr-8977-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`GraphQuery` adds the traversal filter as a `MUST` clause next to the frontier query on every hop (`GraphQuery.java` ~L225-228). A pure negative `BooleanQuery` (`-text:foo10`) nested as a `MUST` clause matches nothing, so the traversal stops after the root. `GraphQueryParser` passes `subQuery(traversalFilter).getQuery()` straight through.

## What the branch changes
- `GraphQueryParser.parse`: wrap a non-null traversal filter with `QueryUtils.makeQueryable(...)` (adds `*:*` to pure negatives, leaves others untouched).
- `GraphQueryTest.doGraph`: new assertion on the 10 -> 11 -> (12|13) graph: `traversalFilter='-text:foo10'` from `doc_10` returns 2 docs (root and `doc_11`).

## What was guessed (verify these first)
1. The nested-pure-negative diagnosis (not checked by running); a root-cause alternative is that `getQuery()` returns something other than a pure negative `BooleanQuery` for `-text:foo10`.
2. Expected count 2: root `doc_10` (no `text` field), `doc_11` (`text:foo11`), while `doc_12`/`doc_13` have `text:foo10`. Assumes the filter is not applied to the root and that edges 12/13 are only reachable through `doc_11`.
3. The assertion runs for every field-type combination in `testGraph`, including the point and docValues-only variants.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.join.GraphQueryTest"
```
Fail-before: revert only `GraphQueryParser.java`; the new assertion should return 1 doc.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
