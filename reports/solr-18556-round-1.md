# SOLR-18556 cuVS indexing breaks with Lucene 10.4, round 1

Claim: `claims/solr-18556-cuvs-lucene-round-1.md` (vm6). Assignment: `assignments/solr-18556-cuvs-lucene-round-1.md`. Draft: `jira/SOLR-18556-comment-draft-round-1.md` (not posted). Read-only work: no code change, no pin bump, no gate, nothing posted to JIRA or GitHub.

## Facts checked

- Ticket, read live on 2026-10-24: Open, no fix version, no target, affects 10.1, last updated 2026-10-23, no comments.
- RC3 tip `749712b719`: `gradle/libs.versions.toml` pins `apache-lucene` 10.4.0 and `cuvs-lucene` 25.12.0 (lines 78 and 288).
- Solr docs at RC3 say the problem "is expected to be fixed in a subsequent Solr 10.x release": `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc` line 88 and `solr/solr-ref-guide/modules/query-guide/pages/dense-vector-search.adoc` line 1038. The ticket carries no such commitment.
- Latest cuvs-lucene on Maven Central is 26.10.0. Its POM declares `lucene-core`, `lucene-codecs`, `lucene-backward-codecs` and `lucene-test-framework` at 10.2.0, the same as every earlier release checked in the RC3 audit.

## The failure, from bytecode

The ticket's stack trace is `NoSuchMethodError` for `HnswGraph$NodesIterator.getSortedNodes` inside `Lucene99AcceleratedHNSWVectorsWriter.writeGraph`. The jars were downloaded from Maven Central and read with `javap` only; nothing was executed.

- cuvs-lucene 25.12.0, class `com/nvidia/cuvs/lucene/Lucene99AcceleratedHNSWVectorsWriter`, method `writeGraph(GPUBuiltHnswGraph, IndexOutput)`, bytecode offset 42: `invokestatic HnswGraph$NodesIterator.getSortedNodes:(Lorg/apache/lucene/util/hnsw/HnswGraph$NodesIterator;)[I`. This is the only class in the jar that references `getSortedNodes`.
- lucene-core 10.2.0: `HnswGraph$NodesIterator` declares `public static int[] getSortedNodes(HnswGraph$NodesIterator)`.
- lucene-core 10.4.0: `HnswGraph$NodesIterator` has no `getSortedNodes`. It has only the constructor and the inherited iterator members.

So the module was compiled against a Lucene API that Solr's Lucene 10.4 no longer provides. The call site is the one in the ticket's trace.

- cuvs-lucene 26.10.0 (latest) still calls `getSortedNodes`, from `com/nvidia/cuvs/lucene/AcceleratedHNSWUtils`. A newer release does not fix the linkage in its own jar.

## Reproduction check (rung 2)

- Runtime reproduction needs a GPU. `Lucene99AcceleratedHNSWVectorsWriter.writeGraph` takes a GPU-built graph. The Solr codec selects that format only for `knnAlgorithm="cagra_hnsw"` (`solr/modules/cuvs/src/java/org/apache/solr/cuvs/CuVSCodec.java` lines 64 to 66 and 94 to 100).
- `TestCuVSCodecSupportIT` is skipped on hosts without support: `assumeTrue("Skipping cuvs tests", Lucene99AcceleratedHNSWVectorsFormat.supported())` at line 71. On a host without a GPU the test is skipped, not run, so CI on such a host does not exercise the failure.
- vm6 has no GPU (`nvidia-smi` absent, no `/dev/nvidia*`). The runtime failure was not reproduced here. Result: "not reproduced at runtime: GPU required".
- The linkage evidence above reproduces the missing method on a host with no GPU.

## Options

**(a) Document the known issue and leave the code alone.**
- Changes: correct the two doc sentences that promise a fix (`major-changes-in-solr-10.adoc` line 88, `dense-vector-search.adoc` line 1038) to say no fix is scheduled. Keep the code as it is.
- Fixes: the docs match the facts. Nothing else changes.
- Risks: users who set `cagra_hnsw` on 10.1 still hit the failure during indexing. The test that would show it is skipped without a GPU.
- Recommendation: do this now. It is the only option with no behavior change, and the docs statement is the part that is wrong today.

**(b) A Solr-side guard in `CuVSCodec`.**
- The ticket's wording, catching `NoSuchMethodError` when the writer fails, is not workable as written. The error fires during flush, after part of the segment is written, and the codec has no point to retry from.
- The workable form is a check at format selection: in the `cagra_hnsw` branch (`CuVSCodec.java` line 66), test whether `HnswGraph.NodesIterator` has `getSortedNodes`. If it does not, return `fallbackCodec.getKnnVectorsFormatForField(field)` (the branch already used at line 102) and log one warning.
- Fixes: indexing on 10.1 succeeds without GPU acceleration for those fields.
- Risks: a silent drop to CPU HNSW for users who expected GPU indexing. Reflection on a Lucene internal. The check has to be removed when cuvs-lucene is built against Lucene 10.4, or it hides a later real error.
- This is a public behavior change, so it needs the owner's decision (assignment decision 2, rung 5). No gate is set up for it here; the assignment assigns that to a later round.
- Recommendation: only if the owner wants 10.1 indexing to stop failing for `cagra_hnsw` before a cuVS fix ships. If chosen, the form above, with a test that uses a stub so it runs without a GPU.

**(c) Pin to a newer cuvs-lucene built against Lucene 10.4.**
- Not possible today. The newest release, 26.10.0, is built against Lucene 10.2.0 and still calls `getSortedNodes`. No release with a 10.4 POM exists.
- Fixes: nothing until a release exists.
- Risks: none while no release exists. A pin bump now would only break the build.
- Recommendation: do not pin. Ask the cuVS maintainers whether a build against Lucene 10.4 is planned, and check the POM of each new release (the assignment's item 4).

## Recommendation

1. (a) now: correct the two doc sentences. This is a documentation change for the owner to approve.
2. (b) only if the owner wants indexing to stop failing on 10.1 before a cuVS fix.
3. (c) not until a release exists with a Lucene 10.4 POM.

## Limits

- Evidence is from bytecode of the published jars, not from a run. The runtime failure was not reproduced (no GPU).
- Jars were taken from Maven Central at the time of this round. The newest release checked is 26.10.0.
- The ticket does not name a maintainer for the cuvs module. The JIRA draft does not address anyone.
