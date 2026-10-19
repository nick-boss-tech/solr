# Skip record: SOLR-5383

- Ticket: SOLR-5383, Group Faceting does not play nice with Trie fields that use precsionStep. JIRA status as recorded in the B1 reading: Open. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-5383-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B1 reading (reports/2026-10-18-skip-repro/B1-removed-round-1.md, vm3, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Group Faceting does not play nice with Trie fields that use precsionStep.

## Code reading

Group-facet path getGroupedCounts (SimpleFacets.java:775-835) has no trie-prefix filter. getNumericHidingWrapper (:836-850) hides only single-valued non-DV numerics. Missing: whether Lucene's TermGroupFacetCollector enumerates precision-step terms (Lucene source is not in this repo), or a head reproduction with a multiValued trie field, precisionStep above 0, and group.facet=true. The reproduction needs a build, which this item does not allow.

## History

- None cited in the B1 reading.

## Reproduction attempts

Not attempted. The B1 scope ran no builds or tests.

## Gap analysis

The code reading gives a verdict but misses the piece noted above.

## Verdict justification

keep-open: the code reading does not settle the question alone. The B1 reading names a missing piece that needs a build or a source check.

Missing for this verdict: The missing piece noted in the code reading below.

## Limits

This record does not establish a reproduction. Commit linkage is by code path where the commit subject does not cite the ticket. It does not post anything to JIRA or GitHub.
