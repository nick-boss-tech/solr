# Gate job: SOLR-4502 changelog title correction, re-gate at the new head

- Branch: solr-4502-submit. Head: 0ee4c644ee63c65cbda5752bb4e4e9fcdb70b4bf (live tip, verified by ls-remote at posting, 2026-10-11; this is the packaged head, GATE GREEN per receipts/SOLR-4502.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: CLAIMED by vm2 (coordinator) at 2026-10-12T10:04Z; claims/pool-vm2-gate-backlog-4502-title-fix.md. Fork tip pinned at claim time: 0ee4c644ee63.
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/core-admin-convergence-round-1-response.md, item CA-R1-01 (verified there against the premise run in receipts/SOLR-4502.md: the base failure is at create time, so the current title's search-time NullPointerException state is never reached on base).
- The fix: in `changelog/unreleased/SOLR-4502-create-before-load.yml` at the head above, the title reads "CoreContainer.create now fails with a clear message when the container has not been loaded, instead of creating a core that later hits a NullPointerException on search." Replace the title with exactly: "CoreContainer.create now fails with a message that names load() when the container has not been loaded, instead of a NullPointerException message." Keep the fragment's YAML shape (the block scalar form is fine); the parse step below verifies it. No other file changes: verify with `git diff` against 0ee4c644ee63 that the delta is this one fragment only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production or test code, so the gate evidence at 0ee4c644ee63 carries over the title-only commit (the SOLR-16630 precedent: re-gate green at the new head, delta changelog-only). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused class at the new head: org.apache.solr.core.TestCoreContainer (package read from the branch's test file by grep, per the FQCN rule; the receipt's count at the gated head is 26 run, 0 failed, 3 skipped).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; the title commit should leave tidy clean, and any tidy change to another file invalidates the delta check above); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to solr-4502-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-4502.md with the new head and a line recording that the delta from 0ee4c644ee63 is the CA-R1-01 title correction only, with the focused counts from fresh JUnit XML. Re-point the changelog link in pr-drafts/core-admin/SOLR-4502.md to the new head. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] title replaced verbatim in SOLR-4502-create-before-load.yml; delta against 0ee4c644ee63 is that fragment only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:core)
- [ ] focused tests at the new head: TestCoreContainer, counted from fresh JUnit XML
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-4502-submit; receipt refreshed; draft changelog link re-pointed; job and claim DONE
