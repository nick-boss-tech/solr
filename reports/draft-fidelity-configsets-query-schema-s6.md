# Schema and analysis draft fidelity, slice 6

Assignment: `assignments/pool-draft-fidelity-configsets-query-schema.md`. Claim: `claims/pool-draft-fidelity-configsets-query-schema.md`. Worktree `wt/pr-prepare-suggester` at `d627304e96b`. Read only: no build, no test, no commit, no push, no fork or PR write.

Slice drafts: `pr-drafts/schema-analysis/` SOLR-15358, SOLR-15945, SOLR-16977, SOLR-17047, SOLR-18134, SOLR-9349. Round 1 inputs: `reports/schema-analysis-round-1.md` and `reports/schema-analysis-round-1-s1.md` to `-s4.md`. Receipts: `receipts/<TICKET>.md`. Answers material: all of `material/` was grepped for the six ticket numbers, with no match, so no answers material applies. The drafts carry no separate PR title line, so check 5 was applied to each changelog title against the draft's change summary.

Heads (`git ls-remote origin refs/heads/<branch>`). All six match the head each draft names:
- solr-15358-submit: cbb345f2e7df452e20036d357c82cc63db96ffd3
- solr-15945-submit: adb0fd450c06c07f49ccd187c3d810410a76c5b3
- solr-16977-submit: 1142f9563abe13aea113bd80d241db770fd6cdb6
- solr-17047-submit: 1ba7e33bfe7b86c147f2cd22d6ba517aaa5cbb00
- solr-18134-submit: c5a0bdb21e862ec403b37c51c39070d952b972d8
- solr-9349-submit: 1e79bb42123cddd64767b04a34add2e590a2f244

The base 14c7aac0d151402b00259e2fb9bf5eed7049ec5d resolves and is the merge base for all six. Each changelog fragment the drafts link exists at its head (`git show <head>:changelog/unreleased/...`).

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-15358 | cbb345f2e7df (match) | DRIFT (2 items) |
| SOLR-15945 | adb0fd450c06 (match) | DRIFT (2 items) |
| SOLR-16977 | 1142f9563abe (match) | DRIFT (1 item) |
| SOLR-17047 | 1ba7e33bfe7b (match) | DRIFT (3 items) |
| SOLR-18134 | c5a0bdb21e86 (match) | DRIFT (5 items) |
| SOLR-9349 | 1e79bb42123c (match) | DRIFT (2 items) |

## SOLR-15358

Verdict: DRIFT (2 items).

1. Draft says: "**CurrencyFieldTypeDocValuesTest passes 2 of 2 at this head. The base result is not filled in yet.**" and "Gate run at cbb345f2e7df452e20036d357c82cc63db96ffd3, recorded 2026-10-03: CurrencyFieldTypeDocValuesTest 2 of 2. CurrencyFieldTypeTest 42 tests run, with 21 skipped by its currency locale assumption. Both new tests call no new API, so they compile on base."
   - Evidence: "Gate" and "not filled in yet" are internal process wording (brief check 7). The counts match `receipts/SOLR-15358.md` lines 6 and 7. Round 1 "Draft fixes before posting" asks to replace "Gate run".
   - Replacement (bold line): "**CurrencyFieldTypeDocValuesTest passes 2 of 2 at this head.**"
   - Replacement (Proof line): "Run at cbb345f2e7df452e20036d357c82cc63db96ffd3, recorded 2026-10-03: CurrencyFieldTypeDocValuesTest 2 of 2. CurrencyFieldTypeTest 42 tests run, with 21 skipped by its currency locale assumption. Both new tests call no new API, so they compile on base."

2. Draft says: "Base result: [TO FILL before posting: name the two tests and the failing assertion or error from a base run at the parent commit.]"
   - Evidence: no base run result is on disk. `receipts/SOLR-15358.md` line 7 says only that the pre-fix proof step passed at this head. Round 1 s2 finding 5 says neither new test has a recorded base failure. A placeholder cannot ship.
   - Replacement: "On the base code, both sub-fields are written with the single-value call, which writes no docValues. The two new tests check for those docValues, so they are expected to fail there." Name the two tests and the failing assertion only after a base run confirms it.

Consistent: head; changelog link and title (`changelog/unreleased/SOLR-15358.yml` at head); Limits match round 1 s2 findings 4, 6 and 7 (CurrencyFieldTypeTest L130-L132 and test schema `*_l1_ns` at schema.xml L724 with no docValues); the Choice has a live alternative (owner decision 6) and ends with a question.

