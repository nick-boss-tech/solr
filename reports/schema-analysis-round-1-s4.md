# Schema round 1, part s4 (SOLR-16977, SOLR-17047, SOLR-18134)

Result: all three are draftable; 16977 needs the stored-vector call, 17047 needs the quantized and cuVS calls plus two FIXes, and 18134 needs its docs scope fixed and its base overlay confirmed.

Heads checked (local refs match the claim table): 16977 `1142f9563abe13aea113bd80d241db770fd6cdb6`, 17047 `1ba7e33bfe7b86c147f2cd22d6ba517aaa5cbb00`, 18134 `c5a0bdb21e862ec403b37c51c39070d952b972d8`. Bases: 16977 and 17047 merge-base `14c7aac0d15`, 18134 merge-base `14c7aac0d15`. Lucene base is 10.4.0 (`gradle/libs.versions.toml` at `14c7aac0d15`).

Drafts written: `pr-drafts/schema-analysis/SOLR-16977.md`, `SOLR-17047.md`, `SOLR-18134.md`. Each names its head in its Proof.

## Findings

Findings 1 to 4 are SOLR-16977, 5 to 13 are SOLR-17047, 14 to 25 are SOLR-18134, and 26 covers the cross-checks.

**1. FIX (16977, scope of the zero-vector check).** Only the `{!knn}` parser reaches the check. `solr/core/src/java/org/apache/solr/search/vector/KnnQParser.java` L179 calls `getKnnVectorQuery`. `solr/core/src/java/org/apache/solr/search/vector/VectorSimilarityQParser.java` L45-L75 builds its own vector and never calls the check. `solr/core/src/java/org/apache/solr/search/VectorSimilaritySourceParser.java` L92 and L130 (the `vectorSimilarity()` function) do not call it either. Replacement: the draft's Limits already says this. Do not widen the changelog title, which says "A KNN query" and is accurate.

**2. NOTE (16977, changelog wording).** `changelog/unreleased/SOLR-16977.yml` L1 says "instead of an internal error while rendering results". That is the Solr 9.2.1 symptom in `research/jira-context/SOLR-16977.json` (HTTP 500, `DocsStreamer` trace). Nothing here reproduced it on the base. Replacement for L1: "A KNN query with an all-zero vector against a cosine-similarity DenseVectorField now fails with a clear BAD_REQUEST error instead of an unclear HTTP 500 error".

**3. NOTE (16977, on-disk record).** `research/test-queue/results/SOLR-16977.json` records headSha `11fce021b09`. That commit has the same subject as `f3764e038e2` but is not in the branch history (`merge-base --is-ancestor` is false). The fail-before file (`SOLR-16977.failbefore.json`) is for that earlier head and base `56ec140e36`. Its failure line ("Expected exception SolrException but no exception was thrown") fits the test. Receipt log `g16977r35-gate.log` is not on disk. Cite the receipt counts only, and do not cite the JSON.

**4. NOTE (16977, owner call, stored vectors).** The ticket (`research/jira-context/SOLR-16977.json`, last paragraph of the description) says an all-zero stored embedding fails the same way. Nothing in the branch addresses it. The changelog L2 says so. The draft lists it under Limits with a follow-up offer. Owner call: follow-up or this PR (see Owner decisions).

**5. FIX (17047, changelog title).** `changelog/unreleased/SOLR-17047.yml` L1 is about 300 characters and leaves out the quantized behavior change. Replacement for L1: "DenseVectorField KNN settings are validated at core load, including quantized fields under a codec factory that cannot apply them".

**6. FIX (17047, javadoc accuracy).** `solr/core/src/java/org/apache/solr/schema/DenseVectorField.java` L269-L275 says the options "are only honored by a codec factory that supports them, such as SchemaCodecFactory", and that "with any other codec factory they are silently ignored". The cuVS options are honored by the cuVS codec (`solr/modules/cuvs/src/java/org/apache/solr/cuvs/CuVSCodec.java` L66-L73), not by `SchemaCodecFactory`. Under `SchemaCodecFactory` they are silently ignored too (see finding 7). Replacement for L270-L274: "Returns true if the algorithm, the HNSW parameters, or the cuVS parameters are set to a non-default value. SolrCore uses this for codec factories that are not SolrCoreAware. SchemaCodecFactory does not apply the cuVS parameters, and the cuVS codec factory does."

**7. NOTE (17047, owner call, cuVS settings under SchemaCodecFactory).** `SchemaCodecFactory.java` L144-L151 (`validateKnnVectorsOptions`) checks the algorithm and builds the format. It never checks the cuVS settings. A field with `cuvsHnswM="32"` on the default codec loads and ignores it. Options: add a cuVS-specific failure, or keep the draft's Limits line. The draft currently has the Limits line.

