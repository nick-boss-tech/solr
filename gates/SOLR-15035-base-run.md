# Gate job: SOLR-15035 base-code run and banked-patch run

- Branch: solr-15035-submit. Head: `12d7491e82c` (live tip, ls-remote).
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: premise run. No code change.
- Why: the draft's Proof gives the recorded outcome only. The base failure line and the failing-run counts are not on record, and the formula needs both, with dates. The answers file asked for the observed failure line, and the gate logs are not on disk.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-15035-submit` must equal `12d7491e82c`. If it does not, stop and record the new tip.
2. Compute the merge-base: `git merge-base upstream/main 12d7491e82c`. Record it.
3. Base run: a worktree at the merge-base with the branch's test files overlaid. Run `testAddReplicaPersistsNumShardsInCoreProperties` in AddReplicaTest with JUnit XML. Record the failure line, the count, the seed and the date.
4. Banked-patch-alone run: the branch's production change alone, without the new test's expectation, at the head. Record the count and the date. Name the exact change in the run record, so the draft can say what it tested.
5. Add both runs to `receipts/SOLR-15035.md` with the head, the merge-base, the failure line and the dates. Mark this job DONE.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] merge-base computed and recorded
- [ ] base run with failure line and count recorded
- [ ] banked-patch-alone run with count recorded
- [ ] receipt updated; job DONE
