# Run job: SOLR-13705 premise run (deterministic reflection spec)

- Branch: solr-13705-submit. Head: 5f141fb2af381801f58139d4f5739ff0354a244f (live tip, verified by ls-remote 2026-10-10).
- Intended host: vm2. Any capable Linux host may claim if vm2 is busy (WORKFLOW.md).
- Status: CLAIMED by vm2 (2026-10-11T01:51:14Z, UTC).
- Job type: premise run only. The branch has never had a full gate anywhere; the only run evidence is a single GitHub focused-test SUCCESS, which is not a gate (receipt). This job runs the premise leg only; the full gate is a separate, later job.
- Spec (preserved in the receipt, from the revoked batch spec): base 9b3a84b1c460. On the base with only the branch's test files copied in, testLazilyInitializedSingletonIsVolatile is expected to FAIL: it is a reflection assertion that the singleton field is volatile, and the failure is deterministic. The concurrency test in the same class passes with and without the fix; it is a sanity check, not part of the premise.
- Run shape: copy only the branch's test files onto the base 9b3a84b1c460 and run org.apache.solr.util.configuration.SSLConfigurationsFactoryTest in :solr:core (claimant verifies the exact package by grep). Record the outcome of testLazilyInitializedSingletonIsVolatile on base from the fresh JUnit XML. Then run the same class at the branch head, where the method must pass. Record both outcomes here.
- If the base run passes, the premise is dead as specified; record that plainly as the result. The ticket's complaint is theoretical per the reconciliation register, so a dead premise is a meaningful outcome, not a failed job.
- Deliverable: update receipts/SOLR-13705.md with the premise outcome, in the same push that marks this job DONE.
- On completion: mark this job DONE and the claim DONE.

## Step checklist

- [ ] base run (test files only on 9b3a84b1c460), volatile assertion outcome from fresh JUnit XML
- [ ] head run, same class
- [ ] receipt updated, job and claim marked DONE
