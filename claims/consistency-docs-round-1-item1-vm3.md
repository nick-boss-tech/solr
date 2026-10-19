# Claim: consistency-docs-round-1, item 1 (V2 core backup create permission)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED.
- Assignment: `assignments/pool-consistency-docs-round-1.md`, item 1 only.
- Other items: item 2 is DONE by vm4 (`claims/consistency-docs-round-1-item2-vm4.md`). Items 3 and 4 are not claimed by vm3.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T17:40Z (UTC).

Heartbeat: 2026-10-19T17:40Z

Heartbeat: 2026-10-19T18:05Z

BLOCKED: 2026-10-19 by vm3 at gate step 2 (Error Prone compile). Branch `consistency-v1-core-backup-permission`, head `08bbb44b9e` (base `498a09aea9`, same base as item 2). The item change is `CreateCoreBackup.java` `@PermissionName(COLL_EDIT_PERM)` to `CORE_EDIT_PERM`, with its import. `BackupCoreAPITest` passed 5 of 5 (seed A1B2C3D4E5F60718, XML). The Error Prone compile failed on two `DirectInvocationOnMock` warnings in `solr/core/src/test/org/apache/solr/handler/admin/api/CoreReplicationAPITest.java` lines 213 and 217 (`-Werror`). That file is untouched from the base, so the failure is at base. The item's Limits say to change only the declaration and its asserting tests, so vm3 did not edit it. Decision needed from the owner: fix `CoreReplicationAPITest` in a separate item, or waive the errorprone step for this item. Logs: `/workspace/gates/logs/vm3/consistency-item1/`.
