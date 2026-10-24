# Report: solr-17988-subtasks-round-1

## ST1: the silent env var renames (audit gaps 1 and 4)

Verified by vm2 (not vm6). Trees: `releases/solr/10.0.0` and `releases/solr/9.10.1` (the 9.x line), read with `git grep`. Counts are hits in `solr/bin/` (scripts and `solr.in.*` samples).

| Old name | New name | 9.10.1 reads old | 10.0.0 reads old | 10.0.0 reads new | Audit verdict | Verified |
| --- | --- | --- | --- | --- | --- | --- |
| `SOLR_PORT` | `SOLR_PORT_LISTEN` | 73 | 0 | 72 | gap | confirmed |
| `SOLR_HOST` | `SOLR_HOST_ADVERTISE` | 17 | 0 | 14 | gap, "deprecated" wording | confirmed; the note should say removed |
| `SOLR_REQUESTLOG_ENABLED` | `SOLR_LOGS_REQUESTLOG_ENABLED` | 6 | 0 | 6 | gap | confirmed |
| `SOLR_ADMIN_UI_DISABLED` | `SOLR_UI_ENABLED` (inverted) | 4 | 0 | 6 | gap | confirmed; `SOLR_UI_ENABLED=false` disables the UI (`solr/bin/solr` line 319) |
| `SOLR_WAIT_FOR_ZK` | `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS` | 6 | 0 | 0 (sample only) | gap, mapping inferred | CORRECTED, see below |

Correction to the audit. The audit says `SOLR_WAIT_FOR_ZK` maps to the env var `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS`. That is wrong for 10.0.0. The new name appears only as a commented sample in `solr.in.cmd` (line 72) and `solr.in.sh` (line 78). No script reads it. The Java code reads only the system property `solr.cloud.wait.for.zk.seconds` (`solr/core/src/java/org/apache/solr/core/NodeConfig.java`, line 247). So the working replacement in 10.0 is `-Dsolr.cloud.wait.for.zk.seconds`, and the upgrade note says that.

Draft: branch `solr-17988-st1` on the fork, head `1937323508f1674892acba4095cb1685f754f648`, one commit on `upstream/main` `79b23df58d`. The change is in `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc` only: the `SOLR_HOST` sentence changes from "deprecated" to "removed", and a list of the four other removed names follows it.

Not verified: the Ref Guide build was not run. No code or script change. The `host` system property mentioned in the old sentence was not checked and was dropped from the new text; it needs a check before the PR.

## ST2: the stale port docs (audit gap 2)

Verified by vm2 (not vm6) against `releases/solr/10.0.0`.

- Stop and RMI ports are derived from `SOLR_PORT_LISTEN` in 10.0: `solr/bin/solr` line 515 (`THIS_STOP_PORT`), line 971 (`STOP_PORT=$((SOLR_PORT_LISTEN - 1000))`), line 1122 (`RMI_PORT=$((SOLR_PORT_LISTEN + 10000))`). The help text in `solr/bin/solr` lines 386 and 387 and `solr.cmd` lines 304 and 305 says the same. The doc's formula is correct; only the variable name is stale.
- The env var is read in 10.0: `solr/bin/solr` line 158 (`PROVIDED_SOLR_PORT="${SOLR_PORT_LISTEN:-}"`). It has a sample in `solr.in.sh` line 135 (`#SOLR_PORT_LISTEN=8983`). `SOLR_PORT` has zero hits in 10.0 `solr/bin/`.
- Stale passages on main, with main's line numbers (10.0 numbers differ, the page moved): `solr-control-script-reference.adoc` lines 161 and 162. `upgrading-a-solr-cluster.adoc` lines 45, 89 and 99.
- Audit correction. The audit listed `upgrading-a-solr-cluster.adoc` lines 45 and 89 only. Line 99 (the sentence that says the upgraded node joins because `SOLR_PORT` is the same) is also stale. The draft fixes it.
- Doc home for `SOLR_PORT_LISTEN` (gap 2): the control-script reference now names it next to `-p`, in the sentence that defines the stop and RMI ports.

