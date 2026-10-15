# Gate job: SOLR-16437 nightly-group run at the live head

- Branch: solr-16437-submit. Head: `f72a9edd0a8b` (fork tip, per the receipt; confirm by ls-remote).
Status: DONE
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: focused run with the nightly group enabled. No code change.
- Why: the receipt records 39 of 39 for TestPullReplica and 2 of 14 for TestTlogReplica with nightly tests skipped. The draft's Limits discloses the skip. A run with the nightly group enabled would clear the Limits line, or give a count to replace it.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-16437-submit` must equal `f72a9edd0a8b`. If it does not, stop and record the new tip.
2. Grep the branch for the exact packages of TestPullReplica and TestTlogReplica before composing the run spec.
3. Run both classes with the nightly group enabled, with JUnit XML. Record the counts, the seed and the date.
4. Add the counts to `receipts/SOLR-16437.md` with the head and the date. Mark this job DONE.

## Checklist

- [x] head confirmed (owner's allowance for f72a9edd0a8b; live tip a23f770e4b9 differs by the changelog title)
- [x] packages grepped (org.apache.solr.cloud.TestPullReplica, org.apache.solr.cloud.TestTlogReplica)
- [x] both classes run with the nightly group enabled; JUnit XML counts and date recorded
- [x] receipt updated; job DONE
