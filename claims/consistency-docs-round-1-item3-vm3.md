# Claim: consistency-docs-round-1, item 3 (configset file delete containment)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED.
- Assignment: `assignments/pool-consistency-docs-round-1.md`, item 3 only.
- Other items: item 1 is held by vm3 (`claims/consistency-docs-round-1-item1-vm3.md`); item 2 is DONE by vm4; item 4 is BLOCKED by vm4 (`claims/consistency-docs-round-1-item4-vm4.md`).
- Branch: `hardening-configset-delete-containment`, base `498a09aea9` (same base as item 1 and item 2).
- Capability tags used: implementation, gate.
- Started: 2026-10-19T18:20Z (UTC).

Heartbeat: 2026-10-19T18:20Z

Result (vm3, 2026-10-19T19:12Z): STALE, superseded by vm4. `claims/consistency-docs-round-1-item3-vm4.md` is DONE with branch `hardening-configset-delete-containment` at `3905ad78e8` (pushed 18:27Z). vm3 did not push over it; the push of vm3's commits was rejected as non-fast-forward, and the remote branch was left unchanged.

vm3's own implementation, for comparison and not pushed: `154208bbde` (fix, tree `025da0c769`) plus `eda3c44efc` (the Error Prone repair, same as item 1's `0c37fbd580`, tree `9e5c77924a`). It throws `SolrException` FORBIDDEN for any name resolving outside the configset directory, checks every name before any delete, and adds `testDeleteRejectsNamesOutsideConfigSetDirectory`. Its gate: first run red at Error Prone on `CoreReplicationAPITest` (`/workspace/gates/logs/vm3/configset-item3/errorprone.log`); re-gate on `eda3c44efc` green: tidy 0, Error Prone 0, `TestFileSystemConfigSetService` 3 of 3 with 0 failures, `:solr:core:check -x test` 0 (`/workspace/gates/logs/vm3/configset-item3-r2/`).

Heartbeat: 2026-10-19T19:12Z
