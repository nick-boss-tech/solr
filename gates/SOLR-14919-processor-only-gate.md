# Gate job: SOLR-14919 processor-only route, re-gate at the new head

- Branch: solr-14919-submit. Head: c1fa37e33c1e00ab06ed59646b6a739facf50017 (fork tip at posting, 2026-10-12). Prior gated head: 84e7bcaeb57d88e1d9d519389d96ff14a66acb6e (receipts/SOLR-14919.md). Merge-base: b5c71bc5573c4e31b4cee5a7965d73587fc0ae58.
- Posted by: windows (review agent), for Nick Shanin, 2026-10-12. Claim of the round: `claims/pool-solrcloud-readiness-round-1-application.md`.
- Change since the prior gated head: the recovery marker is removed (RecoveryStrategy.java back to the merge-base), the test comment is corrected, the processor javadoc is corrected, and the changelog title is replaced. The processor code is unchanged.
- Capability tags: gate. Intended host: any capable Linux host under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: RE-CLAIMED by vm2 at 8f1f4ef91e (fix commit on solr-14919-submit, fast-forward from c1fa37e33c). Gate run 1 at c1fa37e33c: GATE END 2026-10-13T02:05:17Z, logs/vm2/queue-g14919-gate-20261013T014330Z.out. Changelog parse, Error Prone compile, proof leg (exit 1, expected nonzero, 2 marker hits), and focused head (IgnoreCommitOptimizeUpdateProcessorFactoryTest, BUILD SUCCESSFUL) passed.
- Failure record (run 1): module check `:solr:core:check -x test` returned rc=1 from spotlessJavaCheck. Failing file: solr/core/src/java/org/apache/solr/update/processor/IgnoreCommitOptimizeUpdateProcessorFactory.java, the COMMIT_END_POINT javadoc (lines 117-120). Classification: tidy defect in production javadoc wrapping; no test failed, no environment cause. Fix: the spotless-expected rewrap of those lines (moves "or" onto the first line), committed as "SOLR-14919: tidy the processor javadoc wrap" and pushed as a fast-forward. The test and proof behaviour are unchanged by the fix. Log: logs/vm2/14919-check.log.
- Re-gate: queued behind SOLR-15863 at 8f1f4ef91e (runner gates/g14919-gate.sh) on vm2's serial queue.
- Module: :solr:core. Focused class: org.apache.solr.update.processor.IgnoreCommitOptimizeUpdateProcessorFactoryTest (confirm the package by grep before composing the run).
- Proof shape: with IgnoreCommitOptimizeUpdateProcessorFactory.java at the merge-base, the focused class must fail with "invalid boolean value: leaders". At the head it must pass.
- Steps: changelog YAML parse of changelog/unreleased/SOLR-14919.yml; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward); Error Prone compile; proof leg; focused test at the head (fresh JUnit XML, record the seed); module check `:solr:core:check -x test`.
- On green: refresh receipts/SOLR-14919.md with the new head and the dated run, update the draft's head citations in pr-drafts/solrcloud/SOLR-14919.md (they already name c1fa37e33c1), mark this job DONE and the claim line for SOLR-14919 DONE.

## Step checklist

- [ ] changelog parse
- [ ] tidy with the GATE INVALID guard
- [ ] Error Prone compile
- [ ] proof leg at the merge-base processor
- [ ] focused test at the head
- [ ] module check
- [ ] receipt refreshed; draft citations confirmed; job DONE

- Run 2 at 8f1f4ef91e: GATE START 2026-10-13T02:41:55Z, interrupted at the focused-head leg with no GATE END (logs/vm2/queue-g14919-gate-20261013T024155Z.out). Not relaunched; main agent decides the re-run.

## Main-side re-run decision (2026-10-13)

Run 2 at 8f1f4ef91e was interrupted during the focused-head leg: changelog parse, tidy, Error Prone compile, the proof leg and the proof-restore all passed, and the log ends mid-leg with no GATE END and no test failure. Decision: the interruption is an environment stop, not a gate failure. VM2 re-runs the gate at 8f1f4ef91e. Because the earlier legs' results are recorded in run 2's log, the re-run may cover only the remaining legs: the focused-head test counted from fresh JUnit XML, then the module check. A full re-run is also acceptable if that is simpler on the host. The job stays CLAIMED by vm2; on green, the on-green steps above (receipt refresh, draft citations, job DONE) proceed as written.
