# Schema convergence, round 1: main-side response

Round 1 (`reports/schema-convergence-round-1.md`) returned NOT CONVERGED: 3 blocking, 25 should-fix and 10 note findings across the eight drafts in `pr-drafts/schema-analysis/`. Each blocking and should-fix finding was checked against the receipt in `receipts/` and the branch code before anything was applied. Every finding held. Nothing was rebutted. Applied items carry the round's exact replacement text, with one disclosed deviation below (SCH-R1-29).

## Counts

| Grade | Findings | Applied to drafts | Held (record runs and owner items) | Branch-side (recorded verbatim) | Standing (record items) |
|---|---|---|---|---|---|
| Blocking | 3 | 3 | 0 | 0 | 0 |
| Should-fix | 25 | 15 | 2 | 8 | 0 |
| Note | 10 | 0 | 0 | 0 | 10 |
| **Total** | **38** | **18** | **2** | **8** | **10** |

The two held should-fix items are inside the SOLR-14199 and SOLR-15358 drafts (their record runs), counted in the per-finding dispositions below; the counts table counts per finding, and SCH-R1-05/06 (applied) sit in the same drafts.

## Fact checks run before applying

- SCH-R1-02 (SOLR-9349): at head `1e79bb42123c`, `ManagedIndexSchema` lines 499-503 are the uniqueKey check; at base `14c7aac0d151`, lines 503-505 are the copy-field check. Both anchors in the replacement hold.
- SCH-R1-06 (SOLR-14199): at head `e743c90da79a`, `PointField.checkSchemaField` begins at line 96 (range 96-110), the upgrade note's omitNorms section is at `major-changes-in-solr-11.adoc` lines 30-35, and `BadIndexSchemaTest.testSevereErrorsForPointFieldOmitNorms` is at lines 35-40. All three replacement anchors hold.
- SCH-R1-09 (SOLR-15357, blocking): `receipts/SOLR-15357.md` classifies the base proof as inconclusive by construction: the test class calls the new `FieldType.getSubFields` API and cannot compile on base. The replacement's proof wording matches the receipt.
- SCH-R1-10 (SOLR-15357, blocking): `CopyFieldSubFieldsTest` at head `d717899b8736309bd72f54b11230f198ae226e7d` asserts `loc`, `amount`, `box` and `price_c` and their sub-fields are copy targets (lines 87-94 and 101-109). The replacement's coverage statement holds.
- SCH-R1-11 (SOLR-15357): holds by the draft's own mechanism: sub-fields now counted as copy targets are filtered from real-time get output; a stored currency sub-field is such a target, so it would no longer appear there.
- SCH-R1-12 (SOLR-15357): SOLR-9349's uniqueKey check and SOLR-15357's delete refusal both sit in `ManagedIndexSchema.deleteFields` (at SOLR-9349's head, lines 499-503 are the uniqueKey throw; the copy-target check follows in the same method), so the same-method bullet holds. Placement: the bullet was inserted after the second Behavior-changes bullet, directly under the delete-refusal bullet it comments on.
- SCH-R1-14 (SOLR-15358): `FieldType.java` line 190 at base reads `if (schemaVersion >= 1.6f) properties |= USE_DOCVALUES_AS_STORED;`. The replacement holds.
- SCH-R1-15 (SOLR-15358): the register conditions the landing-order bullet on owner confirmation. The record carries it: `reports/schema-analysis-round-1.md` line 40 and the s2 finding 8 state the atomic update path needs SOLR-15357's copy-target skip, and the roll-up order (line 74) is "9349, 14199, 15357, 15358". The condition is met by the record, and the bullet was applied.
- SCH-R1-21 (SOLR-18134, blocking): `receipts/SOLR-18134.md` records the base leg as 2 tests with 1 failure (`testAncestors`) and states the `src/test-files` overlay was not separately recorded, so the shorter replacement "On base, one of its two tests fails." is the supported form. Applied.
- SCH-R1-22 (SOLR-15945): `reports/schema-analysis-round-1-s3.md` finding F13 confirms SOLR-15403's duplicate status is not established, so the replacement's "duplicate status not established" wording holds.
- SCH-R1-27 (SOLR-17047): `SolrCore.java` at base lines 1584-1601 is the postings/docValues format pattern the replacement cites. It holds.
- SCH-R1-29 (SOLR-18134): applied with a disclosed deviation. The register's bullet replacement is its first two sentences ("The PathHierarchy test covers the field query parser path only. The filter test checks the filter alone."). The bullet's two schema-version sentences were retained after them (the test schema uses version 1.0, where the standard parser also builds phrases; shipped configsets use 1.7, and no test runs them), because dropping them would lose a specific recorded limit the register's summary does not state. The deviation and its reason are recorded here for round 2 to judge.

## Per-finding dispositions

### SOLR-9349

- SCH-R1-01 (should-fix) APPLIED: both bold leads are in (the Changes lead and the "What happens today" lead).
- SCH-R1-02 (should-fix) APPLIED: the copy-field citation now anchors at the head commit, and the summary line carries the rewritten wording.
- SCH-R1-03 (should-fix) APPLIED: the Proof paragraph now ends with the register's sentence about which checks ran, and the Limits carry the word-count bullet.
- SCH-R1-04 (note) STANDS as a record item.

### SOLR-14199

- SCH-R1-05 (should-fix) APPLIED: the Proof lead and the Choice now carry the register's forms (the Choice's first sentence is the "proceeds as if" wording; the Limits carry the version-1.5 bullet).
- SCH-R1-06 (should-fix) APPLIED: both citation fixes are in (the `PointField.checkSchemaField` link at lines 96-110 and the upgrade-note blob link at lines 30-35 of `major-changes-in-solr-11.adoc`).
- SCH-R1-07 (note) STANDS as the draft's partial hold: the [CONFIRM COUNT BEFORE POSTING] item (the receipt says TestSolrQueryParser 37 of 37 from fresh XML; the source declares 36 test methods) and the [HOLD: SchemaVersionSpecificBehaviorTest was not in this run] item both ride `gates/SOLR-14199-record.md`. The two owed Limits lines are named in `receipts/SOLR-14199.md` ("non-indexed explicit omitNorms=false not rejected and not distinguishable; no TEST_HACK exemption"); composing their public wording stays owner and record-run work, so the draft's [OWNER] marker is left untouched and the pointer is recorded here. The draft stays NOT CONVERGED (held in part).

