# SOLR-17612 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-17612 - "spellcheck.maxResultsForSuggest percentage is flawed when multiple shards" (David Smiley, 2025). Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-17612-submit` off `apache/solr` main `e2cdb2d7e8ae`

## The bug, as understood
A fractional `spellcheck.maxResultsForSuggest` means "this share of the docs matching the filter(s)". `SpellCheckComponent.maxResultsForSuggest(rb)` evaluates the filter doc set against `rb.req.getSearcher()`, which in `finishStage` is the coordinator core's searcher: empty with the coordinator feature, or an unrepresentative slice otherwise. The threshold is then too small and `correctlySpelled` is wrongly true.

## What the branch changes (follows the fix shape in the ticket)
- Each shard (IS_SHARD request) with a fractional value now reports its local filtered doc count under `spellcheck.maxResultsByFilters` in its response, and does not apply the fraction itself (it passes `null`, so it always computes suggestions).
- `finishStage` sums the shard values and uses that as the base: `round(sum * fraction)`. If no shard reports one it falls back to the old local evaluation.
- `maxResultsForSuggest` split into `maxResultsForSuggest(rb, override)`, `maxResultsByFilters(rb)` (returns null when there is no filter) and `isFractionalMaxResultsForSuggest`.
- Test: a new `query(...)` in `DistributedSpellCheckComponentTest.test` with `maxResultsForSuggest=1.5` and `fq=lowerfilt:quote` (12 matching docs), comparing control vs distributed.

## Guesses to verify first
1. The new test really fails without the fix: it needs the coordinator's local share of the 12 docs times 1.5 to be below the hit count (12) so suggestions get suppressed. With a lucky distribution it could pass anyway; raise the fraction or the doc count if so.
2. `SpellCheckResponse` (SolrJ) tolerates the extra `maxResultsByFilters` key in the shard response; and the key does not leak into the client-visible response (finishStage builds a new `spellcheck` section, so it shouldn't).
3. `getSubsectionFromShardResponse(..., true)` returns null for shard responses without a spellcheck section.
4. Shard-side whole-number values are unchanged (still compared to the shard's local hits), which is arguably also wrong; left alone.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.component.DistributedSpellCheckComponentTest"
```
Fail-before: revert `SpellCheckComponent.java` only.

## Not done
No JIRA comment, no PR.
