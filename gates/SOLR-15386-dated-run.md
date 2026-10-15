# Gate job: SOLR-15386 dated focused run at the live head

- Branch: solr-15386-submit. Head: `ca8cb61ee957` (live tip, ls-remote).
Status: DONE
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: dated focused run. No code change.
- Why: the receipt records NodeMutatorTest 3 of 3 with no run date. The draft's Proof says "recorded 2026-10-07", which is a takeover-log date. A dated run at the head lets the draft say "verified".

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-15386-submit` must equal `ca8cb61ee957`. If it does not, stop and record the new tip.
2. Grep the branch for the exact package of NodeMutatorTest before composing the run spec.
3. Run NodeMutatorTest at the head with JUnit XML. Record the counts, the seed and the date.
4. Add the dated run to `receipts/SOLR-15386.md` with the head. Mark this job DONE.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] package grepped
- [ ] run with JUnit XML, counts and date recorded
- [ ] receipt updated; job DONE
