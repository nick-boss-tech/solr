DRAFT, NOT POSTED. Addressee: to be chosen by Nick Shanin. No mention in this draft.

I checked the failure against the published jars for cuvs-lucene and Lucene, without a GPU.

 * The ticket's trace is {{NoSuchMethodError}} for {{HnswGraph$NodesIterator.getSortedNodes}} in {{Lucene99AcceleratedHNSWVectorsWriter.writeGraph}}. In cuvs-lucene 25.12.0, {{writeGraph}} calls that method, and it is the only class in the jar that does.
 * lucene-core 10.2.0 declares {{public static int[] getSortedNodes(HnswGraph$NodesIterator)}}. lucene-core 10.4.0 does not declare it. Solr 10.1 RC3 uses Lucene 10.4.0 ({{gradle/libs.versions.toml}}, {{apache-lucene}}).
 * Every cuvs-lucene release on Maven Central, up to 26.10.0, declares lucene-core 10.2.0 in its POM. The 26.10.0 jar still calls {{getSortedNodes}}, from {{AcceleratedHNSWUtils}}.
 * The module's integration test is skipped when the GPU is not present ({{TestCuVSCodecSupportIT}}, the {{assumeTrue}} on {{Lucene99AcceleratedHNSWVectorsFormat.supported()}}). I did not run the GPU path.
 * The 10.1 docs say the problem "is expected to be fixed in a subsequent Solr 10.x release" ({{major-changes-in-solr-10.adoc}} line 88, {{dense-vector-search.adoc}} line 1038). The ticket has no fix version or target.

Options I see:

 * (a) Leave the code alone and correct the two doc sentences to say no fix is scheduled yet.
 * (b) Add a check when the cagra_hnsw format is chosen ({{CuVSCodec}}): if {{getSortedNodes}} is missing, use the default HNSW format for that field and log a warning. Indexing would then succeed without GPU acceleration. This changes behavior, so it needs a decision.
 * (c) Pin to a newer cuvs-lucene. No release built against Lucene 10.4 exists yet, so this is not possible today.

My recommendation is (a) now, because the docs promise a fix the ticket does not carry. I would pick (b) only if indexing on 10.1 should stop failing before a cuVS release ships. For (c), the question is whether the cuVS maintainers have a Lucene 10.4 build planned, and when.

Which option do you want?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
