# PR draft: SOLR-18503 (for Nick to approve; not opened)

Status: DRAFT. Not opened. Proof pending the gate. Head: `solr-18503-submit` at `dba48d69b4d8f2c1f367e48fbcc884cce82420e6`. Base: apache/solr `main` at `c8ab71430f6e7547c773da596101d680862310ff`.

Title: SOLR-18503: Document JSON Facet parser options and aliases

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18503

## What happens today

**The guide omits parser-supported options, and range facets do not resolve `f`.**

At the merge base, the parser reads [`processEmpty`](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/core/src/java/org/apache/solr/search/facet/FacetParser.java#L73-L74), [facet-level `excludeTags`](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/core/src/java/org/apache/solr/search/facet/FacetParser.java#L209-L211), the [`query` alias](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/core/src/java/org/apache/solr/search/facet/FacetParser.java#L506-L508), and [`cacheDf`](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/core/src/java/org/apache/solr/search/facet/FacetParser.java#L564), while [`CountAgg` registers `count`](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/core/src/java/org/apache/solr/search/facet/CountAgg.java#L23). The pre-change guide does not describe them in its [terms and query sections](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L188-L232), [stat table](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L570-L580), [nested-facet section](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L662-L668), and [domain section](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L842-L849). The range parser reads `field` directly, so a range facet using only `f` has no field value ([merge-base code](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/core/src/java/org/apache/solr/search/facet/FacetRangeParser.java#L42)).

## What this change does

**The guide describes the accepted options, and range facets now resolve `f` like other field facets.**

The guide documents `f` for terms, range, and heatmap facets; `cacheDf`; `query` on query facets; `count()`; `processEmpty`; and facet-level `excludeTags` ([terms and query](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L191-L232), [range](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L312-L312), [heatmap](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L505-L505), [count](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L575-L575), [processEmpty](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L668-L668), [excludeTags](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc#L849-L849)). `FacetRangeParser` now uses the shared field helper, and the range test uses an `f`-only request with the existing expected bucket count ([parser](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/core/src/java/org/apache/solr/search/facet/FacetRangeParser.java#L42), [test](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/core/src/test/org/apache/solr/search/facet/TestJsonRangeFacets.java#L386-L387)).

## Proof

**Proof is pending the hardening gate.**

The gate will run `TestJsonRangeFacets` with only the changed test overlaid on the merge base, where the `f`-only range request is expected to fail, then run the focused class at this head. No test counts are claimed yet. Before opening, replace this section with the failure text, focused counts, verification date, and exact head from the gate receipt.

## A choice to check

**This patch keeps the parser-accepted aliases and documents them, while making `f` consistent for range facets.**

The alternative is to deprecate the `f` and `query` aliases. Was keeping them the right call?

## Limits

**`perSeg` remains undocumented because the source marks it experimental and for testing purposes only.** ([source comment](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/solr/core/src/java/org/apache/solr/search/facet/FacetField.java#L38-L40))

Changelog: [changelog/unreleased/SOLR-18503-json-facet-parser-options.yml](https://github.com/nick-boss-tech/solr/blob/dba48d69b4d8f2c1f367e48fbcc884cce82420e6/changelog/unreleased/SOLR-18503-json-facet-parser-options.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
