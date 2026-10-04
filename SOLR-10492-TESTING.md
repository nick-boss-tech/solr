# SOLR-10492 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses. This ticket was originally skipped ("deep, no repro"); a later spot-check of the skips found a plausible root cause by reading the code.

- JIRA: https://issues.apache.org/jira/browse/SOLR-10492 - "problem with group faceting, facet.limit in solrcloud" (5.5.4, no comments). On 28 shards, `group.facet=true` with `facet.limit=50` returns un-grouped counts for `facet.field` (233 instead of 8), while `facet.query` is right, `distrib=false` is right, and `facet.limit=1000` is right.
- Branch: `solr-10492-submit` off `apache/solr main` (`upstream/main` at branch time)
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood (my reading of the code, not confirmed by a run)
When a shard's facet results are truncated by `facet.limit`, `FacetComponent` sends a refinement request per shard: `facet.field={!terms=...}field`. On the shard that is handled by `SimpleFacets.getListedTermCounts`, which computes each count as `searcher.numDocs(termQuery, baseDocset)`, i.e. plain document counts. Unlike `getTermCounts` it never looks at `group.facet`. The coordinator then takes these refined document counts in place of grouped ones. This explains the reporter's pattern: a high `facet.limit` means no refinement is needed, so counts stay grouped; `distrib=false` has no refinement either.

## What the branch changes
- `SimpleFacets.getListedTermCounts`: if `group.facet` is set for the field, counts come from the existing `getGroupedCounts` (a `TermGroupFacetCollector`) with a term filter limited to the requested terms; missing terms still get a 0. New private helper `getGroupedListedCounts`.
- `TestDistributedGrouping`: new loop (facet.limit 1 and 2, `group.facet=true`, `facet.overrequest.count=0`, `facet.overrequest.ratio=1`) compared against the control collection by `query(...)`.

## Why the existing coverage missed it
`TestDistributedGrouping` already runs `group.facet` with `facet.limit` 1 and 2, but default overrequest (`limit * 1.5 + 10`) means each shard returns all of its few terms, so no refinement ever happens. The new test disables overrequest so refinement is forced.

## What was guessed (verify these first)
1. That refinement is really what produces the ungrouped counts (I did not see the ticket's screenshots or logs).
2. That the test data has enough distinct `t1` values with groups spread across shards for refinement to trigger; if the fail-before check shows the test passes without the fix, add more terms or an extra test-specific field.
3. `getGroupedCounts` with `limit=-1`, `mincount=0`, `FACET_SORT_INDEX` and a term filter returns every requested term that occurs; terms absent from the base set are filled with 0 by the caller.
4. `ft.indexedToReadable` gives the same string as the refinement term for all field types (checked by reading, not run), which matters for numeric fields.
5. Grouped counts across shards are only exact when a group's docs are on one shard; that is a pre-existing group.facet limitation, not changed here.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.TestDistributedGrouping"
```
Fail-before: revert only `SimpleFacets.java`; the new queries should differ from the control collection.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
