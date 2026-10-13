# Gate job: SOLR-11288 base-code run at the live head

- Branch: solr-11288-submit. Head: `cbf0c678f277d6f50d75408d4889d3b664469905` (live tip, ls-remote).
- Status: DONE. Claimed by vm2 (gate backlog coordinator) 2026-10-13T07:37:27Z; run 07:45:13Z to 07:52:09Z; base-run section in receipts/SOLR-11288.md. Claim: claims/pool-vm2-gate-backlog-11288-baserun.md.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: premise run. No code change.
- Why: the only base-code run on record is at `cd094c3c623`. The receipt attributes one base run to the padded repeat, but the test asserts the exact repeat first (TestCollectionAPI, line 321), so that run cannot reach the padded case. The draft's Proof at the live head needs its own base run.

## Steps

1. Compute the merge-base: `git merge-base upstream/main cbf0c678f277`. Expect `97d973814336`; record what you get.
2. Make a worktree at the merge-base. Overlay only the branch's test files, so the production files are the merge-base versions.
3. Run TestCollectionAPI with JUnit XML. Grep the branch for the exact package before composing the run spec.
4. Record the failing test names, the failure lines, the counts, the seed and the date.
5. If it does not compile, record the compiler message and classify the run INCONCLUSIVE. Do not relabel it.
6. Add a base-run section to `receipts/SOLR-11288.md`, with the head, the merge-base, the counts and the date. Mark this job DONE.

## Checklist

- [ ] merge-base computed and recorded
- [ ] worktree at the merge-base, branch tests overlaid
- [ ] focused run with JUnit XML
- [ ] failure lines and counts recorded
- [ ] receipt base-run section written; job DONE
