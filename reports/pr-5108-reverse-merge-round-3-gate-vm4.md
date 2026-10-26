# PR 5108 reverse merge round 3: Linux gate report (vm4)

Status: GREEN at F2 `495d3fbdbe07bfb474b43d77b89f8c033bbeb8fd`. Receipt: `receipts/SOLR-17786-round-3-vm4.md`.
Gate: `gates/pr-5108-reverse-merge-round-3.md`. Superseding job: `gates/pr-5108-reverse-merge-round-3-f2-vm4.md`. Claim: `claims/pr-5108-reverse-merge-round-3-vm4.md`. Heartbeats: `hosts/vm4.md`.

## What was proven

- The two round-3 regressions (`testReverseCursorMergeAcrossShards`, `testReverseCursorMergeWithTiesAcrossShards`) pass at F2 under seeds `5108C0DE` and `17786C0DE`, and again in the final focused run.
- Error Prone compile, existing cursor and ordering suites (18 tests), and solrj `QueryResponseTest` (7 tests) pass at F2, all with 0 failures, 0 errors, 0 skips.
- `check -x test` for core and solrj passes at F2, and the tree stays clean. The `git diff --check` at F2 is clean.
- Two coordinator-only mutations in `QueryComponent.mergeIds` each make the new tests fail on the selected document IDs. M1 (comparator) fails both tests. M2 (unique-key tiebreaker) fails the tied test.

## Mutation evidence (fresh XML)

| Mutation | Test | Expected selected | Actual selected |
| --- | --- | --- | --- |
| M1 | WithTiesAcrossShards | doc107_3, doc108_0, doc109_2 | doc103_0, doc106_0, doc109_2 |
| M1 | AcrossShards | doc107_3, doc108_0, doc109_2 | doc107_3, doc108_0, doc103_0 |
| M2 | WithTiesAcrossShards | doc107_3, doc108_0, doc109_2 | doc108_0, doc106_0, doc109_2 |

Patches (host paths): `/workspace/gates/logs/vm4/pr-5108-round-3/m1-patch.diff` and `m2-patch.diff`. Logs and XML: `/workspace/gates/logs/vm4/pr-5108-round-3/` (`f2-*.xml/`, `m1-focused-5108.xml/`, `m2-tie-5108.xml/`).

## Heads and history

- Original round-3 head `c5a16393d2` failed the tidy check by one line. The repair commit `495d3fbdbe` (formatting only) was pushed fast-forward to `solr-17786-reverse-cursor-submit`, and a superseding job pinned it.
- Round-2 receipt `receipts/SOLR-17786.md` (vm2, `fde07028`) is preserved unchanged.
- Public apache/solr PR 5108 remains at `32f80171030ca1108cd20292f50232d2729ed930`. Nothing was posted or opened.

## Limits

- The check leg is module-level with tests excluded, not a full repository run.
- Each test leg was prefixed with `cleanTest` to force fresh execution.
- The first tidy preflight attempt failed on runner arguments before tidy ran; its logs were replaced by the rerun.
- No Windows build or test was run.

## Suggested PR description draft (for Nick to review, not posted)

Adds two deterministic two-shard regressions for reverse cursor merges (`testReverseCursorMergeAcrossShards`, `testReverseCursorMergeWithTiesAcrossShards`). The tests place documents on exact shards, then check that a backward page selects the globally correct documents across shards, including ties on the primary sort. On Linux at `495d3fbdbe`, both pass under two seeds, the cursor and ordering suites pass, and `check -x test` passes. Coordinator-only mutations that change the merge comparator, or stop inverting the unique-key tiebreaker, each cause the new tests to select different documents. Test-only change, no production code touched. Whether to open or update a PR is Nick's decision.
