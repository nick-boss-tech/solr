# Gate job: SOLR-17047 branch-side items SCH-R1-32 and SCH-R1-33, re-gate at the new head

- Branch: solr-17047-submit. Head: 1ba7e33bfe7b86c147f2cd22d6ba517aaa5cbb00 (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-17047.md). Merge-base with main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: CLAIMED by vm2 (2026-10-12T21:35Z, claims/pool-vm2-gate-backlog-17047.md).
- Capability tags: gate.
- Job type: changelog title correction and a javadoc correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/schema-convergence-round-1-response.md, items SCH-R1-32 and SCH-R1-33 (both held branch-side there, replacements recorded verbatim; register text in reports/schema-convergence-round-1.md lines 66 and 68).
- The fixes:
  1. SCH-R1-32: in `changelog/unreleased/SOLR-17047.yml`, replace the title (currently about 300 characters, naming the algorithm and HNSW cases and omitting the quantized change) with exactly: "DenseVectorField KNN settings are validated at core load, including quantized fields under a codec factory that cannot apply them."
  2. SCH-R1-33: in `solr/core/src/java/org/apache/solr/schema/DenseVectorField.java`, the javadoc on `hasNonDefaultKnnOptions()` (lines 269 to 275 at the head above) says other codec factories "silently ignore" the cuVS options. Replace the javadoc body lines 270 to 274 with exactly: "Returns true if the algorithm, the HNSW parameters, or the cuVS parameters are set to a non-default value. SolrCore uses this for codec factories that are not SolrCoreAware. SchemaCodecFactory does not apply the cuVS parameters, and the cuVS codec factory does." Anchor the edit on the method and its current text at claim time, not on the line numbers alone.
  No other file changes: verify with `git diff` against 1ba7e33bfe7b that the delta is these two files only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta is a fragment title and a javadoc comment; no behavior changes, so the gate evidence at 1ba7e33bfe7b carries over the text-only commits (the SOLR-16630 precedent). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused class at the new head: org.apache.solr.schema.BadIndexSchemaTest (the branch's own test class; package read from the branch's test file).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; re-run tidy on the final tree after the last edit, before committing; a javadoc reflow by tidy is acceptable inside the same file, anything wider invalidates the delta check); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the commits push plainly (fast-forward) to solr-17047-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-17047.md with the new head and a line recording that the delta from 1ba7e33bfe7b is the SCH-R1-32 title and the SCH-R1-33 javadoc only, with the focused count from fresh JUnit XML. Re-point the changelog link in pr-drafts/schema-analysis/SOLR-17047.md to the new head. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] fragment title replaced verbatim (SCH-R1-32) and DenseVectorField javadoc replaced verbatim (SCH-R1-33); delta against 1ba7e33bfe7b is those two files only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:core)
- [ ] focused tests at the new head: BadIndexSchemaTest, counted from fresh JUnit XML
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-17047-submit; receipt refreshed; draft changelog link re-pointed; job and claim DONE
