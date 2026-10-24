# Assignment: verify upstream cuvs-lucene GPU and release evidence, round 4

Status: HANDED (waiting for concrete upstream runtime evidence or a published candidate release)

- Intended host: review/records host; runtime execution belongs to NVIDIA or an explicitly available compatible GPU host.
- Capability tags: review, records. These tags do not authorize a native/GPU run.
- Claim: `claims/cuvs-lucene-06-release-evidence-round-4-<host>-<cycle>.md` after a concrete trigger.
- Context: `material/cuvs-lucene-next-steps-round-4.md` and latest upstream contribution/handoff records.

## Objective

Establish what upstream actually validated and shipped, separating the production compatibility fix from adoption of Nick's tests. A CPU test pass, merge button or draft PR is not release/runtime evidence. Test additions can be declined while the production fix still ships; record those outcomes separately.

## Work

1. Inspect the relevant PR, merge commit, CI and release/tag history using primary sources. Record the exact source revision for each claim. Verify whether production fixes and any contributed test files are present. Do not assume the PR head, a CI merge ref, release branch and published tag identify the same tree.
2. For GPU/native evidence, obtain the actual run URL or maintained result, tested source/artifact identity, JDK, GPU architecture, driver/CUDA/native cuVS versions, test names, counts and skips. At minimum distinguish GPU construction/graph serialization, reading/searching, and any relevant merge path. A job labelled GPU that skipped the writer test is not proof. Existing CPU archived-index reads remain useful but do not fill that gap.
3. If evidence is missing, write a bounded request specification for upstream: exact candidate revision/artifact, actual existing test targets discovered from that revision, original failure path, successful execution criteria and required environment record. This is a draft. Do not execute on a CPU host, contact maintainers, create an automation or reserve a nonexistent GPU machine. Name the remaining prerequisite for a later round.
4. For a published artifact, verify actual Maven coordinates, module naming, version, POM, transitive Lucene/native dependencies, jar digests and source tag. The multi-version PR may change packaging; do not assume the old single cuvs-lucene coordinate or a version-only Solr bump. Check the artifact corresponding to Solr's exact Lucene release and its Java/runtime requirements. A snapshot/local jar or planned release date is not a released artifact.
5. Inspect released bytecode/API sufficiently to confirm the removed getSortedNodes dependency and the other known linkage changes are addressed in the selected artifact. Compare to the source actually tested. A new or materially changed artifact may need a concrete release-audit CPU job; reuse a prior source audit only with an explicit equivalence argument and limited claim.
6. Produce the adoption prerequisites for Solr: exact coordinates/digests, compatible Lucene/JDK/native/GPU constraints, API/codec registration changes observed, source evidence and runtime gaps. State AVAILABLE, WAITING FOR RELEASE, or RUNTIME EVIDENCE MISSING with reasons. No compatible candidate means no pin bump.

## Deliverables and acceptance

Add `reports/cuvs-lucene-release-evidence-round-4-<cycle>.md` and a machine-readable artifact/evidence manifest under a new material path. Add a GPU-proof request draft only if needed. Acceptance is a source-backed disposition with exact identities; READY FOR SOLR ASSESSMENT requires a real compatible published artifact, not a CPU-only success. Do not call the upstream GPU leg green unless actual evidence supports it.

If a required external fact is absent, finish the bounded assessment with that fact named and resume only when a new signal arrives. This is not a recurring monitoring request and does not require a new owner question merely to report an unavailable release.

## Limits

No native builds or GPU execution under this records claim, no Solr edits, no source push, no public message or release promise. Any eventual GPU run requires a separately concrete authorized job on a host whose capability is established.
