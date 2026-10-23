# PR draft: SOLR-10928 (for Nick to approve; not opened)

Status: DRAFT. Not opened. Opening needs Nick's approval. Head: `solr-10928-submit` at `f2001e640a464e8e861ee3c202821b6dcc4bd497`, base Apache `main` at `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Gate: GREEN at this head (`receipts/SOLR-10928.md`).

Title: SOLR-10928: Support separate query text for query elevation

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-10928

## What happens today

**An `elevate.xml` entry for `brain` matches `q=brain`, but not `q=indexingabstract:brain`.**

At the base, [the lookup key](https://github.com/nick-boss-tech/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/handler/component/QueryElevationComponent.java#L548-L568) is parser-local `v`, or `q` when local parameters are absent. There is no separate request value for the simpler elevation key.

## What this change does

**`elevate.q` supplies lookup text without changing the search query.**

It takes precedence over `v` and `q` for elevation matching; if omitted, the existing fallback remains. Explicit `elevateIds` and `excludeIds` still take precedence ([production path](https://github.com/nick-boss-tech/solr/blob/f2001e640a464e8e861ee3c202821b6dcc4bd497/solr/core/src/java/org/apache/solr/handler/component/QueryElevationComponent.java#L548-L573)). The [guide](https://github.com/nick-boss-tech/solr/blob/f2001e640a464e8e861ee3c202821b6dcc4bd497/solr/solr-ref-guide/modules/query-guide/pages/query-elevation-component.adoc#L139-L152) covers request placement, fallback, precedence, and empty values.

## Proof

**Mutation proofs catch broken override and parser fallback; the four-class set passes on two seeds.**

- [QueryElevationComponentParamTest](https://github.com/nick-boss-tech/solr/blob/f2001e640a464e8e861ee3c202821b6dcc4bd497/solr/core/src/test/org/apache/solr/handler/component/QueryElevationComponentParamTest.java#L28-L62): 4/4.
- [QueryElevationComponentTest](https://github.com/nick-boss-tech/solr/blob/f2001e640a464e8e861ee3c202821b6dcc4bd497/solr/core/src/test/org/apache/solr/handler/component/QueryElevationComponentTest.java#L138-L250): 23/23; seven full-request cases cover precedence, fallback, query independence, and interactions.
- [DistributedQueryElevationComponentTest](https://github.com/nick-boss-tech/solr/blob/f2001e640a464e8e861ee3c202821b6dcc4bd497/solr/core/src/test/org/apache/solr/handler/component/DistributedQueryElevationComponentTest.java#L77-L96): 1/1 across three shards.
- [TestReRankQParserPlugin](https://github.com/nick-boss-tech/solr/blob/f2001e640a464e8e861ee3c202821b6dcc4bd497/solr/core/src/test/org/apache/solr/search/TestReRankQParserPlugin.java#L43-L43): 11/11.

`elevate.q` is new, so the request tests cannot run unchanged on base. Forcing the old fallback made exactly the two override tests fail; disabling parser-local fallback made the fallback test fail. The four-class set passed 39/39 on each seed (`10928E1E10928E1E`, `10928E2E10928E2E`), verified 2026-10-23 at `f2001e640a464e8e861ee3c202821b6dcc4bd497`. Changelog parsing, tidy, Error Prone, `:solr:core:check -x test`, and root `check -x test` passed.

## Limits

**The full test suite and SolrCloud failover or restart behavior were not tested.**

The three-shard test checks request propagation, not failover or restart. The override was not combined with every parser, grouping, cursor, subset-rule, rerank, and combined-query option.

Changelog: [changelog/unreleased/SOLR-10928-elevate-q.yml](https://github.com/nick-boss-tech/solr/blob/f2001e640a464e8e861ee3c202821b6dcc4bd497/changelog/unreleased/SOLR-10928-elevate-q.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
