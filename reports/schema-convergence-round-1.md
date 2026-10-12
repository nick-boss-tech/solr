# Schema convergence, round 1

Verdict: **NOT CONVERGED.** All eight drafts hold open items. Two carry a blocking finding (SOLR-15357 and SOLR-18134); six carry should-fix findings only.

Counts: drafts checked 8 (slice A: 9349, 14199, 15357, 15358; slice B: 15945, 16977, 17047, 18134). Drafts with no finding 0. Drafts with blocking findings 2. Drafts with should-fix findings only 6. Drafts with notes only 0. Findings 38: 3 blocking (SCH-R1-09, 10, 21), 25 should-fix, 10 note. Register IDs SCH-R1-01 to 17 come from slice A; SCH-R1-21 to 41 from slice B. Carried items are listed with the branch-side changes they wait on.

Scope: `assignments/pool-schema-convergence-round-1.md`. The decision record is `reports/schema-analysis-round-1.md` and its parts, with the draft-fidelity settlements. Receipts were read at the local `origin/pr-prepare` ref; the live receipts tip could not be read in the subagent's view, so a receipt change would need a re-check. Heads were checked with `git ls-remote origin refs/heads/<branch>`, and all eight match their named heads. Two read-only subagents did the review. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-9349: NOT CONVERGED.** SCH-R1-01, 02, 03 (should-fix); SCH-R1-04 (note).
- **SOLR-14199: NOT CONVERGED.** SCH-R1-05, 06 (should-fix); SCH-R1-07, 08 (note). The CONFIRM and HOLD markers match the record.
- **SOLR-15357: NOT CONVERGED (blocking).** SCH-R1-09, 10 (blocking); SCH-R1-11, 12 (should-fix); SCH-R1-13 (note).
- **SOLR-15358: NOT CONVERGED.** SCH-R1-14, 15 (should-fix); SCH-R1-16, 17 (note).
- **SOLR-15945: NOT CONVERGED.** SCH-R1-22, 30, 37 (should-fix); SCH-R1-38 (note).
- **SOLR-16977: NOT CONVERGED.** SCH-R1-23, 24, 31 (should-fix); SCH-R1-39 (note).
- **SOLR-17047: NOT CONVERGED.** SCH-R1-25, 26, 27, 32, 33 (should-fix); SCH-R1-41 (note).
- **SOLR-18134: NOT CONVERGED (blocking).** SCH-R1-21 (blocking); SCH-R1-28, 29, 34, 35, 36 (should-fix); SCH-R1-40 (note).

## Register: blocking rows

**SCH-R1-09 | SOLR-15357 | blocking.** Line 26 says "**CopyFieldSubFieldsTest passes 4 of 4 at this head, but it shows no failure on the base code.**" The receipt classifies the base proof as inconclusive, because the class does not compile on base. Replacement: "**CopyFieldSubFieldsTest passes 4 of 4 at this head. The proof is inconclusive on the base code by construction.**" Delete the last sentence of line 28.

**SCH-R1-10 | SOLR-15357 | blocking.** Line 41 says "No test covers PointType or BBoxField sub-fields, the glob case, or the delete refusal." The test asserts the `loc` (PointType) and `box` (BBoxField) sub-fields are copy targets. Replacement: "- The copy target counts for PointType (`loc`) and BBoxField (`box`) sub-fields are tested. Real-time get and atomic update output for those types are not. No test covers a glob copyField destination or the delete refusal."

**SCH-R1-21 | SOLR-18134 | blocking.** The Proof says "On base, with the head test expectations, one of its two tests fails." The receipt names one failure on base but says the src/test-files overlay was not separately recorded. Replacement: "On base, one of its two tests fails." Keep the longer sentence only if the owner confirms the base run overlaid both source and test files.

## Register: should-fix rows

**SCH-R1-01 | SOLR-9349 | should-fix.** Line 7 says "The Schema API accepts a request to delete the uniqueKey field." The base request fails with a server error, which the draft's own Proof states. Replacement: "**The Schema API does not refuse a request to delete the uniqueKey field.**" Line 9 "The request goes through." becomes "No check stops the request in that method."

**SCH-R1-02 | SOLR-9349 | should-fix.** `ManagedIndexSchema.deleteFields` is named without links. Link the head check at line 499 to 503 (`#L499-L503` at `1e79bb42123cddd64767b04a34add2e590a2f244`) on line 15, and the base copy check at line 503 to 505 (`#L503-L505` at `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`), labelled "at the base commit", on line 9.

