# Gate job: SOLR-10882 changelog title correction, re-gate at the new head

- Branch: solr-10882-submit. Head: 5d7d07a5b17bfbf9370fe8562ce7b5d9909118ee (live tip, verified by ls-remote at posting, 2026-10-11; this is the packaged head, GATE GREEN per receipts/SOLR-10882.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization. The earlier job gates/SOLR-10882.md (premise run and first gate) is DONE and does not cover this correction.
- Spec: reports/streaming-convergence-round-1-response.md, item ST-R1-08 (verified there against receipts/SOLR-10882.md: no null-value test was gated, so the title's null clause is unproven).
- The fix: in `changelog/unreleased/SOLR-10882-array-sort-mixed-types.yml` at the head above, the title (block scalar form) still ends with the clause "and no longer hits a NullPointerException when reporting a null value." Replace the title with exactly: "The array stream evaluator with sort=asc|desc now fails with a clear error when the values mix incomparable types (for example text and numbers) instead of a ClassCastException." Keep the fragment's YAML shape (the block scalar form is fine); the parse step below verifies it. No other file changes: verify with `git diff` against 5d7d07a5b17b that the delta is this one fragment only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production or test code, so the gate evidence at 5d7d07a5b17b carries over the title-only commit (the SOLR-16630 precedent: re-gate green at the new head, delta changelog-only). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:solrj-streaming. Focused class at the new head: org.apache.solr.client.solrj.io.stream.eval.ArrayEvaluatorTest (package read from the branch's test file by grep, per the FQCN rule; the receipt's count at the gated head is 7 tests, 0 failures).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; any tidy change to another file invalidates the delta check above); Error Prone compile for :solr:solrj-streaming with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:solrj-streaming:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to solr-10882-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-10882.md with the new head and a line recording that the delta from 5d7d07a5b17b is the ST-R1-08 title correction only, with the focused counts from fresh JUnit XML. In the same refresh, apply the ST-R1-11 correction the response record schedules for the receipt's next refresh: the receipt labels e044bf20b405 as the merge-base; that commit is the proof base, and the merge-base with upstream/main is cabedd1d968. Re-point the changelog link in pr-drafts/streaming/SOLR-10882.md to the new head. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] title replaced verbatim in SOLR-10882-array-sort-mixed-types.yml; delta against 5d7d07a5b17b is that fragment only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:solrj-streaming)
- [ ] focused tests at the new head: ArrayEvaluatorTest, counted from fresh JUnit XML
- [ ] module check (:solr:solrj-streaming)
- [ ] pushed plainly to solr-10882-submit; receipt refreshed (new head, delta line, ST-R1-11 merge-base label correction); draft changelog link re-pointed; job and claim DONE
