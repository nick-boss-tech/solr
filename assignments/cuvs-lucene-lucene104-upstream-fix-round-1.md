# Assignment: cuvs-lucene fix for Lucene 10.4, upstream package, round 1

Status: OPEN

- Intended host: Windows, strong agent. This is a strong-agent job: claim it only if you can do source-level Java work plus bytecode verification. Claim as `claims/cuvs-lucene-lucene104-upstream-fix-round-1.md` before starting.
- Capability tags: `implementation`, `records`.
- Owner direction, 2026-10-24: the most ambitious route. The fix for the SOLR-18556 breakage belongs upstream in NVIDIA's cuvs-lucene, and this assignment produces the complete, ready-to-open upstream package: patch, compile proof on two Lucene versions, a full linkage audit, an executed semantics check, and a draft PR text. Opening the upstream PR is reserved to the owner; nothing is submitted or posted under this assignment.

## Evidence base (already established; build on it, do not redo it)

`reports/solr-18556-round-1.md` on this board established, from the published jars read with `javap`:

- Solr 10.1 pins cuvs-lucene 25.12.0 against Lucene 10.4.0. Indexing with `knnAlgorithm="cagra_hnsw"` fails with `NoSuchMethodError` for `HnswGraph$NodesIterator.getSortedNodes` inside `Lucene99AcceleratedHNSWVectorsWriter.writeGraph`.
- In 25.12.0 the call is in `com/nvidia/cuvs/lucene/Lucene99AcceleratedHNSWVectorsWriter.writeGraph` (bytecode offset 42), the only class in that jar referencing `getSortedNodes`. Lucene 10.2.0 declares the method; Lucene 10.4.0 does not.
- The newest cuvs-lucene, 26.10.0, still calls it, from `com/nvidia/cuvs/lucene/AcceleratedHNSWUtils`, and its POM builds against Lucene 10.2.0, as does every release checked. No upstream release works with Lucene 10.4 today.
- Runtime reproduction of the writer path needs a GPU, which no factory host has. This assignment's proofs are compile, linkage, and function-semantics evidence, and the report must label them exactly that way.

## Work

1. Identify the upstream source. From the cuvs-lucene POM on Maven Central, confirm the SCM repository, the Java module that builds the artifact, and the tags for 25.12.0 and the newest release. Record all three in the report. Work against upstream's current main (or its newest release branch, stating which) so the fix is submittable; check the 25.12.0 tag as well, because Solr 10.1 pins it.
2. Establish the semantics of the removed method from the Lucene 10.2.0 source: exactly what `HnswGraph$NodesIterator.getSortedNodes` returns for a given iterator (ordering, duplicates, empty input). Cite the source lines. The replacement must reproduce those semantics, not approximate them.
3. Write the patch. Replace the removed call at every site found in the upstream tree (at minimum the writer call site and `AcceleratedHNSWUtils` if it is present there). The replacement must compile against BOTH Lucene 10.2.0 and Lucene 10.4.0 from the same source, using only APIs both versions keep, because upstream builds against 10.2 today; a patch that only compiles on 10.4 is not submittable. Keep the change minimal: the call sites and any private helper they need, nothing else.
4. Compile proof. With `javac` (no Gradle, no native build), compile the patched sources twice: once with the Lucene 10.2.0 jars on the classpath, once with the 10.4.0 jars, assembling the rest of the compile classpath from Maven Central artifacts. If the module's Java sources cannot all be compiled without the native toolchain, compile every class in the affected files plus their in-module dependencies and state exactly what was and was not compiled.
5. Linkage audit. Rebuild the artifact in shim form: the published cuvs-lucene jar with the patched classes swapped in. Scan the whole rebuilt jar's constant pools for references to Lucene symbols, and verify every referenced symbol exists in Lucene 10.4.0. The claim this audit supports is specific: the removed `getSortedNodes` call was the whole 10.4 breakage in this jar, or it was not, and if it was not, the report names every other missing symbol found.
6. Semantics check, executed. Write a small JVM harness that runs the replacement logic and the original `getSortedNodes` (on Lucene 10.2.0, where it exists) over the same synthetic node iterators, including empty and single-node cases, and compares the returned arrays. The harness runs on a plain JVM; no GPU is involved because neither is the writer. Record the cases and the results.
7. Prepare the submission package. Read the upstream repo's CONTRIBUTING for its commit requirements (RAPIDS projects commonly require a DCO sign-off) and follow them in the prepared commits or patch. Draft the PR description: what breaks, the bytecode evidence in one paragraph, what the patch changes, the dual-version compile result, the linkage audit result, the semantics check, and the honest limit (not runtime-validated; the writer path needs a GPU, and upstream CI is the place that can run it). The draft states that the change was prepared with AI assistance and reviewed by Nick Shanin.

## Deliverables

- `reports/cuvs-lucene-lucene104-upstream-fix-round-1.md`: the repo, module, and tags identified; the removed method's semantics with citations; the patch summary by file; both compile results; the linkage audit result (complete symbol check, not a sample); the semantics harness cases and results; the limits, stated plainly.
- The patch itself, as `material/cuvs-lucene-lucene104-fix.patch` (unified diff against the upstream tree it targets, with the base commit named in the report).
- The draft PR description, as `drafts/cuvs-lucene-upstream-pr-draft.md`, marked DRAFT (not submitted).

## Limits

- Do not open the upstream PR, push to any fork, or post to GitHub, Jira, or any list. The package is delivered on this board; the owner opens the PR.
- No Gradle and no Solr build anywhere in this work (Windows host rule). `javac`, `javap`, and a plain JVM harness only.
- Do not change the Solr-side items: the SOLR-18556 docs correction, the CuVSCodec guard question, and the existing Jira draft are separate and already routed.
- Commit as Nick Shanin with no trailers. No em dashes in authored text. No "we" for the owner plus the agent in the draft PR text; first person or neutral phrasing.