**SCH-R1-03 | SOLR-9349 | should-fix.** The Proof (line 17) and the Limits (line 29) have no bold lead. Proof: "**The new test fails on the base code and passes with this change.**" Limits: "**This change checks only the uniqueKey field. Other references and changing the uniqueKey are not covered.**"

**SCH-R1-05 | SOLR-14199 | should-fix.** The Proof, Choice and Limits have no bold lead. Proof: "**The new test fails on the base code and passes with this change.**" Choice: "**Point types now report `omitNorms` as true on schema versions 1.4 and older too.**" Limits: "**Field checks cover indexed fields only. The Schema API path and stored-only point fields have no test.**"

**SCH-R1-06 | SOLR-14199 | should-fix.** The file citations are bare. Link each at `e743c90da79a155fd6e6161233af5d924dc9dad1`: `PointField.checkSchemaField` (lines 96 to 110), the upgrade note (lines 30 to 35), and `BadIndexSchemaTest` (lines 35 to 40).

**SCH-R1-11 | SOLR-15357 | should-fix.** Line 40 says currency sub-fields have no docValues, so currency output does not change. A stored currency sub-field would leave real-time get output. Replacement: "- Currency sub-fields are written without docValues on this branch. For sub-fields that are not stored, as in the test schema, currency output does not change. A stored currency sub-field would no longer appear in real-time get output."

**SCH-R1-12 | SOLR-15357 | should-fix.** No mention of SOLR-9349, which changes the same method. Add after the second behavior bullet: "- This check is in the same method that SOLR-9349 changes for the uniqueKey field. This change lands after SOLR-9349."

**SCH-R1-14 | SOLR-15358 | should-fix.** Lines 19 and 31 omit the schema version condition. The base code sets the stored-value default only on schema version 1.6 and later. Replacement: line 19 "With the default docValues as stored setting (schema version 1.6 and later), a docValues field that is not stored can be returned as a stored value." Line 31 "**Sub-fields with docValues are returned as stored values by default on schema version 1.6 and later.**"

**SCH-R1-15 | SOLR-15358 | should-fix.** The landing dependency on SOLR-15357 is in the HOLD but not in the public text. Insert before the last Limits line, once the owner confirms the order: "- Land SOLR-15357 first. Without its copy target change, an atomic update could carry the derived currency values into the document it sends back."

**SCH-R1-22 | SOLR-15945 | should-fix.** The Limits say "SOLR-15403 describes the same index-options check on this path, and this change covers that check." Duplicate status is not established. Replacement: "SOLR-15403 describes the same index-options check. Duplicate status is not established. Whether the two tickets are duplicates is for maintainers to decide."

**SCH-R1-30 | SOLR-15945 | should-fix (branch changelog).** The title says indexed fields with docValues no longer fail. Indexed fields with docValues still fail. Branch title: "Stored-only DateRangeField and SpatialRecursivePrefixTreeFieldType fields (indexed="false") no longer fail to load because of the norms and index-options checks."

**SCH-R1-37 | SOLR-15945 | should-fix (branch comment).** The XML comment says the invariants "hold"; the checks are skipped. Replacement: "<!-- stored-only: the norms and index-options checks do not apply to these fields, so the type skips them -->".

**SCH-R1-23 | SOLR-16977 | should-fix.** The Limits say "That case fails the same way at query time, and this change does not cover it." The ticket reports a similar error, not reproduced here. Replacement: "The ticket reports a similar error for this case, and this change does not cover it."

**SCH-R1-24 | SOLR-16977 | should-fix.** "## Limits" opens with a bullet. Insert: "**The stored-vector case and two other query paths are not covered.**"

**SCH-R1-31 | SOLR-16977 | should-fix (branch changelog).** The title ends "instead of an internal error while rendering results", which nothing here reproduced. Branch title: "A KNN query with an all-zero vector against a cosine-similarity DenseVectorField now fails with a clear BAD_REQUEST error instead of an unclear HTTP 500 error."

**SCH-R1-25 | SOLR-17047 | should-fix.** "## Limits" opens with a plain bullet. Insert: "**Quantized fields under other codec factories are untested, and cuVS settings are not checked.**"

**SCH-R1-26 | SOLR-17047 | should-fix.** No Limit names the untested quantized case the Choice describes. Add: "- No test covers a quantized field under a codec factory that cannot apply quantization."

