# SOLR-8003 - hypothetical reproduction (nothing was compiled or run)

JIRA: `fl=*_json:[json]` returns 400 "Error parsing fieldname: Expected identifier at pos 0"; the reporter wants every field
ending in `_json` returned raw. Earlier audit note: "fl glob with transformer, feature" and "needs a per-field transformer
design". The design chosen here is a glob-level raw marker, not one transformer per field.

## Change
- `SolrReturnFields.add`: after a glob, `:` followed by `[name ...]` is parsed like an ordinary augmenter. The factory must be a
  `RawValueTransformerFactory` (`[json]`, `[xml]`), otherwise 400 "A glob can only be combined with a raw value transformer".
  The glob is registered as a normal glob (so all fields load and `wantsField` filters), or sets `wantsAllFields` for `*`.
- `RawValueTransformerFactory.createForGlob` returns a `GlobRawTransformer` (no document change) unless the factory's `wt`
  does not apply to the request (then the fields come back unmodified, like `field:[xml]` with `wt=json`). The `wt` check was
  extracted into `appliesTo`.
- New `DocTransformer.getRawFieldGlobs()` (default empty; `DocTransformers` concatenates children). `TextResponseWriter`
  keeps the globs next to `rawFields`; `shouldWriteRaw` also tests `GlobPatternUtil.matches`.
- Only wt json and xml use `shouldWriteRaw`; other writers ignore it as before.

## Test
`TestRawTransformer.testGlobJsonTransformer`: `id,su*:[json],link?:[json]` writes `subject` and `links` raw and omits `author`;
`au*:[xml]` with `wt=json` returns the plain string; `su*:[docid]` is a 400.

## Guesses to verify first
- `getGlobbedId` returns `su*` and leaves `sp.pos` on `:`; `QueryParsing.parseLocalParams(rest, 0, ...)` with `[`/`]` returns
  the offset past `]` (copied from the existing augmenter branch).
- `GlobPatternUtil.matches("link?", "links")` is true (`?` single char); `su*` matches `subject`.
- A `RawShimTextResponseWriter` is created whenever any raw field or glob exists (was: only raw fields).
- The `[docid]` factory exists in the test core (`DocIdAugmenterFactory`), otherwise the 400 comes from a null factory path
  (still a `SolrException`? a null factory also fails the instanceof check, so the 400 holds either way).
- Spotless: the long assertFalse line in the test needs `spotlessApply`.
- Not covered: the CSV/javabin writers, `renameFields` combined with a glob, nested child documents.

## Fail-before
Expected: on main the glob form throws "Error parsing fieldname" (400) for the first request, so the CLIENT call fails.
