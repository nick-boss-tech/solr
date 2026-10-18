# Claim: SOLR-16887 restore ErrorFile and BATS gate (vm1)

Status: CLAIMED by vm1 2026-10-17 (takes the vm3 handoff of 2026-10-18 in gates/SOLR-16887-errorfile-bats-name.md)

- Job: gates/SOLR-16887-errorfile-bats-name.md.
- Head to gate: the four edits from gates/SOLR-16887-head-b0223fd235.patch applied on 42675f65d6 and committed as Nick Shanin on this host; the new hash is recorded in the gate file when built (it will differ from b0223fd235).
- Capability tags: implementation, gate, bats.
- Runner: ~/workspace/tools/solr-gradle.sh under the test-queue flock, with -PdisableJsClient=true on every Gradle run (deviation recorded in the receipt). BATS legs through the Gradle bats task so the SOLR_PORT family is set the way solr/packaging/build.gradle sets it. lsof is present on this host (/usr/bin/lsof).
- Push rule: fast-forward only to solr-16887-submit, and only on green. Gate failures are recorded in the job file before any relaunch.

Heartbeat: 2026-10-17 (claim).
