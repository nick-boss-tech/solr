# Receipt: PR 5305 ignored-field sibling fixes gate, round 1

Verdict: GATE GREEN at final head F.

- Job: gates/pr-5305-ignored-fields-round-1.md.
- Candidate F: 219fee78bf7cae5039e50fe885108a1d4937ccd3; tree 0f979d6c070da09f889b829f51c915743df77239.
- Contributor parent O: 00292cd3729126194235e939afe38e351d850c70, verified as F's direct parent in both gate clones and against the live fork ref, which was read back equal to F before the run and again after it (2026-11-07T18:53Z). Public PR 5305 remains open at O, untouched by this gate.
- Host: phone agent VM (Linux). JDK 21 (vendored Temurin 21.0.12.1+1), Gradle 9.7.0 via solr-gradle.sh, builds serialized on the host build lock. Clones: solr-clones/5305-pos (positive legs and root check, a normal clone) and solr-clones/5305-fb (fail-before overlay), both detached at F with HEAD and tree recorded before building.
- Seed for every test leg: 18627C0DE. All counts below are from fresh JUnit XML copied out after each leg.
- Execution window: 2026-11-07T18:27:34Z (tidy preflight) to 18:52:42Z (root check rerun end).

## Formatting preflight

Root tidy at detached F (run-preflight.sh, 18:27:34Z to 18:29:36Z): rc 0, zero tracked changes, git diff --check rc 0. Repeated at the start of the positive chain (P0): tidy rc 0, zero tracked changes, diff-check rc 0. No repair commit was needed; the head never moved.

## Fail-before proof (O implementations, F tests retained)

Overlay in the 5305-fb clone: the seven implementations listed in the job restored from O, staged alone; synthetic overlay tree c8e5776b4d03c58e21ac5932ae4db97541d36512; HEAD remained F. Every restored file verified byte-identical to its O blob by SHA-256, and both modified test files verified byte-identical to F before and after the restore (PrimitiveFieldTypeTest.java bc7e82d31d7b, TestICUCollationField.java aa99f146f339).

1. `:solr:core:test --tests org.apache.solr.schema.PrimitiveFieldTypeTest -Ptests.seed=18627C0DE -Ptests.failfast=false -Ptests.maxfailures=100`, rc 1 as expected. 10 tests, 6 failures, 0 errors, 0 skipped. Each failure is a NullPointerException at java.util.List.of from the named class's createFields:
   - testIgnoredBoolField: BoolField.createFields (BoolField.java:207)
   - testIgnoredBinaryField: BinaryField.createFields (BinaryField.java:152)
   - testIgnoredSortableBinaryField: SortableBinaryField.createFields (SortableBinaryField.java:62)
   - testIgnoredSortableTextField: SortableTextField.createFields (SortableTextField.java:101)
   - testIgnoredCollationField: CollationField.createFields (CollationField.java:262)
   - testIgnoredTrieFields: TrieField.createFields (TrieField.java:691, first concrete case TrieIntField)
   Controls testIgnoredStrField, testDocValuesOnlyFields, testDefaultOmitNorms, testDateField pass.
2. `:solr:modules:analysis-extras:test --tests org.apache.solr.schema.TestICUCollationField` (same flags), rc 1 as expected. 9 tests, 1 failure, 0 errors, 0 skipped: only testIgnoredField fails, NullPointerException at java.util.List.of from ICUCollationField.createFields (ICUCollationField.java:335). testDocValuesOnlyField and the seven existing methods pass.
3. Control `:solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testIgnoredFields`, rc 0. 1 test, passes: the contributor's StrField repair at O already covers the reported indexing path. This control is not counted as a fail-before regression.

## Positive proof at exact clean F

- `:solr:core:test --tests org.apache.solr.schema.PrimitiveFieldTypeTest`: rc 0; 10 tests, 0 failures, 0 errors, 0 skipped. All seven ignored-field methods and both preservation/control methods executed.
- `:solr:modules:analysis-extras:test --tests org.apache.solr.schema.TestICUCollationField`: rc 0; 9 tests, 0 failures, 0 errors, 0 skipped.
- Neighboring core suites (one invocation): rc 0; 81 tests, 0 failures, 0 errors, 1 skipped. Per class: BasicFunctionalityTest 23 (testIgnoredFields executed and passing), TestTrie 8, DocValuesTest 8, TestCollationField 8, TestCollationFieldDocValues 8, TestUseDocValuesAsStored 11, TestSortableTextField 7, DocValuesMultiTest 6, BooleanFieldTest 1, TestBinaryField 1. The single skip is TestTrie.testTrieFacet_PrecisionStep, a pre-existing randomized-testing assumption at this seed ("Points+facets require docValues, but randomizer: points=true && DV=false"); it is unrelated to the ignored-field boundary and no required case was skipped.
- `:solr:modules:analysis-extras:test --tests org.apache.solr.schema.TestICUCollationFieldDocValues`: rc 0; 7 tests, 0 failures, 0 errors, 0 skipped.
- Positive total: 107 tests, 0 failures, 0 errors, 1 characterized skip.

## Root validation

`check -x test` at F in the normal clone. First attempt (18:39:41Z to 18:45:31Z) failed at `:solr:webapp:verifyUiWasm` only: the sandbox egress relay corrupted the solr-ui-wasm.zip download (checksum fe065385e8 received against the gradle.properties pin 654494303e4a). This is a known host artifact, not a branch result. The zip was re-fetched with curl, verified at SHA-256 654494303e4af3b8bfb26a83d250603f19033820295591d88db0597991ce8ad2 (the pinned value for UI commit 473a55ef64a9a1605e00eb6c1c386887d4cb7dac), and seeded into solr/webapp/build/ui-download/. Rerun (18:46:01Z to 18:52:42Z): rc 0, BUILD SUCCESSFUL, no added task exclusions.

Final state: HEAD and tree unchanged at F, tracked status clean, git diff --check rc 0, live fork ref still equal to F. The overlay was never pushed; the contributor's PR and fork were never written to.

## Limits

- The docValues assertions are preservation coverage pins; this gate does not claim each one is mutation-proven, and no synthetic mutation was run, per the job.
- The gate proves the named head on this host only. Upstream CI has validated O, not F.
- Six of the seven fail-before negatives exercise newly repaired production classes (five core classes plus ICUCollationField); the seventh, testIgnoredSortableBinaryField, exercises the SortableBinaryField test-framework fallback, which delegates to the repaired BinaryField.

## Updated PR description draft (not posted anywhere)

For the follow-up PR from branch pr-5305-solr-18627-ignored-fields, once PR 5305 lands:

Title: SOLR-18627: Fix ignored-field NPE for the remaining field types

Body sketch: Follow-up to #5305, which fixed StrField. The same List.of(null) failure remained in BinaryField, BoolField, CollationField, SortableTextField, TrieField and ICUCollationField, plus the SortableBinaryField test-framework fallback; an ignored field of any of these types failed the whole update with a NullPointerException. This change returns an empty field list for ignored fields of those types, matching the StrField behavior. Proof: with the pre-fix implementations restored under the new tests, the six core ignored-field methods and the ICU ignored-field method fail with NullPointerException at List.of in each class's createFields, and the contributor's BasicFunctionalityTest.testIgnoredFields control passes; at this head, PrimitiveFieldTypeTest (10 methods), TestICUCollationField (9 methods), the neighboring field suites (81 tests) and TestICUCollationFieldDocValues (7 tests) all pass, and root check passes. Full counts and the gate receipt: receipts/PR-5305-ignored-fields-round-1.md on the coordination board.
