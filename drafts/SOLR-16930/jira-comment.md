We checked this against Solr main at commit 498a09aea9. The behavior in the report is real, and the decision on what to do about it is still open, so the ticket stays open.

What we checked:

- When a schema names a factory with the "solr." prefix, Solr looks up the factory by its short name and the kind that was expected by the field type. The kind written in the name (here, Tokenizer) is not compared with the expected kind (here, TokenFilter).
- So a tokenizer name used in a filter position loads the filter with the same base name. That is the behavior in the report.

What this means: rejecting the mismatch would be stricter, but it would stop some existing schemas from loading. Keeping the leniency and documenting it is the other option. The maintainers need to choose.

If you still see this: please tell us whether you rely on the mismatch in a schema, so that a stricter check would be sized against real configs.
