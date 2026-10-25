# SOLR-18564 exact-head: independent confirmation by vm2

Head `dd65b21b41dc1d008c51031b71f61964ee2ca169` on `nick-boss-tech/solr:corecontainer-shutdown-closes` (apache/solr#5192). vm6's receipt for the same head is `receipts/SOLR-18564.md` (GREEN, claim `claims/solr-18564-exact-head-gate-round-1.md`). This file is vm2's own record; vm6's receipt is not edited.

## Process note

vm2 claimed `claims/solr-18564-exact-head-gate-vm2.md` without first reading `receipts/SOLR-18564.md`. The job's claim and receipt already existed. The duplicate run was not concurrent with vm6's and changed no shared state. This file is the record of it.

## Result: GREEN (vm2 run, independent)

| Leg | Result |
| --- | --- |
| changelog YAML | rc 0 |
| `tidy` | rc 0, tree clean afterwards |
| `compileJava compileTestJava -Pvalidation.errorprone=true` | rc 0 |
| focused, seed `18564C0FFEE18564`, `cleanTest` first | rc 0, `CoreContainerShutdownTest` 1/0/0/0, `TestCoreContainer` 25/0/0/3 (tests/failures/errors/skipped) |
| `:solr:core:check -x test` | rc 0 |

The 3 skips in `TestCoreContainer` are the Windows-only assumption tests `assertAllowPathWindows`, `assertAllowPathNormalizationWin`, `assertAllowPathFromSolrXmlWin`. They skip on Linux by design.

These counts match vm6's runs (1/0/0/0 and 25/0/0/3). Seeds differ: vm6 used `18564ABC0001`/`0002`, vm2 used `18564C0FFEE18564`.

Logs: `/workspace/gates/gates-vm2/logs/solr-18564/` on vm2 (`summary.txt`, `focused.log`, `check.log`). JUnit XML: `solr/core/build/test-results/test` in the (now removed) worktree; the XML counts above were read from it before removal.

Host: vm2, Linux aarch64, JDK 21.0.12.1+1, `GRADLE_USER_HOME=/workspace/gradle-home`. Build serialized with `/workspace/tools/.gradle-vm2.lock`.
