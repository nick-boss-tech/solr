# Gate job: SOLR-15035 base-code run and head-production run

- Branch: solr-15035-submit. Head: `12d7491e82c` (live tip, ls-remote).
- Status: DONE 2026-10-13T08:05Z. Runs 06:07:52Z to 06:19:20Z; recorded in receipts/SOLR-15035.md.
  - Merge-base: `b5c71bc5573c4e31b4cee5a7965d73587fc0ae58`. Head: `12d7491e82c`.
  - Base run (rc=1, the premise run's expected failure): `AddReplicaTest.testAddReplicaPersistsNumShardsInCoreProperties` failed with `java.lang.AssertionError: expected:<2> but was:<null>`. Count tests=1 failures=1 errors=0. XML timestamp 2026-10-13T06:13:52Z. Log: logs/vm2/15035-base.log. The seed is not in the JUnit XML.
  - Head-production run on base tests (rc=0): test file at the merge-base; production files `AddReplicaCmd.java` and `CoreAdminHandler.java` at the head (the full production change). Count tests=3 failures=0 errors=0. XML timestamp 2026-10-13T06:18:58Z. Log: logs/vm2/15035-banked.log (name predates this label).
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: premise run. No code change.
- Why: the draft's Proof gives the recorded outcome only. The base failure line and the failing-run counts are not on record, and the formula needs both, with dates. The answers file asked for the observed failure line, and the gate logs are not on disk.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-15035-submit` must equal `12d7491e82c`. If it does not, stop and record the new tip.
2. Compute the merge-base: `git merge-base upstream/main 12d7491e82c`. Record it.
3. Base run: a worktree at the merge-base with the branch's test files overlaid. Run `testAddReplicaPersistsNumShardsInCoreProperties` in AddReplicaTest with JUnit XML. Record the failure line, the count, the seed and the date.
4. Head-production run on base tests: the branch's production change at the head, with the merge-base test file (no new test expectation). Record the count and the date. Name the exact change in the run record, so the draft can say what it tested.
5. Add both runs to `receipts/SOLR-15035.md` with the head, the merge-base, the failure line and the dates. Mark this job DONE.

## Checklist

- [x] head confirmed by ls-remote
- [x] merge-base computed and recorded
- [x] base run with failure line and count recorded
- [x] head-production run on base tests with count recorded
- [x] receipt updated; job DONE
