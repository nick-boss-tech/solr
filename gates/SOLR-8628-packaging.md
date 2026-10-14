# Gate job: SOLR-8628, packaging and top-up gate

- Branch: solr-8628-submit. Head: 5b606dc779d0f367d7c8ac7235988a65f092b6a4 (fork tip, verified by ls-remote 2026-10-14; matches receipts/SOLR-8628.md).
- Status: OPEN. Gate run finished with a nonzero rc at the proof leg (see Gate run record); not promoted, not relaunched, left for the main agent. Posted 2026-10-14 by the Windows review coordinator from the census follow-up core admin round 1 (assignments/pool-census-followup-core-admin-round-1.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Capability tags: gate.
- Why: receipts/SOLR-8628.md says the root SOLR-8628-TESTING.md comes off as packaging after the green gate. The file is still on the live tip. Opening the pull request needs it removed, which moves the head, so the packaged head needs its own gate, as the SOLR-5262 and SOLR-8554 gates did.
- Change: delete SOLR-8628-TESTING.md in one packaging commit. Nothing else changes.
- Steps, in order:
  1. Packaging commit that removes SOLR-8628-TESTING.md only. Push as a fast-forward from 5b606dc779d0. Verify with ls-remote and record the packaged head here.
  2. Changelog YAML parse (changelog/unreleased/SOLR-8628-index-dir-with-only-write-lock.yml).
  3. Tidy, then confirm the tree is clean.
  4. Error Prone compile of :solr:core:compileTestJava.
  5. Proof leg: solr/core/src/java/org/apache/solr/core/SolrCore.java reverted to the base commit cabedd1d968059215188f4e7563fb303241899ed, branch tests kept. Expected TestCoreContainer 26 tests, 3 skipped, 1 failure (testCreateCoreOverIndexDirWithOnlyWriteLock, "no segments* file found"). Restore the head tree and verify the hash.
  6. Focused tests: org.apache.solr.core.TestCoreContainer at the packaged head. Expected 26 tests, 3 skipped, 0 failures, counted from fresh JUnit XML after cleanTest.
  7. Module check :solr:core:check -x test.
- On green: the gating host refreshes receipts/SOLR-8628.md at the packaged head, marks this job DONE, and records the old head (5b606dc779d0) and the packaged head.
- Draft effect: the head citations in pr-drafts/core-admin/SOLR-8628.md move to the packaged head in the same pass. The draft text does not cite the root testing file and is otherwise unchanged.

## Gate run record

- Run: GATE START 2026-10-14T22:24:18Z, GATE END 2026-10-14T22:48:07Z, head 2e56ce9990b008c00d2d07b2498cf28cdc101a06 (the packaging commit in gates/solr-8628-packaging; `git diff --stat 5b606dc779d0 HEAD` shows only SOLR-8628-TESTING.md, 41 deletions). Log: logs/vm2/queue-g8628-packaging-gate-20261014T222418Z.out.
- rc lines: changelog 0, tidy 0, tidy-clean 0 (tree clean), compile 0, proof 1, head 0, module check 0.
- The one nonzero rc is the proof leg. At the base commit cabedd1d the focused class ran 26 tests, 3 skipped, 1 failure, and the failure is testCreateCoreOverIndexDirWithOnlyWriteLock, which matches the expected proof outcome in step 5 (logs/vm2/8628-proof.log). The proof file was restored and the tree was clean afterward (proof-restore:[]). The run was not promoted to GREEN, because the queue rule requires every rc to be 0, and it was not relaunched. The main agent classifies whether this proof rc 1 is the intended result.
- Focused head leg (logs/vm2/8628-head.log): BUILD SUCCESSFUL. Fresh JUnit XML after cleanTest, TestCoreContainer: tests 26, skipped 3, failures 0, errors 0. Module check (logs/vm2/8628-check.log): BUILD SUCCESSFUL.
- Fork check: origin refs/heads/solr-8628-submit is still 5b606dc779d0 (ls-remote). Nothing pushed, no receipt refresh, no DONE.

## Step checklist

- [ ] packaging commit pushed (fast-forward from 5b606dc779d0), packaged head recorded
- [ ] changelog YAML parse
- [ ] tidy, clean tree afterwards
- [ ] Error Prone compile
- [ ] proof leg at the base commit (26 tests, 3 skipped, 1 failure)
- [ ] focused TestCoreContainer at the packaged head (26 tests, 3 skipped, 0 failures, fresh XML)
- [ ] module check :solr:core:check -x test
- [ ] receipt refreshed at the packaged head; job and claim marked DONE
