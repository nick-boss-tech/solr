# SOLR-10252 testing notes (config comment only; no test, nothing run)

Ticket: the `_default` configset spellchecks on `_text_` (copyField catch-all) with `text_general`, which
applies stopwords and synonyms; a large synonym list explodes the spellcheck query. The ticket's
discussion (James Dyer) suggests at least a comment explaining the tradeoff; a different field would
need a schema change and no safe field is known to exist in an arbitrary collection.

## Change
An XML comment above the spellchecker definitions in
`solr/server/solr/configsets/_default/conf/solrconfig.xml`. No behaviour change.

## Guesses
- A comment (rather than a new `_spell_` copyField/fieldType) is what a maintainer will accept; a schema
  change would touch every `_default` user and tests that count fields.
- The configset is parsed by tests (comments are ignored), so nothing should break.

## Fail-before
Not applicable: documentation only. `sample_techproducts_configs` has a different spellcheck setup and is unchanged.
