# Run job: SOLR-10390 premise run (PATH without lsof)

- Branch: solr-10390-submit. Head: 4af4a6834e2b43251381027c24b18761fc5a9513 (live tip, verified by ls-remote 2026-10-10).
- Intended host: vm2. Any capable Linux host may claim if vm2 is busy (WORKFLOW.md).
- Status: DONE by vm2 (2026-10-11T01:49Z, UTC). Premise HOLDS on the added test's body: base fails at the lsof NOTE, head passes. The bats report marks the test not ok only from its teardown (solr stop --all timed out on this host); see Result. No gate run.
- Base used: cabedd1d96, the parent of the branch's first commit 3f4e497364 (the bin/solr change). The merge-base with origin/main (e044bf20b4) is older and is not the ticket's base.
- Job type: premise run (BATS). No gate, BATS run or premise run has been executed for this branch; premise unverified (receipt). The branch changes bin/solr so start detects the listening port without lsof, via a bash /dev/tcp fallback, adds a test to test_start_solr.bats, and adds a changelog fragment; it still carries SOLR-10390-TESTING.md at the tip, which this run does not remove.
- Run shape: the fair setup the receipt names: a PATH without lsof, so the fallback is the path exercised. Run the branch's added test in solr/packaging/test/test_start_solr.bats (claimant locates the added test by name from the branch diff) twice under that PATH: once against the base bin/solr, where port detection without lsof is the defect and the start path the test covers must fail; once at the branch head, where the /dev/tcp fallback must carry it. Record both outcomes and the failure text here.
- If lsof cannot be removed from the PATH cleanly on the claiming host, or the base run passes, record exactly that as the result; do not substitute a lsof-present run and call it the premise.
- Deliverable: update receipts/SOLR-10390.md with the premise outcome, in the same push that marks this job DONE.
- On completion: mark this job DONE and the claim DONE.

## Step checklist

- [x] PATH without lsof established and recorded
- [x] branch's added BATS test against base bin/solr under that PATH
- [x] same test at the branch head under that PATH
- [x] receipt updated, job and claim marked DONE

## Result

- PATH: lsof is not installed on vm2 (absent from /usr/bin, /usr/sbin, /bin, /usr/local/bin), so the run PATH has no lsof. The added test prepends its own stub lsof that exits 1, as the test is written.
- Distribution: built from the head worktree with -PdisableJsClient=true. The first attempt failed in the js-client npm step (ERR_MODULE_NOT_FOUND for @babel/plugin-syntax-dynamic-import in the generated babel workspace); the JS client only feeds the webapp war, not bin/solr or this test. Base dist is the head dist with solr/bin/solr replaced by cabedd1d96's copy.
- Base (cabedd1d96 bin/solr), log /workspace/gates/logs/vm2/10390-base.log: FAILED as predicted. test_start_solr.bats line 129, refute_output --partial 'Please install lsof' failed; output "NOTE: Please install lsof as this script needs it to determine if Solr is listening on port 45659." Solr still started.
- Head (4af4a6834e bin/solr), log /workspace/gates/logs/vm2/10390-head.log: test body PASSED. The refute held, "Started Solr server on port" was asserted, and solr assert --started passed. The report is "not ok" only from the teardown: `SOLR_STOP_WAIT=30 solr stop --all` returned 1.
- Teardown caveat: a manual start and stop reproduces the same timeout on both builds. Base stop took 51s and head 41s, each JVM force-killed after the 30s wait (rc=1, logs under /tmp/10390-diag). So the teardown result is host shutdown timing, not a branch difference. The base BATS run's teardown did not report, which is run-to-run variance. Not rerun, per the protocol for executed tests.
- Not established: the first gate (changelog parse, tidy, Error Prone compile, focused tests, module check) is owed. The branch still carries SOLR-10390-TESTING.md at the tip; this run did not remove it. Shutdown timing on vm2 is an open host question, not settled here.
- Runner: /workspace/gates/g10390-premise.sh. Receipt: receipts/SOLR-10390.md.
