# cuvs-lucene test port to NVIDIA/cuvs PR 2970, round 5

Status: IMPLEMENTED, PROOF HANDED TO GATE (`gates/cuvs-lucene-pr2970-port-round-5.md`). Nothing posted.

Claim: `claims/cuvs-lucene-pr2970-port-round-5-vm6.md`. Owner direction: Nick, 2026-11-03, option 1 of `reports/cuvs-lucene-readiness-review-round-4.md`; implement here, hand the proof to another host. Package: `material/cuvs-lucene-pr2970-port-round-5/` at board commit `57e8a6c989ec63c306d571cd85435d38403cfdce`.

## Base

NVIDIA/cuvs PR 2970 "Add support for Lucene 10.3" (imotov), head `3602ef0a8047ef222ad994a316d69ffec26e98bd`, open, approved by jameslamb (2026-10-30) and paul-aiyedun (2026-11-03T01:15Z). Rechecked at claim time. PR 2970 carries the compat modules and both Lucene modules (10.2.0 and 10.3.2) that the tests target. PR 2702 stays at `23004d0f` and is the umbrella being split.

## The port

`port.patch` (SHA-256 `3185ea28f53520e9fd1e8b72d83b15d3f912c2cb88b6591c3c97989d19a8c36d`) adds 214 lines to four test files and removes none.

| File | Before (PR 2970) | After | What changed against the round 4 package |
| --- | --- | --- | --- |
| `compat/leaf-reader-102-bits/.../TestLuceneCompat.java` | `ba897c67` | `fa04413d` | Nothing: same original, same hunk, same final blob as round 4. |
| `compat/leaf-reader-103-acceptdocs/.../TestLuceneCompat.java` | `dcd6544f` | `9bdc09a5` | Nothing: as above. |
| `src/test/.../TestCompatKnnFloatVectorQuery.java` | `66409f69` | `f2692a1f` | Rebased onto PR 2970's rewrite. Kept all three PR 2970 tests. Reused its three-argument `CountingQuery`, which now counts through `newPerLeafCollector`; the direct `approximateSearch` call still goes through that hook, so the 1-then-2 search count holds on both variants. Added an assertion that the accepted documents outnumber `perLeafTopK` (on a single segment `perLeafTopK` is `k` on both 10.2 and 10.3), so the approximate path is the one under test. `getFirst()` became `get(0)` to match the file. |
| `src/test/.../TestBackCompatIndices.java` | `ab1ea443` | `cfcab142` | Same method and helper. The fixture name now comes from PR 2970's `indexPath(zip).group(3)`; the round 4 `FILE_NAME` pattern no longer exists. Placed next to the other upgrade methods. |

Fixture data contract (decision ladder rung 1): the new method regenerates the expected vectors with `Random(42)`, 200 documents and 8 dimensions. PR 2970's `GenerateReleasedIndices.writeIndex` (blob `81182548`) and the class's own `writeIndex` write exactly that, with `id` and `vector` fields. The data-fault control's failure message on PR 2970's 26.10.0 fixture is byte-identical to round 3's on PR 2702's fixtures (`vector for id 1 ... expected:<0.6655489> but was:<0.7275637>`), which confirms the vectors did not change.

Formatting: google-java-format 1.27.0 with `--skip-javadoc-formatting`, matching the upstream spotless setting `formatJavadoc=false`. No change on the patched files, and none on the PR 2970 originals either. (Without the flag the formatter would reflow two upstream javadoc blocks; the patch leaves them alone.)

## Harness

`reproduce.py` is self-contained and does not read the round 2 or round 3 packages. It pins 113 upstream blobs, compiles the cuvs-java API (44 sources) and both modules (41 and 44 sources) with direct javac `--release 21`, then runs the query tests with the round 2 mask-drop mutation and the archived-index tests with the round 3 data-fault control, on both Lucene versions. Changes from the earlier harnesses: modules 10.2 and 10.3 only, the `knnVectorsWriter` compat property removed (PR 2970 has no writer variant), mutations on both modules (each is a different compat variant), the archived-index run also covers the two existing methods in the ported class, and the control index is written at `backcompat/releases/0.0.0/<running Lucene>/<codec>.zip` so PR 2970's path pattern reads it.

## Implementer dry run (vm6, not a gate)

Run twice from fresh copies; the second, on the published bytes, is saved under `results/`. vm6, Linux aarch64, JDK 21.0.12.1, 2026-11-03 around 02:55Z, under a minute.

| Leg | 10.2.0 | 10.3.2 |
| --- | --- | --- |
| Compile, exit code (API 44 sources, module) | API 0, module 0 (41 sources) | module 0 (44 sources) |
| Query, PR 2970 class | 3 of 3 | 3 of 3 |
| Query, ported class | 6 of 6 | 6 of 6 |
| Mask dropped, PR 2970 class | 3 of 3 pass | 3 of 3 pass |
| Mask dropped, ported class | exactly the 3 new methods fail | exactly the 3 new methods fail |
| Archived-index, PR 2970 (read, upgrade) | pass, 7 fixtures | pass, 7 fixtures |
| Archived-index, ported (read, upgrade, new) | pass, 7 fixtures | pass, 7 fixtures |
| Data-fault control | read, upgrade pass; new fails, vector mismatch | same |

Mutation failure shapes are assertions: `filtered document returned: 0` where filtered documents are nearest, and `expected:<5> but was:<0>` where the nearest documents are deleted (the query's scoring drops them, so the search comes back empty).

These counts come from the author's host and are not independent proof. The gate reruns them elsewhere.

## Next

1. Gate: `gates/cuvs-lucene-pr2970-port-round-5.md`, any capable Linux CPU host other than vm6.
2. After a GREEN receipt: step 02 revision 2 drafts aimed at PR 2970 (fixing T1 to T5 of the round 4 review), then a step 03 re-review by a host that prepared neither. Neither is claimed here.
3. If PR 2970 merges or moves first, the gate stops at its head recheck. A merge at the same head needs nothing; a moved head needs a refreshed port.

## Limits

- CPU test logic on Lucene 10.2.0 and 10.3.2 only. No GPU, native, Solr, or release packaging proof.
- Lucene 10.4 and 10.5 are outside PR 2970. Their results stay recorded against PR 2702 `23004d0f` and are not carried over.
- Nothing pushed upstream, no PR, no comment, no Jira write.
