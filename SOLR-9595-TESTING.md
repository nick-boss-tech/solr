# SOLR-9595 - hypothetical reproduction (nothing was compiled or run)

JIRA (Yonik, Smiley +1): `SlowCompositeReaderWrapper` recreates `Multi*` views (MultiDocValues, MultiTerms, MultiBits)
on every call instead of caching them. On `upstream/main` the ordinal maps are cached (SOLR-12878 era) but
`terms(field)`, `getLiveDocs()` and the numeric/binary/norms getters still carry `// TODO cache?`.

## Change
- `terms(field)` caches the non-null `MultiTerms.getTerms(in, field)` result in a `ConcurrentHashMap`
  (a missing field is not cached, it stays a cheap null).
- `getLiveDocs()` returns a `Bits` built once in the constructor.
- The doc values and norms getters are deliberately NOT cached: they return stateful, single-threaded iterators
  that cannot be shared between requests. Their `TODO cache?` comments stay.

## Test (guessed)
`TestSlowCompositeReaderWrapper.testTermsAndLiveDocsAreCached`: two segments, one deleted doc; `assertSame` on
repeated `terms("f")` and `getLiveDocs()`, `assertNull` for an unknown field, liveDocs content checked.

## Guesses to verify first
- `Terms` from `MultiTerms.getTerms` is safe to share across threads (stateless, `iterator()` makes a fresh enum).
- No caller relies on `getLiveDocs()` of the wrapper being a fresh object (e.g. mutating it); `Bits` is read-only.
- Memory: one `Terms` per queried field per wrapper; the wrapper is per searcher, so it is bounded by field count.
- `MultiBits.getLiveDocs(in)` in the constructor is O(leaves); `in` is open at that point.

## Fail-before
Reverting the change makes both `assertSame` calls fail (new objects each call).
