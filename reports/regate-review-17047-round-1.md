# SOLR-17047 re-gate review, round 1

Verdict. SOLR-17047 is CURRENT AT THE RE-GATE HEAD `79fc289bfc1a`. The changelog title and the javadoc now match the two recorded replacements, the delta is exactly the two files the gate names, and the draft's citations and counts hold. No draft edit is needed.

Inputs: `claims/pool-regate-review-17047-round-1.md`, the re-gate record `gates/SOLR-17047-branch-items.md`, and the refreshed `receipts/SOLR-17047.md`. Two read-only reviewers checked the draft and the branch delta. The first pass could not read the re-gate commit, because it was not in this clone. The review agent then fetched `solr-17047-submit` read-only, and the branch checks were re-run against the fetched commit.

## Checks

1. **Heads.** The fork tip of `solr-17047-submit` is `79fc289bfc1a`, the re-gate head. Its parent is `1ba7e33bfe7b`, the earlier head. Every head in the draft is either the earlier head, the merge-base `14c7aac0d151`, or the re-gate head in the changelog link. Nothing names an unknown head.
2. **Delta.** `git diff --stat 1ba7e33bfe7b 79fc289bfc1a` shows two files: `changelog/unreleased/SOLR-17047.yml` (one line) and `solr/core/src/java/org/apache/solr/schema/DenseVectorField.java` (javadoc only). This is the delta the gate job lists.
3. **Changelog title (SCH-R1-32).** The title at `79fc289` is "DenseVectorField KNN settings are validated at core load, including quantized fields under a codec factory that cannot apply them." It matches the recorded replacement character for character.
4. **Javadoc (SCH-R1-33).** The only hunk in `DenseVectorField.java` is in the javadoc of `hasNonDefaultKnnOptions()`. The new text is "Returns true if the algorithm, the HNSW parameters, or the cuVS parameters are set to a non-default value. SolrCore uses this for codec factories that are not SolrCoreAware. SchemaCodecFactory does not apply the cuVS parameters, and the cuVS codec factory does." It matches the recorded replacement. The method body is unchanged.
5. **Draft citations.** Every citation in the draft resolves at its named commit with its line range inside the file. The changelog link names `79fc289bfc1a`, and the file exists there. The draft's other citations name the earlier head or the merge-base, and they stay correct because those files did not change in the re-gate.
6. **Proof counts and dates.** Each Proof count comes from `receipts/SOLR-17047.md`, and the regression counts stay tied to the earlier head. `BadIndexSchemaTest` 29 of 29 was re-run at the re-gate head, so that count holds at either head. The draft has no dates to check.
7. **Text the re-gate touched.** The draft quotes neither the old title nor the old javadoc claim ("silently ignore"). Its nearest paraphrases still hold under the new text. For example, "the default codec still ignores them" matches the corrected javadoc, not the old claim.

## Notes for the main side

- The new javadoc does not list quantization among the options it names. The draft's quantization statements rest on the `ScalarQuantizedDenseVectorField` override, which the draft links correctly. This is not a mismatch, but a reader may notice the gap.
- The cuVS parameters are applied only on the CAGRA HNSW path. The javadoc does not say otherwise.

## Not done

- No build, test, Gradle run, `gh` call, PR write, comment or post.
- No branch was edited. The fetch was read-only.
