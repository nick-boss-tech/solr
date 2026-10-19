# Skip record: SOLR-16930

- Ticket: SOLR-16930, "schema short class name support can use factories w/different names then specified name". JIRA status Open, resolution none, created 2023-08-11, last updated 2023-10-15, checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16930-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

A field type declares `solr.EdgeNGramTokenizerFactory` as a `TokenFilter` in its index analyzer. The reporter expected a failure (the class is a tokenizer, not a filter) but the schema loads and the field gets an `EdgeNGramTokenFilter`. The reporter suspects that the short-name lookup takes the name and ignores the kind. Comment: hossman (2023-08-11) lists the SPI names in Lucene's branch_9x that are used by more than one factory kind.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:466-470`: `legacyAnalysisPattern` is `((solr\.|<base>.analysis.)) ([A-Za-z_][A-Za-z0-9_]+?)(TokenFilter|Filter|Tokenizer|CharFilter)Factory`. Group 4 is the name; group 5 is the kind suffix.
- `SolrResourceLoader.java:515-526`: when the cname matches the pattern, the lookup uses only group 4 and the expected type: `CharFilterFactory.lookupClass(name)`, `TokenizerFactory.lookupClass(name)`, or `TokenFilterFactory.lookupClass(name)`, chosen by `expectedType`. Group 5 (the kind written in the config) is not compared with `expectedType`.
- For `solr.EdgeNGramTokenizerFactory` with expected type `TokenFilterFactory`, group 4 is `EdgeNGram` and group 5 is `Tokenizer`. The lookup becomes `TokenFilterFactory.lookupClass("EdgeNGram")`, which resolves to the Lucene token filter. That is the reported behavior.
- The shim returns the class on success; on `IllegalArgumentException` it falls back to the legacy path.

So the mismatch is accepted by design of the shim at this head. The code does not check that the kind in the config name matches the expected factory kind.

## History

- `c99af207c7` (2022-03-04, SOLR-14920, formatting only) and `d172b2c7ae` (2021-12-05, SOLR-15826, simplification) are the recent changes to `findClass`. The pattern and lookup logic predate them (`515608a087`, 2020-09-17, SOLR-14151, classloading fix).
- No commit on the fork names SOLR-16930 (empty `git log --grep`).

## Reproduction attempts

Not attempted. The mechanism is shown by the pattern and the lookup at the lines above. A run that would show it on main is a schema with the tokenizer name in a filter position, loaded and inspected through the analysis request handler, which the reporter already did. The missing piece is the decision below, not a reproduction.

## Gap analysis

The shim accepts a wrong-kind name because it never compares group 5 with the expected type. Changing that (rejecting the mismatch, or only allowing the matching kind) would make configs that currently load fail to load. Configs that rely on the leniency exist in the wild (the reporter found one). Whether to break them is a compatibility decision for the maintainers, not a code-path question. The ticket's own comments say the maintainers are unsure it is worth fixing.

## Verdict justification

keep-open: the mechanism is shown with file and line at the pinned head, and the missing piece is the compatibility decision (reject the mismatch, which may break existing schemas, or keep the leniency and document it). pr-needed needs that decision made; it is not made by the code or the ticket. close-answered and close-fixed do not apply: the question is not answered by the docs, and nothing is fixed. The keep-open minimum is met.

## Limits

This record does not check every factory name that the Lucene SPI maps to more than one kind. It does not check existing schemas in the wild. It does not change behavior.
