We checked this against Solr main at commit 498a09aea97 and could not confirm the result yet.

What we checked:

- DateRangeField is a spatial field type. Its indexed terms are shape cells, not the from and to dates of each range, and it has no uninverted type for term-based facets (AbstractSpatialFieldType).
- The reference guide says the range-faceting method dv is not supported for DateRangeField. It does not say that faceting on the field is unsupported.

How we tried to reproduce it:

- Not attempted. The reproduction needs a core with a DateRangeField and indexed ranges.

What this means:

- The empty list is consistent with the field's indexed terms, which do not hold the range values. Whether faceting should return the ranges, or the field should document that it is not for facets, is a decision for the Solr developers.

If you still see this:

- The Solr version, the field type definition, and the facet request would let us reproduce it on the current code. Please reopen with that information.
