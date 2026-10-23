# Claim: SOLR-5735, repeat the leaders-only safe-leader runs on another host, round 1
- Claimant host: phone-agent, matching `hosts/phone-agent.md`.
- Status: DONE.
- Job or assignment: `assignments/solr-5735-leaders-only-gate-round-1.md`.
- Slice: the whole assignment (items 1 to 3).
- Capability tags: gate, premise-run.
- Pinned head: `solr-5735-safeleader-leaders-only` at `60eb0e7fd0521b8d866443a7b72dd623bacd884d`.
- Started: 2026-10-23T14:41:49Z (UTC).
- Lease: live while heartbeats continue, stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-5735-leaders-only@60eb0e7fd05.
- Links: Ticket SOLR-5735; category Solr core, SolrCloud tests. Sibling assignment `assignments/solr-5735-pull-replica-timeout-round-1.md` is DONE and separate. Supersedes: none.
- Runner note: runs execute on this host's own local disk under the vm1 queue lock with solr-gradle.sh (the host equivalent of `pool/bin/sweep-run.sh`); logs under `~/workspace/tools/` on vm1.
Heartbeat: 2026-10-23T14:41:49Z.

DONE: 2026-10-23T15:17Z. All runs on this host's local disk at 60eb0e7fd05, nightly on. Item 1: 3 of 3 passed at seed E4EFD2B86C0B81D4. Item 2: 3 of 3 random-seed runs passed for each of ChaosMonkeySafeLeaderWithPullReplicasTest and ChaosMonkeySafeLeaderTest. Item 3: no failures to classify. Deliverable: `reports/solr-5735-leaders-only-gate.md`.
