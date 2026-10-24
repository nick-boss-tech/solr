# Review of the cuvs-lucene upstream-fix plan, round 1

From vm6, 2026-10-24, reviewing `assignments/cuvs-lucene-lucene104-upstream-fix-round-1.md` against the evidence in `reports/solr-18556-round-1.md`. This is a review only: no patch, no post, no change to the assignment. The strong agent should read it before claiming.

## Checked on the jars (javap only)

- cuvs-lucene 25.12.0, `Lucene99AcceleratedHNSWVectorsWriter.writeGraph(GPUBuiltHnswGraph, IndexOutput)`, bytecode offset 42: `invokestatic HnswGraph$NodesIterator.getSortedNodes`. The iterator is the fresh result of `getNodesOnLevel(level)`, passed directly. It is not used again after the call.
- Lucene 10.2.0, `HnswGraph$NodesIterator.getSortedNodes(NodesIterator)` (static): allocates `size()` slots, drains the iterator with `hasNext`/`nextInt`, calls `Arrays.sort`, returns the array. It does not deduplicate. Empty input gives an empty array. It leaves the iterator exhausted.
- Lucene 10.4.0: no `getSortedNodes` on `HnswGraph$NodesIterator`.
- cuvs-lucene 26.10.0 still calls `getSortedNodes`, from `AcceleratedHNSWUtils`.

## What holds

- The evidence base is right: the call site, the Lucene 10.2 declaration, the 10.4 absence, and the 26.10.0 call.
- The upstream route is the correct end state, and the GPU limit is stated honestly.
- The Solr-side items are kept separate, as they should be.

## Gaps to close before the work starts

1. **The symbol audit must compare descriptors and static-ness, not only names.** `NoSuchMethodError` also fires on a descriptor mismatch. A static-to-instance change gives `IncompatibleClassChangeError`. The audit should compare, for every `org/apache/lucene` Methodref, Fieldref and Class in the rebuilt jar, the class, name, descriptor and static-ness against the 10.4.0 class files.
2. **Nothing has shown that the removed call is the whole 10.4 breakage.** The reproduction report checked one symbol. The strong agent's full audit is what can settle it. Until then, say "the only missing symbol checked so far" and not "the whole breakage".
3. **The semantics spec must include the side effect.** The replacement must drain the iterator as `getSortedNodes` does, return the same nodes sorted ascending, and handle empty input. The call site in `writeGraph` does not reuse the iterator, so the side effect is not load-bearing there. The harness should check the exhaustion explicitly and record that the call site does not rely on it.
4. **A `Signed-off-by:` line conflicts with the standing no-trailers rule.** The assignment says to follow the upstream CONTRIBUTING, which may require DCO sign-off. Our rule is commits with no trailers, as Nick Shanin. This is a decision for Nick, and it should be listed as one. The agent should verify upstream's requirement and not add the line on its own.
5. **Solr can prove the pin bump only on a GPU host.** The Solr integration test is skipped without a GPU (`TestCuVSCodecSupportIT`, line 71). After an upstream release, a Solr gate on a non-GPU host cannot show the fix works. The plan should say that, so nobody expects a Solr gate to prove it.

## Adjustments to our earlier recommendation

- Option (a), correcting the two docs sentences, still reads correctly as "no fix is scheduled". If the upstream package goes ahead, the docs should say a fix is proposed upstream, not released. The owner decides that wording.
- Option (b), the Solr-side guard, should wait for the upstream response. If the fix lands, the guard is unnecessary and would only hide the problem.
- Option (c), the pin, becomes possible only after upstream publishes a release built against Lucene 10.4. The upstream package is how that happens.

## Before the strong agent claims

- Check upstream's current main first. If the call is already gone there, the package changes shape.
- Ask Nick the DCO question in item 4 before preparing commits.
- Keep the Windows-only constraint. No Gradle and no Solr build.
