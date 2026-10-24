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

## ST4: the stale sysprop entries (audit gap 5)

Verified by vm2 (not vm6) against `releases/solr/10.0.0`. Result: no doc edit. The audit's gap 5 does not hold.

- `solr.disableFingerprint`, `solr.dns.prevent.reverse.lookup` and `solr.allow.unsafe.resourceloading` are not current property names. Each is the legacy alias of a current name, and the legacy column of `solr-properties.adoc` documents it correctly: line 67 (`solr.index.replication.fingerprint.enabled`), line 31 (`solr.admin.handler.systeminfo.dns.reverse.lookup.enabled`) and line 89 (`solr.resourceloading.restricted.enabled`). The three names appear in 10.0 code only in `solr/solrj/src/resources/DeprecatedSystemPropertyMappings.properties`, which the audit did not account for.
- The mapping is live in 10.0. `EnvUtils` loads the file (`solr/solrj/src/java/org/apache/solr/common/util/EnvUtils.java` lines 66 and 78) and falls back to the legacy name, with the `!` inversion, when the current name is missing (lines 221 to 226). Removing the rows would break users who still set the legacy names, so the "remove or correct" advice in the audit is wrong.
- `solr.node` has no entry in the docs. The 10.0 and main reference guide have no row or prose for a bare `solr.node`. `node-roles.adoc` uses only `solr.node.roles`, which is a current property (`node-roles.adoc` lines 39 to 97). The audit's citation appears to match the prefix of `solr.node.roles`.
- The three legacy names are not sysprops that code reads by their own names. Only the `solr.node` claim is a doc gap, and it does not exist.

Outcome: no confirmed gap, so no draft branch. The claim is DONE with no commit to `solr-17988-st4`.

Not verified: the runtime alias behavior itself (set a legacy name and check the value) was not run. The verdict rests on the mapping code and the 10.0 tree.

## ST5: the undocumented renames and the script removal (audit gaps 6 and 7)

Verified by vm2 (not vm6) against `releases/solr/9.10.1` and `releases/solr/10.0.0`. Checked in code, not inferred from names.

Gap 6, the SSL client auth renames. The audit said the mapping was inferred from names. Code confirms it:
- 9.10.1 `solr/bin/solr` lines 252 and 255 set `-Dsolr.jetty.ssl.needClientAuth` and `-Dsolr.jetty.ssl.wantClientAuth`. `solr/server/etc/jetty-ssl.xml` line 21 reads `solr.jetty.ssl.needClientAuth`.
- 10.0 `solr/bin/solr` lines 248 and 251 set `-Dsolr.jetty.ssl.need.client.auth.enabled` and `-Dsolr.jetty.ssl.want.client.auth.enabled`. `solr/server/etc/jetty-ssl.xml` lines 22 and 23 read the new names.
- Neither `DeprecatedSystemPropertyMappings.properties` nor `EnvToSyspropMappings.properties` has an alias for the old names in 9.10.1 or 10.0. So setting the old names in 10.0 has no effect. The mapping is a real rename, not an alias.
- The environment variables `SOLR_SSL_NEED_CLIENT_AUTH` and `SOLR_SSL_WANT_CLIENT_AUTH` are unchanged in both versions (`solr/bin/solr` lines 247 and 250 in 10.0).
- No 10.0 doc page mentions either old or new property name, so the upgrade note is the only place the change is stated.

Gap 7, the script removal. Verified:
- `solr/bin/post` and `solr/bin/postlogs` exist in 9.10.1 and are absent from 10.0.0.
- Replacements: `bin/solr post` (`SolrCLI.java` line 198 maps `post` to `PostTool`) and `bin/solr postlogs` (`SolrCLI.java` line 199 maps `postlogs` to `PostLogsTool`). `bin/solr` hands unknown commands to `SolrCLI` (`solr/bin/solr` lines 494 to 503), so both forms work.
- The 10.0 Ref Guide already uses the replacements: `query-guide/pages/logs.adoc` lines 27 to 70 document `bin/solr postlogs`, and `enabling-ssl.adoc` line 359 documents `bin/solr post`. No 10.0 page documents `bin/post` or `bin/postlogs`.
- The 10.0 upgrade notes do not say the scripts were removed. That is the gap.

Draft: branch `solr-17988-st5` on the fork, head `0965fe75e4dd0491fade1447d8b41aef624be8a1`, one commit on `upstream/main` `79b23df58d`. The change is in `major-changes-in-solr-10.adoc` only: two rename bullets with the no-alias warning, one sentence that the environment variables are unchanged, and one sentence on the script removal.

Not verified: the Ref Guide build was not run. The `wantClientAuth` rename was checked in the same script lines as `needClientAuth`, not by a separate run.

## ST6: the 56 undocumented new sysprops (audit gap 9)

Assessment only, by vm2 (not vm6), against `releases/solr/10.0.0`. No doc edits.

Scope. The audit appendix lists 56 names. `solr.NumFieldLimitingUpdateRequestProcessorFactory` is a class name used in a test configset, not a property, so it is excluded. The other 55 are each read by 10.0 code, a Jetty XML file or the start script. None has a Ref Guide row.

Not verified. This ranks by what each property controls and where it is read. Defaults, whether a default changed in 10.0, and runtime behavior were not checked.

