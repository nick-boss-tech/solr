# Run job: SOLR-11650 base run for the follower-details case

- Branch: solr-11650-submit. Head: e4f5e941cd8e88524196b03f2022261c2215c3ba (live tip, verified by ls-remote 2026-10-10).
- Intended host: vm2. Any capable Linux host may claim if vm2 is busy (WORKFLOW.md).
- Status: UNCLAIMED.
- Job type: premise (base) run. Not a gate: the branch is already gated green at this head (receipt: URLUtilTest 18 of 18, IndexFetcherLeaderUrlRedactionTest 2 of 2, TestUserManagedReplicationWithAuth 3 of 3).
- Run owed: a base run for the method testFollowerDetailsRedactLeaderUrlPassword, owed before the draft's Proof may claim that case fails without the change (Replication and backup round 1 answers, main-side work owed, item 3). The receipt's existing proof covers the redaction test against the pre-fix IndexFetcher (fails 2 of 2 with the password visible); the follower-details case has no base run yet.
- Run shape: locate the method by grep and copy its class's exact package from the file (FQCN rule). Run that single method with the branch's tests in place and the production code at base (the pre-fix IndexFetcher path). Expected on base: the case fails with the password visible in the details output. Record the outcome, the failure text, and the seed here, from the fresh JUnit XML.
- If the method is not found on the branch, or the case passes on base, record that here as the result; do not adjust the branch under this job.
- Deliverable: update receipts/SOLR-11650.md with the base-run outcome, in the same push that marks this job DONE.
- On completion: mark this job DONE and the claim DONE.

## Step checklist

- [ ] method located by grep, exact class recorded
- [ ] base run executed, outcome and failure text recorded from fresh JUnit XML
- [ ] receipt updated, job and claim marked DONE
