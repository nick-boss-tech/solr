This is answered by the reference guide. We checked against Solr main at commit 498a09aea9.

The implicit pre-filter for dense vector search includes all fq filters by default. To leave tagged fq filters out of the pre-filter for a facet drill-down, use the excludeTags option on the knn query parser. The dense vector search page describes this exact case: https://solr.apache.org/guide/solr/latest/query-guide/dense-vector-search.html (the includeTags and excludeTags options, and the use case where users drill down on facets).

Solr tests this option on the knn query parser (KnnQParserTest, knnQueryWithFilterQuery_localParamIncludeExcludeTags).

If you want the default pre-filter to skip facet filters without the extra parameter, that is a change in behavior and needs a decision from the maintainers. If you still see a problem with excludeTags, please reopen the ticket with the query, the fq tags, and the Solr version.
