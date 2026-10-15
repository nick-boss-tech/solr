# Changelog-only job: SOLR-9124 title (wording the proof supports)

- Branch: solr-9124-submit. Head: 17a279d4dce1a8a3e41669d248b047489abd3cd6 (live tip, checked by ls-remote on 2026-10-14). The code proof stays at this head.
- Change: `changelog/unreleased/SOLR-9124-grouped-exact-stats.yml`, title only. Nothing else in the tree changes.
- Intended host: any Linux host with the `implementation` tag. Windows never claims this.
- Filed by the review lane on 2026-10-14 from the census round 1 follow-up (search components drafts). The review lane does not commit or push.
Status: DONE

## Why the title changes

The current title says the change makes "group scores match the ungrouped ones." The test that carries the proof does not compare grouped with ungrouped scores. `TestBaseStatsCache.checkGroupedScores` (`solr/core/src/test/org/apache/solr/search/stats/TestBaseStatsCache.java` lines 79-95 at the head) compares a grouped query on the control node with the same grouped query on the shards.

The title also names two cache classes. The recorded proof shows a failure only on `TestExactStatsCache` when the production change is reverted. `TestLRUStatsCache` passes on the base code too, so the proof does not show the change for the LRU cache.

## Replacement title

    Distributed grouped queries now send the merged global term statistics to the shards in the group stages, so grouped scores on a sharded query match the same grouped query on one node.

## Steps

1. Replace the title in the changelog file with the text above. Keep the `type`, `authors` and `links` lines as they are. Keep the author as Nick Shanin.
2. Parse the YAML. Record the result.
3. `git diff 17a279d4dce1 <new head> --stat` must show only the changelog file.
4. Commit with the workspace commit path (no trailers, no other author name). Push as a plain fast-forward to `solr-9124-submit` on the fork. Do not force.
5. No Gradle run is needed for a changelog-only change. Say so in the receipt. The code proof is still the one recorded at 17a279d4dce1.

## On green

- Refresh `receipts/SOLR-9124.md` so it names the new tip as the live tip. Label the code proof as the one at 17a279d4dce1.
- Mark this job DONE. Then the review lane can write the draft at the new tip.

## Result

Landed at 64b8256cde72665c7053d741e9bb572955afbaa7 (2026-10-14T09:42ZZ, vm2). Changelog YAML parsed OK; diff from 17a279d4dce1 is the changelog file only (1 line). The code proof is the one recorded at 17a279d4dce1.
