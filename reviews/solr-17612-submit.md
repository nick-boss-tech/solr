# solr-17612-submit

- Branch: origin/solr-17612-submit
- Head: cd0426e40a72 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base e2cdb2d7e8ae, 35 commits behind, 4 commits ahead)
- Scope: 4 commits, 4 files (+155/-31). `SpellCheckComponent.java` (per-shard `maxResultsByFilters` at `:304-330`, coordinator `sumMaxResultsByFiltersFromShards` at `:341-353`, fed into `maxResultsForSuggest` at `:465-470`), `DistributedSpellCheckComponentTest.java` (+18, the distributed case; read through the bulk anchors only), changelog `SOLR-17612-spellcheck-max-results-distributed.yml` (9 lines), `SOLR-17612-TESTING.md` (+31, author's handoff note, in the tree at the tip).
- Verdict: Not ready (the multi-filter fractional case is computed wrongly by the coordinator's sum)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-17612-review.md` (verdict Not ready) was written at the same head (cd0426e40a72). No delta. The file also has no separate SOLR-9060 section; SOLR-9060 has its own review file (`SOLR-9060-review.md`), reviewed separately.

- Bulk F1 (HIGH, sum of per-shard minima is not the global minimum across filters): **confirmed by the code and by arithmetic.** See finding 1.
- Bulk F2 (LOW, the TESTING file is in the outbound diff): **confirmed as a fact; the bulk's advice conflicts with the handoff.** See finding 2 and owner call 1.

## Findings (ranked)

1. **HIGH, verified. The coordinator sums per-shard minima, and the minimum is taken per shard first.**
   - Each shard computes `maxResultsByFilters` as the minimum over its own filters (`Math.min(s.size(), maxResultsByFilters)` in the `rb.getFilters()` loop, `SpellCheckComponent.java:~304-330`).
   - The coordinator adds those per-shard minima (`sumMaxResultsByFiltersFromShards`, `:341-353`), and the fraction is applied to that sum (`:465-470`).
   - Counterexample with two filters and two shards: shard A reports filter1=100, filter2=10 (minimum 10); shard B reports filter1=10, filter2=100 (minimum 10). The coordinator sums to 20. The global counts are filter1=110 and filter2=110, so the correct global minimum is 110. A fractional `maxResultsForSuggest` (for example 0.5) then sets the threshold at 10 instead of 55, which can suppress suggestions for a query that is under the true threshold.
   - The single-filter case is correct: the sum of per-shard counts is the total for that one filter, so the regression test (one `fq`) cannot show the bug.
   - Proposed fix (not applied): each shard reports its count per filter (for example a list in filter order, or a map keyed by the filter string). The coordinator sums per filter across shards, then takes the minimum over those sums. Add a distributed test with two `fq` values whose per-shard counts are skewed in the opposite direction, and a fraction whose outcome depends on the right total.

2. **LOW, verified. The author's testing note is in the outbound tree.** `SOLR-17612-TESTING.md` (+31) is in the branch's tree and diff. The bulk review suggests keeping it out of the contributor patch. The round-36 handoff says the opposite: "Leave the file in place; the Linux side removes it at ship time, after gating." The two instructions conflict, so the owner decides (owner call 1). The file is not a code defect.

## Owner calls (not decided here)

1. **TESTING file in the outbound patch.** The bulk review says remove it from the contributor patch. The round-36 handoff says leave it in place until the Linux side removes it at ship time. Pose it: which applies to this branch?

## Proposed fixes (not applied; the owner decides)

- Finding 1: per-filter reporting and per-filter coordinator sum, then minimum; two-filter distributed test with skewed shard counts (see finding 1).
- Finding 2: no code change; owner call 1.

## Interactions with other branches

- SOLR-9060 is in the same distributed spellcheck path (`SpellCheckComponent.java`). Its diff touches a different hunk (`extendedResults` at `:156-161`). The two branches are not reviewed together here; no shared code was found in the hunks read.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- `DistributedSpellCheckComponentTest.java` (the one-`fq` case): not read; the bulk anchor (`:161-177`) is taken as given.
- The `spellcheck.maxResultsForSuggest.fq` explicit-filter path: read only at its entry; its per-shard count is a single query, so the sum is correct for it.
- The JIRA text was not re-read; the "total across all shards" requirement is from the bulk review and the code comment at `:171-173`.
- No GitHub or JIRA writes were made.
