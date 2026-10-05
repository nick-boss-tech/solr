# SOLR-10897 - hypothetical reproduction and fix (not run)

Nothing here was compiled or executed. The fix and the test were written by reading
`upstream/main`; treat every claim below as a guess to verify first.

## JIRA context
"SimpleQParserPlugin doesn't work with PointFields". The ticket has a title only: no description,
no comments. Its sibling SOLR-10896 (raw parser) was judged by-design; this one was not.

## Bug mechanism (guessed)
`SolrSimpleQueryParser` overrides `newPrefixQuery` and `newFuzzyQuery` but not `newDefaultQuery`,
so plain terms use Lucene's `SimpleQueryParser`: the schema query analyzer turns the text into a
`TermQuery` on the field. A point field stores values in a BKD tree, not as terms, so
`{!simple qf=int_p}7` builds a `TermQuery` that matches nothing.

## Fix
`newDefaultQuery` is overridden. For a point field it asks the field type for its own query
(`FieldType.getFieldQuery`), which gives the exact-match point query. Text that is not a valid
value for the field (a `BAD_REQUEST` `SolrException`) contributes no clause, so a mixed
`qf=text0 int_p` still works for words. All other field types keep the previous
`createBooleanQuery` path, boosts and the `SHOULD` combination across fields as before.

## What the test pins
`TestSimpleQParserPlugin.testPointFieldQuery` (schema gets an `IntPointField` `int_p`)
- single point field via `qf` and via `df`: match, non-match, `7 8` gives both documents
- non-numeric text on a point field gives zero hits instead of an error
- mixed `qf=text0 int_p`: the text field and the point field each match their own kind

## What was guessed / verify first
- That the ticket really means exact-match terms on a point field; phrase, prefix and fuzzy on a
  point field are not addressed (prefix/fuzzy on numbers have no meaning).
- `getDefaultOperator()` is passed as before, but multi-token text on a point field is not split
  per token by the field query; `7 8` works only because the parser already splits on whitespace
  before calling `newDefaultQuery`. Check the `7 8` assertion first if the test fails.
- That the new `int_p` docs (ids 60, 61) do not disturb other tests in the class (they only use
  text fields in `qf`).
- Fail-before: revert only `SimpleQParserPlugin.java`; `testPointFieldQuery` should fail on the
  first assertion.
- Spotless formatting.
