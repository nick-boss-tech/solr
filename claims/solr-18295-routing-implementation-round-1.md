# SOLR-18295 routing implementation, round 1

Status: CLAIMED

- Host: windows (Codex desktop); capability: implementation, review, records.
- Started: 2026-10-24 17:50:54 UTC.
- Assignment: Nick's direct request in this chat to implement a branch for SOLR-18295, using PR #5098 and Sanjay's report as related evidence.
- Board snapshot: `961e8ac77c8e092d98b3e6b37fa42e28189df2a0`; live recursive tree contains no SOLR-18295 claim, gate, receipt, or assignment. Fork submit branch absent (Contents/ref API 404).
- Slice: test-local cloud-client routing override; unmute ReplicationFactorTest; preserve every replication-factor assertion, proxy cut/reopen, and explicit non-direct request to the healthy leader and follower. No SolrJ or shared harness change.
- Base: upstream/main `13c693d805f6541f7cec684cd27d28eef7b3b614`.
- Decision ladder: rung 1 (code and history) plus rung 3 (owner's explicit implementation request). JIRA's c851aae5 and branch_10x cherry-pick only mute the test. AbstractFullDistribZkTestBase.createCloudClient randomizes any-replica routing; this test then closes non-leader proxies and sends updates through that client. Its existing override hook can select leaders-first routing for all cloud update helpers. The test's explicit non-direct requests still target the healthy shard1 leader and follower after only shard2's follower is cut.
- Existing work: Jan Høydahl's open apache/solr #4563 (`54f375b0bed805a08e1b456bd782e15dfce74723`) retries single-document sends. Prepare a fork-only alternative for Nick's review; do not post, supersede, or alter that PR.
- Deliverables: fork `solr-18295-submit`; `reports/solr-18295-routing-implementation-round-1.md`; `gates/SOLR-18295-routing-round-1.md` pinned to the pushed SHA.
- Acceptance: exact-head Linux compile/format/check and repeated whole-class runs, matched against baseline with its mute removed and risky routing forced. Record non-reproduction honestly; PR #5098's 16/100 to 0/100 result is related evidence, not proof for SOLR-18295.
- Windows restriction: no Gradle, test queue, or local test execution. No public PR or JIRA writes authorized.
