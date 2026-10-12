# Gate job: SOLR-15945 branch-side items SCH-R1-30 and SCH-R1-37, re-gate at the new head

- Branch: solr-15945-submit. Head: adb0fd450c06c07f49ccd187c3d810410a76c5b3 (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-15945.md). Merge-base with main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: changelog title correction and a test-schema comment correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/schema-convergence-round-1-response.md, items SCH-R1-30 and SCH-R1-37 (both held branch-side there, replacements recorded verbatim; register text in reports/schema-convergence-round-1.md lines 50 and 52).
- The fixes:
  1. SCH-R1-30: in `changelog/unreleased/SOLR-15945.yml`, the title now reads `DateRangeField and SpatialRecursivePrefixTreeFieldType fields declared with indexed="false" no longer fail to load because of the norms and index-options checks`. Replace the title with exactly: `Stored-only DateRangeField and SpatialRecursivePrefixTreeFieldType fields (indexed="false") no longer fail to load because of the norms and index-options checks.` Keep the fragment valid YAML (quote the scalar if the parser requires it, keeping the rendered title identical; the parse step below verifies).
  2. SCH-R1-37: in `solr/core/src/test-files/solr/collection1/conf/schema-nonindexed-spatial.xml`, the comment above the two stored-only fields now reads "<!-- stored-only: no norms or index options exist for these, so the type's invariants hold -->". (The register names this file loosely as the schema XML; the comment is in this test schema file at the head above.) Replace it with exactly: "<!-- stored-only: the norms and index-options checks do not apply to these fields, so the type skips them -->"
  No other file changes: verify with `git diff` against adb0fd450c06 that the delta is these two files only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production code and no test logic (a fragment title and an XML comment), so the gate evidence at adb0fd450c06 carries over the text-only commits (the SOLR-16630 precedent). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused class at the new head: org.apache.solr.schema.NonIndexedSpatialFieldTest (package read from the branch's test file; the receipt's count at the gated head is 1 of 1; the test loads the edited schema file, so it also exercises the comment's file).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; re-run tidy on the final tree after the last edit, before committing); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the commits push plainly (fast-forward) to solr-15945-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-15945.md with the new head and a line recording that the delta from adb0fd450c06 is the SCH-R1-30 title and the SCH-R1-37 comment only, with the focused count from fresh JUnit XML. Re-point the changelog link in pr-drafts/schema-analysis/SOLR-15945.md to the new head. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] fragment title replaced verbatim (SCH-R1-30) and schema comment replaced verbatim (SCH-R1-37); delta against adb0fd450c06 is those two files only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:core)
- [ ] focused tests at the new head: NonIndexedSpatialFieldTest, counted from fresh JUnit XML
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-15945-submit; receipt refreshed; draft changelog link re-pointed; job and claim DONE