Draft: branch `solr-17988-st2` on the fork, head `19a191d34fdea3c679340ea1cc24ae0dd18d3305`, one commit on `upstream/main` `79b23df58d`. Three lines in `solr-control-script-reference.adoc` and three in `upgrading-a-solr-cluster.adoc`. No code change.

Not verified: the Ref Guide build was not run. ST7 runs it on the assembled branch.

## ST1 correction (vm2, after ST3 verification)

The ST1 section above says `SOLR_ADMIN_UI_DISABLED` is replaced by `SOLR_UI_ENABLED`, and that `SOLR_UI_ENABLED=false` disables the Admin UI. That is wrong for 10.0.

- In 9.10.1, `SOLR_ADMIN_UI_DISABLED=true` sets `-DdisableAdminUI=true` (`solr/bin/solr` lines 1254 to 1256), which disables the classic Admin UI.
- In 10.0, the classic Admin UI is `LoadAdminUiServlet`, which reads the system property `solr.ui.enabled` (`solr/core/src/java/org/apache/solr/servlet/LoadAdminUiServlet.java` line 44). No script maps any environment variable to it (`git grep` for `ui.enabled` in `solr/bin` returns nothing).
- In 10.0, `SOLR_UI_ENABLED` is read only by the start script, at line 319, to decide whether to add `--module=new-ui`. It controls the new Admin UI module, not the classic UI.

Corrected text: `SOLR_ADMIN_UI_DISABLED` has no environment variable replacement. Disable the classic Admin UI with the system property `solr.ui.enabled=false`, for example in `SOLR_OPTS`. `SOLR_UI_ENABLED` controls only the new Admin UI module. The `solr-properties.adoc` table already lists `solr.ui.enabled`. `SOLR_OPTS` reaches the JVM (`solr/bin/solr` line 679, and the `solr.in.sh` sample at line 100).

Corrected branch: `solr-17988-st1`, head `39284426d4c3834d35c4c22816ec97c521ec81d2`.

## ST3: the undocumented new env vars (audit gap 3)

Verified by vm2 (not vm6) against `releases/solr/10.0.0`.

- `SOLR_UI_ENABLED`: read by the start script only (`solr/bin/solr` line 319). Its effect is to add `--module=new-ui`. No Java code reads it, and no system property is set from it.
- `SOLR_UI_EXPERIMENTAL_ENABLED`: read by the start script only (`solr/bin/solr` line 320). Setting it to `false` also skips `--module=new-ui`. The `solr.in.sh` sample at lines 253 to 258 says the same.
- `solr.ui.experimental.enabled`: listed in `solr-properties.adoc` (line 123 on main), but the only 10.0 occurrence in the tree is `DeprecatedSystemPropertyMappings.properties` line 67. No Java code reads it. Not documented further here. Flagged as unverified.
- `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS`: not read by any 10.0 script. It appears only as a commented sample in `solr.in.cmd` (line 72) and `solr.in.sh` (line 78). The 10.0 Java code reads `solr.cloud.wait.for.zk.seconds` (`NodeConfig.java` line 247). The doc home is the existing `solr-properties.adoc` row at line 49 (`solr.cloud.wait.for.zk.seconds`, legacy `waitForZk`, default 30). That row is accurate and names no environment variable, so it is not changed. The env var is not documented as working.

Draft: branch `solr-17988-st3` on the fork, head `4103e5602da31ff969184f184ae69f61ea2ad886`, one commit on `upstream/main` `79b23df58d`. The change is in `major-changes-in-solr-10.adoc` only: two bullets for the new-UI variables and one sentence that the classic UI uses `solr.ui.enabled`.

Not verified: the Ref Guide build was not run. The `solr.ui.experimental.enabled` sysprop has no reader in 10.0 code, which the table does not say.
