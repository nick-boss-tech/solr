# Run job: SOLR-10390 premise run (PATH without lsof)

- Branch: solr-10390-submit. Head: 4af4a6834e2b43251381027c24b18761fc5a9513 (live tip, verified by ls-remote 2026-10-10).
- Intended host: vm2. Any capable Linux host may claim if vm2 is busy (WORKFLOW.md).
- Status: CLAIMED by vm2 (2026-10-11T01:25:00Z, UTC).
- Job type: premise run (BATS). No gate, BATS run or premise run has been executed for this branch; premise unverified (receipt). The branch changes bin/solr so start detects the listening port without lsof, via a bash /dev/tcp fallback, adds a test to test_start_solr.bats, and adds a changelog fragment; it still carries SOLR-10390-TESTING.md at the tip, which this run does not remove.
- Run shape: the fair setup the receipt names: a PATH without lsof, so the fallback is the path exercised. Run the branch's added test in solr/packaging/test/test_start_solr.bats (claimant locates the added test by name from the branch diff) twice under that PATH: once against the base bin/solr, where port detection without lsof is the defect and the start path the test covers must fail; once at the branch head, where the /dev/tcp fallback must carry it. Record both outcomes and the failure text here.
- If lsof cannot be removed from the PATH cleanly on the claiming host, or the base run passes, record exactly that as the result; do not substitute a lsof-present run and call it the premise.
- Deliverable: update receipts/SOLR-10390.md with the premise outcome, in the same push that marks this job DONE.
- On completion: mark this job DONE and the claim DONE.

## Step checklist

- [ ] PATH without lsof established and recorded
- [ ] branch's added BATS test against base bin/solr under that PATH
- [ ] same test at the branch head under that PATH
- [ ] receipt updated, job and claim marked DONE
