# DRAFT, NOT SUBMITTED: preserve HNSW node sorting without the removed Lucene helper

Owner note: do not submit as a complete Lucene 10.4 fix. PRs #2586 and #2702 overlap this work, and #2702 already contains the same helper algorithm. Prefer sharing the verification evidence on the existing compatibility work, after owner authorization. The disclosure below is proposed submission text; Nick must review the patch and text before it can truthfully be used.

---

🤖 *Prepared with AI assistance and reviewed by Nick Shanin.*

Solr 10.1 combines cuvs-lucene 25.12.0 with Lucene 10.4.0. Accelerated HNSW indexing reaches a call to `HnswGraph.NodesIterator.getSortedNodes` that Lucene 10.4 no longer provides. The published 25.12.0 writer invokes it at bytecode offset 42; 26.10.0 still references it from `AcceleratedHNSWUtils`. Both released artifacts declare Lucene 10.2.0 dependencies.

This patch replaces the utility's call with a private helper that preserves the old implementation: allocate `size()` slots, drain through `hasNext`/`nextInt`, sort ascending, retain duplicates, and leave the iterator exhausted. It changes one Java file and keeps the source compatible with Lucene 10.2.0 and 10.4.0.

Direct `javac` compilation passed for the affected file and all four in-module source dependencies against each Lucene version. All 33 production sources also compiled against 10.2.0. The full 10.4.0 compile still fails in unchanged query/reader code.

A whole-jar constant-pool audit of a 26.10.0 shim checked all 54 class files, exact member descriptors, inheritance, access, class/interface kinds and instruction/method-handle staticness. It confirms the removed helper reference is gone, but finds three remaining incompatible references: the `Bits` form of `LeafReader.searchNearestVectors`, removed `IOContext.withReadAdvice`, and a class-style invocation of `IOContext.context` after its owner became an interface. This patch therefore does not establish complete Lucene 10.4 support.

The actual patched private helper matched Lucene 10.2's original method in 526 cases, including empty/singleton inputs, duplicates, partial consumption, size inconsistencies, and explicit iterator-exhaustion checks. Both compiled variants passed this comparison; a separate invocation on Lucene 10.4 also passed.

No GPU writer path was runtime-validated. This is compile, static linkage, and function-semantics evidence. GPU indexing, file compatibility, merge/search behavior and broader compatibility require upstream CI on appropriate hardware.

Related work: [#2586](https://github.com/NVIDIA/cuvs/pull/2586), [#2702](https://github.com/NVIDIA/cuvs/pull/2702), [#2867](https://github.com/NVIDIA/cuvs/pull/2867).

🤖 *AI-assisted change and text; reviewed by Nick Shanin.*
