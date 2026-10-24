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
