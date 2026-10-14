# Gate job: SOLR-15712 version-gated useDocValuesAsStored refusal (ICU rule on the core CollationField), with the changelog title to match

- Branch: solr-15712-submit. Head under gate: the live fork tip `cb988a2ff4c3e890e3a9c258019d22d1f888aebd` (verified by ls-remote at posting, 2026-10-14). Merge-base with upstream main: `cabedd1d968059215188f4e7563fb303241899ed`. The gated head in `receipts/SOLR-15712.md` is the same commit, so the receipt stands until this job lands.
- Intended host: a Linux host with the `implementation` and `gate` tags (WORKFLOW.md). Windows never claims build or implementation work. The Windows review coordinator posts this job and does not claim it.
- Status: OPEN, unclaimed. Posted by the Windows review coordinator under the census follow-up round 5 claim (`claims/pool-census-followup-round-5-scheduled.md`, slice S4).
- Gate failure, 2026-10-14T13:37Z: runner gates/g15712-gate.sh (log logs/vm2/queue-g15712-gate-20261014T131618Z.out), gate head 2d65ed6329 (implementation commit, local to the gate worktree, not pushed). Focused-head run rc=1: TestCollationFieldUDVAS 5 tests, 4 failed (testNoUdvasNoWarning, testFieldUdvasFailsFrom90, testTypeUdvasWarnsBelow90, testFieldUdvasWarnsBelow90). Each fails with "Can't load schema ... Plugin Initializing failure for [schema.xml] fieldType" where the test expects FAIL or WARN and gets OK. The fail-before step ended rc=1 as expected (GATE END 2026-10-14T13:36:44Z). Not relaunched; left for the main agent.
- Capability tags: implementation, gate.
- Question: the branch refuses `useDocValuesAsStored` on a `CollationField` at every schema version (`CollationField.java` lines 91 to 105 at the head). The ICU collation field, in the same repository, has warned below Lucene 9.0 and failed from 9.0 since SOLR-15777 (`solr/modules/analysis-extras/src/java/org/apache/solr/schema/ICUCollationField.java` lines 97 and 126 at the merge-base). An existing schema that sets the flag on a core collation field stops loading under the current branch.
- Decision (decision default): the recorded recommendation in `reports/schema-analysis-round-1-s3.md` (finding F4, "Recommended: the ICU gate") is adopted. This job file is the record of the adoption; the round 5 report carries it as well.
- Steps:
  1. Changelog YAML parse for `changelog/unreleased/SOLR-15712-collationfield-no-docvalues-as-stored.yml` (at the new head).
  2. Implementation, one new commit on top of the head, touching only `solr/core/src/java/org/apache/solr/schema/CollationField.java`, one new test class, and the changelog title:
     - Add `import org.apache.lucene.util.Version;`.
     - Add `static final Version UDVAS_FORBIDDEN_AS_OF = Version.LUCENE_9_0_0;` and a `private boolean failHardOnUdvas;` next to `UDVAS_MESSAGE`.
     - In `checkSchemaField`, call `assertWarnOrFail(UDVAS_MESSAGE, false, failHardOnUdvas);`.
     - In `init`, first set `failHardOnUdvas = schema.getDefaultLuceneMatchVersion().onOrAfter(UDVAS_FORBIDDEN_AS_OF);`, and make the init check use `failHardOnUdvas` the same way.
     - Add a test class in the style of `solr/modules/analysis-extras/src/test/org/apache/solr/schema/TestICUCollationFieldUDVAS.java`: warn (no exception) below 9.0 and refusal at 9.0, with the flag set on a field and on a field type.
     - Changelog title, warn-gated wording: `CollationField no longer defaults useDocValuesAsStored, so fl=* does not decode binary collation keys as UTF-8 text. Setting useDocValuesAsStored=true on a CollationField warns below luceneMatchVersion 9.0 and fails from 9.0 on.`
  3. Tidy: `:solr:core:tidy` leaves 0 changed files afterward. Any tree change after tidy marks the run GATE INVALID.
  4. Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
  5. Focused run at the new head: `:solr:core:test --tests org.apache.solr.schema.TestCollationFieldDocValues --tests org.apache.solr.schema.TestCollationField --tests <new test class>`. Count each class from fresh JUnit XML; all must pass.
  6. Pre-fix proof for the new test: with `CollationField.java` reverted to the merge-base version, keep the new test and run it. Expected: it fails by content (on the base code no refusal exists, so the "refused at 9.0" assertion fails). Restore the file afterward and verify the tree matches the new head.
  7. Module check: `:solr:core:check -x test`.

