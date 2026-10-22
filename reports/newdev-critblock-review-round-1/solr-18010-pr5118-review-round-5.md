# SOLR-18010, PR #5118: review round 5, blast-radius pass (vm3, 2026-10-22)

Head reviewed: `1b14ce9556` (unchanged). No code change this round. Body updated and read back: `pr-drafts/solr-18010/PR-5118-body-round-5.md` (4,397 visible characters, from 4,104 plus two sentences).

Maintainer activity at review time: no reviews and no comments from maintainers (`reports/maintainer-threads/5118.md`).

## Blast-radius checks (playbook checklist)

1. **Readers and writers.** Standalone `security.json` is read and written only by `SecurityConfHandlerLocal` at head. `AuthTool` writes `/security.json` to ZooKeeper only (`AuthTool.java` L345, L448), so `bin/solr auth` is unaffected. Tests that mention the saved file are `SecurityConfHandlerTest`, `BasicAuthStandaloneTest`, `BasicAuthOnSingleNodeTest`, `BasicAuthIntegrationTest`, `MultiAuthPluginTest`, `AuthToolTest`, `ZookeeperReadAPITest`, `ReplicationHandlerRecoveryAuthIntegrationTest`, `TestUserManagedReplicationWithAuth`, `AuditLoggerIntegrationTest`, `VMParamsZkACLAndCredentialsProvidersTest`. None of the standalone ones checks for the `""` key. Only the two in this PR's run were executed this round. The rest are on the list to run if a maintainer asks.
2. **Docs.** The ref guide version passage (`authentication-and-authorization-plugins.adoc` L111) is under the ZooKeeper section and says to remove version data when editing a ZooKeeper copy. The standalone section (L115 to L120) says nothing about versions. No doc change is needed in this PR. This is a reading, not a run.
3. **ZooKeeper versus standalone.** `SecurityConfHandlerZk` takes the version from the znode stat (`configDataFromZk.version`, L57) and writes with that version (L82). The stored `v` in ZooKeeper is therefore not used for conditional writes. Standalone never used the stored value for writes. The two storage forms now differ on purpose, and the body says so.
4. **Reload gating.** `CoreContainer.readVersion` returns -1 when the marker is absent, and a standalone file with `v` 0 gave the same result before this change. Reloads are gated only on a positive version, so none changes. A file copied from standalone into ZooKeeper now has no marker. The ZooKeeper write then uses the stat version, so nothing breaks.
5. **Deployment shapes.** Symlinks, read-only SOLR_HOME, and owner change fall back in place (covered by code and by the symlink and read-only runs in round 3). Single-file bind mounts are not run. On Linux, a rename onto a mount point fails with an IOException, which the same fallback catches (reading only).
6. **Crash leftovers. New finding.** A crash between `createTempFile` and the move leaves `security.json*.tmp` in SOLR_HOME with the new content. `createTempFile` creates it 0600 on POSIX (probe, round 3: `rw-------`). Nothing in Solr removes it. Added to the body's Limits.
7. **Upgrade and downgrade.** An older Solr reading a file written by this change has no `""` key, which the older code tolerates, since it reads the marker only for reloads (same as a standalone file before). A newer Solr reading an older file keeps the marker until the next edit. Not run; reasoning from the readers above.
8. **Test coverage.** Fail-before on main: `testConcurrentPersistConfLeavesOneWholeDocument` and `BasicAuthStandaloneTest.testBasicAuth` (round 4 run). Pins only: the other five (round 4).
9. **Changelog.** Type `fixed`. The title says the temporary file is used "where that is safe" in the body; the changelog title already states the fallback. No change.

## Findings and changes

- Added to Limits: the crash leftover (check 6).
- Added to What this change does: ZooKeeper is unchanged, and its writes use the znode version (check 3). A maintainer would ask why the two stored forms differ.
- Checked and not changed: the ref guide, `AuthTool`, reload gating.

## Open, for maintainers (unchanged)

- Keep the atomic move, or write in place and strip only the marker.
- Allow `LinkPermission "symbolic"` in the repo test policy, or leave the link case untested.

## Not run

- Single-file bind mount (no root). Windows. Owner change (needs root). The other standalone test classes listed in check 1, not run this round.
