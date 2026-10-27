# Receipt: packaging BATS suite, standalone, patched head (vm5, 2026-10-27, 00:36 to 01:04 UTC)

Command: `:solr:packaging:integrationTests --rerun` on head `23053d08816`, with `JAVA_HOME` and `SOLR_JAVA_HOME` set to JDK 21.0.12.1, `/workspace/tools/bats-shim/bin` first on PATH, and the Gradle command wrapped by `/workspace/tools/bats-shim/reap-run.py`.

Result: 127 ok, 0 not ok, exit 0. Log: `/workspace/smoke-rc3/bats-alone/bats.log` (on vm5).

Why the workarounds: this host has no `lsof` on PATH, and PID 1 does not reap orphaned children, so stopped Solr JVMs stayed as zombies and `solr stop` waited out its timeout. The shim is a substitute written on this host (ss-based, supports only the calls `solr/bin/solr` makes). The reaper is a child-subreaper wrapper. Neither is the vm4 tool of the same name, which is not on this host.

Earlier integration failure (full smoke run 2, 46 `not ok`) had the same symptoms without these workarounds. The run-3 integration step passed later (`BUILD SUCCESSFUL in 29m 12s`, see `rc3-patched-smoke-run3-round-1-vm5.md`).
