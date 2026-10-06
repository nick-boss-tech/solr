# SOLR-7267 testing notes (hypothetical, nothing was compiled or run)

Ticket (2015): the sample configs name Czech field types, dynamic fields and the stopword file with `cz`
(country code); the ISO 639 language code is `cs`. Hoss (2015): the names come from the Lucene package
names, resolve LUCENE-6366 first. On main the `_default` configset still has only `text_cz`,
`*_txt_cz` and `lang/stopwords_cz.txt` (also `sample_techproducts_configs` for the type).

## Change (additive only)
- `_default/conf/managed-schema.xml`: new `text_cs` field type (same analyzer as `text_cz`) and
  `*_txt_cs` dynamic field, stopwords `lang/stopwords_cs.txt`.
- `_default/conf/lang/stopwords_cs.txt`: copy of `stopwords_cz.txt`.
- Nothing is renamed or removed, so existing collections and Schema API calls using `cz` keep working.

## Test
`DefaultConfigSetCzechTest` parses the configset XML and compares the stopword files; it does not load
the schema into a core.

## Guesses to verify first
- A duplicate type rather than a rename is what a maintainer wants (the rename would break users of `cz`).
  Another option is a deprecation comment on `cz`; not done.
- `ExternalPaths.DEFAULT_CONFIGSET` is resolvable in the test environment (SolrTestCase uses it).
- Other tests may count `_default` dynamic fields or compare it with another configset; none was found.
- The ref guide (language-analysis.adoc, Czech section) does not mention the field names; not edited.
- Not done: `sample_techproducts_configs` also has `text_cz` and was left alone.
