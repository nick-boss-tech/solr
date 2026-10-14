# Job: SOLR-5394 push the gated changelog-type head (post-gate step)

- Branch: `solr-5394-submit` on the fork. Current fork tip: `967445622f9294e26e425ec494d37e5ca13d4fe4` (draft PR #5131). Gated head: `6e4c302c418be91b71305a1fe2f718e96295835f`, one commit on the fork tip changing `changelog/unreleased/SOLR-5394-fcs-default-threads.yml` from `type: fixed` to `type: changed`.
- Status: OPEN for vm2. vm2 ran the gate (gates/SOLR-5394-review-changelog-type.md, DONE 2026-10-14, GATE GREEN at the gated head; record commit 432285f807 and receipts/SOLR-5394.md) and holds the gated head locally. That job's own step 8 kept the head move as a separate step after the gate; this job is that step.
- Authorization: no further approval is needed. Pushes to Nick's own open PRs carry his standing authorization (ruled 2026-10-14), and the changelog type `changed` carries his owner ruling of 2026-10-14.
- Capability tags: push, description edit. Linux only (vm2 holds the head). Windows never runs this job.

## Steps

1. Confirm the starting point: `git ls-remote` on the fork shows `solr-5394-submit` at `967445622f9294e26e425ec494d37e5ca13d4fe4`, and the local gated head is `6e4c302c418be91b71305a1fe2f718e96295835f` with parent `967445622f9294e26e425ec494d37e5ca13d4fe4` (a plain fast-forward). Stop and record here if either differs.
2. Push the gated head to the fork as a fast-forward with the lease pinned to the current tip: `git push --force-with-lease=refs/heads/solr-5394-submit:967445622f9294e26e425ec494d37e5ca13d4fe4 origin 6e4c302c418be91b71305a1fe2f718e96295835f:refs/heads/solr-5394-submit` (adjust the remote name to the fork remote in the local clone).
3. Verify by `git ls-remote`: the fork tip is now `6e4c302c418be91b71305a1fe2f718e96295835f`.
4. Re-point PR #5131's body to the new head: replace every occurrence of `967445622f9294e26e425ec494d37e5ca13d4fe4` with `6e4c302c418be91b71305a1fe2f718e96295835f`. Leave the base citations (`b6b2b8f10e9827e3b86e44649fb7966ce646c185`) untouched; they point at the pre-change code on purpose. In the Proof section, the head reference moves to the new head and the verification becomes the 2026-10-14 gate at that head: SimpleFacetsTest 49 tests, 0 failures, 0 errors, 1 skipped (receipts/SOLR-5394.md). Re-read the body through the API and verify the new SHA is present, the old head SHA is absent, and the PR is still an open draft at the new head.
5. Refresh receipts/SOLR-5394.md: the 2026-10-14 gate section's status line records the push (new fork tip, verified by ls-remote, PR body re-pointed and re-read), so the receipt no longer says the head move waits.
6. Mark this job DONE with the date and the verification results.

## Checklist

- [ ] fork tip and gated head confirmed
- [ ] push landed with the lease pinned to 967445622f92
- [ ] fork tip verified by ls-remote at 6e4c302c418b
- [ ] PR #5131 body re-pointed and re-read
- [ ] receipts/SOLR-5394.md refreshed
- [ ] job DONE