**SCH-R1-27 | SOLR-17047 | should-fix.** The SolrCore link at line 1585 to 1602 is the head, for code the change does not alter. Replacement: "Postings and docValues formats already work this way ([SolrCore, code before this change](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/core/SolrCore.java#L1584-L1601))."

**SCH-R1-32 | SOLR-17047 | should-fix (branch changelog).** The changelog is about 300 characters and omits the quantized change. Branch title: "DenseVectorField KNN settings are validated at core load, including quantized fields under a codec factory that cannot apply them."

**SCH-R1-33 | SOLR-17047 | should-fix (branch comment).** `DenseVectorField.java` lines 269 to 275 say other codec factories "silently ignore" the cuVS options. Only the cuVS codec honors them. Replacement for lines 270 to 274: "Returns true if the algorithm, the HNSW parameters, or the cuVS parameters are set to a non-default value. SolrCore uses this for codec factories that are not SolrCoreAware. SchemaCodecFactory does not apply the cuVS parameters, and the cuVS codec factory does."

**SCH-R1-28 | SOLR-18134 | should-fix.** "On Lucene 10, a phrase query on `ancestor_path`" and "Lucene 10 gives those prefixes consecutive positions" name a line that was checked only at 10.4. Replacement: "On Lucene 10.4, ..." in both places. Add a Limit: "The Lucene 10.0 to 10.3 releases were not checked one by one."

**SCH-R1-29 | SOLR-18134 | should-fix.** "## Limits" has no bold summary, and the first bullet overlaps the Proof. Insert: "**Quoted text and the shipped configsets are not covered by any test.**" The first bullet becomes "The PathHierarchy test covers the field query parser path only. The filter test checks the filter alone."

**SCH-R1-34 | SOLR-18134 | should-fix (branch changelog).** The title is wider than the field parser path. Branch title: "Add solr.ZeroPositionIncrementFilterFactory so ancestor_path phrase queries match ancestor paths after Lucene 10 made PathHierarchyTokenizer emit sequential tokens."

**SCH-R1-35 | SOLR-18134 | should-fix.** The upgrade note says the field query parser "builds a phrase" for all parsers, but the standard parser builds no phrase for unquoted text on schema 1.4 and later. Replacements: upgrade note "The field query parser, and quoted text, build a phrase from those query tokens. The standard parser does not build a phrase for unquoted text on schema version 1.4 and later." and "On the phrase path (the field query parser and quoted text), that configuration matches descendant paths, not ancestors." The `filters.adoc` phrase becomes "so a phrase over the prefixes, as the field query parser builds it, cannot match a path indexed as a single keyword."

**SCH-R1-36 | SOLR-18134 | should-fix (branch comment).** The test comment "(the both-sides Lucene 10 config would)" is internal shorthand. Replacement: "    // A short path must not match longer descendants." Delete the blank line before it.

## Notes

SCH-R1-04 (the base count names no commit; the changelog and head move with the owner's squash), SCH-R1-07 and 08 (SOLR-14199's OWNER HOLD for the Limits wording, and the "returns 400" claim rests on Lucene disassembly), SCH-R1-13 (SOLR-15357 length and the `/get` asserts that cannot fail until field names are fixed), SCH-R1-16 (SOLR-15358's base run must overlay the new test file), SCH-R1-17 (the internal comment blocks come out before posting), SCH-R1-38 (SOLR-15945's base failure line is not on record), SCH-R1-39 (the HTTP 500 to 400 wording is a code mapping, not a tested HTTP result), SCH-R1-40 (SOLR-18134's base leg is a compile failure, which the Proof should say), and SCH-R1-41 (SOLR-17047 length, and "exact" in the codec check can go).

## Carried and branch-side items

Several should-fix rows (SCH-R1-30 to 37) are text in the PR diff. They are listed so the drafts can converge, and each waits on a branch commit that moves the head. The Proof counts for SOLR-9349, 14199, 15357 and 15358 match their receipts.

## Not checked

- The live receipts tip, which was not readable in the subagent's view; the receipts were read at the local `origin/pr-prepare` ref.
- Lucene 10.0 to 10.3 class files, and external Lucene and issue links.
- Gate logs and JUnit XML, which are not on disk.
- Live Jira and GitHub state.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Three blocking and twenty-five should-fix findings are open across the eight drafts.