Optional notes, not blocking:
- Line 1 is an INTERNAL comment with HOLD and round 1 references. Delete it before posting.
- "docValues as stored" and "copyField target" are compressed jargon. Plain phrasing would help.
- "recorded 2026-10-03" is the receipt's ledger and push date. The receipt gives no separate run date.

## SOLR-15945

Verdict: DRIFT (2 items).

1. Draft says: "The check is in [`AbstractSpatialPrefixTreeFieldType.checkSchemaField`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/schema/AbstractSpatialPrefixTreeFieldType.java#L149-L158). The same method also rejects index options other than `DOCS` ([lines 159-167](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/schema/AbstractSpatialPrefixTreeFieldType.java#L159-L167)). A field with `indexed="false"` reports `IndexOptions.NONE` ([`SchemaField.indexOptions`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/schema/SchemaField.java#L513-L516)), so the second check fails too."
   - Evidence: all three links are pre-change symptom code at the merge base 14c7aac0d15. Base lines were read: AbstractSpatialPrefixTreeFieldType L149-L158 (norms check) and L159-L167 (index options); SchemaField L513-L516 (`indexOptions` returns NONE when not indexed). The text does not say they are pre-change code, which `pr-formula.md` requires.
   - Replacement: "The check is in [`AbstractSpatialPrefixTreeFieldType.checkSchemaField`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/schema/AbstractSpatialPrefixTreeFieldType.java#L149-L158), as it stood before this change. The same method also rejects index options other than `DOCS` ([lines 159-167](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/schema/AbstractSpatialPrefixTreeFieldType.java#L159-L167), also before this change). A field with `indexed="false"` reports `IndexOptions.NONE` ([`SchemaField.indexOptions`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/schema/SchemaField.java#L513-L516), also before this change), so the second check fails too."

2. Draft says: "**Only stored-only fields are covered.**"
   - Evidence: `changelog/unreleased/SOLR-15945.yml` line 1 at head adb0fd450c06 says "DateRangeField and SpatialRecursivePrefixTreeFieldType fields declared with indexed="false" no longer fail to load because of the norms and index-options checks". That is wider than the fix. A field with `indexed="false"` and docValues still fails: base FieldType.java L1194-L1197 calls `checkSupportsDocValues`, and neither class overrides it at head (grep of the schema package). Round 1 s3 finding F9 gives the same replacement.
   - Replacement (changelog title, line 1): "Stored-only DateRangeField and SpatialRecursivePrefixTreeFieldType fields (indexed="false") no longer fail to load because of the norms and index-options checks"

Consistent: head; test and schema links at head (NonIndexedSpatialFieldTest.java L48-L64, schema-nonindexed-spatial.xml L27-L28); the head early return at AbstractSpatialPrefixTreeFieldType.java L146-L152; the Limits match round 1 s3 F13 and F14; the Choice has a live alternative (owner decision 8) and ends with a question.

Optional notes, not blocking:
- The Proof says the test fails on base. That rests on base code reading and on the receipt's "pre-fix proof step passed". The base failure line is not on disk (round 1 s3 F14, owner decision 8). Confirm it before posting.
- "verified 2026-10-03" is the receipt's push and ledger date. Round 1 s3 owner decision 8 asks for the run date to be confirmed.
- Branch text outside the draft, public once posted: the test schema comment at `schema-nonindexed-spatial.xml` L26 (round 1 s3 F10) and the code comment at `AbstractSpatialPrefixTreeFieldType.java` L150 (F11). Replacements are in round 1 s3.
- The branch is 66 commits behind upstream main (round 1 s3 F12). A rebase changes the head and every link.

## SOLR-16977

Verdict: DRIFT (1 item).

1. Draft says: "The error is a Lucene `docID must be >= 0` message raised while the matching documents are written." The changelog title says something the draft does not.
   - Evidence: `changelog/unreleased/SOLR-16977.yml` line 1 at head 1142f9563abe says "...instead of an internal error while rendering results". Round 1 s4 finding 2: nothing reproduced an internal error while rendering on base. The 500 symptom comes from the ticket (Solr 9.2.1). The title states it as base behavior, so it is wider than the evidence.
   - Replacement (changelog title): "A KNN query with an all-zero vector against a cosine-similarity DenseVectorField now fails with a clear BAD_REQUEST error instead of an unclear HTTP 500 error"

