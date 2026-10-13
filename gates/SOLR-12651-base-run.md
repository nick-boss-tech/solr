# Gate job: SOLR-12651 base-code run at the live head

- Branch: solr-12651-submit. Head: `404f13b501c97d6c6b65743a27f54f4f38363afa` (live tip, ls-remote).
- Status: OPEN, unclaimed. Runs before the history scrub job, so the head it covers is the one that is about to change.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: premise run. No code change.
- Why: the only base-code run on record is at `f3131d1ee846`. The Proof at the live head needs its own base run. The delta from that head is the changelog only, so the risk is low.

## Steps

1. Compute the merge-base: `git merge-base upstream/main 404f13b501c9`. Expect `14c7aac0d151`; record what you get.
2. Make a worktree at the merge-base. Overlay only the branch's test files, so RestoreCmd.java and the other production files are the merge-base versions.
3. Run TestLocalFSCloudBackupRestore with JUnit XML. Grep the branch for the exact package before composing the run spec.
4. Record the failing test names, the failure lines, the counts, the seed and the date.
5. If it does not compile, record the compiler message and classify the run INCONCLUSIVE.
6. Add a base-run section to `receipts/SOLR-12651.md`, with the head, the merge-base, the counts and the date. Mark this job DONE.

## Checklist

- [ ] merge-base computed and recorded
- [ ] worktree at the merge-base, branch tests overlaid
- [ ] focused run with JUnit XML
- [ ] failure lines and counts recorded
- [ ] receipt base-run section written; job DONE
