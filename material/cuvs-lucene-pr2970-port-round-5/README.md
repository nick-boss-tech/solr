# cuvs-lucene test port to NVIDIA/cuvs PR 2970, round 5

This package ports the two CPU test contributions (round 2 query acceptance masks, round 3 archived-index upgrades with deletes and sparse vectors) from PR 2702 `23004d0f` to PR 2970 head `3602ef0a8047ef222ad994a316d69ffec26e98bd` ("Add support for Lucene 10.3", imotov). It changes only test files and only adds lines. No production file is touched.

## Files

- `port.patch`: the four-file test patch against the PR 2970 head. SHA-256 and per-file before/after Git blobs are in `patch-manifest.json`.
- `source-manifest.json`: the 113 upstream blobs the proof reads (cuvs-java API sources, the shared cuvs-lucene module, `lucene-10.2` and `lucene-10.3`, the six compat source trees, the seven released fixtures, the two target tests).
- `reproduce.py`: the CPU proof, direct javac and JUnit only.
- `BackCompatRunner.java`: unchanged from round 3; runs named methods and rejects skips, ignores and assumption failures.
- `FixtureMappingControl.java`: the round 3 data-fault control, adapted to the PR 2970 fixture layout (writes `backcompat/releases/0.0.0/<running Lucene>/<codec>.zip`).
- `results/`: the implementer's dry run on vm6 (see Limits). A gate host writes its own.

## What changed from the PR 2702 package

| File | Port |
| --- | --- |
| `compat/leaf-reader-102-bits/.../TestLuceneCompat.java` | Same hunk as round 4 (`approximateSearch` bridge); the PR 2970 original is the PR 2702 original, so the final blob is the round 4 final `fa04413d`. |
| `compat/leaf-reader-103-acceptdocs/.../TestLuceneCompat.java` | Same as above; final blob `9bdc09a5`. |
| `TestCompatKnnFloatVectorQuery.java` | The three tests and `assertAcceptedHits` are added next to PR 2970's own three tests. PR 2970 already has the three-argument `CountingQuery`, which now counts through `newPerLeafCollector`, so the round 4 constructor change is dropped. Added: an assertion that the filtered segment holds more accepted documents than `perLeafTopK`, so the approximate search is the path under test on 10.3 as well. `getFirst()` became `get(0)` to match the file. |
| `TestBackCompatIndices.java` | The new method and `assertLiveDocumentsAndVectors` are unchanged in logic. The fixture name now comes from PR 2970's `indexPath(zip).group(3)` instead of the removed `FILE_NAME` pattern. The released fixtures keep the data contract the method relies on: `GenerateReleasedIndices.writeIndex` uses `Random(42)`, 200 documents, 8 dimensions, `id` and `vector` fields. |

## Reproduce

Use a fresh directory, Python 3, git, network access, and an absolute JDK 21 root in `CUVS_CPU_JDK`:

```sh
python3 reproduce.py --preflight-only
python3 reproduce.py
```

Preflight verifies every source blob, the patch digest, `git apply --whitespace=error-all`, the final blobs, every Maven Central jar against its published SHA-1, and google-java-format 1.27.0 with `--skip-javadoc-formatting` (upstream spotless sets `formatJavadoc=false`). It compiles and runs nothing.

The full run compiles the cuvs-java API (44 sources) and both Lucene modules with `--release 21`, then for each of Lucene 10.2.0 and 10.3.2 (the versions PR 2970's module POMs pin):

1. Query tests: PR 2970's test class (3 methods) and the ported class (6 methods), then both again with the acceptance mask dropped in that module's `CompatKnnFloatVectorQuery` variant (the round 2 mutation, unchanged). Expected: originals pass, port passes, under the mutation the original 3 still pass and exactly the 3 new methods fail.
2. Archived-index tests: PR 2970's `testReadIndices` and `testUpgradeIndices`, then the ported class's two plus `testUpgradeIndicesWithDeletesAndSparseVectors`, over the 7 released fixtures (all written on Lucene 10.2, so both modules read all 7).
3. Data-fault control: the 26.10.0 accelerated HNSW fixture rewritten with each stored ID shifted by one. Expected: read and upgrade pass, the new method fails with a vector-for-ID array mismatch.

Expected totals: 18 positive query executions and 6 expected mutation failures; 10 positive archived-index method executions over 70 fixture cases; 2 expected data-fault failures. `results/summary.json` holds the counts, and the script asserts each one.

No Maven, Gradle, native cuVS, CUDA or GPU. GPU-only methods (`testWriteIndicesForLaterReleases` and the module-to-module methods) are never invoked.

## Limits

- CPU proof of the test logic only. It does not validate GPU index construction or search, native integration, Solr, or release packaging.
- Lucene 10.4 and 10.5 are not in PR 2970. Their results stay recorded against PR 2702 `23004d0f` (rounds 2 and 3) and are not claimed here.
- The `results/` directory is the implementer's dry run on the authoring host. It is not an independent gate.
