# DRAFT, NOT POSTED: technical notes for an engaged NVIDIA/cuvs maintainer

Owner note (not part of the notes): Nick has not reviewed this text. It is for a maintainer who has engaged after the first-contact comment. Do not post it, attach it, or link it until Nick reviews it. Upstream state was read on 2026-10-24. Nothing here authorizes a branch, PR, or comment.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

## Identity

- Upstream test base: NVIDIA/cuvs PR 2702 base `23004d0f0de2014f11b09313f04fcf0de7643c3b`.
- Related open PR: NVIDIA/cuvs PR 2867, head `1c6bc921126e3fb1a051c91733c77ca75681c146`. It is based on main and is one of the pieces PR 2702 was split into. It uses per-version Lucene modules instead of the `compat/` modules these tests target.
- Test patch: `combined-tests.patch`, SHA-256 `84688de57719d0600505e42f4a62bfbd8b49373486ee90f6c4d9efbeff73ee5c`. It applies to the PR 2702 base with `git apply --whitespace=error-all`. It changes only test files; no production file is touched.

| Path | Original blob | Final blob |
| --- | --- | --- |
| `java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestCompatKnnFloatVectorQuery.java` | `2dc5e3b8` | `0a2628c1` |
| `java/cuvs-lucene/compat/leaf-reader-102-bits/src/test/java/com/nvidia/cuvs/lucene/TestLuceneCompat.java` | `ba897c67` | `fa04413d` |
| `java/cuvs-lucene/compat/leaf-reader-103-acceptdocs/src/test/java/com/nvidia/cuvs/lucene/TestLuceneCompat.java` | `dcd6544f` | `9bdc09a5` |
| `java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestBackCompatIndices.java` | `203b5746` | `ee3cd673` |

PR 2867 adds a file at the last path with different content (blob `f6701a14`). The two versions conflict.

## Environment

- Linux aarch64, OpenJDK 21.0.12.1, compiled with `--release 21`.
- Lucene 10.2.2, 10.3.2, 10.4.0 and 10.5.1, one module each.
- Formatting checked with google-java-format 1.27.0 with no rewrite. The repository's own spotless task was not run.
- No Gradle, Maven, native cuVS build, Solr build, or GPU was used.

## Check A: query acceptance masks

Test class `TestCompatKnnFloatVectorQuery`, seed `18556C0D`.

- Four methods per module: the existing test, `testFilterIsForwarded`, `testDeletedDocumentsAreExcluded`, and `testFilterAndDeletesAreIntersected`.
- Positive runs: 16 method executions (four methods on each of four modules), 0 failures, 0 skipped.
- Mutation control: the acceptance mask is discarded. Run on Lucene 10.2.2 and 10.4.0. The existing test passes on both. The three new methods fail on both, six expected failures in total. No initialization or linkage error.
- Compile and linkage: each module compiled with exit 0. The whole-jar linkage inventory recorded zero failures on each module.

| Module | Classes | Checked constants and types | Member constants | Member uses | Failures |
| --- | --- | --- | --- | --- | --- |
| 10.2 | 64 | 692 | 338 | 556 | 0 |
| 10.3 | 67 | 703 | 343 | 562 | 0 |
| 10.4 | 72 | 723 | 346 | 566 | 0 |
| 10.5 | 72 | 729 | 346 | 566 | 0 |

## Check B: archived-index upgrades

Test class `TestBackCompatIndices`, seed `18556C0D`.

- Existing methods `testReadIndices` and `testUpgradeIndices` pass on all four Lucene versions. Fixture counts are those recorded for each module (7, 10, 13 and 16 cases).
- New method `testUpgradeIndicesWithDeletesAndSparseVectors` passes on all four versions. It checks 183 live documents and 173 vectors. It checks that each vector keeps its original document ID before and after the merge, and it checks the merge state.
- Positive runs: 12 method executions across 138 fixture cases, 0 ignored, 0 assumption failures.
- Data-fault control: a test index whose vectors are attached to the wrong document IDs. Run on Lucene 10.2.2 and 10.4.0. Both existing methods pass on each. The new method fails on each with a vector mismatch at element 0, two expected failures in total. No linkage or initialization error.

The patch reads only the fixtures in the PR 2702 base, at the top of `src/test/resources/backcompat/`. PR 2867 uses a `releases/` tree with different file names and adds archives for 25.12.0, 26.08.0 and 26.10.0.

## Not covered

- GPU index construction and search, and the native cuVS build (the Java 22 bindings were not run).
- Solr integration, release packaging, and Maven publication.
- Any run against the PR 2867 head. The tests need a port to that layout first, and the port needs its own CPU run.

## Upstream requirements checked on 2026-10-24

- The repository's CONTRIBUTING.md points to the NVIDIA developer guide. That guide asks for unit tests with changes and requires benchmarks only for performance-critical paths. These tests do not touch such a path.
- The PR template and `.github/workflows/pr.yaml` at both revisions contain no DCO or sign-off requirement. No sign-off is added.
- No AI-use policy was found in the repository. The AI disclosure follows the owner's own policy.

🤖 *AI text above* 🤖 *(reviewed by Nick Shanin)*