Consistent: head; Proof counts (DenseVectorFieldTest 51 tests, 2 skipped, 0 failures; base 3 tests with 1 failure, `zeroQueryVector_cosineSimilarity_shouldBeRejected`), matching `receipts/SOLR-16977.md` lines 6 and 7. Citations hold at head: DenseVectorField.java L503-L524 (method through the check call at L524), L573-L597 (helper), KnnQParser.java L179 (call), VectorSimilarityQParser.java L45-L75 (builds its own vector, no check call), DenseVectorFieldTest.java L1316-L1357 (three tests calling `getKnnVectorQuery` directly). Limits and the absence of a Choice section match round 1 s4 findings 1 and 4.

Optional notes, not blocking:
- The Limits offer ("I can open a follow-up ticket and PR") presumes the follow-up route. Round 1 owner decision 11 is open. Keep it only after the owner picks that route.
- "verified 2026-10-07" is the receipt's round-35 takeover-log date. Same record-date basis as the other drafts.
- Round 1 s4 finding 3: `research/test-queue/results/SOLR-16977.json` records another head. Do not cite that file.

## SOLR-17047

Verdict: DRIFT (3 items).

1. Draft says: "The error appears only when a segment is first written, because the vectors format is built per field at that point ([SchemaCodecFactory](https://github.com/nick-boss-tech/solr/blob/1ba7e33bfe7b86c147f2cd22d6ba517aaa5cbb00/solr/core/src/java/org/apache/solr/core/SchemaCodecFactory.java#L126-L139))."
   - Evidence: this is pre-change symptom code. Base 14c7aac0d15 L126-L139 is the same `getKnnVectorsFormatForField` method, with the per-field build at L136-L137. At head, L126-L139 also contains the new `validateKnnAlgorithm` call (L131), so the head link does not show the code the sentence describes.
   - Replacement: "The error appears only when a segment is first written, because the vectors format is built per field at that point ([SchemaCodecFactory, code before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/core/SchemaCodecFactory.java#L126-L139))."

2. Draft says: "Postings and docValues formats already work this way ([SolrCore](https://github.com/nick-boss-tech/solr/blob/1ba7e33bfe7b86c147f2cd22d6ba517aaa5cbb00/solr/core/src/java/org/apache/solr/core/SolrCore.java#L1603-L1611))."
   - Evidence: at head, SolrCore.java L1585-L1593 is the postings-format check and L1594-L1602 is the docValues-format check. L1603-L1611 is only the DenseVectorField branch. The cited range misses the formats the sentence names.
   - Replacement: "Postings and docValues formats already work this way ([SolrCore](https://github.com/nick-boss-tech/solr/blob/1ba7e33bfe7b86c147f2cd22d6ba517aaa5cbb00/solr/core/src/java/org/apache/solr/core/SolrCore.java#L1585-L1602)). The KNN check at [lines 1603-1611](https://github.com/nick-boss-tech/solr/blob/1ba7e33bfe7b86c147f2cd22d6ba517aaa5cbb00/solr/core/src/java/org/apache/solr/core/SolrCore.java#L1603-L1611) follows the same pattern."

3. Draft says: "Quantized field types always count as a setting that differs from the default". The changelog title leaves this out.
   - Evidence: `changelog/unreleased/SOLR-17047.yml` line 1 at head 1ba7e33bfe7b is about 300 characters and does not mention quantized fields or the cagra_hnsw change. Round 1 s4 finding 5 gives the same replacement.
   - Replacement (changelog title): "DenseVectorField KNN settings are validated at core load, including quantized fields under a codec factory that cannot apply them"

Consistent: head; Proof counts (BadIndexSchemaTest 29 of 29; DenseVectorFieldTest 48; TestSchemaCodecFactoryDefaults 3; ScalarQuantized 15; BinaryQuantized 2) match `receipts/SOLR-17047.md` line 6. The three named base failures exist at head (BadIndexSchemaTest.java L120, L137, L144), and the fourth new test (L127) is the guard. The Choice has a live alternative (owner decision 12) and ends with a question. The Limits (cuVS settings not checked under SchemaCodecFactory; eager check only for an exact SchemaCodecFactory; Schema API timing) match round 1 s4 findings 7, 9 and 10.

