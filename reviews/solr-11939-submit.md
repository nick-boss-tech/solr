# solr-11939-submit

- Branch: origin/solr-11939-submit
- Head: d4cff5e76430 (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base b6b2b8f10e9, 18 commits behind)
- Scope: 3 files, +32 lines. `solr/solr-ref-guide/.../deployment-guide/pages/collection-management.adoc` (a 3-line NOTE under the CREATE `property._name_=_value_` entry), `changelog/unreleased/SOLR-11939-property-name-docs.yml`, `SOLR-11939-TESTING.md` (author's hypothetical note, left in place). No Java changes.
- Verdict: Ready for review (docs only, at d4cff5e76430)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

No defects found. Checked against the code:

- verified: the NOTE is true for CREATE. `CreateCollectionCmd` builds each core name with `Assign.buildSolrCoreName` (around line 305) and never reads `property.name`. So `property.name` does not rename the cores of a new collection, as the NOTE says.
- verified: the example `techproducts_shard1_replica_n1` matches `Assign.buildSolrCoreName` (`<collection>_<shard>_replica_<type letter><n>`), with `n` for NRT.
- verified, scope caution for the owner: ADDREPLICA does honour `property.name` as a fallback core name (`AddReplicaCmd.java` around lines 355-357, same on this base). The NOTE is correctly limited to "a new collection", so it is accurate. It must not be copied onto the ADDREPLICA `property._name_` entry, because that would be wrong for a single-replica add. Whether ADDREPLICA should document that fallback is outside this ticket. Posed, not decided.
- verified: the `++` / `+NOTE:` continuation attaches the NOTE to the `property._name_` list item (`collection-management.adoc` around lines 263-265), before `waitForFinalState`.
- LOW: the changelog title is a full sentence. Style only, left to the Linux-side changelog parse.

## Not checked
- Nothing built or rendered. The AsciiDoc was checked by reading the list structure only, not by the ref-guide build.
- The changelog YAML was not checked against the logchange schema.
- The JIRA premise (reporter's `property.name` expectation) was taken from the author's note, not re-read from JIRA.
- No Gradle, no spotless, no tests.
