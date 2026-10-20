# Claim: pool-decisions-impl, slice SOLR-18214 (owner decision row 2, option A)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE, stopped at verification (no branch). The answer requires an empty configset to load a core. It does not: the upload succeeds, but collection creation fails. The answer is not changed; see the evidence below.
- Job or assignment: `assignments/pool-owner-decisions-round-1-implementation.md`, item 2. Source answer: `assignments/pool-vm3-owner-decisions-round-1.md` row 2.
- Slice: the whole ticket.
- Capability tags used: implementation, gate.
- Started: 2026-10-20T03:50:00Z (UTC).
- Links: Ticket or parent: SOLR-18214. Category: Configsets and the filestore. Siblings: `CloneConfigSet` (v2 create) and `ConfigSetCmds.createConfigSet` (v1 and overseer). Supersedes: the stopped claim `claims/pool-vm3-jira-newdev-bugs-round-1-solr-18214.md`.
- Idempotency key: SOLR-18214@c8ab71430f.

Heartbeat: 2026-10-20T03:50:00Z

## Verification evidence (probe, 2026-10-20)

- Probe: an uncommitted test, `EmptyConfigSetProbeTest` (one cluster, one node), on base `c8ab71430f`. Gate: `/workspace/gates/logs/vm3/solr-18214/` (compile rc=0, test rc=0, seed `18214A001`, 1 test). The probe was not committed; its outcome is the test output below.
- `configSetService.uploadConfig("empty-probe", <empty directory>)`: OK; `checkConfigExists("empty-probe")` returns true.
- `CollectionAdminRequest.createCollection("probe-coll", "empty-probe", 1, 1)`: FAILED. The server reports `Underlying core creation failed ... Unable to create core [probe-coll_shard1_replica_n1] ... Can't find resource 'solrconfig.xml' in classpath or '/configs/empty-probe'`.
- Conclusion: an empty configset registers but cannot load a core, because there is no `solrconfig.xml`. So "creates an empty configset" as written in the answer does not produce a usable configset. Per the answer ("if that fails, stop and record the evidence ... do not change the answer"), the branch is not started. No code change was made.
- Next decision for the owner or phone agent: either accept a minimal empty configset with a generated `solrconfig.xml` and `schema.xml` (a new behavior the answer does not describe), or change the answer to option C (reject with a clear message). Recorded in the decisions file under "Evidence from implementation".
