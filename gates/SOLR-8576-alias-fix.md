# Gate job: SOLR-8576 alias check fix in the new test, re-run, re-gate

- Branch: solr-8576-submit. Head: 4c46f95c7851f2adbf3377d746aadc9c2249cdb4 (live tip, verified by ls-remote at posting, 2026-10-11; matches receipts/SOLR-8576.md, GATE GREEN at this head for the branch as it stands). Merge-base with main: b6b2b8f10e9827e3b86e44649fb7966ce646c185.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: test fix on a gated test-only branch, then a re-gate at the fixed head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: the [OWED BEFORE POSTING] marker in pr-drafts/core-admin/SOLR-8576.md, with reports/core-admin-convergence-round-1-response.md (CA-R1-21, CA-R1-24) and the register text in reports/core-admin-convergence-round-1.md. The green gate at the head above covers the test as it stands; the draft stays held until the fix and the run below land.
- The fix: in `solr/core/src/test/org/apache/solr/cloud/CollectionsAPISolrJTest.java`, `testCreateCollectionNameAlreadyTaken` (lines 1157 to 1189 at the head above), the closing checks assert that the original collection exists and that no collection has the alias name (`hasCollection(aliasName)` is false), but nothing checks that the alias itself is unchanged. Strengthen the test so it also asserts, after both rejected creates, that the alias still resolves to the original collection, using the alias lookup idiom already used in this test class. Test-only: no production change is part of this job. If the strengthened assertion fails at the head, stop and record it here; the premise would have changed, and production code is not fixed under this job.
- Proof shape: a pin, as receipts/SOLR-8576.md records. The behavior the test checks already exists on base, so the proof leg runs the single new test with production reverted to the merge-base `b6b2b8f10e98` and the fixed test tree kept: the expected result is a pass (1 test, 0 failures), confirming the strengthened test still pins existing behavior. A failure on base is not expected; if it happens, stop and record it here and in the receipt, because the draft's Proof section is written for a pin.
- Modules: :solr:core. Focused class at the fixed head: org.apache.solr.cloud.CollectionsAPISolrJTest (the full class, counted from fresh JUnit XML; the receipt's counts at the pre-fix head are 25 tests, 1 skipped, 0 failures, and the fix edits assertions inside one existing test, so the class count should not change). Seed: 8576C0FFEE8576, as in the receipt.
- Gate steps: changelog YAML parse (`changelog/unreleased/SOLR-8576-create-collection-exists-tests.yml`, unchanged by this job, parsed as part of the gate); tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; re-run tidy on the final tree after the last edit, before committing); Error Prone compile for :solr:core; proof leg as above; focused tests; module check `:solr:core:check -x test`.
- Push: the fix commit pushes plainly (fast-forward) to solr-8576-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-8576.md at the fixed head (the fix described, the new counts from fresh JUnit XML, the pin proof result). Update pr-drafts/core-admin/SOLR-8576.md as CA-R1-24 prescribes: delete the [OWED BEFORE POSTING] marker, update the head, the counts and the links to the fixed head, and apply CA-R1-21's post-fix variant, so the closing sentence of "What this change does" reads "After that, it checks that the original collection is unchanged and that the alias still resolves to it." Mark this job DONE and the claim DONE.

## Step checklist

- [ ] alias resolution assertion added to testCreateCollectionNameAlreadyTaken; test-only delta verified against 4c46f95c7851
- [ ] tidy on the final tree; tree clean
- [ ] changelog parse
- [ ] Error Prone compile (:solr:core)
- [ ] proof leg: the single new test on merge-base production passes (pin confirmed)
- [ ] focused tests at the fixed head: CollectionsAPISolrJTest
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-8576-submit; receipt refreshed; draft updated (marker deleted, head, counts and links at the fixed head, CA-R1-21 post-fix sentence applied); job and claim DONE