## On green

- Push the new commit plainly to fork `solr-15712-submit` (fast-forward from `cb988a2ff4c`).
- Refresh `receipts/SOLR-15712.md` at the new head, with the counts, the tidy and compile results, and the pre-fix verdict.
- Mark this job DONE in the same push sequence.
- The draft `pr-drafts/schema-analysis/SOLR-15712.md` is then rewritten: its "What this change does" and "A choice to check" sections describe the version rule, and its Proof uses the counts at the new head. Until then the draft describes the head that the receipt records.

## On failure

- A test that executed and failed is not relaunched by another host. Record the failing step, test and seed in the gate file and report to the main agent.

## Limits for this gate

- The change does not cover the backup path named in SOLR-15712, nor explicit `fl` requests for collation fields.
- The duplicate question (whether SOLR-15712 should close against SOLR-15777) is not decided by this job. The default is to keep it as a separate core change, with SOLR-15777 named as the precedent.

## Gate failure investigation (main side, 2026-10-14)

Classification: (b). The defect is in the new test class's schema, not in the
production implementation and not in the vm2 environment. The fix is a corrected
test class, filed as `gates/SOLR-15712-udvas-test-fix.md` for the patch loop. This
job stays FAILED at `2d65ed6329` until the corrected test is dropped into the gate
worktree and steps 5 to 7 of this job are re-run there. Do not relaunch the failed
run as it stands.

What the pattern shows:

- Four of the five new tests fail with the same text: "Can't load schema ...
  Plugin Initializing failure for [schema.xml] fieldType". That wrapper is
  `AbstractPluginLoader` (line 192 at the merge-base `cabedd1d968`), thrown when a
  field type's `init` throws. So every failing test died during field type
  initialization, before any field-level check ran.
- The failing four include `testNoUdvasNoWarning`, the control with the flag set
  nowhere. A defect in the version rule cannot fail the control: with no flag,
  neither the type-level check in `init` nor `checkSchemaField` fires. The load
  itself is broken in the test's schema.
- The one passing test, `testTypeUdvasFailsFrom90`, is the one case where the new
  code acts before the break point: in `CollationField.init` the type-level
  `useDocValuesAsStored` check runs before `setup(...)` (same order as the branch
  head `cb988a2ff4c` and as `ICUCollationField`), so at 9.0 it throws the refusal
  and the load fails as the test expects. Every other variant reaches `setup(...)`
  and dies there.
- Core `CollationField.setup` (merge-base) requires a `language` or `custom`
  argument and throws "Either custom or language is required." otherwise. The ICU
  field it was modeled on takes `locale` instead. A generated schema carrying the
  ICU-style attributes, or no collator argument at all, fails core field type init
  in exactly this shape. The gate job limited the implementation commit to the
  production file, one test class and the changelog title, so the new test had to
  generate its schema in code; that generated schema does not meet the core
  field's init contract.
- The production side is cleared by the code: the prescribed implementation is
  the ICU one verbatim, and `IndexSchema.getDefaultLuceneMatchVersion()` returns
  a constructor-injected value that is `Objects.requireNonNull` (IndexSchema
  line 202 at the merge-base), set before field types load, so it cannot fail at
  init. The same focused run's two pre-existing classes, which load valid
  collation schemas (`language=` attributes in `schema-collate-dv.xml`), have no
  recorded failures.
- Caveat, recorded plainly: the implementation commit `2d65ed6329` and the test
  source are local to the vm2 gate worktree and are not pushed, so the exact
  schema defect is identified by elimination from the failure pattern and the
  code, not read from the test source. The re-gate with the corrected test
  confirms this classification; if a valid schema still fails at init, the
  classification reopens as (a) against the implementation.
- The draft banner's clause "where the test expected a failure or warning and
  got OK" does not match the exception text it quotes and is treated as a
  recording error. The quoted exception text and the test names are the reliable
  parts of the record.
