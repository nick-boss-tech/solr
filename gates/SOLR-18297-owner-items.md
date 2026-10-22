# Gate job: SOLR-18297 owner-items round 1 (item 8, tree cleanup, DirectInvocationOnMock, lockfiles)

Status: PENDING re-gate at `73f269e99a0`

- Branch `solr-18297-jetty-12-1-14`. Commits: `c44fc60601e` (PR draft removed, `DirectInvocationOnMock` fixed), `73f269e99a0` (module lockfiles regenerated for Jetty 12.1.14).
- Host: vm5. Logs: `/workspace/gates/logs/vm5/solr-18297-owner-items/` and `solr-18297-owner-items-2/`.

## Gate at `c44fc60601e` (tree identical to the commit)

1. Tidy: rc=0, no changes.
2. `:solr:core:cleanCompileTestJava :solr:core:compileTestJava`: rc=0 (Error Prone `-Werror` clean; the vm3 finding was on a clean compile).
3. `:solr:core:test --tests CoreReplicationAPITest`: rc=0. XML: 12 tests, 1 skipped, 0 failures.

## Lockfiles

- Before: 14 module lockfiles pinned `jetty-server` 12.1.12 while `gradle/libs.versions.toml` says 12.1.14. Offline regeneration failed (javacc not cached). Online `resolveAndLockAll --write-locks` rc=0.
- After: all 26 `jetty-server` pins are 12.1.14. The diff touches only Jetty lines (14 files, 280 lines).

## Re-gate

The gate for `73f269e99a0` is queued (tidy, clean compile, `CoreReplicationAPITest`). The result goes in this file when it runs.
