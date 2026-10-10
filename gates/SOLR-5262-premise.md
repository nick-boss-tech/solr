# Run job: SOLR-5262 premise run

- Branch: solr-5262-submit. Head: ade8b80264ac126c7c4b932388aa6ebb9b200c0e (live tip, verified by ls-remote 2026-10-10).
- Intended host: vm2. Any capable Linux host may claim if vm2 is busy (WORKFLOW.md).
- Status: CLAIMED by vm2 (claimed 2026-10-10T23:02:19Z, UTC).
- Job type: premise run only. No gate is recorded for this branch and no pipeline record exists on the main side at all; this run is the first record the branch will have (Core admin round 1 answers, SOLR-5262 entry). The first gate is a separate, later job; do not run gate steps under this job.
- Spec (the three showings the answers require): (a) a scratch core outside the branch whose solrconfig.xml uses ${solr.core.ulogDir} and whose core.properties omits ulogDir, loaded on base and at the head; record the error text. Not committed; no branch edits under this backlog; (b) TestCoreDescriptorImplicitProperties.testUlogDirDefaultsToDataDir fails on base (the base value is null); (c) both pass with the change, at the branch head above. Claimant confirms the test class's exact package by grep before composing any run (FQCN rule).
- Method: for (a) and (b), run on base production with the branch's tests in place where a test is the vehicle; for (c), run the same checks at the branch head as it stands. Record each showing's outcome and evidence (failure text on base, pass at head) here, from fresh JUnit XML where a test run produces it.
- If any showing does not behave as the spec states, record exactly that; the premise conclusion follows the run, not the spec.
- Not part of this job: removing the root SOLR-5262-TESTING.md (that is packaging, at the later gate).
- Deliverable: update receipts/SOLR-5262.md with the premise outcome, in the same push that marks this job DONE.
- On completion: mark this job DONE and the claim DONE.

## Step checklist

- [ ] showing (a): base fails with the ${solr.core.ulogDir} config
- [ ] showing (b): testUlogDirDefaultsToDataDir fails on base
- [ ] showing (c): both pass at the branch head
- [ ] receipt updated, job and claim marked DONE
