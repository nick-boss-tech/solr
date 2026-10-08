# solr-9060-submit

- Branch: origin/solr-9060-submit
- Head: 704ca28bf79d (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base e432df19c4a5, 23 commits behind, 2 commits ahead)
- Scope: 2 commits, 3 files. `SpellCheckComponent.java` (`extendedResults = shardRequest || params.getBool(SPELLCHECK_EXTENDED_RESULTS, false)`, `:159-161`: shard requests always report frequencies), `DistributedSpellCheckComponentTest.java` (+10 in the existing distributed test; +40 for `testShardMergeRanksTiesByFrequency`), changelog `SOLR-9060-spellcheck-shard-frequencies.yml` (`type: fixed`). No `SOLR-9060-TESTING.md` on the tip.
- Verdict: Not ready (the coordinator merge ignores the configured frequency comparator, and the changelog overstates the ordering the patch gives)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-9060-review.md` (verdict Not ready) was written at the same head (704ca28bf79d). No delta.

- Bulk F1 (HIGH, the coordinator merge does not honor `comparatorClass=freq`): **confirmed by the code.** See finding 1.
- Bulk changelog point (overstated): **confirmed.** See finding 2.
- Bulk test-limitation point: **confirmed.** See finding 3.

## Findings (ranked)

1. **HIGH, verified. The coordinator merge builds its queue with the default comparator.** `SolrSpellChecker.mergeSuggestions` (`SolrSpellChecker.java:88`) creates `new SuggestWordQueue(numSug)` (`:118`), the one-argument constructor. The configured comparator is not passed in, and this file is not in the branch's diff. The branch only changes which frequencies the shards return (`SpellCheckComponent.java`, `extendedResults = shardRequest || ...`). So for `comparatorClass=freq` the merged order is still the default order: score first, with frequency only as a tie-break. Lucene's one-argument `SuggestWordQueue` constructor uses its default score comparator; that Lucene source was not re-read here. The JIRA packet says frequency-first ordering is the expected cloud behavior for `comparatorClass=freq` (from the bulk review; not re-read here).
   - Proposed fix (not applied): pass the configured comparator (the one the shard-side spell checker uses) into `mergeSuggestions`, and build the coordinator queue with it.

2. **MEDIUM, verified. The changelog overstates the guarantee.** `changelog/unreleased/SOLR-9060-spellcheck-shard-frequencies.yml:8` says distributed spellcheck "now ranks merged suggestions by frequency even when spellcheck.extendedResults is false". That holds only for the default comparator's frequency tie-break (finding 1). With `comparatorClass=freq`, the ranking is still score-first.

3. **MEDIUM, verified in the test. The new test exercises the default comparator only.** `testShardMergeRanksTiesByFrequency` (diff lines 55-93) uses four candidates with equal scores and frequencies 2, 5, 8, 11, and queries `/spellCheckCompRH` and `/spellCheckCompRH_Direct` with the default dictionary. It does not select the freq dictionary, and it has no candidates whose scores differ. So it proves the default tie-break, not the `comparatorClass=freq` ordering the ticket asks for. Bulk anchors for the configs: the default spelling dictionary at `solrconfig-spellcheckcomponent.xml:68-75`, the freq comparator at `:142-150`. Those lines were not re-read here.

4. **LOW, verified. The existing distributed test was extended, not replaced.** The added `query(...)` calls without `extendedResults` (diff lines 38-47) compare coordinator output with the same handler. They do not check ordering against a freq-dictionary expectation. Harmless, but not evidence for finding 1.

## Owner calls (not decided here)

1. **Expected cloud ordering for `comparatorClass=freq`.** The JIRA packet (per the bulk review) expects frequency-first. The fix in finding 1 changes the coordinator ordering for every spellchecker that uses a non-default comparator. Confirm the intended ordering before the test is written, and before the changelog wording is chosen.

## Proposed fixes (not applied; the owner decides)

- Finding 1: pass the configured comparator into `mergeSuggestions` and use it for the coordinator queue.
- Finding 2: reword the changelog to the actual guarantee once finding 1 is settled.
- Finding 3: add a test with the freq dictionary and candidates whose scores differ, asserting frequency-first order.

## Interactions with other branches

- SOLR-17612 touches the same distributed spellcheck component (`SpellCheckComponent.java`) at a different hunk. Not reviewed together here; no shared code in the hunks read.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- Lucene's `SuggestWordQueue` default comparator (finding 1): taken as score-first from its use here, not re-read in Lucene source.
- The spelling config lines cited by the bulk review (`solrconfig-spellcheckcomponent.xml:68-75`, `:142-150`): not re-read.
- The JIRA packet text: not re-read; the frequency-first expectation is from the bulk review.
- No GitHub or JIRA writes were made.