**8. NOTE (17047, quantized behavior change, decision drafted as a Choice).** `solr/core/src/java/org/apache/solr/schema/ScalarQuantizedDenseVectorField.java` L124-L128 and `BinaryQuantizedDenseVectorField.java` L44-L48 return `true` unconditionally. `SolrCore.java` L1603 then applies that to any non-SolrCoreAware codec factory. No test covers a quantized field under such a factory. `solrconfig-lucene-codec.xml` is used only by `BadIndexSchemaTest` (L115-L130), with flat and default-option schemas. The draft's "A choice to check" carries this.

**9. NOTE (17047, eager check misses CuVSCodecFactory).** `solr/core/src/java/org/apache/solr/core/SolrCore.java` L1614 runs the eager check only when `factory instanceof SchemaCodecFactory`. `solr/modules/cuvs/src/java/org/apache/solr/cuvs/CuVSCodecFactory.java` L27 is not a subclass. It wraps a `SchemaCodecFactory` as `fallback` (L36). The draft's Limits says so.

**10. NOTE (17047, Schema API timing).** `SolrCore.java` L1096 runs `initCodec` once, at construction. `setLatestSchema` (L365) does not rerun it. `SchemaManager.java` L172 and `AddSchemaFieldsUpdateProcessorFactory.java` L513 call `setLatestSchema`. So a field type added later is not checked until the next core load. The draft's Limits says so.

**11. NOTE (17047, receipt wording, not public).** `receipts/SOLR-17047.md` names three failing tests, then says "The third is the round 35 gap fix ... hnswM=0". The three named are `testKnnVectorOptionsButNoSchemaCodecFactory`, `testInvalidHnswParametersFailAtCoreInit`, and an unsupported-algorithm test. The hnswM gap fix is `testInvalidHnswParametersFailAtCoreInit` (`solr/core/src/test/org/apache/solr/schema/BadIndexSchemaTest.java` L144-L149). The set of three is right. Only the wording is off. The draft names all three.

**12. NOTE (17047, record and caution).** `research/test-queue/results/SOLR-17047.json` shows `BadIndexSchemaTest` 28 of 28. That matches the 28 test methods at `3826cd12d4d`, the head before the round 35 hnswM test was added. The receipt's 29 of 29 is the one to cite. The receipt also says an earlier gap-proof report "rested on a fabricated tool delivery". I did not use that report. The round 35 proof has no gate log on disk.

**13. NOTE (17047, Lucene text).** `BadIndexSchemaTest.java` L148 asserts "maxConn must be positive". Lucene 9.12.0 and 10.4.0 both throw `IllegalArgumentException` with "maxConn must be positive and less than or equal to 512; maxConn=0" (`Lucene99HnswVectorsFormat`, checked by javap). The substring holds on both lines. No change needed. The assertion depends on Lucene's wording.

**14. FIX (18134, upgrade note mechanism).** `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc` L379 says "The field query parser and other parsers then build a phrase from those query tokens." Evidence: `TextField.java` L228-L232 (`parseFieldQuery`) calls `QueryBuilder.createPhraseQuery`, which passes `quoted=true` (lucene-core 10.4.0 bytecode). The standard parser passes `quoted || fieldAutoGenPhraseQueries || autoGeneratePhraseQueries` (`SolrQueryParserBase.java` L534). `fieldAutoGenPhraseQueries` comes from `TextField.getAutoGeneratePhraseQueries()` (L774-L775, L1112-L1113), which is false for schema version above 1.3 (`TextField.java` L74-L78). The shipped configsets are version 1.7 (`_default/conf/managed-schema.xml` L41). For unquoted text with more than one position, Lucene 10.4 then uses `analyzeMultiBoolean`, a SHOULD query. Replacement for L379: "The field query parser, and quoted text, build a phrase from those query tokens. The standard parser does not build a phrase for unquoted text on schema version 1.4 and later."

**15. FIX (18134, filters reference).** `solr/solr-ref-guide/modules/indexing-guide/pages/filters.adoc` L3884 says "so a query-time phrase over the prefixes cannot match a path indexed as a single keyword." Replacement for L3884: "so a phrase over the prefixes, as the field query parser builds it, cannot match a path indexed as a single keyword."

**16. FIX (18134, upgrade note conclusion).** `major-changes-in-solr-10.adoc` L383 says "That configuration produces descendant matching, not ancestor matching." Replacement for L383: "On the phrase path (the field query parser and quoted text), that configuration matches descendant paths, not ancestors."

