# Skip record: SOLR-12460

- Ticket: SOLR-12460, Filtercache getting filled up when domain switches are involved in Json facets. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments.
- Verdict: keep-open.
- Draft branch: solr-12460-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2026-10-04, audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: a JSON facet with a join domain (domain: join from vin_l to vin_l, filter doc_type_s:claim) calls getDocSet on the join query, which is cached in the filterCache. The reporter says repeated executions of the same facet request do not reuse some of the cached join entries, and that the problem appears with more than one shard (the collection has 10 shards). The cache key shows a join over a "ConstantScore(BitSetDocTopFilter)" with "{!cache=false}" on the inner query. The full text was read from the Jira REST issue record; the evidence tool output is truncated at 1200 characters.

## Code reading

- solr/core/src/java/org/apache/solr/search/facet/FacetProcessor.java:238-243 (handleJoinField): calls fcontext.searcher.getDocSet(domainQuery) with the domain query built from the current base docset.
- solr/core/src/java/org/apache/solr/search/facet/FacetRequest.java:260-273 (JoinField.createDomainQuery): wraps fcontext.base.makeQuery() in a WrappedQuery, calls setCache(false) on the wrapper, and passes it to JoinQParserPlugin.createJoinQuery. So the inner base query is marked uncached, but the outer join query is still a normal query given to getDocSet, and the filterCache keys it.
- solr/core/src/java/org/apache/solr/search/WrappedQuery.java:60-69: hashCode and equals delegate to the wrapped query.
- solr/core/src/java/org/apache/solr/search/DocSetQuery.java:57-67: equals and hashCode use the wrapped docSet, so the cache key depends on the content of the base docset that the domain was computed from.
- solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java:249-263 (createJoinQuery): returns the join query built from the wrapped base; no cacheability change is made here.

So the cache key for the join domain still depends on the base docset of the facet request. Different base sets (for example, each shard's local base, or each bucket's base) produce different keys, and the entries are added to the filterCache as the ticket describes. No code at the pin bypasses the filterCache for this domain query.

## History

- No commit at the pin names SOLR-12460 (git log --grep at the pin: none).
- The file history of FacetRequest.java and FacetProcessor.java was not traced in this pass; the commits listed by the evidence tool for the file-level path are not fixes for this ticket.

## Reproduction attempts

Not run. A reproduction needs a multi-shard collection (the ticket uses 10 shards) and a repeated JSON facet with a join domain, comparing the filterCache lookups and hits for the second run at the pin. Not built or run in this pass.

## Gap analysis

The cache behavior the ticket describes is consistent with the code at the pin: the join domain query is cached under a key derived from the base docset, and the inner uncached wrapper does not stop the outer entry from being stored. Whether the second run misses on the join entries in a live 10-shard run is not established. No fix commit or cache bypass was found.

## Verdict justification

keep-open: the mechanism is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made. Whether the cache behavior should change (for example, bypassing the filterCache for a base-dependent join domain) is a design decision.

Missing for a closing verdict: a multi-shard run at the pin that records filterCache lookups and hits for a repeated join-domain facet, and a maintainer decision on whether base-dependent join domains should be cached.

## Limits

This record does not run a multi-shard facet request, does not trace the file-history diffs, and does not post anything to JIRA or GitHub.
