# Gate job: SOLR-9091 changelog title correction, re-gate at the new head

- Branch: solr-9091-submit. Head: e6bfa5c626812f1452ed8c0158697ab8653a40d2 (live tip, verified by ls-remote at posting, 2026-10-11; this is the packaged head, GATE GREEN per receipts/SOLR-9091.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE by vm2 (gate GREEN at a596a39d2e1eb3dcc9d2360c2b13c81925dfbfcc, gate 2026-10-13T05:19:08Z to 05:37:18Z; pushed as a fast-forward from e6bfa5c626; receipt receipts/SOLR-9091.md refreshed; draft changelog link re-pointed in pr-drafts/replication-backup/SOLR-9091.md).
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/replication-backup-convergence-round-1-response.md, item RB-R1-04 (verified there against the head code: `verifyRestoredFile` runs inside the download copy in RestoreCore.java and the throw comes before the switch, so no rollback is involved in the failure the title describes).
- The fix: in `changelog/unreleased/SOLR-9091-restore-verify-backup-checksum.yml` at the head above, the title says a corrupt backup file "fails the restore (and rolls back) instead of being switched in silently". Replace the title with exactly: "Replication-handler restore now verifies every file copied from the backup against the checksum in its footer, so a corrupt backup file fails the restore before it is switched in." Keep the fragment's YAML shape (the block scalar form is fine); the parse step below verifies it. No other file changes: verify with `git diff` against e6bfa5c62681 that the delta is this one fragment only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production or test code, so the gate evidence at e6bfa5c62681 carries over the title-only commit (the SOLR-16630 precedent: re-gate green at the new head, delta changelog-only). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused class at the new head: org.apache.solr.handler.TestRestoreCore (package read from the branch's test file by grep, per the FQCN rule; the receipt's count at the gated head is 4 tests).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; any tidy change to another file invalidates the delta check above); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to solr-9091-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-9091.md with the new head and a line recording that the delta from e6bfa5c62681 is the RB-R1-04 title correction only, with the focused counts from fresh JUnit XML. Re-point the changelog link in pr-drafts/replication-backup/SOLR-9091.md to the new head. Mark this job DONE and the claim DONE.
- Landing order note: the RestoreCore cluster order (9865, then 17287, then 9091) applies at opening time and does not block this job.

## Step checklist

- [ ] title replaced verbatim in SOLR-9091-restore-verify-backup-checksum.yml; delta against e6bfa5c62681 is that fragment only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:core)
- [ ] focused tests at the new head: TestRestoreCore, counted from fresh JUnit XML
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-9091-submit; receipt refreshed; draft changelog link re-pointed; job and claim DONE
