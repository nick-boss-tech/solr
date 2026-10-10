# Run job: TestRestoreCore recount for SOLR-9865 and SOLR-17287

- Branches and heads: solr-9865-submit at 4937608bb181efae104c0d6f0257f445af50bf52; solr-17287-submit at 6957daf82610d6f09c533297cceec173a46e82a7 (both live tips, verified by ls-remote 2026-10-10).
- Intended host: vm2. Any capable Linux host may claim if vm2 is busy (WORKFLOW.md).
- Status: UNCLAIMED.
- Job type: focused settling run (one run at each head). Not a gate: no tidy, compile, proof or module-check steps.
- Question to settle: both receipts record TestRestoreCore at 4 tests, and the total of 11 in the SOLR-9865 receipt depends on that count; the head files on both branches declare 3 @Test methods. The drafts for both tickets carry a [CONFIRM: count] placeholder that stays until this count is confirmed (Replication and backup round 1 answers, main-side work owed, item 1). One confirmation settles both.
- Run shape: at each head, run TestRestoreCore alone in :solr:core and count the tests from the fresh JUnit XML the run produces (claimant confirms the class's exact package by grep; it is the TestRestoreCore class both branches' gates ran). Record per head: the XML test count, the testcase names, and whether any name carries a parameter suffix or a repeated invocation that explains 4 from 3 declared methods.
- Deliverable: correct receipts/SOLR-9865.md and receipts/SOLR-17287.md to the confirmed count (or confirm 4, with the explanation), in the same push that marks this job DONE. If a run cannot be produced at a head, record that here instead of guessing.
- On completion: mark this job DONE and the claim DONE.

## Step checklist

- [ ] TestRestoreCore at the SOLR-9865 head, count from fresh JUnit XML
- [ ] TestRestoreCore at the SOLR-17287 head, count from fresh JUnit XML
- [ ] both receipts corrected or confirmed, job and claim marked DONE
