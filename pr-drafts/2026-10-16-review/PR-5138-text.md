# PR #5138 draft: SOLR-7520 wider change named, trimmed (review item 6)

- Written against head: 15cc9ba5a63cd79518a72bda1fc67539956f5c31 (live branch solr-7520-submit, read through the API 2026-10-17).
- Visible characters: 2,929 (body; markdown syntax and link URLs removed). Limit 3,500.
- Proof numbers: receipts/SOLR-7520.md, section "Full gate at the live head, vm2, 2026-10-15, at 15cc9ba5a6" (AnalyticsMergeStrategyTest 2 of 2 at head, fresh XML, and the proof-leg messages) and the formatting-join section for the re-verification at the same head.
- Wider change checked in code at 15cc9ba5a6: CommandHandler.java lines 262 to 266 call `filter.postFilter.complete()` in a `finally` whenever `filter.postFilter` is set. The call is not limited to AnalyticsQuery.
- The changelog fragment and the tests do not yet say this. That edit is owed to the branch owner. This draft changes the body only.
- Not applied to the live PR. Drafts only, per the assignment.

=== TITLE ===

(unchanged) SOLR-7520: The grouped path now calls `complete()` on the post filter after each shard search, as the non-grouped path does

=== BODY ===

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-7520

## What happens today

**A grouped distributed search never completes its post filter on the shards.**

The Jira report describes a query with an `fq` post filter, several shards, and grouping. The post filter's results are missing from the response. In [`CommandHandler.searchWithTimeLimiter`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/grouping/CommandHandler.java#L254-L257) at base commit `14c7aac0d15`, the post filter is set as the collector, the search runs, and nothing calls `complete()` on the filter afterwards. The non-grouped path in [`SolrIndexSearcher`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java#L330-L335) does call `complete()`. The ticket names the method `finish`; the current name is `complete()`.

## What this change does

**Both distributed grouping phases now call `complete()` on the post filter after each shard search, as the non-grouped path does. This applies to every post filter, not only AnalyticsQuery.**

The first phase (`group.distributed.first`) and the second phase (`group.distributed.second`) both run `CommandHandler`. The search in [CommandHandler.java](https://github.com/nick-boss-tech/solr/blob/15cc9ba5a63cd79518a72bda1fc67539956f5c31/solr/core/src/java/org/apache/solr/search/grouping/CommandHandler.java#L259-L268) runs inside a `try`, and its `finally` calls `complete()` on whatever post filter is set. The coordinator code is not changed.

## What the coordinator returns

**A distributed grouped response still has no `analytics` section, and this change does not add one.**

Merge strategies run only for ungrouped responses. `handleGroupedResponses` runs no merge strategy, so the coordinator never combines the shard sections. This change puts the section into shard responses only, and the coordinator's grouped result is the same as before. A merged `analytics` section for grouped requests needs a design decision first, because both phases now carry the section. I suggest a separate ticket for that.

## Proof

**The first-phase assertion fails on the base code and passes with this change.**

The [AnalyticsMergeStrategyTest](https://github.com/nick-boss-tech/solr/blob/15cc9ba5a63cd79518a72bda1fc67539956f5c31/solr/core/src/test/org/apache/solr/search/AnalyticsMergeStrategyTest.java#L48-L117) tests check three things. The first-phase and second-phase requests to one shard each expect `mycount` 3. A grouped request through the coordinator expects 11 groups and 11 matches.

- Head `15cc9ba5a6`, vm2, 2026-10-15: AnalyticsMergeStrategyTest 2 of 2 pass, counted from fresh JUnit XML.
- Full gate at this head: with `CommandHandler.java` reverted to the merge base `14c7aac0d151`, the two tests fail with "analytics section missing for grouped shard query" and "analytics section missing for grouped second phase shard query". The focused run and the module check pass.

## Limits

- The coordinator gap above remains. The coordinator check covers grouped counts, not a merged `analytics` value.
- The shard checks read shard 0 only. The second-phase check uses fixed top-group values, `5` and `50`.
- Ungrouped requests and standalone grouped requests (without `group.distributed.*`) are unchanged.

Changelog: [changelog/unreleased/SOLR-7520-grouping-postfilter-complete.yml](https://github.com/nick-boss-tech/solr/blob/15cc9ba5a63cd79518a72bda1fc67539956f5c31/changelog/unreleased/SOLR-7520-grouping-postfilter-complete.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
