# Gate job: SOLR-12916 changelog title correction, re-gate at the new head

- Branch: solr-12916-submit. Head: 50b1f4bcab039f8d25a729a8d02a1e8a16defd3e (live tip, verified by ls-remote at posting, 2026-10-11; this is the packaged head, GATE GREEN per receipts/SOLR-12916.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/core-admin-convergence-round-1-response.md, item CA-R1-34 (verified there: the branch accepts the flat form from the Config API and from XML, and the current title names only the Config API).
- The fix: in `changelog/unreleased/SOLR-12916-query-sender-flat-queries.yml` at the head above, the title reads "QuerySenderListener accepts warming queries given to the Config API as flat name/value lists, e.g. "queries": [ ["q", "*:*", "rows", 1] ], instead of ignoring them." Replace the title with exactly: "QuerySenderListener accepts warming queries given as flat name and value lists, from the Config API and from XML, instead of ignoring them." Keep the fragment's YAML shape; the parse step below verifies it. No other file changes: verify with `git diff` against 50b1f4bcab03 that the delta is this one fragment only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production or test code, so the gate evidence at 50b1f4bcab03 carries over the title-only commit (the SOLR-16630 precedent: re-gate green at the new head, delta changelog-only). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused class at the new head: QuerySenderListenerTest (package read from the branch's test file by grep, per the FQCN rule; it is the class the original gate's premise and head runs used).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; any tidy change to another file invalidates the delta check above); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to solr-12916-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-12916.md with the new head and a line recording that the delta from 50b1f4bcab03 is the CA-R1-34 title correction only, with the focused counts from fresh JUnit XML. Re-point the changelog link in pr-drafts/core-admin/SOLR-12916.md to the new head (CA-R1-34 prescribes the link move in the same pass). Mark this job DONE and the claim DONE.

## Step checklist

- [ ] title replaced verbatim in SOLR-12916-query-sender-flat-queries.yml; delta against 50b1f4bcab03 is that fragment only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:core)
- [ ] focused tests at the new head: QuerySenderListenerTest, counted from fresh JUnit XML
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-12916-submit; receipt refreshed; draft changelog link re-pointed; job and claim DONE
