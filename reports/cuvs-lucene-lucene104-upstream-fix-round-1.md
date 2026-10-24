# cuvs-lucene Lucene 10.4 upstream package, round 1

Status: DELIVERED, LIMITED FIX; NOT COMPLETE LUCENE 10.4 SUPPORT.

Claim: `claims/cuvs-lucene-lucene104-upstream-fix-round-1.md`, Windows NCBIPC0140, Codex desktop. Assignment read at board `54903210bc889562028149cbfa1fa7f036ddc30f`; claim published at `0faf171be4be4cba9100f92bcad1c879628ddda0`. Follow-up review `reports/cuvs-plan-review-round-1.md` read at `ec8251e18c8a589173b5e61004adda214b5e2f0a`. Evidence collected and executed 2026-10-24.

## Outcome

The minimal replacement for the removed `NodesIterator.getSortedNodes` is prepared, compiles against both Lucene versions, and reproduces the old method's array results and iterator side effects. It does **not** make the published artifact fully compatible with Lucene 10.4: the whole-jar audit leaves three incompatible method references, including two other missing exact member descriptors. A full production-source compile against 10.4 also fails in unchanged reader/query classes. The earlier report established one removed method, not the absence of all other breakage; this report supplies that broader check and falsifies the single-cause assumption.

