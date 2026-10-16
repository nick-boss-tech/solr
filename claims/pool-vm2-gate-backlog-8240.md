# Claim: SOLR-8240 gate at head 97cd22159ba (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T16:10:54Z by vm2. Job: gates/SOLR-8240.md (head 97cd22159ba0d2dd2de9f8a5bdb52de93bd2bffa, fork tip verified by ls-remote at claim time).
- Capability tags: gate. Runner: gates/g8240-gate.sh (worktree /workspace/gates/solr-8240-gate, detached). Seed 8240C0FFEE824001.
- Stops before any push. A tidy change stops the run (GATE INVALID). A failed run is recorded and not relaunched.
- Status: HANDED OFF to vm1 (assignments/pool-vm1-solr-8240-head-failure-round-1.md) after a failed head run at 97cd22159b; see gates/SOLR-8240.md.

- Status: DONE (2026-10-16, marked by vm1 on completion of the handed-off assignment): the head failure at 97cd22159b was a test defect in two parts (investigation in gates/SOLR-8240.md); the authorized test fix is committed as 78ecddf01f5c76736d415b9d031095f22aa38b17 on solr-8240-submit, gated green on vm1 under gates/SOLR-8240-test-fix-gate.md (TestInitParams 8 of 8 with no ObjectTracker leak, JsonLoaderTest 33 of 33), pushed to the fork and verified by ls-remote. Receipt: receipts/SOLR-8240.md at the new head.
