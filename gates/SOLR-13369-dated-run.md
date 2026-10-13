# Gate job: SOLR-13369 dated focused run at the live head

- Branch: solr-13369-submit. Head: `dfa0db5bdf96` (live tip, ls-remote).
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: dated focused run. No code change.
- Why: the draft's Proof gives both counts as "recorded 2026-10-07". That date is a takeover-log date, and the formula needs a verification date. A dated run at the head settles it.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-13369-submit` must equal `dfa0db5bdf96`. If it does not, stop and record the new tip.
2. Grep the branch for the exact package of TriLevelCompositeIdRoutingTest before composing the run spec.
3. Run TriLevelCompositeIdRoutingTest at the head with JUnit XML. Record the counts, the seed and the date.
4. Run the base-code counterpart if the draft's base failure is to be cited again: the production files at the merge-base, the branch tests kept. Record the failure and the date. Compute the merge-base with `git merge-base upstream/main dfa0db5bdf96`.
5. Add the dated counts to `receipts/SOLR-13369.md` with the head and the date. Mark this job DONE.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] package grepped
- [ ] head run with JUnit XML, counts and date recorded
- [ ] base-code run recorded, if cited
- [ ] receipt updated; job DONE
