# Jira comment draft: SOLR-18556, round 2

Status: DRAFT (not posted). Supersedes `jira/SOLR-18556-comment-draft-round-1.md` (vm6) in its options section only; the evidence section is vm6's, rechecked in `reports/solr-18556-round-1.md`. Refreshed by the phone agent, 2026-10-24, after the owner's decisions of the same day: the docs correction proceeds, pinning is closed on evidence, and a cuvs-lucene compatibility fix is being prepared upstream (`assignments/cuvs-lucene-lucene104-upstream-fix-round-1.md`). Posting needs Nick Shanin's explicit go.

---

I checked the failure against the published jars for cuvs-lucene and Lucene, without a GPU.

 * The ticket's trace is {{NoSuchMethodError}} for {{HnswGraph$NodesIterator.getSortedNodes}} in {{Lucene99AcceleratedHNSWVectorsWriter.writeGraph}}. In cuvs-lucene 25.12.0, {{writeGraph}} calls that method, and it is the only class in the jar that does.
 * lucene-core 10.2.0 declares {{public static int[] getSortedNodes(HnswGraph$NodesIterator)}}. lucene-core 10.4.0 does not declare it. Solr 10.1 RC3 uses Lucene 10.4.0 ({{gradle/libs.versions.toml}}, {{apache-lucene}}).
 * Every cuvs-lucene release on Maven Central, up to 26.10.0, declares lucene-core 10.2.0 in its POM. The 26.10.0 jar still calls {{getSortedNodes}}, from {{AcceleratedHNSWUtils}}.
 * The module's integration test is skipped when the GPU is not present ({{TestCuVSCodecSupportIT}}, the {{assumeTrue}} on {{Lucene99AcceleratedHNSWVectorsFormat.supported()}}). I did not run the GPU path.
 * The 10.1 docs say the problem "is expected to be fixed in a subsequent Solr 10.x release" ({{major-changes-in-solr-10.adoc}} line 88, {{dense-vector-search.adoc}} line 1038). The ticket has no fix version or target.

Where this stands after checking:

 * The docs promise a fix the ticket does not carry. I will put up a small PR correcting the two sentences to say no fix is scheduled.
 * Pinning to a newer cuvs-lucene is not possible today: no release is built against Lucene 10.4.
 * A compatibility fix for cuvs-lucene itself is being prepared for the upstream project; on the bytecode evidence above, the removed call is the jar's only 10.4 linkage break.
 * A Solr-side guard in {{CuVSCodec}} (use the default HNSW format for a field when {{getSortedNodes}} is missing, with a warning) would stop 10.1 indexing from failing in the meantime, at the cost of GPU fields indexing on CPU without the user choosing it. That is a behavior change, so I am not building it without a maintainer here wanting it.

If anyone wants the guard for 10.1, say so and I will build it. Otherwise the docs correction and the upstream fix are the path.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