Optional notes, not blocking:
- "non-SolrCoreAware codec factory" and "cuVS" are compressed jargon. The Choice paragraph is dense.
- "verified 2026-10-07" is the round-35 record date, as for 16977 and 18134.
- Branch text outside the draft, public once posted: the javadoc at `DenseVectorField.java` L269-L275 says cuVS options are silently ignored under any non-cuVS codec. Round 1 s4 finding 6 gives the replacement for L270-L274: "Returns true if the algorithm, the HNSW parameters, or the cuVS parameters are set to a non-default value. SolrCore uses this for codec factories that are not SolrCoreAware. SchemaCodecFactory does not apply the cuVS parameters, and the cuVS codec factory does."

## SOLR-18134

Verdict: DRIFT (5 items).

1. Draft says: "The shipped `ancestor_path` type indexes each path as one keyword and queries with `PathHierarchyTokenizer` ([_default schema](https://github.com/nick-boss-tech/solr/blob/c5a0bdb21e862ec403b37c51c39070d952b972d8/solr/server/solr/configsets/_default/conf/managed-schema.xml#L513-L520))."
   - Evidence: pre-change symptom code. At head, L513-L520 of the `_default` managed-schema includes the new `zeroPositionIncrement` filter at L519. Base 14c7aac0d15 L513-L520 is the pre-change type.
   - Replacement: "The shipped `ancestor_path` type indexes each path as one keyword and queries with `PathHierarchyTokenizer` ([_default schema, code before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/server/solr/configsets/_default/conf/managed-schema.xml#L513-L520))."

2. Draft says: "It used PathHierarchy on both sides before ([test schema](https://github.com/nick-boss-tech/solr/blob/c5a0bdb21e862ec403b37c51c39070d952b972d8/solr/core/src/test-files/solr/collection1/conf/schema.xml#L474-L486))."
   - Evidence: the "before" state is base 14c7aac0d15 L474-L486, which has PathHierarchyTokenizerFactory on both analyzers. Head L474-L486 shows the new keyword index and filter, so the link does not show what the sentence describes.
   - Replacement: "It used PathHierarchy on both sides before ([test schema, code before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/test-files/solr/collection1/conf/schema.xml#L474-L486))."

3. Draft says: "Lucene 9.x already emits overlapping prefixes, so the filter changes nothing there."
   - Evidence: round 1 s4 finding 22 read `PathHierarchyTokenizer` in lucene-analysis-common 9.12.0 and 9.12.3 only. Lucene 9.0 to 9.11 were not checked, so the claim covers the 9.12 line only.
   - Replacement: "Lucene 9.12 already emits overlapping prefixes, so the filter changes nothing there."

4. Draft says: "On base, with the head test expectations, one of its two tests fails."
   - Evidence: `receipts/SOLR-18134.md` line 7 records one failure on base, with no overlay list. Round 1 s4 finding 17: whether the base run overlaid `src/test-files` is not confirmed. If it did not, the failure may come from the base schema fixture, not from the filter.
   - Replacement (use unless the owner confirms the overlay): "On base, one of its two tests fails." If the owner confirms that the base run overlaid both `src/test` and `src/test-files`, the original sentence stands.

5. Draft says: "- The unit tests cover the field query parser path only." The changelog title is wider than that.
   - Evidence: `changelog/unreleased/SOLR-18134.yml` line 1 at head c5a0bdb21e86 says "Add solr.ZeroPositionIncrementFilterFactory to restore ancestor_path matching after Lucene 10 sequential PathHierarchy tokens." "Restore ancestor_path matching" is wider than the field query parser path that the draft and tests cover. Round 1 s4 finding 19.
   - Replacement (changelog title): "Add solr.ZeroPositionIncrementFilterFactory so ancestor_path phrase queries match ancestor paths after Lucene 10 made PathHierarchyTokenizer emit sequential tokens"

Branch text outside the draft (not counted; fix before posting, since the PR diff is public):
- `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc` L379 "The field query parser and other parsers then build a phrase from those query tokens." Replacement (round 1 s4 finding 14): "The field query parser, and quoted text, build a phrase from those query tokens. The standard parser does not build a phrase for unquoted text on schema version 1.4 and later."
- Same file L383 "That configuration produces descendant matching, not ancestor matching." Replacement (s4 finding 16): "On the phrase path (the field query parser and quoted text), that configuration matches descendant paths, not ancestors."
- `solr/solr-ref-guide/modules/indexing-guide/pages/filters.adoc` L3884 (s4 finding 15): replace with "so a phrase over the prefixes, as the field query parser builds it, cannot match a path indexed as a single keyword."
- `solr/core/src/test/org/apache/solr/analysis/PathHierarchyTokenizerFactoryTest.java` L111 "(the both-sides Lucene 10 config would)" becomes "    // A short path must not match longer descendants." Delete the blank line at L91 (s4 finding 20). This file is inside the Proof link L90-L116.

