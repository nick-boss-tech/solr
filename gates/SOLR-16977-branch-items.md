# Gate job: SOLR-16977 branch-side item SCH-R1-31 (changelog title), re-gate at the new head

- Branch: solr-16977-submit. Head: 1142f9563abe13aea113bd80d241db770fd6cdb6 (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-16977.md). Merge-base with main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE (vm2, 2026-10-12T22:04Z). Title commit c65d242718e7953e81016526372421d7a47a7db0 pushed fast-forward to solr-16977-submit; gate green; receipt refreshed in receipts/SOLR-16977.md. Pending from the job's On-green list: re-point the changelog link in pr-drafts/schema-analysis/SOLR-16977.md to the new head (not done in this run).
- Capability tags: gate.
- Job type: changelog title correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization. Scope note: this job is the title only; the ticket's document-vector half stays unaddressed, as the draft's Limits now state, and is not part of this job.
- Spec: reports/schema-convergence-round-1-response.md, item SCH-R1-31 (held branch-side there, replacement recorded verbatim; register text in reports/schema-convergence-round-1.md line 58: the current title's "internal error while rendering results" ending was not reproduced by anything in the record).
- The fix: in `changelog/unreleased/SOLR-16977.yml`, the title now reads "A KNN query with an all-zero vector against a cosine-similarity DenseVectorField now fails with a clear BAD_REQUEST error instead of an internal error while rendering results". Replace the title with exactly: "A KNN query with an all-zero vector against a cosine-similarity DenseVectorField now fails with a clear BAD_REQUEST error instead of an unclear HTTP 500 error." The fragment's description line stays as it is. No other file changes: verify with `git diff` against 1142f9563abe that the delta is this one fragment only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production or test code, so the gate evidence at 1142f9563abe carries over the title-only commit (the SOLR-16630 precedent). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused class at the new head: org.apache.solr.schema.DenseVectorFieldTest (package read from the branch's test file; the receipt's count at the gated head is 51 tests, 2 skipped, 0 failures, seed 16977C0FFEE16977).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to solr-16977-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-16977.md with the new head and a line recording that the delta from 1142f9563abe is the SCH-R1-31 title correction only, with the focused counts from fresh JUnit XML. Re-point the changelog link in pr-drafts/schema-analysis/SOLR-16977.md to the new head. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] fragment title replaced verbatim (SCH-R1-31); delta against 1142f9563abe is that fragment only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:core)
- [ ] focused tests at the new head: DenseVectorFieldTest, counted from fresh JUnit XML
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-16977-submit; receipt refreshed; draft changelog link re-pointed; job and claim DONE