**17. FIX (18134, base proof overlay, blocking the Proof sentence).** The receipt says `PathHierarchyTokenizerFactoryTest` has one failure on base, but not which files were overlaid. The fixture schema is `solr/core/src/test-files/solr/collection1/conf/schema.xml` (changed at L474-L486). The one fail-before record on disk (`SOLR-16977.failbefore.json`) lists only a `src/test` Java file as overlaid. If the 18134 run did not overlay `src/test-files`, the base failure comes from the base schema, not from the code change. Hold the draft's sentence "On base, with the head test expectations, one of its two tests fails" until the overlay list is confirmed. If the fixture was not overlaid, the Proof has to say the base failure comes from the schema fixture.

**18. NOTE (18134, test schema version).** `solr/core/src/test-files/solr/collection1/conf/schema.xml` L28 is `version="1.0"`. Version 1.0 makes `TextField` auto-generate phrases for every parser (`TextField.java` L74-L78). The shipped configsets are 1.7. The draft's Limits says the unit test covers the field parser path only.

**19. NOTE (18134, changelog title).** `changelog/unreleased/SOLR-18134.yml` L1 says "restore ancestor_path matching". That is wider than the phrase path. Replacement for L1: "Add solr.ZeroPositionIncrementFilterFactory so ancestor_path phrase queries match ancestor paths after Lucene 10 made PathHierarchyTokenizer emit sequential tokens".

**20. NOTE (18134, test comment and blank line).** `solr/core/src/test/org/apache/solr/analysis/PathHierarchyTokenizerFactoryTest.java` L111 reads "(the both-sides Lucene 10 config would)". That is internal shorthand. Replacement for L111: "    // A short path must not match longer descendants." Also delete the blank line at L91, right after `public void testAncestors() {` (L90).

**21. NOTE (18134, on-disk record).** `research/test-queue/results/SOLR-18134.json` lists `TestZeroPositionIncrementFilterFactory` with 4 tests. The head has 5 test methods, and the receipt says 5 of 5. The record is from an older head and points at `worktrees\solr-18134-verify`. Cite the receipt counts only.

**22. NOTE (18134, Lucene behavior verified on both lines).** `PathHierarchyTokenizer.incrementToken` in `lucene-analysis-common` 9.12.0 and 9.12.3 sets position increment 1 for the first token and 0 after it (`ifne` on `resultToken`). In 10.4.0 it sets 1 for every token. So "Lucene 10 emits sequential prefixes" holds on 10.4.0, and "Lucene 9.x already emits overlapping prefixes, so the filter changes nothing there" holds on 9.12. The 10.0 to 10.3 lines were not checked one by one.

**23. NOTE (18134, external links not fetched).** The draft's links to Lucene PR 12875 (in the upgrade note) and Lucene issue 15769 (in the upgrade note and the draft's choice) were not opened. The Jira packet (`research/jira-context/SOLR-18134.json`) names issue 15769 as the Lucene problem, which is the same reference.

**24. NOTE (18134, filter code).** `ZeroPositionIncrementFilter.java` and the factory look correct on reading. The factory follows the `TokenFilterFactory` pattern used by the other factories, and the SPI name `zeroPositionIncrement` matches the configset `<filter name="zeroPositionIncrement" />` lines. The filter's javadoc says "Tokens that already have a zero increment are left unchanged", which holds in effect because setting 0 on a 0 changes nothing.

**25. NOTE (18134, no runtime check).** The SOLR-18134 shipped-configset claim ("the field query parser builds a phrase, so the shipped types fail on Lucene 10") is from code reading and the test expectations. It has not been run. The draft presents it as the failure path, not as a measured result.

**26. Cross-checks (16977 with 17047, and the others).** (a) Both change `solr/core/src/java/org/apache/solr/schema/DenseVectorField.java`, in separate hunks (base lines 521-600 for 16977, 266-287 for 17047). `git merge-tree --write-tree` gives tree `0c331cb28c68016d480e3d014919ee1b7f47c045` in both orders, with no conflicts. (b) The eager call in 17047 (`buildKnnVectorsFormat`, `DenseVectorField.java` L485-L491 at 17047) only builds a format and discards it. It does not write `similarityFunction` or `vectorEncoding`, which the 16977 check reads at query time. So 17047's eager construction changes no assumption in 16977's zero-vector check. (c) No landing dependency. Either order merges cleanly. (d) 18134 shares no files with 16977 or 17047. Trial merges are clean (trees `9a0311592205cb39bd1e48bb9fc751f0bac4ec09` with 17047 and `4d09e193498da4bdeb2ba0f7e5d9e5fbee0632b9` with 16977). (e) 18134 shares no files with 15945 (14 files against 4, empty `comm` intersection).