### Recommended minimum for this PR (T1 to T4, pending the owner's approval)

**T1, cluster behavior toggles** (3 names)

| Property | Where read in 10.0 |
| --- | --- |
| `solr.cloud.overseer.enabled` | `solr/core/src/java/org/apache/solr/cloud/ZkController.java` line 381 |
| `solr.cloud.downgrade.enabled` | `solr/core/src/java/org/apache/solr/cloud/ZkController.java` line 629 |
| `solr.cloud.prs.enabled` | `solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java` line 98 |

**T2, operator security settings** (4 names)

| Property | Where read in 10.0 |
| --- | --- |
| `solr.ui.headers.csp.connect-src.urls` | `solr/core/src/java/org/apache/solr/servlet/LoadAdminUiServlet.java` line 46 |
| `solr.configset.upload.mimetypes.forbidden` | `solr/core/src/java/org/apache/solr/util/FileTypeMagicUtil.java` line 198 |
| `solr.jetty.keystoreParentPath` | `solr/bin/solr` line 223 |
| `solr.keystore.reload.enabled` | `solr/bin/solr` line 216 |

**T3, TLS hardening in jetty-ssl.xml** (9 names)

| Property | Where read in 10.0 |
| --- | --- |
| `solr.jetty.ssl.sts.max.age.secs` | `solr/server/etc/jetty-ssl.xml` line 47 |
| `solr.jetty.ssl.sts.include.subdomains.enabled` | `solr/server/etc/jetty-ssl.xml` line 48 |
| `solr.jetty.ssl.renegotiationAllowed` | `solr/server/etc/jetty-ssl.xml` line 30 |
| `solr.jetty.ssl.renegotiationLimit` | `solr/server/etc/jetty-ssl.xml` line 31 |
| `solr.jetty.ssl.sniRequired` | `solr/server/etc/jetty-ssl.xml` line 32 |
| `solr.jetty.ssl.sni.required.enabled` | `solr/server/etc/jetty-ssl.xml` line 45 |
| `solr.jetty.ssl.sni.host.check.enabled` | `solr/bin/solr` line 277 |
| `solr.jetty.ssl.useCipherSuitesOrder` | `solr/server/etc/jetty-ssl.xml` line 27 |
| `solr.jetty.ssl.provider` | `solr/server/etc/jetty-ssl.xml` line 10 |

**T4, runtime limits and operations** (7 names)

| Property | Where read in 10.0 |
| --- | --- |
| `solr.logs.requestlog.enabled` | `solr/core/src/java/org/apache/solr/util/StartupLoggingUtils.java` line 210 |
| `solr.admin.async.max` | `solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java` line 369 |
| `solr.admin.async.timeout.minutes` | `solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java` line 395 |
| `solr.admin.async.timeout.completed.minutes` | `solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java` line 397 |
| `solr.update.docLockTimeoutMs` | `solr/core/src/java/org/apache/solr/update/UpdateLog.java` line 397 |
| `solr.pid.dir` | `solr/bin/solr` line 500 |
| `solr.schemaUpdateTimeoutSeconds` | `solr/core/src/java/org/apache/solr/schema/SchemaManager.java` line 83 |

### Covered by ST5

* `solr.jetty.ssl.need.client.auth.enabled`: the rename note in ST5 covers it.
* `solr.jetty.ssl.want.client.auth.enabled`: the rename note in ST5 covers it.

### Follow-up, not in this PR (30 names)

Socket, buffer, pool and internal tuning knobs. An operator is unlikely to set most of them, and each needs a sentence on what it does. Listed for the follow-up ticket:

* `jetty.gzip.deflaterPool.capacity`
* `jetty.gzip.deflaterPool.compressionLevel`
* `jetty.gzip.deflaterPool.noWrap`
* `jetty.server.stopTimeout`
* `solr.OverseerStateUpdateMaxQueueSize`
* `solr.allowPartialResultsDefault`
* `solr.blockcache.blockshift`
* `solr.blockcache.readbuffer.size.default`
* `solr.exportbuffers.exchange.timeout.sec`
* `solr.install.symDir`
* `solr.jetty.http.acceptedReceiveBufferSize`
* `solr.jetty.http.acceptedSendBufferSize`
* `solr.jetty.http.acceptedTcpNoDelay`
* `solr.jetty.http.reuseAddress`
* `solr.jetty.http.reusePort`
* `solr.jetty.ssl.acceptedReceiveBufferSize`
* `solr.jetty.ssl.acceptedSendBufferSize`
* `solr.jetty.ssl.acceptedTcpNoDelay`
* `solr.jetty.ssl.reuseAddress`
* `solr.jetty.ssl.reusePort`
* `solr.jetty.ssl.sslSessionCacheSize`
* `solr.jetty.ssl.sslSessionTimeout`
* `solr.log.cputime`
* `solr.otelDefaultConfigurator`
* `solr.search.multiThreaded.queueSize`
* `solr.solrj.cache.timeout.sec`
* `solr.solrj.javabin.readMapAsNamedList`
* `solr.traceHostName`
* `solr.url.scheme`
* `solr.use.str.intern`

Total: 23 recommended, 2 covered by ST5, 30 follow-up, 55 in all, plus 1 class name excluded.

Draft: no branch. Nothing here is a doc edit. ST7 adds the owner's approved subset; the owner decides the set.