### SOLR-15357

- SCH-R1-09 (blocking) APPLIED: the Proof lead and paragraph now state the proof is inconclusive on base by construction, in the register's form; the paragraph's last sentence ("None of the four shows a failure on base.") is deleted, as prescribed.
- SCH-R1-10 (blocking) APPLIED: the coverage bullet now reads in the register's form (PointType `loc` and BBoxField `box` copy target counts tested; real-time get and atomic update output for those types not; no glob or delete-refusal test).
- SCH-R1-11 (should-fix) APPLIED: the currency bullet now reads in the register's form (sub-fields written without docValues; a stored currency sub-field would no longer appear in real-time get output).
- SCH-R1-12 (should-fix) APPLIED: the same-method bullet is in the Behavior changes list (placement recorded in the fact checks).
- The draft's own INTERNAL hold block (test-name fix and rerun, changelog title, owner decision 2) predates this round and stands; when the fix commit lands, the head and the Proof links re-point. The draft is CONVERGED on text at the current head, with that held item named for round 2.

### SOLR-15358

- SCH-R1-14 (should-fix) APPLIED: the Choice bullet now carries the register's form (schema version 1.5 or earlier with docValues as stored on, and the base-commit anchor).
- SCH-R1-15 (should-fix) APPLIED: the landing-order bullet is in ("Land SOLR-15357 first. Its copy-target skip is what the atomic update path needs to keep these new sub-fields out. Without it, an atomic update can merge and store them."), placed before the last Limits bullet. The owner-confirmation condition is met by the record (see fact checks).
- SCH-R1-13, SCH-R1-16, SCH-R1-17 (notes) STAND: SCH-R1-16 confirms the Proof placeholder rides the posted record run (`gates/SOLR-15358-proof-record.md`); no text the run has not produced was forced into the Proof. The draft stays NOT CONVERGED on the placeholder alone.

### SOLR-15945

- SCH-R1-22 (should-fix) APPLIED: the Related bullet now states that SOLR-15403's duplicate status is not established.
- SCH-R1-30 (should-fix) HELD, branch-side. Recorded verbatim for the branch commit: the fragment title becomes "Stored-only DateRangeField and SpatialRecursivePrefixTreeFieldType fields (indexed=\"false\") no longer fail to load because of the norms and index-options checks."
- SCH-R1-37 (should-fix) HELD, branch-side. Recorded verbatim: the XML comment in `managed-schema.xml` becomes "<!-- stored-only: the norms and index-options checks do not apply to these fields, so the type skips them -->"

### SOLR-16977

