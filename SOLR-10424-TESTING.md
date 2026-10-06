# SOLR-10424 testing notes (hypothetical, nothing was compiled or run)

Ticket: with the techproducts configset, `curl .../update/json/docs --data-binary @books.json` indexes
only `id`, `_version_` and `_src_`. Cause (Noble, Alexandre in the ticket): `params.json` of
`sample_techproducts_configs` sets `mapUniqueKeyOnly: true` for the `_UPDATE_JSON_DOCS` param set, which
tells `JsonLoader` to keep only the uniqueKey and put everything else into `df` (`_text_` here).
Still present on `upstream/main`.

## Change
Remove `mapUniqueKeyOnly` from `_UPDATE_JSON_DOCS` in
`solr/server/solr/configsets/sample_techproducts_configs/conf/params.json`; `srcField` stays.

## Guesses
- The flag was meant as an example (SOLR-9557) of keeping the source; the user-visible effect is data
  loss, so dropping it is the less radical default, as Alexandre suggested.
- Nothing in the repo (BATS, ref guide examples for techproducts) depends on the flag; I only grepped for
  `mapUniqueKeyOnly`, and ref-guide examples that use it pass it explicitly.
- The test only inspects the JSON file via `ExternalPaths.TECHPRODUCTS_CONFIGSET`; it does not post
  documents end to end.

## Fail-before
Restore the line; `assertFalse` fails.

Test: `TechproductsJsonDocsParamsTest`.
