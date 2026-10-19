# Claim: skip-SOLR-15997-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15997, item 5 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 5. Start from `material/skip-audit/remediation-current.json` (item SOLR-15997) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T17:48:11Z (UTC).

Heartbeat: 2026-10-19T17:48:11Z

Result: 2026-10-19T17:48:11Z: keep-open. Code path at the pinned head: CollectionMutator.java:118-133 (flip reads and writes the collection state), ZkStateWriter.java:349 (BadVersionException handling). The mechanism is not confirmed; PerReplicaStatesOps not read. Not reproduced: needs a multi-node cluster run. Missing pieces listed in the record.

DONE: 2026-10-19T17:48:11Z. Deliverables: `drafts/SOLR-15997/record.md` and `drafts/SOLR-15997/jira-comment.md` on branch `solr-15997-draft-open` (84c59b4692), `material/skip-audit/queue-current.json` (SOLR-15997 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 5).
