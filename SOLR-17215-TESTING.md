# SOLR-17215 - hypothetical reproduction (nothing was compiled or run)

JIRA: FreeTextLookupFactory suggester built via `suggest.buildAll` on one replica; queries routed to other replicas fail with
`java.lang.IllegalStateException: Lookup not supported at this time`. Jan Hoydahl: suggester dictionaries needing an explicit
build were never replicated; build on each node. Earlier audit note: "feature, not a bug". The part that is a defect is the
error: a raw ISE (HTTP 500) with no hint.

## Change
`SolrSuggester.getSuggestions` wraps the lookup in a try/catch for `IllegalStateException` and rethrows
`SolrException(SERVICE_UNAVAILABLE)` naming the suggester and the workaround (build on every node, or buildOnStartup/buildOnCommit).
Replication of the dictionary itself is NOT attempted.

## Test
`TestFreeTextSuggesterNotBuilt` (`solrconfig-phrasesuggest.xml`, `free_text_suggest`, no build): query yields 503 with
"is not built on this node"; after `suggest.buildAll` the same query returns the usual suggestion. One test method on purpose
(order matters).

## Guesses to verify first
- `FreeTextSuggester.lookup` really throws ISE before any build or load (Lucene 10 source not re-read; the ticket stack trace says so).
- `assertQEx` with an `ErrorCode` overload exists in `SolrTestCaseJ4` and matches the message substring.
- The suggest request path (`SuggestComponent`) does not already catch the ISE higher up.
- Other lookups (Analyzing, Fuzzy) return empty results when unbuilt, so the new branch should not change them.

## Fail-before
Expected: on main the query throws ISE (500), `assertQEx` for 503 fails.
