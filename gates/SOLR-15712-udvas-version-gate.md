# Gate job: SOLR-15712 version-gated useDocValuesAsStored refusal (ICU rule on the core CollationField), with the changelog title to match

- Branch: solr-15712-submit. Head under gate: the live fork tip `cb988a2ff4c3e890e3a9c258019d22d1f888aebd` (verified by ls-remote at posting, 2026-10-14). Merge-base with upstream main: `cabedd1d968059215188f4e7563fb303241899ed`. The gated head in `receipts/SOLR-15712.md` is the same commit, so the receipt stands until this job lands.
- Intended host: a Linux host with the `implementation` and `gate` tags (WORKFLOW.md). Windows never claims build or implementation work. The Windows review coordinator posts this job and does not claim it.
- Status: OPEN, unclaimed. Posted by the Windows review coordinator under the census follow-up round 5 claim (`claims/pool-census-followup-round-5-scheduled.md`, slice S4).
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
