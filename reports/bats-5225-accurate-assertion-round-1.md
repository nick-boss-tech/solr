# BATS follow-up for PR 5225: accurate allowPaths assertion (round 1, vm2)

Claim: `claims/bats-5225-accurate-assertion-round-1-vm2.md`. Assignment: `assignments/bats-5225-accurate-assertion-round-1.md`.

## Base and branch

- Base: apache/solr main `07fdebdda54a`, the merge of PR 5225 on 2026-10-25T04:16:10Z. The assignment said to branch from the PR head. The PR has merged, so the branch starts from the merge commit. The claim records this change.
- Branch: `nick-boss-tech/solr:solr-5225-bats-followup`, commit `096da6567e3`, author Nick Shanin, no trailers. One file: `solr/packaging/test/test_security_manager.bats`, 9 insertions and 2 deletions.

## The change

The final assertion of `allowPaths - backup` accepted `access denied|Could not backup all shards` on every Java version. On Java 24+ the test passed on any backup failure.

The new assertion reads the Java major version the same way `solr/bin/solr` does (line 178). Then:
- Java before 24: `assert_output --partial 'access denied'`.
- Java 24+: `assert_output --partial 'Could not backup all shards'`, and `assert_file_contains "${SOLR_LOGS_DIR}/solr.log" "must be relative to SOLR_HOME.*${backup_dir_other}"`.

## Evidence

Code, on `07fdebdda54a`:
- `solr/bin/solr` line 1175: the security manager is disabled on Java 24+.
- `CreateCoreBackup.java:81`: each shard runs `assertPathAllowed` for the backup location.
- `SolrPaths.assertPathAllowed`: throws `Path <path> must be relative to SOLR_HOME ...`.
- `CollectionHandlingUtils.java:781`: the coordinator throws `Could not backup all shards` with the shard exception as its cause. The API response carries only the wrapper. The specific reason is in the coordinator's stack trace, which the framework writes to solr.log.

Run, JDK 21.0.12.1+1, Linux aarch64 (`logs/bats-5225/verify.log`):
- The assertion ran and matched `access denied` on the security-manager path. BATS reported no assertion failure.
- The test still reported `not ok 1` because `teardown` failed (see below). A run on unchanged main (`logs/bats-5225/baseline.log`) fails the same way.

## Host finding: teardown fails on this host

`SOLR_STOP_WAIT=1 solr stop --all` (and `SOLR_STOP_WAIT=30`) returns 1 when a running node is still alive after the wait. The stop script then cannot attach to the JVM (`Unable to open socket file /tmp/.java_pid...`) and force-kills it. This happens with and without the security manager (`SOLR_SECURITY_MANAGER_ENABLED=false` also returns 1). It affects every BATS test that stops a node, and the unchanged main fails the same way. It is not caused by this change. The local verification run used a 30-second wait and was reverted afterwards; the committed file contains no workaround.

## Limits

- Java 24+ leg: code reading only. This host has JDK 21 and no JDK 24. The claim that the reason is in solr.log comes from the code path, not from a run. A run on JDK 24 is needed before the PR states it as proven.
- A fully green BATS run is blocked on this host by the teardown finding above.
- PR not opened. Opening a PR needs Nick's go.
