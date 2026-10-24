# Jira comment draft: SOLR-18556, round 4

Status: DRAFT (not posted). Replaces `jira/SOLR-18556-comment-draft-round-2.md` as the single candidate comment. Do not post both. The footer's "reviewed by Nick Shanin" is accurate only after Nick has reviewed this text. Before posting, confirm that the named addressee is a committer sponsor under `comment-formula.md`. The reporter and assignee is Rahul Goswami (live Jira read, 2026-10-24).

---

@Rahul Goswami, I checked the failure against the published jars for cuvs-lucene and Lucene, without a GPU.

 * The ticket's trace is NoSuchMethodError for HnswGraph$NodesIterator.getSortedNodes in Lucene99AcceleratedHNSWVectorsWriter.writeGraph. In cuvs-lucene 25.12.0, writeGraph calls that method, and it is the only class in the jar that does.
 * lucene-core 10.2.0 declares getSortedNodes. lucene-core 10.4.0 does not. Solr 10.1 uses Lucene 10.4.0.
 * Every cuvs-lucene release on Maven Central, up to 26.10.0, declares lucene-core 10.2.0 in its POM. The 26.10.0 jar still calls getSortedNodes.
 * The module's integration test is skipped when no supported GPU is present, so CI on such hosts does not run the GPU path. I did not run that path, and this comment makes no claim about GPU behavior.
 * The 10.1 upgrade notes say the problem is expected to be fixed in a later Solr 10.x release. The ticket has no fix version, so that sentence has no basis yet.

Where this stands:

 * Pinning to a newer cuvs-lucene is not possible today, because no release is built against Lucene 10.4.
 * A compatibility fix for cuvs-lucene is being prepared upstream. The removed call is the only Lucene 10.4 linkage break found in the jar.
 * A Solr-side guard in CuVSCodec would stop 10.1 indexing from failing in the meantime, but it changes behavior: GPU fields would index on the CPU without the user choosing that. No guard is being built.
 * A proposed correction to the two upgrade-note sentences would say that no Solr 10.x fix is scheduled yet. It has not been submitted and needs a decision.

The checks above cover the library's archived-index and query-adapter tests on the CPU. They do not cover GPU construction or search, or Solr integration.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