Existing upstream work overlaps the patch. [NVIDIA/cuvs PR #2586](https://github.com/NVIDIA/cuvs/pull/2586), by punAhuja, upgrades to Lucene 10.4. [Draft PR #2702](https://github.com/NVIDIA/cuvs/pull/2702), by imotov, implements separate Lucene-version artifacts and already includes the same `size`/`hasNext`/`nextInt`/sort replacement. [PR #2867](https://github.com/NVIDIA/cuvs/pull/2867), also by imotov, prepares that architecture. All were open when checked; heads are recorded in `material/cuvs-lucene-lucene104-proof/upstream-overlap.json`. The packaged change is independently prepared from the old Lucene implementation, but is not a novel solution absent from upstream review.

Recommendation: do not open a competing PR claiming complete 10.4 support. The immediate useful contribution is this reproducible verification evidence for the existing upstream compatibility work. The owner controls any public contact or submission. Nothing was submitted to NVIDIA, posted to Jira, or pushed as upstream code.

## Upstream identity, versions and source base

Maven coordinates are `com.nvidia.cuvs.lucene:cuvs-lucene`, not `com.nvidia:cuvs-lucene`. Maven metadata still lists 26.10.0 as the latest release on this date.

| Artifact | Published SCM | Source tag and commit | Java module |
| --- | --- | --- | --- |
| 25.12.0 | [POM](https://repo.maven.apache.org/maven2/com/nvidia/cuvs/lucene/cuvs-lucene/25.12.0/cuvs-lucene-25.12.0.pom) names `rapidsai/cuvs-lucene`, now `NVIDIA/cuvs-lucene` | [`v25.12.00`, `5488ac8ef3f56c699f96d91e557be7aa7ce3a727`](https://github.com/NVIDIA/cuvs-lucene/tree/5488ac8ef3f56c699f96d91e557be7aa7ce3a727) | root `pom.xml`, `src/main/java` |
| 26.10.0 | [POM](https://repo.maven.apache.org/maven2/com/nvidia/cuvs/lucene/cuvs-lucene/26.10.0/cuvs-lucene-26.10.0.pom) names `rapidsai/cuvs`, now `NVIDIA/cuvs` | [`v26.10.00`, `24ee1bf1cb17cad0ac611e39643af39ee1870124`](https://github.com/NVIDIA/cuvs/tree/24ee1bf1cb17cad0ac611e39643af39ee1870124/java/cuvs-lucene) | `java/cuvs-lucene/pom.xml`, `src/main/java` |

Both released POMs depend on Lucene 10.2.0. They do not declare 10.4 compatibility. POM SCM tags say `HEAD`, so actual release tags were separately resolved from GitHub. The old standalone repo's `v26.10.00a` is not the source tag of the released 26.10.0 artifact.

Patch base: NVIDIA/cuvs **`release/26.10` at `24ee1bf1cb17cad0ac611e39643af39ee1870124`**, the newest release branch and the 26.10.0 release tag. This branch was chosen because the shim must match a published artifact, with all compile dependencies available on Maven Central. The affected source is byte-identical to the Maven 26.10.0 sources jar. Current `main` was also inspected at `244f118479961a4448c2dfa294a4c2b93898edf6`, where the POM targets the unpublished cuvs-java 26.12.0. `git apply --check` passes for the supplied patch on both pinned source trees. Main was not compiled; the compile evidence below covers the release branch.

## Source semantics and patch scope

The original [Lucene 10.2 implementation, HnswGraph.java lines 174-181](https://github.com/apache/lucene/blob/releases/lucene/10.2.0/lucene/core/src/java/org/apache/lucene/util/hnsw/HnswGraph.java#L174) allocates exactly `size()` slots, drains the iterator using `hasNext` and `nextInt`, sorts the whole array ascending, and returns it. It keeps duplicate values and leaves a normally terminating iterator exhausted. Empty input with size zero returns an empty array. For partial or inconsistent iterators it does not resize to the actual remainder: unused slots stay zero, and too many values cause `ArrayIndexOutOfBoundsException`. The replacement preserves those details too.

[Lucene commit `3340a5463ce247fcd0e6f3c454c82d9b9ef71972`](https://github.com/apache/lucene/commit/3340a5463ce247fcd0e6f3c454c82d9b9ef71972) removed the static method and introduced a graph-level sorted iterator API. Using that newer API would fail the assignment's requirement to keep compiling on 10.2.

The one-file [patch](../material/cuvs-lucene-lucene104-fix.patch) adds a private helper to `java/cuvs-lucene/src/main/java/com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.java` and redirects its sole removed-method call. It copies the old allocation/drain/sort algorithm using APIs present in both versions. No public API, native code, dependency version, codec policy or Solr file changes.

All Java sources in the pinned release module and current main module were searched. Each tree has one removed-method call, in `AcceleratedHNSWUtils.writeGraph`; the current writer delegates to it. The 25.12.0 tag instead has the direct call in [Lucene99AcceleratedHNSWVectorsWriter.java line 507](https://github.com/NVIDIA/cuvs-lucene/blob/5488ac8ef3f56c699f96d91e557be7aa7ce3a727/src/main/java/com/nvidia/cuvs/lucene/Lucene99AcceleratedHNSWVectorsWriter.java#L507). That source also matches its Maven sources jar. It was inspected, not backported or claimed fixed by this 26.10 patch.

At both call sites the input is a fresh `graph.getNodesOnLevel(level)` iterator, passed directly and not reused. Exhaustion is nevertheless part of the preserved semantics and is explicitly checked.

## Executed compile proof

Portable Temurin JDK 22.0.2+9, Windows x64. Its archive SHA-256 is `bf61e74c3dbd2da27d6415a804813c5ade87f9c838acca1edfa08ce9155bd82b`. All compiles used `javac --release 22 -proc:none -implicit:none`, with the chosen version of `lucene-core`, `lucene-backward-codecs`, `lucene-codecs`, and Maven Central `cuvs-java:26.10.0`. No published cuvs-lucene binary was used to mask missing in-module compile dependencies. Exact argument arrays, source lists, output counts and diagnostics are in [compile-results.json](../material/cuvs-lucene-lucene104-proof/results/compile-results.json) and its adjacent logs.

| Sources | Lucene | Exit | Result |
| --- | --- | --- | --- |
| All 33 production source files, including package-info | 10.2.0 | 0 | 53 class files generated |
| Affected file and complete source dependency closure: 5 files | 10.2.0 | 0 | 8 class files generated |
| Same 33 production sources | 10.4.0 | 1 | 6 errors in unchanged reader/query sources; not a successful module build |
| Same affected 5-file source closure | 10.4.0 | 0 | 8 class files generated |

The five files are `AcceleratedHNSWUtils.java`, `GPUBuiltHnswGraph.java`, `LuceneProvider.java`, `ThreadLocalCuVSResourcesProvider.java`, and `Utils.java`. Every class declared in those files was compiled in each affected-set run. This limited compile is valid evidence for the patch, but the rest of the module cannot compile against 10.4 because of real Lucene API changes, not a missing native toolchain. Tests, benchmarks and native implementations were not built. Partial output from the failed full-module 10.4 compile was not used in the shim or harness.

The six full-module errors cover `CuVS2510GPUVectorsReader` missing the new abstract `search(..., AcceptDocs)` implementation, removed `IOContext.withReadAdvice`, obsolete override signatures, and `GPUKnnFloatVectorQuery` still calling the `Bits` form of nearest-vector search. The complete stderr is retained.

## Whole-jar linkage audit

The shim starts with the unmodified Maven Central `cuvs-lucene-26.10.0.jar` and swaps **only** `com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.class`, compiled against 10.2. The enum and all other entries remain byte-identical. Shim SHA-256: `99cfef0236df141a217c75102e9263c9c0462ac3f82546ead3a1131383c077f1`. The jar is a proof artifact, not a release artifact or a proposed Solr pin.

The supplied auditor parses all 54 class files and their complete constant pools. Every Lucene Class, Fieldref, Methodref and InterfaceMethodref is checked, along with Lucene types in descriptors. Member checks use **owner + name + exact descriptor**, inherited-member resolution, class/interface reference kind, access flags, and **static/instance expectations from every consuming instruction and method handle**. All member constants in these jars have identified uses; no staticness check was skipped for an unused member constant. It scans code without loading cuVS classes or native libraries.

| Artifact and target | Classes | Member constants | Member uses | Member/type checks | Incompatible references |
| --- | --- | --- | --- | --- | --- |
| Original 26.10.0 on 10.2.0, control | 54 | 318 | 536 | 616 | 0 |
| Original 26.10.0 on 10.4.0 | 54 | 318 | 536 | 616 | 4 |
| Patched 26.10.0 shim on 10.4.0 | 54 | 319 | 538 | 617 | 3 |
| Original Solr-pinned 25.12.0 on 10.4.0, supplemental inventory | 22 | 194 | 343 | 354 | 3 |

These counts include repeated references from different callers, not unique API names. Every checked record and its call-site opcode/offset is retained in the [shim inventory](../material/cuvs-lucene-lucene104-proof/results/linkage-shim-10.4.0.json); adjacent inventories contain the controls and 25.12 results.

Every remaining incompatible reference in the 26.10 shim:

| Caller, method and bytecode offset | Exact referenced symbol | Failure on Lucene 10.4 |
| --- | --- | --- |
| `GPUKnnFloatVectorQuery.approximateSearch`, 49 | `org/apache/lucene/index/LeafReader.searchNearestVectors:(Ljava/lang/String;[FLorg/apache/lucene/search/KnnCollector;Lorg/apache/lucene/util/Bits;)V` | Exact method descriptor absent; target now takes `AcceptDocs` |
| `CuVS2510GPUVectorsReader.<init>`, 144 | `org/apache/lucene/store/IOContext.withReadAdvice:(Lorg/apache/lucene/store/ReadAdvice;)Lorg/apache/lucene/store/IOContext;` | Exact method absent; owner also changed from class to interface |
| `CuVS2510GPUVectorsReader.<init>`, 165 | `org/apache/lucene/store/IOContext.context:()Lorg/apache/lucene/store/IOContext$Context;` | Method exists and remains instance, but old `Methodref`/`invokevirtual` expects a class; 10.4 owner is an interface |

The last row is **not** a missing method. Distinguishing it is why owner kind and instruction checks matter. [IOContext.java line 31](https://github.com/apache/lucene/blob/releases/lucene/10.4.0/lucene/core/src/java/org/apache/lucene/store/IOContext.java#L31) declares an interface; its replacement hint API is at line 151. [LeafReader.java line 343](https://github.com/apache/lucene/blob/releases/lucene/10.4.0/lucene/core/src/java/org/apache/lucene/index/LeafReader.java#L343) has the changed search signature. `javap -p -s` output for both versions is included.

The Solr-pinned 25.12.0 artifact has the removed helper plus the same `LeafReader.searchNearestVectors` and `IOContext.withReadAdvice` incompatibilities; it has no `IOContext.context` call. It is not fully compatible either.

The auditor itself passed an executed adversarial fixture suite: inherited resolution control, changed return descriptor, static-to-instance method/field, instance-to-static field, removed inherited method, class-to-interface change, descriptor-only missing class, method handles, and both switch instruction layouts. [Self-check result](../material/cuvs-lucene-lucene104-proof/results/audit-selfcheck.json).

## Executed semantics proof

The JVM harness reflectively invokes the **actual private helper in the compiled production class**, not a copied implementation. On Lucene 10.2 it compares that helper with the original Lucene method using independent equivalent iterators. It compares arrays, exception classes, iterator positions, `nextInt` counts, and exhaustion. The synthetic iterator rejects any `consume` call, so a switch to that API cannot silently pass the exhaustion check.

Each comparison run passed 526 cases: 14 named cases plus 512 randomized cases, seed `0x18556`. Named cases cover empty, singleton, ascending, descending, duplicates, integer extremes, an exhausted iterator, a partially consumed iterator, under/overstated sizes, zero size with values, and Lucene's explicit-array, implicit-sequence and empty array iterators. Duplicate retention and normal exhaustion passed. The deliberately invalid size cases matched the original exception and consumption behavior.

The full 526-case comparison passed for both the 10.2-compiled and 10.4-compiled production classes, each executed with the original Lucene 10.2 implementation available. An additional plain-JVM check executed the 10.2-compiled helper on Lucene 10.4 and verified sorted duplicate retention and exhaustion. All three runs exited 0. Logs and argument arrays are in [semantics-results.json](../material/cuvs-lucene-lucene104-proof/results/semantics-results.json). Only the expected optional Vector API warning was emitted. No GPU operation was requested.

## Contribution requirements and owner review

Upstream [CONTRIBUTING.md at inspected main](https://github.com/NVIDIA/cuvs/blob/244f118479961a4448c2dfa294a4c2b93898edf6/CONTRIBUTING.md) delegates to its [contribution guide source](https://github.com/NVIDIA/cuvs/blob/244f118479961a4448c2dfa294a4c2b93898edf6/fern/pages/contributing.md). The release-branch guide was read as well. Both guide versions and the PR template request tests, formatting and CI/review; none states a DCO sign-off requirement. No `Signed-off-by`, co-author, or other trailer was added. No upstream commit or PR was created. This is a unified source diff, with board commits authored as Nick Shanin.

The follow-up review's DCO conflict is recorded as conditional: if a maintainer or required check requests sign-off, **Nick must decide before submission**. This package does not infer permission to override the standing no-trailers rule. The draft contains the required AI/Nick disclosure as proposed submission text, explicitly contingent on Nick actually reviewing it before use.

## Decisions, reproduction and limits

Decision ladder: rung 1 settles exact historical iterator behavior and demonstrates the extra linkage failures; rung 2 supplies executed semantics and auditor mutation checks; rung 3 authorizes the latest-release source base and the deliberately minimal call-site/helper scope. The additional 10.4 incompatibilities are named follow-up work, not silently folded into this patch. The existing-PR overlap supports the recommendation to contribute evidence to that work rather than duplicate it.

The proof directory contains Python scripts, the Java harness, pinned source blob IDs, artifact URLs/checksums, and full results. On Windows with Python and the documented git executable, run `python reproduce.py` from a copy of `material/cuvs-lucene-lucene104-proof`. It downloads pinned inputs and a checksum-verified portable JDK 22 into that directory, invokes only javac, javap and plain JVM checks, and regenerates the expected positive and negative results. The full-module 10.4 compile and three shim linkage defects are expected recorded findings, not hidden successes. Downloaded jars, JDK binaries and class files are not committed to the board; they are reproducible from the manifests.

No Gradle, Maven build, Solr build, native build, GPU writer/index/merge/search execution, or upstream CI was run. Constant-pool linkage does not validate reflection-generated names, native linkage, newly required abstract overrides, on-disk compatibility, or GPU semantics. The source compile separately exposes abstract/override incompatibilities. No result here is a complete runtime-compatibility claim.

After an upstream release, a Solr pin change still requires a **GPU host exercising the integration test**; a non-GPU skip of `TestCuVSCodecSupportIT` proves nothing about the accelerated path. The Solr docs wording and guard remain owner-routed separately. Existing upstream proposals justify describing a proposed fix, never a released fix or scheduled Solr release commitment.
