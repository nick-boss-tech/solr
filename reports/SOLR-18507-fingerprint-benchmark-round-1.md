# SOLR-18507 fingerprint benchmark, round 1 (item 1, partial)

Status: PARTIAL, handed back for direction (written 2026-10-20T10:34:28Z by vm2). Item 1 asks for flush and merge costs, retained memory, persisted metadata size, and a separate max-version case. None of those were measured in this round, so no optimization claim is made.

- Assignment: `assignments/pool-solr-18507-fingerprint-benchmark-round-1.md`. Claim: `claims/pool-solr-18507-fingerprint-benchmark-round-1.md`.
- Heads: Phase 2 `137e77a4c8baa2684ea758edb6bbaa6bda7b7de7` (gate-green phase 2 head) and Phase 3 `ad706640ffd62907961749dc9319ca8bff35bbb0` (gate-green phase 3 head, gate job `gates/SOLR-18507-phase-3.md`). Both core jars built from detached worktrees (`solr/core/build/libs/solr-core-11.0.0-SNAPSHOT.jar`). The solrj, api and solrj-* modules have no diff between the two heads and are shared.
- Host: 4 CPUs, 7 GiB RAM, Linux 6.1, OpenJDK 21.0.12.1, `-Xmx2g`. Single host, shared with other pool activity (load about 1 during the run). Phase 2 and Phase 3 legs ran on the same host, one after the other.
- Harness: `reports/data/SOLR-18507-fingerprint-benchmark-round-1-Bench.java.txt` (stored as text; not a Java source on this branch) (throwaway, outside the product branches). Each cell builds an index with EmbeddedSolrServer (one doc per add, commit every 5000 docs, then deletes of a seeded random set using `Random(18507)`), closes the container, then runs a fresh JVM that reopens the core and times `IndexFingerprint.getFingerprint(core, Long.MAX_VALUE)`: one cold call and three warm calls. Driver: `reports/data/SOLR-18507-fingerprint-benchmark-round-1-run-matrix.sh`. Config: the `_default` configset from the Phase 3 worktree.
- Matrix: sizes 10,000, 100,000, 1,000,000 docs; delete ratios 0, 0.01, 0.10, 0.50; 5 fresh-JVM reps per cell; 480 rows. Raw rows: `reports/data/SOLR-18507-fingerprint-benchmark-round-1.csv` (columns phase, docs, delratio, kind, rep, millis, liveDocs). Live-doc counts match the deletes in every cell.

## Results (medians in ms; min and max across reps)

| docs | deletes | kind | Phase 2 median (min-max) | Phase 3 median (min-max) |
|---|---|---|---|---|
| 10k | 0% | cold | 22.1 (18.6-29.5) | 12.0 (11.2-28.5) |
| 10k | 50% | cold | 20.9 (18.8-32.2) | 11.6 (6.8-12.9) |
| 100k | 0% | cold | 36.0 (31.5-57.6) | 14.3 (11.6-27.3) |
| 100k | 50% | cold | 36.3 (30.6-41.4) | 19.2 (6.7-24.4) |
| 1M | 0% | cold | 92.1 (42.1-106.8) | 16.2 (12.9-29.0) |
| 1M | 1% | cold | 75.8 (70.3-91.2) | 53.8 (43.5-68.5) |
| 1M | 10% | cold | 65.4 (63.4-79.5) | 70.1 (64.2-78.6) |
| 1M | 50% | cold | 42.5 (39.8-71.2) | 13.0 (11.1-14.8) |
| all | all | warm (3 per rep) | 0.55-1.14 | 0.56-0.82 |

Warm medians are under 1.2 ms in every cell for both phases.

## Reading

- Cold reopen cost is lower for Phase 3 in most cells, most clearly at 1M docs with 0% and 50% deletes (16 vs 92 ms and 13 vs 43 ms). At 1M docs with 10% deletes the two legs are within noise (65 vs 70 ms). At 1M docs with 1% deletes Phase 3 is faster in the medians (54 vs 76 ms), but the min-max ranges overlap.
- Warm calls show no meaningful difference. Both legs keep the fingerprint in the cache, so the Phase 3 change does not move the warm path.
- The spread in cold numbers is large (up to 3x within one cell), which is the host noise. Five reps are too few to call the 10% cells different.
- Not measured: segment flush and merge cost, retained summary memory, persisted metadata size, and the max-version fallback case. Without the flush and merge side, the assignment's rule (do not claim a win unless read savings exceed flush, merge and memory costs) cannot be applied. No win is claimed.

## What is needed to finish item 1

- Flush and merge timing per leg, the retained summary memory (heap delta after the cold call), the persisted metadata size per segment, and the max-version case, each with the same five-rep rule.
- A decision on whether a 4-CPU, 7 GiB host is an acceptable benchmark host for the owner's question, or a larger host should be used.

## Item 2

Not run. The Phase 4 gate (`gates/SOLR-18507-phase-4.md`) is not DONE; its head moved to `d797b9b310` and it has no claim. Item 2 stays blocked.

## Limits

- Five reps per cell, one host, one JVM setting. Medians are reported with min-max; this is not a confidence interval.
- The cold path is measured after a fresh JVM start on a persisted index. It is not measured after a restart of a whole node.
- EmbeddedSolrServer indexing, one add per request, is slower than a bulk load. Index shape is one segment per 5000 docs before merges settle.