- SCH-R1-23 (should-fix) APPLIED: the Limits now state that the document-vector half of the ticket is not addressed by this change.
- SCH-R1-24 (should-fix) APPLIED: the Summary bullet now carries the register's form.
- SCH-R1-31 (should-fix) HELD, branch-side. Recorded verbatim: the fragment title becomes "A KNN query with an all-zero vector against a cosine-similarity DenseVectorField now fails with a clear BAD_REQUEST error instead of an unclear HTTP 500 error."

### SOLR-17047

- SCH-R1-25 (should-fix) APPLIED: the Summary now reads in the register's form.
- SCH-R1-26 (should-fix) APPLIED: the second bullet now reads in the register's form.
- SCH-R1-27 (should-fix) APPLIED: the Limits now carry the register's bullet on the `SolrCore` pattern at its base-commit anchor.
- SCH-R1-32 (should-fix) HELD, branch-side. Recorded verbatim: the fragment title becomes "DenseVectorField KNN settings are validated at core load, including quantized fields under a codec factory that cannot apply them."
- SCH-R1-33 (should-fix) HELD, branch-side. Recorded verbatim: the javadoc at lines 270-274 becomes "Returns true if the algorithm, the HNSW parameters, or the cuVS parameters are set to a non-default value. SolrCore uses this for codec factories that are not SolrCoreAware. SchemaCodecFactory does not apply the cuVS parameters, and the cuVS codec factory does."

### SOLR-18134

- SCH-R1-21 (blocking) APPLIED: the Proof now reads "On base, one of its two tests fails." (see fact checks).
- SCH-R1-28 (should-fix) APPLIED: all three citation fixes are in (the harvest query-parser link at its proper anchor, and the two base-commit anchors labelled as such).
- SCH-R1-29 (should-fix) APPLIED with the disclosed deviation recorded in the fact checks.
- SCH-R1-34 (should-fix) HELD, branch-side. Recorded verbatim: the fragment title becomes "Add solr.ZeroPositionIncrementFilterFactory so ancestor_path phrase queries match ancestor paths after Lucene 10 made PathHierarchyTokenizer emit sequential tokens."
- SCH-R1-35 (should-fix) HELD, branch-side. Recorded verbatim: the upgrade note line becomes "The field query parser, and quoted text, build a phrase from those query tokens. The standard parser does not build a phrase for unquoted text on schema version 1.4 and later." and "On the phrase path (the field query parser and quoted text), that configuration matches descendant paths, not ancestors." The `filters.adoc` line becomes "so a phrase over the prefixes, as the field query parser builds it, cannot match a path indexed as a single keyword."
- SCH-R1-36 (should-fix) HELD, branch-side. Recorded verbatim: the test comment becomes "    // A short path must not match longer descendants." and the blank line before it is deleted.
- SCH-R1-38 to SCH-R1-41 (notes) STAND as record items.

## Verdicts after application

- SOLR-9349: CONVERGED on text.
- SOLR-14199: NOT CONVERGED (held in part: the count confirmation and the SchemaVersionSpecificBehaviorTest run ride `gates/SOLR-14199-record.md`; the two owed Limits lines stay an owner item pointed at `receipts/SOLR-14199.md`).
- SOLR-15357: CONVERGED on text at the current head. Its internal hold block (branch fix and rerun, changelog title, owner decision 2) stands and is named for round 2 as a held item.
- SOLR-15358: NOT CONVERGED (the Proof placeholder rides `gates/SOLR-15358-proof-record.md`).
- SOLR-15945: CONVERGED on text. SCH-R1-30 and SCH-R1-37 are owed branch-side before opening.
- SOLR-16977: CONVERGED on text. SCH-R1-31 is owed branch-side before opening. The document-vector half of the ticket is not addressed and is now stated in Limits.
- SOLR-17047: CONVERGED on text. SCH-R1-32 and SCH-R1-33 are owed branch-side before opening.
- SOLR-18134: CONVERGED on text. SCH-R1-34, SCH-R1-35 and SCH-R1-36 are owed branch-side before opening.

## What round 2 must settle

Round 2 is posted as `assignments/pool-schema-convergence-round-2.md`, scoped to SOLR-14199 and SOLR-15358 (NOT CONVERGED), the SOLR-15357 held item (head state after its branch fix), and the branch-side items SCH-R1-30, 31, 32, 33, 34, 35, 36 and 37 (state checks: if a branch commit has landed, the fragment or file text is checked against the replacement recorded here; if not, the item is confirmed standing). Round 2 also judges the SCH-R1-29 deviation recorded above.
