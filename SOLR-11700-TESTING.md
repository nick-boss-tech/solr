# SOLR-11700 - hypothetical reproduction (nothing was compiled or run)

JIRA: the ref guide's WordDelimiterGraphFilter examples list a concatenated token ("XL4000ES", "hotspot", "10042") at the
position of the LAST part; the reporter's Analysis screen shows it first, at position 1. On `upstream/main`
`filters.adoc` still has the old (WordDelimiterFilter-era) positions. Earlier sessions left this as "Lucene WDGF semantics,
docs example only" without changing the docs.

## Change
Docs only: the two examples that show catenated tokens now list the catenated token first at the position of the first part,
plus a NOTE that the filter emits a token graph. No code, no test.

## Guesses to verify first
- Real order and positions: run the Analysis screen on `XL-4000/ES` with `catenateAll=1` (reporter: `XL4000ES(1)`, `XL(1)`, `4000(2)`, `ES(3)`).
- The `hot-spot 100+42 XL40` example with catenateWords/Numbers: `hotspot` before `hot`, `10042` before `100`? (positionLength not shown in the guide).
- Other catenate examples in the same section (`catenateWords="1" catenateNumbers="1"`) were not re-checked.

## Fail-before
n/a (documentation). Check by running the analysis and comparing.