## Task results

**SOLR-16977 (draftable, with one owner call).** Head `1142f9563abe` matches the claim table and the receipt. The static `@Test` count is 51 at head and 48 at base, which matches the receipt's 51 with 2 skipped and the 3 new tests. The ticket text is in `research/jira-context/SOLR-16977.json` (Solr 9.2.1 report). The check covers only the `{!knn}` path (finding 1). The draft is `pr-drafts/schema-analysis/SOLR-16977.md`, with the stored-vector half in Limits (finding 4). Fix finding 2 before posting. Verdict: draftable once the owner decides the stored-vector half.

**SOLR-17047 (draftable, with owner calls).** Head `1ba7e33bfe7b` matches. The static counts match the receipt: `BadIndexSchemaTest` 29 at head (25 at base plus 4 new), `DenseVectorFieldTest` 48, `ScalarQuantizedDenseVectorFieldTest` 15, `BinaryQuantizedDenseVectorFieldTest` 2, and `TestSchemaCodecFactoryDefaults` 3 test methods. The base failures are the three named tests (finding 11). The draft is `pr-drafts/schema-analysis/SOLR-17047.md`. Fix the changelog title and the javadoc (findings 5 and 6). Owner calls: quantized fields (finding 8) and cuVS settings (finding 7). Verdict: draftable after those calls.

**SOLR-18134 (draftable, held for FIX).** Head `c5a0bdb21e86` matches. The direction (ship the Solr filter now) is kept as the Choice, as the owner recorded it. The draft is `pr-drafts/schema-analysis/SOLR-18134.md`. The docs overstate the phrase mechanism (findings 14, 15, 16). The base proof overlay is unconfirmed (finding 17). The changelog title and one test comment need fixing (findings 19 and 20). Lucene claims hold on 9.12 and 10.4 (finding 22). Verdict: draftable after the docs scope fix and the overlay check.

## Owner decisions

- 16977: the stored-vector half goes in a follow-up ticket (current draft, with an offer) or in this PR.
- 16977: keep the `vectorSimilarity` parser and function out of scope (current draft) or extend the check.
- 17047: quantized fields under a non-Schema codec fail at load (current draft Choice) or the check drops quantization.
- 17047: cuVS settings under SchemaCodecFactory stay a Limit (current draft) or get their own failure.
- 18134: ship the Solr filter now instead of waiting for Lucene (recorded direction, kept as the draft's Choice).
- 18134: approve the phrase-scope wording in the upgrade note and the filters reference (findings 14 to 16).
- 18134: confirm the base run overlaid `src/test-files` before the Proof sentence is used (finding 17).

## Not checked

- No builds, tests, `gh` calls, posts, or fetches. Git was read-only on local refs. Live heads were matched to local refs, not re-fetched.
- Live Jira was not queried. Ticket text comes from `research/jira-context/SOLR-16977.json`, `SOLR-17047.json`, `SOLR-18134.json`, and the Jira CSV export dated 2026-08-18.
- Gate logs named in receipts (`g16977r35-gate.log`, `g17047r35-gate.log`, `g18134r35-gate.log`) are not on disk. I searched the Solr-issues tree to depth 6. GitHub run numbers (17047 run 37711654004, 18134 run 37639809766) were not checked.
- Lucene: javap on `lucene-analysis-common` 9.12.0, 9.12.3, and 10.4.0 (`PathHierarchyTokenizer`), and `lucene-core` 9.12.0 and 10.4.0 (`QueryBuilder`, `Lucene99HnswVectorsFormat`, `VectorUtil`). Lucene 10.0 to 10.3 were not checked one by one. PR 12875 and issue 15769 were not opened.
- Solr query behavior comes from reading source at the local `upstream/main` (`8e62c268688`) and the branch heads. Nothing was run, so the search-time behavior of a zero vector on base 10.4 is unverified.
- Whether the 18134 base run overlaid `src/test-files` (finding 17).
- The cuVS module test `TestCuVSCodecSupportIT` was not read in full.
- Only the test configs found by grep were read for DenseVectorField under non-SolrCoreAware codecs. No full sweep.
- The changelog YAML files were read, not parsed.
- Quantized format constructors may throw exception types other than IllegalArgumentException. Not checked, and the eager check catches only IllegalArgumentException.
- The 17047 receipt's "fabricated tool delivery" note was not reviewed beyond its text.
