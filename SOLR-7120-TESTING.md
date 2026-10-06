# SOLR-7120 - hypothetical reproduction (nothing was compiled or run)

JIRA (2015, Hoss): `ExtendedDismaxConfiguration` caught the `SyntaxError` from `DisMaxQParser.parseQueryFields` and threw
`new RuntimeException()` with no message, so an edismax request with neither `qf` nor `df` (e.g. `bin/solr -e cloud` with
`basic_configs`) failed with an unhelpful 500. The audit note said "obsolete: no more `throw new RuntimeException()`
in ExtendedDismaxConfiguration". The bare message is gone, but the wrapper is not: main still has
`catch (SyntaxError e) { throw new RuntimeException(e); }` (`ExtendedDismaxQParser` ~L1732).

## What main does (read on `upstream/main`)
- `ExtendedDismaxQParser` constructor -> `createConfiguration` -> `ExtendedDismaxConfiguration` -> `parseQueryFields`
  throws `SyntaxError("Neither qf nor df are present.")` when `qf` is empty and `df` is null.
- The constructor cannot declare `SyntaxError` (QParserPlugin.createParser does not), so the checked exception is wrapped in a
  plain RuntimeException: HTTP 500, message `org.apache.solr.search.SyntaxError: Neither qf nor df ...`.
- `DisMaxQParser.parse()` throws the same `SyntaxError` from `parse()`, which the query component turns into 400.

## Change
The wrapper becomes `SolrException(BAD_REQUEST, e.getMessage(), e)`, so the client sees 400 and the plain message.
New `TestExtendedDismaxParser.testNeitherQfNorDfIsBadRequest` builds the parser directly with `QParser.getParser` on a request
with only `q` (no handler, so the `df=text` initParams default is not applied).

## Guesses to verify first
- `req("q", "foo")` carries no default `df`; if the test harness injects one the test will not throw.
- A subclass that overrides `createConfiguration` is unaffected.
- Nothing else catches `RuntimeException` around parser construction expecting the old type (not searched beyond `getParser`).

## Fail-before
Expected: `testNeitherQfNorDfIsBadRequest` fails on `upstream/main` (the thrown exception is a plain RuntimeException, so
`expectThrows(SolrException.class, ...)` reports the wrong type).
