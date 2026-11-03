# Assignment: cuvs-lucene Lucene 10.4 remaining incompatibilities (round 2)

Status: OPEN
Tags: implementation, records
Intended host: Windows (`javac`, `javap`, and a plain JVM only; no Gradle, no Maven build, no native build). A Linux host may take it only if its `hosts/` file lists `implementation`.

Origin: `reports/cuvs-lucene-lucene104-upstream-fix-round-1.md` (DELIVERED, LIMITED FIX; NOT COMPLETE LUCENE 10.4 SUPPORT). Round 1 fixed the removed `NodesIterator.getSortedNodes` call in `AcceleratedHNSWUtils` and proved it. It left the defects listed below. This round scopes the rest and checks whether existing upstream work already covers them.

## Evidence base (established; build on it, do not redo it)

- Release base: NVIDIA/cuvs `release/26.10` at `24ee1bf1cb17cad0ac611e39643af39ee1870124`.
- Whole-jar audit of the 26.10.0 shim on Lucene 10.4.0: 3 incompatible references remain, listed in the report's "Whole-jar linkage audit" table (`LeafReader.searchNearestVectors` with the `Bits` descriptor, `IOContext.withReadAdvice`, and the `IOContext.context` owner kind change).
- Full-module compile against 10.4.0 fails with 6 errors in `CuVS2510GPUVectorsReader`, `GPUKnnFloatVectorQuery`, and related readers (`search(..., AcceptDocs)` not implemented, `withReadAdvice` removed, stale override signatures, `Bits` search call). Complete stderr is in `material/cuvs-lucene-lucene104-proof/results/`.
- Existing upstream work: NVIDIA/cuvs PR #2702 (open, head `23004d0f0de2` at the time of round 1's check; re-check it), PR #2867 and PR #2586 (both open at round 1). Heads are recorded in `material/cuvs-lucene-lucene104-proof/upstream-overlap.json`.

## Work

1. Re-check the upstream PR heads listed above. Record the head SHAs and whether each one touches the 3 linkage defects or the 6 compile errors. Use the GitHub API read-only; do not comment, review, or push.
2. For each linkage defect and each compile error, state: the Lucene 10.4 source that changed (cite file and line from the 10.4.0 tag), the minimal call-site change that compiles on BOTH Lucene 10.2.0 and 10.4.0, and whether an open upstream PR already makes that change. Changes that compile on only one version are not submittable; say so instead.
3. For the changes that are not covered upstream, produce a patch against `release/26.10` as `material/cuvs-lucene-lucene104-remaining.patch`, limited to the affected files. Keep it minimal. No public API change, no dependency version change, no native code.
4. Prove each changed file compiles with `javac --release 22 -proc:none -implicit:none` against Lucene 10.2.0 and against 10.4.0, using the same method as round 1. Record exit codes and the argument arrays.
5. Rerun the whole-jar linkage audit against the patched shim and record which references remain. The audit scripts are in `material/cuvs-lucene-lucene104-proof/`.
6. Do not claim runtime compatibility. The writer and reader paths need a GPU, which no factory host has. The report labels every proof as compile, linkage, or semantics only.

## Rules

- Do not open an upstream PR, push to any fork, or post to GitHub, Jira, or any list. The owner decides any submission.
- Do not overlap with `PR #2702`'s own work. If that PR already contains the change, record it as covered and do not duplicate it in the patch.
- No Solr-side edits. The SOLR-18556 docs correction and the CuVSCodec guard question stay with the owner.
- Commit as Nick Shanin with no trailers. No em dashes in authored text.

## Deliverables

- `reports/cuvs-lucene-lucene104-remaining-gaps-round-2.md`: upstream head SHAs, per-defect table (covered upstream, or minimal change and its compile result), linkage re-audit result, and limits.
- `material/cuvs-lucene-lucene104-remaining.patch`, only if any gap is uncovered upstream. Base commit named in the report.
