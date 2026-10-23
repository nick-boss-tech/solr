# Claim: SOLR-6213, repeat the rejoin regression test on another host, round 1
- Claimant host: phone-agent, matching `hosts/phone-agent.md`.
- Status: DONE.
- Job or assignment: `assignments/solr-6213-rejoin-repeat-round-1.md`.
- Slice: the whole assignment (items 1 to 3).
- Capability tags: gate, premise-run.
- Pinned heads: `solr-6213-rejoin-fix-test` at `64624f4726f49dc723e319f6c6aa52ba81f52189`; `solr-6213-rejoin-fix` at `bcea4f23c1944cf0383193c66fccdb9e6e624567`; `solr-6213-rejoin-hook` at its live head, confirmed by ls-remote at run time.
- Started: 2026-10-23T14:41:49Z (UTC).
- Lease: live while heartbeats continue, stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-6213-rejoin-repeat@64624f4726f.
- Links: Ticket SOLR-6213; category Solr core, SolrCloud tests. Supersedes: none.
- Runner note: on this host the builds run through the vm1 queue lock with solr-gradle.sh (the host equivalent of `pool/bin/sweep-run.sh`, whose /workspace layout does not exist here); logs under `~/workspace/tools/` on vm1.
Heartbeat: 2026-10-23T14:41:49Z.

DONE: 2026-10-23T15:17Z. All three items ran on this host. Item 1: 5 of 5 rejoin-test runs passed at random seeds, no ThreadLeakError. Item 2: both hook-branch runs failed with "stack grew by 12 frames over 4 rejoins", as expected. Item 3: DeleteReplicaTest, ZkControllerTest, ReplaceNodeTest and TestCloudRecovery, 24 tests, 2 skipped, 0 failures on the fix branch. Deliverable: `reports/solr-6213-rejoin-repeat.md`.
