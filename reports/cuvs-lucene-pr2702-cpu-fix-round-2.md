# cuvs-lucene PR 2702: CPU compatibility review and regression patch, round 2

Status: IMPLEMENTED, CPU PROOF PENDING (independent Linux gate).

Claim: `claims/cuvs-lucene-pr2702-cpu-fix-round-2.md`, Windows NCBIPC0140. Owner authorization: Nick's 2026-10-24 chat instruction to implement substantiated gaps and hand off a CPU-only gate if needed. Claim commit: `4897825efede97375f3a1f6b266a8acc2ae38f55`. This follows the round 1 package at `1ea03b601b9a5560086d487a2e622b4a137ed08d`.

## Outcome and scope

[NVIDIA/cuvs PR 2702](https://github.com/NVIDIA/cuvs/pull/2702) at **`23004d0f0de2014f11b09313f04fcf0de7643c3b`** already fixes all three linkage failures left by the minimal round 1 shim. All four versioned modules compile from source, and all four whole-jar Lucene linkage inventories are clean on their intended versions. No additional production defect was established in those three paths.

The implemented contribution is a **three-file CPU regression test patch**, not a claim to have authored the production fixes already in the PR. It checks filter forwarding, deleted-document exclusion, and their intersection in the query adapter. The tests run through query rewriting and directly through the approximate adapter, because an exact-search fallback can otherwise conceal a dropped mask. Two test-only compatibility bridges accommodate the `Bits` and `AcceptDocs` signatures. No production Java, dependencies, native code or Solr files change.

The patch, source manifests, reproduction scripts and raw results are under [material/cuvs-lucene-pr2702-round-2](../material/cuvs-lucene-pr2702-round-2/README.md). Patch SHA-256: **`d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154`**. Base is the pinned PR head above; it is a patch on imotov's existing work. Nothing was submitted publicly or pushed to NVIDIA or a source-code fork.

## Resolution of the original three failures

1. `LeafReader.searchNearestVectors(..., Bits)` is isolated to the 10.2 query adapter. The [10.3+ adapter](https://github.com/NVIDIA/cuvs/blob/23004d0f0de2014f11b09313f04fcf0de7643c3b/java/cuvs-lucene/compat/knn-float-vector-query-103-acceptdocs/src/main/java/com/nvidia/cuvs/lucene/CompatKnnFloatVectorQuery.java#L58) calls the exact `AcceptDocs` signature. The [reader adapter](https://github.com/NVIDIA/cuvs/blob/23004d0f0de2014f11b09313f04fcf0de7643c3b/java/cuvs-lucene/compat/knn-vectors-reader-103-acceptdocs/src/main/java/com/nvidia/cuvs/lucene/CompatKnnVectorsReader.java#L44) also implements the newer abstract search methods.
2. Removed `IOContext.withReadAdvice` is replaced in the [10.4 version-specific compatibility class](https://github.com/NVIDIA/cuvs/blob/23004d0f0de2014f11b09313f04fcf0de7643c3b/java/cuvs-lucene/lucene-10.4/src/main/java/com/nvidia/cuvs/lucene/LuceneCompat.java#L42) with `withHints(DataAccessHint.SEQUENTIAL)`. The shared GPU reader calls that adapter.
3. The unchanged source spelling `state.context.context()` is recompiled separately against each module's Lucene API. It therefore gets interface invocation bytecode on 10.4, rather than retaining the 10.2 class-style `Methodref` from a published old jar. The exact owner kind, descriptor and instruction checks pass.

The removed sorted-node helper is already replaced in this PR too. This review does not duplicate that production patch.

## Executed compilation and linkage evidence

The public Java dependency API was compiled from the **same pinned cuvs repository revision**: 44 source files, 95 class files, `javac --release 21`. Its Java 22/native implementation and `module-info.java` were excluded. This is an authentic API source dependency for CPU proof, not a hand-written API stub or a published native-capable artifact.

Each module's shared sources, version-specific sources, selected `compat/` implementations, and inherited `src/since/java` sources were then compiled with direct javac. The source list is derived from the module POM's properties and source declarations. JDK used locally: Temurin 22.0.2+9, Windows x64. No Maven or Gradle build was invoked.

| Module | Actual Lucene dependency | Production source files | Generated classes / audited classes | Lucene member references | Linkage failures |
| --- | --- | --- | --- | --- | --- |
| 10.2 | 10.2.2 | 42 | 64 | 338 | 0 |
| 10.3 | 10.3.2 | 45 | 67 | 343 | 0 |
| 10.4 | 10.4.0 | 50 | 72 | 346 | 0 |
| 10.5 | 10.5.1 | 50 | 72 | 346 | 0 |

All compiles exited 0. Thin proof jars were assembled from every generated production class plus the module/shared resources. They are freshly compiled CPU proof artifacts, not official Maven packaging or NVIDIA release jars. All their class files were audited, without sampling, using round 1's verified constant-pool parser. Checks cover Lucene Class/Fieldref/Methodref/InterfaceMethodref entries, descriptor types, inherited members, exact descriptors, class/interface kind, access, and consuming instruction/method-handle staticness. Inventories and exact commands are retained in `results/matrix.json`, `results/linkage-summary.json`, and the four `results/linkage-10.*.json` files.

This patch changes tests only, so the audited production bytecode is the PR's unmodified implementation. Static linkage does not prove native behavior or on-disk compatibility.

## Patch and discriminating test proof

The [unified patch](../material/cuvs-lucene-pr2702-round-2/cpu-regression.patch) changes:

- `src/test/java/.../TestCompatKnnFloatVectorQuery.java`: three new cases; 100 vectors with excluded/deleted vectors nearest the query; more accepted candidates than `k`; one segment with merges disabled; checks that the approximate path ran, five results remain, and every result satisfies both acceptance conditions. It also calls the adapter directly with the equivalent acceptance bit set, preventing rewrite's exact fallback from hiding a forwarding error.
- `compat/leaf-reader-102-bits/src/test/java/.../TestLuceneCompat.java`: a test-only bridge calling the `Bits` form of `approximateSearch`.
- `compat/leaf-reader-103-acceptdocs/src/test/java/.../TestLuceneCompat.java`: the equivalent bridge converting the test mask to `AcceptDocs`.

Seed: **`18556C0D`**. Each intended Lucene version passed all four tests: the existing segment-count test plus three new cases. **16 tests passed, zero failures**, with no GPU skip. The tests exercise actual query-adapter code and stock Lucene indexing/search; they do not create a cuVS index or resource.

Two source mutations deliberately discard the acceptance mask while preserving valid API linkage:

- 10.2 adapter passes `null` instead of its `Bits` argument.
- 10.4 adapter passes `AcceptDocs.fromLiveDocs(null, maxDoc)` instead of the supplied `AcceptDocs`.

On each mutation, the original one-test suite **still passes**. On each mutation, the patched suite runs four tests and **all three added cases fail**, while the original case passes. Failures are the named filter/deletion/intersection assertions, not initialization or compilation failures. In the 10.4 direct probe, the dropped mask returns filtered document 0, which the new assertions reject. This is executed evidence that the additional coverage detects a real possible regression; it is not a claimed defect in the unmodified PR.

`results/cpu-tests.json` records exact commands, test counts and failure method names. The runner validates that normal runs have four successes, original controls have one success, and each mutation has exactly the three expected failing methods. Raw logs are adjacent. Autogenerated Gradle reproduction strings in Lucene's failure logs are inert diagnostic text; no Gradle was run.

Google Java Format 1.27.0 passed a direct JVM formatting check for all three changed files. The formatter artifact is SHA-256 pinned. `git apply --check --whitespace=error-all` passed against the pinned base. The source manifest records original Git blob IDs and all three formatted target blob IDs.

## Handoff and limits

An independent **CPU-only Linux gate** is being posted as `gates/cuvs-lucene-pr2702-cpu-round-2.md`. It will pin this package commit, the upstream PR revision, and the patch digest. It asks for formatting, the full four-module javac/linkage matrix, all 16 positive JUnit tests, and both mutation/control comparisons on JDK 21+. It requires no CUDA installation, native implementation build or GPU. The test implementation remains Proof Pending until that gate reports success for those exact inputs.

Decision ladder: rung 1 settled that the three production linkage fixes already exist upstream; rung 2 exposed the missing regression discrimination and justifies the direct-adapter checks; rung 3 authorizes the owner-requested implementation and CPU-only handoff. No unrelated graph/native issue was pulled into this patch. Recommend offering this small test patch and the compatibility evidence to the existing PR after Nick reviews the draft.

No public PR, comment, Jira update, live PR edit, upstream write, source-code fork push, Solr pin change, GPU indexing/search/merge validation, native library validation or official Maven package verification occurred. The existing contribution instructions do not state a DCO sign-off requirement; no trailers were added. GPU validation remains outside this package, and these CPU results do not establish complete GPU or file-format compatibility.