Consistent: head; the filter and factory links (ZeroPositionIncrementFilter.java L24-L59 and the factory L23-L65 hold the code the draft describes); Proof counts (TestZeroPositionIncrementFilterFactory 5 methods, PathHierarchyTokenizerFactoryTest 2 methods, matching the receipt); configset links L678-L686 in sample_techproducts (head, filter present); test-schema version 1.0 and configset version 1.7 (checked at head). The Choice has a live alternative (wait for Lucene issue 15769; owner decision 13) and ends with a question.

Optional notes, not blocking:
- The Lucene 10 claims were read on 10.4.0 only (round 1 s4 finding 22). The Solr build uses 10.4, so the draft is accurate for it.
- Lucene issue 15769 and PR 12875 were not opened (round 1 s4 finding 23).
- "verified 2026-10-07" is the round-35 record date.
- "phrase over consecutive positions" and "SHOULD query" are compressed. Plain phrasing would help.

## SOLR-9349

Verdict: DRIFT (2 items).

1. Draft says: "Changelog: `changelog/unreleased/SOLR-9349-refuse-delete-uniquekey-field.yml`"
   - Evidence: the file exists at head 1e79bb42123c, but the line is a bare path, not a link at the head SHA (brief check 4; `pr-formula.md` changelog rule).
   - Replacement: "Changelog: [changelog/unreleased/SOLR-9349-refuse-delete-uniquekey-field.yml](https://github.com/nick-boss-tech/solr/blob/1e79bb42123cddd64767b04a34add2e590a2f244/changelog/unreleased/SOLR-9349-refuse-delete-uniquekey-field.yml)"

2. Draft says: "The failure shows up later as a server error from the persist step ("Unable to persist managed schema"), not as a clear refusal."
   - Evidence: the changelog title (line 1-2 of the fragment) says "instead of leaving a schema that fails to load". The draft and `receipts/SOLR-9349.md` line 7 support only a server error from the persist step. Neither shows a schema that fails to load.
   - Replacement (changelog title): "The Schema API delete-field command now refuses to delete the uniqueKey field with a 400 error, instead of failing later with a server error from the persist step."

Consistent: head 1e79bb42123c; Proof counts (TestBulkSchemaAPI 17, with 16 on base; TestFieldCollectionResource 6; TestFieldResource 5; TestUniqueKeyFieldResource 1; total 29) match `receipts/SOLR-9349.md` lines 6 and 7; `testDeleteUniqueKeyFieldRefused` exists at head (TestBulkSchemaAPI.java L712). The code at head matches the draft: in `ManagedIndexSchema.deleteFields` the uniqueKey check sits inside `if (null != field)` and before the copy-field checks, uses BAD_REQUEST, and has the message "Can't delete field '<name>' because it is the uniqueKey field." No Choice section is correct. The df and qf follow-up in Limits is the one round 1 s1 owner decision 5 leaves open.

Optional notes, not blocking:
- Not a draft text issue, but it blocks posting: the fork branch's commit messages become public on the PR. Commit `16129c64b81` says "Hypothetical, unrun regression test; see SOLR-9349-TESTING.md." Other subjects are "add hypothetical-reproduction handoff doc" and "remove the handoff doc before submission" (round 1 s1 finding 5). Squashing rewrites the fork branch (owner decision 1). A squash also changes the head SHA, the Proof, and the changelog link.
- "on 2026-10-05" is the receipt's push and reconciliation-gate date.

## Not done

- Gate logs, JUnit XML, and any base run for 15358 are not on disk. Counts and base failures were checked against receipts only. Nothing was re-run.
- Live JIRA was not read. Ticket text was not checked against the drafts.
- Lucene versions beyond 9.12 and 10.4 were not checked. The upgrade-note and filters.adoc text was taken from round 1 s4 quotes, not re-read.
- For 15358, "compile on base" was checked only by confirming that the test helpers the new test calls exist at base b5c71bc5573. No compile was run.
- The verification-date basis (receipt record date versus run date) is noted, not resolved, for all six drafts.
- Lucene issue 15769 and PR 12875 links were not opened.
